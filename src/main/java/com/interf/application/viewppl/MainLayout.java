package com.interf.application.viewppl;


import com.interf.application.components.appnav.AppNav;
import com.interf.application.components.appnav.AppNavItem;

import com.interf.application.security.SecurityService;
import com.interf.application.views.onglets.Accueil;
import com.interf.application.views.onglets.Ajouter;
import com.interf.application.views.onglets.Biens;
import com.interf.application.views.onglets.Cuisine;
import com.interf.application.views.onglets.Habits;
import com.interf.application.views.onglets.Infouser;
import com.interf.application.views.onglets.Login;
import com.interf.application.views.onglets.Mesencheres;
import com.interf.application.views.onglets.Mesoffres;
import com.interf.application.views.onglets.Outillage;
import com.interf.application.views.onglets.Utilisateurs;
import com.interf.application.views.onglets.Voitures;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
    
    private H2 viewTitle;

    public MainLayout(SecurityService securityService) {
        try {
            setPrimarySection(Section.DRAWER);
            addDrawerContent();
            addHeaderContent();
            createHeader();
            createDrawer();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainLayout.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainLayout.class.getName()).log(Level.SEVERE, null, ex);
        }
     
    }

    private void createHeader() throws ClassNotFoundException, SQLException {
        
        H1 logo = new H1("");
        /*on crée un bouton pour se déconnecter*/
        Button logout = new Button("Se déconnecter", e -> UI.getCurrent().navigate(Login.class)); 
        Button profile = new Button(new Icon(VaadinIcon.USER), e -> UI.getCurrent().navigate(Infouser.class));
        profile.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_PRIMARY);
        HorizontalLayout header = new HorizontalLayout(logo, logout, profile); 
        
                    
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo); 
        header.setWidth("100%");
        header.addClassNames("py-0", "px-m");

        addToNavbar(header);
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        
        
        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Enchères");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        addToDrawer(header, scroller, createFooter());
    }

    private Footer createFooter() {
        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
         
        private void createDrawer() {
        RouterLink listLink = new RouterLink("List", Accueil.class);
        listLink.setHighlightCondition(HighlightConditions.sameLocation());
        }
        
    private AppNav createNavigation() {
        // AppNav is not yet an official component.
        // For documentation, visit https://github.com/vaadin/vcf-nav#readme
        /*ici on ajoute dans la liste des onglets les vues que l'on veut voir apparaitre*/
        AppNav nav = new AppNav();
        nav.addItem(new AppNavItem("Accueil", Accueil.class, "la la-globe"));
        nav.addItem(new AppNavItem("Ajouter un article", Ajouter.class, "la la-plus"));
        nav.addItem(new AppNavItem("Mes articles", Biens.class, "la la-folder-open"));
        nav.addItem(new AppNavItem("Mes offres", Mesoffres.class, "la la-toolbox"));
        nav.addItem(new AppNavItem("Mes encheres", Mesencheres.class, "la la-dollar"));
        nav.addItem(new AppNavItem("Utilisateurs", Utilisateurs.class, "la la-user"));
        nav.addItem(new AppNavItem("Cuisine", Cuisine.class, "la la-coffee"));
        nav.addItem(new AppNavItem("Voitures", Voitures.class, "la la-car"));
        nav.addItem(new AppNavItem("Habits", Habits.class, "la la-gift"));
        nav.addItem(new AppNavItem("Outillage", Outillage.class, "la la-tools"));
        return nav;
    }
    
}
   
