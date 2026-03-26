package br.com.pdv.smartpos.repository;

import br.com.pdv.smartpos.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findByCodigoBarras(String codigoBarras);

    List<Produto> findTop30ByNomeContainingIgnoreCaseOrCodigoBarrasContainingOrCodigoInternoContainingOrderByNomeAsc(
        String nome,
        String codigoBarras,
        String codigoInterno
    );
}
