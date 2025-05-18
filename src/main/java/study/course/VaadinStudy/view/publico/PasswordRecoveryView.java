package study.course.VaadinStudy.view.publico;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import study.course.VaadinStudy.entities.PasswordRecoveryToken;
import study.course.VaadinStudy.entities.Usuario;
import study.course.VaadinStudy.services.EmailService;
import study.course.VaadinStudy.services.PasswordRecoveryTokenServices;
import study.course.VaadinStudy.services.UsuarioService;

@AnonymousAllowed
@Route("/reset")
public class PasswordRecoveryView extends VerticalLayout {

    private final UsuarioService usuarioService;
    private final PasswordRecoveryTokenServices passwordRecoveryTokenServices;
    private final EmailService emailService;

    private final EmailField emailField = new EmailField("Seu e-mail");
    private final Button sendButton = new Button("Enviar link de recuperação");

    public PasswordRecoveryView(UsuarioService usuarioService, PasswordRecoveryTokenServices passwordRecoveryTokenServices, EmailService emailService) {
        this.usuarioService = usuarioService;
        this.passwordRecoveryTokenServices = passwordRecoveryTokenServices;
        this.emailService = emailService;

        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        setSizeFull();


        H1 title = new H1("Recuperar Senha");

        emailField.setPlaceholder("exemplo@dominio.com");
        emailField.setClearButtonVisible(true);
        emailField.setErrorMessage("E-mail inválido");
        emailField.setRequiredIndicatorVisible(true);

        sendButton.addClickListener(e -> handleSend());

        add(title, emailField, sendButton);
    }

    private void handleSend() {
        String email = emailField.getValue();
        Usuario usuario = usuarioService.find(email);

        if (email == null || email.isBlank() || usuario == null) {
            Notification.show("Informe um e-mail válido.", 3000, Notification.Position.MIDDLE);
            return;
        }

        PasswordRecoveryToken passwordRecoveryToken = passwordRecoveryTokenServices.createToken(usuario);
        emailService.enviarEmail("Recuperação de senha",
                "Use o link para alterar sua senha http://localhost:8080/recovery/" + passwordRecoveryToken.getUuid(),
                usuario.getEmail());
        Notification.show("Um link foi enviado ao seu e-mail", 4000, Notification.Position.MIDDLE);
    }
}
