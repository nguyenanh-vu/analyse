package analyse.messageanalysis;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Message {
	private LocalDateTime timestamp;
	private Author author;
	private String content;
	private String conversation;
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
	
	public Message(LocalDateTime timestamp, 
			Author author, String content,
			String conversation) {
		this.timestamp = timestamp;
		this.author = author;
		this.content = content;
		this.conversation = conversation;
	}
	
	public boolean search(String str) {
		return this.content.contains(str);
	}
	
	public Author getAuthor() {
		return this.author;
	}
	
	public void setAuthor(Author author) {
		this.author = author;
	}
	
	public LocalDateTime getTimestamp() {
		return this.timestamp;
	}
	
	public String getContent() {
		return this.content;
	}
	
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
	
	public void extend(String str) {
		this.content = this.content + str;
	}
}
