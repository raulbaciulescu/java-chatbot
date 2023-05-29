package com.university.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "filename_chatid")
public class FilenameChatId {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String filename;
    @Column(name = "chatid")
    private Integer chatId;

    public FilenameChatId(String filename, Integer chatId) {
        this.filename = filename;
        this.chatId = chatId;
    }
}
