package br.com.pdv.smartpos.controller;

import br.com.pdv.smartpos.dto.FiadoResponse;
import br.com.pdv.smartpos.service.FiadoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/fiados")
public class FiadoController {

    private final FiadoService fiadoService;

    public FiadoController(FiadoService fiadoService) {
        this.fiadoService = fiadoService;
    }

    @GetMapping("/buscar")
    public List<FiadoResponse> pesquisar(@RequestParam(defaultValue = "") String termo) {
        return fiadoService.pesquisar(termo);
    }
}
