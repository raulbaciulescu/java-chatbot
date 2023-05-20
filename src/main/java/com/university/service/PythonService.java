package com.university.service;

import com.university.dto.MessageRequest;
import com.university.dto.MessageRequestToPython;
import com.university.dto.MessageResponse;
import com.university.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class PythonService {
    @Value("${python-chat-bot.url}")
    private String url;
    private final RestTemplate restTemplate;

    public MessageResponse createMessage(MessageRequestToPython messageRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MessageRequestToPython> requestBody = new HttpEntity<>(messageRequest, headers);

        ResponseEntity<MessageResponse> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestBody, MessageResponse.class);

        return response.getBody();
    }
}
