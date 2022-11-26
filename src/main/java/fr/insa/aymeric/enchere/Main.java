/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.aymeric.enchere;

import fr.insa.aymeric.enchere.ressources.Lire;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author aymer
 */
public class Main 
{
    public static Connection connectGeneralPostGres(String host,
            int port, String database,
            String user, String pass)
            throws ClassNotFoundException, SQLException {
        Class.forName("org.postgresql.Driver");
        Connection con = DriverManager.getConnection(
                "jdbc:postgresql://" + host + ":" + port
                + "/" + database,
                user, pass);
        con.setTransactionIsolation(Connection.TRANSACTION_SERIALIZABLE);
        return con;
    }
   
   public static Connection defautConnect()
            throws ClassNotFoundException, SQLException 
   {
        return connectGeneralPostGres("localhost", 5439, "postgres", "postgres", "pass");
   }
   public static void menu(Connection con)
   {
        int rep = -1;
        while (rep != 0) 
        {
            System.out.println("-----------------------");
            System.out.println("");
            System.out.println("0) Fin du menu");
            System.out.println("1) Créer les tables ");
            System.out.println("2) Afficher une table");
            System.out.println("3) Supprimer les tables ");
            System.out.println("4) Ajout dans une table");
            System.out.println("5) Supprimer dans une table");
            System.out.println("6) Donner les information sur un composant");
            System.out.println("7) Enchérir");
            System.out.println("");
            
            rep = Lire.i();
            
            try 
            {
               if (rep == 1) 
               {
                  System.out.println("Création des tables");
                  System.out.println("Quelle table créer ? Personnes (2), Articles (3), Enchere (4)");
                  int res = Lire.i();
                  
                  if(res == 2)
                  {
                     Personne.CreerTablePersonnes(con);
                     System.out.println("La table Personnes a été créée");
                  }
                  else if(res ==3)
                  {
                     Article.CreerTableArticles(con);
                     System.out.println("La table Articles a été créée");
                  }
                  else if(res == 4)
                  {
                     Enchere.CreerTableEnchere(con);
                     System.out.println("La table des Enchères a été créée");
                  }
               }
               if (rep == 2) 
               {
                  System.out.println("Afficher quelle table ? Personne (1), Articles (2), Enchere (3)");
                  int ans = Lire.i();
                  
                  if(ans == 1)
                  {
                     Personne.AffichePersonnes(con);
                  }
                  else if(ans == 2)
                  {
                     Article.AfficheArticles(con);
                  }
                  else if(ans == 3)
                  {
                     Enchere.AfficheEncheres(con);
                  }
                  
               }
               if (rep == 3) 
               {
                  System.out.println("Supprimer les tables");
                  System.out.println("Quelles tables supprimer ? Personnes (2), Articles (3), Encheres (4)");
                  int res = Lire.i();
                  
                 if(res == 2)
                  {
                     System.out.println("Certain de supprimer la tables Personnes ? oui : 1, non : 0");
                     int ver = Lire.i();
                     
                     if(ver == 1)
                     {
                        Personne.SupprimeTablePersonnes(con);
                        System.out.println("Table Personnes supprimée");
                     }
                  }
                  else if(res == 3)
                  {
                     System.out.println("Certain de supprimer la tables Articles ? oui : 1, non : 0");
                     int ver = Lire.i();
                     
                     if(ver == 1)
                     {
                        Article.SupprimeTableArticles(con);
                        System.out.println("Table Articles supprimée");
                     }
                  }
                  else if(res == 4)
                  {
                     System.out.println("Certain de supprimer la tables Encheres ? oui : 1, non : 0");
                     int ver = Lire.i();
                     
                     if(ver == 1)
                     {
                        Enchere.SupprimeTableEncheres(con);
                        System.out.println("Table Encheres supprimée");
                     }
                  }
               }
               
               
               if (rep == 4) 
               {
                  System.out.println("Ajout dans une table : Dans quelles table ? Personne(1), Article(2), Enchere(3)");
                  int auto = Lire.i();
                  if (auto == 1)
                  {
                     System.out.println("Ajout d'une personne ? Auto (1) ou Manuel (2) ?");
                     auto = Lire.i();
                     
                     try
                     {
                        if(auto == 2)
                        {
                           System.out.println("Nom :");
                           String nom = Lire.S();
                           System.out.println("Prénom :");
                           String prenom = Lire.S();
                           System.out.println("Email :");
                           String email = Lire.S();
                           System.out.println("CP :");
                           String CP = Lire.S();
                           System.out.println("Mdp :");
                           String mdp = Lire.S();
                           
                           Personne pers = new Personne(nom, prenom, email, CP, mdp);
                           
                           Personne.NouvellePersonne(con, pers);
                        }
                        else if(auto == 1)
                        {
                           String nom = "GOSSE";
                           String prenom = "Stanislas";
                           String email = "stanislas.gosse@insa-strasbourg.fr";
                           String CP = "FR-67000";
                           String mdp = "Mt_St_Odile!";
                           
                           Personne pers = new Personne(nom, prenom, email, CP, mdp);
                           
                           Personne.NouvellePersonne(con, pers);
                        }
                     }
                     catch(Personne.EmailExisteDeja ex)
                     {

                     }
                  }
                  else if(auto == 2)
                  {
                     System.out.println("Ajout d'un article. Auto(1) ou Manuel(2) ?");
                     auto = Lire.i();
                     
                     if(auto == 1)
                     {
                        System.out.println("Automatique");
                        
                        Personne per = Personne.TrouvePersonne(con, 1);
                        String nom = "Baguettes Batterie";
                        String desc = "Baguettes en bois, presque pas cassées. Sonorité exceptionnelle.";
                        String cat = "LO";
                        
                        Article art = new Article(nom, desc, per, cat);

                        Article.creerArticle(con, art, per.getIdPersonne(con));

                        
                     }
                     else if(auto == 2)
                     {
                        
                        System.out.println("Manuel");
                        
                        System.out.println("Catégorie : ");
                        String cat = Lire.S();
                        System.out.println("Nom de l'article");
                        String nom_art = Lire.S();
                        System.out.println("Description de l'article");
                        String desc = Lire.S();
                        
                        System.out.println("Id Personne");
                        int id_proprio = Lire.i();
                        
                        Personne perso = Personne.TrouvePersonne(con, id_proprio);
                        System.out.println(perso);
                        
                        Article art = new Article(nom_art, desc, perso, cat);

                        Article.creerArticle(con, art, perso.getIdPersonne(con));    
                     }
                  }
                  else if (auto == 3)
                  {
                     System.out.println("Ajout d'une enchère ? Auto (1) ou Manuel (2) ?");
                     auto = Lire.i();
                     
                     try
                     {
                        if(auto == 2)
                        {
                           System.out.println("Choisissez une personne (id)");
                           Personne.AffichePersonnes(con);
                           int ans = Lire.i();
                           Personne pers = Personne.TrouvePersonne(con, ans);
                           
                           System.out.println("Sélectionner un article (id)");
                           Article.AfficheArticlesPersonnes(con, ans);
                           ans = Lire.i();
                           Article art = Article.TrouveArticle(con, ans);
                           
                           float prx = -1;
                           while (prx <= 0)
                           {
                              System.out.println("Choisissez un prix");
                              prx = Lire.f();
                           }
                           
                           String dt = "";
                           while(dt.length() != 10)
                           {
                              System.out.println("Définissez une date de fin : AAAA-MM-JJ");
                              dt = Lire.S();
                           }
                           
                           Enchere ench = new Enchere(art, prx, dt, pers);
                           
                           Enchere.NouvelleEnchere(con, ench);
                           
                        }
                        else if(auto == 1)
                        {
                           
                        }
                     }
                     catch(Enchere.EnchereExisteDeja ex)
                     {

                     }
                  }
                  else if(rep == 7)
                  {
                     
                  }
               }
               
               if (rep == 5) 
               {
                  System.out.println("Supprimer dans quelle table ? Personne (1), Articles (2), Enchere (3)");
                  int ans = Lire.i();
                  
                  if(ans == 1)
                  {
                     Personne.SupprimePersonne(con);
                     //Coucou !!!!
                  }
                  else if (ans == 2)
                  {
                     Article.SupprimeArticle(con);
                  }
                  else if (ans == 3)
                  {
                     //Enchere.SupprimeEnchere(con);
                  }
                  
               }
               
               if (rep == 6)
               {
                  System.out.println("Choper les infos de : donner id");
                  int id = Lire.i();
                  
                  Personne per = Personne.TrouvePersonne(con, id);
                  System.out.println(per);
                  
                  System.out.println("Récupérer l'Id");
                  int idddd = per.getIdPersonne(con);
                  System.out.println(idddd);
               }
               
               if (rep == 7)
               {
                  System.out.println("Enchérir");
                  System.out.println("Choisissez sur quel article enchérir (id)");
                  Enchere.AfficheArticlesEncheres(con);
                  
                  int id_art = Lire.i();
                  
                  System.out.println("Quel prix ?");
                  double prx = Lire.d();
                  
                  Personne.AffichePersonnes(con);
                  System.out.println("Quelle personne ? (id)");
                  int id_ach = Lire.i();
                  
                  Personne acheteur = Personne.TrouvePersonne(con, id_ach);
                  
                  System.out.println("Acheteur : " + acheteur);
                  Enchere ench = Enchere.TrouveEnchere(con, id_art);
                  
                  Enchere.Encherir(con, ench, acheteur, prx);
               }
               if(rep == 8)
               {
                  Enchere.AfficheArticlesEncheres(con);
                  System.out.println("Id enchère : " + Enchere.TrouveEnchere(con, Lire.i()).getIdEnchere(con));
                  
                  
               }
               
            } 
            catch (SQLException ex) 
            {
                throw new Error(ex);
            }
        }
    }
           
    public static void main(String[] args) 
    {
        try (Connection con = defautConnect()) 
        {
            System.out.println("connecté !!!");
            menu(con);
        } 
        catch (Exception ex) 
        {
            throw new Error(ex);
        }
    }
}
