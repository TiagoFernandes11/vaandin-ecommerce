package study.course.VaadinStudy.services;

import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Categoria> findAll(){
        return categoriaRepository.findAll();
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

    public void delete(Categoria categoria){
        categoriaRepository.delete(categoria);
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
