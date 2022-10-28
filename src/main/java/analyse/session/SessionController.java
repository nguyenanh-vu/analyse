package analyse.session;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import analyse.exceptions.NotEnoughArgumentException;

public class SessionController {
	private Session session;
	SessionActive active;
	
	public SessionController(SessionActive active) {
		this.active = active;
	}
	
	public SessionController(Session session, SessionActive active) {
		this.session = session;
		this.active = active;
	}
	
	public Session getSession() {
		return this.session;
	}
	
	public void decide(String str) {
		String[] s = str.split(" ");
		if (s.length == 0) {
			System.out.println("No command");
		} else if (s[0].contentEquals("start")) {
			this.startSession();
		} else if (s[0].contentEquals("quit")) {
			this.active.off();
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
				e.printStackTrace();
			}
		} 
	}
	
	private void startSession() {
		this.session = new Session();
	}
	
	private String replaceSpace(String str) {
		return str.replace("__", " ");
	}
	
	private void merge(String[] s) throws NotEnoughArgumentException {
		if (s.length < 2) {
			throw new NotEnoughArgumentException(String.join(" ", s), 2, s.length);
		} else {
			this.session.mergeAuthor(this.replaceSpace(s[0]), this.replaceSpace(s[1]));
		}
	}
	
	private void label(String[] s) throws NotEnoughArgumentException {
		if (s.length < 2) {
			throw new NotEnoughArgumentException(String.join(" ", s), 2, s.length);
		} else {
			this.session.labelAuthor(this.replaceSpace(s[0]), s[1]);
		}
	}
	
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
		    	e.printStackTrace();
		    }
			
		}
	}
}
