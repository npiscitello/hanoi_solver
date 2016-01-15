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
		System.out.println("Initialized successfully! " + num_pieces + " pieces loaded onto the first peg.");
		System.out.println("The shortest possible solution requires " + calculateSteps(num_pieces) + " moves...");
	}
}