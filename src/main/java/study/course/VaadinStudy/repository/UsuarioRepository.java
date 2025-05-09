package study.course.VaadinStudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import study.course.VaadinStudy.entities.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findById(Long id);

    Optional<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u WHERE " +
            "CAST(u.nomeCompleto AS STRING) ILIKE CONCAT('%', :termo, '%') OR " +
            "CAST(u.email as STRING) ILIKE CONCAT('%', :termo, '%')")
    List<Usuario> findByTermoLike(@Param("termo") String termo);
}
