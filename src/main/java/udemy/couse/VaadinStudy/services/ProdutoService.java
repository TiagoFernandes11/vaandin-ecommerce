package udemy.couse.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import udemy.couse.VaadinStudy.entities.Produto;
import udemy.couse.VaadinStudy.repository.ProdutoRepository;

import java.util.List;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    public List<Produto> findAll(){
        return produtoRepository.findAll();
    }

    public boolean create(Produto produto){
        if(produtoRepository.findBySku(produto.getSku()).isPresent()){
            return false;
        }
        produtoRepository.save(produto);
        return true;
    }
}
