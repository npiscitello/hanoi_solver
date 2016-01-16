package hanoi_solver;

import java.util.ArrayList;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	
	// constants
	private final static String USAGE = "java -jar hanoi_solver.jar";
	private final static int NUM_PEGS = 3;
	private final static int PRINT_THRESH_DEFAULT = 100;
	private final static String PIECE_FLAG = "p";
	private final static String THRESH_FLAG = "t";
	
	// variables
	private static int print_thresh = PRINT_THRESH_DEFAULT;
	private static int num_pieces;
	private static ArrayList<Peg> pegs = new ArrayList<>();
	private static ArrayList<Piece> pieces = new ArrayList<>();
	
	// objects
	private static Options options = new Options();
	private static CommandLineParser parser = new DefaultParser();
	
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

	// main program execution
	public static void main(String[] args) {
		
		// receive and parse command line options
		Option pieces_opt = new Option(PIECE_FLAG, true, "number of pieces");
		pieces_opt.setRequired(true);
		options.addOption(pieces_opt);
		options.addOption(THRESH_FLAG, true, "instruction set length threshold");
		try {
			CommandLine cli = parser.parse(options, args);
			num_pieces = Integer.parseInt(cli.getOptionValue(PIECE_FLAG));
			if(cli.getOptionValue(THRESH_FLAG) != null) {
				print_thresh = Integer.parseInt(cli.getOptionValue(THRESH_FLAG));
			}
		} catch (ParseException e) {
			HelpFormatter help = new HelpFormatter();
			help.printHelp(USAGE,
					   "Displays the optimal solution to a Tower of Hanoi puzzle.", 
					   options, "", true);
			return;
		} catch (NumberFormatException e) {
			HelpFormatter help = new HelpFormatter();
			help.printHelp(USAGE,
					   "Displays the optimal solution to a Tower of Hanoi puzzle.", 
					   options, 
					   "Did you supply valid integers?",
					   true);
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
			System.out.println("\nTo play a perfect game, " + num_steps + " steps must be performed.");
			System.out.println("Printing of the instruction set has been disabled due to its length.");
			if(print_thresh == PRINT_THRESH_DEFAULT) {
				System.out.println("To override the default threshold (" + PRINT_THRESH_DEFAULT + " steps), use:");
				System.out.println("\t'java -jar hanoi_solver.jar [THRESHOLD] [NUMBER OF PIECES]");
			}
			return;
		}
	}
}