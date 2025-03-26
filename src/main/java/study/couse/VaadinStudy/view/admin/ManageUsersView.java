package study.couse.VaadinStudy.view.admin;

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
import study.couse.VaadinStudy.entities.Cliente;
import study.couse.VaadinStudy.services.ClienteService;
import study.couse.VaadinStudy.view.components.AdminLayout;

@RolesAllowed("ROLE_ADMIN")
@PageTitle("Administração usuários")
@Route(value = "/admin/users", layout = AdminLayout.class)
public class ManageUsersView extends VerticalLayout {

    private final ClienteService clienteService;
    private Grid<Cliente> listaUsuarios;

    public ManageUsersView(ClienteService clienteService){
        this.clienteService = clienteService;
        var gridTitle = new H3("Lista de usuários ");
        var criarUsuarioButton = new Button("Criar novo usuario", event -> {
            abrirFormUsuario(new Cliente());
        });
        listaUsuarios = new Grid<>(Cliente.class, false);

        listaUsuarios.addColumn(Cliente::getId).setHeader("Id").setSortable(true);
        listaUsuarios.addColumn(Cliente::getNomeCompleto).setHeader("Nome").setSortable(true);
        listaUsuarios.addColumn(Cliente::getEmail).setHeader("Email").setSortable(true);
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

    private void abrirFormUsuario(Cliente cliente){
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Edição usuário");

        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();

        var inputNome = new TextField("Nome completo: ");
        var inputEmail = new TextField("Email: ");
        var inputSenha = new PasswordField("Senha: ");
        inputNome.setValue(cliente.getNomeCompleto() != null ? cliente.getNomeCompleto() : "");
        inputEmail.setValue(cliente.getEmail() != null ? cliente.getEmail() : "");

        if(!inputNome.getValue().isBlank() && !inputEmail.getValue().isBlank()){
            verticalLayout.add(inputNome, inputEmail, horizontalLayout);
        } else {
            verticalLayout.add(inputNome, inputEmail, inputSenha, horizontalLayout);
        }

        horizontalLayout.add(new Button("Salvar", event -> {
            cliente.setNomeCompleto(inputNome.getValue());
            cliente.setEmail(inputEmail.getValue());
            if(cliente.getNomeCompleto().isBlank() && cliente.getEmail().isBlank()){
                cliente.setSenha(inputSenha.getValue());
            }
            if(validarInput(cliente)){
                if(clienteService.update(cliente)){
                    Notification.show("Usuario " + cliente.getId() + " salvo");
                    atualizarLista();
                    dialog.close();
                } else {
                    abrirDialogoDeErro("O email " + cliente.getEmail() + " já está cadastrado");
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

    private void abrirDialogoConfirmacaoExclusao(Cliente cliente){
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Confirmar exclusão");

        var verticalLayout = new VerticalLayout();

        var horizontalLayout = new HorizontalLayout();

        var botaoConfirmar = new Button("Confirmar", event -> {
            clienteService.delete(cliente);
            Notification.show("Usuário " + cliente.getEmail() + " removido");
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

    private boolean validarInput(Cliente cliente){
        if((!cliente.getEmail().isBlank() || !cliente.getNomeCompleto().isBlank()) && cliente.getEmail().contains("@")){
            return true;
        } else {
            return false;
        }
    }

    private void atualizarLista(){
        listaUsuarios.setItems(clienteService.findAll());
    }
}
