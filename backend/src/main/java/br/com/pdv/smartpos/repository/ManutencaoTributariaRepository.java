package br.com.pdv.smartpos.repository;

import br.com.pdv.smartpos.model.ManutencaoTributaria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManutencaoTributariaRepository extends JpaRepository<ManutencaoTributaria, Long> {

    Optional<ManutencaoTributaria> findByProdutoId(Long produtoId);
}
