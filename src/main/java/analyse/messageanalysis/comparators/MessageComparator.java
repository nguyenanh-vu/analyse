package analyse.messageanalysis.comparators;

import java.util.Comparator;

import analyse.messageanalysis.Message;

public class MessageComparator implements Comparator<Message>{

	@Override
	public int compare(Message o1, Message o2) {
		return o1.compareTo(o2);
	}

}
