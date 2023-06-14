package com.university.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.dto.ChatResponse;
import com.university.dto.MessagePdfRequest;
import com.university.dto.MessageRequest;
import com.university.dto.MessageResponse;
import com.university.model.MessageType;
import com.university.model.Role;
import com.university.service.JwtServiceImpl;
import com.university.service.api.ChatService;
import com.university.service.api.MessagePdfService;
import com.university.service.api.MessageService;
import com.university.util.ChatResponseImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockPart;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class MessageControllerTest {
    @Autowired
    private MessageController controller;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private MessageService messageService;
    @Autowired
    private JwtServiceImpl jwtService;
    @MockBean
    private UserDetailsService userDetailsService;
    @MockBean
    private MessagePdfService messagePdfService;
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void saveMessage() throws Exception {
        MessageRequest messageRequest = new MessageRequest("message", MessageType.USER, 1);
        MessageResponse messageResponse = new MessageResponse("response", "", MessageType.AI, 1, null);
        UserDetails dummyUser = new User("foo", "foo", List.of(new SimpleGrantedAuthority("ROLE_" + Role.USER)));
        String jwtToken = jwtService.generateToken(dummyUser);

        Mockito.when(messageService.save(messageRequest)).thenReturn(messageResponse);
        Mockito.when(userDetailsService.loadUserByUsername(eq("foo"))).thenReturn(dummyUser);
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(messageRequest))
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.title").value(""))
                .andExpect(jsonPath("$.text").value("response"))
                .andExpect(jsonPath("$.type").value(MessageType.AI.toString()));
    }

    @Test
    void getMessages() throws Exception {
        UserDetails dummyUser = new User("foo", "foo", List.of(new SimpleGrantedAuthority("ROLE_" + Role.USER)));
        String jwtToken = jwtService.generateToken(dummyUser);
        List<MessageResponse> messageResponses = List.of(
                new MessageResponse("m1", "t1", MessageType.USER, 1, null),
                new MessageResponse("m2", "t1", MessageType.AI, 1, null),
                new MessageResponse("m3", "t1", MessageType.USER, 1, null),
                new MessageResponse("m4", "t1", MessageType.AI, 1, null)
        );
        Mockito.when(messageService.getMessagesByChat(1, 0)).thenReturn(messageResponses);
        Mockito.when(userDetailsService.loadUserByUsername(eq("foo"))).thenReturn(dummyUser);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/messages/1/0")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].text").value("m1"))
                .andExpect(jsonPath("$[0].title").value("t1"))
                .andExpect(jsonPath("$[1].text").value("m2"))
                .andExpect(jsonPath("$[1].title").value("t1"))
                .andExpect(jsonPath("$[2].text").value("m3"))
                .andExpect(jsonPath("$[2].title").value("t1"));
    }

    @Test
    void saveMessageWithPdf() throws Exception {
        MessageResponse messageResponse = new MessageResponse("response", "", MessageType.AI, 1, null);
        UserDetails dummyUser = new User("foo", "foo", List.of(new SimpleGrantedAuthority("ROLE_" + Role.USER)));
        File file = new File("src/test/resources/Licenta.pdf");
        MockMultipartFile multipartFile = new MockMultipartFile(
                "file", file.getPath(),
                MediaType.MULTIPART_FORM_DATA_VALUE,
                new FileInputStream(file)
        );
        MessagePdfRequest messagePdfRequest = new MessagePdfRequest(multipartFile, "message");
        String jwtToken = jwtService.generateToken(dummyUser);
        Mockito.when(messageService.saveMessageWithPdf(messagePdfRequest)).thenReturn(messageResponse);
        Mockito.when(userDetailsService.loadUserByUsername(eq("foo"))).thenReturn(dummyUser);


        MockMultipartFile caseDetailsJson = new MockMultipartFile(
                "text",
                "",
                "application/json","message".getBytes());

        mockMvc.perform(MockMvcRequestBuilders
                        .multipart("/messages/pdf")
                        .file(multipartFile)
                        .file(caseDetailsJson)
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.text").value("response"));
    }

    @Test
    void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
}