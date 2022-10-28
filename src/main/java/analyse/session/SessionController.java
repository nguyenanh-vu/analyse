package analyse.session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Scanner;

import analyse.exceptions.NotEnoughArgumentException;

public class SessionController {
	private Session session;
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
		this.session = session;
		this.active = active;
	}
	
	/**
	 * getter
	 * @return analyse.session.Session this.session
	 */
	public Session getSession() {
		return this.session;
	}
	
	/**
	 * Command-line reader
	 * @param str command line input to read
	 */
	public void decide(String str) {
		String[] s = str.split(" ");
		if (s.length == 0) {
			System.out.println("No command");
		} else if (s[0].contentEquals("start")) {
			this.startSession();
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
					SessionLoader.load(args, this.session);
				} else if (s[0].contentEquals("export")) {
					SessionExporter.export(args, this.session);
				} else if (s[0].contentEquals("merge")) {
					this.merge(args);
				} else if (s[0].contentEquals("label")) {
					this.label(args);
				} else if (s[0].contentEquals("run")) {
					this.run(args);
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
	private void startSession() {
		this.session = new Session();
	}
	
	/**
	 * pre-treatment for text
	 * @param str String to treat
	 * @return String str with "__" replaced with " "
	 */
	private String replaceSpace(String str) {
		return str.replace("__", " ");
	}
	
	/**
	 * Merge two authors with command line
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	private void merge(String[] s) throws NotEnoughArgumentException {
		if (s.length < 2) {
			throw new NotEnoughArgumentException(String.join(" ", s), 2, s.length);
		} else {
			this.session.mergeAuthor(this.replaceSpace(s[0]), this.replaceSpace(s[1]));
		}
	}
	
	/**
	 * Attach label with command line
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	private void label(String[] s) throws NotEnoughArgumentException {
		if (s.length < 2) {
			throw new NotEnoughArgumentException(String.join(" ", s), 2, s.length);
		} else {
			this.session.labelAuthor(this.replaceSpace(s[0]), s[1]);
		}
	}
	
	/**
	 * Run script
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	private void run(String[] s) throws NotEnoughArgumentException {
		if (s.length < 1) {
			System.out.println(Arrays.asList(s));
			throw new NotEnoughArgumentException(String.join(" ", s), 1, s.length);
		} else {
			try {
				File myObj = new File(s[0]);
				Scanner myReader = new Scanner(myObj);
				while (myReader.hasNextLine()) {
					this.decide(myReader.nextLine());
				}
				myReader.close();
			} catch (FileNotFoundException e) {
		    	System.out.println("An error occurred.");
		    	System.out.println(e.getMessage());
		    }
			
		}
	}
	
	/**
	 * Save to file as defined by session.address
	 */
	private void save() {
		String adr = this.session.getAdress();
		if (adr.isEmpty()) {
			System.out.println("No save file address. Use \"export session [file path]\" instead");
		} else {
			try (FileWriter fw = new FileWriter(adr)){
				fw.write(SessionExporter.exportSession(session));
				System.out.println(String.format("%s data written to %s", "session", adr));
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
