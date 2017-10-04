package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;
import java.util.StringTokenizer;
import java.io.Reader;

import EvalToolProject_ice.tools.*;

public class Auswertung_Cubes_RCom2Com_Mean {


	
	
	String FileName;
	String FileNameWithEnd;
	String FileDirectory;
	
	
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
	int Gitter_x;
	int Gitter_y;
	int Gitter_z;
	
	
	BFMImportData importData;
	
	int MONOMERZAHL;
	
	Statistik RCom2Com_stat, durchschnittbond;
	
	Histogramm HG_RCom2Com;
	BFMFileSaver histo_RCom2Com;
	
	Int_IntArrayList_Table Bindungsnetzwerk; 
	
	long deltaT;
	
	
	Statistik Rg2_Stat;
	Statistik Rg2_x_Stat;
	Statistik Rg2_y_Stat;
	Statistik Rg2_z_Stat;
	
	Statistik Bondlength2_Stat;
	
	
	String dstDir;
	
	
	public Auswertung_Cubes_RCom2Com_Mean(String fdir, String fname, String dirDst, double springconstant, double equilibriumlength)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileNameWithEnd  = fname;
		FileName = fname.replaceAll(".bfm", "").replaceFirst(".xo", "");
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		dstDir = dirDst;
		
		System.out.println("-7%6="+ (-7%6));
		
		
		HistogrammStatistik meanDep = new HistogrammStatistik(+0.125,20.0+0.125,80);
		
		BFMFileLoader loadFile = new BFMFileLoader();
		
		loadFile.DateiOeffnen(FileDirectory+FileNameWithEnd);
		
		String line;
		
		
		while (!(line = loadFile.readNextLine()).isEmpty()) {
            
			System.out.println(line);
			StringTokenizer st = new StringTokenizer(line, " ");
			double x = Double.parseDouble(st.nextToken());
			double y = Double.parseDouble(st.nextToken());
			
			meanDep.AddValue(x, y);
	   }
		loadFile.DateiSchliessen();
		
		BFMFileSaver meanDepSaver = new BFMFileSaver();
		meanDepSaver.DateiAnlegen(dirDst+"/"+FileName+"_Histo_MeanDepletionb.dat", false);
		for(int i = 0; i < meanDep.GetNrBins(); i++)
		{
			meanDepSaver.setzeZeile(meanDep.GetRangeInBin(i)+" "+meanDep.GetAverageInBin(i));
		}
		meanDepSaver.DateiSchliessen();
		
		
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 5)
		{
			System.out.println("Berechnung Rc2c Cubes");
			System.out.println("USAGE: dirSrc/ FileLinearChains[.xo]  dirDst/ springconstant equilibriumlength");
		}
		else new Auswertung_Cubes_RCom2Com_Mean(args[0], args[1], args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]));//,args[1],args[2]);
	
		
	}
	


}
