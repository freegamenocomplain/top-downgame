//imports
import java.awt.Rectangle;

/*class HealthLocation
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is used to store information belonging to a health location, or a healthpack
 * health locations heal players who stand on them
 */

//class starts here
class HealthLocation{
  //variable declaration =
  int cooldown;
  Rectangle hitbox;
  
  /**
   * HealthLocation
   * Constructor for the HealthLocation class
   * @param x the horizontal location of this object, y the vertical location of this object, width the width of this object, height the height of this object
   */
  HealthLocation(int x,int y,int width,int height){
    //create this object's bounding box
    this.hitbox = new Rectangle(x, y, width, height);
  }
  
  /**
   * getCooldown
   * getter for cooldown
   * @return boolean, true if this object is currently on cooldown
   */ 
  public boolean getCooldown(){
    if(cooldown<=0){
      return false;
    }
    return true;
  }
  
  /**
   * processCooldown
   * decreases the cooldown of the this HealthLocation
   */
  public void processCooldown(){
    if(cooldown>0){
      cooldown--;
    }
  }
  
  /**
   * heal
   * heals a player
   * @param p the player that this HealthLocation will heal
   */
  public void heal(Player p){
    if(p.getHealth()<100){ //if player is not on full health
      p.addHealth(35); //heal the player
      cooldown = 300; //put this object on cooldown
    }
  }
  
  /**
   * getHitbox
   * getter for hitbox
   * @return Rectangle, the bounding box of this Flag Location
   */ 
  public Rectangle getHitbox(){
    return hitbox;
  }

}