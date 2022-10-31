package analyse.exceptions;

public class ConditionalThrow {
	
	public static void notEnoughArguments(String[] s, Integer expected) throws NotEnoughArgumentException {
		if (s.length < expected) {
			throw new NotEnoughArgumentException(String.join(" ", s), expected, s.length);
		}
	}
}
