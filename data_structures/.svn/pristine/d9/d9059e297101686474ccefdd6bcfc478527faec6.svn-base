/*
 * Brandon Robinson
 * CS245 Galles
 * 22 February 2015
 * Project 1 
 */
 
 /** Implements a sparse array using a linked list. Contains methods 
  * that are used to add and remove elements as needed. It contains 
  * three nested classes used for iterators. 
  *  @author Brandon Robinson
 */
public class MySparseArray implements SparseArray {
	Object defaultEle; /* Default value */
	RowColumnElement rowElementHead; /* Head of row pointer */
	RowColumnElement columnElementHead; /* Head of column pointer */
	
	public MySparseArray(Object defValue) {
		defaultEle = defValue;
		rowElementHead = new RowColumnElement(0); /* Creating dummy element */
		columnElementHead = new RowColumnElement(0); /* Creating dummy element */
	}
	
	/** Returns the default value for the sparse array
	 */
	@Override
	public Object defaultValue() {
		return defaultEle;
	}
	
	/** Returns an iterator that can be used to iterate through the 
	 * rows of the array. 
	 */
	@Override
	public RowIterator iterateRows() {
		return new InnerRowIterator();
	}
	
	/** Returns an iterator that can be used to iterate through the columns
	 *  of the array
	 */
	@Override
	public ColumnIterator iterateColumns() {
		return new InnerColumnIterator();
	}
	
	/** Returns the object stored at (row,col), if such an element exists, 
	 * or the default value, if not such an element exists. Utilizing the 
	 * column elements, it first locates the correct column's inner 
	 * list elements and then traverses through them till it reaches a 
	 * match with the corresponding row index.
	 */
	@Override
	public Object elementAt(int row, int col) {
		RowColumnElement currentColumnEle = columnElementHead.getNext();
		Element currentList;
		while (currentColumnEle != null) {
			if (currentColumnEle.getIndex() == col) {
				currentList = currentColumnEle.getList();
				while (currentList != null) {
					if (currentList.rowIndex() == row) {
						return currentList.value(); 
					}
					currentList = currentList.getBottom();
				}
			}
			currentColumnEle = currentColumnEle.getNext();
		}
		return defaultEle;
	}
	
	/** Sets the value of the matrix at position (row,col), adding new linked
	 *  list element(s) as necessary. If element already exists, then the elements
	 *  of the list will be added as necessary. It first starts off by creating 
	 *  dummy elements for both lists and then adds row and column elements as 
	 *  needed. If the row or column element already exists, then instead of adding 
	 *  an additinal element, this method calls a method to update the inner list
	 *  elements. If an element is passed in with a default value, that element
	 *  will be removed by calling another method. 
	 */
	@Override
	public void setValue(int row, int col, Object value) {
		Element elem = new Element(row, col, value); /* New element object */
		RowColumnElement currentRow = rowElementHead; /* Head of row elements */
		RowColumnElement currentCol = columnElementHead; /* Head of column elements */
        
		/* ------------ Adding column ------------ */
    	while (currentCol.getNext() != null && currentCol.getNext().getIndex() < col) {
            currentCol = currentCol.getNext();
        }
    	if (currentCol.getNext() != null && currentCol.getNext().getIndex() == col) { /* Element exists; update elements */
    		if (value.equals(defaultEle)) { /* If value equals defaultEle, then we need to remove that node */
    			removeColumnElement(currentCol, row, col);
    		} else {
    			updateColumnListElements(currentCol.getNext(), elem, row, col, value);
    		}
    	} else { /* Element does not exist; add it */
            RowColumnElement tempCol = new RowColumnElement(col); 
    		tempCol.setNext(currentCol.getNext()); 
    		currentCol.setNext(tempCol);
    		updateColumnListElements(currentCol.getNext(), elem, row, col, value);
    	}
    	
        /* ------------ Adding Row ------------ */
    	while (currentRow.getNext() != null && currentRow.getNext().getIndex() < row) {
            currentRow = currentRow.getNext();
        }
    	if (currentRow.getNext() != null && currentRow.getNext().getIndex() == row) {
    		if (value.equals(defaultEle)) { 
    			removeRowElement(currentRow, row, col);
    		} else { 
    			updateRowListElements(currentRow.getNext(), elem, row, col, value);
    		}
    	} else { 
            RowColumnElement tempRow = new RowColumnElement(row); 
    		tempRow.setNext(currentRow.getNext()); 
    		currentRow.setNext(tempRow); 
    		updateRowListElements(currentRow.getNext(), elem, row, col, value);
    	}
	} 
	
	/** Updates the row's inner list elements if the row element already 
	 *  exists or if a new row was added.
	 *  @param currentRowEle
	 *  @param elem
	 *  @param row
	 *  @param col
	 *  @param value
	 */
	public void updateRowListElements(RowColumnElement currentRowEle, Element elem, int row, int col, Object value) {
		Element temp = elem;
		Element currentList = currentRowEle.getList();
		int elemColIdx = elem.columnIndex();
		while (currentList.getNext() != null && currentList.getNext().columnIndex() < elemColIdx) {
			currentList = currentList.getNext();
		}
		if (currentList.getNext() != null && currentList.getNext().columnIndex() == elemColIdx) {
			currentList.getNext().value = elem.value;
			return;
		}
		temp.setNext(currentList.getNext());
		currentList.setNext(temp);
	} 
	
	/** Updates the column's inner list elements if the column element already
	 *  exists or if a new column was added.
	 *  @param currentColEle
	 *  @param elem
	 *  @param row
	 *  @param col
	 *  @param value
	 */
	public void updateColumnListElements(RowColumnElement currentColEle, Element elem, int row, int col, Object value) {
		Element temp = elem;
		Element currentList = currentColEle.getList();
		int elemRowIdx = elem.rowIndex();
		while (currentList.getBottom() != null && currentList.getBottom().rowIndex() < elemRowIdx) {
			currentList = currentList.getBottom();
		}
		if (currentList.getBottom() != null && currentList.getBottom().rowIndex() == elemRowIdx) {
			currentList.getBottom().value = elem.value;
			return;
		}
		temp.setBottom(currentList.getBottom());
		currentList.setBottom(temp);
	} 
	
	/** Removes the inner list column elements if the element's value is set to the default value.
	 *  @param currentCol
	 *  @param row
	 *  @param col
	 */
	public void removeColumnElement(RowColumnElement currentCol, int row, int col) {
    	Element colElementHead = currentCol.getNext().getList();
    	while (colElementHead.getBottom() != null) {
			if (colElementHead.getBottom().rowIndex() == row) {
				colElementHead.setBottom(colElementHead.getBottom().getBottom());
				break;
			}
			colElementHead = colElementHead.getBottom();
    	}
    	removeEmptyColumn(currentCol, row); 	
	} 
	
	/** Removes the inner list row elements if the element's value is set to the default value.
	 *  @param currentRow
	 *  @param row
	 *  @param col
	 */
	public void removeRowElement(RowColumnElement currentRow, int row, int col) {
    	Element rowElementHead = currentRow.getNext().getList();
    	while (rowElementHead.getNext() != null) {
			if (rowElementHead.getNext().rowIndex() == row) {
				rowElementHead.setNext(rowElementHead.getNext().getNext());
				break;
			}
			rowElementHead = rowElementHead.getNext();
    	}
    	removeEmptyRow(currentRow, row);
	} 
	
	/** Removes any row elements that do not contain inner list elements leaving the 
	 *  row empty.
	 *  @param currentRow
	 *  @param row
	 */
	public void removeEmptyRow(RowColumnElement currentRow, int row) {
		Element currentList = currentRow.getNext().getList();
		if (currentList.getNext() == null) {
			currentRow.setNext(currentRow.getNext().getNext());
		}
	}
	
	/** Removes any column elements that do not contain inner list elements leaving the 
	 *  column empty.
	 *  @param currentCol
	 *  @param col
	 */
	public void removeEmptyColumn(RowColumnElement currentCol, int col) {
		Element currentList = currentCol.getNext().getList();
		if (currentList.getBottom() == null) {
			currentCol.setNext(currentCol.getNext().getNext());
		}	
	} 

	
	/***********************************
	 * Nested class -- InnerRowIterator 
	 ***********************************/
	
	/** Creates a row iterator to parse the elements of the row
	 *  @author Brandon Robinson
	 */
	private class InnerRowIterator extends RowIterator {
		public RowColumnElement currentNode;
		public boolean rowTrue;

		private InnerRowIterator() {
			currentNode = rowElementHead;
			rowTrue = true; /* Using a boolean to determine if we are iterating through a row or column */
		}

		/** Does not return an element, but another iterator to traverse every element in the row.
		 */
		@Override
		public ElemIterator next() {
			return new InnerElementIterator(currentNode, rowTrue);
		}

		/** Returns true if there are more nodes in the row 
		 */
		@Override
		public boolean hasNext() {
			if (currentNode.getNext() != null) {
				currentNode = currentNode.getNext();
				return true;
			} else {
				return false;
			}
		}
	}

	
	/***********************************
	 * Nested class -- InnerColumnIterator 
	 ***********************************/
	/** Creates a column iterator to parse through column elements
	 *  @author Brandon Robinson
	 */
	private class InnerColumnIterator extends ColumnIterator {
		private RowColumnElement currentNode;
		private boolean columnFalse;

		private InnerColumnIterator() {
			currentNode = columnElementHead;
			columnFalse = false;
		}

		/** Does not return an element, but another iterator to traverse 
		 *  every element in the column.
		 */
		@Override
		public ElemIterator next() {
			return new InnerElementIterator(currentNode, columnFalse);
		}

		/** Returns true if there are more nodes in this column 
		 */
		@Override
		public boolean hasNext() { 
			if (currentNode.getNext() != null) {
				currentNode = currentNode.getNext();
				return true;
			} else {
				return false;
			}
		}
	}	

	
	/***********************************
	 * Nested class -- InnerElementIterator 
	 ***********************************/
	/** Creates an iterator to parse through the inner list elements 
	 *  of the row or column elements 
	 *  @author Brandon Robinson
	 *
	 */
	private class InnerElementIterator extends ElemIterator {

		private Element currentListHead; /* Inner list head */
		private boolean result; /* Boolean to determine row or column */
		private int rowIndex;
		private int colIndex;


		private InnerElementIterator(RowColumnElement currentNode, boolean type) {
			rowIndex = currentNode.index;
			colIndex = currentNode.index;
			currentListHead = currentNode.getList();
			result = type;
		}
		
		/** Returns true if this iterator is iterating through a row
		 */
		@Override
		public boolean iteratingRow() {
			if (result == true)
				return true;
			else 
				return false;
		}
		
		/** Returns true if this iterator is iterating through a column
		 */
		@Override
		public boolean iteratingCol() {
			if (result == false) 
				return true;
			else
				return false;
		}

		/** If iterating through a row, return the index of 
		 *  the row that is being traversed. Iterating through a column,
		 *  return the index of the column that is being traversed.
		 */
		@Override
		public int nonIteratingIndex() {
			if (iteratingRow() == true) {
				return rowIndex;
			} else {
				return colIndex;
			}
		}

		/** Returns the next element in the row or column we are traversing
		 */
		@Override
		public MatrixElem next() {
			return currentListHead;
		}

		/** Returns true if there are more elements in the row or column
		 */
		@Override
		public boolean hasNext() {
			if (iteratingRow() == true) {
				if (currentListHead.getNext() != null) {
					currentListHead = currentListHead.getNext();
					return true;
				} else {
					return false;
				}
			} else {
				if (currentListHead.getBottom() != null) {
					currentListHead = currentListHead.getBottom();
					return true;
				} else {
					return false;  
				}
			}
		} 
	}
}

