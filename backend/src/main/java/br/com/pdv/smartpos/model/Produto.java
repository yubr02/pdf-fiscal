package br.com.pdv.smartpos.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "produtos")
public class Produto extends BaseEntity {

    @Column(nullable = false, unique = true, length = 30)
    private String codigoInterno;

    @Column(nullable = false, unique = true, length = 30)
    private String codigoBarras;

    @Column(nullable = false, length = 160)
    private String nome;

    @Column(length = 300)
    private String descricao;

    @Column(length = 80)
    private String categoria;

    @Column(length = 80)
    private String subcategoria;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal precoCusto = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal precoVenda = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 3)
    private BigDecimal estoque = BigDecimal.ZERO;

    @Column(nullable = false, length = 10)
    private String unidade = "UN";

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

    public String getCodigoInterno() { return codigoInterno; }
    public void setCodigoInterno(String codigoInterno) { this.codigoInterno = codigoInterno; }
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getSubcategoria() { return subcategoria; }
    public void setSubcategoria(String subcategoria) { this.subcategoria = subcategoria; }
    public BigDecimal getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(BigDecimal precoCusto) { this.precoCusto = precoCusto; }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }
    public BigDecimal getEstoque() { return estoque; }
    public void setEstoque(BigDecimal estoque) { this.estoque = estoque; }
    public String getUnidade() { return unidade; }
    public void setUnidade(String unidade) { this.unidade = unidade; }
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
