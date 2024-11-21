package com.demo.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.auth0.jwt.exceptions.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    //Excpetions

    public static class UserNotFoundException extends IllegalArgumentException{

        public UserNotFoundException(String username){
            super("user with username: " + username + " not found!");
        }

        public UserNotFoundException(Long id){
            super("user with userId: " + id + " not found!");
        }
    }

    public static class InvalidCredentialException extends IllegalArgumentException{
        public InvalidCredentialException(){
            super("Invalid username or password combination!");
        }
    }

    public static class RoleNotFoundException extends IllegalArgumentException{
        public RoleNotFoundException(){
            super("Role Not Found!");
        }
    }

    public static class EmailNotFound extends IllegalArgumentException{
        public EmailNotFound(){ super("Email not found!"); }
    }

    @ExceptionHandler({
            UserNotFoundException.class,
            InvalidCredentialException.class,
            RoleNotFoundException.class,
            IllegalArgumentException.class,
            TokenExpiredException.class,
            SignatureVerificationException.class,
            AlgorithmMismatchException.class,
            JWTVerificationException.class,
            AccessDeniedException.class,
            RuntimeException.class
    })
    ResponseEntity<ErrorResponseDTO> handleUserExceptions(Exception ex){
        String message;
        HttpStatus status;

        if(ex instanceof UserNotFoundException){
            message = ex.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else if(ex instanceof InvalidCredentialException) {
            message = ex.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }else if(ex instanceof AccessDeniedException) {
            message = ex.getMessage();
            status = HttpStatus.UNAUTHORIZED;
        }else if(ex instanceof RoleNotFoundException) {
            message = ex.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else if(ex instanceof EmailNotFound) {
            message = ex.getMessage();
            status = HttpStatus.NOT_FOUND;
        } else if(ex instanceof TokenExpiredException) {
            message = "Token has expired. Please log in again!";
            status = HttpStatus.UNAUTHORIZED;
        } else if(ex instanceof SignatureVerificationException) {
            message = "Invalid Token Signature. Please log in again!";
            status = HttpStatus.UNAUTHORIZED;
        } else if(ex instanceof AlgorithmMismatchException) {
            message = "Algorithm mismatch. Please log in again!";
            status = HttpStatus.UNAUTHORIZED;
        } else if(ex instanceof JWTVerificationException) {
            message = "Invalid Token. Please log in again!";
            status = HttpStatus.UNAUTHORIZED;
        } else if(ex instanceof IllegalArgumentException) {
            message = ex.getMessage();
            status = HttpStatus.FORBIDDEN;
        } else{
            message = "Something went wrong!";
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ErrorResponseDTO response = ErrorResponseDTO.builder().message(message).build();
        return ResponseEntity.status(status).body(response);
    }
}
