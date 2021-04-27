import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**************************************************************
 * Squares are labeled:
 * 0 1 2
 * 3 4 5
 * 6 7 8
 ******************************************************************/


public class Variant {
	static final Scanner CONSOLE = new Scanner(System.in);
	
	public static void main (String args[]) {
		int numTrials = 1000;
		int firstPlayerWins = 0;
		int secondPlayerWins = 0;
		for (int i=0; i<numTrials; i++) {
			if (playGame()==1) {
				firstPlayerWins++;
			}
			else {
				secondPlayerWins++;
			}
			System.out.println("First player has won " + firstPlayerWins + " times");
			System.out.println("Second player has won " + secondPlayerWins + " times");
		}
		System.out.println("First player wins " + (double) firstPlayerWins/numTrials);
		System.out.println("Second player wins " + (double)secondPlayerWins/numTrials);
	}
	
	public static int playGame() {
//		int[] board = {2, 0, 1, 0, 1, 1, 0, 0, 2};
//		System.out.println(getValidMovesPlacing(board, 1));
//		printBoard2D(board);
//		System.out.println();
//		board = makeMovePlacing (board, 2, myPlayerPlacing(board, 2));
//		printBoard2D(board);
//		System.out.println();
//		return 0;
		System.out.println("Note: Squares are labeled:");
		System.out.println("0 1 2");
		System.out.println("3 4 5");
		System.out.println("6 7 8");
		System.out.println();
		int[] board = {0, 0, 0, 0, 0, 0, 0, 0, 0};
		printBoard2D(board);
		System.out.println();
		int status = checkWin(board);
		System.out.println("Placing...");
		for (int i=1; i<4; i++) {
			board = makeMovePlacing(board, 1, randomPlayerPlacing(board, 1)); // 0.8692 if i'm first player
			printBoard2D(board);
			System.out.println();
			status = checkWin(board);
			if (status!=0) {
				System.out.println("Player " + status + " won!");
				return status;	
			}
			board = makeMovePlacing(board, 2, myPlayerPlacing(board, 2)); // 0.7383 if i'm second player
			printBoard2D(board);
			System.out.println();
			status = checkWin(board);
			if (status!=0) {
				System.out.println("Player " + status + " won!");
				return status;	
			}
		}
		System.out.println("Moving...");
		while (true) {
			board = makeMoveShifting(board, 1, randomPlayerShifting(board, 1));
			printBoard2D(board);
			System.out.println();
			status = checkWin(board);
			if (status!=0) {
				System.out.println("Player " + status + " won!");
				return status;
			}
			board = makeMoveShifting(board, 2, myPlayerShifting(board, 2));
			printBoard2D(board);
			System.out.println();
			status = checkWin(board);
			if (status!=0) {
				System.out.println("Player " + status + " won!");
				return status;
			}
		}
	}
	
	
	
	
	/********************************************************************
	 * 
	 * Methods relating to shifting/moving pieces (second stage of the game)
	 * 
	 *******************************************************************/
	
	// Returns a 2-digit integer representing the (random) move, chosen among all possible legal moves
	public static int randomPlayerShifting (int[] board, int player) {
		ArrayList<Integer> possibleMoves = getValidMovesShifting(board, player);
	    int randomIndex = (int)(Math.random() * (possibleMoves.size()));
		return possibleMoves.get(randomIndex);
	}
	
	public static int userPlayerShifting (int[] board, int player) {
		System.out.println("You are player " + player + ". Please enter a 2-digit number to shift your piece");
		while (true) {
			int move = CONSOLE.nextInt();
			CONSOLE.nextLine();
			if (isValidMoveShifting(board, player, move)) {
				return move;
			}
			else {
				System.out.println("Try again");
			}
		}
	}
	
	public static int myPlayerShifting (int[] board, int player) {
		double result = myPlayerShiftingHelper(board, player);
		System.out.println(result);
		return (int) Math.floor(result);
	}
	
	public static double myPlayerShiftingHelper (int[] board, int player) {
		ArrayList<Integer> myMoves = getValidMovesShifting(board, player);
		double bestMoveRated = 0;
		for (int i=0; i<myMoves.size(); i++) {
			double winProbability = 0;
			int[] boardTemp = makeMoveShifting(board, player, myMoves.get(i));
			if (checkWin(boardTemp)==player) {
				return myMoves.get(i) + 0.1;
			}
			else {
				ArrayList<Integer> yourMoves = getValidMovesShifting(boardTemp, player%2+1);
				for (int j=0; j<yourMoves.size(); j++) {
					int[] boardTempTemp = makeMoveShifting(boardTemp, player%2+1, yourMoves.get(j));
					
					// If the other player can force a win (i.e. if this move causes you to lose), the move is deemed horrible
					if (checkWin(boardTempTemp)==player%2+1) { 
						 winProbability = 0;
						 break;
					}
					winProbability = winProbability + 0.5; // Default if we're in a stalemate // (myPlayerShiftingHelper(boardTempTemp, player)%1)*10; 
				}
				winProbability = winProbability/yourMoves.size();
			}
			if (winProbability >= (bestMoveRated%1)*10) {
				bestMoveRated = myMoves.get(i) + 0.1*winProbability;	
			}
		}
		return bestMoveRated;
	}
	
	public static ArrayList<Integer> getValidMovesShifting (int[] board, int player){
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		for (int i=0; i<88; i++) {
			if (isValidMoveShifting(board, player, i)) {
				possibleMoves.add(i);
			}
		}
		return possibleMoves;
	}
	
	// Determines if "player" can make "move" on "board," where "move" is a two digit integer
	// with both digits 0-8
	public static boolean isValidMoveShifting (int[] board, int player, int move) {
		if (player!=1 && player!=2) {
			return false;
		}
		if (move>88 || move<0 || move%11==0) {
			return false;
		}
		int newSquare = move%10;
		int oldSquare = (move-move%10)/10;
		if (newSquare>8 || oldSquare>8) {
			return false;
		}
		if (board[oldSquare]!=player) {
			return false;
		}
		if (board[newSquare]!=0) {
			return false;
		}
		return true;
	}
	
	// Player = 1 or 2, move is a 2-digit number composed of the square the piece is moved from
	// and the square the piece is moved to (e.g. 08 corresponds to moving a piece from 0 to 8)
	public static int[] makeMoveShifting (int[] board, int player, int move){
		if (!isValidMoveShifting(board, player, move)) {
			return null;
		}
		int newSquare = move%10;
		int oldSquare = (move-move%10)/10;
		int[] newBoard = board.clone();
		newBoard[oldSquare] = 0;
		newBoard[newSquare] = player;
		return newBoard;
	}
	
	
	/********************************************************************
	 * 
	 * Methods relating to placing pieces (first stage of the game)
	 * 
	 *******************************************************************/
	
	// Chooses randomly among all possible valid moves
	public static int randomPlayerPlacing (int[] board, int player) {
		ArrayList<Integer> possibleMoves = getValidMovesPlacing(board, player);
	    int randomIndex = (int)(Math.random() * (possibleMoves.size()));
		return possibleMoves.get(randomIndex);
	}
	
	public static int userPlayerPlacing (int[] board, int player) {
		System.out.println("You are player " + player + ". Please enter a location to place your piece");
		while (true) {
			int move = CONSOLE.nextInt();
			CONSOLE.nextLine();
			if (isValidMovePlacing(board, player, move)) {
				return move;
			}
			else {
				System.out.println("Try again");
			}
		}
	}
	
	// 
	// \todo{assume the other player is a bit smarter than random?}
	// 
	public static int myPlayerPlacing (int[] board, int player) {
		double result = myPlayerPlacingHelper(board, player);
		System.out.println(result);
		System.out.println((int) Math.floor(result));
		return (int) Math.floor(result);
	}
	
	// For some reason this sometimes returns an invalid move?? (results in a null
	// pointer exception in playGame() \todo{}
	
	// Always happens as second player
	// During the placing stage given the board
	// 2 0 1
	// 0 1 1
	// 0 0 2
	// It tries to move at spot 0
	
	// Encodes a move and the probability for that move by using
	// move+0.1*probability
	// For example, if move 3 has probability 0.75 of winning, this would be 03.075
	public static double myPlayerPlacingHelper (int[] board, int player) {
		ArrayList<Integer> myMoves = getValidMovesPlacing(board, player);
		double bestMoveRated = 0;
		for (int i=0; i<myMoves.size(); i++) {
			double winProbability = 0;
			int[] boardTemp = makeMovePlacing(board, player, myMoves.get(i));
			if (checkWin(boardTemp)==player) {
				return myMoves.get(i) + 0.1;
			}
			if (checkIsPlacingDone(boardTemp)) {
				winProbability = 1-(myPlayerShiftingHelper(boardTemp, player%2+1)%1)*10; // Default, \todo{should probably be replaced with a value from myPlayerShiftingHelper}
			}
			else {
				ArrayList<Integer> yourMoves = getValidMovesPlacing(boardTemp, player%2+1);
				for (int j=0; j<yourMoves.size(); j++) {
					int[] boardTempTemp = makeMovePlacing(boardTemp, player%2+1, yourMoves.get(j));
					
					if (checkIsPlacingDone(boardTempTemp)) {
						winProbability = winProbability + (myPlayerShiftingHelper(boardTempTemp, player)%1)*10; // Default, \todo{should probably be replaced with a value from myPlayerShiftingHelper}
					}
					// If the other player can force a win (i.e. if this move causes you to lose), the move is deemed horrible
					else if (checkWin(boardTempTemp)==player%2+1) { 
						 winProbability = 0;
						 break;
					}
					else {
						winProbability = winProbability + (myPlayerPlacingHelper(boardTempTemp, player)%1)*10; 
					}
				}
				winProbability = winProbability/yourMoves.size();
			}
			// To correct for small errors in which winProbability is very slightly 
			// less than 0, meaning that bestMoveRated never changes
			winProbability = winProbability + 0.00001; 
			if (winProbability >= (bestMoveRated%1)*10) {
				bestMoveRated = myMoves.get(i) + 0.1*winProbability;	
			}
		}
		return bestMoveRated;
	}
	
	// Gets all valid moves for "player," disregarding whether the placing stage should be done
	public static ArrayList<Integer> getValidMovesPlacing (int[] board, int player){
		ArrayList<Integer> possibleMoves = new ArrayList<Integer>();
		for (int i=0; i<9; i++) {
			if (isValidMovePlacing(board, player, i)) {
				possibleMoves.add(i);
			}
		}
		return possibleMoves;
	}
	
	// "Move" is just an integer from 0-8 to place a mark for "player" at "move"
	public static boolean isValidMovePlacing (int[] board, int player, int move) {
		if (move<0 || move>8) {
			return false;
		}
		if (board[move]!=0) {
			return false;
		}
		return true;
	}
	
	// Makes a "move" for "player" and returns the new board
	public static int[] makeMovePlacing (int[] board, int player, int move) {
		if (!isValidMovePlacing(board, player, move)) {
			return null;
		}
		int[] newBoard = board.clone();
		newBoard[move] = player;
		return newBoard;
	}	
	
	
	/********************************************************************
	 * 
	 * Miscellaneous methods
	 * 
	 *******************************************************************/
	
	// Returns 0 if no one won, returns 1/2 if one player won
	// Assumes a valid board, with only 0's (empty), 1's, and 2's
	public static int checkWin(int[] board) {
		for (int i=1; i<3; i++) {
			if (board[0]==i && board[1]==i && board[2]==i) {
				return i;
			}
			if (board[3]==i && board[4]==i && board[5]==i) {
				return i;
			}
			if (board[6]==i && board[7]==i && board[8]==i) {
				return i;
			}
			if (board[0]==i && board[3]==i && board[6]==i) {
				return i;
			}
			if (board[1]==i && board[4]==i && board[7]==i) {
				return i;
			}
			if (board[2]==i && board[5]==i && board[8]==i) {
				return i;
			}
			if (board[0]==i && board[4]==i && board[8]==i) {
				return i;
			}
			if (board[2]==i && board[4]==i && board[6]==i) {
				return i;
			}
		}
		return 0;
	}
	
	public static boolean checkIsPlacingDone (int[] board) {
		int numOnes = 0;
		int numTwos = 0;
		for (int i=0; i<board.length; i++) {
			if (board[i]==1) {
				numOnes = numOnes + 1;
			}
			if (board[i]==2) {
				numTwos = numTwos + 1;
			}
		}
		return (numOnes==3) && (numTwos==3);
	}
		
	// Prints 1D array of length 8 in a 3x3 grid
	public static void printBoard2D (int[] board) {
		for (int i=0; i<3; i++) {
			System.out.print(board[3*i] + " " + board[3*i+1] + " " + board[3*i+2]);
			System.out.println();
		}
	}
	
	public static void printArr (int[] arr) {
		for (int i=0; i<arr.length; i++) {
			System.out.print(arr[i] + " ");
		}
		System.out.println();
	}
}
