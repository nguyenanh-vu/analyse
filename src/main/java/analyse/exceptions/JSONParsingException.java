package analyse.exceptions;

import org.json.JSONObject;

public class JSONParsingException extends Exception {
	private static final long serialVersionUID = 2352967660699755952L;
	
	public JSONParsingException(JSONObject o, String errorMessage) {
		super(String.format("error parsing object: %s with error:%s",o.toString(), errorMessage));
	}
}
