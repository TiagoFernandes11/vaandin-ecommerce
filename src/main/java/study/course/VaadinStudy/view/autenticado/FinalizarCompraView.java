package study.course.VaadinStudy.view.autenticado;

import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.view.components.MainLayout;

@PageTitle("Finalizar compra")
@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "/finalizar-compra", layout = MainLayout.class)
public class FinalizarCompraView {
}
