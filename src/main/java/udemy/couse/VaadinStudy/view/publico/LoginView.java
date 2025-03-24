package udemy.couse.VaadinStudy.view.publico;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import udemy.couse.VaadinStudy.view.components.LoginForm;

@PageTitle("login")
@Route(value = "/login")
public class LoginView extends VerticalLayout {

    public LoginView(){
        var loginForm = new LoginForm();
        setSizeFull();

        var title = new H1();
        title.setText("Formulario de Login");

        getStyle().set("display", "flex").set("align-items", "center").set("justify-content", "center");
        add(title, loginForm);
    }
}
