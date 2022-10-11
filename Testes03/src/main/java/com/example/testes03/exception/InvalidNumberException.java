package com.example.testes03.exception;

public class InvalidNumberException extends RuntimeException{
    public InvalidNumberException(String message) {
        super(message);
    }
}
