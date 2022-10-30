package analyse;

import java.util.Scanner;

import analyse.UI.ResourcesDisplay;
import analyse.session.SessionActive;
import analyse.session.SessionController;

/**
 * Hello world!
 *
 */
public class App 
{
	public static Scanner input = new Scanner(System.in);
	
    public static void main( String[] args )
    {
    	ResourcesDisplay.display("welcome.txt");
    	SessionActive active = new SessionActive();
    	active.on();
    	SessionController controller = new SessionController(active);
    	controller.reset();
    	controller.run(args);
    	while (Boolean.TRUE.equals(active.getActive())) {
        	controller.decide(input.nextLine());	
    	}
    }
}
