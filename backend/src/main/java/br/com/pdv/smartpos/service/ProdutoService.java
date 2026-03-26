package br.com.pdv.smartpos.service;

import br.com.pdv.smartpos.dto.ProdutoRequest;
import br.com.pdv.smartpos.dto.ProdutoResponse;

import java.util.List;

public interface ProdutoService {

    ProdutoResponse salvar(ProdutoRequest request);

    ProdutoResponse atualizar(Long id, ProdutoRequest request);

    void excluir(Long id);

    ProdutoResponse buscarPorCodigoBarras(String codigoBarras);

    List<ProdutoResponse> pesquisar(String termo);

    List<ProdutoResponse> listar();
}
