/** This class is used for two human players to interact with 
 * one another in the game. 
 * @author brandonrobinson
 *
 */
public class HumanPlayer extends Player {
	//Move array[] = new Move[10]; // 10 pieces at all times
	blrobinsonBoard board;
	int numOfMoves = 0;
	
	public HumanPlayer(int playerNum) {
		super(playerNum);
		board = new blrobinsonBoard();
	}

	@Override
	public Move getMove() {
		boolean confirm = true;
		while (confirm) {
			if (numOfMoves < 10) {
				System.out.println("Player " +playerNum+": Enter row and column position to place piece: ");
				String input = SingletonScanner.getInstance().next();
				String input2 = SingletonScanner.getInstance().next();
				if (input.matches("[a-hA-H]+") && input2.matches("[1-8]")) {
					char row = input.charAt(0);
					int col = Character.getNumericValue(input2.charAt(0)) -1;
					Move m = new Move(row, col);
					return m;
				} else {
					System.out.println("Incorrect input: letter(A-H) + number(1-8)");
				}
			} else { /* Overtime input */
				System.out.println("No more pieces to move; therefore:");
				System.out.println("Player " +playerNum+": Enter row and column position to move from followed by " +
						"row and column position to move to: ");
				String input1 = SingletonScanner.getInstance().next();
				String input2 = SingletonScanner.getInstance().next();
				String input3 = SingletonScanner.getInstance().next();
				String input4 = SingletonScanner.getInstance().next();
				if (input1.matches("[a-hA-H]+") && input2.matches("[1-8]") && input3.matches("[a-hA-H]+") && input4.matches("[1-8]")) {
					char fromRow = input1.charAt(0);
					int fromCol = Character.getNumericValue(input2.charAt(0)) -1;
					char toRow = input3.charAt(0);
					int toCol = Character.getNumericValue(input4.charAt(0)) -1;
					if ((playerNum == 1 && board.getBoard()[convertToInt(fromRow)][fromCol] != '1') ||
							(playerNum == 2 && board.getBoard()[convertToInt(fromRow)][fromCol] != '2')) {
						System.out.println("First set needs to be a taken spot already. Retry.");
						continue;
					}
					Move m = new Move(fromRow, fromCol, toRow, toCol);
					return m;
				} else {
					System.out.println("Incorrect input: letter(A-H) + number(1-8) + letter(A-H) + number(1-8)");
				}
			}
		}
		return null;
	}

	@Override
	public void OpponentMove(Move m) {
		int toRow = convertToInt(m.toRow);
		int toCol = m.toCol;
		if (playerNum == 1) {
			if (numOfMoves > 10) {
				int fromRow = convertToInt(m.fromRow);
				int fromCol = m.fromCol;
				board.add(toRow, toCol, '2');
				deleteFromBoard(fromRow, fromCol);
			} else {
				board.add(toRow, toCol, '2');
			}
		} else { 
			if (numOfMoves >= 10) {
				int fromRow = convertToInt(m.fromRow);
				int fromCol = m.fromCol;
				board.add(toRow, toCol, '1');
				deleteFromBoard(fromRow, fromCol);
			} else {
				board.add(toRow, toCol, '1');
			}
		}
	}
	
	public void updateOpponentMove(Move m) {
		int toRow = convertToInt(m.toRow);
		int toCol = m.toCol;
		int fromRow = convertToInt(m.fromRow);
		int fromCol = m.fromCol;
		if (playerNum == 1) {
			board.add(fromRow, fromCol, '2');
			deleteFromBoard(toRow, toCol);
		} else {
			board.add(fromRow, fromCol, '1');
			deleteFromBoard(toRow, toCol);
		}
	}

	
	public char[][] getBoard() {
		return board.getBoard();
	}
	
	public void showBoard() {
		board.showBoard();
	}
	
	public void addToBoard(int row, int col) {
		numOfMoves++;
		if (playerNum == 1)
			board.add(row, col, '1');
		else
			board.add(row, col, '2');
	}
	
	public int getNumOfMoves() {
		return numOfMoves;
	}
	
	public int convertToInt(char row) {
		if (row == 'a' || row == 'A')
			return 0;
		else if (row == 'b' || row == 'B') 
			return 1;
		else if (row == 'c' || row == 'C') 
			return 2;
		else if (row == 'd' || row == 'D') 
			return 3;
		else if (row == 'e' || row == 'E') 
			return 4;
		else if (row == 'f' || row == 'F') 
			return 5;
		else if (row == 'g' || row == 'G') 
			return 6;
		else if (row == 'h' || row == 'H') 
			return 7;
		else 
			return -1;
	}

	public void deleteFromBoard(int row, int col) {
		board.add(row, col, '.');
	}
}
