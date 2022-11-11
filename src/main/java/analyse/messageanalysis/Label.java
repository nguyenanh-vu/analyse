package analyse.messageanalysis;

/**
 * class representing data label
 */
public class Label extends NamedObject implements JSONExportable{
	public Label(String name) {
		this.setName(name);
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Label) {
			Label label = (Label) other;
			return this.getName().contentEquals(label.getName());
		} else if (other instanceof String) {
			String str = (String) other;
			return this.getName().contentEquals(str);
		} else {
			return false;
		}
	}

	@Override
	public String toJSON() {
		return this.toJSON(false);
	}

	@Override
	public String toJSON(Boolean verbose) {
		return "\"" + this.getName() + "\"";
	}
}
