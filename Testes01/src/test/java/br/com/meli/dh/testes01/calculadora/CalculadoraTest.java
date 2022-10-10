package br.com.meli.dh.testes01.calculadora;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculadoraTest {
    // dado que - quando - então
    // métodos_testado_ O que é esperado _Dados fornecidos
    Calculadora calculadora;

    @BeforeEach
    public void setup(){
        calculadora = new Calculadora();
        System.out.println("Before");
    }

    @Test
    @DisplayName("valida a soma")
    public void soma_returnDouble_whenValidInput(){
        // setup
        double n1 = 10, n2 = 20;
        double expected = 30;

        // run
        double resp = calculadora.soma(n1, n2);

        // validate
        assertEquals(expected, resp);
    }

    @Test
    public void dividir_returnDouble_whenTwoPositiveNumbers(){
        // setup
        double n1 = 30, n2 = 3;
        double expected = 10;

        // run
        double resp = calculadora.dividir(n1, n2);

        // validate
        assertEquals(expected, resp);
    }
}
