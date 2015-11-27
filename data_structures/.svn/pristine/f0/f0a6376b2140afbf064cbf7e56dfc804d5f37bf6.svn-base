
public class blrobinsonBoard {
	public char[][] board; 
	
	public blrobinsonBoard() {
		board = new char[8][8];
		setBoard();
	}
	
	public void showBoard() {
		System.out.print("  ");
		for(int i = 1; i<=8; i++) {
			System.out.print(i + " ");
		}
		System.out.println();
		int r= 0;
		for (int row = 65; row<73; row++) {
			System.out.print((char)row + " ");
			for (int col = 0; col<8; col++) {
				System.out.print(board[r][col] + " ");
			}
			r++;
			System.out.println();
		}
	}
	
	public void setBoard() {
		for (int i = 0; i<8; i++) {
			for (int j = 0; j <8; j++)
				board[i][j] = '.';
		}
	}
	
	public char[][] getBoard() {
		return board;
	}
	
	public void add(int row, int col, char c) {
		board[row][col] = c;
	}
}
