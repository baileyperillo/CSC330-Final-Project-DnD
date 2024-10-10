// import static org.junit.jupiter.api.Assertions.assertEquals;

// import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Scanner;
import java.io.FileInputStream;



public class Main {
  public static void main(String[] args) throws IOException{

    
    System.out.println("Welcome to DnD assist.");
    //start up gameplay.java class
    System.out.println("Please input F to insert a file from a previous session or C to continue");
    


    
      Scanner scnr = new Scanner(System.in);
      String  insert = scnr.next(); //enter F or C
      String filename = "";
      FileInputStream fileByteStream = null; //file input stream
      Scanner inFS = null; 
      GamePlay A = new GamePlay();
    boolean incombat;
    boolean exit = true;
    
    
    if (insert.equals("F")){

        
          System.out.print("Please input the name of the file: ");
          filename = scnr.nextLine();
           //file insert
          // Try to open file
          System.out.println("Opening file numFile.txt.");
          fileByteStream = new FileInputStream(filename);
          inFS = new Scanner(fileByteStream);
  
          // File is open and valid if we got this far 
          // (otherwise exception thrown)
        //---------NOT WORKING YET-----------------
          fileByteStream.close();



      insert = "C";
      }// CREATE A METHOD FOR FILE INPUT HERE (not implemented)
      
    if(insert.equals("C")){
      System.out.println();
      System.out.println("Input H to see the list of commands"); // main menu
      while(exit)
        {
          insert = scnr.next();
          
          if(insert.equals("H"))//Help command to view current available commands
          {
            System.out.println();
            System.out.println("(C) To create a new hero.");//list commands
            System.out.println("(E) To create a new enemy.");
            System.out.println("(W) To create a new weapon or ability.");
            System.out.println("(G) To Give a character a new weapon or ability.");
            System.out.println("(L) To list all created characters.");
            System.out.println("(T) To start Combat.");
            System.out.println("(R) To use a long rest, and (r) to use a short rest.");
            System.out.println("(D) To access DM Commands.");
            System.out.println("(P) To Roll a dice for a character with a given stat.");
            System.out.println("(N) To exit to file output.");
          }
            
          else if(insert.equals("C")) //Create hero
          {
            System.out.println();
            A.CreateCharacterParty();//calls create Hero function in gameplay.java
          }
            else if(insert.equals("E"))//create enemy
            {
               System.out.println();
              A.CreateCharacterEnemy();//calls create Enemy function in gameplay.java
            }
              else if(insert.equals("W"))//create weapon or ability
                {
                  System.out.println();
                   boolean add = true;
                    while(add)
                      {
                        System.out.println("(M) To add a main action.");//allow user to select if its a Main action or a Bonus action
                        System.out.println("(B) To add a bonus action.");
                        insert = scnr.next();

                        if(insert.equals("M"))
                        {
                          A.createMainAction();//create a main action

                         add=false;
                        }
                         else if(insert.equals("B"))
                            {
                              A.createBonusAction();//create a bonus action
                              add=false;
                            }
                        else System.out.println("Inccorect input. M for main action. B for bonus action.");
                      }
                }
                else if(insert.equals("G"))//Give a character a weapon or ability
                  {
                    System.out.println();
                     boolean add = true;
                     while(add)
                       {
                         System.out.println("(M) To add a main action to a character.");//select wether or not to give the character a main or bonus action
                         System.out.println("(B) To add a bonus action to a character.");
                         insert = scnr.next();
                      
                         if(insert.equals("M"))
                         {
                        try{
                           A.GiveMainAction();//calls give main action function in gameplay.java
                        }
                        catch(Exception e)//if no characters are created it will throw an exception and return to main menu
                          {
                            System.out.println("No characters to give a Main action to or no main actions to give. Returning to main menu");
                            System.out.println();
                          }
                          add=false;
                         }
                          else if(insert.equals("B"))
                             {
                               try{
                               A.GiveBonusAction();//calls give bonus action function in gameplay.java
                               }
                                 catch(Exception e)//if no characters to give a bonus action to. return to main menu.
                                   {
                                     System.out.println("No characters to give a Bonus action to or no bonus actions to give. Returning to main menu");
                                     System.out.println();
                                   }
                               add=false;
                             }
                         else System.out.println("Inccorect input. M for main action. B for bonus action.");
                       }
                  }
          else if(insert.equals("L"))//list all created characters
          {
            System.out.println();
            A.statusprint();//lists all characters in the array of characters if there are no characters nothing will be listed
          }
          else if(insert.equals("T"))//start combat
          {
             System.out.println();
            try{
              A.RollForInitiative();//roll for initiative to set the turn order
            }
          catch (Exception e)//if no characters are created. there will be an exception. catch that exception and return to main menu
              {
                System.out.println("No characters to roll initiative. Returning to main menu");
                 System.out.println();
              }
            System.out.println("NOW IN COMBAT MODE");
            System.out.println("Input H to see the list of commands");
            incombat = true;//used to exit combat mode. set to true while entering combat mode
            while(incombat)//enter a new while loop for that specific comabt scenario. Changes the functions that can be called in the combat menu
              {
                insert = scnr.next();//ask for user input

                if(insert.equals("H"))
                {
                  System.out.println();
                  System.out.println("(C) To view current characters turn");//list of new commands in combat mode
                  System.out.println("(A) To take an action");
                  System.out.println("(L) To list all created characters.");
                  System.out.println("(T) To view turn order");
                  System.out.println("(S) Force End Turn");
                  System.out.println("(D) To access DM Commands");
                  System.out.println("(E) To exit combat.");
                }
                  else if(insert.equals("S"))//Used to force end turn if the current character cant make any actions
                    {
                       System.out.println();
                      A.nextturn();//Skips to the next turn
                    }
                else if(insert.equals("C"))//view the current characters turn
                {
                  try{
                  System.out.println();
                  System.out.println("It is " + A.CurrentCharacter().getname() + " turn.");//get the name of the current character and print it. Exception should be caught by turn order but just incase
                  }
                  catch (Exception e)
                    {
                      System.out.println("No Current Characters. Returning to combat menu");//throws exception and returns to combat menu if there are no characters created
                    }
                }
                else if(insert.equals("A"))//Prompts the characters whos turn it is to take an action
                {
                  System.out.println();
                   boolean add = true;
                   while(add)
                     {
                       System.out.println("(M) To use a main action.");//asks user if they want to use a main action or a bonus action
                       System.out.println("(B) To use a bonus action");
                       insert = scnr.next();

                       if(insert.equals("M"))
                       {
                      try{
                         A.MainAction();
                      }
                      catch(Exception e)//if the current character dosent have a main action we return to the combat menu
                        {
                          System.out.println("No MainActions to use. Returning to combat menu");
                          System.out.println();
                        }
                        add=false;
                       }
                        else if(insert.equals("B"))
                           {
                             try{
                             A.BonusAction();
                             }
                               catch(Exception e)//if the character dosent have a bonus action we return to the combat menu.
                                 {
                                   System.out.println("No BonusActions to use. Returning to combat menu");
                                   System.out.println();
                                 }
                             add=false;
                           }
                       else System.out.println("Inccorect input. M for main action. B for bonus action.");
                     }
                }
                else if(insert.equals("L"))//lists all created characters
                {
                  System.out.println();
                  A.statusprint();//same as in main menu
                }
                else if(insert.equals("T"))//view current turn order
                {
                  try{
                  System.out.println();
                  A.printturnorder();
                    }
                    catch (Exception e)//incase there are no characters created. return to combat menu.
                      {
                        System.out.println("No Current Characters. Returning to combat menu");
                      }
                }
                else if(insert.equals("E"))//exits combat mode
                {
                  System.out.println();
                  System.out.println("Exiting Combat");
                  incombat = false;//sets incombat to false. allowing the code to leave the while loop and go back to main menu
                }
                  else if(insert.equals("D"))//access admin commands
                    {
                      try{
                      A.DM();//can either delete a character or level up a character
                        }
                        catch (Exception e)//incase there are no created characters it returns to combat menu
                          {
                            System.out.println("No Current Characters. Returning to combat menu");
                          }
                    }
                else System.out.println("No Corisponding Function. Please Input H to see the list of commands or try again");//if command not found
                
              }
            
          }
            else if(insert.equals("R"))//use long rest
            {
               System.out.println();
              A.PartyLongRest();//applys longrest to all created heros
            }
              else if(insert.equals("r"))//use short rest
              {
                 System.out.println();
                A.PartyShortRest();//applys short rest to all created heros
              }
                else if(insert.equals("D"))//Admin Commands (currently not implemented outside of remove character and level up character)
                  {
                    System.out.println();
                    try{
                    A.DM();
                      }
                      catch (Exception e)
                        {
                          System.out.println("No Current Characters. Returning to main menu");
                        }
                  }
                else if(insert.equals("N"))//Exit to file output (currently not implemented)
                  {
                     System.out.println();
                    exit = false;
                  }
                  else if(insert.equals("P"))//Roll a dice. Can be used with atributes from a character as a bonus
                    {
                      try{
                       System.out.println();
                        System.out.println(A.roll());
                        }
                        catch (Exception e)//if trying to use an atribute of a character that doesnt exist. return to main menu.
                          {
                            System.out.println("No Current Characters. Returning to main menu");
                          }
                    }
else System.out.println("No Corisponding Function. Please Input H to see the list of commands or try again");//if command not found
        }  
    }
   
    else{
      System.out.println("This is wrong.Please input F to insert a file or C to create characters");//if command not found
      insert = scnr.next(); 
    }
    
    scnr.close(); //see if we want to add more  
    // CREATE A FILE OUTPUT TO SAVE CHARACTERS AND WEAPONS TO A FILE BEFORE EXITING PROGRAM

  }
      
}


