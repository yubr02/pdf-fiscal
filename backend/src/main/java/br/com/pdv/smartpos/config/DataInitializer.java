package br.com.pdv.smartpos.config;

import br.com.pdv.smartpos.model.Cliente;
import br.com.pdv.smartpos.model.CupomNaoLancado;
import br.com.pdv.smartpos.model.Produto;
import br.com.pdv.smartpos.model.Usuario;
import br.com.pdv.smartpos.repository.ClienteRepository;
import br.com.pdv.smartpos.repository.CupomNaoLancadoRepository;
import br.com.pdv.smartpos.repository.ProdutoRepository;
import br.com.pdv.smartpos.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner seedData(
        UsuarioRepository usuarioRepository,
        ProdutoRepository produtoRepository,
        ClienteRepository clienteRepository,
        CupomNaoLancadoRepository cupomNaoLancadoRepository,
        PasswordEncoder passwordEncoder
    ) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                Usuario admin = new Usuario();
                admin.setNome("Administrador");
                admin.setUsuario("admin");
                admin.setSenhaHash(passwordEncoder.encode("1234"));
                admin.setPerfil("GERENTE");
                usuarioRepository.save(admin);
            }

            if (produtoRepository.count() == 0) {
                produtoRepository.save(createProduct("0001", "7891000000010", "ARROZ TIPO 1 5KG", "Mercearia", new BigDecimal("22.00"), new BigDecimal("29.90"), new BigDecimal("50")));
                produtoRepository.save(createProduct("0002", "7891000000027", "FEIJAO CARIOCA 1KG", "Mercearia", new BigDecimal("6.90"), new BigDecimal("9.49"), new BigDecimal("80")));
                produtoRepository.save(createProduct("0010", "0010", "BALAS SORTIDAS", "Bomboniere", new BigDecimal("0.10"), new BigDecimal("0.20"), new BigDecimal("500")));
                produtoRepository.save(createProduct("0100", "7891000000102", "REFRIGERANTE COLA 2L", "Bebidas", new BigDecimal("6.00"), new BigDecimal("8.99"), new BigDecimal("40")));
            }

            if (clienteRepository.count() == 0) {
                Cliente cliente = new Cliente();
                cliente.setNome("Cliente Fiado Exemplo");
                cliente.setCpfCnpj("12345678900");
                cliente.setTelefone("11999999999");
                cliente.setEndereco("Rua Central, 100");
                clienteRepository.save(cliente);
            }

            if (cupomNaoLancadoRepository.count() == 0) {
                CupomNaoLancado cupom = new CupomNaoLancado();
                cupom.setNumeroCupom("CUP-20260326-001");
                cupom.setDataHoraCupom(LocalDateTime.now().minusHours(2));
                cupom.setValor(new BigDecimal("42.70"));
                cupom.setStatusCupom("PENDENTE");
                cupomNaoLancadoRepository.save(cupom);
            }
        };
    }

    private Produto createProduct(String codigoInterno, String codigoBarras, String nome, String categoria, BigDecimal custo, BigDecimal venda, BigDecimal estoque) {
        Produto produto = new Produto();
        produto.setCodigoInterno(codigoInterno);
        produto.setCodigoBarras(codigoBarras);
        produto.setNome(nome);
        produto.setDescricao(nome);
        produto.setCategoria(categoria);
        produto.setPrecoCusto(custo);
        produto.setPrecoVenda(venda);
        produto.setEstoque(estoque);
        produto.setUnidade("UN");
        produto.setNcm("00000000");
        produto.setCfop("5102");
        produto.setCst("102");
        produto.setAliquota(BigDecimal.ZERO);
        produto.setOrigem("0 - Nacional");
        return produto;
    }
}
