package EvalToolProject_ice.tools;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;

public class BFMFileLoader {

	
	LineNumberReader f;
	
	public BFMFileLoader(){
		
	}
	
	public Reader DateiOeffnen(String Dateiname) {
		 
		try {
			
			f = new LineNumberReader(new FileReader(Dateiname));
		
		} catch (IOException e){
			System.out.println("Fehler beim Oeffnen der Datei");
		}  
		
		
		return f;
	}
	
	public void DateiSchliessen() {
		try {
			f.close();
		} catch (IOException e){
			System.out.println("Fehler beim Schliessen der Datei");
		} 
	}
	
	public String readNextLine(){
		
		String line = null;
		
		try {
			line = f.readLine();
		} catch (IOException e){
			System.out.println("Fehler beim Lesen der Datei "+e );
		}
		
		if (line == null)
			return "";
		
		return line;
	}
}
