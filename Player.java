//imports
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.Shape;

/*class Player
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * This is the class that represents the player of the class, storing all variables and methods required by the player
 */

//class starts here
class Player{
  
  //variable declaration
  //variable to do with player status
  private double x;
  private double y;
  private int health;
  private double moveSpeed = 15.0;
  private double xVelocity;
  private double yVelocity;
  private boolean movingUp;
  private boolean movingDown;
  private boolean movingLeft;
  private boolean movingRight;
  private int yDirection;
  private int xDirection;
  private int team;
  private boolean flag;
  private boolean moving;
  private int moveStun;
  private int rootDuration;
  private int animationCounter;
  private int invulDuration;
  private int respawnCounter;
  private Weapon weapon;
  
  //directionMatrix used to access images based on the direction the player is facing (could be any of the 8 below)
  private int[][] directionMatrix = {{3, 0, 3},
                                      {2, 0, 2},
                                      {1, 0, 1}};
  
  //attack information
  private Attack currentAction;
  private double attackDirection;
  private int actionCounter;
  private int actionStun;
  //hitboxes and hurtboxes
  private Shape[] hitbox;
  private Rectangle hurtbox;
  //animations
  private int[] runAnim = {4, 4, 5, 6, 7, 8, 8, 9, 10, 11};
  
  /**
   * Player
   * Constructor for the Player class
   * @param x the initial horizontal coordinate, y the initial vertical coordinate, team the team this player belongs to
   */
  Player(double x, double y, int team){
    this.x = x;
    this.y = y;
    this.team = team;
    this.flag = false;
    this.xDirection = 1;
    this.yDirection = 0;
    this.health = 100;
    this.weapon = new Sword();
  }
  
  /**
   * setWeapon
   * this method i
   * @param w, the new Weapon this player is to change to
   */
  public void setWeapon(Weapon w){
    this.weapon = w;
  }
  
   /**
    * setXDirection
    * setter for the xDirection of the player
    * the xDirection represents the horizontal direction of the player (left, middle or right)
    * @param i, the new xDirection of the player
    */ 
  public void setXDirection(int i){
    xDirection = i;
  }
   /**
    * setYDirection
    * setter for the yDirection of the player
    * the yDirection represents the vertical direction of the player (up, middle or down)
    * @param i, the new yDirection of the player
    */ 
  public void setYDirection(int i){
    yDirection = i;
  }
  
   /**
    * setMoveStun
    * setter for the moveStun of the player
    * the moveStun represents the amount of frames before this player could move again
    * @param i, the new moveStun of the player
    */ 
  public void setMoveStun(int i){
    moveStun = i;
  }
  
   /**
    * setRoot
    * setter for the rootDuration of the player
    * the rootDuration represents the amount of frames this player is rooted for (cannot move or be moved by movement attacks)
    * @param i, the new rootDuration of the player
    */ 
  public void setRoot(int i){
    rootDuration = i;
  }
  
   /**
    * setFlag
    * setter for the flag of the player
    * the flag represents if this player is currently in posession of the flag
    * @param b, the new flag of the player
    */ 
  public void setFlag(boolean b){
    flag = b;
  }
  
   /**
    * addHealth
    * adds health to this player
    * the health cannot be added over 100
    * @param i, the amount of health to be added
    */ 
  public void addHealth(int i){
    health+=i;
    if(health>100){
      health = 100;
    }
  }
  
  /**
   * getX
   * getter for x
   * @return int, the horizontal location of this object
   */ 
  public int getX(){
    return (int)Math.round(x);
  }
  /**
   * getY
   * getter for y
   * @return int, the vertical location of this object
   */ 
  public int getY(){
    return (int)Math.round(y);
  }
  
  /**
   * getHealth
   * getter for health
   * @return int, the current health of this object
   */ 
  public int getHealth(){
    return health;
  }
  
  /**
   * getFlag
   * getter for flag
   * @return boolean, true if this player currently has the flag
   */ 
  public boolean hasFlag(){
    return flag;
  }
  
  /**
   * getTeam
   * getter for team
   * @return int, the team this object belongs to
   */ 
  public int getTeam(){
    return team;
  }
  
  /**
   * getDirection
   * getter for yDirection and xDirection
   * @return String, the yDirection followed by a space and then xDirection
   */ 
  public String getDirection(){
    return yDirection+" "+xDirection;
  }
  
  /**
   * getAction
   * getter for currentAction
   * @return int, identifier of the current action (-1 if not in action)
   */ 
  public int getAction(){
    if(currentAction==null){
      return -1;
    } 
    return currentAction.getAttackIdentifier();
  }
  
  /**
   * getAttackDirection
   * getter for attackDirection
   * @return double, the direction of this object's attack
   */ 
  public double getAttackDirection(){
    return attackDirection;
  }
  
  /**
   * getCurrentAction
   * getter for currentAction
   * @return Attack, the current action of this object
   */ 
  public Attack getCurrentAction(){
    return currentAction;
  }
  
  /**
   * isInvul
   * getter for invulDuration
   * @return boolean, true if this player is invulnerable
   */ 
  public boolean isInvul(){
    if(invulDuration>0){
      return true;
    }
    return false;
  }
  
  /**
   * getRespawnCounter
   * getter for respawnCounter
   * @return int, the amount of frames until this player respawns
   */ 
  public int getRespawnCounter(){
    return respawnCounter;
  }
  
  /**
   * getWeapon
   * getter for Weapon
   * @return String, the name of the weapon this player uses
   */ 
  public String getWeapon(){
    if(weapon instanceof Bow){
      return "bo";
    }
    return "sw";
  }
  
  /**
   * getWeaponObj
   * other getter for Weapon
   * @return Weapon, the Weapon object of this player
   */ 
  public Weapon getWeaponObj(){
    return weapon;
  }
  
  /**
   * getHurtBox
   * getter for hurtbox
   * @return Rectangle, the current hurtbox(s) of this player
   */ 
  public Rectangle getHurtbox(){
    return new Rectangle((int)x, (int)y, 20, 50);
  }
  
  /**
   * getHitBox
   * getter for hitbox
   * @return Shape array, the current hitbox(s) of this player
   */
  public Shape[] getHitBox(){
    return hitbox;
  }
  
  /**
   * processAnimationCounter
   * this method calculates the current animationCounter, a variable used as an index marker for the current image
   * this method also processes the various counter and status variables of this player
   */ 
  public void processAnimationCounter(){
    
    //if the player is dead, decrease the respawn counter
    if(respawnCounter>0){
      respawnCounter--;
      if(respawnCounter == 0){//if the player is to be respawned, respawn the player with full health at a location based on their team
        health = 100;
        if(team == 1){
          x = 600;
          y = 250;
        } else {
          x = 600;
          y = 1350;
        }
      }
    }
    
    //calculate the animation counter
    if(moving){ //if moving, increase animation counter until end of animation is reached, and then reset back to 0
      if(animationCounter<runAnim.length-1){
        animationCounter++;
      } else {
        animationCounter = 0;
      }
    } else { //if not moving animationcounter is 0
      animationCounter = 0;
    }
    
    //calculate the action counter (like the animation counter except for attacks and skills)
    if(currentAction != null){
      if(actionCounter == currentAction.getDuration()){ //if the action counter reached the end of the attack or skill the action ends
        currentAction = null;
        actionStun = 0;
        attackDirection = 0;
      } else {
        actionCounter++;
        actionStun--;
      }
    }
    
    //other status variables
    if(moveStun>0){ //decrease moveStun
      moveStun--;
    }
    if(rootDuration>0){ //decrease rootDuration
      rootDuration--;
    }
    if(invulDuration>0){ //decrease invulDuration
      invulDuration--;
    }
    
    //decrease the cooldown of the weapon skills
    weapon.processCooldown();
  }
  
  /**
   * processInput
   * this method takes the input the user has given this object and performs actions accordingly
   * @param input, the boolean array representing keyboard input the user has given, mouseInput the int array representing the mouse input the user has given
   */
  public void processInput(boolean[] input, int[] mouseInput){
    //variable to track if any movement is registered
    boolean movementInput = false;
    
    //if any of the direction keys are entered update the moving direction variables
    if(input[0]){
      movingUp = true;
      movementInput = true;
    }
    if(input[1]){
      movingDown = true;
      movementInput = true;
    }
    if(input[2]){
      movingRight = true;
      movementInput = true;
    }
    if(input[3]){
      movingLeft = true;
      movementInput = true;
    }
    if(input[4]){
      movingUp = false;
      if(((movingLeft) && (!movingRight))||((movingRight) && (!movingLeft))){ //if a key is released and the player is still moving reset direction of that axis to neutral
        yDirection = 1;
      }
    }
    if(input[5]){
      movingDown = false;
      if(((movingLeft) && (!movingRight))||((movingRight) && (!movingLeft))){
        yDirection = 1;
      }
    }
    if(input[6]){
      movingRight = false;
      if(((movingUp) && (!movingDown))||((movingDown) && (!movingUp))){
        xDirection = 1;
      }
    }
    if(input[7]){
      movingLeft = false;
      if(((movingUp) && (!movingDown))||((movingDown) && (!movingUp))){
        xDirection = 1;
      }
    }
    
    //variable to track if player is moving or not
    moving = false;
    if(movementInput){ //if a new movement input is inputted reset the x and y direction to neutral
      yDirection = 1;
      xDirection = 1;
    }
    
    //determine the x and y direction
    //if moving up yDirection is 0
    if((movingUp) && (!movingDown)){
      yDirection = 0;
      moving = true;
    }
    //if moving down yDirection is 2
    if((movingDown) && (!movingUp)){
      yDirection = 2;
      moving = true;
    }
    //same logic applies to left and right
    if((movingLeft) && (!movingRight)){
      xDirection = 0;
      moving = true;
    }
    if((movingRight) && (!movingLeft)){
      xDirection = 2;
      moving = true;
    }
    
    //if player is not moving or has stopped moving reset animationCounter to 0
    if(!moving){
      animationCounter = 0;
    }
    
    //if player can issue an action
    if(actionStun<=0){
      //check if skill button is inputted
      //moving attacks cannot be activated when the player has the flag
      if((input[8]) && (weapon.getSkill1Cooldown() <=0) && !((flag) && (weapon.getAttack(2) instanceof MovingAttack))){
        attackDirection = getRadians(mouseInput[0], mouseInput[1], mouseInput[2]/2, (mouseInput[3]/2)+30); //calculate the attack direction
        currentAction = weapon.getAttack(2); //set up the attack
        actionCounter = -1;
        actionStun = weapon.getAttack(2).getSelfStun()+1;
        ((Skill)currentAction).activate(this); //call the activiate method of the skill
        
      } else if((input[9]) && (mouseInput[0]!=-1) && (mouseInput[1]!=-1)){ //check if basic attack is inputted
        attackDirection = getRadians(mouseInput[0], mouseInput[1], mouseInput[2]/2, (mouseInput[3]/2)+30); //calculate the attack direction
        currentAction = weapon.getAttack(1); //set up the attack
        actionCounter = -1;
        actionStun = weapon.getAttack(1).getSelfStun()+1;
      }
    }
  }
  
  /**
   * getRadians
   * This method will calculate an angle in radians given a point and the center of the screen
   * the angle will be formed by a vertical line in the center of the screen
   * @param x the x coordinate of the mouse, y the y coordinate of the mouse, fx the x coordinate of the center of the screen, fy the y coordinate of the center of the screen
   * @return double, the angle in radians
   */
  private double getRadians(double x, double y, double fx, double fy){
    double angle1 = Math.atan2(-fy, 0);
    double angle2 = Math.atan2(y - fy, x - fx);
    return angle2-angle1;
  }
  
  /**
   * processMovement
   * this method moves the player
   */ 
  public void processMovement(){
    //if the player has the flag their movespeed is slowed by 5
    if(flag){
      moveSpeed = weapon.getMoveSpeed()-5; //access the weapon's move speed
    } else{
      moveSpeed = weapon.getMoveSpeed();
    }
    
    //if rooted or move stunned the player cannot move
    if(rootDuration<=0){
      if(moveStun <= 0){
        if(movingUp){
          y-=moveSpeed;
        }
        if(movingDown){
          y+=moveSpeed;
        }
        if(movingRight){
          x+=moveSpeed;
        }
        if(movingLeft){
          x-=moveSpeed;
        }
      } else {
        moving = false;
      }
      
      //attack movement is not affected by moveStun but is affected by root
      if((currentAction!=null) && (currentAction instanceof MovingAttack)){
        x+= Math.sin(attackDirection)*((MovingAttack)currentAction).getXMove(actionCounter);
        y+= -Math.cos(attackDirection)*((MovingAttack)currentAction).getXMove(actionCounter);
      }
    } else {
      moving = false;
    }
    
    //check that the player is not out of bounds
    if(x<0){
      x = 0;
    }
    if(y<0){
      y = 0;
    }
    if(x>1200){
      x = 1200;
    }
    if(y>1450){
      y = 1450;
    }
  }
  
  /**
   * processHitBox
   * this method adds the appropriate hitboxes and hurtboxes to the player's hitbox array
   * this method also spawns projectiles if the attack calls for it
   * @param projArr, the projectile array from GameServer passed into here
   */ 
  public void processHitBox(SimpleLinkedList<Projectile> projArr){
    //clear the hitbox
    hitbox = null;
    //check that the current action has a hitbox
    if(currentAction != null){
      if(currentAction.getHitBox()[actionCounter]!= null){
        hitbox = new Shape[currentAction.getHitBox()[actionCounter].length];
        AffineTransform t = new AffineTransform(); //create and rotate an AffineTransform to be applied to all hitboxes
        t.rotate(attackDirection, x+29, y+17);
        for(int i = 0; i<currentAction.getHitBox()[actionCounter].length; i++){ //loop through hitboxes to rotate all of them and add them to the player's hitbox
          if(currentAction.getHitBox()[actionCounter][i]!=null){
            Rectangle tempHitBox = new Rectangle((int)(x+currentAction.getHitBox()[actionCounter][i].getX()), (int)(y+currentAction.getHitBox()[actionCounter][i].getY()), (int)(currentAction.getHitBox()[actionCounter][i].getWidth()), (int)(currentAction.getHitBox()[actionCounter][i].getHeight()));
            hitbox[i] = t.createTransformedShape(tempHitBox);
          }
        }
      }
      
      //check that the current action spawns a projectile
      if((currentAction instanceof ProjectileSpawning) && (currentAction.getProjectile(actionCounter)!=null)){
        for(int i = 0; i<currentAction.getProjectile(actionCounter).length; i++){
          if(currentAction.getProjectile(actionCounter)[i]!= null){
            //use the projectile's copy constructor in order to spawn a new instance of the projectile to the game by adding it to projArr
            if(currentAction.getProjectile(actionCounter)[i] instanceof BowProj2){ //if the projectile is a special projectile use the special projectile's copy constructor
              projArr.add(new BowProj2((BowProj2)currentAction.getProjectile(actionCounter)[i], x+currentAction.getProjectileX(actionCounter, i), y+currentAction.getProjectileY(actionCounter, i), attackDirection, team));
            } else {
              projArr.add(new Projectile(currentAction.getProjectile(actionCounter)[i], x+currentAction.getProjectileX(actionCounter, i), y+currentAction.getProjectileY(actionCounter, i), attackDirection, team));
            }
          }
        }
      }
    }
  }
  
  /**
   * hurt
   * this method registers an attack that has hit the player
   * @param a the Attack object that has hit this player
   */
  public void hurt(Attack a){
    //deduct health, apply moveStun and hitInvul
    health-=a.getDamage();
    moveStun+=a.getMoveStun();
    invulDuration+=a.getHitInvul();
    if(health<=0){ //if the player died, set the respawn counter and reset certain variables
      respawnCounter = 150;
      hitbox = null;
      movingUp = false;
      movingDown = false;
      movingLeft = false;
      movingRight = false;
    }
  }
  
  /**
   * hurt
   * this method registers a projectile that has hit the player
   * this method overloads the previous hurt method
   * @param a the Projectile object that has hit this player
   */
  public void hurt(Projectile a){
    //deduct health, apply moveStun and hitInvul
    health-=a.getDamage();
    moveStun+=a.getMoveStun();
    invulDuration+=a.getHitInvul();
    if(a instanceof BowProj2){ //if the projectile is a special projectile apply it's on hit effect
      ((BowProj2)a).onHit(this);
    }
    if(health<=0){ //if the player died, set the respawn counter and reset certain variables
      respawnCounter = 150;
      hitbox = null;
      movingUp = false;
      movingDown = false;
      movingLeft = false;
      movingRight = false;
    }
  }
  
  /**
   * getImage
   * getter for current image of the player
   * @return String, the index of the current image of the player (to be sent to client and displayed)
   */
  public String getImage(){
    //if the current action calls for special animations
    if((currentAction != null) && (currentAction instanceof Skill) && (((Skill)currentAction).getPlayerAnim() != null)){
      if(((Skill)currentAction).getPlayerAnim()[actionCounter] == 72){ //72 is the index of the empty sprite, for when the player is not visible on screen
        return "72";
      }
      //access the special animation of the action
      return Integer.toString(((Skill)currentAction).getPlayerAnim()[actionCounter]+(8*(directionMatrix[yDirection][xDirection]))+36);
    }
    
    //if the player is moving, access the run animation
    //directionMatrix is used to access the correct image for where the player is facing
    //the player can face in 8 directions, and has 4 different images (the images are flipped for the other 4 directions)
    if(moving){
      if (currentAction == null){
        return Integer.toString(runAnim[animationCounter]+(8*(directionMatrix[yDirection][xDirection])));
      } else {
        return Integer.toString(runAnim[animationCounter]+(8*(directionMatrix[yDirection][xDirection]))+36);
      }
    } else { //if the player is not moving access the standing image
      if(currentAction == null){
        return Integer.toString((directionMatrix[yDirection][xDirection]));
      } else {
        return Integer.toString((directionMatrix[yDirection][xDirection])+36);
      }
    }
  }
  
  /**
   * getActionImage
   * getter for the image of the player's current action
   * @return String, the index of the current image of the player's action (to be sent to client and displayed)
   */
  public String getActionImage(){
    if(currentAction == null){ //if the player has no action return -1
      return "-1";
    } else {
      return Integer.toString(currentAction.getAttackAnim(actionCounter)); //access the attack animation of the current action
    }
  }
}