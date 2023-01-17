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
import java.util.ArrayList;
import java.util.List;

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
      this.cat = cat.toUpperCase();
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
        return this.cat + " : " + this.nom_art + " : " + this.desc_art + ". (De : " + this.per_art.getNom_per() + " " + this.per_art.getPrenom_per() + ")";
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
                        cat varchar(20)
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
   
   public static void updateCat(Connection con, int id_art, String cat) throws SQLException
   {
      try(PreparedStatement pst = con.prepareStatement("""
            Update articles
            set cat=?
            where id_art=?"""))
       {
          pst.setString(1, cat);
          pst.setInt(2, id_art);
          
          pst.executeUpdate();
       }
   }
   
   public static void updateNom(Connection con, int id_art, String nom) throws SQLException
   {
      try(PreparedStatement pst = con.prepareStatement("""
            Update articles
            set nom_art=?
            where id_art=?"""))
       {
          pst.setString(1, nom);
          pst.setInt(2, id_art);
          
          pst.executeUpdate();
       }
   }
   
   public static void updateDesc(Connection con, int id_art, String desc) throws SQLException
   {
      try(PreparedStatement pst = con.prepareStatement("""
            Update articles
            set desc_art=?
            where id_art=?"""))
       {
          pst.setString(1, desc);
          pst.setInt(2, id_art);
          
          pst.executeUpdate();
       }
   }
   
   public static void updateProprio(Connection con, int id_art, Personne pers) throws SQLException
   {
      try(PreparedStatement pst = con.prepareStatement("""
            Update articles
            set id_proprietaire=?
            where id_art=?"""))
       {
          pst.setInt(1, pers.getIdPersonne(con));
          pst.setInt(2, id_art);
          
          pst.executeUpdate();
       }
   }
   
   public static void AfficheArticles(Connection con) throws SQLException 
   {
      List<Article> Lart = getAllArticle(con);
      
      System.out.println("");
      for(int i=0; i<Lart.size(); i++)
      {
         System.out.println(Lart.get(i));
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
   
    public static List<Article> getAllArticle(Connection con) throws SQLException
    {
      List<Article> Lart = new ArrayList<>();
       
      String nom;
      String desc;
      int id_proprio;
      String cat;
       
      try ( Statement st = con.createStatement()) 
      {
         try (ResultSet tlu = st.executeQuery("select * from articles")) 
         {
            while(tlu.next())
            {
               nom = tlu.getString(3);
               desc = tlu.getString(4);
               id_proprio = tlu.getInt(2);
               cat = tlu.getString(5);

               Lart.add(new Article(nom, desc, Personne.TrouvePersonne(con, id_proprio), cat));               
            }
         }
      }
      return Lart;
       
    }
    
   public static void AfficheArticlesPersonnes(Connection con, int id_per) throws SQLException 
   {
        List<Article> Lart = getAllArticle(con);
        
        System.out.println("");
        for(int i=0; i<Lart.size(); i++)
        {
           System.out.println(Lart.get(i));
        }
        System.out.println("");
    }
   
   public static void creerArticle(Connection con, Article art)
            throws SQLException
    {
        
        con.setAutoCommit(false);
                        
        try (PreparedStatement pst = con.prepareStatement(
               """
           insert into articles (id_proprietaire,nom_art,desc_art,cat) values (?,?,?,?)
           """, PreparedStatement.RETURN_GENERATED_KEYS)) 
          {
              pst.setInt(1, art.getPer_art().getIdPersonne(con));
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
      Enchere.SupprimeEnchere(con, idArt); // Enlève l'article de la liste des enchères     
       
       
      con.setAutoCommit(false); // Supprime l'article
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
         else
         {
            return -1;
         }
       }
       
   }
   
   public static int getIdArticle(Connection con, int id_proprio, String nom_art) throws SQLException 
   {
       try ( PreparedStatement chercheCondition = con.prepareStatement(
               "select id_art from articles where (nom_art=?) and (id_proprietaire=?)")) 
       {
         chercheCondition.setString(1, nom_art);
         chercheCondition.setInt(2, id_proprio);
         ResultSet test = chercheCondition.executeQuery();
        
         if(test.next())
         {
            return test.getInt(1);
         }
         else
         {
            return -1;
         }
       }
       
   }
   
   public static List<Article> getAllArticleProprio(Connection con, int id_proprio) throws SQLException
   {
      List<Article> Lart = new ArrayList<>();
      int id_art;
      
      try(PreparedStatement pst = con.prepareStatement("""
            select id_art from articles
            where id_proprietaire=?"""))
      {
         pst.setInt(1, id_proprio);
         ResultSet rst = pst.executeQuery();
         
         while(rst.next())
         {
            id_art = rst.getInt(1);
            Lart.add(TrouveArticle(con, id_art));
         }
      }
      
      return Lart;
   }
   
   public static List<Article> ListArticleAlea(Connection con, int n) throws SQLException
   {
      String[] cat = {"Technologie", "Cuisine", "Maison", "Vetement", "Bricolage", "Transport", "Jeux", "Puericulture",  "Musique", "Sport"};
      String[] nom = {"Telephone", "Smartphone", "Casque chantier", "Classeur", "Machine à laver", "Lave vaisselle", "Chaine HI-FI", "Roman", "Bande dessinée", "Statuette", "Batte", "Chaussures à crampons", "Protèges tibia", "Lunette de vision nocturne", "Jumelles", "Poële", "Visseuse", "Burineur", "Ponceuse", "Tournevis", "Marteau", "Radiateur", "Cale porte", "Cadre photo", "Enceinte portable", "Vélo elliptique", "Meuble télé", "Armoire", "Manteau", "Robinet", "Chaise", "Imprimante", "DVD", "Chaussettes", "Tshirt", "Bonnet en laine", "Jeu de société", "Cartes de visite", "tondeuse à barbe", "tondeuse à gazon", "Rasoir", "Tronçonneuse", "Tracteur", "Fiat Multipla"};;
      
      List<Personne> Lper = Personne.getAllPersonne(con);
      List<Article> Lart = new ArrayList<>();
      
      int a_nm;
      int a_cat;
      int a_pers;
      
      String Anom;
      String Acat;
      Personne per;
     
      double var;
      
      for(int i=0; i<n; i++)
      {
         var = Math.random()*(cat.length);
         Acat = cat[(int)var];
    
         var = Math.random()*nom.length;
         Anom = nom[(int)var];
         
         var = Math.random()*Lper.size();
         per = Lper.get((int)var);
        
         Lart.add(new Article(Anom, Anom, per, Acat));
      }
      return Lart;
   }
   
   public static List<Article> getArticleCat(Connection con, String cat) throws SQLException
    {
      List<Article> Lart = new ArrayList<>();
       
      String nom;
      String desc;
      int id_proprio;
       
      try(PreparedStatement pst = con.prepareStatement(
                        """
                        select * from articles
                        where cat = ?
                        """
                )){
                    pst.setString(1, cat);
                    ResultSet tlu= pst.executeQuery();
                    
            while(tlu.next())
            {
               nom = tlu.getString(3);
               desc = tlu.getString(4);
               id_proprio = tlu.getInt(2);
               
               Lart.add(new Article(nom, desc, Personne.TrouvePersonne(con, id_proprio), cat));               
            }
         }
      
      return Lart;
       
    }
     
   public static List<Article> getAllArticleSaufProprio(Connection con, int id_proprio) throws SQLException
   {
      List<Article> Lart = new ArrayList<>();
      int id_art;
      
      try(PreparedStatement pst = con.prepareStatement("""
            select id_art from articles
            where id_proprietaire!=?"""))
      {
         pst.setInt(1, id_proprio);
         ResultSet rst = pst.executeQuery();
         
         while(rst.next())
         {
            id_art = rst.getInt(1);
            Lart.add(TrouveArticle(con, id_art));
         }
      }
      
      return Lart;
   }
   
   public static List<Article> getAllArticleSaufVendus(Connection con, int id_proprio) throws SQLException
   {
      List<Article> Lart = new ArrayList<>();
      int id_art;
      
      try(PreparedStatement pst = con.prepareStatement("""
            select id_art from articles
            where id_proprietaire=? and not exists (
                select id_art 
                    from encheres
                        join articles
                            on encheres.id_art=articles.id_art)
                                                     
            """))
      {
         pst.setInt(1, id_proprio);
         ResultSet rst = pst.executeQuery();
         
         while(rst.next())
         {
            id_art = rst.getInt(1);
            Lart.add(TrouveArticle(con, id_art));
         }
      }
      
      return Lart;
   }
}
