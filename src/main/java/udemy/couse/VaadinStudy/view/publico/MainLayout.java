package udemy.couse.VaadinStudy.view.publico;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;

@Layout
public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout(){
        setPrimarySection(Section.DRAWER);
        createHeader();
        createDrawer();
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        this.viewTitle.setText(getPageTitle());
    }

    private void createHeader(){
        this.viewTitle = new H2();
        this.viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE, LumoUtility.Flex.GROW);

        DrawerToggle drawerToggle = new DrawerToggle();
        drawerToggle.addClassNames(LumoUtility.Padding.MEDIUM);

        var linkLogin = new SideNavItem("login", LoginView.class);
        var linkRegistrar = new SideNavItem("registrar", RegisterView.class);

        var header = new Header( drawerToggle, this.viewTitle, linkLogin, linkRegistrar);
        header.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.Display.FLEX,
                LumoUtility.Padding.End.MEDIUM, LumoUtility.Width.FULL);

        addToNavbar(false, header);
    }

    private void createDrawer(){
        setDrawerOpened(false);
        var sideNavItens = new VerticalLayout();

        var title = new H3("Título");

        var sideNav = new SideNav();

        var item1 = new SideNavItem("Main");
        var item2 = new SideNavItem("Cadastro e login");
        sideNav.addItem(item1, item2);

        var subItem11 = new SideNavItem("Main", MainView.class);
        item1.addItem(subItem11);

        var subItem21 = new SideNavItem("Login", LoginView.class);
        var subItem22 = new SideNavItem("Cadastro", RegisterView.class);
        item2.addItem(subItem21, subItem22);

        sideNavItens.add(title, sideNav);

        addToDrawer(sideNavItens);
    }

    private String getPageTitle(){
        if(getContent() == null){
            return "";
        } else if (getContent() instanceof HasDynamicTitle titleHolder){
            return titleHolder.getPageTitle();
        } else {
            var title = getContent().getClass().getAnnotation(PageTitle.class);
            return title == null ? "" : title.value();
        }
    }
}
