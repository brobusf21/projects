/* Brandon Robinson
 * Project 2 : Huffman Coding
 * CS 245 / Prof. Galles
 * 23 March 2015
 */

/**
 * Driver class. Used to read in command line arguments and set up program.
 * @author Brandon Robinson
 *
 */
public class Driver {
	/**
	 * Main method that reads in command line arguments: (-c|-u) [-v] [-f] infile outfile. 
	 * -c: Compression; reads in input file and compresses the file if it is worth it. 
	 * -u: Decompression; reads in input file, checks magic number and decompresses the file. 
	 * -v: Optional; used to print certain tasks to system out.
	 * -f: Optional; used to force compression regradless if it is worth it or not.
	 * infile: Input file that needs to be compressed or decompressed.
	 * outfile: Output file which is either an encoded file or a regular text file after
	 * decompression.
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			System.err.println("Invalid command line: (-c|-u) [-v] [-f] infile outfile ");
			System.exit(1);
		}
		if (args[0].equals("-c") || args[0].equals("-u") && !args[args.length-1].startsWith("-") && !args[args.length-2].startsWith("-")) {
			if (args[0].equals("-c")) {
				HuffmanCoding hc = new HuffmanCoding(args[args.length-1], 'w'); /* Passing in the output File */
				hc.readFile(args[args.length-2], 'r'); /* Passing in input file */
				hc.buildHuffmanTree();
				hc.writeToFile();
				if (hc.sizeOfCompressed() < hc.sizeOfUncompressed() || hc.checkOptions(args, "-f")) { // We need to include the idea of flag -f
					hc.writeEncodedValues(args[args.length-2], 'r');
				} else {
					System.err.println("The file was not compressed");
					hc.closeFile();
					System.exit(1);
				}
				if (hc.checkOptions(args, "-v"))
					hc.verboseFlag();
			}
			if (args[0].equals("-u")) {
				Decompression d = new Decompression(args[args.length-2], 'r');
				if (d.checkMN() == true) {
					d.buildTree();
					d.decodeFile(args[args.length-1], 'w');
				} else {
					System.err.println("Magic numbers do not match");
					System.exit(1);
				}
				if (d.checkOptions(args, "-v"))
					d.verboseFlag();
			}
		} else {
			System.err.println("Invalid command line: (-c|-u) [-v] [-f] infile outfile ");
			System.exit(1);
		}
	}
}
