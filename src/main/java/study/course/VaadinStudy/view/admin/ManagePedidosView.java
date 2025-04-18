package study.course.VaadinStudy.view.admin;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
        atualizarGrid();

        H2 titulo = new H2("Pedidos");
        add(titulo, pedidoGrid);
    }

    private void atualizarGrid(){
        pedidoGrid.setItems(pedidoService.findAll());
    }
}
