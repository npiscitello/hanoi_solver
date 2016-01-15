package hanoi_solver;

import java.util.ArrayList;

public class Peg {
	
	// variables
	private int id;
	private ArrayList<Piece> contents = new ArrayList<>();

	// constructor
	public Peg(int serial_id) {
		id = serial_id;
	}
	
	// add a piece to the peg - throws a BadMoveException if it's not a valid add
	public void add(Piece piece) throws BadMoveException {
		if(contents.isEmpty()) {
			contents.add(piece);
		} else if(piece.getId() < contents.get(contents.size() - 1).getId()) {
			contents.add(piece);
		} else {
			throw new BadMoveException("Trying to put a large piece onto a smaller piece");
		}
	}
	
	// remove a piece from the peg - throws a BadMoveException if it's not a valid remove
	public void remove(Piece piece) throws BadMoveException {
		if(piece.getId() == contents.get(contents.size() - 1).getId()) {
			contents.remove(contents.size() - 1);
		} else {
			throw new BadMoveException("Trying to remove a piece that is not on the top of the stack");
		}
	}
	
	// return the ArrayList of pieces on the peg
	public ArrayList<Piece> getContents() {
		return contents;
	}
}
