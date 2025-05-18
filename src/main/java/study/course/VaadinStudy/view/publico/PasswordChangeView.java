package study.course.VaadinStudy.view.publico;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import study.course.VaadinStudy.entities.PasswordRecoveryToken;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.services.PasswordRecoveryTokenServices;
import study.course.VaadinStudy.services.UsuarioService;

import java.util.UUID;

@AnonymousAllowed
@Route("recovery")
public class PasswordChangeView extends VerticalLayout implements HasUrlParameter<String> {

    private String uuid;

    private final PasswordRecoveryTokenServices passwordRecoveryTokenServices;
    private final UsuarioService usuarioService;

    private final PasswordField passwordField = new PasswordField("Nova Senha");
    private final PasswordField confirmPasswordField = new PasswordField("Confirmar Senha");
    private final Button submitButton = new Button("Alterar Senha");

    public PasswordChangeView(PasswordRecoveryTokenServices passwordRecoveryTokenServices, UsuarioService usuarioService) {
        this.passwordRecoveryTokenServices = passwordRecoveryTokenServices;
        this.usuarioService = usuarioService;

        setAlignItems(Alignment.CENTER);
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);

        H1 title = new H1("Recuperação de Senha");

        passwordField.setRequired(true);
        confirmPasswordField.setRequired(true);

        submitButton.addClickListener(e -> handleSubmit());

        add(title, passwordField, confirmPasswordField, submitButton);
    }

    private void handleSubmit() {
        String password = passwordField.getValue();
        String confirmPassword = confirmPasswordField.getValue();

        if (password.isEmpty() || confirmPassword.isEmpty()) {
            Notification.show("Todos os campos são obrigatórios.", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            Notification.show("As senhas não coincidem.", 3000, Notification.Position.MIDDLE);
            return;
        }

        if (password.length() < 6) {
            Notification.show("A senha deve ter pelo menos 6 caracteres.", 3000, Notification.Position.MIDDLE);
            return;
        }

        if(passwordRecoveryTokenServices.validateToken(UUID.fromString(uuid))){
            PasswordRecoveryToken passwordRecoveryToken = passwordRecoveryTokenServices.getToken(UUID.fromString(uuid));
            Usuario usuario = passwordRecoveryToken.getUsuario();
            passwordRecoveryToken.setActive(false);
            passwordRecoveryTokenServices.save(passwordRecoveryToken);
            usuarioService.save(usuario);
            Notification.show("Senha alterada com sucesso!", 3000, Notification.Position.MIDDLE);
            UI.getCurrent().navigate(MainView.class);
        } else {
            Notification.show("Link inválido");
        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String uuid) {
        if(uuid != null){
            this.uuid = uuid;
        }
    }
}
