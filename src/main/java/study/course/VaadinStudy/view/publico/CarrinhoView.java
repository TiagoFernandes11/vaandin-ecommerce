package study.course.VaadinStudy.view.publico;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.entities.ItemPedido;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.ItemPedidoService;
import study.course.VaadinStudy.services.PedidoService;
import study.course.VaadinStudy.services.ProdutoService;
import study.course.VaadinStudy.services.UsuarioService;
import study.course.VaadinStudy.view.components.Carrinho;
import study.course.VaadinStudy.view.components.MainLayout;
import study.course.VaadinStudy.view.components.Vitrine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@PageTitle("Carrinho")
@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "/carrinho", layout = MainLayout.class)
public class CarrinhoView extends HorizontalLayout {

    public CarrinhoView(AuthenticationContext authenticationContext, ItemPedidoService itemPedidoService, ProdutoService produtoService, PedidoService pedidoService, UsuarioService usuarioService){
        addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER);

        Button buttonVoltar = new Button("Voltar para a pagina principal", event -> {
            UI.getCurrent().navigate(MainView.class);
        });

        VerticalLayout menuEsquerdo = new VerticalLayout();
        VerticalLayout menuDireito = new VerticalLayout();

        menuEsquerdo.addClassNames(LumoUtility.Border.ALL);
        menuDireito.addClassNames(LumoUtility.Border.ALL);

        Carrinho carrinho = new Carrinho(authenticationContext, usuarioService, pedidoService);
        carrinho.addClassNames(LumoUtility.Border.ALL);

        H3 outrosProdutosTitulo = new H3("Outros produtos");

        List<Produto> listaProdutos = produtoService.findAll().stream().filter(produto -> {
            List<ItemPedido> itens= itemPedidoService.findAllItems(authenticationContext.getPrincipalName().orElse(null));
            if(!Objects.isNull(itens)){
                for(ItemPedido item : itens){
                    if(Objects.equals(item.getProduto().getSku(), produto.getSku())){
                        return false;
                    }
                }
            }
            return true;
        }).toList();

        List<Produto> produtosMiniVitrine = new ArrayList<>();

        for(int i = 0; produtosMiniVitrine.size() < 3; i++){
            produtosMiniVitrine.add(listaProdutos.get(i));
        }

        Vitrine miniVitrine = new Vitrine(authenticationContext, produtosMiniVitrine, usuarioService, pedidoService);

        VerticalLayout botaoEMenuEsquerdo = new VerticalLayout();

        menuEsquerdo.add(buttonVoltar, carrinho, outrosProdutosTitulo, miniVitrine);
        botaoEMenuEsquerdo.add(buttonVoltar,menuEsquerdo);

        ListBox<String> listBox = new ListBox<>();
        listBox.setItems("Entrega padrão", "Entrega agendada", "Entrega expressa");
        listBox.setValue("Entrega padrão");

        H4 cepTitulo = new H4("Seu cep: ");
        TextField inputCep = new TextField();

        Button btnFinalizarCompra = new Button("Finalizar compra", event -> {
            Notification.show("Não implementado");
        });

        Button btnEscolherMaisProdutos = new Button("Escolher mais produtos", event -> {
            UI.getCurrent().navigate(MainView.class);
        });

        HorizontalLayout menuInformacoesDeEntrega = new HorizontalLayout();
        menuInformacoesDeEntrega.addClassNames(LumoUtility.Width.FULL);
        menuDireito.add(cepTitulo, inputCep, listBox);

        VerticalLayout endereco = new VerticalLayout();
        H4 enderecoTitulo = new H4("Seu endereço: ");
        endereco.add(enderecoTitulo);

        endereco.setClassName(LumoUtility.Border.ALL);

        H5 tituloEndereco = new H5("Principal");
        Span ruaENumero = new Span("Rua dos testes, 69");
        Span bairroCidadeEEstado = new Span("Bairro dos testes - São Teste - ST");
        Span cep = new Span("CEP: 696969-060");

        endereco.add(tituloEndereco, ruaENumero, bairroCidadeEEstado, cep);

        menuInformacoesDeEntrega.add(menuDireito, endereco);

        VerticalLayout menuDireitoEBotoes = new VerticalLayout();

        menuDireitoEBotoes.add(menuInformacoesDeEntrega, btnEscolherMaisProdutos, btnFinalizarCompra);

        add(botaoEMenuEsquerdo, menuDireitoEBotoes);
    }
}
