package com.example.testes03.dao;

import com.example.testes03.exception.ContaInexistenteException;
import com.example.testes03.model.Conta;
import com.example.testes03.model.ContaCorrente;
import com.example.testes03.model.ContaEspecial;
import com.example.testes03.model.ContaPoupanca;
import com.example.testes03.util.NumberGenerator;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class ContaDAO {
    NumberGenerator numberGenerator;
    private Map<Integer, Conta> contas;

    public ContaDAO(){
        contas = new HashMap<>();
        numberGenerator = NumberGenerator.getInstance();

        //Dados testes
        novaContaCorrente("Cliente 1");
        novaContaEspecial("Cliente 2", 1000);
        novaContaPoupanca("Cliente 3");
    }

    public ContaCorrente novaContaCorrente(String cliente){
        int numeroConta = numberGenerator.getNext();
        ContaCorrente conta = new ContaCorrente(numeroConta, cliente);
        contas.put(numeroConta, conta);
        return conta;
    }

    public ContaEspecial novaContaEspecial(String cliente, double limite){
        int numeroConta = numberGenerator.getNext();
        ContaEspecial conta = new ContaEspecial(numeroConta, cliente, limite);
        contas.put(numeroConta, conta);
        return conta;
    }

    public ContaPoupanca novaContaPoupanca(String cliente){
        int numeroConta = numberGenerator.getNext();
        ContaPoupanca conta = new ContaPoupanca(numeroConta, cliente);
        contas.put(numeroConta, conta);
        return conta;
    }

    public Conta getConta(int numeroConta) throws ContaInexistenteException {
        Conta conta = contas.get(numeroConta);

        if(conta != null) return conta;

        throw new ContaInexistenteException("Conta não cadastrada");
    }

    public ContaCorrente getContaCorrente(int numeroConta) throws ContaInexistenteException{
        Conta conta = getConta(numeroConta);

        if(conta instanceof ContaCorrente) return (ContaCorrente) conta;

        throw new ContaInexistenteException("A conta informada não é uma conta corrente");
    }

    public ContaEspecial getContaEspecial(int numeroConta) throws ContaInexistenteException{
        Conta conta = getConta(numeroConta);

        if(conta instanceof ContaEspecial) return (ContaEspecial) conta;

        throw new ContaInexistenteException("A conta informada não é uma conta especial");
    }

    public ContaPoupanca getContaPoupanca(int numeroConta) throws ContaInexistenteException{
        Conta conta = getConta(numeroConta);

        if(conta instanceof ContaPoupanca) return (ContaPoupanca) conta;

        throw new ContaInexistenteException("A conta informada não é uma conta poupanca");
    }

    public boolean updateConta(Conta conta) throws ContaInexistenteException{
        getConta(conta.getNumero());
        contas.put(conta.getNumero(), conta);
        return true;
    }

    public String obterDadosConta(int numeroConta) throws ContaInexistenteException{
        return getConta(numeroConta).toString();
    }

    public void remover(int numeroConta) throws ContaInexistenteException{
        getConta(contas.remove(numeroConta).getNumero());
    }

    public List<String> listarTodasContas(){
        return contas.values().stream().map(conta -> conta.toString()+ "\n").collect(Collectors.toList());
    }

    public List<String> listarContaCorrentePorNumero(){
        return contas.values().stream()
                .filter(conta -> conta instanceof ContaCorrente)
                .sorted(Comparator.comparingInt(Conta::getNumero))
                .map(conta -> conta.toString())
                .collect(Collectors.toList());
    }

    public List<String> listarContaCorrentePorSaldo(){
        return contas.values().stream()
                .filter(conta -> conta instanceof ContaCorrente)
                .sorted(Comparator.reverseOrder())
                .map(conta -> conta.toString())
                .collect(Collectors.toList());
    }

    public List<String> listarContaEspecialNegativa(){
        return contas.values().stream()
                .filter(conta -> conta instanceof ContaEspecial)
                .filter(conta -> conta.getSaldo() < 0)
                .sorted(Comparator.reverseOrder())
                .map(conta -> conta.toString())
                .collect(Collectors.toList());
    }
}
