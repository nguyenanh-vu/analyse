package analyse.session;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import analyse.UI.UIUtils;
import analyse.exceptions.NotEnoughArgumentException;
import analyse.messageanalysis.JSONExportable;
import analyse.utils.JSONUtils;

/**
 * Util to export session data to JSON
 */
public class SessionExporter extends SessionTools {
	public static Map<String,String> exportable = new HashMap<String, String>() {
		private static final long serialVersionUID = -7044555477985107836L;
		{
			put("messages", "messagesFile");
			put("vmessages", "messagesFile");
			put("results", "resultsFile");
			put("session", "sessionFile");
		}};
		
	private static void indentWrite(BufferedWriter bw, String s, Integer indent) throws IOException {
		String str = s;
		for (int i = 0; i < indent; i++) {
			str = JSONUtils.indent(str);
		}
		bw.write(str);
	}
	/**
	 * Export whole session data to file as JSON
	 * @param bw BufferedWriter
	 * @param path 
	 * @throws IOException 
	 */
	public void exportSession(BufferedWriter bw, String path) throws IOException {
		bw.write("{\n	\"authors\":");
		this.export(bw, this.getSession().getAuthorList(), 0, "authors" , path, false);
		this.overwrite();
		bw.write(",\n	\"labels\":");
		this.overwrite();
		this.export(bw, this.getSession().getLabels(), 0, "labels" , path, false);
		bw.write(",\n	\"conversations\":");
		this.overwrite();
		this.export(bw, this.getSession().getConversations(), 0, "conversations" , path, false);
		bw.write(",\n	\"parameters\":");
		this.overwrite();
		this.export(bw, this.getSession().getSearchHandler().getParams(), 0, "parameters" , path, false);
		bw.write(",\n	\"results\":");
		this.overwrite();
		this.export(bw, this.getSession().getSearchHandler().getResults(), 0, "results" , path, false);
		bw.write(",\n	\"messages\":");
		this.overwrite();
		this.export(bw, this.getSession().getMessageList(), 0, "messages" , path, false);
		bw.write("\n}");
	}
	
	/**
	 * Command-line controller for data exporter
	 * set save file address if export session to file
	 * @param s String[] arguments
	 * @throws NotEnoughArgumentException
	 */
	public void export(String[] s) throws NotEnoughArgumentException {
		UIUtils.notEnoughArguments(s, 2);
		File file = this.getSession().getFileSystem().getPath(s[1]).toFile();;
		if (SessionExporter.exportable.containsKey(s[0])) {
			this.getSession().getFileSystem()
			.set(SessionExporter.exportable.get(s[0]), file);
		}
		this.export(s[0], file);
	}
	
	public void export(String mode, File file) {
		try (FileWriter fw = new FileWriter(file);){
			fw.write("");
		} catch (IOException e) {
			SessionPrinter.printException(e);
		}
		try (FileWriter fw = new FileWriter(file, true)){
			this.printf("Exporting %s data to %s", mode, file.toString());
			BufferedWriter bw = new BufferedWriter(fw);
			List<? extends JSONExportable> l = null;
			Boolean verbose = false;
			switch (mode) {
				case "authors":
					l = this.getSession().getAuthorList();
					break;
				case "labels":
					l = this.getSession().getLabels();
					break;
				case "messages":
					l = this.getSession().getMessageList();
					break;
				case "vmessages":
					l = this.getSession().getMessageList();
					verbose = true;
					break;
				case "results":
					l = this.getSession().getSearchHandler().getResults();
					break;
				case "session":
					this.exportSession(bw, file.toString());
					break;
				default:
					UIUtils.modeUnknown(mode, Arrays.asList("authors",
							"labels", "messages", "vmessages", "results", "session"));
					break;
			}
			if (l != null) {
				this.export(bw, l, 0, mode, file.toString(), verbose);
			}
			bw.newLine();
			bw.close();
			this.overwrite();
			this.printfln("Sucessfully exported %s data to %s", mode, file.toString());
		} catch (IOException e) {
			SessionPrinter.printException(e);
		}
	}
	
	/**
	 * write List<JSONExportable> to file as JSON objects
	 * @param bw BufferedWriter
	 * @param l List<JSONExportable> containing objects to export
	 * @param indent Integer level of indentation
	 * @param mode String type of objects written
	 * @param path String path of file
	 * @param verbose Boolean
	 * @throws IOException
	 */
	public void export(BufferedWriter bw, List<? extends JSONExportable> l, 
			Integer indent, String mode, String path, Boolean verbose) throws IOException {
		SessionExporter.indentWrite(bw, "[", indent);
		if (!l.isEmpty()) {
			boolean isFirst = true;
			int size = l.size();
			int total = 0;
			float progression;
			for (JSONExportable s : l) {
				total++;
				progression = ((float) total * 100)/this.getSession()
						.getMessageList().size();
				if (size < 1000 || total % (size / 1000) == 0) {
					this.printf("\r%s %.02f%% Writing %s data to : %s", 
							UIUtils.progressBar(progression, 10), progression, 
							mode, path);
				}
				bw.write(isFirst ? "" : ",");
				bw.newLine();
				SessionExporter.indentWrite(bw, s.toJSON(), indent + 1);
				isFirst = false;
			}
		}
		bw.newLine();
		SessionExporter.indentWrite(bw, "]", indent);
	}
}
