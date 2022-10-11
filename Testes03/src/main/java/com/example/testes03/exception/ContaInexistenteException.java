package com.example.testes03.exception;

public class ContaInexistenteException extends RuntimeException{
    public ContaInexistenteException(String message) {
        super(message);
    }
}
