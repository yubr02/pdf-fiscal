package br.com.pdv.smartpos.controller;

import br.com.pdv.smartpos.dto.FechamentoCaixaRequest;
import br.com.pdv.smartpos.dto.ResumoCaixaResponse;
import br.com.pdv.smartpos.service.CaixaService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/caixa")
public class CaixaController {

    private final CaixaService caixaService;

    public CaixaController(CaixaService caixaService) {
        this.caixaService = caixaService;
    }

    @GetMapping("/resumo")
    public ResumoCaixaResponse resumo(@RequestParam(required = false) LocalDate dataCaixa, @RequestParam Long usuarioId) {
        return caixaService.resumo(dataCaixa, usuarioId);
    }

    @PostMapping("/fechar")
    public ResumoCaixaResponse fechar(@Valid @RequestBody FechamentoCaixaRequest request) {
        return caixaService.fechar(request);
    }
}
