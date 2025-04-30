package study.course.VaadinStudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.course.VaadinStudy.entities.Endereco;
import study.course.VaadinStudy.entities.Usuario;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

    Optional<Endereco> findByLogradouroAndNumero(String logradouro, Long numero);

    List<Endereco> findAllByUsuario(Usuario usuario);
}
