package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Categoria;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.repository.CategoriaRepository;
import study.course.VaadinStudy.repository.ProdutoRepository;
import study.course.VaadinStudy.services.base.BaseService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProdutoService extends BaseService<Produto> {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        super(produtoRepository);
    }

    public boolean create(Produto produto) {
        Optional<Produto> produtoDB = produtoRepository.findBySku(produto.getSku());
        if (produtoDB.isPresent()) {
            if (Objects.equals(produtoDB.get().getId(), produto.getId())) {
                save(produto);
                return true;
            }
            return false;
        }
        save(produto);
        return true;
    }

    public void adicionarACategoria(String nomeCategoria, long idProduto) {
        Categoria categoria = categoriaRepository.findByNome(nomeCategoria).orElse(null);
        Produto produto = produtoRepository.findById(idProduto).orElse(null);

        if (Objects.isNull(categoria) || Objects.isNull(produto)) return;

        produto.getCategorias().add(categoria);
        categoria.getProdutos().add(produto);
        save(produto);
        categoriaRepository.save(categoria);

    }

    public List<Produto> findAllByTermo(String termo){
        return produtoRepository.findByTermoLike(termo);
    }

    public void removerDaCategoria(String nomeCategoria, long idProduto) {
        Categoria categoria = categoriaRepository.findByNome(nomeCategoria).orElse(null);
        Produto produto = produtoRepository.findById(idProduto).orElse(null);

        if (Objects.isNull(categoria) || Objects.isNull(produto)) return;

        produto.getCategorias().remove(categoria);
        categoria.getProdutos().remove(produto);
        save(produto);
        categoriaRepository.save(categoria);

    }
}
