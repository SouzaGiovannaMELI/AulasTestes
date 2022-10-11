package com.example.testes03.exception;

public class InsuficienteSaldoException extends RuntimeException{
    public InsuficienteSaldoException(String message) {
        super(message);
    }
}
