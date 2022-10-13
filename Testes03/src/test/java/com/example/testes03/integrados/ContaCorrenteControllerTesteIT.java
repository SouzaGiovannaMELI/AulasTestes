package com.example.testes03.integrados;

import com.example.testes03.dao.ContaDAO;
import com.example.testes03.model.ContaCorrente;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import org.springframework.test.web.servlet.ResultActions;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Log4j2
@SpringBootTest
@AutoConfigureMockMvc
public class ContaCorrenteControllerTesteIT {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContaDAO contaDAO;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup(){
        contaDAO.deleteAll();
        log.info("Contas: " +contaDAO.listarTodasContas().size());
    }

    @Test
    void novaContaCorrente_returnContaCorrente_quandoCriadaComSucesso() throws Exception {
        String nomeCliente = "Cliente 1";

        ResultActions resposta = mockMvc.perform(
                post("/conta_corrente/{cliente}", nomeCliente)
                .contentType(MediaType.APPLICATION_JSON)
        );

        resposta.andExpect(status().isCreated())
                .andExpect(jsonPath("$.cliente", CoreMatchers.is(nomeCliente)))
                .andExpect(jsonPath("$.saldo", CoreMatchers.is(0.0)));

        assertThat(contaDAO.listarTodasContas().size()).isEqualTo(1);
        assertThat(contaDAO.listarTodasContas().get(0)).isNotNull();

        log.info(contaDAO.listarTodasContas().get(0));
        log.info(objectMapper.readValue(resposta.andReturn().getResponse().getContentAsString(), ContaCorrente.class));
    }

    @Test
    void getContaCorrente_returnContaCorrente_quandoContaExiste() throws Exception {
        ContaCorrente conta = contaDAO.novaContaCorrente("Cliente 1");

        ResultActions resposta = mockMvc.perform(
                get("/conta_corrente/{numero}", conta.getNumero())
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente", CoreMatchers.is(conta.getCliente())))
                .andExpect(jsonPath("$.numero", CoreMatchers.is(conta.getNumero())));
    }

    @Test
    void depositar_returnContaCorrenteAtualizada_quandoDepositarComSucesso() throws Exception {
        ContaCorrente conta = contaDAO.novaContaCorrente("Cliente 1");
        double valorDeposito = 100;

        ResultActions resposta = mockMvc.perform(
                patch("/conta_corrente/dep/{numero}/{valor}", conta.getNumero(), valorDeposito)
                        .contentType(MediaType.APPLICATION_JSON)
        );

        resposta.andExpect(status().isOk())
                .andExpect(jsonPath("$.cliente", CoreMatchers.is(conta.getCliente())))
                .andExpect(jsonPath("$.saldo", CoreMatchers.is(valorDeposito)));

        assertThat(contaDAO.getContaCorrente(conta.getNumero()).getSaldo()).isEqualTo(valorDeposito);
    }
}
