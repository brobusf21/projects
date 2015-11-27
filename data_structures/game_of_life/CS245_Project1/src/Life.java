import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/** Uses the class MySparseArray to play the Game of life. Three sparse arrays are used in this class:
 * currentGen - First data points are input from a text file and then this sparse array is later manipulated.
 * nextGen - Used to create new currentGen sparse array after calculating the number of neighbors
 * numOfNeighbors - Sparse array of ints that keeps track of the number of neighbors; a new one is created each 
 * generation. 
 * @author Brandon Robinson
 */
public class Life {
	MySparseArray currentGen;
	MySparseArray nextGen;

	public Life() {
		currentGen = new MySparseArray(false); 
		nextGen = new MySparseArray(false);  
	}
	/** Reads input from a textfile to populate the first generation sparse array. Uses a BufferedReader
	 * to read line by line extracting row and column values. Will throw an exception if the file is not found
	 * or the it is not able to read from the textfile.
	 * @param inputFile
	 */
	public void readFile(File inputFile) {
		try {
			BufferedReader input = new BufferedReader(new FileReader(inputFile));
			String lines;
			while ((lines = input.readLine()) != null) { /* This condition allows BufferedReader to read words line by line */
				if (lines.length() > 0) { // This condition eliminates blank lines from text file 
					String[] rowColValues = lines.split(",");
					int row = Integer.parseInt(rowColValues[0]);
					int col = Integer.parseInt(rowColValues[1]);
					currentGen.setValue(row, col, true); // Setting up the first generation
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("Can not locate file that is being inputted " + e.getMessage());
		} catch (IOException e) {
			System.out.println("Not able to read lines from file" + e.getMessage());
		}
	}
	
	/** Uses to start up the Game of Life after reading input and populating the currentGen 
	 * sparse array. Receives number of generations from command line and uses a for loop to 
	 * accurately create x number of generations. A new numOfNeighbors is called each generation.
	 * @param numOfGens
	 */
	public void startUp(int numOfGens) {
		for (int i = 0; i < numOfGens; i++) {
			MySparseArray numOfNeighbors = new MySparseArray(0);
			currentGenIterator(numOfNeighbors);
			nextGenCreator(numOfNeighbors);
		}
	}
	
	/** Uses a ColumnIterator to iterate through the currentGen elements in order to find the 
	 * elements that are alive or "true." If found, those values are passed to updateNeighborCount.
	 * @param numOfNeighbors
	 */
	public void currentGenIterator(MySparseArray numOfNeighbors) {
		ColumnIterator colIter = currentGen.iterateColumns();
		while (colIter.hasNext())
		{
			ElemIterator elmIt = colIter.next();
			while (elmIt.hasNext())
			{
				MatrixElem m = elmIt.next();
				if (m.value().equals(true)) {
					updateNeighborCount(m.rowIndex(), m.columnIndex(), numOfNeighbors);
				}
			}
		}
	}
	
	/** Counts the number of neighbors by looking at and around the alive elements in currentGen
	 * and places them in the numOfNeighbors sparse array to be used to create the nextGen array.
	 * @param row
	 * @param col
	 * @param numOfNeighbors
	 */
	public void updateNeighborCount(int row, int col, MySparseArray numOfNeighbors) {	
		for (int startCol = col-1; startCol <= col+1; startCol++) {
			for (int startRow = row-1; startRow <= row+1; startRow++) {
				int value = (int) numOfNeighbors.elementAt(startRow, startCol);
				if ((startRow == row) && (startCol == col)) {
				} else {
					value++;
				}
				numOfNeighbors.setValue(startRow, startCol, value);
			}
		}	
	}

	/**
	 * Creates the nextGen sparse array by looking at the number of neighbors the currentGen
	 * array has. There are multple rules that decide whether or not an element is dead or alive.
	 * If the element is alive, it is placed in the nextGen array. If it is dead, the nextGen array
	 * will update the value to false. The currentGen array is then replaced by the nextGen array. 
	 * @param numOfNeighbors
	 */
	public void nextGenCreator(MySparseArray numOfNeighbors) {
		ColumnIterator colIter = numOfNeighbors.iterateColumns();
		while (colIter.hasNext())
		{
			ElemIterator elmIt = colIter.next();
			while (elmIt.hasNext())
			{
				MatrixElem m = elmIt.next();
				boolean deadOrAlive = (boolean) currentGen.elementAt(m.rowIndex(), m.columnIndex());
				if (deadOrAlive == true) {
					if ((m.value().equals(2) || m.value().equals(3))) {
						nextGen.setValue(m.rowIndex(), m.columnIndex(), true);
					} else if ((m.value().equals(0) || m.value().equals(1))) {
						nextGen.setValue(m.rowIndex(), m.columnIndex(), false);
					} else if ((int) m.value() >= 4) { 
						nextGen.setValue(m.rowIndex(), m.columnIndex(), false); 
					}
				} else {
					if (m.value().equals(3)){
						nextGen.setValue(m.rowIndex(), m.columnIndex(), true);
					}
				}
			}
		}
		currentGen = nextGen;
	}

	/** Writes results to a text file after x number of generations are completed. Uses PrintWrtier and 
	 * a RowIterator to iterate though the currentGen rows to extract the row and columns.
	 * @param outputFile
	 */
	public void writeToFile(File outputFile) {
		PrintWriter writer;
		try {
			writer = new PrintWriter(new FileWriter(outputFile));
			RowIterator rowIter = currentGen.iterateRows();
			while (rowIter.hasNext())
			{
				ElemIterator elmIt = rowIter.next();
				while (elmIt.hasNext())
				{
					MatrixElem m = elmIt.next();
					if (m.value().equals(true)) {
						writer.println(m.rowIndex() + "," + m.columnIndex());
					}
				}
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			System.out.println("File already exists");
		}
	}

	public static void main(String[] args) {
		File inputFile = null;
		File outputFile = null;
		if (args.length != 3) {
			System.out.println(args.length);
			System.err.println("Invalid command line: <input file name> <output file name> <number of generations> ");
			System.exit(1);
		}
		inputFile = new File(args[0]);
		outputFile = new File (args[1]);
		int numOfGens = Integer.parseInt(args[2]);
		
		Life gameOfLife = new Life();
		gameOfLife.readFile(inputFile);
		gameOfLife.startUp(numOfGens);
		gameOfLife.writeToFile(outputFile);
	}
}
