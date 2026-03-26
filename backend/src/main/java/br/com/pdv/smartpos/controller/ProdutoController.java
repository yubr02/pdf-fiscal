package br.com.pdv.smartpos.controller;

import br.com.pdv.smartpos.dto.ProdutoRequest;
import br.com.pdv.smartpos.dto.ProdutoResponse;
import br.com.pdv.smartpos.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<ProdutoResponse> listar() {
        return produtoService.listar();
    }

    @GetMapping("/buscar")
    public List<ProdutoResponse> pesquisar(@RequestParam(defaultValue = "") String termo) {
        return produtoService.pesquisar(termo);
    }

    @GetMapping("/codigo-barras/{codigoBarras}")
    public ProdutoResponse buscarPorCodigoBarras(@PathVariable String codigoBarras) {
        return produtoService.buscarPorCodigoBarras(codigoBarras);
    }

    @PostMapping
    public ProdutoResponse salvar(@Valid @RequestBody ProdutoRequest request) {
        return produtoService.salvar(request);
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        return produtoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id) {
        produtoService.excluir(id);
    }
}
