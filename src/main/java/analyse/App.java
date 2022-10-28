package analyse;

import analyse.session.SessionController;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	SessionController controller = new SessionController();
    	controller.decide("run ../test.txt");
    }
}
