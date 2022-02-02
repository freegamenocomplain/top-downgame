//imports
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

/*class GameServer
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this is the server for the game, where all calculations happen and the gameloop is located
 */

//class starts here
class GameServer { 
  
  //variable declaration
  
  //socket
  private ServerSocket socket;
  
  private boolean inGame;
  
  //linkedlists used to store various information about the players
  private SimpleLinkedList<ClientHandler> currentClients = new SimpleLinkedList<ClientHandler>();
  private SimpleLinkedList<Player> team1 = new SimpleLinkedList<Player>();
  private SimpleLinkedList<Player> team2 = new SimpleLinkedList<Player>();
  private SimpleLinkedList<Projectile> team1Projectiles = new SimpleLinkedList<Projectile>();
  private SimpleLinkedList<Projectile> team2Projectiles = new SimpleLinkedList<Projectile>();
  private SimpleLinkedList<boolean[]> input = new SimpleLinkedList<boolean[]>();
  private SimpleLinkedList<int[]> mouseInput = new SimpleLinkedList<int[]>();
  
  //double nextTick, used to store the time the next frame occurs
  private double nextTick;
  
  //flag and health location variables
  private FlagLocation flag1;
  private FlagLocation flag2;
  private SimpleLinkedList<HealthLocation> healthLocation = new SimpleLinkedList<HealthLocation>();
  
  //variables to store team score;
  private int team1Score;
  private int team2Score;
  
  /**
   * main
   * this is the main method used to start the server
   */
  public static void main(String[] args) { 
    new GameServer().startServer(); 
  }
  
  /**
   * startServer
   * this method will load required assets, start the server and accept clients
   */
  public void startServer(){
    
    //call load assets on the required classes 
    Sword.loadAssets();
    Bow.loadAssets();
    DisplayProjectile.loadAssets();
    
    //ask user to input the amount of players
    Scanner scanner = new Scanner(System.in);
    System.out.println("ENTER NUMBER OF PLAYERS");
    int players = scanner.nextInt();
     
    //accept client connections
    Socket client = null;
    try{
      socket = new ServerSocket(6000);
      //loop for the desired amount of players
      while(currentClients.size()<players){
        client = socket.accept();
        System.out.println("Client Connected");
        //set up input arrays for the new client
        input.add(new boolean [10]);
        mouseInput.add(new int [4]);
        Arrays.fill(mouseInput.get(mouseInput.size()-1), -1);
        
        //create new ClientHandler class for the client
        ClientHandler currClient;
        //calculate spawn location based on the team (which is calculated based on if the current amount of clients is odd or even)
        int spawnY = 200+((currentClients.size()+2)%2)*1050;
        int spawnX = 600;
        //declare ClientHandler
        currClient = new ClientHandler(client, new Player(spawnX, spawnY, ((currentClients.size()+2)%2)+1), currentClients.size());
        //add the player to the corrisponding team array
        if(currClient.getPlayer().getTeam() == 1){
          team1.add(currClient.getPlayer());
        } else {
          team2.add(currClient.getPlayer());
        }
        //add the ClientHandler to the linkedlist
        currentClients.add(currClient);
        //start a tread for the client
        Thread thread = new Thread(currClient);
        thread.start();
      }
    } catch(Exception e){
      System.out.println("Client Connection Failed");
      e.printStackTrace();
    }
    
    //set up flag locations and health locations
    flag1 = new FlagLocation(575, 100, 100, 100, 1);
    flag2 = new FlagLocation(575, 1300, 100, 100, 2);
    healthLocation.add(new HealthLocation(50, 350, 50, 50));
    healthLocation.add(new HealthLocation(1150, 350, 50, 50));
    healthLocation.add(new HealthLocation(50, 1100, 50, 50));
    healthLocation.add(new HealthLocation(1150, 1100, 50, 50));
    
    //set the team scores to 0
    team1Score = 0;
    team2Score = 0;
    
    //set the time for the next frame to be 33 milliseconds after this (so it maintains 30 fps
    nextTick = System.currentTimeMillis() + 33;
    
    //set inGame to true;
    inGame = true;
    
    //begin the game loop
    gameLoop();
  }
  
  /**
   * gameLoop
   * this method contains the main game loop where the game is ran
   */
  private void gameLoop(){
    
    //the gameloop
    while(inGame){
      
      //loop through all projectiles (team 1 and team 2) and call their process method as well as removing them when needed
      for(int i = 0; i<team1Projectiles.size(); i++){
        if(!team1Projectiles.get(i).process()){
          team1Projectiles.remove(i);
          i--;
        }
      }
      for(int i = 0; i<team2Projectiles.size(); i++){
        if(!team2Projectiles.get(i).process()){
          team2Projectiles.remove(i);
          i--;
        }
      }
      
      //call processCooldown on the health locations to decrease their cooldowns
      for(int i = 0; i<healthLocation.size(); i++){
        healthLocation.get(i).processCooldown();
      }
      
      //loop through all players and take appropriate calculations
      for(int i = 0; i<currentClients.size(); i++){
        //check if player is alive
        if(currentClients.get(i).getPlayer().getRespawnCounter()<=0){
          //process player input
          currentClients.get(i).getPlayer().processInput(input.get(i), mouseInput.get(i));
        }
        //reset input arrays
        Arrays.fill(input.get(i), false);
        Arrays.fill(mouseInput.get(i), -1);
        
        //call other processes like animationcounter and movement
        currentClients.get(i).getPlayer().processAnimationCounter();
        if(currentClients.get(i).getPlayer().getRespawnCounter()<=0){
          currentClients.get(i).getPlayer().processMovement();
          //call processHitBox to create the player's current hitbox
          if(currentClients.get(i).getPlayer().getTeam() == 1){
            currentClients.get(i).getPlayer().processHitBox(team1Projectiles);
          } else {
            currentClients.get(i).getPlayer().processHitBox(team2Projectiles);
          }
          
          //check if the player is standing on a flag location
          if(currentClients.get(i).getPlayer().getHurtbox().intersects(flag1.getHitbox())){
            if(currentClients.get(i).getPlayer().getTeam() != flag1.getTeam()){ //if player is not on the same team as the flag take the flag
              currentClients.get(i).getPlayer().setFlag(true);
              flag1.setFlag(false);
            } else if (currentClients.get(i).getPlayer().hasFlag()){ //if player is on the same team as flag and has the enemy flag the flag is reset and the player's team earns a score
              flag2.setFlag(true);
              currentClients.get(i).getPlayer().setFlag(false);
              team1Score++;
            }
          }
          
          //check the same but with the other flag location
          if(currentClients.get(i).getPlayer().getHurtbox().intersects(flag2.getHitbox())){
            if(currentClients.get(i).getPlayer().getTeam() != flag2.getTeam()){
              currentClients.get(i).getPlayer().setFlag(true);
              flag2.setFlag(false);
            } else if (currentClients.get(i).getPlayer().hasFlag()){
              flag1.setFlag(true);
              currentClients.get(i).getPlayer().setFlag(false);
              team2Score++;
            }
          }
          
          //check if player is on a health location and heal the player if the health location is off cooldown
          for(int j = 0; j<healthLocation.size(); j++){
            if(!(healthLocation.get(j).getCooldown()) && (currentClients.get(i).getPlayer().getHurtbox().intersects(healthLocation.get(j).getHitbox()))){
              healthLocation.get(j).heal(currentClients.get(i).getPlayer());
            }
          }
        } else if(currentClients.get(i).getPlayer().hasFlag()){ //if player is dead with the flag return the flag
          currentClients.get(i).getPlayer().setFlag(false);
          if(currentClients.get(i).getPlayer().getTeam() == 1){
            flag2.setFlag(true);
          } else {
            flag1.setFlag(true);
          }
        }
      }
      
      //damage checking
      //check if any projectiles have hit a player on the enemy team, if so set the projectile to expire and damage the player hit
      for(int i = 0; i<team2.size(); i++){
        for(int j = 0; j<team1Projectiles.size(); j++){
          for(int k = 0; k<team1Projectiles.get(j).getHitBox()[0].length; k++){
            if(team1Projectiles.get(j).getCurrentHitBox(k).intersects(team2.get(i).getHurtbox())){
              team2.get(i).hurt(team1Projectiles.get(j));
              team1Projectiles.get(j).setExpire();
            }
          }
        }
      }
      for(int i = 0; i<team1.size(); i++){
        //do the same projectile check for the other team
        for(int j = 0; j<team2Projectiles.size(); j++){
          for(int k = 0; k<team2Projectiles.get(j).getHitBox()[0].length; k++){
            if(team2Projectiles.get(j).getCurrentHitBox(k).intersects(team1.get(i).getHurtbox())){
              team1.get(i).hurt(team2Projectiles.get(j));
              team2Projectiles.get(j).setExpire();
            }
          }
        }
        //check all players on both teams with each other and see if they have hit each other
        //if so damage the player hit
        for(int j = 0; j<team2.size(); j++){
          if(team1.get(i).getHitBox()!=null){
            for(int k = 0; k<team1.get(i).getHitBox().length; k++){
              if(!(team2.get(j).isInvul()) && (team2.get(j).getRespawnCounter()<=0 && team1.get(i).getHitBox()[k]!=null) && (team1.get(i).getHitBox()[k].intersects(team2.get(j).getHurtbox()))){
                team2.get(j).hurt(team1.get(i).getCurrentAction());
              }
            }
          }
          if(team2.get(j).getHitBox()!=null){
            for(int k = 0; k<team2.get(j).getHitBox().length; k++){
              if(!(team1.get(i).isInvul()) && (team1.get(i).getRespawnCounter()<=0 && team2.get(j).getHitBox()[k]!=null) && (team2.get(j).getHitBox()[k].intersects(team1.get(i).getHurtbox()))){
                team1.get(i).hurt(team2.get(j).getCurrentAction());
              }
            }
          }
        }
      }
      
      //calculate the next delay to maintain 30fps
      int sleepTime = (int)(nextTick - System.currentTimeMillis());
      if (sleepTime>=0){
        try{ Thread.sleep(sleepTime);} catch (Exception exc){}  //delay
      }
      nextTick = System.currentTimeMillis() + 33;
      
      //output information to the clients
      for(int i = 0; i<currentClients.size(); i++){
        //add universal information (team scores, if the flag locations have flags and if the health locations have health) as well as the information of this player
        String out = team1Score+" "+team2Score+" "+flag1.hasFlag()+" "+flag2.hasFlag()+" "+healthLocation.get(0).getCooldown()+" "+healthLocation.get(1).getCooldown()+" "+healthLocation.get(2).getCooldown()
          +" "+healthLocation.get(3).getCooldown()+" "+currentClients.get(i).getPlayer().getX()+" "+currentClients.get(i).getPlayer().getY()+" "+currentClients.get(i).getPlayer().getImage()+" "
          +currentClients.get(i).getPlayer().getDirection()+" "+currentClients.get(i).getPlayer().hasFlag()+" "+currentClients.get(i).getPlayer().getAction()+" "+currentClients.get(i).getPlayer().getActionImage()+" "
          +currentClients.get(i).getPlayer().getAttackDirection()+" "+currentClients.get(i).getPlayer().getHealth()+" "+currentClients.get(i).getPlayer().getTeam()+" "+currentClients.get(i).getPlayer().getRespawnCounter()
          +" "+currentClients.get(i).getPlayer().getWeapon()+" "+currentClients.get(i).getPlayer().getWeaponObj().getSkill1Cooldown()+" "; //a very long string seperated with spaces
        //loop through the other clients to output their display information
        for(int j = 0; j<currentClients.size(); j++){
          if(j!= i){ //make sure the current player is not outputted twice
            //add information of other players to the output string
            out+= currentClients.get(j).getPlayer().getX()+" "+currentClients.get(j).getPlayer().getY()+" "+currentClients.get(j).getPlayer().getImage()+" "+currentClients.get(j).getPlayer().getDirection()+" "
              +currentClients.get(j).getPlayer().hasFlag()+" "+currentClients.get(j).getPlayer().getAction()+" "+currentClients.get(j).getPlayer().getActionImage()+" "+currentClients.get(j).getPlayer().getAttackDirection()
              +" "+currentClients.get(j).getPlayer().getHealth()+" "+currentClients.get(j).getPlayer().getTeam()+" "+currentClients.get(j).getPlayer().getRespawnCounter()+" "+currentClients.get(j).getPlayer().getWeapon()+" ";
          }
        }
        //add information about projectiles onto output string
        out+= "proj ";
        for(int j = 0; j<team1Projectiles.size(); j++){
          out+= team1Projectiles.get(j).getIdentifier()+" "+team1Projectiles.get(j).getX()+" "+team1Projectiles.get(j).getY()+" "+team1Projectiles.get(j).getImage()+" "+team1Projectiles.get(j).getDirection()+" ";
        }
        for(int j = 0; j<team2Projectiles.size(); j++){
          out+= team2Projectiles.get(j).getIdentifier()+" "+team2Projectiles.get(j).getX()+" "+team2Projectiles.get(j).getY()+" "+team2Projectiles.get(j).getImage()+" "+team2Projectiles.get(j).getDirection()+" ";
        }
        //output the string to the client
        currentClients.get(i).output(out);
      }
      
      //check if a team has won or if a draw occured, if so inform all clients and exit the gameloop
      if(team1Score>=5){
        if(team2Score>=5){
          for(int i = 0; i<currentClients.size(); i++){
            currentClients.get(i).output("DRAW");
          }
          inGame = false;
        } else {
          for(int i = 0; i<currentClients.size(); i++){
            currentClients.get(i).output("TEAM ONE WINS");
          }
          inGame = false;
        }
      }
      if(team2Score>=5){
        for(int i = 0; i<currentClients.size(); i++){
            currentClients.get(i).output("TEAM TWO WINS");
        }
        inGame = false;
      }
    }
    
    //close the client sockets
    for(int i = 0; i<currentClients.size(); i++){
      currentClients.get(i).close();
    }
    
    //exit the program
    System.exit(0);
  }
  
  /** --------- INNER CLASSES ------------- **/
  //***** Inner class - thread for client connection
  private class ClientHandler implements Runnable{
    
    //variable declaration
    private BufferedReader clientInput;
    private PrintWriter clientOutput;
    private Socket socket;
    private Player player;
    private boolean running;
    private int playerNum;
    
    /* ClientHandler
     * Constructor for the ClientHandler class
     * @param s the socket belonging to this client connection, p the Player object that this client controls, playerNum the int that this player uses to access the input arrays
     */   
    ClientHandler(Socket s, Player p, int playerNum){
      this.socket = s; //assign socket
      try{ //initiate input and output with the client
        InputStreamReader clientStream = new InputStreamReader(socket.getInputStream());
        this.clientInput = new BufferedReader(clientStream);
        this.clientOutput = new PrintWriter(socket.getOutputStream());
        
        //get the weapon this client chose
        String wpn = clientInput.readLine();
        //set the player object's weapon (sword is default)
        if(wpn.equals("bo")){
          p.setWeapon(new Bow());
        }
      } catch(Exception e){
        System.out.println("client creation failed");
      }
      
      //set variables
      this.player = p;
      this.playerNum = playerNum;
      running = true;
    }
    
    /**
     * getPlayer
     * this method is a getter for the player this client controls
     * @return Player, the player this client controls
     */
    public Player getPlayer(){
      return player;
    }
    
    /**
     * run
     * executed at start of thread, takes client input
     */
    @Override
    public void run(){
      //variable declaration
      String msg = "";
      //main loop
      while(running){
        try{
          //take input from client
          if (clientInput.ready()){
            msg = clientInput.readLine();
            //if input is a mouse click set mouseInput for this player
            //first 2 indexes of the array is the mouse x and y locations, while the last 2 is the dimensions of the screen for that client
            if ((msg.length()>=2) && (msg.substring(0, 2).equals("MC"))){
              String[] split = msg.split(" ");
              input.get(playerNum)[9] = true;
              mouseInput.get(playerNum)[0] = Integer.parseInt(split[1]);
              mouseInput.get(playerNum)[1] = Integer.parseInt(split[2]);
              mouseInput.get(playerNum)[2] = Integer.parseInt(split[3]);
              mouseInput.get(playerNum)[3] = Integer.parseInt(split[4]);
            }
            
            //if input is a keyboard input set the appropriate input array
            if (msg.equals("W")){
              input.get(playerNum)[0] = true;
              input.get(playerNum)[4] = false;
            }
            if (msg.equals("S")){
              input.get(playerNum)[1] = true;
              input.get(playerNum)[5] = false;
            }
            if (msg.equals("D")){
              input.get(playerNum)[2] = true;
              input.get(playerNum)[6] = false;
            }
            if (msg.equals("A")){
              input.get(playerNum)[3] = true;
              input.get(playerNum)[7] = false;
            }
            if (msg.equals("rW")){ //inputs starting with "r" is the release of the key
              input.get(playerNum)[4] = true;
              input.get(playerNum)[0] = false;
            }
            if (msg.equals("rS")){
              input.get(playerNum)[5] = true;
              input.get(playerNum)[1] = false;
            }
            if (msg.equals("rD")){
              input.get(playerNum)[6] = true;
              input.get(playerNum)[2] = false;
            }
            if (msg.equals("rA")){
              input.get(playerNum)[7] = true;
              input.get(playerNum)[3] = false;
            }
            if (msg.substring(0, 1).equals("E")){ //E also stores mouse input because it is a skill key
              input.get(playerNum)[8] = true;
              String[] split = msg.split(" ");
              mouseInput.get(playerNum)[0] = Integer.parseInt(split[1]);
              mouseInput.get(playerNum)[1] = Integer.parseInt(split[2]);
              mouseInput.get(playerNum)[2] = Integer.parseInt(split[3]);
              mouseInput.get(playerNum)[3] = Integer.parseInt(split[4]);
            }
          }
        } catch(Exception e){
          e.printStackTrace();
          System.out.println("Failed to recieve message from client");
        }
      }
      
      //after client stops running close the socket
      try {
        clientInput.close();
        clientOutput.close();
        socket.close();
      } catch (Exception e){
        System.out.println("Failed to close client");
      }
    }
    
    /**
     * output
     * outputs a string to the client over the network
     * @param s the String to be output
     */
    public void output(String s){
      clientOutput.println(s);
      clientOutput.flush();
    }
    
    /**
     * close
     * sets running to false, which stops the ClientHandler
     */
    public void close(){
      running = false;
    }
  } //end of client handler
  //end of inner classes
}