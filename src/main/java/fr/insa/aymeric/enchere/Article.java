/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.aymeric.enchere;

/**
 *
 * @author aymer
 */
public class Article 
{
   private String cat;
   private int id_art;
   private String nom_art;
   private String desc_art;
   private Date date_fin;
   private float prix_mini;
   private Personne per_art;
   
   public Article(int id_art, String nom_art, String desc_art, float prix_ini, Date date_fin, Personne perso)
   {
      this.id_art = id_art;
      this.nom_art = nom_art;
      this.desc_art = desc_art;
      this.prix_mini = prix_ini;
      this.date_fin = date_fin;
      this.per_art = perso;
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
    * @return the id_art
    */
   public int getId_art() {
      return id_art;
   }

   /**
    * @param id_art the id_art to set
    */
   public void setId_art(int id_art) {
      this.id_art = id_art;
   }

   /**
    * @return the nom_art
    */
   public String getNom_art() {
      return nom_art;
   }

   /**
    * @param nom_art the nom_art to set
    */
   public void setNom_art(String nom_art) {
      this.nom_art = nom_art;
   }

   /**
    * @return the desc_art
    */
   public String getDesc_art() {
      return desc_art;
   }

   /**
    * @param desc_art the desc_art to set
    */
   public void setDesc_art(String desc_art) {
      this.desc_art = desc_art;
   }

   /**
    * @return the date_fin
    */
   public Date getDate_fin() {
      return date_fin;
   }

   /**
    * @param date_fin the date_fin to set
    */
   public void setDate_fin(Date date_fin) {
      this.date_fin = date_fin;
   }

   /**
    * @return the prix_mini
    */
   public float getPrix_mini() {
      return prix_mini;
   }

   /**
    * @param prix_mini the prix_mini to set
    */
   public void setPrix_mini(float prix_mini) {
      this.prix_mini = prix_mini;
   }

   /**
    * @return the per_art
    */
   public Personne getPer_art() {
      return per_art;
   }

   /**
    * @param per_art the per_art to set
    */
   public void setPer_art(Personne per_art) {
      this.per_art = per_art;
   }
   
   
   
}
