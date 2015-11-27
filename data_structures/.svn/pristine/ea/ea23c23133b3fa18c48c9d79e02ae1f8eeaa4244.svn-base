/**
 * Decompression class. Reads and writes text files in order to decompress file.
 * @author Brandon Robinson
 *
 */
public class Decompression {
	BinaryFile inputFile; /* Input file */
	HuffmanNode root = new HuffmanNode('\0', null, null, ""); /* Null root node */
	HuffmanNode tmp = root; /* Setting up tmp node as root */
	int count = 0; /* Used to keep track of encoded values */

	/**
	 * Decompression constructor. Takes in file name and char type which is 'r' 
	 * for reading.
	 * @param fileName Input file
	 * @param type 'r' for reading
	 */
	public Decompression(String fileName, char type) {
		inputFile = new BinaryFile(fileName, type);
	}

	/**
	 * Boolean method that checks if magic numbers exists in the input file. If the magic 
	 * numbers are not there, returns false and will not comtinue on with decompression.
	 * @return
	 */
	public boolean checkMN() {
		if (inputFile.readChar() == 'H' && inputFile.readChar() == 'F') {
			return true;
		}
		return false;
	}

	/**
	 * Calls private recursive method to build the tree by reading in the tree from the input
	 * file. Uses helper function to compute number of nodes that are in tree.
	 */
	public void buildTree() {
		int numTrees = getNumTrees();
		buildTree(root, numTrees); 
	}

	/**
	 * Recursive method that builds the tree by reading it in from the input file. This tree is then 
	 * used to decode the file by traversing the tree, following each characters code until a leaf is 
	 * reached.
	 * @param node HuffmanNode tree root
	 * @param limit Controls the number of trees there are
	 * @return The leaf node
	 */
	private HuffmanNode buildTree(HuffmanNode node, int limit) {
		boolean t = inputFile.readBit();
		HuffmanNode newNode = new HuffmanNode('\0', null, null, ""); // empty internal node
		if (t == false) {
			char c = inputFile.readChar();
			HuffmanNode leafNode = new HuffmanNode(c, null, null, "");
			return leafNode;
		} else {
			node.setLeftChild(buildTree(newNode, limit-1));
			node.setRightChild(buildTree(newNode, limit-1));
			return node;
		}
	}

	/**
	 * Helper function that computes the number of nodes that are in the tree.
	 * @return
	 */
	public int getNumTrees() {
		int numTrees = inputFile.readChar();
		return numTrees;
	}

	/**
	 * Reads the 0's and 1's that represent characters in the input file. Calls on
	 * helper function to read each bit and then iterates through tree.
	 * @param fileName output file
	 * @param type 'w' to write
	 */
	public void decodeFile(String fileName, char type) {
		TextFile outputFile = new TextFile(fileName, type);
		while (!inputFile.EndOfFile()) {
			iterateTree(inputFile.readBit(), outputFile);
		}
		outputFile.close();
	}

	/**
	 * Iterates through tree that was built in order to write out characters. As the
	 * tree is traversed, it will follow each bit's path, 0 for leftChild and 1 for 
	 * rightChild until a leaf is reached and the character is written to output file.
	 * @param bit Boolean bit, 0=false, 1=true
	 * @param outputFile output file
	 */
	public void iterateTree(boolean bit, TextFile outputFile) {
		if (bit == false) {
			tmp = tmp.getLeftChild();
			count++;
		} else if (bit == true) {
			tmp = tmp.getRightChild();
			count++;
		}
		if (tmp.getCharacter_() != '\0') {
			outputFile.writeChar(tmp.getCharacter_());
			count = 0;
			tmp = root;
		}
	}

	/**
	 * Used to print tree. Calls on private recursive method.
	 */
	public void print() {
		print(root);
	}
	
	/**
	 * Recursive method that prints the tree to standard out.
	 * @param tree HuffmanNode tree
	 */
	private void print(HuffmanNode tree) {
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
	 * Checks to see if a -v flag is apart of the command line.
	 * @param args Command line arguments
	 * @param Flag
	 * @return True if flag is there, false if otherwise
	 */
	public boolean checkOptions(String[] args, String c) {
		for (int i = 1; i < args.length-2; i++) {
			if (args[i].equals(c)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * If verbose flag is apart of command line, then tree will printed to standard out. 
	 */
	public void verboseFlag() {
		System.out.println("Tree:");
		print();
	}
}