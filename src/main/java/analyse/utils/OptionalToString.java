package analyse.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Optional to String
 */
public class OptionalToString {
	public static String toString(String str) {
		if (str==null) {
			return "null";
		} else {
			return "\"" + str + "\"";
		}
	}
	
	public static String format(LocalDateTime date, DateTimeFormatter formatter) {
		if (date==null) {
			return "null";
		} else {
			return "\"" + date.format(formatter) + "\"";
		}
	}
}
