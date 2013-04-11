package EvalToolProject_bcc.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_bcc.PEG.Auswertung_Sterne_Verknuepfung_HepPEG;
import EvalToolProject_bcc.tools.BFMFileSaver;
import EvalToolProject_bcc.tools.BFMImportData;
import EvalToolProject_bcc.tools.Histogramm;
import EvalToolProject_bcc.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_bcc.tools.Statistik;

public class Auswertung_TrennungDNA_Gitterbelegung_Frei {


	
	
	String FileName;
	String FileDirectory;
	
	int[] Polymersystem;
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
//	int maxBox_x;
//	int maxBox_y;
//	int maxBox_z;
	
	int maxBox_4;
	int maxBox_8;
	int maxBox_16;
	int maxBox_32;
	int maxBox_64;
	
	int MinMono_4;
	int MinMono_8;
	int MinMono_16;
	int MinMono_32;
	int MinMono_64;
	
	int Gitter_x;
	int Gitter_y;
	int Gitter_z;
	
	
	BFMImportData importData;
	
	
	int[] dumpsystem;

	
	
	BFMFileSaver rg;
	
	int MONOMERZAHL;
	
	//int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	long deltaT;
	
//	int[][][] Anzahlgitter;
//	double[][][] Ordnungsgitter;
	
	int[][][] Anzahlgitter_4;
	int[][][] Anzahlgitter_8;
	int[][][] Anzahlgitter_16;
	int[][][] Anzahlgitter_32;
	int[][][] Anzahlgitter_64;
	double[][][] Ordnungsgitter_4;
	double[][][] Ordnungsgitter_8;
	double[][][] Ordnungsgitter_16;
	double[][][] Ordnungsgitter_32;
	double[][][] Ordnungsgitter_64;

	//int MinMono;
	
//	Histogramm[] HG_Density_Time;
	
	Histogramm[] HG_Density_Time_4;
	Histogramm[] HG_Density_Time_8;
	Histogramm[] HG_Density_Time_16;
	Histogramm[] HG_Density_Time_32;
	Histogramm[] HG_Density_Time_64;
	
	public Auswertung_TrennungDNA_Gitterbelegung_Frei(String fdir, String fname, String dirDst, int nrOfFiles)//, String skip, String current)
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
		
		//HG_Density_Time = new Histogramm[maxframe+1] ;
		HG_Density_Time_4 = new Histogramm[maxframe+1] ;
		HG_Density_Time_8 = new Histogramm[maxframe+1] ;
		HG_Density_Time_16 = new Histogramm[maxframe+1] ;
		HG_Density_Time_32 = new Histogramm[maxframe+1] ;
		HG_Density_Time_64 = new Histogramm[maxframe+1] ;
		
		for(int i = 0; i <= maxframe; i++)
		{
			//HG_Density_Time[i] = new Histogramm(-1.001, 1.001,100);
			HG_Density_Time_4[i] = new Histogramm(-1.001, 1.001,100);
			HG_Density_Time_8[i] = new Histogramm(-1.001, 1.001,100);
			HG_Density_Time_16[i] = new Histogramm(-1.001, 1.001,100);
			HG_Density_Time_32[i] = new Histogramm(-1.001, 1.001,100);
			HG_Density_Time_64[i] = new Histogramm(-1.001, 1.001,100);
			
		}
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		maxBox_4 = 4;
		maxBox_8 = 8;
		maxBox_16 = 16;
		maxBox_32 = 32;
		maxBox_64 = 64;
		
		MinMono_4 = 4;
		MinMono_8 = 4;
		MinMono_16 = 10;
		MinMono_32 = 15;
		MinMono_64 = 40;
		
		
	
	System.out.println("file : " +FileName );
	System.out.println("dir : " + FileDirectory);
	
	//LoadFile("DNALadderX1023YZ10Parted.bfm", 1);
	//LoadFile("Ring500ReplicationX1023YZ15ExtBond__01.bfm", 1);

	//for(int i = 10; i <= 100; i+=5)
	DecimalFormat dh = new DecimalFormat("000");
	
	//for(int i = 0; i <= 9; i++)
	//	LoadFile("buerste_straight_25_0"+i+".bfm", 1)		
	for(int j = 10; j <= nrOfFiles; j+=1)//i++)
		LoadFile(FileName+"__"+dh.format(j)+"_sep.bfm", 1, maxframe);
	
	
	BFMFileSaver saveVer_Stat_4 = new BFMFileSaver();
	saveVer_Stat_4.DateiAnlegen(dirDst+"/"+FileName+"_phi_zw_M0.2_P0.2_"+maxBox_4+"x"+maxBox_4+"x"+maxBox_4+".dat",false);
	BFMFileSaver saveVerM1_Stat_4 = new BFMFileSaver();
	saveVerM1_Stat_4.DateiAnlegen(dirDst+"/"+FileName+"_phi_kl_M0.8_"+maxBox_4+"x"+maxBox_4+"x"+maxBox_4+".dat",false);
	BFMFileSaver saveVerM2_Stat_4 = new BFMFileSaver();
	saveVerM2_Stat_4.DateiAnlegen(dirDst+"/"+FileName+"_phi_gr_P0.8_"+maxBox_4+"x"+maxBox_4+"x"+maxBox_4+".dat",false);
	
	BFMFileSaver saveVer_Stat_8 = new BFMFileSaver();
	saveVer_Stat_8.DateiAnlegen(dirDst+"/"+FileName+"_phi_zw_M0.2_P0.2_"+maxBox_8+"x"+maxBox_8+"x"+maxBox_8+".dat",false);
	BFMFileSaver saveVerM1_Stat_8 = new BFMFileSaver();
	saveVerM1_Stat_8.DateiAnlegen(dirDst+"/"+FileName+"_phi_kl_M0.8_"+maxBox_8+"x"+maxBox_8+"x"+maxBox_8+".dat",false);
	BFMFileSaver saveVerM2_Stat_8 = new BFMFileSaver();
	saveVerM2_Stat_8.DateiAnlegen(dirDst+"/"+FileName+"_phi_gr_P0.8_"+maxBox_8+"x"+maxBox_8+"x"+maxBox_8+".dat",false);
	
	BFMFileSaver saveVer_Stat_16 = new BFMFileSaver();
	saveVer_Stat_16.DateiAnlegen(dirDst+"/"+FileName+"_phi_zw_M0.2_P0.2_"+maxBox_16+"x"+maxBox_16+"x"+maxBox_16+".dat",false);
	BFMFileSaver saveVerM1_Stat_16 = new BFMFileSaver();
	saveVerM1_Stat_16.DateiAnlegen(dirDst+"/"+FileName+"_phi_kl_M0.8_"+maxBox_16+"x"+maxBox_16+"x"+maxBox_16+".dat",false);
	BFMFileSaver saveVerM2_Stat_16 = new BFMFileSaver();
	saveVerM2_Stat_16.DateiAnlegen(dirDst+"/"+FileName+"_phi_gr_P0.8_"+maxBox_16+"x"+maxBox_16+"x"+maxBox_16+".dat",false);
	
	BFMFileSaver saveVer_Stat_32 = new BFMFileSaver();
	saveVer_Stat_32.DateiAnlegen(dirDst+"/"+FileName+"_phi_zw_M0.2_P0.2_"+maxBox_32+"x"+maxBox_32+"x"+maxBox_32+".dat",false);
	BFMFileSaver saveVerM1_Stat_32 = new BFMFileSaver();
	saveVerM1_Stat_32.DateiAnlegen(dirDst+"/"+FileName+"_phi_kl_M0.8_"+maxBox_32+"x"+maxBox_32+"x"+maxBox_32+".dat",false);
	BFMFileSaver saveVerM2_Stat_32 = new BFMFileSaver();
	saveVerM2_Stat_32.DateiAnlegen(dirDst+"/"+FileName+"_phi_gr_P0.8_"+maxBox_32+"x"+maxBox_32+"x"+maxBox_32+".dat",false);
	
	BFMFileSaver saveVer_Stat_64 = new BFMFileSaver();
	saveVer_Stat_64.DateiAnlegen(dirDst+"/"+FileName+"_phi_zw_M0.2_P0.2_"+maxBox_64+"x"+maxBox_64+"x"+maxBox_64+".dat",false);
	BFMFileSaver saveVerM1_Stat_64 = new BFMFileSaver();
	saveVerM1_Stat_64.DateiAnlegen(dirDst+"/"+FileName+"_phi_kl_M0.8_"+maxBox_64+"x"+maxBox_64+"x"+maxBox_64+".dat",false);
	BFMFileSaver saveVerM2_Stat_64 = new BFMFileSaver();
	saveVerM2_Stat_64.DateiAnlegen(dirDst+"/"+FileName+"_phi_gr_P0.8_"+maxBox_64+"x"+maxBox_64+"x"+maxBox_64+".dat",false);
	
	
	double summe_4 = 0.0; double summek08_4 = 0.0; double summeg08_4 = 0.0;
	double summe_8 = 0.0; double summek08_8 = 0.0; double summeg08_8 = 0.0;
	double summe_16 = 0.0; double summek08_16 = 0.0; double summeg08_16 = 0.0;
	double summe_32 = 0.0; double summek08_32 = 0.0; double summeg08_32 = 0.0;
	double summe_64 = 0.0; double summek08_64 = 0.0; double summeg08_64 = 0.0;
	
	for(int zeit = 0; zeit < maxframe; zeit++)
	{
		summe_4 = 0.0; summek08_4 =0.0;	summeg08_4 =0.0;
		summe_8 = 0.0; summek08_8 =0.0;	summeg08_8 =0.0;
		summe_16 = 0.0; summek08_16 =0.0; summeg08_16 =0.0;
		summe_32 = 0.0; summek08_32 =0.0; summeg08_32 =0.0;
		summe_64 = 0.0; summek08_64 =0.0; summeg08_64 =0.0;
		
		for(int l = HG_Density_Time_4[zeit].GetBinOfValue(-0.2); l <= HG_Density_Time_4[zeit].GetBinOfValue(+0.2); l++)
		{
			//l=0 -> -1.0
			//l=1000 -> 0
			//l=2000 -> +1.0
			//(l-1000)/1000.0 = x bei phi(x)
				//summe += haufigkeitUzeit[l][zeit];
				//HG_Density_Time[(int) (importData.MCSTime/(deltaT))].AddValue(Ordnungsgitter[i][k][l]);
				summe_4 += HG_Density_Time_4[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_4[zeit].GetBinOfValue(-1.0); l <= HG_Density_Time_4[zeit].GetBinOfValue(-0.8); l++)
		{
			//l=0 -> -1.0
			//l=1000 -> 0
			//l=2000 -> +1.0
			//(l-1000)/1000.0 = x bei phi(x)
			
			summek08_4 += HG_Density_Time_4[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_4[zeit].GetBinOfValue(0.8); l <= HG_Density_Time_4[zeit].GetBinOfValue(1.0); l++)
		{
			//l=0 -> -1.0
			//l=1000 -> 0
			//l=2000 -> +1.0
			//(l-1000)/1000.0 = x bei phi(x)
			
			summeg08_4 += HG_Density_Time_4[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_8[zeit].GetBinOfValue(-0.2); l <= HG_Density_Time_8[zeit].GetBinOfValue(+0.2); l++)
		{
			summe_8 += HG_Density_Time_8[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_8[zeit].GetBinOfValue(-1.0); l <= HG_Density_Time_8[zeit].GetBinOfValue(-0.8); l++)
		{
			summek08_8 += HG_Density_Time_8[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_8[zeit].GetBinOfValue(0.8); l <= HG_Density_Time_8[zeit].GetBinOfValue(1.0); l++)
		{
			summeg08_8 += HG_Density_Time_4[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_16[zeit].GetBinOfValue(-0.2); l <= HG_Density_Time_16[zeit].GetBinOfValue(+0.2); l++)
		{
			summe_16 += HG_Density_Time_16[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_16[zeit].GetBinOfValue(-1.0); l <= HG_Density_Time_16[zeit].GetBinOfValue(-0.8); l++)
		{
			summek08_16 += HG_Density_Time_16[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_16[zeit].GetBinOfValue(0.8); l <= HG_Density_Time_16[zeit].GetBinOfValue(1.0); l++)
		{
			summeg08_16 += HG_Density_Time_16[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_32[zeit].GetBinOfValue(-0.2); l <= HG_Density_Time_32[zeit].GetBinOfValue(+0.2); l++)
		{
			summe_32 += HG_Density_Time_32[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_32[zeit].GetBinOfValue(-1.0); l <= HG_Density_Time_32[zeit].GetBinOfValue(-0.8); l++)
		{
			summek08_32 += HG_Density_Time_32[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_32[zeit].GetBinOfValue(0.8); l <= HG_Density_Time_32[zeit].GetBinOfValue(1.0); l++)
		{
			summeg08_32 += HG_Density_Time_32[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_64[zeit].GetBinOfValue(-0.2); l <= HG_Density_Time_64[zeit].GetBinOfValue(+0.2); l++)
		{
			summe_64 += HG_Density_Time_64[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_64[zeit].GetBinOfValue(-1.0); l <= HG_Density_Time_64[zeit].GetBinOfValue(-0.8); l++)
		{
			summek08_64 += HG_Density_Time_64[zeit].GetNrInBinNormiert(l);
		}
		
		for(int l = HG_Density_Time_64[zeit].GetBinOfValue(0.8); l <= HG_Density_Time_64[zeit].GetBinOfValue(1.0); l++)
		{
			summeg08_64 += HG_Density_Time_64[zeit].GetNrInBinNormiert(l);
		}
		
		saveVer_Stat_4.setzeZeile((deltaT*zeit) + " " + +summe_4);
		saveVerM1_Stat_4.setzeZeile((deltaT*zeit) + " " +summek08_4);
		saveVerM2_Stat_4.setzeZeile((deltaT*zeit) + " " +summeg08_4);

		saveVer_Stat_8.setzeZeile((deltaT*zeit) + " " + +summe_8);
		saveVerM1_Stat_8.setzeZeile((deltaT*zeit) + " " +summek08_8);
		saveVerM2_Stat_8.setzeZeile((deltaT*zeit) + " " +summeg08_8);
		
		saveVer_Stat_16.setzeZeile((deltaT*zeit) + " " + +summe_16);
		saveVerM1_Stat_16.setzeZeile((deltaT*zeit) + " " +summek08_16);
		saveVerM2_Stat_16.setzeZeile((deltaT*zeit) + " " +summeg08_16);
		
		saveVer_Stat_32.setzeZeile((deltaT*zeit) + " " + +summe_32);
		saveVerM1_Stat_32.setzeZeile((deltaT*zeit) + " " +summek08_32);
		saveVerM2_Stat_32.setzeZeile((deltaT*zeit) + " " +summeg08_32);
		
		saveVer_Stat_64.setzeZeile((deltaT*zeit) + " " + +summe_64);
		saveVerM1_Stat_64.setzeZeile((deltaT*zeit) + " " +summek08_64);
		saveVerM2_Stat_64.setzeZeile((deltaT*zeit) + " " +summeg08_64);
		//saveVer_Stat.setzeZeile((deltaT*i) + " " + +(summe/(1.0*anzahlzeit[zeit]))+ " "+anzahlzeit[zeit]);
		//saveVerM1_Stat.setzeZeile((deltaT*i) + " " +(summek08/(1.0*anzahlzeit[zeit]))+ " "+anzahlzeit[zeit]);
		//saveVerM2_Stat.setzeZeile((deltaT*i) + " " +(summeg08/(1.0*anzahlzeit[zeit]))+ " "+anzahlzeit[zeit]);

		
	}
	
	saveVer_Stat_4.setzeLeerzeile();
	saveVerM1_Stat_4.setzeLeerzeile();
	saveVerM2_Stat_4.setzeLeerzeile();

	saveVer_Stat_8.setzeLeerzeile();
	saveVerM1_Stat_8.setzeLeerzeile();
	saveVerM2_Stat_8.setzeLeerzeile();
	
	saveVer_Stat_16.setzeLeerzeile();
	saveVerM1_Stat_16.setzeLeerzeile();
	saveVerM2_Stat_16.setzeLeerzeile();
	
	saveVer_Stat_32.setzeLeerzeile();
	saveVerM1_Stat_32.setzeLeerzeile();
	saveVerM2_Stat_32.setzeLeerzeile();
	
	saveVer_Stat_64.setzeLeerzeile();
	saveVerM1_Stat_64.setzeLeerzeile();
	saveVerM2_Stat_64.setzeLeerzeile();
	
	saveVer_Stat_4.DateiSchliessen();
	saveVerM1_Stat_4.DateiSchliessen();
	saveVerM2_Stat_4.DateiSchliessen();
	
	saveVer_Stat_8.DateiSchliessen();
	saveVerM1_Stat_8.DateiSchliessen();
	saveVerM2_Stat_8.DateiSchliessen();
	
	saveVer_Stat_16.DateiSchliessen();
	saveVerM1_Stat_16.DateiSchliessen();
	saveVerM2_Stat_16.DateiSchliessen();
	
	saveVer_Stat_32.DateiSchliessen();
	saveVerM1_Stat_32.DateiSchliessen();
	saveVerM2_Stat_32.DateiSchliessen();
	
	saveVer_Stat_64.DateiSchliessen();
	saveVerM1_Stat_64.DateiSchliessen();
	saveVerM2_Stat_64.DateiSchliessen();
	
	
		
	}
	
	protected void LoadFile(String file, int startframe, int maxframe)
	{
		//FileName = file;
		System.out.println("file : " +file );
		System.out.println("dir : " + FileDirectory);
		System.out.println("lade System");
		LadeSystem(FileDirectory, file);	
		
//		Anzahlgitter = null;
//		Anzahlgitter = new int[Gitter_x/maxBox_x][Gitter_y/maxBox_y][Gitter_z/maxBox_z];
//		Ordnungsgitter = null;
//		Ordnungsgitter = new double[Gitter_x/maxBox_x][Gitter_y/maxBox_y][Gitter_z/maxBox_z];
		
		Anzahlgitter_4= null;
		Anzahlgitter_4 = new int[Gitter_x/maxBox_4][Gitter_y/maxBox_4][Gitter_z/maxBox_4];
		Ordnungsgitter_4 = null;
		Ordnungsgitter_4 = new double[Gitter_x/maxBox_4][Gitter_y/maxBox_4][Gitter_z/maxBox_4];
		
		Anzahlgitter_8= null;
		Anzahlgitter_8 = new int[Gitter_x/maxBox_8][Gitter_y/maxBox_8][Gitter_z/maxBox_8];
		Ordnungsgitter_8 = null;
		Ordnungsgitter_8 = new double[Gitter_x/maxBox_8][Gitter_y/maxBox_8][Gitter_z/maxBox_8];
		
		Anzahlgitter_16= null;
		Anzahlgitter_16 = new int[Gitter_x/maxBox_16][Gitter_y/maxBox_16][Gitter_z/maxBox_16];
		Ordnungsgitter_16 = null;
		Ordnungsgitter_16 = new double[Gitter_x/maxBox_16][Gitter_y/maxBox_16][Gitter_z/maxBox_16];
		
		Anzahlgitter_32= null;
		Anzahlgitter_32 = new int[Gitter_x/maxBox_32][Gitter_y/maxBox_32][Gitter_z/maxBox_32];
		Ordnungsgitter_32 = null;
		Ordnungsgitter_32 = new double[Gitter_x/maxBox_32][Gitter_y/maxBox_32][Gitter_z/maxBox_32];
		
		Anzahlgitter_64= null;
		Anzahlgitter_64 = new int[Gitter_x/maxBox_64][Gitter_y/maxBox_64][Gitter_z/maxBox_64];
		Ordnungsgitter_64 = null;
		Ordnungsgitter_64 = new double[Gitter_x/maxBox_64][Gitter_y/maxBox_64][Gitter_z/maxBox_64];
		
		
		
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
		else new Auswertung_TrennungDNA_Gitterbelegung_Frei(args[0], args[1], args[2] , Integer.parseInt(args[3]));//,args[1],args[2]);
	
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
				

				for(int i = 0; i < Gitter_x/maxBox_4; i++)
					for (int k = 0; k < Gitter_y/maxBox_4; k++)
						for (int l = 0; l < Gitter_z/maxBox_4; l++)
							{
								Anzahlgitter_4[i][k][l] =0;
								Ordnungsgitter_4[i][k][l] = 0.0;
							}
				
				for(int i = 0; i < Gitter_x/maxBox_8; i++)
					for (int k = 0; k < Gitter_y/maxBox_8; k++)
						for (int l = 0; l < Gitter_z/maxBox_8; l++)
						{
								Anzahlgitter_8[i][k][l] =0;
								Ordnungsgitter_8[i][k][l] = 0.0;
						}
				
				for(int i = 0; i < Gitter_x/maxBox_16; i++)
					for (int k = 0; k < Gitter_y/maxBox_16; k++)
						for (int l = 0; l < Gitter_z/maxBox_16; l++)
						{
								Anzahlgitter_16[i][k][l] =0;
								Ordnungsgitter_16[i][k][l] = 0.0;
						}
				
				for(int i = 0; i < Gitter_x/maxBox_32; i++)
					for (int k = 0; k < Gitter_y/maxBox_32; k++)
						for (int l = 0; l < Gitter_z/maxBox_32; l++)
						{
								Anzahlgitter_32[i][k][l] =0;
								Ordnungsgitter_32[i][k][l] = 0.0;
						}
				
				for(int i = 0; i < Gitter_x/maxBox_64; i++)
					for (int k = 0; k < Gitter_y/maxBox_64; k++)
						for (int l = 0; l < Gitter_z/maxBox_64; l++)
						{
								Anzahlgitter_64[i][k][l] =0;
								Ordnungsgitter_64[i][k][l] = 0.0;
						}
				
				for(int nr = 1; nr <= 2000; nr++)
				{
					Anzahlgitter_4[xwert(importData.Polymersystem[nr])/maxBox_4][ywert(importData.Polymersystem[nr])/maxBox_4][zwert(importData.Polymersystem[nr])/maxBox_4] ++;
					Anzahlgitter_8[xwert(importData.Polymersystem[nr])/maxBox_8][ywert(importData.Polymersystem[nr])/maxBox_8][zwert(importData.Polymersystem[nr])/maxBox_8] ++;
					Anzahlgitter_16[xwert(importData.Polymersystem[nr])/maxBox_16][ywert(importData.Polymersystem[nr])/maxBox_16][zwert(importData.Polymersystem[nr])/maxBox_16] ++;
					Anzahlgitter_32[xwert(importData.Polymersystem[nr])/maxBox_32][ywert(importData.Polymersystem[nr])/maxBox_32][zwert(importData.Polymersystem[nr])/maxBox_32] ++;
					Anzahlgitter_64[xwert(importData.Polymersystem[nr])/maxBox_64][ywert(importData.Polymersystem[nr])/maxBox_64][zwert(importData.Polymersystem[nr])/maxBox_64] ++;
					
				}
				
				for(int nr = 1; nr <= 1000; nr++)
				{
					Ordnungsgitter_4[xwert(importData.Polymersystem[nr])/maxBox_4][ywert(importData.Polymersystem[nr])/maxBox_4][zwert(importData.Polymersystem[nr])/maxBox_4] ++;
					Ordnungsgitter_8[xwert(importData.Polymersystem[nr])/maxBox_8][ywert(importData.Polymersystem[nr])/maxBox_8][zwert(importData.Polymersystem[nr])/maxBox_8] ++;
					Ordnungsgitter_16[xwert(importData.Polymersystem[nr])/maxBox_16][ywert(importData.Polymersystem[nr])/maxBox_16][zwert(importData.Polymersystem[nr])/maxBox_16] ++;
					Ordnungsgitter_32[xwert(importData.Polymersystem[nr])/maxBox_32][ywert(importData.Polymersystem[nr])/maxBox_32][zwert(importData.Polymersystem[nr])/maxBox_32] ++;
					Ordnungsgitter_64[xwert(importData.Polymersystem[nr])/maxBox_64][ywert(importData.Polymersystem[nr])/maxBox_64][zwert(importData.Polymersystem[nr])/maxBox_64] ++;
					
				}
				
				for(int nr = 1001; nr <= 2000; nr++)
				{
					Ordnungsgitter_4[xwert(importData.Polymersystem[nr])/maxBox_4][ywert(importData.Polymersystem[nr])/maxBox_4][zwert(importData.Polymersystem[nr])/maxBox_4]--;
					Ordnungsgitter_8[xwert(importData.Polymersystem[nr])/maxBox_8][ywert(importData.Polymersystem[nr])/maxBox_8][zwert(importData.Polymersystem[nr])/maxBox_8]--;
					Ordnungsgitter_16[xwert(importData.Polymersystem[nr])/maxBox_16][ywert(importData.Polymersystem[nr])/maxBox_16][zwert(importData.Polymersystem[nr])/maxBox_16]--;
					Ordnungsgitter_32[xwert(importData.Polymersystem[nr])/maxBox_32][ywert(importData.Polymersystem[nr])/maxBox_32][zwert(importData.Polymersystem[nr])/maxBox_32]--;
					Ordnungsgitter_64[xwert(importData.Polymersystem[nr])/maxBox_64][ywert(importData.Polymersystem[nr])/maxBox_64][zwert(importData.Polymersystem[nr])/maxBox_64]--;
					
				}
				
				for(int i = 0; i < Gitter_x/maxBox_4; i++)
					for (int k = 0; k < Gitter_y/maxBox_4; k++)
						for (int l = 0; l < Gitter_z/maxBox_4; l++)
							{
								if(Anzahlgitter_4[i][k][l] > MinMono_4)
								{
									Ordnungsgitter_4[i][k][l] /= Anzahlgitter_4[i][k][l];
									//haufigkeitUzeit[(int) (1000*Ordnungsgitter[i][k][l])+1000][frame-1] += 1;
									HG_Density_Time_4[(int) (importData.MCSTime/(deltaT))].AddValue(Ordnungsgitter_4[i][k][l]);
								}
								else Ordnungsgitter_4[i][k][l] = Double.NaN;
							}
				
				for(int i = 0; i < Gitter_x/maxBox_8; i++)
					for (int k = 0; k < Gitter_y/maxBox_8; k++)
						for (int l = 0; l < Gitter_z/maxBox_8; l++)
							{
								if(Anzahlgitter_8[i][k][l] > MinMono_8)
								{
									Ordnungsgitter_8[i][k][l] /= Anzahlgitter_8[i][k][l];
									//haufigkeitUzeit[(int) (1000*Ordnungsgitter[i][k][l])+1000][frame-1] += 1;
									HG_Density_Time_8[(int) (importData.MCSTime/(deltaT))].AddValue(Ordnungsgitter_8[i][k][l]);
								}
								else Ordnungsgitter_8[i][k][l] = Double.NaN;
							}
				
				for(int i = 0; i < Gitter_x/maxBox_16; i++)
					for (int k = 0; k < Gitter_y/maxBox_16; k++)
						for (int l = 0; l < Gitter_z/maxBox_16; l++)
							{
								if(Anzahlgitter_16[i][k][l] > MinMono_16)
								{
									Ordnungsgitter_16[i][k][l] /= Anzahlgitter_16[i][k][l];
									//haufigkeitUzeit[(int) (1000*Ordnungsgitter[i][k][l])+1000][frame-1] += 1;
									HG_Density_Time_16[(int) (importData.MCSTime/(deltaT))].AddValue(Ordnungsgitter_16[i][k][l]);
								}
								else Ordnungsgitter_16[i][k][l] = Double.NaN;
							}
			
				for(int i = 0; i < Gitter_x/maxBox_32; i++)
					for (int k = 0; k < Gitter_y/maxBox_32; k++)
						for (int l = 0; l < Gitter_z/maxBox_32; l++)
							{
								if(Anzahlgitter_32[i][k][l] > MinMono_32)
								{
									Ordnungsgitter_32[i][k][l] /= Anzahlgitter_32[i][k][l];
									//haufigkeitUzeit[(int) (1000*Ordnungsgitter[i][k][l])+1000][frame-1] += 1;
									HG_Density_Time_32[(int) (importData.MCSTime/(deltaT))].AddValue(Ordnungsgitter_32[i][k][l]);
								}
								else Ordnungsgitter_32[i][k][l] = Double.NaN;
							}
			
				for(int i = 0; i < Gitter_x/maxBox_64; i++)
					for (int k = 0; k < Gitter_y/maxBox_64; k++)
						for (int l = 0; l < Gitter_z/maxBox_64; l++)
							{
								if(Anzahlgitter_64[i][k][l] > MinMono_64)
								{
									Ordnungsgitter_64[i][k][l] /= Anzahlgitter_64[i][k][l];
									//haufigkeitUzeit[(int) (1000*Ordnungsgitter[i][k][l])+1000][frame-1] += 1;
									HG_Density_Time_64[(int) (importData.MCSTime/(deltaT))].AddValue(Ordnungsgitter_64[i][k][l]);
								}
								else Ordnungsgitter_64[i][k][l] = Double.NaN;
							}
			
			
			
			if((frame %50) == 0)
				System.out.println(""+frame);
			
			
			
				
				
				 
					
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
