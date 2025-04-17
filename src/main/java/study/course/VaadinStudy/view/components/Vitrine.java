package study.course.VaadinStudy.view.components;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.PedidoService;
import study.course.VaadinStudy.services.UsuarioService;
import study.course.VaadinStudy.services.ProdutoService;

import java.util.List;

public class Vitrine extends FlexLayout {

    public Vitrine(AuthenticationContext authenticationContext, List<Produto> produtos, UsuarioService usuarioService, PedidoService pedidoService) {
        setFlexDirection(FlexLayout.FlexDirection.ROW);
        setFlexWrap(FlexLayout.FlexWrap.WRAP);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        for (Produto produto : produtos) {
            ProdutoVitrine produtoVitrine = new ProdutoVitrine(produto, authenticationContext, usuarioService, pedidoService);
            produtoVitrine.setWidth("220px");
            produtoVitrine.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.Margin.LARGE);
            add(produtoVitrine);
        }

        addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.START, LumoUtility.FlexWrap.WRAP);
    }
}
