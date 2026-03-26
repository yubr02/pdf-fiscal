package br.com.pdv.smartpos.controller;

import br.com.pdv.smartpos.dto.TributacaoRequest;
import br.com.pdv.smartpos.dto.TributacaoResponse;
import br.com.pdv.smartpos.service.TributacaoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tributacao")
public class TributacaoController {

    private final TributacaoService tributacaoService;

    public TributacaoController(TributacaoService tributacaoService) {
        this.tributacaoService = tributacaoService;
    }

    @PostMapping
    public TributacaoResponse salvar(@Valid @RequestBody TributacaoRequest request) {
        return tributacaoService.salvar(request);
    }

    @GetMapping
    public TributacaoResponse buscar(@RequestParam Long produtoId) {
        return tributacaoService.buscarPorProduto(produtoId);
    }
}
