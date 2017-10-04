package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_LineareObjekteZeit {


	
	
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
	
	
	
	Statistik[] MessungG1;
	Statistik[] MessungG3;
	
	double[] RcmX;
	double[] RcmY;
	double[] RcmZ;
	
	double[][] RX;
	double[][] RY;
	double[][] RZ;
	
	int MonoStrang=0;
	int dt=0;
	int maxFrame = 0;
	
	public Auswertung_LineareObjekteZeit()
	{
		
		
		Polymersystem = new int[1];
		
		//System.out.println("cf="+currentFrame);
		
		 MonoStrang=500;
		 dt=500;
		 maxFrame = 1000;
		
		 MessungG1 = new Statistik[maxFrame+2];
		 MessungG3 = new Statistik[maxFrame+2];
		
		 for(int i = 0; i <= maxFrame; i++)
				MessungG1[i] = new Statistik(); 
			
			for(int i = 0; i <= maxFrame; i++)
				MessungG3[i] = new Statistik();
		 
			RcmX = new double[maxFrame+2];
			 RcmY = new double[maxFrame+2];
			 RcmZ = new double[maxFrame+2];
			 
			 RX = new double[maxFrame+2][MonoStrang+1];
			 RY = new double[maxFrame+2][MonoStrang+1];
			 RZ = new double[maxFrame+2][MonoStrang+1];
			 
	
	
		//FileName = "1024_1024_0.00391_32";
	//	FileName = "Linear25FreeMSD.bfm";
		FileDirectory = "/home/users/dockhorn/MessungDiplom/DiffusionFrei/N500_dt500/";
		
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		DecimalFormat dh = new DecimalFormat("0000");
		
		//for(int i = 0; i <= 9; i++)
		//	LoadFile("buerste_straight_25_0"+i+".bfm", 1);
		//LoadFile(FileName, currentFrame);
		
		for(int i = 1; i <= 500; i++)
		{	
			skipFrames = 0;
			currentFrame = 1;
			LoadFile("Linear_N"+MonoStrang+"__"+dh.format(i)+"_MSD.bfm",1);
			BerechneG1();
			BerechneG3();
			
		
		}
		
		BFMFileSaver save_G1 = new BFMFileSaver();
		save_G1.DateiAnlegen("Linear"+MonoStrang+"PerXYZ_g1_kurzeZeit.dat",false);
		//saveRee_Stat.DateiAnlegen("TrennungA"+MonoProStrang+"PerXYZ_g1.dat",false);
		

		save_G1.setzeZeile("# Messung g1(t) = 1/N sum_k <(r_k(t)-r_k(0))**2> N="+MonoStrang);
		save_G1.setzeZeile("# t g1 dg1");
		
		for(int l = 0; l <= maxFrame; l++)//10001; l++)
		{
			save_G1.setzeZeile(l*dt  + " " +MessungG1[l].ReturnM1() +" "+  ( 2.0* MessungG1[l].ReturnSigma()/ Math.sqrt(1.0*MessungG1[l].ReturnN()))  );
		}
		
		save_G1.DateiSchliessen();
		
		
		BFMFileSaver save_G3 = new BFMFileSaver();
	
		save_G3.DateiAnlegen("Linear"+MonoStrang+"PerXYZ_g3_kurzeZeit.dat",false);
	
		
		save_G3.setzeZeile("# Messung g3(t) = <(Rcm(t)-Rcm(0))**2> N="+MonoStrang);
		save_G3.setzeZeile("# t g3 dg3");
		
		for(int l = 0; l <= maxFrame; l++)//10001; l++)
		{
			save_G3.setzeZeile(l*dt + " " +MessungG3[l].ReturnM1() +" "+  ( 2.0* MessungG3[l].ReturnSigma()/ Math.sqrt(1.0*MessungG3[l].ReturnN()))  );
		}
		save_G3.DateiSchliessen();
		
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
		new Auswertung_LineareObjekteZeit();
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
				
				
				double Rcm_x1 = 0.0;
				double Rcm_y1 = 0.0;
				double Rcm_z1 = 0.0;

				
				
			    for (int zk = 1; zk <= MonoStrang ;zk++)
		  		  {  
		   			Rcm_x1 += importData.PolymerKoordinaten[zk][0] + 0.5;
		   			Rcm_y1 += importData.PolymerKoordinaten[zk][1] + 0.5;
		   			Rcm_z1 += importData.PolymerKoordinaten[zk][2] + 0.5;	 
		  		  }
		  		  
				
				Rcm_x1 /= 1.0*MonoStrang;
				Rcm_y1 /= 1.0*MonoStrang;
				Rcm_z1 /= 1.0*MonoStrang;
				
				
				
				
				RcmX[(int) (importData.MCSTime/dt)] = Rcm_x1;
				RcmY[(int) (importData.MCSTime/dt)] = Rcm_y1;
				RcmZ[(int) (importData.MCSTime/dt)] = Rcm_z1;
				
				
				for (int zk = 1; zk <= MonoStrang ;zk++)
				{
				RX[(int) (importData.MCSTime/dt)][zk] = importData.PolymerKoordinaten[zk][0] + 0.5;
				RY[(int) (importData.MCSTime/dt)][zk] = importData.PolymerKoordinaten[zk][1] + 0.5;
				RZ[(int) (importData.MCSTime/dt)][zk] = importData.PolymerKoordinaten[zk][2] + 0.5;
				}
				
				
					
	}
	
	public void BerechneG1()
	{
		
		for(int i = 0; i <= maxFrame;i++)
		{
			double summe = 0.0;
			for(int kl=1; kl <=MonoStrang; kl++)
				summe += (RX[i][kl]-RX[0][kl])*(RX[i][kl]-RX[0][kl]) + (RY[i][kl]-RY[0][kl])*(RY[i][kl]-RY[0][kl]) + (RZ[i][kl]-RZ[0][kl])*(RZ[i][kl]-RZ[0][kl]);
			
			summe /= (1.0*MonoStrang);
			
			MessungG1[i].AddValue( summe);
		}
		
	}
	
	public void BerechneG3()
	{
		
		for(int i = 0; i <= maxFrame;i++)
		MessungG3[i].AddValue( (RcmX[i]-RcmX[0])*(RcmX[i]-RcmX[0]) + (RcmY[i]-RcmY[0])*(RcmY[i]-RcmY[0]) + (RcmZ[i]-RcmZ[0])*(RcmZ[i]-RcmZ[0]));
	
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
