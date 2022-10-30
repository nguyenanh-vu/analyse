package analyse.messageanalysis.comparators;

import java.util.Comparator;

import analyse.messageanalysis.Message;

public class DatedMessageComparator implements Comparator<Message>{

	@Override
	public int compare(Message o1, Message o2) {
		return o1.getTimestamp().compareTo(o2.getTimestamp());
	}

}
