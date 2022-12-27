/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.aymeric.enchere;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author aymer
 */
public class InterfEnchere 
{
   private int id_ench;
   private String nom_art;
   private String desc_art;
   private int id_proprio;
   private int id_acheteur;
   private String cat;
   private double prix;
   
   public InterfEnchere(int id_ench, String nom_art, String desc, int id_acheteur, int id_proprio, String cat, double prix)
   {
      this.id_ench = id_ench;
      this.nom_art = nom_art;
      this.desc_art = desc;
      this.id_acheteur = id_acheteur;
      this.id_proprio = id_proprio;
      this.cat = cat;
      this.prix = prix;
   }

   public void setId_ench(int id_ench) {
      this.id_ench = id_ench;
   }

   public void setNom_art(String nom_art) {
      this.nom_art = nom_art;
   }

   public void setDesc_art(String desc_art) {
      this.desc_art = desc_art;
   }

   public void setId_proprio(int id_proprio) {
      this.id_proprio = id_proprio;
   }

   public void setId_acheteur(int id_acheteur) {
      this.id_acheteur = id_acheteur;
   }

   public int getId_ench() {
      return id_ench;
   }

   public String getNom_art() {
      return nom_art;
   }

   public String getDesc_art() {
      return desc_art;
   }

   public int getId_proprio() {
      return id_proprio;
   }

   public int getId_acheteur() {
      return id_acheteur;
   }
   
   /**
    * @return the cat
    */
   public String getCat() {
      return cat;
   }

   /**
    * @param cat the cat to set
    */
   public void setCat(String cat) {
      this.cat = cat;
   }

   /**
    * @return the prix
    */
   public double getPrix() {
      return prix;
   }

   /**
    * @param prix the prix to set
    */
   public void setPrix(double prix) {
      this.prix = prix;
   }
   
   
   
   
   
   
   public String toString(Connection con) throws SQLException
   {
      String nom_proprio = Personne.TrouvePersonne(con, this.getId_proprio()).getNom_per();
      String prenom_proprio = Personne.TrouvePersonne(con, this.getId_proprio()).getPrenom_per();
      
      String nom_ach = Personne.TrouvePersonne(con, this.getId_acheteur()).getNom_per();
      String prenom_ach = Personne.TrouvePersonne(con, this.getId_acheteur()).getPrenom_per();
      
      
      String rstt = this.getId_ench() + "    ";
      rstt = rstt + this.getNom_art() + " : " + this.getDesc_art() + " ";
      rstt = rstt + this.getPrix() + "€ (Propriétaire : " + nom_proprio.toUpperCase() + " " + prenom_proprio;
      rstt = rstt + " - Pour : " + nom_ach.toUpperCase() + " " + prenom_ach + ")";
      
            
      return rstt;
   }
   
   public static InterfEnchere getInterEnchere(Connection con, Enchere ench) throws SQLException
   {
      int id_ench = ench.getIdEnchere(con);
      int id_proprio = ench.getArt().getPer_art().getIdPersonne(con);
      int id_ach = ench.getEnchereur().getIdPersonne(con);
      String nom_art = ench.getArt().getNom_art();
      String desc= ench.getArt().getDesc_art();
      String cat = ench.getArt().getCat();
      double prix = ench.getPrix();
      
      InterfEnchere interfEnch = new InterfEnchere(id_ench, nom_art, desc, id_ench, id_proprio, cat, prix);
      
      return interfEnch;
   }
   
   public static List<InterfEnchere> getAllEnchere(Connection con, List<Enchere> Lench) throws SQLException
   {
      List<InterfEnchere> LenchInter = new ArrayList<>();
      
      for(int i=0; i<Lench.size(); i++)
      {
         LenchInter.add(getInterEnchere(con, Lench.get(i)));
      }
      
      return LenchInter;
   }
}
