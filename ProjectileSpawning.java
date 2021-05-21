/*interface ProjectileSpawning
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this interface is used for attacks and skills that spawn projectiles
 */

//interface starts here
interface ProjectileSpawning{
  
  /**
   * getProjectile
   * getter for projectile
   * @param i, the index of the projectile array needed
   * @return Projectile array, the Projectile(s) this attack spawns at the ith frame
   */
  public Projectile[] getProjectile(int i);
  
  /**
   * getProjectileX
   * getter for projectileLocation 
   * @param i and j, the index of the projectileLocation needed
   * @return double, the location that the projectile this attack creates spawn at (X coordinate)
   */ 
  public double getProjectileX(int i, int j);
  
   /*
   * getProjectileY
   * getter for projectileLocation 
   * @param int i and j, the index of the projectileLocatin needed
   * @return double, the location that the projectile this attack creates spawn at (Y coordinate)
   */
  public double getProjectileY(int i, int j);
}