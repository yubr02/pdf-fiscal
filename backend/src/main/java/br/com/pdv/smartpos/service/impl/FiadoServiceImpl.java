package br.com.pdv.smartpos.service.impl;

import br.com.pdv.smartpos.dto.FiadoResponse;
import br.com.pdv.smartpos.repository.FiadoRepository;
import br.com.pdv.smartpos.service.FiadoService;
import br.com.pdv.smartpos.util.MapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FiadoServiceImpl implements FiadoService {

    private final FiadoRepository fiadoRepository;

    public FiadoServiceImpl(FiadoRepository fiadoRepository) {
        this.fiadoRepository = fiadoRepository;
    }

    @Override
    public List<FiadoResponse> pesquisar(String termo) {
        String query = termo == null ? "" : termo;
        return fiadoRepository
            .findByClienteNomeContainingIgnoreCaseOrClienteCpfCnpjContainingOrClienteTelefoneContainingOrderByDataVencimentoDesc(query, query, query)
            .stream()
            .map(MapperUtils::toFiadoResponse)
            .toList();
    }
}
