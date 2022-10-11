package com.example.testes03.service;

import com.example.testes03.dao.ContaDAO;
import com.example.testes03.exception.ContaInexistenteException;
import com.example.testes03.exception.InvalidNumberException;
import com.example.testes03.model.ContaCorrente;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ContaCorrenteServiceTest {
    @InjectMocks
    private ContaCorrenteService service;

    @Mock
    private ContaDAO dao;

    private ContaCorrente novaConta;

    @BeforeEach
    void setup(){
        final String nomeCliente = "Cliente 1";
        final int numeroConta = 1;
        novaConta = new ContaCorrente(numeroConta, nomeCliente);
    }

    @Test
    @DisplayName("Valida nova Conta")
    void novaContaCorrente_retorneNovaContaCorrente_quandoSucesso(){
        Mockito.when(dao.novaContaCorrente(ArgumentMatchers.anyString())).thenReturn(novaConta);

        ContaCorrente conta = service.novaContaCorrente(novaConta.getCliente());

        assertThat(conta).isNotNull();
        assertThat(conta.getNumero()).isPositive();
        assertThat(conta.getCliente()).isEqualTo(novaConta.getCliente());
    }

    @Test
    void getConta_retornaContaCorrente_quandoContaExiste() throws ContaInexistenteException{
        Mockito.when(dao.getContaCorrente(ArgumentMatchers.anyInt())).thenReturn(novaConta);

        ContaCorrente contaCorrenteFound = service.getConta(novaConta.getNumero());

        assertThat(contaCorrenteFound).isNotNull();
        assertThat(contaCorrenteFound.getNumero()).isEqualTo(novaConta.getNumero());
        assertThat(contaCorrenteFound.getSaldo()).isZero();
    }

    @Test
    void getConta_lancaContaInexistenteException_quandoContaNaoExiste() throws ContaInexistenteException{
        int numeroContaInexistente = 999;

        BDDMockito.given(dao.getContaCorrente(ArgumentMatchers.anyInt())).willThrow(new ContaInexistenteException("Conta não existe"));

        assertThrows(ContaInexistenteException.class, () -> {
            service.getConta(numeroContaInexistente);
        });
    }

    @Test
    void sacar_returnTrue_quandoContaExisteEValorValidoESaldoSuficiente() throws InvalidNumberException, ContaInexistenteException{
        final String nomeCliente = "Cliente 1";
        final int numeroConta = 1;
        final double valorOperacao = 100;

        ContaCorrente novaConta = new ContaCorrente(numeroConta, nomeCliente);
        novaConta.depositar(valorOperacao);

        Mockito.when(dao.getContaCorrente(ArgumentMatchers.anyInt())).thenReturn(novaConta);
        Mockito.when(dao.updateConta(ArgumentMatchers.any())).thenReturn(true);

        boolean sucesso = service.sacar(numeroConta, valorOperacao);

        assertThat(sucesso).isTrue();
        assertThat(novaConta.getSaldo()).isZero();
    }

    @Test
    void sacar_lancaContaInexistenteException_quandoContaNaoExiste() throws ContaInexistenteException{
        int numeroContaInexistente = 999;
        double valorOperacao = 100;

        BDDMockito.given(dao.getContaCorrente(ArgumentMatchers.anyInt())).willThrow(new ContaInexistenteException("Conta não existe"));

        assertThrows(ContaInexistenteException.class, () -> {
            service.sacar(numeroContaInexistente, valorOperacao);
        });

        verify(dao, never()).updateConta(ArgumentMatchers.any());
    }

    @Test
    void sacar_lancarInvalidNumberException_quandoValorOperacaoInvalida() throws ContaInexistenteException{
        final String nomeCliente = "Cliente 1";
        final int numeroConta = 1;
        final double valorOperacao = -100;

        ContaCorrente novaConta = new ContaCorrente(numeroConta, nomeCliente);

        Mockito.when(dao.getContaCorrente(ArgumentMatchers.anyInt())).thenReturn(novaConta);

        assertThrows(InvalidNumberException.class, () -> {
            service.sacar(numeroConta, valorOperacao);
        });

        verify(dao, never()).updateConta(ArgumentMatchers.any());
    }
}
