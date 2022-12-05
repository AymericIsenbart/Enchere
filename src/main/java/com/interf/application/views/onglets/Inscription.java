/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interf.application.views.onglets;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import javax.annotation.security.PermitAll;

/**
 *
 * @author Xavier Weissenberger
 */
/*on ne la met pas dans le MainLayout car on ne veut pas qu'elle y apparaisse*/ 
@Route(value = "Inscription")
/*tout le monde peut accéder à la page*/
@PermitAll
@PageTitle("S'inscrire")
@AnonymousAllowed
public class Inscription extends VerticalLayout {
    
    public Inscription(){
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
        Email.setErrorMessage("Enter a valid email address");
        Email.setClearButtonVisible(true);
        
        TextField num = new TextField();
        num.setLabel("Numéro de téléphone");
        num.setHelperText("Inclure les préfixes de pays");
        num.setRequiredIndicatorVisible(true);
        num.setErrorMessage("Ce champ est requis");
        num.setClearButtonVisible(true);
        
        Button delete = new Button("Supprimer");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.getStyle().set("margin-inline-end", "auto");

        Button cancel = new Button("Annuler");

        Button createAccount = new Button("Créer");
        createAccount.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createAccount.addClickListener( e -> UI.getCurrent().navigate(LoginView.class));
        
        HorizontalLayout buttonLayout = new HorizontalLayout(delete, cancel,
                createAccount);
        buttonLayout.getStyle().set("flex-wrap", "wrap");
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        
        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("J'accepte les conditions d'utilisation");
        
        HorizontalLayout noms = new HorizontalLayout();
        noms.add(prenom, nom);
        
        HorizontalLayout infos = new HorizontalLayout();
        infos.add(num, Email);
        
        add(new H1("INSCRIPTION"), noms, infos,checkbox, buttonLayout);   
    }
}