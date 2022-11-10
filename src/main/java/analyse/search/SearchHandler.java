package analyse.search;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import analyse.UI.UIUtils;
import analyse.exceptions.NotEnoughArgumentException;
import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Message;
import analyse.messageanalysis.Parameter;
import analyse.messageanalysis.Reactions;
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
	
	public void reset() {
		results = new ArrayList<>();
		params = new ArrayList<>();
		counter = 0L;
	}
	
	/**
	 * use search engine from command line
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void search(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 2);
		String regex = s[1];
		Parameter p = null;
		if (s.length > 2) {
			try {
				p = this.getSession().searchParameter(s[2]);
			} catch (NotFoundException e) {
				System.out.println(e.getMessage());
			}
		}
		if (s[0].contentEquals("simple")) {
			this.simpleSearch(regex, p);
		} else {
			UIUtils.modeUnknown(s[0], Arrays.asList("simple"));
		}
	}
	
	/**
	 * manage search parameters using command line
	 * @param s
	 * @throws NotEnoughArgumentException 
	 */
	public void params(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 2);
		if (s[0].contentEquals("add")) {
			this.addParams(new Parameter(s[1]));
		} else {
			try {
				List<String> possibleParams = new LinkedList<String>(Arrays.asList("minDate", "maxDate"));
				possibleParams.addAll(Parameter.possibleKeys);
				possibleParams.addAll(Reactions.possibleKeys);
				Parameter p = this.getSession().searchParameter(s[1]);
				switch (s[0]) {
					case "generate":
						UIUtils.notEnoughArguments(s, 6);
						p.generateSubParameters(Boolean.valueOf(s[2]), Boolean.valueOf(s[3]), 
								Boolean.valueOf(s[4]), Boolean.valueOf(s[5]), this.getSession());
						break;
					case "set":
						UIUtils.notEnoughArguments(s, 4);
						String value = s[3];
						if (Parameter.possibleKeys.contains(s[2])) {
							p.set(s[2], value);
						} else if (s[2].contentEquals("minDate")) {
							p.setMinDate(LocalDateTime.parse(value, formatter));
						} else if (s[2].contentEquals("maxDate")) {
							p.setMaxDate(LocalDateTime.parse(value, formatter));
						} else if (Reactions.possibleKeys.contains(s[2])) {
							p.setReactions(s[2], Integer.valueOf(value));
						} else {
							UIUtils.parameterUnknown(s[2], possibleParams);
						}
						break;
					case "remove":
						UIUtils.notEnoughArguments(s, 3);
						if (Parameter.possibleKeys.contains(s[2])) {
							p.set(s[2], null);
						} else if (s[2].contentEquals("minDate")) {
							p.setMinDate(null);
						} else if (s[2].contentEquals("maxDate")) {
							p.setMaxDate(null);
						} else if (Reactions.possibleKeys.contains(s[2])) {
							p.setReactions(s[2], 0);
						} else {
							UIUtils.parameterUnknown(s[2], possibleParams);
						}
						break;
					default:
						UIUtils.modeUnknown(s[0], Arrays.asList("add","set","remove", "generate"));
						break;
				}
			} catch (NotFoundException e) {
				System.out.println(e.getMessage());
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
	public void simpleSearch(String str, Parameter params) {
		Map<Message, Integer> res = new HashMap<>();
		Integer total = 0;
		for (Message message : this.getSession().getMessageList()) {
			total ++;
			if (params == null || params.matches(message)) {
				int count = message.count(str);
				if (count != 0) {
					res.put(message, count);
				}
			}
		}
		SimpleResult result = new SimpleResult(res, this.counter, 
				str, total, params);
		this.results.add(result);
		System.out.println(result.toString());
		this.counter++;
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
