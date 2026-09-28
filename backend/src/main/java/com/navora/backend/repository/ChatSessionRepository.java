package com.navora.backend.repository;

import com.navora.backend.entity.ChatSession;
import com.navora.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ChatSessionRepository extends JpaRepository<ChatSession, UUID> {
    List<ChatSession> findByUserOrderByCreatedAtDesc(User user);
}
