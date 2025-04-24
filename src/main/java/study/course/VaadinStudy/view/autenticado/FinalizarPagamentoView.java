package study.course.VaadinStudy.view.autenticado;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.constants.StatusPedido;
import study.course.VaadinStudy.entities.ItemPedido;
import study.course.VaadinStudy.entities.Pedido;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.PedidoService;
import study.course.VaadinStudy.view.components.MainLayout;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@PageTitle("Finalização de pagamento")
@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "/finalizar-pagamento", layout = MainLayout.class)
public class FinalizarPagamentoView extends VerticalLayout {

    private final AuthenticationContext authenticationContext;
    private final PedidoService pedidoService;

    public FinalizarPagamentoView(AuthenticationContext authenticationContext, PedidoService pedidoService) {
        this.authenticationContext = authenticationContext;
        this.pedidoService = pedidoService;

        H2 tituloH2 = new H2("Pagamento");

        Button voltarButton = new Button("Voltar passo", event -> {
            UI.getCurrent().navigate(CarrinhoView.class);
        });

        FormLayout layoutResponsivo = new FormLayout();
        layoutResponsivo.setWidthFull();
        layoutResponsivo.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2) // 2 colunas a partir de 600px
        );

        VerticalLayout menuPagamento = montarMenuPagamento();
        VerticalLayout menuConfirmacao = montarMenuConfirmacao();

        layoutResponsivo.add(menuConfirmacao, menuPagamento);

        add(tituloH2, voltarButton, layoutResponsivo);
    }

    private VerticalLayout montarMenuPagamento() {
        VerticalLayout menuPagamento = new VerticalLayout();
        H3 tituloMenu = new H3("Pagamento");

        Tab cartaoTab = new Tab("Cartão");
        Tab pixTab = new Tab("Pix");
        Tab boletoTab = new Tab("Boleto");
        Tabs opcoesPagamentoTabs = new Tabs();
        opcoesPagamentoTabs.add(cartaoTab, pixTab, boletoTab);

        VerticalLayout conteudoTabCartao = montarConteudoTabCartao();
        VerticalLayout conteudoTabPix = montarConteudoTabPix();
        VerticalLayout conteudoTabBoleto = montarConteudoTabBoleto();

        Map<Tab, VerticalLayout> conteudosPagamento = new HashMap<>();
        conteudosPagamento.put(cartaoTab, conteudoTabCartao);
        conteudosPagamento.put(pixTab, conteudoTabPix);
        conteudosPagamento.put(boletoTab, conteudoTabBoleto);

        VerticalLayout tabEConteudo = new VerticalLayout();
        tabEConteudo.addClassNames(LumoUtility.Border.ALL);

        Div conteudoAtual = new Div();
        conteudoAtual.add(conteudoTabCartao);

        opcoesPagamentoTabs.addSelectedChangeListener(event -> {
            conteudoAtual.removeAll();
            conteudoAtual.add(conteudosPagamento.get(opcoesPagamentoTabs.getSelectedTab()));
        });

        tabEConteudo.add(opcoesPagamentoTabs, conteudoAtual);

        menuPagamento.setWidthFull();
        menuPagamento.add(tituloMenu, tabEConteudo);
        return menuPagamento;
    }

    private VerticalLayout montarConteudoTabCartao() {
        VerticalLayout tab = new VerticalLayout();
        H3 tituloTab = new H3("Cartão");

        tab.add(tituloTab);
        return tab;
    }

    private VerticalLayout montarConteudoTabPix() {
        VerticalLayout tab = new VerticalLayout();
        H3 tituloTab = new H3("Pix");

        tab.add(tituloTab);
        return tab;
    }

    private VerticalLayout montarConteudoTabBoleto() {
        VerticalLayout tab = new VerticalLayout();
        H3 tituloTab = new H3("Boleto");

        tab.add(tituloTab);
        return tab;
    }

    private VerticalLayout montarMenuConfirmacao() {
        VerticalLayout menuConfirmacao = new VerticalLayout();
        H3 tituloMenu = new H3("Confirmação");

        VerticalLayout conteudoMenu = new VerticalLayout();
        conteudoMenu.addClassNames(LumoUtility.Border.ALL, LumoUtility.Padding.SMALL, LumoUtility.Gap.MEDIUM);

        Pedido pedido = pedidoService.findUltimoPedido(authenticationContext.getPrincipalName().orElse(null), StatusPedido.PENDENTE);

        if (Objects.nonNull(pedido)) {
            for (ItemPedido itemPedido : pedido.getItens()) {
                Produto produto = itemPedido.getProduto();

                HorizontalLayout linhaProduto = new HorizontalLayout();
                linhaProduto.setWidthFull();
                linhaProduto.setJustifyContentMode(JustifyContentMode.BETWEEN);

                Span nome = new Span(produto.getNome());
                Span preco = new Span("R$ %.2f".formatted(produto.getPreco()));
                Span qtd = new Span("%d unid".formatted(itemPedido.getQuantidade()));
                Span subtotal = new Span("R$ %.2f".formatted(itemPedido.getSubTotal()));

                linhaProduto.add(nome, preco, qtd, subtotal);
                conteudoMenu.add(linhaProduto);
            }

            // Linha de total
            HorizontalLayout linhaTotal = new HorizontalLayout();
            linhaTotal.setWidthFull();
            linhaTotal.setJustifyContentMode(JustifyContentMode.END);

            Span total = new Span("Total: R$ %.2f".formatted(pedido.getTotal()));
            total.getStyle().set("font-weight", "bold");

            linhaTotal.add(total);
            conteudoMenu.add(linhaTotal);
        }

        menuConfirmacao.setWidthFull();
        menuConfirmacao.add(tituloMenu, conteudoMenu);
        return menuConfirmacao;
    }
}
