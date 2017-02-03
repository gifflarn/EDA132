import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class reversi {
	static String[][] board;
	static Scanner s;
	private static final int size = 8;

	public static void main(String[] args) {
		board = new String[size][size];
		setUp();
		draw();
		startGame();
	}

	private static void startGame() {
		while (true) {
			playerMove();
			draw();
			compMove();
			draw();
		}

	}

	private static void compMove() {
		// TODO Auto-generated method stub
		
	}

	private static void playerMove() {
		s = new Scanner(System.in);
		boolean accepted = false;
		String[] inputs = null;
		int inputA = 0;
		int inputB = 0;
		while (!accepted) {
			String input = s.nextLine();
			inputs = input.split("");
			if (input.length() != 2 || !Pattern.matches("[a-hA-H]", inputs[0])
					|| !Pattern.matches("[1-8]", inputs[1])) {
				System.out.println("Faulty input, try again");
			} else {
				inputB = inputs[0].toLowerCase().charAt(0) - 'a';
				inputA = Integer.parseInt(inputs[1])-1;
				Coords coord = new Coords(inputA, inputB);
				if (isLegalMove(coord, "BLACK", "WHITE")) {
					accepted = true;
				} else {
					System.out.println("Illegal move, try again");
				}
			}
		}

		board[inputA][inputB] = "WHITE";

	}

	private static boolean isLegalMove(Coords coord, String opponentCol, String col) {
		if(!board[coord.x][coord.y].equals("EMPTY")){
			return false;
		}
		checkAxis(coord.x+1, coord.y, opponentCol, col, "E");
		checkAxis(coord.x+1, coord.y+1, opponentCol, col, "SE");
		checkAxis(coord.x, coord.y+1, opponentCol, col, "S");
		checkAxis(coord.x-1, coord.y+1, opponentCol, col, "SW");
		checkAxis(coord.x-1, coord.y, opponentCol, col, "W");
		checkAxis(coord.x-1, coord.y-1, opponentCol, col, "NW");
		checkAxis(coord.x, coord.y-1, opponentCol, col, "N");
		checkAxis(coord.x+1, coord.y, opponentCol, col, "NE");
		
		return true;

	}

	private static boolean checkAxis(int x, int y, String opponentCol,
			String col, String direction) {
		if(board[x][y].equals("EMPTY") || x == 8 || x == -1 || y == 8 || y == -1){
			return false;
		} else if(board[x][y].equals(col)){
			return true;
		}
		switch(direction){
		case("E"): return board[x][y] == opponentCol && checkAxis(x+1, y, opponentCol, col, "E");
		case("SE"): return board[x][y] == opponentCol && checkAxis(x+1, y+1, opponentCol, col, "E");
		case("S"): return board[x][y] == opponentCol && checkAxis(x, y+1, opponentCol, col, "E");
		case("SW"): return board[x][y] == opponentCol && checkAxis(x-1, y+1, opponentCol, col, "E");
		case("W"): return board[x][y] == opponentCol && checkAxis(x-1, y, opponentCol, col, "E");
		case("NW"): return board[x][y] == opponentCol && checkAxis(x-1, y-1, opponentCol, col, "E");
		case("N"): return board[x][y] == opponentCol && checkAxis(x, y-1, opponentCol, col, "E");
		case("NE"): return board[x][y] == opponentCol && checkAxis(x+1, y-1, opponentCol, col, "E");
		}
		return false;
		
	}

	private static void draw() {
		for (int i = -1; i < size; i++) {
			if (i == -1) {
				System.out.print("  | a | b | c | d | e | f | g | h |\n");
				continue;
			}
			for (int j = 0; j < size; j++) {
				if (j == 0) {
					System.out.print((i + 1) + " |");
				}
				if (board[i][j].equals("EMPTY")) {
					System.out.print("   |");
				} else if (board[i][j].equals("WHITE")) {
					System.out.print(" W |");
				} else if (board[i][j].equals("BLACK")) {
					System.out.print(" B |");
				} 
			}
			System.out.println("");
		}

	}

	private static void setUp() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = "EMPTY";
			}
		}
		board[3][3] = "WHITE";
		board[4][4] = "WHITE";
		board[3][4] = "BLACK";
		board[4][3] = "BLACK";
	}
	
	
}

