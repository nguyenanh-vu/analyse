package analyse.session;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import analyse.UI.UIUtils;
import analyse.exceptions.JSONParsingException;
import analyse.exceptions.NotEnoughArgumentException;
import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;
import analyse.messageanalysis.Parameter;
import analyse.messageanalysis.Reactions;
import analyse.search.SimpleResult;
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
			UIUtils.notEnoughArguments(s, 3);
			File file = this.getSession().getFileSystem().getPath(s[1]).toFile();
			List<Label> labels = new ArrayList<>();
			if (s.length > 3) {
				List<String> l = Arrays.asList(Arrays.copyOfRange(s, 3, s.length));
				for (String str : l) {
					labels.add(new Label(str));
				}
			}
			switch (s[0]) {
				case "folder":
					switch (s[2]) {
						case "fb":
							MessengerUtils.loadFolder(file, labels, editor);
							break;
						case "whatsapp":
							WhatsappUtils.loadFolder(file, labels, editor);
							break;
						default:
							UIUtils.modeUnknown(s[2], Arrays.asList("fb", "whatsapp"));
							break;
					}
					break;
				case "whatsapp":
					WhatsappUtils.load(file, labels, s[2], this.editor);
					break;
				case "fb":
					MessengerUtils.load(file, labels, s[2], this.editor);
					break;
				case "session":
					if (Boolean.TRUE.equals(Boolean.valueOf(s[2]))) {
						this.getSession().restart();
					}
					this.loadSession(file);
					break;
				case "messages":
					if (Boolean.TRUE.equals(Boolean.valueOf(s[2]))) {
						this.getSession().restart();
					}
					this.loadMessages(file);
					break;
				default:
					UIUtils.modeUnknown(s[0], Arrays.asList("whatsapp",
							"fb","session","messages"));
			}
		}
	}
	
	/**
	 * Load saved session
	 * @param file File to session save file
	 * @param session analyse.session.Session
	 */
	public void loadSession(File file) {
		try {
			InputStream  is = new FileInputStream(file);
			Scanner myReader = new Scanner(is);
			String str = "";
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				str += data;
			}
			System.out.println(String.format("Session data file %s finished loading", file.toString()));
			JSONObject jo = new JSONObject(str);
			 
			this.parseAuthors(jo.getJSONArray("authors"));
			this.parseMessages(jo.getJSONArray("messages"));
			this.parseParams(jo.getJSONArray("parameters"));
			this.parseResults(jo.getJSONArray("results"));
			
			this.getSession().getFileSystem().set("sessionFile", file);
			System.out.println(String.format("Session data file %s finished parsing", file.toString()));
			myReader.close();
	    } catch (FileNotFoundException | JSONException | JSONParsingException e) {
	    	System.out.println("An error occurred.");
	    	System.out.println(e.getLocalizedMessage());
	    }
	}
	
	/**
	 * load authors from save file
	 * @param authors JSONArray containing authors to parse
	 */
	private void parseAuthors(JSONArray authors) {
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
			this.editor.addAuthor(author);
		}
	}
	
	/**
	 * Load session data from message save file
	 * @param file File to save file
	 */
	private void loadMessages(File file) {
		try {
			InputStream  is = new FileInputStream(file);
			Scanner myReader = new Scanner(is);
			String str = "";
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				str += data;
			}
			System.out.println(String.format("Session data file %s finished loading", file.toString()));
			JSONArray jo = new JSONArray(str);
			this.parseMessages(jo);
			myReader.close();
		} catch (FileNotFoundException | JSONParsingException | JSONException e) {
			System.out.println(e.getMessage());;
		}
		
	}
	
	/***
	 * load messages from save file
	 * @param messages JSONArray containing messages to parse
	 * @throws JSONParsingException 
	 */
	private void parseMessages(JSONArray messages) throws JSONParsingException {
		for (int i = 0; i < messages.length(); i++) {
			JSONObject o = messages.getJSONObject(i);
			LocalDateTime date = LocalDateTime.parse(o.getString("date"),formatter);
			Author author = new Author(o.getString("author"));
			Conversation conv = new Conversation(o.getString("conversation"));
			if (o.has("author_labels")) {
				JSONArray authorLabels = o.getJSONArray("author_labels");
				for (int j = 0; j < authorLabels.length(); j++) {
					author.addLabel(new Label(authorLabels.getString(j)));
				}
			}
			JSONArray labels = o.getJSONArray("labels");
			for (int j = 0; j < labels.length(); j++) {
				conv.addLabel(new Label(labels.getString(j)));
			}
			String content = o.getString("content");
			author.addConversation(conv);
			Reactions reactions = Reactions.parse(o);
			this.editor.addMessage(new Message(0l, date, author, content,conv, reactions));
		}
	}
	
	/**
	 * load results from save file
	 * @param results JSONArray containing results to parse
	 */
	private void parseResults(JSONArray results) {
		for (int i = 0; i < results.length(); i++) {
			JSONObject o = results.getJSONObject(i);
			if (o.getString("type").contentEquals("SIMPLE")) {
				try {
					this.getSession().getSearchHandler().addResult(SimpleResult.parse(o, this.getSession()));
				} catch (JSONException | NotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * load search parameters from save file
	 * @param params
	 */
	private void parseParams(JSONArray params) {
		for (int i = 0; i < params.length(); i++) {
			JSONObject o = params.getJSONObject(i);
			this.getSession().getSearchHandler().addParams(Parameter.parse(o));
		}
	}
}
