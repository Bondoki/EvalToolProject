package EvalToolProject_ice.Heparin;
import java.text.DecimalFormat;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Histogramm;
import EvalToolProject_ice.tools.Histogramm3D;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Heparin_Orientierung {


	
	
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

	/*Statistik[] GesamtStat_x;
	Statistik[] GesamtStat_y;
	Statistik[] GesamtStat_z;*/
	
	
	
	//BFMFileSaver rg;
	BFMFileSaver orient;
	BFMFileSaver punktwolke;
	BFMFileSaver histo3D;
	BFMFileSaver histo_theta;
	BFMFileSaver histo_phi;
	BFMFileSaver histo_ree;
	
	
	
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	double rcm_x_t0=0;
	double rcm_y_t0=0;
	double rcm_z_t0=0;
	
	int time_t0=0;
	
	Statistik[] MessungStat = new Statistik[34001];
	
	//double rcmX[] = new double[34001];
	//double rcmY[] = new double[34001];
	//double rcmZ[] = new double[34001];
	int stupidcounter;
	
	Histogramm3D HG3D;
	
	Histogramm HG_theta, HG_phi, HG_ree;
	
	public Auswertung_Heparin_Orientierung(String fdir, String fname, String dstDir)
	{
		//FileName = "1024_1024_0.00391_32";
		//FileName = "HeparinNetz_N_84_MSD";
		//FileName = "HeparinNetzHarder_N_90_MSD";
		FileName = fname;//"HeparinNetzHarderAdd_N_90_MSD";
		//FileName = "HeparinQuad_N_80_MSD";
		//FileName = "HeparinRect_N_84_MSD";
		//FileDirectory = "/home/users/dockhorn/workspace/Evaluation_Tools/";
		FileDirectory = fdir;//"/home/users/dockhorn/workspace/Evaluation_Tools/Heparin/";
		
		HG3D = new Histogramm3D(-1.1,1.1,50,-1.1,1.1,50,-1.1,1.1,50);
		
		HG_theta = new Histogramm(-1,199,100);
		HG_phi = new Histogramm(-185,185,37);
		HG_ree = new Histogramm(0,90,100);
		
		Polymersystem = new int[1];
		skipFrames = 0;
		currentFrame = 5;
		//System.out.println("cf="+currentFrame);
		
		//rg = new BFMFileSaver();
		orient = new BFMFileSaver();
		punktwolke= new BFMFileSaver();
		histo3D = new BFMFileSaver();
		histo_theta= new BFMFileSaver();
		histo_phi= new BFMFileSaver();
		histo_ree= new BFMFileSaver();
		
	
		//BFMFileSaver newDataclass = new BFMFileSaver(); создание нового объекта класса - это первое что надо сделать
		// newDataclass.DataiAnlegen ("написать имя файла", не правда  - создание нового файла, правда - добавление к существующему файлу)
		// newDataclass.setzeZeile("someting to print out into the file");
		
		
		
		//rg.DateiAnlegen("Linear25FreeMSD_MSD_10E6MCS.dat", false);
		orient.DateiAnlegen(dstDir+"/"+FileName+"_Orient.dat", false);
		punktwolke.DateiAnlegen(dstDir+"/"+FileName+"_Punktwolke.dat", false);
		histo3D.DateiAnlegen(dstDir+"/"+FileName+"_Histo3D.vtk", false);
		histo_theta.DateiAnlegen(dstDir+"/"+FileName+"_Histo_theta.dat", false);
		histo_phi.DateiAnlegen(dstDir+"/"+FileName+"_Histo_phi.dat", false);
		histo_ree.DateiAnlegen(dstDir+"/"+FileName+"_Histo_ree.dat", false);
		
		for(int i = 0; i < 34001; i++)
			MessungStat[i] = new Statistik(); 
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		DecimalFormat dh = new DecimalFormat("0.0000");
		
		//for(int i = 0; i <= 9; i++)
		//	LoadFile("buerste_straight_25_0"+i+".bfm", 1);
		orient.setzeZeile("# r	theta	phi");
		punktwolke.setzeZeile("# ree_x	ree_y	ree_z");
		
		LoadFile(FileName+".bfm", currentFrame);
		
		double test = 0.0;
		for(int i = 0; i < HG_theta.GetNrBins(); i++)
		{
			histo_theta.setzeZeile(HG_theta.GetRangeInBin(i)+" "+HG_theta.GetNrInBinNormiert(i));
			test += HG_theta.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_theta: "+ test);	
		
		test = 0.0;
		for(int i = 0; i < HG_phi.GetNrBins(); i++)
		{
			histo_phi.setzeZeile(HG_phi.GetRangeInBin(i)+" "+HG_phi.GetNrInBinNormiert(i));
			test += HG_phi.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_phi: "+ test);	
		
		test = 0.0;
		for(int i = 0; i < HG_ree.GetNrBins(); i++)
		{
			histo_ree.setzeZeile(HG_ree.GetRangeInBin(i)+" "+HG_ree.GetNrInBinNormiert(i));
			test += HG_ree.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_ree: "+ test);	

		histo3D.setzeZeile("# vtk DataFile Version 1.0");
		histo3D.setzeZeile("Oreientierung EndzuEndVektor");
		histo3D.setzeZeile("ASCII");
		histo3D.setzeZeile("DATASET STRUCTURED_POINTS");
		histo3D.setzeZeile("DIMENSIONS "+HG3D.GetNrBins1() + " " +HG3D.GetNrBins2() +" "+HG3D.GetNrBins3());
		histo3D.setzeZeile("ORIGIN 0.0 0.0 0.0");
		histo3D.setzeZeile("ASPECT_RATIO 1.0 1.0 1.0");
		histo3D.setzeZeile("");
		histo3D.setzeZeile("POINT_DATA " +(HG3D.GetNrBins1()  * HG3D.GetNrBins2() * HG3D.GetNrBins3() ));
		histo3D.setzeZeile("SCALARS scalars double");
		histo3D.setzeZeile("LOOKUP_TABLE default");
		
		
		for(int z = 0; z < (HG3D.GetNrBins3() );z++)
		{
			String zhl = "";
			
			for(int y = 0; y < (HG3D.GetNrBins2() );y++)
				for(int x = 0; x < (HG3D.GetNrBins1() );x++)
				{	
					
					//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
					zhl += ((float) HG3D.GetNrInBinNormiertMaximum(x,y,z) ) + " ";
					
				}
			
			histo3D.setzeZeile(zhl);
					
		}
		
		
		//for(int l = 0; l <  201; l+=1)
		//	rg.setzeZeile((l*50000) + " " +MessungStat[l].ReturnM1() +" "+  ( 2.0* MessungStat[l].ReturnSigma()/ Math.sqrt(1.0*MessungStat[l].ReturnN())) );
		orient.DateiSchliessen();
		punktwolke.DateiSchliessen();
		histo_theta.DateiSchliessen();
		histo_phi.DateiSchliessen();
		histo_ree.DateiSchliessen();
		histo3D.DateiSchliessen();
		
		
		//rg.DateiSchliessen();
		//newDataclass.DateiSchliessen();закрыть файл
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
			skipFrames=0;
			int z = currentFrame;//1;
			stupidcounter=0;
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
		
	
	if(args.length != 3)
	{
		System.out.println("Berechnung Bindungsstatistik von PEG+HEP");
		System.out.println("USAGE: dir/ Heparin[__xxx.bfm] dstDir/");
		System.exit(1);
	}
	System.out.println("Berechnung der Orientierung von Heparin");
	new  Auswertung_Heparin_Orientierung(args[0], args[1], args[2]);
	
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
				
				if(importData.MCSTime == time_t0)
				{
					System.out.println ("phys. NrofMonomers: " + importData.NrOfMonomers);
					
					for(int j=1; j <= importData.NrOfMonomers; j++)
					{
						rcm_x_t0 += importData.PolymerKoordinaten[j][0];
						rcm_y_t0 += importData.PolymerKoordinaten[j][1];
						rcm_z_t0 += importData.PolymerKoordinaten[j][2];
					}
					
					rcm_x_t0 /= (1.0*importData.NrOfMonomers);
					rcm_y_t0 /= (1.0*importData.NrOfMonomers);
					rcm_z_t0 /= (1.0*importData.NrOfMonomers);
					
				}*/
				
				/*double rcm_x=0;
				double rcm_y=0;
				double rcm_z=0;
				
				for(int j=1; j <= importData.NrOfMonomers; j++)
				{
					rcm_x += importData.PolymerKoordinaten[j][0];
					rcm_y += importData.PolymerKoordinaten[j][1];
					rcm_z += importData.PolymerKoordinaten[j][2];
				}
				
				rcm_x /= (1.0*importData.NrOfMonomers);
				rcm_y /= (1.0*importData.NrOfMonomers);
				rcm_z /= (1.0*importData.NrOfMonomers);*/
				
				int start=1;
				//int ende=79; //HeparinNetz_N_84_MSD
				//int ende=85; // Network model ;HeparinNetzHarder_N_90_MSD & HeparinNetzHarderAdd_N_90_MSD
				//int ende=20; //HeparinQuad
				int ende=78; // Network model 
				//int ende=14; //HeparinRect
				double ree_x =importData.PolymerKoordinaten[ende][0]-importData.PolymerKoordinaten[start][0];
				double ree_y =importData.PolymerKoordinaten[ende][1]-importData.PolymerKoordinaten[start][1];
				double ree_z =importData.PolymerKoordinaten[ende][2]-importData.PolymerKoordinaten[start][2];
				
				double ree = Math.sqrt(ree_x*ree_x+ree_y*ree_y+ree_z*ree_z);
				double ree_xy = Math.sqrt(ree_x*ree_x+ree_y*ree_y);
				
				HG_ree.AddValue(ree);
				
				HG3D.AddValue(ree_x/ree, ree_y/ree, ree_z/ree);
				
				double theta = 0.0;//Math.atan(Math.sqrt(ree_x*ree_x+ree_y*ree_y)/ree_z);
				
				if(ree_z == 0.0)
				{
					theta = Math.PI/2.0;
				}
				else
					{
					if(ree_z > 0)
						theta = Math.atan(ree_xy/ree_z);
					else theta = Math.atan(ree_xy/ree_z)+Math.PI;
					}
				
				theta=(360.0/(2.0*Math.PI))*theta;
				
				
				
				HG_theta.AddValue(theta);
				
				double phi = Math.atan(ree_y/ree_x);
				
				if(ree_x == 0.0)
				{
					if(ree_y > 0.0)
					{
						phi= Math.PI/2.0;
					}
					else phi = -Math.PI/2.0;
				}
				
				else{
					if (ree_x > 0.0)
						phi = Math.atan(ree_y/ree_x);
					else
						{
							if(ree_y > 0) 
								phi = Math.atan(ree_y/ree_x)+Math.PI;
							else
								phi = Math.atan(ree_y/ree_x)-Math.PI;
							
						}
				}
				
				phi=(360.0/(2.0*Math.PI))*phi;
				
				/*if(phi > Math.PI)
					phi-= Math.PI;
				else if (phi < -Math.PI)
					phi += Math.PI;
				*/
				
				HG_phi.AddValue(phi);
				
				stupidcounter++;
				
				orient.setzeZeile(stupidcounter+" "+ree +" "+ theta + " "+ phi);
				punktwolke.setzeZeile((ree_x/ree)+" "+(ree_y/ree)+" "+(ree_z/ree));
				//punktwolke.setzeZeile(stupidcounter+" "+(ree_x/ree)+" "+(ree_y/ree)+" "+(ree_z/ree));
				//rcmX[(int)(importData.MCSTime/50000)]= rcm_x;
				//rcmY[(int)(importData.MCSTime/50000)]= rcm_y;
				//rcmZ[(int)(importData.MCSTime/50000)]= rcm_z;
				
				//MessungStat[(int)((importData.MCSTime-time_t0)/50000)].AddValue(((rcm_x-rcm_x_t0)*(rcm_x-rcm_x_t0) + (rcm_y-rcm_y_t0)*(rcm_y-rcm_y_t0) + (rcm_z-rcm_z_t0)*(rcm_z-rcm_z_t0)));
				//GesamtStat_x[frame-1].AddValue(Rg2_x);
				//GesamtStat_y[frame-1].AddValue(Rg2_y);
				//GesamtStat_z[frame-1].AddValue(Rg2_z);
				
					
	}
	
	/*public void calculateMSD()
	{
		for(int u = 0; u < 34001; u++)//((34001-time_t0)); u++)
		{
			if(u >= time_t0)
			{
				if(u == time_t0)
				{
					rcm_x_t0 = rcmX[u];
					rcm_y_t0 = rcmY[u];
					rcm_z_t0 = rcmZ[u];
				}
				
				MessungStat[u-time_t0].AddValue(((rcmX[u]-rcm_x_t0)*(rcmX[u]-rcm_x_t0) + (rcmY[u]-rcm_y_t0)*(rcmY[u]-rcm_y_t0) + (rcmZ[u]-rcm_z_t0)*(rcmZ[u]-rcm_z_t0)));
				
			}
				
		}
			
		
	}*/

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
