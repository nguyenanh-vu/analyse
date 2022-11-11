package analyse.session;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import analyse.UI.UIUtils;
import analyse.exceptions.NotEnoughArgumentException;

public class SessionFilesSystem extends SessionTools{
	Map<String, File> files;
	private File currentDir = null;
	
	public SessionFilesSystem() {
		this.currentDir = FileSystems.getDefault()
				.getPath(System.getProperty("user.dir"))
				.toAbsolutePath().toFile();
		this.files = new HashMap<>();
		this.files.put("sessionFile", null);
		this.files.put("messagesFile", null);
		this.files.put("resultsFile", null);
	}
	
	public void reset() {
		this.files.put("sessionFile", null);
		this.files.put("messagesFile", null);
		this.files.put("resultsFile", null);
	}
	
	/**
	 * change directory with command line
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void cd(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 1);
		File newDir= this.getPath(s[0])
				.toAbsolutePath().toFile();
		if (!newDir.exists()) {
			this.printfln("Directory %s not found", s[0]);
		} else if (!newDir.isDirectory()) {
			this.printfln("%s not a directory", s[0]);
		} else {
			this.currentDir = newDir;
		}
	}
	
	public void mkdir(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 1);
		File newDir= this.getPath(s[0]).toFile();
		if (!newDir.exists()) {
			newDir.mkdirs();
		}
	}
	
	public void ls(String[] s) {
		String[] list = {};
		if (s.length > 0) {
			list = this.getPath(s[0]).toFile().list();
		} else {
			list = this.currentDir.list();
		}
		for (String str : list) {
			this.println(str);
		}
	}
	
	public void move(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 2);
		Path source = this.getPath(s[0]);
		Path target = this.getPath(s[1]);
		
		try {
			Files.move(source, target);
		} catch (IOException e) {
			SessionPrinter.printException(e);
		}
	}
	
	public void echo(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 1);
		if (this.files.containsKey(s[0])) {
			File file = this.files.get(s[0]);
			if (file != null) {
				this.println(file.toPath().toString());
			} else {
				this.printfln("File %s not yet defined", s[0]);
			}
		} else {
			UIUtils.parameterUnknown(s[0], 
					new ArrayList<String>(this.files.keySet()));
		}
	}
	
	public void set(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 2);
		this.set(s[0], this.currentDir
				.toPath().resolve(this.getPath(s[1])).toFile());
	}
	
	public void set(String key, File file) {
		if (this.files.containsKey(key)) {
			this.files.put(key, file);
		} else {
			UIUtils.parameterUnknown(key, 
					new ArrayList<String>(this.files.keySet()));
		}
	}
	
	/**
	 * getter
	 * @return
	 */
	public File getCurrentDir() {
		return this.currentDir;
	}
	
	public File get(String key) {
		if (this.files.containsKey(key)) {
			return this.files.get(key);
		} else {
			return null;
		}
	}
	
	Path getPath(String s) {
		String path = s;
		if (path.startsWith("~")) {
			path = path.substring(1);
		}
		String sep = FileSystems.getDefault().getSeparator().replace("\\", "\\\\");
		List<String> pathList = new LinkedList<String>(Arrays.asList(
				s.split(String.format("[%s|/]", sep))));
		for (String str : pathList) {
			if (str.contentEquals(".")) {
				pathList.remove(str);
			} else if (str.contentEquals("..") && pathList.indexOf(str)> 0) {
				int index = pathList.indexOf(str);
				pathList.remove("..");
				pathList.remove(index - 1);
			}
		}
		if (pathList.size() > 0 && pathList.get(0).contentEquals("..")) {
			if (pathList.size() == 1) {
				return this.currentDir.getParentFile().toPath();
			} else {
				return this.currentDir.getParentFile().toPath()
						.resolve(String.join(sep, 
								pathList.subList(1, pathList.size())));
			}
		} else {
			return this.currentDir.toPath()
					.resolve(String.join(sep, pathList));
		}
	}
}
