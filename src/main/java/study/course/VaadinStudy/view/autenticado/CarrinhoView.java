package study.course.VaadinStudy.view.autenticado;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.H4;
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
import study.course.VaadinStudy.constants.StatusPedido;
import study.course.VaadinStudy.constants.TiposDeEntrega;
import study.course.VaadinStudy.entities.*;
import study.course.VaadinStudy.services.*;
import study.course.VaadinStudy.view.components.Carrinho;
import study.course.VaadinStudy.view.components.MainLayout;
import study.course.VaadinStudy.view.components.Vitrine;
import study.course.VaadinStudy.view.publico.MainView;

import java.util.List;
import java.util.Objects;

@PageTitle("Carrinho")
@RolesAllowed(value = {"ROLE_USER", "ROLE_ADMIN"})
@Route(value = "/carrinho", layout = MainLayout.class)
public class CarrinhoView extends VerticalLayout {

    private final AuthenticationContext authenticationContext;
    private final ItemPedidoService itemPedidoService;
    private final ProdutoService produtoService;
    private final PedidoService pedidoService;
    private final UsuarioService usuarioService;
    private final EnderecoService enderecoService;

    public CarrinhoView(AuthenticationContext authenticationContext, ItemPedidoService itemPedidoService, ProdutoService produtoService, PedidoService pedidoService, UsuarioService usuarioService, EnderecoService enderecoService){
        this.authenticationContext = authenticationContext;
        this.itemPedidoService = itemPedidoService;
        this.produtoService = produtoService;
        this.pedidoService = pedidoService;
        this.usuarioService = usuarioService;
        this.enderecoService = enderecoService;

        Button buttonVoltar = new Button("Voltar para a pagina principal", event -> {
            UI.getCurrent().navigate(MainView.class);
        });

        HorizontalLayout menus = new HorizontalLayout();

        menus.addClassNames(LumoUtility.Width.FULL, LumoUtility.AlignItems.START, LumoUtility.JustifyContent.CENTER);

        VerticalLayout menuEsquerdo = montarMenuEsquerdo();
        VerticalLayout menuDireito = montarMenuDireito();

        menus.add(menuEsquerdo, menuDireito);
        add(buttonVoltar, menus);
    }

    public VerticalLayout montarMenuEsquerdo(){
        VerticalLayout menuEsquerdo = new VerticalLayout();

        menuEsquerdo.addClassNames(LumoUtility.Border.ALL);

        Carrinho carrinho = new Carrinho(authenticationContext, usuarioService, pedidoService);
        carrinho.addClassNames(LumoUtility.Border.ALL);

        H3 outrosProdutosTitulo = new H3("Outros produtos");

        Vitrine miniVitrine = new Vitrine(authenticationContext, getProdutosMiniVitrine(), usuarioService, pedidoService);

        menuEsquerdo.add(carrinho, outrosProdutosTitulo, miniVitrine);

        return menuEsquerdo;
    }

    private VerticalLayout montarMenuDireito() {

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
        listBox.setItems(TiposDeEntrega.PADRAO, TiposDeEntrega.AGENDADA, TiposDeEntrega.EXPRESSA);
        listBox.setValue(TiposDeEntrega.PADRAO);

        HorizontalLayout buscaCep = new HorizontalLayout();
        H4 cepTitulo = new H4("Seu cep: ");
        TextField inputCep = new TextField();
        Button btnBuscaCep = new Button("Buscar", event-> {
            String cep = inputCep.getValue();
            Endereco enderecoAPartirDoCep = getEnderecoPeloCep(cep);

            rua.setValue(enderecoAPartirDoCep.getLogradouro());
            numero.setValue(Long.toString(enderecoAPartirDoCep.getNumero()));
            estado.setValue(enderecoAPartirDoCep.getEstado());
            cidade.setValue(enderecoAPartirDoCep.getCidade());
            bairro.setValue(enderecoAPartirDoCep.getBairro());
        });

        buscaCep.add(inputCep, btnBuscaCep);

        H4 entregaTitulo = new H4("Forma de entrega: ");

        Button btnFinalizarCompra = new Button("Ir para o pagamento", event -> {

            Usuario usuario = usuarioService.find(authenticationContext.getPrincipalName().orElse(null));
            Pedido pedido = pedidoService.findUltimoPedido(authenticationContext.getPrincipalName().orElse(null), StatusPedido.CARRINHO);

            Endereco enderecoPedido = new Endereco(
                    true,
                    false,
                    inputCep.getValue(),
                    rua.getValue(),
                    Long.parseLong(numero.getValue().isBlank() ? "0" : numero.getValue()),
                    cidade.getValue(),
                    estado.getValue(),
                    bairro.getValue(),
                    usuario
            );

            if(preenchimentoEnderecoValido(enderecoPedido)){
                if(Objects.nonNull(usuario) && Objects.nonNull(pedido)){
                    enderecoService.salvarEnderecoCliente(enderecoPedido, usuario.getEmail());

                    pedido.setTipoDeEntrega(listBox.getValue());
                    pedido.setStatus(StatusPedido.PENDENTE);
                    pedidoService.save(pedido);

                    UI.getCurrent().navigate(FinalizarPagamentoView.class);
                }
            } else {
                Notification.show("Todos os campos devem ser preenchidos");
            };
        });

        Button btnEscolherMaisProdutos = new Button("Escolher mais produtos", event -> {
            UI.getCurrent().navigate(MainView.class);
        });

        VerticalLayout buscaCepEFormaDeEntrega = new VerticalLayout();
        buscaCepEFormaDeEntrega.addClassNames(LumoUtility.Border.ALL);

        HorizontalLayout menuInformacoesDeEntrega = new HorizontalLayout();
        buscaCepEFormaDeEntrega.add(cepTitulo, buscaCep, entregaTitulo, listBox);

        menuInformacoesDeEntrega.addClassNames(LumoUtility.Width.FULL);

        menuInformacoesDeEntrega.add(buscaCepEFormaDeEntrega, endereco);

        HorizontalLayout btnEscolherEFinalizar = new HorizontalLayout();
        btnEscolherEFinalizar.add(btnEscolherMaisProdutos, btnFinalizarCompra);
        btnEscolherEFinalizar.addClassName(LumoUtility.Width.FULL);
        btnEscolherEFinalizar.addClassName(LumoUtility.JustifyContent.BETWEEN);

        VerticalLayout menuDireito = new VerticalLayout();
        menuDireito.add(menuInformacoesDeEntrega, btnEscolherEFinalizar);

        return menuDireito;
    }

    private List<Produto> getProdutosMiniVitrine() {
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

        return listaProdutos.stream().limit(3).toList();
    }

    private boolean preenchimentoEnderecoValido(Endereco endereco) {

        return endereco.getCep() != null && !endereco.getCep().isBlank() &&
                endereco.getLogradouro() != null && !endereco.getLogradouro().isBlank() &&
                endereco.getNumero() != null && endereco.getNumero() > 0 &&
                endereco.getCidade() != null && !endereco.getCidade().isBlank() &&
                endereco.getEstado() != null && !endereco.getEstado().isBlank() &&
                endereco.getBairro() != null && !endereco.getBairro().isBlank();
    }


    private Endereco getEnderecoPeloCep(String cep){
        return new Endereco(null, null, "06969-069", "Rua teste", 69L, "São Teste", "ET", "Bairro teste", null);
    }
}
