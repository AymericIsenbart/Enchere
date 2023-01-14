/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interf.application.views.onglets;

import com.interf.application.viewppl.MainLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.Route;
import fr.insa.aymeric.enchere.Article;
import fr.insa.aymeric.enchere.Enchere;
import fr.insa.aymeric.enchere.Main;
import fr.insa.aymeric.enchere.Personne;
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
@Route(value = "Cuisine", layout = MainLayout.class)

/*on met un titre à notre page*/
@PageTitle("Cuisine")
@PermitAll
public class Cuisine extends VerticalLayout {
    
    public Cuisine() {
        try {
        Connection con = Main.connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "pass");
        List<Article> art = Article.getAllArticle(con);
                
        ComponentRenderer<Component, Article> personCardRenderer = new ComponentRenderer<>(
            article -> {
                HorizontalLayout cardLayout = new HorizontalLayout();
                cardLayout.setMargin(true);

                VerticalLayout infoLayout = new VerticalLayout();
                /*infoLayout.setSpacing(false);
                infoLayout.setPadding(false);*/
                infoLayout.getElement().appendChild(ElementFactory.createStrong(article.getNom_art()));
                infoLayout.add(new Div(new Text(article.getDesc_art())));
                
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
                    int id_ach = 2;
                    Personne acheteur = Personne.TrouvePersonne(con, id_ach);
                    int id_art = article.getIdArticle(con);
                    Enchere ench = Enchere.TrouveEnchere(con, id_art);
                    double prx = prix.getValue(); 
                    Enchere.Encherir(con, ench, acheteur, prx);
                    dialog.close();       
                    } catch (SQLException ex) {
                        Logger.getLogger(Cuisine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    });
                saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button cancelButton = new Button("Annuler", e -> dialog.close());
                dialog.getFooter().add(cancelButton);
                dialog.getFooter().add(saveButton);
                Button button = new Button("Enchérir", e -> dialog.open());
                Button closeButton = new Button(new Icon(VaadinIcon.TRASH));
                closeButton.addThemeVariants(ButtonVariant.LUMO_ICON);
                closeButton.getElement().setAttribute("aria-label", "Close");
                infoLayout.add(dialog, button);
               /* VerticalLayout contactLayout = new VerticalLayout();
                contactLayout.setSpacing(false);
                contactLayout.setPadding(false);
                contactLayout.add(new Div(new Text(article.getPer_art())));
                contactLayout
                        .add(new Div(new Text(person.getAddress().getPhone())));
                infoLayout
                        .add(new Details("Plus d'informations", contactLayout));*/

                cardLayout.add(infoLayout);
                return cardLayout;
                });
        
                VirtualList<Article> list = new VirtualList<>();
                list.setItems(art);
                list.setRenderer(personCardRenderer);
                add(list);
            
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(Cuisine.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}