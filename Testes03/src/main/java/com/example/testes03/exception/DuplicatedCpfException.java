package com.example.testes03.exception;

public class DuplicatedCpfException extends RuntimeException{
    public DuplicatedCpfException(String message) {
        super(message);
    }
}
