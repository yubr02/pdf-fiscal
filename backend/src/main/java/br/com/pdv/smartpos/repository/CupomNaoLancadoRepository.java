package br.com.pdv.smartpos.repository;

import br.com.pdv.smartpos.model.CupomNaoLancado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CupomNaoLancadoRepository extends JpaRepository<CupomNaoLancado, Long> {

    List<CupomNaoLancado> findByStatusCupomOrderByDataHoraCupomDesc(String statusCupom);
}
