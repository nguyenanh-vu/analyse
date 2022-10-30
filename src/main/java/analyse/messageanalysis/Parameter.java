package analyse.messageanalysis;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Parameter extends NamedObject {
	private String author;
	private String authorLabels;
	private String labels;
	private String conversations;
	private LocalDateTime minDate;
	private LocalDateTime maxDate;
	List<Parameter> subParameters = new ArrayList<>();
	
	public Parameter(String name) {
		this.setName(name);
	}
	
	/**
	 * return true if message matches all present search parameters
	 * @param message analyse.messageanalysis.Message to match
	 * @return Boolean
	 */
	public Boolean matches(Message message) {
		if (!this.author.equals(null) 
				&& !message.getAuthor().getName().matches(this.author)) {
			return false;
		}
		if (!this.authorLabels.equals(null) 
				&& !message.getAuthor().matchesLabels(this.authorLabels)) {
			return false;
		}
		if (!this.labels.equals(null) 
				&& !message.getConversation().matchesLabels(this.labels)) {
			return false;
		}
		if (!this.conversations.equals(null) 
				&& !message.getConversation().getName().matches(this.conversations)) {
			return false;
		}
		if (!this.minDate.equals(null) 
				&& message.getTimestamp().compareTo(minDate) < 0) {
			return false;
		}
		if (!this.maxDate.equals(null) 
				&& message.getTimestamp().compareTo(maxDate) > 0) {
			return false;
		}
		return true;
	}
	
	/**
	 * return true if author matches all present search parameters
	 * @param message analyse.messageanalysis.Author to match
	 * @return Boolean
	 */
	public Boolean matches(Author author) {
		if (!this.author.equals(null) 
				&& author.getName().matches(this.author)) {
			return false;
		}
		if (!this.authorLabels.equals(null) 
				&& author.matchesLabels(this.authorLabels)) {
			return false;
		}
		if (!this.labels.equals(null) 
				&& author.matchesConversationLabel(this.labels)) {
			return false;
		}
		if (!this.conversations.equals(null) 
				&& author.matchesConversation(conversations)) {
			return false;
		}
		return true;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorLabels() {
		return authorLabels;
	}

	public void setAuthorLabels(String authorLabels) {
		this.authorLabels = authorLabels;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getConversations() {
		return conversations;
	}

	public void setConversations(String conversations) {
		this.conversations = conversations;
	}

	public LocalDateTime getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(LocalDateTime maxDate) {
		this.maxDate = maxDate;
	}

	public LocalDateTime getMinDate() {
		return minDate;
	}

	public void setMinDate(LocalDateTime minDate) {
		this.minDate = minDate;
	}
	
	public List<Parameter> getSubParameters() {
		return this.subParameters;
	}
}
