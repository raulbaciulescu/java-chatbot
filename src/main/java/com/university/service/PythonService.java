package com.university.service;

import com.university.dto.MessagePdfRequest;
import com.university.dto.MessageRequestToPython;
import com.university.dto.MessageRequestToPythonWithFilename;
import com.university.dto.MessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
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
                url + "/messages",
                HttpMethod.POST,
                requestBody, MessageResponse.class);

        return response.getBody();
    }

    public MessageResponse createPdfMessageWithFile(MessagePdfRequest messageRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", messageRequest.file().getResource());
        body.add("message", messageRequest.text());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<MessageResponse> response = restTemplate.postForEntity(
                url + "/pdf-messages",
                requestEntity, MessageResponse.class);

        return response.getBody();
    }

    public MessageResponse createPdfMessageWithFilename(MessageRequestToPythonWithFilename messageRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MessageRequestToPythonWithFilename> requestBody = new HttpEntity<>(messageRequest, headers);

        ResponseEntity<MessageResponse> response = restTemplate.exchange(
                url + "/pdf-messages/without-pdf",
                HttpMethod.POST,
                requestBody, MessageResponse.class);

        return response.getBody();
    }
}
