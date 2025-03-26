package study.couse.VaadinStudy.view.publico;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import study.couse.VaadinStudy.services.CarrinhoService;
import study.couse.VaadinStudy.services.ClienteService;
import study.couse.VaadinStudy.services.ProdutoService;
import study.couse.VaadinStudy.view.components.MainLayout;
import study.couse.VaadinStudy.view.components.Vitrine;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    private final transient AuthenticationContext authenticationContext;

    public MainView(AuthenticationContext authenticationContext, ProdutoService produtoService, ClienteService clienteService, CarrinhoService carrinhoService) {
        this.authenticationContext = authenticationContext;
        Vitrine vitrine = new Vitrine(this.authenticationContext, produtoService, clienteService, carrinhoService);
        add(vitrine);
    }
}
