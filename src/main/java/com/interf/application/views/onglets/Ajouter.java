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
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.aymeric.enchere.Article;
import fr.insa.aymeric.enchere.Enchere;
import fr.insa.aymeric.enchere.Main;
import fr.insa.aymeric.enchere.Personne;
import fr.insa.aymeric.enchere.Session;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.security.PermitAll;

/**
 *
 * @author Megaport
 */
/*ici on crée la route vers la calsse MainLayout afin que notre onglet aparaisse dans la vue principale*/
@Route(value = "Ajouter", layout = MainLayout.class)

/*on met un titre à notre page*/
@PageTitle("Ajouter une enchère")
@PermitAll
public class Ajouter extends VerticalLayout {
    public Ajouter(){
        
        TextField designation = new TextField("Désignation du produit");
        /*Création d'une zone pour rentrer le nom du produit*/
        designation.setRequiredIndicatorVisible(true);
        designation.setErrorMessage("Ce champ est recquis");
        /*Ces 2 lignes servent à afficher que le champs est requis s'il est vide*/
        designation.setClearButtonVisible(true);
        /*on ajoute ici un bouton pour effacer ce qui est écrit dans le champ*/
        
        /*on le refait pour chaque élément nécessaire*/
   
       
      
        /*il s'agit ici d'un champs spécial qui va nous permettre de savoir si l'adresse mail existe ou non*/
        EmailField Email = new EmailField();
        Email.setLabel("Adresse Email");
        Email.getElement().setAttribute("name", "email");
        Email.setErrorMessage("Enter a valid email address");
        Email.setClearButtonVisible(true);
        
        
        
        /*ce champ est une liste dans laquelle on pour sélectionner la catégorie de notre enchère*/
        Select<String> select = new Select<>();
        select.setLabel("Catégorie");
        select.setItems("Cuisine", "Habits", "Outillage","Voitures");
        select.setEmptySelectionAllowed(true);
        
        /*on aligne ici les boutons que l'on veut les uns à coté des autres*/
        /*HorizontalLayout noms = new HorizontalLayout();
        noms.add(prenom, nom);
        nom.setClearButtonVisible(true);
        HorizontalLayout dates = new HorizontalLayout();
        dates.add(datedebut, datefin); 
        /*HorizontalLayout infos = new HorizontalLayout();
        infos.add(num, Email);
        HorizontalLayout adress = new HorizontalLayout();
        adress.add(adresse, ville, dep);*/
        HorizontalLayout prod = new HorizontalLayout();
        prod.add(designation, select);
        
        /*ici on crée une aire de texte pour la descritpion*/
        TextArea textArea = new TextArea();
        textArea.setWidthFull();
        textArea.setMinHeight("100px");
        textArea.setMaxHeight("150px");
        textArea.setLabel("Description");
        
         
        
        
        /*on ajoute ensuite les boutons pour créer ou annuler l'enchere */
        
       
        Button cancel = new Button("Annuler");
        cancel.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancel.getStyle().set("margin-inline-end", "auto");
        cancel.addClickListener(e->UI.getCurrent().navigate(Accueil.class));
        
        Button createAccount = new Button("Créer");
        createAccount.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createAccount.addClickListener((event) -> {
            String nom_art = designation.getValue();
            String desc_art = textArea.getValue();
            String cat = select.getValue();
            
            try{
            Connection con = Main.connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "pass");
            /*int id_proprio = Personne.getIdPersonne(con);*/
            int id_proprio = Session.getId_session();
            Personne pers = Personne.TrouvePersonne(con, id_proprio);
            Article art = new Article(nom_art, desc_art, pers, cat);
            Article.creerArticle(con, art); 
            Notification.show("Article ajouté");
            UI.getCurrent().navigate(Accueil.class);
            }
            catch (SQLException ex) {
                 Notification.show("Problème BdD : " + ex.getLocalizedMessage());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(Inscription.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        createAccount.addClickShortcut(Key.ENTER);
        HorizontalLayout buttonLayout = new HorizontalLayout(cancel,
                createAccount);
        buttonLayout.getStyle().set("flex-wrap", "wrap");
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        
        /*il s'agit ici d'une checkbox permettant d'accepter les conditions d'utilisation (il n'y a en pas c'est juste un plus)*/
        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("J'accepte les conditions d'utilisation");
 
        /*il s'agit là d'une zone pour upload les photos du produit*/
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.addSucceededListener(event -> {
        String fileName = event.getFileName();
        InputStream inputStream = buffer.getInputStream(fileName);
        });
        /*enfin on ajoute tout sur la page*/       
        add(prod, textArea, upload, checkbox, buttonLayout);    
    }
}
