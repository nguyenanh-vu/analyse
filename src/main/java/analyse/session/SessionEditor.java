package analyse.session;

import java.util.Arrays;
import java.util.List;

import analyse.UI.UIUtils;
import analyse.exceptions.NotEnoughArgumentException;
import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.LabelledObject;
import analyse.messageanalysis.Message;

public class SessionEditor extends SessionTools {
	/**
	 * Add a analyse.messageanalysis.Message to this.session
	 * @param message
	 */
	public void addMessage(Message message) {
		this.addAuthor(message.getAuthor());
		Author a = message.getAuthor();
		try {
			a = this.getSession().searchAuthor(message.getAuthor().getName());
		} catch (NotFoundException e) {
			SessionPrinter.printException(e);
		}
		Conversation conv = message.getConversation();
		this.addConversation(conv);
		try {
			conv = this.getSession()
					.searchConversation(message.getConversation().getName());
		} catch (NotFoundException e) {
			SessionPrinter.printException(e);
		}
		Message newMessage = new Message(this.getSession().getCounter(), message.getTimestamp(), 
				a, message.getContent(), conv, message.getReactions());
		if (!this.getSession().getMessageList().contains(newMessage)) {
			this.getSession().getMessageList().add(newMessage);
		}
		this.getSession().incr();
	}
	
	/**
	 * Add a analyse.messageanalysis.Conversation to this.session if not already present
	 * @param conv Conversation to add
	 */
	public void addConversation(Conversation conv) {
		try {
			Conversation c = this.getSession().searchConversation(conv.getName());
			for (Label l : conv.getLabels()) {
				c.addLabel(l);
				this.addLabel(l);
			}
		} catch (NotFoundException e) {
			this.getSession().getConversations().add(conv);
			for (Label l : conv.getLabels()) {
				this.addLabel(l);
			}
		}
	}
	
	/**
	 * Add a analyse.messageanalysis.Label to this.session if not already present
	 * @param label Label to add
	 */
	public void addLabel(Label label) {
		if (!this.getSession().getLabels().contains(label)) {
			this.getSession().getLabels().add(label);
		}
	}
	
	/**
	 * Add a analyse.messageanalysis.Author to this.session if not already present
	 * Edit existing author if already present
	 * @param author
	 */
	public void addAuthor(Author author) {
		for (Label label : author.getLabels()) {
			this.addLabel(label);
		}
		for (Conversation conv : author.getConversations()) {
			this.addConversation(conv);
		}
		List<Author> authorList = this.getSession().getAuthorList();
		if (!authorList.contains(author)) {
			authorList.add(author);
		} else {
			Author a = author;
			try {
				a = this.getSession().searchAuthor(author.getName());
				for (Label label : author.getLabels()) {
					a.addLabel(label);
				}
				for (Conversation conv : author.getConversations()) {
					a.addConversation(conv);
				}
			} catch (NotFoundException e) {
				SessionPrinter.printException(e);
			}
		}
	}
	
	/**
	 * Merge two elements with command line
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void merge(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 3);
		try {
			switch (s[0]) {
				case "authors":
					Author a1 = this.getSession().searchAuthor(s[1]);
					Author a2 = this.getSession().searchAuthor(s[2]);
					this.mergeAuthor(a1, a2);
					break;
				case "labels":
					Label l1 = this.getSession().searchLabel(s[1]);
					Label l2 = this.getSession().searchLabel(s[2]);
					this.mergeLabels(l1, l2);
					break;
				case "conversations":
					Conversation c1 = this.getSession().searchConversation(s[1]);
					Conversation c2 = this.getSession().searchConversation(s[2]);
					this.mergeConversation(c1, c2);
					break;
				default:
					UIUtils.modeUnknown(s[0], Arrays.asList("authors",
							"labels","conversation"));
					break;
			}
		}catch (NotFoundException e) {
			SessionPrinter.printException(e);
		}
	}
	
	/**
	 * Merge two authors
	 * @param author1 analyse.messageanalysis.Author in which to merge
	 * @param author2 analyse.messageanalysis.Author to merge and delete
	 */
	public void mergeAuthor(Author author1, Author author2) {
		for (Message message : this.getSession().getMessageList()) {
			if (message.getAuthor().equals(author2)) {
				message.setAuthor(author1);
			}
		}
		for (Conversation conv : author2.getConversations()) {
			author1.addConversation(conv);
		}
		for (Label label : author2.getLabels()) {
			author1.addLabel(label);
		}
		this.getSession().getAuthorList().remove(author2);
	}
	
	/**
	 * Merge two labels
	 * @param label1 analyse.messageanalysis.Label in which to merge
	 * @param label2 analyse.messageanalysis.Label to merge and delete
	 */
	public void mergeLabels(Label label1, Label label2) {
		for (Author author : this.getSession().getAuthorList()) {
			author.addLabel(label1);
			author.removeLabel(label2);
		}
		for (Conversation conv: this.getSession().getConversations()){
			conv.addLabel(label1);
			conv.removeLabel(label2);
		}
		this.getSession().getLabels().remove(label2);
	}
	
	/**
	 * Merge two conversations
	 * @param conv1 analyse.messageanalysis.Conversation in which to merge
	 * @param conv2 analyse.messageanalysis.Conversation to merge and delete
	 */
	public void mergeConversation(Conversation conv1, Conversation conv2) {
		for (Message message : this.getSession().getMessageList()) {
			if (message.getConversation().equals(conv2)) {
				message.setConversation(conv1);
			}
		}
		for (Author author : this.getSession().getAuthorList()) {
			author.addConversation(conv1);
			author.removeConversation(conv2);
		}
		for (Label l : conv2.getLabels()) {
			conv1.addLabel(l);
		}
		this.getSession().getConversations().remove(conv2);
	}
	
	/**
	 * Label management with command line
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void label(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 3);
		try {
			LabelledObject o;
			Label l;
			switch (s[1]) {
				case "authors":
					o = this.getSession().searchAuthor(s[2]);
					break;
				case "conversations":
					o = this.getSession().searchConversation(s[2]);
					break;
				default:
					UIUtils.modeUnknown(s[1], Arrays.asList("authors", "conversations"));
					throw new Exception();
			}
			switch (s[0]) {
				case "add":
					UIUtils.notEnoughArguments(s, 4);
					l = new Label(s[3]);
					if (this.getSession().getLabels().contains(l)) {
						l = this.getSession().searchLabel(s[3]);
					} else {
						this.addLabel(l);
					}
					o.addLabel(l);
					break;
				case "remove":
					UIUtils.notEnoughArguments(s, 4);
					o.removeLabel(new Label(s[3]));
					break;
				case "clear":
					UIUtils.notEnoughArguments(s, 4);
					o.clearLabels();
					break;
				default:
					UIUtils.modeUnknown(s[0], Arrays.asList("add", "remove", "clear"));
					break;
			}
		} catch (Exception e) {
			SessionPrinter.printException(e);
		}
	}
	
	/**
	 * Rename elements with command line
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void rename(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 3);
		try {
			switch (s[0]) {
				case "authors":
					Author a = this.getSession().searchAuthor(s[1]);
					a.setName(s[2]);
					break;
				case "labels":
					Label l = this.getSession().searchLabel(s[1]);
					l.setName(s[2]);
					break;
				case "conversations":
					Conversation c = this.getSession().searchConversation(s[1]);
					c.setName(s[2]);
					break;
				default:
					UIUtils.modeUnknown(s[0], Arrays.asList("authors",
							"labels","conversation"));
					break;
			}
		} catch (NotFoundException e) {
			SessionPrinter.printException(e);
		}
	}
	
	/**
	 * anonymise data, replacing authors and conversation names by counter
	 * @param s String[] arguments
	 */
	public void anonymise(String[] s) {
		int authors = 0;
		int conv = 0;
		for (Author a : this.getSession().getAuthorList()) {
			a.setName(String.valueOf(authors));
			authors++;
		}
		for (Conversation c : this.getSession().getConversations()) {
			c.setName(String.valueOf(conv));
			conv++;
		}
	}
}
