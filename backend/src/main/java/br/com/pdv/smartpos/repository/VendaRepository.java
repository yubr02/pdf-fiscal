package br.com.pdv.smartpos.repository;

import br.com.pdv.smartpos.model.Venda;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VendaRepository extends JpaRepository<Venda, Long> {

    List<Venda> findByDataHoraVendaBetweenOrderByDataHoraVendaDesc(LocalDateTime inicio, LocalDateTime fim);

    List<Venda> findByUsuarioIdAndDataHoraVendaBetweenOrderByDataHoraVendaDesc(Long usuarioId, LocalDateTime inicio, LocalDateTime fim);
}
