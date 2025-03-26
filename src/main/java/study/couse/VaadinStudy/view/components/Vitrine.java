package study.couse.VaadinStudy.view.components;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import study.couse.VaadinStudy.entities.Produto;
import study.couse.VaadinStudy.services.CarrinhoService;
import study.couse.VaadinStudy.services.ClienteService;
import study.couse.VaadinStudy.services.ProdutoService;

import java.util.List;

public class Vitrine extends FlexLayout {
    
    private List<Produto> produtos;
    private final transient AuthenticationContext authenticationContext;

    public Vitrine(AuthenticationContext authenticationContext, ProdutoService produtoService, ClienteService clienteService, CarrinhoService carrinhoService) {
        this.authenticationContext = authenticationContext;
        this.produtos = produtoService.findAll();

        setFlexDirection(FlexLayout.FlexDirection.ROW);
        setFlexWrap(FlexLayout.FlexWrap.WRAP);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        for (Produto produto : this.produtos) {
            ProdutoVitrine produtoVitrine = new ProdutoVitrine(produto, this.authenticationContext, clienteService, carrinhoService);
            produtoVitrine.setWidth("220px");
            produtoVitrine.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.Margin.LARGE);
            add(produtoVitrine);
        }

        addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.START, LumoUtility.FlexWrap.WRAP);
    }
}
