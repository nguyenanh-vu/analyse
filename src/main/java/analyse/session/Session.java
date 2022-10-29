package analyse.session;

import java.util.ArrayList;
import java.util.List;

import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;

/**
 * class representing user session
 */
public class Session {
	private List<Author> authorList = new ArrayList<>();
	private List<Message> messageList = new ArrayList<>();
	private List<Label> labels = new ArrayList<>();
	private List<Conversation> conversations = new ArrayList<>();
	private String address = "";
	private String workdir = "";
	private Long counter = 0L;
	
	public void restart() {
		this.authorList =  new ArrayList<>();
		this.messageList = new ArrayList<>();
		this.labels = new ArrayList<>();
		this.conversations = new ArrayList<>();
		this.address = "";
	}
	
	/**
	 * getter
	 * @return List<analyse.messageanalysis.Author> this.authorList
	 */
	public List<Author> getAuthorList() {
		return this.authorList;
	}
	
	/**
	 * getter
	 * @return List<analyse.messageanalysis.Message> this.messageList
	 */
	public List<Message> getMessageList() {
		return this.messageList;
	}
	
	/**
	 * getter
	 * @return List<analyse.messageanalysis.Label> this.labels
	 */
	public List<Label> getLabels() {
		return this.labels;
	}
	
	/**
	 * getter
	 * @return List<analyse.messageanalysis.Conversation> this.conversations
	 */
	public List<Conversation> getConversations() {
		return this.conversations;
	}
	
	/**
	 * getter
	 * @return String this.adress
	 */
	public String getAddress() {
		return this.address;
	}
	
	/**
	 * getter
	 * @return Long this.counter
	 */
	public Long getCounter() {
		return this.counter;
	}
	
	/**
	 * getter
	 * @return String this.workdir
	 */
	public String getWorkdir() {
		return this.workdir;
	}
	
	/**
	 * Incrementer for this.counter
	 */
	public void incr() {
		this.counter += 1;
	}
	
	/**
	 * setter
	 * @param adr String save file address
	 */
	public void setAddress(String adr) {
		this.address = adr;
	}
	
	/**
	 * setter
	 * @param workdir String working directory
	 */
	public void setWorkdir(String workdir) {
		this.workdir = workdir;
	}
	
	/**
	 * search for label in this.labels
	 * @param str String name of label to find
	 * @return analyse.messageanalysis.Label
	 * @throws NotFoundException label not found
	 */
	public Label searchLabel(String str) throws NotFoundException {
		int index = this.labels.indexOf(new Label(str));
		if (index != -1) {
			return this.labels.get(index);
		} else {
			throw new NotFoundException(String.format("label %s not found", str));
		}
	}
	
	/**
	 * search for conversation in this.conversations
	 * @param str String name of conversation to find
	 * @return analyse.messageanalysis.Conversation
	 * @throws NotFoundException conversation not found
	 */
	public Conversation searchConversation(String str) throws NotFoundException {
		int index = this.conversations.indexOf(new Conversation(str));
		if (index != -1) {
			return this.conversations.get(index);
		} else {
			throw new NotFoundException(String.format("conversation %s not found", str));
		}
	}
	
	/**
	 * search for author in this.authorList
	 * @param str String name of author to find
	 * @return analyse.messageanalysis.Author
	 * @throws NotFoundException author not found
	 */
	public Author searchAuthor(String str) throws NotFoundException {
		int index = this.authorList.indexOf(new Author(str));
		if (index != -1) {
			return this.authorList.get(index);
		} else {
			throw new NotFoundException(String.format("author %s not found", str));
		}
	}
	
	/**
	 * Attach label to author in this.authorList
	 * @param author analyse.messageanalysis.Author
	 * @param label String
	 */
	public void labelAuthor(String author, String label) {
		try {
			Author a = this.searchAuthor(author);
			this.labelAuthor(a, label);
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Attach label to analyse.messageanalysis.Author
	 * @param author analyse.messageanalysis.Author
	 * @param label String
	 */
	public void labelAuthor(Author author, String label) {
		Label l;
		try {
			l = this.searchLabel(label);
		} catch (NotFoundException e) {
			l = new Label(label);
			this.labels.add(l);
		}
		author.addLabel(l);
	}
	
	/**
	 * Remove single label from author
	 * @param author analyse.messageanalysis.Author
	 * @param label analyse.messageanalysis.Label
	 */
	public void removeLabelAuthor(String author, String label) {
		try {
			Author a = this.searchAuthor(author);
			Label l = this.searchLabel(label);
			a.removeLabel(l);
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Merge two authors
	 * @param author1 analyse.messageanalysis.Author in which to merge
	 * @param author2 analyse.messageanalysis.Author to merge and delete
	 */
	public void mergeAuthor(String author1, String author2) {
		try {
			Author a1 = this.searchAuthor(author1);
			Author a2 = this.searchAuthor(author2);
			for (Message message : this.messageList) {
				if (message.getAuthor().equals(a2)) {
					message.setAuthor(a1);
				}
			}
			for (Label label : a2.getLabels()) {
				if (!a1.getLabels().contains(label)) {
					a1.getLabels().add(label);
				}
			}
			this.authorList.remove(a2);
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
}
