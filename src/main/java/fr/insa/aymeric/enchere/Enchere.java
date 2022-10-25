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
import java.util.ArrayList;
import java.util.List;

public class Enchere 
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
   
   public static void creerTableTest(Connection con) throws SQLException 
   {
        // je veux que le schema soit entierement créé ou pas du tout
        // je vais donc gérer explicitement une transaction
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) 
        {
            // creation des tables
            st.executeUpdate(
                    """
                    create table utilisateur (
                        id integer not null primary key
                        generated always as identity,
                    -- ceci est un exemple de commentaire SQL :
                    -- un commentaire commence par deux tirets,
                    -- et fini à la fin de la ligne
                    -- cela me permet de signaler que le petit mot clé
                    -- unique ci-dessous interdit deux valeurs semblables
                    -- dans la colonne des noms.
                        nom varchar(30) not null unique,
                        pass varchar(30) not null
                    )
                    """);
            st.executeUpdate(
                    """
                    create table TestBDD (
                        u1 integer not null,
                        u2 integer not null
                    )
                    """);
            // je defini les liens entre les clés externes et les clés primaires
            // correspondantes
            /*st.executeUpdate(
                    """
                    alter table aime
                        add constraint fk_aime_u1
                        foreign key (u1) references utilisateur(id)
                    """);
            st.executeUpdate(
                    """
                    alter table aime
                        add constraint fk_aime_u2
                        foreign key (u2) references utilisateur(id)
                    """);
            // si j'arrive jusqu'ici, c'est que tout s'est bien passé
            // je confirme (commit) la transaction
            con.commit();
            // je retourne dans le mode par défaut de gestion des transaction :
            // chaque ordre au SGBD sera considéré comme une transaction indépendante
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            // quelque chose s'est mal passé
            // j'annule la transaction
            con.rollback();
            // puis je renvoie l'exeption pour qu'elle puisse éventuellement
            // être gérée (message à l'utilisateur...)
            throw ex;*/
        } 
        finally 
        {
            // je reviens à la gestion par défaut : une transaction pour
            // chaque ordre SQL
            con.setAutoCommit(true);
        }
   }
   
   public static void creerTable(Connection con) throws SQLException 
   {
        // je veux que le schema soit entierement créé ou pas du tout
        // je vais donc gérer explicitement une transaction
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) 
        {
            // creation des tables
            st.executeUpdate(
                    """
                    create table utilisateur (
                        id integer not null primary key
                        generated always as identity,
                    -- ceci est un exemple de commentaire SQL :
                    -- un commentaire commence par deux tirets,
                    -- et fini à la fin de la ligne
                    -- cela me permet de signaler que le petit mot clé
                    -- unique ci-dessous interdit deux valeurs semblables
                    -- dans la colonne des noms.
                        nom varchar(30) not null unique,
                        pass varchar(30) not null
                    )
                    """);
            st.executeUpdate(
                    """
                    create table TestBDD (
                        u1 integer not null,
                        u2 integer not null
                    )
                    """);
            // je defini les liens entre les clés externes et les clés primaires
            // correspondantes
            /*st.executeUpdate(
                    """
                    alter table aime
                        add constraint fk_aime_u1
                        foreign key (u1) references utilisateur(id)
                    """);
            st.executeUpdate(
                    """
                    alter table aime
                        add constraint fk_aime_u2
                        foreign key (u2) references utilisateur(id)
                    """);
            // si j'arrive jusqu'ici, c'est que tout s'est bien passé
            // je confirme (commit) la transaction
            con.commit();
            // je retourne dans le mode par défaut de gestion des transaction :
            // chaque ordre au SGBD sera considéré comme une transaction indépendante
            con.setAutoCommit(true);
        } catch (SQLException ex) {
            // quelque chose s'est mal passé
            // j'annule la transaction
            con.rollback();
            // puis je renvoie l'exeption pour qu'elle puisse éventuellement
            // être gérée (message à l'utilisateur...)
            throw ex;*/
        } 
        finally 
        {
            // je reviens à la gestion par défaut : une transaction pour
            // chaque ordre SQL
            con.setAutoCommit(true);
        }
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
                        desc varchar(30) not null,
                        prix_mini real not null default '0.01',
                        cat varchar(30),
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
                        id_proprio integer not null,
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
    
    public static void SupprimeTable(Connection con) throws SQLException 
    {
        try ( Statement st = con.createStatement()) {
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
                    drop table testbdd
                    """);
                System.out.println("table testbdd supprimée");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
            try {
                st.executeUpdate(
                        """
                    drop table utilisateur
                    """);
                System.out.println("table utilisateur supprimée");
            } catch (SQLException ex) {
                // nothing to do : maybe the table was not created
            }
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
    
    public static void SupprimeTableArticles(Connection con) throws SQLException 
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
                  drop table Articles
                  """);
          } catch (SQLException ex) {
              // nothing to do : maybe the table was not created
          }
      }
   }
    
    public static void SupprimeTableEncheres(Connection con) throws SQLException 
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
                  drop table Encheres
                  """);
          } catch (SQLException ex) {
              // nothing to do : maybe the table was not created
          }
      }
   }
    
    // exemple de requete à la base de donnée
    public static void AffichePersonnes(Connection con) throws SQLException {
        try ( Statement st = con.createStatement()) {
            // pour effectuer une recherche, il faut utiliser un "executeQuery"
            // un executeQuery retourne un ResultSet qui contient le résultat
            // de la recherche (donc une table avec quelques information supplémentaire)
            try ( ResultSet tlu = st.executeQuery("select * from personnes")) {
                // un ResultSet se manipule un peu comme un fichier :
                // - il faut le fermer quand on ne l'utilise plus
                //   d'où l'utilisation du try(...) ci-dessus
                // - il faut utiliser la méthode next du ResultSet pour passer
                //   d'une ligne à la suivante.
                //   . s'il y avait effectivement une ligne suivante, next renvoie true
                //   . si l'on était sur la dernière ligne, next renvoie false
                //   . au début, on est "avant la première ligne", il faut donc
                //     faire un premier next pour accéder à la première ligne
                //     Note : ce premier next peut renvoyer false si le résultat
                //            du select était vide
                // on va donc très souvent avoir un next
                //   . dans un if si l'on veut tester qu'il y a bien un résultat
                //   . dans un while si l'on veut traiter l'ensemble des lignes
                //     de la table résultat

                System.out.println("liste des personnes :");
                System.out.println("------------------------");
                System.out.println("");
                // ici, on veut lister toutes les lignes, d'où le while
                while (tlu.next()) {
                    // Ensuite, pour accéder à chaque colonne de la ligne courante,
                    // on a les méthode getInt, getString... en fonction du type
                    // de la colonne.

                    // on peut accéder à une colonne par son nom :
                    int id = tlu.getInt("id");
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
    }
    
    
    
    public static class EmailExisteDeja extends Exception {
    }
    
    // lors de la création d'un utilisateur, l'identificateur est automatiquement
    // créé par le SGBD.
    // on va souvent avoir besoin de cet identificateur dans le programme,
    // par exemple pour gérer des liens "aime" entre utilisateur
    // vous trouverez ci-dessous la façon de récupérer les identificateurs
    public static int creerPersonne(Connection con, String nom, String prenom, String email, String codepost, String mdp)
            throws SQLException, EmailExisteDeja
    {
        // je me place dans une transaction pour m'assurer que la séquence
        // test du nom - création est bien atomique et isolée
        con.setAutoCommit(false);
        
        try ( PreparedStatement chercheMail = con.prepareStatement(
                "select id from personnes where email = ?")) 
        {
            chercheMail.setString(1, nom);
            ResultSet testMail = chercheMail.executeQuery();
            if (testMail.next()) 
            {
                throw new EmailExisteDeja();
            }
            try ( PreparedStatement pst = con.prepareStatement(
                  """
              insert into personnes (nom,prenom,email,codepost,mdp) values (?,?,?,?,?)
              """, PreparedStatement.RETURN_GENERATED_KEYS)) 
             {
                 pst.setString(1, nom);
                 pst.setString(2, prenom);
                 pst.setString(3, email);
                 pst.setString(4, codepost);
                 pst.setString(5, mdp);
                 pst.executeUpdate();
                 con.commit();

                 // je peux alors récupérer les clés créées comme un result set :
                 try ( ResultSet rid = pst.getGeneratedKeys()) 
                 {
                     // et comme ici je suis sur qu'il y a une et une seule clé, je
                     // fait un simple next 
                     rid.next();
                     // puis je récupère la valeur de la clé créé qui est dans la
                     // première colonne du ResultSet
                     int id = rid.getInt(1);
                     return id;
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
    
    public static void SupprimePersonne(Connection con) throws SQLException 
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
            System.out.println("2) Afficher les personnes (dans la table Personnes)");
            System.out.println("3) Supprimer les tables ");
            System.out.println("4) Ajout dans une table");
            System.out.println("5) Supprimer une personne (dans la table Personnes)");
            System.out.println("");
            
            rep = Lire.i();
            
            try 
            {
               if (rep == 1) 
               {
                  System.out.println("Création des tables");
                  System.out.println("Quelle table créer ? Test (1), Personnes (2), Articles (3), Enchere (4)");
                  int res = Lire.i();
                  
                  if(res == 1)
                  {
                     creerTableTest(con);
                     System.out.println("Les tables test ... & ... créées");
                  }
                  else if(res == 2)
                  {
                     CreerTablePersonnes(con);
                     System.out.println("La table Personnes a été créée");
                  }
                  else if(res ==3)
                  {
                     CreerTableArticles(con);
                     System.out.println("La table Articles a été créée");
                  }
                  else if(res == 4)
                  {
                     CreerTableEnchere(con);
                     System.out.println("LA table des Enchères a été créée");
                  }
               }
               if (rep == 2) 
               {
                  System.out.println("Affiche les personnes");
                  AffichePersonnes(con);
               }
               if (rep == 3) 
               {
                  System.out.println("Supprimer les tables");
                  System.out.println("Quelles tables supprimer ? Test (1), Personnes (2), Articles (3), Encheres (4)");
                  int res = Lire.i();
                  
                  if(res == 1)
                  {
                     System.out.println("Certain de supprimer les Tables ... & ... ? oui : 1, non : 0");
                     int ver = Lire.i();
                     
                     if(ver == 1)
                     {
                        SupprimeTable(con);
                        System.out.println("Tables ... & ... supprimées");
                     }
                  }
                  else if(res == 2)
                  {
                     System.out.println("Certain de supprimer la tables Personnes ? oui : 1, non : 0");
                     int ver = Lire.i();
                     
                     if(ver == 1)
                     {
                        SupprimeTablePersonnes(con);
                        System.out.println("Table Personnes supprimée");
                     }
                  }
                  else if(res == 3)
                  {
                     System.out.println("Certain de supprimer la tables Articles ? oui : 1, non : 0");
                     int ver = Lire.i();
                     
                     if(ver == 1)
                     {
                        SupprimeTableArticles(con);
                        System.out.println("Table Articles supprimée");
                     }
                  }
                  else if(res == 4)
                  {
                     System.out.println("Certain de supprimer la tables Encheres ? oui : 1, non : 0");
                     int ver = Lire.i();
                     
                     if(ver == 1)
                     {
                        SupprimeTableEncheres(con);
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

                           creerPersonne(con, nom, prenom, email, CP, mdp);
                        }
                        else if(auto == 1)
                        {
                           String nom = "GOSSE";
                           String prenom = "Stanislas";
                           String email = "stanislas.gosse@insa-strasbourg.fr";
                           String CP = "FR-67000";
                           String mdp = "Mt_St_Odile!";
                           creerPersonne(con, nom, prenom, email, CP, mdp);
                        }
                     }
                     catch(EmailExisteDeja ex)
                     {

                     }
                  }
                  else if(auto == 2)
                  {
                     System.out.println("Ajout d'un article. Auto(1) ou Manuel(2) ?");
                     auto = Lire.i();
                     
                     if(auto == 1)
                     {
                        System.out.println("automatique");
                     }
                     else if(auto == 2)
                     {
                        System.out.println("manuel");
                     }
                  }
                  
                  
                  
                  
               }
               if (rep == 5) 
               {
                  System.out.println("Supprime une personnes");
                  SupprimePersonne(con);
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