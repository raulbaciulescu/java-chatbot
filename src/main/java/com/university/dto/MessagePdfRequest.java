package com.university.dto;

import org.springframework.web.multipart.MultipartFile;

public record MessagePdfRequest(MultipartFile file, String text) {
}
