package analyse.messageanalysis.comparators;

import java.util.Comparator;

import analyse.messageanalysis.NamedObject;

public class DTOComparator implements Comparator<NamedObject>{

	@Override
	public int compare(NamedObject o1, NamedObject o2) {
		return o1.getName().compareTo(o2.getName());
	}
}
