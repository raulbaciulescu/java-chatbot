package com.university;

import com.university.model.Chat;
import com.university.model.Message;
import com.university.model.MessageType;
import com.university.repository.ChatRepository;
import com.university.repository.MessageRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JavaChatbotApplication {
	public static void main(String[] args) {
		SpringApplication.run(JavaChatbotApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(MessageRepository messageRepository, ChatRepository chatRepository) {
		return args -> {
//			Chat chat = Chat.builder()
//					.title("first chat")
//					.build();
//			chatRepository.save(chat);
//			chat = chatRepository.findAll().get(0);
//
//			Message message = Message.builder()
//					.type(MessageType.AI)
//					.chat(chat)
//					.text("message!!!")
//					.build();
//
//			message = messageRepository.save(message);
//
//			System.out.println(chat);
//			System.out.println(message);
		};
	}
}
