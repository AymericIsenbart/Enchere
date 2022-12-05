/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interf.application.views.onglets;

import com.interf.application.viewppl.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.Div;
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
import com.vaadin.flow.router.RouteAlias;
import java.io.InputStream;
import javax.annotation.security.PermitAll;

/**
 *
 * @author Megaport
 */
/*ici on crée la route vers la calsse MainLayout afin que notre onglet aparaisse dans la vue principale*/
@Route(value = "Ajouter", layout = MainLayout.class)
@RouteAlias(value = "Ajouter", layout = MainLayout.class)
/*on autorise tout le monde à accéder à la page*/
@PermitAll
/*on met un titre à notre page*/
@PageTitle("Ajouter une enchère")

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
        
        TextField prenom = new TextField("Prénom");
        prenom.setRequiredIndicatorVisible(true);
        prenom.setErrorMessage("Ce champ est requis");
        prenom.setClearButtonVisible(true);
        
        TextField nom = new TextField("Nom");
        nom.setRequiredIndicatorVisible(true);
        nom.setErrorMessage("Ce champ est requis");
       
        TextField adresse = new TextField("Numéro et rue");
        adresse.setRequiredIndicatorVisible(true);
        adresse.setErrorMessage("Ce champ est requis");
        adresse.setClearButtonVisible(true);
        
        TextField ville = new TextField("Ville");
        ville.setRequiredIndicatorVisible(true);
        ville.setErrorMessage("Ce champ est requis");
        ville.setClearButtonVisible(true);
        
        /*ici le champs n'accepte que les numéros*/
        NumberField dep = new NumberField();
        dep.setLabel("Code postal");
        dep.setRequiredIndicatorVisible(true);
        dep.setErrorMessage("Ce champ est requis");
        dep.setClearButtonVisible(true);
        
        /*On ajoute ici un champ permettant de sélection facilement un date*/
        DatePicker datedebut = new DatePicker("Début de l'enchère");
        datedebut.setRequiredIndicatorVisible(true);
        datedebut.setErrorMessage("Ce champ est requis");
        
        
        DatePicker datefin = new DatePicker("Fin de l'enchère");
        datefin.setRequiredIndicatorVisible(true);
        datefin.setErrorMessage("Ce champ est requis");
        
        /*il s'agit ici d'un champs spécial qui va nous permettre de savoir si l'adresse mail existe ou non*/
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
        
        /*ce champ est une liste dans laquelle on pour sélectionner la catégorie de notre enchère*/
        Select<String> select = new Select<>();
        select.setLabel("Catégorie");
        select.setItems("Cuisine", "Habits", "Outillage", "Vélos", "Voitures");
        select.setEmptySelectionAllowed(true);
        
        /*on aligne ici les boutons que l'on veut les uns à coté des autres*/
        HorizontalLayout noms = new HorizontalLayout();
        noms.add(prenom, nom);
        nom.setClearButtonVisible(true);
        HorizontalLayout dates = new HorizontalLayout();
        dates.add(datedebut, datefin); 
        HorizontalLayout infos = new HorizontalLayout();
        infos.add(num, Email);
        HorizontalLayout adress = new HorizontalLayout();
        adress.add(adresse, ville, dep);
        HorizontalLayout prod = new HorizontalLayout();
        prod.add(designation, select);
        
        /*ici on crée une aire de texte pour la descritpion*/
        TextArea textArea = new TextArea();
        textArea.setWidthFull();
        textArea.setMinHeight("100px");
        textArea.setMaxHeight("150px");
        textArea.setLabel("Description");
        
        /*on ajoute ensuite les boutons pour supprimer l'enchere, la créer ou annuler*/
        Button delete = new Button("Supprimer");
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.getStyle().set("margin-inline-end", "auto");

        Button cancel = new Button("Annuler");

        Button createAccount = new Button("Créer");
        createAccount.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout buttonLayout = new HorizontalLayout(delete, cancel,
                createAccount);
        buttonLayout.getStyle().set("flex-wrap", "wrap");
        buttonLayout.setJustifyContentMode(JustifyContentMode.END);
        
        /*il s'agit ici d'une checkbox permettant d'accepter les conditions d'utilisation (il n'y a en pas c'est juste un plus)*/
        Checkbox checkbox = new Checkbox();
        checkbox.setLabel("J'accepte les conditions d'utilisation");
        
        /*ici on ajoute un champ avec un petit logo euro pour le prix*/
        NumberField prix = new NumberField();
        prix.setLabel("Prix");
        Div euroSuffix = new Div();
        euroSuffix.setText("€");
        prix.setSuffixComponent(euroSuffix);
        prix.setRequiredIndicatorVisible(true);
        prix.setErrorMessage("Ce champ est requis");
        
        /*il s'agit là d'une zone pour upload les photos du produit*/
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);
        upload.addSucceededListener(event -> {
        String fileName = event.getFileName();
        InputStream inputStream = buffer.getInputStream(fileName);
        });
        /*enfin on ajoute tout sur la page*/       
        add(prod, noms, infos, adress, dates, prix, textArea, upload, checkbox, buttonLayout);    
    }
}
