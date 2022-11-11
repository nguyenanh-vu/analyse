package analyse.utils;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Utils for JSON data treatment
 */
public class JSONUtils {
	public static String indent(String str) {
		String res = "	" + str;
		res = res.replace("\n", "\n	");
		return res;
	}
	
	public static JSONObject convert(org.json.simple.JSONObject o){
		JSONObject res = new JSONObject();
		@SuppressWarnings("unchecked")
		List<String> keys = new ArrayList<>(o.keySet());
		for (String k : keys) {
			if (o.get(k) instanceof org.json.simple.JSONObject) {
				res.put(k, JSONUtils.convert((org.json.simple.JSONObject)o.get(k)));
			} else if (o.get(k) instanceof org.json.simple.JSONArray) {
				res.put(k, JSONUtils.convert((org.json.simple.JSONArray)o.get(k)));
			} else {
				res.put(k, o.get(k));
			}
		}
		return res;
	}
	
	public static JSONArray convert(org.json.simple.JSONArray o) {
		JSONArray res = new JSONArray();
		for (int i = 0; i < o.size(); i++) {
			if (o.get(i) instanceof org.json.simple.JSONObject) {
				res.put(JSONUtils.convert((org.json.simple.JSONObject)o.get(i)));
			} else if (o.get(i) instanceof org.json.simple.JSONArray) {
				res.put(JSONUtils.convert((org.json.simple.JSONArray)o.get(i)));
			} else {
				res.put(o.get(i));
			}
		}
		return res;
	}
}
