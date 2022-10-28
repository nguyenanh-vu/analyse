package analyse.exceptions;

public class NotFoundException extends Exception {
	private static final long serialVersionUID = 3081170288725760352L;

	public NotFoundException(String errorMessage) {
		super(errorMessage);
	}
}
