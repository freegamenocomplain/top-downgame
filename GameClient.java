//imports
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.imageio.ImageIO;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Image;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.MouseInfo;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.File;
import java.util.Scanner;

//Keyboard imports
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
//mouse imports
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/*class GameClient
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this is the client for the game, where information from the server is recieved and displayed
 */

//class starts here
class GameClient extends JFrame{
  
  //variable declaration
  //networking variables
  private Socket clientSocket;
  private BufferedReader input; //reader for network stream
  private PrintWriter output;  //printwriter for network output
  
  //display variables
  private JFrame thisFrame;
  private DisplayPanel gamePanel;
  
  //displayPlayer and displayProjectile variables to store information to be displayed
  private DisplayPlayer thisPlayer;
  private SimpleLinkedList<DisplayPlayer> otherPlayers;
  private SimpleLinkedList<DisplayProjectile> projectiles;
  
  //variables to store the screen size
  private int screenWidth;
  private int screenHeight;
  
  //image variables
  private Image[] sprites;
  private Image background;
  private Image flagPresent;
  private Image flagAbsent;
  private Image flag;
  private Image healthPresent;
  private Image healthAbsent;
  
  //directionMatrix used to access images based on the direction the player is facing (could be any of the 8 below)
  private int[][] directionMatrix = {{3, 0, 3},
                                      {2, 0, 2},
                                      {1, 0, 1}};
  
  private int team1Score;
  private int team2Score;
  private boolean flag1;
  private boolean flag2;
  private boolean health1;
  private boolean health2;
  private boolean health3;
  private boolean health4;
  
  //jbutton and jtextfield variables
  private JButton swButton;
  private JButton boButton;
  private JTextField ipInput;
  
  //variables to store the status of the program;
  private boolean running = true;
  private boolean inGame = false;
  private double nextTick;
  
  //volatile variables to store ip address and weapon choice (also the result of the game)
  //these are variables that are to be updated in other threads
  private volatile String ip;
  private volatile String weapon;
  private volatile String gameResult;
 
  /**
   * GameClient
   * Constructor for the GameClient class
   */
  GameClient(){
    //call super
    super("Not a Fighting Game");
    
    //set thisFrame variable to point to this frame
    this.thisFrame = this;
    
    //load assets for sword, bow and projectiles
    Sword.loadAssets();
    Bow.loadAssets();
    DisplayProjectile.loadAssets();
    sprites = new Image[73];
    try{ //load images
      background = ImageIO.read(new File("background.png")); 
      flagPresent = ImageIO.read(new File("flagLocation.png"));
      flagAbsent = ImageIO.read(new File("healthEmpty.png"));
      healthPresent = ImageIO.read(new File("healthLocation.png"));
      healthAbsent = ImageIO.read(new File("flagEmpty.png"));
      flag = ImageIO.read(new File("flag.png"));
      for(int i = 1; i<=73; i++){
        if (i>=10){
          sprites[i-1] = ImageIO.read(new File("stick00"+i+".png"));
        } else {
          sprites[i-1] = ImageIO.read(new File("stick000"+i+".png"));
        }
      }
    } catch(IOException e){
      System.out.println("Image loading failed");
      e.printStackTrace();
    }
    
    //initiate otherPlayers linkedlist
    otherPlayers = new SimpleLinkedList<DisplayPlayer>();
    
    //set screen width and height
    screenWidth = 1920;
    screenHeight = 1080;
    Dimension screenSize = new Dimension(1920, 1080);
    
    // Set the frame to full screen 
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    this.setSize(screenSize);
    thisFrame.setResizable(false);
    
    //initiate gamePanel to store buttons and display the game
    gamePanel = new DisplayPanel();
    gamePanel.setLayout(null);
    
    //button for selecting sword
    swButton = new JButton("Sword");
    swButton.addActionListener(new SwordButton());
    swButton.setBounds(screenWidth/2-110, screenHeight/2, 100, 30);
    
    //button for selecting bow
    boButton = new JButton("Bow");
    boButton.addActionListener(new BowButton());
    boButton.setBounds(screenWidth/2+10, screenHeight/2, 100, 30);
    
    //button for inputting IP
    ipInput = new JTextField();
    ipInput.setBounds(screenWidth/2-100, screenHeight/2, 200, 30);
    ipInput.addActionListener(new EnterListener());
    
    //add buttons and textfield to gamePanel
    gamePanel.add(swButton);
    gamePanel.add(boButton);
    gamePanel.add(ipInput);
    
    //add gamePanel to this frame
    this.add(gamePanel);
    
    //create key and mouse listeners
    MyKeyListener keyListener = new MyKeyListener();
    this.addKeyListener(keyListener);
    
    MyMouseListener mouseListener = new MyMouseListener();
    this.addMouseListener(mouseListener);
    
    this.requestFocusInWindow(); //make sure the frame has focus   
    this.setFocusable(true);
    this.requestFocus();
      
    //set the time for the next frame to be 33 milliseconds after this (so it maintains 30 fps
    nextTick = System.currentTimeMillis() + 33;
    
    //set the buttons to be invisible
    swButton.setEnabled(false);
    boButton.setEnabled(false);
    swButton.setVisible(false);
    boButton.setVisible(false);
    
    //set this frame to be visible
    thisFrame.setVisible(true);
    
    //Start the client in a separate thread
    Thread tempThread = new Thread(new Runnable() { public void run() { startClient(); }}); //start the Client with a tempoary runnable that runs startClient
    tempThread.start();
  }
  
  /**
   * startClient
   * this method takes user input IP and weapon, sets up connection with the server and runs throughout the course of the game
   */
  private void startClient(){
    thisFrame.repaint();
    while(ip == null){ //wait for IP input from user
    }
    //close IP input textfield and open weapon selection buttons 
    ipInput.setEditable(false);
    ipInput.setVisible(false); 
    swButton.setEnabled(true);
    boButton.setEnabled(true);
    swButton.setVisible(true);
    boButton.setVisible(true);
    thisFrame.repaint();
    while(weapon == null){ //wait for weapon input from user
    }
    //close weapon selection buttons
    swButton.setEnabled(false);
    boButton.setEnabled(false);
    swButton.setVisible(false);
    boButton.setVisible(false);
    
    //set up server connection with IP inputted from user
    try{
      System.out.println("Attempting Connection");
      clientSocket = new Socket(ip, 6000);
      InputStreamReader stream1= new InputStreamReader(clientSocket.getInputStream()); //Stream for network input
      input = new BufferedReader(stream1);
      output = new PrintWriter(clientSocket.getOutputStream()); //assign printwriter to network stream
      output.println(weapon); //inform server of the weapon this client is using
      output.flush();
    } catch (IOException e) {  //connection error occured
      System.out.println("Connection to Server Failed");
      e.printStackTrace();
      thisFrame.dispose(); //close this frame and return to menu
      new GameMenu();
    }
    
    System.out.println("Connection made.");
    
    //create thread for server input
    Thread serverIn = new Thread(new ServerInput());
    serverIn.start();
    
    //start the main game 
    inGame = true;
    while(inGame){ //program stays in this loop until the game is over
    }
    
    try {  //after leaving the main loop close all the sockets
      input.close();
      output.close();
      clientSocket.close();
    }catch (Exception e) { 
      System.out.println("Failed to close socket");
    }
  }
  
  //***** Inner class - used to display the game
  private class DisplayPanel extends JPanel{
    //variable to store the amount the screen zooms in on the player
    double zoomScale = 1.5;
    //variable to store font used
    Font font = new Font("Arial", Font.BOLD, (int)(30/zoomScale));
    
    /**
     * paintComponent
     * this overwirtes the paintComponent in JPanel to display the content of this game
     * @param Graphics g
     */ 
    @Override
    public void paintComponent(Graphics g){
      super.paintComponent(g); //call to super
      setDoubleBuffered(true);
      
      //set the font
      g.setFont(font);
      
      //if in the IP or weapon choosing stages display the corrisponding instructions
      if(ip == null){
        g.drawString("ENTER HOST IP (press enter to confirm)", screenWidth/2-100, screenHeight/2-50);
      } else if(weapon == null){
        g.drawString("CHOSE YOUR WEAPON", screenWidth/2-120, screenHeight/2-10);
      }
      
      //when in game or displaying game results
      if(((inGame) || (gameResult != null)) && (thisPlayer!= null)){
        
        //calculate screen x and y based on the player location so the screen is always centered on the player
        int screenX = (int)(-(thisPlayer.getX()-(screenWidth/(2*zoomScale)))-(25));
        int screenY = (int)(((screenHeight/(2*zoomScale))-thisPlayer.getY())-(18.75));
        //scale and translate the screen based on the player location
        ((Graphics2D)g).scale(zoomScale, zoomScale);
        ((Graphics2D)g).translate(screenX, screenY);
        
        //draw background
        g.drawImage(background, 0, 0, 1250, 1500, this);
        
        //draw the flag locations (there are 2 icons for when the flag is there and when it is not)
        if(flag1){
          g.drawImage(flagPresent, 575, 100, 100, 100, this);
        } else {
          g.drawImage(flagAbsent, 575, 100, 100, 100, this);
        }
        if(flag2){
          g.drawImage(flagPresent, 575, 1300, 100, 100, this);
        } else {
          g.drawImage(flagAbsent, 575, 1300, 100, 100, this);
        }
        
        //draw the four health locations
        if(!health1){
          g.drawImage(healthPresent, 50, 350, 50, 50, this);
        } else {
          g.drawImage(healthAbsent, 50, 350, 50, 50, this);
        }
        if(!health2){
          g.drawImage(healthPresent, 1150, 350, 50, 50, this);
        } else {
          g.drawImage(healthAbsent, 1150, 350, 50, 50, this);
        }
        if(!health3){
          g.drawImage(healthPresent, 50, 1100, 50, 50, this);
        } else {
          g.drawImage(healthAbsent, 50, 1100, 50, 50, this);
        }
        if(!health4){
          g.drawImage(healthPresent, 1150, 1100, 50, 50, this);
        } else {
          g.drawImage(healthAbsent, 1150, 1100, 50, 50, this);
        }
        
        //if the player is alive
        if(thisPlayer.getRespawnCounter()<=0){
          displayPlayer(thisPlayer, g); //display the player
        } else { //otherwise display the amount of time left until player respawns
          g.drawString("RESPAWNING IN", (int)(-screenX+(screenWidth/(zoomScale*2))), (int)(-screenY+(screenHeight/(zoomScale*2))-30));
          g.drawString(Integer.toString((int)Math.ceil(thisPlayer.getRespawnCounter()/30.0)), (int)(-screenX+(screenWidth/(zoomScale*2))+30), (int)(-screenY+(screenHeight/(zoomScale*2))));
        }
        
        //display other players
        for(int i = 0; i<otherPlayers.size(); i++){
          if(otherPlayers.get(i).getRespawnCounter()<=0){
            displayPlayer(otherPlayers.get(i), g);
          }
        }
        
        //display projectiles
        for(int i = 0; i<projectiles.size(); i++){
          displayProjectile(projectiles.get(i), g);
        }
        
        //if this player is alive
        if(thisPlayer.getRespawnCounter()<=0){ 
          displayHealth(thisPlayer, g); //display this player's health bar
        }
        //display other player's health bars
        for(int i = 0; i<otherPlayers.size(); i++){
          if(otherPlayers.get(i).getRespawnCounter()<=0){
            displayHealth(otherPlayers.get(i), g);
          }
        }
        
        //display skill cooldown
        g.drawString("Skill Cooldown: "+Integer.toString((int)Math.ceil(thisPlayer.getSkill1CD()/30.0)), (int)(-screenX+20), (int)-screenY+80);
        
        //display team scores
        g.drawString(Integer.toString(team1Score), (int)(-screenX+20), (int)-screenY+20);
        g.drawString(Integer.toString(team2Score), (int)(-screenX+(screenWidth/zoomScale)-40), (int)-screenY+20);
        
        //if the game is over display game results
        if(gameResult!= null){
          g.drawString(gameResult, (int)(-screenX+(screenWidth/(zoomScale*2))-60), (int)(-screenY+(screenHeight/(zoomScale*2))-30));
        }
      }
    }
    
    /**
     * displayProjectile
     * this method takes a DisplayProjectile and draws it to the screen
     * @param DisplayProjectile p the projectile to be displayed, Graphics g
     */ 
    private void displayProjectile(DisplayProjectile p, Graphics g){
      //get the DisplayProjectile's sprite
      BufferedImage tempImg = (BufferedImage)DisplayProjectile.getSprites(p.getIdentifier())[p.getImage()];
      //create an AffineTransform to scale and rotate the projectile
      AffineTransform t = new AffineTransform();
      //scale, translate and rotate the projectile sprite
      t.scale(0.25, 0.25);
      t.translate(p.getX()*4, p.getY()*4);
      t.rotate(p.getDirection(), tempImg.getWidth()/2, tempImg.getHeight()/2);
      ((Graphics2D)g).drawImage(tempImg, t, null); //draw the projectile sprite
    }
    
    /**
     * displayPlayer
     * this method takes a DisplayPlayer and draws it to the screen
     * @param DisplayPlayer p the player to be displayed, Graphics g
     */ 
    private void displayPlayer(DisplayPlayer p, Graphics g){
      //variable to store the weapon size (1/4 of it's original picture size)
      int wpnScaleX = p.getWeapon().getSprites()[directionMatrix[p.getYDirection()][p.getXDirection()]].getWidth(null)/4;
      int wpnScaleY = p.getWeapon().getSprites()[directionMatrix[p.getYDirection()][p.getXDirection()]].getHeight(null)/4;
      
      //draw the player based on it's direction
      //if the player is facing left (or straight up) the sprite is flipped
      //if the player is facing up the weapon is drawn in front of the player (weapon is not drawn on idle position if player is attacking)
      //the weapon sprite is accessed with a direction matrix
      if((p.getXDirection() == 0) || ((p.getXDirection() == 1) && (p.getYDirection() == 0))){
        if(p.getYDirection() == 0){
          g.drawImage(sprites[p.getImage()], p.getX()+50, p.getY(), -50, 50, this);
          if(p.getAction()==-1){
            g.drawImage(p.getWeapon().getSprites()[directionMatrix[p.getYDirection()][p.getXDirection()]], p.getX()+50-p.getWeapon().getIdleX(), p.getY()+p.getWeapon().getIdleY(), -wpnScaleX, wpnScaleY, this);
          }
        } else {
          if(p.getAction()==-1){
            g.drawImage(p.getWeapon().getSprites()[directionMatrix[p.getYDirection()][p.getXDirection()]], p.getX()+50-p.getWeapon().getIdleX(), p.getY()+p.getWeapon().getIdleY(), -wpnScaleX, wpnScaleY, this);
          }
          g.drawImage(sprites[p.getImage()], p.getX()+50, p.getY(), -50, 50, this);
        }
      } else {
        if(p.getYDirection() == 0){
          g.drawImage(sprites[p.getImage()], p.getX(), p.getY(), 50, 50, this);
          if(p.getAction()==-1){
            g.drawImage(p.getWeapon().getSprites()[directionMatrix[p.getYDirection()][p.getXDirection()]], p.getX()+p.getWeapon().getIdleX(), p.getY()+p.getWeapon().getIdleY(), wpnScaleX, wpnScaleY, this);
          }
        } else {
          if(p.getAction()==-1){
            g.drawImage(p.getWeapon().getSprites()[directionMatrix[p.getYDirection()][p.getXDirection()]], p.getX()+p.getWeapon().getIdleX(), p.getY()+p.getWeapon().getIdleY(), wpnScaleX, wpnScaleY, this);
          }
          g.drawImage(sprites[p.getImage()], p.getX(), p.getY(), 50, 50, this);
        }
      }
      
      //if player has the flag draw the flag on the player
      if(p.getFlag()){
        g.drawImage(flag, p.getX()+20, p.getY()-40, 50, 50, this);
      }
      
      //if the player is currently attacking (or performing other actions)
      if(p.getAction()!=-1){
        //get the attack sprite
        BufferedImage tempImg = (BufferedImage)p.getWeapon().getSprites()[p.getActionImage()];
        AffineTransform t = new AffineTransform();
        //scale, transform and rotate it based on the player location and the attack's direction
        t.scale(0.25, 0.25);
        t.translate((p.getX()+p.getWeapon().getAttack(p.getAction()).getImageX())*4, (p.getY()+p.getWeapon().getAttack(p.getAction()).getImageY())*4);
        t.rotate(p.getAttackDirection(), p.getWeapon().getAttack(p.getAction()).getRotateX(), p.getWeapon().getAttack(p.getAction()).getRotateY());
        ((Graphics2D)g).drawImage(tempImg, t, null); //draw the attack sprite
      }
    }
    
    /**
     * displayHealth
     * this method takes a DisplayPlayer and draws it's health bar to the screen
     * @param DisplayPlayer p, Graphics g
     */ 
    private void displayHealth(DisplayPlayer p, Graphics g){
      if(p.getTeam() == 1){ //set health bar color based on team
        g.setColor(new Color(225, 0, 0));
      } else {
        g.setColor(new Color(0, 0, 225));
      }
      g.fillRect(p.getX(), p.getY()-14, p.getHealth()/2, 10);
      g.setColor(new Color(0, 0, 0));
      g.drawRect(p.getX(), p.getY()-14, 50, 10);
    }
  } //end of display panel
  
  /** --------- INNER CLASSES ------------- **/
  //***** Inner class - used to take input from the server
  private class ServerInput implements Runnable{
    
    /**
     * run
     * executed at start of thread, takes server input
     */
    @Override
    public void run(){
      while(running) {  // loop until a message is received
        try {
          if (input.ready()) { //check for an incoming messge
            String msg;          
            msg = input.readLine(); //read the message
            
            //if the message signilfies the end of the game
            if((msg.substring(0, 4).equals("TEAM")) || (msg.substring(0, 4).equals("DRAW"))){ 
              //set the game to end and display the result
              gameResult = msg;
              inGame = false;
              running = false;
            } else { //if game is ongoing
              //split the input with spaces
              String[] split = msg.split(" ");
              //set the variables related to the state of the game
              team1Score = Integer.parseInt(split[0]);
              team2Score = Integer.parseInt(split[1]);
              flag1 = Boolean.parseBoolean(split[2]);
              flag2 = Boolean.parseBoolean(split[3]);
              health1 = Boolean.parseBoolean(split[4]);
              health2 = Boolean.parseBoolean(split[5]);
              health3 = Boolean.parseBoolean(split[6]);
              health4 = Boolean.parseBoolean(split[7]);
              
              //read the information related to the player this client controls
              thisPlayer = new DisplayPlayer(Integer.parseInt(split[8]), Integer.parseInt(split[9]), Integer.parseInt(split[10]), Integer.parseInt(split[11]), Integer.parseInt(split[12]), 
                                             Boolean.parseBoolean(split[13]), Integer.parseInt(split[14]), Integer.parseInt(split[15]), Double.parseDouble(split[16]), Integer.parseInt(split[17]), 
                                             Integer.parseInt(split[18]), Integer.parseInt(split[19]), readWeaponCode(split[20]), Integer.parseInt(split[21]));
              
              //create a tempoary list for other players
              SimpleLinkedList<DisplayPlayer> tempOtherPlayers = new SimpleLinkedList<DisplayPlayer>();
              int i = 22;
              boolean readPlayer = true;
              //this loop works somewhat like a for loop but a for loop is not used due to it's exit condition
              while(readPlayer){ //while other players are still to be read
                if((i>=split.length) || (split[i].equals("proj"))){ //check if the player information has ended (and projectile information begin)
                  readPlayer = false;
                } else {
                  //read the information for a player
                  tempOtherPlayers.add(new DisplayPlayer(Integer.parseInt(split[i]), Integer.parseInt(split[i+1]), Integer.parseInt(split[i+2]), Integer.parseInt(split[i+3]), Integer.parseInt(split[i+4]), 
                                                         Boolean.parseBoolean(split[i+5]), Integer.parseInt(split[i+6]), Integer.parseInt(split[i+7]), Double.parseDouble(split[i+8]), Integer.parseInt(split[i+9]), 
                                                         Integer.parseInt(split[i+10]), Integer.parseInt(split[i+11]), readWeaponCode(split[i+12]), 0));
                  i+=13;
                }
              }
              
              //create a tempoary list for projectiles
              SimpleLinkedList<DisplayProjectile> tempProj = new SimpleLinkedList<DisplayProjectile>();
              if(split[i].equals("proj")){ //read input for projectiles
                i++;
                for(int j = i; j<split.length; j+=5){
                  tempProj.add(new DisplayProjectile(Integer.parseInt(split[j]), Integer.parseInt(split[j+1]), Integer.parseInt(split[j+2]), Integer.parseInt(split[j+3]), Double.parseDouble(split[j+4])));
                }
              }
              
              //set the new projectiles and players
              otherPlayers = tempOtherPlayers;
              projectiles = tempProj;
            }
            
            //call for this screen to be updated (the screen update rate depends on the rate the server sends information
            thisFrame.repaint();
          }
        }catch (IOException e) { 
          System.out.println("Failed to receive msg from the server");
          e.printStackTrace();
        }
      }
    }
    
    /**
     * readWeaponCode
     * this method converts a string representing a weapon into a weapon object
     * @param s the String to be converted into a weapon
     */
    private Weapon readWeaponCode(String s){
      if(s.equals("bo")){
        return new Bow();
      }
      return new Sword();
    }
  } //end of server input

  // -----------  Inner class - this detects key presses outputs those presses to the server
  private class MyKeyListener implements KeyListener{
    
    /**
     * keyTyped
     * does nothing, just here to overwrite the interface
     * @param KeyEvent e
     */
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    /**
     * keyPressed
     * method that detects key presses and sends those key presses to the server
     * @param KeyEvent e 
     */ 
    @Override
    public void keyPressed(KeyEvent e) {
      if(inGame){
        //detects if any of the specific keys are pressed and sends that key to the server
        if (KeyEvent.getKeyText(e.getKeyCode()).equals("D")) {  
          output.println("D");
          output.flush();
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {
          output.println("A");
          output.flush();
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("W")) {
          output.println("W");
          output.flush();
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("S")) {
          output.println("S");
          output.flush();
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("E")) { //since E is a skill key the mouse information is sent along with keyboard information
          output.println("E "+(int)MouseInfo.getPointerInfo().getLocation().getX()+" "+(int)MouseInfo.getPointerInfo().getLocation().getY()+" "+screenWidth+" "+screenHeight);
          output.flush();
        }
      }
      if(gameResult!= null){ //if the game is over and escape is pressed exit the frame
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
          thisFrame.dispose();
          new GameMenu();
        }
      }
    }
    
    /**
     * keyReleased
     * method that detects key releases and sends that key to the server. This allows for smooth movement
     * @param KeyEvent e
     */ 
    @Override
    public void keyReleased(KeyEvent e) {
      if(inGame){
        if (KeyEvent.getKeyText(e.getKeyCode()).equals("D")) {  
          output.println("rD");
          output.flush();
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("A")) {
          output.println("rA");
          output.flush();
        }if (KeyEvent.getKeyText(e.getKeyCode()).equals("W")) {
          output.println("rW");
          output.flush();
          //System.out.println("W");
        } if (KeyEvent.getKeyText(e.getKeyCode()).equals("S")) {
          output.println("rS");
          output.flush();
        }
      }
    }
  }//end of key listener
  
  // -----------  Inner class - this detects mouse clicks outputs those to the server
  private class MyMouseListener implements MouseListener{
    
    /**
     * mousePressed
     * method that detects mouse presses and sends those mouse presses to the server
     * @param MouseEvent e 
     */ 
    @Override
    public void mousePressed(MouseEvent e) {
      if(inGame){
        //send the mouse location and the screen width and height to the server
        output.println("MC "+e.getX()+" "+e.getY()+" "+screenWidth+" "+screenHeight);
        output.flush();
      }
    }
    
    /**
     * mouseReleased
     * unused
     * @param MouseEvent e
     */ 
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    /**
     * mouseEntered
     * unused
     * @param MouseEvent e
     */ 
    public void mouseEntered(MouseEvent e) {
    }
    /**
     * mouseExited
     * unused
     * @param MouseEvent e
     */ 
    @Override
    public void mouseExited(MouseEvent e) {
    }
    /**
     * mouseClicked
     * unused
     * @param MouseEvent e
     */ 
    @Override
    public void mouseClicked(MouseEvent e) {
    }
  } //end of mouse listener
  
  // -----------  Inner class - this actionlistener is used to select the sword
  private class SwordButton implements ActionListener{
    /**
     * actionPerformed
     * this method sets the weapon to sword
     * @param ActionEvent event
     */ 
    @Override
    public void actionPerformed(ActionEvent event){
      weapon = "sw";
    }
  } //end of swordbutton
  
  // -----------  Inner class - this actionlistener is used to select the bow
  private class BowButton implements ActionListener{
    /**
     * actionPerformed
     * this method sets the weapon to bow
     * @param ActionEvent event
     */ 
    @Override
    public void actionPerformed(ActionEvent event){
      weapon = "bo";
    }
  } //end of bowbutton
  
  // -----------  Inner class - this actionlistener is used to enter the IP
  private class EnterListener implements ActionListener{
    /**
     * actionPerformed
     * this method saves the IP entered through the ipInput textfield0
     * @param ActionEvent event
     */ 
    @Override
    public void actionPerformed(ActionEvent event){
      ip = ipInput.getText();
    }
  } //end of enterlistener
  //end of inner classes
}
