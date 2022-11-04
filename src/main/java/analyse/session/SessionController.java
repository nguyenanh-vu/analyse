package analyse.session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import analyse.UI.Info;
import analyse.UI.ResourcesDisplay;
import analyse.UI.UIUtils;
import analyse.exceptions.NotEnoughArgumentException;

public class SessionController extends SessionTools{
	private SessionExporter exporter = new SessionExporter();
	private SessionLoader loader = new SessionLoader();
	private SessionEditor editor = new SessionEditor();
	private Info info = new Info();
	SessionActive active;
	
	/**
	 * constructor
	 * @param active analyse.session.SessionActive
	 */
	public SessionController(SessionActive active) {
		this.active = active;
	}
	
	/**
	 * all args constructor
	 * @param session analyse.session.Session
	 * @param active analyse.session.SessionActive
	 */
	public SessionController(Session session, SessionActive active) {
		this.setSession(session);
		this.active = active;
	}
	
	/**
	 * Command-line reader
	 * @param str command line input to read
	 */
	public void decide(String str) {
		String[] s = str.split(" ");
		if (s.length == 0) {
			System.out.println("No command");
		} else if (str.trim().startsWith("//")) {
			
		} else if (s[0].contentEquals("help")) {
			ResourcesDisplay.display("help.txt");
		} else if (s.length > 1 && s[1].contentEquals("help")) {
			ResourcesDisplay.help(s[0]);
		} else {
			String[] args = {};
			if (s.length > 1) {
				args = Arrays.copyOfRange(s, 1, s.length);
			}
			try {
				switch (s[0]) {
					case "reset":
						this.reset();
						break;
					case "quit":
						this.active.off();
						break;
					case "save":
						this.save();
						break;
					case "print":
						this.print(args);
						break;
					case "load":
						this.loader.load(args);
						break;
					case "export":
						this.exporter.export(args);
						break;
					case "merge":
						this.editor.merge(args);
						break;
					case "rename":
						this.editor.rename(args);
						break;
					case "label":
						this.editor.label(args);
						break;
					case "run":
						this.run(args);
						break;
					case "set":
						this.set(args);
						break;
					case "echo":
						this.echo(args);
						break;
					case "list":
						this.info.list(args);
						break;
					case "info":
						this.info.info(args);
						break;
					case "read":
						this.info.read(args);
						break;
					case "search":
						this.getSession().getSearchHandler().search(args);
						break;
					case "parameters":
						this.getSession().getSearchHandler().params(args);
						break;
					default:
						System.out.println(String.format("Command \"%s\" unknown", s[0]));
						break;
				}
			} catch (NotEnoughArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Start new session in controller
	 */
	public void reset() {
		if (this.getSession() == null) {
			this.setSession(new Session());
			this.exporter.setSession(this.getSession());
			this.editor.setSession(this.getSession());
			this.loader.setSession(this.getSession());
			this.loader.setEditor(this.editor);
			this.info.setSession(this.getSession());
			System.out.println("New session successfully started");
		} else {
			this.getSession().restart();
			System.out.println("Session successfully restarted");
		}
		
	}
	
	/**
	 * Run script
	 * @param s String[] arguments
	 */
	public void run(String[] s) {
		for (int i = 0; i < s.length; i++ ) {
			this.run(this.getSession().getWorkdir() + s[i]);
		}
	}
	
	/**
	 * Run single script
	 * @param s String name of scrupt to run
	 */
	private void run(String s) {
		try {
			System.out.println(String.format("Running script %s", s));
			File myObj = new File(s);
			Scanner myReader = new Scanner(myObj);
			while (myReader.hasNextLine()) {
				this.decide(myReader.nextLine());
			}
			myReader.close();
			System.out.println(String.format("Finished running script %s", s));
		} catch (FileNotFoundException e) {
	    	System.out.println("An error occurred.");
	    	System.out.println(e.getMessage());
	    }
	}
	
	/**
	 * Save to file as defined by session.address
	 */
	private void save() {
		String adr = this.getSession().getAddress();
		if (adr.isEmpty()) {
			System.out.println("No save file address. Use \"export session [file path]\" instead");
		} else {
			try (FileWriter fw = new FileWriter(adr)){
				fw.write(this.exporter.exportSession());
				System.out.println(String.format("%s data written to %s", "session", adr));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
	
	/**
	 * Set working variables
	 * @param s arguments
	 * @throws NotEnoughArgumentException
	 */
	private void set(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 2);
		if (s[0].contentEquals("workdir")) {
			this.getSession().setWorkdir(s[1]);
		} else if (s[0].contentEquals("address")) {
			this.getSession().setAddress(s[1]);
		} else {
			System.out.println(String.format("%s not a variable", s[0]));
		}
	}
	
	/**
	 * Read working variables
	 * @param s arguments
	 * @throws NotEnoughArgumentException
	 */
	private void echo(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 1);
		String str;
		if (s[0].contentEquals("workdir")) {
			str = this.getSession().getWorkdir();
		} else if (s[0].contentEquals("address")) {
			str = this.getSession().getAddress();
		} else {
			str = String.format("%s not a variable", s[0]);
		}
		System.out.println(str);
	}
	
	/**
	 * print a line
	 * @param s
	 */
	private void print(String[] s) {
		System.out.println(String.join(" ", Arrays.asList(s)));
	}
}
