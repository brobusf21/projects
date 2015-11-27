/** Contains the pointer attributes of the row or column elements.
 *  Creates a linked list that contains an element, an index, and 
 *  a pointer to the next row or column node.
 *  @author Brandon Robinson
 *
 */
public class RowColumnElement {
	
	public Element listHead; /* Pointer to the element */
	public int index; /* Index of row or column */
	public RowColumnElement next; /* Pointer to next node */ 
	
	public RowColumnElement(int i) {
		this(i, null); 
	}
	public RowColumnElement(int i, RowColumnElement n) {  
		listHead = new Element(-1, -1, null);
		index = i;
		next = n;
	}
	
	public RowColumnElement getNext() {
		return next;
	}
	
	public void setNext(RowColumnElement n) {
		next = n;
	}
	
	public Element getList() {
		return listHead;
	}
	
	public void setList(Element l) {
		l = listHead;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int i) {
		index = i;
	}
}
