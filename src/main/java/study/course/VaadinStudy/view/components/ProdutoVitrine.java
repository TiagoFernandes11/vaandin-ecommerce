package study.course.VaadinStudy.view.components;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.security.AuthenticationContext;
import org.springframework.beans.factory.annotation.Autowired;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.CarrinhoService;
import study.course.VaadinStudy.services.UsuarioService;
import study.course.VaadinStudy.view.publico.LoginView;

import java.io.ByteArrayInputStream;
import java.util.Objects;

public class ProdutoVitrine extends VerticalLayout {

    private final transient AuthenticationContext authenticationContext;

    @Autowired
    private CarrinhoService carrinhoService;

    @Autowired
    private UsuarioService usuarioService;

    public ProdutoVitrine(Produto produto, AuthenticationContext authenticationContext, UsuarioService usuarioService, CarrinhoService carrinhoService){
        this.authenticationContext = authenticationContext;
        StreamResource resource = new StreamResource(produto.getNome(), () -> new ByteArrayInputStream(produto.getImagem()));
        Image imagem = new Image();
        imagem.setSrc(resource);

        imagem.setHeight("200px");
        imagem.setWidth("200px");

        H4 nomeProduto = new H4(produto.getNome());

        HorizontalLayout precoEBotao = new HorizontalLayout();

        Text preco = new Text("R$ " + produto.getPreco().toString());
        Button adicionarCarrinhoButton = new Button("Comprar", event -> {
            Usuario usuario = usuarioService.find(this.authenticationContext.getPrincipalName().orElse(null));
            if(!Objects.isNull(usuario)){
                carrinhoService.adicionarProduto(usuario.getId(), produto.getId());
            } else {
                UI.getCurrent().navigate(LoginView.class);
            }
        });

        precoEBotao.add(preco, adicionarCarrinhoButton);
        precoEBotao.setWidth(imagem.getWidth());
        precoEBotao.setAlignItems(Alignment.CENTER);
        precoEBotao.setJustifyContentMode(JustifyContentMode.BETWEEN);

        add(imagem, nomeProduto, precoEBotao);
    }
}
