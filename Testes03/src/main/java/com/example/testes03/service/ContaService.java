package com.example.testes03.service;

import com.example.testes03.dao.ContaDAO;
import com.example.testes03.dto.TransfDTO;
import com.example.testes03.exception.ContaInexistenteException;
import com.example.testes03.exception.InvalidNumberException;
import com.example.testes03.model.Conta;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContaService {
    @Autowired
    private ContaDAO dao;

    public TransfDTO transferir(int numeroContaOrigem, int numeroContaDestino, double valor) throws ContaInexistenteException, InvalidNumberException{
        Conta contaOrigem = dao.getConta(numeroContaOrigem);
        Conta contaDestino = dao.getConta(numeroContaDestino);

        if(contaOrigem.sacar(valor)) contaDestino.depositar(valor);

        dao.updateConta(contaOrigem);
        dao.updateConta(contaDestino);

        return new TransfDTO(contaOrigem, contaDestino);
    }
}
