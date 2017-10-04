package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_LineareObjekte_Gyro {


	
	
	String FileName;
	String FileDirectory;
	
	int[] Polymersystem;
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
	int Gitter_z;
		
	BFMImportData importData;
	
	int Kettenanzahl;
	int Kettenlaenge;
	
	int[] dumpsystem;

	

	int MONOMERZAHL;

	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	
	
	Statistik [][] Gyrationstensor = new Statistik [3][3];
	BFMFileSaver saveGy2_Stat = new BFMFileSaver();
	
	int MonoStrang=0;
	
	
	public Auswertung_LineareObjekte_Gyro()
	{
		
		
		Polymersystem = new int[1];
		
		//System.out.println("cf="+currentFrame);
		
		 MonoStrang=1000;
		
		
		
		 for(int i = 0; i < 3; i++)
				for(int u = 0; u < 3; u++)
					Gyrationstensor[i][u] = new Statistik();
		
	
		//FileName = "1024_1024_0.00391_32";
	//	FileName = "Linear25FreeMSD.bfm";
		FileDirectory = "/home/users/dockhorn/MessungDiplom/MessunGGW/";
		
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		DecimalFormat dh = new DecimalFormat("0000");
		
		
		
		//BFMFileSaver saveGy2_Stat = new BFMFileSaver();
		saveGy2_Stat.DateiAnlegen("EineKetteDoppelstrangA1000MSD_Gyrationstensor_octave.m",false);
		saveGy2_Stat.setzeZeile("g = 0.0");
		saveGy2_Stat.setzeZeile("g2 = 0.0");
		saveGy2_Stat.setzeZeile("axy = 0.0");
		saveGy2_Stat.setzeZeile("axz = 0.0");
		saveGy2_Stat.setzeZeile("nr = 0");
		saveGy2_Stat.setzeZeile("file_id = fopen(\'EineKetteDoppelstrangA1000MSD_Gyrationstensor.dat\', \'a\')");
		saveGy2_Stat.setzeZeile("file_id2 = fopen(\'EineKetteDoppelstrangA1000MSD_Gyrationstensor_ratioxy.dat\', \'a\')");
		saveGy2_Stat.setzeZeile("file_id3 = fopen(\'EineKetteDoppelstrangA1000MSD_Gyrationstensor_ratioxz.dat\', \'a\')");

		
		
		for(int i = 1; i <= 1; i+=1)
		{
			for(int z = 0; z < 3; z++)
				for(int u = 0; u < 3; u++)
					Gyrationstensor[z][u].clear();
			
		
			skipFrames = 0;
			currentFrame = 1;
			LoadFile("DoppelstrangA1000MSD.bfm",1);
		
		}
		
		
		saveGy2_Stat.setzeZeile("fdisp(file_id, (0))");
		saveGy2_Stat.setzeZeile("fdisp(file_id, (g/nr))");
		saveGy2_Stat.setzeZeile("fdisp(file_id, (g2/nr))");
		saveGy2_Stat.setzeZeile("fdisp(file_id, (2*sqrt(((g2-g*g/nr)/(nr*(nr-1.0))))))");
		saveGy2_Stat.setzeZeile("fdisp(file_id2, (axy/nr))");
		saveGy2_Stat.setzeZeile("fdisp(file_id3, (axz/nr))");
		saveGy2_Stat.setzeZeile("fclose(file_id)");
		saveGy2_Stat.setzeZeile("fclose(file_id2)");
		saveGy2_Stat.setzeZeile("fclose(file_id3)");
		saveGy2_Stat.DateiSchliessen();
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
		new Auswertung_LineareObjekte_Gyro();
	}
	
	public void LadeSystem(String FileDir, String FileName)
	{
		importData=null;
		importData = new BFMImportData(FileDir+FileName);
		
		MONOMERZAHL = importData.NrOfMonomers + 1;
		
		Gitter_z = importData.box_z;
		
		
		
		Polymersystem = null;
		Polymersystem = new int[MONOMERZAHL];
		
		dumpsystem = null;
		dumpsystem = new int[MONOMERZAHL];
	
		boolean periodRB_x = importData.periodic_x;
		boolean periodRB_y = importData.periodic_y;
		boolean periodRB_z = importData.periodic_z;
		
		Kettenlaenge = importData.NrOfMonomers;
		Kettenanzahl = 1;
		
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

				//int AnzahlMono = dumpsystem.length-1;
				
				/*if(importData.MCSTime < time_t0)
					return;
				*/
				
				
				
				
				//if((frame%10)==0)
				{
				for(int z = 0; z < 3; z++)
					for(int u = 0; u < 3; u++)
						Gyrationstensor[z][u].clear();
					
				double Rcm_x1 = 0.0;
				double Rcm_y1 = 0.0;
				double Rcm_z1 = 0.0;

				
				
				
				//Schwerpunkt Ring1
			    for (int zk = 1; zk <= MonoStrang ;zk++)
		  		  {  
		   			Rcm_x1 += importData.PolymerKoordinaten[zk][0] + 0.5;
		   			Rcm_y1 += importData.PolymerKoordinaten[zk][1] + 0.5;
		   			Rcm_z1 += importData.PolymerKoordinaten[zk][2] + 0.5;	 
		  		  }
		  		  
				
				Rcm_x1 /= 1.0*MonoStrang;
				Rcm_y1 /= 1.0*MonoStrang;
				Rcm_z1 /= 1.0*MonoStrang;
				
				
				/*double Rcm_x2 = 0.0;
				double Rcm_y2 = 0.0;
				double Rcm_z2 = 0.0;

				
				
				
				//Schwerpunkt Ring1
			    for (int zk = (MonoHalb+1); zk <= (2*MonoHalb) ;zk++)
		  		  {  
		   			Rcm_x2 += importData.PolymerKoordinaten[zk][0] + 0.5;
		   			Rcm_y2 += importData.PolymerKoordinaten[zk][1] + 0.5;
		   			Rcm_z2 += importData.PolymerKoordinaten[zk][2] + 0.5;	 
		  		  }
		  		  
				
				Rcm_x2 /= 1.0*MonoHalb;
				Rcm_y2 /= 1.0*MonoHalb;
				Rcm_z2 /= 1.0*MonoHalb;*/
				
				double M00 = 0.0;
				double M10 = 0.0;
				double M20 = 0.0;
				double M01 = 0.0;
				double M02 = 0.0;
				double M11 = 0.0;
				double M12 = 0.0;
				double M22 = 0.0;
				double M21 = 0.0;
				
				for(int i = 1; i <= MonoStrang; i++)
				{
					M00 += (importData.PolymerKoordinaten[i][0] + 0.5 - Rcm_x1)*(importData.PolymerKoordinaten[i][0] + 0.5 - Rcm_x1);
					M01 += (importData.PolymerKoordinaten[i][0] + 0.5 - Rcm_x1)*(importData.PolymerKoordinaten[i][1] + 0.5 - Rcm_y1);
					M02 += (importData.PolymerKoordinaten[i][0] + 0.5 - Rcm_x1)*(importData.PolymerKoordinaten[i][2] + 0.5 - Rcm_z1);
					M10 += (importData.PolymerKoordinaten[i][1] + 0.5 - Rcm_y1)*(importData.PolymerKoordinaten[i][0] + 0.5 - Rcm_x1);
					M11 += (importData.PolymerKoordinaten[i][1] + 0.5 - Rcm_y1)*(importData.PolymerKoordinaten[i][1] + 0.5 - Rcm_y1);
					M12 += (importData.PolymerKoordinaten[i][1] + 0.5 - Rcm_y1)*(importData.PolymerKoordinaten[i][2] + 0.5 - Rcm_z1);
					M20 += (importData.PolymerKoordinaten[i][2] + 0.5 - Rcm_z1)*(importData.PolymerKoordinaten[i][0] + 0.5 - Rcm_x1);
					M21 += (importData.PolymerKoordinaten[i][2] + 0.5 - Rcm_z1)*(importData.PolymerKoordinaten[i][1] + 0.5 - Rcm_y1);
					M22 += (importData.PolymerKoordinaten[i][2] + 0.5 - Rcm_z1)*(importData.PolymerKoordinaten[i][2] + 0.5 - Rcm_z1);
				}
				
				M00 /= (1.0*MonoStrang);
				M01 /= (1.0*MonoStrang);
				M02 /= (1.0*MonoStrang);
				M10 /= (1.0*MonoStrang);
				M11 /= (1.0*MonoStrang);
				M12 /= (1.0*MonoStrang);
				M20 /= (1.0*MonoStrang);
				M21 /= (1.0*MonoStrang);
				M22 /= (1.0*MonoStrang);
					
				Gyrationstensor[0][0].AddValue(M00);
				Gyrationstensor[0][1].AddValue(M01);
				Gyrationstensor[0][2].AddValue(M02);
				
				Gyrationstensor[1][0].AddValue(M10);
				Gyrationstensor[1][1].AddValue(M11);
				Gyrationstensor[1][2].AddValue(M12);
				
				Gyrationstensor[2][0].AddValue(M20);
				Gyrationstensor[2][1].AddValue(M21);
				Gyrationstensor[2][2].AddValue(M22);
				
				

				saveGy2_Stat.setzeString("A = [");
				for(int zeile = 0; zeile < 3; zeile++)
				{
					for(int spalte = 0; spalte < 3; spalte++)
					{
						saveGy2_Stat.setzeString(""+((float) Gyrationstensor[zeile][spalte].ReturnM1()) +" ");
						//saveRee_Stat.setzeZeile(l*2000  + " " +MessungStat[l].ReturnM1() +" "+  ( 2.0* MessungStat[l].ReturnSigma()/ Math.sqrt(1.0*MessungStat[l].ReturnN()))+" " +MessungStat[l].ReturnN()  );
					}
					if (zeile != 2)
						saveGy2_Stat.setzeString("; ");
				}
				
				saveGy2_Stat.setzeString(" ];");
				saveGy2_Stat.setzeZeile("");
				saveGy2_Stat.setzeZeile("e = eig(A);");
				saveGy2_Stat.setzeZeile("I1 = e(1)+e(2)+e(3);");
				saveGy2_Stat.setzeZeile("I2 = e(1)*e(2)+e(1)*e(3)+e(2)*e(3);");
				saveGy2_Stat.setzeZeile("I3 = e(1)*e(2)*e(3);");
				saveGy2_Stat.setzeZeile("a = 1-3.0*I2/(I1**2)");
				saveGy2_Stat.setzeZeile("g += a");
				saveGy2_Stat.setzeZeile("g2 += a*a");
				saveGy2_Stat.setzeZeile("axy += e(3)/e(2)");
				saveGy2_Stat.setzeZeile("axz += e(3)/e(1)");
				saveGy2_Stat.setzeZeile("nr += 1");
				saveGy2_Stat.setzeZeile("fdisp(file_id, a)");
				
				
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
