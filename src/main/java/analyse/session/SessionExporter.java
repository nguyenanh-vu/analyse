package analyse.session;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import analyse.UI.UIUtils;
import analyse.exceptions.NotEnoughArgumentException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Conversation;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;
import analyse.messageanalysis.Parameter;
import analyse.search.Result;
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
	 * write List<analyse.messageanalysis.Author> to file as JSON
	 * @param bw BufferedWriter
	 * @param indent
	 * @throws IOException 
	 */
	public void exportAuthors(BufferedWriter bw, Integer indent) throws IOException {
		SessionExporter.indentWrite(bw, "[", indent);
		boolean isFirst = true;
		for (Author a : this.getSession().getAuthorList()) {
			bw.write(isFirst ? "" : ",");
			bw.newLine();
			SessionExporter.indentWrite(bw,a.toJSON(), indent + 1);
			isFirst = false;
		}
		bw.newLine();
		SessionExporter.indentWrite(bw, "]", indent);
	}
	
	/**
	 * write List<analyse.messageanalysis.Message> to file as JSON
	 * @param bw BufferedWriter
	 * @param indent
	 * @throws IOException 
	 */
	public void exportMessages(BufferedWriter bw, Integer indent) throws IOException {
		this.exportMessages(false, bw, indent);
	}
	
	/**
	 * write List<analyse.messageanalysis.Message> to file as JSON
	 * @param verbose Boolean if true add labels to message
	 * @param bw BufferedWriter
	 * @param indent
	 * @throws IOException 
	 */
	public void exportMessages(Boolean verbose, BufferedWriter bw, Integer indent) throws IOException {
		SessionExporter.indentWrite(bw, "[", indent);
		boolean isFirst = true;
		for (Message m : this.getSession().getMessageList()) {
			bw.write(isFirst ? "" : ",");
			bw.newLine();
			SessionExporter.indentWrite(bw,m.toJSON(verbose), indent + 1);
			isFirst = false;
		}
		bw.newLine();
		SessionExporter.indentWrite(bw, "]", indent);
	}
	
	/**
	 * write List<analyse.search.Result> to file as JSON
	 * @param bw BufferedWriter
	 * @param indent
	 * @throws IOException 
	 */
	public void exportResults(BufferedWriter bw, Integer indent) throws IOException {
		SessionExporter.indentWrite(bw, "[", indent);
		boolean isFirst = true;
		for (Result r : this.getSession().getSearchHandler().getResults()) {
			bw.write(isFirst ? "" : ",");
			bw.newLine();
			SessionExporter.indentWrite(bw, r.toJSON(), indent + 1);
			isFirst = false;
		}
		bw.newLine();
		SessionExporter.indentWrite(bw, "]", indent);
	}
	
	/**
	 * write List<analyse.messageanalysis.Label> to file as JSON
	 * @param bw BufferedWriter
	 * @param indent
	 * @throws IOException 
	 */
	public void exportLabels(BufferedWriter bw, Integer indent) throws IOException {
		SessionExporter.indentWrite(bw, "[", indent);
		boolean isFirst = true;
		for (Label l : this.getSession().getLabels()) {
			bw.write(isFirst ? "" : ",");
			bw.newLine();
			SessionExporter.indentWrite(bw, "\"" + l.getName() + "\"", indent + 1);
			isFirst = false;
		}
		bw.newLine();
		SessionExporter.indentWrite(bw, "]", indent);
	}
	
	/**
	 * write List<analyse.messageanalysis.Conversation> to file as JSON
	 * @param bw BufferedWriter
	 * @param indent
	 * @throws IOException 
	 */
	public void exportConversations(BufferedWriter bw, Integer indent) throws IOException {
		SessionExporter.indentWrite(bw, "[", indent);
		boolean isFirst = true;
		for (Conversation c : this.getSession().getConversations()) {
			bw.write(isFirst ? "" : ",");
			bw.newLine();
			SessionExporter.indentWrite(bw, c.toJSON(), indent + 1);
			isFirst = false;
		}
		bw.newLine();
		SessionExporter.indentWrite(bw, "]", indent);
	}
	
	/**
	 * write List<analyse.messageanalysis.Parameter> to file as JSON
	 * @param bw
	 * @param indent
	 * @throws IOException
	 */
	public void exportParams(BufferedWriter bw, Integer indent) throws IOException {
		SessionExporter.indentWrite(bw, "[", indent);
		boolean isFirst = true;
		for (Parameter p : this.getSession().getSearchHandler().getParams()) {
			bw.write(isFirst ? "" : ",");
			bw.newLine();
			SessionExporter.indentWrite(bw, p.toJSON(), indent + 1);
			isFirst = false;
		}
		bw.newLine();
		SessionExporter.indentWrite(bw, "]", indent);
	}
	
	/**
	 * Export whole session data to file as JSON
	 * @param bw BufferedWriter
	 * @throws IOException 
	 */
	public void exportSession(BufferedWriter bw) throws IOException {
		bw.write("{\n	\"authors\":");
		this.exportAuthors(bw, 1);
		bw.write(",\n	\"labels\":");
		this.exportLabels(bw, 1);
		bw.write(",\n	\"conversations\":");
		this.exportConversations(bw, 1);
		bw.write(",\n	\"parameters\":");
		this.exportParams(bw, 1);
		bw.write(",\n	\"results\":");
		this.exportResults(bw, 1);
		bw.write(",\n	\"messages\":");
		this.exportMessages(bw, 1);
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
			switch (mode) {
				case "authors":
					exportAuthors(bw, 0);
					break;
				case "labels":
					this.exportLabels(bw, 0);
					break;
				case "messages":
					this.exportMessages(bw, 0);
					break;
				case "vmessages":
					this.exportMessages(true, bw, 0);
					break;
				case "results":
					this.exportResults(bw, 0);
					break;
				case "session":
					this.exportSession(bw);
					break;
				default:
					UIUtils.modeUnknown(mode, Arrays.asList("authors",
							"labels", "messages", "vmessages", "results", "session"));
					break;
			}
			bw.newLine();
			bw.close();
			this.overwrite();
			this.printfln("Sucessfully exported %s data to %s", mode, file.toString());
		} catch (IOException e) {
			SessionPrinter.printException(e);
		}
	}
}
