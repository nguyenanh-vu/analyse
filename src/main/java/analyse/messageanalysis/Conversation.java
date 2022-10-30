package analyse.messageanalysis;

public class Conversation extends LabelledObject {
	public Conversation(String name) {
		this.setName(name);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Conversation) {
			Conversation label = (Conversation) other;
			return this.getName().contentEquals(label.getName());
		} else if (other instanceof String) {
			String str = (String) other;
			return this.getName().contentEquals(str);
		} else {
			return false;
		}
	}
	
	public String toString() {
		return String.format("{\"name\":\"%s\",\"labels\":[%s]}", 
				this.getName(), this.labelsToString());
	}
}
