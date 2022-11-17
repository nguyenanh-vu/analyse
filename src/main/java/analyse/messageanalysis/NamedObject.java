package analyse.messageanalysis;

import lombok.Getter;
import lombok.Setter;

/**
 * Class for named object
 */
@Getter
@Setter
public class NamedObject implements Comparable<NamedObject> {
	private String name;
	
	public String toString() {
		return this.name;
	}

	@Override
	public int compareTo(NamedObject o) {
		return 0;
	}
}
