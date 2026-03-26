package br.com.pdv.smartpos.service;

import br.com.pdv.smartpos.dto.FiadoResponse;

import java.util.List;

public interface FiadoService {

    List<FiadoResponse> pesquisar(String termo);
}
