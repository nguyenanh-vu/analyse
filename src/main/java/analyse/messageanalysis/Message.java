package analyse.messageanalysis;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class representing messages
 */
public class Message {
	private Long id;
	private LocalDateTime timestamp;
	private Author author;
	private String content;
	private Conversation conversation;
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyyMMddHHmm");
	
	/**
	 * all args constructor
	 * @param id Long
	 * @param timestamp LocalDateTime
	 * @param author analyse.messageanalysis.Author
	 * @param content String
	 * @param conversation String
	 */
	public Message(Long id, LocalDateTime timestamp, 
			Author author, String content,
			Conversation conversation) {
		this.id = id;
		this.timestamp = timestamp;
		this.author = author;
		this.content = content;
		this.conversation = conversation;
	}
	
	/**
	 * getter
	 * @return Long this.id
	 */
	public Long getId() {
		return this.id;
	}
	
	/**
	 * getter
	 * @return analyse.messageanalysis.Author this.author
	 */
	public Author getAuthor() {
		return this.author;
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
	 * @return Conversation this.converation
	 */
	public Conversation getConversation() {
		return this.conversation;
	}
	
	/**
	 * setter
	 * @param author analyse.messageanalysis.Author
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	/**
	 * setter
	 * @param author analyse.messageanalysis.Conversation
	 */
	public void setConversation(Conversation conv) {
		this.conversation = conv;
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
			return String.format("{\"id\":%s,\"date\":\"%s\",\"author\":\"%s\",\"conversation\":\"%s\",\"labels\":[%s],\n	\"content\":\"%s\"}", 
					this.id.toString(),
					this.timestamp.format(formatter), 
					this.author.getName(), 
					this.conversation,
					str,
					content.replace("\n", "\\n").replace("\"", "\\\""));
		} else {
			return String.format("{\"id\":%s,\"date\":\"%s\",\"author\":\"%s\",\"conversation\":\"%s\",\n	\"content\":\"%s\"}", 
					this.id.toString(),
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
	
	/**
	 * count number of occurrences of regex in content
	 * @param regex
	 * @return
	 */
	public int count(String regex) {
		int res = 0;
		Matcher matcher = Pattern.compile(regex).matcher(this.content);
		while (matcher.find()) {
		    res++;
		}
		return res;
	}
}
