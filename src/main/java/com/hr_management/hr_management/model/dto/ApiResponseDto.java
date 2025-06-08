package com.hr_management.hr_management.model.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class ApiResponseDto {
    private HttpStatus status;
    private String message;
    private String path;
    private Object data;
    private LocalDateTime timestamp;
}
