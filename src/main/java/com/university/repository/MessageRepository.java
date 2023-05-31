package com.university.repository;

import com.university.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer>, PagingAndSortingRepository<Message, Integer> {
    List<Message> findByChatId(Integer chatId);

    Page<Message> findAllByChatIdOrderByIdDesc(Integer chatId, Pageable pageable);

    void deleteByChatId(Integer chatId);
}
