package EvalToolProject_bcc.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_bcc.PEG.Auswertung_Sterne_Verknuepfung_HepPEG;
import EvalToolProject_bcc.tools.BFMFileSaver;
import EvalToolProject_bcc.tools.BFMImportData;
import EvalToolProject_bcc.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_bcc.tools.Statistik;

public class Auswertung_TrennungDNA_Rc2c_G_Frei {


	
	
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

	/*Statistik[] GesamtStat_x;
	Statistik[] GesamtStat_y;
	Statistik[] GesamtStat_z;*/
	
	
	Statistik[] Rg2_stat;
	Statistik[] Rc2c_stat;
	Statistik[] MetricsG_stat;
	//Statistik rg_x_stat;
	//Statistik rg_y_stat;
	//Statistik rg_z_stat;
	
	BFMFileSaver rg;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	long deltaT;
	
	double Rcom_x1, Rcom_x2, Rcom_y1, Rcom_y2, Rcom_z1, Rcom_z2;
	
	public Auswertung_TrennungDNA_Rc2c_G_Frei(String fdir, String fname, String dirDst, int nrOfFiles)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ fname+"__001_sep.bfm");
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		//End - Determine MaxFrame out of the first file
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		
		Rg2_stat = new Statistik[maxframe+1];
		
		for(int i = 0; i <= maxframe; i++)
			Rg2_stat[i] = new Statistik();
		
		Rc2c_stat = new Statistik[maxframe+1];
		for(int i = 0; i <= maxframe; i++)
			Rc2c_stat[i] = new Statistik();
		
		MetricsG_stat = new Statistik[maxframe+1];
		for(int i = 0; i <= maxframe; i++)
			MetricsG_stat[i] = new Statistik();
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		DecimalFormat dh = new DecimalFormat("000");
		
		//for(int i = 0; i <= 9; i++)
		//	LoadFile("buerste_straight_25_0"+i+".bfm", 1)		
		for(int i = 10; i <= nrOfFiles; i+=1)//i++)
			LoadFile(FileName+"__"+dh.format(i)+"_sep.bfm", 1, maxframe);
		
		rg = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.DateiAnlegen(dirDst+"/Rg2_"+FileName+".dat", false);
		rg.setzeZeile("# t[MCS] <(Rg2)^0.5>  <(Rg2)> d<Rg2>");
		
		for(int i=0; i < maxframe; i++)
			rg.setzeZeile((deltaT*i) + " " + Rg2_stat[i].ReturnM1()+" "+(Rg2_stat[i].ReturnM2())+" "+( 2.0* Rg2_stat[i].ReturnSigma()/Math.sqrt(1.0*Rg2_stat[i].ReturnN())) + " " +Rg2_stat[i].ReturnN());
			 
			
		rg.DateiSchliessen();
		
		BFMFileSaver rc2c = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rc2c.DateiAnlegen(dirDst+"/Rc2c_"+FileName+".dat", false);
		rc2c.setzeZeile("# t[MCS] <|Rc2c|>  <(Rc2c)2> d<Rc2c>");
		
		for(int i=0; i < maxframe; i++)
			rc2c.setzeZeile((deltaT*i) + " " + Rc2c_stat[i].ReturnM1()+" "+(Rc2c_stat[i].ReturnM2())+" "+( 2.0* Rc2c_stat[i].ReturnSigma()/Math.sqrt(1.0*Rc2c_stat[i].ReturnN())) + " " +Rc2c_stat[i].ReturnN());
			 
			
		rc2c.DateiSchliessen();
		
		BFMFileSaver metricsG = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		metricsG.DateiAnlegen(dirDst+"/MetricsG_"+FileName+".dat", false);
		metricsG.setzeZeile("# t[MCS] <G>  <G^2> d<G>");
		
		for(int i=0; i < maxframe; i++)
			metricsG.setzeZeile((deltaT*i) + " " + MetricsG_stat[i].ReturnM1()+" "+(MetricsG_stat[i].ReturnM2())+" "+( 2.0* MetricsG_stat[i].ReturnSigma()/Math.sqrt(1.0*MetricsG_stat[i].ReturnN())) + " " +MetricsG_stat[i].ReturnN());
			 
			
		metricsG.DateiSchliessen();
		
		
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
			System.out.println("Berechnung Rc2c, G Trennung");
			System.out.println("Only valid for N=1000 per chain!!!");
			System.out.println("USAGE: dirSrc/ DNAStaveA1000PerXYZ512[__xxx_sep.bfm]  dirDst/ Files");
		}
		else new Auswertung_TrennungDNA_Rc2c_G_Frei(args[0], args[1], args[2] , Integer.parseInt(args[3]));//,args[1],args[2]);
	
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
		
		
		
		
	}
	
	public void playSimulation(int frame)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				
				importData.GetFrameOfSimulation(frame);
				
				//System.out.println("Laenge :" + AnzahlMono);
				 int offset = 0;
				 
				 // COM
				 double rcm1_x =0.0;
				 double rcm1_y =0.0;
				 double rcm1_z =0.0;
				 
				 for (int i= 1; i <= 1000; i++)
				 {
					 rcm1_x += 1.0*(importData.PolymerKoordinaten[i][0]) ;
					 rcm1_y += 1.0*(importData.PolymerKoordinaten[i][1]) ;
					 rcm1_z += 1.0*(importData.PolymerKoordinaten[i][2]) ;  
				 }
				 rcm1_x /= 1000.0;
				 rcm1_y /= 1000.0;
				 rcm1_z /= 1000.0;
				 
				 
				 double rcm2_x =0.0;
				 double rcm2_y =0.0;
				 double rcm2_z =0.0;
				 
				 for (int i= 1001; i <= 2000; i++)
				 {
					 rcm2_x += 1.0*(importData.PolymerKoordinaten[i][0]) ;
					 rcm2_y += 1.0*(importData.PolymerKoordinaten[i][1]) ;
					 rcm2_z += 1.0*(importData.PolymerKoordinaten[i][2]) ;  
				 }
				 rcm2_x /= 1000.0;
				 rcm2_y /= 1000.0;
				 rcm2_z /= 1000.0;
				 
				 if(frame == 1)
				 {
					 Rcom_x1=rcm1_x;
					 Rcom_x2=rcm2_x;
					 Rcom_y1=rcm1_y;
					 Rcom_y2=rcm2_y;
					 Rcom_z1=rcm1_z;
					 Rcom_z2=rcm2_z;
				 }
				 
				 //chain-jump over -X
				 if((rcm1_x-Rcom_x1) > 30.0)
				 {
					 for (int i= 1; i <= 1000; i++)
						 importData.PolymerKoordinaten[i][0] -= Gitter_x;
				 }
				//chain-jump over +X
				 if((rcm1_x-Rcom_x1) < -30.0)
				 {
					 for (int i= 1; i <= 1000; i++)
						 importData.PolymerKoordinaten[i][0] += Gitter_x;
				 }
				 //chain-jump over -Y
				 if((rcm1_y-Rcom_y1) > 30.0)
				 {
					 for (int i= 1; i <= 1000; i++)
						 importData.PolymerKoordinaten[i][1] -= Gitter_y;
				 }
				//chain-jump over +Y
				 if((rcm1_y-Rcom_y1) < -30.0)
				 {
					 for (int i= 1; i <= 1000; i++)
						 importData.PolymerKoordinaten[i][1] += Gitter_y;
				 }
				 //chain-jump over -Z
				 if((rcm1_z-Rcom_z1) > 30.0)
				 {
					 for (int i= 1; i <= 1000; i++)
						 importData.PolymerKoordinaten[i][2] -= Gitter_z;
				 }
				//chain-jump over +Z
				 if((rcm1_z-Rcom_z1) < -30.0)
				 {
					 for (int i= 1; i <= 1000; i++)
						 importData.PolymerKoordinaten[i][2] += Gitter_z;
				 }
				 
				//chain-jump over -X
				 if((rcm2_x-Rcom_x2) > 30.0)
				 {
					 for (int i= 1001; i <= 2000; i++)
						 importData.PolymerKoordinaten[i][0] -= Gitter_x;
				 }
				//chain-jump over +X
				 if((rcm2_x-Rcom_x2) < -30.0)
				 {
					 for (int i= 1001; i <= 2000; i++)
						 importData.PolymerKoordinaten[i][0] += Gitter_x;
				 }
				 //chain-jump over -Y
				 if((rcm2_y-Rcom_y2) > 30.0)
				 {
					 for (int i= 1001; i <= 2000; i++)
						 importData.PolymerKoordinaten[i][1] -= Gitter_y;
				 }
				//chain-jump over +Y
				 if((rcm2_y-Rcom_y2) < -30.0)
				 {
					 for (int i= 1001; i <= 2000; i++)
						 importData.PolymerKoordinaten[i][1] += Gitter_y;
				 }
				 //chain-jump over -Z
				 if((rcm2_z-Rcom_z2) > 30.0)
				 {
					 for (int i= 1001; i <= 2000; i++)
						 importData.PolymerKoordinaten[i][2] -= Gitter_z;
				 }
				//chain-jump over +Z
				 if((rcm2_z-Rcom_z2) < -30.0)
				 {
					 for (int i= 1001; i <= 2000; i++)
						 importData.PolymerKoordinaten[i][2] += Gitter_z;
				 }
				 
				 
				 //1.st chain
				 {
					double Rg2 = 0.0;
					double Rg2_x = 0.0;
					double Rg2_y = 0.0;
					double Rg2_z = 0.0;
						
					 for (int i= (offset + 1); i <= (offset +1000); i++)
					  for (int j = i; j <= (offset + 1000); j++)
					  {
						  Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
						  Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
						  Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);

						  
					  }
					 
					Rg2 = Rg2_x + Rg2_y + Rg2_z;
					 
					Rg2 /= 1.0*(1000.0*1000.0);
					  
					Rg2_stat[(int) (importData.MCSTime/(deltaT))].AddValue(Math.sqrt(Rg2));
				 
					// COM
					 double rcmA_x =0.0;
					 double rcmA_y =0.0;
					 double rcmA_z =0.0;
					 
					 for (int i= 1; i <= 1000; i++)
					 {
						 rcmA_x += 1.0*(importData.PolymerKoordinaten[i][0]) ;
						 rcmA_y += 1.0*(importData.PolymerKoordinaten[i][1]) ;
						 rcmA_z += 1.0*(importData.PolymerKoordinaten[i][2]) ;  
					 }
					 rcmA_x /= 1000.0;
					 rcmA_y /= 1000.0;
					 rcmA_z /= 1000.0;
					 
					 Rcom_x1=rcmA_x;
					 Rcom_y1=rcmA_y;
					 Rcom_z1=rcmA_z;
					 
				 }
				 
				 offset = 1000;
				  
				 //2.nd chain
				 {
					double Rg2 = 0.0;
					double Rg2_x = 0.0;
					double Rg2_y = 0.0;
					double Rg2_z = 0.0;
						
					 for (int i= (offset + 1); i <= (offset +1000); i++)
					  for (int j = i; j <= (offset + 1000); j++)
					  {
						  Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
						  Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
						  Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);

						  
					  }
					 
					Rg2 = Rg2_x + Rg2_y + Rg2_z;
					 
					Rg2 /= 1.0*(1000.0*1000.0);
					  
					Rg2_stat[(int) (importData.MCSTime/(deltaT))].AddValue(Math.sqrt(Rg2));
				 
					 double rcmB_x =0.0;
					 double rcmB_y =0.0;
					 double rcmB_z =0.0;
					 
					 for (int i= 1001; i <= 2000; i++)
					 {
						 rcmB_x += 1.0*(importData.PolymerKoordinaten[i][0]) ;
						 rcmB_y += 1.0*(importData.PolymerKoordinaten[i][1]) ;
						 rcmB_z += 1.0*(importData.PolymerKoordinaten[i][2]) ;  
					 }
					 rcmB_x /= 1000.0;
					 rcmB_y /= 1000.0;
					 rcmB_z /= 1000.0;
					 
					 Rcom_x2=rcmB_x;
					 Rcom_y2=rcmB_y;
					 Rcom_z2=rcmB_z;
				 }
				 
				 //Rc2c - center to center distance
				// double Rc2c = (rcm2_x-rcm1_x)*(rcm2_x-rcm1_x) + (rcm2_y-rcm1_y)*(rcm2_y-rcm1_y) + (rcm2_z-rcm1_z)*(rcm2_z-rcm1_z);
				 double Rc2c = (Rcom_x2-Rcom_x1)*(Rcom_x2-Rcom_x1) + (Rcom_y2-Rcom_y1)*(Rcom_y2-Rcom_y1) + (Rcom_z2-Rcom_z1)*(Rcom_z2-Rcom_z1);
				 
				 Rc2c_stat[(int) (importData.MCSTime/(deltaT))].AddValue(Math.sqrt(Rc2c));
				 
				 
				 //MetricsG
				 double MetricsG = 0.0;
				 for (int i= 1; i <= 1000; i++)
				 {
					 double r1_x = 1.0*(importData.PolymerKoordinaten[i][0]) ;
					 double r1_y = 1.0*(importData.PolymerKoordinaten[i][1]) ;
					 double r1_z = 1.0*(importData.PolymerKoordinaten[i][2]) ;
					 
					 double r2_x = 1.0*(importData.PolymerKoordinaten[i+1000][0]) ;
					 double r2_y = 1.0*(importData.PolymerKoordinaten[i+1000][1]) ;
					 double r2_z = 1.0*(importData.PolymerKoordinaten[i+1000][2]) ;
					 
					 MetricsG += (r2_x-r1_x)*(r2_x-r1_x) + (r2_y-r1_y)*(r2_y-r1_y) + (r2_z-r1_z)*(r2_z-r1_z);
				 }
				 MetricsG /= 1000.0;
				
				 MetricsG_stat[(int) (importData.MCSTime/(deltaT))].AddValue(MetricsG);
				 
				 
					
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
