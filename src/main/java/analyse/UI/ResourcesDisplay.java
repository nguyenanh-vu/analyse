package analyse.UI;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ResourcesDisplay {
	private static final List<String> availableHelp = Arrays.asList("load","export","merge","rename",
			"label", "info", "list");
	
	public static void display(String filename) {
		InputStream is = ResourcesDisplay.class.getClassLoader().getResourceAsStream(filename);
		Scanner myReader = new Scanner(is);
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			System.out.println(data);
		}
		myReader.close();
	}
	
	public static boolean help(String str) {
		for (String s : ResourcesDisplay.availableHelp) {
			if (s.contentEquals(str)) {
				ResourcesDisplay.display(str + ".txt");
				return true;
			}
		}
		System.out.println(String
				.format("Help unavailable for command \"%s\". See \"help\" for list of available commands", str));
		return false;
	}
}
