package br.com.pdv.smartpos.controller;

import br.com.pdv.smartpos.dto.CupomNaoLancadoResponse;
import br.com.pdv.smartpos.service.CupomService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/cupons")
public class CupomController {

    private final CupomService cupomService;

    public CupomController(CupomService cupomService) {
        this.cupomService = cupomService;
    }

    @GetMapping("/pendentes")
    public List<CupomNaoLancadoResponse> listarPendentes() {
        return cupomService.listarPendentes();
    }

    @PatchMapping("/{id}/lancar")
    public CupomNaoLancadoResponse marcarComoLancado(@PathVariable Long id) {
        return cupomService.marcarComoLancado(id);
    }
}
