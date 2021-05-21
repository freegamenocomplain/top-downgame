//imports
import java.awt.Rectangle;
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/*class Sword
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is the sword weapon used by the player, specializing in close ranged combat and mobility
 * this class stores the weapon's images, attacks and other information
 */

//class starts here
class Sword extends Weapon{
  //variable declaration
  private static Image[] sprites;
  private static Attack atk1;
  private static Skill skill1;
  private static int moveSpeed = 15;
  private int skill1CD;
  
  /**
   * loadAssets
   * loads the images used by this weapon and sets up attacks
   */ 
  static void loadAssets(){
    
    //load the images used by this weapon
    sprites = new Image[20];
    try{
      for(int i = 1; i<=4; i++){
        sprites[i-1] = ImageIO.read(new File("sword000"+i+".png"));
      }
      for(int i = 1; i<=9; i++){
        if (i>=10){
          sprites[i+3] = ImageIO.read(new File("atksw100"+i+".png"));
        } else {
          sprites[i+3] = ImageIO.read(new File("atksw1000"+i+".png"));
        }
      }
      for(int i = 1; i<=7; i++){
        sprites[i+12] = ImageIO.read(new File("skillsw1000"+i+".png"));
      }
    } catch(IOException e){
      System.out.println("Image loading failed");
      e.printStackTrace();
    }
    
    //set up the basic attack of the sword
    int[] atk1Anim = {4, 5, 5, 6, 7, 8, 9, 10, 11, 12};
    Rectangle[][] atk1hitBox = new Rectangle [10][1];
    atk1hitBox[1][0] = new Rectangle(0, -46, 59, 65);
    atk1hitBox[2][0] = new Rectangle(0, -46, 59, 65);
    atk1 = new Attack(1, atk1Anim, atk1hitBox, 20, 3, 5, 2, -11, -50, 149, 269, null, null);
    
    //set up the dashing skill of the sword
    int[] skill1Anim = {13, 14, 15, 16, 17, 18, 18, 18, 18, 19, 19};
    Rectangle[][] skill1Hitbox = new Rectangle [11][1];
    skill1Hitbox[2][0] = new Rectangle(13, -44, 25, 220);
    skill1Hitbox[3][0] = skill1Hitbox[1][0];
    double skill1Move[] = new double[11];
    skill1Move[1] = 140;
    skill1Move[4] = 5;
    skill1Move[9] = 5;;
    skill1 = new SwSkill1(skill1Anim, skill1Hitbox, skill1Move);
  }
  
  /**
   * getIdleX
   * getter for idleX
   * @return int, the x coordiante of this weapon when idle relative to the player (used for displaying)
   */ 
  @Override
  public int getIdleX(){
    return 0;
  }
  
  /**
   * getIdleY
   * getter for idleY
   * @return int, the y coordiante of this weapon when idle relative to the player (used for displaying)
   */
  @Override
  public int getIdleY(){
    return 0;
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