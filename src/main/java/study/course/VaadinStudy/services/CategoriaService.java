package study.course.VaadinStudy.services;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Categoria;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.repository.CategoriaRepository;
import study.course.VaadinStudy.repository.ProdutoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CategoriaService extends BaseService<Categoria>{

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        super(categoriaRepository);
    }

    public Categoria find(String nome){
        return categoriaRepository.findByNome(nome).orElse(null);
    }

    public boolean create(Categoria categoria){
        Optional<Categoria> categoriaFromDb = categoriaRepository.findByNome(categoria.getNome());
        if(categoriaFromDb.isEmpty()){
            categoriaRepository.save(categoria);
            return true;
        }
        return false;
    }

    public boolean adicionarProduto(String nomeCategoria, Long produtoId){
        Categoria categoria = categoriaRepository.findByNome(nomeCategoria).orElse(null);
        Produto produto = produtoRepository.findById(produtoId).orElse(null);
        if(Objects.isNull(categoria) || Objects.isNull(produto)){
            return false;
        } else if(categoria.getProdutos().contains(produto)){
            return false;
        } else {
            categoria.getProdutos().add(produto);
            categoriaRepository.save(categoria);
            return true;
        }
    }

    public boolean removerProduto(String nomeCategoria, Long produtoId){
        Categoria categoria = categoriaRepository.findByNome(nomeCategoria).orElse(null);
        Produto produto = produtoRepository.findById(produtoId).orElse(null);
        if(!Objects.isNull(categoria) && !Objects.isNull(produto)){
            if(categoria.getProdutos().contains(produto)){
                categoria.getProdutos().remove(produto);
                categoriaRepository.save(categoria);
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
