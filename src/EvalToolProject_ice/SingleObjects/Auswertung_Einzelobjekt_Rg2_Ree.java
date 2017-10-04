package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Histogramm;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Einzelobjekt_Rg2_Ree {


	
	
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
	
	Statistik Rg2_stat,Ree2_stat, durchschnittbond;
	
	BFMFileSaver rg;
	BFMFileSaver ree;
	
	
	Histogramm HG_ree;
	BFMFileSaver histo_ree;
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	public Auswertung_Einzelobjekt_Rg2_Ree(String fdir, String fname)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		durchschnittbond = new Statistik();
		Rg2_stat = new Statistik();
		Ree2_stat = new Statistik();
		
		HG_ree = new Histogramm(0,25,100);
		histo_ree= new BFMFileSaver();
		histo_ree.DateiAnlegen(fdir+fname+"_Histo_Ree.dat", false);
		
		rg = new BFMFileSaver();
		rg.DateiAnlegen(fdir+fname+"_Rg2.dat", false);
		rg.setzeZeile("#<Rg2>  <(Rg2)^2> dRg2 N");
		
		ree = new BFMFileSaver();
		ree.DateiAnlegen(fdir+fname+"_Ree2.dat", false);
		ree.setzeZeile("#<Ree2>  <(Ree2)^2> dRg2 N");
		
		
		
		DecimalFormat dh = new DecimalFormat("000");
		
		
		LoadFile(FileName+".bfm", 1);
		
		rg.setzeZeile(Rg2_stat.ReturnM1()+" "+Rg2_stat.ReturnM2()+" "+( 2.0* Rg2_stat.ReturnSigma()/ Math.sqrt(1.0*Rg2_stat.ReturnN() ))+ " " + Rg2_stat.ReturnN());
		ree.setzeZeile(Ree2_stat.ReturnM1()+" "+Ree2_stat.ReturnM2()+" "+( 2.0* Ree2_stat.ReturnSigma()/ Math.sqrt(1.0*Ree2_stat.ReturnN() ))+ " " + Ree2_stat.ReturnN());
		
		rg.DateiSchliessen();
		
		ree.DateiSchliessen();
		
		
		double test = 0.0;
		for(int i = 0; i < HG_ree.GetNrBins(); i++)
		{
			histo_ree.setzeZeile(HG_ree.GetRangeInBin(i)+" "+HG_ree.GetNrInBinNormiert(i));
			test += HG_ree.GetNrInBinNormiert(i);
		}
		
		histo_ree.DateiSchliessen();
		
		System.out.println("test_ree: "+ test);	
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1() );
	}
	
	protected void LoadFile(String file, int startframe)
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
		  
		  importData.OpenSimulationFile(FileDirectory+file);
			
		  
			System.out.println("file : " +file );
			System.out.println("dir : " + FileDirectory);
			
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
		    
		  
		    System.out.println("addbonds :" +importData.additionalbonds.size());
			System.out.println();
			
			durchschnittbond.AddValue(importData.additionalbonds.size());
		   
			
			currentFrame = z;
			
			if( (currentFrame+skipFrames) >= NrofFrames)
				currentFrame =  NrofFrames;
			
		
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Auswertung_Einzelobjekt_Rg2_Ree(args[0], args[1]);//,args[1],args[2]);
	}
	
	public void LadeSystem(String FileDir, String FileName)
	{
		importData=null;
		importData = new BFMImportData(FileDir+FileName);
		
		MONOMERZAHL = importData.NrOfMonomers + 1;
		
		System.out.println("Nr of Monomers: "+ importData.NrOfMonomers);
		
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
		
		
		
	}
	
	public void playSimulation(int frame)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				importData.GetFrameOfSimulation( frame);;
				
					double Rg2 = 0.0;
					double Rg2_x = 0.0;
					double Rg2_y = 0.0;
					double Rg2_z = 0.0;
						
					 for (int i= 1; i <= importData.NrOfMonomers; i++)
					  for (int j = i; j <= importData.NrOfMonomers; j++)
					  {
						  Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
						  Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
						  Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);
					  }
					 
					Rg2 = Rg2_x + Rg2_y + Rg2_z;
					 
					  //Rg2_x /= 1.0*((4*NrOfMonomersPerStarArm + 1)*(4*NrOfMonomersPerStarArm + 1));
					 // Rg2_y /= 1.0*((4*NrOfMonomersPerStarArm + 1)*(4*NrOfMonomersPerStarArm + 1));
					  //Rg2_z /= 1.0*((4*NrOfMonomersPerStarArm + 1)*(4*NrOfMonomersPerStarArm + 1));
					Rg2 /= 1.0*(importData.NrOfMonomers*importData.NrOfMonomers);
					  
					//rg_x_stat.AddValue(Rg2_x);
					//rg_y_stat.AddValue(Rg2_y);
					//rg_z_stat.AddValue(Rg2_z);
					Rg2_stat.AddValue(Rg2);
					
					Ree2_stat.AddValue(importData.GetDistanceWithoutCheck(1, 100));
				 
					HG_ree.AddValue(Math.sqrt(importData.GetDistanceWithoutCheck(1, 100)));
				 
				
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
	   
	private int KoordBind(int x, int y, int z)
	{
		return (x & 7) + ((y&7) << 3) + ((z&7) << 6);
	}
	

}
