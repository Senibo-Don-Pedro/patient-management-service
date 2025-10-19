package com.pm.patientservice.exception;

import com.pm.patientservice.dto.ApiErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex) {

        List<String> errors = ex.getBindingResult()
                                .getFieldErrors()
                                .stream()
                                .map(FieldError::getDefaultMessage)
                                .toList();

        ApiErrorResponse errorResponse = new ApiErrorResponse(
                false,
                "Validation Failed",
                errors
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(AlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleAlreadyExistsException(AlreadyExistsException ex) {

        log.warn("Email already exists: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ApiErrorResponse(
                        false,
                        ex.getMessage(),
                        null
                )
        );

    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleNotFoundException(NotFoundException ex) {

        log.warn("Patient not found: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorResponse(false, ex.getMessage(), null)
        );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex) {
        Throwable cause = ex.getMostSpecificCause();

        // General case for bad JSON (malformed or invalid types)
        if (cause != null) {
            String errorMessage = cause.getMessage();

            return ResponseEntity.badRequest().body(
                    new ApiErrorResponse(false, "Malformed JSON", List.of(errorMessage))
            );
        }

        // Generic fallback for unknown cases
        return ResponseEntity.badRequest().body(
                new ApiErrorResponse(false,
                                     "Malformed JSON",
                                     List.of("Request body could not be read. Please check the syntax."))
        );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleAny(Exception ex) {
        log.error("Unexpected unhandled error: {} {}", ex.getMessage(), ex.toString());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .body(new ApiErrorResponse(false,
                                                        ex.getMessage(),
//                                                        "An unexpected error occurred.",
                                                        null));
    }
}
