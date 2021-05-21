//imports
import java.awt.Image;

/*abstract class Weapon
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is the superclass that all seperately coded Weapons extend from
 */

//class starts here
abstract class Weapon{
  
  /**
   * getIdleX
   * getter for idleX
   * @return int, the x coordiante of this weapon when idle relative to the player (used for displaying)
   */ 
  public abstract int getIdleX();
  
  /**
   * getIdleY
   * getter for idleY
   * @return int, the y coordiante of this weapon when idle relative to the player (used for displaying)
   */
  public abstract int getIdleY();
  
  /**
   * getSprites
   * getter for sprites
   * @return Image array, the sprites of this attack (used for displaying)
   */
  public abstract Image[] getSprites();
  
  /**
   * getAttack
   * getter for attacks and skills
   * @param i, the identifier of the attack to be accessed
   * @return Attack, the attack of this weapon depending on the identifier used
   */
  public abstract Attack getAttack(int i);
  
  /**
   * getMoveSpeed
   * getter for moveSpeed
   * @return int, the move speed of a player carrying this weapon
   */
  public abstract int getMoveSpeed();
  
  /**
   * getSkill1Cooldown
   * getter for skill1CD
   * @return int, current cooldown of skill 1
   */
  public abstract int getSkill1Cooldown();
  
  /**
   * setSkill1Cooldown
   * setter for skill1CD
   * @param i, the value that the current cooldown of skill 1 will be set to
   */
  public abstract void setSkill1Cooldown(int i);
  
  /**
   * processCooldown
   * decreases the cooldown of the skill(s) of this weapon
   */
  public abstract void processCooldown();
}