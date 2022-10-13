package com.example.testes03.controller;

import com.example.testes03.dto.ContaDTO;
import com.example.testes03.model.ContaCorrente;
import com.example.testes03.service.ContaCorrenteService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ContaCorrenteController.class)
class ContaCorrenteControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContaCorrenteService service;

    private ContaCorrente conta;

    @BeforeEach
    void setup(){
        conta = new ContaCorrente(1, "Cliente 1");
    }

    @Test
    void getConta_returnContaCorrente_quandoContaExiste() throws Exception{
        BDDMockito.when(service.getConta(anyInt())).thenReturn(conta);

        ResultActions resposta = mockMvc.perform(
                get("conta_corrente/{numero}", conta.getNumero())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente", CoreMatchers.is(conta.getCliente())))
                .andExpect(jsonPath("$.saldo", CoreMatchers.is(conta.getSaldo())));
    }

    @Test
    void novaContaCorrente_returnContaCorrente_quandoCriarNovaConta() throws Exception{
        BDDMockito.when(service.novaContaCorrente(anyString())).thenReturn(conta);

        ResultActions resposta = mockMvc.perform(
                post("conta_corrente/{cliente}", conta.getCliente())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resposta.andExpect(status().isCreated())
                .andExpect(jsonPath("$.cliente", CoreMatchers.is(conta.getCliente())))
                .andExpect(jsonPath("$.saldo", CoreMatchers.is(conta.getSaldo())));
    }

    @Test
    void novaContaCorrenteBody_returnContaCorrente_quandoCriarNovaConta() throws Exception {
        BDDMockito.when(service.novaContaCorrente(anyString())).thenReturn(conta);

        ResultActions resposta = mockMvc.perform(post("/conta_corrente/new")
                        .content(objectMapper.writeValueAsString(new ContaDTO(conta.getCliente())))
                        .contentType(MediaType.APPLICATION_JSON));

        resposta.andExpect(status().isCreated())
                .andExpect(jsonPath("$.cliente", CoreMatchers.is(conta.getCliente())))
                .andExpect(jsonPath("$.saldo", CoreMatchers.is(conta.getSaldo())));
    }

    @Test
    void depositar_returnContaCorrenteAtualizada_quandoDepositarComSucesso() throws Exception {
        double valorDeposito = 100;

        BDDMockito.when(service.getConta(anyInt())).thenReturn(conta);
        BDDMockito.doAnswer(invocationOnMock -> {
            conta.depositar(valorDeposito);
            return null;
        }).when(service).depositar(conta.getNumero(), valorDeposito);

        ResultActions resposta = mockMvc.perform(
                patch("/conta_corrente/dep/{numero}/{valor}", conta.getNumero(), valorDeposito)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente", CoreMatchers.is(conta.getCliente())))
                .andExpect(jsonPath("$.saldo", CoreMatchers.is(valorDeposito)));
    }
}
