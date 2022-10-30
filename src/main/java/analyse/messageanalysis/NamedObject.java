package analyse.messageanalysis;

/**
 * Class for named object
 */
public class NamedObject {
	private String name;
	
	/**
	 * getter
	 * @return String this.name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * setter
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return this.name;
	}
}
