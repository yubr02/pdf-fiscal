package br.com.pdv.smartpos.service.impl;

import br.com.pdv.smartpos.dto.FechamentoCaixaRequest;
import br.com.pdv.smartpos.dto.ResumoCaixaResponse;
import br.com.pdv.smartpos.exception.BusinessException;
import br.com.pdv.smartpos.exception.NotFoundException;
import br.com.pdv.smartpos.model.FechamentoCaixa;
import br.com.pdv.smartpos.model.Usuario;
import br.com.pdv.smartpos.model.Venda;
import br.com.pdv.smartpos.repository.FechamentoCaixaRepository;
import br.com.pdv.smartpos.repository.UsuarioRepository;
import br.com.pdv.smartpos.repository.VendaRepository;
import br.com.pdv.smartpos.service.CaixaService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class CaixaServiceImpl implements CaixaService {

    private final VendaRepository vendaRepository;
    private final FechamentoCaixaRepository fechamentoCaixaRepository;
    private final UsuarioRepository usuarioRepository;

    public CaixaServiceImpl(VendaRepository vendaRepository, FechamentoCaixaRepository fechamentoCaixaRepository, UsuarioRepository usuarioRepository) {
        this.vendaRepository = vendaRepository;
        this.fechamentoCaixaRepository = fechamentoCaixaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public ResumoCaixaResponse resumo(LocalDate dataCaixa, Long usuarioId) {
        LocalDate data = dataCaixa != null ? dataCaixa : LocalDate.now();
        List<Venda> vendas = vendaRepository.findByUsuarioIdAndDataHoraVendaBetweenOrderByDataHoraVendaDesc(
            usuarioId,
            data.atStartOfDay(),
            data.atTime(LocalTime.MAX)
        );

        BigDecimal totalVendido = vendas.stream().map(Venda::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalFiscal = vendas.stream().filter(v -> "FISCAL".equalsIgnoreCase(v.getTipoVenda())).map(Venda::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalNaoFiscal = vendas.stream().filter(v -> !"FISCAL".equalsIgnoreCase(v.getTipoVenda())).map(Venda::getTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        boolean fechado = fechamentoCaixaRepository.findByDataCaixaAndUsuarioId(data, usuarioId).isPresent();

        return new ResumoCaixaResponse(data, totalVendido, totalFiscal, totalNaoFiscal, vendas.size(), fechado);
    }

    @Override
    public ResumoCaixaResponse fechar(FechamentoCaixaRequest request) {
        LocalDate data = request.dataCaixa() != null ? request.dataCaixa() : LocalDate.now();
        Usuario usuario = usuarioRepository.findById(request.usuarioId()).orElseThrow(() -> new NotFoundException("Usuário não encontrado."));

        if (fechamentoCaixaRepository.findByDataCaixaAndUsuarioId(data, request.usuarioId()).isPresent()) {
            throw new BusinessException("O caixa deste operador já foi fechado nesta data.");
        }

        ResumoCaixaResponse resumo = resumo(data, request.usuarioId());
        FechamentoCaixa fechamento = new FechamentoCaixa();
        fechamento.setUsuario(usuario);
        fechamento.setDataCaixa(data);
        fechamento.setTotalVendido(resumo.totalVendido());
        fechamento.setTotalFiscal(resumo.totalFiscal());
        fechamento.setTotalNaoFiscal(resumo.totalNaoFiscal());
        fechamento.setQuantidadeVendas(resumo.quantidadeVendas());
        fechamento.setDataHoraFechamento(LocalDateTime.now());
        fechamentoCaixaRepository.save(fechamento);

        return new ResumoCaixaResponse(data, resumo.totalVendido(), resumo.totalFiscal(), resumo.totalNaoFiscal(), resumo.quantidadeVendas(), true);
    }
}
