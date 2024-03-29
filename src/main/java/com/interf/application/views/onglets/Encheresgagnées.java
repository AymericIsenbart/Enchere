/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interf.application.views.onglets;

import com.interf.application.viewppl.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import fr.insa.aymeric.enchere.Enchere;
import fr.insa.aymeric.enchere.Main;
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
@Route(value = "Encheresgagnees", layout = MainLayout.class)
@PageTitle("Encheres gagnées")
@PermitAll
public class Encheresgagnées extends VerticalLayout{
   public Encheresgagnées() {
        try {
        Connection con = Main.connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "pass");
        int id_proprio = Session.getId_session();
        List<Enchere> enche =  Enchere.getMesoffresGagnées(con, id_proprio);   
      
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
                
                /*VerticalLayout contactLayout = new VerticalLayout();
                contactLayout.setSpacing(false);
                contactLayout.setPadding(false);
                contactLayout.add(new Div(new Text(enchere.getArt().getPer_art().getNom_per()+"  "+enchere.getArt().getPer_art().getPrenom_per())));
                contactLayout.add(new Div(new Text(enchere.getArt().getPer_art().getEmail())));
                infoLayout.add(new Details("Plus d'informations", contactLayout));*/
                
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

