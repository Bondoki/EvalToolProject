package EvalToolProject_ice.PEG;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.StringTokenizer;

import EvalToolProject_ice.tools.BFMFileLoader;
import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Histogramm;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.LongArrayList;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Sterne_Verknuepfung_ExtentOfReaction_HepPEG {


	private String FileNameSrc;
	private String FileNameDst;
	private String FileDirectorySrc;
	private String FileDirectoryDst;
	
	private BFMFileLoader BFMReader;
	private BFMFileSaver BFMSaver;
	
	
	private LongArrayList Bonds;
	private LongArrayList AdditionalBonds;
	
	private long maxTime;
	private String MaxMCSString;
	
	private int NrOfStars;
	
	private boolean NrOfBondsReached; 
	
	Histogramm Histogramm_ExtentOfReaction;
	
	public Auswertung_Sterne_Verknuepfung_ExtentOfReaction_HepPEG(String DirSrc, String Src, String DirDst, int PEGNr, int MaxTime)//, String skip, String current)
	{
		FileNameSrc = Src;
		FileNameDst = "Bonds_PEG_ExtentOfReaction_Time_HepPEGConnectedGel_"+Src+".dat";
		FileDirectorySrc = DirSrc;
		FileDirectoryDst = DirDst;
		
		Bonds = new LongArrayList();
		AdditionalBonds = new LongArrayList();
		
		maxTime = MaxTime;
		MaxMCSString = new String("");
		
		NrOfStars=PEGNr;
		NrOfBondsReached = false;
		
		double lowerBoundary = 0.0;
		double higherBoundary = maxTime;
		int NrBins = 200;
		
		Histogramm_ExtentOfReaction = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		
		 
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		if (args.length != 5)
        {	System.out.println("Erstellung Histogramm p(t) der HEP-PEG.bfm-file, wenn moeglich");
        	System.out.println("USAGE: dirSrc/ Hydrogel_HEP_hep__PEG_peg_NStar_length__NoPerXYZ128[__xxx__xxx.dat] dirDst/ PEG-Nr MaxTime");
		
        }
	 else
	 {
		 Auswertung_Sterne_Verknuepfung_ExtentOfReaction_HepPEG	 mc = new Auswertung_Sterne_Verknuepfung_ExtentOfReaction_HepPEG(args[0], args[1], args[2] , Integer.parseInt(args[3]), Integer.parseInt(args[4]));//,args[1],args[2]);
	
		 mc.evalExtentOfReaction();
	 }

	}
	
	
	
	public void evalExtentOfReaction()
	{
		
		DecimalFormat dh = new DecimalFormat("000");
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMANY);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(','); 
		DecimalFormat df =   new DecimalFormat  ( "0.00000", otherSymbols );
		DecimalFormat dc =   new DecimalFormat  ( "0.00", otherSymbols );
		
		int usedFilesForAveraging = 0;
		
		for(int i = 1; i <= 26; i+=1)//i++)
		{
			System.out.println("File Nr "+ FileNameSrc+"__"+dh.format(i));
			usedFilesForAveraging++;
		BFMReader = new BFMFileLoader();
		BFMReader.DateiOeffnen(FileDirectorySrc+FileNameSrc+"__"+dh.format(i)+"__"+dh.format(i)+".dat");
		
		boolean weiterladen = true;
		String line = "";
		
		do { 
			
			if ((line = BFMReader.readNextLine()) == null) 
				weiterladen = false;
			
			else
			{
				
				int BondNr = 0;
				int Time = 0;
			
				//System.out.println("addbonds:");
				do
				{
					line = BFMReader.readNextLine();
					StringTokenizer st = new StringTokenizer(line, " ");
					//System.out.println(line);
				
				while (st.hasMoreTokens()) {
					
					st.nextToken(); //mono1
					st.nextToken(); //mono2
					BondNr = Integer.parseInt(st.nextToken());
					Time = Integer.parseInt(st.nextToken());
				
					Histogramm_ExtentOfReaction.AddValue(Time);
					
			     }
				}while(!line.equals(""));
				
				weiterladen = false;
			}
				
			
			
			
		}while(weiterladen == true);
		
		BFMReader.DateiSchliessen();
		
		}
		
		BFMFileSaver ExtentOfReactionSaver = new BFMFileSaver();
		ExtentOfReactionSaver.DateiAnlegen(FileDirectoryDst+"/"+FileNameDst, false);
		ExtentOfReactionSaver.setzeZeile("# t (MCS) p");
		
		for(int i = 0; i < Histogramm_ExtentOfReaction.GetNrBins(); i++)
		{
			//HigherOrderDefectSaver_Histogramm.setzeZeile("# p\t<S0D0T0D0>\t<S1D0T0Q0>\t<S2D0T0Q0>\t<S0D1T0Q0>\t<S3D0T0Q0>\t<S1D1T0Q0>\t<S0D0T1Q0>\t<S4D0T0Q0>\t<S2D1T0Q0>\t<S0D2T0Q0>\t<S1D0T1Q0>\t<S0D0T0Q1>\tSum=1\t<f_HEP>");
			double factor = 1./(4.0*usedFilesForAveraging*NrOfStars);
			
			//ExtentOfReactionSaver.setzeZeile(df.format(Histogramm_ExtentOfReaction.GetRangeInBin(i))+"\t"+df.format(Histogramm_ExtentOfReaction.GetCumulativeNrInBinNormiert(i))+"\t"+df.format(factor*Histogramm_ExtentOfReaction.GetCumulativeNrInBin(i)));
			ExtentOfReactionSaver.setzeZeile(df.format(Histogramm_ExtentOfReaction.GetRangeInBin(i))+"\t"+df.format(factor*Histogramm_ExtentOfReaction.GetCumulativeNrInBin(i)));
			
		}
		
		ExtentOfReactionSaver.DateiSchliessen();
		
	}
	
	

	public int xwert (int ds) {
		/** Umwandlung von int-wert zur x-Koordinate (0...Gitterbreite-2)Kernpunkt*/
		return (ds & 1023);
	}
	
	public int ywert (int ds) {
		/** Umwandlung von int-wert zur y-Koordinate (0...Gitterbreite-2)Kernpunkt*/
		return (ds & 1047552) >> 10;
	}
	
	public int zwert (int ds) {
		/** Umwandlung von int-wert zur z-Koordinate (0...Gitterbreite-2)Kernpunkt*/
		return (ds & 1072693248) >> 20;
	}

	private int getMono1Nr(long obj)//int mono)
	   {
		   return ((int) ( obj & 2147483647));   
	   }
	   
	private int getMono2Nr(long obj)//(int mono)
	   {   
		return (int) (( (obj >> 31) & 2147483647));
	   }
	   
}
