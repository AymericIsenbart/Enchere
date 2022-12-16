/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.aymeric.enchere;

import java.sql.Connection;
import java.util.Optional;
import fr.insa.aymeric.enchere.Personne;
import java.sql.SQLException;

/**
 *
 * @author aymer
 */
public class SessionInfo 
{
   private Optional<Personne> curUser;
    private Connection conBdD;

    public SessionInfo() {
        this.curUser = Optional.empty();
        this.conBdD = null;
    }

    public boolean userConnected() {
        return this.curUser.isPresent();
    }

    public Optional<Personne> getCurUser() {
        return this.curUser;
    }

    public void setCurUser(Optional<Personne> curUser) {
        this.curUser = curUser;
    }

    public int getUserID() throws SQLException
    {
        return this.curUser.orElseThrow().getIdPersonne(conBdD);
    }

    /**
     * @return the conBdD
     */
    public Connection getConBdD() {
        return conBdD;
    }

    /**
     * @param conBdD the conBdD to set
     */
    public void setConBdD(Connection conBdD) {
        this.conBdD = conBdD;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return this.curUser.orElseThrow().getNom_per();
    }
}
