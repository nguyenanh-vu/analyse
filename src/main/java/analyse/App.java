package analyse;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import analyse.messageanalysis.Label;
import analyse.session.Session;
import analyse.session.SessionExporter;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	List<Label> a = new ArrayList<>();
    	List<Label> b = new ArrayList<>();
    	a.add(new Label("a"));
    	b.add(new Label("b"));
        Session session = new Session();
        session.loadWhatsapp("../emmeline_whatsapp.txt", a, "emmeline_whatsapp");
        session.loadFb("../emmeline_fb_1.json", b, "emmeline_fb");
        session.mergeAuthor("Nguyen-Anh Vu", "Nguyen Anh");
        File file = new File("../out.json");
		try (FileWriter fw = new FileWriter(file)){
			fw.write(SessionExporter.exportSession(session));
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}
