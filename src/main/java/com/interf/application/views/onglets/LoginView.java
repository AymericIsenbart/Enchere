/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.interf.application.views.onglets;

/**
 *
 * @author Megaport
 */

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import javax.annotation.security.PermitAll;

@Route("login") 
@PageTitle("Login")
@AnonymousAllowed
@PermitAll
/*on rajoute BeforeEnterObserver afin que l'on tombe en premier sur la vue de login quand on arrive sur le site*/
public class LoginView extends VerticalLayout {

	private final LoginForm login = new LoginForm(); 

	public LoginView(){
		addClassName("login-view");
		setAlignItems(Alignment.CENTER);
		setJustifyContentMode(JustifyContentMode.CENTER);

                login.setAction("login"); 
                /*on ajoute un bouton afin de s'inscrire si jamais l'utilisateur n'a pas de comtpe*/
                
                
		add(new H1("Accès au projet"),
                        new H2("Ceci n'est qu'une page pour accéder au projet, vous pouvez vous connectez avec :"),
                        new H2("Identifiant: user"),
                        new H2("Mot de passe: userpass"),
                        login);
	}

	public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
		// inform the user about an authentication error
		if(beforeEnterEvent.getLocation()  
        .getQueryParameters()
        .getParameters()
        .containsKey("error")) {
            login.setError(true);
        }
	}
}
