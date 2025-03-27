package study.course.VaadinStudy.view.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.entities.Categoria;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.services.CategoriaService;
import study.course.VaadinStudy.view.components.AdminLayout;
import com.vaadin.flow.component.html.H2;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RolesAllowed("ROLE_ADMIN")
@Route(value = "/admin/categories", layout = AdminLayout.class)
@PageTitle("Manage categorias")
public class ManageCategoriaView extends VerticalLayout {

    private final CategoriaService categoriaService;

    private Grid<Categoria> categoriaGrid;

    public ManageCategoriaView(CategoriaService categoriaService){
        this.categoriaService = categoriaService;
        List<Categoria> categorias = categoriaService.findAll();
        categoriaGrid = new Grid<>(Categoria.class, false);

        var titulo = new H2("Categorias");

        var criarNovaCategoriaBtn = new Button("Criar nova categoria", event -> {
            abrirFormCategoria(new Categoria());
        });

        categoriaGrid.addColumn(Categoria::getId).setHeader("ID");
        categoriaGrid.addColumn(Categoria::getNome).setHeader("Nome");
        categoriaGrid.addComponentColumn(categoria -> {
            var botaoEditar = new Button("Editar Produtos", event -> {

            });
            var botaoEditarNome = new Button("Editar nome", event -> {

            });
            var botaoExcluir = new Button("Excluir", event -> {

            });
            return new HorizontalLayout(botaoEditar, botaoEditarNome, botaoExcluir);
        }).setHeader("Ações");

        atualizarGrid();
        add(titulo, criarNovaCategoriaBtn, categoriaGrid);
    }

    private void abrirFormCategoria(Categoria categoria){
        var dialog = new Dialog();

        var verticalLayout = new VerticalLayout();
        var horizontalLayout = new HorizontalLayout();

        if(Objects.isNull(categoria.getId())){
            dialog.setHeaderTitle("Criar categoria");
        } else{
            dialog.setHeaderTitle("Editar categoria");
        }

        var inputNome = new TextField("Nome da categoria: ");

        verticalLayout.add(inputNome, horizontalLayout);

        horizontalLayout.add(new Button("Salvar", event -> {
            String nome = inputNome.getValue();
            categoria.setNome(nome);

            if(Objects.isNull(categoria.getId())) {
                categoria.setProdutos(new ArrayList<>());
            }
            if(categoriaService.create(categoria)){
                Notification.show("Categoria " + categoria.getNome() + " criada");
                atualizarGrid();
                dialog.close();
            } else{
                abrirDialogoDeErro("Ja existe uma categoria com este nome");
            }
        }), new Button("Cancelar", event -> {
            dialog.close();
        }));

        dialog.add(verticalLayout);
        dialog.open();
    }

    private void abrirDialogoDeErro(String erro){
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Erro");

        VerticalLayout verticalLayout = new VerticalLayout();

        verticalLayout.add(erro);

        verticalLayout.add(new Button("Ok", event -> {
            dialog.close();
        }));

        dialog.add(verticalLayout);
        dialog.open();
    }

    private void abrirDialogoConfirmacaoExclusao(Categoria categoria){
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Confirmar exclusão");

        var verticalLayout = new VerticalLayout();

        var horizontalLayout = new HorizontalLayout();

        var botaoConfirmar = new Button("Confirmar", event -> {
            categoriaService.delete(categoria);
            Notification.show("Usuário " + categoria.getNome() + " removido");
            atualizarGrid();
            dialog.close();
        });

        var botaoCancelar = new Button("Cancelar", event -> {
            dialog.close();
        });

        horizontalLayout.add(botaoConfirmar, botaoCancelar);

        verticalLayout.add("Tem certeza de que quer excluir o usuario ?");
        verticalLayout.add(horizontalLayout);

        dialog.add(verticalLayout);

        dialog.open();
    }

    private void atualizarGrid(){
        this.categoriaGrid.setItems(categoriaService.findAll());
    }
}
