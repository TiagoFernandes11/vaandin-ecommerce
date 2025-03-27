package study.course.VaadinStudy.view.components;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.CarrinhoService;
import study.course.VaadinStudy.services.UsuarioService;
import study.course.VaadinStudy.services.ProdutoService;

import java.util.List;

public class Vitrine extends FlexLayout {
    
    private List<Produto> produtos;
    private final transient AuthenticationContext authenticationContext;

    public Vitrine(AuthenticationContext authenticationContext, ProdutoService produtoService, UsuarioService usuarioService, CarrinhoService carrinhoService) {
        this.authenticationContext = authenticationContext;
        this.produtos = produtoService.findAll();

        setFlexDirection(FlexLayout.FlexDirection.ROW);
        setFlexWrap(FlexLayout.FlexWrap.WRAP);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        for (Produto produto : this.produtos) {
            ProdutoVitrine produtoVitrine = new ProdutoVitrine(produto, this.authenticationContext, usuarioService, carrinhoService);
            produtoVitrine.setWidth("220px");
            produtoVitrine.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.Margin.LARGE);
            add(produtoVitrine);
        }

        addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.START, LumoUtility.FlexWrap.WRAP);
    }
}
