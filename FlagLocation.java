//imports
import java.awt.Rectangle;

/*class FlagLocation
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is used to store information belonging to a flag location, or the objective of a team
 * the flag location stores the flag, which could be picked up by players and taken to their own flag location
 */

//class starts here
class FlagLocation{
  //variable declaration
  boolean flag;
  Rectangle hitbox;
  int team;
  
  /**
   * FlagLocation
   * Constructor for the FlagLocation class
   * @param x the horizontal location of this object, y the vertical location of this object, width the width of this object
   * @param height the height of this object, team the team this object belongs to
   */
  FlagLocation(int x,int y,int width,int height,int team){
    //create a hitbox based on the x, y, width and height
    this.hitbox = new Rectangle(x, y, width, height);
    //set team and flag
    this.team = team;
    this.flag = true;
  }
  
  /**
   * setFlag
   * setter for the flag of the FlagLocation
   * the flag represents if there is currently a flag at this FlagLocation
   * @param b, the new flag value of the FlagLocation
   */ 
  public void setFlag(boolean b){
    flag = b;
  }
  
  /**
   * getFlag
   * getter for flag
   * @return boolean, true if this FlagLocation currently has the flag
   */ 
  public boolean hasFlag(){
    return flag;
  }
  
  /**
   * getHitbox
   * getter for hitbox
   * @return Rectangle, the bounding box of this Flag Location
   */ 
  public Rectangle getHitbox(){
    return hitbox;
  }
  
  /**
   * getTeam
   * getter for team
   * @return int, the team this object belongs to
   */ 
  public int getTeam(){
    return team;
  }
}