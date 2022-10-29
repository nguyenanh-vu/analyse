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
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;
import analyse.utils.MessengerUtils;
import analyse.utils.WhatsappUtils;

/**
 * Data loader for analyse.session.Session
 */
public class SessionLoader extends SessionTools {
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyyMMddHHmm");
	private SessionEditor editor;
	
	/**
	 * setter
	 * @param editor SessionEditor
	 */
	public void setEditor(SessionEditor editor) {
		this.editor = editor;
	}
	
	/**
	 * Command-line controller for data loader
	 * @param s String[] arguments
	 * @param session analyse.session.Session
	 * @throws NotEnoughArgumentException
	 */
	public void load(String[] s) throws NotEnoughArgumentException {
		if (this.getSession() == null) {
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
					WhatsappUtils.load(s[1], labels, s[2], this.editor);
				} else if (s[0].contentEquals("fb")) {
					MessengerUtils.load(s[1], labels, s[2], this.editor);
				} else if (s[0].contentEquals("session")) {
					if (Boolean.TRUE.equals(Boolean.valueOf(s[2]))) {
						this.getSession().restart();
					}
					this.loadSession(s[1]);
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
	public void loadSession(String path) {
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
				JSONArray labels = o.getJSONArray("labels");
				JSONArray conversations = o.getJSONArray("conversations");
				for (int j = 0; j < labels.length(); j++) {
					author.addLabel(new Label(labels.getString(j)));
				}
				for (int j = 0; j <  conversations.length(); j++) {
					author.addConversation(new Conversation(conversations.getString(j)));
				}
				editor.addAuthor(author);
			}
			
			for (int i = 0; i < messages.length(); i++) {
				JSONObject o = messages.getJSONObject(i);
				LocalDateTime date = LocalDateTime.parse(o.getString("date"),formatter);
				Author author = this.getSession().searchAuthor(o.getString("author"));
				Conversation conv = new Conversation(o.getString("conversation"));
				author.addConversation(conv);
				String content = o.getString("content");
				
				this.editor.addMessage(new Message(0l, date, author, content,conv));
			}
			this.getSession().setAddress(path);
			System.out.println(String.format("Session data file %s finished parsing", path));
			myReader.close();
	    } catch (FileNotFoundException | JSONException | NotFoundException e) {
	    	System.out.println("An error occurred.");
	    	System.out.println(e.getMessage());
	    }
	}
}
