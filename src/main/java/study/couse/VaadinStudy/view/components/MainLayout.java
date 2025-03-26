package study.couse.VaadinStudy.view.components;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.HasDynamicTitle;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.security.core.context.SecurityContextHolder;
import study.couse.VaadinStudy.view.admin.ManageProductsView;
import study.couse.VaadinStudy.view.publico.LoginView;
import study.couse.VaadinStudy.view.publico.MainView;
import study.couse.VaadinStudy.view.publico.RegisterView;

@AnonymousAllowed
public class MainLayout extends AppLayout {

    private final transient AuthenticationContext authContext;
    private H2 viewTitle;

    public MainLayout(AuthenticationContext authContext){
        this.authContext = authContext;
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

        Header header = new Header();

        if(this.authContext.isAuthenticated()){
            String userName = SecurityContextHolder.getContext().getAuthentication().getName();
            Span loggedUser = new Span("Welcome back, " + userName);
            loggedUser.getStyle().set("margin-right", "20px");
            Button logout = new Button("logout", event -> {
                this.authContext.logout();
            });
            header.add(drawerToggle, this.viewTitle, loggedUser, logout);
        } else {
            header.add(drawerToggle, this.viewTitle, linkLogin, linkRegistrar);
        }

        addToNavbar(header);

        header.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.Display.FLEX,
                LumoUtility.Padding.End.MEDIUM, LumoUtility.Width.FULL);

        addToNavbar(false, header);
    }

    private void createDrawer(){
        setDrawerOpened(false);
        var sideNavItens = new VerticalLayout();

        var title = new H3("Título");

        var sideNav = new SideNav();

        var item1 = new SideNavItem("Home");
        var item2 = new SideNavItem("Cadastro e login");
        if(authContext.hasRole("ADMIN")){
            var item3 = new SideNavItem("Tela de administração", ManageProductsView.class);
            sideNav.addItem(item1, item2, item3);
        } else{
            sideNav.addItem(item1, item2);
        }

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
