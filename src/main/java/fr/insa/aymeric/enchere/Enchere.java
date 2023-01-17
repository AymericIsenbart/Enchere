/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package fr.insa.aymeric.enchere;


import static fr.insa.aymeric.enchere.Article.TrouveArticle;
import fr.insa.aymeric.enchere.Ressources.Utile;
import fr.insa.aymeric.enchere.ressources.Lire;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;  
import java.util.ArrayList;
import java.util.Collections;
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
      this.date_fin = "2030-11-01";
      this.enchereur = enchereur;
   }
   
   public Enchere(Article art, Personne enchereur)
   {
      this.art = art;
      this.prix = 0.01;
      this.date_fin = "2030-11-01";
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
   
   public String getPrixString() {
      return String.format("%f", prix, 2);
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
        return this.art + " : " + this.prix + "€ pour " + this.enchereur.getNom_per().toUpperCase() + " " + this.enchereur.getPrenom_per() + ". Termine le " + this.getDate_fin();
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
                        date_fin date not null default '01-01-2025',
                        enCours boolean default 'true'
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
               dte = tlu.getString(5);
               
               Lench.add(new Enchere(Article.TrouveArticle(con, id_art), prx, dte, Personne.TrouvePersonne(con, id_enchereur)));
            }
         }
      }
       
       return Lench;
    }
    
    public static List<Enchere> getAllEncheresInterf (Connection con) throws SQLException
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
               dte = tlu.getString(5);
               
               Lench.add(new Enchere(Article.TrouveArticle(con, id_art), prx, dte, Personne.TrouvePersonne(con, id_enchereur)));
            }
         }
      }
       
       return Lench;
    }
    
    public static boolean getStatut(Connection con, int id_art) throws SQLException
    {
        try (PreparedStatement pst = con.prepareStatement("""
            Select enCours from encheres
            where id_art=?"""))
        {
            pst.setInt(1, id_art);
            ResultSet rst = pst.executeQuery();
            
            while(rst.next())
            {
                return rst.getBoolean(1);
            }
        }
        return false;
    }
    
        
    public static void AfficheEncheres(Connection con) throws SQLException 
   {
      List<Enchere> Lench = getAllEncheres(con);
      
      System.out.println("");
      for(int i=0; i<Lench.size(); i++)
      {
         if(Lench.get(i).getArt().getPer_art() == null)
         {
            System.out.println(Lench.get(i).getIdEnchere(con) + " : problème avec le propriétaire");
         }
         else if(Lench.get(i).getEnchereur() == null)
         {
            System.out.println(Lench.get(i).getIdEnchere(con) + " : problème avec l'acheteur");
         }
         else
         {
            System.out.print(getIdEnchere(con, Lench.get(i).getArt())+ " " + Lench.get(i) + " -> ");
            System.out.println(getStatut(con, Lench.get(i).getArt().getIdArticle(con)));
         }
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
            else
            {
               return null;
            }
        }
    }
    
    
    public static Enchere TrouveEnchereId(Connection con, int id_ench) throws SQLException 
    {
        try ( PreparedStatement chercheId = con.prepareStatement(
                "select * from encheres where id_ench = ?")) 
        {
            chercheId.setInt(1, id_ench);
            ResultSet testId = chercheId.executeQuery();
                        
            if(testId.next())
            {
               int id_art = testId.getInt(2);
               int id_ache = testId.getInt(3);
               double prx = testId.getDouble(4);
               String dt = testId.getString(5);
               
               Article art = Article.TrouveArticle(con, id_art);
               Personne acheteur = Personne.TrouvePersonne(con, id_ache);
               
               Enchere ench = new Enchere(art, prx, dt, acheteur);
               return ench;
            }    
            else
            {
               return null;
            }
        }
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
         else
         {
            return -1;
         }
      }
      
    }
    
    public static int getIdEnchere(Connection con, int id_art) throws SQLException
    {
      try ( PreparedStatement chercheId = con.prepareStatement(
               "select id_ench from encheres where id_art = ?")) 
      {
         chercheId.setInt(1, id_art);
         ResultSet testId = chercheId.executeQuery();
        
         if(testId.next())
         {
            return testId.getInt(1);
         }    
         else
         {
            return -1;
         }
      }
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
         else
         {
            return 0;
         }
       }
   }
    
    public static class EnchereExisteDeja extends Exception {
    }
    
    public static void NouvelleEnchere(Connection con, Enchere ench)
            throws SQLException, EnchereExisteDeja
    {
        if(!Utile.dateDepassee(ench.getDate_fin()))
        {
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
                else
                {
                   try (PreparedStatement pst = con.prepareStatement(
                         """
                     insert into encheres (id_art,id_acheteur,prix_achat,date_fin,enCours) values (?,?,?,?,?)
                     """, PreparedStatement.RETURN_GENERATED_KEYS)) 
                    {
                        pst.setInt(1, ench.getArt().getIdArticle(con));
                        pst.setInt(2, ench.getEnchereur().getIdPersonne(con));
                        pst.setDouble(3, ench.getPrix());
                        pst.setDate(4, Date.valueOf(ench.getDate_fin()));
                        pst.setBoolean(5, true);
                        pst.executeUpdate();
                        con.commit();

                        try ( ResultSet rid = pst.getGeneratedKeys()) 
                        {
                            rid.next();
                        }
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
        else
        {
            System.out.println("Date déjà dépassée");
        }
        
        
    }
    
    public static void updateAcheteur(Connection con, int id_ench, int id_per) throws SQLException
    {
       try(PreparedStatement pst = con.prepareStatement("""
            Update encheres
            set id_acheteur=?
            where id_ench=?"""))
       {
          pst.setInt(1, id_per);
          pst.setInt(2, id_ench);
          
          pst.executeUpdate();
       }
    }
    
    public static void updateEnCours(Connection con, int id_ench, boolean statut) throws SQLException
    {
       try(PreparedStatement pst = con.prepareStatement("""
            Update encheres
            set enCours=?
            where id_ench=?"""))
       {
          pst.setBoolean(1, statut);
          pst.setInt(2, id_ench);
          
          pst.executeUpdate();
       }
    }
    
    public static void AutoUpdateEnCours(Connection con) throws SQLException
    {
       List<Enchere> Lench = getAllEncheres(con);
       
       String date_fin;
       int id_Ench;
       
       for(int i=0; i<Lench.size(); i++)
       {
           date_fin = Lench.get(i).getDate_fin();
           
           System.out.print(date_fin + " ");
           System.out.println(Utile.dateDepassee(date_fin) + "  an : " + Utile.getYear(date_fin));
           
           
           if(Utile.dateDepassee(date_fin))
           {
               id_Ench = getIdEnchere(con, Lench.get(i).getArt().getIdArticle(con));
               updateEnCours(con, id_Ench, false);
               
           }
       }
       changeProprio(con);
       
    }
    
    public static List<Enchere> getAllEnchereEnCours(Connection con) throws SQLException
    {
        List<Enchere> Lench = new ArrayList<>();
        
        try(PreparedStatement pst = con.prepareStatement("select id_ench from encheres where enCours=true"))
        {
            ResultSet rst = pst.executeQuery();
            
            while(rst.next())
            {
                Lench.add(TrouveEnchereId(con, rst.getInt(1)));
            }
        }
        
        return Lench;
    }
    
    public static List<Enchere> getAllEnchereFinie(Connection con) throws SQLException
    {
        List<Enchere> Lench = new ArrayList<>();
        
        try(PreparedStatement pst = con.prepareStatement("select id_ench from encheres where enCours=false"))
        {
            ResultSet rst = pst.executeQuery();
            
            while(rst.next())
            {
                System.out.println(rst.getInt(1));
                Lench.add(TrouveEnchereId(con, rst.getInt(1)));
            }
        }
        
        return Lench;
    }
    
    
    public static void updatePrix(Connection con, int id_ench, double prix) throws SQLException
    {
        
       
       try(PreparedStatement pst = con.prepareStatement("""
            update encheres
            set prix_achat = ?
            where id_ench = ?"""))
       {
          pst.setDouble(1, prix);
          pst.setInt(2, id_ench);
          
          pst.executeUpdate();
       }
    }
    
    public static void updateDate(Connection con, int id_ench, String date) throws SQLException
    {
       try(PreparedStatement pst = con.prepareStatement("""
            Update encheres
            set date_fin=to_date(?,'YYYYMMDD')
            where id_ench=?"""))
       {
          pst.setString(1, date);
          pst.setInt(2, id_ench);
          
          pst.executeUpdate();
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
         try (PreparedStatement pst = con.prepareStatement(
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
    
    public static List<Article> getArticleSansEnchere(Connection con) throws SQLException
    {
        List<Article> Lart =Article.getAllArticle(con);
        List<Enchere> Lench_creee = getAllEncheres(con);

        for(int j=0; j<Lench_creee.size(); j++)
        {
          /*if(Lart.contains(Lench_creee.get(j).getArt()))
          {
                 Lart.remove(Lench_creee.get(j).getArt());
          }*/
          Lart.remove(Lench_creee.get(j).getArt());
        }
        
        return Lart;
    }
    
    public static List<Enchere> getCatEnchere(Connection con, String cat) throws SQLException
    {
        if(cat.length() != 2)
        {
            return null;
        }
        else
        {
            List<String> cats = new ArrayList<>();
            Collections.addAll(cats, "Cu", "TE", "MA", "VE", "BR", "TR", "JE", "PU", "BE", "MU", "SP");
            
            int idArt;
            List<Enchere> Lench = new ArrayList();
                    
            if(cats.contains(cat))
            {
                try(PreparedStatement pst = con.prepareStatement("""
                    select encheres.id_art from encheres
                    join articles on articles.id_art = encheres.id_art
                    where articles.cat=?"""))
                {
                    pst.setString(1, cat);
                    ResultSet rst = pst.executeQuery();
                    
                    while(rst.next())
                    {
                        idArt = rst.getInt(1);
                        Lench.add(Enchere.TrouveEnchere(con, idArt));
                    }
                }
            }
            return Lench;
        }
    }
    
    public static List<Enchere> GetProprioEnchere(Connection con, int id_proprio) throws SQLException
    {
       List<Enchere> Lench = new ArrayList<>();
       Article art;
       
       int id_art;
       int id_ench;
       String date;
       double prix;
       
       
       try(PreparedStatement pst = con.prepareStatement("""
           select * from encheres
           left join articles on articles.id_art = encheres.id_art
           where id_proprietaire=?"""))
       {
          pst.setInt(1, id_proprio);
          
          ResultSet rst = pst.executeQuery();
          while(rst.next())
          {
             id_art = rst.getInt(2);
             id_ench = rst.getInt(3);
             prix = rst.getDouble(4);
             date = rst.getString(5);
             
             Lench.add(new Enchere(Article.TrouveArticle(con, id_art), prix, date, Personne.TrouvePersonne(con, id_ench)));            
          }
       }
       
       return Lench;
    }
    
    public static List<Enchere> getAllMisePersonne(Connection con, int id_pers) throws SQLException
    {
       List<Enchere> Lench = new ArrayList<>();
       
       int id_art;
      
       
       try(PreparedStatement pst = con.prepareStatement("""
               select id_art from encheres
               where id_acheteur=?"""))
       {
          pst.setInt(1, id_pers);
          
          ResultSet rst = pst.executeQuery();
          while(rst.next())
          {
             id_art = rst.getInt(1);
             
             Article art = Article.TrouveArticle(con, id_art);
             
             Lench.add(TrouveEnchere(con, id_art));
          }
          
       }
       
       return Lench;
    }
    
    public static void SupprimeEnchereProprio(Connection con, Personne pers) throws SQLException
    {
       int id_perso = pers.getIdPersonne(con);
       List<Enchere> Lench = GetProprioEnchere(con, id_perso);
       
       int id_ench;
       
       for(int i=0; i<Lench.size(); i++)
       {
          id_ench = Lench.get(i).getIdEnchere(con);
          try (PreparedStatement pst = con.prepareStatement("""
                     delete from encheres
                     where id_ench=?
                     """))  
         {
            pst.setInt(1, id_ench);
            pst.executeUpdate();
         }
       }    
    }
    
    public static void SupprimeAllEnchereProprio(Connection con, int id_pers) throws SQLException
    {
      List<Enchere> Lench = GetProprioEnchere(con, id_pers);
       
      int id_ench;

      for(int i=0; i<Lench.size(); i++)
      {
         id_ench = Lench.get(i).getIdEnchere(con);
         try (PreparedStatement pst = con.prepareStatement("""
                    delete from encheres
                    where id_ench=?
                    """))  
         {
            pst.setInt(1, id_ench);
            pst.executeUpdate();
         }
      }     
    }
    
    public static void SupprimeAllMisePersonne(Connection con, int id_pers) throws SQLException
    {
       List<Enchere> Lench = getAllMisePersonne(con, id_pers);
       int id_proprio;
       
       for(int i=0; i<Lench.size(); i++)
       {
          id_proprio = Lench.get(i).getArt().getPer_art().getIdPersonne(con);
          updateAcheteur(con, Lench.get(i).getIdEnchere(con), id_proprio);
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
    
   public static void Encherir(Connection con, int id_ench, int id_acheteur, double prx) throws SQLException 
   {
      if(prx > TrouveEnchereId(con, id_ench).getPrix())
      {
         updateAcheteur(con, id_ench, id_acheteur);
         updatePrix(con, id_ench, prx);
      }
  }

    
   public static void Encherir(Connection con, Enchere ench, Personne acheteur, double prx) throws SQLException 
   {
      System.out.println("N°ench : " + ench.getIdEnchere(con));
      System.out.println("Id_per : " + acheteur.getIdPersonne(con));
      System.out.println("Prix : " + prx);
      
      if(prx > ench.getPrix())
      {
         updateAcheteur(con, ench.getIdEnchere(con), acheteur.getIdPersonne(con));
         updatePrix(con, ench.getIdEnchere(con), prx);
      }
  }
  
  public static List<Enchere> ListEnchereAlea(Connection con, int n) throws SQLException
   {
      List<Personne> Lper = Personne.getAllPersonne(con);
      List<Article> Lart = getArticleSansEnchere(con);
            
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

            a_prix = Lire.troncature(0.01 + Math.random()*1000, n);
            
            var = Math.random()*10;
            a_annee = 2023 + (int)var;
            var = Math.random()*12;
            a_mois = 1+ (int)var;
            var =  var = Math.random()*29;
            a_jour = 1 + (int)var;

            a_date = a_annee + "-";
            if(a_mois<10)
            {
                a_date = a_date + "0" + a_mois + "-";
            }
            else
            {
                a_date = a_date + a_mois + "-";
            }
            if(a_jour<10)
            {
                a_date = a_date + "0" + a_jour;
            }
            else
            {
                a_date = a_date + a_jour;
            }         

            Lench.add(new Enchere(a_art, Lire.troncature(a_prix, 2), a_date, a_per));
            
            Lart.remove(a_art);
         }
      }
      return Lench;
   }
  
  public static List<Enchere> getEncheresSaufProprio(Connection con, int id_proprio) throws SQLException
    {
       List<Enchere> Lench = new ArrayList<>();
       
       int id_art;
       int id_enchereur;
       double prx;
       String dte;
       
       try ( PreparedStatement pst = con.prepareStatement( 
               """
                 select * from encheres
                 join articles 
                    on encheres.id_art=articles.id_art
                        where id_proprietaire!=? and enCours=true
                 """
       ))
      {
         pst.setInt(1, id_proprio);
         /*pst.setString(2, categorie);*/
         ResultSet tlu = pst.executeQuery(); 
         
            while(tlu.next())
            {
               id_art = tlu.getInt(2);
               id_enchereur = tlu.getInt(3);
               prx = tlu.getDouble(4);
               dte = tlu.getString(5);
               
               Lench.add(new Enchere(Article.TrouveArticle(con, id_art), prx, dte, Personne.TrouvePersonne(con, id_enchereur)));
            }
         }
       
       return Lench;
    }
  public static List<Enchere> getMesoffres(Connection con, int id_proprio) throws SQLException
    {
       List<Enchere> Lench = new ArrayList<>();
       
       int id_art;
       int id_enchereur;
       double prx;
       String dte;
       
       try ( PreparedStatement pst = con.prepareStatement( 
               """
                 select * from encheres
                        where id_acheteur=? and enCours=true
                 """
       ))
      {
         pst.setInt(1, id_proprio);
         /*pst.setString(2, categorie);*/
         ResultSet tlu = pst.executeQuery(); 
         
            while(tlu.next())
            {
               id_art = tlu.getInt(2);
               id_enchereur = tlu.getInt(3);
               prx = tlu.getDouble(4);
               dte = tlu.getString(5);
               
               Lench.add(new Enchere(Article.TrouveArticle(con, id_art), prx, dte, Personne.TrouvePersonne(con, id_enchereur)));
            }
         }
       
       return Lench;
    }
  public static List<Enchere> getMesoffresGagnées(Connection con, int id_proprio) throws SQLException
    {
       List<Enchere> Lench = new ArrayList<>();
       
       int id_art;
       int id_enchereur;
       double prx;
       String dte;
       
       try ( PreparedStatement pst = con.prepareStatement( 
               """
                 select * from encheres
                        where id_acheteur=? and enCours=false
                 """
       ))
      {
         pst.setInt(1, id_proprio);
         /*pst.setString(2, categorie);*/
         ResultSet tlu = pst.executeQuery(); 
         
            while(tlu.next())
            {
               id_art = tlu.getInt(2);
               id_enchereur = tlu.getInt(3);
               prx = tlu.getDouble(4);
               dte = tlu.getString(5);
               
               Lench.add(new Enchere(Article.TrouveArticle(con, id_art), prx, dte, Personne.TrouvePersonne(con, id_enchereur)));
            }
         }
       
       return Lench;
    }
  
  public static List<Enchere> getMesEncheres(Connection con, int id_proprio) throws SQLException
    {
       List<Enchere> Lench = new ArrayList<>();
       
       int id_art;
       int id_enchereur;
       double prx;
       String dte;
       
       try ( PreparedStatement pst = con.prepareStatement( 
               """
                 select * from encheres
                    join articles
                    on encheres.id_art=articles.id_art
                        where id_proprietaire=? and enCours=true
                 """
       ))
      {
         pst.setInt(1, id_proprio);
         /*pst.setString(2, categorie);*/
         ResultSet tlu = pst.executeQuery(); 
         
            while(tlu.next())
            {
               id_art = tlu.getInt(2);
               id_enchereur = tlu.getInt(3);
               prx = tlu.getDouble(4);
               dte = tlu.getString(5);
               
               Lench.add(new Enchere(Article.TrouveArticle(con, id_art), prx, dte, Personne.TrouvePersonne(con, id_enchereur)));
            }
         }
       
       return Lench;
    }
  
  public static List<Enchere> getMesEncheresVendues(Connection con, int id_proprio) throws SQLException
    {
       List<Enchere> Lench = new ArrayList<>();
       
       int id_art;
       int id_enchereur;
       double prx;
       String dte;
       
       try ( PreparedStatement pst = con.prepareStatement( 
               """
                 select * from encheres
                    join articles
                    on encheres.id_art=articles.id_art
                        where id_proprietaire=? and enCours=true
                 """
       ))
      {
         pst.setInt(1, id_proprio);
         /*pst.setString(2, categorie);*/
         ResultSet tlu = pst.executeQuery(); 
         
            while(tlu.next())
            {
               id_art = tlu.getInt(2);
               id_enchereur = tlu.getInt(3);
               prx = tlu.getDouble(4);
               dte = tlu.getString(5);
               
               Lench.add(new Enchere(Article.TrouveArticle(con, id_art), prx, dte, Personne.TrouvePersonne(con, id_enchereur)));
            }
         }
       
       return Lench;
    }
  
  public static List<Enchere> getRecherche(Connection con, String mot) throws SQLException
    {
       List<Enchere> Lench = new ArrayList<>();
       
       int id_art;
       int id_enchereur;
       double prx;
       String dte;
       
       try ( PreparedStatement pst = con.prepareStatement( 
               """
                 select * from encheres
                    join articles
                    on encheres.id_art=articles.id_art
                        where nom_art like ? and enCours=true
                 """
       ))
      {
         pst.setString(1, mot);
         /*pst.setString(2, categorie);*/
         ResultSet tlu = pst.executeQuery(); 
         
            while(tlu.next())
            {
               id_art = tlu.getInt(2);
               id_enchereur = tlu.getInt(3);
               prx = tlu.getDouble(4);
               dte = tlu.getString(5);
               
               Lench.add(new Enchere(Article.TrouveArticle(con, id_art), prx, dte, Personne.TrouvePersonne(con, id_enchereur)));
            }
         }
       
       return Lench;
    }
  
  public static List<Enchere> getEnchereCat(Connection con, String mot) throws SQLException
    {
       List<Enchere> Lench = new ArrayList<>();
       
       int id_art;
       int id_enchereur;
       double prx;
       String dte;
       
       try ( PreparedStatement pst = con.prepareStatement( 
               """
                 select * from encheres
                    join articles
                    on encheres.id_art=articles.id_art and enCours=true
                        where cat like ? 
                 """
       ))
      {
         pst.setString(1, mot);
         /*pst.setString(2, categorie);*/
         ResultSet tlu = pst.executeQuery(); 
         
            while(tlu.next())
            {
               id_art = tlu.getInt(2);
               id_enchereur = tlu.getInt(3);
               prx = tlu.getDouble(4);
               dte = tlu.getString(5);
               
               Lench.add(new Enchere(Article.TrouveArticle(con, id_art), prx, dte, Personne.TrouvePersonne(con, id_enchereur)));
            }
         }
       
       return Lench;
    }
  
  public static void changeProprio(Connection con) throws SQLException
  {
      
      System.out.println("");
      List<Enchere> Lench = getAllEnchereFinie(con);
      for(int i=0; i<Lench.size(); i++)
      {
          System.out.println(Lench.get(i));
      }
      
      for(int i=0; i<Lench.size(); i++)
      {
          Article.updateProprio(con, Lench.get(i).getArt().getIdArticle(con), Lench.get(i).getEnchereur());
      }
  }
}