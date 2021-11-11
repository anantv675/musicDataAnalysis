package com.musicdataanalysis.dataanalysis.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

// in this class, we modify the behaviour of the default ResourceNotFoundException
@ControllerAdvice
public class ControllerExceptionHandler {

    //Instead of ResourceNotFoundException, we want to send NOT_FOUND notification in response message.
    // After this, we modify our controller methods by using try-catch blocks
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<CustomErrorMessage> resourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        CustomErrorMessage message = new CustomErrorMessage(
                HttpStatus.NOT_FOUND.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<CustomErrorMessage>(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorMessage> globalExceptionHandler(Exception ex, WebRequest request) {
        CustomErrorMessage message = new CustomErrorMessage(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Date(),
                ex.getMessage(),
                request.getDescription(false));

        return new ResponseEntity<CustomErrorMessage>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

