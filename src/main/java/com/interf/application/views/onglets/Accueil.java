package com.interf.application.views.onglets;

import com.interf.application.viewppl.MainLayout;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("Accueil")
@Route(value = "accueil", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class Accueil extends VerticalLayout {

    public Accueil() {
        setSizeFull(); 
        setAlignItems(FlexComponent.Alignment.CENTER);
	setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        
        HorizontalLayout layout = new HorizontalLayout();
        layout.setPadding(true);
        TextField textField = new TextField();
        textField.setPlaceholder("Rechercher");
        textField.setClearButtonVisible(true);
        
        Button search = new Button(new Icon(VaadinIcon.SEARCH));
        search.addClickListener( e -> UI.getCurrent().navigate(Inscription.class));
        
        Select<String> select = new Select<>();
        select.setItems("Cuisine", "Habits", "Outillage", "Vélos", "Voitures");
        select.setPlaceholder("Catégorie");
        select.setEmptySelectionAllowed(true);
        
        if(textField.getValue().equals("Cuisine")){
        search.addClickListener( e -> UI.getCurrent().navigate(Cuisine.class));
        }
        layout.add(textField, select, search);
        layout.setFlexGrow(1,textField);
        setMargin(true);
        add(new H1("BIENVENUE !"), layout);
             
    }

}
