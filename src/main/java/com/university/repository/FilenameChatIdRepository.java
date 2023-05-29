package com.university.repository;

import com.university.model.FilenameChatId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FilenameChatIdRepository extends JpaRepository<FilenameChatId, Integer> {
    FilenameChatId findByChatId(Integer id);
}
