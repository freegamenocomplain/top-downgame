//imports
import java.awt.Rectangle;

/*abstract class Skill
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is the superclass that all seperately coded skills extend from
 */

//class starts here
abstract class Skill extends Attack{
  
  /**
   * Skill
   * Constructor for the SKill class, pases all parameters to the superclass
   * @param attackIdentifier the identifier for this skill, atkAnim the animation array of this skill, hit the hitboxes of this skill, damage the damage this skill deals
   * @param moveStun the amount of frames this skill restricts movement for, selfStun the amount of time this skill stuns the user, imageX the location of the sprite relative to the player (x coordinate)
   * @param imageY the location of the sprite relative to the player (y coordinate), rotateX the rotational axis for this skill's sprite (x coordinate), rotateY the rotational axis for this skill's sprite (y coordinate)
   * @param projectile the projectile(s) this skill spawns, projectileLocation the location of the projectile this skill spawns
   */
  Skill (int attackIdentifier, int[] atkAnim, Rectangle[][] hit, int damage, int moveStun, int selfStun, int hitInvul, int imageX, int imageY, int rotateX, int rotateY, Projectile[][] projectile, double[][][] projectileLocation){
    super(attackIdentifier, atkAnim, hit, damage, moveStun, selfStun, hitInvul, imageX, imageY, rotateX, rotateY, projectile, projectileLocation);
  }
  
  /**
   * getPlayerAnim
   * getter for playerAnim
   * @return int array, the player animation of this skill (specific player sprites to be displayed while attacking)
   */ 
  public abstract int[] getPlayerAnim();
  
  /**
   * getCoolDown
   * getter for cooldown
   * @return int, the amount of cooldown this skill has
   */ 
  public abstract int getCoolDown();
  
  /**
   * activate
   * a method that runs when the skill begins
   * @param p, the player that has activated this skill
   */ 
  public abstract void activate(Player p);
}