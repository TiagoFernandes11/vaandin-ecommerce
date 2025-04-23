package study.course.VaadinStudy.view.autenticado;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.services.PedidoService;
import study.course.VaadinStudy.view.components.MainLayout;

import java.util.HashMap;
import java.util.Map;

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

        HorizontalLayout menus = new HorizontalLayout();

        VerticalLayout menuPagamento = montarMenuPagamento();
        VerticalLayout menuConfirmacao = montarMenuConfirmacao();

        menus.add(menuConfirmacao, menuPagamento);

        add(tituloH2, voltarButton, menus);
    }

    private VerticalLayout montarMenuPagamento() {
        VerticalLayout menuPagamento = new VerticalLayout();
        Tabs opcoesPagamentoTabs = new Tabs();

        Tab cartaoTab = new Tab("Cartão");
        Tab pixTab = new Tab("Pix");
        Tab boletoTab = new Tab("Boleto");

        opcoesPagamentoTabs.add(cartaoTab, pixTab, boletoTab);

        VerticalLayout conteudoTabCartao = new VerticalLayout();
        VerticalLayout conteudoTabPix = new VerticalLayout();
        VerticalLayout conteudoTabBoleto = new VerticalLayout();

        conteudoTabCartao.add(new H3("Aba de cartões"));
        conteudoTabPix.add(new H3("Aba de Pix"));
        conteudoTabBoleto.add(new H3("Aba de Boletos"));

        Map<Tab, VerticalLayout> conteudosPagamento = new HashMap<>();
        conteudosPagamento.put(cartaoTab, conteudoTabCartao);
        conteudosPagamento.put(pixTab, conteudoTabPix);
        conteudosPagamento.put(boletoTab, conteudoTabBoleto);

        Div conteudoAtual = new Div();
        conteudoAtual.add(conteudoTabCartao);

        opcoesPagamentoTabs.addSelectedChangeListener(event -> {
            conteudoAtual.removeAll();
            conteudoAtual.add(conteudosPagamento.get(opcoesPagamentoTabs.getSelectedTab()));
        });

        menuPagamento.add(opcoesPagamentoTabs, conteudoAtual);
        return menuPagamento;
    }

    private VerticalLayout montarMenuConfirmacao() {
        VerticalLayout menuConfirmacao = new VerticalLayout();
        return menuConfirmacao;
    }




}
