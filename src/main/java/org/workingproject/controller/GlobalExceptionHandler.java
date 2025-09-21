package org.workingproject.controller;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.workingproject.service.exceptions.AlreadyExistException;
import org.workingproject.service.exceptions.NotFoundException;

import java.time.format.DateTimeParseException;


@RestController
public class GlobalExceptionHandler {

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<String> handlerDateTimeParseException(DateTimeParseException  e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public  ResponseEntity<String> handlerNullPointerException(NullPointerException  e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NotFoundException.class)
    public  ResponseEntity<String> handlerNotFoundException(NotFoundException  e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(AlreadyExistException.class)
    public  ResponseEntity<String> handlerAlreadyExistException(AlreadyExistException  e){
        return new ResponseEntity<>(e.getMessage(),HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlerConstraintViolationException(ConstraintViolationException e){
        StringBuilder responseMessage = new StringBuilder();

        e.getConstraintViolations().forEach(
                constraintViolation -> {
                    String currentField = constraintViolation.getPropertyPath().toString();
                    String currentMessage = constraintViolation.getMessage();
                    responseMessage.append("В поле: " + currentField + " : " + currentMessage);
                    responseMessage.append("\n");
                }
        );
        return new ResponseEntity<>(responseMessage.toString(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handlerMethodArgumentNotValidException(MethodArgumentNotValidException e) {

        StringBuilder responseMessage = new StringBuilder();

        e.getBindingResult().getAllErrors().forEach(
                objectError -> {
                    String fieldName = ((FieldError) objectError).getField();
                    String currentMessage = objectError.getDefaultMessage();
                    responseMessage.append(fieldName + " : " + currentMessage);
                    responseMessage.append("\n");

                }
        );

        return new ResponseEntity<>(responseMessage.toString(),HttpStatus.BAD_REQUEST);
    }
}
