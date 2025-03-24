package udemy.couse.VaadinStudy.view.components;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.theme.lumo.LumoUtility;
import udemy.couse.VaadinStudy.entities.Produto;
import udemy.couse.VaadinStudy.services.ProdutoService;

import java.util.List;

public class Vitrine extends VerticalLayout {
    
    private List<Produto> produtos;
    
    public Vitrine(ProdutoService produtoService, Integer larguraDaTela){
        this.produtos = produtoService.findAll();
        HorizontalLayout vitrineRow = new HorizontalLayout();
        Integer fatorTela = (int) ((larguraDaTela - 500) / 200) > 0 ? (int) ((larguraDaTela - 500) / 200) : 2;

        for(int i = 1; i <= this.produtos.size(); i++){
            ProdutoVitrine produtoVitrine = new ProdutoVitrine(this.produtos.get(i - 1));
            vitrineRow.add(produtoVitrine);
            if(i % fatorTela == 0 && i > 0){
                vitrineRow.setWidthFull();
                vitrineRow.addClassNames(LumoUtility.Display.FLEX, LumoUtility.JustifyContent.BETWEEN);
                add(vitrineRow);
                vitrineRow = new HorizontalLayout();
            }
        }
    }
}
