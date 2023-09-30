package basic;

public class InvalidPinCountException extends RuntimeException {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidPinCountException(int pins) {
        super("Invalid number of pins. Please enter a number between 0 and 10,"
        		+ " with total number of pins per frame up to 10 (except for the last frame).");
    }

    public InvalidPinCountException(String message) {
        super(message);
    }
}
