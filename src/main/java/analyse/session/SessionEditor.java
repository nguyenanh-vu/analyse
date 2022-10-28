package analyse.session;

import java.util.List;

import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
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
			System.out.println(e.getMessage());
		}
		Conversation conv;
		try {
			conv = this.getSession()
					.searchConversation(message.getConversation().getName());
		} catch (NotFoundException e) {
			conv = message.getConversation();
		}
		this.getSession().getMessageList()
		.add(new Message(this.getSession().getCounter(), 
				message.getTimestamp(), a, message.getContent(), conv));
		this.getSession().incr();
	}
	
	/**
	 * Add a analyse.messageanalysis.Conversation to this.session if not already present
	 * @param conv
	 */
	public void addConversation(Conversation conv) {
		if (!this.getSession().getConversations().contains(conv)) {
			this.getSession().getConversations().add(conv);
		}
	}
	
	/**
	 * Add a analyse.messageanalysis.Label to this.session if not already present
	 * @param conv
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
				System.out.println(e.getMessage());
			}
		}
	}
}
