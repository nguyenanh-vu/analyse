package analyse.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import analyse.exceptions.JSONParsingException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;
import analyse.session.SessionEditor;

/**
 * Utils for handling Facebook Messenger backup file
 */
public class MessengerUtils {
	
	/**
	 * Load data from Facebook Messenger backup file
	 * @param path String path to backup file
	 * @param labels List<analyse.messageanalysis.Label>
	 * @param editor analyse.session.SessionEditor to use for edition
	 * @param conversation String
	 * @return
	 */
	public static void load(String path,  List<Label> labels,
			String conversation, SessionEditor editor) {
		try {
			InputStream  is = new FileInputStream(path);
			Scanner myReader = new Scanner(is);
			String str = "";
			while (myReader.hasNextLine()) {
				String data = myReader.nextLine();
				str += data;
			}
			System.out.println(String.format("Facebook Messenger file %s finished loading", path));
			JSONObject jo = new JSONObject(str);
			JSONArray messages = jo.getJSONArray("messages");
			for (int i = 0; i < messages.length(); i++) {
				JSONObject o = messages.getJSONObject(i);
				if ((o.getString("type").contentEquals("Generic")) &&
						(o.has("content"))) {
					editor.addMessage(MessengerUtils.parse(o, labels, conversation));
				}
			}
			System.out.println(String.format("Facebook Messenger file %s finished parsing", path));
			myReader.close();
	    } catch (FileNotFoundException | JSONException | JSONParsingException e) {
	    	System.out.println("An error occurred.");
	    	System.out.println(e.getMessage());
	    }
	}
	
	/**
	 * <JSONObject,analyse.messageanalysis.Message> parser
	 * @param o JSONObject
	 * @param labels List<analyse.messageanalysis.Label> to attach
	 * @param editor analyse.session.SessionEditor to use for edition
	 * @param conversation String
	 * @return new Message
	 * @throws JSONParsingException 
	 */
	public static Message parse(JSONObject o, 
			List<Label> labels,
			String conversation) throws JSONParsingException {
		LocalDateTime ts = LocalDateTime.ofInstant(
				Instant.ofEpochMilli(o.getLong("timestamp_ms")), 
                TimeZone.getDefault().toZoneId());
		String content;
		try {
			content = new String(o.getString("content").getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException | JSONException e) {
			throw new JSONParsingException(o, e.getMessage());
		}
		Author author = new Author(o.getString("sender_name").replace(" ", "_"));
		Conversation conv = new Conversation(conversation);
		for (Label label : labels) {
			conv.addLabel(label);
		}
		author.addConversation(conv);
		return new Message(0l, ts, author, content, conv);
	}
}
