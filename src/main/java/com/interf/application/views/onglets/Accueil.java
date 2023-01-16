package com.interf.application.views.onglets;

import com.interf.application.viewppl.MainLayout;
import com.vaadin.flow.component.Key;
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
import fr.insa.aymeric.enchere.Recherche;
import javax.annotation.security.PermitAll;

@PermitAll
@PageTitle("Accueil")
@Route(value = "accueil", layout = MainLayout.class)
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
        search.addClickListener( e -> {
            Recherche.setMot(textField.getValue());
            UI.getCurrent().navigate(RechercheObj.class);
        });
        search.addClickShortcut(Key.ENTER);
        
        if(textField.getValue().equals("Cuisine")){
        search.addClickListener( e -> {
            Recherche.setMot(textField.getValue());
            UI.getCurrent().navigate(RechercheObj.class);
                });
        }
        layout.add(textField, search);
        layout.setFlexGrow(1,textField);
        setMargin(true);
        add(new H1("BIENVENUE !"), layout);
             
    }

}
