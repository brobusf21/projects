/** This class is imcomplete; however, it was intended that 
 * a computer player would have been used to play against other 
 * computer players or human players. A counting array was used
 * to help determine importance of the moves.
 * @author Brandon Robinson
 *
 */
public class blrobinsonComputerPlayer extends Player{
	blrobinsonBoard board;
	blrobinsonBoard tempBoard = new blrobinsonBoard();
	boolean[][] visited;
	int[][] countBoard = new int[8][8];
	int numOfMoves = 0;
	Move startPath;

	public blrobinsonComputerPlayer(int playerNum) {
		super(playerNum);
		board = new blrobinsonBoard();
		setCountBoard();
		clearVisited();
	}

	@Override
	public Move getMove() {
		boolean confirm = true;
		while (confirm) {
			if (numOfMoves == 0) {
				System.out.println("Computer Player " +playerNum+": Enter row and column position to place piece: ");
				return startUp();
			}
			if (numOfMoves >= 1 && numOfMoves < 10) {
				System.out.println("Computer Player " +playerNum+": Enter row and column position to place piece: ");
				return nextMove();
			} else { /* Overtime input */
				System.out.println("No more pieces to move; therefore:");
				System.out.println("Player " +playerNum+": Enter row and column position to move from followed by " +
						"row and column position to move to: ");
				String input = SingletonScanner.getInstance().next();
				if (input.matches("[a-hA-H1-8 ]+")) {
					char fromRow = input.charAt(0);
					int fromCol = Integer.parseInt(input.substring(2, 3)) -1;
					char toRow = input.charAt(4);
					int toCol = Integer.parseInt(input.substring(input.length()-1)) -1;
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
				System.out.println("COME ON x2");
				int fromRow = convertToInt(m.fromRow);
				int fromCol = m.fromCol;
				System.out.println("fromRow: " +fromRow + " fromCol: " + fromCol);
				board.add(toRow, toCol, '1');
				deleteFromBoard(fromRow, fromCol);
				System.out.println("**HERE after add");
			} else {
				board.add(toRow, toCol, '1');
				tempBoard.add(toRow, toCol, '1');
				directionSetter(toRow, toCol);
				showCountBoard();
			}
		}
	}

	public void directionSetter(int row, int col) {
		for (int xdef = -1; xdef<=1; xdef++) {
			for (int ydef = -1; ydef <=1; ydef++) {
				if (xdef != 0 || ydef != 0) {
					int currX = row + xdef;
					int currY = col + ydef;
					if (currX != -1 && currX != 8 && currY != -1 && currY != 8) {
						while(isValid(currX, currY)) {
							countBoard[currX][currY]++;
							currX+=xdef;
							currY+=ydef;
						}
					}
				}
			}
		}
	}
	
	public void setCountBoard() {
		for (int i = 0; i< 8; i++) {
			for (int j = 0; j<8; j++) {
				countBoard[i][j] = 0;
			}
		}
	}
 

	public boolean isValid(int row, int col) {
		System.out.println("Enter isValid w/ row: " + row + " " + "& col: " + col);
		if (row == 8 || row == -1 || col == 8 || col == -1) {
			return false;
		} else if ((playerNum == 1 && row == 0) || (playerNum==2 && col == 0)) {
			return true;
		} else if (playerNum == 1 && tempBoard.getBoard()[row][col]=='2')  {
			return false;
		} else if (playerNum == 2 && tempBoard.getBoard()[row][col] == '1') {
			return false;
		} else if (playerNum == 1 && tempBoard.getBoard()[row][col]=='1')  {
			return false;
		} else if (playerNum == 2 && tempBoard.getBoard()[row][col]=='2')  {
			return false;
		}
		return true;
	}
	
	public boolean validCompLocation(Move m) {
		//char[][] b = p.getBoard();
		int row = convertToInt(m.toRow);
		int col = m.toCol;
		if (board.getBoard()[row][col] != '.') { /* Spot is taken */
			System.out.println("Player " + playerNum + ": " + "Bad move!");
			System.out.println("Spot taken!");
			return false;
		} else if ((row==0 && col==0) || (row==0 && col==7) || (row==7 && col==0) || (row==7 && col==7)) { /* 4 corners */
			System.out.println("Player " + playerNum + ": " + "Bad move!");
			System.out.println("Can not put a marker on a corner");
			return false;
		} else if ((playerNum==1 && col==0) || (playerNum==1 && col==7) || (playerNum==2 && row==0) || (playerNum==2 && row==7)) { /* Opponent's goal */
			System.out.println("Player " + playerNum + ": " + "Bad move!");
			System.out.println("Position in opponent's goal!");
			return false;
		} else {
			return true;
		}
	}

	/* ********************************************** */
	public Move startUp() {
		Move m;
		if (playerNum == 1) {
			m = new Move('A', 3);
			startPath = m;
		} else {
			m = new Move('D', 0);
			startPath = m;
		}
		return m;
	}

	public Move nextMove() {
		Move m;
		//Move m = new Move('A', 6);
		// Go defensive or not? Check opponents path to see if they are close
		// Continue to move forward starting from col 0 to col 7
		if (oppIsAggressive()) {
			return locateBlockMove();
		} else {
			return createPath(startPath.toRow, startPath.toCol);
		}
		//return evaluateBoard();
		//return m;
	}

	public Move createPath(int row, int col) {
		for (int xdef = -1; xdef<=1; xdef++) {
			for (int ydef = -1; ydef <=1; ydef++) {
				if (xdef != 0 || ydef != 0) {
					int currX = row + xdef;
					int currY = col + ydef;
					if (currX != -1 && currX != 8 && currY != -1 && currY != 8) {
						while(isValid(currX, currY)) {
							currX+=xdef;
							currY+=ydef;
						}
						Move m = new Move(convertToCh(currX), currY);
						if (validCompLocation(m)) {
							return m;
						}
					}
				}
			}
		}
		return null;
	}
	
	public Move locateBlockMove() {
		for (int i = 0; i< 8; i++) {
			for (int j = 0; j<8; j++) {
				if (countBoard[i][j] > numOfMoves) {
					Move m = new Move(convertToCh(i), j);
					return m;
				}
			}
		}
		return null;
	}
	
	public boolean oppIsAggressive() {
		for (int i = 0; i< 8; i++) {
			for (int j = 0; j<8; j++) {
				if (countBoard[i][j] > numOfMoves) {
					return true;
				}
			}
		}
		return false;
	}

	/* ************************************************** */

	public char[][] getBoard() {
		return board.getBoard();
	}

	public void showBoard() {
		board.showBoard();
	}
	
	public void showTempBoard() {
		tempBoard.showBoard();
	}
	
	public void showCountBoard() {
		for (int i = 0; i< 8; i++) {
			for (int j = 0; j<8; j++) {
				System.out.print(countBoard[i][j]);
			}
			System.out.println();
		}
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
	
	public char convertToCh(int row) {
		if (row == 0)
			return 'A';
		else if (row == 1) 
			return 'B';
		else if (row == 2) 
			return 'C';
		else if (row == 3) 
			return 'D';
		else if (row == 4) 
			return 'E';
		else if (row == 5) 
			return 'F';
		else if (row == 6) 
			return 'G';
		else if (row == 7) 
			return 'H';
		else 
			return ' ';
	}

	public void deleteFromBoard(int row, int col) {
		board.add(row, col, '.');
	}
	
	/** This method is used to clear the boolean 
	 * visited array in order to generate a path.
	 */
	public void clearVisited()
	{
		if (visited == null)
		{
			visited = new boolean[8][8];
		}
		for (int i = 0; i < visited.length; i++)
			for (int j = 0; j < visited.length; j++)
				visited[i][j] = false;   
	}
}
