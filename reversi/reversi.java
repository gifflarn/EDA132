import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Pattern;

public class Reversi {
    private static String[][] board;
    private static Scanner s;
    private static final int size = 8;
    private static int argumentedSearchDepth;
    private static boolean playerCanMove = true, botCanMove = true, timeOut = false;
    private static long maxTime;

    public static void main(String[] args) {
        board = new String[size][size];
        argumentedSearchDepth = 10;
        if (args.length==0){
            System.out.println("No search depth given! Setting default to size-1 of the table. " +
                    "Next time add desired search depth as argument (int).");
            maxTime = 3000;
        } else {
            maxTime = Long.valueOf(args[0]);
        }
        setUp();
        draw();
        startGame();
    }

    private static void startGame() {
        System.out.println();
        playerMove();
        System.out.println("Game over! Winner: ");

    }

    private static HashMap<Coords, ArrayList<Coords>> getLegalMoves(String[][] currentBoard, String color, String oppColor) {
        HashMap<Coords, ArrayList<Coords>> legalMoves = new HashMap<Coords, ArrayList<Coords>>();
        for(int i = 0; i < size; i++){
            for(int j = 0; j < size; j++){
                ArrayList<Coords> currList = isLegalMove(currentBoard, new Coords(i,j), oppColor, color);
                if(!currList.isEmpty()){
                    legalMoves.put(new Coords(i,j), currList);
                }
            }
        }
        return legalMoves;
    }

    private static void compMove() {
    	long startTime = System.currentTimeMillis();
        HashMap<Coords, ArrayList<Coords>> legalMoves = getLegalMoves(board, "BLACK", "WHITE");

        // check to see if both can move, otherwise end game
        if (legalMoves.size()==0){
            if (!playerCanMove){
                gameOver();
            }
            botCanMove = false;
            System.out.println("Bot cannot move! Player turn...");
            playerMove();
        } else {
            botCanMove = true;
        }
        System.out.println("Possible bot moves: " + legalMoves.keySet());

        String[][] currentBoard = new String[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(board[i], 0, currentBoard[i], 0, board.length);
        }
        // start recursive depth search
        Coords calculatedBotMove = pruning(currentBoard, startTime, argumentedSearchDepth,
                Integer.MIN_VALUE, Integer.MAX_VALUE, "max");


        board[calculatedBotMove.y][calculatedBotMove.x] = "BLACK";
        for(Coords c : legalMoves.get(calculatedBotMove)){
            board[c.y][c.x] = "BLACK";
        }
        draw();
        System.out.println("Bot chose: " + calculatedBotMove);

        if(timeOut){
        	argumentedSearchDepth--;
        	System.out.println("Search Depth decreased to " + argumentedSearchDepth);
        	timeOut = false;
        } else {
        	argumentedSearchDepth++;
        	System.out.println("Search Depth increased to " + argumentedSearchDepth);
        }

        playerMove();

    }

    private static Coords pruning(String[][] newBoard, long startTime, int searchDepth, int alpha,
                                  int beta, String maxMin){
    	String[][] currentBoard = new String[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(newBoard[i], 0, currentBoard[i], 0, board.length);
        }
        Coords co = new Coords(-1, -1);
        if((System.currentTimeMillis() - startTime) > maxTime){
        	timeOut = true;
        }
        if (searchDepth == 0 || timeOut){
            co.value=boardValue(currentBoard, "BLACK");
            return co;
        }

        searchDepth--;
        if (maxMin.equals("max")){
            HashMap<Coords, ArrayList<Coords>> maxMoves = getLegalMoves(currentBoard, "BLACK", "WHITE");
            if (maxMoves.size() == 0){
                return pruning(currentBoard, startTime, searchDepth, alpha, beta, "min");
            }
            co.value=Integer.MIN_VALUE;
            
            for(Coords c : maxMoves.keySet()){
                Coords maximizedCoord = pruning(updateBoardFromCoords(currentBoard, c, maxMoves.get(c), "BLACK"),
                        startTime, searchDepth, alpha, beta, "min");
                if (maximizedCoord.value > co.value){
                    co = c;
                    co.value =maximizedCoord.value;
                    alpha = Math.max(alpha, co.value);
                    if (beta <= alpha) {
                        break;
                    }
                }
            }
            
            //Minimizing (using opponent's moves)
        } else {
            HashMap<Coords, ArrayList<Coords>> minMoves = getLegalMoves(currentBoard, "WHITE", "BLACK");
            if (minMoves.size() == 0){
                return pruning(currentBoard, startTime, searchDepth, alpha, beta, "max");
            }
            co.value=Integer.MAX_VALUE;
            for (Coords c : minMoves.keySet()){
                Coords minimizedCoord = pruning(updateBoardFromCoords(currentBoard, c, minMoves.get(c), "WHITE"),
                        startTime, searchDepth, alpha ,beta, "max");
                if (minimizedCoord.value < co.value){
                    co = c;
                    co.value = minimizedCoord.value;
                    beta = Math.min(beta, co.value);
                    if (beta <= alpha){
                        break;
                    }
                }
            }
        }
        return co;
    }

    private static int boardValue(String[][] currBoard, String color){
        int val = 0;
        for(int i = 0; i < board.length; i++){
            for (int e = 0; e < board.length; e++){
                if(currBoard[i][e].equals(color)){
                    val++;
                }
            }
        }
        return val;
    }

    private static void playerMove() {
        HashMap<Coords, ArrayList<Coords>> legalMoves = getLegalMoves(board, "WHITE", "BLACK");
        System.out.println("Legal moves are:" + legalMoves.keySet());
        if (legalMoves.size()==0){
            if (!botCanMove){
                gameOver();
            }
            System.out.println("Player cannot move! Bot turn...");
            playerCanMove=false;
            compMove();
        } else {
            playerCanMove=true;
        }
        System.out.println("Your current score: " + boardValue(board, "WHITE"));

        s = new Scanner(System.in);
        boolean accepted = false;
        String[] inputs;
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
        draw();
        compMove();

    }

    private static void gameOver(){
        int whiteVal = boardValue(board, "WHITE");
        int blackVal = boardValue(board, "BLACK");
        String winner = whiteVal > blackVal ? "WHITE" : "BLACK";
        System.out.println("WHITE: " + whiteVal);
        System.out.println("BLACK: " + blackVal);
        System.out.println("Game over! Winner: " + winner);
        System.exit(0);
    }

    private static String[][] updateBoardFromCoords(String[][] currentBoard, Coords currentCoord, ArrayList<Coords> coordList, String color){
    	String[][] newBoard = new String[board.length][board.length];
        for (int i = 0; i < board.length; i++) {
            System.arraycopy(currentBoard[i], 0, newBoard[i], 0, board.length);
        }
        newBoard[currentCoord.y][currentCoord.x] = color;
    	for(Coords c : coordList){
            newBoard[c.y][c.x] = color;
        }
        return newBoard;
    }

    private static ArrayList<Coords> isLegalMove(String[][] currentBoard, Coords coord,
                                                 String opponentColor, String color) {
        ArrayList<Coords> list = new ArrayList<Coords>();
        if (!currentBoard[coord.y][coord.x].equals("EMPTY")) {
            return list;
        }
        checkAxis(currentBoard, coord.x + 1, coord.y, opponentColor, color, "E", list, true);
        checkAxis(currentBoard, coord.x + 1, coord.y + 1, opponentColor, color, "SE", list,
                true);
        checkAxis(currentBoard, coord.x, coord.y + 1, opponentColor, color, "S", list, true);
        checkAxis(currentBoard, coord.x - 1, coord.y + 1, opponentColor, color, "SW", list,
                true);
        checkAxis(currentBoard, coord.x - 1, coord.y, opponentColor, color, "W", list, true);
        checkAxis(currentBoard, coord.x - 1, coord.y - 1, opponentColor, color, "NW", list,
                true);
        checkAxis(currentBoard, coord.x, coord.y - 1, opponentColor, color, "N", list, true);
        checkAxis(currentBoard, coord.x + 1, coord.y-1, opponentColor, color, "NE", list, true);

        return list;

    }

    private static boolean checkAxis(String[][] currentBoard, int x, int y, String opponentColor,
                                     String color, String direction, ArrayList<Coords> list,
                                     boolean first) {

        if (x == 8 || x == -1 || y == 8
                || y == -1 || currentBoard[y][x].equals("EMPTY")) {
            return false;
        } else if (currentBoard[y][x].equals(color) && !first) {
            return true;
        }
        switch (direction) {
            case ("E"):
                if (currentBoard[y][x].equals(opponentColor)
                        && checkAxis(currentBoard, x + 1, y, opponentColor, color, "E", list,
                        false)) {
                    list.add(new Coords(x, y));
                    return true;
                } else {
                    return false;
                }
            case ("SE"):
                if (currentBoard[y][x].equals(opponentColor)
                        && checkAxis(currentBoard, x + 1, y + 1, opponentColor, color, "SE",
                        list, false)) {
                    list.add(new Coords(x, y));
                    return true;
                } else {
                    return false;
                }
            case ("S"):
                if (currentBoard[y][x].equals(opponentColor)
                        && checkAxis(currentBoard, x, y + 1, opponentColor, color, "S", list,
                        false)) {
                    list.add(new Coords(x, y));
                    return true;
                } else {
                    return false;
                }
            case ("SW"):
                if (currentBoard[y][x].equals(opponentColor)
                        && checkAxis(currentBoard, x - 1, y + 1, opponentColor, color, "SW",
                        list, false)) {
                    list.add(new Coords(x, y));
                    return true;
                } else {
                    return false;
                }
            case ("W"):
                if (currentBoard[y][x].equals(opponentColor)
                        && checkAxis(currentBoard, x - 1, y, opponentColor, color, "W", list,
                        false)) {
                    list.add(new Coords(x, y));
                    return true;
                } else {
                    return false;
                }
            case ("NW"):
                if (currentBoard[y][x].equals(opponentColor)
                        && checkAxis(currentBoard, x - 1, y - 1, opponentColor, color, "NW",
                        list, false)) {
                    list.add(new Coords(x, y));
                    return true;
                } else {
                    return false;
                }
            case ("N"):
                if (currentBoard[y][x].equals(opponentColor)
                        && checkAxis(currentBoard, x, y - 1, opponentColor, color, "N", list,
                        false)) {
                    list.add(new Coords(x, y));
                    return true;
                } else {
                    return false;
                }
            case ("NE"):
                if (currentBoard[y][x].equals(opponentColor)
                        && checkAxis(currentBoard, x + 1, y - 1, opponentColor, color, "NE",
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
    
    private static void draw(String[][] currentBoard) {
        for (int i = -1; i < size; i++) {
            if (i == -1) {
                System.out.print("  | a | b | c | d | e | f | g | h |\n");
                continue;
            }
            for (int j = 0; j < size; j++) {
                if (j == 0) {
                    System.out.print((i + 1) + " |");
                }
                if (currentBoard[i][j].equals("EMPTY")) {
                    System.out.print("   |");
                } else if (currentBoard[i][j].equals("WHITE")) {
                    System.out.print(" W |");
                } else if (currentBoard[i][j].equals("BLACK")) {
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