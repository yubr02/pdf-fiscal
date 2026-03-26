package br.com.pdv.smartpos.service.impl;

import br.com.pdv.smartpos.dto.ProdutoRequest;
import br.com.pdv.smartpos.dto.ProdutoResponse;
import br.com.pdv.smartpos.exception.NotFoundException;
import br.com.pdv.smartpos.model.Produto;
import br.com.pdv.smartpos.repository.ProdutoRepository;
import br.com.pdv.smartpos.service.ProdutoService;
import br.com.pdv.smartpos.util.MapperUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    @Override
    public ProdutoResponse salvar(ProdutoRequest request) {
        Produto produto = buildProduto(new Produto(), request);
        return MapperUtils.toProdutoResponse(produtoRepository.save(produto));
    }

    @Override
    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado."));
        return MapperUtils.toProdutoResponse(produtoRepository.save(buildProduto(produto, request)));
    }

    @Override
    public void excluir(Long id) {
        Produto produto = produtoRepository.findById(id).orElseThrow(() -> new NotFoundException("Produto não encontrado."));
        produto.setAtivo(false);
        produtoRepository.save(produto);
    }

    @Override
    public ProdutoResponse buscarPorCodigoBarras(String codigoBarras) {
        Produto produto = produtoRepository.findByCodigoBarras(codigoBarras)
            .filter(Produto::getAtivo)
            .orElseThrow(() -> new NotFoundException("Produto não encontrado para o código informado."));
        return MapperUtils.toProdutoResponse(produto);
    }

    @Override
    public List<ProdutoResponse> pesquisar(String termo) {
        return produtoRepository
            .findTop30ByNomeContainingIgnoreCaseOrCodigoBarrasContainingOrCodigoInternoContainingOrderByNomeAsc(termo, termo, termo)
            .stream()
            .filter(Produto::getAtivo)
            .map(MapperUtils::toProdutoResponse)
            .toList();
    }

    @Override
    public List<ProdutoResponse> listar() {
        return produtoRepository.findAll().stream().filter(Produto::getAtivo).map(MapperUtils::toProdutoResponse).toList();
    }

    private Produto buildProduto(Produto produto, ProdutoRequest request) {
        produto.setCodigoInterno(request.codigoInterno());
        produto.setCodigoBarras(request.codigoBarras());
        produto.setNome(request.nome());
        produto.setDescricao(request.descricao());
        produto.setCategoria(request.categoria());
        produto.setSubcategoria(request.subcategoria());
        produto.setPrecoCusto(request.precoCusto());
        produto.setPrecoVenda(request.precoVenda());
        produto.setEstoque(request.estoque());
        produto.setUnidade(request.unidade());
        produto.setNcm(request.ncm());
        produto.setCest(request.cest());
        produto.setCfop(request.cfop());
        produto.setCst(request.cst());
        produto.setCsosn(request.csosn());
        produto.setAliquota(request.aliquota() != null ? request.aliquota() : BigDecimal.ZERO);
        produto.setOrigem(request.origem());
        return produto;
    }
}
