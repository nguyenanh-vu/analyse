package analyse.session;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import analyse.exceptions.NotEnoughArgumentException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;
import analyse.utils.JSONUtils;

/**
 * Util to export session data to JSON
 */
public class SessionExporter extends SessionTools {
	/**
	 * Export List<analyse.messageanalysis.Author> to JSON
	 * @param session analyse.session.Session
	 * @return JSON data
	 */
	public String exportAuthors() {
		String str = "";
		for (Author author : this.getSession().getAuthorList()) {
			str += ",\n" + author.toString();
		}
		if (!str.isEmpty()) {
			str = str.substring(2);
		}
		return "[\n" + JSONUtils.indent(str) + "\n]";
	}
	
	/**
	 * Export List<analyse.messageanalysis.Message> to JSON
	 * @param session analyse.session.Session
	 * @return JSON data
	 */
	public String exportMessages() {
		return this.exportMessages(false);
	}
	
	/**
	 * Export List<analyse.messageanalysis.Message> to JSON
	 * @param session analyse.session.Session
	 * @param verbose Boolean if true add labels to message
	 * @return JSON data
	 */
	public String exportMessages(Boolean verbose) {
		String str = "";
		for (Message message : this.getSession().getMessageList()) {
			str += ",\n" + message.toString(verbose);
		}
		if (!str.isEmpty()) {
			str = str.substring(2);
		}
		return "[\n" + JSONUtils.indent(str) + "\n]";
	}
	
	/**
	 * Export List<analyse.messageanalysis.Label> to JSON
	 * @param session analyse.session.Session
	 * @return JSON data
	 */
	public String exportLabels() {
		String str = "";
		for (Label label : this.getSession().getLabels()) {
			str += ",\"" + label.toString() + "\"";
		}
		if (!str.isEmpty()) {
			str = str.substring(1);
		}
		return "[" + str + "]";
	}
	
	/**
	 * Export List<analyse.messageanalysis.Conversation> to JSON
	 * @param session analyse.session.Session
	 * @return JSON data
	 */
	public String exportConversations() {
		String str = "";
		for (Conversation conv : this.getSession().getConversations()) {
			str += ",\"" + conv.toString() + "\"";
		}
		if (!str.isEmpty()) {
			str = str.substring(1);
		}
		return "[" + str + "]";
	}
	
	/**
	 * Export whole session data
	 * @param session analyse.session.Session
	 * @return JSON data
	 */
	public String exportSession() {
		return String.format("{\n	\"authors\":%s,\n	\"labels\":%s,\n	\"conversations\":%s,\n	\"messages\":%s\n}", 
				JSONUtils.indent(this.exportAuthors()),
				JSONUtils.indent(this.exportLabels()),
				JSONUtils.indent(this.exportConversations()),
				JSONUtils.indent(this.exportMessages()));
	}
	
	/**
	 * Command-line controller for data exporter
	 * set save file address if export session to file
	 * @param s String[] arguments
	 * @param session analyse.session.Session
	 * @throws NotEnoughArgumentException
	 */
	public void export(String[] s) throws NotEnoughArgumentException {
		if (s.length < 1) {
			throw new NotEnoughArgumentException(String.join(" ", s), 1, s.length);
		} else {
			boolean toFile;
			File file = null;
			if (s.length > 1) {
				toFile = true;
				file = new File(this.getSession().getWorkdir() + s[1]);
			} else {
				toFile = false;
			}
			String str = "";
			if (s[0].contentEquals("authors")) {
				str = this.exportAuthors();
			} else if (s[0].contentEquals("labels")) {
				str = this.exportLabels();
			} else if (s[0].contentEquals("messages")) {
				str = this.exportMessages();
			} else if (s[0].contentEquals("vmessages")) {
				str = this.exportMessages(true);
			} else if (s[0].contentEquals("session")) {
				if (toFile) {
					this.getSession().setAddress(s[1]);
				}
				str = this.exportSession();
			} else {
				System.out.println(String
						.format("Mode \"%s\" unknown, expected authors|labels|messages|session", s[0]));
			}
			
			if (toFile) {
				try (FileWriter fw = new FileWriter(file)){
					fw.write(str);
					System.out.println(String.format("%s data written to %s", s[0], s[1]));
				} catch (IOException e) {
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println(str);
			}
		}
	}
}
