package com.university.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public record RegisterRequest(String firstName, String lastName, String email, String password) {
}
