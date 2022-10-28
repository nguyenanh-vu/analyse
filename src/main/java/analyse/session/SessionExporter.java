package analyse.session;

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
	
	public static String exportSession(Session session) {
		String str = "";
		for (Label label : session.getLabels()) {
			str += ",\"" + label.toString() + "\"";
		}
		if (!str.isEmpty()) {
			str = str.substring(1);
		}
		return String.format("{\n	\"authors\":%s,\n	\"labels\":[%s],\n	\"messages\":%s\n}", 
				JSONUtils.indent(SessionExporter.exportAuthors(session)),
				JSONUtils.indent(str),
				JSONUtils.indent(SessionExporter.exportMessages(session)));
	}
}
