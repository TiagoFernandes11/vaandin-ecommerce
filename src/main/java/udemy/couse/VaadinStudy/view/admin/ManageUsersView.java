package udemy.couse.VaadinStudy.view.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import udemy.couse.VaadinStudy.entities.Usuario;
import udemy.couse.VaadinStudy.services.UsuarioService;

@RolesAllowed("ADMIN")
@PageTitle("Manage users")
@Route(value = "/admin/users", layout = AdminLayout.class)
public class ManageUsersView extends VerticalLayout {

    private final UsuarioService usuarioService;
    private Grid<Usuario> listaUsuarios;

    public ManageUsersView(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
        var gridTitle = new H3("Lista de usuários ");
        var criarUsuarioButton = new Button("Criar novo usuario", event -> {
            abrirFormUsuario(new Usuario());
        });
        listaUsuarios = new Grid<>(Usuario.class, false);

        listaUsuarios.addColumn(Usuario::getId).setHeader("Id").setSortable(true);
        listaUsuarios.addColumn(Usuario::getNomeCompleto).setHeader("Nome").setSortable(true);
        listaUsuarios.addColumn(Usuario::getEmail).setHeader("Email").setSortable(true);
        listaUsuarios.addComponentColumn(usuario -> {
            Button editarButton = new Button("Editar", event -> {
                abrirFormUsuario(usuario);
            });
            Button deletarButton = new Button("Deletar", event -> {
                abrirDialogoConfirmacaoExclusao(usuario);
            });
            return new HorizontalLayout(editarButton, deletarButton);
        });

        listaUsuarios.setHeight(LumoUtility.Height.AUTO);

        listaUsuarios.setMultiSort(true);

        atualizarLista();
        add(gridTitle, criarUsuarioButton, listaUsuarios);
    }

    private void abrirFormUsuario(Usuario usuario){
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edição usuário");

        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        var inputNome = new TextField("Nome completo: ");
        var inputEmail = new TextField("Email: ");
        var inputSenha = new PasswordField("Senha: ");
        inputNome.setValue(usuario.getNomeCompleto() != null ? usuario.getNomeCompleto() : "");
        inputEmail.setValue(usuario.getEmail() != null ? usuario.getEmail() : "");

        if(!inputNome.getValue().isBlank() && !inputEmail.getValue().isBlank()){
            verticalLayout.add(inputNome, inputEmail, horizontalLayout);
        } else {
            verticalLayout.add(inputNome, inputEmail, inputSenha, horizontalLayout);
        }

        horizontalLayout.add(new Button("Salvar", event -> {
            usuario.setNomeCompleto(inputNome.getValue());
            usuario.setEmail(inputEmail.getValue());
            if(usuario.getNomeCompleto().isBlank() && usuario.getEmail().isBlank()){
                usuario.setSenha(inputSenha.getValue());
            }
            if(validarInput(usuario)){
                if(usuarioService.update(usuario)){
                    Notification.show("Usuario " + usuario.getId() + " salvo");
                    atualizarLista();
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
            atualizarLista();
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

    private void atualizarLista(){
        listaUsuarios.setItems(usuarioService.findAll());
    }
}
