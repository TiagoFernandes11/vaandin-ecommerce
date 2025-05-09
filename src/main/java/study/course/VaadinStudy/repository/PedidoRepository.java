package study.course.VaadinStudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.course.VaadinStudy.entities.Pedido;

import java.util.List;
import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Optional<List<Pedido>> findAllByIdCliente(Long idCliente);

    @Query("SELECT p FROM Pedido p WHERE " +
            "CAST(p.id AS string) ILIKE CONCAT('%', :termo, '%') OR " +
            "CAST(p.idCliente AS string) ILIKE CONCAT('%', :termo, '%') OR " +
            "LOWER(p.status) ILIKE CONCAT('%', :termo, '%') OR " +
            "LOWER(p.tipoDeEntrega) ILIKE CONCAT('%', :termo, '%')")
    List<Pedido> buscarPorTermo(@Param("termo") String termo);

}
