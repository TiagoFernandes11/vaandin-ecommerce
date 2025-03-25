package udemy.couse.VaadinStudy.view.publico;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import udemy.couse.VaadinStudy.services.CarrinhoService;
import udemy.couse.VaadinStudy.services.ClienteService;
import udemy.couse.VaadinStudy.services.ProdutoService;
import udemy.couse.VaadinStudy.view.components.MainLayout;
import udemy.couse.VaadinStudy.view.components.Vitrine;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

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
