package br.com.pdv.smartpos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "fiado")
public class Fiado extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venda_id", nullable = false)
    private Venda venda;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valorPendente = BigDecimal.ZERO;

    @Column(nullable = false)
    private LocalDate dataVencimento;

    @Column(nullable = false, length = 20)
    private String statusFiado = "PENDENTE";

    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public Venda getVenda() { return venda; }
    public void setVenda(Venda venda) { this.venda = venda; }
    public BigDecimal getValorPendente() { return valorPendente; }
    public void setValorPendente(BigDecimal valorPendente) { this.valorPendente = valorPendente; }
    public LocalDate getDataVencimento() { return dataVencimento; }
    public void setDataVencimento(LocalDate dataVencimento) { this.dataVencimento = dataVencimento; }
    public String getStatusFiado() { return statusFiado; }
    public void setStatusFiado(String statusFiado) { this.statusFiado = statusFiado; }
}
