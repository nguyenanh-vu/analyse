package analyse.messageanalysis;

import java.util.ArrayList;
import java.util.List;

public class Author {
	private String name;
	private List<Label> labels = new ArrayList<>();
	
	public Author(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
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
	
	public boolean equals(String name) {
		return this.name.contentEquals(name);
	}
	
	public boolean equals(Author author) {
		return this.name.contentEquals(author.getName());
	}
	
	public void addLabel(Label label) {
		this.labels.add(label);
	}
	
	public void clearLabel() {
		this.labels = new ArrayList<>();
	}
	
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
