/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.aymeric.enchere;

import fr.insa.aymeric.enchere.ressources.Lire;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author aymer
 */
public class Personne 
{
   private String nom_per;
   private String prenom_per;
   private String email;
   private String codePost;
   private String mdp;
   
   public Personne(String nom_per, String prenom_per, String email, String codePost, String mdp)
   {
      this.nom_per = nom_per;
      this.prenom_per = prenom_per;
      this.email = email;
      this.codePost = codePost;
      this.mdp = mdp;
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
   
   @Override
   public String toString()
   {
       return this.nom_per + " " + this.prenom_per + ", " + this.codePost + ", "+ this.email + ": " + this.mdp;
   }
    
    
    public static void CreerTablePersonnes(Connection con) throws SQLException 
   {
        // je veux que le schema soit entierement créé ou pas du tout
        // je vais donc gérer explicitement une transaction
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) {
            // creation de la table Personne
            st.executeUpdate(
                    """
                    create table Personnes (
                        id_per integer not null unique primary key
                        generated always as identity,
                        --
                        nom_perso varchar(30) not null,
                        prenom varchar(30) not null,
                        email varchar(70) not null unique,
                        codePost varchar (8) not null,
                        mdp varchar(30) not null,
                        statut boolean default 'false'
                    )
                    """);
        }
        finally
        {
            // je reviens à la gestion par défaut : une transaction pour
            // chaque ordre SQL
            con.setAutoCommit(true);
        }
   }
    
    public static void SupprimeTablePersonnes(Connection con) throws SQLException 
    {
      try ( Statement st = con.createStatement()) 
      {
          // pour être sûr de pouvoir supprimer, il faut d'abord supprimer les liens
          // puis les tables
          // suppression des liens
          /*try 
          {
              st.executeUpdate(
                      """
                  alter table aime
                      drop constraint fk_aime_u1
                           """);
              System.out.println("constraint fk_aime_u1 dropped");
          } catch (SQLException ex) {
              // nothing to do : maybe the constraint was not created
          }
          try {
              st.executeUpdate(
                      """
                  alter table aime
                      drop constraint fk_aime_u2
                  """);
              System.out.println("constraint fk_aime_u2 dropped");
          } catch (SQLException ex) {
              // nothing to do : maybe the constraint was not created
          }*/
          // je peux maintenant supprimer les tables
          try {
              st.executeUpdate(
                      """
                  drop table Personnes
                  """);
          } catch (SQLException ex) {
              // nothing to do : maybe the table was not created
          }
      }
   }
    
    // exemple de requete à la base de donnée
    public static void AffichePersonnes(Connection con) throws SQLException {
       System.out.println("");
        try ( Statement st = con.createStatement()) {
            try (ResultSet tlu = st.executeQuery("select * from personnes")) {
                while (tlu.next()) {
                    // Ensuite, pour accéder à chaque colonne de la ligne courante,
                    // on a les méthode getInt, getString... en fonction du type
                    // de la colonne.

                    // on peut accéder à une colonne par son nom :
                    int id = tlu.getInt(1);
                    // ou par son numéro (la première colonne a le numéro 1)
                    
                    String nom = tlu.getString(2);
                    String prenom = tlu.getString(3);
                    String email = tlu.getString(4);
                    String CP = tlu.getString(5);
                    String Mdp = tlu.getString(6);
                    System.out.println(id + " : " + nom + " " + prenom + " " + CP + " (" + email + ")" + " : " + Mdp);
                }
            }
        }
        System.out.println("");
    }
    
     // exemple de requete à la base de donnée
    public static Personne TrouvePersonne(Connection con, int id) throws SQLException 
    {
        try ( PreparedStatement chercheId = con.prepareStatement(
                "select * from personnes where id_per = ?")) 
        {
           chercheId.setInt(1, id);
            ResultSet testId = chercheId.executeQuery();
            
            
            if(testId.next())
            {
               String nom = testId.getString(2);
               String prenom = testId.getString(3);
               String email = testId.getString(4);
               String cp = testId.getString(5);
               String mdp = testId.getString(6);

               Personne perso = new Personne(nom, prenom, email, cp, mdp);
               return perso;
            }    
        }
        return null;
    }
    
    public static class EmailExisteDeja extends Exception {
    }
    
    // lors de la création d'un utilisateur, l'identificateur est automatiquement
    // créé par le SGBD.
    // on va souvent avoir besoin de cet identificateur dans le programme,
    // par exemple pour gérer des liens "aime" entre utilisateur
    // vous trouverez ci-dessous la façon de récupérer les identificateurs
    public static int NouvellePersonne(Connection con, Personne pers)
            throws SQLException, EmailExisteDeja
    {
        // je me place dans une transaction pour m'assurer que la séquence
        // test du nom - création est bien atomique et isolée
        con.setAutoCommit(false);
        
        try ( PreparedStatement chercheMail = con.prepareStatement(
                "select id_per from personnes where email = ?")) 
        {
            chercheMail.setString(1, pers.getEmail());
            ResultSet testMail = chercheMail.executeQuery();
            if (testMail.next()) 
            {
                throw new EmailExisteDeja();
            }
            try ( PreparedStatement pst = con.prepareStatement(
                  """
              insert into personnes (nom_perso,prenom,email,codepost,mdp) values (?,?,?,?,?)
              """, PreparedStatement.RETURN_GENERATED_KEYS)) 
             {
                 pst.setString(1, pers.getNom_per());
                 pst.setString(2, pers.getPrenom_per());
                 pst.setString(3, pers.getEmail());
                 pst.setString(4, pers.getCodePost());
                 pst.setString(5, pers.getMdp());
                 pst.executeUpdate();
                 con.commit();
            }
         }
         catch (Exception ex)
         {
            con.rollback();
            throw ex; 
         }
         finally 
         {
            con.setAutoCommit(true);
         }
        
        return 0;
    }
    
    /*public static void SupprimePersonne(Connection con) throws SQLException 
    {
       System.out.println("Afficher les personnes ? oui -> 1");
       int rst = Lire.i();
       
       if(rst == 1)
       {
          AffichePersonnes(con);
          System.out.println("");
       }
       
       System.out.println("Qui supprimer ? id :");
       int id = Lire.i();
       
       
      // On supprime toutes les enchères où l'utilisateur est propriétaire
      con.setAutoCommit(false);
      try ( PreparedStatement pst = con.prepareStatement(
                    "Select id_art from articles where id_proprietaire=?"))
      {
         pst.setInt(1, id);
         ResultSet rs = pst.executeQuery();
         
         int idArt;
         
         while(rs.next())
         {
            idArt = rs.getInt(1); 
            
            try (PreparedStatement pst2 = con.prepareStatement(
            "Delete from encheres where id_art=?"))
            {
               pst2.setInt(1, idArt);
               pst2.execute();
            }
         }
      }
      finally 
      {
          con.setAutoCommit(true);
      }
       
       

       con.setAutoCommit(false);
       try ( PreparedStatement pst = con.prepareStatement(
                    "delete from personnes where id = ?"))
       {
                pst.setInt(1, id);
                pst.executeUpdate();
            }
      finally 
      {
          con.setAutoCommit(true);
      }
    }*/
   
   public int getIdPersonne(Connection con) throws SQLException 
   {
       try ( PreparedStatement chercheEmail = con.prepareStatement(
               "select * from personnes where email = ?")) 
       {
         chercheEmail.setString(1, this.getEmail());
         ResultSet testEmail = chercheEmail.executeQuery();
        
         if(testEmail.next())
         {
            return testEmail.getInt(1);
         }    
       }
       return 0;
   }
   
   public void MiseEnEnchere(Connection con) throws SQLException 
   {
      int id_per = this.getIdPersonne(con);
      
   }
   
   
   public void RemplirTableEnch(Connection con, int n)
   {
      String[] prenom;
      String[] nom;
      String[] mdp;
      
      //prenom = ["Adele", "Albane", "Anais", "Axel", "Agathe", "Adrien", "Alice", "Amelia", "Anna", "Apolline", "Augustin", "Ava", "Candice", "Arthur", "Mae", "Manon", "Mathias", "Maelle", "Martin", "Lois", "Louis", "Louka", "Mateo", "Lena", "Lucas", "Marceau", "Matteo", "Valentin", "Robin"];
      
      //List<String> supplierNames = new List<String>();
      
   }
}
