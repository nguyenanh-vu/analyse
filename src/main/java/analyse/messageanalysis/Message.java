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
			DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
	
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
		String content = this.content;
		return String.format("{\n	\"date\":\"%s\",\n	\"author\":\"%s\",\n	\"conversation\":\"%s\",\n	\"content\":\"%s\"\n}", 
				this.timestamp.format(formatter), 
				this.author.getName(), 
				this.conversation,
				content.replace("\n", "\\n").replace("\"", "\\\""));
	}
	
	/**
	 * extend the content
	 * @param str String additional content
	 */
	public void extend(String str) {
		this.content = this.content + str;
	}
}
