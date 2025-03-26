package study.couse.VaadinStudy.view.components;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;

public class LoginForm extends Composite<Div> implements HasElement {

    public LoginForm(){
        var form = new VerticalLayout();
        var emailInput = new TextField("Insira seu e-mail");
        var senhaInput = new PasswordField("insira sua senha");
        var submitButton = new Button("submit", event -> {
            var email = emailInput.getValue();
            var senha = senhaInput.getValue();
            fazerLogin(email, senha);
        });

        getContent().getStyle().set("background-color", "#f0f0f0");

        form.add(emailInput, senhaInput, submitButton);
        getContent().add(form);
    }

    private void fazerLogin(String email, String senha){
        Notification.show("Email: " + email + " Senha: " + senha);
    }
}
