package com.example.testes03.dto;

import com.example.testes03.model.Conta;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransfDTO {
    private Conta origem;
    private Conta destino;
}
