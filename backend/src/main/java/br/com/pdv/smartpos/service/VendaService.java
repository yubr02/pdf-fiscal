package br.com.pdv.smartpos.service;

import br.com.pdv.smartpos.dto.VendaRequest;
import br.com.pdv.smartpos.dto.VendaResponse;

import java.time.LocalDate;
import java.util.List;

public interface VendaService {

    VendaResponse registrar(VendaRequest request);

    List<VendaResponse> historico(LocalDate dataInicial, LocalDate dataFinal, Long usuarioId);

    byte[] exportarHistorico(LocalDate dataInicial, LocalDate dataFinal, Long usuarioId);
}
