package study.course.VaadinStudy.view.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.stereotype.Component;
import study.course.VaadinStudy.constants.Role;
import study.course.VaadinStudy.entities.Cliente;
import study.course.VaadinStudy.services.ClienteService;
import study.course.VaadinStudy.view.publico.LoginView;

import java.util.ArrayList;
import java.util.List;

@Component
public class RegisterForm extends Composite<Div> implements HasElement {

    protected final ClienteService clienteService;

    public RegisterForm(ClienteService clienteService){
        //Injeção de dependencia
        this.clienteService = clienteService;

        //declaração dos elementos
        var form = new VerticalLayout();
        var nomeInput = new TextField("Nome completo");
        var emailInput = new TextField("Email");
        var senhaInput = new PasswordField("Senha");
        var confirmaSenhaInput = new PasswordField("Confirmação da senha");

        var botaoEnviar = new Button("Enviar", clique -> {
            var nome = nomeInput.getValue();
            var email = emailInput.getValue();
            var senha = senhaInput.getValue();
            var confirmaSenha = confirmaSenhaInput.getValue();

            boolean inputValido = validarInput(nome, email, senha, confirmaSenha);

            if(inputValido){
                registrar(nome, email, senha);
            }
        });

        //Estilos
        getContent().getStyle().set("background-color", "#f0f0f0").set("width", "400px");
        form.setSizeFull();
        nomeInput.setWidthFull();
        emailInput.setWidthFull();
        senhaInput.setWidthFull();
        confirmaSenhaInput.setWidthFull();

        // Adição do conteudo na tela
        form.add(nomeInput, emailInput, senhaInput, confirmaSenhaInput, botaoEnviar);
        getContent().add(form);
    }

    public boolean validarInput(String nome, String email, String senha, String confirmaSenha) {
        List<String> mensagensErro = new ArrayList<>();

        if (nome.isBlank()) mensagensErro.add("Campo nome é obrigatório");
        if (email.isBlank()) mensagensErro.add("Campo email é obrigatório");
        if (senha.isEmpty()) mensagensErro.add("Campo senha é obrigatório");
        if (confirmaSenha.isEmpty()) mensagensErro.add("Campo de confirmação de senha é obrigatório");
        if (!senha.equals(confirmaSenha)) mensagensErro.add("As senhas não são iguais");

        mensagensErro.forEach(Notification::show);
        return mensagensErro.isEmpty();
    }

    public void registrar(String nome, String email, String senha){
        Cliente cliente = new Cliente(null, nome, email, senha, Role.USER);
        boolean foiCadastrado = clienteService.create(cliente);
        if(foiCadastrado){
            UI.getCurrent().navigate(LoginView.class);
            Notification.show("Registrado com sucesso");
        } else {
            Notification.show("Já existe um cadastro com esse email");
        }
    }
}
