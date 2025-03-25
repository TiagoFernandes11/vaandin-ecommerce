package udemy.couse.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import udemy.couse.VaadinStudy.entities.Carrinho;
import udemy.couse.VaadinStudy.entities.Cliente;
import udemy.couse.VaadinStudy.entities.Produto;
import udemy.couse.VaadinStudy.repository.CarrinhoRepository;

import java.util.Objects;
import java.util.Optional;

@Service
public class CarrinhoService {

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ProdutoService produtoService;

    public boolean exists(Long idCliente){
        Optional<Carrinho> carrinho = carrinhoRepository.findByIdCliente(idCliente);
        return carrinho.isPresent();
    }

    public Carrinho find(Long idCliente){
        return carrinhoRepository.findByIdCliente(idCliente).orElse(null);
    }

    public void adicionarProduto(Long idCliente, Long idProduto) {
        Carrinho carrinho = carrinhoRepository.findByIdCliente(idCliente).orElse(null);
        Produto produto = produtoService.find(idProduto);
        if(!Objects.isNull(carrinho) && !Objects.isNull(produto)){
            carrinho.getProdutos().add(produto);
            carrinhoRepository.save(carrinho);
        }
    }

}
