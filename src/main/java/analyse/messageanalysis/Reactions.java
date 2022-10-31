package analyse.messageanalysis;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class Reactions {
	Map<String, Integer> map;
	public static List<String> possibleKeys = Arrays.asList("laugh",
			"laughTears", "love", "thumbs", "smile", "surprise", "tears", "angry");
	
	/**
	 * no args constructor
	 */
	public Reactions() {
		this.map = new HashMap<>();
		for (String s : Reactions.possibleKeys) {
			this.map.put(s, 0);
		}
	}
	
	/**
	 * all args constructor
	 * @param laugh
	 * @param laughTears
	 * @param love
	 * @param thumbs
	 * @param smile
	 * @param surprise
	 * @param tears
	 */
	public Reactions(Integer laugh, Integer laughTears,
			Integer love, Integer thumbs, Integer smile,
			Integer surprise, Integer tears, Integer angry) {
		this.map = new HashMap<>();
		this.map.put("laugh", laugh);
		this.map.put("laughTears", laughTears);
		this.map.put("love", love);
		this.map.put("thumbs", thumbs);
		this.map.put("smile", smile);
		this.map.put("surprise", surprise);
		this.map.put("tears", tears);
		this.map.put("angry", angry);
	}
	
	public Integer get(String key) {
		return this.map.get(key);
	}
	
	public void set(String key, Integer value) {
		this.map.put(key, value);
	}
	
	public void incr(String key) {
		this.map.put(key, this.map.get(key) + 1);
	}
	
	public String toString() {
		List<String> str = new ArrayList<>();
		for (Map.Entry<String, Integer> entry : this.map.entrySet()) {
			str.add(String.format("\"%s\":%d", entry.getKey(), entry.getValue()));
		}
		return String.join(",", str);
	}
	
	public static Reactions parse(JSONObject o) {
		Reactions r = new Reactions();
		for (String s : Reactions.possibleKeys) {
			if (o.has(s)) {
				r.set(s, o.getInt(s));
			}
		}
		return r;
	}
	
	public Boolean matches(Reactions other) {
		for (String s : Reactions.possibleKeys) {
			if (this.map.get(s) > other.get(s)) {
				return false;
			}
		}
		return true;
	}
}
