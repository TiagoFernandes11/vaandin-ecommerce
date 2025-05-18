package study.course.VaadinStudy.view.publico;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import study.course.VaadinStudy.view.components.MainLayout;

@AnonymousAllowed
@PageTitle("login")
@Route(value = "/login", layout = MainLayout.class)
public class LoginView extends VerticalLayout {

    public LoginView(){
        setSizeFull();

        getStyle().set("display", "flex").set("align-items", "center").set("justify-content", "center");
        LoginForm loginForm = new LoginForm();
        loginForm.setForgotPasswordButtonVisible(true);
        loginForm.addForgotPasswordListener(e -> {
            UI.getCurrent().navigate(PasswordRecoveryView.class);
        });
        loginForm.getStyle().set("background-color", "#f0f0f0");
        loginForm.setAction("login");

        add(loginForm);
    }
}
