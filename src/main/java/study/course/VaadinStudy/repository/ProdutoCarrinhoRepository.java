package study.course.VaadinStudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.course.VaadinStudy.entities.ItemCarrinho;

@Repository
public interface ProdutoCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {
}
