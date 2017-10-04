package EvalToolProject_ice.tools;



/** Klasse zur Berechnung von Mittelwerten, 2. Moment, Varianz 
 */
public class Histogram2DStatistic {
	
	
	private int NrBins1, NrBins2; //Anzahl der Bins
	private int N; //Anzahl aller Eintraege
	private double Min1, Min2; // untere Grenze
	private double Max1, Max2; // obere Grenze
	private double wert1, wert2; //Wert weiterreichen
	private double dI1, dI2; //Intervalleinteilung
	
	private Statistik Werte[][]; //Speicherung im Intervall
	
	
	/**
	 * 
	 * @param min lower boundary
	 * @param max upper boundary
	 * @param nrbins nr of bins
	 */
	public Histogram2DStatistic (double min1, double max1, int nrbins1, double min2, double max2, int nrbins2) {
		NrBins1 = nrbins1;
		NrBins2 = nrbins2;
		
		N= 0;
		Min1 = min1;
		Min2 = min2;
		
		Max1 = max1;
		Max2 = max2;
		
		wert1 = 0;
		wert2 = 0;
		
		dI1 = (max1-min1)/(1.0*nrbins1);
		dI2 = (max2-min2)/(1.0*nrbins2);
		
		Werte = new Statistik[NrBins1+1][NrBins2+1];
		
		for(int i= 0; i < NrBins1+1; i++)
		{
			for(int j= 0; j < NrBins2+1; j++)
			{
				Werte[i][j]= new Statistik();
			}
		}
		
	}

	/** Hinzufuegen wert zum Histogramm
	 * 
	 */
	public void AddValue(double x1, double x2, double yValue) {
		
		wert1 = x1;wert2 = x2;
		N++;
		
		
		if( ( x1 > Max1) ||  ( x2 > Max2))
		{
			System.out.println("Max Bereichsueberschreitung in Histogram2DStatistic!!!");
			System.exit(1);
			//wert = Max;
		}
		
		if ((x1 < Min1) || (x2 < Min2))
		{
			System.out.println("Min Bereichsunterschreitung in Histogram2DStatistic!!!");
			System.exit(1);
			//wert = Min;
		}
		
		
		Werte[(int) Math.floor((wert1-Min1)/dI1)][(int) Math.floor((wert2-Min2)/dI2)].AddValue(yValue); 
		
	}
	
	public void AddValueInBin(int bin1, int bin2, double yValue) {
		
		//wert1 = x1;wert2 = x2;
		N++;
		
		
		if( ( bin1 > NrBins1) ||  ( bin2 > NrBins2))
		{
			System.out.println("Max Bereichsueberschreitung in Bins Histogram2DStatistic!!!");
			System.exit(1);
			//wert = Max;
		}
		
		if ((bin1 < 0) || (bin2 < 0))
		{
			System.out.println("Min Bereichsunterschreitung in Bins Histogram2DStatistic!!!");
			System.exit(1);
			//wert = Min;
		}
		
		
		Werte[bin1][bin2].AddValue(yValue); 
		
	}
	
	public double GetAverageInBin(int bin1, int bin2)
	{
		return Werte[bin1][bin2].ReturnM1();
	}
	
	public void ClearBin(int bin1, int bin2)
	{
		Werte[bin1][bin2].clear();
	}
	
	/*public double GetNrInBinNormiert(int bin1, int bin2)
	{
		return Werte[bin1][bin2]/N;
	}
	*/
	
	
	
	public double GetRangeInBin1(int bin)
	{
		return (Min1+(bin)*dI1 + dI1/2.0);
	}
	
	public double GetRangeInBin2(int bin)
	{
		return (Min2+(bin)*dI2 + dI2/2.0);
	}
	
	public int GetNrBins1()
	{
		return NrBins1;
	}
	
	public int GetNrBins2()
	{
		return NrBins2;
	}
	
	public double GetIntervallThicknessOfBins1()
	{
		return dI1;
	}
	
	public double GetIntervallThicknessOfBins2()
	{
		return dI2;
	}
	
	public int GetNrOfCounts()
	{
		return N;
	}
	
}
