package br.com.pdv.smartpos.repository;

import br.com.pdv.smartpos.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    List<Cliente> findTop30ByNomeContainingIgnoreCaseOrCpfCnpjContainingOrTelefoneContainingOrderByNomeAsc(
        String nome,
        String cpfCnpj,
        String telefone
    );
}
