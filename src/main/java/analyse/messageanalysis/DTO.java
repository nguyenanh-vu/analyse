package analyse.messageanalysis;

/**
 * Class for small data transfer object
 */
public class DTO {
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
