package com.navora.backend.service;

import com.navora.backend.dto.*;
import com.navora.backend.entity.ChatMessage;
import com.navora.backend.entity.ChatSession;
import com.navora.backend.entity.User;
import com.navora.backend.repository.ChatMessageRepository;
import com.navora.backend.repository.ChatSessionRepository;
import com.navora.backend.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class ChatService {
    private static final int MAX_HISTORY_MESSAGES = 6;
    private static final int TITLE_MAX_LENGTH = 60;

    private final ChatSessionRepository chatSessionRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final RagAiServiceClient ragAiServiceClient;

    public ChatService(ChatSessionRepository chatSessionRepository, ChatMessageRepository chatMessageRepository, UserRepository userRepository, RagAiServiceClient ragAiServiceClient) {
        this.chatSessionRepository = chatSessionRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userRepository = userRepository;
        this.ragAiServiceClient = ragAiServiceClient;
    }

    @Transactional
    public AskResponseDto ask(String userEmail, AskRequestDto requestDto) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found."));

        ChatSession session = resolveSession(user, requestDto.sessionId());
        boolean isFirstMessage = session.getMessages().isEmpty();

        chatMessageRepository.save(new ChatMessage(session, "user", requestDto.question()));

        List<AiChatMessageDto> history = buildHistoryForAi(session);

        AiAskResponseDto aiAskResponseDto = ragAiServiceClient.ask(new AiAskRequestDto(requestDto.question(), history));

        chatMessageRepository.save(new ChatMessage(session, "assistant", aiAskResponseDto.answer()));

        if(isFirstMessage) {
            session.setTitle(generateTitle(requestDto.question()));
            chatSessionRepository.save(session);
        }

        return new AskResponseDto(session.getId(), aiAskResponseDto.answer(), aiAskResponseDto.sources());
    }

    private ChatSession resolveSession(User user, UUID sessionId) {
        if(sessionId == null) {
            ChatSession newSession = new ChatSession(user);
            return chatSessionRepository.save(newSession);
        }

        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Chat session not found."));

        if(!session.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Chat session does not belong to this user.");
        }

        return session;
    }

    private List<AiChatMessageDto> buildHistoryForAi(ChatSession session) {
        List<ChatMessage> messages = chatMessageRepository.findBySessionOrderByCreatedAtAsc(session);

        int fromIndex = Math.max(0, messages.size() - MAX_HISTORY_MESSAGES);

        return messages.subList(fromIndex, messages.size()).stream()
                .map(m -> new AiChatMessageDto(m.getRole(), m.getContent()))
                .toList();
    }

    private String generateTitle(String firstQuestion) {
        String trimmed = firstQuestion.trim();
        if (trimmed.length() <= TITLE_MAX_LENGTH) {
            return trimmed;
        }
        return trimmed.substring(0, TITLE_MAX_LENGTH).trim() + "...";
    }

    public List<ChatSessionSummaryDto> getSessionsForUser(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found."));

        return chatSessionRepository.findByUserOrderByCreatedAtDesc(user).stream()
                .map(s -> new ChatSessionSummaryDto(s.getId(), s.getTitle(), s.getCreatedAt()))
                .toList();
    }

    public List<ChatMessageDto> getMessagesForSession(String userEmail, UUID sessionId) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Authenticated user not found."));

        ChatSession session = chatSessionRepository.findById(sessionId)
                .orElseThrow(() -> new IllegalArgumentException("Chat session not found."));

        if(!session.getUser().getId().equals(user.getId())) {
            throw  new IllegalArgumentException("Chat session do not belong to this user.");
        }

        return chatMessageRepository.findBySessionOrderByCreatedAtAsc(session).stream()
                .map(m -> new ChatMessageDto(m.getId(), m.getRole(), m.getContent(), m.getCreatedAt()))
                .toList();
    }


}
