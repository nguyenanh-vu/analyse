package analyse.messageanalysis;

public class Label {
	private String name;
	
	public Label(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Label) {
			return this.equals((Label) other);
		} else if (other instanceof String) {
			return this.equals((String) other);
		} {
			return false;
		}
	}
	
	public boolean equals(String str) {
		return this.name.contentEquals(str);
	}
	
	public boolean equals(Label label) {
		return this.name.contentEquals(label.getName());
	}
	
	public String toString() {
		return this.name;
	}
}
