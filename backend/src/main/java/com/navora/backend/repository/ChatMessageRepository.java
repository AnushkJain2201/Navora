package com.navora.backend.repository;

import com.navora.backend.entity.ChatMessage;
import com.navora.backend.entity.ChatSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, UUID> {
    List<ChatMessage> findBySessionOrderByCreatedAtAsc(ChatSession session);
}
