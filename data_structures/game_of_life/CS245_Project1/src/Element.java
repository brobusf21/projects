/**
 * Contains the pointer attributes for the inner list elements.
 * Contains a row index, column index, and an object.  
 * @author Brandon Robinson 
 *
 */
public class Element implements MatrixElem {
	int rowIndex;
	int colIndex;
	Object value; 
	public Element next; /* Next element in row's inner list */
	public Element bottom; /* Next element in column's inner list */

	public Element(int row, int col, Object val) {
		this(row, col, val, null, null);
	}
	
	public Element(int row, int col, Object val, Element n, Element b) {
		rowIndex = row;
		colIndex = col;
		value = val;
		next = n;
		bottom = b;
	}
	
	public Element getBottom() {
		return bottom;
	}

	public void setBottom(Element b) {
		bottom = b;
	}

	public Element getNext() {
		return next;
	}

	public void setNext(Element n) {
		next = n;
	}
	
	@Override
	public int rowIndex() {
		return rowIndex;
	}
	
	@Override
	public int columnIndex() {
		return colIndex;
	}
	
	@Override
	public Object value() {
		return value;
	}
}
