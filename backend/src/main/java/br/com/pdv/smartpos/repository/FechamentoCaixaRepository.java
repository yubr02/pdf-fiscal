package br.com.pdv.smartpos.repository;

import br.com.pdv.smartpos.model.FechamentoCaixa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface FechamentoCaixaRepository extends JpaRepository<FechamentoCaixa, Long> {

    Optional<FechamentoCaixa> findByDataCaixaAndUsuarioId(LocalDate dataCaixa, Long usuarioId);
}
