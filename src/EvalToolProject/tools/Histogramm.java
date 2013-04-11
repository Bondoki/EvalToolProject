package EvalToolProject.tools;


/** Klasse zur Berechnung von Mittelwerten, 2. Moment, Varianz 
 */
public class Histogramm {
	
	
	private int NrBins; //Anzahl der Bins
	private long N; //Anzahl aller Eintraege
	private double Min; // untere Grenze
	private double Max; // obere Grenze
	private double wert; //Wert weiterreichen
	private double dI; //Intervalleinteilung
	
	private double Werte[]; //Speicherung im Intervall
	
	/**
	 * 
	 * @param min lower boundary
	 * @param max upper boundary
	 * @param nrbins nr of bins
	 */
	public Histogramm (double min, double max, int nrbins) {
		NrBins = nrbins;
		N= 0;
		Min = min;
		Max = max;
		wert = 0;
		dI = (max-min)/(1.0*nrbins);
		
		Werte = new double[NrBins+1];
	}

	/** Hinzufuegen wert zum Histogramm
	 * 
	 */
	public void AddValue(double x) {
		
		wert = x;
		N++;
		
		
		if ( x > Max)
		{
			System.out.println("Bereichsueberschreitung in Histo1D!!!");
			System.exit(1);
			//wert = Max;
		}
		
		if (x < Min)
		{
			System.out.println("Bereichsunterschreitung in Histo1D!!!");
			System.exit(1);
			//wert = Min;
		}
		
		Werte[(int) Math.floor((wert-Min)/dI)]++; 
	}
	
	/** Reduce value of histogram
	 * 
	 */
	public void ReduceValue(double x) {
		
		wert = x;
		//N--;
		
		
		if ( x > Max)
		{
			System.out.println("Bereichsueberschreitung!!!");
			wert = Max;
		}
		
		if (x < Min)
		{
			System.out.println("Bereichsunterschreitung!!!");
			wert = Min;
		}
		
		Werte[(int) Math.floor((wert-Min)/dI)]--; 
	}
	
	public double GetNrInBin(int bin)
	{
		return Werte[bin];
	}
	
	public double GetNrInBinNormiert(int bin)
	{
		return Werte[bin]/N;
	}
	
	public double GetCumulativeNrInBin(int bin)
	{
		double cumValue=0.0;
		
		for(int i=0; i<=bin;i++)
			cumValue+=Werte[i];
		
		return cumValue;
	}
	
	public double GetCumulativeNrInBinNormiert(int bin)
	{
		double cumValue=0.0;
		
		for(int i=0; i<=bin;i++)
			cumValue+=Werte[i];
		
		return cumValue/N;
	}
	
	
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
		return (Min+(bin+1.0)*dI);
		
	}
	
	public int GetNrBins()
	{
		return NrBins;
	}
	
	public int GetBinOfValue(double  value)
	{
		return (int) Math.floor((value-Min)/dI);
	}
	
	public double GetIntervallThickness()
	{
		return dI;
	}
	
	public long GetNrOfCounts()
	{
		return N;
	}
	
	
}
