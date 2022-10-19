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
   
   public static void creeTable(Connection con) throws SQLException 
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
    
   public static void creeTablePersonne(Connection con) throws SQLException 
   {
        // je veux que le schema soit entierement créé ou pas du tout
        // je vais donc gérer explicitement une transaction
        con.setAutoCommit(false);
        try ( Statement st = con.createStatement()) {
            // creation de la table Personne
            st.executeUpdate(
                    """
                    create table Personnes (
                        id integer not null unique primary key
                        generated always as identity,
                    --
                    -- ceci est un exemple de commentaire SQL :
                    --
                        nom varchar(30) not null,
                        prenom varchar(30) not null,
                        email varchar(70) not null,
                        codePost varchar (8) not null,
                        mdp varchar(30) not null 
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
    
    // lors de la création d'un utilisateur, l'identificateur est automatiquement
    // créé par le SGBD.
    // on va souvent avoir besoin de cet identificateur dans le programme,
    // par exemple pour gérer des liens "aime" entre utilisateur
    // vous trouverez ci-dessous la façon de récupérer les identificateurs
    // créés : ils se présentent comme un ResultSet particulier.
    public static int creerPersonne(Connection con, String nom, String prenom, String email, String codepost, String mdp)throws SQLException
    {
        // je me place dans une transaction pour m'assurer que la séquence
        // test du nom - création est bien atomique et isolée
        con.setAutoCommit(false);
        
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
            System.out.println("1) créer la BdD ");
            System.out.println("2) Affiche les personnes ");
            System.out.println("3) supprime les tables ");
            System.out.println("4) Créer une personne");
            System.out.println("5) Supprimer une personne");
            System.out.println("");
            
            rep = Lire.i();
            
            try 
            {
               if (rep == 1) 
               {
                  System.out.println("Crée une table");
                  creeTable(con);
               }
               if (rep == 2) 
               {
                  System.out.println("Affiche les personnes");
                  AffichePersonnes(con);
               }
               if (rep == 3) 
               {
                  System.out.println("Supprime une table");
                  SupprimeTable(con);
               }
               if (rep == 4) 
               {
                  System.out.println("Créer une personne");

                  System.out.println("Auto (1) ou Manuel (2) ?");
                  int auto = Lire.i();
                  
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