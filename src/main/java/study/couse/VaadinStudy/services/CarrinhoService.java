package study.couse.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.couse.VaadinStudy.entities.Carrinho;
import study.couse.VaadinStudy.entities.Produto;
import study.couse.VaadinStudy.entities.ItemCarrinho;
import study.couse.VaadinStudy.repository.CarrinhoRepository;
import study.couse.VaadinStudy.repository.ProdutoCarrinhoRepository;
import study.couse.VaadinStudy.repository.ProdutoRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CarrinhoService {

    @Autowired
    private ProdutoCarrinhoRepository produtoCarrinhoRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    public boolean exists(Long idCliente){
        Optional<Carrinho> carrinho = carrinhoRepository.findByIdCliente(idCliente);
        return carrinho.isPresent();
    }

    public Carrinho find(Long idCliente){
        return carrinhoRepository.findByIdCliente(idCliente).orElse(null);
    }

    public void create(Long idCliente){
        List<ItemCarrinho> produtos = new ArrayList<>();
        Carrinho carrinho = new Carrinho(null, idCliente, produtos, 0.00);
        carrinhoRepository.save(carrinho);
    }

    public void adicionarProduto(Long idCliente, Long idProduto) {
        Carrinho carrinho = carrinhoRepository.findByIdCliente(idCliente).orElse(null);
        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        ItemCarrinho itemCarrinho = null;


        if(!Objects.isNull(carrinho) && !Objects.isNull(produto)){
            for(ItemCarrinho produtoCar : carrinho.getProdutos()){
                if(Objects.equals(produtoCar.getProduto().getSku(), produto.getSku())){
                    itemCarrinho = produtoCar;
                }
            }

            if(carrinho.getProdutos().contains(itemCarrinho)){
                int quantidade = carrinho.getProdutos().get(carrinho.getProdutos().indexOf(itemCarrinho)).getQuantidade();
                carrinho.getProdutos().get(carrinho.getProdutos().indexOf(itemCarrinho)).setQuantidade(quantidade + 1);
                produtoCarrinhoRepository.save(itemCarrinho);
            } else {
                ItemCarrinho novoItemCarrinho = new ItemCarrinho(null, produto, 1);
                produtoCarrinhoRepository.save(novoItemCarrinho);
                carrinho.getProdutos().add(novoItemCarrinho);
            }

            carrinhoRepository.save(carrinho);
        } else {
            this.create(idCliente);
            this.adicionarProduto(idCliente, idProduto);
        }
    }


}
