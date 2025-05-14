package study.course.VaadinStudy.view.publico;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.data.domain.Page;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.CategoriaService;
import study.course.VaadinStudy.services.PedidoService;
import study.course.VaadinStudy.services.UsuarioService;
import study.course.VaadinStudy.services.ProdutoService;
import study.course.VaadinStudy.view.components.MainLayout;
import study.course.VaadinStudy.view.components.Vitrine;

import java.util.List;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@AnonymousAllowed
public class MainView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final int PAGE_SIZE_PRODUTOS = 10;

    private Vitrine vitrine;
    private Integer paginaAtual;
    private final AuthenticationContext authenticationContext;
    private final ProdutoService produtoService;
    private final UsuarioService usuarioService;
    private final PedidoService pedidoService;
    private final CategoriaService categoriaService;


    public MainView(AuthenticationContext authenticationContext, ProdutoService produtoService, UsuarioService usuarioService, PedidoService pedidoService, CategoriaService categoriaService) {
        this.authenticationContext = authenticationContext;
        this.produtoService = produtoService;
        this.usuarioService = usuarioService;
        this.pedidoService= pedidoService;
        this.categoriaService = categoriaService;
    }


    @Override
    public void setParameter(BeforeEvent beforeEvent, @OptionalParameter Integer page) {
        this.paginaAtual = (page == null || page < 1) ? 0 : page;

        int offSet = paginaAtual < 1 ? 0 : (paginaAtual - 1) * PAGE_SIZE_PRODUTOS;
        Page<Produto> produtosPages = this.produtoService.findAll(offSet, PAGE_SIZE_PRODUTOS);

        renderizarMenuEVitrine(produtosPages.getContent());
        renderizarBotoesDePagina(produtosPages);
    }

    private void renderizarBotoesDePagina(Page<Produto> pages) {
        HorizontalLayout botoes = new HorizontalLayout();
        botoes.setWidthFull();
        botoes.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW, LumoUtility.JustifyContent.CENTER);

        Button btnAnterior = new Button("Pagina anterior", event -> {
            UI.getCurrent().navigate(MainView.class, this.paginaAtual - 1);
        });

        btnAnterior.addClassNames(LumoUtility.Width.AUTO);

        Button btnProxima = new Button("Próxima pagina", event -> {
            UI.getCurrent().navigate(MainView.class, this.paginaAtual + 1);
        });

        btnProxima.addClassNames(LumoUtility.Width.AUTO);

        if(pages.hasPrevious()){
            botoes.add(btnAnterior);
        }

        if(pages.hasNext()){
            botoes.add(btnProxima);
        }

        add(botoes);
    }

    private void renderizarMenuEVitrine(List<Produto> produtos){
        removeAll();

        HorizontalLayout menuSuperior = getMenuSuperior(categoriaService);

        menuSuperior.addClassNames(LumoUtility.Width.FULL, LumoUtility.Border.ALL, LumoUtility.Padding.MEDIUM);

        this.vitrine = new Vitrine(authenticationContext, produtos, usuarioService, this.pedidoService);

        getElement().removeAttribute("theme");
        add(menuSuperior, this.vitrine);
    }

    private HorizontalLayout getMenuSuperior(CategoriaService categoriaService){
        HorizontalLayout menuSuperior = new HorizontalLayout();
        menuSuperior.addClassNames(LumoUtility.Display.FLEX, LumoUtility.FlexDirection.ROW, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.START);

        TextField buscaInput = new TextField();
        buscaInput.setPlaceholder("O que você procura ?");

        Button buscarBtn = new Button("Buscar", event -> {
            List<Produto> produtos = produtoService.findAllByTermo(buscaInput.getValue());
            renderizarMenuEVitrine(produtos);
        });

        Span textoCategoria = new Span("Categorias: ");

        Button categoriaTodosBtn = new Button("Todos", event -> {
            List<Produto> produtos = produtoService.findAll(0, 10).getContent();
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
