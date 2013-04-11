package EvalToolProject_bcc.HEP_PEG;

import java.text.DecimalFormat;

import EvalToolProject_bcc.tools.*;

public class Auswertung_Perkolation_HepPEG {


	
	
	String FileName;
	String FileDirectory;
	
	int[] Polymersystem;
    int[] Attributesystem;
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
	
	
	
	BFMImportData importData;
	
	int NrOfHeparin = 0;
	int NrOfStars = 0;
	int NrOfMonomersPerStarArm = 0;
	
	int[] dumpsystem;

	int ColorHeparin[];
	int ColorStars[];
	int StarClusterSize[];
	int HeparinClusterSize[];

	
	
	Statistik durchschnittbond;
	Statistik InitialFunctionalGroupHeparin;
	Statistik InitialFunctionalGroupStars;
	
	int[] PEGBonds;
	
	
	BFMFileSaver ClusterSaver;
	BFMFileSaver ClusterSaver_LinearBonds;
	BFMFileSaver Rg2BiggestClusterSaver;
	BFMFileSaver Rg2BiggestClusterTimeSaver;
	BFMFileSaver FuncHEPBiggestClusterSaver;
	BFMFileSaver FuncStarsBiggestClusterSaver;
	//BFMFileSaver FuncHEPSolSaver;
	//BFMFileSaver FuncStarsSolSaver;
	BFMFileSaver GammaClusterSaver;
	
	BFMFileSaver NrOfHEPSaver;
	BFMFileSaver NrOfStarsSaver;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	HistogrammStatistik CrosslinksVsCluster;
	HistogrammStatistik CrosslinksVsCluster_LinearBonds; //Clusterverteilung aus singelBonds
	HistogrammStatistik CrosslinksVsRGBiggestCluster;
	HistogrammStatistik CrosslinksVsFuncHEPBiggestCluster;
	HistogrammStatistik CrosslinksVsFuncStarsBiggestCluster;
	HistogrammStatistik CrosslinksVsFuncHEPSol;
	HistogrammStatistik CrosslinksVsFuncStarsSol;
	HistogrammStatistik CrosslinksVsNrStarsBiggestCluster;
	HistogrammStatistik CrosslinksVsNrHEPBiggestCluster;
	HistogrammStatistik CrosslinksVsGammaCluster;
	
	int[][] HeparinSterneVerknuepfung;
	int counterSinglebonds;
	
	int counterAdditionalBonds;
	
	Statistik[] Rg2VsTime;
	
	long deltaT;
	double RadiusOfGyration2;
	
	public Auswertung_Perkolation_HepPEG(String fdir, String fname, String gamma, String dstDir, String Experiment)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ fname+"__001.bfm");
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		//End - Determine MaxFrame out of the first file
		
		
		
		Polymersystem = new int[1];
		Attributesystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		CrosslinksVsCluster = new HistogrammStatistik(-0.01,1.01,102);//(-0.05,1.05,44);
		CrosslinksVsCluster_LinearBonds = new HistogrammStatistik(-0.01,1.01,51);
		CrosslinksVsRGBiggestCluster = new HistogrammStatistik(-0.01,1.01,51);
		CrosslinksVsFuncHEPBiggestCluster = new HistogrammStatistik(-0.01,1.01,102);
		CrosslinksVsFuncStarsBiggestCluster = new HistogrammStatistik(-0.01,1.01,102);
		CrosslinksVsFuncHEPSol = new HistogrammStatistik(-0.01,1.01,102);
		CrosslinksVsFuncStarsSol = new HistogrammStatistik(-0.01,1.01,102);
		CrosslinksVsNrStarsBiggestCluster = new HistogrammStatistik(-0.01,1.01,102);
		CrosslinksVsNrHEPBiggestCluster = new HistogrammStatistik(-0.01,1.01,102);
		CrosslinksVsGammaCluster = new HistogrammStatistik(-0.01,1.01,102);
		
		
		
		durchschnittbond = new Statistik();
		
		InitialFunctionalGroupHeparin = new Statistik();
		InitialFunctionalGroupStars = new Statistik() ;
		
		Rg2VsTime = new Statistik[maxframe+1];
		for(int i = 0; i <= maxframe; i++)
		{
			Rg2VsTime[i] = new Statistik();
		}
		
		DecimalFormat dh = new DecimalFormat("000");
		
		for(int i = 1; i <= 2; i+=1)
		{
			RadiusOfGyration2=0.0;
			counterAdditionalBonds = 0;
			counterSinglebonds =0;
			LoadFile(FileName+"__"+dh.format(i)+".bfm", 1);
			
		}
		
		
		
		ClusterSaver = new BFMFileSaver();
		ClusterSaver.DateiAnlegen(dstDir+"/Percolation_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat", false);
		ClusterSaver.setzeZeile("# crosslinks(all) <BMC Monomers/AllMonomers>");
		
		
		for(int i = 0; i < CrosslinksVsCluster.GetNrBins(); i++)
		{
			ClusterSaver.setzeZeile(CrosslinksVsCluster.GetRangeInBin(i)+" "+CrosslinksVsCluster.GetAverageInBin(i));
		}
		ClusterSaver.DateiSchliessen();
		
		ClusterSaver_LinearBonds = new BFMFileSaver();
		ClusterSaver_LinearBonds.DateiAnlegen(dstDir+"Percolation_BMC_Distribution_SingleBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		ClusterSaver_LinearBonds.setzeZeile("# crosslinks(single) <NrOfClusterMonomers/AllMonomers>");
		
		for(int i = 0; i < CrosslinksVsCluster_LinearBonds.GetNrBins(); i++)
		{
			ClusterSaver_LinearBonds.setzeZeile(CrosslinksVsCluster_LinearBonds.GetRangeInBin(i)+" "+CrosslinksVsCluster_LinearBonds.GetAverageInBin(i));
		}
		ClusterSaver_LinearBonds.DateiSchliessen();
		
		Rg2BiggestClusterSaver = new BFMFileSaver();
		Rg2BiggestClusterSaver.DateiAnlegen(dstDir+"/Percolation_Rg2_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat", false);
		Rg2BiggestClusterSaver.setzeZeile("# crosslinks(all) <Rg^2 of BMC>");
		
		for(int i = 0; i < CrosslinksVsRGBiggestCluster.GetNrBins(); i++)
		{
			Rg2BiggestClusterSaver.setzeZeile(CrosslinksVsRGBiggestCluster.GetRangeInBin(i)+" "+CrosslinksVsRGBiggestCluster.GetAverageInBin(i));
		}
		Rg2BiggestClusterSaver.DateiSchliessen();
		
		Rg2BiggestClusterTimeSaver = new BFMFileSaver();
		Rg2BiggestClusterTimeSaver.DateiAnlegen(dstDir+"/Percolation_Rg2_BMC_Distribution_Time_HepPEGConnectedGel_"+FileName+".dat", false);
		Rg2BiggestClusterTimeSaver.setzeZeile("# time[MCS] <Rg^2 of BMC>");
		
		for(int i=0; i <=maxframe; i++)
			Rg2BiggestClusterTimeSaver.setzeZeile((deltaT*i) + " " + Rg2VsTime[i].ReturnM1());
		
		Rg2BiggestClusterTimeSaver.DateiSchliessen();
		
		
		//output functionality biggest Cluster
		FuncHEPBiggestClusterSaver = new BFMFileSaver();
		FuncHEPBiggestClusterSaver.DateiAnlegen(dstDir+"/Percolation_HEP_BMC_Functionality_HepPEGConnectedGel_"+FileName+".dat", false);
		FuncHEPBiggestClusterSaver.setzeZeile("# Init Fct Group HEP: " + InitialFunctionalGroupHeparin.ReturnM1());
		FuncHEPBiggestClusterSaver.setzeZeile("# crosslinks(all) <FunctionalGroup HEP BMC> <Functional Group HEP BMC/InitFctGroup> <FunctionalGroup HEP Sol> <Functional Group HEP Sol/InitFctGroup>");;
		
		for(int i = 0; i < CrosslinksVsFuncHEPBiggestCluster.GetNrBins(); i++)
		{
			FuncHEPBiggestClusterSaver.setzeZeile(CrosslinksVsFuncHEPBiggestCluster.GetRangeInBin(i)+" "+CrosslinksVsFuncHEPBiggestCluster.GetAverageInBin(i)+" "+(CrosslinksVsFuncHEPBiggestCluster.GetAverageInBin(i)/InitialFunctionalGroupHeparin.ReturnM1())+" "+CrosslinksVsFuncHEPSol.GetAverageInBin(i)+" "+(CrosslinksVsFuncHEPSol.GetAverageInBin(i)/InitialFunctionalGroupHeparin.ReturnM1()));
		}
		FuncHEPBiggestClusterSaver.DateiSchliessen();
		
		
		
		FuncStarsBiggestClusterSaver = new BFMFileSaver();
		FuncStarsBiggestClusterSaver.DateiAnlegen(dstDir+"/Percolation_PEG_BMC_Functionality_HepPEGConnectedGel_"+FileName+".dat", false);
		FuncStarsBiggestClusterSaver.setzeZeile("# Init Fct Group Stars: " + InitialFunctionalGroupStars.ReturnM1());
		FuncStarsBiggestClusterSaver.setzeZeile("# crosslinks(all) <FunctionalGroup Stars BMC> <Functional Group Stars BMC/InitFctGroup>  <FunctionalGroup Stars Sol> <FunctionalGroup Stars Sol/InitFctGroup>");;
		
		for(int i = 0; i < CrosslinksVsFuncStarsBiggestCluster.GetNrBins(); i++)
		{
			FuncStarsBiggestClusterSaver.setzeZeile(CrosslinksVsFuncStarsBiggestCluster.GetRangeInBin(i)+" "+CrosslinksVsFuncStarsBiggestCluster.GetAverageInBin(i)+" "+(CrosslinksVsFuncStarsBiggestCluster.GetAverageInBin(i)/InitialFunctionalGroupStars.ReturnM1())+" "+CrosslinksVsFuncStarsSol.GetAverageInBin(i)+" "+(CrosslinksVsFuncStarsSol.GetAverageInBin(i)/InitialFunctionalGroupStars.ReturnM1()));
		}
		FuncStarsBiggestClusterSaver.DateiSchliessen();
		
		//output functionality of sol
		/*FuncHEPSolSaver = new BFMFileSaver();
		FuncHEPSolSaver.DateiAnlegen(dstDir+"/Percolation_FuncHEP_Sol_AllMonomers_HepPEGConnectedGel_"+FileName+".dat", false);
		FuncHEPSolSaver.setzeZeile("# Init Fct Group HEP: " + InitialFunctionalGroupHeparin.ReturnM1());
		FuncHEPSolSaver.setzeZeile("# crosslinks(all) <FunctionalGroup HEP Sol> <Functional Group HEP Sol/InitFctGroup>");;
		
		for(int i = 0; i < CrosslinksVsFuncHEPSol.GetNrBins(); i++)
		{
			FuncHEPSolSaver.setzeZeile(CrosslinksVsFuncHEPSol.GetRangeInBin(i)+" "+CrosslinksVsFuncHEPSol.GetAverageInBin(i)+" "+(CrosslinksVsFuncHEPSol.GetAverageInBin(i)/InitialFunctionalGroupHeparin.ReturnM1()));
		}
		FuncHEPSolSaver.DateiSchliessen();
		*/
		
		
		/*FuncStarsSolSaver = new BFMFileSaver();
		FuncStarsSolSaver.DateiAnlegen(dstDir+"/Percolation_FuncStars_Sol_AllMonomers_HepPEGConnectedGel_"+FileName+".dat", false);
		FuncStarsSolSaver.setzeZeile("# Init Fct Group Stars: " + InitialFunctionalGroupStars.ReturnM1());
		FuncStarsSolSaver.setzeZeile("# crosslinks(all) <Functional Group Stars> <Functional Group Stars/InitFctGroup>");;
		
		for(int i = 0; i < CrosslinksVsFuncStarsSol.GetNrBins(); i++)
		{
			FuncStarsSolSaver.setzeZeile(CrosslinksVsFuncStarsSol.GetRangeInBin(i)+" "+CrosslinksVsFuncStarsSol.GetAverageInBin(i)+" "+(CrosslinksVsFuncStarsSol.GetAverageInBin(i)/InitialFunctionalGroupStars.ReturnM1()));
		}
		FuncStarsSolSaver.DateiSchliessen();
		*/
		
		//export the archieved Gamma in the Biggest Cluster
		GammaClusterSaver = new BFMFileSaver();
		GammaClusterSaver.DateiAnlegen(dstDir+"/Percolation_Gamma_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat", false);
		GammaClusterSaver.setzeZeile("# crosslinks(all) <Gamma in BMC> <Gamma in BMC/InitGamma>");;
		
		for(int i = 0; i < CrosslinksVsGammaCluster.GetNrBins(); i++)
		{
			GammaClusterSaver.setzeZeile(CrosslinksVsGammaCluster.GetRangeInBin(i)+" "+CrosslinksVsGammaCluster.GetAverageInBin(i)+" "+(CrosslinksVsGammaCluster.GetAverageInBin(i)/(NrOfStars/(1.0*NrOfHeparin))));
		}
		GammaClusterSaver.DateiSchliessen();
		
		//output of nr Heparin & PEG in- and outside of the cluster
		
		NrOfHEPSaver = new BFMFileSaver();
		NrOfHEPSaver.DateiAnlegen(dstDir+"/Percolation_HEP_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat", false);
		NrOfHEPSaver.setzeZeile("# Init Nr HEP: " + NrOfHeparin);
		NrOfHEPSaver.setzeZeile("# Init Nr PEG: " + NrOfStars);
		NrOfHEPSaver.setzeZeile("# Init Fct Group HEP: " + InitialFunctionalGroupHeparin.ReturnM1());
		NrOfHEPSaver.setzeZeile("# Init Fct Group Stars: " + InitialFunctionalGroupStars.ReturnM1());
		NrOfHEPSaver.setzeZeile("# crosslinks(all) <NrHEP BMC> <NrHEP BMC/NrHeparin> <NrHEP Sol> <NrHEP Sol/NrHeparin>");;
		
		for(int i = 0; i < CrosslinksVsNrHEPBiggestCluster.GetNrBins(); i++)
		{
			NrOfHEPSaver.setzeZeile(CrosslinksVsNrHEPBiggestCluster.GetRangeInBin(i)+" "+CrosslinksVsNrHEPBiggestCluster.GetAverageInBin(i)+" "+(CrosslinksVsNrHEPBiggestCluster.GetAverageInBin(i)/NrOfHeparin)+" "+(NrOfHeparin-CrosslinksVsNrHEPBiggestCluster.GetAverageInBin(i))+" "+(1.0-(CrosslinksVsNrHEPBiggestCluster.GetAverageInBin(i)/NrOfHeparin)));
		}
		NrOfHEPSaver.DateiSchliessen();
		
		
		
		NrOfStarsSaver = new BFMFileSaver();
		NrOfStarsSaver.DateiAnlegen(dstDir+"/Percolation_PEG_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat", false);
		NrOfStarsSaver.setzeZeile("# Init Nr HEP: " + NrOfHeparin);
		NrOfStarsSaver.setzeZeile("# Init Nr PEG: " + NrOfStars);
		NrOfStarsSaver.setzeZeile("# Init Fct Group HEP: " + InitialFunctionalGroupHeparin.ReturnM1());
		NrOfStarsSaver.setzeZeile("# Init Fct Group Stars: " + InitialFunctionalGroupStars.ReturnM1());
		NrOfStarsSaver.setzeZeile("# crosslinks(all) <NrStars BMC> <NrStars BMC/NrStars> <NrStars Sol> <NrStars Sol/NrStars>");
		
		for(int i = 0; i < CrosslinksVsNrStarsBiggestCluster.GetNrBins(); i++)
		{
			NrOfStarsSaver.setzeZeile(CrosslinksVsNrStarsBiggestCluster.GetRangeInBin(i)+" "+CrosslinksVsNrStarsBiggestCluster.GetAverageInBin(i)+" "+(CrosslinksVsNrStarsBiggestCluster.GetAverageInBin(i)/NrOfStars)+" "+(NrOfStars-CrosslinksVsNrStarsBiggestCluster.GetAverageInBin(i))+" "+(1.0-(CrosslinksVsNrStarsBiggestCluster.GetAverageInBin(i)/NrOfStars)));
		}
		NrOfStarsSaver.DateiSchliessen();
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1() + "   InitFctGroupHEP: "+ InitialFunctionalGroupHeparin.ReturnM1() +  "   InitFctGroupStars: "+ InitialFunctionalGroupStars.ReturnM1());
		
		
		
		BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(dstDir+"/Percolation_Distribution_BMC_HepPEGConnectedGel_"+FileName+".batch", false);
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_HEP_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:3\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:5\"");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_PEG_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:3\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:5\"");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace.setzeZeile(" world xmin 0");
	    xmgrace.setzeZeile(" world xmax 1.0");
	    xmgrace.setzeZeile(" world ymin 0");
	    xmgrace.setzeZeile(" world ymax 1.0");
	    xmgrace.setzeZeile(" xaxis label \"extent of reaction p\"");
	    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace.setzeZeile(" xaxis tick major 0.2");//50000");
	    xmgrace.setzeZeile(" xaxis tick minor 0.05");
	    xmgrace.setzeZeile(" yaxis label \"relative frequency\"");
	    xmgrace.setzeZeile(" yaxis tick major 0.2");
	    xmgrace.setzeZeile(" yaxis tick minor 0.05");

	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 legend \"ClusterHEP (BMC)\"");

	    xmgrace.setzeZeile(" s1 line color 2");
	    xmgrace.setzeZeile(" s1 line linestyle 1");
	    xmgrace.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace.setzeZeile(" s1 legend \"SolHEP (BMC)\"");
	    
	    xmgrace.setzeZeile(" s2 line color 3");
	    xmgrace.setzeZeile(" s2 line linestyle 1");
	    xmgrace.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace.setzeZeile(" s2 legend \"ClusterPEG (BMC)\"");

	    xmgrace.setzeZeile(" s3 line color 4");
	    xmgrace.setzeZeile(" s3 line linestyle 1");
	    xmgrace.setzeZeile(" s3 line linewidth 1.5");
	    xmgrace.setzeZeile(" s3 legend \"SolPEG (BMC)\"");
	    
	    xmgrace.setzeZeile(" s4 line color 8");
	    xmgrace.setzeZeile(" s4 line linestyle 1");
	    xmgrace.setzeZeile(" s4 line linewidth 1.5");
	    xmgrace.setzeZeile(" s4 legend \"Monomers in Cluster (BMC)\"");
	    
	    xmgrace.setzeZeile(" LEGEND 0.15, 0.6");
	    xmgrace.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\sHEP,init\\N="+InitialFunctionalGroupHeparin.ReturnM1()+"\"");

	    xmgrace.setzeZeile(" SAVEALL \""+dstDir+"Percolation_Distribution_BMC_HepPEGConnectedGel_"+FileName+".agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+dstDir+"Percolation_Distribution_BMC_HepPEGConnectedGel_"+FileName+".ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	   
	    try {
	    	
	    	  System.out.println("xmgrace -batch "+dstDir+"/Percolation_Distribution_BMC_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
		      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/Percolation_Distribution_BMC_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
		    } catch (Exception e) {
		      System.err.println(e.toString());
		    }
		    
		    BFMFileSaver xmgraceFunc = new BFMFileSaver();
			xmgraceFunc.DateiAnlegen(dstDir+"/Percolation_Functionality_BMC_HepPEGConnectedGel_"+FileName+".batch", false);
			xmgraceFunc.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
		    xmgraceFunc.setzeZeile("FOCUS G0");
		    xmgraceFunc.setzeZeile(" AUTOSCALE ONREAD None");
		    xmgraceFunc.setzeZeile("READ BLOCK \""+dstDir+"Percolation_HEP_BMC_Functionality_HepPEGConnectedGel_"+FileName+".dat\"");
		    xmgraceFunc.setzeZeile("BLOCK xy \"1:3\"");
		    //xmgraceFunc.setzeZeile("READ BLOCK \""+dstDir+"Percolation_FuncHEP_Sol_AllMonomers_HepPEGConnectedGel_"+FileName+".dat\"");
		    xmgraceFunc.setzeZeile("BLOCK xy \"1:5\"");
		    xmgraceFunc.setzeZeile("READ BLOCK \""+dstDir+"Percolation_PEG_BMC_Functionality_HepPEGConnectedGel_"+FileName+".dat\"");
		    xmgraceFunc.setzeZeile("BLOCK xy \"1:3\"");
		    //xmgraceFunc.setzeZeile("READ BLOCK \""+dstDir+"Percolation_FuncStars_Sol_AllMonomers_HepPEGConnectedGel_"+FileName+".dat\"");
		    xmgraceFunc.setzeZeile("BLOCK xy \"1:5\"");
		    xmgraceFunc.setzeZeile(" world xmin 0");
		    xmgraceFunc.setzeZeile(" world xmax 1.0");
		    xmgraceFunc.setzeZeile(" world ymin 0");
		    xmgraceFunc.setzeZeile(" world ymax 1.0");
		    xmgraceFunc.setzeZeile(" xaxis label \"extent of reaction p\"");
		    xmgraceFunc.setzeZeile(" xaxis TICK MAJOR on");
		    xmgraceFunc.setzeZeile(" xaxis TICK MINOR on");
		    xmgraceFunc.setzeZeile(" xaxis tick major 0.2");//50000");
		    xmgraceFunc.setzeZeile(" xaxis tick minor 0.1");
		    xmgraceFunc.setzeZeile(" yaxis label \"normalized functionality\"");
		    xmgraceFunc.setzeZeile(" yaxis tick major 0.2");
		    xmgraceFunc.setzeZeile(" yaxis tick minor 0.1");

		    xmgraceFunc.setzeZeile(" s0 line color 1");
		    xmgraceFunc.setzeZeile(" s0 line linestyle 1");
		    xmgraceFunc.setzeZeile(" s0 line linewidth 1.5");
		    xmgraceFunc.setzeZeile(" s0 legend \"ClusterHEP\"");

		    xmgraceFunc.setzeZeile(" s1 line color 2");
		    xmgraceFunc.setzeZeile(" s1 line linestyle 1");
		    xmgraceFunc.setzeZeile(" s1 line linewidth 1.5");
		    xmgraceFunc.setzeZeile(" s1 legend \"SolHEP\"");
		    
		    xmgraceFunc.setzeZeile(" s2 line color 3");
		    xmgraceFunc.setzeZeile(" s2 line linestyle 1");
		    xmgraceFunc.setzeZeile(" s2 line linewidth 1.5");
		    xmgraceFunc.setzeZeile(" s2 legend \"ClusterPEG\"");

		    xmgraceFunc.setzeZeile(" s3 line color 4");
		    xmgraceFunc.setzeZeile(" s3 line linestyle 1");
		    xmgraceFunc.setzeZeile(" s3 line linewidth 1.5");
		    xmgraceFunc.setzeZeile(" s3 legend \"SolPEG\"");

		    xmgraceFunc.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\sHEP,init\\N="+InitialFunctionalGroupHeparin.ReturnM1()+"\"");

		    xmgraceFunc.setzeZeile(" SAVEALL \""+dstDir+"Percolation_Functionality_BMC_HepPEGConnectedGel_"+FileName+".agr\"");

		    xmgraceFunc.setzeZeile(" PRINT TO \""+dstDir+"Percolation_Functionality_BMC_HepPEGConnectedGel_"+FileName+".ps\"");
		    xmgraceFunc.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
		    xmgraceFunc.setzeZeile("PRINT");
			
		    xmgraceFunc.DateiSchliessen();
		    
		    try {
		    	
		    	  System.out.println("xmgrace -batch "+dstDir+"/Percolation_Functionality_BMC_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
			      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/Percolation_Functionality_BMC_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
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
		  
		  System.arraycopy(importData.GetFrameOfSimulation(currentFrame),0,Polymersystem,0, Polymersystem.length);
		  
			
		  importData.CloseSimulationFile();
		  
		  InitArrays();
		  
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
	
	 
	  private void InitArrays()
	  {
		  HeparinSterneVerknuepfung = null;
		  HeparinSterneVerknuepfung = new int[NrOfHeparin][NrOfStars];
		  
		  ColorHeparin = new int[NrOfHeparin+1];
		  ColorStars = new int[NrOfStars+1];
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  ColorStars[i]=i;
		  
		  for(int i = 0; i <= NrOfHeparin; i++)
			  ColorHeparin[i]=-i;
		  
		  StarClusterSize = new int[NrOfStars+1]; //Anzahl Sterne im Cluster
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  StarClusterSize[i]=1; 	//Anzahl der Bindungen - Summe ergibt NrOfStars auch nach Perkolation
		  
		  HeparinClusterSize = new int[NrOfStars+1]; //Anzahl Sterne im Cluster
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  HeparinClusterSize[i]=0; 	//Anzahl der Bindungen - Summe ergibt NrOfHeparin auch nach Perkolation
	  }
	  
	  
	  private void resetArrays()
	  {
		  for(int i = 0; i <= NrOfStars; i++)
			  ColorStars[i]=i;
		  
		  for(int i = 0; i <= NrOfHeparin; i++)
			  ColorHeparin[i]=-i;
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  StarClusterSize[i]=1; 	//Anzahl der Bindungen - Summe ergibt NrOfStars auch nach Perkolation
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  HeparinClusterSize[i]=0; 	//Anzahl der Bindungen - Summe ergibt NrOfHeparin auch nach Perkolation
	  
	  }
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 5)
		{
			System.out.println("Berechnung Bindungsstatistik von PEG+HEP");
			System.out.println("USAGE: dir/ Hydrogel_HEP_hep__PEG_peg_NStar_length__NoPerXYZ128[__xxx.bfm] StringGamma dstDir/ StringExperiment");
			System.exit(1);
		}
		System.out.println("Berechnung Perkolation und Clustergroesse von PEG-HEP-Netzwerk");
		new Auswertung_Perkolation_HepPEG(args[0], args[1], args[2], args[3], args[4]);//,args[1],args[2]);
	}
	
	public void LadeSystem(String FileDir, String FileName)
	{
		importData=null;
		importData = new BFMImportData(FileDir+FileName);
		
		MONOMERZAHL = importData.NrOfMonomers + 1;
		
		
		
		Polymersystem = null;
		Polymersystem = new int[MONOMERZAHL];
		
		Attributesystem = null;
		Attributesystem = new int[MONOMERZAHL];
		
		dumpsystem = null;
		dumpsystem = new int[MONOMERZAHL];
	
		boolean periodRB_x = importData.periodic_x;
		boolean periodRB_y = importData.periodic_y;
		boolean periodRB_z = importData.periodic_z;
		
		NrOfStars = importData.NrOfStars;
		NrOfMonomersPerStarArm = importData.NrOfMonomersPerStarArm;
		NrOfHeparin = importData.NrOfHeparin;
		
		
		PEGBonds = new int[NrOfStars];
		
		boolean periodRB = false;
		
		if ((periodRB_x == true) || (periodRB_y == true) || (periodRB_z == true))
			periodRB = true;
		
		
		System.arraycopy(importData.Polymersystem,0,Polymersystem,0, Polymersystem.length);
		
		System.arraycopy(importData.Attributes,0,Attributesystem,0, Attributesystem.length);
		
		//attributes: 1 -> HEP-COOH,  2 -> PEG-NH2
		int counterStarsGroup = 0;
		int counterHeparinGroup = 0;
		
		int offset = 90*NrOfHeparin;
		
		for (int k= 1; k <= NrOfHeparin; k++)
			for (int i= ((k-1)*90 +1); i <= k*90; i++)
				if(Attributesystem[i]==1)
					counterHeparinGroup++;
		
		for (int k= 1; k <= NrOfStars; k++)
			for (int i= (offset + (4*NrOfMonomersPerStarArm + 1)*(k-1) +1); i <= offset +( 4*NrOfMonomersPerStarArm + 1)*k; i++)
				if(Attributesystem[i]==2)
					counterStarsGroup++;
			
		InitialFunctionalGroupHeparin.AddValue((1.0*counterHeparinGroup)/NrOfHeparin);
		InitialFunctionalGroupStars.AddValue((1.0*counterStarsGroup)/NrOfStars);
		
		System.out.println("Stars: " + NrOfStars + "    ArmLength: "+ NrOfMonomersPerStarArm + "    Heparin:"+NrOfHeparin + "   InitAverageFuncHEP:"+((1.0*counterHeparinGroup)/NrOfHeparin) + "   InitAverageFuncStars:"+ ((1.0*counterStarsGroup)/NrOfStars));
		
		
		
		NrofFrames = importData.GetNrOfFrames();
	
		String zutrt = 	FileName.substring(0, FileName.length()-4);

		System.out.println("String : " + zutrt);
		
		
		
	}
	
	public void playSimulation(int frame)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				importData.GetFrameOfSimulation(frame);
				
				
				
				for(int it = 0; it < importData.addedBondsBetweenFrames.size(); it++)
				{
					resetArrays();
					
					long bondobj = importData.addedBondsBetweenFrames.get(it);
					//System.out.println(it + " bond " + bondobj);
					
					int a = getMono1Nr(bondobj);
					int b = getMono2Nr(bondobj);
					
					int HEPNumber = 0; //von 1 bis HEP
					int StarNumber = 0; //von 1 bis PEG
					
					if(a < b) //a=Heparin, b=Peg-Stern
					{
						HEPNumber = ((a-1)/90)+1;
						StarNumber = ((b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1))+1;
							
						HeparinSterneVerknuepfung[(a-1)/90][(b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						
						Attributesystem[a]-=1;
						Attributesystem[b]-=2;
					}
					else //b=Heparin, a=Peg-Stern
					{
						HEPNumber = ((b-1)/90)+1;
						StarNumber = ((a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1))+1;
						
						HeparinSterneVerknuepfung[(b-1)/90][(a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						
						Attributesystem[a]-=2;
						Attributesystem[b]-=1;
					}
					
					if((HEPNumber <= 0) || (HEPNumber > NrOfHeparin))
					{
						System.out.println("Fehler bei Heparin-Indizierung: " + HEPNumber +" von "+ NrOfHeparin );
						System.exit(1);
					}
					
					if((StarNumber <= 0) || (StarNumber > NrOfStars))
					{
						System.out.println("Fehler bei StarPEG-Indizierung: " + StarNumber +" von "+ NrOfStars );
						System.exit(1);
					}
					
					
					/*if(ColorHeparin[HEPNumber] < 0)	//Heparin hat noch keine Verbindung
					{
						ColorHeparin[HEPNumber] = ColorStars[StarNumber];
					}
					else
					{
						if(ColorStars[StarNumber] == StarNumber) //erste Modifizierung zum ersten Cluster
						{
							
							for(int i = 1; i <= NrOfHeparin; i++)
							{
								if(ColorHeparin[i] == StarNumber)
								{
									ColorHeparin[i]=ColorHeparin[HEPNumber];
								}
							}
							
							int counter = 0;
							for(int i = 1; i <= NrOfStars; i++)
							{
								if(ColorStars[i] == StarNumber)
								{
									counter++;
									ColorStars[i]=ColorHeparin[HEPNumber];
								}
							}
							
							StarClusterSize[StarNumber]=0;
							StarClusterSize[ColorHeparin[HEPNumber]]+=counter;
							
						}
						else //schon Clusterbildung
						{
							if(StarClusterSize[StarNumber] != 0)//gehoert schon zu einen Cluster
							{
								ColorStars[StarNumber] = ColorHeparin[HEPNumber];
								StarClusterSize[ColorHeparin[HEPNumber]]++;
								StarClusterSize[StarNumber]--;
							}
						}
					}*/
					
					/*
					 * Bestimmung der SingleBonds
					 */
					
					int counterSinglebondsTmp = 0;
					for(int i = 0; i < NrOfHeparin; i++)
						for(int j = 0; j < NrOfStars; j++)
						{
							if(HeparinSterneVerknuepfung[i][j] == 1)
								counterSinglebondsTmp++;
							/*else if (HeparinSterneVerknuepfung[i][j] == 2)
								counterDoublebonds++;
							else if (HeparinSterneVerknuepfung[i][j] == 3)
								counterTriplebonds++;
							else if (HeparinSterneVerknuepfung[i][j] == 4)
								counterQuadbonds++;
							*/
							else if (HeparinSterneVerknuepfung[i][j] > 4)
							{
								System.out.println("Verknuepfung 5! Sollte nicht vorkommen! Wert: " +HeparinSterneVerknuepfung[i][j] + " bei Hep:" + (i+1) + "   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
								System.exit(1); 
							}
						}
					
					
					/*
					 * Perkolationsalgorithmus
					 */
					
					for(int j = 1; j <= NrOfStars; j++)
					{
						if(ColorStars[j] == j) //Stern und Abzweigungen wurden noch nicht untersucht 
						for(int i = 0; i < returnHeparinConnectedToPEG(j).size(); i++)
						{
							ColoringHeparin(returnHeparinConnectedToPEG(j).get(i), j);
						}
							
					}
						
					
					//Auswertung
					
					counterAdditionalBonds++;
					
					int allStars=0;
					for(int i = 1; i<= NrOfStars; i++)
						allStars += StarClusterSize[i];
					
					
					for(int j = 0; j < NrOfStars; j++)
					{
						 if(PEGBonds[j] > 4)
						 {
								System.out.println("Verknuepfung 5! Sollte nicht vorkommen!   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
								System.exit(1); 
						 }
					}
					

					int offset = 90*NrOfHeparin;
					int ColorHighestNumber = 0;
					ColorHighestNumber = findMaximumArrayValueIndex();
					 
					
					//Rg2
					
					double Rcm_x = 0.0;
					double Rcm_y = 0.0;
					double Rcm_z = 0.0;
					
					int NrOfStarsInCluster = 0;
					int NrOfHeparinInCluster = 0;
					
					for (int k= 1; k <= NrOfStars; k++)
						if(ColorStars[k]==ColorHighestNumber)
						 {
							 for (int i= (offset + (4*NrOfMonomersPerStarArm + 1)*(k-1) +1); i <= offset +( 4*NrOfMonomersPerStarArm + 1)*k; i++)
							  {
								  Rcm_x += 1.0*(importData.PolymerKoordinaten[i][0]);
								  Rcm_y += 1.0*(importData.PolymerKoordinaten[i][1]);
								  Rcm_z += 1.0*(importData.PolymerKoordinaten[i][2]); 
							  }
							 
							NrOfStarsInCluster++;
						 }
					
					for (int k= 1; k <= NrOfHeparin; k++)
						if(ColorHeparin[k]==ColorHighestNumber)
						{
							for (int i= ((k-1)*90 +1); i <= k*90; i++)
							{
							  Rcm_x += 1.0*(importData.PolymerKoordinaten[i][0]);
							  Rcm_y += 1.0*(importData.PolymerKoordinaten[i][1]);
							  Rcm_z += 1.0*(importData.PolymerKoordinaten[i][2]);
							}
						 
						  NrOfHeparinInCluster++;
					 }
					
					Rcm_x /= (NrOfHeparinInCluster*90.0+NrOfStarsInCluster*(4*NrOfMonomersPerStarArm + 1.0));
					Rcm_y /= (NrOfHeparinInCluster*90.0+NrOfStarsInCluster*(4*NrOfMonomersPerStarArm + 1.0));
					Rcm_z /= (NrOfHeparinInCluster*90.0+NrOfStarsInCluster*(4*NrOfMonomersPerStarArm + 1.0));
					
					
					RadiusOfGyration2 = 0.0;
					
					for (int k= 1; k <= NrOfStars; k++)
						if(ColorStars[k]==ColorHighestNumber)
						 {
							double Rg2_x = 0.0;
							double Rg2_y = 0.0;
							double Rg2_z = 0.0;
							
							 for (int i= (offset + (4*NrOfMonomersPerStarArm + 1)*(k-1) +1); i <= offset +( 4*NrOfMonomersPerStarArm + 1)*k; i++)
							  //for (int j = i; j <= offset +( 4*NrOfMonomersPerStarArm + 1)*k; j++)
							  {
								  Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - Rcm_x)*(importData.PolymerKoordinaten[i][0] - Rcm_x);
								  Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - Rcm_y)*(importData.PolymerKoordinaten[i][1] - Rcm_y);
								  Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - Rcm_z)*(importData.PolymerKoordinaten[i][2] - Rcm_z);

								  //Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
								  //Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
								  //Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);
							  }
							 
							 RadiusOfGyration2 += Rg2_x + Rg2_y + Rg2_z;
							//NrOfStarsInCluster++;
						 }
					
					for (int k= 1; k <= NrOfHeparin; k++)
						if(ColorHeparin[k]==ColorHighestNumber)
						{
						
						double Rg2_x = 0.0;
						double Rg2_y = 0.0;
						double Rg2_z = 0.0;
							
						for (int i= ((k-1)*90 +1); i <= k*90; i++)
						  //for (int j = i; j <= k*90; j++)
						  {
							  Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - Rcm_x)*(importData.PolymerKoordinaten[i][0] - Rcm_x);
							  Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - Rcm_y)*(importData.PolymerKoordinaten[i][1] - Rcm_y);
							  Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - Rcm_z)*(importData.PolymerKoordinaten[i][2] - Rcm_z);

							  //Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
							 // Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
							 // Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);

						  }
						 
						RadiusOfGyration2 += Rg2_x + Rg2_y + Rg2_z;
						 
						 
						 // NrOfHeparinInCluster++;
						
					 }
					
					RadiusOfGyration2 /= (NrOfHeparinInCluster*90.0+NrOfStarsInCluster*(4*NrOfMonomersPerStarArm + 1.0));
					
					//Rg2 /= (NrOfHeparinInCluster*90.0+NrOfStarsInCluster*(4*NrOfMonomersPerStarArm + 1.0))*(NrOfHeparinInCluster*90.0+NrOfStarsInCluster*(4*NrOfMonomersPerStarArm + 1.0));
					  
					CrosslinksVsRGBiggestCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), RadiusOfGyration2);
					
					System.out.println("Frame: " +frame + "     maxCluster: "+ (117*StarClusterSize[findMaximumArrayValueIndex()]+90*HeparinClusterSize[findMaximumArrayValueIndex()]) +"       relativeMonomers: "+(117*StarClusterSize[findMaximumArrayValueIndex()]+90*HeparinClusterSize[findMaximumArrayValueIndex()])/(117.0*NrOfStars+90.0*NrOfHeparin)+"   addBonds: " + counterAdditionalBonds + "   Color: "+ ColorHighestNumber + "  Rg2: "+ RadiusOfGyration2);
					System.out.println("Stars: "+ (StarClusterSize[ColorHighestNumber]) + "  vs. "+NrOfStarsInCluster);
					System.out.println("HEP  : "+ (HeparinClusterSize[ColorHighestNumber])+ "  vs. "+NrOfHeparinInCluster);
					
					//End RG2-Calculation
					
					// Gamma in Biggest Cluster
					CrosslinksVsGammaCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), NrOfStarsInCluster/(1.0*NrOfHeparinInCluster));
					// End-Gamma in Biggest Cluster
					
					// FunctionalGroup in biggestCluster
					
					int counterFuncGroupBiggestClusterHeparin=0;
					int counterFuncGroupBiggestClusterStars=0;
					
					for (int k= 1; k <= NrOfStars; k++)
						if(ColorStars[k]==ColorHighestNumber)
						 {
							 for (int i= (offset + (4*NrOfMonomersPerStarArm + 1)*(k-1) +1); i <= offset +( 4*NrOfMonomersPerStarArm + 1)*k; i++)
								 if(Attributesystem[i]==2)
									 counterFuncGroupBiggestClusterStars++;
							
						 }
					
					for (int k= 1; k <= NrOfHeparin; k++)
						if(ColorHeparin[k]==ColorHighestNumber)
						{
							for (int i= ((k-1)*90 +1); i <= k*90; i++)
							 if(Attributesystem[i]==1)
								 counterFuncGroupBiggestClusterHeparin++;
					    }
					
					CrosslinksVsFuncHEPBiggestCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), (1.0*counterFuncGroupBiggestClusterHeparin)/NrOfHeparinInCluster);
					CrosslinksVsFuncStarsBiggestCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), (1.0*counterFuncGroupBiggestClusterStars)/NrOfStarsInCluster);
					
					CrosslinksVsNrStarsBiggestCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), NrOfStarsInCluster);
					CrosslinksVsNrHEPBiggestCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), NrOfHeparinInCluster);
					
					// FunctionalGroup in Sol
					
					int counterFuncGroupSolHeparin=0;
					int counterFuncGroupSolStars=0;
					
					for (int k= 1; k <= NrOfStars; k++)
						if(ColorStars[k]!=ColorHighestNumber)
						 {
							 for (int i= (offset + (4*NrOfMonomersPerStarArm + 1)*(k-1) +1); i <= offset +( 4*NrOfMonomersPerStarArm + 1)*k; i++)
								 if(Attributesystem[i]==2)
									 counterFuncGroupSolStars++;
							
						 }
					
					for (int k= 1; k <= NrOfHeparin; k++)
						if(ColorHeparin[k]!=ColorHighestNumber)
						{
							for (int i= ((k-1)*90 +1); i <= k*90; i++)
							 if(Attributesystem[i]==1)
								 counterFuncGroupSolHeparin++;
					    }
					
					//test if Heparin still there
					if((NrOfHeparin-NrOfHeparinInCluster)!=0)
					CrosslinksVsFuncHEPSol.AddValue(counterAdditionalBonds/(4.0*NrOfStars), (1.0*counterFuncGroupSolHeparin)/(NrOfHeparin-NrOfHeparinInCluster));
					
					//test if Star-sol still there
					if((NrOfStars-NrOfStarsInCluster)!=0)
					CrosslinksVsFuncStarsSol.AddValue(counterAdditionalBonds/(4.0*NrOfStars), (1.0*counterFuncGroupSolStars)/(NrOfStars-NrOfStarsInCluster));
					
					// end - FunctionalGroup in Sol
					
					for (int k= 1; k < MONOMERZAHL; k++)
						if(Attributesystem[k] < 0)
						{
							System.out.println("Functional group not correct -  Zeit:"+importData.MCSTime);
							System.exit(1);
						}
					
					// End-FunctionalGroup in biggestCluster
					
					//amount of Stars
					
					CrosslinksVsCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), (117*StarClusterSize[findMaximumArrayValueIndex()]+90*HeparinClusterSize[findMaximumArrayValueIndex()])/(117.0*NrOfStars+90.0*NrOfHeparin));
					
					if(counterSinglebondsTmp != counterSinglebonds) //Bindung hat sich erhoeht
					{
						counterSinglebonds=counterSinglebondsTmp;
						//Nicht hierfuer geschrieben
						CrosslinksVsCluster_LinearBonds.AddValue(counterSinglebonds/(4.0*NrOfStars), (117*StarClusterSize[findMaximumArrayValueIndex()]+90*HeparinClusterSize[findMaximumArrayValueIndex()])/(117.0*NrOfStars+90.0*NrOfHeparin));
						
					}
				}
				
				Rg2VsTime[(int) (importData.MCSTime/(deltaT))].AddValue(RadiusOfGyration2);
				
				
				System.out.println("size: " +importData.addedBondsBetweenFrames.size());
				importData.addedBondsBetweenFrames.clear();
	}
	
	
	private void ColoringHeparin(int HEPNr, int farbe)
	{
		//noch keine Verknuepfung
		if(ColorHeparin[HEPNr] < 0)
		{
			ColorHeparin[HEPNr] = farbe;
			HeparinClusterSize[farbe]++;
			
			for(int i = 0; i < returnPEGConnectedToHeparin(HEPNr).size(); i++)
				ColoringPEG(returnPEGConnectedToHeparin(HEPNr).get(i), farbe);
		}
		else //schon verknuepft
		{
			//for(int i = 0; i < returnPEGConnectedToHeparin(HEPNr).size(); i++)
			//	ColoringPEG(returnPEGConnectedToHeparin(HEPNr).get(i), farbe);
		}
	}
	
	private void ColoringPEG(int PEGNr, int farbe)
	{
		//schon verknuepft
		if(ColorStars[PEGNr] == farbe)
		{
			
		}
		else //noch nicht 
		{
			ColorStars[PEGNr] = farbe;
			StarClusterSize[PEGNr]--;
			StarClusterSize[farbe]++;
			
			for(int i = 0; i < returnHeparinConnectedToPEG(PEGNr).size(); i++)
				ColoringHeparin(returnHeparinConnectedToPEG(PEGNr).get(i), farbe);
		}
	}
	
	/**
	 * @param PEGnr starts at 1, end at NrOfStars
	 * @return IntArrayList with Heparinnumber connected to PEG with 1 <= HEPnr <= NrOfHeparin
	 */
	private IntArrayList returnHeparinConnectedToPEG(int PEGnr)
	{
		IntArrayList dummyList = new IntArrayList();
		
		int j = PEGnr-1;
		
		for(int i = 0; i < NrOfHeparin; i++)
			{
				if((HeparinSterneVerknuepfung[i][j] >= 1))
				{
					dummyList.add(i+1);
				}
			}
		
		return dummyList;
	}
	
	/**
	 * @param HEPnr 1 <= HEPnr <= NrOfHeparin
	 * @return IntArrayList with PEGnumber connected to HEPnr with 1 <= PEGnr <= NrOfStars
	 */
	private IntArrayList returnPEGConnectedToHeparin(int HEPnr)
	{
		IntArrayList dummyList = new IntArrayList();
		
		int i = HEPnr-1;
		
		for(int j = 0; j < NrOfStars; j++)
			{
				if((HeparinSterneVerknuepfung[i][j] >= 1))
				{
					dummyList.add(j+1);
				}
			}
		
		return dummyList;
	}
	/*private int findMaximumArrayValue()
	{
		int maximum = StarClusterSize[1];   // 1.Wert
		for (int i=2; i <= NrOfStars; i++)
		{
			if(StarClusterSize[i] > maximum) {
		            maximum = StarClusterSize[i];   // new maximum
		        }
		}
		return maximum;
	}*/
	
	private int findMaximumArrayValueIndex()
	{
		int maximum = 0;   // 1.Wert
		int maximumIndex =0;   // 1.Wert
		
		for (int i=1; i <= NrOfStars; i++)
		{
			//if(ColorHeparin[i] > 0)
			
				if((117*StarClusterSize[i]+90*HeparinClusterSize[i]) > maximum) {
		            maximum = (117*StarClusterSize[i]+90*HeparinClusterSize[i]);   // new maximum
		            maximumIndex = i;
		        }
			
			
		}
		return maximumIndex;
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
	   
}
