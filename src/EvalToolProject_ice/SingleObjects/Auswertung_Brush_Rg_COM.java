package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_ice.PEG.Auswertung_Sterne_Verknuepfung_HepPEG;
import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Brush_Rg_COM {


	
	
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

	
	
	Statistik Rg2_Stat;
	Statistik Rg2Perp_Stat;
	Statistik Rg2Para_Stat;
	
	Statistik COM_z_stat;
	Statistik COM_x_stat;
	Statistik COM_y_stat;
	
	int BrushLength;
	
	BFMFileSaver rg;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	long deltaT;
	
	
	public Auswertung_Brush_Rg_COM(String fdir, String fname, String dirDst, int brushLength)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		//FileName = fname.replaceAll(".bfm", "").replaceFirst(".xo", "");
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ fname);
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		//End - Determine MaxFrame out of the first file
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		Rg2_Stat= new Statistik();
		Rg2Perp_Stat= new Statistik();
		Rg2Para_Stat= new Statistik();
		
		COM_z_stat= new Statistik();
		COM_x_stat= new Statistik();
		COM_y_stat= new Statistik();
		
		BrushLength = brushLength;
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		DecimalFormat dh = new DecimalFormat("000");
		
		
		
		LoadFile(FileName, 6, maxframe);
		
		rg = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.DateiAnlegen(dirDst+"/Rg2_"+FileName.replaceAll(".bfm", "").replaceFirst(".xo", "")+".dat", false);
		rg.setzeZeile("# <(Rg2)>  <(Rg2)^2> d<Rg2> SampleSize");
		
		rg.setzeZeile(Rg2_Stat.ReturnM1()+" "+(Rg2_Stat.ReturnM2())+" "+( 2.0* Rg2_Stat.ReturnSigma()/Math.sqrt(1.0*Rg2_Stat.ReturnN())) + " " +Rg2_Stat.ReturnN());
		rg.setzeZeile("#");
		rg.setzeZeile("#");
		rg.setzeZeile("# <(Rg2Perp)>  <(Rg2Perp)^2> d<Rg2Perp> SampleSize");
		rg.setzeZeile(Rg2Perp_Stat.ReturnM1()+" "+(Rg2Perp_Stat.ReturnM2())+" "+( 2.0* Rg2Perp_Stat.ReturnSigma()/Math.sqrt(1.0*Rg2Perp_Stat.ReturnN())) + " " +Rg2Perp_Stat.ReturnN());
		rg.setzeZeile("#");
		rg.setzeZeile("#");
		rg.setzeZeile("# <(Rg2Para)>  <(Rg2Para)^2> d<Rg2para> SampleSize");
		rg.setzeZeile(Rg2Para_Stat.ReturnM1()+" "+(Rg2Para_Stat.ReturnM2())+" "+( 2.0* Rg2Para_Stat.ReturnSigma()/Math.sqrt(1.0*Rg2Para_Stat.ReturnN())) + " " +Rg2Para_Stat.ReturnN());
			
		rg.DateiSchliessen();
		
		BFMFileSaver com = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		com.DateiAnlegen(dirDst+"/COM_"+FileName.replaceAll(".bfm", "").replaceFirst(".xo", "")+".dat", false);
		com.setzeZeile("# <COM_Z>  <(COM_Z)^2> d<COM_Z> SampleSize");
		com.setzeZeile(COM_z_stat.ReturnM1()+" "+(COM_z_stat.ReturnM2())+" "+( 2.0* COM_z_stat.ReturnSigma()/Math.sqrt(1.0*COM_z_stat.ReturnN())) + " " +COM_z_stat.ReturnN());
		com.setzeZeile("zCOM/N^0.588 = " + (COM_z_stat.ReturnM1()/Math.pow(BrushLength, 0.588)) + " +-" + (( 2.0* COM_z_stat.ReturnSigma()/Math.sqrt(1.0*COM_z_stat.ReturnN()))/Math.pow(BrushLength, 0.588)));
		com.setzeZeile("#");
		com.setzeZeile("#");
		com.setzeZeile("# <COM_X>  <(COM_X)^2> d<COM_X> SampleSize");
		com.setzeZeile(COM_x_stat.ReturnM1()+" "+(COM_x_stat.ReturnM2())+" "+( 2.0* COM_x_stat.ReturnSigma()/Math.sqrt(1.0*COM_x_stat.ReturnN())) + " " +COM_x_stat.ReturnN());
		com.setzeZeile("#");
		com.setzeZeile("#");
		com.setzeZeile("# <COM_Y>  <(COM_Y)^2> d<COM_Y> SampleSize");
		com.setzeZeile(COM_y_stat.ReturnM1()+" "+(COM_y_stat.ReturnM2())+" "+( 2.0* COM_y_stat.ReturnSigma()/Math.sqrt(1.0*COM_y_stat.ReturnN())) + " " +COM_y_stat.ReturnN());
		
		com.DateiSchliessen();
		
		
		
		
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
		  
		 // System.arraycopy(importData.GetFrameOfSimulation(currentFrame),0,Polymersystem,0, Polymersystem.length);
		  importData.GetFrameOfSimulation(currentFrame);
		  
		  
			
		  importData.CloseSimulationFile();
		  
		  importData.OpenSimulationFile(FileDirectory+file);
			
		  
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
			
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 4)
		{
			System.out.println("Berechnung Rg2, COM");
			System.out.println("USAGE: dirSrc/ FileBrush[.bfm]  dirDst/ BrushLength");
		}
		else new Auswertung_Brush_Rg_COM(args[0], args[1], args[2] , Integer.parseInt(args[3]));//,args[1],args[2]);
	
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
		
		
		//System.arraycopy(importData.Polymersystem,0,Polymersystem,0, Polymersystem.length);
		
		
		
		
		
		NrofFrames = importData.GetNrOfFrames();
	
		String zutrt = 	FileName.substring(0, FileName.length()-4);

		System.out.println("String : " + zutrt);
		
		
		
		
	}
	
	public void playSimulation(int frame)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				
				importData.GetFrameOfSimulation(frame);
				
				 // COM
				 double rcm1_x =0.0;
				 double rcm1_y =0.0;
				 double rcm1_z =0.0;
				 
				 for (int i= 1; i <= BrushLength; i++)
				 {
					 rcm1_x += 1.0*(importData.PolymerKoordinaten[i][0]) ;
					 rcm1_y += 1.0*(importData.PolymerKoordinaten[i][1]) ;
					 rcm1_z += 1.0*(importData.PolymerKoordinaten[i][2]) ;  
				 }
				 rcm1_x /= 1.0*BrushLength;
				 rcm1_y /= 1.0*BrushLength;
				 rcm1_z /= 1.0*BrushLength;
				 
				 COM_x_stat.AddValue(rcm1_x);
				 COM_y_stat.AddValue(rcm1_y);
				 COM_z_stat.AddValue(rcm1_z);
				
				 //RG2
				 
				 
					double Rg2 = 0.0;
					double Rg2_x = 0.0;
					double Rg2_y = 0.0;
					double Rg2_z = 0.0;
					double Rg2_Perp = 0.0;
					double Rg2_Para = 0.0;
						
					 for (int i= 1; i <= BrushLength; i++)
					  for (int j = i; j <= BrushLength; j++)
					  {
						  Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
						  Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
						  Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);

						  
					  }
					 
					Rg2 = Rg2_x + Rg2_y + Rg2_z;
					Rg2 /= 1.0*(BrushLength*BrushLength);
					
					Rg2_Perp = Rg2_x + Rg2_y;
					Rg2_Perp /= 1.0*(BrushLength*BrushLength);
					
					Rg2_Para  = Rg2_z;
					Rg2_Para /= 1.0*(BrushLength*BrushLength);
					  
					//Rg2_Stat.AddValue(Math.sqrt(Rg2));
					//Rg2Perp_Stat.AddValue(Math.sqrt(Rg2_Perp));
					//Rg2Para_Stat.AddValue(Math.sqrt(Rg2_Para));
					Rg2_Stat.AddValue((Rg2));
					Rg2Perp_Stat.AddValue((Rg2_Perp));
					Rg2Para_Stat.AddValue((Rg2_Para));
				 
					
				
				 
					
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
