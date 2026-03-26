package br.com.pdv.smartpos.service.impl;

import br.com.pdv.smartpos.dto.TributacaoRequest;
import br.com.pdv.smartpos.dto.TributacaoResponse;
import br.com.pdv.smartpos.exception.NotFoundException;
import br.com.pdv.smartpos.model.ManutencaoTributaria;
import br.com.pdv.smartpos.model.Produto;
import br.com.pdv.smartpos.repository.ManutencaoTributariaRepository;
import br.com.pdv.smartpos.repository.ProdutoRepository;
import br.com.pdv.smartpos.service.TributacaoService;
import br.com.pdv.smartpos.util.MapperUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class TributacaoServiceImpl implements TributacaoService {

    private final ManutencaoTributariaRepository manutencaoTributariaRepository;
    private final ProdutoRepository produtoRepository;

    public TributacaoServiceImpl(ManutencaoTributariaRepository manutencaoTributariaRepository, ProdutoRepository produtoRepository) {
        this.manutencaoTributariaRepository = manutencaoTributariaRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    public TributacaoResponse salvar(TributacaoRequest request) {
        Produto produto = produtoRepository.findById(request.produtoId()).orElseThrow(() -> new NotFoundException("Produto não encontrado."));

        ManutencaoTributaria tributacao = manutencaoTributariaRepository.findByProdutoId(produto.getId()).orElse(new ManutencaoTributaria());
        tributacao.setProduto(produto);
        tributacao.setNcm(request.ncm());
        tributacao.setCest(request.cest());
        tributacao.setCfop(request.cfop());
        tributacao.setCst(request.cst());
        tributacao.setCsosn(request.csosn());
        tributacao.setAliquota(request.aliquota() != null ? request.aliquota() : BigDecimal.ZERO);
        tributacao.setOrigem(request.origem());

        produto.setNcm(request.ncm());
        produto.setCest(request.cest());
        produto.setCfop(request.cfop());
        produto.setCst(request.cst());
        produto.setCsosn(request.csosn());
        produto.setAliquota(request.aliquota() != null ? request.aliquota() : BigDecimal.ZERO);
        produto.setOrigem(request.origem());
        produtoRepository.save(produto);

        return MapperUtils.toTributacaoResponse(manutencaoTributariaRepository.save(tributacao));
    }

    @Override
    public TributacaoResponse buscarPorProduto(Long produtoId) {
        return manutencaoTributariaRepository.findByProdutoId(produtoId)
            .map(MapperUtils::toTributacaoResponse)
            .orElseThrow(() -> new NotFoundException("Tributação não encontrada para o produto."));
    }
}
