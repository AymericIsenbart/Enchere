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
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.aymeric.enchere.Article;
import static fr.insa.aymeric.enchere.Article.TrouveArticle;
import fr.insa.aymeric.enchere.Enchere;
import static fr.insa.aymeric.enchere.Enchere.SupprimeEnchere;
import static fr.insa.aymeric.enchere.Enchere.TrouveEnchere;
import fr.insa.aymeric.enchere.Main;
import fr.insa.aymeric.enchere.Personne;
import fr.insa.aymeric.enchere.Session;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;

/**
 *
 * @author Xavier Weissenberger
 */
@Route(value = "Mes_encheres", layout = MainLayout.class)
@PageTitle("Mes encheres")
@PermitAll
public class Mesencheres extends VerticalLayout{
    public Mesencheres() {
        try {
        Connection con = Main.connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "pass");
        int id_proprio = Session. getId_session();
        List<Enchere> enche =  Enchere.getMesEncheres(con, id_proprio);   
      
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
                
      
                VerticalLayout contactLayout = new VerticalLayout();
                contactLayout.setSpacing(false);
                contactLayout.setPadding(false);
                contactLayout.add(new Div(new Text(enchere.getEnchereur().getNom_per()+"  "+enchere.getEnchereur().getPrenom_per())));
                contactLayout.add(new Div(new Text(enchere.getEnchereur().getEmail())));
                infoLayout.add(new Details("Plus d'informations", contactLayout));
                
                
                 NumberField prix = new NumberField();
                prix.setLabel("Prix");
                Div euroSuffix = new Div();
                euroSuffix.setText("€");
                prix.setSuffixComponent(euroSuffix);
                prix.setRequiredIndicatorVisible(true);
                prix.setErrorMessage("Ce champ est requis");
                
                DatePicker datedebut = new DatePicker("Fin de l'enchère");
                datedebut.setRequiredIndicatorVisible(true);
                datedebut.setErrorMessage("Ce champ est requis");
                
                VerticalLayout dialogLayout = new VerticalLayout(prix, datedebut);
                dialogLayout.setPadding(false);
                dialogLayout.setSpacing(false);
                dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
                dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
                
                Dialog dialog = new Dialog();
                dialog.setHeaderTitle("Voulez vous vraiment supprimer cette enchère ?");
                
                Dialog dialog2 = new Dialog();
                dialog2.setHeaderTitle("Modifier les informations");
                                
                Button en = new Button(new Icon(VaadinIcon.PENCIL), e -> dialog2.open());
                en.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                
                Button valid2 = new Button("Oui", (event) -> {
                    
                    try {
                        LocalDate dat = datedebut.getValue();
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                        String dt = dat.format(formatter);
                        Double prx = prix.getValue();
                        int id_ench = Enchere.getIdEnchere(con, enchere.getArt());
                        Enchere.updatePrix(con, id_ench, prx);
                        Enchere.updateDate(con, id_ench, dt);
                        Notification.show("Enchère modifiée");
                        dialog2.close();
                        UI.getCurrent().getPage().reload();
                    } catch (SQLException ex) {
                        Logger.getLogger(Biens.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                               
                valid2.addThemeVariants(ButtonVariant.LUMO_ICON, ButtonVariant.LUMO_PRIMARY);
                Button cancelButton2 = new Button("Non", e -> dialog2.close());
                dialog2.getFooter().add(cancelButton2);
                dialog2.getFooter().add(valid2);
                dialog2.add(dialogLayout);
                
                Button valid = new Button("Oui", (event) -> {
                    try{
                    int id_art = enchere.getArt().getIdArticle(con);
                    SupprimeEnchere(con, id_art);
                    dialog.close(); 
                    UI.getCurrent().getPage().reload();
                    } catch (SQLException ex) {
                        Logger.getLogger(Biens.class.getName()).log(Level.SEVERE, null, ex);
                    }
                });
                valid.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                Button cancelButton = new Button("Non", e -> dialog.close());
                dialog.getFooter().add(cancelButton);
                dialog.getFooter().add(valid);
                
                Button closeButton = new Button(new Icon(VaadinIcon.TRASH),e -> dialog.open());
                closeButton.addThemeVariants(ButtonVariant.LUMO_ICON,  ButtonVariant.LUMO_ERROR);
                closeButton.getElement().setAttribute("aria-label", "Close");
                
                cardLayout.add(infoLayout, closeButton, en, dialog2, dialog);
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
