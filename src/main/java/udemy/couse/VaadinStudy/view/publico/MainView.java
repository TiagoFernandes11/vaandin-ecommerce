package udemy.couse.VaadinStudy.view.publico;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import udemy.couse.VaadinStudy.services.ProdutoService;
import udemy.couse.VaadinStudy.view.components.Vitrine;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Route(value = "")
@PageTitle("Home")
public class MainView extends VerticalLayout {

    public MainView(ProdutoService produtoService) {
        pegarLarguraDaTela(largura -> {
            Vitrine vitrine = new Vitrine(produtoService, largura);
            add(vitrine);
        });
    }

    private void pegarLarguraDaTela(Consumer<Integer> callback) {
        UI.getCurrent().getPage().executeJs("return window.innerWidth;")
                .then(width -> {
                    Integer largura = (int) ((elemental.json.JsonNumber) width).asNumber();
                    callback.accept(largura);  // Passa o valor para o callback
                });
    }
}
