package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_ice.SingleObjects.Auswertung_Brush_Time_Rg_COM;
import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Histogramm;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Int_IntArrayList_Table;
import EvalToolProject_ice.tools.IntArrayList;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Dendrimer_g1g2g3 {


	
	
	String FileName;
	String FileNameWithEnd;
	String FileDirectory;
	
	
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
	int Gitter_x;
	int Gitter_y;
	int Gitter_z;
	
	
	BFMImportData importData;
	
	int MONOMERZAHL;
	
	Statistik durchschnittbond;
	
	
	Int_IntArrayList_Table Bindungsnetzwerk; 
	
	long deltaT;
	
	int NrOfMonoPerDendrimer;
	int NrOfDendrimer;
	
	
	double[][] rCOM_X; //[time][idx=chainnumber]
	double[][] rCOM_Y; //[time][idx=chainnumber]
	double[][] rCOM_Z; //[time][idx=chainnumber]
	
	double[][] rN2_X; //[time][idx=chainnumber] Center Monomer
	double[][] rN2_Y; //[time][idx=chainnumber] Center Monomer
	double[][] rN2_Z; //[time][idx=chainnumber] Center Monomer
	
	double[][] rAllEnds_X; //[time][idx=end group number] Center Monomer
	double[][] rAllEnds_Y; //[time][idx=end group number] Center Monomer
	double[][] rAllEnds_Z; //[time][idx=end group number] Center Monomer
	
	
	double[][] rKernel_X; //[time][idx=chainnumber] First Chain Monomer
	double[][] rKernel_Y; //[time][idx=chainnumber] First Chain Monomer
	double[][] rKernel_Z; //[time][idx=chainnumber] First Chain Monomer
	
	double[][] rLast_X; //[time][idx=chainnumber] Last Chain Monomer
	double[][] rLast_Y; //[time][idx=chainnumber] Last Chain Monomer
	double[][] rLast_Z; //[time][idx=chainnumber] Last Chain Monomer
	
	
	Statistik[] g1_Time_Stat;
	Statistik[] g2_Time_Stat; //ends to com
	Statistik[] g2kernelCOM_Time_Stat;   // kernel to com
	Statistik[] g3_Time_Stat;
	Statistik[] g4_Time_Stat;
	
	int startAtFrame;
	int diffFrame;
	
	
	String dstDir;
	
	IntArrayList EndArray;
	
	
	public Auswertung_Dendrimer_g1g2g3(String fdir, String fname, String dirDst, int nrOfMonoPerDendrimer, int nrOfDendrimer, int _startAtFrame, int _diffFrame)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileNameWithEnd = fname;
		FileName = fname.replaceAll(".ice", "").replaceAll(".bfm", "").replaceFirst(".xo", "");
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		dstDir = dirDst;
		
		NrOfMonoPerDendrimer=nrOfMonoPerDendrimer;
		NrOfDendrimer=nrOfDendrimer;
		
		startAtFrame = _startAtFrame-1;
		diffFrame = _diffFrame;
		
		System.out.println("-7%6="+ (-7%6));
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ FileNameWithEnd);
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT + "  startAtFrame:" +startAtFrame);
		//End - Determine MaxFrame out of the first file
		
		System.out.println("Difference in frame: "+ diffFrame+ "  = dt "+ (diffFrame*deltaT));
		
		
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		rCOM_X = new double[maxframe+1][nrOfDendrimer]; //[time][idx=chainnumber]
		rCOM_Y = new double[maxframe+1][nrOfDendrimer];//[time][idx=chainnumber]
		rCOM_Z = new double[maxframe+1][nrOfDendrimer];//[time][idx=chainnumber]
		
		rN2_X = new double[maxframe+1][nrOfDendrimer]; //[time][idx=chainnumber]
		rN2_Y = new double[maxframe+1][nrOfDendrimer]; //[time][idx=chainnumber]
		rN2_Z = new double[maxframe+1][nrOfDendrimer]; //[time][idx=chainnumber]
		
		rAllEnds_X = new double[maxframe+1][193]; //[time][idx=chainnumber]
		rAllEnds_Y = new double[maxframe+1][193]; //[time][idx=chainnumber]
		rAllEnds_Z = new double[maxframe+1][193]; //[time][idx=chainnumber]
		
		
		rKernel_X = new double[maxframe+1][nrOfDendrimer]; //[time][idx=chainnumber]
		rKernel_Y = new double[maxframe+1][nrOfDendrimer]; //[time][idx=chainnumber]
		rKernel_Z = new double[maxframe+1][nrOfDendrimer]; //[time][idx=chainnumber]
		
		rLast_X = new double[maxframe+1][nrOfDendrimer]; //[time][idx=chainnumber]
		rLast_Y = new double[maxframe+1][nrOfDendrimer]; //[time][idx=chainnumber]
		rLast_Z = new double[maxframe+1][nrOfDendrimer]; //[time][idx=chainnumber]
		
		
		g1_Time_Stat = new Statistik[maxframe+1];
		g2_Time_Stat = new Statistik[maxframe+1];
		g2kernelCOM_Time_Stat = new Statistik[maxframe+1];
		g3_Time_Stat = new Statistik[maxframe+1];
		g4_Time_Stat = new Statistik[maxframe+1];
		
		for(int i = 0; i <= maxframe; i++)
		{
			g1_Time_Stat[i] = new Statistik();
			g2_Time_Stat[i] = new Statistik();
			g2kernelCOM_Time_Stat[i] = new Statistik();
			g3_Time_Stat[i] = new Statistik();
			g4_Time_Stat[i] = new Statistik();
		}
		
		
		
		durchschnittbond = new Statistik();
		
		
		
		DecimalFormat dh = new DecimalFormat("000");
		
		//load file
		LoadFile(FileNameWithEnd, 1);
		
		System.out.println("calculation g1, g2, g3");
		
		//calculate g1, g2, g3
		for(int dt = 0; dt <= maxframe; dt+=diffFrame)
			{
			System.out.print("dt="+dt+ " ");
			
			for(int time = 0; time <= maxframe; time++)
				if( (startAtFrame+time+dt) < maxframe)
				{
					for (int nrChains= 0; nrChains < NrOfDendrimer; nrChains++)
					{
						double diffRcom_X = rCOM_X[startAtFrame+time+dt][nrChains]-rCOM_X[startAtFrame+dt][nrChains];
						double diffRcom_Y = rCOM_Y[startAtFrame+time+dt][nrChains]-rCOM_Y[startAtFrame+dt][nrChains];
						double diffRcom_Z = rCOM_Z[startAtFrame+time+dt][nrChains]-rCOM_Z[startAtFrame+dt][nrChains];
						
						double diffRN2_X = rN2_X[startAtFrame+time+dt][nrChains]-rN2_X[startAtFrame+dt][nrChains];
						double diffRN2_Y = rN2_Y[startAtFrame+time+dt][nrChains]-rN2_Y[startAtFrame+dt][nrChains];
						double diffRN2_Z = rN2_Z[startAtFrame+time+dt][nrChains]-rN2_Z[startAtFrame+dt][nrChains];
						
						for(int ends=0; ends <= 192; ends++)
						{
							double diffRends2_X = rAllEnds_X[startAtFrame+time+dt][ends]-rAllEnds_X[startAtFrame+dt][ends];
							double diffRends2_Y = rAllEnds_Y[startAtFrame+time+dt][ends]-rAllEnds_Y[startAtFrame+dt][ends];
							double diffRends2_Z = rAllEnds_Z[startAtFrame+time+dt][ends]-rAllEnds_Z[startAtFrame+dt][ends];
							
							double diffRN2MRcom_X = rAllEnds_X[startAtFrame+time+dt][ends]-rCOM_X[startAtFrame+time+dt][nrChains]-(rAllEnds_X[startAtFrame+dt][ends]-rCOM_X[startAtFrame+dt][nrChains]);
							double diffRN2MRcom_Y = rAllEnds_Y[startAtFrame+time+dt][ends]-rCOM_Y[startAtFrame+time+dt][nrChains]-(rAllEnds_Y[startAtFrame+dt][ends]-rCOM_Y[startAtFrame+dt][nrChains]);
							double diffRN2MRcom_Z = rAllEnds_Z[startAtFrame+time+dt][ends]-rCOM_Z[startAtFrame+time+dt][nrChains]-(rAllEnds_Z[startAtFrame+dt][ends]-rCOM_Z[startAtFrame+dt][nrChains]);
							
							
							g2_Time_Stat[time].AddValue(diffRN2MRcom_X*diffRN2MRcom_X + diffRN2MRcom_Y*diffRN2MRcom_Y + diffRN2MRcom_Z*diffRN2MRcom_Z);
							
							g4_Time_Stat[time].AddValue(diffRends2_X*diffRends2_X + diffRends2_Y*diffRends2_Y + diffRends2_Z*diffRends2_Z);
							
						}
						
						double diffRKernel_X = rKernel_X[startAtFrame+time+dt][nrChains]-rKernel_X[startAtFrame+dt][nrChains];
						double diffRKernel_Y = rKernel_Y[startAtFrame+time+dt][nrChains]-rKernel_Y[startAtFrame+dt][nrChains];
						double diffRKernel_Z = rKernel_Z[startAtFrame+time+dt][nrChains]-rKernel_Z[startAtFrame+dt][nrChains];
						
						double diffRLast_X = rLast_X[startAtFrame+time+dt][nrChains]-rLast_X[startAtFrame+dt][nrChains];
						double diffRLast_Y = rLast_Y[startAtFrame+time+dt][nrChains]-rLast_Y[startAtFrame+dt][nrChains];
						double diffRLast_Z = rLast_Z[startAtFrame+time+dt][nrChains]-rLast_Z[startAtFrame+dt][nrChains];
						
						double diffRKernelRcom_X = rKernel_X[startAtFrame+time+dt][nrChains]-rCOM_X[startAtFrame+time+dt][nrChains]-(rKernel_X[startAtFrame+dt][nrChains]-rCOM_X[startAtFrame+dt][nrChains]);
						double diffRKernelRcom_Y = rKernel_Y[startAtFrame+time+dt][nrChains]-rCOM_Y[startAtFrame+time+dt][nrChains]-(rKernel_Y[startAtFrame+dt][nrChains]-rCOM_Y[startAtFrame+dt][nrChains]);
						double diffRKernelRcom_Z = rKernel_Z[startAtFrame+time+dt][nrChains]-rCOM_Z[startAtFrame+time+dt][nrChains]-(rKernel_Z[startAtFrame+dt][nrChains]-rCOM_Z[startAtFrame+dt][nrChains]);
						
						//double diffRN2MRcom_X = rLast_X[startAtFrame+time+dt][nrChains]-rCOM_X[startAtFrame+time+dt][nrChains]-(rLast_X[startAtFrame+dt][nrChains]-rCOM_X[startAtFrame+dt][nrChains]);
						//double diffRN2MRcom_Y = rLast_Y[startAtFrame+time+dt][nrChains]-rCOM_Y[startAtFrame+time+dt][nrChains]-(rLast_Y[startAtFrame+dt][nrChains]-rCOM_Y[startAtFrame+dt][nrChains]);
						//double diffRN2MRcom_Z = rLast_Z[startAtFrame+time+dt][nrChains]-rCOM_Z[startAtFrame+time+dt][nrChains]-(rLast_Z[startAtFrame+dt][nrChains]-rCOM_Z[startAtFrame+dt][nrChains]);
						
						//double diffRN2MRcom_X = rLast_X[startAtFrame+time+dt][nrChains]-rKernel_X[startAtFrame+time+dt][nrChains]-(rLast_X[startAtFrame+dt][nrChains]-rKernel_X[startAtFrame+dt][nrChains]);
						//double diffRN2MRcom_Y = rLast_Y[startAtFrame+time+dt][nrChains]-rKernel_Y[startAtFrame+time+dt][nrChains]-(rLast_Y[startAtFrame+dt][nrChains]-rKernel_Y[startAtFrame+dt][nrChains]);
						//double diffRN2MRcom_Z = rLast_Z[startAtFrame+time+dt][nrChains]-rKernel_Z[startAtFrame+time+dt][nrChains]-(rLast_Z[startAtFrame+dt][nrChains]-rKernel_Z[startAtFrame+dt][nrChains]);
						
						
						g1_Time_Stat[time].AddValue(diffRKernel_X*diffRKernel_X+diffRKernel_Y*diffRKernel_Y+diffRKernel_Z*diffRKernel_Z);
						
						g2kernelCOM_Time_Stat[time].AddValue(diffRKernelRcom_X*diffRKernelRcom_X + diffRKernelRcom_Y*diffRKernelRcom_Y + diffRKernelRcom_Z*diffRKernelRcom_Z);
						
						g3_Time_Stat[time].AddValue(diffRcom_X*diffRcom_X+diffRcom_Y*diffRcom_Y+diffRcom_Z*diffRcom_Z);
					
						//g4_Time_Stat[time].AddValue(diffRFirst_X*diffRFirst_X+diffRFirst_Y*diffRFirst_Y+diffRFirst_Z*diffRFirst_Z);
						//g4_Time_Stat[time].AddValue(diffRN2_X*diffRN2_X + diffRN2_Y*diffRN2_Y + diffRN2_Z*diffRN2_Z);
						//g4_Time_Stat[time].AddValue(diffRLast_X*diffRLast_X+diffRLast_Y*diffRLast_Y+diffRLast_Z*diffRLast_Z);
						
						
					}
				}
			
			}
		
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1() );
		
		BFMFileSaver g1Saver = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		g1Saver.DateiAnlegen(dirDst+"/"+FileName+"_g123_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".dat", false);
		g1Saver.setzeZeile("# NrOfMonomersPerDendrimer="+ NrOfMonoPerDendrimer);
		g1Saver.setzeZeile("# NrOfDendrimer="+ NrOfDendrimer);
		g1Saver.setzeZeile("# NrDensity c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z)));
		g1Saver.setzeZeile("# tBeginn = "+ (startAtFrame*deltaT) + "    startAtFrame(+1) = " + (startAtFrame+1));
		g1Saver.setzeZeile("# dt = "+ (diffFrame*deltaT) + "    dframe = " + diffFrame);
		g1Saver.setzeZeile("# t  g1  (g1)^2 d(g1) g2  (g2)^2 d(g2) g3  (g3)^2 d(g3) g4  (g4)^2 d(g4) g5 (g5)^2 d(g5) SampleSize");
		
		for(int time=1; time < (maxframe-startAtFrame); time++)
		{
			if(g1_Time_Stat[time].ReturnN() > 2)
				g1Saver.setzeZeile((deltaT*time) + " " + g1_Time_Stat[time].ReturnM1()+" "+(g1_Time_Stat[time].ReturnM2())+" "+( 2.0* g1_Time_Stat[time].ReturnSigma()/Math.sqrt(1.0*g1_Time_Stat[time].ReturnN())) + " " + g2_Time_Stat[time].ReturnM1()+" "+(g2_Time_Stat[time].ReturnM2())+" "+( 2.0* g2_Time_Stat[time].ReturnSigma()/Math.sqrt(1.0*g2_Time_Stat[time].ReturnN())) + " " + g3_Time_Stat[time].ReturnM1()+" "+(g3_Time_Stat[time].ReturnM2())+" "+( 2.0* g3_Time_Stat[time].ReturnSigma()/Math.sqrt(1.0*g3_Time_Stat[time].ReturnN())) + " " + g4_Time_Stat[time].ReturnM1()+" "+(g4_Time_Stat[time].ReturnM2())+" "+( 2.0* g4_Time_Stat[time].ReturnSigma()/Math.sqrt(1.0*g4_Time_Stat[time].ReturnN())) + " " + g2kernelCOM_Time_Stat[time].ReturnM1()+" "+(g2kernelCOM_Time_Stat[time].ReturnM2())+" "+( 2.0* g2kernelCOM_Time_Stat[time].ReturnSigma()/Math.sqrt(1.0*g2kernelCOM_Time_Stat[time].ReturnN())) + " "+g4_Time_Stat[time].ReturnN());
		}
		
		g1Saver.DateiSchliessen();
		
		
		
		createXmGraceFile();
	}
	
	protected void createXmGraceFile()
	{
		BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(dstDir+"/"+FileName+"_g123_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".batch", false);
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    //xmgrace.setzeZeile("G0 TYPE XY");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_g123_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".dat\"");
	    xmgrace.setzeZeile("BLOCK xydy \"1:2:4\"");
	    xmgrace.setzeZeile("BLOCK xydy \"1:5:7\"");
	    xmgrace.setzeZeile("BLOCK xydy \"1:8:10\"");
	    xmgrace.setzeZeile("BLOCK xydy \"1:11:13\"");
	    xmgrace.setzeZeile("BLOCK xydy \"1:14:16\"");
	    //xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_PEG_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    //xmgrace.setzeZeile("BLOCK xy \"1:3\"");
	    //xmgrace.setzeZeile("BLOCK xy \"1:5\"");
	    //xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    //xmgrace.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace.setzeZeile(" xaxes scale Logarithmic");
	    xmgrace.setzeZeile(" yaxes scale Logarithmic");
	    xmgrace.setzeZeile(" world xmin 100000");
	    xmgrace.setzeZeile(" world xmax 1000000000.0");
	    xmgrace.setzeZeile(" world ymin 0.5");
	    xmgrace.setzeZeile(" world ymax 500000.0");
	    
	    xmgrace.setzeZeile(" xaxis label \"t [MCS]\"");
	    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace.setzeZeile(" xaxis tick major 10");
	    xmgrace.setzeZeile(" xaxis tick minor 1");
	    xmgrace.setzeZeile(" yaxis label \"g1, g2, g3\"");
	    xmgrace.setzeZeile(" yaxis  label place opposite");
	    xmgrace.setzeZeile(" yaxis tick major 10");
	    xmgrace.setzeZeile(" yaxis tick minor 1");

	    
	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 symbol 0");
	    //xmgrace.setzeZeile(" s0 symbol 1");
	    xmgrace.setzeZeile(" s0 errorbar off");
	    xmgrace.setzeZeile(" s0 SYMBOL Skip 100");
	    xmgrace.setzeZeile(" s0 legend \"g1\"");

	    
	    xmgrace.setzeZeile(" s1 line color 2");
	    xmgrace.setzeZeile(" s1 line linestyle 1");
	    xmgrace.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace.setzeZeile(" s1 symbol 0");
	    //xmgrace.setzeZeile(" s1 symbol 2");
	    xmgrace.setzeZeile(" s1 errorbar off");
	    xmgrace.setzeZeile(" s1 SYMBOL Skip 100");
	    xmgrace.setzeZeile(" s1 legend \"g2\"");
	    
	    
	    xmgrace.setzeZeile(" s2 line color 3");
	    xmgrace.setzeZeile(" s2 line linestyle 1");
	    xmgrace.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace.setzeZeile(" s2 symbol 0");
	    //xmgrace.setzeZeile(" s2 symbol 3");
	    xmgrace.setzeZeile(" s2 errorbar off");
	    xmgrace.setzeZeile(" s2 SYMBOL Skip 100");
	    xmgrace.setzeZeile(" s2 SYMBOL linewidth 1.5");
	    xmgrace.setzeZeile(" s2 legend \"g3\"");

	    xmgrace.setzeZeile(" s3 line color 4");
	    xmgrace.setzeZeile(" s3 line linestyle 1");
	    xmgrace.setzeZeile(" s3 line linewidth 1.5");
	    xmgrace.setzeZeile(" s3 symbol 0");
	    //xmgrace.setzeZeile(" s3 symbol 3");
	    xmgrace.setzeZeile(" s3 errorbar off");
	    xmgrace.setzeZeile(" s3 SYMBOL Skip 100");
	    xmgrace.setzeZeile(" s3 SYMBOL linewidth 1.5");
	    xmgrace.setzeZeile(" s3 legend \"g4\"");

	    xmgrace.setzeZeile(" s4 line color 9");
	    xmgrace.setzeZeile(" s4 line linestyle 1");
	    xmgrace.setzeZeile(" s4 line linewidth 1.5");
	    xmgrace.setzeZeile(" s4 symbol 0");
	    //xmgrace.setzeZeile(" s4 symbol 3");
	    xmgrace.setzeZeile(" s4 errorbar off");
	    xmgrace.setzeZeile(" s4 SYMBOL Skip 100");
	    xmgrace.setzeZeile(" s4 SYMBOL linewidth 1.5");
	    xmgrace.setzeZeile(" s4 legend \"g5\"");

	    
	    xmgrace.setzeZeile(" LEGEND 0.15, 0.6");
	    xmgrace.setzeZeile(" subtitle \"Melt; N="+NrOfMonoPerDendrimer+"; n\\sChain\\N="+NrOfDendrimer+"; c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z))+";dt="+(diffFrame*deltaT)+";t\\sBegin\\N="+(startAtFrame*deltaT)+"\"");

	    xmgrace.setzeZeile(" SAVEALL \""+dstDir+"/"+FileName+"_g123_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+dstDir+"/"+FileName+"_g123_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	   
	    try {
	    	
	    	  System.out.println("xmgrace -batch "+dstDir+"/"+FileName+"_g123_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".batch -nosafe -hardcopy");
		      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/"+FileName+"_g123_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".batch -nosafe -hardcopy");
		    } catch (Exception e) {
		      System.err.println(e.toString());
		    }
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
		  
		importData.GetFrameOfSimulation(currentFrame);
		  
		  
			
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
		if(args.length != 7)
		{
			System.out.println("Berechnung g1, g2, g3");
			System.out.println("Starting at [startAtFrame] with [deltaFrames]");
			System.out.println("[startAtFrame] is element [1:maxframe]");
			System.out.println("USAGE: dirSrc/ FileLinearChains[.xo]  dirDst/ NrOfMonoPerDendrimer NrOfDendrimer startAtFrame DiffFrames");
		}
		else new Auswertung_Dendrimer_g1g2g3(args[0], args[1], args[2] , Integer.parseInt(args[3]) , Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));//,args[1],args[2]);
	
		
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
		
		
		boolean periodRB_x = importData.periodic_x;
		boolean periodRB_y = importData.periodic_y;
		boolean periodRB_z = importData.periodic_z;
		
		boolean periodRB = false;
		
		if ((periodRB_x == true) || (periodRB_y == true) || (periodRB_z == true))
			periodRB = true;
		
		Bindungsnetzwerk = null;
		Bindungsnetzwerk = new Int_IntArrayList_Table(MONOMERZAHL);
		
		
		for(int it = 0; it < importData.bonds.size(); it++)
		{
			long bondobj = importData.bonds.get(it);
			//System.out.println(it + " bond " + bondobj);
			int mono1 = getMono1Nr(bondobj);
			int mono2 = getMono2Nr(bondobj);
			
			boolean foundBond = false;  
			
			for (int j = 0; j < Bindungsnetzwerk.get(mono1).size(); j++)
				if(Bindungsnetzwerk.get(mono1).get(j) == mono2)
					foundBond = true;
			
			for (int j = 0; j < Bindungsnetzwerk.get(mono2).size(); j++)
				if(Bindungsnetzwerk.get(mono2).get(j) == mono1)
					foundBond = true;
			
			if(foundBond == false)
			{
				Bindungsnetzwerk.put(mono1, mono2);
				Bindungsnetzwerk.put(mono2, mono1);
				System.out.println("bonds_sim: "+it+"   a:" + getMono1Nr(bondobj) + " b:" + getMono2Nr(bondobj));
			}
			
		}
		
		for(int it = 0; it < importData.additionalbonds.size(); it++)
		{
			long bondobj = importData.additionalbonds.get(it);
			//System.out.println(it + " bond " + bondobj);
			int mono1 = getMono1Nr(bondobj);
			int mono2 = getMono2Nr(bondobj);
			
			boolean foundBond = false;  
			
			for (int j = 0; j < Bindungsnetzwerk.get(mono1).size(); j++)
				if(Bindungsnetzwerk.get(mono1).get(j) == mono2)
					foundBond = true;
			
			for (int j = 0; j < Bindungsnetzwerk.get(mono2).size(); j++)
				if(Bindungsnetzwerk.get(mono2).get(j) == mono1)
					foundBond = true;
			
			if(foundBond == false)
			{
				Bindungsnetzwerk.put(mono1, mono2);
				Bindungsnetzwerk.put(mono2, mono1);
				System.out.println("additionalbonds_sim: "+it+"   a:" + getMono1Nr(bondobj) + " b:" + getMono2Nr(bondobj));
			}
			
			
		}
		
		EndArray = null;
		EndArray = new IntArrayList();
		
		int counter = 0;
		for (int i= 1; i < MONOMERZAHL; i++)
		{
			
			if(Bindungsnetzwerk.get(i).size() == 1)
			{
				counter ++;
				System.out.println(counter + " Monomer with 1 connection: "+ i);
				EndArray.add(i);
			}
			
			if(Bindungsnetzwerk.get(i).size() > 3)
				System.out.println("Monomers with >3 connections: "+ i);
		}
		
		/*System.out.println("Nr of Monomers per Chains in File: "+ importData.NrOfMonoPerChain);
		if(importData.NrOfMonoPerChain != NrOfMonoPerChain)
		{
			System.out.println("Nr of Monomers per Chains does not met. Exiting...");
			System.exit(1);
		}
		System.out.println("Nr of Chains in File: "+ (importData.NrOfMonomers/importData.NrOfMonoPerChain));
		if( (importData.NrOfMonomers/importData.NrOfMonoPerChain) != NrOfChains)
		{
			System.out.println("Nr of Chains does not met. Exiting...");
			System.exit(1);
		}*/
		
		NrofFrames = importData.GetNrOfFrames();
	
		String zutrt = 	FileName.substring(0, FileName.length()-4);

		System.out.println("String : " + zutrt);
		
		
		
	}
	
	public void playSimulation(int frame)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				importData.GetFrameOfSimulation( frame);
				
				
				
				for (int nrChains= 0; nrChains < NrOfDendrimer; nrChains++)
				{
					
					double Rcom_x = 0.0;
					double Rcom_y = 0.0;
					double Rcom_z = 0.0;
						
					 for (int i= nrChains*NrOfMonoPerDendrimer+1; i <= (nrChains+1)*NrOfMonoPerDendrimer; i++)
					  {
						  Rcom_x += 1.0*(importData.PolymerKoordinaten[i][0]);
						  Rcom_y += 1.0*(importData.PolymerKoordinaten[i][1]);
						  Rcom_z += 1.0*(importData.PolymerKoordinaten[i][2]);
					  }
					 
					 
					Rcom_x /= 1.0*(NrOfMonoPerDendrimer);
					Rcom_y /= 1.0*(NrOfMonoPerDendrimer);
					Rcom_z /= 1.0*(NrOfMonoPerDendrimer);
					
					/*
					rCOM_X[(int) (importData.MCSTime/(deltaT))][nrChains]=Rcom_x; //[time][idx=chainnumber]
					rCOM_Y[(int) (importData.MCSTime/(deltaT))][nrChains]=Rcom_y;//[time][idx=chainnumber]
					rCOM_Z[(int) (importData.MCSTime/(deltaT))][nrChains]=Rcom_z;//[time][idx=chainnumber]
					
					rN2_X[(int) (importData.MCSTime/(deltaT))][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerChain+NrOfMonoPerChain/2][0]; //[time][idx=chainnumber]
					rN2_Y[(int) (importData.MCSTime/(deltaT))][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerChain+NrOfMonoPerChain/2][1]; //[time][idx=chainnumber]
					rN2_Z[(int) (importData.MCSTime/(deltaT))][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerChain+NrOfMonoPerChain/2][2]; //[time][idx=chainnumber]
					*/
					
					rCOM_X[frame-1][nrChains]=Rcom_x; //[time][idx=chainnumber]
					rCOM_Y[frame-1][nrChains]=Rcom_y;//[time][idx=chainnumber]
					rCOM_Z[frame-1][nrChains]=Rcom_z;//[time][idx=chainnumber]
					
					//rN2_X[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+NrOfMonoPerDendrimer/2][0]; //[time][idx=chainnumber]
					//rN2_Y[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+NrOfMonoPerDendrimer/2][1]; //[time][idx=chainnumber]
					//rN2_Z[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+NrOfMonoPerDendrimer/2][2]; //[time][idx=chainnumber]
					
					rN2_X[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+445][0]; //[time][idx=chainnumber]
					rN2_Y[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+445][1]; //[time][idx=chainnumber]
					rN2_Z[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+445][2]; //[time][idx=chainnumber]
					
					for(int i = 0; i < EndArray.size(); i++)
					{
						rAllEnds_X[frame-1][i]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+ EndArray.get(i)][0];
						rAllEnds_Y[frame-1][i]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+ EndArray.get(i)][1];
						rAllEnds_Z[frame-1][i]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+ EndArray.get(i)][2];
					}
					
					rKernel_X[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+1][0]; //[time][idx=chainnumber]
					rKernel_Y[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+1][1]; //[time][idx=chainnumber]
					rKernel_Z[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+1][2]; //[time][idx=chainnumber]
					
					rLast_X[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+NrOfMonoPerDendrimer][0]; //[time][idx=chainnumber]
					rLast_Y[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+NrOfMonoPerDendrimer][1]; //[time][idx=chainnumber]
					rLast_Z[frame-1][nrChains]=importData.PolymerKoordinaten[nrChains*NrOfMonoPerDendrimer+NrOfMonoPerDendrimer][2]; //[time][idx=chainnumber]
					
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
