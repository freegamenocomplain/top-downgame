//imports
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.awt.Graphics;
import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.border.EmptyBorder;

/*class InformationFrame
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class displays a window where the controls are displayed
 */

//class starts here
class InformationFrame extends JFrame{
  
  /**
   * InformationFrame
   * Constructor for the InformationFrame class
   */
  InformationFrame(){
    
    //call to super
    super("Controls");
    
    //configure the window
    this.setSize(1000,1000);     
    this.setLocationRelativeTo(null); //start the frame in the center of the screen
    this.setResizable (false);
    
    //Create a Panel for stuff
    //this uses a decPanel which has a picture attached to it
    JPanel decPanel = new DecoratedPanel();
    decPanel.setBorder(BorderFactory.createEmptyBorder());
    decPanel.setLayout(null);
    
    //add the main panel to the frame
    this.add(decPanel);
    
    //Start the app
    this.setVisible(true);
  }
  
  //INNER CLASS - Overide Paint Component for JPANEL
  //same one as in GameMenu but with a different picture
  private class DecoratedPanel extends JPanel {
    private BufferedImage image;
    
    /**
     * DecoratedPanel()
     * Constructor for the DecoratedPanel class
     */
    DecoratedPanel() {
      try {
        image = ImageIO.read(new File("instructions.png"));
      } catch (Exception e) {}
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
      g.drawImage(image,0,-20,null); 
    }
  }//inner classes end
}