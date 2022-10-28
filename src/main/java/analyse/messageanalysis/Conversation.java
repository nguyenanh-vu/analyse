package analyse.messageanalysis;

public class Conversation extends DTO {
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
}
