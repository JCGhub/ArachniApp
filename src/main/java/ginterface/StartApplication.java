package ginterface;

import javax.swing.UIManager;

import com.alee.laf.WebLookAndFeel;

/**
 * @author Juanca
 *
 * Clase para el lanzamiento de la ejecución de la aplicación de escritorio.
 * 
 */

public class StartApplication{	
	static MainWindow mW;

	public static void main(String[] args){
		try {
			//UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
			
			UIManager.setLookAndFeel ( new WebLookAndFeel () );
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		mW = new MainWindow();
		mW.setVisible(true);
	}
}