package analyse.session;

import java.util.ArrayList;
import java.util.List;

import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;
import analyse.utils.MessengerUtils;
import analyse.utils.WhatsappUtils;

public class Session {
	private List<Author> authorList = new ArrayList<>();
	private List<Message> messageList = new ArrayList<>();
	private List<Label> labels = new ArrayList<>();
	
	public List<Author> getAuthorList() {
		return this.authorList;
	}
	
	public List<Message> getMessageList() {
		return this.messageList;
	}
	
	public List<Label> getLabels() {
		return this.labels;
	}
	
	public void loadWhatsapp(String path, List<Label> labels, String conversation) {
		WhatsappUtils.load(path, this.messageList, this.authorList, labels, conversation);
		for (Label l : labels) {
			if (!this.labels.contains(l)) {
				this.labels.add(l);
			}
		}
	} 
	
	public void loadFb(String path, List<Label> labels, String conversation) {
		MessengerUtils.load(path, this.messageList, this.authorList, labels, conversation);
		for (Label l : labels) {
			if (!this.labels.contains(l)) {
				this.labels.add(l);
			}
		}
	} 
	
	private Label searchLabel(String str) throws NotFoundException {
		int index = this.labels.indexOf(new Label(str));
		if (index != -1) {
			return this.labels.get(index);
		} else {
			throw new NotFoundException(String.format("label %s not found", str));
		}
	}
	
	private Author searchAuthor(String str) throws NotFoundException {
		int index = this.authorList.indexOf(new Author(str));
		if (index != -1) {
			return this.authorList.get(index);
		} else {
			throw new NotFoundException(String.format("author %s not found", str));
		}
	}
	
	public void labelAuthor(String author, String label) {
		try {
			Author a = this.searchAuthor(author);
			Label l;
			try {
				l = this.searchLabel(label);
			} catch (NotFoundException e) {
				l = new Label(label);
				this.labels.add(l);
			}
			a.addLabel(l);
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void removeLabelAuthor(String author, String label) {
		try {
			Author a = this.searchAuthor(author);
			Label l = this.searchLabel(label);
			a.removeLabel(l);
		} catch (NotFoundException e) {
			System.out.println(e.getMessage());
		}
	}
	
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
