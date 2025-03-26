package study.course.VaadinStudy.view.publico;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import study.course.VaadinStudy.services.CarrinhoService;
import study.course.VaadinStudy.services.ClienteService;
import study.course.VaadinStudy.services.ProdutoService;
import study.course.VaadinStudy.view.components.MainLayout;
import study.course.VaadinStudy.view.components.Vitrine;

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
