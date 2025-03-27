package study.course.VaadinStudy.view.admin;

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
import study.course.VaadinStudy.constants.Role;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.services.UsuarioService;
import study.course.VaadinStudy.view.components.AdminLayout;

import java.util.Objects;

@RolesAllowed("ROLE_ADMIN")
@PageTitle("Administração usuários")
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
        if(Objects.isNull(usuario.getId())){
            dialog.setHeaderTitle("Novo usuário");
        } else{
            dialog.setHeaderTitle("Edição usuário");
        }

        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        var inputNome = new TextField("Nome completo: ");
        var inputEmail = new TextField("Email: ");
        var inputSenha = new PasswordField("Senha: ");
        var inputConfirmaSenha = new PasswordField("Confirmação senha: ");
        inputNome.setValue(usuario.getNomeCompleto() != null ? usuario.getNomeCompleto() : "");
        inputEmail.setValue(usuario.getEmail() != null ? usuario.getEmail() : "");

        if(!inputNome.getValue().isBlank() && !inputEmail.getValue().isBlank()){
            verticalLayout.add(inputNome, inputEmail, horizontalLayout);
        } else {
            verticalLayout.add(inputNome, inputEmail, inputSenha, inputConfirmaSenha, horizontalLayout);
        }

        horizontalLayout.add(new Button("Salvar", event -> {
            usuario.setNomeCompleto(inputNome.getValue());
            usuario.setEmail(inputEmail.getValue());
            usuario.setRole(Role.ADMIN);

            if(Objects.isNull(usuario.getId())){
                if(inputSenha.getValue().equals(inputConfirmaSenha.getValue())){
                    usuario.setSenha(inputSenha.getValue());
                    if(validarInputComSenha(usuario)){
                        if(usuarioService.create(usuario)){
                            Notification.show("Usuario " + usuario.getEmail() + " criado");
                            atualizarLista();
                            dialog.close();
                        } else {
                            abrirDialogoDeErro("Email ja está em uso por outra conta");
                        }
                    } else{
                        abrirDialogoDeErro("Todos os campos devem ser preenchidos com valores validos");
                    }
                } else{
                    abrirDialogoDeErro("As senhas não são iguais");
                }
            } else{
                if(validarInput(usuario)){
                    if(usuarioService.update(usuario)){
                        Notification.show("Usuario id: " + usuario.getId() + " atualizado");
                        atualizarLista();
                        dialog.close();
                    } else {
                        abrirDialogoDeErro("Email ja cadastrado");
                    }
                } else {
                    abrirDialogoDeErro("Todos os campos devem ser preenchidos com valores validos");
                }
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

    private boolean validarInputComSenha(Usuario usuario){
        return (!usuario.getEmail().isBlank() || !usuario.getNomeCompleto().isBlank()) && usuario.getEmail().contains("@") && !usuario.getSenha().isBlank();
    }

    private boolean validarInput(Usuario usuario){
        return (!usuario.getEmail().isBlank() || !usuario.getNomeCompleto().isBlank()) && usuario.getEmail().contains("@");
    }

    private void atualizarLista(){
        listaUsuarios.setItems(usuarioService.findAll()).addFilter(cliente -> {
            return Objects.equals(cliente.getRole(), "ROLE_USER");
        });
    }
}
