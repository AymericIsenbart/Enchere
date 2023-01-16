/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interf.application.views.onglets;

import com.interf.application.viewppl.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.aymeric.enchere.Enchere;
import fr.insa.aymeric.enchere.Main;
import fr.insa.aymeric.enchere.Personne;
import fr.insa.aymeric.enchere.Session;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;

/**
 *
 * @author Xavier Weissenberger
 */
@Route(value = "Voiture", layout = MainLayout.class)

/*on met un titre à notre page*/
@PageTitle("Voiture")
@PermitAll
public class Voitures extends VerticalLayout{
    public Voitures() {
        try {
        Connection con = Main.connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "pass");
        int id_proprio = Session. getId_session();
        List<Enchere> enche =  Enchere.getEnchereCat(con, "VOITURES");   
      
        ComponentRenderer <Component, Enchere> personCardRenderer = new ComponentRenderer<>(
            enchere -> {
                HorizontalLayout cardLayout = new HorizontalLayout();
                cardLayout.setMargin(true);

                VerticalLayout infoLayout = new VerticalLayout();
                /*infoLayout.setSpacing(false);
                infoLayout.setPadding(false);*/
                infoLayout.getElement().appendChild(ElementFactory.createStrong(enchere.getArt().getNom_art()));
                infoLayout.add(new Div(new Text(enchere.getArt().getDesc_art())));
                infoLayout.add(new Div(new Text(enchere.getPrixString()+"$")));
                
                NumberField prix = new NumberField();
                prix.setLabel("Prix proposé");
                Div euroSuffix = new Div();
                euroSuffix.setText("€");
                prix.setSuffixComponent(euroSuffix);
                prix.setRequiredIndicatorVisible(true);
                prix.setErrorMessage("Ce champ est requis"); 
                


                VerticalLayout dialogLayout = new VerticalLayout(prix);
                dialogLayout.setPadding(false);
                dialogLayout.setSpacing(false);
                dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
                dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        
                Dialog dialog = new Dialog();
                dialog.setHeaderTitle("Enchérir");
                dialog.add(dialogLayout);
               
                Button saveButton; 
                    saveButton = new Button("Valider", (event) -> { 
                    try{
                    int id_ach = Session.getId_session();
                    Personne acheteur = Personne.TrouvePersonne(con, id_ach);
                    int id_art = enchere.getArt().getIdArticle(con);
                    Enchere ench = Enchere.TrouveEnchere(con, id_art);
                    double prx = prix.getValue(); 
                    Enchere.Encherir(con, ench, acheteur, prx);
                    dialog.close();  
                    UI.getCurrent().getPage().reload();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cuisine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    });
                saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button cancelButton = new Button("Annuler", e -> dialog.close());
                dialog.getFooter().add(cancelButton);
                dialog.getFooter().add(saveButton);
                Button button = new Button("Enchérir", e -> dialog.open());
                button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                
                infoLayout.add(dialog);
                VerticalLayout contactLayout = new VerticalLayout();
                contactLayout.setSpacing(false);
                contactLayout.setPadding(false);
                contactLayout.add(new Div(new Text(enchere.getArt().getPer_art().getNom_per()+"  "+enchere.getArt().getPer_art().getPrenom_per())));
                contactLayout.add(new Div(new Text(enchere.getArt().getPer_art().getEmail())));
                infoLayout.add(new Details("Plus d'informations", contactLayout));
                infoLayout.add(button);
                
                cardLayout.add(infoLayout);
                return cardLayout;
                });
        
                VirtualList<Enchere> list = new VirtualList<>();
                list.setItems(enche);
                list.setRenderer(personCardRenderer);
                add(list);
                
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Cuisine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
