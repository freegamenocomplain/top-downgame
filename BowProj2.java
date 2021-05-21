//imports
import java.awt.Rectangle;

/*class BowProj2
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is a special projectile that roots on hit
 */

//class starts here
class BowProj2 extends Projectile{
  
  /**
   * BowProj2
   * Constructor for the BowProj2 class
   * @param projectileAnim the animation array of this projectile, hitbox the hitboxes of this projectile
   */
  BowProj2 (int[] projectileAnim, Rectangle[][] hitbox){
    //call super to set up the projectile
    super(projectileAnim, 40, hitbox, 20, 4, 1);
  }
  
  /**
   * BowProj2
   * Copy Constructor for the BowProj2 class
   * this constructor is used when this projectile is spawned into the game
   * @param projectileCopy the BowProj2 that this class will become a copy of, x the location that this projectile will be spawned
   * @param y the location this projectile will be spawned, direction the direction that this projectile faces, team the team that this projectile belongs to
   */
  BowProj2 (BowProj2 projectileCopy, double x, double y, double direction, int team){
    //call super to set up the projectile
    super(projectileCopy, x, y, direction, team);
  }
  
  /**
   * onHit
   * this method applies an on hit effect when hitting a player, in this case rooting for 20 frames
   * @param p, the Player hit by this projectile
   */
  public void onHit(Player p){
    p.setRoot(20);
  }
}