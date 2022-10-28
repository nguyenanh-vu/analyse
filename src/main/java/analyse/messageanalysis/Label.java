package analyse.messageanalysis;

/**
 * class representing data label
 */
public class Label {
	private String name;
	
	public Label(String name) {
		this.name = name;
	}
	
	/**
	 * getter
	 * @return String this.name
	 */
	public String getName() {
		return this.name;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Label) {
			Label label = (Label) other;
			return this.name.contentEquals(label.getName());
		} else if (other instanceof String) {
			String str = (String) other;
			return this.name.contentEquals(str);
		} else {
			return false;
		}
	}
	
	public String toString() {
		return this.name;
	}
}
