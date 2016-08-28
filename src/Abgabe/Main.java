package Abgabe;

import com.jme3.system.AppSettings;
/*
 * @author Eric Wolf
 */
public class Main {
	
	private static Interface app;
	
	// creates object to start jmonkey engine
	public static void main(String[] args) {
		
		app = new Interface();
		
		AppSettings settings = new AppSettings(true);
		settings.setTitle("Eric Wolf");
		app.setSettings(settings);
				
		app.start();
	}
	
	// need for reload
	public static Interface getInstance() {
		
		if (app == null) {
			app = new Interface();
		}
		return app;
	}
	
}
