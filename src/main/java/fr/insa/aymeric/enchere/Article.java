/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.aymeric.enchere;

import static fr.insa.aymeric.enchere.Personne.AffichePersonnes;
import fr.insa.aymeric.enchere.ressources.Lire;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author aymer
 */
public class Article 
{
   private String cat;
   private String nom_art;
   private String desc_art;
   private Personne per_art;
   
   public Article(String nom_art, String desc_art, Personne perso, String cat)
   {
      this.nom_art = nom_art;
      this.desc_art = desc_art;
      this.per_art = perso;
      this.cat = cat;
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
   
   @Override
    public String toString()
    {
        return this.cat + " : " + this.nom_art + " : " + this.desc_art + ". Appartient à : " + this.per_art;
    }
   
   
   
   public static void CreerTableArticles(Connection con) throws SQLException 
   {
        // je veux que le schema soit entierement créé ou pas du tout
        // je vais donc gérer explicitement une transaction
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) {
            // creation de la table Personne
            st.executeUpdate(
                    """
                    create table Articles (
                        id_art integer not null unique primary key
                        generated always as identity,
                        --
                        id_proprietaire integer not null,
                        nom_art varchar(60) not null,
                        desc_art varchar(600),
                        cat varchar(2)
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
   
   public static void SupprimeTableArticles(Connection con) throws SQLException 
    {
      try ( Statement st = con.createStatement()) 
      {
          try {
              st.executeUpdate(
                      """
                  drop table Articles
                  """);
          } catch (SQLException ex) {
              // nothing to do : maybe the table was not created
          }
      }
   }
   
   public static void AfficheArticles(Connection con) throws SQLException 
   {
      System.out.println("");
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tlu = st.executeQuery("select * from articles")) {
                while (tlu.next()) {
                    // Ensuite, pour accéder à chaque colonne de la ligne courante,
                    // on a les méthode getInt, getString... en fonction du type
                    // de la colonne.

                    // on peut accéder à une colonne par son nom :
                    int id = tlu.getInt("id_art");
                    // ou par son numéro (la première colonne a le numéro 1)
                    
                    int idArt = tlu.getInt(1);
                    
                    int idPer = tlu.getInt(2);
                    String nom = tlu.getString(3);
                    String desc = tlu.getString(4);
                    String cat = tlu.getString(5);
                    
                    Personne perso = Personne.TrouvePersonne(con, idPer);
                    Article art = new Article(nom, desc, perso, cat);
                    
                    System.out.println(idArt + " : " + art);
                }
            }
        }
        System.out.println("");
    }
   
    public static Article TrouveArticle(Connection con, int id) throws SQLException 
    {
        try ( PreparedStatement chercheId = con.prepareStatement(
                "select * from articles where id_art = ?")) 
        {
           chercheId.setInt(1, id);
            ResultSet testId = chercheId.executeQuery();
            
            
            if(testId.next())
            {
               int id_proprietaire = testId.getInt(2);
               String nom_article = testId.getString(3);
               String desc = testId.getString(4);
               String cat = testId.getString(5);
               
               Personne proprio = Personne.TrouvePersonne(con, id_proprietaire);

               Article art = new Article(nom_article, desc, proprio, cat);
               
               return art;
            }    
        }
        return null;
    }
   
   public static void AfficheArticlesPersonnes(Connection con, int id_per) throws SQLException 
   {
        try ( Statement st = con.createStatement()) 
        {
            try ( ResultSet tlu = st.executeQuery(
            """
                     select id_art, id_proprietaire from Articles 
                           join Personnes on Personnes.id = Articles.id_proprietaire
                     """)) {
               
                while (tlu.next()) 
                {
                    int id_art =tlu.getInt(1);
                    int id_perso =tlu.getInt(2);
                
                                        
                    if (id_perso== id_per)
                    {
                        System.out.println("id art :" + id_art + " " + TrouveArticle(con, id_art));
                    }
                }
            }
        }
    }
   
   public static void creerArticle(Connection con, Article art, int id)
            throws SQLException
    {
        // je me place dans une transaction pour m'assurer que la séquence
        // test du nom - création est bien atomique et isolée
        con.setAutoCommit(false);
                        
        try (PreparedStatement pst = con.prepareStatement(
               """
           insert into articles (id_proprietaire,nom_art,desc_art,cat) values (?,?,?,?)
           """, PreparedStatement.RETURN_GENERATED_KEYS)) 
          {
              pst.setInt(1, id);
              pst.setString(2, art.getNom_art());
              pst.setString(3, art.getDesc_art());
              pst.setString(4, art.getCat());
              pst.executeUpdate();
              con.commit();
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
    }
   
   public static void SupprimeArticle(Connection con) throws SQLException 
    {
         System.out.println("Afficher les articles ? oui -> 1");
         int rst = Lire.i();

         if(rst == 1)
         {
            AfficheArticles(con);
            System.out.println("");
         }

         System.out.println("Quel article supprimer ? id :");
         int id = Lire.i();

         con.setAutoCommit(false);
         try ( PreparedStatement pst = con.prepareStatement(
                      "delete from articles where id_art = ?"))
         {
           pst.setInt(1, id);
           pst.executeUpdate();
         }
        finally 
        {
            con.setAutoCommit(true);
        }
    }
   
   public static void SupprimeArticle(Connection con, int idArt) throws SQLException 
    {
      con.setAutoCommit(false);
      try ( PreparedStatement pst = con.prepareStatement(
                   "delete from articles where id_art = ?"))
      {
         pst.setInt(1, idArt);
         pst.executeUpdate();
      }
      finally 
      {
         con.setAutoCommit(true);
      }
    }
   
   public int getIdArticle(Connection con) throws SQLException 
   {
       try ( PreparedStatement chercheCondition = con.prepareStatement(
               "select id_art from articles where (nom_art=?) and (id_proprietaire=?)")) 
       {
         chercheCondition.setString(1, this.getNom_art());
         chercheCondition.setInt(2, this.getPer_art().getIdPersonne(con));
         ResultSet test = chercheCondition.executeQuery();
        
         if(test.next())
         {
            return test.getInt(1);
         }    
       }
       return 0;
   }
}
