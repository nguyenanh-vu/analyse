package analyse.messageanalysis;

/**
 * Class for named object
 */
public class NamedObject implements Comparable<NamedObject> {
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

	@Override
	public int compareTo(NamedObject o) {
		
		return 0;
	}
}
