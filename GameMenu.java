//Imports
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Image;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.SwingUtilities;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;

/*class GameMenu
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this is the menu for the game, where the user can either view instructions or begin the game
 */

//class starts here
class GameMenu extends JFrame { 

  //variable that points to this frame
  JFrame thisFrame;
  
  /**
   * GameMenu
   * Constructor for the GameMenu class
   */
  GameMenu() { 
    
    //call to super
    super("Menu");
    this.thisFrame = this; //set thisFrame variable
    
    //configure the window
    this.setSize(550,450);     
    this.setLocationRelativeTo(null); //start the frame in the center of the screen
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
    this.setResizable (false);
    
    //Create a Panel for buttons
    //this uses a decPanel which has a picture attached to it
    JPanel decPanel = new DecoratedPanel();
    decPanel.setBorder(BorderFactory.createEmptyBorder());
    decPanel.setLayout(null);
    
    //Create a JButton whose purpose is to open the main game
    ImageIcon sb =new ImageIcon("button1.png");
    JButton startButton = new JButton(sb);
    startButton.setBackground(new Color(0, 0, 0, 0));
    startButton.setBorder(BorderFactory.createEmptyBorder());
    startButton.setFocusPainted(false);
    startButton.addActionListener(new StartButtonListener());
    startButton.setBounds(10, 190, 240, 60);
    
    //Create a JButton whose purpose is to open the controls page
    ImageIcon cb =new ImageIcon("button2.png");
    JButton controlButton = new JButton(cb);
    controlButton.setBackground(new Color(0, 0, 0, 0));
    controlButton.setBorder(BorderFactory.createEmptyBorder());
    controlButton.setFocusPainted(false);
    controlButton.addActionListener(new ControlButtonListener());
    controlButton.setBounds(10, 260, 240, 60);
    
    //Create a JButton whose purpose is to close the program
    ImageIcon eb =new ImageIcon("button3.png");
    JButton exitButton = new JButton(eb);
    exitButton.setBackground(new Color(0, 0, 0, 0));
    exitButton.setBorder(BorderFactory.createEmptyBorder());
    exitButton.setFocusPainted(false);
    exitButton.addActionListener(new ExitButtonListener());
    exitButton.setBounds(10, 330, 240, 60);
    
    //Add all buttons to the panel
    decPanel.add(startButton);
    decPanel.add(controlButton);
    decPanel.add(exitButton);
    
    //add the main panel to the frame
    this.add(decPanel);
    
    //Start the app
    this.setVisible(true);
  }
  
  //INNER CLASS - Overide Paint Component for JPANEL
  private class DecoratedPanel extends JPanel {
    
    /**
     * DecoratedPanel()
     * Constructor for the DecoratedPanel class
     */
    DecoratedPanel() {
      this.setBackground(new Color(0,0,0,0));
    }
    
    /**
     * paintComponent
     * this overwirtes the paintComponent in JPanel to display the content of this  panel
     * @param Graphics g
     */ 
    @Override
    public void paintComponent(Graphics g) { 
        super.paintComponent(g);     
        Image pic = new ImageIcon( "menuScreen.png" ).getImage();
        g.drawImage(pic,0,20,null); 
   }
  }
  
  //These are inner classes that is used to detect a button press
  //this ActionListener is used to start the main game
  class StartButtonListener implements ActionListener {  //this is the required class definition
    /*
     * actionPerformed
     * this method disposes of this frame and starts the main game
     * @param ActionEvent event
     * @return none
     */ 
    @Override
    public void actionPerformed(ActionEvent event)  {  
      System.out.println("Starting new Game");
      thisFrame.dispose();
      new GameClient(); //create a new GameClient (another file that extends JFrame)
    }
  }
  //this ActionListener is used to open the controls page
  class ControlButtonListener implements ActionListener {  //this is the required class definition
    /*
     * actionPerformed
     * this method starts the controls frame
     * @param ActionEvent event
     * @return none
     */ 
    @Override
    public void actionPerformed(ActionEvent event)  {  
      new InformationFrame(); //create a new InformationFrame (another file that extends JFrame)
    }
  }
  //this ActionListener is used to close this program
  class ExitButtonListener implements ActionListener {  //this is the required class definition
    /*
     * actionPerformed
     * this method closes this program
     * @param ActionEvent event
     * @return none
     */ 
    @Override
    public void actionPerformed(ActionEvent event)  { 
      //close the program
      System.exit(0);
    }
  }
  //end of inner classes
  
  //Main method starts this application
  public static void main(String[] args) { 
    new GameMenu();

  }
  
}