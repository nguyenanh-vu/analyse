package analyse.session;

public class SessionPrinter extends SessionTools {
	private Integer previous = 0;
	
	public void reset() {
		this.previous = 0;
	}
	
	public void print(String s) {
		this.previous += s.length();
		System.out.print(s);
	}
	
	public void println(String s) {
		this.previous = 0;
		System.out.println(s);
	}
	
	public void printf(String s, Object... args) {
		String str = String.format(s, args);
		this.previous += str.length();
		System.out.print(str);
	}
	
	public void printfln(String s, Object... args) {
		String str = String.format(s, args);
		this.previous = 0;
		System.out.println(str);
	}
	
	public void overwrite() {
		if (this.previous > 0) {
			StringBuilder str = new StringBuilder();
			System.out.print("\r");
			for (int i = 0; i < this.previous; i++) {
				str.append(" ");
			}
			System.out.print(str.toString());
			System.out.print("\r");
			this.previous = 0;
		}
	}
	
	public static void printException(Exception e) {
		System.out.println(e.getMessage());
	}
}
