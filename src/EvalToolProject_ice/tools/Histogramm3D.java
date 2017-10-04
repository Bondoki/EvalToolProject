package EvalToolProject_ice.tools;


/** Klasse zur Berechnung von Mittelwerten, 2. Moment, Varianz 
 */
public class Histogramm3D {
	
	
	private int NrBins1, NrBins2,NrBins3; //Anzahl der Bins
	private int N; //Anzahl aller Eintraege
	private double Min1, Min2, Min3; // untere Grenze
	private double Max1, Max2, Max3; // obere Grenze
	private double wert1, wert2, wert3; //Wert weiterreichen
	private double dI1, dI2,dI3; //Intervalleinteilung
	
	private double Werte[][][]; //Speicherung im Intervall
	
	private double MaximumValue; //Maximalwert der Stichprobe
	
	/**
	 * 
	 * @param min lower boundary
	 * @param max upper boundary
	 * @param nrbins nr of bins
	 */
	public Histogramm3D (double min1, double max1, int nrbins1, double min2, double max2, int nrbins2, double min3, double max3, int nrbins3 ) {
		NrBins1 = nrbins1;
		NrBins2 = nrbins2;
		NrBins3 = nrbins3;
		N= 0;
		Min1 = min1;
		Min2 = min2;
		Min3 = min3;
		Max1 = max1;
		Max2 = max2;
		Max3 = max3;
		wert1 = 0;
		wert2 = 0;
		wert3 = 0;
		dI1 = (max1-min1)/(1.0*nrbins1);
		dI2 = (max2-min2)/(1.0*nrbins2);
		dI3 = (max3-min3)/(1.0*nrbins3);
		
		MaximumValue = 0;
		Werte = new double[NrBins1+1][NrBins2+1][NrBins3+1];
	}

	/** Hinzufuegen wert zum Histogramm
	 * 
	 */
	public void AddValue(double x1, double x2, double x3) {
		
		wert1 = x1;wert2 = x2;wert3 = x3;
		N++;
		
		
		if( ( x1 > Max1) ||  ( x2 > Max2) ||  ( x3 > Max3))
		{
			System.out.println("Bereichsueberschreitung in Histo3D!!!");
			System.exit(1);
			//wert = Max;
		}
		
		if ((x1 < Min1) || (x2 < Min2) || (x3 < Min3))
		{
			System.out.println("Bereichsunterschreitung in Histo3D!!!");
			System.exit(1);
			//wert = Min;
		}
		
		Werte[(int) Math.floor((wert1-Min1)/dI1)][(int) Math.floor((wert2-Min2)/dI2)][(int) Math.floor((wert3-Min3)/dI3)]++; 
	
		if(MaximumValue < Werte[(int) Math.floor((wert1-Min1)/dI1)][(int) Math.floor((wert2-Min2)/dI2)][(int) Math.floor((wert3-Min3)/dI3)])
			MaximumValue = Werte[(int) Math.floor((wert1-Min1)/dI1)][(int) Math.floor((wert2-Min2)/dI2)][(int) Math.floor((wert3-Min3)/dI3)];
	}
	
	public double GetNrInBin(int bin1, int bin2, int bin3)
	{
		return Werte[bin1][bin2][bin3];
	}
	
	public double GetNrInBinNormiert(int bin1, int bin2, int bin3)
	{
		return Werte[bin1][bin2][bin3]/N;
	}
	
	public double GetNrInBinNormiertMaximum(int bin1, int bin2, int bin3)
	{
		return Werte[bin1][bin2][bin3]/MaximumValue;
	}
	
	public double GetRangeInBin1(int bin)
	{
		return (Min1+(bin)*dI1 + dI1/2.0);
	}
	
	public double GetRangeInBin2(int bin)
	{
		return (Min2+(bin)*dI2 + dI2/2.0);
	}
	
	public double GetRangeInBin3(int bin)
	{
		return (Min3+(bin)*dI3 + dI3/2.0);
	}
	
	
	public int GetNrBins1()
	{
		return NrBins1;
	}
	
	public int GetNrBins2()
	{
		return NrBins2;
	}
	
	public int GetNrBins3()
	{
		return NrBins3;
	}
	
	public double GetIntervallThicknessOfBins1()
	{
		return dI1;
	}
	
	public double GetIntervallThicknessOfBins2()
	{
		return dI2;
	}
	
	public double GetIntervallThicknessOfBins3()
	{
		return dI3;
	}
	
}
