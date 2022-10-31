package analyse.messageanalysis;

import java.util.Arrays;
import java.util.List;

import org.json.JSONObject;

public class Reactions {
	Integer laugh = 0;
	Integer laughTears = 0;
	Integer love = 0;
	Integer thumbs = 0;
	Integer smile = 0;
	Integer surprise = 0;
	Integer tears = 0;
	Integer angry = 0;
	public static List<String> possibleKeys = Arrays.asList("laugh",
			"laughTears", "love", "thumbs", "smile", "surprise", "tears", "angry");
	
	/**
	 * no args constructor
	 */
	public Reactions() {
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
		this.laugh = laugh;
		this.laughTears = laughTears;
		this.love = love;
		this.thumbs = thumbs;
		this.smile = smile;
		this.surprise = surprise;
		this.tears = tears;
		this.angry = angry;
	}
	
	/**
	 * add a reaction from Facebook Messenger message
	 * @param str
	 */
	public void addFbReactions(String str) {
		switch (str) {
		case "\u00f0\u009f\u0098\u0086":
			this.laugh++;
			break;
		case "\u00f0\u009f\u0098\u0082":
			this.laughTears++;
			break;
		case "\u00e2\u009d\u00a4":
			this.love++;
			break;
		case "\u00f0\u009f\u0091\u008d":
			this.thumbs++;
			break;
		case "\u00f0\u009f\u0099\u0082":
			this.smile++;
			break;
		case "\u00f0\u009f\u0098\u00ae":
			this.surprise++;
			break;
		case "\u00f0\u009f\u0098\u00a2":
			this.tears++;
			break;
		case "\u00f0\u009f\u0098\u00a0":
			this.angry++;
			break;
		default:
			break;
		}
	}
	
	public Integer getLaugh() {
		return this.laugh;
	}
	
	public Integer getLaughTears() {
		return this.laughTears;
	}
	
	public Integer getLove() {
		return this.love;
	}
	
	public Integer getThumbs() {
		return this.thumbs;
	}
	
	public Integer getSmile() {
		return this.smile;
	}
	
	public Integer getSurprise() {
		return this.surprise;
	}
	
	public Integer getTears() {
		return this.tears;
	}
	
	public Integer getAngry() {
		return this.angry;
	}
	
	public void set(String key, Integer value) {
		switch (key) {
		case "laugh":
			this.laugh = value;
			break;
		case "laughTears":
			this.laughTears = value;
			break;
		case "love":
			this.love = value;
			break;
		case "thumbs":
			this.thumbs = value;
			break;
		case "smile":
			this.smile = value;
			break;
		case "surprise":
			this.surprise = value;
			break;
		case "tears":
			this.tears = value;
			break;
		case "angry":
			this.angry = value;
			break;
		default:
			break;
		}
	}
	
	public String toString() {
		return String.format("{\"laugh\":%d,\"laughTears\":%d,\"love\":%d,\"thumbs\":%d,\"smile\":%d,\"surprise\":%d,\"tears\":%d,\"angry\":%d}", 
				this.laugh,
				this.laughTears,
				this.love,
				this.thumbs,
				this.smile,
				this.surprise,
				this.tears,
				this.angry);
	}
	
	public static Reactions parse(JSONObject o) {
		return new Reactions(o.getInt("laugh"), o.getInt("laughTears"),
				o.getInt("love"), o.getInt("thumbs"), o.getInt("smile"),
				o.getInt("surprise"), o.getInt("tears"), o.getInt("angry"));
	}
	
	public Boolean matches(Reactions other) {
		return this.laugh <= other.getLaugh()
				&& this.laughTears <= other.getLaughTears()
				&& this.love <= other.getLove()
				&& this.thumbs <= other.getThumbs()
				&& this.smile <= other.getSmile()
				&& this.surprise <= other.getSurprise()
				&& this.tears <= other.getTears()
				&& this.angry <= other.getAngry();
	}
}
