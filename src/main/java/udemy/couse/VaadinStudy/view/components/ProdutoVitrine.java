package udemy.couse.VaadinStudy.view.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import udemy.couse.VaadinStudy.entities.Produto;

import java.io.ByteArrayInputStream;

public class ProdutoVitrine extends VerticalLayout {

    public ProdutoVitrine(Produto produto){
        StreamResource resource = new StreamResource(produto.getNome(), () -> new ByteArrayInputStream(produto.getImagem()));
        Image imagem = new Image();
        imagem.setSrc(resource);

        imagem.setHeight("200px");
        imagem.setWidth("200px");

        H4 nomeProduto = new H4(produto.getNome());

        HorizontalLayout precoEBotao = new HorizontalLayout();

        Text preco = new Text("R$ " + produto.getPreco().toString());
        Button adicionarCarrinhoButton = new Button("Comprar", event -> {

        });

        precoEBotao.add(preco, adicionarCarrinhoButton);
        precoEBotao.setWidth(imagem.getWidth());
        precoEBotao.setAlignItems(Alignment.CENTER);
        precoEBotao.setJustifyContentMode(JustifyContentMode.BETWEEN);

        add(imagem, nomeProduto, precoEBotao);
    }
}
