/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interf.application.views.onglets;

import com.interf.application.viewppl.MainLayout;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.aymeric.enchere.Main;
import fr.insa.aymeric.enchere.Personne;
import static fr.insa.aymeric.enchere.Personne.SupprimePersonne;
import static fr.insa.aymeric.enchere.Personne.getIdPersonne;
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
@Route(value = "Utilisateurs", layout = MainLayout.class)
@PageTitle("Utilisateurs")
@PermitAll
public class Utilisateurs extends VerticalLayout {
    public Utilisateurs() throws ClassNotFoundException, SQLException {
    Connection con = Main.connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "pass");
    
    Grid<Personne> grid = new Grid<>(Personne.class, false);
    grid.addColumn(Personne::getNom_per).setHeader("Nom").setSortable(true);
    grid.addColumn(Personne::getPrenom_per).setHeader("Prénom").setSortable(true);
    grid.addColumn(Personne::getEmail).setHeader("Email").setSortable(true);
    grid.addColumn(Personne::getCodePost).setHeader("Code postal").setSortable(true);
    grid.addColumn(Personne::getMdp).setHeader("Mot de passe").setSortable(true);
    grid.addColumn(
                 new ComponentRenderer<>(Button::new, (button, person) -> {          
        try {
            String email=person.getEmail();
            int id;
            id = getIdPersonne(con, email);
            button.addThemeVariants(ButtonVariant.LUMO_ICON,
                             ButtonVariant.LUMO_ERROR,
                             ButtonVariant.LUMO_TERTIARY);
                     button.addClickListener(e -> {
                 try {
                     SupprimePersonne(con, id);
                     UI.getCurrent().getPage().reload();
                 } catch (SQLException ex) {
                     Logger.getLogger(Utilisateurs.class.getName()).log(Level.SEVERE, null, ex);
                 }
             });
                     button.setIcon(new Icon(VaadinIcon.TRASH));       
        } catch (SQLException ex) {
            Logger.getLogger(Utilisateurs.class.getName()).log(Level.SEVERE, null, ex);
        }
      })).setHeader("Supprimer");               
        
     List<Personne> people = Personne.getAllPersonne(con);
     grid.setItems(people);
                grid.addColumn(
                new ComponentRenderer<>(Button::new, (button2, person) -> { 
                     
        TextField prenom = new TextField("Prénom");                    
        prenom.setRequiredIndicatorVisible(true);
        prenom.setValue(person.getPrenom_per());
        prenom.setErrorMessage("Ce champ est requis");
        prenom.setClearButtonVisible(true);
        
        TextField nom = new TextField("Nom");
        nom.setRequiredIndicatorVisible(true);
        nom.setValue(person.getNom_per());
        nom.setErrorMessage("Ce champ est requis");
        nom.setClearButtonVisible(true);
        
        EmailField Email = new EmailField();
        Email.setLabel("Adresse Email");
        Email.setValue(person.getEmail());
        Email.getElement().setAttribute("name", "email");
        Email.setRequiredIndicatorVisible(true);
        Email.setErrorMessage("Enter a valid email address");
        Email.setClearButtonVisible(true);
        
        TextField codepost = new TextField();
        codepost.setLabel("Code postal");
        codepost.setRequiredIndicatorVisible(true);
        codepost.setValue(person.getCodePost());
        codepost.setErrorMessage("Ce champ est requis");
        codepost.setClearButtonVisible(true);
        
        PasswordField mdp1 = new PasswordField();
        mdp1.setLabel("Mot de passe");
        mdp1.setValue(person.getMdp());
        mdp1.setRequiredIndicatorVisible(true);
        mdp1.setErrorMessage("Ce champ est requis");
        
        VerticalLayout dialogLayout = new VerticalLayout(prenom, nom, Email, codepost, mdp1);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");
        
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Changer les informations");
        dialog.add(dialogLayout);
     
                    Button saveButton; 
                    saveButton = new Button("Valider", (event) -> { 
                    try{
                    String email=person.getEmail();
                    int id;
                    id = getIdPersonne(con, email);
                    
                    person.updateNom(con, id, nom.getValue());
                    person.updatePrenom(con, id, prenom.getValue());
                    person.updateEmail(con, id, Email.getValue());
                    person.updateMdp(con, id, mdp1.getValue());
                    person.updateCP(con, id, codepost.getValue());
                    dialog.close();
                    UI.getCurrent().getPage().reload();
                    } catch (SQLException ex) {
                        Logger.getLogger(Cuisine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    });
                saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                saveButton.addClickShortcut(Key.ENTER);
                Button cancelButton = new Button("Annuler", e -> dialog.close());
                dialog.getFooter().add(cancelButton);
                dialog.getFooter().add(saveButton);
                add(dialog);
                
                button2.addClickListener(e -> dialog.open());
                button2.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                button2.setIcon(new Icon(VaadinIcon.PENCIL));
                
                 })).setHeader("Modifier");
                
    add(grid);
    }
}
