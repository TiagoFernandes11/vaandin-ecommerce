package study.course.VaadinStudy.view.publico;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.PedidoService;
import study.course.VaadinStudy.services.UsuarioService;
import study.course.VaadinStudy.services.ProdutoService;
import study.course.VaadinStudy.view.components.MainLayout;
import study.course.VaadinStudy.view.components.Vitrine;

import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    private final transient AuthenticationContext authenticationContext;

    public MainView(AuthenticationContext authenticationContext, ProdutoService produtoService, UsuarioService usuarioService, PedidoService pedidoService) {
        this.authenticationContext = authenticationContext;
        List<Produto> produtos = produtoService.findAll();
        Vitrine vitrine = new Vitrine(this.authenticationContext, produtos, usuarioService, pedidoService);
        add(vitrine);
    }
}
