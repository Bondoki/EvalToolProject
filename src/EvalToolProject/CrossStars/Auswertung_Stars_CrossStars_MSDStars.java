package EvalToolProject.CrossStars;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import EvalToolProject.tools.BFMFileSaver;
import EvalToolProject.tools.BFMImportData;
import EvalToolProject.tools.Int_IntArrayList_Hashtable;
import EvalToolProject.tools.Statistik;

public class Auswertung_Stars_CrossStars_MSDStars {


	
	
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
	
	int NrOfCrosslinker = 0;
	int CrosslinkerFunctionality = 0;
	
	int NrOfStars = 0;
	int NrOfMonomersPerStarArm = 0;
	
	int[] dumpsystem;

	/*Statistik[] GesamtStat_x;
	Statistik[] GesamtStat_y;
	Statistik[] GesamtStat_z;*/
	
	double [] Rcm_x;
	double [] Rcm_y;
	double [] Rcm_z;
	
	Statistik MSD_stat;
	Statistik RootMSD_stat;
	//Statistik rg_x_stat;
	//Statistik rg_y_stat;
	//Statistik rg_z_stat;
	
	BFMFileSaver rg;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	//Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	long deltaT;
	
	public Auswertung_Stars_CrossStars_MSDStars(String SrcDir, String SrcName, String DstDir)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		//FileName = SrcName;
		FileDirectory = SrcDir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		rg = new BFMFileSaver();
		rg.DateiAnlegen(DstDir+ SrcName+"_MSD_Stars.dat", false);
		rg.setzeZeile("# MSD for Stars");
		rg.setzeZeile("# t[MCS] <MSD^0.5> <MSD>  <(MSD)^2> dMSD N");
		rg.setzeZeile("0 0 0 0 0 0");
		
		MSD_stat = new Statistik();
		RootMSD_stat = new Statistik();
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ SrcName+".bfm");
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		
		
		System.out.println("file : " +SrcName );
		System.out.println("dir : " + FileDirectory);
		
		DecimalFormat dh = new DecimalFormat("0.0000");
		
		//for(int i = 0; i <= 9; i++)
		//	LoadFile("buerste_straight_25_0"+i+".bfm", 1);
		
		LoadFile(SrcName+".bfm", currentFrame);
		
		//rg.setzeKommentar("c <Rg2> <(Rg2)2> dF N");
		//rg.setzeZeile((importData.NrOfMonomers*8.0/(Gitter_x*Gitter_y*Gitter_z))+" "+MSD_stat.ReturnM1()+" "+MSD_stat.ReturnM2()+" "+( 2.0* MSD_stat.ReturnSigma()/ Math.sqrt(1.0*MSD_stat.ReturnN() ))+ " " + MSD_stat.ReturnN());
		
		rg.DateiSchliessen();
		
	    NumberFormat nf = NumberFormat.getInstance(Locale.US);

	    nf.setMaximumFractionDigits(2);
	    
	    BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(DstDir+"/"+SrcName+"_MSDRoot_Stars.batch", false);
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ NXY \""+DstDir+SrcName+"_MSD_Stars.dat\"");
	    xmgrace.setzeZeile(" world xmin 0");
	    xmgrace.setzeZeile(" world xmax "+importData.MCSTime);//225000");
	    xmgrace.setzeZeile(" world ymin 0");
	    xmgrace.setzeZeile(" world ymax "+RootMSD_stat.ReturnM1());
	    xmgrace.setzeZeile(" xaxis label \"time [MCS]\"");
	    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace.setzeZeile(" xaxis tick major "+(maxframe-1)*deltaT/4);
	    //xmgrace2.setzeZeile(" xaxis tick minor 1");
	    xmgrace.setzeZeile(" yaxis label \"MSD\\S0.5\"");
	    //xmgrace2.setzeZeile(" yaxis tick major 0.1");
	    //xmgrace2.setzeZeile(" yaxis tick minor 0.02");

	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 legend \"MSD\\S0.5\"");

	   // xmgrace2.setzeZeile(" s0 symbol 1");
	  //  xmgrace2.setzeZeile(" s0 legend \"<f>="+nf.format(DurchschnittsfunktionalitaetHEP)+"\"");

	   // xmgrace2.setzeZeile(" s1 line color 2");
	   // xmgrace2.setzeZeile(" s1 line linestyle 1");
	   // xmgrace2.setzeZeile(" s1 line linewidth 1.5");
	   // xmgrace2.setzeZeile(" s1 legend \"MSD\"");
	    
	    xmgrace.setzeZeile(" s1 off");
	    xmgrace.setzeZeile(" s2 off");
	    xmgrace.setzeZeile(" s3 off");
	    xmgrace.setzeZeile(" s4 off");
	    
	   
	    
	    
	    xmgrace.setzeZeile(" subtitle \"N\\sCrosslinker\\N="+NrOfCrosslinker+"; f\\sCross\\N="+CrosslinkerFunctionality+"; N\\sStars\\N="+NrOfStars+"; N\\sStars\\N\\SArm\\N="+NrOfMonomersPerStarArm+"; \\f{Symbol}a\\f{}="+nf.format((CrosslinkerFunctionality*NrOfCrosslinker/(4.0*NrOfStars)))+"; \"");

	    xmgrace.setzeZeile(" SAVEALL \""+DstDir+SrcName+"_MSDRoot_Stars.agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+DstDir+SrcName+"_MSDRoot_Stars.ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	    
	    try {
		      Runtime.getRuntime().exec("xmgrace -batch "+DstDir+"/"+SrcName+"_MSDRoot_Stars.batch -nosafe -hardcopy");
		    } catch (Exception e) {
		      System.err.println(e.toString());
		    }
	    
	    BFMFileSaver xmgrace2 = new BFMFileSaver();
		xmgrace2.DateiAnlegen(DstDir+"/"+SrcName+"_MSD_Stars.batch", false);
		xmgrace2.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace2.setzeZeile("FOCUS G0");
	    xmgrace2.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace2.setzeZeile("READ NXY \""+DstDir+SrcName+"_MSD_Stars.dat\"");
	    xmgrace2.setzeZeile(" world xmin 0");
	    xmgrace2.setzeZeile(" world xmax "+importData.MCSTime);//225000");
	    xmgrace2.setzeZeile(" world ymin 0");
	    xmgrace2.setzeZeile(" world ymax "+MSD_stat.ReturnM1());
	    xmgrace2.setzeZeile(" xaxis label \"time [MCS]\"");
	    xmgrace2.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace2.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace2.setzeZeile(" xaxis tick major "+(maxframe-1)*deltaT/4);
	    //xmgrace2.setzeZeile(" xaxis tick minor 1");
	    xmgrace2.setzeZeile(" yaxis label \"MSD\"");
	    //xmgrace2.setzeZeile(" yaxis tick major 0.1");
	    //xmgrace2.setzeZeile(" yaxis tick minor 0.02");

	    xmgrace2.setzeZeile(" s0 off");
	    //xmgrace2.setzeZeile(" s0 line color 1");
	    //xmgrace2.setzeZeile(" s0 line linestyle 1");
	   // xmgrace2.setzeZeile(" s0 line linewidth 1.5");
	    //xmgrace2.setzeZeile(" s0 legend \"MSD\\S0.5\"");

	   // xmgrace2.setzeZeile(" s0 symbol 1");
	  //  xmgrace2.setzeZeile(" s0 legend \"<f>="+nf.format(DurchschnittsfunktionalitaetHEP)+"\"");

	    xmgrace2.setzeZeile(" s1 line color 2");
	    xmgrace2.setzeZeile(" s1 line linestyle 1");
	    xmgrace2.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace2.setzeZeile(" s1 legend \"MSD\"");
	    
	    //xmgrace2.setzeZeile(" s1 off");
	    xmgrace2.setzeZeile(" s2 off");
	    xmgrace2.setzeZeile(" s3 off");
	    xmgrace2.setzeZeile(" s4 off");
	    
	   
	    
	    
	    xmgrace2.setzeZeile(" subtitle \"N\\sCrosslinker\\N="+NrOfCrosslinker+"; f\\sCross\\N="+CrosslinkerFunctionality+"; N\\sStars\\N="+NrOfStars+"; N\\sStars\\N\\SArm\\N="+NrOfMonomersPerStarArm+"; \\f{Symbol}a\\f{}="+nf.format((NrOfCrosslinker/NrOfStars))+"; \"");

	    xmgrace2.setzeZeile(" SAVEALL \""+DstDir+SrcName+"_MSD_Stars.agr\"");

	    xmgrace2.setzeZeile(" PRINT TO \""+DstDir+SrcName+"_MSD_Stars.ps\"");
	    xmgrace2.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace2.setzeZeile("PRINT");
		
	    xmgrace2.DateiSchliessen();
	    
	    try {
		      Runtime.getRuntime().exec("xmgrace -batch "+DstDir+"/"+SrcName+"_MSD_Stars.batch -nosafe -hardcopy");
		    } catch (Exception e) {
		      System.err.println(e.toString());
		    }
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
		if(args.length != 3)
		{
			System.out.println("Berechnung MSD von Sternen im StarsCross-Netzwerk");
			System.out.println("USAGE: dirSrc/ HydrogelStar_Star_aaa_NStar_bbb__Cross_ccc_F_f__NoPerXYZ128[__xxx.bfm] dirDst/");
		}
		else new Auswertung_Stars_CrossStars_MSDStars(args[0], args[1], args[2]);
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
		NrOfCrosslinker = importData.NrOfCrosslinker;
		CrosslinkerFunctionality = importData.CrosslinkerFunctionality;
		
		Rcm_x = new double[NrOfStars+1];
		Rcm_y = new double[NrOfStars+1];
		Rcm_z = new double[NrOfStars+1];
		
		System.out.println("Stars: " + NrOfStars + "    ArmLength: "+ NrOfMonomersPerStarArm + "    Crosslinker:"+NrOfCrosslinker + "    CrossFunc: "+ CrosslinkerFunctionality);
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
				
				
				
				
				MSD_stat.clear();
				RootMSD_stat.clear();
				 int offset = 0;// 90*NrOfHeparin;
				 
				 if(frame == 1)
				 {
					 
					 for (int k= 1; k <= NrOfStars; k++)
					 {
						 double Rcm1 = 0.0;
						double Rcm_x1 = 0.0;
						double Rcm_y1 = 0.0;
						double Rcm_z1 = 0.0;
							
						 for (int i= (offset + (4*NrOfMonomersPerStarArm + 1)*(k-1) +1); i <= offset +( 4*NrOfMonomersPerStarArm + 1)*k; i++)
						  {
							  Rcm_x1 += 1.0*(importData.PolymerKoordinaten[i][0]);// - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
							  Rcm_y1 += 1.0*(importData.PolymerKoordinaten[i][1]);// - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
							  Rcm_z1 += 1.0*(importData.PolymerKoordinaten[i][2]);// - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);

							  
						  }
						 Rcm_x1 /= 1.0*(4*NrOfMonomersPerStarArm + 1);
						 Rcm_y1 /= 1.0*(4*NrOfMonomersPerStarArm + 1);
						 Rcm_z1 /= 1.0*(4*NrOfMonomersPerStarArm + 1);
						 
						 Rcm_x[k] = Rcm_x1;
						 Rcm_y[k] = Rcm_y1;
						 Rcm_z[k] = Rcm_z1;
					 }
				 }
				 else
				 {
					 
				 for (int k= 1; k <= NrOfStars; k++)
				 {
					 double Rcm1 = 0.0;
					double Rcm_x1 = 0.0;
					double Rcm_y1 = 0.0;
					double Rcm_z1 = 0.0;
						
					 for (int i= (offset + (4*NrOfMonomersPerStarArm + 1)*(k-1) +1); i <= offset +( 4*NrOfMonomersPerStarArm + 1)*k; i++)
					  {
						  Rcm_x1 += 1.0*(importData.PolymerKoordinaten[i][0]);// - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
						  Rcm_y1 += 1.0*(importData.PolymerKoordinaten[i][1]);// - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
						  Rcm_z1 += 1.0*(importData.PolymerKoordinaten[i][2]);// - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);

						  
					  }
					 Rcm_x1 /= 1.0*(4*NrOfMonomersPerStarArm + 1);
					 Rcm_y1 /= 1.0*(4*NrOfMonomersPerStarArm + 1);
					 Rcm_z1 /= 1.0*(4*NrOfMonomersPerStarArm + 1);
					 
					
					 
					 
					 
					
					MSD_stat.AddValue((Rcm_x1 - Rcm_x[k])*(Rcm_x1-Rcm_x[k]) + (Rcm_y1 - Rcm_y[k])*(Rcm_y1-Rcm_y[k]) + (Rcm_z1 - Rcm_z[k])*(Rcm_z1-Rcm_z[k]));
					RootMSD_stat.AddValue(Math.sqrt((Rcm_x1 - Rcm_x[k])*(Rcm_x1-Rcm_x[k]) + (Rcm_y1 - Rcm_y[k])*(Rcm_y1-Rcm_y[k]) + (Rcm_z1 - Rcm_z[k])*(Rcm_z1-Rcm_z[k])));
				 }
				 rg.setzeZeile(importData.MCSTime + " "+ RootMSD_stat.ReturnM1() + " " + MSD_stat.ReturnM1()+" "+MSD_stat.ReturnM2()+" "+( 2.0* MSD_stat.ReturnSigma()/ Math.sqrt(1.0*MSD_stat.ReturnN() ))+ " " + MSD_stat.ReturnN());
				
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
