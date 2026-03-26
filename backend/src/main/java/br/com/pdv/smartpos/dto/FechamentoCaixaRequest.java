package br.com.pdv.smartpos.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record FechamentoCaixaRequest(
    @NotNull(message = "Informe o operador.") Long usuarioId,
    LocalDate dataCaixa
) {
}
