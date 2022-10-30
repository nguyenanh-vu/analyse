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
		String lab = "";
		for (Label label : this.labels) {
			lab += ",\"" + label.getName() + "\"";
		}
		if (!lab.isEmpty()) {
			lab = lab.substring(1);
		}
		return lab;
	}
	
	/**
	 * remove all labels
	 */
	public void clearLabels() {
		this.labels = new ArrayList<>();
	}
}
