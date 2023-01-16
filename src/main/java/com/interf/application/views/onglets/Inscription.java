/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interf.application.views.onglets;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.aymeric.enchere.Main;
import fr.insa.aymeric.enchere.Personne;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author Xavier Weissenberger
 */
/*on ne la met pas dans le MainLayout car on ne veut pas qu'elle y apparaisse*/ 
@Route(value = "Inscription")
/*tout le monde peut accéder à la page*/

@PageTitle("S'inscrire")
@Configuration
@PermitAll
public class Inscription extends VerticalLayout {
 
    public Inscription() {
        
        /*on setup la page pour que les composants soient bien centrés*/
        setSizeFull(); 
        setAlignItems(Alignment.CENTER);
	setJustifyContentMode(JustifyContentMode.CENTER);
        
        /*on crée les champs à la manière de la classe Ajouter*/
        TextField prenom = new TextField("Prénom");
        prenom.setRequiredIndicatorVisible(true);
        prenom.setErrorMessage("Ce champ est requis");
        prenom.setClearButtonVisible(true);
        
        TextField nom = new TextField("Nom");
        nom.setRequiredIndicatorVisible(true);
        nom.setErrorMessage("Ce champ est requis");
        nom.setClearButtonVisible(true);
        
        EmailField Email = new EmailField();
        Email.setLabel("Adresse Email");
        Email.getElement().setAttribute("name", "email");
        Email.setRequiredIndicatorVisible(true);
        Email.setErrorMessage("Enter a valid email address");
        Email.setClearButtonVisible(true);
        
        TextField codepost = new TextField();
        codepost.setLabel("Code postal");
        codepost.setRequiredIndicatorVisible(true);
        codepost.setErrorMessage("Ce champ est requis");
        codepost.setClearButtonVisible(true);
        
        PasswordField mdp1 = new PasswordField();
        mdp1.setLabel("Mot de passe");
        mdp1.setValue("");
        mdp1.setRequiredIndicatorVisible(true);
        mdp1.setErrorMessage("Ce champ est requis");
        
        PasswordField mdp2 = new PasswordField();
        mdp2.setLabel("Vérifier le mot de passe");
        mdp2.setValue("");
        mdp2.setRequiredIndicatorVisible(true);
        mdp2.setErrorMessage("Ce champ est requis");
        
        Button delete = new Button("Supprimer");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.getStyle().set("margin-inline-end", "auto");

        Button cancel = new Button("Annuler");
        cancel.addClickListener(e->UI.getCurrent().navigate(Login.class));
        Button createAccount = new Button("Créer");
        createAccount.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createAccount.addClickListener((event) -> {
            String nom_per = nom.getValue();
            String prenom_per = prenom.getValue();
            String email = Email.getValue();
            String codePost = codepost.getValue();
            String mdp = mdp1.getValue();
            
            try {
            Connection con = Main.connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "pass");
            Personne pers = new Personne(nom_per, prenom_per, email,  codePost, mdp);
            Personne.NouvellePersonne(con, pers);
            Notification.show("Utilisateur " + nom_per + " créé");
            UI.getCurrent().navigate(Login.class);
            }
            catch (SQLException ex) {
                 Notification.show("Problème BdD : " + ex.getLocalizedMessage());
            }
            catch (Personne.EmailExisteDeja ex) {
                 Notification.show("Cet email existe déjà, choississez en un autre");
                 
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Inscription.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        createAccount.addClickShortcut(Key.ENTER);
        HorizontalLayout buttonLayout = new HorizontalLayout(delete, cancel,
                createAccount);
        buttonLayout.getStyle().set("flex-wrap", "wrap");
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        
        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("J'accepte les conditions d'utilisation");
        
        HorizontalLayout noms = new HorizontalLayout();
        noms.add(prenom, nom);
        
        HorizontalLayout infos = new HorizontalLayout();
        infos.add(codepost, Email);
        
        HorizontalLayout mdp = new HorizontalLayout();
        mdp.add(mdp1, mdp2);
        
        add(new H1("INSCRIPTION"), noms, infos, mdp, checkbox, buttonLayout);   
    }
}