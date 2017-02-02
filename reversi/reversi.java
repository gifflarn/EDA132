import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class reversi {
	static Boolean[][] board;
	static Scanner s;
	private static final int size = 8;
	private static final Boolean WHITE = Boolean.TRUE, BLACK = Boolean.FALSE;

	public static void main(String[] args) {
		board = new Boolean[size][size];
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
				inputB = inputs[0].toLowerCase().charAt(0) - 'a' + 1;
				inputA = Integer.parseInt(inputs[1]);
				if (isLegalMove(inputA, inputB, BLACK, WHITE)) {
					accepted = true;
				} else {
					System.out.println("Illegal move, try again");
				}
			}
		}

		board[inputA-1][inputB-1] = WHITE;

	}

	private static boolean isLegalMove(int inputA, int inputB, Boolean opponentCol, Boolean col) {
		if (!(board[inputA + 1][inputB] == opponentCol
				|| board[inputA + 1][inputB + 1] == opponentCol
				|| board[inputA + 1][inputB - 1] == opponentCol
				|| board[inputA - 1][inputB] == opponentCol
				|| board[inputA - 1][inputB + 1] == opponentCol
				|| board[inputA - 1][inputB - 1] == opponentCol
				|| board[inputA][inputB + 1] == opponentCol || board[inputA][inputB - 1] == opponentCol)) {
			return false;
		}
//		int counter = 0;
//		for(int i = 0; i < size; i++){
//			if(board[inputA][i] == col){
//				counter+= Math.abs(inputA-i)-1;
//			}
//		}
		return true;

	}

	private static void draw() {
		try {
			Runtime.getRuntime().exec("cls");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = -1; i < size; i++) {
			if (i == -1) {
				System.out.print("  | a | b | c | d | e | f | g | h |\n");
				continue;
			}
			for (int j = 0; j < size; j++) {
				if (j == 0) {
					System.out.print((i + 1) + " |");
				}
				if (board[i][j] == null) {
					System.out.print("   |");
				} else if (board[i][j].equals(WHITE)) {
					System.out.print(" W |");
				} else if (board[i][j].equals(BLACK)) {
					System.out.print(" B |");
				} else {
				}
			}
			System.out.println("");
		}

	}

	private static void setUp() {
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				board[i][j] = null;
			}
		}
		board[3][3] = WHITE;
		board[4][4] = WHITE;
		board[3][4] = BLACK;
		board[4][3] = BLACK;
	}
}
