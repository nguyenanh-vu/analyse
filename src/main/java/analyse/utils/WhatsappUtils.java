package analyse.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Pattern;

import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;
import analyse.session.SessionEditor;

/**
 * Utils for handling Whatsapp backup file
 */
public class WhatsappUtils{
	
	/**
	 * DateTime formatter for Whatsapp backup file
	 */
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("dd/MM/yyyy, HH:mm ");
	
	/**
	 * Load data from Whatsapp backup file
	 * @param file File to backup file
	 * @param labels List<analyse.messageanalysis.Label>
	 * @param editor analyse.session.SessionEditor to use for edition
	 * @param conversation String
	 * @return
	 */
	public static void load(File file, List<Label> labels, 
			String conversation, SessionEditor editor) {
		Pattern datePattern = Pattern
				.compile("\\d\\d/\\d\\d/\\d\\d\\d\\d, \\d\\d:\\d\\d ");
		try {
			InputStream  is = new FileInputStream(file);
			Scanner myReader = new Scanner(is);
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				Message message = null;
				if (datePattern.matcher(data).find()) {
					message = WhatsappUtils.parse(data, labels, conversation);
					editor.addMessage(message);
				} else {
					List<Message> messageList = editor.getSession().getMessageList();
					message  = messageList.get(messageList.size() - 1);
					message.extend("\n" + data);
				}
				
			}
			myReader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("An error occurred.");
	    	System.out.println(e.getMessage());
	    }
	}
	
	/**
	 * <String, analyse.messageanalysis.Message> line parser
	 * @param line String
	 * @param labels List<analyse.messageanalysis.List> 
	 * @param conversation String
	 * @return new Message
	 */
	public static Message parse(String line, 
			List<Label> labels, String conversation) {
		String[] s1 = line.split("- ", 2);
		String[] s2 = s1[1].split(": ", 2);
		Author author = new Author(s2[0].replace(" ", "_"));
		Conversation conv = new Conversation(conversation); 
		for (Label l : labels) {
			conv.addLabel(l);
		}
		author.addConversation(conv);
		return new Message(0l, LocalDateTime.parse(s1[0], formatter), 
				author, s2[1].replace("\r", ""),conv);
	}
	
}
