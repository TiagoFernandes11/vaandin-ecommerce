package udemy.couse.VaadinStudy.view.publico;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import udemy.couse.VaadinStudy.services.UsuarioService;
import udemy.couse.VaadinStudy.view.components.RegisterForm;

@Route(value = "/registrar")
@PageTitle("Registrar")
public class RegisterView extends VerticalLayout {

    public RegisterView(UsuarioService usuarioService){
        var form = new RegisterForm(usuarioService);
        setSizeFull();
        getStyle().set("display", "flex").set("align-items", "center").set("justify-content", "center");

        add(form);
    }
}
