package EvalToolProject_bcc.tools;


/** Klasse zur Berechnung von Mittelwerten, 2. Moment, Varianz 
 */
public class Statistik {
	
	
	int N; //Anzahl der Elemnte
	double M1; //1.Moment
	double M2; //2.Moment
	double Var; //Varianz
	double sigma; //Standardabweichung
	double Min; // kleinste Element
	double Max; // grooesste Element
	double wert; //Wert weiterreichen
	
	
	public Statistik () {
		N = 0;
		M1 = 0;
		M2 = 0;
		Var = 0;
		sigma = 0;
		Min = 0;
		Max = 0;
	}

	/** Brechnung von M1,M2,Var fuer neuen Stichprobenwert
	 * 
	 */
	public void AddValue(double x) {
		
		wert = x;
		M1 = M1 * (double) N/(N+1) + x/(N+1);
		M2 = M2 * (double) N/(N+1) + x*x/(N+1);
		N++;
		Var = (M2 - M1*M1)* (double) N/(N-1);
		//Var = (M2 - M1*M1/N)/(N-1);
		sigma = Math.sqrt(Var);
		
		
		if(M1 == Double.NaN)
			System.exit(1);
		
		if ( N == 1)
			Min = x;
		
		if ( x > Max)
			Max = x;
		
		if ( x < Min)
			Min = x; 
	}
	
	public void clear()
	{
		N = 0;
		M1 = 0;
		M2 = 0;
		Var = 0;
		sigma = 0;
		Min = 0;
		Max = 0;
	}
	
	public double ReturnM1() {
		/** Rueckgabe des 1.Momentes
		 */
		if(M1 == Double.NaN)
			System.exit(1);
		return M1;
	}
	
	public double ReturnM2() {
		/** Rueckgabe des 2.Momentes
		 */
		return M2;
	}
	
	public double ReturnVar() {
		/** Rueckgabe der Varianz
		 */
		return Var;
	}
	
	public double ReturnSigma() {
		/** Rueckgabe der Standardabeichung
		 */
		return sigma;
	}
	
	public int ReturnN() {
		/** Rueckgabe des Stichprobenumfangs
		 */
		return N;
	}
	
	public double ReturnMin() {
		/** Rueckgabe der Standardabeichung
		 */
		return Min;
	}
	
	public double ReturnMax() {
		/** Rueckgabe der Standardabeichung
		 */
		return Max;
	}
	
	public double ReturnWert() {
		/** Rueckgabe des Wertes
		 */
		return wert;
	}
}
