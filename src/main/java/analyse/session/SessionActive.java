package analyse.session;

public class SessionActive {
	boolean active = true;
	
	public boolean getActive() {
		return this.active;
	}
	
	public void on() {
		this.active = true;
	}
	
	public void off() {
		this.active = false;
	}
}
