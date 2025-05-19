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
import study.course.VaadinStudy.services.base.BaseService;

import java.util.*;

@Service
public class PedidoService extends BaseService<Pedido> {

    @Autowired
    private ItemPedidoRepository itemPedidoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public PedidoService(PedidoRepository pedidoRepository) {
        super(pedidoRepository);
    }

    public void create(Long idCliente) {
        List<ItemPedido> produtos = new ArrayList<>();
        Pedido pedido = new Pedido(idCliente, StatusPedido.CARRINHO, new Date(), false,
                null, null, null, null, produtos, 0.00);
        pedidoRepository.save(pedido);
    }

    public boolean exists(String email) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (!Objects.isNull(usuario)) {
            return existsByIdCliente(usuario.getId());
        }
        return false;
    }

    public boolean existsByIdCliente(Long idCliente) {
        List<Pedido> pedidos = pedidoRepository.findAllByIdCliente(idCliente).orElse(null);
        if (Objects.nonNull(pedidos) && !pedidos.isEmpty()) {
            return pedidos.getLast().getStatus().equals(StatusPedido.CARRINHO);
        }
        return false;
    }

    public List<Pedido> findAllByTermoLike(String termo){
        return pedidoRepository.buscarPorTermo(termo);
    }

    public Pedido findUltimoPedido(String emailCliente, String statusPedido) {
        Usuario usuario = usuarioRepository.findByEmail(emailCliente).orElse(null);
        if (Objects.nonNull(usuario)) {
            return findUltimoPedido(usuario.getId(), statusPedido);
        }
        return null;
    }

    public Pedido findUltimoPedido(Long idCliente, String statusPedido) {
        List<Pedido> pedidos = pedidoRepository.findAllByIdCliente(idCliente).orElse(null);
        Pedido ultimoCarrinho = null;
        if (Objects.nonNull(pedidos) && !pedidos.isEmpty()) {
            ultimoCarrinho = pedidos.getLast();
        }
        if (Objects.nonNull(ultimoCarrinho) && ultimoCarrinho.getStatus().equals(statusPedido)) {
            return ultimoCarrinho;
        }
        return null;
    }

    public List<Pedido> findAll() {
        return pedidoRepository.findAll();
    }

    public void adicionarProduto(String email, Long idProduto) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (Objects.nonNull(usuario)) {
            adicionarProduto(usuario.getId(), idProduto);
        }
    }

    public void adicionarProduto(Long idCliente, Long idProduto) {
        Pedido pedido = findUltimoPedido(idCliente, StatusPedido.CARRINHO);
        Produto produto = produtoRepository.findById(idProduto).orElse(null);

        if (Objects.isNull(pedido) || Objects.isNull(produto)) {
            this.create(idCliente);
            this.adicionarProduto(idCliente, idProduto);
        }

        ItemPedido itemPedido = pedido.getItens().stream().filter(item -> Objects.equals(item.getProduto().getSku(), produto.getSku())).findFirst().orElse(null);

        if (itemPedido != null) {
            itemPedido.setQuantidade(itemPedido.getQuantidade() + 1);
            itemPedido.setSubTotal(itemPedido.getProduto().getPreco() * itemPedido.getQuantidade());
            itemPedidoRepository.save(itemPedido);
        } else {
            ItemPedido novoItemPedido = new ItemPedido(produto, 1, produto.getPreco());
            pedido.getItens().add(novoItemPedido);
            itemPedidoRepository.save(novoItemPedido);
        }

        pedido.setTotal(pedido.getItens().stream().mapToDouble(ItemPedido::getSubTotal).sum());
        save(pedido);

    }

    public void removerProduto(String email, Long idProduto) {
        Usuario usuario = usuarioRepository.findByEmail(email).orElse(null);
        if (!Objects.isNull(usuario)) {
            removerProduto(usuario.getId(), idProduto);
        }
    }

    private void removerProduto(Long idCliente, Long idProduto) {
        Pedido pedido = findUltimoPedido(idCliente, StatusPedido.CARRINHO);
        if (Objects.isNull(pedido)) return;

        Produto produto = produtoRepository.findById(idProduto).orElse(null);
        if (Objects.isNull(produto)) return;

        ItemPedido itemPedido = pedido.getItens().stream().filter(item -> Objects.equals(item.getProduto().getSku(), produto.getSku())).findFirst().orElse(null);

        if (Objects.isNull(itemPedido)) return;

        if (itemPedido.getQuantidade() <= 1) {
            pedido.getItens().remove(itemPedido);
            pedidoRepository.save(pedido);

            if (pedido.getItens().isEmpty()) {
                delete(pedido);
            }

            calcularTotalPedido(pedido);
            itemPedidoRepository.delete(itemPedido);
            return;
        } else {
            int quantidade = itemPedido.getQuantidade();
            itemPedido.setQuantidade(quantidade - 1);
            itemPedido.setSubTotal(itemPedido.getProduto().getPreco() * itemPedido.getQuantidade());
            itemPedidoRepository.save(itemPedido);
        }

        calcularTotalPedido(pedido);
        save(pedido);

    }

    private void calcularTotalPedido(Pedido pedido) {
        pedido.setTotal(0.0);

        for (ItemPedido item : pedido.getItens()) {
            pedido.setTotal(pedido.getTotal() + item.getSubTotal());
        }

    }
}
