/*class DisplayPlayer
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is used to store information related to a player to be used for displaying
 */

//class starts here
class DisplayPlayer{
  //variable declaration 
  int x;
  int y;
  int health;
  int team;
  int xDirection;
  int yDirection;
  int image;
  boolean hasFlag;
  int action;
  int actionImage;
  double attackDirection;
  Weapon weapon;
  int respawnCounter;
  int skill1CD;
  
  /**
   * DisplayPlayer
   * Constructor for the DisplayPlayer class
   * @param x the horizontal coordinate, y the vertical coordinate, playerImage the image index of this player's current sprite, yDirection the vertical direction of this player, 
   * @param xDirection the horizontal direction of this player, hasFlag if the player is carrying a flag, action the identifier of the player's current action, actionImage the image index of the player's current action, 
   * @param attackDirection the direction the player's attack is facing, health the health of this player, team the team this player belongs to, respawnCounter the amount of time before this player respawns
   * @param weapon the weapon this player currently is using, skill1CD the skill cooldown of this player
   */
  DisplayPlayer(int x, int y, int playerImage, int yDirection, int xDirection, boolean hasFlag, int action, int actionImage, double attackDirection, int health, int team, int respawnCounter, Weapon weapon, int skill1CD){
    //set up variables based on parameters
    this.x = x;
    this.y = y;
    this.image = playerImage;
    this.xDirection = xDirection;
    this.yDirection = yDirection;
    this.hasFlag = hasFlag;
    this.action = action;
    this.actionImage = actionImage;
    this.attackDirection = attackDirection;
    this.health = health;
    this.team = team;
    this.respawnCounter = respawnCounter;
    this.weapon = weapon;
    this.skill1CD = skill1CD;
  }
  
  /**
   * getX
   * getter for x
   * @return int, the horizontal location of this object
   */ 
  public int getX(){
    return x;
  }
  
  /**
   * getY
   * getter for y
   * @return int, the vertical location of this object
   */ 
  public int getY(){
    return y;
  }
  
  /**
   * getHealth
   * getter for health
   * @return int, the current health of this object
   */ 
  public int getHealth(){
    return health;
  }
  
  /**
   * getTeam
   * getter for team
   * @return int, the team this object belongs to
   */ 
  public int getTeam(){
    return team;
  }
  
  /**
   * getXDirection
   * getter for xDirection
   * @return int, the horizontal direction of this object
   */ 
  public int getXDirection(){
    return xDirection;
  }
  
  /**
   * getYDirection
   * getter for yDirection
   * @return int, the vertical direction of this object
   */ 
  public int getYDirection(){
    return yDirection;
  }
  
  /**
   * getFlag
   * getter for hasFlag
   * @return boolean, true if this DisplayPlayer currently has the flag
   */ 
  public boolean getFlag(){
    return hasFlag;
  }
  
  /**
   * getImage
   * getter for current image of the DisplayPlayer
   * @return int, the index of the current image of the DisplayPlayer (for displaying)
   */
  public int getImage(){
    return image;
  }
  
  /**
   * getAction
   * getter for action
   * @return int, the identifier of the current action of this DisplayPlayer
   */
  public int getAction(){
    return action;
  }
  
  /**
   * getActionImage
   * getter for the image of the DisplayPlayer's current action
   * @return int, the index of the current image of the DisplayPlayer's action (for displaying)
   */
  public int getActionImage(){
    return actionImage;
  }
  
  /**
   * getAttackDirection
   * getter for attackDirection
   * @return double, the direction of this object's attack
   */ 
  public double getAttackDirection(){
    return attackDirection;
  }
  
  /**
   * getWeapon
   * getter for Weapon
   * @return Weapon, the Weapon object of this DisplayPlayer
   */
  public Weapon getWeapon(){
    return weapon;
  }
  
  /**
   * getRespawnCounter
   * getter for respawnCounter
   * @return int, the amount of frames until this DisplayPlayer respawns
   */ 
  public int getRespawnCounter(){
    return respawnCounter;
  }
  
  /**
   * getSkill1CD
   * getter for skill1CD
   * @return int, current cooldown of skill 1
   */
  public int getSkill1CD(){
    return skill1CD;
  }
}