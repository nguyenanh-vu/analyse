package analyse.messageanalysis.comparators;

import java.util.Comparator;

import analyse.messageanalysis.DTO;

public class DTOComparator implements Comparator<DTO>{

	@Override
	public int compare(DTO o1, DTO o2) {
		return o1.getName().compareTo(o2.getName());
	}
}
