package analyse.messageanalysis;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import analyse.exceptions.NotFoundException;
import analyse.session.Session;
import analyse.utils.OptionalToString;

public class Parameter extends NamedObject {
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyyMMddHHmm");
	private String author;
	private String authorLabels;
	private String labels;
	private String conversations;
	private LocalDateTime minDate;
	private LocalDateTime maxDate;
	private Reactions reactions = new Reactions();
	List<String> subParameters = new ArrayList<>();
	
	public Parameter(String name) {
		this.setName(name);
	}
	
	/**
	 * return true if message matches all present search parameters
	 * @param message analyse.messageanalysis.Message to match
	 * @return Boolean
	 */
	public Boolean matches(Message message) {
		if (this.author != null 
				&& !message.getAuthor().getName().matches(this.author)) {
			return false;
		}
		if (this.authorLabels != null 
				&& !message.getAuthor().matchesLabels(this.authorLabels)) {
			return false;
		}
		if (this.labels != null  
				&& !message.getConversation().matchesLabels(this.labels)) {
			return false;
		}
		if (this.conversations != null  
				&& !message.getConversation().getName().matches(this.conversations)) {
			return false;
		}
		if (this.minDate != null 
				&& message.getTimestamp().compareTo(minDate) < 0) {
			return false;
		}
		if (this.maxDate != null  
				&& message.getTimestamp().compareTo(maxDate) > 0) {
			return false;
		}
		if (!this.reactions.matches(message.getReactions())) {
			return false;
		}
		return true;
	}
	
	/**
	 * return true if author matches all present search parameters
	 * @param message analyse.messageanalysis.Author to match
	 * @return Boolean
	 */
	public Boolean matches(Author author) {
		if (this.author != null 
				&& author.getName().matches(this.author)) {
			return false;
		}
		if (this.authorLabels != null 
				&& author.matchesLabels(this.authorLabels)) {
			return false;
		}
		if (this.labels != null  
				&& author.matchesConversationLabel(this.labels)) {
			return false;
		}
		if (this.conversations != null 
				&& author.matchesConversation(conversations)) {
			return false;
		}
		return true;
	}
	
	public String toString() {
		String str = "";
		for (String p : this.subParameters) {
			str += ",\"" + p + "\"";
		}
		if (!str.isEmpty()) {
			str = str.substring(1);
		}
		return String.format("{\"name\":\"%s\",\"author\":%s,\"authorLabels\":%s,\"labels\":%s,\"conversations\":%s,\"minDate\":%s,\"maxDate\":%s,\n	\"reactions\":%s,	\n	\"subParameters\":[%s]}", 
				this.getName(),
				OptionalToString.toString(this.author),
				OptionalToString.toString(this.authorLabels),
				OptionalToString.toString(this.labels),
				OptionalToString.toString(this.conversations),
				OptionalToString.format(minDate, formatter),
				OptionalToString.format(maxDate, formatter),
				this.reactions.toString(),
				str);
	}
	
	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAuthorLabels() {
		return authorLabels;
	}

	public void setAuthorLabels(String authorLabels) {
		this.authorLabels = authorLabels;
	}

	public String getLabels() {
		return labels;
	}

	public void setLabels(String labels) {
		this.labels = labels;
	}

	public String getConversations() {
		return conversations;
	}

	public void setConversations(String conversations) {
		this.conversations = conversations;
	}

	public LocalDateTime getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(LocalDateTime maxDate) {
		this.maxDate = maxDate;
	}

	public LocalDateTime getMinDate() {
		return minDate;
	}

	public void setMinDate(LocalDateTime minDate) {
		this.minDate = minDate;
	}
	
	public Reactions getReactions() {
		return this.reactions;
	}
	
	public void setReactions(String key, Integer value) {
		this.reactions.set(key, value);
	}
	
	public void setReaction(Reactions reactions) {
		this.reactions = reactions;
	}
	
	/**
	 * add name of sub parameter
	 * @param s String name
	 */
	public boolean addSubParameters(String str) {
		for (String s : this.subParameters) {
			if (s.contentEquals(str)) {
				System.out.println(String.format("Sub Parameter %s already present for %s", 
						str, this.getName()));
			}
		}
		return true;
	}
	
	/**
	 * getter
	 * @return List<String> this.subParameters
	 */
	public List<String> getSubParameters() {
		return this.subParameters;
	}
	
	/**
	 * Get associated search parameters in session
	 * @param session Session 
	 * @return List<Parameter>
	 */
	public List<Parameter> getSubParameters(Session session) {
		List<Parameter> res = new ArrayList<>();
		for (String s : this.subParameters) {
			try {
				Parameter p = session.searchParameter(s);
				res.add(p);
			} catch (NotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
		return res;
	}
	
	/**
	 * copy all search parameters
	 * @param p
	 */
	private void copy(Parameter p) {
		this.author = p.getAuthor();
		this.authorLabels = p.getAuthorLabels();
		this.labels = p.getLabels();
		this.conversations = p.getConversations();
		this.maxDate = p.getMaxDate();
		this.minDate = p.getMinDate();
		this.reactions = p.getReactions();
	}
	
	/**
	 * Generate a List<Parameter> of sub parameters
	 * @param author boolean to split on author
	 * @param authorLabels boolean to split on authorLabels
	 * @param labels boolean to split on labels
	 * @param conversations boolean to split on conversations
	 * @param session Session to which to add parameters
	 * @return List<Parameter> of sub parameters
	 */
	public void generateSubParameters(boolean author, 
			boolean authorLabels, boolean labels, boolean conversations,
			Session session) {
		List<String> a;
		if (author && this.author != null) {
			a = Arrays.asList(this.author.split("\\|"));
			if (a.size()>1) {
				for (String s : a) {
					Parameter p = new Parameter(this.getName() + "_" + s);
					p.copy(this);
					p.setAuthor(s);
					if (session.getSearchHandler().addParams(p)) {
						this.getSubParameters().add(p.getName());
						p.generateSubParameters(false, authorLabels, labels, conversations, session);
					} 
				}
			}
		}
		if (authorLabels && this.authorLabels != null) {
			a = Arrays.asList(this.authorLabels.split("\\|"));
			if (a.size()>1) {
				for (String s : a) {
					Parameter p = new Parameter(this.getName() + "_" + s);
					p.copy(this);
					p.setAuthorLabels(s);
					if (session.getSearchHandler().addParams(p)) {
						this.getSubParameters().add(p.getName());
						p.generateSubParameters(author, false, labels, conversations, session);
					} 
				}
			}
		}
		if (labels && this.labels != null) {
			a = Arrays.asList(this.labels.split("\\|"));
			if (a.size()>1) {
				for (String s : a) {
					Parameter p = new Parameter(this.getName() + "_" + s);
					p.copy(this);
					p.setLabels(s);
					if (session.getSearchHandler().addParams(p)) {
						this.getSubParameters().add(p.getName());
						p.generateSubParameters(author, authorLabels, false, conversations, session);
					} 
				}
			}
		}
		if (conversations && this.conversations != null) {
			a = Arrays.asList(this.conversations.split("\\|"));
			if (a.size()>1) {
				for (String s : a) {
					Parameter p = new Parameter(this.getName() + "_" + s);
					p.copy(this);
					p.setConversations(s);
					if (session.getSearchHandler().addParams(p)) {
						this.getSubParameters().add(p.getName());
						p.generateSubParameters(author, authorLabels, labels, false, session);
					} 
				}
			}
		}
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Parameter) {
			Parameter param = (Parameter) other;
			return this.getName().contentEquals(param.getName());
		} else if (other instanceof String) {
			String str = (String) other;
			return this.getName().contentEquals(str);
		} else {
			return false;
		}
	}
	
	public static Parameter parse(JSONObject o) {
		Parameter res = new Parameter(o.getString("name"));
		if (!o.isNull("author")) {
			res.setAuthor(o.getString("author"));
		}
		if (!o.isNull("authorLabels")) {
			res.setAuthorLabels(o.getString("authorLabels"));
		}
		if (!o.isNull("labels")) {
			res.setLabels(o.getString("labels"));
		}
		if (!o.isNull("conversations")) {
			res.setConversations(o.getString("conversations"));
		}
		if (!o.isNull("minDate")) {
			res.setMinDate(LocalDateTime.parse(o.getString("minDate"), formatter));
		}
		if (!o.isNull("maxDate")) {
			res.setMaxDate(LocalDateTime.parse(o.getString("maxDate"), formatter));
		}
		if (!o.isNull("reactions")) {
			res.setReaction(Reactions.parse(o.getJSONObject("reactions")));
		}
		JSONArray subParameters = o.getJSONArray("subParameters");
		for (int i = 0; i < subParameters.length(); i++) {
			res.addSubParameters(subParameters.getString(i));
		}
		return res;
	}
}
