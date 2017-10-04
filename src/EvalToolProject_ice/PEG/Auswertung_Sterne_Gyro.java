package EvalToolProject_ice.PEG;
import java.text.DecimalFormat;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Sterne_Gyro {


	
	
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
	
	int NrOfStars = 0;
	int NrOfMonomersPerStarArm = 0;
	
	int[] dumpsystem;

	/*Statistik[] GesamtStat_x;
	Statistik[] GesamtStat_y;
	Statistik[] GesamtStat_z;*/
	
	
	Statistik rg_xyz_stat;
	Statistik rg_x_stat;
	Statistik rg_y_stat;
	Statistik rg_z_stat;
	
	BFMFileSaver rg;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	public Auswertung_Sterne_Gyro(String fname, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = "/home/users/dockhorn/Simulationen/StarSolutionPEG/Messung/";
		
		
		Polymersystem = new int[1];
		skipFrames =  Integer.parseInt(skip);
		currentFrame = Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		rg = new BFMFileSaver();
		rg.DateiAnlegen(FileDirectory+"StarSolutionPEG_Ree2.dat", true);
		
		rg_xyz_stat = new Statistik();
		rg_x_stat = new Statistik();
		rg_y_stat = new Statistik();
		rg_z_stat = new Statistik();
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		DecimalFormat dh = new DecimalFormat("0.0000");
		
		
		
		LoadFile(FileName+".bfm", currentFrame);
		
		//rg.setzeKommentar("c <Reg2> <(Reg2)2> dF N");
		rg.setzeZeile((importData.NrOfMonomers*8.0/(Gitter_x*Gitter_y*Gitter_z))+" "+rg_xyz_stat.ReturnM1()+" "+rg_xyz_stat.ReturnM2()+" "+( 2.0* rg_xyz_stat.ReturnSigma()/ Math.sqrt(1.0*rg_xyz_stat.ReturnN() ))+ " " + rg_xyz_stat.ReturnN());
		
		rg.DateiSchliessen();
		
		BFMFileSaver rg_X = new BFMFileSaver();
		rg_X.DateiAnlegen(FileDirectory+"StarSolutionPEG_Ree2_x.dat", true);
		//rg_X.setzeKommentar("c <Rg2_x> <(Rg2_x)2> dF N");
		rg_X.setzeZeile((importData.NrOfMonomers*8.0/(Gitter_x*Gitter_y*Gitter_z))+" "+rg_x_stat.ReturnM1()+" "+rg_x_stat.ReturnM2()+" "+( 2.0* rg_x_stat.ReturnSigma()/ Math.sqrt(1.0*rg_x_stat.ReturnN() ))+ " " + rg_x_stat.ReturnN());
		rg_X.DateiSchliessen();
		
		BFMFileSaver rg_Y = new BFMFileSaver();
		rg_Y.DateiAnlegen(FileDirectory+"StarSolutionPEG_Ree2_y.dat", true);
		//rg_Y.setzeKommentar("c <Rg2_y> <(Rg2_y)2> dF N");
		rg_Y.setzeZeile((importData.NrOfMonomers*8.0/(Gitter_x*Gitter_y*Gitter_z))+" "+rg_y_stat.ReturnM1()+" "+rg_y_stat.ReturnM2()+" "+( 2.0* rg_y_stat.ReturnSigma()/ Math.sqrt(1.0*rg_y_stat.ReturnN() ))+ " " + rg_y_stat.ReturnN());
		rg_Y.DateiSchliessen();
		
		BFMFileSaver rg_Z = new BFMFileSaver();
		rg_Z.DateiAnlegen(FileDirectory+"StarSolutionPEG_Ree2_z.dat", true);
		//rg_Z.setzeKommentar("c <Rg2_z> <(Rg2_z)2> dF N");
		rg_Z.setzeZeile((importData.NrOfMonomers*8.0/(Gitter_x*Gitter_y*Gitter_z))+" "+rg_z_stat.ReturnM1()+" "+rg_z_stat.ReturnM2()+" "+( 2.0* rg_z_stat.ReturnSigma()/ Math.sqrt(1.0*rg_z_stat.ReturnN() ))+ " " + rg_z_stat.ReturnN());
		rg_Z.DateiSchliessen();
	}
	
	protected void LoadFile(String file, int startframe)
	{
		FileName = file;
		
		System.out.println("lade System");
		LadeSystem(FileDirectory, FileName);	
		
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(FileDirectory+FileName);
		  
		  System.arraycopy(importData.GetFrameOfSimulation(currentFrame),0,Polymersystem,0, Polymersystem.length);
		  
		  importData.CloseSimulationFile();
		  
		  importData.OpenSimulationFile(FileDirectory+FileName);
			
		  
			System.out.println("file : " +FileName );
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
		    
		  
		    
		   
			
			currentFrame = z;
			
			if( (currentFrame+skipFrames) >= NrofFrames)
				currentFrame =  NrofFrames;
			
		
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Auswertung_Sterne_Gyro(args[0],args[1],args[2]);
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
		
		NrOfStars = importData.NrOfStars;
		NrOfMonomersPerStarArm = importData.NrOfMonomersPerStarArm;
		
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
				
				System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);

				int AnzahlMono = dumpsystem.length-1;
				
				
				
				
				//System.out.println("Laenge :" + AnzahlMono);
				
				//RG-Berechnung
				  /*
				 for (int k= 1; k <= NrOfStars; k++)
				 {
					double Rg2 = 0.0;
					double Rg2_x = 0.0;
					double Rg2_y = 0.0;
					double Rg2_z = 0.0;
						
					 for (int i= ((4*NrOfMonomersPerStarArm + 1)*(k-1) +1); i <= (4*NrOfMonomersPerStarArm + 1)*k; i++)
					  for (int j = i; j <= (4*NrOfMonomersPerStarArm + 1)*k; j++)
					  {
						  Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
						  Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
						  Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);

						  
					  }
					 
					Rg2 = Rg2_x + Rg2_y + Rg2_z;
					 
					  Rg2_x /= 1.0*((4*NrOfMonomersPerStarArm + 1)*(4*NrOfMonomersPerStarArm + 1));
					  Rg2_y /= 1.0*((4*NrOfMonomersPerStarArm + 1)*(4*NrOfMonomersPerStarArm + 1));
					  Rg2_z /= 1.0*((4*NrOfMonomersPerStarArm + 1)*(4*NrOfMonomersPerStarArm + 1));
					  Rg2 /= 1.0*((4*NrOfMonomersPerStarArm + 1)*(4*NrOfMonomersPerStarArm + 1));
					  
					rg_x_stat.AddValue(Rg2_x);
					rg_y_stat.AddValue(Rg2_y);
					rg_z_stat.AddValue(Rg2_z);
					rg_xyz_stat.AddValue(Rg2);
				 }*/
				 
				//Ree-Berechnung
				 for (int k= 1; k <= NrOfStars; k++)
				 {
					double Ree2 = 0.0;
					double Ree2_x = 0.0;
					double Ree2_y = 0.0;
					double Ree2_z = 0.0;
						
					 int i= ((4*NrOfMonomersPerStarArm + 1)*(k-1) +1); 
					 
					 
					 for(int z = 1; z <=4; z++)
					 {
					 int j =  (4*NrOfMonomersPerStarArm + 1)*(k-1)+1 +NrOfMonomersPerStarArm*z; 
					  
					 Ree2_x += 1.0*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
					 Ree2_y += 1.0*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
					 Ree2_z += 1.0*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);
					 }
					 
					 Ree2_x /= 4.0;
					 Ree2_y /= 4.0;
					 Ree2_z /= 4.0;
					 
					  	  					  		 
					 Ree2 = Ree2_x + Ree2_y + Ree2_z;
					 
					  
					rg_x_stat.AddValue(Ree2_x);
					rg_y_stat.AddValue(Ree2_y);
					rg_z_stat.AddValue(Ree2_z);
					rg_xyz_stat.AddValue(Ree2);
				 }	
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
