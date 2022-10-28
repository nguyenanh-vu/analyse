package analyse.utils;

public class JSONUtils {
	public static String indent(String str) {
		String res = "	" + str;
		res = res.replace("\n", "\n	");
		return res;
	}
}
