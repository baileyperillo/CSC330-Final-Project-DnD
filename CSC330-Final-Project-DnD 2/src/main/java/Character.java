interface NAME//used so i can make functions that return the string name for both characters and weapons
{
  String getname();
}



class Character implements NAME{  //a base class used to create all characters in the game. Implements NAME to be used in the generalized display method
  public String name;
  int MaxHP;//maximum hp a character has
  int CurrentHP;//current hp
  int level;//changes how many spell slots you have access to.
  int ArmorClass;
  int Maxspellslots;//spell slots used for certain spells and abilities
  int spellslots;
  int strength; //stats that apply when a weapon has a atribute modifier
  int dexterity;
  int intelligence;
  int wisdom;
  int constitution;
  int charisma;
  
  Weaponry[] Weapons = new Weaponry[80]; //All weapons in inventory
  Weaponry [] MainActions = new Weaponry[40]; //Weapons / abilities that take up a main action
  Weaponry [] BonusActions = new Weaponry[40]; // abilities that take up a bonus action
  int WeaponCount = 0;
  int MainActionCount = 0;
  int BonusActionCount = 0;
  
public String getname(){//implementation needed for NAME
    return name;
    }

  
Character(String Name, int Health, int Level, int AC, int Strength, int Dexterity, int Intelligence, int Wisdom, int cons, int chars) //character constructor
  {
    name = Name;
    MaxHP = Health;
    CurrentHP = Health;
    level = Level;
    ArmorClass = AC;
    strength = Strength;
    dexterity = Dexterity;
    intelligence = Intelligence;
    wisdom = Wisdom;
    constitution = cons;
    charisma = chars;
    int Maxspellslots = 0;//inherited by multiple character classes
    int spellslots = 0;
  }
  Character()
  {
    name = "";
    MaxHP = 0;
    CurrentHP = 0;
    level = 0;
    ArmorClass = 0;
    strength = 0;
    dexterity = 0;
    intelligence = 0;
    wisdom = 0;
    constitution = 0;
    charisma = 0;
    int Maxspellslots = 0;
    int spellslots = 0;
  }
  void addtoMainAction(Weaponry w)//adds a weapon to the main action list (for easier search of main actions)
  {
    Weaponry a = w;
    Weapons[WeaponCount] = a;
    WeaponCount++;
    MainActions[MainActionCount] = w;
    MainActionCount++;
  }
  void addtoBonusAction(Weaponry w)//adds a weapon to the bonus action list (for easier search of bonus actions)
    {
      Weaponry a = w;
      Weapons[WeaponCount] = a;
      WeaponCount++;
      BonusActions[BonusActionCount] = w;
      BonusActionCount++;
    }
  //A bunch of set and get functions for the character class
    void setName(String inputName){
      name = inputName;
    }
  
    String getName(){
      return name;
    }
  


  
    void setMaxHP(int inputMaxHP) //will be used for level up scenarios.
    {
      CurrentHP += inputMaxHP - MaxHP;//when raising max hp, we will add the extra hp onto the current total. EX   (if i had 5 hp and 10 max hp. and my max hp goes up to 12. I would do 12-10 (2) and add that to my current 5+2 = 7 current. so i would go from 5/10 to 7/12.)
      MaxHP = inputMaxHP; 
      
    }
    int getMaxHP()
    {
      return MaxHP;
    }
    void setCurrentHP(int inputCurrentHP)
    {
      CurrentHP = inputCurrentHP;
    }
    int getCurrentHP()
    {
      return CurrentHP;
    }
  void setlevel(int x)
  {
    level = x;
  }
  void setArmorClass(int x)
  {
    ArmorClass = x;
  }
  void setstrength (int x)
  {
    strength = x;
  }
  void setdexterity (int x)
  {
    dexterity = x;
  }
  void setintelligence(int x)
  {
    intelligence = x;
  }
  void setwisdom(int x)
  {
    wisdom = x;
  }
  void setconstitution(int x)
  {
    constitution = x;
  }
  void setcharisma(int x)
  {
    charisma = x;
  }
  void setMaxSpellSlots(int x)
  {
    spellslots += x - Maxspellslots;//when raising max hp, we will add the extra hp onto the current total. EX   (if i had 5 hp and 10 max hp. and my max hp goes up to 12. I would do 12-10 (2) and add that to my current 5+2 = 7 current. so i would go from 5/10 to 7/12.)
    Maxspellslots = x;
  }
   // End of set and get functions for the character class
  void takedamage(int x)//used in Weaponry class in order to deal damage to the targeted character
  {
    System.out.println(name + " took " + x + " damage!");
    CurrentHP -= x;
    if(CurrentHP > 0) System.out.println(name + " has " + CurrentHP + " / "+ MaxHP + " HP left");
    else System.out.println(name + " has died!");
  }
  void RecieveHealing(int x)//used in the Weaponry class in order to heal the targeted character
  {
    if(CurrentHP < 0)//if the character targeted is dead
    {
      System.out.println(name + " is dead and can not be healed");//they can not be healed
    }
    else{
      System.out.println(name + " Recieved " + x + " HP!");//if they have more than 0 hp
      CurrentHP += x;
      if (CurrentHP > MaxHP) CurrentHP = MaxHP;//checks that currenthp dosent go above maxhp
      System.out.println(name + " has " + CurrentHP + " / "+ MaxHP + " HP left");//they get healed
    }
  }
 
  
  
  

  void status(){ //used to print out the main atributes of a character. adds the characters class and also spell slots if they have any
      System.out.println("STATUS");
      System.out.println("Name: " + name);
      System.out.println("Health: " + CurrentHP + "/" + MaxHP);
      System.out.println("Level: " + level);
      System.out.println("Armorclass: " + ArmorClass);
    System.out.println("strength: " + strength);
    System.out.println("dexterity: " + dexterity);
    System.out.println("intelligence: " + intelligence);
    System.out.println("wisdom: " + wisdom);
    System.out.println("constitution: " + constitution);
    System.out.println("charisma: " + charisma);
  }
  void levelup()//temporarily just increases all stats by 1. and hp by 4
    {
      level++;
      setMaxHP(getMaxHP()+4);
      if(Maxspellslots!= 0)setMaxSpellSlots(Maxspellslots+1);//only increases spellslots by 1 if the character has spellslots.
      ArmorClass++;
      strength++; 
      dexterity++;
      intelligence++;
      wisdom++;
      constitution++;
      charisma++;
    }
}

class Hero extends Character{//adds 
  Hero(String Name, int Health, int Level, int AC, int Strength, int Dexterity, int Intelligence, int Wisdom, int cons, int chars) // hero constructor (just inherits character constructor) for now
  {
    super(Name,Health,Level,AC,Strength,Dexterity,Intelligence,Wisdom,cons,chars);
  }
  void shortRest()//used by gameplay class to give characters a short rest
  {
    RecieveHealing(MaxHP/2);//heals
    for(int i = 0; i < WeaponCount; i++)
      {
        Weapons[i].SRcharge = Weapons[i].ShortRest;//resets all weapons short rest charge
      }
  }
  void LongRest()//used by gameplay class to give characters a long rest
  {
    for(int i = 0; i < WeaponCount; i++)
      {
        Weapons[i].SRcharge = Weapons[i].ShortRest;//resets all weapons short rest charge
        Weapons[i].LRcharge = Weapons[i].LongRest;//resets all weapons long rest charge
      }
    RecieveHealing(MaxHP);//heals
  }
}

class Warrior extends Hero{//one of the character classes(dosent have spell slots)
  Warrior (String Name, int Health, int Level, int AC, int Strength, int Dexterity, int Intelligence, int Wisdom, int cons, int chars)
  {
    super(Name,Health,Level,AC,Strength,Dexterity,Intelligence,Wisdom,cons,chars);
  }
  void status()
  {
    super.status();
    System.out.println("Character Class: Warrior");//add current class to status
  }
  
}

class Mage extends Hero{//one of the character classes that has spell slots
  Mage (String Name, int Health, int Level, int AC, int Strength, int Dexterity, int Intelligence, int Wisdom, int cons, int chars)
  {
    super(Name,Health,Level,AC,Strength,Dexterity,Intelligence,Wisdom,cons,chars);
    spellslots = 3 + Level;//# of spell slots is equal to the level of the character + 3
    Maxspellslots = spellslots;
  }
  void LongRest()
  {
    super.LongRest();
    spellslots = Maxspellslots;//has to reset spell slots on long rest

  }
  void status()
  {
    super.status();
    System.out.println("Spellslots: " + spellslots+ "/" + Maxspellslots);//add spell slots to status
    System.out.println("Character Class: Mage");//add current class to status
  }
}

class Cleric extends Hero{//same as mage for now but has Cleric as their character class in status output
  Cleric (String Name, int Health, int Level, int AC, int Strength, int Dexterity, int Intelligence, int Wisdom, int cons, int chars)
  {
    super(Name,Health,Level,AC,Strength,Dexterity,Intelligence,Wisdom,cons,chars);
    spellslots = 3 + Level;
    Maxspellslots = spellslots;
  }
 void LongRest()
  {
    super.LongRest();
    spellslots = Maxspellslots;
      
  }
  void status()
  {
    super.status();
    System.out.println("Spellslots: " + spellslots+ "/" + Maxspellslots);
    System.out.println("Character Class: Cleric");
  }

  
}



class Enemy extends Character {//constructor for enemy units (might add more to make them unique to character class)
     Enemy(String Name, int Health, int Level, int AC, int Strength, int Dexterity, int Intelligence, int Wisdom, int cons, int chars) // enemy constructor (just inherits character constructor) for now
     {
       super(Name,Health,Level,AC,Strength,Dexterity,Intelligence,Wisdom,cons,chars);
     }
    


}

