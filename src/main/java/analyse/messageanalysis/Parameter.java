package analyse.messageanalysis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import analyse.utils.OptionalToString;

public class Parameter extends NamedObject {
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyyMMddHHmm");
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
		if (this.author != null 
				&& !message.getAuthor().getName().matches(this.author)) {
			return false;
		}
		if (this.authorLabels != null 
				&& !message.getAuthor().matchesLabels(this.authorLabels)) {
			return false;
		}
		if (this.labels != null  
				&& !message.getConversation().matchesLabels(this.labels)) {
			return false;
		}
		if (this.conversations != null  
				&& !message.getConversation().getName().matches(this.conversations)) {
			return false;
		}
		if (this.minDate != null 
				&& message.getTimestamp().compareTo(minDate) < 0) {
			return false;
		}
		if (this.maxDate != null  
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
		if (this.author != null 
				&& author.getName().matches(this.author)) {
			return false;
		}
		if (this.authorLabels != null 
				&& author.matchesLabels(this.authorLabels)) {
			return false;
		}
		if (this.labels != null  
				&& author.matchesConversationLabel(this.labels)) {
			return false;
		}
		if (this.conversations != null 
				&& author.matchesConversation(conversations)) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		String str = "";
		for (Parameter p : this.subParameters) {
			str += "," + p.getName();
		}
		if (!str.isEmpty()) {
			str = str.substring(1);
		}
		return String.format("{\"name\":\"%s\",\"author\":%s,\"authorLabels\":%s,\"labels\":%s,\"conversations\":%s,\"minDate\":%s,\"maxDate\":%s,\n	\"subParameters\":[%s]}", 
				this.getName(),
				OptionalToString.toString(this.author),
				OptionalToString.toString(this.authorLabels),
				OptionalToString.toString(this.labels),
				OptionalToString.toString(this.conversations),
				OptionalToString.format(minDate, formatter),
				OptionalToString.format(maxDate, formatter),
				str);
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
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Parameter) {
			Parameter param = (Parameter) other;
			return this.getName().contentEquals(param.getName());
		} else if (other instanceof String) {
			String str = (String) other;
			return this.getName().contentEquals(str);
		} else {
			return false;
		}
	}
}
