package analyse.session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import analyse.exceptions.NotEnoughArgumentException;

public class SessionController extends SessionTools{
	private SessionExporter exporter = new SessionExporter();
	private SessionLoader loader = new SessionLoader();
	private SessionEditor editor = new SessionEditor();
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
			
		} else if (s[0].contentEquals("reset")) {
			this.reset();
		} else if (s[0].contentEquals("quit")) {
			this.active.off();
		} else if (s[0].contentEquals("save")) {
			this.save();
		} else {
			String[] args = {};
			if (s.length > 1) {
				args = Arrays.copyOfRange(s, 1, s.length);
			}
			
			try {
				if (s[0].contentEquals("load")) {
					this.loader.load(args);
				} else if (s[0].contentEquals("export")) {
					this.exporter.export(args);
				} else if (s[0].contentEquals("merge")) {
					this.editor.merge(args);
				} else if (s[0].contentEquals("rename")) {
					this.editor.rename(args);
				} else if (s[0].contentEquals("label")) {
					this.editor.label(args);
				} else if (s[0].contentEquals("run")) {
					this.run(args);
				} else if (s[0].contentEquals("set")) {
					this.set(args);
				} else if (s[0].contentEquals("echo")) {
					this.echo(args);
				} else {
					System.out.println(String.format("Command \"%s\" unknown", s[0]));
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
		String adr = this.getSession().getWorkdir() + this.getSession().getAddress();
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
		if (s.length < 2) {
			System.out.println(Arrays.asList(s));
			throw new NotEnoughArgumentException(String.join(" ", s), 2, s.length);
		} else {
			if (s[0].contentEquals("workdir")) {
				this.getSession().setWorkdir(s[1]);
			} else if (s[0].contentEquals("address")) {
				this.getSession().setAddress(s[1]);
			} else {
				System.out.println(String.format("%s not a variable", s[0]));
			}
		}
	}
	
	/**
	 * Read working variables
	 * @param s arguments
	 * @throws NotEnoughArgumentException
	 */
	private void echo(String[] s) throws NotEnoughArgumentException {
		if (s.length < 1) {
			System.out.println(Arrays.asList(s));
			throw new NotEnoughArgumentException(String.join(" ", s), 1, s.length);
		} else {
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
	}
}
