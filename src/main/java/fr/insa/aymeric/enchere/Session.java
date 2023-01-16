/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.aymeric.enchere;

/**
 *
 * @author Xavier Weissenberger
 */
public class Session {
    private static int Session_id;
    
    public static void setId_session(int id) {
      Session_id = id;
   }
   
   public static int getId_session() {
      return Session_id;
   }
}
