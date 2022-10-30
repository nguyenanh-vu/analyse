package analyse.UI;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import analyse.exceptions.NotEnoughArgumentException;
import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;
import analyse.messageanalysis.comparators.DTOComparator;
import analyse.messageanalysis.comparators.MessageComparator;
import analyse.session.SessionTools;

public class Info extends SessionTools {
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm");
	/**
	 * Display infos about elements
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void info(String[] s) throws NotEnoughArgumentException {
		if (s.length < 2) {
			throw new NotEnoughArgumentException(String.join(" ", s), 2, s.length);
		} else {
			try {
				if (s[0].contentEquals("authors")) {
					Author a = this.getSession().searchAuthor(s[1]);
					System.out.println(a.toString());
				} else if (s[0].contentEquals("messages")) {
					Message m = this.getSession().searchMessage(Long.valueOf(s[1]));
					System.out.println(m.toString());
				} else {
					System.out.println(String
							.format("Mode \"%s\" unknown, expected authors|messages", s[0]));
				}
			} catch (NotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Display list of elements
	 * @param s
	 * @throws NotEnoughArgumentException 
	 */
	public void list(String[] s) throws NotEnoughArgumentException {
		if (s.length < 1) {
			throw new NotEnoughArgumentException(String.join(" ", s), 1, s.length);
		} else {
			if (s[0].contentEquals("authors")) {
				List<Author> authorList = new ArrayList<>();
				authorList.addAll(this.getSession().getAuthorList());
				authorList.sort(new DTOComparator());
				for (Author a : authorList) {
					System.out.println(a.getName());
				}
			} else if (s[0].contentEquals("messages")) {
				List<Message> messageList = new ArrayList<>();
				messageList.addAll(this.getSession().getMessageList());
				messageList.sort(new MessageComparator());
				for (Message m : this.getSession().getMessageList()) {
					System.out.println(String.format("id: %s, author: %s, date: %s, conversation: %s", 
							m.getId().toString(),
							m.getAuthor().getName(),
							m.getTimestamp().format(formatter),
							m.getConversation().toString()));
				}
			} else if (s[0].contentEquals("labels")) {
				List<Label> labels = new ArrayList<>();
				labels.addAll(this.getSession().getLabels());
				labels.sort(new DTOComparator());
				for (Label l : labels) {
					System.out.println(l.toString());
				}
			} else if (s[0].contentEquals("conversations")) {
				List<Conversation> conversations = new ArrayList<>();
				conversations.addAll(this.getSession().getConversations());
				conversations.sort(new DTOComparator());
				for (Conversation c : conversations) {
					System.out.println(c.toString());
				}
			} else {
				System.out.println(String
						.format("Mode \"%s\" unknown, expected authors|messages|labels|conversations", s[0]));
			}
		}
	}
}
