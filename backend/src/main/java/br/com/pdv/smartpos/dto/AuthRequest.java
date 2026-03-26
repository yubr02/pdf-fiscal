package br.com.pdv.smartpos.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @NotBlank(message = "Informe o usuário.") String usuario,
    @NotBlank(message = "Informe a senha.") String senha
) {
}
