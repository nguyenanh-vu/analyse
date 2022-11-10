package analyse.UI;

import java.util.List;

import analyse.exceptions.NotEnoughArgumentException;

public class UIUtils {
	
	public static void notEnoughArguments(String[] s, Integer expected) throws NotEnoughArgumentException {
		if (s.length < expected) {
			throw new NotEnoughArgumentException(String.join(" ", s), expected, s.length);
		}
	}
	
	public static void modeUnknown(String instead, List<String> s) {
		System.out.println(String
				.format("Mode \"%s\" unknown, expected %s", 
						instead,
						String.join("|", s)));
	}
	
	public static void parameterUnknown(String instead, List<String> s) {
		System.out.println(String
				.format("Parameter \"%s\" unknown, expected %s", 
						instead,
						String.join("|", s)));
	}
	
	public static String progressBar(Float percent, Integer size) {
		StringBuilder str = new StringBuilder();
		Integer i = (int) ((percent * size) / 100);
		for (int j = 0; j < size; j ++) {
			if (j < i) {
				str.append("#");
			} else {
				str.append(".");
			}
		}
		return "[" + str.toString() + "]";
	}
}
