package br.com.pdv.smartpos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "manutencao_tributaria")
public class ManutencaoTributaria extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(length = 12)
    private String ncm;

    @Column(length = 12)
    private String cest;

    @Column(length = 10)
    private String cfop;

    @Column(length = 10)
    private String cst;

    @Column(length = 10)
    private String csosn;

    @Column(precision = 6, scale = 2)
    private BigDecimal aliquota = BigDecimal.ZERO;

    @Column(length = 30)
    private String origem;

    public Produto getProduto() { return produto; }
    public void setProduto(Produto produto) { this.produto = produto; }
    public String getNcm() { return ncm; }
    public void setNcm(String ncm) { this.ncm = ncm; }
    public String getCest() { return cest; }
    public void setCest(String cest) { this.cest = cest; }
    public String getCfop() { return cfop; }
    public void setCfop(String cfop) { this.cfop = cfop; }
    public String getCst() { return cst; }
    public void setCst(String cst) { this.cst = cst; }
    public String getCsosn() { return csosn; }
    public void setCsosn(String csosn) { this.csosn = csosn; }
    public BigDecimal getAliquota() { return aliquota; }
    public void setAliquota(BigDecimal aliquota) { this.aliquota = aliquota; }
    public String getOrigem() { return origem; }
    public void setOrigem(String origem) { this.origem = origem; }
}
