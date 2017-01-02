package ginterface;

/**
 * @author Juanca
 *
 * Clase para el lanzamiento de la ejecución de la aplicación de escritorio.
 * 
 */

public class StartApplication{
	static MainWindow mW = new MainWindow();

	public static void main(String[] args){		
		mW.setVisible(true);
	}
}