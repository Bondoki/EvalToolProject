package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_ice.PEG.Auswertung_Sterne_Verknuepfung_HepPEG;
import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Int_IntArrayList_Table;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Hyperbranched_RouseMatrix {


	
	
	String FileName;
	String FileDirectory;
	
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
	
	int haeufigkeit[];
	
	Int_IntArrayList_Table Bindungsnetzwerk; 
	
	long deltaT;
	
	
	public Auswertung_Hyperbranched_RouseMatrix(String fdir, String fname, String dirDst)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ fname+"_001.bfm");
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		//End - Determine MaxFrame out of the first file
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		
		DecimalFormat dg = new DecimalFormat("000");
		
		for(int hjk = 1; hjk <= 20; hjk++)
		{
		LoadFile(FileName+"_"+dg.format(hjk)+".bfm", 1, maxframe);
		
		
		DecimalFormat dh = new DecimalFormat("00");
		
		BFMFileSaver com = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		com.DateiAnlegen(dirDst+"/"+FileName+"_"+dg.format(hjk)+"_RouseMatrix.dat", false);
		
		BFMFileSaver rouseElements = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rouseElements.DateiAnlegen(dirDst+"/"+FileName+"_"+dg.format(hjk)+"_RouseMatrixElements.dat", false);
		
		
		for(int zeile = 1; zeile < MONOMERZAHL; zeile++)
		{
			int sumSpalte = 0;
			int sumZeile = 0;
			
			for(int spalte = 1; spalte < MONOMERZAHL; spalte++)
			{
				int output = 0;
				
				if(spalte != zeile)
				{
					for(int zui = 0; zui < Bindungsnetzwerk.get(spalte).size(); zui++)
						if( Bindungsnetzwerk.get(spalte).get(zui) == zeile)
							{
								output = -1;
								sumSpalte++;
							}
				}
				else
				{
					output = Bindungsnetzwerk.get(spalte).size();
					sumZeile = Bindungsnetzwerk.get(spalte).size();
				}
				String s = String.format( "%2d", output );
				com.setzeString(s+" ");
				
				if(output != 0)
				{
					rouseElements.setzeZeile("m("+zeile+","+spalte+")="+output);
				}
				//com.setzeString(dh.format(output)+" ");
			}
			com.setzeString(java.lang.System.getProperty("line.separator"));
			
			if(sumZeile != sumSpalte)
			{
				System.out.println("Error in Rouse-Matrix...Exiting...");
				System.exit(1);
			}
			if(sumZeile > 3)
			{
				System.out.println("Error...To many bonds...Exiting...");
				System.exit(1);
			}
			if(sumSpalte > 3)
			{
				System.out.println("Error...To many bonds...Exiting...");
				System.exit(1);
			}
			
			
		}
		
			
		
		com.DateiSchliessen();
		rouseElements.DateiSchliessen();
		}
		
		
		
	}
	
	protected void LoadFile(String file, int startframe, int maxframe)
	{
		//FileName = file;
		System.out.println("file : " +file );
		System.out.println("dir : " + FileDirectory);
		System.out.println("lade System");
		LadeSystem(FileDirectory, file);	
		
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(FileDirectory+file);
		  
		  System.arraycopy(importData.GetFrameOfSimulation(currentFrame),0,Polymersystem,0, Polymersystem.length);
		  
		  
			
		  importData.CloseSimulationFile();
		  
		  /*importData.OpenSimulationFile(FileDirectory+file);
			
		  
			System.out.println("file : " +file );
			System.out.println("dir : " + FileDirectory);
			
			int z = currentFrame;//1;
				
			if(maxframe == -1)
		    while ( (z <= NrofFrames))
		      {

		    		playSimulation(z);
		    		z++;
		    	
		    		for(int u = 1; u <= skipFrames; u++)
		    		{
		    			z++;
		    		}

		      }
			else
			while ( (z <= maxframe))
			      {

			    		playSimulation(z);
			    		z++;
			    	
			    		for(int u = 1; u <= skipFrames; u++)
			    		{
			    			z++;
			    		}

			      }
			
		    
			
		    
		    
		    importData.CloseSimulationFile();
		    
		  
		    System.out.println("addbonds :" +importData.additionalbonds.size());
			System.out.println();
			
			
			
			currentFrame = z;
			
			if( (currentFrame+skipFrames) >= NrofFrames)
				currentFrame =  NrofFrames;
			*/
			
			
			for(int it = 0; it < importData.bonds.size(); it++)
			{
				long bondobj = importData.bonds.get(it);
				//System.out.println(it + " bond " + bondobj);
				int mono1 = getMono1Nr(bondobj);
				int mono2 = getMono2Nr(bondobj);
				
				Bindungsnetzwerk.put(mono1, mono2);
				Bindungsnetzwerk.put(mono2, mono1);
				System.out.println("bonds_sim: "+it+"   a:" + getMono1Nr(bondobj) + " b:" + getMono2Nr(bondobj));
			}
			
			
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 3)
		{
			System.out.println("Calculates the Rouse-Matrix of a single object");
			System.out.println("USAGE: dirSrc/ File[.bfm]  dirDst/");
		}
		else new Auswertung_Hyperbranched_RouseMatrix(args[0], args[1], args[2]);//,args[1],args[2]);
	
	}
	
	 private int getMono1Nr(long obj)//int mono)
	   {
		   return ((int) ( obj & 2147483647));   
	   }
	   
	private int getMono2Nr(long obj)//(int mono)
	   {   
		return (int) (( (obj >> 31) & 2147483647));
	   }

	
	
	public void LadeSystem(String FileDir, String FileName)
	{
		importData=null;
		importData = new BFMImportData(FileDir+FileName);
		
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
	
		String zutrt = 	FileName.substring(0, FileName.length()-4);

		System.out.println("String : " + zutrt);
		
		Bindungsnetzwerk = null;
		Bindungsnetzwerk = new Int_IntArrayList_Table(MONOMERZAHL);
		
		
		for(int it = 0; it < importData.additionalbonds.size(); it++)
		{
			long bondobj = importData.additionalbonds.get(it);
			//System.out.println(it + " bond " + bondobj);
			int mono1 = getMono1Nr(bondobj);
			int mono2 = getMono2Nr(bondobj);
			
			Bindungsnetzwerk.put(mono1, mono2);
			Bindungsnetzwerk.put(mono2, mono1);
			System.out.println("addbonds_sim: "+it+"   a:" + getMono1Nr(bondobj) + " b:" + getMono2Nr(bondobj));
		}
		
		
		
		
		
	}
	
	public void playSimulation(int frame)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				
				importData.GetFrameOfSimulation(frame);
				
				
				 
					
				 importData.addedBondsBetweenFrames.clear();
					
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
