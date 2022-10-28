package analyse.session;

/**
 * Switch for session activity
 */
public class SessionActive {
	boolean active = true;
	
	/**
	 * getter
	 * @return boolean this.active
	 */
	public boolean getActive() {
		return this.active;
	}
	
	/**
	 * Turn switch on
	 */
	public void on() {
		this.active = true;
	}
	
	/**
	 * Turn switch off
	 */
	public void off() {
		this.active = false;
	}
}
