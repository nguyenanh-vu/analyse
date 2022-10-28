package analyse.session;

import java.util.List;

import analyse.exceptions.NotEnoughArgumentException;
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
	
	/**
	 * Merge two elements with command line
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void merge(String[] s) throws NotEnoughArgumentException {
		if (s.length < 3) {
			throw new NotEnoughArgumentException(String.join(" ", s), 3, s.length);
		} else {
			if (s[0].contentEquals("authors")) {
				Author a1;
				Author a2;
				try {
					a1 = this.getSession().searchAuthor(s[1]);
					a2 = this.getSession().searchAuthor(s[2]);
					this.mergeAuthor(a1, a2);
				} catch (NotFoundException e) {
					System.out.println(e.getMessage());
				}
			} else if (s[0].contentEquals("labels")) {
				Label a1;
				Label a2;
				try {
					a1 = this.getSession().searchLabel(s[1]);
					a2 = this.getSession().searchLabel(s[2]);
					this.mergeLabels(a1, a2);
				} catch (NotFoundException e) {
					System.out.println(e.getMessage());
				}
			} else if (s[0].contentEquals("conversations")) {
				Conversation a1;
				Conversation a2;
				try {
					a1 = this.getSession().searchConversation(s[1]);
					a2 = this.getSession().searchConversation(s[2]);
					this.mergeConversation(a1, a2);
				} catch (NotFoundException e) {
					System.out.println(e.getMessage());
				}
			} else {
				System.out.println(String
						.format("Mode \"%s\" unknown, expected authors|labels|conversations", s[0]));
			}
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
		this.getSession().getConversations().remove(conv2);
	}
	
	/**
	 * Label management with command line
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void label(String[] s) throws NotEnoughArgumentException {
		if (s.length < 3) {
			throw new NotEnoughArgumentException(String.join(" ", s), 3, s.length);
		} else {
			Author a;
			Label l;
			try {
				a = this.getSession().searchAuthor(s[1]);
				if (s[0].contentEquals("add")) {
					l = new Label(s[2]);
					if (this.getSession().getLabels().contains(l)) {
						l = this.getSession().searchLabel(s[2]);
					} else {
						this.getSession().getLabels().add(l);
					}
					a.addLabel(l);
					this.getSession().getLabels().add(l);
				} else if (s[0].contentEquals("remove")) {
					a.getLabels().remove(new Label(s[2]));
				} else {
					System.out.println(String
							.format("Mode \"%s\" unknown, expected add|remove", s[0]));
				}
			} catch (NotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Rename elements with command line
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void rename(String[] s) throws NotEnoughArgumentException {
		if (s.length < 3) {
			throw new NotEnoughArgumentException(String.join(" ", s), 3, s.length);
		} else {
			try {
				if (s[0].contentEquals("authors")) {
					Author a = this.getSession().searchAuthor(s[1]);
					a.setName(s[2]);
				} else if (s[0].contentEquals("labels")) {
					Label l = this.getSession().searchLabel(s[1]);
					l.setName(s[2]);
				} else if (s[0].contentEquals("conversations")) {
					Conversation c = this.getSession().searchConversation(s[1]);
					c.setName(s[2]);
				} else {
					System.out.println(String
							.format("Mode \"%s\" unknown, expected authors|labels|conversations", s[0]));
				}
			} catch (NotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
