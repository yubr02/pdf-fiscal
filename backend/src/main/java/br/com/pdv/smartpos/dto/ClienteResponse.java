package br.com.pdv.smartpos.dto;

public record ClienteResponse(
    Long id,
    String nome,
    String cpfCnpj,
    String telefone,
    String endereco
) {
}
