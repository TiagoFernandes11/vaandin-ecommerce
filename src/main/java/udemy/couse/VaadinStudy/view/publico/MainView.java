package udemy.couse.VaadinStudy.view.publico;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import udemy.couse.VaadinStudy.services.ProdutoService;
import udemy.couse.VaadinStudy.view.components.MainLayout;
import udemy.couse.VaadinStudy.view.components.Vitrine;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@AnonymousAllowed
public class MainView extends VerticalLayout {

    public MainView(ProdutoService produtoService) {
        Vitrine vitrine = new Vitrine(produtoService);
        add(vitrine);
    }
}
