package analyse.utils;

/**
 * Utils for JSON data treatment
 */
public class JSONUtils {
	public static String indent(String str) {
		String res = "	" + str;
		res = res.replace("\n", "\n	");
		return res;
	}
}
