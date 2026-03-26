package br.com.pdv.smartpos.service;

import br.com.pdv.smartpos.dto.ClienteRequest;
import br.com.pdv.smartpos.dto.ClienteResponse;

import java.util.List;

public interface ClienteService {

    ClienteResponse salvar(ClienteRequest request);

    List<ClienteResponse> pesquisar(String termo);
}
