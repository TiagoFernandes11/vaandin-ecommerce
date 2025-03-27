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
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.constants.Role;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.services.UsuarioService;
import study.course.VaadinStudy.view.components.AdminLayout;

import java.util.Objects;

@RolesAllowed("ROLE_ADMIN")
@PageTitle("Administração administradores")
@Route(value = "/admin/administrators", layout = AdminLayout.class)
public class ManageAdministradorView extends VerticalLayout {

    private UsuarioService usuarioService;
    private Grid<Usuario> listaAdministradores;

    public ManageAdministradorView(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
        var titulo = new H3("Lista de administradores");
        var adicionarProdutoButton = new Button("Criar novo administrador", event -> {
            abrirFormAdmin(new Usuario());
        });

        listaAdministradores = new Grid<>(Usuario.class, false);

        listaAdministradores.addColumn(Usuario::getId).setHeader("ID");
        listaAdministradores.addColumn(Usuario::getEmail).setHeader("Email");
        listaAdministradores.addColumn(Usuario::getNomeCompleto).setHeader("Nome");
        listaAdministradores.addComponentColumn(cliente -> {
            Button editarProduto = new Button("Editar", event -> {
                abrirFormAdmin(cliente);
            });
            Button excluirProduto = new Button("Excluir", event -> {
                abrirDialogoConfirmacaoExclusao(cliente);
            });
            return new HorizontalLayout(editarProduto, excluirProduto);
        });

        atualizarLista();
        add(titulo, adicionarProdutoButton, listaAdministradores);
    }

    public void abrirFormAdmin(Usuario usuario){
        Dialog dialog = new Dialog();
        if(Objects.isNull(usuario.getId())){
            dialog.setHeaderTitle("Novo administrador");
        } else{
            dialog.setHeaderTitle("Edição administrador");
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
                            Notification.show("administrador " + usuario.getEmail() + " criado");
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
                        Notification.show("Administrador id: " + usuario.getId() + " atualizado");
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

    private void abrirDialogoConfirmacaoExclusao(Usuario usuario){
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Confirmar exclusão");

        var verticalLayout = new VerticalLayout();

        var horizontalLayout = new HorizontalLayout();

        var botaoConfirmar = new Button("Confirmar", event -> {
            usuarioService.delete(usuario);
            Notification.show("Produto: " + usuario.getEmail() + " removido");
            dialog.close();
            atualizarLista();
        });

        var botaoCancelar = new Button("Cancelar", event -> {
            dialog.close();
        });

        horizontalLayout.add(botaoConfirmar, botaoCancelar);

        verticalLayout.add("Tem certeza de que quer excluir o administrador ?");
        verticalLayout.add(horizontalLayout);

        dialog.add(verticalLayout);

        dialog.open();
    }

    private void atualizarLista(){
        this.listaAdministradores.setItems(usuarioService.findAll()).addFilter(cliente -> {
            return cliente.getRole().equals("ROLE_ADMIN");
        });
    }

    private boolean validarInputComSenha(Usuario usuario){
        return (!usuario.getEmail().isBlank() || !usuario.getNomeCompleto().isBlank()) && usuario.getEmail().contains("@") && !usuario.getSenha().isBlank();
    }

    private boolean validarInput(Usuario usuario){
        return (!usuario.getEmail().isBlank() || !usuario.getNomeCompleto().isBlank()) && usuario.getEmail().contains("@");
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
}
