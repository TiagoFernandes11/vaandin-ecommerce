package study.course.VaadinStudy.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import study.course.VaadinStudy.entities.Carrinho;
import study.course.VaadinStudy.entities.Cliente;
import study.course.VaadinStudy.entities.ItemCarrinho;

import java.util.List;

@Service
public class ItemCarrinhoService {

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private ClienteService clienteService;

    public List<ItemCarrinho> findAllItems(String clienteEmail){
        Cliente cliente = clienteService.find(clienteEmail);
        Carrinho carrinho = carrinhoService.find(cliente.getId());
        return carrinho.getItens();
    }
}
