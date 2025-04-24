package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.constants.StatusPedido;
import study.course.VaadinStudy.entities.Pedido;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.entities.ItemPedido;
import study.course.VaadinStudy.repository.PedidoRepository;
import study.course.VaadinStudy.repository.ItemPedidoRepository;
import study.course.VaadinStudy.repository.ProdutoRepository;
import study.course.VaadinStudy.repository.UsuarioRepository;

import java.util.*;

@Service
public class PedidoService {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    public void create(Long idCliente){
        List<ItemPedido> produtos = new ArrayList<>();
        Pedido pedido = new Pedido(null, idCliente, StatusPedido.CARRINHO, new Date(), false,
                null, null, null, null, produtos, 0.00);
        pedidoRepository.save(pedido);
    }

    public boolean exists (String email){
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if(!Objects.isNull(usuario)){
            return exists(usuario.getId());
        }
        return false;
    }

    public boolean exists(Long idCliente){
        List<Pedido> pedidos = pedidoRepository.findAllByIdCliente(idCliente).orElse(null);
        if (!Objects.isNull(pedidos)){
            return pedidos.getLast().getStatus().equals(StatusPedido.CARRINHO);
        }
        return false;
    }

    public Pedido findUltimoPedido(String emailCliente, String statusPedido){
        Usuario usuario = usuarioRepository.findByEmail(emailCliente).orElse(null);
        if(!Objects.isNull(usuario)){
            return findUltimoPedido(usuario.getId(), statusPedido);
        }
        return null;
    }

    public Pedido findUltimoPedido(Long idCliente, String statusPedido){
        List<Pedido> pedidos = pedidoRepository.findAllByIdCliente(idCliente).orElse(null);
        Pedido ultimoCarrinho = null;
        if(Objects.nonNull(pedidos) && !pedidos.isEmpty()){
            ultimoCarrinho = pedidos.getLast();
        }
        if(!Objects.isNull(ultimoCarrinho) && ultimoCarrinho.getStatus().equals(statusPedido)){
            return ultimoCarrinho;
        }
        return null;
    }

    public List<Pedido> findAll(){return pedidoRepository.findAll();}

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
        Pedido pedido = findUltimoPedido(idCliente, StatusPedido.CARRINHO);
        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        ItemPedido itemPedido = null;

        if(!Objects.isNull(pedido) && !Objects.isNull(produto)){
            for(ItemPedido item : pedido.getItens()){
                if(Objects.equals(item.getProduto().getSku(), produto.getSku())){
                    itemPedido = item;
                }
            }

            if(pedido.getItens().contains(itemPedido)){
                ItemPedido item = pedido.getItens().get(pedido.getItens().indexOf(itemPedido));
                int quantidade = item.getQuantidade();
                item.setQuantidade(quantidade + 1);
                item.setSubTotal(item.getProduto().getPreco() * item.getQuantidade());
                itemPedidoRepository.save(itemPedido);
            } else {
                ItemPedido novoItemPedido = new ItemPedido(null, produto, 1, produto.getPreco());
                itemPedidoRepository.save(novoItemPedido);
                pedido.getItens().add(novoItemPedido);
            }

            pedido.setTotal(0.0);

            for(ItemPedido item : pedido.getItens()){
                pedido.setTotal(pedido.getTotal() + item.getSubTotal());
            }
            pedidoRepository.save(pedido);
        } else {
            this.create(idCliente);
            this.adicionarProduto(idCliente, idProduto);
        }
    }

    private void removerProduto(Long idCliente, Long idProduto){
        Pedido pedido = findUltimoPedido(idCliente, StatusPedido.CARRINHO);
        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        ItemPedido itemPedido = null;

        if(!Objects.isNull(pedido) && !Objects.isNull(produto)){
            for(ItemPedido item : pedido.getItens()){
                if(Objects.equals(item.getProduto().getSku(), produto.getSku())){
                    itemPedido = item;
                }
            }

            if(pedido.getItens().contains(itemPedido)){
                ItemPedido item = pedido.getItens().get(pedido.getItens().indexOf(itemPedido));
                if(item.getQuantidade() <= 1){
                    pedido.getItens().remove(item);
                    pedidoRepository.save(pedido);
                    if(pedido.getItens().isEmpty()){
                        pedidoRepository.delete(pedido);
                    }
                    calcularTotalPedido(pedido);
                    pedidoRepository.save(pedido);
                    itemPedidoRepository.delete(item);
                    return;
                } else{
                    int quantidade = item.getQuantidade();
                    item.setQuantidade(quantidade - 1);
                    item.setSubTotal(item.getProduto().getPreco() * item.getQuantidade());
                    itemPedidoRepository.save(itemPedido);
                }
            }
            calcularTotalPedido(pedido);
            pedidoRepository.save(pedido);
        }
    }

    private void calcularTotalPedido(Pedido pedido){
        pedido.setTotal(0.0);

        for(ItemPedido item : pedido.getItens()){
            pedido.setTotal(pedido.getTotal() + item.getSubTotal());
        }
    }

    public void save(Pedido pedido) {
        pedidoRepository.save(pedido);
    }
}
