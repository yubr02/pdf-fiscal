package br.com.pdv.smartpos.service.impl;

import br.com.pdv.smartpos.dto.CupomNaoLancadoResponse;
import br.com.pdv.smartpos.exception.NotFoundException;
import br.com.pdv.smartpos.model.CupomNaoLancado;
import br.com.pdv.smartpos.repository.CupomNaoLancadoRepository;
import br.com.pdv.smartpos.service.CupomService;
import br.com.pdv.smartpos.util.MapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CupomServiceImpl implements CupomService {

    private final CupomNaoLancadoRepository cupomNaoLancadoRepository;

    public CupomServiceImpl(CupomNaoLancadoRepository cupomNaoLancadoRepository) {
        this.cupomNaoLancadoRepository = cupomNaoLancadoRepository;
    }

    @Override
    public List<CupomNaoLancadoResponse> listarPendentes() {
        return cupomNaoLancadoRepository.findByStatusCupomOrderByDataHoraCupomDesc("PENDENTE")
            .stream()
            .map(MapperUtils::toCupomResponse)
            .toList();
    }

    @Override
    public CupomNaoLancadoResponse marcarComoLancado(Long id) {
        CupomNaoLancado cupom = cupomNaoLancadoRepository.findById(id).orElseThrow(() -> new NotFoundException("Cupom não encontrado."));
        cupom.setStatusCupom("LANCADO");
        return MapperUtils.toCupomResponse(cupomNaoLancadoRepository.save(cupom));
    }
}
