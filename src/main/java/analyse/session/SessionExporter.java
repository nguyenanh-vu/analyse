package analyse.session;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import analyse.exceptions.NotEnoughArgumentException;
import analyse.messageanalysis.Author;
import analyse.messageanalysis.Label;
import analyse.messageanalysis.Message;
import analyse.utils.JSONUtils;

public class SessionExporter {
	public static String exportAuthors(Session session) {
		String str = "";
		for (Author author : session.getAuthorList()) {
			str += ",\n" + author.toString();
		}
		if (!str.isEmpty()) {
			str = str.substring(2);
		}
		return "[\n" + JSONUtils.indent(str) + "\n]";
	}
	
	public static String exportMessages(Session session) {
		String str = "";
		for (Message message : session.getMessageList()) {
			str += ",\n" + message.toString();
		}
		if (!str.isEmpty()) {
			str = str.substring(2);
		}
		return "[\n" + JSONUtils.indent(str) + "\n]";
	}
	
	public static String exportLabels(Session session) {
		String str = "";
		for (Label label : session.getLabels()) {
			str += ",\"" + label.toString() + "\"";
		}
		if (!str.isEmpty()) {
			str = str.substring(1);
		}
		return "[" + str + "]";
	}
	
	public static String exportSession(Session session) {
		return String.format("{\n	\"authors\":%s,\n	\"labels\":%s,\n	\"messages\":%s\n}", 
				JSONUtils.indent(SessionExporter.exportAuthors(session)),
				JSONUtils.indent(SessionExporter.exportLabels(session)),
				JSONUtils.indent(SessionExporter.exportMessages(session)));
	}
	
	public static void export(String[] s, Session session) throws NotEnoughArgumentException {
		if (s.length < 1) {
			throw new NotEnoughArgumentException(String.join(" ", s), 1, s.length);
		} else {
			boolean toFile;
			File file = null;
			if (s.length > 1) {
				toFile = true;
				file = new File(s[1]);
			} else {
				toFile = false;
			}
			String str = "";
			if (s[0].contentEquals("authors")) {
				str = SessionExporter.exportAuthors(session);
			} else if (s[0].contentEquals("labels")) {
				str = SessionExporter.exportLabels(session);
			} else if (s[0].contentEquals("messages")) {
				str = SessionExporter.exportMessages(session);
			} else if (s[0].contentEquals("session")) {
				str = SessionExporter.exportSession(session);
			} else {
				System.out.println(String
						.format("Mode \"%s\" unknown, expected authors|labels|messages|session", s[0]));
			}
			
			if (toFile) {
				try (FileWriter fw = new FileWriter(file)){
					fw.write(str);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println(str);
			}
		}
	}
}
