package study.course.VaadinStudy.view.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import study.course.VaadinStudy.entities.ItemCarrinho;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.CarrinhoService;
import study.course.VaadinStudy.services.ItemCarrinhoService;

import java.io.ByteArrayInputStream;
import java.util.List;

public class Carrinho extends VerticalLayout {

    public Carrinho(AuthenticationContext authenticationContext, ItemCarrinhoService itemCarrinhoService, CarrinhoService carrinhoService){
        H2 titulo = new H2("Carrinho");
        VerticalLayout itensContainer = new VerticalLayout();

        String emailCliente = authenticationContext.getPrincipalName().orElse(null);

        if(carrinhoService.exists(emailCliente)){
            List<ItemCarrinho> itens = itemCarrinhoService.findAllItems(emailCliente);

            for(ItemCarrinho item : itens){
                HorizontalLayout imagemInfoContainer = new HorizontalLayout();
                VerticalLayout nomeQuantidadeContainer = new VerticalLayout();
                Produto produto = item.getProduto();
                Span nomeProduto = new Span(item.getProduto().getNome());
                Span quantidade = new Span("Quantidade: " + item.getQuantidade());
                Span valor = new Span("R$ " + item.getSubTotal());

                StreamResource resource = new StreamResource(produto.getNome(), () -> new ByteArrayInputStream(produto.getImagem()));
                Image imagemProduto = new Image();

                imagemProduto.setWidth("100px");
                imagemProduto.setHeight("100px");

                imagemProduto.setSrc(resource);

                imagemInfoContainer.addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER);

                Button adicionarBotao = new Button("Adicionar", event -> {
                    carrinhoService.adicionarProduto(emailCliente, item.getProduto().getId());
                });
                Button removerBotao = new Button("Remover", event -> {

                });

                nomeQuantidadeContainer.add(nomeProduto, quantidade);
                imagemInfoContainer.add(imagemProduto, removerBotao, nomeQuantidadeContainer, adicionarBotao, valor);
                itensContainer.add(imagemInfoContainer);
            }
            Button finalizarButton = new Button("Finalizar compra", event -> {
                Notification.show("Finalizando compra");
            });
            add(itensContainer, finalizarButton);
        }
        else{
            add(new H1("Você não tem produtos adicionados no carrinho"));
        }
    }
}
