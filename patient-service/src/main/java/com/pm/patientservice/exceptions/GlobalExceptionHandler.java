package com.pm.patientservice.exceptions;

import com.pm.patientservice.dto.ApiResponse;
import com.pm.patientservice.dto.ValidationError;
import com.pm.patientservice.utils.ApiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Object>> handleDataIntegrityViolationException(EmailAlreadyExistsException ex) {
        String message = "A patient with this email already exists.";
        // Optionally, you can inspect the cause or message to make this more generic or specific

        log.warn("A patient with this email already exists: {}", ex.getMessage(), ex);

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                ApiStatus.ERROR,
                message,
                null,
                null // or you can provide more error detail here if you want
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT); // 409 CONFLICT is appropriate for duplicate data
    }

    @ExceptionHandler(PatientNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handlePatientNotFoundException(PatientNotFoundException ex) {
        // Optionally, you can inspect the cause or message to make this more generic or specific

        log.warn("A patient with this ID does not exist. {}", ex.getMessage(), ex);

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                ApiStatus.ERROR,
                ex.getMessage(),
                null,
                null // or you can provide more error detail here if you want
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT); // 409 CONFLICT is appropriate for duplicate data
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException ex) {

        List<ValidationError> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new ValidationError(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue()
                ))
                .toList();

        log.warn("Validation failed: {}", ex.getMessage(), ex);

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                ApiStatus.ERROR,
                "Validation failed",
                null,      // data is null on error
                errors     // errors field is filled
        );

        return ResponseEntity.badRequest().body(apiResponse);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        String message = "Request body is missing or unreadable.";

        log.warn("Request body is missing or unreadable: {}", ex.getMessage(), ex);

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                ApiStatus.ERROR,
                message,
                null,
                null
        );

        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    //If exceptions are not known
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleAllExceptions(Exception ex) {
        // Fallback handler for unexpected errors


        log.warn("Unexpected exception: {}", ex.getMessage(), ex);

        ApiResponse<Object> apiResponse = new ApiResponse<>(
                ApiStatus.ERROR,
                "An unexpected error occurred.",
                null,
                null
        );
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
