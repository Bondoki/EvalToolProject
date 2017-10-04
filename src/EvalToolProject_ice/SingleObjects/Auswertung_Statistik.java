package EvalToolProject_ice.SingleObjects;

import EvalToolProject_ice.tools.BFMFileLoader;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Statistik {

	public Auswertung_Statistik()
	{
		BFMFileLoader loader = new BFMFileLoader();
		loader.DateiOeffnen("/home/users/dockhorn/MessungDiplom/octave/fertigAusgewertet/AuswertungTwistN1000_StatKette1.dat");
		
		String line = "";
		
		Statistik Twist_Stat = new Statistik();
		
		while ((line = loader.readNextLine()) != "") {
			Twist_Stat.AddValue(Double.parseDouble(line));
		}
		
		System.out.println("Twist: M1 "+Twist_Stat.ReturnM1() + "  M2: " + Twist_Stat.ReturnM2() + "  D: "+ ( 2.0* Twist_Stat.ReturnSigma()/ Math.sqrt(1.0*Twist_Stat.ReturnN())) + "  N: "+Twist_Stat.ReturnN() );
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Auswertung_Statistik();
	}
}

