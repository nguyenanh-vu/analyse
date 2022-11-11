package analyse.messageanalysis;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * class representing messages
 */
public class Message implements Comparable<Message>, JSONExportable {
	private Long id;
	private LocalDateTime timestamp;
	private Author author;
	private String content;
	private Conversation conversation;
	private Reactions reactions = new Reactions();
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyyMMddHHmm");
	
	/**
	 * all args constructor without reactions
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
	 * all args constructor with reactions
	 * @param id Long
	 * @param timestamp LocalDateTime
	 * @param author analyse.messageanalysis.Author
	 * @param content String
	 * @param conversation String
	 * @param reactions Reactions
	 */
	public Message(Long id, LocalDateTime timestamp, 
			Author author, String content,
			Conversation conversation, Reactions reactions) {
		this.id = id;
		this.timestamp = timestamp;
		this.author = author;
		this.content = content;
		this.conversation = conversation;
		this.reactions = reactions;
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
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(String.format("id:%d,", this.id));
		str.append(String.format("date:%s,", this.timestamp.format(formatter)));
		str.append(String.format("author:%s,", this.author.getName()));
		str.append(String.format("conversation:%s,", this.conversation.getName()));
		str.append(String.format("labels:%s,\n	", this.conversation.labelsToString()));
		str.append(this.reactions.toString());
		return str.toString();
	}
	
	public String toJSON() {
		return this.toJSON(false);
	}
	
	public String toJSON(Boolean verbose) {
		StringBuilder str = new StringBuilder();
		str.append(String.format("\"id\":%d,", this.id));
		str.append(String.format("\"date\":\"%s\",", this.timestamp.format(formatter)));
		str.append(String.format("\"author\":\"%s\",", this.author.getName()));
		if (verbose) {
			str.append(String.format("\"author_labels\":%s,", this.author.labelsToJSON()));
		}
		str.append(String.format("\"conversation\":\"%s\",", this.conversation.getName()));
		str.append(String.format("\"labels\":%s,\n	", this.conversation.labelsToJSON()));
		str.append(this.reactions.toString() + ",\n	");
		str.append(String.format("\"content\":\"%s\"", this.content
				.replace("\\", "\\\\").replace("\r", "\\r").replace("\n", "\\n").replace("\"", "\\\"")));
		return "{" + str.toString() + "}";
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
	
	public Reactions getReactions() {
		return this.reactions;
	}

	@Override
	public int compareTo(Message o) {
		return this.id.compareTo(o.getId());
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Message) {
			Message m = (Message) other;
			return this.content.contentEquals(m.getContent())
					&& this.author.equals(m.author)
					&& this.timestamp.isEqual(m.getTimestamp());
		} else {
			return false;
		}
	}
}
