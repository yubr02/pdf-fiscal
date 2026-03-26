package br.com.pdv.smartpos.dto;

import jakarta.validation.constraints.NotBlank;

public record ClienteRequest(
    @NotBlank(message = "Informe o nome.") String nome,
    String cpfCnpj,
    String telefone,
    String endereco
) {
}
