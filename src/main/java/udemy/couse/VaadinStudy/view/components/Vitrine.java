package udemy.couse.VaadinStudy.view.components;

import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import udemy.couse.VaadinStudy.entities.Produto;
import udemy.couse.VaadinStudy.services.ProdutoService;

import java.util.List;

public class Vitrine extends FlexLayout {
    
    private List<Produto> produtos;

    public Vitrine(ProdutoService produtoService) {
        this.produtos = produtoService.findAll();

        setFlexDirection(FlexLayout.FlexDirection.ROW);
        setFlexWrap(FlexLayout.FlexWrap.WRAP);
        setAlignItems(FlexComponent.Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        for (Produto produto : this.produtos) {
            ProdutoVitrine produtoVitrine = new ProdutoVitrine(produto);
            produtoVitrine.setWidth("220px");
            produtoVitrine.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.Margin.LARGE);
            add(produtoVitrine);
        }

        addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.START, LumoUtility.FlexWrap.WRAP);
    }
}
