/* Brandon Robinson
 * CS245 Prof. Galles
 * 13 May 2015
 * Project 4: Network Assignment
 */

/** This is the main class for the our Network Project. It starts off by requesting
 * input from the user and then checking different conditions in order to see if a 
 * winning path is created from one network end to the other. The details and rules are 
 * rather long but can be found in the assignment sheet. 
 * @author Brandon Robinson
 */
public class Network {
	public boolean[][] visited;
	public String[] p1WinArray = new String[10];
	public String[] p2WinArray = new String[10];

	public Network() {
		clearVisited();
	}

	/** This method is essentially the main method that runs the game.
	 * It reads in number of players and then checks multiple conditions
	 * in order to see if a player has won the game.
	 */
	public void playGame() {
		int numPlayers = 0;
		boolean correctNP = true;
		while(correctNP) {
			System.out.println("Enter the number of human players (0, 1, or 2): ");
			numPlayers = SingletonScanner.getInstance().nextInt();
			if (numPlayers == 0 || numPlayers == 1 || numPlayers == 2) 
				correctNP = false;
		}
		if (numPlayers == 1) {
			HumanPlayer hp = new HumanPlayer(1);
			blrobinsonComputerPlayer cp = new blrobinsonComputerPlayer(2);
			hp.showBoard();
			boolean win = true;
			while (win) {
				verifyHPCPMove(hp, cp);
				if (checkForWin(hp) == true) {
					printWinArray(hp);
					break;
				}
				verifyCPHPMove(hp, cp);
				//				if (checkForWin(cp) == true) {
				//					printWinArray(cp);
				//					break;
			}
		} else if (numPlayers == 2) { /* 2 player game */
			HumanPlayer hp1 = new HumanPlayer(1);
			HumanPlayer hp2 = new HumanPlayer(2);
			hp1.showBoard();
			boolean win = true;
			while (win) {
				verifyP1Move(hp1, hp2);
				if (checkForWin(hp1) == true) {
					printWinArray(hp1);
					break;
				}
				verifyP2Move(hp1, hp2);
				if (checkForWin(hp2) == true) {
					printWinArray(hp2);
					break;
				}
				if (hp1.getNumOfMoves() >= 10 && hp2.getNumOfMoves() >= 10) {
					verifyP1OTMove(hp1, hp2);
					if (checkForWin(hp1) == true) {
						printWinArray(hp1);
						break;
					}
					verifyP2OTMove(hp1, hp2);
					if (checkForWin(hp2) == true) {
						printWinArray(hp2);
						break;
					}
				}
			}
		}
	}


	/* ********************* HUMAN PLAYER OVERTIME vs HUMAN PLAYER OVERTIME ********************* */

	/** This method reads in Player 1's overtime moves.
	 * @param hp1 Player 1.
	 * @param hp2 Player 2.
	 */
	public void verifyP1OTMove(HumanPlayer hp1, HumanPlayer hp2) {
		boolean confirm = true;
		while (confirm) {
			Move p1Move = hp1.getMove();
			if (validReplaceLocation(hp1, p1Move)) {
				hp1.addToBoard(convertToInt(p1Move.toRow), p1Move.toCol);
				hp1.deleteFromBoard(convertToInt(p1Move.fromRow), p1Move.fromCol);
				hp1.showBoard();
				hp2.OpponentMove(p1Move);
				if (bothWins(hp1, hp2)) {
					System.out.println("Player 1: " + "Bad move!");
					System.out.println("That would cause both players to win");
					hp1.addToBoard(convertToInt(p1Move.fromRow), p1Move.fromCol);
					hp1.deleteFromBoard(convertToInt(p1Move.toRow), p1Move.toCol);
					hp2.updateOpponentMove(p1Move);
				} else {
					confirm = false;
				}
			}
		}
	}

	/** This method reads in Player 2's overtime moves.
	 * @param hp1 Player 1.
	 * @param hp2 Player 2.
	 */
	public void verifyP2OTMove(HumanPlayer hp1, HumanPlayer hp2) {
		boolean confirm = true;
		while (confirm) {
			Move p2Move = hp2.getMove();
			if (validReplaceLocation(hp2, p2Move)) {
				hp2.addToBoard(convertToInt(p2Move.toRow), p2Move.toCol);
				hp2.deleteFromBoard(convertToInt(p2Move.fromRow), p2Move.fromCol);
				hp2.showBoard();
				hp1.OpponentMove(p2Move);
				if (bothWins(hp1, hp2)) {
					System.out.println("Player 2: " + "Bad move!");
					System.out.println("That would cause both players to win");
					hp2.addToBoard(convertToInt(p2Move.fromRow), p2Move.fromCol);
					hp2.deleteFromBoard(convertToInt(p2Move.toRow), p2Move.toCol);
					hp1.updateOpponentMove(p2Move);
				} else {
					confirm = false;
				}
			}
		}
	}


	/* ********************* HUMAN PLAYER vs COMPUTER PLAYER ********************* */	

	/** This method reads in the moves for the human player vs computer player.
	 * @param hp Human player 1.
	 * @param cp Computer player 1
	 */
	public void verifyHPCPMove(HumanPlayer hp, blrobinsonComputerPlayer cp) {
		boolean confirm = true;
		while (confirm) {
			Move hpMove = hp.getMove();
			if (validLocation(hp, hpMove)) {
				hp.addToBoard(convertToInt(hpMove.toRow), hpMove.toCol);
				hp.showBoard();
				cp.OpponentMove(hpMove);
				confirm = false;
			}
		}
	}


	/** This method reads in the moves for the computer vs human player.
	 * @param hp Human player 1.
	 * @param cp Computer player 1
	 */
	public void verifyCPHPMove(HumanPlayer hp, blrobinsonComputerPlayer cp) {
		boolean confirm = true;
		while (confirm) {
			Move cpMove = cp.getMove();
			if (validCompLocation(cp, cpMove)) {
				cp.addToBoard(convertToInt(cpMove.toRow), cpMove.toCol);
				cp.showBoard();
				hp.OpponentMove(cpMove);	
				confirm = false;
			}
		}
	}

	/* ********************* HUMAN PLAYER vs HUMAN PLAYER ********************* */

	/** This method reads in the moves for player one.
	 * @param hp1 Player 1.
	 * @param hp2 Player 2.
	 */
	public void verifyP1Move(HumanPlayer hp1, HumanPlayer hp2) {
		boolean confirm = true;
		while (confirm) {
			Move p1Move = hp1.getMove();
			if (validLocation(hp1, p1Move)) {
				hp1.addToBoard(convertToInt(p1Move.toRow), p1Move.toCol);
				hp1.showBoard();
				hp2.OpponentMove(p1Move);
				confirm = false;
			}
		}
	}


	/** This method reads in the moves for player two.
	 * @param hp1 Player 1.
	 * @param hp2 Player 2.
	 */
	public void verifyP2Move(HumanPlayer hp1, HumanPlayer hp2) {
		boolean confirm = true;
		while (confirm) {
			Move p2Move = hp2.getMove();
			if (validLocation(hp2, p2Move)) {
				hp2.addToBoard(convertToInt(p2Move.toRow), p2Move.toCol);
				hp2.showBoard();
				hp1.OpponentMove(p2Move);	
				confirm = false;
			}
		}
	}
	/* ***************************************************************************** */

	/** This method checks to see if one player's move will create a winnning network
	 * for both players. If this is the case, that is not allowed and the user will be 
	 * asked to enter in a different location.
	 * @param hp1 Player 1.
	 * @param hp2 Player 2.
	 * @return True if the move creates a winning network for both players, false if 
	 * otherwise.
	 */
	public boolean bothWins(HumanPlayer hp1, HumanPlayer hp2) {
		if (hp1.getNumOfMoves() >= 6 && hp2.getNumOfMoves() >= 6) {
			if (startPoint(hp1) == true && startPoint(hp2) == true){
				return true;	
			} else {
				clearWinArray(hp1);
				clearWinArray(hp2);
			}
		}
		return false;
	}


	/** This method validates the replacement of values if the number of moves
	 * exceeds 10.
	 * @param p Player
	 * @param m Move
	 * @return True if the value is valid, false otherwise.
	 */
	public boolean validReplaceLocation(HumanPlayer p, Move m) {
		char[][] b = p.getBoard();
		int fromRow = convertToInt(m.fromRow);
		int fromCol = m.fromCol;
		if (p.playerNum == 1) {
			if (b[fromRow][fromCol] == '1' && validLocation(p, m)) {
				return true;
			}
		} else {
			if (b[fromRow][fromCol] == '2' && validLocation(p, m)) {
				return true;
			}
		}
		return false; 
	}


	/** This method is used to validate the computer's move.
	 * @param p Player
	 * @param m Move
	 * @return True if the move is valid, false otherwise.
	 */
	public boolean validCompLocation(blrobinsonComputerPlayer p, Move m) {
		char[][] b = p.getBoard();
		int row = convertToInt(m.toRow);
		int col = m.toCol;
		if (b[row][col] != '.') { /* Spot is taken */
			System.out.println("Player " + p.playerNum + ": " + "Bad move!");
			System.out.println("Spot taken!");
			return false;
		} else if ((row==0 && col==0) || (row==0 && col==7) || (row==7 && col==0) || (row==7 && col==7)) { /* 4 corners */
			System.out.println("Player " + p.playerNum + ": " + "Bad move!");
			System.out.println("Can not put a marker on a corner");
			return false;
		} else if ((p.playerNum==1 && col==0) || (p.playerNum==1 && col==7) || (p.playerNum==2 && row==0) || (p.playerNum==2 && row==7)) { /* Opponent's goal */
			System.out.println("Player " + p.playerNum + ": " + "Bad move!");
			System.out.println("Position in opponent's goal!");
			return false;
		} else if (clumpOf3(m, p.playerNum, b) >= 3) { /* Clump of 3 markers */
			System.out.println("Player " + p.playerNum + ": " + "Bad move!");
			System.out.println("Position would form clump");
			return false;
		} else {
			return true;
		}
	}

	/** This method checks whether or not the player's move is valid. 
	 * @param hp1 
	 * @param row Row by alphabet letters A-H
	 * @param col Column by numbers 1-8
	 * @param playerNum Player 1 or player 2
	 * @return
	 */
	public boolean validLocation(HumanPlayer p, Move m) {
		char[][] b = p.getBoard();
		int row = convertToInt(m.toRow);
		int col = m.toCol;
		if (b[row][col] != '.') { /* Spot is taken */
			System.out.println("Player " + p.playerNum + ": " + "Bad move!");
			System.out.println("Spot taken!");
			return false;
		} else if ((row==0 && col==0) || (row==0 && col==7) || (row==7 && col==0) || (row==7 && col==7)) { /* 4 corners */
			System.out.println("Player " + p.playerNum + ": " + "Bad move!");
			System.out.println("Can not put a marker on a corner");
			return false;
		} else if ((p.playerNum==1 && col==0) || (p.playerNum==1 && col==7) || (p.playerNum==2 && row==0) || (p.playerNum==2 && row==7)) { /* Opponent's goal */
			System.out.println("Player " + p.playerNum + ": " + "Bad move!");
			System.out.println("Position in opponent's goal!");
			return false;
		} else if (clumpOf3(m, p.playerNum, b) >= 3) { /* Clump of 3 markers */
			System.out.println("Player " + p.playerNum + ": " + "Bad move!");
			System.out.println("Position would form clump");
			return false;
		} else {
			return true;
		}
	}

	/** This method is used to determine whether or not a move will create a clump of 3 or not.
	 * If the player decides to place a marker next to a previous marker of theirs, this would 
	 * increase the total to 2 which is a valid move. However, if the player decides to place 
	 * an additional marker next to the previous 2 markers, this would bring the total to 3 which
	 * makes the move invalid. In order to check for the third move, a private helper function is used.
	 * @param row Row by alphabet letters A-H
	 * @param col Column by numbers 1-8
	 * @param playerNum Player 1 or player 2
	 * @return
	 */
	public int clumpOf3(Move m, int playerNum, char[][] board) {
		int fromRow = convertToInt(m.fromRow);
		int fromCol = m.fromCol;
		int toRow = convertToInt(m.toRow);
		int toCol = m.toCol;
		int total = 0;
		for (int i = toRow-1; i<=toRow+1 && i!=8; i++) {
			i = rangeChecker(toRow, i); 
			for (int j = toCol-1; j<=toCol+1 && j!=8; j++) {
				j = rangeChecker(toCol, j);
				if (i != fromRow && j != fromCol) {
					if (playerNum == 1 && board[i][j] == '1') {
						total++;
						total += subFunction(i, j, playerNum, board);
					} else if (playerNum==2 && board[i][j] == '2') {
						total++;
						total += subFunction(i, j, playerNum, board);
					}
				}
			}
		}
		return total;
	}

	/** This method is used to catch an ArrayOutOfBoundsException by making sure that if the 
	 * index is trying to use a -1 value, it would skip that row or column by setting it to
	 * the correct row or column.
	 * @param rowCol The row or column 
	 * @param index The index the loop is currently on
	 * @return
	 */
	private int rangeChecker(int rowCol, int index) {
		if (index==-1) 
			index = rowCol;
		return index;
	}

	/**This method is a private sub function for the clumpOf3 method. It is used to check
	 * whether or not there is a third neighbor in close proximity. If so, the total would be 
	 * increased to 3 instead of remaining at 2; therefore, making the move invalid. 
	 * @param row Row by alphabet letters A-H
	 * @param col Column by numbers 1-8
	 * @param playerNum Player 1 or player 2
	 * @return
	 */
	private int subFunction(int row, int col, int playerNum, char[][] board) {
		int total = 0;
		for (int i = row-1; i<=row+1 && i!=8; i++) {
			i = rangeChecker(row, i);
			for (int j = col-1; j<=col+1 && j!=8; j++) {
				j = rangeChecker(col, j);
				if (playerNum == 1 && board[i][j] == '1') {
					total++;
				} else if (playerNum==2 && board[i][j] == '2') {
					total++;
				}
			}
		}
		return total;
	}

	/** This method checks the game to see if there is a win or not.
	 * @param p Which player it is.
	 * @return True for win or False for no win.
	 */
	public boolean checkForWin(HumanPlayer p) {
		if (p.getNumOfMoves() >= 6) {
			if (startPoint(p) == true){
				System.out.print("Player " + p.playerNum + " wins! ");
				return true;	
			} else {
				clearWinArray(p);
			}
		}
		return false;
	}


	/** This method locates the starting point for the winning network if
	 * there is one. 
	 * @param p Player.
	 * @return True if a winning path is found, false otherwise.
	 */
	public boolean startPoint(HumanPlayer p) {
		char[][] board = p.getBoard();
		if (p.playerNum==1) {
			for(int i=0; i<1; i++) {
				for (int j=0; j<=7; j++) {
					if (i==0 && board[i][j]=='1') {
						if (findPath(p, i, j, 0, 0, board)) {
							addToWinArray(p, 0, i, j);
							return true;
						}
						clearVisited();
					}
				}
			}
		} else {
			for(int j=0; j<1; j++) {
				for (int i=0; i<=7; i++) {
					if (j==0 && board[i][j]=='2') {
						if (findPath(p, i, j, 2, 0, board)) {
							addToWinArray(p, 0, i, j);
							return true;
						}
						clearVisited();
					}
				}
			}
		}
		return false;
	}


	/** This method finds the winning path by iterating through the board. It looks at all 8
	 * directions by utilizing a nested for loop. It then uses a while loop to iterate in one
	 * direction until it runs into a wall which is defined by the isValid function. In addition,
	 * if the path comes acrossed a player's marker, the slope is different, and it has not been 
	 * visited yet, it will return true. Once the ending marker is found and the count is 6 or greater,
	 * true will be returned a path has been found.
	 * @param p Player.
	 * @param row Row in integer form.
	 * @param col Col in interger form.
	 * @param slope Slope value that has been calculated in the getSlope function.
	 * @param count Count variable that needs to be at least 6 in order for a winning path.
	 * @param board Player's board. 
	 * @return True if there is a winning path, false otherwise.
	 */
	public boolean findPath(HumanPlayer p, int row, int col, int slope, int count, char[][] board) {
		count++;
		if (p.playerNum == 1 && row==7 && count>=6 && slope != 0) { // && count>=6) {
			return true;
		} else if (p.playerNum == 2 && col==7 && count>=6 && slope != 2) {
			return true;
		} else {
			visited[row][col] = true;
			for (int xdef = -1; xdef<=1; xdef++) {
				for (int ydef = -1; ydef <=1; ydef++) {
					if (xdef != 0 || ydef != 0) {
						int currX = row + xdef;
						int currY = col + ydef;
						if (currX != -1 && currX != 8 && currY != -1 && currY != 8) {
							while(isValid(currX, currY, p, board)) {
								currX+=xdef;
								currY+=ydef;
							}
							if (currX != -1 && currX != 8 && currY != -1 && currY != 8 && visited[currX][currY] == false ) {
								int currSlope = getSlope(currX, row, currY, col);
								if (p.playerNum==1 && board[currX][currY] == '1' && (checkDirection(slope, currSlope)) && findPath(p, currX, currY, currSlope, count, board)) {
									addToWinArray(p, count, currX, currY);
									return true;
								} else if (p.playerNum==2 && board[currX][currY] == '2' && (checkDirection(slope, currSlope)) && findPath(p, currX, currY, currSlope, count, board)) {
									addToWinArray(p, count, currX, currY);
									return true;
								}
							}
						}
					}
				}
			}
			return false;
		}
	}


	/** This method generates the slope in order to see if directions have been changed.
	 * If the direction is vertical, it will return 2. If the direction is horizontal, it 
	 * will return 0. And finally, if the direction is diagonal, it will return -1.
	 * @param y2 
	 * @param y1
	 * @param x2
	 * @param x1
	 * @return The slope value.
	 */
	public int getSlope(int y2, int y1, int x2, int x1) {
		int top = y2-y1;
		int bottom = x2-x1;	
		if (bottom == 0 && top != 0) {
			return 2;
		}
		int m = top / bottom;
		return m;
	}


	/** This method checks if there is a change in direction of the slope. 
	 * @param oldSlope Old slope value.
	 * @param currSlope The current slope value.
	 * @return True if the direction has been changed, false otherwise.
	 */
	public boolean checkDirection(int oldSlope, int currSlope) {
		if (oldSlope == currSlope)
			return false; 
		else
			return true;
	}


	/** This method checks whether or not the move is valid when trying to locate a winning
	 * path.
	 * @param row Row in interger form.
	 * @param col Col in interger form.
	 * @param p Which player it is.
	 * @param board Player's board.
	 * @return True if the location is valid and false otherwise.
	 */
	public boolean isValid(int row, int col, HumanPlayer p, char[][] board) {
		if (row == 8 || row == -1 || col == 8 || col == -1) {
			return false;
		} else if ((p.playerNum == 1 && row == 0) || (p.playerNum==2 && col == 0)) {
			return true;
		} else if (visited[row][col]==true) {
			return false;
		} else if (p.playerNum == 1 && board[row][col]=='2')  {
			return false;
		} else if (p.playerNum == 2 && board[row][col] == '1') {
			return false;
		} else if (p.playerNum == 1 && board[row][col]=='1')  {
			return false;
		} else if (p.playerNum == 2 && board[row][col]=='2')  {
			return false;
		}
		return true;
	}


	/** This method converts the row characters into integers.
	 * @param row Which is a character when passed in.
	 * @return The corresponding integer. 
	 */
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


	/** This method converts the row integers into characters.
	 * @param row Which is a integer when passed in.
	 * @return The corresponding character. 
	 */
	public String convertToCh(int row) {
		if (row == 0)
			return "A";
		else if (row == 1) 
			return "B";
		else if (row == 2) 
			return "C";
		else if (row == 3) 
			return "D";
		else if (row == 4) 
			return "E";
		else if (row == 5) 
			return "F";
		else if (row == 6) 
			return "G";
		else if (row == 7) 
			return "H";
		else 
			return " ";
	}

	/** This method adds the path that will be printed out when there is a win.
	 * @param p Which player it is
	 */
	public void addToWinArray(HumanPlayer p, int count, int row, int col) {
		if (p.playerNum == 1) {
			p1WinArray[count] = convertToCh(row) + (col+1) + " "; //+ col;
		} else {
			p2WinArray[count] = convertToCh(row) + (col+1) + " "; //+ col;
		}
	}

	/** This method prints the winnning path.
	 * @param p Which player it is
	 */
	public void printWinArray(HumanPlayer p) {
		if (p.playerNum == 1) {
			for (int i = 0; i<p1WinArray.length; i++) {
				if (p1WinArray[i] != null)
					System.out.print(p1WinArray[i]);
			}
		} else {
			for (int i = 0; i<p2WinArray.length; i++) {
				if (p2WinArray[i] != null)
					System.out.print(p2WinArray[i]);
			}
		}
	}

	/** This method clears the path that turned out to be false. 
	 * @param p Which player it is
	 */
	public void clearWinArray(HumanPlayer p) {
		for (int i = 0; i<8; i++) {
			if (p.playerNum==1) {
				p1WinArray[i] = null;
			} else {
				p2WinArray[i] = null;
			}
		}
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

	public static void main (String args[]) {
		Network game = new Network();
		game.playGame();
	}

}
