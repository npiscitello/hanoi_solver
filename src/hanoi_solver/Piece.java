package hanoi_solver;

public class Piece {
	
	// variables
	private int id;
	private Peg peg;
	
	// constructor
	public Piece(int serial_id) {
		id = serial_id;
	}
	
	// return the id
	public int getId() {
		return(id);
	}
	
	// tell the piece what peg its on
	public void setPeg(Peg on_peg) {
		peg = on_peg;
	}
	
	// return which peg the piece is on
	public Peg getPeg() {
		return(peg);
	}

}
