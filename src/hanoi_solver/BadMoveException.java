package hanoi_solver;

@SuppressWarnings("serial")
public class BadMoveException extends Exception {

	public BadMoveException() {
		super();
	}
	
	public BadMoveException(String message) {
		super(message);
	}
	
	public BadMoveException(Throwable cause) {
		super(cause);
	}
	
	public BadMoveException(String message, Throwable cause) {
		super(message, cause);
	}
}
