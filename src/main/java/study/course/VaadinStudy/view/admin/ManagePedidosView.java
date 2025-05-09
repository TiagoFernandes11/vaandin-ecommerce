package study.course.VaadinStudy.view.admin;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.entities.Pedido;
import study.course.VaadinStudy.services.PedidoService;
import study.course.VaadinStudy.view.components.AdminLayout;

@RolesAllowed("ROLE_ADMIN")
@PageTitle("Manage Pedidos")
@Route(value = "/admin/pedidos", layout = AdminLayout.class)
public class ManagePedidosView extends VerticalLayout {
    private final PedidoService pedidoService;
    private final Grid<Pedido> pedidoGrid;

    public ManagePedidosView(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
        this.pedidoGrid = new Grid<>(Pedido.class, false);

        pedidoGrid.addColumn(Pedido::getIdCliente).setHeader("ID Cliente").setSortable(true);
        pedidoGrid.addColumn(Pedido::getId).setHeader("ID Pedido").setSortable(true);
        pedidoGrid.addColumn(Pedido::getStatus).setHeader("Status").setSortable(true);
        pedidoGrid.addColumn(Pedido::getTotal).setHeader("Valor Produtos").setSortable(true);
        pedidoGrid.addColumn(Pedido::isPago).setHeader("Pago").setSortable(true);
        pedidoGrid.addColumn(Pedido::getDataCriacao).setHeader("Data criação").setSortable(true);
        pedidoGrid.addColumn(Pedido::getTipoDeEntrega).setHeader("Tipo entrega").setSortable(true);
        pedidoGrid.addComponentColumn(pedido -> new Button("Ver detalhes", event -> {
            // TODO: implementar modal ou redirecionamento
        })).setHeader("Ações");

        HorizontalLayout buscaInputEBtn = getBuscaInputEBtn(pedidoService);

        atualizarGrid();

        H2 titulo = new H2("Pedidos");
        add(titulo, buscaInputEBtn, pedidoGrid);
    }

    private HorizontalLayout getBuscaInputEBtn(PedidoService pedidoService) {
        TextField buscaInput = new TextField();
        buscaInput.setPlaceholder("Buscar por ID, status ou tipo de entrega");

        Button buscarBtn = new Button("Buscar", event -> {
            String termo = buscaInput.getValue().toLowerCase();
            pedidoGrid.setItems(pedidoService.findAllByTermoLike(termo));
        });

        return new HorizontalLayout(buscaInput, buscarBtn);
    }

    private void atualizarGrid(){
        pedidoGrid.setItems(pedidoService.findAll());
    }
}
