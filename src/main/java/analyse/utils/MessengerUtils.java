package analyse.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;
import java.util.TimeZone;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import analyse.messageanalysis.Author;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;

/**
 * Utils for handling Facebook Messenger backup file
 */
public class MessengerUtils {
	
	/**
	 * Load data from Facebook Messenger backup file
	 * @param path String path to backup file
	 * @param messageList List<analyse.messageanalysis.List> 
	 * @param authorList List<analyse.messageanalysis.Author>
	 * @param labels List<analyse.messageanalysis.Label>
	 * @param conversation String
	 * @return
	 */
	public static void load(String path, List<Message> messageList, 
			List<Author> authorList, List<Label> labels,
			String conversation) {
		try {
			File myObj = new File(path);
			Scanner myReader = new Scanner(myObj);
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
					messageList.add(MessengerUtils
							.parse((JSONObject)o, authorList, labels, conversation));
				}
			}
			System.out.println(String.format("Facebook Messenger file %s finished parsing", path));
			myReader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("An error occurred.");
	    	System.out.println(e.getMessage());
	    }
	}
	
	/**
	 * <JSONObject,analyse.messageanalysis.Message> parser
	 * @param o JSONObject
	 * @param authorList List<analyse.messageanalysis.Author>
	 * @param labels List<analyse.messageanalysis.Label>
	 * @param conversation String
	 * @return
	 */
	public static Message parse(JSONObject o, 
			List<Author> authorList, 
			List<Label> labels,
			String conversation) {
		LocalDateTime ts = LocalDateTime.ofInstant(
				Instant.ofEpochMilli(o.getLong("timestamp_ms")), 
                TimeZone.getDefault().toZoneId());
		String content = "";
		try {
			content = new String(o.getString("content").getBytes("ISO-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Author author = new Author(o.getString("sender_name"));
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
		return new Message(ts, author, content, conversation);
	}
}
