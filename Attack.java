//imports
import java.awt.Rectangle;

/*class Attack
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class stores information to be used for a basic attack
 */

//class starts here
class Attack implements ProjectileSpawning{
  
  //variable declaration
  private int attackIdentifier;
  private int[] attackAnim;
  private int duration;
  //NOTE: the hitbox array is 2D because there could be multiple hitboxes in every frame of the attack
  //while no attack in this game uses multiple hitboxes, this is kept here for design's sake and for easy future implementation
  private Rectangle[][] hitbox;
  private int damage;
  private int moveStun;
  private int selfStun;
  private int hitInvul = 2;
  private int imageX;
  private int imageY;
  private int rotateX;
  private int rotateY;
  //projectile storing variables
  //same thing goes here as the hitboxes, while there is no multiple projectile spawning attack in the current build, there could be in the future
  private Projectile[][] projectile;
  private double[][][] projectileLocation;
  
  /**
   * Attack
   * Constructor for the Attack class
   * @param attackIdentifier the identifier for this attack, atkAnim the animation array of this attack, hit the hitboxes of this attack, damage the damage this attack deals
   * @param moveStun the amount of frames this attack restricts movement for, selfStun the amount of time this attack stuns the user, imageX the location of the sprite relative to the player (x coordinate)
   * @param imageY the location of the sprite relative to the player (y coordinate), rotateX the rotational axis for this attack's sprite (x coordinate), rotateY the rotational axis for this attack's sprite (y coordinate)
   * @param projectile the projectile(s) this attack spawns, projectileLocation the location of the projectile this attack spawns
   */
  Attack (int attackIdentifier, int[] atkAnim, Rectangle[][] hit, int damage, int moveStun, int selfStun, int hitInvul, int imageX, int imageY, int rotateX, int rotateY, Projectile[][] projectile, double[][][] projectileLocation){
    //set all parameters as variables
    this.attackIdentifier = attackIdentifier;
    this.attackAnim = atkAnim;
    this.duration = attackAnim.length-1;
    this.hitbox = hit;
    this.damage = damage;
    this.moveStun = moveStun;
    this.selfStun = selfStun;
    this.projectile = projectile;
    this.projectileLocation = projectileLocation;
    this.hitInvul = hitInvul;
    this.imageX = imageX;
    this.imageY = imageY;
    this.rotateX = rotateX;
    this.rotateY = rotateY;
  }
  
  
  /**
   * getAttackIdentifier
   * getter for attackIdentifier
   * @return int, identifier of this attack (used to access this attack in the Weapon class)
   */ 
  public int getAttackIdentifier(){
    return attackIdentifier;
  }
  
  /**
   * getAttackAnim
   * getter for attackAnim
   * @param i, the index of the attackAnim array needed
   * @return int, the ith value of the attackAnim array (used to animate the attack and make calls to certain images)
   */ 
  public int getAttackAnim(int i){
    return attackAnim[i];
  }
  
  /**
   * getDuration
   * getter for Duration
   * @return int, the amount of frames this attack takes
   */ 
  public int getDuration(){
    return duration;
  }
  
  /**
   * getHitInvul
   * getter for hitInvul
   * @return int, the amount of frames of invulnerability the player hit by this attack is granted
   */ 
  public int getHitInvul(){
    return hitInvul;
  }
  
  /**
   * getDamage
   * getter for damage
   * @return int, the amount of damage this attack deals
   */ 
  public int getDamage(){
    return damage;
  }
  
  /**
   * getMoveStun
   * getter for moveStun
   * @return int, the amount of frames this attack restricts movement for
   */
  public int getMoveStun(){
    return moveStun;
  }
  
  
  /**
   * getHitBox
   * getter for hitbox 
   * @return 2D Rectangle array, the hitboxes of this attack
   */ 
  public Rectangle[][] getHitBox () {
    return hitbox;
  }
  
  /**
   * getSelfStun
   * getter for selfStun
   * @return int, the amount of frames this attack stuns the user
   */ 
  public int getSelfStun(){
    return selfStun;
  }

  /**
   * getImageX()
   * getter for imageX
   * @return int, the x coordinate of this attack's sprite relative to the player
   */
  public int getImageX(){
    return imageX;
  }
  
  /**
   * getImageY()
   * getter for imageY
   * @return int, the y coordinate of this attack's sprite relative to the player
   */
  public int getImageY(){
    return imageY;
  }
  
  /**
   * getRotateX()
   * getter for rotateX
   * @return int, the x coordinate of this attack's rotational axis (used for displaying)
   */
  public int getRotateX(){
    return rotateX;
  }
  
  /**
   * getRotateY()
   * getter for rotateY
   * @return int, the y coordinate of this attack's rotational axis (used for displaying)
   */
  public int getRotateY(){
    return rotateY;
  }
  
  /**
   * getProjectile
   * getter for projectile
   * @param i, the index of the projectile array needed
   * @return Projectile array, the Projectile(s) this attack spawns at the ith frame
   */ 
  @Override
  public Projectile[] getProjectile(int i){
    if (projectile!=null){
      return projectile[i];
    }
    return null;
  }
  
  /**
   * getProjectileX
   * getter for projectileLocation 
   * @param i and j, the index of the projectileLocation needed
   * @return double, the location that the projectile this attack creates spawn at (X coordinate)
   */ 
  @Override
  public double getProjectileX(int i, int j){
    if (projectileLocation != null){
      return projectileLocation[i][j][0];
    }
    return 0;
  }
  
  /*
   * getProjectileY
   * getter for projectileLocation 
   * @param int i and j, the index of the projectileLocatin needed
   * @return double, the location that the projectile this attack creates spawn at (Y coordinate)
   */
  @Override
  public double getProjectileY(int i, int j){
    if (projectileLocation != null){
      return projectileLocation[i][j][1];
    }
    return 0;
  }
}