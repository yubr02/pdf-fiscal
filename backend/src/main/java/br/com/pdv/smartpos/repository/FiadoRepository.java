package br.com.pdv.smartpos.repository;

import br.com.pdv.smartpos.model.Fiado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FiadoRepository extends JpaRepository<Fiado, Long> {

    List<Fiado> findByClienteNomeContainingIgnoreCaseOrClienteCpfCnpjContainingOrClienteTelefoneContainingOrderByDataVencimentoDesc(
        String nome,
        String cpfCnpj,
        String telefone
    );
}
