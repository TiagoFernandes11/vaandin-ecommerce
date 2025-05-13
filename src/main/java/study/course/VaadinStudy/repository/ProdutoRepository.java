package study.course.VaadinStudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.course.VaadinStudy.entities.Produto;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Optional<Produto> findBySku(long sku);

    @Query("SELECT p FROM Produto p WHERE " +
            "CAST(p.nome AS STRING) LIKE CONCAT('%', :termo, '%') OR " +
            "CAST(p.sku AS STRING) LIKE CONCAT('%', :termo, '%')")
    List<Produto> findByTermoLike(@Param("termo") String termo);

    @Query("SELECT p FROM Produto p JOIN p.categorias c WHERE c.id = :categoriaId")
    List<Produto> findAllByCategoriaId(@Param("categoriaId") Long categoriaId);

    @Query("SELECT p FROM Produto p LEFT JOIN p.categorias c WHERE c.id <> :categoriaId OR c.id IS NULL")
    List<Produto> findAllByNotCategoriaId(@Param("categoriaId") Long categoriaId);
}
