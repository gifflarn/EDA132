import java.util.ArrayList;
import java.util.HashMap;
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
		HashMap<Coords, ArrayList<Coords>> legalMoves;
		while (true) {
			legalMoves = getLegalMoves("WHITE", "BLACK");
			System.out.println(legalMoves);
			playerMove(legalMoves);
			draw();
			legalMoves.clear();
			legalMoves = getLegalMoves("BLACK", "WHITE");
			compMove(legalMoves);
			draw();
			legalMoves.clear();
		}

	}

	private static HashMap<Coords, ArrayList<Coords>> getLegalMoves(String color, String oppColor) {
		HashMap<Coords, ArrayList<Coords>> legalMoves = new HashMap<Coords, ArrayList<Coords>>();
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				ArrayList<Coords> currList = isLegalMove(new Coords(i,j), oppColor, color);
				if(!currList.isEmpty()){
					legalMoves.put(new Coords(i,j), currList);
				}
			}
		}
		return legalMoves;
	}

	private static void compMove(HashMap<Coords, ArrayList<Coords>> legalMoves) {
		// TODO Auto-generated method stub

	}

	private static void playerMove(HashMap<Coords, ArrayList<Coords>> legalMoves) {
		s = new Scanner(System.in);
		boolean accepted = false;
		String[] inputs = null;
		int inputA = 0;
		int inputB = 0;
		Coords coord = null;
		while (!accepted) {
			String input = s.nextLine();
			inputs = input.split("");
			if (input.length() != 2 || !Pattern.matches("[a-hA-H]", inputs[0])
					|| !Pattern.matches("[1-8]", inputs[1])) {
				System.out.println("Faulty input, try again");
			} else {
				inputA = inputs[0].toLowerCase().charAt(0) - 'a';
				inputB = Integer.parseInt(inputs[1])-1;
				coord = new Coords(inputA, inputB);
				System.out.println(coord);
				if (legalMoves.containsKey(coord)) {
					accepted = true;
				} else {
					System.out.println("Illegal move, try again");
				}
			}
		}

		board[inputB][inputA] = "WHITE";
		for(Coords c : legalMoves.get(coord)){
			board[c.y][c.x] = "WHITE"; 
		}

	}

	private static ArrayList<Coords> isLegalMove(Coords coord,
			String opponentColor, String color) {
		ArrayList<Coords> list = new ArrayList<Coords>();
		if (!board[coord.y][coord.x].equals("EMPTY")) {
			return list;
		}
		checkAxis(coord.x + 1, coord.y, opponentColor, color, "E", list, true);
		checkAxis(coord.x + 1, coord.y + 1, opponentColor, color, "SE", list,
				true);
		checkAxis(coord.x, coord.y + 1, opponentColor, color, "S", list, true);
		checkAxis(coord.x - 1, coord.y + 1, opponentColor, color, "SW", list,
				true);
		checkAxis(coord.x - 1, coord.y, opponentColor, color, "W", list, true);
		checkAxis(coord.x - 1, coord.y - 1, opponentColor, color, "NW", list,
				true);
		checkAxis(coord.x, coord.y - 1, opponentColor, color, "N", list, true);
		checkAxis(coord.x + 1, coord.y, opponentColor, color, "NE", list, true);

		return list;

	}

	private static boolean checkAxis(int x, int y, String opponentColor,
			String color, String direction, ArrayList<Coords> list,
			boolean first) {

		if (x == 8 || x == -1 || y == 8
				|| y == -1 || board[y][x].equals("EMPTY")) {
			return false;
		} else if (board[y][x].equals(color) && !first) {
			return true;
		}
		switch (direction) {
		case ("E"):
			if (board[y][x].equals(opponentColor)
					&& checkAxis(x + 1, y, opponentColor, color, "E", list,
							false)) {
				list.add(new Coords(x, y));
				return true;
			} else {
				return false;
			}
		case ("SE"):
			if (board[y][x].equals(opponentColor)
					&& checkAxis(x + 1, y + 1, opponentColor, color, "SE",
							list, false)) {
				list.add(new Coords(x, y));
				return true;
			} else {
				return false;
			}
		case ("S"):
			if (board[y][x].equals(opponentColor)
					&& checkAxis(x, y + 1, opponentColor, color, "S", list,
							false)) {
				list.add(new Coords(x, y));
				return true;
			} else {
				return false;
			}
		case ("SW"):
			if (board[y][x].equals(opponentColor)
					&& checkAxis(x - 1, y + 1, opponentColor, color, "SW",
							list, false)) {
				list.add(new Coords(x, y));
				return true;
			} else {
				return false;
			}
		case ("W"):
			if (board[y][x].equals(opponentColor)
					&& checkAxis(x - 1, y, opponentColor, color, "W", list,
							false)) {
				list.add(new Coords(x, y));
				return true;
			} else {
				return false;
			}
		case ("NW"):
			if (board[y][x].equals(opponentColor)
					&& checkAxis(x - 1, y - 1, opponentColor, color, "NW",
							list, false)) {
				list.add(new Coords(x, y));
				return true;
			} else {
				return false;
			}
		case ("N"):
			if (board[y][x].equals(opponentColor)
					&& checkAxis(x, y - 1, opponentColor, color, "N", list,
							false)) {
				list.add(new Coords(x, y));
				return true;
			} else {
				return false;
			}
		case ("NE"):
			if (board[y][x].equals(opponentColor)
					&& checkAxis(x + 1, y - 1, opponentColor, color, "NE",
							list, false)) {
				list.add(new Coords(x, y));
				return true;
			} else {
				return false;
			}
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