package br.com.pdv.smartpos.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

public record VendaRequest(
    @NotNull(message = "Informe o operador.") Long usuarioId,
    Long clienteId,
    @NotBlank(message = "Informe o tipo de venda.") String tipoVenda,
    @NotBlank(message = "Informe a forma de pagamento.") String formaPagamento,
    @NotNull(message = "Informe o valor pago.") BigDecimal valorPago,
    BigDecimal desconto,
    BigDecimal acrescimo,
    Boolean gerarFiado,
    @Valid @NotEmpty(message = "Adicione pelo menos um item.") List<ItemVendaRequest> itens
) {
}
