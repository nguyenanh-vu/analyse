package analyse.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import analyse.messageanalysis.Reactions;
import analyse.session.SessionEditor;

/**
 * Utils for handling Facebook Messenger backup file
 */
public class MessengerUtils {
	public static Map<String,String> reactions = new HashMap<String, String>() {
		private static final long serialVersionUID = -1875872005610564502L;
		{
			put("\u00f0\u009f\u0098\u0086", "laugh");
			put("\u00f0\u009f\u0098\u0082", "laughTears");
			put("\u00e2\u009d\u00a4", "love");
			put("\u00f0\u009f\u0091\u008d", "thumbs");
			put("\u00f0\u009f\u0099\u0082", "smile");
			put("\u00f0\u009f\u0098\u00ae", "surprise");
			put("\u00f0\u009f\u0098\u00a2", "tears");
			put("\u00f0\u009f\u0098\u00a0", "angry");
		}};
	
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
		Reactions reactions = new Reactions();
		if (!o.isNull("reactions")) {
			JSONArray r = o.getJSONArray("reactions");
			for (int i = 0; i < r.length(); i++) {
				MessengerUtils.addReactions(reactions, r.getJSONObject(i).getString("reaction"));
			}
		}
		return new Message(0l, ts, author, content, conv, reactions);
	}
	
	public static void addReactions(Reactions r, String str) {
		if (MessengerUtils.reactions.containsKey(str)) {
			r.incr(MessengerUtils.reactions.get(str));
		}
	}
}
