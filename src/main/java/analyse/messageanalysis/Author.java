package analyse.messageanalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing message author
 */
public class Author extends LabelledObject{
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
	 * @return List<analyse.messageanalysis.Conversation> this.conversations
	 */
	public List<Conversation> getConversations() {
		return this.conversations;
	}
	
	public List<Label> getAllLabels() {
		List<Label> res = new ArrayList<>();
		res.addAll(this.labels);
		for (Conversation conv : this.conversations) {
			res.addAll(conv.getLabels());
		}
		return res;
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
	
	private String convToString() {
		List<String> conv = new ArrayList<>();
		for (Conversation c : this.conversations) {
			conv.add(c.getName());
		}
		return String.join(",", conv);
	}
	
	/**
	 * get JSONArray list of conversations
	 * @return
	 */
	private String convToJSON() {
		List<String> conv = new ArrayList<>();
		for (Conversation c : this.conversations) {
			conv.add("\"" + c.getName() + "\"");
		}
		return "[" + String.join(",", conv) + "]";
	}
	
	@Override
	public String toString(){
		StringBuilder str = new StringBuilder();
		str.append(String.format("name: %s,", this.getName()));
		str.append(String.format("labels: [%s],", this.labelsToString()));
		str.append(String.format("conversations: [%s]", this.convToString()));
		return str.toString();
	}
	
	public String toJSON() {
		StringBuilder str = new StringBuilder();
		str.append(String.format("\"name\": \"%s\",", this.getName()));
		str.append(String.format("\"labels\": %s,", this.labelsToJSON()));
		str.append(String.format("\"conversations\": %s", this.convToJSON()));
		return "{" + str.toString() + "}";
	}
	
	/**
	 * Return true if at least one conversation matches regex
	 * @param regex String regex to match
	 * @return Boolean
	 */
	public Boolean matchesConversation(String regex) {
		for (Conversation c : this.conversations) {
			if (c.getName().matches(regex)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Return true if at least one conversation has on label matching regex
	 * @param regex String regex to match
	 * @return Boolean
	 */
	public Boolean matchesConversationLabel(String regex) {
		for (Conversation c : this.conversations) {
			if (c.matchesLabels(regex)) {
				return true;
			}
		}
		return false;
	}
}
