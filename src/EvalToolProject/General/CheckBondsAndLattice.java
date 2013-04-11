package EvalToolProject.General;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import EvalToolProject.tools.BFMFileSaver;
import EvalToolProject.tools.BFMImportData;
import EvalToolProject.tools.Int_IntArrayList_Hashtable;
import EvalToolProject.tools.Statistik;

public class CheckBondsAndLattice {


	
	
	String PathToFile;
	
	
	int[] Polymersystem;
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
	int Gitter_x;
	int Gitter_y;
	int Gitter_z;
	
	
	BFMImportData importData;
	
	
	int[] dumpsystem;

	
	
	int MONOMERZAHL;
	
	
	
	long deltaT;
	
	public CheckBondsAndLattice(String SrcDirFileName)//, String skip, String current)
	{
		PathToFile =SrcDirFileName;
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(PathToFile);
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		
		
		System.out.println("file : " +PathToFile );
		
		DecimalFormat dh = new DecimalFormat("0.0000");
		
		
		LoadFile(PathToFile, currentFrame);
		
		
		System.out.println("Successful Run - nothing special found");
	    
	}
	
	protected void LoadFile(String file, int startframe)
	{
		
		
		System.out.println("lade System");
		LadeSystem(PathToFile);	
		
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(PathToFile);
		  
		  System.arraycopy(importData.GetFrameOfSimulation(currentFrame),0,Polymersystem,0, Polymersystem.length);
		  
		  importData.CloseSimulationFile();
		  
		  importData.OpenSimulationFile(PathToFile);
			
		  
			System.out.println("file : " +PathToFile);
			
			int z = currentFrame;//1;
				
		    while ( (z <= NrofFrames))
		      {

		    		playSimulation(z);
		    		z++;
		    	
		    		for(int u = 1; u <= skipFrames; u++)
		    		{
		    			z++;
		    		}

		      }
		    
			
		    
		    
		    importData.CloseSimulationFile();
		    
		  
		    
		   
			
			currentFrame = z;
			
			if( (currentFrame+skipFrames) >= NrofFrames)
				currentFrame =  NrofFrames;
			
		
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 1)
		{
			System.out.println("Check the bonds and lattice occupation of specified file");
			System.out.println("USAGE: SrcDir/SrcFile");
		}
		else new CheckBondsAndLattice(args[0]);
	}
	
	public void LadeSystem(String FileDirFileName)
	{
		importData=null;
		importData = new BFMImportData(FileDirFileName);
		
		MONOMERZAHL = importData.NrOfMonomers + 1;
		
		Gitter_x = importData.box_x;
		Gitter_y = importData.box_y;
		Gitter_z = importData.box_z;
		
		
		
		Polymersystem = null;
		Polymersystem = new int[MONOMERZAHL];
		
		dumpsystem = null;
		dumpsystem = new int[MONOMERZAHL];
	
		boolean periodRB_x = importData.periodic_x;
		boolean periodRB_y = importData.periodic_y;
		boolean periodRB_z = importData.periodic_z;
		
		boolean periodRB = false;
		
		if ((periodRB_x == true) || (periodRB_y == true) || (periodRB_z == true))
			periodRB = true;
		
		
		System.arraycopy(importData.Polymersystem,0,Polymersystem,0, Polymersystem.length);
		
		
		
		
		
		NrofFrames = importData.GetNrOfFrames();
	
		
		
		
	}
	
	public void playSimulation(int frame)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				importData.GetFrameOfSimulation( frame);
					
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

}
