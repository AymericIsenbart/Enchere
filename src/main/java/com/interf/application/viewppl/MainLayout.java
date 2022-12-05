package com.interf.application.viewppl;


import com.interf.application.components.appnav.AppNav;
import com.interf.application.components.appnav.AppNavItem;

import com.interf.application.security.SecurityService;
import com.interf.application.views.onglets.Accueil;
import com.interf.application.views.onglets.Ajouter;
import com.interf.application.views.onglets.Cuisine;
import com.interf.application.views.onglets.Utilisateurs;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.HighlightConditions;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;


/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {
    
    private H2 viewTitle;
    private SecurityService securityService;

    public MainLayout(SecurityService securityService) {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
        this.securityService = securityService;
        createHeader();
        createDrawer();
     
    }

    private void createHeader() {
        H1 logo = new H1("");
        /*on crée un bouton pour se déconnecter*/
        Button logout = new Button("Se déconnecter", e -> securityService.logout()); 

        HorizontalLayout header = new HorizontalLayout(logo, logout); 

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
        nav.addItem(new AppNavItem("Ajouter une enchère", Ajouter.class, "la la-plus"));
        nav.addItem(new AppNavItem("Utilisateurs", Utilisateurs.class, "la la-user"));
        nav.addItem(new AppNavItem("Cuisine", Cuisine.class, "la la-heart"));
        return nav;
    }
    
}
   
