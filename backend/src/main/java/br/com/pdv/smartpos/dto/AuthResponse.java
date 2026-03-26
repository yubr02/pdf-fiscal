package br.com.pdv.smartpos.dto;

public record AuthResponse(
    Long id,
    String nome,
    String usuario,
    String perfil,
    String token
) {
}
