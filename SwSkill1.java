//imports
import java.awt.Rectangle;

/*class SwSkill1
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is a seperately coded skill
 * this skill is a dashing attack that moves the player forwards
 */

//class starts here
class SwSkill1 extends Skill implements MovingAttack{
  //variable declaration
  static int cooldown = 90; //cooldown of 90 frames or 3 secs
  int[] playerAnim; //the player animation of this skill
  double[] xMove; //the movement of this skill
  
  /**
   * SwSkill1
   * Constructor for the SwSkill1 class
   * @param atkAnim the animation array of this skill, hitbox the hitboxes of this skill, xMove the movement array of this skill
   */
  SwSkill1 (int[] atkAnim, Rectangle[][] hitbox, double[] xMove){
    //call to super to set up the skill
    super(2, atkAnim, hitbox, 30, 4, 7, 4, 6, -65, 76, 330, null, null);
    //set up playerAnim
    int[] playerAnim = {6, 72, 72, 7, 7, 7, 7, 7, 7, 8, 8};
    this.playerAnim = playerAnim;
    this.xMove = xMove; //set up xMove
  }
  
  /**
   * getPlayerAnim
   * getter for playerAnim
   * @return int array, the player animation of this skill (specific player sprites to be displayed while attacking)
   */ 
  @Override
  public int[] getPlayerAnim(){
    return playerAnim;
  }
  
  /**
   * getCoolDown
   * getter for cooldown
   * @return int, the amount of cooldown this skill has
   */ 
  @Override
  public int getCoolDown(){
    return cooldown;
  }
  
  /**
   * activate
   * a method that runs when the skill begins
   * @param p, the player that has activated this skill
   */ 
  @Override
  public void activate(Player p){
    
    //change the player's direction to match this skill's direction
    double direction = Math.toDegrees(p.getAttackDirection());
    if((direction<337.5) && (direction>=202.5)){
      p.setXDirection(0);
    } else if ((direction<157.5) && (direction>= 22.5)){
      p.setXDirection(2);
    } else {
      p.setXDirection(1);
    }
    if((direction<67.5) || (direction>= 292.5)){
      p.setYDirection(0);
    } else if((direction<247.5) && (direction>=112.5)){
      p.setYDirection(2);
    } else {
      p.setYDirection(1);
    }
    
    //the player cannot move during this skill
    p.setMoveStun(7);
    
    //put this skill on cooldown (current cooldown is located in weapon)
    p.getWeaponObj().setSkill1Cooldown(cooldown);
  }
  
  /*
   * getXMove
   * getter for xMove 
   * @param i, the index of the xMove array needed
   * @return double, the distance this attack moves the player on the ith frame
   */ 
  @Override
  public double getXMove(int i){
    if (xMove != null){
      return xMove[i];
    } 
    return 0;
  }
}