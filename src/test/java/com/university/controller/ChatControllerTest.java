package com.university.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.university.dto.ChatResponse;
import com.university.dto.ChatUpdateRequest;
import com.university.dto.ChatUpdateResponse;
import com.university.model.Role;
import com.university.service.JwtServiceImpl;
import com.university.service.api.ChatService;
import com.university.util.ChatResponseImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
class ChatControllerTest {
    @Autowired
    private ChatController controller;
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ChatService chatService;
    @Autowired
    private JwtServiceImpl jwtService;
    @MockBean
    private UserDetailsService userDetailsService;
    ObjectMapper objectMapper = new ObjectMapper();
    @Test
    void getChats() throws Exception {
        UserDetails dummyUser = new User("foo", "foo", List.of(new SimpleGrantedAuthority("ROLE_" + Role.USER)));
        String jwtToken = jwtService.generateToken(dummyUser);
        List<ChatResponse> chatResponses = List.of(
                new ChatResponseImpl("1", "chat1", "filename1"),
                new ChatResponseImpl("2", "chat2", "filename2"),
                new ChatResponseImpl("3", "chat3", "filename3")
        );
        Mockito.when(chatService.getChats()).thenReturn(chatResponses);
        Mockito.when(userDetailsService.loadUserByUsername(eq("foo"))).thenReturn(dummyUser);
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/chats")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().is(200))
                .andExpect(jsonPath("$[0].id").value("1"))
                .andExpect(jsonPath("$[0].title").value("chat1"))
                .andExpect(jsonPath("$[0].filename").value("filename1"))
                .andExpect(jsonPath("$[1].id").value("2"))
                .andExpect(jsonPath("$[1].title").value("chat2"))
                .andExpect(jsonPath("$[1].filename").value("filename2"))
                .andExpect(jsonPath("$[2].id").value("3"))
                .andExpect(jsonPath("$[2].title").value("chat3"))
                .andExpect(jsonPath("$[2].filename").value("filename3"));
    }

    @Test
    void deleteChat() throws Exception {
        UserDetails dummyUser = new User("foo", "foo", List.of(new SimpleGrantedAuthority("ROLE_" + Role.USER)));
        String jwtToken = jwtService.generateToken(dummyUser);

        Mockito.when(userDetailsService.loadUserByUsername(eq("foo"))).thenReturn(dummyUser);
        Mockito.doNothing().when(chatService).deleteChat(1);
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/chats/{chatId}", "1")
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().isOk());

        verify(chatService, times(1)).deleteChat(1);
    }

    @Test
    void updateChat() throws Exception {
        ChatUpdateRequest chatUpdateRequest = new ChatUpdateRequest(1, "new title");
        ChatUpdateResponse chatUpdateResponse = new ChatUpdateResponse(1, "new title", "filename");
        UserDetails dummyUser = new User("foo", "foo", List.of(new SimpleGrantedAuthority("ROLE_" + Role.USER)));
        String jwtToken = jwtService.generateToken(dummyUser);

        Mockito.when(userDetailsService.loadUserByUsername(eq("foo"))).thenReturn(dummyUser);
        Mockito.when(chatService.updateChat(chatUpdateRequest)).thenReturn(chatUpdateResponse);
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/chats")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(chatUpdateRequest))
                        .header("Authorization", "Bearer " + jwtToken)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("1"))
                .andExpect(jsonPath("$.filename").value("filename"))
                .andExpect(jsonPath("$.title").value("new title"));
    }

    @Test
    public void contextLoads() throws Exception {
        assertThat(controller).isNotNull();
    }
}