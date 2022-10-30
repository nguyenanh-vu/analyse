package analyse.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ResourcesDisplay {
	private static final List<String> availableHelp = Arrays.asList("load","export","merge","rename",
			"label");
	
	public static void display(String filename) {
		URL resource = ResourcesDisplay.class.getClassLoader().getResource(filename);
		if (resource == null) {
			throw new IllegalArgumentException("file not found!");
		} else {
			try {
				File file = new File(resource.toURI());
				Scanner myReader = new Scanner(file);
				while (myReader.hasNextLine()) {
					String data = myReader.nextLine();
					System.out.println(data);
				}
				myReader.close();
			} catch (URISyntaxException | FileNotFoundException e) {
				e.printStackTrace();
			}
		}
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
