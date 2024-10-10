import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;
import java.util.*;

import javax.security.auth.DestroyFailedException;

import java.io.IOException;
import java.io.FileInputStream;
class GamePlay {
     //If i were to do this again. I would try to implement lists more often.
    //decide how gameplay is displayed and played
     Scanner scnr = new Scanner(System.in);//a scnr to look for inputs
     Hero[] Heros = new Hero[10]; //list of heros 
     Enemy[] Enemy = new Enemy[12]; //list of enemies
     Character[] TurnOrder = new Character[22];//the current turn order list. gets assigned everytime roll for initiative is called
     int TurnOrderIndex = 0;// Some type of index to show which character is currently active (might not be needed)
     Character[] allcharacters = new Character[22];//list of all characters good or bad
     int pplCount = 0; // increase everytime we add a character good or bad. Also used as TurnOrderMax
     int HeroCount = 0; // increase when we add a hero
     int EnemyCount = 0; // increase when we add an enemy, decrease when we remove an enemy
     Random random = new Random();
     boolean gameOver = false; // will change when one is dead
     boolean InCombat = false; //
     Weaponry[] MainActions = new Weaponry[100];
     Weaponry[] BonusActions = new Weaponry[100];
     Weaponry[] AllWeapons = new Weaponry[200];
     int WeaponCount = 0;
     int MainActionCount = 0;
     int BonusActionCount = 0;
     boolean mainaction;
     boolean bonusaction;
     List<Character> DeadHeros; //a list of dead heros incase people wanted to look at characters they have made. Or potential revive feature in the future.
     


GamePlay()//base line abilities and weapons pulled from dnd 5e.
     {
          
          createMainAction(createweapon("Dagger", 4, 0, 1, "dex",false,false,false)); 
          createMainAction(createweapon("ShortBow", 6, 0, 1, "dex",false,false,false));
          createMainAction(createweapon("ShortSword", 4, 0, 1, "str",false,false,false));
          createMainAction(createweapon("FireBolt", 10, 0, 1, "int",false,false,false));
          createMainAction(createweapon("CureWounds", 8, 1, 1, "char",true,false,false));
          createMainAction(createweapon("Cleave", 8, 0, 1, "str",false,true,false));
          createMainAction(createweapon("MagicMissle",5,1,3,"int",false,false,false));
     }

     
void DM()//This is used for the DM commands. Its to allow the dungeon master to modify whatever they want at any time.(still needs to be implemented)
     {
          System.out.println("DM commands");
          System.out.println();
          System.out.println("(D) To Remove A Character");
          System.out.println("(L) To Level up A Character");
          System.out.println("Anything else to exit");
          String insert = scnr.next();
       if(insert.equals("D"))removeCharacter(selectTarget());//removes a character of the selected target
          //CURENTLY BUGGED
       if(insert.equals("L"))selectTarget().levelup();//levels up a character of the selected target
     }

void removeCharacter(Character a) //passes the character to be removed from the arrays (Currently Bugged)
     {
         Character current = CurrentCharacter();//store current players turn
          boolean MainActions = mainaction;//store main action status
          boolean BonusActions = bonusaction;//store bonus action status
          List<Character> all = Arrays.asList(allcharacters);//converts the arrays into lists
          List<Enemy> enemy = Arrays.asList(Enemy);
          List<Hero> hero = Arrays.asList(Heros);
          List<Character> TO = Arrays.asList(TurnOrder);
          if(all.remove(a))//removes the character from the lists. and then decrements the corisponding count
          {
               pplCount--;
          all.toArray(allcharacters);//converts the lists back into arrays
          }
          if(enemy.remove(a))
          {
               EnemyCount--;
               enemy.toArray(Enemy);
          }
          if(hero.remove(a))
          {
               HeroCount--;
               hero.toArray(Heros);
               DeadHeros.add(a); //add dead heros to a list in case we want to add the ability to revive them later (dnd has a spell called revivify)
               
          }
          if(TO.remove(a))//removes the character from the lists. and then decrements the corisponding count
          {
               pplCount--;
               TO.toArray(TurnOrder);
          }
          while (CurrentCharacter() != current)//Sets Turn back to the current character before removal
               {
                    nextturn();
               }
          mainaction = MainActions;//resets the main action to the previous state
          bonusaction = BonusActions;//resets the bonus action to the previous state
          
     }
void MainAction()//Uses one of the main actions of the current characters turn
     {
          if (mainaction == false)
          {
               System.out.println("Main Action used");
               System.out.println();
               return;
          }
          System.out.println("Select the number for your MainAction"); 
     Weaponry a = selectAction(CurrentCharacter().MainActions, CurrentCharacter().MainActionCount);//select a weapon/ability from the list of main actions that the character has
          if(spellslotcheck(a)) return; //if you dont have enough spell slots to use this weapon/ability you will leave the function. Otherwise you will continue
          if(restcheck(a)) return; //check if you have the ability to use this action or need to rest before next use
          System.out.println();
         attack(a);//uses the action on the specified number of targets
          if(bonusaction == false)//if the user used both their bonus and main action
          {
               nextturn();//move to the next persons turn and set the main action and bonus action to true
          System.out.println("End Of Turn");
               return;
          }
          mainaction = false;//sets main action to false.
     }

     
void BonusAction()//Uses one of the bonus actions of the current characters turn
     {
          if (bonusaction == false)
               {
                    System.out.println("Bonus Action used");
                    System.out.println();
                    return;
               }
          System.out.println("Select the number for your BonusAction"); 
          Weaponry a = selectAction(CurrentCharacter().BonusActions, CurrentCharacter().BonusActionCount);
               if(spellslotcheck(a)) return; //if you dont have enough spell slots to use this weapon/ability you will leave the function. Otherwise you will continue
               if(restcheck(a)) return;
               System.out.println();
              attack(a);
               if(mainaction == false)//move to next persons turn if you used both your main and bonus actions
               {
                    nextturn();
               System.out.println("End Of Turn");//next persons turn and sets mainaction and bonus action to true
               }
               else bonusaction = false;//sets bonus action to false if main action hasnt been used and allows the current character to continue their turn
     }





//helper methods for Main and Bonus action
     void checkHP(Character a)
          {
               if(a.getCurrentHP() <= 0)//if the character is dead
               {
               System.out.println(a.name + " Has died!");//print that they have died
               removeCharacter(a);//removes dead characters from all arrays including the turn order.
               }
          }
     boolean restcheck(Weaponry a) //return true if check fails. return false if check succeds
     {
          if(a.ShortRest == true)//if the ability costs a Short rest
          {
               if(a.SRcharge == true)// and we have a short rest charge
               {
                    a.SRcharge = false;//set charge to zero
               return false;//return false so that the function will continue
               }
               else
               {
                    System.out.println("Cant use. Need to Short rest");
                    return true;
               }//otherwise return true so that the function will end
          }
          if(a.LongRest == true)//if the ability costs a long rest
          {
               if(a.LRcharge == true)// and we have a long rest charge
                    {
                         a.LRcharge = false;//set charge to zero
                    return false;//return false so that the function will continue
                    }
                    else
               {
                    System.out.println("Cant use. Need to Long rest");
                    return true;
               }//otherwise return true so that the function will end
          }
          return false;//if the ability dosent cost a short rest or a long rest. return false so the function can continue
     }
     boolean spellslotcheck(Weaponry a)
          {
          if(CurrentCharacter().spellslots < a.SpellSlotCost)/*if we dont have enough spell slots*/
               {
               System.out.println("You have no spell slots left, returning to combat menu");
               return true;/*exit from using an atack. and tell them to try again*/
               }
               else 
               {
               CurrentCharacter().spellslots -= a.SpellSlotCost;//reduce the number of spell slots by the cost of the weapon
               return false;//return false so that the function will continue
               }
          }
     int targetcheck(int count)//checks for a valid target number.
          {
               int target = -1;
          if(count == 0)//if there are no targets to choose from
          {
               System.out.println("No targets to choose from");
               return -1; //this will return -1 to select target. From there the try catch will catch it and tell the user that there are no targets to select from and return them to the main menu options
          }
               while(target < 0 || target >= count)//while a useable target hasnt been chosen
                    {
               target = scnr.nextInt();//chose a target
               if(target < 0 || target >= count)//if the target is not a valid target
               {
               System.out.println("Invalid Target Slot");
               }        
               }
               return target;//else return the targeted index
          }

     void attack(Weaponry a)//passes the weapon from the main action or bonus action to attack.
          {
                System.out.println("Using " + a.name);//prints name of weapon being used
                         for(int i = 0; i < a.NumberOfTargets;i++)//for loop using the ammount of times the weapon atacks
                              {
                              Character b = selectTarget();//user selects a target
                         a.AtackDMG(b,statbonus(CurrentCharacter(),a.atribute));//user atacks the target with the weapon. and adds the stat bonus to the damage.
                              checkHP(b); //checks if the target has died. if so they are removed from all arrays and the turn order. Turn order and everything else is then reset.
    if(i+1 < a.NumberOfTargets)
    {
         System.out.println("Input 0, to continue inputing targets, Input 1 to exit");//INCASE THE USER KILLS ALL ENEMYS. AND STILL HAS MORE TARGETS THEY CAN CHOOSE TO ATTACK. ALLOWS THE USER TO END ACTION EARLY SO THEY ARENT FORCED TO HIT THEMSELVES
          if(scnr.nextInt() == 1) i = a.NumberOfTargets;
    }//if the user inputs 1 the for loop will end.
                              }
                         if(a.NumberOfTargets == 0)//if the weapon is an aoe atack
                         {
                              boolean j = true;
                              while(j)//while loop to keep asking for targets until the aoe atack has hit all targets
                                   {
                                        Character b = selectTarget();//select a target
               a.AtackDMG(b,statbonus(CurrentCharacter(),a.atribute));//atack the target
                                        checkHP(b);//if the targets hp is now bellow zero remove target
                                        System.out.println("Input 0, to continue inputing targets, Input 1 to exit");
                                        //ask if user wants to stop selecting targets
                                        if(scnr.nextInt() == 1) j = false;
                                   }
                         }
                         System.out.println();
          }
     int statbonus(Character a, String bonus)//we pass a character and the string of what stat we want the bonus from
          {
     //for every 2 points above or bellow the stat. we add or subtract 1 from the bonus. if no stat is selected we return 0. 
          if(bonus == "str")return (a.strength-10)/2;  
          if(bonus == "dex")return (a.dexterity-10)/2; 
          if(bonus == "int")return (a.intelligence-10)/2; 
          if(bonus == "wis")return (a.wisdom-10)/2; 
          if(bonus == "con")return (a.constitution-10)/2; 
          if(bonus == "char")return (a.charisma-10)/2; 
             return 0;  
          }

     
//Character modification methods
void GiveMainAction() // give a character an action from the stored actions in gameplay
     {
     selectTarget().addtoMainAction(selectAction(MainActions, MainActionCount));
     }
     
void GiveBonusAction()
     {
     selectTarget().addtoBonusAction(selectAction(BonusActions, BonusActionCount));
     }
     
          
//Game action methods
int roll()
     {
     System.out.println("Input the # of sides on the die");
     int a = Dice(scnr.nextInt());//rolls a dice with the number of sides the user inputs
     System.out.println("Is there a stat bonus on this roll? (true) or (false)");
     if(scnr.nextBoolean())//check if they want to apply a stat bonus
     {
          
      a += statbonus(selectTarget(),selectatribute());//apply a stat bonus of the selected atribute from the selected character
     }
 return a;//return the value
     }
     
void nextturn()
     {
          TurnOrderIndex += 1;//increases the turn order index by 1
          if(TurnOrderIndex >= pplCount) TurnOrderIndex = 0;//if the turn order index is greater than the number of people in the game. reset it to 0 (the next index)
          mainaction = true;//reset mainaction and bonus action for the next persons turn
          bonusaction = true;
     }

int Dice(int x /* determines how many sides are on the dice*/ )
{  //use for any and all dice roll 
         return random.nextInt(x)+1;
    }
     

          
void RollForInitiative()
     { //Done
          int pplRoll[] = new int[pplCount];  // make a new array that is the size of the # of people
          
          for(int i = 0; i < pplCount; i++){ // roll for every person
               pplRoll[i] = Dice(20); // Index = index of character, value = roll
          }
          for(int i = 0; i < pplCount; i++)//for everyelement in the array
          {
              int GreaterIndex = 0;
               for(int j = 1; j < pplCount; j++) 
                    {
                         if(pplRoll[j] > pplRoll[GreaterIndex]) //find who rolled the highest
                         {
                              GreaterIndex = j; //set the index of the highest roll to the index of the highest roll
                         }
                    }
               TurnOrder[i] = allcharacters[GreaterIndex];//put them at the start of the turn order
               pplRoll[GreaterIndex] = 0; //remove them from the rolls, and repeat until all characters are done.
          }
          mainaction = true;//set actions to true so that the next turn can be used
          bonusaction = true;
     }


//Party Rest Methods
     
void PartyShortRest()
     {
       for(int i = 0; i < HeroCount; i++)
            {
                Heros[i].shortRest();//calls short rest for all heros
            }
     }

void PartyLongRest()
     {
          for(int i = 0; i < HeroCount; i++)
            {
                Heros[i].LongRest();//calls long rest for all heros
            }
     }



//Create Methods
     
Character Create()//creates a character by asking the user for all the character stats
     {
          Character player = new Character();
           
               System.out.print("Please enter a name for your character: ");
               String inputName = scnr.next();//scans for name
          
               player.setName(inputName);//sets name
          System.out.print("Please enter stats as numbers. (HP level ac str dex int wis con char)");
          System.out.println();
          System.out.print("HP: ");
          player.setMaxHP(scnr.nextInt());//scan for hp and sets hp
          System.out.println();
          System.out.print("LVL: ");
          player.setlevel(scnr.nextInt());//scan and set level
          System.out.println();
          System.out.print("AC: ");
          player.setArmorClass(scnr.nextInt());//scan and set ac
          System.out.println();
          System.out.print("STR: ");
          player.setstrength(scnr.nextInt());//scan and set str
          System.out.println();
          System.out.print("DEX: ");
          player.setdexterity(scnr.nextInt());//scan and set dex
          System.out.println();
          System.out.print("INT: ");
          player.setintelligence(scnr.nextInt());//scan and set int
          System.out.println();
          System.out.print("WIS: ");
          player.setwisdom(scnr.nextInt());//scan and set wis
          System.out.println();
          System.out.print("CON: ");
          player.setconstitution(scnr.nextInt());//scan and set con
          System.out.println();
          System.out.print("CHAR: ");
          player.setcharisma(scnr.nextInt());//scan and set char
          System.out.println();
          
               System.out.println("Character created: " + player.getName());
               System.out.println();
          

             return player;
             
     }

void CreateCharacterEnemy()
     {
          Character b = Create();//asks for user inputs for the character
           Enemy a = new Enemy(b.name,b.MaxHP,b.level,b.ArmorClass,b.strength,b.dexterity,b.intelligence,b.wisdom,b.constitution,b.charisma);
          //passes those parameters into the creation of an enemy
          Weaponry e = selectAction(MainActions, MainActionCount);//selects a main action from the main actions array
          a.addtoMainAction(e);//gives the enemy the main action
          a.status();//prints the status
          Enemy[EnemyCount] = a;//increments and adds to corisponding arrays
          allcharacters[pplCount] = a;
          EnemyCount++;
          pplCount++;
     }
void CreateCharacterEnemy(String name,int MaxHP,int level,int ArmorClass,int strength,int dexterity,int intelligence,int wisdom,int constitution,int charisma)//polymorphism for file input
          {
                Enemy a = new Enemy(name,MaxHP,level,ArmorClass,strength,dexterity,intelligence,wisdom,constitution,charisma);
               Enemy[EnemyCount] = a;
               allcharacters[pplCount] = a;
               EnemyCount++;
               pplCount++;
          }
     
void CreateCharacterParty()
     {
     Character b = Create();//asks for user inputs for the character

          
          System.out.println("Please choose a class:");//selects a character class
           
               System.out.println("1. Warrior");
               System.out.println("2. Mage");
               System.out.println("3. Cleric");

int input = 0;
while(input > 3 || input < 1){//checks that user input is valid
input = scnr.nextInt();
     System.out.println();
     if(input == 1){//creates a Warrior character
                    Warrior a = new Warrior(b.name,b.MaxHP,b.level,b.ArmorClass,b.strength,b.dexterity,b.intelligence,b.wisdom,b.constitution,b.charisma);
                         Heros[HeroCount] = a;//increments and adds to corisponding arrays
          allcharacters[pplCount] = a;
                    System.out.println("Warrior selected:");
                    System.out.println();
          Weaponry e = selectAction(MainActions, MainActionCount);;//select a main action
          a.addtoMainAction(e);//sets it to main action inside the character
          a.status();
     }
   else if(input == 2)
                  {
                    Mage a = new Mage(b.name,b.MaxHP,b.level,b.ArmorClass,b.strength,b.dexterity,b.intelligence,b.wisdom,b.constitution,b.charisma);
                         Heros[HeroCount] = a;
                       allcharacters[pplCount] = a;//same as Warior but mage
                    System.out.println("Mage selected:");
                    System.out.println();
                       Weaponry e = selectAction(MainActions, MainActionCount);;
                       a.addtoMainAction(e);
                       a.status();
             }
else if(input == 3)
                  {
                    Cleric a = new Cleric(b.name,b.MaxHP,b.level,b.ArmorClass,b.strength,b.dexterity,b.intelligence,b.wisdom,b.constitution,b.charisma);
                    Heros[HeroCount] = a;
                       allcharacters[pplCount] = a;
                    System.out.println("Cleric selected:");//same as warrior but Cleric
                    System.out.println();
                       Weaponry e = selectAction(MainActions, MainActionCount);;
                       a.addtoMainAction(e);
                       a.status();

             }
}
       
          System.out.println();
          System.out.println("Returning to main menu");
          HeroCount++;//increases indexes
          pplCount++;
     }
     
void createMainAction()
     {
          Weaponry a = createweapon();
          MainActions[MainActionCount] = a;
          MainActionCount++;
          AllWeapons[WeaponCount] = a;
          WeaponCount++;
}
void createMainAction(Weaponry a)//used for file input
          {
               MainActions[MainActionCount] = a;
               MainActionCount++;
               AllWeapons[WeaponCount] = a;
               WeaponCount++;
     
          }

void createBonusAction()
     {
          Weaponry a = createweapon();
          BonusActions[BonusActionCount] = a; 
          BonusActionCount++;
          AllWeapons[WeaponCount] = a;
          WeaponCount++;
     }
void createBonusAction(Weaponry a)//used for file input
          {
               BonusActions[BonusActionCount] = a; 
               BonusActionCount++;
               AllWeapons[WeaponCount] = a;
               WeaponCount++;
          }
     
//create weapon with no parameters asks for user input. Create weapon with parameters uses passed values
Weaponry createweapon()// Creates a Weapon using input from the user
     {
               System.out.print("Please enter a name for the Weapon / ability ");//asks the user for input parameters
               String Name = scnr.next();
               System.out.println();
               System.out.print("Enter the number of sides on the dice: ");
               int diceroll = scnr.nextInt();
               System.out.println();
               System.out.print("Enter the number of spell slots this weapon / ability costs ");
               int Cost = scnr.nextInt();
               System.out.println();
               System.out.print("Enter the number of targets this weapon/ability can hit. for aoe abilities enter 0 ");
               int Targets = scnr.nextInt();
               String atributeused = selectatribute();
               System.out.println();
               System.out.print("If this ability heals. enter true if it does not enter false ");
               boolean heals = scnr.nextBoolean();
               System.out.print("If this ability can be used once per Short rest enter true");
               boolean Short = scnr.nextBoolean();
               System.out.print("If this ability can be used once per Long rest enter true");
               boolean Long = scnr.nextBoolean();
               return createweapon(Name,diceroll,Cost,Targets,atributeused,heals,Short,Long); //we pass the inputs from the user to the createweapon function that uses parameters to create the weapon
          }
     
Weaponry createweapon(String Name,int diceroll,int Cost,int Targets,String atributeused,boolean heals,boolean shortrest, boolean longrest)//this can be used for file input/
     {
                    if(heals == true)return new Healing(Name,diceroll,Cost,Targets,atributeused,shortrest,longrest);
                    return new Damage(Name,diceroll,Cost,Targets,atributeused,shortrest,longrest);
                    //checks if the weapon is a healing or damage weapon, and then create the weapon with the correct parameters
               }



//Select Methods
     
Weaponry selectAction(Weaponry []actions,int count)//Pass the array of actions and the count of actions to select from.
     {
          System.out.println();
          listelements(actions,count);//lists out the elements to choose from
          return actions[targetcheck(count)];//allows you to choose the index of one of those elements and then returns the weapon associated with that index
     }
               
     
Character selectTarget()//allows the user to select a character from the character list
     {
               System.out.println("Select the number for your target");
               System.out.println();
               listelements(allcharacters,pplCount);//lists all characters
               return allcharacters[targetcheck(pplCount)];//lets you select the index of the character you want to select and returns the character value
          }

String selectatribute()//allows the user to chose an atribute(str,dex,int,wis,con,char)
     {
               System.out.println();
               System.out.print("Please enter the atribute used(int = inteligence, str = strength, wis = wisdom, dex = dexterity, con = constitution, char = charisma) ");
               String atributeused = scnr.next();//ask for a string input for the atribute
               while(atributeused.equals("int") == false && atributeused.equals("str") == false && atributeused.equals("wis") == false && atributeused.equals("dex") == false && atributeused.equals("con") == false && atributeused.equals("char") == false)//checks that the string is a valid string 
                    {
                         System.out.print("Not a valid atribute. Please try again");
                         atributeused = scnr.next();
                    }
               return atributeused;//return the string of atribute used

          }
     
Character CurrentCharacter()//returns the current character who's turn it is
     {
               return TurnOrder[TurnOrderIndex];
          }


//List Methods
     

void listelements(NAME[] a,int count)//lists the elements in an array of an object that implements the interface NAME
     {
               for(int i = 0; i < count; i++)
                    {
                         System.out.println( i + ". " + a[i].getname());//lists all elements in the array given the max index
                    }
          }
     
void printturnorder()//prints out the turn order 
     {

               for(int i = 0;i < pplCount;i++)//for the amount of characters
                    {
                         System.out.println((i+1) + ". " + TurnOrder[TurnOrderIndex].getName());//check the current turn
                         TurnOrderIndex += 1;//increase the turn order index (will increase enough times to loop back to current persons turn)
                         if(TurnOrderIndex >= pplCount) TurnOrderIndex = 0;//reset to 0 if you get to the number people in the array)
                    }
          }
     

void statusprint()//prints out the status of all characters
     {
     for(int i = 0;i<pplCount ;i++)
          {
               allcharacters[i].status();
               System.out.println();
          }
     }

}