package hanoi_solver;

import java.util.ArrayList;

public class Main {
	
	// constants
	private final static String USAGE = "Usage: java hanoi_solver [NUMBER OF PIECES]";
	private final static int NUM_PEGS = 3;
	
	// variables
	private static int num_pieces;
	private static ArrayList<Peg> pegs = new ArrayList<>();
	private static ArrayList<Piece> pieces = new ArrayList<>();
	
	// calculate the lowest number of steps required for a solution
	private static int calculateSteps(int num_pieces) {
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
		System.out.println("new_destination: " + new_destination);
		return new_destination;
	}
	
	// calculate the previous destination (used exclusively in movePiece())
	private static int calculatePrevDestination(int location, int destination) {
		int new_destination = destination;
		while(new_destination == location || new_destination == destination) {
			new_destination--;
			if(new_destination < 0) {
				new_destination = NUM_PEGS - 1;
			}
		}
		System.out.println("new_destination: " + new_destination);
		return new_destination;
	}
	
	// try to move the largest piece to the correct peg
	private static void movePiece(int piece_index, int destination_index) {
		Piece piece = pieces.get(piece_index);
		Peg destination = pegs.get(destination_index);
		try {
			// try to move the piece to the requested peg
			System.out.println("Trying to move piece " + piece_index + " to peg " + destination_index);
			piece.getPeg().remove(piece);
			destination.add(piece);
			System.out.println("Piece " + piece_index + " moved to peg " + destination_index);
		} catch (BadMoveException e) {
			// if it doesn't work, try moving the next smallest piece to the other free peg
			movePiece(piece_index - 1, calculateNextDestination(piece.getPeg().getId(), destination_index));
		}
	}

	public static void main(String[] args) {
		
		// test the number of supplied arguments
		if(args.length != 1) {
			System.err.println(USAGE);
			return;
		}
		
		// test the type of the supplied argument
		try {
			num_pieces = Integer.parseInt(args[0]);
		} catch(NumberFormatException e) {
			System.err.println(USAGE + "\nDid you supply an integer?");
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
		
		// status message
		System.out.println("Initialized successfully! " + num_pieces + " pieces were loaded onto the first peg.");
		System.out.println("The shortest possible solution requires " + calculateSteps(num_pieces) + " moves...");
		
		// start the recursion: try to move the biggest piece to the last peg
		movePiece(pieces.size() - 1, pegs.size() - 1);
	}
}