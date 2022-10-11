package com.example.testes03.model;

import com.example.testes03.exception.InvalidNumberException;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ContaPoupanca extends Conta{
    private static double taxa = 0.1;

    public ContaPoupanca(int numero, String cliente) {
        super(numero, cliente);
    }

    public static void setTaxa(double taxa) {
        ContaPoupanca.taxa = taxa;
    }

    @Override
    public boolean sacar(double valor) throws InvalidNumberException {
        if(saldoInsuficiente(valor)) return false;
        return super.sacar(valor);
    }

    private boolean saldoInsuficiente(double valor){
        return getSaldo() < valor + taxa;
    }

    @Override
    public String toString() {
        return "ContaPoupanca: " +super.toString();
    }
}
