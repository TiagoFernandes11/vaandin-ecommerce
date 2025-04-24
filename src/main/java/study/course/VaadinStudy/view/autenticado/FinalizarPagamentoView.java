package study.course.VaadinStudy.view.autenticado;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.constants.StatusPedido;
import study.course.VaadinStudy.entities.ItemPedido;
import study.course.VaadinStudy.entities.Pedido;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.PedidoService;
import study.course.VaadinStudy.view.components.MainLayout;

import java.io.IOException;
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

        opcoesPagamentoTabs.addClassNames(LumoUtility.Width.FULL);

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
        conteudoAtual.addClassNames(LumoUtility.Width.FULL);

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
        Pedido pedido = pedidoService.findUltimoPedido(authenticationContext.getPrincipalName().orElse(null), StatusPedido.PENDENTE);

        H3 tituloTab = new H3("Cartão");

        HorizontalLayout numeroENome = new HorizontalLayout();
        TextField numeroCartaoInput = new TextField("Numero do cartão");
        TextField nomeImpressoCartaoInput = new TextField("Nome impresso");

        numeroENome.addClassNames(LumoUtility.Width.FULL);
        numeroCartaoInput.addClassNames(LumoUtility.Width.FULL);
        nomeImpressoCartaoInput.addClassNames(LumoUtility.Width.FULL);

        numeroENome.add(numeroCartaoInput, nomeImpressoCartaoInput);

        HorizontalLayout validadeCVVeParcelas = getValidadeCVVeParcelasIputs(pedido);

        HorizontalLayout btnPagarContainer = new HorizontalLayout();
        Button btnPagar = new Button("Realizar pagamento", event -> {

        });
        btnPagarContainer.addClassNames(LumoUtility.Width.FULL, LumoUtility.JustifyContent.END);
        btnPagarContainer.add(btnPagar);

        VerticalLayout tab = new VerticalLayout();
        tab.addClassNames(LumoUtility.Width.FULL);

        tab.add(tituloTab, numeroENome, validadeCVVeParcelas, btnPagarContainer);
        return tab;
    }

    private static HorizontalLayout getValidadeCVVeParcelasIputs(Pedido pedido) {
        HorizontalLayout validadeCVVeParcelas = new HorizontalLayout();
        validadeCVVeParcelas.addClassNames(LumoUtility.Width.FULL);

        DatePicker validadeCartaoInput = new DatePicker("Start date");
        TextField codigoSegurancaoInput = new TextField("Cod de segurança");

        Select<String> parcelasSelect = new Select<>();
        parcelasSelect.setLabel("Parcelas");
        parcelasSelect.setItems(
                "1x de R$%.2f".formatted(pedido.getTotal()),
                "2x de R$%.2f".formatted(pedido.getTotal() / 2),
                "3x de R$%.2f".formatted(pedido.getTotal() / 3)
                );
        parcelasSelect.setValue("1x de R$%.2f".formatted(pedido.getTotal()));

        validadeCartaoInput.addClassNames(LumoUtility.Width.FULL);
        codigoSegurancaoInput.addClassNames(LumoUtility.Width.FULL);
        parcelasSelect.addClassNames(LumoUtility.Width.FULL);

        validadeCVVeParcelas.add(validadeCartaoInput, codigoSegurancaoInput,parcelasSelect);
        return validadeCVVeParcelas;
    }

    private VerticalLayout montarConteudoTabPix() {
        VerticalLayout tab = new VerticalLayout();
        tab.setWidthFull();
        tab.setPadding(true);
        tab.setSpacing(true);

        Pedido pedido = pedidoService.findUltimoPedido(authenticationContext.getPrincipalName().orElse(null), StatusPedido.PENDENTE);

        H3 tituloTab = new H3("Pagamento via Pix");

        String chavePix = "pix@empresa.com";

        Span valor = new Span("Total a pagar: R$ %.2f".formatted(pedido.getTotal()));
        valor.getStyle().set("font-weight", "bold");

        TextField chavePixField = new TextField("Chave Pix");
        chavePixField.setValue(chavePix);
        chavePixField.setReadOnly(true);

        StreamResource qrCodeResource = new StreamResource("qrcode.png", () -> {
            try {
                return getClass().getResourceAsStream("/static/qrcode.png");
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
        Image qrCodeImg = new Image(qrCodeResource, "QR Code Pix");
        qrCodeImg.setWidth("200px");

        Span instrucao = new Span("Escaneie o QR Code acima ou copie a chave para realizar o pagamento.");
        instrucao.getStyle().set("font-style", "italic");

        Button btnPagar = new Button("Realizar pagamento", event -> {

        });

        HorizontalLayout btnPagarContainer = new HorizontalLayout(btnPagar);
        btnPagarContainer.setWidthFull();
        btnPagarContainer.setJustifyContentMode(JustifyContentMode.END); // 👈 agora alinha à direita


        tab.add(tituloTab, valor, qrCodeImg, instrucao, chavePixField, btnPagarContainer);
        return tab;
    }


    private VerticalLayout montarConteudoTabBoleto() {
        VerticalLayout tab = new VerticalLayout();
        tab.addClassNames(LumoUtility.Width.FULL);

        H3 tituloTab = new H3("Boleto");

        Span instrucoes = new Span("Você pode pagar seu boleto via app bancário, internet banking ou em qualquer casa lotérica.");
        instrucoes.getStyle().set("font-size", "14px").set("color", "gray");

        TextField codigoBarras = new TextField("Código de barras");
        codigoBarras.setValue("34191.79001 01043.510047 91020.150008 7 90270000020000");
        codigoBarras.setReadOnly(true);
        codigoBarras.setWidthFull();

        Button baixarBoleto = new Button("Baixar boleto PDF");
        baixarBoleto.getStyle().set("margin-bottom", "var(--lumo-space-m)");

        Button btnPagar = new Button("Realizar pagamento", event -> {

        });
        HorizontalLayout btnPagarContainer = new HorizontalLayout(btnPagar);
        btnPagarContainer.setWidthFull();
        btnPagarContainer.setJustifyContentMode(JustifyContentMode.END);

        tab.add(tituloTab, instrucoes, codigoBarras, baixarBoleto, btnPagarContainer);
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
                HorizontalLayout linhaProduto = getLinhaProduto(itemPedido);
                conteudoMenu.add(linhaProduto);
            }

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

    private static HorizontalLayout getLinhaProduto(ItemPedido itemPedido) {
        Produto produto = itemPedido.getProduto();

        HorizontalLayout linhaProduto = new HorizontalLayout();
        linhaProduto.setWidthFull();
        linhaProduto.setJustifyContentMode(JustifyContentMode.BETWEEN);

        Span nome = new Span(produto.getNome());
        Span preco = new Span("R$ %.2f".formatted(produto.getPreco()));
        Span qtd = new Span("%d unid".formatted(itemPedido.getQuantidade()));
        Span subtotal = new Span("R$ %.2f".formatted(itemPedido.getSubTotal()));

        linhaProduto.add(nome, preco, qtd, subtotal);
        return linhaProduto;
    }
}
