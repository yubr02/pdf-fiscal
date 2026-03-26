package br.com.pdv.smartpos.service;

import br.com.pdv.smartpos.dto.CupomNaoLancadoResponse;

import java.util.List;

public interface CupomService {

    List<CupomNaoLancadoResponse> listarPendentes();

    CupomNaoLancadoResponse marcarComoLancado(Long id);
}
