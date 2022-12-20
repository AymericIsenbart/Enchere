/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package fr.insa.aymeric.enchere;


import fr.insa.aymeric.enchere.ressources.Lire;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;  
import java.util.ArrayList;
import java.util.List;

public class Enchere 
{   
   private Article art;
   private double prix;
   private String date_fin;
   private Personne enchereur;
   
   
   public Enchere(Article art, double prix, String date, Personne enchereur)
   {
      this.art = art;
      this.prix = prix;
      this.date_fin = date;
      this.enchereur = enchereur;
   }
   
   public Enchere(Article art, String date, Personne enchereur)
   {
      this.art = art;
      this.prix = 0.01;
      this.date_fin = date;
      this.enchereur = enchereur;
   }
   
   public Enchere(Article art, double prix, Personne enchereur)
   {
      this.art = art;
      this.prix = prix;
      this.date_fin = "2030-01-01";
      this.enchereur = enchereur;
   }
   
   public Enchere(Article art, Personne enchereur)
   {
      this.art = art;
      this.prix = 0.01;
      this.date_fin = "2030-01-01";
      this.enchereur = enchereur;
   }
   
   /**
    * @return the art
    */
   public Article getArt() {
      return art;
   }

   /**
    * @param art the art to set
    */
   public void setArt(Article art) {
      this.art = art;
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
   public void setPrix(float prix) {
      this.prix = prix;
   }

   /**
    * @return the date_fin
    */
   public String getDate_fin() {
      return date_fin;
   }

   /**
    * @param date_fin the date_fin to set
    */
   public void setDate_fin(String date_fin) {
      this.date_fin = date_fin;
   }

   /**
    * @return the enchereur
    */
   public Personne getEnchereur() {
      return enchereur;
   }

   /**
    * @param enchereur the enchereur to set
    */
   public void setEnchereur(Personne enchereur) {
      this.enchereur = enchereur;
   }
   
   
   @Override
    public String toString()
    {
        return this.art + " : " + this.prix + " pour " + this.enchereur + ". Termine le " + this.date_fin;
    }
   
   
   
    public static void CreerTableEnchere(Connection con) throws SQLException 
   {
        // je veux que le schema soit entierement créé ou pas du tout
        // je vais donc gérer explicitement une transaction
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) {
            // creation de la table Personne
            st.executeUpdate(
                    """
                    create table Encheres (
                        id_ench integer not null unique primary key
                        generated always as identity,
                        --
                        id_art integer not null,
                        id_acheteur integer not null,
                        prix_achat real not null,
                        date_fin date not null default '01.01.2025'
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
    
    public static void SupprimeTableEncheres(Connection con) throws SQLException 
    {
      try ( Statement st = con.createStatement()) 
      {
          try 
          {
              st.executeUpdate(
                      """
                  drop table Encheres
                  """);
          } catch (SQLException ex) 
          {
              // nothing to do : maybe the table was not created
          }
      }
    }
    
    public static List<Enchere> getAllEncheres (Connection con) throws SQLException
    {
       List<Enchere> Lench = new ArrayList<>();
       
       int id_art;
       int id_enchereur;
       double prx;
       String dte;
       
       try ( Statement st = con.createStatement()) 
      {
         try (ResultSet tlu = st.executeQuery("select * from encheres")) 
         {
            while(tlu.next())
            {
               id_art = tlu.getInt(2);
               id_enchereur = tlu.getInt(3);
               prx = tlu.getDouble(4);
               dte = tlu.getString(4);
               
               Lench.add(new Enchere(Article.TrouveArticle(con, id_art), prx, dte, Personne.TrouvePersonne(con, id_enchereur)));
            }
         }
      }
       
       return Lench;
    }
    
        
    public static void AfficheEncheres(Connection con) throws SQLException 
   {
      List<Enchere> Lench = getAllEncheres(con);
      
      System.out.println("");
      for(int i=0; i<Lench.size(); i++)
      {
         System.out.println(Lench.get(i));
      }
      System.out.println("");
    }
    
    public static void AfficheArticlesEncheres(Connection con) throws SQLException 
   {
      System.out.println("");
        try ( Statement st = con.createStatement()) {
            try ( ResultSet tlu = st.executeQuery("select id_art, prix_achat, date_fin, id_acheteur from encheres"))
            {
                while (tlu.next())
                {
                    int id_art = tlu.getInt(1);
                    Article art = Article.TrouveArticle(con, id_art);
                    
                    double prx = tlu.getDouble(2);
                    
                    String dt_fin = tlu.getString(3);
                    
                    int id_ach = tlu.getInt(4);
                    Personne ach = Personne.TrouvePersonne(con, id_ach);
                    
                    System.out.println(id_art + " : " + art.getNom_art() + " - " + prx + " - " + dt_fin + ". Acheteur : " + ach.getNom_per() + ", " + ach.getPrenom_per());
                }
            }
        }
        System.out.println("");
    }
    

    public static Enchere TrouveEnchere(Connection con, int id_art) throws SQLException 
    {
        try ( PreparedStatement chercheId = con.prepareStatement(
                "select * from encheres where id_art = ?")) 
        {
            chercheId.setInt(1, id_art);
            ResultSet testId = chercheId.executeQuery();
                        
            if(testId.next())
            {
               int id_ache = testId.getInt(3);
               double prx = testId.getDouble(4);
               String dt = testId.getString(5);
               
               Article art = Article.TrouveArticle(con, id_art);
               Personne acheteur = Personne.TrouvePersonne(con, id_ache);
               
               Enchere ench = new Enchere(art, prx, dt, acheteur);
               return ench;
            }    
        }
        return null;
    }
    
    public int getIdEnchere(Connection con) throws SQLException
    {
      try ( PreparedStatement chercheId = con.prepareStatement(
               "select id_ench from encheres where id_art = ?")) 
      {
         chercheId.setInt(1, this.getArt().getIdArticle(con));
         ResultSet testId = chercheId.executeQuery();
        
         if(testId.next())
         {
            return testId.getInt(1);
         }    
      }
      return -1;
    }
    
    
    public static int getIdEnchere(Connection con, Article art) throws SQLException 
   {
       try ( PreparedStatement chercheId = con.prepareStatement(
               "select id_ench from encheres where id_art = ?")) 
       {
         chercheId.setInt(1, art.getIdArticle(con));
         ResultSet testId = chercheId.executeQuery();
        
         if(testId.next())
         {
            return testId.getInt(1);
         }    
       }
       return 0;
   }
    
    public static class EnchereExisteDeja extends Exception {
    }
    
    public static void NouvelleEnchere(Connection con, Enchere ench)
            throws SQLException, EnchereExisteDeja
    {
        // je me place dans une transaction pour m'assurer que la séquence
        // test du nom - création est bien atomique et isolée
        con.setAutoCommit(false);
        
        try ( PreparedStatement chercheEnchere = con.prepareStatement(
                "select id_ench from encheres where id_art = ?")) 
        {
            chercheEnchere.setInt(1, ench.getArt().getIdArticle(con));
            
            ResultSet rsttest = chercheEnchere.executeQuery();
            if (rsttest.next()) 
            {
                throw new EnchereExisteDeja();
            }
            try ( PreparedStatement pst = con.prepareStatement(
                  """
              insert into encheres (id_art,id_acheteur,prix_achat,date_fin) values (?,?,?,?)
              """, PreparedStatement.RETURN_GENERATED_KEYS)) 
             {
                 pst.setInt(1, ench.getArt().getIdArticle(con));
                 pst.setInt(2, ench.getArt().getPer_art().getIdPersonne(con));
                 pst.setDouble(3, ench.getPrix());
                 pst.setDate(4, Date.valueOf(ench.getDate_fin()));
                 pst.executeUpdate();
                 con.commit();

                 try ( ResultSet rid = pst.getGeneratedKeys()) 
                 {
                     rid.next();
                 }
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
    }
    
    public static void SupprimeEnchere(Connection con) throws SQLException 
    {
       System.out.println("Afficher les enchère ?");
       int rep = Lire.i();
       
       if(rep == 1)
       {
          AfficheEncheres(con);
       }
       
       System.out.println("Sélectionner une enchère à supprimer");
       rep = Lire.i();
       
       
        con.setAutoCommit(false);
         try ( PreparedStatement pst = con.prepareStatement(
            "delete from encheres where id_art = ?"))
         {
           pst.setInt(1, rep);
           pst.executeUpdate();
         }
        finally 
        {
            con.setAutoCommit(true);
        }
    }
    
    public static void SupprimeEnchere(Connection con, int idArt) throws SQLException 
    {
        con.setAutoCommit(false);
         try ( PreparedStatement pst = con.prepareStatement(
            "delete from encheres where id_art = ?"))
         {
           pst.setInt(1, idArt);
           pst.executeUpdate();
         }
        finally 
        {
            con.setAutoCommit(true);
        }
    }
    
    
    public static double PrixArticleEnchere(Connection con, Enchere ench) throws SQLException
    {
       try(PreparedStatement chercheEnchere = con.prepareStatement(
                "select prix_achat from encheres where id_ench = ?")) 
       {
           chercheEnchere.setInt(1, ench.getIdEnchere(con, ench.getArt()));
           ResultSet testPrix = chercheEnchere.executeQuery();

           if(testPrix.next())
            {
               return testPrix.getDouble(1);
            }
       }
       catch (Exception ex)
       {

       }
       return -1;
    }
    
    public static void Encherir(Connection con, Enchere ench, Personne acheteur, double prx) throws SQLException 
    {
      if(prx > ench.getPrix())
      {
         try
         {
            con.setAutoCommit(false);
            // suppression de tous les anciens amours
            try ( PreparedStatement pst = con.prepareStatement(
               """
              update encheres
               set prix_achat = ?, id_acheteur = ?
               where id_ench = ?
              """)) 
            {
                pst.setDouble(1, prx);
                pst.setInt(2, acheteur.getIdPersonne(con));
                pst.setInt(3, ench.getIdEnchere(con, ench.getArt()));
                pst.executeUpdate();
            }

            con.commit();
         }
         catch (SQLException ex) 
         {
            con.rollback();
            throw ex;
         } 
         finally 
         {
            con.setAutoCommit(true);
         }  
    }
  }
  
  public static List<Enchere> ListEnchereAlea(Connection con, int n) throws SQLException
   {
      
      List<Personne> Lper = Personne.getAllPersonne(con);
      List<Article> Lart =Article.getAllArticle(con);
      
      List<Enchere> Lench = new ArrayList<>();
      
      if(n> Lart.size())
      {
         System.out.println("Il n'y a pas assez d'articles pour cela");
      }
      else
      {
         Article a_art;
         Personne a_per;
         double a_prix;
         String a_date;

         int a_annee;
         int a_mois;
         int a_jour;

         double var;

         for(int i=0; i<n; i++)
         {
            var = Math.random()*Lart.size();
            a_art = Lart.get((int)var);

            var = Math.random()*Lper.size();
            a_per = Lper.get((int)var);

            a_prix = 0.01 + Math.random()*1000;
            
            
            var = Math.random()*10;
            a_annee = 2023 + (int)var;
            var = Math.random()*12;
            a_mois = 1+ (int)var;
            var =  var = Math.random()*29;
            a_jour = 1 + (int)var;

            a_date = a_annee + "-" + a_mois + "-" + a_jour;         

            Lench.add(new Enchere(a_art, a_prix, a_date, a_per));
            
            Lart.remove(a_art);
         }
      }
      return Lench;
   }
      
    
    
    
    
}