package analyse.utils;

import java.util.Arrays;

public class FileNameUtils {
	private static boolean checkNumber(String s) {
		if (s == null) {
	        return false;
	    }
	    try {
	        Double.parseDouble(s);
	    } catch (NumberFormatException nfe) {
	        return false;
	    }
	    return true;	
	}
		
	/**
	 * truncate filename if file number is present
	 * @param s
	 * @return
	 */
	public static String check(String s) {
		String[] str = s.split("\\_");
		if (FileNameUtils.checkNumber(str[str.length - 1])) {
			return String.join("_", Arrays.copyOfRange(str, 0, str.length-1));
		} else {
			return s;
		}
	}
}
