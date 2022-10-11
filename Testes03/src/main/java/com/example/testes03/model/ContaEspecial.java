package com.example.testes03.model;

import com.example.testes03.exception.InvalidNumberException;

public class ContaEspecial extends Conta{
    private double limite;

    public ContaEspecial(int numero, String cliente, double limite) {
        super(numero, cliente);
        this.limite = limite;
    }

    @Override
    public boolean sacar(double valor) throws InvalidNumberException {
        if(saldoInsuficiente(valor)) return false;
        return super.sacar(valor);
    }

    private boolean saldoInsuficiente(double valor){
        return getSaldo() + limite < valor;
    }

    @Override
    public String toString() {
        return "ContaEspecial: " +super.toString();
    }
}
