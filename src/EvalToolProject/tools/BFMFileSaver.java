package EvalToolProject.tools;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class BFMFileSaver {

	//FileDialog FD;
	
	
	PrintWriter f;
	
	public BFMFileSaver() {//(bfm bfmclass, Frame parent) {
		
		
		
		//FD = new FileDialog(parent, "Dateiauswahl", FileDialog.SAVE);
		//System.out.println("dialog");
		//FD.show();
 			
	}
	
	
	public void DateiAnlegen(String Dateiname, boolean appendFile ) {
		 
		try {
			
			f = new PrintWriter(new BufferedWriter(new FileWriter(Dateiname,appendFile)));
		
		} catch (IOException e){
			System.out.println("Fehler beim Erstellen der Datei");
		}
			
	    
	}
	
	public void DateiSchliessen() {
		f.close();
	}
	
	public void setzeKommentar(String comment) {
		
		f.println("#"+comment);
	}
	
	public void setzeBefehl(String command) {
		
		f.println("!"+command);
	}
	
	public void setzeKommentarBefehl(String command) {
		
		f.println("#!"+command);
	}
	
	public void setzeLeerzeile() {
		
		f.println();
	}
	
	public void setzeZeile(String command) {
		
		f.println(command);
	}
	
	public void setzeString(String command) {
		
		f.print(command);
	}
	
}
