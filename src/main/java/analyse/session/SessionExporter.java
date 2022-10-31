package analyse.session;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import analyse.exceptions.NotEnoughArgumentException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;
import analyse.messageanalysis.Parameter;
import analyse.search.Result;
import analyse.utils.JSONUtils;

/**
 * Util to export session data to JSON
 */
public class SessionExporter extends SessionTools {
	/**
	 * Export List<analyse.messageanalysis.Author> to JSON
	 * @return JSON data
	 */
	public String exportAuthors() {
		List<String> str = new ArrayList<>();
		for (Author author : this.getSession().getAuthorList()) {
			str.add(author.toJSON());
		}
		return "[\n" + JSONUtils.indent(String.join(",\n", str)) + "\n]";
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
	 * @param verbose Boolean if true add labels to message
	 * @return JSON data
	 */
	public String exportMessages(Boolean verbose) {
		List<String> str = new ArrayList<>();
		for (Message message : this.getSession().getMessageList()) {
			str.add(message.toJSON(verbose));
		}
		return "[\n" + JSONUtils.indent(String.join(",\n", str)) + "\n]";
	}
	
	/**
	 * Export List<analyse.search.Result> to JSON
	 * @return JSON data
	 */
	public String exportResults() {
		List<String> str = new ArrayList<>();
		for (Result r: this.getSession().getSearchHandler().getResults()) {
			str.add(r.toJSON());
		}
		return "[\n" + JSONUtils.indent(String.join(",\n", str)) + "\n]";
	}
	
	/**
	 * Export List<analyse.messageanalysis.Label> to JSON
	 * @return JSON data
	 */
	public String exportLabels() {
		List<String> str = new ArrayList<>();
		for (Label label : this.getSession().getLabels()) {
			str.add("\"" + label.getName() + "\"");
		}
		return "[" + String.join(",", str) + "]";
	}
	
	/**
	 * Export List<analyse.messageanalysis.Conversation> to JSON
	 * @return JSON data
	 */
	public String exportConversations() {
		List<String> str = new ArrayList<>();
		for (Conversation conv : this.getSession().getConversations()) {
			str.add(conv.toJSON());
		}
		return "[\n" + JSONUtils.indent(String.join(",", str)) + "\n]";
	}
	
	public String exportParams() {
		List<String> str = new ArrayList<>();
		for (Parameter p : this.getSession().getSearchHandler().getParams()) {
			str.add(p.toJSON());
		} 
		return "[\n" + JSONUtils.indent(String.join(",", str)) + "\n]";
	}
	
	/**
	 * Export whole session data
	 * @return JSON data
	 */
	public String exportSession() {
		StringBuilder str = new StringBuilder();
		str.append(String.format("\n\"authors\":%s,",this.exportAuthors()));
		str.append(String.format("\n\"labels\":%s,",this.exportLabels()));
		str.append(String.format("\n\"conversations\":%s,",this.exportConversations()));
		str.append(String.format("\n\"parameters\":%s,",this.exportParams()));
		str.append(String.format("\n\"results\":%s,",this.exportResults()));
		str.append(String.format("\n\"messages\":%s",this.exportMessages()));
		return "{" + JSONUtils.indent(str.toString()) + "}";
	}
	
	/**
	 * Command-line controller for data exporter
	 * set save file address if export session to file
	 * @param s String[] arguments
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
			} else if (s[0].contentEquals("results")) {
				str = this.exportResults();
			} else if (s[0].contentEquals("session")) {
				if (toFile) {
					this.getSession().setAddress(this.getSession().getWorkdir() + s[1]);
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
