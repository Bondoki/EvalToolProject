package EvalToolProject_bcc.PEG;
import java.text.DecimalFormat;

import EvalToolProject_bcc.tools.BFMFileSaver;
import EvalToolProject_bcc.tools.BFMImportData;
import EvalToolProject_bcc.tools.HistogrammStatistik;
import EvalToolProject_bcc.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_bcc.tools.Statistik;

public class Auswertung_Sterne_Perkolation_HepPEG {


	
	
	String FileName;
	String FileDirectory;
	
	int[] Polymersystem;
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

	
	
	Statistik durchschnittbond;
	
	
	
	int[] PEGBonds;
	
	
	BFMFileSaver StarSaver;
	BFMFileSaver StarSaverNormalized;
	BFMFileSaver HEPSaverNormalized;
	
	BFMFileSaver StarSaverLinear;
	BFMFileSaver Rg2BiggestStarClusterSaver;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	HistogrammStatistik CrosslinksVsMonomersInCluster;
	HistogrammStatistik CrosslinksVsNrPEGBiggestStarCluster;
	HistogrammStatistik CrosslinksVsNrHEPBiggestStarCluster;
	
	HistogrammStatistik CrosslinksVsGammaStarCluster;
	
	HistogrammStatistik CrosslinksVsCluster_LinearBonds; //Clusterverteilung aus singelBonds
	HistogrammStatistik CrosslinksVsRGBiggestStarCluster;
	
	
	
	
	int[][] HeparinSterneVerknuepfung;
	int counterSinglebonds;
	
	int counterAdditionalBonds;
	
	Statistik InitialFunctionalGroupHeparin;
	Statistik InitialFunctionalGroupStars;
	
	
	public Auswertung_Sterne_Perkolation_HepPEG(String fdir, String fname, String gamma, String dstDir, String Experiment)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		CrosslinksVsMonomersInCluster= new HistogrammStatistik(-0.01,1.01,102);//(-0.05,1.05,44);
		CrosslinksVsNrPEGBiggestStarCluster = new HistogrammStatistik(-0.01,1.01,102);//51);
		CrosslinksVsNrHEPBiggestStarCluster= new HistogrammStatistik(-0.01,1.01,102);//51);
		
		CrosslinksVsCluster_LinearBonds = new HistogrammStatistik(-0.01,1.01,102);//51);;
		CrosslinksVsRGBiggestStarCluster = new HistogrammStatistik(-0.01,1.01,102);
		
		CrosslinksVsGammaStarCluster = new HistogrammStatistik(-0.01,1.01,102);
		
		
		InitialFunctionalGroupHeparin = new Statistik();
		InitialFunctionalGroupStars = new Statistik() ;
		
		
		/*StarSaver = new BFMFileSaver();
		StarSaver.DateiAnlegen(dstDir+"/Percolation_GammaEff_BSC_HepPEGConnectedGel_"+FileName+".dat", false);
		StarSaver.setzeZeile("# GammaEff <NrOfStars/AllStars>");
		
		StarSaverLinear = new BFMFileSaver();
		StarSaverLinear.DateiAnlegen(dstDir+"/Percolation_SingleBonds_BSC_HepPEGConnectedGel_"+FileName+".dat", false);
		StarSaverLinear.setzeZeile("# crosslinks(singlebonds) <NrOfStars/AllStars>");
		*/
		
		Rg2BiggestStarClusterSaver = new BFMFileSaver();
		Rg2BiggestStarClusterSaver.DateiAnlegen(dstDir+"/Percolation_Rg2_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat", false);
		Rg2BiggestStarClusterSaver.setzeZeile("# BiggestStarCluster incl. HEP & PEG");
		Rg2BiggestStarClusterSaver.setzeZeile("# crosslinks(all) <Rg^2>");
		
		
	
		
		durchschnittbond = new Statistik();
		
		
		
		DecimalFormat dh = new DecimalFormat("000");
		
		for(int i = 1; i <= 2; i+=1)//++)
		{
			counterAdditionalBonds = 0;
			LoadFile(FileName+"__"+dh.format(i)+".bfm", 1);
			
		}
		
		/*counterAdditionalBonds = 0;
		LoadFile(FileName+"__"+dh.format(11)+".bfm", 1);counterAdditionalBonds = 0;
		LoadFile(FileName+"__"+dh.format(16)+".bfm", 1);counterAdditionalBonds = 0;
		LoadFile(FileName+"__"+dh.format(21)+".bfm", 1);counterAdditionalBonds = 0;
		LoadFile(FileName+"__"+dh.format(26)+".bfm", 1);
		*/
		
		//output PEG-Distribution in BSC
		StarSaverNormalized = new BFMFileSaver();
		StarSaverNormalized.DateiAnlegen(dstDir+"/Percolation_PEG_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat", false);
		StarSaverNormalized.setzeZeile("# Init Nr HEP: " + NrOfHeparin);
		StarSaverNormalized.setzeZeile("# Init Nr PEG: " + NrOfStars);
		StarSaverNormalized.setzeZeile("# Init Fct Group HEP: " + InitialFunctionalGroupHeparin.ReturnM1());
		StarSaverNormalized.setzeZeile("# Init Fct Group Stars: " + InitialFunctionalGroupStars.ReturnM1());
		StarSaverNormalized.setzeZeile("# crosslinks(all) <NrStars BSC> <NrStars BSC/NrStars> <NrStars Sol> <NrStars Sol/NrStars>");
		
		for(int i = 0; i < CrosslinksVsNrPEGBiggestStarCluster.GetNrBins(); i++)
		{
			StarSaverNormalized.setzeZeile(CrosslinksVsNrPEGBiggestStarCluster.GetRangeInBin(i)+" "+CrosslinksVsNrPEGBiggestStarCluster.GetAverageInBin(i)+" "+(CrosslinksVsNrPEGBiggestStarCluster.GetAverageInBin(i)/NrOfStars)+" "+(NrOfStars-CrosslinksVsNrPEGBiggestStarCluster.GetAverageInBin(i))+" "+(1.0-(CrosslinksVsNrPEGBiggestStarCluster.GetAverageInBin(i)/NrOfStars)));
		}
		StarSaverNormalized.DateiSchliessen();
		
		
		//output HEP-Distribution in BSC
		HEPSaverNormalized = new BFMFileSaver();
		HEPSaverNormalized.DateiAnlegen(dstDir+"/Percolation_HEP_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat", false);
		HEPSaverNormalized.setzeZeile("# Init Nr HEP: " + NrOfHeparin);
		HEPSaverNormalized.setzeZeile("# Init Nr PEG: " + NrOfStars);
		HEPSaverNormalized.setzeZeile("# Init Fct Group HEP: " + InitialFunctionalGroupHeparin.ReturnM1());
		HEPSaverNormalized.setzeZeile("# Init Fct Group Stars: " + InitialFunctionalGroupStars.ReturnM1());
		HEPSaverNormalized.setzeZeile("# crosslinks(all) <NrHEP BSC> <NrHEP BSC/NrHeparin> <NrHEP Sol> <NrHEP Sol/NrHeparin>");;
		
		for(int i = 0; i < CrosslinksVsNrHEPBiggestStarCluster.GetNrBins(); i++)
		{
			HEPSaverNormalized.setzeZeile(CrosslinksVsNrHEPBiggestStarCluster.GetRangeInBin(i)+" "+CrosslinksVsNrHEPBiggestStarCluster.GetAverageInBin(i)+" "+(CrosslinksVsNrHEPBiggestStarCluster.GetAverageInBin(i)/NrOfHeparin)+" "+(NrOfHeparin-CrosslinksVsNrHEPBiggestStarCluster.GetAverageInBin(i))+" "+(1.0-(CrosslinksVsNrHEPBiggestStarCluster.GetAverageInBin(i)/NrOfHeparin)));
		}
		HEPSaverNormalized.DateiSchliessen();
		
		
		
		
		/*for(int i = 0; i < CrosslinksVsCluster_LinearBonds.GetNrBins(); i++)
		{
			StarSaverLinear.setzeZeile(CrosslinksVsCluster_LinearBonds.GetRangeInBin(i)+" "+CrosslinksVsCluster_LinearBonds.GetAverageInBin(i));
		}
		StarSaverLinear.DateiSchliessen();
		*/
		
		for(int i = 0; i < CrosslinksVsRGBiggestStarCluster.GetNrBins(); i++)
		{
			Rg2BiggestStarClusterSaver.setzeZeile(CrosslinksVsRGBiggestStarCluster.GetRangeInBin(i)+" "+CrosslinksVsRGBiggestStarCluster.GetAverageInBin(i));
		}
		Rg2BiggestStarClusterSaver.DateiSchliessen();
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1());
		
		
		//Monomers in the Biggest Star Cluster
		BFMFileSaver ClusterSaver = new BFMFileSaver();
		ClusterSaver.DateiAnlegen(dstDir+"/Percolation_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat", false);
		ClusterSaver.setzeZeile("# crosslinks(all) <BSC Monomers/AllMonomers>");
		
		
		for(int i = 0; i < CrosslinksVsMonomersInCluster.GetNrBins(); i++)
		{
			ClusterSaver.setzeZeile(CrosslinksVsMonomersInCluster.GetRangeInBin(i)+" "+CrosslinksVsMonomersInCluster.GetAverageInBin(i));
		}
		ClusterSaver.DateiSchliessen();
		
		
		//export the archieved Gamma in the Biggest Cluster
		BFMFileSaver GammaClusterSaver = new BFMFileSaver();
		GammaClusterSaver.DateiAnlegen(dstDir+"/Percolation_Gamma_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat", false);
		GammaClusterSaver.setzeZeile("# crosslinks(all) <Gamma in BSC> <Gamma in BSC/InitGamma>");
		
		for(int i = 0; i < CrosslinksVsGammaStarCluster.GetNrBins(); i++)
		{
			GammaClusterSaver.setzeZeile(CrosslinksVsGammaStarCluster.GetRangeInBin(i)+" "+CrosslinksVsGammaStarCluster.GetAverageInBin(i)+" "+(CrosslinksVsGammaStarCluster.GetAverageInBin(i)/(NrOfStars/(1.0*NrOfHeparin))));
		}
		GammaClusterSaver.DateiSchliessen();
		

		BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(dstDir+"/Percolation_Distribution_BSC_HepPEGConnectedGel_"+FileName+".batch", false);
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_HEP_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:3\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:5\"");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_PEG_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:3\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:5\"");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
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
	    //xmgrace.setzeZeile(" s0 symbol 1");
	    //xmgrace.setzeZeile(" s0 symbol color 1");
	    xmgrace.setzeZeile(" s0 legend \"ClusterHEP (BSC)\"");

	    xmgrace.setzeZeile(" s1 line color 2");
	    xmgrace.setzeZeile(" s1 line linestyle 1");
	    xmgrace.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace.setzeZeile(" s1 legend \"SolHEP (BSC)\"");
	    
	    xmgrace.setzeZeile(" s2 line color 3");
	    xmgrace.setzeZeile(" s2 line linestyle 1");
	    xmgrace.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace.setzeZeile(" s2 legend \"ClusterPEG (BSC)\"");

	    xmgrace.setzeZeile(" s3 line color 4");
	    xmgrace.setzeZeile(" s3 line linestyle 1");
	    xmgrace.setzeZeile(" s3 line linewidth 1.5");
	    xmgrace.setzeZeile(" s3 legend \"SolPEG (BSC)\"");
	    
	    xmgrace.setzeZeile(" s4 line color 8");
	    xmgrace.setzeZeile(" s4 line linestyle 1");
	    xmgrace.setzeZeile(" s4 line linewidth 1.5");
	    xmgrace.setzeZeile(" s4 legend \"Monomers in Cluster (BSC)\"");
	    
	    //xmgrace.setzeZeile(" s4 line color 5");
	    //xmgrace.setzeZeile(" s4 line linestyle 1");
	    //xmgrace.setzeZeile(" s4 line linewidth 1.5");
	    //xmgrace.setzeZeile(" s4 legend \"\\xg\\f{}/\\xg\\f{}\\sinit\\N\"");

	    xmgrace.setzeZeile(" LEGEND 0.15, 0.6");
	    
	    xmgrace.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\sHEP,init\\N="+InitialFunctionalGroupHeparin.ReturnM1()+"\"");

	    xmgrace.setzeZeile(" SAVEALL \""+dstDir+"Percolation_Distribution_BSC_HepPEGConnectedGel_"+FileName+".agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+dstDir+"Percolation_Distribution_BSC_HepPEGConnectedGel_"+FileName+".ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	   
	    try {
	    	
	    	  System.out.println("xmgrace -batch "+dstDir+"/Percolation_Distribution_BSC_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
		      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/Percolation_Distribution_BSC_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
		    } catch (Exception e) {
		      System.err.println(e.toString());
		    }

		    BFMFileSaver xmgrace2 = new BFMFileSaver();
			xmgrace2.DateiAnlegen(dstDir+"/Percolation_Gamma_Comparison_HepPEGConnectedGel_"+FileName+".batch", false);
			xmgrace2.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
		    xmgrace2.setzeZeile("FOCUS G0");
		    xmgrace2.setzeZeile(" AUTOSCALE ONREAD None");
		    xmgrace2.setzeZeile("READ BLOCK \""+dstDir+"Percolation_Gamma_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
		    xmgrace2.setzeZeile("BLOCK xy \"1:2\"");
		    xmgrace2.setzeZeile("BLOCK xy \"1:3\"");
		    xmgrace2.setzeZeile("READ BLOCK \""+dstDir+"Percolation_Gamma_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
		    xmgrace2.setzeZeile("BLOCK xy \"1:2\"");
		    xmgrace2.setzeZeile("BLOCK xy \"1:3\"");
		    xmgrace2.setzeZeile(" world xmin 0");
		    xmgrace2.setzeZeile(" world xmax 1.0");
		    xmgrace2.setzeZeile(" world ymin 0");
		    xmgrace2.setzeZeile(" world ymax 10.0");
		    xmgrace2.setzeZeile(" xaxis label \"extent of reaction p\"");
		    xmgrace2.setzeZeile(" xaxis TICK MAJOR on");
		    xmgrace2.setzeZeile(" xaxis TICK MINOR on");
		    xmgrace2.setzeZeile(" xaxis tick major 0.2");//50000");
		    xmgrace2.setzeZeile(" xaxis tick minor 0.05");
		    xmgrace2.setzeZeile(" yaxis label \"relative frequency\"");
		    xmgrace2.setzeZeile(" yaxis tick major 2.0");
		    xmgrace2.setzeZeile(" yaxis tick minor 0.5");

		    xmgrace2.setzeZeile(" s0 line color 1");
		    xmgrace2.setzeZeile(" s0 line linestyle 1");
		    xmgrace2.setzeZeile(" s0 line linewidth 1.5");
		    //xmgrace.setzeZeile(" s0 symbol 1");
		    //xmgrace.setzeZeile(" s0 symbol color 1");
		    xmgrace2.setzeZeile(" s0 legend \"\\xg\\f{}\\N (BMC)\"");

		    xmgrace2.setzeZeile(" s1 line color 2");
		    xmgrace2.setzeZeile(" s1 line linestyle 1");
		    xmgrace2.setzeZeile(" s1 line linewidth 1.5");
		    xmgrace2.setzeZeile(" s1 legend \"\\xg\\f{}/\\xg\\f{}\\sinit\\N (BMC)\"");
		    
		    xmgrace2.setzeZeile(" s2 line color 3");
		    xmgrace2.setzeZeile(" s2 line linestyle 1");
		    xmgrace2.setzeZeile(" s2 line linewidth 1.5");
		    xmgrace2.setzeZeile(" s2 legend \"\\xg\\f{}\\N (BSC)\"");

		    xmgrace2.setzeZeile(" s3 line color 4");
		    xmgrace2.setzeZeile(" s3 line linestyle 1");
		    xmgrace2.setzeZeile(" s3 line linewidth 1.5");
		    xmgrace2.setzeZeile(" s3 legend \"\\xg\\f{}/\\xg\\f{}\\sinit\\N (BSC)\"");
		    
		    
		    xmgrace2.setzeZeile(" LEGEND 0.85, 0.85");
		    
		    xmgrace2.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\sHEP,init\\N="+InitialFunctionalGroupHeparin.ReturnM1()+"\"");

		    xmgrace2.setzeZeile(" SAVEALL \""+dstDir+"Percolation_Gamma_Comparison_HepPEGConnectedGel_"+FileName+".agr\"");

		    xmgrace2.setzeZeile(" PRINT TO \""+dstDir+"Percolation_Gamma_Comparison_HepPEGConnectedGel_"+FileName+".ps\"");
		    xmgrace2.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
		    xmgrace2.setzeZeile("PRINT");
			
		    xmgrace2.DateiSchliessen();
		   
		    try {
		    	
		    	  System.out.println("xmgrace -batch "+dstDir+"/Percolation_Gamma_Comparison_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
			      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/Percolation_Gamma_Comparison_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
			    } catch (Exception e) {
			      System.err.println(e.toString());
			    }		
			
		BFMFileSaver xmgrace3 = new BFMFileSaver();
		xmgrace3.DateiAnlegen(dstDir+"/Percolation_Distribution_Comparison_HepPEGConnectedGel_"+FileName+".batch", false);
		xmgrace3.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
		xmgrace3.setzeZeile("FOCUS G0");
		xmgrace3.setzeZeile(" AUTOSCALE ONREAD None");
		xmgrace3.setzeZeile("READ BLOCK \""+dstDir+"Percolation_HEP_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace3.setzeZeile("BLOCK xy \"1:3\"");
	    xmgrace3.setzeZeile("READ BLOCK \""+dstDir+"Percolation_PEG_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace3.setzeZeile("BLOCK xy \"1:3\"");
	    xmgrace3.setzeZeile("READ BLOCK \""+dstDir+"Percolation_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace3.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace3.setzeZeile("READ BLOCK \""+dstDir+"Percolation_HEP_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace3.setzeZeile("BLOCK xy \"1:3\"");
	    xmgrace3.setzeZeile("READ BLOCK \""+dstDir+"Percolation_PEG_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace3.setzeZeile("BLOCK xy \"1:3\"");
	    xmgrace3.setzeZeile("READ BLOCK \""+dstDir+"Percolation_BSC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace3.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace3.setzeZeile(" world xmin 0");
		xmgrace3.setzeZeile(" world xmax 1.0");
		xmgrace3.setzeZeile(" world ymin 0");
		xmgrace3.setzeZeile(" world ymax 1.0");
		xmgrace3.setzeZeile(" xaxis label \"extent of reaction p\"");
		xmgrace3.setzeZeile(" xaxis TICK MAJOR on");
		xmgrace3.setzeZeile(" xaxis TICK MINOR on");
		xmgrace3.setzeZeile(" xaxis tick major 0.2");//50000");
		xmgrace3.setzeZeile(" xaxis tick minor 0.05");
		xmgrace3.setzeZeile(" yaxis label \"relative frequency\"");
		xmgrace3.setzeZeile(" yaxis tick major 0.2");
		xmgrace3.setzeZeile(" yaxis tick minor 0.05");

	    xmgrace3.setzeZeile(" s0 line color 1");
	    xmgrace3.setzeZeile(" s0 line linestyle 1");
	    xmgrace3.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace3.setzeZeile(" s0 symbol 1");
	    xmgrace3.setzeZeile(" s0 symbol color 1");
	    xmgrace3.setzeZeile(" s0 SYMBOL SIZE 0.75");
	    xmgrace3.setzeZeile(" s0 SYMBOL Skip 1");
	    xmgrace3.setzeZeile(" s0 SYMBOL FILL pattern 1");
	    xmgrace3.setzeZeile(" s0 SYMBOL FILL COLOR 1");
	    xmgrace3.setzeZeile(" s0 legend \"ClusterHEP (BMC)\"");
	    
	    xmgrace3.setzeZeile(" s1 line color 2");
	    xmgrace3.setzeZeile(" s1 line linestyle 1");
	    xmgrace3.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace3.setzeZeile(" s1 symbol 2");
	    xmgrace3.setzeZeile(" s1 symbol color 2");
	    xmgrace3.setzeZeile(" s1 SYMBOL SIZE 0.75");
	    xmgrace3.setzeZeile(" s1 SYMBOL Skip 1");
	    xmgrace3.setzeZeile(" s1 SYMBOL FILL pattern 1");
	    xmgrace3.setzeZeile(" s1 SYMBOL FILL COLOR 2");
	    xmgrace3.setzeZeile(" s1 legend \"ClusterPEG (BMC)\"");

	    xmgrace3.setzeZeile(" s2 line color 8");
	    xmgrace3.setzeZeile(" s2 line linestyle 1");
	    xmgrace3.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace3.setzeZeile(" s2 symbol 3");
	    xmgrace3.setzeZeile(" s2 symbol color 8");
	    xmgrace3.setzeZeile(" s2 SYMBOL SIZE 0.75");
	    xmgrace3.setzeZeile(" s2 SYMBOL Skip 1");
	    xmgrace3.setzeZeile(" s2 SYMBOL FILL pattern 1");
	    xmgrace3.setzeZeile(" s2 SYMBOL FILL COLOR 8");
	    xmgrace3.setzeZeile(" s2 legend \"Monomers in Cluster (BMC)\"");

	    xmgrace3.setzeZeile(" s3 line color 1");
	    xmgrace3.setzeZeile(" s3 line linestyle 1");
	    xmgrace3.setzeZeile(" s3 line linewidth 1.5");
	    xmgrace3.setzeZeile(" s3 symbol 1");
	    xmgrace3.setzeZeile(" s3 symbol color 1");
	    xmgrace3.setzeZeile(" s3 SYMBOL SIZE 0.75");
	    xmgrace3.setzeZeile(" s3 SYMBOL Skip 3");
	    xmgrace3.setzeZeile(" s3 SYMBOL FILL pattern 4");
	    xmgrace3.setzeZeile(" s3 SYMBOL FILL COLOR 1");
	    xmgrace3.setzeZeile(" s3 legend \"ClusterHEP (BSC)\"");
	    
	    xmgrace3.setzeZeile(" s4 line color 2");
	    xmgrace3.setzeZeile(" s4 line linestyle 1");
	    xmgrace3.setzeZeile(" s4 line linewidth 1.5");
	    xmgrace3.setzeZeile(" s4 symbol 2");
	    xmgrace3.setzeZeile(" s4 symbol color 2");
	    xmgrace3.setzeZeile(" s4 SYMBOL SIZE 0.75");
	    xmgrace3.setzeZeile(" s4 SYMBOL Skip 3");
	    xmgrace3.setzeZeile(" s4 SYMBOL FILL pattern 4");
	    xmgrace3.setzeZeile(" s4 SYMBOL FILL COLOR 2");
	    xmgrace3.setzeZeile(" s4 legend \"ClusterPEG (BSC)\"");

	    xmgrace3.setzeZeile(" s5 line color 8");
	    xmgrace3.setzeZeile(" s5 line linestyle 1");
	    xmgrace3.setzeZeile(" s5 line linewidth 1.5");
	    xmgrace3.setzeZeile(" s5 symbol 3");
	    xmgrace3.setzeZeile(" s5 symbol color 8");
	    xmgrace3.setzeZeile(" s5 SYMBOL SIZE 0.75");
	    xmgrace3.setzeZeile(" s5 SYMBOL Skip 3");
	    xmgrace3.setzeZeile(" s5 SYMBOL FILL pattern 4");
	    xmgrace3.setzeZeile(" s5 SYMBOL FILL COLOR 8");
	    xmgrace3.setzeZeile(" s5 legend \"Monomers in Cluster (BSC)\"");
	    
		xmgrace3.setzeZeile(" LEGEND 0.15, 0.85");
			    
		xmgrace3.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\sHEP,init\\N="+InitialFunctionalGroupHeparin.ReturnM1()+"\"");

		xmgrace3.setzeZeile(" SAVEALL \""+dstDir+"Percolation_Distribution_Comparison_HepPEGConnectedGel_"+FileName+".agr\"");

		xmgrace3.setzeZeile(" PRINT TO \""+dstDir+"Percolation_Distribution_Comparison_HepPEGConnectedGel_"+FileName+".ps\"");
		xmgrace3.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
		xmgrace3.setzeZeile("PRINT");
				
		xmgrace3.DateiSchliessen();
			   
		try {
			  System.out.println("xmgrace -batch "+dstDir+"/Percolation_Distribution_Comparison_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
			  Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/Percolation_Distribution_Comparison_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
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
			  StarClusterSize[i]=1; 	//Anzahl der Bindungen - Summe ergibt 4*NrOfStars auch nach Perkolation
	  }
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 5)
		{
			System.out.println("Berechnung Perkolation und Clustergroesse (Biggest Star Cluster) von Sternen im PEG-HEP-Netzwerk (nur von PEG)");
			System.out.println("USAGE: dir/ Hydrogel_HEP_hep__PEG_peg_NStar_length__NoPerXYZ128[__xxx.bfm] StringGamma dstDir/ StringExperiment");
			System.exit(1);
		}
		System.out.println("Berechnung Perkolation und Clustergroesse von Sternen im PEG-HEP-Netzwerk (nur von PEG)");
		new Auswertung_Sterne_Perkolation_HepPEG(args[0], args[1], args[2], args[3], args[4]);//,args[1],args[2]);
	}
	
	public void LadeSystem(String FileDir, String FileName)
	{
		importData=null;
		importData = new BFMImportData(FileDir+FileName);
		
		MONOMERZAHL = importData.NrOfMonomers + 1;
		
		
		
		Polymersystem = null;
		Polymersystem = new int[MONOMERZAHL];
		
		int[] Attributesystem = null;
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
		
		//System.out.println("Stars: " + NrOfStars + "    ArmLength: "+ NrOfMonomersPerStarArm + "    Heparin:"+NrOfHeparin);
		
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
				
				
				for(int it = 0; it < importData.addedBondsBetweenFrames.size(); it++)
				{
					long bondobj = importData.addedBondsBetweenFrames.get(it);
					//System.out.println(it + " bond " + bondobj);
					
					int a = getMono1Nr(bondobj);
					int b = getMono2Nr(bondobj);
					
					int HEPNumber = 0;
					int StarNumber = 0;
					
					if(a < b) //a=Heparin, b=Peg-Stern
					{
						HEPNumber = ((a-1)/90)+1;
						StarNumber = ((b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1))+1;
							
						HeparinSterneVerknuepfung[(a-1)/90][(b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
					}
					else //b=Heparin, a=Peg-Stern
					{
						HEPNumber = ((b-1)/90)+1;
						StarNumber = ((a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1))+1;
						
						HeparinSterneVerknuepfung[(b-1)/90][(a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
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
					
					if(ColorHeparin[HEPNumber] < 0)	//Heparin hat noch keine Verbindung
					{
						ColorHeparin[HEPNumber] = ColorStars[StarNumber];
					}
					else if (ColorHeparin[HEPNumber] != ColorStars[StarNumber])
					{
						if((StarClusterSize[ColorHeparin[HEPNumber]] <= StarClusterSize[ColorStars[StarNumber]]  )) 
						{
							int oldColor = ColorHeparin[HEPNumber];
							int newColor = ColorStars[StarNumber];
							
							for(int i = 1; i <= NrOfHeparin; i++)
							{
								if(ColorHeparin[i] == oldColor)
								{
									ColorHeparin[i] = newColor;
								}
							}
							
							int counter = 0;
							for(int i = 1; i <= NrOfStars; i++)
							{
								if(ColorStars[i] == oldColor)
								{
									counter+=StarClusterSize[i];
									ColorStars[i]=newColor;
								}
							}
							
							StarClusterSize[oldColor]=0;
							StarClusterSize[newColor]+=counter;
							
						}
						else //neue Verbindung zum groesseren Cluster
						{
							int oldColor = ColorStars[StarNumber];
							int newColor = ColorHeparin[HEPNumber];
							
							for(int i = 1; i <= NrOfHeparin; i++)
							{
								if(ColorHeparin[i] == oldColor)
								{
									ColorHeparin[i] = newColor;
								}
							}
							
							int counter = 0;
							for(int i = 1; i <= NrOfStars; i++)
							{
								if(ColorStars[i] == oldColor)
								{
									counter+=StarClusterSize[i];
									ColorStars[i]=newColor;
								}
							}
							
							StarClusterSize[oldColor]=0;
							StarClusterSize[newColor]+=counter;
						}
					}
					
					counterAdditionalBonds++;
					
					int allStars=0;
					for(int i = 1; i<= NrOfStars; i++)
						allStars += StarClusterSize[i];
					
					System.out.println("Frame: " +frame + "     maxCluster: "+ findMaximumArrayValue() +"       SumCluster: " + allStars + "      addBonds: " + counterAdditionalBonds);
					
					for(int j = 0; j < NrOfStars; j++)
					{
						 if(PEGBonds[j] > 4)
						 {
								System.out.println("Verknuepfung 5! Sollte nicht vorkommen!   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
								System.exit(1); 
						 }
					}
					
					
					//Rg2
					
					int ColorHighestNumber = 0;
					ColorHighestNumber = findMaximumArrayValueIndex();
					
					int offset = 90*NrOfHeparin;
					  
					
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
					
					
					double Rg2 = 0.0;
					
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
							 
							Rg2 += Rg2_x + Rg2_y + Rg2_z;
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
						 
						Rg2 += Rg2_x + Rg2_y + Rg2_z;
						 
						 
						 // NrOfHeparinInCluster++;
						
					 }
					
					Rg2 /= (NrOfHeparinInCluster*90.0+NrOfStarsInCluster*(4*NrOfMonomersPerStarArm + 1.0));
					
					//Rg2 /= (NrOfHeparinInCluster*90.0+NrOfStarsInCluster*(4*NrOfMonomersPerStarArm + 1.0))*(NrOfHeparinInCluster*90.0+NrOfStarsInCluster*(4*NrOfMonomersPerStarArm + 1.0));
					  
					CrosslinksVsRGBiggestStarCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), Rg2);
					
					System.out.println("Frame: " +frame +"   addBonds: " + counterAdditionalBonds + "   Color: "+ ColorHighestNumber + "  Rg2: "+ Rg2);
					System.out.println("Stars: "+ (StarClusterSize[ColorHighestNumber]) + "  vs. "+NrOfStarsInCluster);
					System.out.println("HEP  : "+NrOfHeparinInCluster + "  --> Gamma = " +NrOfStarsInCluster/(1.0*NrOfHeparinInCluster));
					
					//amount of Stars
					
					CrosslinksVsNrPEGBiggestStarCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), NrOfStarsInCluster);
					CrosslinksVsNrHEPBiggestStarCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), NrOfHeparinInCluster);
					
					CrosslinksVsMonomersInCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), (117*NrOfStarsInCluster+90*NrOfHeparinInCluster)/(117.0*NrOfStars+90.0*NrOfHeparin));
					
					
					// Gamma in Biggest Star Cluster
					CrosslinksVsGammaStarCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), NrOfStarsInCluster/(1.0*NrOfHeparinInCluster));
					// End-Gamma in Biggest Star Cluster
	
					
					if(counterSinglebondsTmp != counterSinglebonds) //Bindung hat sich erhoeht
					{
						counterSinglebonds=counterSinglebondsTmp;
						CrosslinksVsCluster_LinearBonds.AddValue(counterSinglebonds/(4.0*NrOfStars), findMaximumArrayValue()/(1.0*NrOfStars));
						
					}
				}
				
				/*int allStars=0;
				for(int i = 1; i<= NrOfStars; i++)
					allStars += StarClusterSize[i];
				
				System.out.println("Frame: " +frame + "     maxCluster: "+ findMaximumArrayValue() +"       SumCluster: " + allStars + "      addBonds: " + counterAdditionalBonds);
				
				for(int j = 0; j < NrOfStars; j++)
				{
					 if(PEGBonds[j] > 4)
					 {
							System.out.println("Verknuepfung 5! Sollte nicht vorkommen!   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
							System.exit(1); 
					 }
				}
				
				CrosslinksVsCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), findMaximumArrayValue()/(1.0*NrOfStars));
				*/
				//System.out.println("size: " +bonds[(int) (importData.MCSTime/(10000))].ReturnN());
				System.out.println("size: " +importData.addedBondsBetweenFrames.size());
				importData.addedBondsBetweenFrames.clear();
	}
	
	
	private int findMaximumArrayValue()
	{
		int maximum = StarClusterSize[1];   // 1.Wert
		for (int i=2; i <= NrOfStars; i++)
		{
			if(StarClusterSize[i] > maximum) {
		            maximum = StarClusterSize[i];   // new maximum
		        }
		}
		return maximum;
	}

	private int findMaximumArrayValueIndex()
	{
		int maximum = 0;   // 1.Wert
		int maximumIndex =0;   // 1.Wert
		
		for (int i=1; i <= NrOfStars; i++)
		{
			//if(ColorHeparin[i] > 0)
			
				if((StarClusterSize[i]) > maximum) {
		            maximum = (StarClusterSize[i]);   // new maximum
		            maximumIndex = i;
		        }
					
		}
		//condition for the first cluster consisting 1 PEG, that HEP should be attached
		// I know that at the beginning Cluster of 4 HEP to 1 Star are not considered, but this not importent
		if(maximum == 1)
		{
			for (int i=1; i <= NrOfStars; i++)
			{
				for(int j = 1; j <= NrOfHeparin; j++)
				{
					
					if(ColorHeparin[j] == i)
					{
				            maximum = (StarClusterSize[i]);   // new maximum
				            maximumIndex = i;
				    }
				}		
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
