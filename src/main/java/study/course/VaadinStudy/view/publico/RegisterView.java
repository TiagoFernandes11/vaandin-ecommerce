package study.course.VaadinStudy.view.publico;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import study.course.VaadinStudy.services.ClienteService;
import study.course.VaadinStudy.view.components.MainLayout;
import study.course.VaadinStudy.view.components.RegisterForm;


@PageTitle("Registrar")
@Route(value = "/registrar", layout = MainLayout.class)
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    public RegisterView(ClienteService clienteService){
        var form = new RegisterForm(clienteService);

        setSizeFull();
        addClassNames(LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER);

        add(form);
    }
}
