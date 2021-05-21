/*interface MovingAttack
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this interface is used for attacks that move the player
 */

//interface starts here
interface MovingAttack{
  
  /**
   * getXMove
   * getter for xMove 
   * @param i, the index of the xMove array needed
   * @return double, the distance this attack moves the player on the ith frame
   */ 
  public double getXMove(int i);
}