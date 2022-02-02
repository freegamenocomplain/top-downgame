/*class Node
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is a node in the linked list
 */

//class starts here
class Node<T> { 
  //variable declaration
  private T item;
  private Node<T> next;

  /**
   * Node
   * Constructor for the Node class
   * @param item, the item this node contains
   */
  public Node(T item) {
    this.item=item;
    this.next=null;
  }
  
  /**
   * Node
   * Alternative Constructor for the Node class
   * @param item the item this node contains, next the next node that this node points to
   */
  public Node(T item, Node<T> next) {
    this.item=item;
    this.next=next;
  }
  
  /*
   * getNext
   * getter for next
   * @return Node, the next node in the linked structure
   */
  public Node<T> getNext(){
    return this.next;
  }
  
  /*
   * setNext
   * setter for next
   * @param next, the next node in the linked structure
   */
  public void setNext(Node<T> next){
    this.next = next;
  }
  
  /*
   * getItem
   * getter for item
   * @return T, the item that this node contains
   */
  public T getItem(){
    return this.item;
  }
  
}