//imports
import java.awt.Rectangle;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/*class Bow
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is the bow weapon used by the player, specializing in projectile attacks and ranged combat
 * this class stores the weapon's images, attacks and other information
 */

//class starts here
class Bow extends Weapon{
  //variable declaration
  private static Image[] sprites;
  private static Attack atk1;
  private static Skill skill1;
  private static int idleX;
  private static int idleY;
  private static int moveSpeed;
  private int skill1CD;
  
  /**
   * loadAssets
   * loads the images used by this weapon and sets up attacks
   */ 
  static void loadAssets(){
    
    //load the images used by this weapon
    sprites = new Image[8];
    Image[] arrow1 = new Image[2];
    try{
      for(int i = 1; i<=4; i++){
        sprites[i-1] = ImageIO.read(new File("bow000"+i+".png"));
      }
      for(int i = 1; i<=4; i++){
        if (i>=10){
          sprites[i+3] = ImageIO.read(new File("atkbo100"+i+".png"));
        } else {
          sprites[i+3] = ImageIO.read(new File("atkbo1000"+i+".png"));
        }
      }
      arrow1[0] = ImageIO.read(new File("projbo10001.png"));
      arrow1[1] = ImageIO.read(new File("projbo10002.png"));
    } catch(IOException e){
      System.out.println("Image loading failed");
      e.printStackTrace();
    }
    
    //set idle x and idle y, the location this weapon is displayed (relative to player) when idle
    idleX = -10;
    idleY = -20;
    
    //set up the basic attack of the bow
    int[] atk1Anim = {4, 5, 6, 7, 7, 7, 7, 7, 7, 7, 7, 7, 7};
    Rectangle[][] arrow1HitBox = new Rectangle[1][1];
    arrow1HitBox[0][0] = new Rectangle(0, 0, 10, 38);
    Projectile proj1 = new Projectile(new int[1], 40, arrow1HitBox, 15, 4, 0);
    Projectile[][] atk1proj = new Projectile[13][1];
    atk1proj[1][0] = proj1;
    double[][][]atk1projLocation = new double [13][1][2];
    atk1projLocation[1][0][0] = 21;
    atk1projLocation[1][0][1] = -3;
    atk1 = new Attack(1, atk1Anim, new Rectangle [13][1], 0, 0, 7, 2, -24, -10, 195, 108, atk1proj, atk1projLocation);
    
    //set up the movement imparing skill of the bow
    Projectile proj2 = new BowProj2(new int[1], arrow1HitBox);
    Projectile[][] atk2proj = new Projectile[13][1];
    atk2proj[1][0] = proj2;
    skill1 = new BoSkill1(atk1Anim, new Rectangle [13][1], atk2proj, atk1projLocation);
    
    //set the movespeed of this weapon
    moveSpeed = 13;   
  }
  
  /**
   * getIdleX
   * getter for idleX
   * @return int, the x coordiante of this weapon when idle relative to the player (used for displaying)
   */ 
  @Override
  public int getIdleX(){
    return idleX;
  }
  
  /**
   * getIdleY
   * getter for idleY
   * @return int, the y coordiante of this weapon when idle relative to the player (used for displaying)
   */
  @Override
  public int getIdleY(){
    return idleY;
  }
  
  /**
   * getSprites
   * getter for sprites
   * @return Image array, the sprites of this attack (used for displaying)
   */
  @Override
  public Image[] getSprites(){
    return sprites;
  }
  
  /**
   * getAttack
   * getter for atk1 and skill1
   * @param i, the identifier of the attack to be accessed
   * @return Attack, the attack of this weapon depending on the identifier used
   */
  @Override
  public Attack getAttack(int i){
    if(i == 1){
      return atk1;
    } else if(i == 2){
      return skill1;
    }
    return null;
  }
  
  /**
   * getMoveSpeed
   * getter for moveSpeed
   * @return int, the move speed of a player carrying this weapon
   */
  @Override
  public int getMoveSpeed(){
    return moveSpeed;
  }
  
  /**
   * setSkill1Cooldown
   * setter for skill1CD
   * @param i, the value that the current cooldown of skill 1 will be set to
   */
  @Override
  public void setSkill1Cooldown(int i){
    skill1CD = i;
  }
  
  /**
   * getSkill1Cooldown
   * getter for skill1CD
   * @return int, current cooldown of skill 1
   */
  @Override
  public int getSkill1Cooldown(){
    return skill1CD;
  }
  
  /**
   * processCooldown
   * decreases the cooldown of the skill(s) of this weapon
   */
  @Override
  public void processCooldown(){
    if(skill1CD>0){
      skill1CD--;
    }
  }
}