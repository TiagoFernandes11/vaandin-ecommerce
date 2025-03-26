package study.couse.VaadinStudy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.couse.VaadinStudy.entities.ItemCarrinho;

@Repository
public interface ProdutoCarrinhoRepository extends JpaRepository<ItemCarrinho, Long> {
}
