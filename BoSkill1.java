//imports 
import java.awt.Rectangle;

/*class BoSkill1
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is a seperately coded skill
 * this skill shoots a special arrow that roots on hit
 */

//class starts here
class BoSkill1 extends Skill implements ProjectileSpawning{
  //variable declaration
  static int cooldown = 120; //cooldown of 120 frames or 4 secs
  
  /**
   * BoSkill1
   * Constructor for the BoSkill1 class
   * @param atkAnim the animation array of this skill, hitbox the hitboxes of this skill, projectile the projectile(s) this skill spawns, projectileLocation the location of the projectile this skill spawns
   */
  BoSkill1 (int [] atkAnim, Rectangle [][] hitbox, Projectile [][] projectile, double[][][] projectileLocation){
    //call to super to set up the rest of this skill
    super(2, atkAnim, hitbox, 0, 0, 7, 2, -24, -10, 195, 108, projectile, projectileLocation);
  }
  
  /**
   * getPlayerAnim
   * getter for playerAnim
   * @return null, this skill does not call for a specific animation
   */ 
  @Override
  public int[] getPlayerAnim(){
    return null;
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
    //put this skill on cooldown (current cooldown is located in weapon)
    p.getWeaponObj().setSkill1Cooldown(cooldown);
  }
}