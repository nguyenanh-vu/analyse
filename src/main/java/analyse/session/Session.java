package analyse.session;

import java.util.ArrayList;
import java.util.List;

import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.LabelledObject;
import analyse.messageanalysis.Message;
import analyse.messageanalysis.Parameter;
import analyse.search.Result;
import analyse.search.SearchHandler;

/**
 * class representing user session
 */
public class Session extends LabelledObject {
	private List<Author> authorList = new ArrayList<>();
	private List<Message> messageList = new ArrayList<>();
	private List<Conversation> conversations = new ArrayList<>();
	private SessionFilesSystem fileSystem = new SessionFilesSystem();
	private Long counter = 0L;
	private SearchHandler searchHandler = new SearchHandler(this);
	private SessionPrinter printer = new SessionPrinter();
	
	public void restart() {
		this.authorList =  new ArrayList<>();
		this.messageList = new ArrayList<>();
		this.clearLabels();
		this.fileSystem.reset();
		this.conversations = new ArrayList<>();
		this.searchHandler.reset();
		this.printer.reset();
		this.printer.setSession(this);
		counter = 0L;
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
	 * @return List<analyse.messageanalysis.Conversation> this.conversations
	 */
	public List<Conversation> getConversations() {
		return this.conversations;
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
	 * @return SearchHandler this.searchHandler
	 */
	public SearchHandler getSearchHandler() {
		return this.searchHandler;
	}
	
	public SessionFilesSystem getFileSystem() {
		return this.fileSystem;
	}
	
	public SessionPrinter getPrinter() {
		return this.printer;
	}
	
	/**
	 * Incrementer for this.counter
	 */
	public void incr() {
		this.counter++;
	}
	
	/**
	 * search for label in this.labels
	 * @param str String name of label to find
	 * @return analyse.messageanalysis.Label
	 * @throws NotFoundException label not found
	 */
	public Label searchLabel(String str) throws NotFoundException {
		int index = this.getLabels().indexOf(new Label(str));
		if (index != -1) {
			return this.getLabels().get(index);
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
	 * search for message in this.messageList
	 * @param id Long id of message to search
	 * @return analyse.messageanalysis.Messagae
	 * @throws NotFoundException message not found
	 */
	public Message searchMessage(Long id) throws NotFoundException {
		for (Message message : this.messageList) {
			if (message.getId().equals(id)) {
				return message;
			}
		}
		throw new NotFoundException(String.format("Message with id: %s not found", id));
	}
	
	/**
	 * search for result in this.searchHandler.results
	 * @param id Long id of result to search
	 * @return analyse.search.Result
	 * @throws NotFoundException result not found
	 */
	public Result searchResult(Long id) throws NotFoundException {
		for (Result r : this.searchHandler.getResults()) {
			if (r.getId().equals(id)) {
				return r;
			}
		}
		throw new NotFoundException(String.format("Result with id: %d not found", id));
	}
	
	/**
	 * search for parameter in this.searchHandler.params
	 * @param name String name of parameter to search
	 * @return analysis.messageanalysis.Parameter
	 * @throws NotFoundException 
	 */
	public Parameter searchParameter(String name) throws NotFoundException {
		for (Parameter p : this.searchHandler.getParams()) {
			if (p.getName().contentEquals(name)) {
				return p;
			}
		}
		throw new NotFoundException(String.format("Search parameter with name: %s not found", name));
	}
}
