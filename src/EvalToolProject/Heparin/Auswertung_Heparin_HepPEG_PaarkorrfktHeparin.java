package EvalToolProject.Heparin;
import java.text.DecimalFormat;

import EvalToolProject.tools.BFMFileSaver;
import EvalToolProject.tools.BFMImportData;
import EvalToolProject.tools.Histogramm;
import EvalToolProject.tools.Int_IntArrayList_Hashtable;
import EvalToolProject.tools.R250_521;
import EvalToolProject.tools.Statistik;

public class Auswertung_Heparin_HepPEG_PaarkorrfktHeparin {


	
	
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
	
	int NrOfHeparin = 0;
	int NrOfStars = 0;
	int NrOfMonomersPerStarArm = 0;
	
	int[] dumpsystem;

	/*Statistik[] GesamtStat_x;
	Statistik[] GesamtStat_y;
	Statistik[] GesamtStat_z;*/
	
	double [] Rcm_x;
	double [] Rcm_y;
	double [] Rcm_z;
	
	Statistik rg_xyz_stat;
	//Statistik rg_x_stat;
	//Statistik rg_y_stat;
	//Statistik rg_z_stat;
	
	BFMFileSaver rg;
	
	int MONOMERZAHL;
	
	Histogramm HG_Paarkorrfct;
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	private R250_521 rand;// = new R250_521();
	
	private int counterTime;
	private int counterMonomer;
	double Bereich;
	
	public Auswertung_Heparin_HepPEG_PaarkorrfktHeparin(String fdir, String fname)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		HG_Paarkorrfct = new Histogramm(0,200,150);
		rand = new R250_521();
		
		counterTime=0;
		counterMonomer=0;
		Bereich= 34.0;
		
		rg = new BFMFileSaver();
		rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGSolution/Heparin_Paarkorrfkt_HepPEGSolution_"+FileName+".dat", false);
		rg.setzeZeile("# Paarcorrfct for Heparin");
		rg.setzeZeile("# r[a] <g(r)> ");
		
		rg_xyz_stat = new Statistik();
		
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		DecimalFormat dh = new DecimalFormat("0.0000");
		
		//for(int i = 0; i <= 9; i++)
		//	LoadFile("buerste_straight_25_0"+i+".bfm", 1);
		
		LoadFile(FileName+".bfm", currentFrame);
		
		double constant =4.0*Math.PI*NrOfHeparin/(3.0*Gitter_x*Gitter_y*Gitter_z);
		
		double test = 0.0;
		for(int i = 0; i < HG_Paarkorrfct.GetNrBins() && HG_Paarkorrfct.GetRangeInBin(i) < 0.5*Gitter_x-Bereich; i++)
		{
			//rg.setzeZeile(HG_Paarkorrfct.GetRangeInBin(i)+" "+ (HG_Paarkorrfct.GetNrInBin(i)/(1.0*NrOfHeparin*counterTime*(constant*Math.pow(HG_Paarkorrfct.GetRangeInBinUpperLimit(i),3)-constant*Math.pow(HG_Paarkorrfct.GetRangeInBinLowerLimit(i),3)))));
			//rg.setzeZeile(HG_Paarkorrfct.GetRangeInBin(i)+" "+ ((1.0*Gitter_x*Gitter_y*Gitter_z)*HG_Paarkorrfct.GetNrInBinNormiert(i)/(1.0*NrOfHeparin*4.0*Math.PI*HG_Paarkorrfct.GetIntervallThickness())));
			//rg.setzeZeile(HG_Paarkorrfct.GetRangeInBin(i)+" "+ ((0.125*Gitter_x*Gitter_y*Gitter_z)*HG_Paarkorrfct.GetNrInBin(i)/(1.0*counterTime*MONOMERZAHL*4.0*Math.PI*HG_Paarkorrfct.GetRangeInBin(i)*HG_Paarkorrfct.GetRangeInBin(i)*HG_Paarkorrfct.GetIntervallThickness())));
			//rg.setzeZeile(HG_Paarkorrfct.GetRangeInBin(i)+" "+ ((1.0*Gitter_x*Gitter_y*Gitter_z)*HG_Paarkorrfct.GetNrInBin(i)/(4.0*HG_Paarkorrfct.GetNrOfCounts()*Math.PI*(Math.pow(HG_Paarkorrfct.GetRangeInBinUpperLimit(i), 3)-Math.pow(HG_Paarkorrfct.GetRangeInBinLowerLimit(i), 3))/3.0)));
			rg.setzeZeile(HG_Paarkorrfct.GetRangeInBin(i)+" "+ (HG_Paarkorrfct.GetNrInBin(i)/(4.0*Math.PI*(Math.pow(HG_Paarkorrfct.GetRangeInBinUpperLimit(i), 3)-Math.pow(HG_Paarkorrfct.GetRangeInBinLowerLimit(i), 3))/3.0)));
			
			
			//rg.setzeZeile(HG_Paarkorrfct.GetRangeInBin(i)+" "+ ((1.0*Gitter_x*Gitter_y*Gitter_z)*HG_Paarkorrfct.GetNrInBin(i)/(1.0*counterMonomer*4.0*Math.PI*HG_Paarkorrfct.GetRangeInBin(i)*HG_Paarkorrfct.GetRangeInBin(i)*HG_Paarkorrfct.GetIntervallThickness())));
			
			//test += HG_Paarkorrfct.GetNrInBinNormiert(i);
		}
		
		for(int i = 0; i < HG_Paarkorrfct.GetNrBins(); i++)
		{
			test += HG_Paarkorrfct.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_HG_Paarkorrfct: "+ test + "    timeCounter: "+ counterTime+ "    counterMonomer: "+ counterMonomer + "    histCounter: "+ HG_Paarkorrfct.GetNrOfCounts());	
		
		rg.DateiSchliessen();
		
		
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
		new Auswertung_Heparin_HepPEG_PaarkorrfktHeparin(args[0], args[1]);//,args[1],args[2]);
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
		NrOfHeparin = importData.NrOfHeparin;
		
		Rcm_x = new double[NrOfHeparin+1];
		Rcm_y = new double[NrOfHeparin+1];
		Rcm_z = new double[NrOfHeparin+1];
		
		System.out.println("Stars: " + NrOfStars + "    ArmLength: "+ NrOfMonomersPerStarArm + "    Heparin:"+NrOfHeparin);
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
				
				
				
				
				rg_xyz_stat.clear();
				//System.out.println("Laenge :" + AnzahlMono);
				 //int offset = 90*NrOfHeparin;
				if((frame >= 1000))
				// if((frame >= 1000)&& ((frame%25)==0))
					//if((frame >= 6))
				 {
					 counterTime++;
					 
					
					 /*for (int k= 1; k < MONOMERZAHL; k++)
					 {
						 for (int l= k; l < MONOMERZAHL; l++)
						 {
							 double distance = 0.0;
							if(l != k)
								if((xwert(importData.Polymersystem[k]) > (0.5*Gitter_x-Bereich)) && (xwert(importData.Polymersystem[k]) < (0.5*Gitter_x+Bereich)) && (ywert(importData.Polymersystem[k]) > (0.5*Gitter_y-Bereich)) && (ywert(importData.Polymersystem[k]) < (0.5*Gitter_y+Bereich)) && (zwert(importData.Polymersystem[k]) > (0.5*Gitter_z-Bereich)) && (zwert(importData.Polymersystem[k]) < (0.5*Gitter_z+Bereich)))
								{
									distance = Math.sqrt(1.0*(xwert(importData.Polymersystem[l])-xwert(importData.Polymersystem[k]))*(xwert(importData.Polymersystem[l])-xwert(importData.Polymersystem[k]))+(ywert(importData.Polymersystem[l])-ywert(importData.Polymersystem[k]))*(ywert(importData.Polymersystem[l])-ywert(importData.Polymersystem[k]))+(zwert(importData.Polymersystem[l])-zwert(importData.Polymersystem[k]))*(zwert(importData.Polymersystem[l])-zwert(importData.Polymersystem[k])));
									HG_Paarkorrfct.AddValue(distance);
									counterMonomer++;
								}
							 
						 }
					 }*/
					for (int k= 1; k <= NrOfHeparin; k++)
					 {
						 double Rcm1 = 0.0;
						double Rcm_x1 = 0.0;
						double Rcm_y1 = 0.0;
						double Rcm_z1 = 0.0;
							
						 for (int i= ((k-1)*90 +1); i <= k*90; i++)
						  {
							  Rcm_x1 += 1.0*(importData.PolymerKoordinaten[i][0]);// - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
							  Rcm_y1 += 1.0*(importData.PolymerKoordinaten[i][1]);// - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
							  Rcm_z1 += 1.0*(importData.PolymerKoordinaten[i][2]);// - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);

							  
						  }
						 Rcm_x1 /= 1.0*(90.0);
						 Rcm_y1 /= 1.0*(90.0);
						 Rcm_z1 /= 1.0*(90.0);
						 
						 Rcm_x[k] = Rcm_x1;
						 Rcm_y[k] = Rcm_y1;
						 Rcm_z[k] = Rcm_z1;
					 }
					 
					 
					 
					
					// boolean gefunden = false;
					// double Bereich = 25.0;
					 
					 for(int ZufallsHEP= 1; ZufallsHEP <= NrOfHeparin; ZufallsHEP++)
					 for (int l= ZufallsHEP; l <= NrOfHeparin; l++)
					 {
						 double distance = 0.0;
						if(l != ZufallsHEP)
							if((Rcm_x[ZufallsHEP] > (0.5*Gitter_x-Bereich)) && (Rcm_x[ZufallsHEP] < (0.5*Gitter_x+Bereich)) && (Rcm_y[ZufallsHEP] > (0.5*Gitter_y-Bereich)) && (Rcm_y[ZufallsHEP] < (0.5*Gitter_y+Bereich)) && (Rcm_z[ZufallsHEP] > (0.5*Gitter_z-Bereich)) && (Rcm_z[ZufallsHEP] < (0.5*Gitter_z+Bereich)))
							{
								distance = Math.sqrt((Rcm_x[l]-Rcm_x[ZufallsHEP])*(Rcm_x[l]-Rcm_x[ZufallsHEP])+(Rcm_y[l]-Rcm_y[ZufallsHEP])*(Rcm_y[l]-Rcm_y[ZufallsHEP])+(Rcm_z[l]-Rcm_z[ZufallsHEP])*(Rcm_z[l]-Rcm_z[ZufallsHEP]));
								HG_Paarkorrfct.AddValue(distance);
								counterMonomer++;
							}
						 
					 }
					 
					
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
