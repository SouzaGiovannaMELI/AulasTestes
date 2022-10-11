package com.example.testes03.model;

import com.example.testes03.exception.InvalidNumberException;

public class ContaCorrente extends Conta{
    public ContaCorrente(int numero, String cliente) {
        super(numero, cliente);
    }

    @Override
    public boolean sacar(double valor) throws InvalidNumberException {
        if(saldoInsuficiente(valor)) return false;
        return super.sacar(valor);
    }

    private boolean saldoInsuficiente(double valor){
        return getSaldo() < valor;
    }

    @Override
    public String toString() {
        return "ContaCorrente: " +super.toString();
    }
}
