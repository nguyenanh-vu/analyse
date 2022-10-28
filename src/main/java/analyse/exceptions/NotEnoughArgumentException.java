package analyse.exceptions;

public class NotEnoughArgumentException extends Exception {
	private static final long serialVersionUID = 913820398760353023L;

	public NotEnoughArgumentException(String s, int expected, int given) {
		super(String.format("%s : Not enough arguments, %s expected, %s given", 
				s, String.valueOf(expected), String.valueOf(given)));
	} 
}
