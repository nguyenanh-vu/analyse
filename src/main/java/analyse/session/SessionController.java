package analyse.session;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
		//special cases
		if (s.length == 0) {
			this.println("No command");
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
					//session controls
					case "reset":
						this.reset();
						break;
					case "quit":
						this.active.off();
						break;
					case "save":
						this.save(args);
						break;
					//file system
					case "cd":
						this.getSession().getFileSystem().cd(args);
						break;
					case "mkdir":
						this.getSession().getFileSystem().mkdir(args);
						break;
					case "move":
						this.getSession().getFileSystem().move(args);
						break;
					case "ls":
						this.getSession().getFileSystem().ls(args);
						break;
					case "set":
						this.getSession().getFileSystem().set(args);
						break;
					case "echo":
						this.getSession().getFileSystem().echo(args);
						break;
					//loading and exporting
					case "load":
						this.loader.load(args);
						break;
					case "export":
						this.exporter.export(args);
						break;
					//data editor
					case "merge":
						this.editor.merge(args);
						break;
					case "rename":
						this.editor.rename(args);
						break;
					case "label":
						this.editor.label(args);
						break;
					case "anonymise":
						this.editor.anonymise(args);
						break;
					//script
					case "run":
						this.run(args);
						break;
					case "print":
						this.print(args);
						break;
					//ui
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
						this.printfln("Command \"%s\" unknown", s[0]);
						break;
				}
			} catch (NotEnoughArgumentException e) {
				SessionPrinter.printException(e);
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
			this.println("New session successfully started");
		} else {
			this.getSession().restart();
			this.println("Session successfully restarted");
		}
		
	}
	
	/**
	 * Run script
	 * @param s String[] arguments
	 */
	public void run(String[] s) {
		for (int i = 0; i < s.length; i++ ) {
			this.run(this.getSession()
					.getFileSystem().getPath(s[i]).toFile());
		}
	}
	
	/**
	 * Run single script
	 * @param file File name of script to run
	 */
	private void run(File file) {
		try {
			this.printfln("Running script %s", file.toString());
			Scanner myReader = new Scanner(file);
			while (myReader.hasNextLine()) {
				this.decide(myReader.nextLine());
			}
			myReader.close();
			this.printfln("Finished running script %s", file.toString());
		} catch (FileNotFoundException e) {
	    	this.println("An error occurred.");
	    	SessionPrinter.printException(e);
	    }
	}
	
	/**
	 * Save to file as defined by SessionFileSystem.files
	 */
	private void save(String[] s) {
		File file = null;
		String str = "";
		if (s.length == 0) {
			file = this.getSession().getFileSystem().get("sessionFile");
			str = this.exporter.exportSession();
		} else if (SessionExporter.exportable.containsKey(s[0])) {
			file = this.getSession().getFileSystem()
					.get(SessionExporter.exportable.get(s[0]));
			switch (s[0]) {
				case "messages":
					str = this.exporter.exportMessages();
					break;
				case "vmessages":
					str = this.exporter.exportMessages(true);
					break;
				case "session":
					str = this.exporter.exportSession();
					break;
				case "results":
					str = this.exporter.exportResults();
					break;
				default:
					UIUtils.modeUnknown(s[0], new ArrayList<String>(
							SessionExporter.exportable.keySet()));
					break;
			}
		}
		if (file == null) {
			this.println("No save file address. Use \"export [mode] [file path]\" instead or set fileSession [file path]");
		} else {
			try (FileWriter fw = new FileWriter(file)){
				fw.write(str);
				this.printfln("%s data written to %s", (s.length == 0) ? "session" : s[0], 
						file.toString());
			} catch (IOException e) {
				SessionPrinter.printException(e);
			}
		}
	}
	
	/**
	 * print a line
	 * @param s
	 */
	private void print(String[] s) {
		this.println(String.join(" ", Arrays.asList(s)));
	}
}
