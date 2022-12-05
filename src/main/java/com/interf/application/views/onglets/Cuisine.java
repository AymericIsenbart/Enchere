/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interf.application.views.onglets;

import com.interf.application.viewppl.MainLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import javax.annotation.security.PermitAll;

/**
 *
 * @author Xavier Weissenberger
 */
@Route(value = "Cuisine", layout = MainLayout.class)
@RouteAlias(value = "Cuisine", layout = MainLayout.class)
/*on autorise tout le monde à accéder à la page*/
@PermitAll
/*on met un titre à notre page*/
@PageTitle("Cuisine")

public class Cuisine extends VerticalLayout {
    public Cuisine(){
        add(new H1("KOUIZINE !") );
    }
}