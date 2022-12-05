/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interf.application.views.onglets;

import com.interf.application.viewppl.MainLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import fr.insa.aymeric.enchere.Personne;
import javax.annotation.security.RolesAllowed;

/**
 *
 * @author Xavier Weissenberger
 */
@Route(value = "Utilisateurs", layout = MainLayout.class)
@RouteAlias(value = "Utilisateurs", layout = MainLayout.class)
/*on limite l'accès à cette page aux administateurs*/
@RolesAllowed("ROLE_ADMIN")
@PageTitle("Utilisateurs")

public class Utilisateurs extends VerticalLayout {
    public Utilisateurs() {
        
    Grid<Personne> grid = new Grid<>(Personne.class, false);
    grid.setSelectionMode(Grid.SelectionMode.MULTI);
    grid.addColumn(Personne::getNom_per).setHeader("Nom").setSortable(true);
    grid.addColumn(Personne::getPrenom_per).setHeader("Prénom").setSortable(true);
    grid.addColumn(Personne::getEmail).setHeader("Email").setSortable(true);
    grid.addColumn(Personne::getCodePost).setHeader("Code postal").setSortable(true);
    grid.addColumn(Personne::getMdp).setHeader("Mot de passe").setSortable(true);
   

    Button editProfile = new Button("Modifier le profil");
    editProfile.setEnabled(false);

    Button managePermissions = new Button("Permissions");
    managePermissions.setEnabled(false);

    Button resetPassword = new Button("Réinitialiser le mot de passe");
    resetPassword.setEnabled(false);

    Button delete = new Button("Supprimer");
    delete.setEnabled(false);
    delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
    delete.getStyle().set("margin-inline-start", "auto");
    
    HorizontalLayout footer = new HorizontalLayout(editProfile,
    managePermissions, resetPassword, delete);
    footer.getStyle().set("flex-wrap", "wrap");

     add(grid, footer);
    }
}
