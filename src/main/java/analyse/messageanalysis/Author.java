package analyse.messageanalysis;

import java.util.ArrayList;
import java.util.List;

/**
 * Class representing message author
 */
public class Author {
	private String name;
	private List<Label> labels = new ArrayList<>();
	
	/**
	 * Constructor
	 * @param name String author's name
	 */
	public Author(String name) {
		this.name = name;
	}
	
	/**
	 * getter
	 * @return String this.name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * getter
	 * @return List<analyse.messageanalysis.Label> this.labels
	 */
	public List<Label> getLabels() {
		return this.labels;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Author) {
			return this.equals((Author) other);
		} else if (other instanceof String) {
			return this.equals((String) other);
		} else {
			return false;
		}
	}
	
	/**
	 * comparator
	 * @param name String
	 * @return
	 */
	public boolean equals(String name) {
		return this.name.contentEquals(name);
	}
	
	/**
	 * comparator
	 * @param author analyse.messageanalysis.Author
	 * @return
	 */
	public boolean equals(Author author) {
		return this.name.contentEquals(author.getName());
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
	 * Remove all labels
	 */
	public void clearLabel() {
		this.labels = new ArrayList<>();
	}
	
	/**
	 * Remove single label
	 * @param label analyse.messageanalysis.Label to remove
	 */
	public void removeLabel(Label label) {
		this.labels.remove(label);
	}
	
	public String toString() {
		String str = "";
		for (Label label : this.labels) {
			str += ",\"" + label.getName() + "\"";
		}
		if (!str.isEmpty()) {
			str = str.substring(1);
		}
		return String.format("{\n	\"name\":\"%s\",\n	\"labels\":[%s] \n}", this.name, str);
	}
}
