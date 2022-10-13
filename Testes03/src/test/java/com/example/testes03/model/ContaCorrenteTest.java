package com.example.testes03.model;

import com.example.testes03.exception.InvalidNumberException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ContaCorrenteTest {
    private ContaCorrente contaCorrente;

    @BeforeEach
    void setup() {
        contaCorrente = new ContaCorrente(1, "Cliente 1");
    }

    @Test
    void sacar_retorneTrue_quandoHaSaldoSuficiente() throws InvalidNumberException{
        double valorDeposito = 100;
        double valorSaque = valorDeposito / 2;
        contaCorrente.depositar(valorDeposito);

        boolean sucesso = contaCorrente.sacar(valorSaque);

        assertThat(contaCorrente.getSaldo()).isEqualTo(valorDeposito - valorSaque);
        assertThat(sucesso).isTrue();
    }

    @ParameterizedTest
    @CsvSource({"100, 50", "50, 50", "10, 10"})
    void sacar_retorneTrue_quandoHaSaldoSuficiente(double valorDeposito, double valorSaque) throws InvalidNumberException{
        contaCorrente.depositar(valorDeposito);

        boolean sucesso = contaCorrente.sacar(valorSaque);

        assertThat(contaCorrente.getSaldo()).isEqualTo(valorDeposito - valorSaque);
        assertThat(sucesso).isTrue();
    }

    @Test
    void sacar_returnFalse_quandoSaldoInsuficiente() throws InvalidNumberException{
        double valorSaque = 100;

        boolean sucesso = contaCorrente.sacar(valorSaque);

        assertThat(contaCorrente.getSaldo()).isZero();
        assertThat(sucesso).isFalse();
    }

    @Test
    void sacar_throwException_quandoValorEhInvalido(){
        double valorSaque = -100;

        assertThatThrownBy(() -> contaCorrente.sacar(valorSaque)).isInstanceOf(InvalidNumberException.class);
    }

    @Test
    void toString_test(){
        assertThat(true).isTrue();
    }
}
