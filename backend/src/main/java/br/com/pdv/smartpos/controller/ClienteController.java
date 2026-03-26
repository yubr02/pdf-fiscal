package br.com.pdv.smartpos.controller;

import br.com.pdv.smartpos.dto.ClienteRequest;
import br.com.pdv.smartpos.dto.ClienteResponse;
import br.com.pdv.smartpos.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ClienteResponse salvar(@Valid @RequestBody ClienteRequest request) {
        return clienteService.salvar(request);
    }

    @GetMapping("/buscar")
    public List<ClienteResponse> pesquisar(@RequestParam(defaultValue = "") String termo) {
        return clienteService.pesquisar(termo);
    }
}
