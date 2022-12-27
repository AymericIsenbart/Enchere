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
public class Personne 
{
   private String nom_per;
   private String prenom_per;
   private String email;
   private String codePost;
   private String mdp;
   private boolean admin;
   
   public Personne(String nom_per, String prenom_per, String email, String codePost, String mdp, boolean admin)
   {
      this.nom_per = nom_per.toUpperCase();
      this.prenom_per = prenom_per;
      this.email = email.toLowerCase();
      this.codePost = codePost;
      this.mdp = mdp;
      this.admin = admin;
   }
   
   public Personne(String nom_per, String prenom_per, String email, String codePost, String mdp)
   {
      this.nom_per = nom_per.toUpperCase();
      this.prenom_per = prenom_per;
      this.email = email.toLowerCase();
      this.codePost = codePost;
      this.mdp = mdp;
      this.admin = false;
   }

   public void setAdmin(boolean admin) {
      this.admin = admin;
   }

   public boolean getAdmin() {
      return admin;
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
          try 
          {
              st.executeUpdate(
                      """
                  drop table Personnes
                  """);
          }
          catch (SQLException ex) {
              // nothing to do : maybe the table was not created
          }
      }
   }
    
    public static void updateNom(Connection con, int id_per, String nom) throws SQLException
    {
      try(PreparedStatement pst = con.prepareStatement("""
            Update personnes
            set nom_perso=?
            where id_per=?"""))
       {
          pst.setString(1, nom.toUpperCase());
          pst.setInt(2, id_per);
          
          pst.executeUpdate();
       }
    }
    
    public static void updatePrenom(Connection con, int id_per, String prenom) throws SQLException
    {
      try(PreparedStatement pst = con.prepareStatement("""
            Update personnes
            set prenom=?
            where id_per=?"""))
       {
          pst.setString(1, prenom);
          pst.setInt(2, id_per);
          
          pst.executeUpdate();
       }
    }
    
    public static void updateEmail(Connection con, int id_per, String email) throws SQLException
    {
      try(PreparedStatement pst = con.prepareStatement("""
            Update personnes
            set email=?
            where id_per=?"""))
       {
          pst.setString(1, email.toLowerCase());
          pst.setInt(2, id_per);
          
          pst.executeUpdate();
       }
    }
    
    public static void updateCP(Connection con, int id_per, String CP) throws SQLException
    {
      try(PreparedStatement pst = con.prepareStatement("""
            Update personnes
            set codepost=?
            where id_per=?"""))
       {
          pst.setString(1, CP);
          pst.setInt(2, id_per);
          
          pst.executeUpdate();
       }
    }
    
    public static void updateMdp(Connection con, int id_per, String mdp) throws SQLException
    {
      try(PreparedStatement pst = con.prepareStatement("""
            Update personnes
            set mdp=?
            where id_per=?"""))
       {
          pst.setString(1, mdp);
          pst.setInt(2, id_per);
          
          pst.executeUpdate();
       }
    }
    
    public static void updateAdmin(Connection con, int id_per, boolean admin) throws SQLException
    {
      try(PreparedStatement pst = con.prepareStatement("""
            Update personnes
            set statut=?
            where id_per=?"""))
       {
          pst.setBoolean(1, admin);
          pst.setInt(2, id_per);
          
          pst.executeUpdate();
       }
    }
    
    // exemple de requete à la base de donnée
    public static void AffichePersonnes(Connection con) throws SQLException {
       System.out.println("");
       
       List<Personne> Lper = getAllPersonne(con);
       
       for (int i=0; i<Lper.size(); i++)
       {
          System.out.println(Lper.get(i));
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
       return -1;
   }
   
   public static int getIdPersonne(Connection con, String email) throws SQLException 
   {
       try ( PreparedStatement chercheEmail = con.prepareStatement(
               "select * from personnes where email = ?")) 
       {
         chercheEmail.setString(1, email);
         ResultSet testEmail = chercheEmail.executeQuery();
        
         if(testEmail.next())
         {
            return testEmail.getInt(1);
         }    
       }
       return -1;
   }
   
   public static List<Personne> getAllPersonne(Connection con) throws SQLException
   {
      List<Personne> Lpers = new ArrayList<>();;
      String nom_per;
      String prenom_per;
      String email;
      String codepost;
      String Mdp;
      boolean statut;
            
      try ( Statement st = con.createStatement()) 
      {
         try (ResultSet tlu = st.executeQuery("select * from personnes")) 
         {
            while(tlu.next())
            {
               nom_per = tlu.getString(2);
               prenom_per = tlu.getString(3);
               email = tlu.getString(4);
               codepost = tlu.getString(5);
               Mdp = tlu.getString(6);
               statut = tlu.getBoolean(7);
               
               Lpers.add(new Personne(nom_per, prenom_per, email, codepost, Mdp, statut));
            }
         }
     }
      return Lpers;
   }
   
   public void MiseEnEnchere(Connection con) throws SQLException 
   {
      int id_per = this.getIdPersonne(con);
      
   }
   
   
   public static Personne[] ListPersonAlea(Connection con, int n)
   {
      String[] prenom = {"Adele", "Albane", "Anais", "Axel", "Agathe", "Adrien", "Alice", "Amelia", "Anna", "Apolline", "Augustin", "Ava", "Candice", "Arthur", "Mae", "Manon", "Mathias", "Maelle", "Martin", "Lois", "Louis", "Louka", "Mateo", "Lena", "Lucas", "Marceau", "Matteo", "Valentin", "Robin"};
      String[] nom = {"Robion", "Freanfe", "Preofr", "Prout", "Peter", "Weissenberger", "Hood", "Riberi", "Benzema", "Lemarchal", "Lizzarazu", "Montagnier", "Renoir", "Potter", "Vae", "Velle", "Pricht", "Wagner", "Marteu", "Langlois", "Deschaux", "Hernadez", "Boubakar", "Gross", "Kante", "Dembele", "Uhman", "Lopez", "Shakespeare"};;
      String[] mdp = {"dqrss", "kijgfhudyg", "<ewvxtbc", "yjbhf", "vikuvhjg", "ubfhy", "'(z-", "cvbdf", "2571", "frcgtègj", "olikujyhtgrf", "cqvstrg", "ujybhdvbdhvgc", "uhtdygx", "igkunbchyvdbcvsxrq", "cscgtdcgts", "dfsvdtygh", "bn(uhgfe(csvxc", "fcvgbhtdgtvxz", "csgzvtxszwc", "fcgzvtsfdfr", "cvsgztdfzsr", "cxqvsgrtczx", "bhtvgcrxfe", "poiuytrez", "azertyuiop", "cvtgrstecgdv", "jyburtecrds", "rfscvdfnyctrxse"};;
      String[] adress = {"gmail", "hotmail", "yahoo", "orange", "free", "printer"};
      String[] CP = {"FR", "AL", "BE", "US", "TW", "CH"};
      
      Personne[] people = new Personne[n];
      
      Personne perso;
      
      int a_nm;
      int a_pnm;
      int a_mdp;
      
      String Pnom;
      String Ppnom;
      String Mdp;
      String Padr;
      String email;
      int codepost;
      String CPp;
     
      double var;
      
      for (int i=0; i<n; i++)
      {
         var = Math.random()*prenom.length;
         Pnom = nom[(int)var].toUpperCase();
         
         var = Math.random()*nom.length;
         Ppnom = prenom[(int)var];
         
         var = Math.random()*mdp.length;
         Mdp = mdp[(int)var];
         
         
         
         var =  Math.random()*99999;
         while(var/1000 < 10)
         {
            var = 10*var;
         }
         codepost = (int)var;
         var = Math.random()*CP.length;
         CPp = CP[(int)var] + "-" + codepost;
         
         var = Math.random()*adress.length;
         Padr = adress[(int)var];
         email = Pnom + "." + Ppnom + "@" + Padr + "." + CPp.substring(0,2).toLowerCase(); 
         
         people[i] = new Personne(Pnom, Ppnom, email, CPp, Mdp);
         System.out.println(people[i]);
      }
      
      
      
      return people;
      
   }
}
