package com.hr_management.hr_management.exception;


import com.hr_management.hr_management.model.dto.ApiResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler   {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseDto> resourceNotFoundException(ResourceNotFoundException ex, HttpServletRequest request) {

        ApiResponseDto err = ApiResponseDto.builder()
                .message(ex.getMessage())
                .status(HttpStatus.NOT_FOUND)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return new ResponseEntity<>(err, HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponseDto> globalExceptionHandler(Exception ex, HttpServletRequest request) {
        ApiResponseDto err = ApiResponseDto.builder()
                .message("An internal server error occurred: " + ex.getMessage())
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(err, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
