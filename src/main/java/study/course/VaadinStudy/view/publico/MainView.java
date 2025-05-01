package study.course.VaadinStudy.view.publico;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import study.course.VaadinStudy.entities.Categoria;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.CategoriaService;
import study.course.VaadinStudy.services.PedidoService;
import study.course.VaadinStudy.services.UsuarioService;
import study.course.VaadinStudy.services.ProdutoService;
import study.course.VaadinStudy.view.components.MainLayout;
import study.course.VaadinStudy.view.components.Vitrine;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    private Vitrine vitrine;
    private AuthenticationContext authenticationContext;
    private ProdutoService produtoService;
    private UsuarioService usuarioService;
    private PedidoService pedidoService;
    private CategoriaService categoriaService;


    public MainView(AuthenticationContext authenticationContext, ProdutoService produtoService, UsuarioService usuarioService, PedidoService pedidoService, CategoriaService categoriaService) {
        this.authenticationContext = authenticationContext;
        this.produtoService = produtoService;
        this.usuarioService = usuarioService;
        this.produtoService = produtoService;
        this.categoriaService = categoriaService;

        List<Produto> produtos = this.produtoService.findAll();

        renderizarMenuEVitrine(produtos);
    }

    private void renderizarMenuEVitrine(List<Produto> produtos){
        removeAll();

        HorizontalLayout menuSuperior = getMenuSuperior(categoriaService);

        menuSuperior.addClassNames(LumoUtility.Width.FULL, LumoUtility.Border.ALL, LumoUtility.Padding.MEDIUM);

        this.vitrine = new Vitrine(authenticationContext, produtos, usuarioService, pedidoService);

        getElement().removeAttribute("theme");
        add(menuSuperior, this.vitrine);
    }

    private HorizontalLayout getMenuSuperior(CategoriaService categoriaService){
        HorizontalLayout menuSuperior = new HorizontalLayout();
        menuSuperior.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.START);

        TextField buscaInput = new TextField();
        buscaInput.setPlaceholder("O que você procura ?");

        Button buscarBtn = new Button("Buscar", event -> {

        });

        Span textoCategoria = new Span("Categorias: ");

        Button categoriaTodosBtn = new Button("Todos", event -> {
            List<Produto> produtos = produtoService.findAll();
            renderizarMenuEVitrine(produtos);
        });

        menuSuperior.add(buscaInput, buscarBtn, textoCategoria, categoriaTodosBtn);

        categoriaService.findAll().forEach(categoria -> {
            Button button = new Button(categoria.getNome(), event -> {
                List<Produto> produtos = produtoService.findAll().stream()
                        .filter(produto -> categoria.getProdutos().contains(produto)).toList();
                renderizarMenuEVitrine(produtos);
            });
            menuSuperior.add(button);
        });

        return menuSuperior;
    }
}
