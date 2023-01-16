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
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.aymeric.enchere.Main;
import fr.insa.aymeric.enchere.Personne;
import static fr.insa.aymeric.enchere.Personne.TrouvePersonne;
import static fr.insa.aymeric.enchere.Personne.getIdPersonne;
import fr.insa.aymeric.enchere.Session;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;

/**
 *
 * @author Xavier Weissenberger
 */
@Route(value = "Infos", layout = MainLayout.class)
@PageTitle("Informations personnelles")
@PermitAll

public class Infouser extends VerticalLayout {
    
    public Infouser() throws ClassNotFoundException, SQLException{
        
        setSizeFull(); 
        setAlignItems(Alignment.CENTER);
	setJustifyContentMode(JustifyContentMode.CENTER);
        
        int id_user = Session.getId_session();
        Connection con = Main.connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "pass");
        
        Personne person = TrouvePersonne(con, id_user);
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
        
        PasswordField mdp2 = new PasswordField();
        mdp2.setLabel("Vérifier le mot de passe");
        mdp2.setValue(person.getMdp());
        mdp2.setRequiredIndicatorVisible(true);
        mdp2.setErrorMessage("Ce champ est requis");
        
        Button cancel = new Button("Annuler");
        cancel.addClickListener(e->UI.getCurrent().navigate(Accueil.class));
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);  
        
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
                    UI.getCurrent().navigate(Accueil.class);
                    
                    } catch (SQLException ex) {
                        Logger.getLogger(Cuisine.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY); 
        saveButton.addClickShortcut(Key.ENTER);
        HorizontalLayout noms = new HorizontalLayout();
        noms.add(prenom, nom);
        
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.add(cancel, saveButton);
        
        HorizontalLayout infos = new HorizontalLayout();
        infos.add(codepost, Email);
        
        HorizontalLayout mdp = new HorizontalLayout();
        mdp.add(mdp1, mdp2);
        
        add(noms, infos, mdp, buttonLayout);
}
}
