package analyse.search;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import analyse.exceptions.NotEnoughArgumentException;
import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Message;
import analyse.messageanalysis.Parameter;
import analyse.session.Session;
import analyse.session.SessionTools;

public class SearchHandler extends SessionTools {
	private static final DateTimeFormatter formatter = 
			DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm");
	Long counter = 0L;
	List<Result> results = new ArrayList<>();
	List<Parameter> params = new ArrayList<>();
	
	public SearchHandler(Session session) {
		this.setSession(session);
	}
	
	/**
	 * use search engine from command line
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void search(String[] s) throws NotEnoughArgumentException {
		if (s.length < 2) {
			throw new NotEnoughArgumentException(String.join(" ", s), 2, s.length);
		} else {
			String regex = s[1];
			if (s[0].contentEquals("simple")) {
				SimpleResult res = new SimpleResult(
						this.simpleSearch(regex),
						this.counter, regex, this.getSession().getMessageList().size());
				this.results.add(res);
				System.out.println(res.getInfo());
				counter++;
			} else {
				System.out.println(String
						.format("Mode \"%s\" unknown, expected simple", s[0]));
			}
		}
	}
	
	/**
	 * manage search parameters using command line
	 * @param s
	 * @throws NotEnoughArgumentException 
	 */
	public void params(String[] s) throws NotEnoughArgumentException {
		if (s.length < 2) {
			throw new NotEnoughArgumentException(String.join(" ", s), 2, s.length);
		} else {
			if (s[0].contentEquals("add")) {
				this.addParams(new Parameter(s[1]));
			} else {
				try {
					Parameter p = this.getSession().searchParameter(s[1]);
					if (s[0].contentEquals("generate")) {
						if (s.length < 6) {
							throw new NotEnoughArgumentException(String.join(" ", s), 6, s.length);
						}
						p.generateSubParameters(Boolean.valueOf(s[2]), Boolean.valueOf(s[3]), 
								Boolean.valueOf(s[4]), Boolean.valueOf(s[5]), this.getSession());
					} else if (s[0].contentEquals("set")) {
						if (s.length < 4) {
							throw new NotEnoughArgumentException(String.join(" ", s), 4, s.length);
						}
						String value = s[3];
						if (s[2].contentEquals("author")) {
							p.setAuthor(value);
						} else if (s[2].contentEquals("authorLabels")) {
							p.setAuthorLabels(value);
						} else if (s[2].contentEquals("labels")) {
							p.setLabels(value);
						} else if (s[2].contentEquals("conversations")) {
							p.setConversations(value);
						} else if (s[2].contentEquals("minDate")) {
							p.setMinDate(LocalDateTime.parse(value, formatter));
						} else if (s[2].contentEquals("maxDate")) {
							p.setMaxDate(LocalDateTime.parse(value, formatter));
						} else {
							System.out.println(String
									.format("Parameter \"%s\" unknown, expected author|authorLabels|labels|conversations|minDate|maxDate", s[2]));
						}
					} else if (s[0].contentEquals("remove")) {
						if (s.length < 3) {
							throw new NotEnoughArgumentException(String.join(" ", s), 4, s.length);
						}
						if (s[2].contentEquals("author")) {
							p.setAuthor(null);
						} else if (s[2].contentEquals("authorLabels")) {
							p.setAuthorLabels(null);
						} else if (s[2].contentEquals("labels")) {
							p.setLabels(null);
						} else if (s[2].contentEquals("conversations")) {
							p.setConversations(null);
						} else if (s[2].contentEquals("minDate")) {
							p.setMinDate(null);
						} else if (s[2].contentEquals("maxDate")) {
							p.setMaxDate(null);
						} else {
							System.out.println(String
									.format("Parameter \"%s\" unknown, expected author|authorLabels|labels|conversations|minDate|maxDate", s[0]));
						}
					} else {
						System.out.println(String
								.format("Mode \"%s\" unknown, expected add|set|remove", s[0]));
					}
				} catch (NotFoundException e) {
					System.out.println(e.getMessage());
				}
			}
		}
	}
	
	/**
	 * add a analyse.search.Result to this.results
	 * @param r
	 */
	public void addResult(Result r) {
		this.results.add(r);
		this.counter++;
	}
	
	/**
	 * add a analyse.messageanalysis.Parameter to this.params
	 * @param p
	 */
	public boolean addParams(Parameter p) {
		if (this.params.contains(p)) {
			System.out.println(String.format("Parameter with name %s already exists", p.getName()));
			return false;
		} else {
			this.params.add(p);
			return true;
		}
	}
	
	/**
	 * Search regex in all messages
	 * @param str String regex to search
	 * @return Map<Long,Integer> message.id : number of occurrences
	 */
	public Map<Message, Integer> simpleSearch(String str) {
		Map<Message, Integer> res = new HashMap<>();
		for (Message message : this.getSession().getMessageList()) {
			int count = message.count(str);
			if (count != 0) {
				res.put(message, count);
			}
		}
		return res;
	}
	
	/**
	 * getter
	 * @return List<Result> this.results
	 */
	public List<Result> getResults() {
		return this.results;
	}
	
	/**
	 * getter
	 * @return List<Parameter> this.params
	 */
	public List<Parameter> getParams() {
		return this.params;
	}
}
