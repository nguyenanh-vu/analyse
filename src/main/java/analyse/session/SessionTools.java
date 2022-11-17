package analyse.session;

import lombok.Getter;
import lombok.Setter;

/**
 * Common class for session tools
 */
@Getter
@Setter
public class SessionTools {
	private Session session;
	
	public SessionPrinter getPrinter() {
		return this.session.getPrinter();
	}
	
	public void print(String s) {
		this.session.getPrinter().print(s);
	}
	
	public void println(String s) {
		this.session.getPrinter().println(s);
	}
	
	public void printf(String s, Object... args) {
		this.session.getPrinter().printf(s, args);
	}
	
	public void printfln(String s, Object... args) {
		this.session.getPrinter().printfln(s, args);
	}
	
	public void overwrite() {
		this.session.getPrinter().overwrite();
	}
}
