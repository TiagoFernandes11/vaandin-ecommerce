package study.course.VaadinStudy.view.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import study.course.VaadinStudy.entities.ItemPedido;
import study.course.VaadinStudy.entities.Pedido;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.services.PedidoService;
import study.course.VaadinStudy.services.UsuarioService;

import java.io.ByteArrayInputStream;
import java.util.List;

public class Carrinho extends VerticalLayout {

    private final AuthenticationContext authenticationContext;
    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;

    public Carrinho(AuthenticationContext authenticationContext, UsuarioService usuarioService, PedidoService pedidoService){
        this.authenticationContext= authenticationContext;
        this.usuarioService = usuarioService;
        this.pedidoService = pedidoService;
        renderizarCarrinho();
    }

    private void renderizarCarrinho(){
        removeAll();
        VerticalLayout itensContainer = new VerticalLayout();

        Usuario cliente = usuarioService.find(authenticationContext.getPrincipalName().orElse(null));

        if(pedidoService.exists(cliente.getEmail())){
            Pedido pedido = pedidoService.findCarrinho(cliente.getEmail());
            List<ItemPedido> itens = pedido.getItens();

            for(ItemPedido item : itens){
                HorizontalLayout imagemInfoContainer = new HorizontalLayout();
                VerticalLayout nomeQuantidadeContainer = new VerticalLayout();
                Produto produto = item.getProduto();
                Span nomeProduto = new Span(item.getProduto().getNome());
                Span quantidade = new Span("Quantidade: " + item.getQuantidade());
                Span valor = new Span("R$ %.2f".formatted(item.getSubTotal()));

                StreamResource resource = new StreamResource(produto.getNome(), () -> new ByteArrayInputStream(produto.getImagem()));
                Image imagemProduto = new Image();

                imagemProduto.setWidth("100px");
                imagemProduto.setHeight("100px");

                imagemProduto.setSrc(resource);

                imagemInfoContainer.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER);

                Button adicionarBotao = new Button("Adicionar", event -> {
                    pedidoService.adicionarProduto(cliente.getEmail(), item.getProduto().getId());
                    renderizarCarrinho();
                });
                Button removerBotao = new Button("Remover", event -> {
                    pedidoService.removerProduto(cliente.getEmail(), item.getProduto().getId());
                    renderizarCarrinho();
                });

                nomeQuantidadeContainer.add(nomeProduto, quantidade);
                imagemInfoContainer.add(imagemProduto, removerBotao, nomeQuantidadeContainer, adicionarBotao, valor);
                itensContainer.add(imagemInfoContainer);
            }
            H4 valorTotal = new H4("Valor total: R$ %.2f".formatted(pedido.getTotal()));
            add(itensContainer, valorTotal);
        }
        else{
            add(new H1("Você não tem produtos adicionados no carrinho"));
        }
    }
}
