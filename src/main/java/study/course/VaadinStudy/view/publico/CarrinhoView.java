package study.course.VaadinStudy.view.publico;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.services.CarrinhoService;
import study.course.VaadinStudy.services.ItemCarrinhoService;
import study.course.VaadinStudy.view.components.Carrinho;
import study.course.VaadinStudy.view.components.MainLayout;

@PageTitle("Carrinho")
@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "/carrinho", layout = MainLayout.class)
public class CarrinhoView extends VerticalLayout {

    public CarrinhoView(AuthenticationContext authenticationContext, ItemCarrinhoService itemCarrinhoService, CarrinhoService carrinhoService){
        Carrinho carrinho = new Carrinho(authenticationContext, itemCarrinhoService, carrinhoService);
        add(carrinho);
    }
}
