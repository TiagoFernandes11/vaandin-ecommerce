package study.course.VaadinStudy.view.components;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import study.course.VaadinStudy.view.publico.MainView;

@RolesAllowed("ROLE_ADMIN")
public class AdminLayout extends AppLayout {

    private H2 viewName;

    public AdminLayout(){
        setPrimarySection(Section.NAVBAR);
        createDrawer();
        createNavBar();
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        this.viewName.setText(getCurrentViewName());
    }

    private void createNavBar(){
        this.viewName = new H2();

        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassNames(LumoUtility.Padding.MEDIUM);

        viewName.addClassNames(LumoUtility.FontWeight.MEDIUM, LumoUtility.FontSize.LARGE);

        addToNavbar(drawerToggle, this.viewName);
    }

    private void createDrawer(){
        var title = new H3("Titulo");
        title.addClassNames(LumoUtility.Padding.SMALL, LumoUtility.FontSize.MEDIUM);

        var home = new SideNavItem("Home", MainView.class);
        var gerenciar = new SideNavItem("Gerenciar");
        var gerenciarAdministradores = new SideNavItem("Administradores", "/admin/administrators");
        var gerenciarUsuarios = new SideNavItem("Usuarios", "/admin/users");
        var gerenciarProdutos = new SideNavItem("Produtos", "/admin/products");
        var gerenciarCategorias = new SideNavItem("Categorias", "/admin/categories");

        gerenciarUsuarios.addClassNames(LumoUtility.FontSize.MEDIUM);
        gerenciarProdutos.addClassNames(LumoUtility.FontSize.MEDIUM);
        gerenciarAdministradores.addClassNames(LumoUtility.FontSize.MEDIUM);

        gerenciar.addItem( gerenciarAdministradores, gerenciarUsuarios, gerenciarProdutos, gerenciarCategorias);

        addToDrawer(title, gerenciar, home);
    }

    private String getCurrentViewName(){
        if(getContent() == null){
            return  "";
        } if(getContent() instanceof HasDynamicTitle title){
            return title.getPageTitle();
        } else {
            return getContent().getClass().getAnnotation(PageTitle.class).value();
        }
    }
}
