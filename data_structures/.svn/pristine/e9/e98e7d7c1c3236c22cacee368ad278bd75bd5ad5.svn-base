/**
 * HuffmanCoding class. Reads and writes text files for compression. 
 * @author Brandon Robinson
 *
 */
public class HuffmanCoding {
	int[] freq; /* This is the array used for keeping track of freq */
	HuffmanNode[] treeArray; /* Array of HuffmanNodes */
	String[] tableArray; /* String array that 0 & 1's for code table */
	int numOfCh = 0; /* Number of characters */
	int numLeaves = 0; /* Number of leaves */
	int numIntern = 0; /* Number of internal nodes */
	BinaryFile outputFile; /* Global in order to write to file and then write later */

	/**
	 * HuffmanCoding constructor. Receives the output file and assigns it to a global 
	 * variable so it can be used throughout the class. The "-w" command is also passed
	 * into constructor so that the file can be written to. Utilzes a BinaryFile when writing
	 * to output file.
	 * @param output The name of the output file.
	 * @param type Either "r" or "w" for reading or writing. 
	 */
	public HuffmanCoding(String output, char type) {
		freq = new int[256]; /* 256 ASCII values */
		outputFile = new BinaryFile(output, type);
	}

	/**
	 * Reads the input file to determine number of characters and each character's frequency. 
	 * Adds the frequency to an array at the location of it's ASCII value.
	 * @param fileName
	 * @param type
	 */
	public void readFile(String fileName, char type) {
		TextFile inputFile = new TextFile(fileName, type);
		while (!inputFile.EndOfFile()) {
			numOfCh++;
			char ch = inputFile.readChar();
			freq[(int) ch]++;
		}
		inputFile.rewind();
		inputFile.close();
	}

	/**
	 * Builds the treeArray by adding new HuffmanNodes for every character that is found in the 
	 * input file. After reading creating the nodes, the treeArray is sorted for easier removal of 
	 * nodes.
	 */
	public void buildHuffmanTree() {
		treeArray = new HuffmanNode[256];
		int numTrees = 0;
		int j = 0;
		for (int i = 0; i < freq.length; i++) {
			if (freq[i] != 0) {
				numTrees++;
				char c = (char) i;
				HuffmanNode hm = new HuffmanNode(c, freq[i], null, null, "");
				treeArray[j] = hm;
				j++;
			}
		}
		treeArray = sortTreeArray(treeArray, numTrees);
		modifyTree(treeArray, numTrees);
	}

	/**
	 * Sorts the treeArray in order to locate the two lowest frequency nodes in array. 
	 * @param treeArray treeArray.
	 * @param numTrees Number of nodes in treeArray
	 * @return
	 */
	public HuffmanNode[] sortTreeArray(HuffmanNode [] treeArray, int numTrees) {
		int i, j;
		HuffmanNode elem; 
		for (i=1; i < numTrees; i++) {
			elem = treeArray[i]; 
			for (j=i-1;j >= 0 && treeArray[j].getFreq_().compareTo(elem.getFreq_()) == -1; j--)
				treeArray[j+1] = treeArray[j];
			treeArray[j+1] = elem;
		}
		return treeArray;
	}

	/**
	 * Modifies the treeArray such that the two lowest frequency nodes are removed, combined, and 
	 * then re-added to the treeArray as a new HuffmanNode with children. numTrees is also reduced
	 * as the amount of HuffmanNodes reduce in treeArray. For every modification to the tree, it is 
	 * then reesorted. 
	 * @param treeArray treeArray
	 * @param numTrees Number of nodes in treeArray
	 */
	public void modifyTree(HuffmanNode[] treeArray, int numTrees) {
		int smallestIndex; 
		int smallest2Index;
		while (numTrees > 1) {
			HuffmanNode smallest = treeArray[numTrees-1];
			HuffmanNode smallest2 = treeArray[numTrees-2];
			smallestIndex = numTrees-1;
			smallest2Index = numTrees-2;
			removeSmallestElements(treeArray, smallestIndex, smallest2Index);
			treeArray[numTrees-2] = combineNodes(smallest, smallest2); // Just added the element
			numTrees--;
			treeArray = sortTreeArray(treeArray, numTrees);
		}
		buildCodeTable(treeArray[0]);
	}

	/** 
	 * Helper function that removes the two lowest frequency nodes in treeArray by assigning them null.
	 * @param treeArray treeArray
	 * @param index First lowest frequnecy
	 * @param index2 Second lowest frequnecy
	 */
	public void removeSmallestElements(HuffmanNode[] treeArray, int index, int index2) {
		treeArray[index] = null;
		treeArray[index2] = null;
	}

	/**
	 * Helper function that combines the two lowest frequnecy nodes by adding their frequnecy count, 
	 * creating a new HuffmanNode with that sum, then assigning a left and right child to that node.
	 * @param smallest First lowest frequnecy
	 * @param smallest2 Second lowest frequnecy
	 * @return The new node that has combined freqeuncy with left and right child
	 */
	public HuffmanNode combineNodes(HuffmanNode smallest, HuffmanNode smallest2) {
		int combination = (Integer) smallest.getFreq_() + (Integer) smallest2.getFreq_();
		HuffmanNode newNode = new HuffmanNode(combination);
		newNode.setRightChild(smallest);
		newNode.setLeftChild(smallest2);
		return newNode;
	}

	/**
	 * Creates a new array of strings that will hold the encoding values for each character 
	 * that is found in the input file. Utilizes a private recursive method to assign the encoding
	 * values to each HuffmanNode that is in treeArray. 
	 * @param root Root of treeArray
	 */
	public void buildCodeTable(HuffmanNode root) {
		tableArray = new String[256];
		buildCodeTable(root, tableArray); 
	}

	/**
	 * Priavte recursive method that iterates through treeArray and adds a "0" or "1" to each 
	 * HuffmanNode's code attribute. For every left child, a "0' is added, for every right 
	 * child, a "1" is added. The tableArray holds the encoding values at each character's
	 * ASCII value.
	 * @param root treeArray root
	 * @param tableArray treeArray
	 * @return Null
	 */
	private String buildCodeTable(HuffmanNode root, String[] tableArray) {
		if (root.getLeftChild() != null) {
			numIntern++;
			root.getLeftChild().setCode(root.getCode() + "0");
			buildCodeTable(root.getLeftChild(), tableArray);
			root.getRightChild().setCode(root.getCode() + "1");
			buildCodeTable(root.getRightChild(), tableArray);
		} else {
			numLeaves++;
			tableArray[(int) root.getCharacter_()] = root.getCode();
		}
		return null;
	}

	/**
	 * Writes magic numbers H & F to the output file, followed by calling on a helper function
	 * to write the treeArray to the output file.
	 */
	public void writeToFile() {
		outputFile.writeChar('H'); // Magic Number
		outputFile.writeChar('F'); // Magic Number
		//		outputFile.writeChar((char) (numLeaves+numIntern));
		writeTree(treeArray[0]); /* Passing in the root */
	}

	/**
	 * Writes the tree to the output file in bits and characters for compression. 
	 * If the character of HuffmanNode is '\0', then this node is an internal node. 
	 * Otherwise, the node is a leaf.
	 * @param tree Root of treeArray
	 */
	public void writeTree(HuffmanNode tree) {
		if (tree != null) {
			if (tree.getCharacter_() != '\0') { /* Leaf */
				outputFile.writeBit(false);
				outputFile.writeChar(tree.getCharacter_());
			} else { /* Internal node */
				outputFile.writeBit(true);
			}
			writeTree(tree.getLeftChild());
			writeTree(tree.getRightChild());
		}
	}

	/**
	 * Re-reads the input file after the magic number and tree have been written
	 * to the output file to add the encoded values for each character. This method
	 * utilizes helper function to write individual bits of encoded string.
	 * @param iF Input file that needs to be re-read again
	 * @param type For the TextFile class, type = 'r' for reading
	 */
	public void writeEncodedValues(String iF, char type) {
		TextFile inputFile = new TextFile(iF, type);
		while (!inputFile.EndOfFile()) {
			char ch = inputFile.readChar();
			String encode = tableArray[(int) ch];
			writeCodeString(encode);
		}
		inputFile.rewind();
		inputFile.close();
		outputFile.close();
	}

	/**
	 * Helper function that assigns bit as false for every 0 and assigns bit
	 * true for every 1. These 0's and 1's are the encoded string that is attached
	 * to every character in the input file. 
	 * @param encode Encoded string that contains 1's and 0's
	 */
	public void writeCodeString(String encode) {
		for (int i = 0; i < encode.length(); i++) {
			if (encode.charAt(i) == '0') {
				outputFile.writeBit(false);
			} else {
				outputFile.writeBit(true);
			}
		}
	}

	/**
	 * Computes the size of the compressed file. 
	 * @return The size of the compressed file.
	 */
	public int sizeOfCompressed() {
		int totalBits = 0;
		for (int i = 0; i < freq.length; i++) {
			if (freq[i] != 0) {
				char c = (char) i;
				int f = freq[i];
				int t = tableArray[(int) c].length();
				totalBits+= (f*t);
			}
		}
		totalBits += (numIntern*8) + (numLeaves*9) + 48;
		return totalBits;
	}

	/** 
	 * Computes the size of the uncompressed file.
	 * @return The size of the uncompressed file.
	 */
	public int sizeOfUncompressed() {
		return numOfCh * 8;
	}

	/**
	 * Checks to see if either a -f or -v flag is apart of the command line.
	 * @param args Command line arguments
	 * @param Flag
	 * @return True if flag is there, false if otherwise
	 */
	public boolean checkOptions(String[] args, String c) {
		for (int i = 1; i < args.length-2; i++) { // Double check
			if (args[i].equals(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * If verbose flag (-v) is apart of the command line, this method will print out
	 * what is required in the assignment sheet. 
	 */
	public void verboseFlag() {
		System.out.println("Character frequency:");
		for (int i = 0; i < freq.length; i++) {
			if (freq[i] != 0) {
				System.out.println(i);
			}
		}
		System.out.println("\n" + "Tree:");
		print(treeArray[0]);
		System.out.println("\n");
		System.out.println("Huffman codes for each character:");
		for (int i = 0; i < freq.length; i++) {
			if (freq[i] != 0) {
				String encode = tableArray[i];
				System.out.println(encode);
			}
		}
		System.out.println("\n");
		System.out.println("Uncompressed size: " + sizeOfUncompressed());
		System.out.println("Compressed size: " + sizeOfCompressed());
	}

	/**
	 * Recursive method is used to print out the tree to standard out. 
	 * @param tree Tree array root
	 */
	public void print(HuffmanNode tree) {
		if (tree != null) {
			if (tree.getCharacter_() != '\0') { /* Leaf */
				System.out.print(tree.getCharacter_());
				System.out.print("1");
			} else { /* Internal node */
				System.out.print(tree.getFreq_());
				System.out.print("0");
			}
			print(tree.getLeftChild());
			print(tree.getRightChild());
		}
	}

	/**
	 * Helper function that is used to close the output file to ensure that it is 
	 * closed properly. 
	 */
	public void closeFile() {
		outputFile.close();
	}

}
