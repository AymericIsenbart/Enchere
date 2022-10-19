/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.aymeric.enchere;

/**
 *
 * @author aymer
 */
public class Date 
{
   private int jour;
   private int mois;
   private int annee;
   
   public Date(int jour, int mois, int annee)
   {
      this.jour = jour;
      this.mois = mois;
      this.annee = annee;
   }

   /**
    * @return the jour
    */
   public int getJour() {
      return jour;
   }

   /**
    * @param jour the jour to set
    */
   public void setJour(int jour) {
      this.jour = jour;
   }

   /**
    * @return the mois
    */
   public int getMois() {
      return mois;
   }

   /**
    * @param mois the mois to set
    */
   public void setMois(int mois) {
      this.mois = mois;
   }

   /**
    * @return the annee
    */
   public int getAnnee() {
      return annee;
   }

   /**
    * @param annee the annee to set
    */
   public void setAnnee(int annee) {
      this.annee = annee;
   }
   
   
   
}
