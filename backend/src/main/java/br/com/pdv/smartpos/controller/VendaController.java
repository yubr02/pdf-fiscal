package br.com.pdv.smartpos.controller;

import br.com.pdv.smartpos.dto.VendaRequest;
import br.com.pdv.smartpos.dto.VendaResponse;
import br.com.pdv.smartpos.service.VendaService;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/vendas")
public class VendaController {

    private final VendaService vendaService;

    public VendaController(VendaService vendaService) {
        this.vendaService = vendaService;
    }

    @PostMapping
    public VendaResponse registrar(@Valid @RequestBody VendaRequest request) {
        return vendaService.registrar(request);
    }

    @GetMapping("/historico")
    public List<VendaResponse> historico(
        @RequestParam(required = false) LocalDate dataInicial,
        @RequestParam(required = false) LocalDate dataFinal,
        @RequestParam(required = false) Long usuarioId
    ) {
        return vendaService.historico(dataInicial, dataFinal, usuarioId);
    }

    @GetMapping("/historico/exportar")
    public ResponseEntity<byte[]> exportar(
        @RequestParam(required = false) LocalDate dataInicial,
        @RequestParam(required = false) LocalDate dataFinal,
        @RequestParam(required = false) Long usuarioId
    ) {
        byte[] arquivo = vendaService.exportarHistorico(dataInicial, dataFinal, usuarioId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.attachment().filename("historico-vendas.xlsx").build());
        return ResponseEntity.ok().headers(headers).body(arquivo);
    }
}
