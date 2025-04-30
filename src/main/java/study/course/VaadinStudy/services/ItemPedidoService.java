package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.constants.StatusPedido;
import study.course.VaadinStudy.entities.ItemPedido;
import study.course.VaadinStudy.entities.Pedido;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.repository.ItemPedidoRepository;

import java.util.List;

@Service
public class ItemPedidoService extends BaseService<ItemPedido> {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    public ItemPedidoService(ItemPedidoRepository itemPedidoRepository) {
        super(itemPedidoRepository);
    }

    public List<ItemPedido> findAllItems(String clienteEmail){
        Usuario usuario = usuarioService.find(clienteEmail);
        Pedido pedido = pedidoService.findUltimoPedido(usuario.getId(), StatusPedido.CARRINHO);

        if(pedido != null){
            return pedido.getItens();
        } else {
            return null;
        }
    }
}
