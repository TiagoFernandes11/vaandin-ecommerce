package udemy.couse.VaadinStudy.view.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import udemy.couse.VaadinStudy.entities.Usuario;
import udemy.couse.VaadinStudy.services.UsuarioService;

@PageTitle("Manage users")
@Route(value = "/admin/users", layout = AdminLayout.class)
public class ManageUsersView extends VerticalLayout {

    private final UsuarioService usuarioService;
    private Grid<Usuario> listaUsuarios;

    public ManageUsersView(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
        var gridTitle = new H3("Lista de usuários ");
        listaUsuarios = new Grid<>(Usuario.class, false);

        listaUsuarios.addColumn(Usuario::getId).setHeader("Id").setSortable(true);
        listaUsuarios.addColumn(Usuario::getNomeCompleto).setHeader("Nome").setSortable(true);
        listaUsuarios.addColumn(Usuario::getEmail).setHeader("Email").setSortable(true);
        listaUsuarios.addComponentColumn(usuario -> {
            Button editarButton = new Button("Editar", event -> {
                abrirDialogoEdicaoUsuario(usuario);
            });
            Button deletarButton = new Button("Deletar", event -> {
                abrirDialogoConfirmacaoExclusao(usuario);
            });
            return new HorizontalLayout(editarButton, deletarButton);
        });

        listaUsuarios.setHeight(LumoUtility.Height.AUTO);

        listaUsuarios.setMultiSort(true);

        atualizarListaUsuarios();
        add(gridTitle, listaUsuarios);
    }

    private void abrirDialogoEdicaoUsuario(Usuario usuario){
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edição usuário");

        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        var inputNome = new TextField("Nome completo: ");
        var inputEmail = new TextField("Email: ");
        inputNome.setValue(usuario.getNomeCompleto());
        inputEmail.setValue(usuario.getEmail());

        verticalLayout.add(inputNome, inputEmail, horizontalLayout);

        horizontalLayout.add(new Button("Salvar", event -> {
            usuario.setNomeCompleto(inputNome.getValue());
            usuario.setEmail(inputEmail.getValue());
            if(validarInput(usuario)){
                if(usuarioService.update(usuario)){
                    Notification.show("Usuario " + usuario.getId() + " atualizado");
                    atualizarListaUsuarios();
                    dialog.close();
                } else {
                    abrirDialogoDeErro("O email " + usuario.getEmail() + " já está cadastrado");
                }
            } else {
                abrirDialogoDeErro("Todos os campos devem ser preenchidos");
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

    private void abrirDialogoConfirmacaoExclusao(Usuario usuario){
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Confirmar exclusão");

        var verticalLayout = new VerticalLayout();

        var horizontalLayout = new HorizontalLayout();

        var botaoConfirmar = new Button("Confirmar", event -> {
            usuarioService.delete(usuario);
            Notification.show("Usuário " + usuario.getEmail() + " removido");
            dialog.close();
            atualizarListaUsuarios();
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

    private boolean validarInput(Usuario usuario){
        if((!usuario.getEmail().isBlank() || !usuario.getNomeCompleto().isBlank()) && usuario.getEmail().contains("@")){
            return true;
        } else {
            return false;
        }
    }

    private void atualizarListaUsuarios(){
        listaUsuarios.setItems(usuarioService.findAll());
    }
}
