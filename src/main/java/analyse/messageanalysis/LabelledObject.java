package analyse.messageanalysis;

import java.util.ArrayList;
import java.util.List;

public class LabelledObject extends NamedObject{
	private List<Label> labels = new ArrayList<>();
	
	/**
	 * getter
	 * @return List<analyse.messageanalysis.Label> this.labels
	 */
	public List<Label> getLabels() {
		return this.labels;
	}
	
	/**
	 * Attach label to author
	 * @param label analyse.messageanalysis.Label to attach
	 */
	public void addLabel(Label label) {
		if (!this.labels.contains(label)) {
			this.labels.add(label);	
		}
	}
	
	/**
	 * Remove single label
	 * @param label analyse.messageanalysis.Label to remove
	 */
	public void removeLabel(Label label) {
		this.labels.remove(label);
	}
	
	/**
	 * Get String list of labels
	 * @return
	 */
	public String labelsToString() {
		List<String> labels = new ArrayList<>();
		for (Label label : this.labels) {
			labels.add(label.getName());
		}
		return String.join(",", labels);
	}
	
	/**
	 * Get JSONArray list of labels
	 * @return
	 */
	public String labelsToJSON() {
		List<String> labels = new ArrayList<>();
		for (Label label : this.labels) {
			labels.add("\"" + label.getName() + "\"");
		}
		return "[" + String.join(",", labels) + "]";
	}
	
	/**
	 * remove all labels
	 */
	public void clearLabels() {
		this.labels = new ArrayList<>();
	}
	
	/**
	 * Return true if at least one label matches regex
	 * @param regex String regex to match
	 * @return
	 */
	public Boolean matchesLabels(String regex) {
		for (Label l : this.labels) {
			if (l.getName().matches(regex)) {
				return true;
			}
		}
		return false;
	}
}
