/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.aymeric.enchere;

/**
 *
 * @author aymer
 */
public class Personne 
{
   private int id_per;
   private String nom_per;
   private String prenom_per;
   private String email;
   private String codePost;
   private String mdp;
   
   public Personne(int id_per, String nom_per, String prenom_per, String email, String codePost, String mdp)
   {
      this.id_per = id_per;
      this.nom_per = nom_per;
      this.prenom_per = prenom_per;
      this.email = email;
      this.codePost = codePost;
      this.mdp = mdp;
   }

   public int getId_per() {
      return id_per;
   }

   public String getNom_per() {
      return nom_per;
   }

   public String getPrenom_per() {
      return prenom_per;
   }

   public String getEmail() {
      return email;
   }

   public String getCodePost() {
      return codePost;
   }

   public String getMdp() {
      return mdp;
   }

   public void setId_per(int id_per) {
      this.id_per = id_per;
   }

   public void setNom_per(String nom_per) {
      this.nom_per = nom_per;
   }

   public void setPrenom_per(String prenom_per) {
      this.prenom_per = prenom_per;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public void setCodePost(String codePost) {
      this.codePost = codePost;
   }

   public void setMdp(String mdp) {
      this.mdp = mdp;
   }
   
   
   
}
