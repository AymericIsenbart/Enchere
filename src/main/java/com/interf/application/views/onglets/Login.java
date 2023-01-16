/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interf.application.views.onglets;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.aymeric.enchere.Main;
import fr.insa.aymeric.enchere.Session;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;

/**
 *
 * @author Xavier Weissenberger
 */
@PageTitle("login")
@Route(value = "")
@PermitAll
public class Login extends VerticalLayout {
    public Login() {
        setSizeFull(); 
	setAlignItems(Alignment.CENTER);
	setJustifyContentMode(JustifyContentMode.CENTER);

        PasswordField mdp1 = new PasswordField();
        mdp1.setLabel("Mot de passe");
        mdp1.setValue("");
        mdp1.setRequiredIndicatorVisible(true);
        mdp1.setErrorMessage("Ce champ est requis");
        
        TextField email = new TextField("Email");
        email.setRequiredIndicatorVisible(true);
        email.setErrorMessage("Ce champ est requis");
        email.setClearButtonVisible(true);
        
        Button buttonlog = new Button("Se connecter");
        buttonlog.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonlog.addClickShortcut(Key.ENTER);
        buttonlog.addClickListener( (event) -> {   
            try{   
                String mdpt = mdp1.getValue();
                String nom = email.getValue();
                ResultSet res;
                Connection con = Main.connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "pass");
                
                try(PreparedStatement pst = con.prepareStatement(
                        """
                        select * from personnes
                        where email = ?
                        """
                )){
                    pst.setString(1, nom);
                    res= pst.executeQuery();
                    res.next();
                    String mdp2=res.getString("mdp");
                    int id=res.getInt("id_per");
                    
                    if(mdpt.equals(mdp2)){
                        UI.getCurrent().navigate(Accueil.class);
                        Session.setId_session(id);
                    }else{
                      Notification.show("Mot de passe ou identifiant invalide");  
                    }      
                } catch (SQLException ex) {
                    Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                }             
            } catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
            }                     
        });
     
        Button button = new Button("S'inscrire");
        button.addThemeVariants(ButtonVariant.LUMO_TERTIARY_INLINE);
        button.addClickListener(e -> UI.getCurrent().navigate(Inscription.class)); 
        
        add(new H1("Enchères"), email, mdp1, button, buttonlog);
    }  
}
