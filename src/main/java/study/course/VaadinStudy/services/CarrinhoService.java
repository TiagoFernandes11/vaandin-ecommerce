package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Carrinho;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.entities.ItemCarrinho;
import study.course.VaadinStudy.repository.CarrinhoRepository;
import study.course.VaadinStudy.repository.ItemCarrinhoRepository;
import study.course.VaadinStudy.repository.ProdutoRepository;
import study.course.VaadinStudy.repository.UsuarioRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CarrinhoService {

    @Autowired
    private ItemCarrinhoRepository itemCarrinhoRepository;

    @Autowired
    private CarrinhoRepository carrinhoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public boolean exists (String email){
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if(!Objects.isNull(usuario)){
            return exists(usuario.getId());
        }
        return false;
    }

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

    public void adicionarProduto(String email, Long idProduto){
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if(!Objects.isNull(usuario)){
            adicionarProduto(usuario.getId(), idProduto);
        }
    }

    public void removerProduto(String email, Long idProduto){
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if(!Objects.isNull(usuario)){
            removerProduto(usuario.getId(), idProduto);
        }
    }

    public void adicionarProduto(Long idCliente, Long idProduto) {
        Carrinho carrinho = carrinhoRepository.findByIdCliente(idCliente).orElse(null);
        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        ItemCarrinho itemCarrinho = null;

        if(!Objects.isNull(carrinho) && !Objects.isNull(produto)){
            for(ItemCarrinho item : carrinho.getItens()){
                if(Objects.equals(item.getProduto().getSku(), produto.getSku())){
                    itemCarrinho = item;
                }
            }

            if(carrinho.getItens().contains(itemCarrinho)){
                ItemCarrinho item = carrinho.getItens().get(carrinho.getItens().indexOf(itemCarrinho));
                int quantidade = item.getQuantidade();
                item.setQuantidade(quantidade + 1);
                item.setSubTotal(item.getProduto().getPreco() * item.getQuantidade());
                itemCarrinhoRepository.save(itemCarrinho);
            } else {
                ItemCarrinho novoItemCarrinho = new ItemCarrinho(null, produto, 1, produto.getPreco());
                itemCarrinhoRepository.save(novoItemCarrinho);
                carrinho.getItens().add(novoItemCarrinho);
            }

            for(ItemCarrinho item : carrinho.getItens()){
                carrinho.setTotal(carrinho.getTotal() + item.getSubTotal());
            }
            carrinhoRepository.save(carrinho);
        } else {
            this.create(idCliente);
            this.adicionarProduto(idCliente, idProduto);
        }
    }

    private void removerProduto(Long idCliente, Long idProduto){
        Carrinho carrinho = carrinhoRepository.findByIdCliente(idCliente).orElse(null);
        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        ItemCarrinho itemCarrinho = null;

        if(!Objects.isNull(carrinho) && !Objects.isNull(produto)){
            for(ItemCarrinho item : carrinho.getItens()){
                if(Objects.equals(item.getProduto().getSku(), produto.getSku())){
                    itemCarrinho = item;
                }
            }

            if(carrinho.getItens().contains(itemCarrinho)){
                ItemCarrinho item = carrinho.getItens().get(carrinho.getItens().indexOf(itemCarrinho));
                if(item.getQuantidade() <= 1){
                    carrinho.getItens().remove(item);
                    carrinhoRepository.save(carrinho);
                    if(carrinho.getItens().isEmpty()){
                        carrinhoRepository.delete(carrinho);
                    }
                    itemCarrinhoRepository.delete(item);
                    return;
                } else{
                    int quantidade = item.getQuantidade();
                    item.setQuantidade(quantidade - 1);
                    item.setSubTotal(item.getProduto().getPreco() * item.getQuantidade());
                    itemCarrinhoRepository.save(itemCarrinho);
                }
            }

            for(ItemCarrinho item : carrinho.getItens()){
                carrinho.setTotal(carrinho.getTotal() + item.getSubTotal());
            }

            carrinhoRepository.save(carrinho);
        }
    }
}
