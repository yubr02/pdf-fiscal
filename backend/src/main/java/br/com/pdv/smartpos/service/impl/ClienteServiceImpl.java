package br.com.pdv.smartpos.service.impl;

import br.com.pdv.smartpos.dto.ClienteRequest;
import br.com.pdv.smartpos.dto.ClienteResponse;
import br.com.pdv.smartpos.model.Cliente;
import br.com.pdv.smartpos.repository.ClienteRepository;
import br.com.pdv.smartpos.service.ClienteService;
import br.com.pdv.smartpos.util.MapperUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;

    public ClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public ClienteResponse salvar(ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNome(request.nome());
        cliente.setCpfCnpj(request.cpfCnpj());
        cliente.setTelefone(request.telefone());
        cliente.setEndereco(request.endereco());
        return MapperUtils.toClienteResponse(clienteRepository.save(cliente));
    }

    @Override
    public List<ClienteResponse> pesquisar(String termo) {
        return clienteRepository
            .findTop30ByNomeContainingIgnoreCaseOrCpfCnpjContainingOrTelefoneContainingOrderByNomeAsc(termo, termo, termo)
            .stream()
            .map(MapperUtils::toClienteResponse)
            .toList();
    }
}
