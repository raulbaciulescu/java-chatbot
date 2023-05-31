package com.university.service;

import com.university.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class PythonService {
    @Value("${python-chat-bot.url}")
    private String url;
    private final RestTemplate restTemplate;

    public <T> MessageResponse sendMessageToPython(T request, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<T> requestBody = new HttpEntity<>(request, headers);

        ResponseEntity<MessageResponse> response = restTemplate.exchange(
                url,
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

    public SpeechRecognitionResponse transcribe(MultipartFile file) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", file.getResource());

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<SpeechRecognitionResponse> response = restTemplate.postForEntity(
                url + "/transcribe",
                requestEntity, SpeechRecognitionResponse.class);

        return response.getBody();
    }
}
