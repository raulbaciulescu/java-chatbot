package com.university.dto;

import java.util.List;
import java.util.Map;

public record MessageRequestToPython(String message, List<Map.Entry<String, String>> messages, Boolean generateTitle) {
}
