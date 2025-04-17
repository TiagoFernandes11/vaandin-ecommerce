package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Pedido;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.entities.ItemPedido;

import java.util.List;

@Service
public class ItemPedidoService {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private UsuarioService usuarioService;

    public List<ItemPedido> findAllItems(String clienteEmail){
        Usuario usuario = usuarioService.find(clienteEmail);
        Pedido pedido = pedidoService.find(usuario.getId());
        if(pedido != null){
            return pedido.getItens();
        } else {
            return null;
        }
    }
}
