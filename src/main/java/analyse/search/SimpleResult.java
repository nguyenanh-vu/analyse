package analyse.search;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import analyse.exceptions.NotFoundException;
import analyse.messageanalysis.Message;
import analyse.messageanalysis.Parameter;
import analyse.session.Session;
import analyse.utils.JSONUtils;

public class SimpleResult implements Result{
	Long id;
	Map<Message,Integer> resultSet;
	Integer total;
	Double avg;
	Double var;
	String regex;
	Parameter params;
	
	public SimpleResult(Map<Message,Integer> resultSet, 
			Long id, String regex, int s, Parameter params) {
		this.id = id;
		this.regex = regex;
		this.resultSet = resultSet;
		this.params = params;
		int size = s;
		this.total = 0;
		double var = 0.0;
		for (Map.Entry<Message,Integer> entry : resultSet.entrySet()) {
			this.total += entry.getValue();
		}
		this.avg = ((double) this.total) / size;
		for (Map.Entry<Message,Integer> entry : resultSet.entrySet()) {
			var += Math.pow(entry.getValue() - this.avg, 2);
		}
		this.var = var / size;
	}
	
	/**
	 * All arguments constructor
	 * @param resultSet
	 * @param id
	 * @param regex
	 * @param total
	 * @param avg
	 * @param var
	 * @param params
	 */
	public SimpleResult(Map<Message,Integer> resultSet, Long id, 
			String regex, Integer total, Double avg, Double var, Parameter params) {
		this.id = id;
		this.regex = regex;
		this.resultSet = resultSet;
		this.total = total;
		this.avg = avg;
		this.var = var;
		this.params = params;
	}
	
	@Override
	public Map<Message, Integer> getResults() {
		return this.resultSet;
	}

	@Override
	public String getType() {
		return "SIMPLE";
	}

	@Override
	public Double getAvg() {
		return this.avg;
	}

	@Override
	public Double getVar() {
		return this.var;
	}

	@Override
	public Long getId() {
		return this.id;
	}

	@Override
	public String toString() {
		return String.format("id: %d, type: SIMPLE, regex: \"%s\",total: %d, average: %f, variance: %f", 
				this.id, this.regex, this.total, this.avg, this.var);
	}
	
	public String toJSON() {
		List<String> res = new ArrayList<>();
		for (Map.Entry<Message,Integer> entry : resultSet.entrySet()) {
			res.add(String.format("{\"id\":%d,\"value\":%d}"
					,entry.getKey().getId(), entry.getValue()));
		}
		StringBuilder str = new StringBuilder();
		str.append(String.format("\"id\":%d,\"type\":\"SIMPLE\",", this.id));
		str.append(String.format("\"regex\":\"%s\",", this.regex));
		str.append(String.format("\"total\":%d,", this.total));
		str.append(String.format("\"average\":%f,", this.avg));
		str.append(String.format("\"variance\":%f,", this.var));
		str.append(String.format("\"params\":%s,", 
				this.params == null ? "null" : "\"" + this.params.getName() +"\""));
		str.append(String.format("\n\"results\":[\n%s\n]", 
				JSONUtils.indent(String.join(",\n", res))));
		return "{" + str.toString() + "}";
	}

	@Override
	public Parameter getParams() {
		return this.params;
	}
	
	/**
	 * parse result from 
	 * @param o
	 * @return
	 * @throws NotFoundException 
	 * @throws JSONException 
	 */
	public static SimpleResult parse(JSONObject jo, Session session) throws JSONException, NotFoundException {
		Long id = jo.getLong("id");
		String regex = jo.getString("regex");
		Integer total = jo.getInt("total");
		Double avg = jo.getDouble("average");
		Double var = jo.getDouble("variance");
		Parameter params = null;
		if (!jo.isNull("params")) {
			params = session.searchParameter(jo.getString("params"));
		}
		Map<Message,Integer> resultSet = new HashMap<>();
		JSONArray results = jo.getJSONArray("results");
		for (int i = 0; i < results.length(); i++) {
			JSONObject o = results.getJSONObject(i);
			Message message = session.searchMessage(o.getLong("id"));
			resultSet.put(message, o.getInt("value"));
		}
		return new SimpleResult(resultSet, id, regex, total, avg, var, params);
	}

	@Override
	public Integer getTotal() {
		return this.total;
	}

	@Override
	public int compareTo(Result o) {
		return this.getId().compareTo(o.getId());
	}
}
