package analyse.messageanalysis;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * class representing messages
 */
public class Message {
	private LocalDateTime timestamp;
	private Author author;
	private String content;
	private String conversation;
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyyMMddHHmm");
	
	/**
	 * all args constructor
	 * @param timestamp LocalDateTime
	 * @param author analyse.messageanalysis.Author
	 * @param content String
	 * @param conversation String
	 */
	public Message(LocalDateTime timestamp, 
			Author author, String content,
			String conversation) {
		this.timestamp = timestamp;
		this.author = author;
		this.content = content;
		this.conversation = conversation;
	}
	
	/**
	 * getter
	 * @return analyse.messageanalysis.Author this.author
	 */
	public Author getAuthor() {
		return this.author;
	}
	
	/**
	 * setter
	 * @param author analyse.messageanalysis.Author
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	/**
	 * getter
	 * @return LocalDateTime this.timestamp
	 */
	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}
	
	/**
	 * getter
	 * @return String this.content
	 */
	public String getContent() {
		return this.content;
	}
	
	/**
	 * getter
	 * @return String this.converation
	 */
	public String getConversation() {
		return this.conversation;
	}
	
	public String toString() {
		return toString(false);
	}
	
	public String toString(Boolean verbose) {
		String content = this.content;
		if (Boolean.TRUE.equals(verbose)) {
			String str = "";
			for (Label label : this.author.getLabels()) {
				str += ",\"" + label.getName() + "\"";
			}
			if (!str.isEmpty()) {
				str = str.substring(1);
			}
			return String.format("{\"date\":\"%s\",\"author\":\"%s\",\"conversation\":\"%s\",\"labels\":[%s],\n	\"content\":\"%s\"}", 
					this.timestamp.format(formatter), 
					this.author.getName(), 
					this.conversation,
					str,
					content.replace("\n", "\\n").replace("\"", "\\\""));
		} else {
			return String.format("{\"date\":\"%s\",\"author\":\"%s\",\"conversation\":\"%s\",\n	\"content\":\"%s\"}", 
					this.timestamp.format(formatter), 
					this.author.getName(), 
					this.conversation,
					content.replace("\n", "\\n").replace("\"", "\\\""));
		}
	}
	
	/**
	 * extend the content
	 * @param str String additional content
	 */
	public void extend(String str) {
		this.content = this.content + str;
	}
}
