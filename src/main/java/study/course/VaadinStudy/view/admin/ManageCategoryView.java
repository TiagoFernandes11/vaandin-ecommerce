package study.course.VaadinStudy.view.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.function.ValueProvider;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.entities.Categoria;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.CategoriaService;
import study.course.VaadinStudy.services.ProdutoService;
import study.course.VaadinStudy.view.components.AdminLayout;

import java.io.ByteArrayInputStream;
import java.util.List;

@RolesAllowed("ROLE_ADMIN")
@Route(value = "/admin/categories/category", layout = AdminLayout.class)
public class ManageCategoryView extends VerticalLayout implements HasUrlParameter<Long>, HasDynamicTitle {

    private FlexLayout produtosCategoriaLayout;
    private Grid<Produto> produtosParaAdicionarGrid;
    private Long idCategoria;
    private Categoria categoria;
    private final CategoriaService categoriaService;
    private final ProdutoService produtoService;

    public ManageCategoryView(CategoriaService categoriaService, ProdutoService produtoService) {
        this.categoriaService = categoriaService;
        this.produtoService = produtoService;
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Long idCategoria) {
        this.idCategoria = idCategoria;
        categoria = categoriaService.find(idCategoria);
        carregarProdutosECriarLayout();
    }

    private void carregarProdutosECriarLayout(){
        H2 titulo = new H2("Produtos da categoria: " + categoria.getNome());

        Dialog addProdutosDialog = createAddProdutoDialog(categoria);

        var adicionarProdutoButton = new Button("Adicionar produtos", event -> {
            addProdutosDialog.open();
        });

        produtosCategoriaLayout = getListaDeProdutos();

        add(titulo, adicionarProdutoButton, produtosCategoriaLayout);
    }

    private Dialog createAddProdutoDialog(Categoria categoria){
        Dialog addProdutosDialog = new Dialog();;

        H2 tituloDialog = new H2("Adicionando produtos a " + categoria.getNome());
        tituloDialog.addClassNames(LumoUtility.Padding.MEDIUM);

        produtosParaAdicionarGrid = new Grid<>(Produto.class,false);
        produtosParaAdicionarGrid.addColumn(Produto::getId).setHeader("ID");
        produtosParaAdicionarGrid.addComponentColumn((ValueProvider<Produto, Component>) produto -> {
            StreamResource resource = new StreamResource(produto.getNome(), () -> new ByteArrayInputStream(produto.getImagem()));
            Image image = new Image();
            image.setHeight("100px");
            image.setWidth("100px");
            image.setSrc(resource);
            return image;
        }).setHeader("Imagem");
        produtosParaAdicionarGrid.addColumn(Produto::getNome).setHeader("Nome");
        produtosParaAdicionarGrid.addComponentColumn(produto -> new Button("Adicionar", buttonClickEvent -> {
            categoriaService.adicionarProduto(categoria.getNome(), produto.getId());
        })).setHeader("Ação");

        atualizarProdutosParaAdicionar();

        Button botaoFechar = new Button("Fechar", buttonClickEvent -> {
            addProdutosDialog.close();
        });

        addProdutosDialog.add(tituloDialog, produtosParaAdicionarGrid, botaoFechar);

        return addProdutosDialog;
    }

    private void atualizarProdutosParaAdicionar() {
        produtosParaAdicionarGrid.setItems(produtoService.findAll().stream().filter(produto -> !categoria.getProdutos().contains(produto)).toList());
    }

    private FlexLayout getListaDeProdutos(){
        List<Produto> produtosAdicionados = produtoService.findAll().stream().filter(produto -> categoria.getProdutos().contains(produto)).toList();

        FlexLayout listaDeProdutos = new FlexLayout();
        listaDeProdutos.setFlexDirection(FlexLayout.FlexDirection.ROW);
        listaDeProdutos.setFlexWrap(FlexLayout.FlexWrap.WRAP);
        listaDeProdutos.setAlignItems(Alignment.START);
        listaDeProdutos.setJustifyContentMode(JustifyContentMode.START);

        for(Produto produto : produtosAdicionados){
            VerticalLayout imagemENome = getImagemENome(produto);
            listaDeProdutos.add(imagemENome);
        }

        return listaDeProdutos;
    }

    private static VerticalLayout getImagemENome(Produto produto) {
        StreamResource resource = new StreamResource(produto.getNome(), () -> new ByteArrayInputStream(produto.getImagem()));
        Image imagem = new Image();
        imagem.setSrc(resource);
        imagem.setWidth("200px");
        imagem.setHeight("200px");

        Span nome = new Span(produto.getNome());

        VerticalLayout imagemENome = new VerticalLayout();
        imagemENome.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.Padding.LARGE);
        imagemENome.setWidth("300px");

        imagemENome.add(imagem, nome);
        return imagemENome;
    }

    @Override
    public String getPageTitle() {
        return "Editando categoria: " + this.categoriaService.find(idCategoria).getNome();
    }
}
