import java.util.Random;
abstract public class Weaponry implements NAME { // This is a class that represents the weapons that the player can use in the game. Implements NAME to be used in the generalized display method
    Random random = new Random();
    String name; //name of weapon or ability
    int damage; //The number of sides on the dice rolled
    int SpellSlotCost;//spell slot cost
    int NumberOfTargets;//# of targets it can hit 0 for aoe (unlimited targets)
    String atribute;//atribute used by the weapon
    boolean SRcharge;//short rest charge
    boolean LRcharge;//long rest charge
    boolean ShortRest;//does the weapon require a shortrest charge
    boolean LongRest;//does the weapon require a longrest charge
Weaponry(String Name,int diceroll,int Cost, int Targets,String atributeused,boolean shortrest,boolean longrest) 
    {//passes the weapons name, the amount of sides on the dice it rolls, the spell slot cost, the number of targets it can hit (0 for aoe). the atribute from character used to increase the dices roll, wether or not it can be used once per short rest, wether or not it can be used once per long rest.
        name = Name;
        damage = diceroll;
        SpellSlotCost = Cost;
        NumberOfTargets = Targets;
        atribute = atributeused;
        ShortRest = shortrest;
        SRcharge = shortrest;//used to check if the weapon can be used. resets to true on rest
        LongRest = longrest;
        LRcharge = longrest;//used to check if the weapon can be used. resets to true on rest
    }
    int Dice(int x /* determines how many sides are on the dice*/ ) {  //use for any and all dice rolls

         return random.nextInt(x)+1;//takes a random int 0->(x-1) adds 1 so it becomes 1->x
    }
    abstract void AtackDMG(Character target, int bonus);//abstract method implemented in Healing and Damage classes. This method is used to call the function the weapon uses after its used to atack someone
    
    public String getname(){//used for the implementation of interface NAME 
        return name;
        }

    
} 


class Healing extends Weaponry // used for all abilities,weapons,items that heal
    {
        Healing(String Name,int diceroll,int Cost, int Targets,String atributeused,boolean shortrest,boolean longrest) //default
            {
                super(Name,diceroll,Cost,Targets,atributeused,shortrest,longrest);    
            }
        void AtackDMG(Character target, int bonus)//healing always makes contact with the target.
        {
            target.RecieveHealing(Dice(damage)+bonus);//dice roll + bonus from stats = healing receieved
        }
    }

class Damage extends Weaponry{ //used for all abilities,weapons,and items that deal damage
    Damage(String Name,int diceroll,int Cost, int Targets,String atributeused,boolean shortrest,boolean longrest) //default
        {
            super(Name,diceroll,Cost,Targets,atributeused,shortrest,longrest);    
        }
    void AtackDMG(Character target, int bonus)//damages the target based on the dice roll and the bonus. checks if your atack made contact
        {
            if(rolltohit(bonus,target))target.takedamage(Dice(damage)+bonus);//if you hit you deal damage
            else System.out.println("You missed");//if you miss it prints that you missed
        }
    boolean rolltohit(int bonus, Character target)//when dealing damage to a target. you must roll a dice to see if your atack connects. The odds of it connecting is based on the target's AC against a roll of a d20 + the bonus from your stats
    {
        int roll = Dice(20) + bonus;//rolls a d20 + bonus
        if(roll >= target.ArmorClass) return true;//if the roll + bonus is > targets armorclass. you will hit
        else return false;//if not you will miss
    }
}
