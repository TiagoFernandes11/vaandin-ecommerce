package udemy.couse.VaadinStudy.view.publico;

import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import udemy.couse.VaadinStudy.repository.ProdutoRepository;
import udemy.couse.VaadinStudy.services.ProdutoService;

import java.io.ByteArrayInputStream;

@Route(value = "")
@PageTitle("Home")
public class MainView extends VerticalLayout {

    public MainView(ProdutoRepository produtoRepository) {
        StreamResource streamResource = new StreamResource("imagem.jpg", () -> new ByteArrayInputStream(produtoRepository.findBySku(1234567890).get().getImagem()));
        Image image = new Image();
        image.setSrc(streamResource);

        add(image);
    }
}
