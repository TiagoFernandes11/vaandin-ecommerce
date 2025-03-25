package udemy.couse.VaadinStudy.view.publico;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import udemy.couse.VaadinStudy.services.UsuarioService;
import udemy.couse.VaadinStudy.view.components.MainLayout;
import udemy.couse.VaadinStudy.view.components.RegisterForm;


@PageTitle("Registrar")
@Route(value = "/registrar", layout = MainLayout.class)
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    public RegisterView(UsuarioService usuarioService){
        var form = new RegisterForm(usuarioService);
        setSizeFull();
        getStyle().set("display", "flex").set("align-items", "center").set("justify-content", "center");

        add(form);
    }
}
