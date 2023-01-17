/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.aymeric.enchere.Ressources;

/**
 *
 * @author aymer
 */
public class Utile 
{
    public static int getYear(String date)
    {
        if(date.length() == 10)
        {
            String year = date.substring(0, 4);
            //System.out.println(year);
            
            return Integer.parseInt(year);
        }
        else
        {
            return -1;
        }
    }
    
    public static int getMonth(String date)
    {
        if(date.length() == 10)
        {
            String month = date.substring(5, 7);
            //System.out.println(month);
            
            return Integer.parseInt(month);
        }
        else
        {
            return -1;
        }
    }
    
    public static int getDay(String date)
    {
        if(date.length() == 10)
        {
            String day = date.substring(8, 10);
            //System.out.println(day);
            
            return Integer.parseInt(day);
        }
        else
        {
            return -1;
        }
    }
    
    public static boolean dateDepassee(String date)
    {
        if(date.length() == 10)
        {
            String currentDate = java.time.LocalDate.now().toString();
            System.out.println(date);
            
            System.out.print("Annee");
            if(getYear(currentDate) < getYear(date))
            {
                
                return false;
            }
            else if(getYear(currentDate) == getYear(date))
            {
                System.out.print(" Mois");
                if(getMonth(currentDate) < getMonth(date))
                {
                    return false;
                }
                else if(getMonth(currentDate) == getMonth(date))
                {
                    System.out.print(" Jour");
                    if(getDay(currentDate) <= getDay(date))
                    {
                        return false;
                    }
                    else
                    {
                        System.out.println("Jour");
                        return true;
                    } 
                }
                else
                {
                    return true;
                }
            }
            else
            {
                return true;
            }
        }
        else
        {
            System.out.println(date + " : longueur=" + date.length());
            return true;
        }
    }
    
    
}
