package com.hr_management.hr_management.utils;


import com.hr_management.hr_management.model.dto.ApiResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class BuildResponse {
    BuildResponse(){}

    public static ResponseEntity<ApiResponseDto> success(Object data, String message, String path){

        ApiResponseDto response = ApiResponseDto.builder()
                .data(data)
                .message(message)
                .status(HttpStatus.OK)
                .timestamp(LocalDateTime.now())
                .path(path)
                .build();
        return  new ResponseEntity<>(response, HttpStatus.OK);
    }

}
