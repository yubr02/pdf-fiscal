package br.com.pdv.smartpos.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UsuarioCadastroRequest(
    @NotBlank(message = "Informe o nome.") String nome,
    @NotBlank(message = "Informe o usuário.") String usuario,
    @Size(min = 4, message = "A senha deve ter pelo menos 4 caracteres.") String senha,
    @NotBlank(message = "Confirme a senha.") String confirmarSenha
) {
}
