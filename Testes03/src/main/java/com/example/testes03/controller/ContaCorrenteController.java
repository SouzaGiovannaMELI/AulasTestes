package com.example.testes03.controller;

import com.example.testes03.dao.ContaDAO;
import com.example.testes03.dto.ContaDTO;
import com.example.testes03.exception.ContaInexistenteException;
import com.example.testes03.exception.InsuficienteSaldoException;
import com.example.testes03.exception.InvalidNumberException;
import com.example.testes03.model.ContaCorrente;
import com.example.testes03.service.ContaCorrenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conta_corrente")
public class ContaCorrenteController {
    @Autowired
    private ContaCorrenteService service;

    @GetMapping("/{numero}")
    public ResponseEntity<ContaCorrente> getConta(@PathVariable int numero) throws ContaInexistenteException{
        return new ResponseEntity<>(service.getConta(numero), HttpStatus.OK);
    }

    @PostMapping("/{cliente}")
    public ResponseEntity<ContaCorrente> novaContaCorrente(@PathVariable String cliente){
        return new ResponseEntity<>(service.novaContaCorrente(cliente), HttpStatus.CREATED);
    }

    @PatchMapping("/dep/{numero}/{valor}")
    public ResponseEntity<ContaCorrente> depositar(@PathVariable int numero, @PathVariable double valor) throws ContaInexistenteException, InvalidNumberException{
        ContaCorrente conta = service.getConta(numero);
        service.depositar(numero, valor);

        return new ResponseEntity<>(conta, HttpStatus.OK);
    }

    @PatchMapping("/sacar/{numero}/{valor}")
    public ResponseEntity<ContaCorrente> sacar(@PathVariable int numero, @PathVariable double valor) throws ContaInexistenteException, InvalidNumberException, InsuficienteSaldoException {
        ContaCorrente conta = service.getConta(numero);
        if(service.sacar(numero, valor)) return new ResponseEntity<>(conta, HttpStatus.OK);

        throw new InsuficienteSaldoException("Saldo insuficiente");
    }

    @PostMapping("/new")
    public ResponseEntity<ContaCorrente> novaContaCorrenteBody(@RequestBody ContaDTO contaDTO){
        return new ResponseEntity<>(service.novaContaCorrente(contaDTO.getCliente()), HttpStatus.CREATED);
    }
}
