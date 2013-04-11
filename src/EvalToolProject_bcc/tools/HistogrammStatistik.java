package EvalToolProject_bcc.tools;



/** Klasse zur Berechnung von Mittelwerten, 2. Moment, Varianz 
 */
public class HistogrammStatistik {
	
	
	private int NrBins; //Anzahl der Bins
	//private int N; //Anzahl aller Eintraege
	private double Min; // untere Grenze
	private double Max; // obere Grenze
	private double wert; //Wert weiterreichen
	private double dI; //Intervalleinteilung
	
	private Statistik Werte[]; //Speicherung im Intervall
	
	/**
	 * Calculation of an Histogramm with statistical properties of all values
	 * 
	 * @param min lower boundary
	 * @param max upper boundary
	 * @param nrbins nr of bins
	 */
	public HistogrammStatistik (double min, double max, int nrbins) {
		NrBins = nrbins;
		//N= 0;
		Min = min;
		Max = max;
		wert = 0;
		dI = (max-min)/(1.0*nrbins);
		
		Werte = new Statistik[NrBins+1];
		
		for(int i= 0; i < Werte.length; i++)
		{
			Werte[i]= new Statistik();
		}
	}

	/** Hinzufuegen wert zum Histogramm
	 * 
	 */
	public void AddValue(double xValue, double yValue) {
		
		wert = xValue;
		//N++;
		
		
		if ( xValue > Max)
		{
			System.out.println("Bereichsueberschreitung!!!");
			wert = Max;
		}
		
		if (xValue < Min)
		{
			System.out.println("Bereichsunterschreitung!!!");
			wert = Min;
		}
		
		Werte[(int) Math.floor((wert-Min)/dI)].AddValue(yValue); 
	}
	
	public double GetAverageInBin(int bin)
	{
		return Werte[bin].ReturnM1();
	}
	
	/*public double GetNrInBinNormiert(int bin)
	{
		return Werte[bin]/N;
	}*/
	
	
	public double GetRangeInBin(int bin)
	{
		return (Min+(bin)*dI + dI/2.0);
		
	}
	
	public double GetRangeInBinLowerLimit(int bin)
	{
		return (Min+(bin)*dI);
		
	}
	
	public double GetRangeInBinUpperLimit(int bin)
	{
		return (Min+(bin+1)*dI);
		
	}
	
	public int GetNrBins()
	{
		return NrBins;
	}
	
	public double GetIntervallThickness()
	{
		return dI;
	}
	
	/*public int GetNrOfCounts()
	{
		return N;
	}*/
	
	
}
