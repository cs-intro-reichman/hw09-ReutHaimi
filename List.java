

/** A linked list of character data objects.
 *  (Actually, a list of Node objects, each holding a reference to a character data object.
 *  However, users of this class are not aware of the Node objects. As far as they are concerned,
 *  the class represents a list of CharData objects. Likwise, the API of the class does not
 *  mention the existence of the Node objects). */
public class List {

    // Points to the first node in this list
    private Node first;

    // The number of elements in this list
    private int size;
	
    /** Constructs an empty list. */
    public List() {
        first = null;
        size = 0;
    }
    
    /** Returns the number of elements in this list. */
    public int getSize() {
 	      return size;
    }

    /** Returns the CharData of the first element in this list. */
    public CharData getFirst() {
        if (first == null) return null;

        return first.cp;
    }

    /** GIVE Adds a CharData object with the given character to the beginning of this list. */
    public void addFirst(char chr) {
    CharData ch = new CharData(chr);
    Node node1 = new Node(ch);
    node1.next = this.first;
    this.first = node1;
    size++;
    }
    
    /** GIVE Textual representation of this list. */
    public String toString() {
    if (size == 0) return "()";
    String str = "(";
    CharData[] arr = new CharData[size];
    arr = this.toArray();
    for (int i = 0 ; i < arr.length ; i++){
        str += arr[i].toString() + " ";
    }
    str = str.substring(0, str.length()-1) + ")";
                return str;
    }

    /** Returns the index of the first CharData object in this list
     *  that has the same chr value as the given char,
     *  or -1 if there is no such object in this list. */
    public int indexOf(char ch) {
    CharData[] arr = new CharData[size];
    arr = this.toArray();
    for (int i = 0 ; i < arr.length ; i++){
        if (arr[i].chr == ch)
        {
         return i;
        } 
    }
        return -1;
    }

    /** If the given character exists in one of the CharData objects in this list,
     *  increments its counter. Otherwise, adds a new CharData object with the
     *  given chr to the beginning of this list. */
    public void update(char chr) {
        Node current = first;
        boolean found = false;
        while (current != null) {
            if (current.cp.chr == chr) {
                current.cp.count++;
                found = true;
                break;
            }
            current = current.next;
            }
        if (!found)
        addFirst(chr); 
    }

    /** GIVE If the given character exists in one of the CharData objects
     *  in this list, removes this CharData object from the list and returns
     *  true. Otherwise, returns false. */
    public boolean remove(char chr) {
        Node prev = null;
        Node current = first;

        while (current != null) {
            if (current.cp.chr == chr) {
                if (prev == null) {
                    first = first.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        return false; 
    }
    

    /** Returns the CharData object at the specified index in this list. 
     *  If the index is negative or is greater than the size of this list, 
     *  throws an IndexOutOfBoundsException. */
       public CharData get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node current = first;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.cp;
    }

    /** Returns an array of CharData objects, containing all the CharData objects in this list. */
    public CharData[] toArray() {
	    CharData[] arr = new CharData[size];
	    Node current = first;
	    int i = 0;
        while (current != null) {
    	    arr[i++]  = current.cp;
    	    current = current.next;
        }
        return arr;
    }

    /** Returns an iterator over the elements in this list, starting at the given index. */
   public ListIterator listIterator(int index) {
//     if (index < 0 || index > size) {
//         throw new IndexOutOfBoundsException();
//     }

//     Node current = first;
//     for (int i = 0; i < index; i++) {
//         current = current.next;  
//     }

//     return new ListIterator(current);
// }
    if (index < 0 || index > size) {
        throw new IndexOutOfBoundsException();
    }

    Node current = first;
    for (int i = 0; i < index; i++) {
        current = current.next;
    }

    return new ListIterator(current); 
}
    
}






