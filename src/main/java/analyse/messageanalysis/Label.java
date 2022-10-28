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
			return this.equals((Label) other);
		} else if (other instanceof String) {
			return this.equals((String) other);
		} {
			return false;
		}
	}
	
	/**
	 * comparator
	 * @param str String 
	 * @return
	 */
	public boolean equals(String str) {
		return this.name.contentEquals(str);
	}
	
	/**
	 * comparator
	 * @param label analyse.messageanalysis.Label
	 * @return
	 */
	public boolean equals(Label label) {
		return this.name.contentEquals(label.getName());
	}
	
	public String toString() {
		return this.name;
	}
}
