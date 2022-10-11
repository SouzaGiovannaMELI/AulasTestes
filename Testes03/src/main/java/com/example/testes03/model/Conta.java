package com.example.testes03.model;

import com.example.testes03.exception.InvalidNumberException;
import lombok.Getter;

@Getter
public abstract class Conta implements Comparable<Conta>{
    private int numero;
    private double saldo;
    private String cliente;

    public Conta(int numero, String cliente) {
        this.numero = numero;
        this.cliente = cliente;
    }

    public void depositar(double valor) throws InvalidNumberException {
        validarValor(valor);
        saldo += valor;
    }

    public boolean sacar(double valor) throws InvalidNumberException {
        validarValor(valor);
        saldo -= valor;
        return true;
    }

    public void validarValor(double valor){
        if(valor <= 0)
            throw new InvalidNumberException("O valor informado é inváido");
    }

    @Override
    public String toString() {
        return "numero=" + numero +
                ", saldo=" + saldo +
                ", cliente='" + cliente;
    }

    @Override
    public int compareTo(Conta outraConta) {
        return Double.compare(this.saldo, outraConta.getSaldo());
    }
}
