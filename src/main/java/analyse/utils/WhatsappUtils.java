package analyse.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import analyse.messageanalysis.Author;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;

public class WhatsappUtils{
	
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm ");

	public static List<Message> load(String path, List<Message> messageList, 
			List<Author> authorList, List<Label> labels, 
			String conversation) {
		try {
			File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				Pattern datePattern = Pattern
						.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d, \\d\\d:\\d\\d ");
				Message message = null;
				if (datePattern.matcher(data).find()) {
					message = WhatsappUtils.parse(data, authorList, labels, conversation);
					messageList.add(message);
				} else {
					message  = messageList.get(messageList.size() - 1);
					message.extend("\n" + data);
				}
			}
			System.out.println(String.format("Whatsapp file %s finished loading and parsing", path));
			myReader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("An error occurred.");
	    	e.printStackTrace();
	    }
		return null;
	}

	public static Message parse(String line, 
		List<Author> authorList, List<Label> labels,
		String conversation) {
		String[] s1 = line.split("- ", 2);
		String[] s2 = s1[1].split(": ", 2);
		Author author = new Author(s2[0]);
		int index = authorList.indexOf(author);
		if (index != -1) {
			author = authorList.get(index);
		} else {
			authorList.add(author);
		}
		for (Label label : labels) {
			if (!author.getLabels().contains(label)) {
				author.getLabels().add(label);
			}
		}
		return new Message(LocalDateTime.parse(s1[0], formatter), author, s2[1], conversation);
	}
	
}
