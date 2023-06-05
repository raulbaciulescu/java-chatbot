package com.university.controller;


import com.university.dto.SpeechRecognitionResponse;
import com.university.service.PythonServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/speech-recognition")
@RequiredArgsConstructor
@CrossOrigin
public class SpeechRecognitionController {
    private final PythonServiceImpl service;

    @PostMapping
    public ResponseEntity<SpeechRecognitionResponse> transcribe(@RequestPart MultipartFile file) {
        return ResponseEntity.ok(service.transcribe(file));
    }
}
