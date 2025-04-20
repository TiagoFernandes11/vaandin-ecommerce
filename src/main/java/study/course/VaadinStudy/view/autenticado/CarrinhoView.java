package study.course.VaadinStudy.view.autenticado;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import org.apache.commons.io.input.TeeInputStream;
import study.course.VaadinStudy.entities.Endereco;
import study.course.VaadinStudy.entities.ItemPedido;
import study.course.VaadinStudy.entities.Produto;
import study.course.VaadinStudy.services.ItemPedidoService;
import study.course.VaadinStudy.services.PedidoService;
import study.course.VaadinStudy.services.ProdutoService;
import study.course.VaadinStudy.services.UsuarioService;
import study.course.VaadinStudy.view.components.Carrinho;
import study.course.VaadinStudy.view.components.MainLayout;
import study.course.VaadinStudy.view.components.Vitrine;
import study.course.VaadinStudy.view.publico.MainView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@PageTitle("Carrinho")
@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "/carrinho", layout = MainLayout.class)
public class CarrinhoView extends HorizontalLayout {

    private final AuthenticationContext authenticationContext;
    private final ItemPedidoService itemPedidoService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;

    public CarrinhoView(AuthenticationContext authenticationContext, ItemPedidoService itemPedidoService, ProdutoService produtoService, PedidoService pedidoService, UsuarioService usuarioService){
        this.authenticationContext = authenticationContext;
        this.itemPedidoService = itemPedidoService;
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;

        addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER);

        montarMenuEsquerdo();
        montarMenuDireito();
    }

    public void montarMenuEsquerdo(){
        Button buttonVoltar = new Button("Voltar para a pagina principal", event -> {
            UI.getCurrent().navigate(MainView.class);
        });

        VerticalLayout menuEsquerdo = new VerticalLayout();

        menuEsquerdo.addClassNames(LumoUtility.Border.ALL);

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

        add(botaoEMenuEsquerdo);
    }

    private void montarMenuDireito() {
        VerticalLayout menuDireito = new VerticalLayout();
        menuDireito.addClassNames(LumoUtility.Border.ALL);

        VerticalLayout endereco = new VerticalLayout();
        H4 enderecoTitulo = new H4("Seu endereço: ");
        endereco.add(enderecoTitulo);

        endereco.setClassName(LumoUtility.Border.ALL);

        HorizontalLayout ruaENumero = new HorizontalLayout();
        TextField rua = new TextField("Rua");
        TextField numero = new TextField("Numero");
        ruaENumero.add(rua, numero);

        TextField estado = new TextField("Estado");
        TextField cidade = new TextField("Cidade");
        TextField bairro = new TextField("Bairro");

        rua.setMaxLength(50);
        numero.setMaxLength(5);
        estado.setMaxLength(50);
        cidade.setMaxLength(30);
        bairro.setMaxLength(30);

        rua.getStyle().set("width", "80%");
        numero.getStyle().set("width", "20%");
        ruaENumero.setClassName(LumoUtility.Width.FULL);
        estado.setClassName(LumoUtility.Width.FULL);
        cidade.setClassName(LumoUtility.Width.FULL);
        bairro.setClassName(LumoUtility.Width.FULL);

        endereco.add(ruaENumero, estado, cidade, bairro);

        ListBox<String> listBox = new ListBox<>();
        listBox.setItems("Entrega padrão", "Entrega agendada", "Entrega expressa");
        listBox.setValue("Entrega padrão");

        HorizontalLayout buscaCep = new HorizontalLayout();
        H4 cepTitulo = new H4("Seu cep: ");
        TextField inputCep = new TextField();
        Button btnBuscaCep = new Button("Buscar", event-> {
            String cep = inputCep.getValue();
            Endereco enderecoAPartirDoCep = getEnderecoPeloCep(cep);

            rua.setValue(enderecoAPartirDoCep.getLogradouro());
            numero.setValue(Integer.toString(enderecoAPartirDoCep.getNumero()));
            estado.setValue(enderecoAPartirDoCep.getEstado());
            cidade.setValue(enderecoAPartirDoCep.getCidade());
            bairro.setValue(enderecoAPartirDoCep.getBairro());
        });

        buscaCep.add(inputCep, btnBuscaCep);

        H4 entregaTitulo = new H4("Forma de entrega: ");

        Button btnFinalizarCompra = new Button("Finalizar compra", event -> {
            Notification.show("Não implementado");
        });

        Button btnEscolherMaisProdutos = new Button("Escolher mais produtos", event -> {
            UI.getCurrent().navigate(MainView.class);
        });

        HorizontalLayout menuInformacoesDeEntrega = new HorizontalLayout();
        menuInformacoesDeEntrega.addClassNames(LumoUtility.Width.FULL);
        menuDireito.add(cepTitulo, buscaCep, entregaTitulo, listBox);

        menuInformacoesDeEntrega.add(menuDireito, endereco);

        VerticalLayout menuDireitoEBotoes = new VerticalLayout();

        menuDireitoEBotoes.add(menuInformacoesDeEntrega, btnEscolherMaisProdutos, btnFinalizarCompra);

        add(menuDireitoEBotoes);
    }

    private Endereco getEnderecoPeloCep(String cep){
        return new Endereco(null, "06969-069", "Rua teste", 69, "São Teste", "ET", "Bairro teste");
    }
}
