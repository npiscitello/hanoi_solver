package hanoi_solver;

import java.util.ArrayList;

public class Main {
	
	// constants
	private final static String USAGE = "Usage: java -jar hanoi_solver.jar [NUMBER OF PIECES]";
	private final static int NUM_PEGS = 3;
	private final static int PRINT_THRESH_DEFAULT = 100;
	
	// variables
	private static int print_thresh = PRINT_THRESH_DEFAULT;
	private static int num_pieces;
	private static ArrayList<Peg> pegs = new ArrayList<>();
	private static ArrayList<Piece> pieces = new ArrayList<>();
	
	// calculate the lowest number of steps required for a solution
	private static long calculateSteps(int num_pieces) {
		if(num_pieces == 1) {
			return(1);
		} else {
			return(2 * calculateSteps(num_pieces - 1) + 1);
		}
	}
	
	// calculate the next destination (used exclusively in movePiece())
	private static int calculateNextDestination(int location, int destination) {
		int new_destination = destination;
		while(new_destination == location || new_destination == destination) {
			new_destination++;
			if(new_destination == NUM_PEGS) {
				new_destination = 0;
			}
		}
		return new_destination;
	}
	
	// try to move the largest piece to the correct peg
	private static void movePiece(int piece_index, int destination_index) {
		Piece piece = pieces.get(piece_index);
		Peg source = piece.getPeg();
		Peg destination = pegs.get(destination_index);
		// loop the try/catch statement until it completes successfully
		while(true) {
			try {
				// try to move the piece to the requested peg
				source.remove(piece);
				destination.add(piece);
				System.out.println("  piece " + (piece_index + 1) + " to peg " + (char)('A' + destination_index));
				// if the piece just moved was not the smallest piece, move the next smallest piece on top of it
				if(piece.getId() > 0) {
					movePiece(piece_index - 1, piece.getPeg().getId());
				}
				break;
			} catch (BadMoveException e) {
				// if it doesn't work, try moving the next smallest piece to the other free peg
				movePiece(piece_index - 1, calculateNextDestination(source.getId(), destination_index));
			}
		}
	}

	public static void main(String[] args) {
		
		// test the number of supplied arguments
		if(args.length != 1 && args.length != 2) {
			System.err.println("\n" + USAGE);
			return;
		}
		
		// test the type of the supplied argument(s)
		try {
			if(args.length == 1) {
				num_pieces = Integer.parseInt(args[0]);
			} else if(args.length == 2) {
				print_thresh = Integer.parseInt(args[0]);
				num_pieces = Integer.parseInt(args[1]);
			}
			
		} catch(NumberFormatException e) {
			System.err.println("\n" + USAGE + "\nDid you input an integer?");
			return;
		}

		// create arrays of peg and piece objects
		for(int i = 0; i < NUM_PEGS; i++) {
			pegs.add(new Peg(i));
		}
		for(int i = 0; i < num_pieces; i++) {
			pieces.add(new Piece(i));
		}
		
		// add all the pieces to the first peg
		for(int i = pieces.size() - 1; i >= 0; i--) {
			try {
				pegs.get(0).add(pieces.get(i));
			} catch (BadMoveException e) {
				e.printStackTrace();
			}
		}
		
		long num_steps = calculateSteps(num_pieces);
		if(num_steps <= print_thresh) {
			// if the solution is small enough to be printed...
			System.out.println("\nPiece 1 is the smallest and peg A is the farthest to the left.");
			if(num_pieces > 1) {
				System.out.println("Initialization successful! Load " + num_pieces + " pieces onto peg A.\n");
				System.out.println("To play a perfect game, perform these " + num_steps + " moves:");
			} else {
				System.out.println("Initialization successful! Load the piece onto peg A.\n");
				System.out.println("To play a perfect game, perform this move:");
			}
			
			// start the recursion: try to move the biggest piece to the last peg
			movePiece(pieces.size() - 1, pegs.size() - 1);
			
			// success!
			System.out.println("\nYou have completed the Tower of Hanoi! Revel in your glory!\n");
			return;
		} else {
			// if the solution is too big to be printed...
			System.out.println("To play a perfect game, " + num_steps + " steps must be performed.");
			System.out.println("Printing of the instruction set has been disabled due to its length.");
			if(print_thresh == PRINT_THRESH_DEFAULT) {
				System.out.println("To override the default threshold (" + PRINT_THRESH_DEFAULT + " steps), use:");
				System.out.println("\t'java -jar hanoi_solver.jar [THRESHOLD] [NUMBER OF PIECES]");
			}
			return;
		}
	}
}