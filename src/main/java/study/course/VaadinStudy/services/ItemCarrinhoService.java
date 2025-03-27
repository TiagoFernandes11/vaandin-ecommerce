package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Carrinho;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.entities.ItemCarrinho;

import java.util.List;

@Service
public class ItemCarrinhoService {

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private UsuarioService usuarioService;

    public List<ItemCarrinho> findAllItems(String clienteEmail){
        Usuario usuario = usuarioService.find(clienteEmail);
        Carrinho carrinho = carrinhoService.find(usuario.getId());
        return carrinho.getItens();
    }
}
