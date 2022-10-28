package analyse.session;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import analyse.exceptions.NotEnoughArgumentException;
import analyse.messageanalysis.Label;

public class SessionLoader {
	public static void load(String[] s, Session session) throws NotEnoughArgumentException {
		if (session == null) {
			System.out.println("No session started");
		} else {
			if (s.length < 3) {
				throw new NotEnoughArgumentException(String.join(" ", s), 3, s.length);
			} else {
				List<Label> labels = new ArrayList<>();
				if (s.length > 3) {
					List<String> l = Arrays.asList(Arrays.copyOfRange(s, 3, s.length));
					for (String str : l) {
						labels.add(new Label(str));
					}
				}
				if (s[0].contentEquals("whatsapp")) {
					session.loadWhatsapp(s[1], labels, s[2]);
				} else if (s[0].contentEquals("fb")) {
					session.loadFb(s[1], labels, s[2]);
				} else {
					System.out.println(String
							.format("Mode \"%s\" unknown, expected whatsapp|fb|session", s[0]));
				}
			}
		}
	}
}
