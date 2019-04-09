package Model;

import javafx.application.Platform;
import javafx.scene.paint.Color;

/**
* Config is a class with some default settings of the application
*
* @author  Daniel Geerts
* @since   2019-03-28
*/
public abstract class Config {
	public final static String APP_NAME = "Thema 2.3 - the best AI of secondary year - ITV2E groep 1";
	public final static int WIDTH = 1280;
	public final static int HEIGHT = 720;
	public final static Color BACKGROUND_COLOR = Color.LIGHTBLUE;
	
	public final static String REMOTE_IP = "145.33.225.170"; // server ip: 145.33.225.170
	public final static int REMOTE_PORT = 7789;
	
	public static void QuitApp() {
		System.out.println("Quiting application. Bye...");
		Platform.exit();
		System.exit(0);
	}
}
