package analyse.session;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import analyse.exceptions.NotEnoughArgumentException;
import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;

/**
 * Data loader for analyse.session.Session
 */
public class SessionLoader {
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
	
	/**
	 * Command-line controller for data loader
	 * @param s String[] arguments
	 * @param session analyse.session.Session
	 * @throws NotEnoughArgumentException
	 */
	public static void load(String[] s, Session session) throws NotEnoughArgumentException {
		if (session == null) {
			System.out.println("No session started");
		} else {
			if (s.length < 3) {
				throw new NotEnoughArgumentException(String.join(" ", s), 3, s.length);
			} else {
				List<Label> labels = new ArrayList<>();
				if (s.length > 3) {
					List<String> l = Arrays.asList(Arrays.copyOfRange(s, 3, s.length));
					for (String str : l) {
						labels.add(new Label(str));
					}
				}
				if (s[0].contentEquals("whatsapp")) {
					session.loadWhatsapp(s[1], labels, s[2]);
				} else if (s[0].contentEquals("fb")) {
					session.loadFb(s[1], labels, s[2]);
				} else if (s[0].contentEquals("session")) {
					if (Boolean.TRUE.equals(Boolean.valueOf(s[2]))) {
						session = new Session();
					}
					SessionLoader.loadSession(s[1], session);
				} else {
					System.out.println(String
							.format("Mode \"%s\" unknown, expected whatsapp|fb|session", s[0]));
				}
			}
		}
	}
	
	/**
	 * Load saved session
	 * @param path String path to session save file
	 * @param session analyse.session.Session
	 */
	public static void loadSession(String path, Session session) {
		try {
			File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
			String str = "";
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				str += data;
			}
			System.out.println(String.format("Session data file %s finished loading", path));
			JSONObject jo = new JSONObject(str);
			JSONArray authors = jo.getJSONArray("authors");
			JSONArray messages = jo.getJSONArray("messages");
			
			for (int i = 0; i < authors.length() ; i++) {
				JSONObject o = authors.getJSONObject(i);
				Author author = new Author(o.getString("name"));
				if (!session.getAuthorList().contains(author)) {
					session.getAuthorList().add(author);
				}
				JSONArray labels = o.getJSONArray("labels");
				for (int j = 0; j < labels.length(); j++) {
					session.labelAuthor(author, labels.getString(j));
				}
			}
			
			for (int i = 0; i < messages.length(); i++) {
				JSONObject o = messages.getJSONObject(i);
				LocalDateTime date = LocalDateTime.parse(o.getString("date"),formatter);
				Author author = session.searchAuthor(o.getString("author"));
				String conversation = o.getString("conversation");
				String content = o.getString("content");
				
				session.getMessageList().add(new Message(date, author, conversation, content));
			}
			
			System.out.println(String.format("Session data file %s finished parsing", path));
			myReader.close();
	    } catch (FileNotFoundException | JSONException | NotFoundException e) {
	    	System.out.println("An error occurred.");
	    	System.out.println(e.getMessage());
	    }
	}
}
