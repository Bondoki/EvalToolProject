package EvalToolProject_ice.CrossStars;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;
import EvalToolProject_ice.tools.Histogramm;
import EvalToolProject_ice.tools.HistogrammStatistik;

public class Auswertung_Stars_CrossStars_Rg2All_WHAM {


	
	
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
	
	//Statistik MSD_stat;
	//Statistik RootMSD_stat;
	Statistik rg_xyz_stat;
	Statistik rg_x_stat;
	Statistik rg_y_stat;
	Statistik rg_z_stat;
	Statistik Rg2all_stat;
	
	Statistik Ree2all_stat;
	
	BFMFileSaver rg;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	//Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	long deltaT;
	
	
	Histogramm[] HG_Rg_Plain;
	HistogrammStatistik[] HG_Rg_Statistik;
	
	HistogrammStatistik HG_Rg_StatistikTotal;
	
	double[] EquLength;
	double[] OffSetF;
	
	
	public Auswertung_Stars_CrossStars_Rg2All_WHAM(String SrcDir, String SrcName, String DstDir, double springconstant)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		//FileName = SrcName;
		FileDirectory = SrcDir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		rg = new BFMFileSaver();
		rg.DateiAnlegen(DstDir+ SrcName+"_Rg2_Stars.dat", false);
		rg.setzeZeile("# MSD for Stars");
		rg.setzeZeile("# t[MCS] <Rg^2_x> <Rg^2_y> <Rg^2_z> <Rg^2> <(Rg^2)^2> dRg2 N");
		//rg.setzeZeile("0 0 0 0 0 0");
		
		rg_xyz_stat = new Statistik();
		rg_x_stat = new Statistik();
		rg_y_stat = new Statistik();
		rg_z_stat = new Statistik();
		Rg2all_stat = new Statistik();
		
		Ree2all_stat = new Statistik();
		
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ SrcName+"40.0.bfm");
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		
		
		System.out.println("file : " +SrcName );
		System.out.println("dir : " + FileDirectory);
		
		int NrOfAllHistogramms = 49;
		
		HG_Rg_Plain = new Histogramm[NrOfAllHistogramms];
		HG_Rg_Statistik = new HistogrammStatistik[NrOfAllHistogramms];
		
		EquLength= new double[NrOfAllHistogramms];
		OffSetF= new double[NrOfAllHistogramms];
		
		for(int i = 0; i < NrOfAllHistogramms; i++)
		{
			
			HG_Rg_Plain[i] = new Histogramm(40.0,115.0,200);
			HG_Rg_Statistik[i] = new HistogrammStatistik(40.0,115.0,200);
			OffSetF[i]=0.0;
			EquLength[i]=40.0+1.25*i;
			System.out.println("EquLength["+i+"]: " + EquLength[i]);
		}
		
		HG_Rg_StatistikTotal = new HistogrammStatistik(40.0,115.0,200);
		
		
		
		DecimalFormat dh = new DecimalFormat("0.0000");
		
		//for(int i = 0; i <= 9; i++)
		//	LoadFile("buerste_straight_25_0"+i+".bfm", 1);
		for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
		{
			System.out.println("Loading" + SrcName+""+( (EquLength[nrHisto]))+".bfm");
			
			LoadFile(SrcName+""+( (EquLength[nrHisto]))+".bfm",1,nrHisto);
			//LoadFile(fname+"R_"+EquLength[nrHisto]+"_C_0.2_HardColloids.bfm",1,nrHisto);
			
		}
		
		//LoadFile(SrcName+".bfm", currentFrame);
		
		//perform the normalization of histograms to spherical coordinates
				for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
				for(int i = 0; i < HG_Rg_Plain[nrHisto].GetNrBins(); i++)
				{
					//double testnorm = (HG_Rg_Plain[nrHisto].GetNrInBinNormiert(i)/(4*Math.PI*HG_Rg_Plain[nrHisto].GetRangeInBin(i)*HG_Rg_Plain[nrHisto].GetRangeInBin(i)*HG_Rg_Plain[nrHisto].GetIntervallThickness()));
					double testnorm = (HG_Rg_Plain[nrHisto].GetNrInBinNormiert(i)/(HG_Rg_Plain[nrHisto].GetIntervallThickness()));
					
					HG_Rg_Statistik[nrHisto].AddValue(HG_Rg_Plain[nrHisto].GetRangeInBin(i), testnorm);
				}
				
				for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
				{
				BFMFileSaver histo= new BFMFileSaver();
				histo.DateiAnlegen(DstDir+"/"+FileName+"Histo_"+EquLength[nrHisto]+".dat", false);
				histo.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
				
				histo.setzeZeile("# intervall from ["+HG_Rg_Statistik[nrHisto].GetRangeInBinLowerLimit(0)+";"+HG_Rg_Statistik[nrHisto].GetRangeInBinLowerLimit(HG_Rg_Statistik[nrHisto].GetNrBins())+"]");
				histo.setzeZeile("# intervall thickness: dI="+HG_Rg_Statistik[nrHisto].GetIntervallThickness());
				histo.setzeZeile("# nr of counts: " + HG_Rg_Statistik[nrHisto].GetNrOfCounts());
				histo.setzeZeile("# r  p(r) F=-ln(p)");
				
				for(int i = 0; i < HG_Rg_Statistik[nrHisto].GetNrBins(); i++)
				{
					if(HG_Rg_Statistik[nrHisto].GetAverageInBin(i) != 0)
						histo.setzeZeile(HG_Rg_Statistik[nrHisto].GetRangeInBin(i)+" "+HG_Rg_Statistik[nrHisto].GetAverageInBin(i) + " "+(-Math.log(HG_Rg_Statistik[nrHisto].GetAverageInBin(i))));
				}
				
				histo.DateiSchliessen();
				}
			//perform all calculation to achieve selfconsitency
			for(int selfconsitency = 0; selfconsitency < 1000; selfconsitency++)
			{
				//calculate the normalization of total histograms
				double normTotal = 0.0;
				double weightTotal = 0.0;
				
				for(int i = 0; i < HG_Rg_StatistikTotal.GetNrBins(); i++)
				{
					normTotal = 0.0;
					weightTotal = 0.0;
					
					for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
					{
						//double normalisation=4*Math.PI*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)*HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i));
					
						normTotal += HG_Rg_Plain[nrHisto].GetNrOfCounts()*Math.exp(- (0.5*springconstant*(HG_Rg_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_Rg_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto]) - OffSetF[nrHisto]) );
						weightTotal += HG_Rg_Plain[nrHisto].GetNrOfCounts()*HG_Rg_Statistik[nrHisto].GetAverageInBin(i);
					}
					
					// there are entries for the PMF
					if(normTotal != 0.0)
					{
						//double normalisation=4*Math.PI*(HG_RCom2Com_StatistikTotal.GetRangeInBin(i)*HG_RCom2Com_StatistikTotal.GetRangeInBin(i)*HG_RCom2Com_StatistikTotal.GetIntervallThickness());
						
						HG_Rg_StatistikTotal.CleanBin(i);
						HG_Rg_StatistikTotal.AddValue(HG_Rg_StatistikTotal.GetRangeInBin(i), weightTotal/(normTotal));
					}
					else HG_Rg_StatistikTotal.CleanBin(i);
					
				}
				//dump the mean probability
				/*System.out.println("PMF-prob");
				for(int i = 0; i < HG_RCom2Com_StatistikTotal.GetNrBins(); i++)
				{
					System.out.println(HG_RCom2Com_StatistikTotal.GetRangeInBin(i)+" "+HG_RCom2Com_StatistikTotal.GetAverageInBin(i));
				}
				*/
				
				System.out.println("Iteration: " + selfconsitency);
				//perform the integration to get the OffsetF
				for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
				{
					double integral = 0.0;
					
					for(int i = 0; i < HG_Rg_Plain[nrHisto].GetNrBins(); i++)
					{
						//double normalisation=4*Math.PI*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)*HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i));
						//integral += normalisation*HG_RCom2Com_Plain[nrHisto].GetIntervallThickness()*HG_RCom2Com_StatistikTotal.GetAverageInBin(i)*Math.exp(- (0.5*0.6*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])));
						
						//integral += 4*Math.PI*(HG_Rg_Plain[nrHisto].GetRangeInBin(i)*HG_Rg_Plain[nrHisto].GetRangeInBin(i))*HG_Rg_Plain[nrHisto].GetIntervallThickness()*HG_Rg_StatistikTotal.GetAverageInBin(i)*Math.exp(- (0.5*springconstant*(HG_Rg_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_Rg_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])));
						integral += HG_Rg_Plain[nrHisto].GetIntervallThickness()*HG_Rg_StatistikTotal.GetAverageInBin(i)*Math.exp(- (0.5*springconstant*(HG_Rg_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_Rg_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])));
						
					}
					
					OffSetF[nrHisto] = -Math.log(integral);
					System.out.println("OffSetF["+nrHisto+"]: " + OffSetF[nrHisto]);
				}
			}
		
			//file dump
			
			BFMFileSaver histo_RPMF= new BFMFileSaver();
			histo_RPMF.DateiAnlegen(DstDir+"/"+FileName+"Histo_PMF_WHAM.dat", false);
			histo_RPMF.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
			histo_RPMF.setzeZeile("# calculation of the mean free energy F using WHAM");
			histo_RPMF.setzeZeile("# intervall from ["+HG_Rg_StatistikTotal.GetRangeInBinLowerLimit(0)+";"+HG_Rg_StatistikTotal.GetRangeInBinLowerLimit(HG_Rg_StatistikTotal.GetNrBins())+"]");
			histo_RPMF.setzeZeile("# intervall thickness: dI="+HG_Rg_StatistikTotal.GetIntervallThickness());
			histo_RPMF.setzeZeile("# r  p(r) F=-ln(p)");
			
			for(int i = 0; i < HG_Rg_StatistikTotal.GetNrBins(); i++)
			{
				if(HG_Rg_StatistikTotal.GetAverageInBin(i) != 0)
					histo_RPMF.setzeZeile(HG_Rg_StatistikTotal.GetRangeInBin(i)+" "+HG_Rg_StatistikTotal.GetAverageInBin(i) + " "+(-Math.log(HG_Rg_StatistikTotal.GetAverageInBin(i))));
			}
			
			histo_RPMF.DateiSchliessen();
			
		//rg.setzeKommentar("c <Rg2> <(Rg2)2> dF N");
		//rg.setzeZeile((importData.NrOfMonomers*8.0/(Gitter_x*Gitter_y*Gitter_z))+" "+MSD_stat.ReturnM1()+" "+MSD_stat.ReturnM2()+" "+( 2.0* MSD_stat.ReturnSigma()/ Math.sqrt(1.0*MSD_stat.ReturnN() ))+ " " + MSD_stat.ReturnN());
		
		rg.DateiSchliessen();
		
		BFMFileSaver RGSaver = new BFMFileSaver();
		RGSaver.DateiAnlegen(DstDir+ SrcName+"_Rg2_All_N.dat", true);
		//RGSaver.setzeZeile("# MSD for Stars");
		RGSaver.setzeZeile( (NrOfMonomersPerStarArm*4+1) + " " + Math.sqrt(Rg2all_stat.ReturnM1())+ " " + Rg2all_stat.ReturnM1()+" "+Rg2all_stat.ReturnM2()+" "+( 2.0* Rg2all_stat.ReturnSigma()/ Math.sqrt(1.0*Rg2all_stat.ReturnN() ))+ " " + Rg2all_stat.ReturnN());
	RGSaver.DateiSchliessen();
	
		BFMFileSaver Ree2Saver = new BFMFileSaver();
		Ree2Saver.DateiAnlegen(DstDir+ SrcName+"_Ree2_Stars_N.dat", true);
		Ree2Saver.setzeZeile( (NrOfMonomersPerStarArm*4+1) + " " + Math.sqrt(Ree2all_stat.ReturnM1())+ " " + Ree2all_stat.ReturnM1()+" "+Ree2all_stat.ReturnM2()+" "+( 2.0* Ree2all_stat.ReturnSigma()/ Math.sqrt(1.0*Ree2all_stat.ReturnN() ))+ " " + Ree2all_stat.ReturnN());
		Ree2Saver.DateiSchliessen();
		
	    NumberFormat nf = NumberFormat.getInstance(Locale.US);

	    nf.setMaximumFractionDigits(2);
	    
	    BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(DstDir+"/"+SrcName+"_Rg2_Stars.batch", false);
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ NXY \""+DstDir+SrcName+"_Rg2_Stars.dat\"");
	    xmgrace.setzeZeile(" world xmin 0");
	    xmgrace.setzeZeile(" world xmax "+importData.MCSTime);//225000");
	    xmgrace.setzeZeile(" world ymin 0");
	    xmgrace.setzeZeile(" world ymax "+rg_xyz_stat.ReturnM1()*1.2);
	    xmgrace.setzeZeile(" xaxis label \"time [MCS]\"");
	    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace.setzeZeile(" xaxis tick major "+(maxframe-1)*deltaT/4);
	    //xmgrace2.setzeZeile(" xaxis tick minor 1");
	    xmgrace.setzeZeile(" yaxis label \"Rg\\S2\"");
	    //xmgrace2.setzeZeile(" yaxis tick major 0.1");
	    //xmgrace2.setzeZeile(" yaxis tick minor 0.02");

	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 legend \"Rg\\S2\\N\\sx\"");

	   // xmgrace2.setzeZeile(" s0 symbol 1");
	  //  xmgrace2.setzeZeile(" s0 legend \"<f>="+nf.format(DurchschnittsfunktionalitaetHEP)+"\"");

	    xmgrace.setzeZeile(" s1 line color 2");
	    xmgrace.setzeZeile(" s1 line linestyle 1");
	    xmgrace.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace.setzeZeile(" s1 legend \"Rg\\S2\\N\\sy\"");
	    
	    xmgrace.setzeZeile(" s2 line color 3");
	    xmgrace.setzeZeile(" s2 line linestyle 1");
	    xmgrace.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace.setzeZeile(" s2 legend \"Rg\\S2\\N\\sz\"");
	    
	    xmgrace.setzeZeile(" s3 line color 4");
	    xmgrace.setzeZeile(" s3 line linestyle 1");
	    xmgrace.setzeZeile(" s3 line linewidth 1.5");
	    xmgrace.setzeZeile(" s3 legend \"Rg\\S2\\N\"");
	    
	   // xmgrace.setzeZeile(" s1 off");
	    //xmgrace.setzeZeile(" s2 off");
	    //xmgrace.setzeZeile(" s3 off");
	    xmgrace.setzeZeile(" s4 off");
	    xmgrace.setzeZeile(" s5 off");
	    xmgrace.setzeZeile(" s6 off");
	    
	   
	    
	    
	    xmgrace.setzeZeile(" subtitle \"N\\sCrosslinker\\N="+NrOfCrosslinker+"; f\\sCross\\N="+CrosslinkerFunctionality+"; N\\sStars\\N="+NrOfStars+"; N\\sStars\\N\\SArm\\N="+NrOfMonomersPerStarArm+"; \\f{Symbol}a\\f{}="+nf.format((CrosslinkerFunctionality*NrOfCrosslinker/(4.0*NrOfStars)))+"; \"");

	    xmgrace.setzeZeile(" SAVEALL \""+DstDir+SrcName+"_Rg2_Stars.agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+DstDir+SrcName+"_Rg2_Stars.ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	    
	    try {
		      Runtime.getRuntime().exec("xmgrace -batch "+DstDir+"/"+SrcName+"_Rg2_Stars.batch -nosafe -hardcopy");
		    } catch (Exception e) {
		      System.err.println(e.toString());
		    }
	    
	}
	
	protected void LoadFile(String file, int startframe, int histonr)
	{
		FileName = file;
		
		System.out.println("lade System");
		LadeSystem(FileDirectory, FileName);	
		
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(FileDirectory+FileName);
		  
		importData.GetFrameOfSimulation(currentFrame);

			
		
		  importData.CloseSimulationFile();
		  
		  importData.OpenSimulationFile(FileDirectory+FileName);
			
		  
			System.out.println("file : " +FileName );
			System.out.println("dir : " + FileDirectory);
			
			int z = currentFrame;//1;
				
		    while ( (z <= NrofFrames))
		      {

		    		playSimulation(z,histonr);
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
		if(args.length != 4)
		{
			System.out.println("Berechnung Rg2(t) von Sternen im StarsCross-Netzwerk");
			System.out.println("USAGE: dirSrc/ HydrogelStar_Star_aaa_NStar_bbb__Cross_ccc_F_f__NoPerXYZ128[__xxx.bfm] dirDst/ SpringConstant");
		}
		else new Auswertung_Stars_CrossStars_Rg2All_WHAM(args[0], args[1], args[2], Double.parseDouble(args[3]));
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
		
		
		
		
		
		
		NrofFrames = importData.GetNrOfFrames();
	
		String zutrt = 	FileName.substring(0, FileName.length()-4);

		System.out.println("String : " + zutrt);
		
		
		
	}
	
	public void playSimulation(int frame, int histonr)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				importData.GetFrameOfSimulation( frame);;
				
				if(importData.MCSTime > 500000)
				{
				
				rg_xyz_stat.clear();
				rg_x_stat.clear();
				rg_y_stat.clear();
				rg_z_stat.clear();
				
				 int offset = 0;// 90*NrOfHeparin;
				 
				 
					double Rg2 = 0.0;
					double Rg2_x = 0.0;
					double Rg2_y = 0.0;
					double Rg2_z = 0.0;
						
					 for (int i= 1; i <= ( 4*NrOfMonomersPerStarArm + 1)*NrOfStars+NrOfCrosslinker; i++)
					  for (int j = i; j <= ( 4*NrOfMonomersPerStarArm + 1)*NrOfStars+NrOfCrosslinker; j++)
					  {
						  Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
						  Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
						  Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);

						  
					  }
					 
					Rg2 = Rg2_x + Rg2_y + Rg2_z;
					 
					  Rg2_x /= 1.0*((( 4*NrOfMonomersPerStarArm + 1)*NrOfStars+NrOfCrosslinker)*(( 4*NrOfMonomersPerStarArm + 1)*NrOfStars+NrOfCrosslinker));
					  Rg2_y /= 1.0*((( 4*NrOfMonomersPerStarArm + 1)*NrOfStars+NrOfCrosslinker)*(( 4*NrOfMonomersPerStarArm + 1)*NrOfStars+NrOfCrosslinker));
					  Rg2_z /= 1.0*((( 4*NrOfMonomersPerStarArm + 1)*NrOfStars+NrOfCrosslinker)*(( 4*NrOfMonomersPerStarArm + 1)*NrOfStars+NrOfCrosslinker));
					  Rg2 /= 1.0*((( 4*NrOfMonomersPerStarArm + 1)*NrOfStars+NrOfCrosslinker)*(( 4*NrOfMonomersPerStarArm + 1)*NrOfStars+NrOfCrosslinker));
					  
					rg_x_stat.AddValue(Rg2_x);
					rg_y_stat.AddValue(Rg2_y);
					rg_z_stat.AddValue(Rg2_z);
					rg_xyz_stat.AddValue(Rg2);
					
					// if(frame > 250)
						 Rg2all_stat.AddValue(Rg2);
						 
						 
						
							 
							 
				 
				 
				 rg.setzeZeile(importData.MCSTime + " " +rg_x_stat.ReturnM1()+ " "+rg_y_stat.ReturnM1()+ " "+rg_z_stat.ReturnM1()+ " "+ rg_xyz_stat.ReturnM1()+" "+  (rg_xyz_stat.ReturnM2()+" "+(Double.isNaN(rg_xyz_stat.ReturnSigma()) ? 0: ( 2.0* rg_xyz_stat.ReturnSigma()/ Math.sqrt(1.0*rg_xyz_stat.ReturnN() ))))+ " " + rg_xyz_stat.ReturnN());
				 
				 HG_Rg_Plain[histonr].AddValue(Math.sqrt(Rg2));
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
