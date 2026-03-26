package br.com.pdv.smartpos.service;

import br.com.pdv.smartpos.dto.TributacaoRequest;
import br.com.pdv.smartpos.dto.TributacaoResponse;

public interface TributacaoService {

    TributacaoResponse salvar(TributacaoRequest request);

    TributacaoResponse buscarPorProduto(Long produtoId);
}
