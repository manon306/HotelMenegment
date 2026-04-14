package com.example.REVIEWSERVICE.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiError> handleNotFound(
                        ResourceNotFoundException ex,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                404,
                                "NOT_FOUND",
                                ex.getMessage(),
                                request.getRequestURI());

                return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }

        @ExceptionHandler(BadRequestException.class)
        public ResponseEntity<ApiError> handleBadRequest(
                        BadRequestException ex,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                400,
                                "BAD_REQUEST",
                                ex.getMessage(),
                                request.getRequestURI());

                return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ApiError> handleGeneral(
                        Exception ex,
                        HttpServletRequest request) {

                ApiError error = new ApiError(
                                500,
                                "INTERNAL_SERVER_ERROR",
                                ex.getMessage(),
                                request.getRequestURI());

                return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
        }
}