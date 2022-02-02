/*class SimpleLinkedList
 * @version beta-1.03
 * @author Eric Wang
 * 06-12-19
 * This is a topdown team based action game
 * the objective of this game is to capture the enemy's objective while defending yours
 * the game is networked with a server and clients
 * server handles all calculations while clients displays the game
 * players can enter combat through melee and projectile based attacks
 * this class is a simple linked structure
 */

class SimpleLinkedList<E> {
  
  //the head of the linked list
  private Node<E> head;
  
  /**
   * add
   * adds an item to the end of this linkedlist
   * @param item, the item to be added
   */
  public void add(E item) { 
    Node<E> tempNode = head;
    if (head==null) {
      head=new Node<E>(item,null);
      return;
    }
    while(tempNode.getNext()!=null) { 
      tempNode = tempNode.getNext();
    }
    tempNode.setNext(new Node<E>(item,null));
    return;
  }
  
  /**
   * isEmpty
   * determines if this linkedlist is empty
   * @return boolean, true if this list is empty
   */
  public boolean isEmpty(){
    if(head == null){
      return true;
    }
    return false;
  }
    
  /**
   * get
   * gets an item from the linked list
   * @param index, the index of the item needed
   * @return E, the item at the index
   */
  public E get(int index) {
    if (head == null){
      return null;
    }
    Node<E> tempNode = head;
    for(int i = 0; i<index; i++){
      if (tempNode.getNext()!= null){
        tempNode = tempNode.getNext();
      } else {
        System.out.println("array out of bounds");
        return null;
      }
    }
    return tempNode.getItem();
  }
  
  /**
   * indexOf
   * finds the index of an item in the linked list
   * @param item, the item whose index is needed
   * @return int, index of the item
   */
  public int indexOf(E item) {
    if (head == null){
      return -1;
    }
    Node<E> tempNode = head;
    int counter = 0;
    for(int i = 0; i<size(); i++){
      if ((tempNode != null) && (tempNode.getItem().equals(item))){
        return i;
      }
      tempNode = tempNode.getNext();
    }
    return -1;
  }
  
  /**
   * remove
   * removes an item at a specified index
   * @param index, the index of the item that needs to be removed
   * @return E, the removed item
   */
  public E remove(int index) { 
    E item;
    if (head == null){
      return null;
    }
    if (index == 0){
      item = head.getItem();
      head = head.getNext();
      return item;
    }
    Node<E> tempNode = head;
    for(int i = 0; i<index-1; i++){
      if(tempNode.getNext() != null){
        tempNode = tempNode.getNext();
      } else {
        System.out.println("array out of bounds");
        return null;
      }
    }
    if (tempNode.getNext()== null){
      item = null;
      tempNode.setNext(null);
    } else {
      item = tempNode.getNext().getItem();
      tempNode.setNext(tempNode.getNext().getNext());
    }
    return item;
  }
  
  
  /**
   * remove
   * removes a specified item
   * @param item, the item that needs to be removed
   * @return boolean, true if the item removal is successful
   */
  public boolean remove(E item) { 
    if (indexOf(item)==-1){
      return false;
    }
    if (remove(indexOf(item))==null){
      return false;
    }
    return true;
  }
    
  /**
   * clear
   * clears this linkedlist
   */
  public void clear() { 
    head = null;
  }
   
  /**
   * size
   * gets the size of this linkedlist
   * @return int, the size of this linkedlist
   */
  public int size() {
    if (head == null){
      return 0;
    }
    Node<E> tempNode = head;
    int counter = 1;
    while(tempNode.getNext() != null){
      tempNode = tempNode.getNext();
      counter++;
    }
    return counter;
  }
}



