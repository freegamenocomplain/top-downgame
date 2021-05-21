//imports
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.Shape;

/*class Projectile
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class stores a projectile that is spawned by certain attacks
 */

//class starts here
class Projectile{
  //variable declaration
  private int[] projectileAnim; //projectiles could be animated, however this is not implemented in this build
  private int animationCounter;
  private double x;
  private double y;
  private double moveSpeed;
  private double xVelocity;
  private double yVelocity;
  //NOTE: the hitbox array is 2D because there could be multiple hitboxes in every frame of the projectile's animation
  //while no projectile in this game uses multiple hitboxes, this is kept here for design's sake and for easy future implementation
  private Rectangle[][] hitbox;
  private double direction;
  private int damage;
  private int moveStun;
  private int hitInvul;
  private int team;
  private int identifier;
  private boolean expire;
  
  /**
   * Projectile
   * Constructor for the Projectile class
   * @param projectileAnim the animation array of this projectile, moveSpeed the move speed of this projectile, hitbox the hitboxes of this projectile, damage the damage this projectile deals
   * @param stun the amount of frames this projectile restricts movement for, identifier the identifier of this projectile
   */
  Projectile (int[] projectileAnim, double moveSpeed, Rectangle[][] hitbox, int damage, int stun, int identifier){
    //set up variables using the parameters
    this.projectileAnim = projectileAnim;
    this.moveSpeed = moveSpeed;
    this.hitbox = hitbox;
    this.animationCounter = 0;
    this.damage = damage;
    this.moveStun = stun;
    this.hitInvul = 0;
    this.identifier = identifier;
  }
  
  /**
   * Projectile
   * Copy Constructor for the Projectile class
   * this constructor is used when the projectile is spawned into the game
   * @param projectileCopy the projectile that this class will become a copy of, x the location that this projectile will be spawned
   * @param y the location this projectile will be spawned, direction the direction that this projectile faces, team the team that this projectile belongs to
   */
  Projectile (Projectile projectileCopy, double x, double y, double direction, int team){
    //make this projectile a copy of projectileCopy and set up the parameters passed in
    this.projectileAnim = projectileCopy.getProjectileAnim();
    this.hitbox = projectileCopy.getHitBox();
    this.x = x;
    this.y = y;
    this.moveSpeed = projectileCopy.getMoveSpeed();
    this.direction = direction;
    this.animationCounter = 0;
    this.damage = projectileCopy.getDamage();
    this.moveStun = projectileCopy.getMoveStun();
    //calculate x and y velocity based on move speed and direction
    this.xVelocity = Math.sin(direction)*moveSpeed;
    this.yVelocity = -Math.cos(direction)*moveSpeed;
    this.identifier = projectileCopy.getIdentifier();
    this.team = team;
    this.expire = false;
  }
  
  /**
   * setExpire
   * setter for expire
   * this method sets this projectile to expire next frame
   */
  public void setExpire(){
    expire = true;
  }
  
  /**
   * getMoveSpeed
   * getter for moveSpeed
   * @return double, movement speed of this projectile
   */ 
  public double getMoveSpeed(){
    return moveSpeed;
  }
  
  /**
   * getHitBox
   * getter for hitbox
   * @return 2D Rectangle array, the raw hitbox of this object
   */
  public Rectangle[][] getHitBox () {
    return hitbox;
  }
  
  /**
   * process
   * move this projectile and animate it
   * @param none
   * @return boolean, false if this projectile needs to be removed
   */
  public boolean process(){
    //if the projectile is out of bounds or is set to expire, return false so it could be removed
    if((x<-10) || (y<-10) || (x>1260) || (y>1510) || (expire)){
      return false;
    }
    
    //increase it's animation counter if it's less than it's length
    if (animationCounter<projectileAnim.length-1){ //currently no projectiles are animated, but this is here for design and futureproofing
      animationCounter++;
    }
    
    //move projectile 
    x+=xVelocity;
    y+=yVelocity;
    
    //return true if this projectile is not to be removed
    return true;
  }
  
  /**
   * getCurrentHitBox
   * alternate getter for hitbox
   * @param j, the index of the hitbox array to be accessed
   * @return Shape, the current hitbox of this object, scaled to direction and the projectile's current location
   */
  public Shape getCurrentHitBox(int j){
    //make sure the animationCounter value is valid
    if (animationCounter == -1){
      animationCounter = 0;
    }
    
    if(hitbox[animationCounter][j]!=null){
      AffineTransform t = new AffineTransform(); //create an affinetransform to rotate the hitbox
      t.rotate(direction, x+(DisplayProjectile.getSprites(identifier)[getImage()].getWidth(null)/8), y+(DisplayProjectile.getSprites(identifier)[getImage()].getHeight(null)/8));
      Rectangle tempHit = new Rectangle((int)(x+hitbox[animationCounter][j].getX()), (int)(y+hitbox[animationCounter][j].getY()), (int)hitbox[animationCounter][j].getWidth(), (int)hitbox[animationCounter][j].getHeight());
      return t.createTransformedShape(tempHit); //return a rotated hitbox
    }
    return new Rectangle (0, 0, 0, 0);
  }
  
  /**
   * getProjectileAnim
   * getter for projectileAnim
   * @return int array, the animation array of this projectile
   */ 
  public int[] getProjectileAnim(){
    return projectileAnim;
  }
  
  /**
   * getX
   * getter for x
   * @return int, the horizontal location of this object
   */
  public int getX(){
    return (int)(Math.round(x));
  }
  
  /**
   * getY
   * getter for y
   * @return int, the vertical location of this object
   */ 
  public int getY(){
    return (int)(Math.round(y));
  }
  
  /**
   * getDirection
   * getter for direction
   * @return double, the direction of this projectile in radians
   */ 
  public double getDirection(){
    return direction;
  }
  
  /**
   * getDamage
   * getter for damage
   * @return int, the amount of damage this projectile deals
   */ 
  public int getDamage(){
    return damage;
  }
  
  /**
   * getMoveStun
   * getter for moveStun
   * @return int, the amount of frames this projectile restricts movement on hit
   */ 
  public int getMoveStun(){
    return moveStun;
  }
  
  /**
   * getHitInvul
   * getter for hitInvul
   * @return int, the amount of frames of invulnerability the hit player is granted
   */ 
  public int getHitInvul(){
    return hitInvul;
  }
  
  /**
   * getIdentifier
   * getter for identifier
   * @return int, the identifier of this projectile (used as a "name" for this projectile)
   */ 
  public int getIdentifier(){
    return identifier;
  }
  
  /**
   * getImage
   * getter for projectileAnim
   * @return int, the index of the current image of the projectile
   */ 
  public int getImage(){
    return projectileAnim[animationCounter]+(team-1);
  }
  
}