/**
 * HuffmanNode class. Used to hold node attributes and pointers.
 * @author Brandon Robinson
 *
 */
public class HuffmanNode {
	private char character_;
	private Comparable freq_;
	private HuffmanNode leftChild_;
	private HuffmanNode rightChild_;
	private String code_;

	/**
	 * HuffmanNode constructor. 
	 * @param f Frequency
	 */
	public HuffmanNode(int f) {
		this('\0', f, null, null, "");
	}
	
	/**
	 * HuffmanNode constructor. 
	 * @param c Character
	 * @param f Frequency
	 * @param lc Left child
	 * @param rc Right Child
	 * @param code Codes: 0's or 1's
	 */
	public HuffmanNode(char c, int f, HuffmanNode lc, HuffmanNode rc, String code) {
		character_ = c;
		freq_ = f;
		leftChild_ = lc;
		rightChild_ = rc;
		code_ = code;
	}

	/**
	 * HuffmanNode constructor.
	 * @param c Character
	 * @param lc Left child
	 * @param rc Right child
	 * @param code Codes: 0's 0r 1's
	 */
	public HuffmanNode(char c, HuffmanNode lc, HuffmanNode rc, String code) {
		character_ = c;
		leftChild_ = lc;
		rightChild_ = rc;
		code_ = code;
	}

	public char getCharacter_() {
		return character_;
	}

	public void setCharacter(char character) {
		character_ = character;
	}

	public Comparable getFreq_() {
		return freq_;
	}

	public void setFreq_(Comparable freq) {
		freq_ = freq;
	}

	public HuffmanNode getLeftChild() {
		return leftChild_;
	}

	public void setLeftChild(HuffmanNode left) {
		leftChild_ = left;
	}

	public HuffmanNode getRightChild() {
		return rightChild_;
	}

	public void setRightChild(HuffmanNode right) {
		rightChild_ = right;
	}

	public String getCode() {
		return code_;
	}

	public void setCode(String code) {
		code_ = code;
	}
}
