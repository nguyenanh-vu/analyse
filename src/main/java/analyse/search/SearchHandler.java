package analyse.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import analyse.exceptions.NotEnoughArgumentException;
import analyse.messageanalysis.Message;
import analyse.session.Session;
import analyse.session.SessionTools;

public class SearchHandler extends SessionTools {
	Long counter = 0L;
	List<Result> results = new ArrayList<>();
	
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
	 * add a analyse.search.Result to this.results
	 * @param r
	 */
	public void addResult(Result r) {
		this.results.add(r);
		this.counter++;
	}
	
	/**
	 * Search regex in all messages
	 * @param str String regex to search
	 * @return Map<Long,Integer> message.id : number of occurrences
	 */
	public Map<Long, Integer> simpleSearch(String str) {
		Map<Long, Integer> res = new HashMap<>();
		for (Message message : this.getSession().getMessageList()) {
			int count = message.count(str);
			if (count != 0) {
				res.put(message.getId(), count);
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
}
