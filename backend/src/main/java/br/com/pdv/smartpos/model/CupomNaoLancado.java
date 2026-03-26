package br.com.pdv.smartpos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "cupons_nao_lancados")
public class CupomNaoLancado extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String numeroCupom;

    @Column(nullable = false)
    private LocalDateTime dataHoraCupom;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor = BigDecimal.ZERO;

    @Column(nullable = false, length = 20)
    private String statusCupom = "PENDENTE";

    public String getNumeroCupom() {
        return numeroCupom;
    }

    public void setNumeroCupom(String numeroCupom) {
        this.numeroCupom = numeroCupom;
    }

    public LocalDateTime getDataHoraCupom() {
        return dataHoraCupom;
    }

    public void setDataHoraCupom(LocalDateTime dataHoraCupom) {
        this.dataHoraCupom = dataHoraCupom;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public String getStatusCupom() {
        return statusCupom;
    }

    public void setStatusCupom(String statusCupom) {
        this.statusCupom = statusCupom;
    }
}
