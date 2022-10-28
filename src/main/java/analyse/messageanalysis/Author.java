package analyse.messageanalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing message author
 */
public class Author extends DTO{
	private List<Label> labels = new ArrayList<>();
	private List<Conversation> conversations = new ArrayList<>();
	
	/**
	 * Constructor
	 * @param name String author's name
	 */
	public Author(String name) {
		this.setName(name);
	}
	
	/**
	 * getter
	 * @return List<analyse.messageanalysis.Label> this.labels
	 */
	public List<Label> getLabels() {
		return this.labels;
	}
	
	/**
	 * getter
	 * @return List<analyse.messageanalysis.Conversation> this.conversations
	 */
	public List<Conversation> getConversations() {
		return this.conversations;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Author) {
			Author author = (Author) other;
			return this.getName().contentEquals(author.getName());
		} else if (other instanceof String) {
			String name = (String) other;
			return this.getName().contentEquals(name);
		} else {
			return false;
		}
	}
	
	/**
	 * Attach label to author
	 * @param label analyse.messageanalysis.Label to attach
	 */
	public void addLabel(Label label) {
		if (!this.labels.contains(label)) {
			this.labels.add(label);	
		}
	}
	
	/**
	 * Remove single label
	 * @param label analyse.messageanalysis.Label to remove
	 */
	public void removeLabel(Label label) {
		this.labels.remove(label);
	}
	
	/**
	 * Attach conversations to author
	 * @param conversations analyse.messageanalysis.Conversation to attach
	 */
	public void addConversation(Conversation conversation) {
		if (!this.conversations.contains(conversation)) {
			this.conversations.add(conversation);	
		}
	}
	
	/**
	 * Remove single conversation
	 * @param conversation analyse.messageanalysis.Conversation to remove
	 */
	public void removeConversation(Conversation conversation) {
		this.conversations.remove(conversation);
	}
	
	@Override
	public String toString() {
		String lab = "";
		String conv = "";
		for (Label label : this.labels) {
			lab += ",\"" + label.getName() + "\"";
		}
		if (!lab.isEmpty()) {
			lab = lab.substring(1);
		}
		for (Conversation conversation: this.conversations) {
			conv += ",\"" + conversation.getName() + "\"";
		}
		if (!conv.isEmpty()) {
			conv = conv.substring(1);
		}
		return String.format("{\"name\":\"%s\",\"labels\":[%s], \"conversations\":[%s]}", 
				this.getName(), lab, conv);
	}
}
