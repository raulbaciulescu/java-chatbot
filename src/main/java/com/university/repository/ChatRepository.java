package com.university.repository;

import com.university.dto.ChatResponse;
import com.university.model.Chat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Integer> {
    List<ChatResponse> findAllBy();

    List<ChatResponse> findAllByUserId(Integer userId);

    void deleteById(Integer id);
}
