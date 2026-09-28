package com.navora.backend.controller;

import com.navora.backend.dto.AskRequestDto;
import com.navora.backend.dto.AskResponseDto;
import com.navora.backend.dto.ChatMessageDto;
import com.navora.backend.dto.ChatSessionSummaryDto;
import com.navora.backend.service.ChatService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "http://localhost:5173")
@SecurityRequirement(name = "bearerAuth")
public class ChatController {
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/ask")
    public ResponseEntity<AskResponseDto> ask(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AskRequestDto requestDto
            ) {
        return ResponseEntity.ok(chatService.ask(userDetails.getUsername(), requestDto));
    }

    @GetMapping("/sessions")
    public ResponseEntity<List<ChatSessionSummaryDto>> getSessions(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(chatService.getSessionsForUser(userDetails.getUsername()));
    }

    @GetMapping("/sessions/{sessionId}/messages")
    public ResponseEntity<List<ChatMessageDto>> getMessages(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable UUID sessionId
            ) {
        return ResponseEntity.ok(chatService.getMessagesForSession(userDetails.getUsername(), sessionId));
    }
}
