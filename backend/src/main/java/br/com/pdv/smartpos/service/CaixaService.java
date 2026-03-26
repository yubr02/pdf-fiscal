package br.com.pdv.smartpos.service;

import br.com.pdv.smartpos.dto.FechamentoCaixaRequest;
import br.com.pdv.smartpos.dto.ResumoCaixaResponse;

import java.time.LocalDate;

public interface CaixaService {

    ResumoCaixaResponse resumo(LocalDate dataCaixa, Long usuarioId);

    ResumoCaixaResponse fechar(FechamentoCaixaRequest request);
}
