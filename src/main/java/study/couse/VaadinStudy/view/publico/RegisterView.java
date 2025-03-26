package study.couse.VaadinStudy.view.publico;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import study.couse.VaadinStudy.services.ClienteService;
import study.couse.VaadinStudy.view.components.MainLayout;
import study.couse.VaadinStudy.view.components.RegisterForm;


@PageTitle("Registrar")
@Route(value = "/registrar", layout = MainLayout.class)
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    public RegisterView(ClienteService clienteService){
        var form = new RegisterForm(clienteService);
        setSizeFull();
        getStyle().set("display", "flex").set("align-items", "center").set("justify-content", "center");

        add(form);
    }
}
