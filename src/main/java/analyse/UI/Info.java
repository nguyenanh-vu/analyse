package analyse.UI;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import analyse.exceptions.NotEnoughArgumentException;
import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;
import analyse.messageanalysis.Parameter;
import analyse.messageanalysis.comparators.DatedMessageComparator;
import analyse.messageanalysis.comparators.MessageComparator;
import analyse.messageanalysis.comparators.NamedObjectComparator;
import analyse.search.Result;
import analyse.search.ResultComparator;
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
		UIUtils.notEnoughArguments(s, 2);
		try {
			switch (s[0]) {
				case "authors":
					Author a = this.getSession().searchAuthor(s[1]);
					System.out.println(a.toJSON());
					break;
				case "messages":
					Message m = this.getSession().searchMessage(Long.valueOf(s[1]));
					System.out.println(m.toJSON());
					break;
				case "results":
					Result r = this.getSession().searchResult(Long.valueOf(s[1]));
					System.out.println(r.toJSON());
					break;
				case "parameters":
					Parameter p = this.getSession().searchParameter(s[1]);
					System.out.println(p.toJSON());
					break;
				default:
					UIUtils.modeUnknown(s[0],Arrays.asList("authors", 
							"messages", "results","parameters"));
					break;
			}
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Display list of elements
	 * @param s
	 * @throws NotEnoughArgumentException 
	 */
	public void list(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 1);
		int count = 0;
		int total = 0;
		try {
			Parameter param = null;
			if (s.length > 1) {
				param = this.getSession().searchParameter(s[1]);
			}
			switch (s[0]) {
				case "authors":
					List<Author> authorList = new ArrayList<>();
					authorList.addAll(this.getSession().getAuthorList());
					authorList.sort(new NamedObjectComparator());
					for (Author a : authorList) {
						total++;
						if (param == null) {
							count++;
							System.out.println(a.toString());
						} else if (param.matches(a)) {
							count++;
							System.out.println(a.toString());
						}
					}
					break;
				case "messages":
					List<Message> messageList = new ArrayList<>();
					messageList.addAll(this.getSession().getMessageList());
					messageList.sort(new MessageComparator());
					for (Message m : this.getSession().getMessageList()) {
						total++;
						if (param == null) {
							count++;
							System.out.println(m.toString());
						} else if (param.matches(m)) {
							count++;
							System.out.println(m.toString());
						}
					}
					break;
				case "labels":
					List<Label> labels = new ArrayList<>();
					labels.addAll(this.getSession().getLabels());
					labels.sort(new NamedObjectComparator());
					for (Label l : labels) {
						count++;
						total++;
						System.out.println(l.getName());
					}
					break;
				case "conversations":
					List<Conversation> conversations = new ArrayList<>();
					conversations.addAll(this.getSession().getConversations());
					conversations.sort(new NamedObjectComparator());
					for (Conversation c : conversations) {
						count++;
						total++;
						System.out.println(c.toString());
					}
					break;
				case "results":
					List<Result> results = new ArrayList<>();
					results.addAll(this.getSession().getSearchHandler().getResults());
					results.sort(new ResultComparator());
					for (Result r : results) {
						total++;
						if (param == null) {
							count++;
							System.out.println(r.toString());
						} else if (r.getParams().equals(param)) {
							count++;
							System.out.println(r.toString());
						}
					}
					break;
				case "parameters":
					List<Parameter> params = new ArrayList<>();
					params.addAll(this.getSession().getSearchHandler().getParams());
					params.sort(new NamedObjectComparator());
					for (Parameter p : params) {
						count++;
						total++;
						System.out.println(p.toString());
					}
					break;
				default:
					UIUtils.modeUnknown(s[0],Arrays.asList("authors","messages",
							"labels","conversations","results","parameters"));
					break;
			}
			if (total > 0) {
				System.out.println(String.format("%d out of %d %s displayed", count, total, s[0]));
			}
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/**
	 * Read a conversation
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void read(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 1);
		try {
			Conversation conv = this.getSession().searchConversation(s[0]);
			List<Message> messageList = new ArrayList<>();
			messageList.addAll(this.getSession().getMessageList());
			messageList.sort(new DatedMessageComparator());
			for (Message m : messageList) {
				if (m.getConversation().equals(conv)) {
					System.out.println(String.format("%s %s: %s",
							m.getTimestamp().format(formatter),
							m.getAuthor().getName(), m.getContent()));
				}
			}
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
