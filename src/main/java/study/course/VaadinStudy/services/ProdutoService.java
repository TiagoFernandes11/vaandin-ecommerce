package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Categoria;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.repository.CategoriaRepository;
import study.course.VaadinStudy.repository.ProdutoRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Produto> findAll(){
        return produtoRepository.findAll();
    }

    public Produto find(long idProduto){
        return produtoRepository.findById(idProduto).orElse(null);
    }

    public boolean create(Produto produto){
        Optional<Produto> produtoDB = produtoRepository.findBySku(produto.getSku());
        if(produtoDB.isPresent()){
            if(Objects.equals(produtoDB.get().getId(), produto.getId())){
                produtoRepository.save(produto);
                return true;
            }
            return false;
        }
        produtoRepository.save(produto);
        return true;
    }

    public void delete(Produto produto){
        produtoRepository.delete(produto);
    }

    public void adicionarACategoria(String nomeCategoria, long idProduto){
        Categoria categoria = categoriaRepository.findByNome(nomeCategoria).orElse(null);
        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        if(!Objects.isNull(categoria) && !Objects.isNull(produto)){
            produto.getCategorias().add(categoria);
            categoria.getProdutos().add(produto);
            produtoRepository.save(produto);
            categoriaRepository.save(categoria);
        }
    }

    public void removerDaCategoria(String nomeCategoria, long idProduto){
        Categoria categoria = categoriaRepository.findByNome(nomeCategoria).orElse(null);
        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        if(!Objects.isNull(categoria) && !Objects.isNull(produto)){
            produto.getCategorias().remove(categoria);
            categoria.getProdutos().remove(produto);
            produtoRepository.save(produto);
            categoriaRepository.save(categoria);
        }
    }
}
