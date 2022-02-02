//imports
import java.awt.Image;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

/*class DisplayProjectile
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is used to store information related to a projectile to be used for displaying
 */

//class starts here
class DisplayProjectile{
  //variable declaration
  int x;
  int y;
  double direction;
  int image;
  int identifier;
  static Image[][] sprites;
  
  /**
   * loadAssets
   * loads the images used by projectiles
   */ 
  static void loadAssets(){
    sprites = new Image[2][2];
    try{
      //load the images
      sprites[0][0] = ImageIO.read(new File("projbo10002.png"));
      sprites[0][1] = ImageIO.read(new File("projbo10001.png"));
      sprites[1][0] = ImageIO.read(new File("projbo20002.png"));
      sprites[1][1] = ImageIO.read(new File("projbo20001.png"));
    } catch(IOException e){
      System.out.println("Image loading failed");
      e.printStackTrace();
    }
  }
  
  /**
   * DisplayProjectile
   * Constructor for the DisplayProjectile class
   * @param identifier the identifier of this projectile, x the location that this projectile will be spawned, y the location this projectile will be spawned
   * @param image the index of this DisplayProjectile's sprite, direction the direction that this projectile faces 
   */
  DisplayProjectile(int identifier, int x, int y, int image, double direction){
    //set up variables
    this.identifier = identifier;
    this.x = x;
    this.y = y;
    this.image = image;
    this.direction = direction;
  }
  
  /**
   * getSprites
   * getter for sprites
   * @param i the identifier of the projectile who's sprites will be needed
   * @return Image array, the sprites of this projectile (used for displaying)
   */
  public static Image[] getSprites(int i){
    return sprites[i];
  }
  
  /**
   * getX
   * getter for x
   * @return int, the horizontal location of this object
   */
  public int getX(){
    return x;
  }
  
  /**
   * getY
   * getter for y
   * @return int, the vertical location of this object
   */ 
  public int getY(){
    return y;
  }
  
  /**
   * getImage
   * getter for image
   * @return int, the index of this DisplayProjectile's sprite
   */ 
  public int getImage(){
    return image;
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
   * getIdentifier
   * getter for identifier
   * @return int, the identifier of this projectile (used as a "name" for this projectile)
   */ 
  public int getIdentifier(){
    return identifier;
  }
}