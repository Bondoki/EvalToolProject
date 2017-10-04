package EvalToolProject_ice.CrossStars;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

import EvalToolProject_ice.tools.Histogramm;
import EvalToolProject_ice.tools.HistogrammStatistik;
import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.IntArrayList;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Stars_CrossStars_VerknuepfungDefekte {


	
	
	String FileName;
	String FileDirectory;
	
	int[] Polymersystem;
	int[] Attributesystem;
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
	int Gitter_x;
	int Gitter_y;
	int Gitter_z;
	
	
	BFMImportData importData;
	
	//int NrOfHeparin = 0;
	int NrOfStars = 0;
	int NrOfMonomersPerStarArm = 0;
	
	int NrOfCrosslinker = 0;
	int CrosslinkerFunctionality = 0;
	
	int[] dumpsystem;

	/*Statistik[] GesamtStat_x;
	Statistik[] GesamtStat_y;
	Statistik[] GesamtStat_z;*/
	
	//double [] Rcm_x;
	//double [] Rcm_y;
	//double [] Rcm_z;
	
	Statistik[] bonds;
	Statistik durchschnittbond;
	
	int[][] CrosslinkerSterneVerknuepfung;
	
	int[] PEGBonds;
	int[] PEGBondsDifferentHEP; //Nr of PEG-bonds attached to distinguishable Crosslinker - index = PEGnr - value in [0, 4]
	
	Statistik[] DenglingEnds2;
	
	Statistik[] Singlebonds;
	Statistik[] Doublebonds;
	Statistik[] Triplebonds;
	Statistik[] Quadbonds;
	Statistik[] DenglingEnds;
	
	BFMFileSaver rg;
	BFMFileSaver BondSaver;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	int[] counterFiles;
	
	int FunktionalitaetCrosslinker[];
	BFMFileSaver FunktionalitaetCrosslinkerSaver;
	
	//relative frequency of bond types at special cross-links
	Statistik SingleBondsAtGamma95;
	Statistik DoubleBondsAtGamma95;
	Statistik TripleBondsAtGamma95;
	Statistik QuadBondsAtGamma95;
	
	BFMFileSaver SaverBondsAtGamma95;
	
	Statistik SingleBondsAtGamma75;
	Statistik DoubleBondsAtGamma75;
	Statistik TripleBondsAtGamma75;
	Statistik QuadBondsAtGamma75;
	
	BFMFileSaver SaverBondsAtGamma75;
	
	Statistik InitialFunctionalGroupCrosslinker;
	Statistik InitialFunctionalGroupStars;
	
	BFMFileSaver BondSaver_ExtentOfReactionHistogramm;
	Histogramm Histogramm_S0D0T0Q0;
	Histogramm Histogramm_S1D0T0Q0;
	Histogramm Histogramm_S2D0T0Q0;
	Histogramm Histogramm_S0D1T0Q0;
	Histogramm Histogramm_S3D0T0Q0;
	Histogramm Histogramm_S1D1T0Q0;
	Histogramm Histogramm_S0D0T1Q0;
	Histogramm Histogramm_S4D0T0Q0;
	Histogramm Histogramm_S2D1T0Q0;
	Histogramm Histogramm_S0D2T0Q0;
	Histogramm Histogramm_S1D0T1Q0;
	Histogramm Histogramm_S0D0T0Q1;
	
	int addedBondsDuringSimulation;
	
	long deltaT;
	
	HistogrammStatistik CrosslinksVsAllMonomersInCluster;
	HistogrammStatistik CrosslinksVsNrStarsBiggestCluster;
	HistogrammStatistik CrosslinksVsNrCrosslinkerBiggestCluster;
	
	HistogrammStatistik CrosslinksVsFuncStarsBiggestCluster;
	HistogrammStatistik CrosslinksVsFuncCrosslinkerBiggestCluster;
	HistogrammStatistik CrosslinksVsFuncCrosslinkerSol;
	HistogrammStatistik CrosslinksVsFuncStarsSol;
	
	int ColorCrosslinker[];
	int ColorStars[];
	int StarClusterSize[];
	int CrosslinkerClusterSize[];
	
	public Auswertung_Stars_CrossStars_VerknuepfungDefekte(String fdir, String fname, String gamma, String dirDst,  String Experiment, int nrOfFiles)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ fname+"__001.bfm");
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		NrOfStars= FirstData.NrOfStars;
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		//End - Determine MaxFrame out of the first file
		
		
		Polymersystem = new int[1];
		Attributesystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		double lowerBoundary = 0.0;
		double higherBoundary = 1.0;
		int NrBins = 200;
		
		Histogramm_S0D0T0Q0 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		Histogramm_S1D0T0Q0 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		Histogramm_S2D0T0Q0 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		Histogramm_S0D1T0Q0 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		Histogramm_S3D0T0Q0 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		Histogramm_S1D1T0Q0 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		Histogramm_S0D0T1Q0 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		Histogramm_S4D0T0Q0 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		Histogramm_S2D1T0Q0 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		Histogramm_S0D2T0Q0 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		Histogramm_S1D0T1Q0 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		Histogramm_S0D0T0Q1 = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		
		
		CrosslinksVsAllMonomersInCluster= new HistogrammStatistik(0.00,1.0,100);//(-0.01,1.01,102);
		CrosslinksVsNrStarsBiggestCluster = new HistogrammStatistik(0.00,1.0,100);//(-0.01,1.01,102);
		CrosslinksVsNrCrosslinkerBiggestCluster = new HistogrammStatistik(0.00,1.0,100);//(-0.01,1.01,102);
		
		CrosslinksVsFuncStarsBiggestCluster = new HistogrammStatistik(0.00,1.00,100);
		CrosslinksVsFuncCrosslinkerBiggestCluster = new HistogrammStatistik(0.00,1.00,100);
		CrosslinksVsFuncCrosslinkerSol= new HistogrammStatistik(0.00,1.00,100);
		CrosslinksVsFuncStarsSol= new HistogrammStatistik(0.00,1.00,100);
		
		InitialFunctionalGroupCrosslinker = new Statistik();
		InitialFunctionalGroupStars = new Statistik() ;
		
		counterFiles = new int[4*NrOfStars+1];
		
		rg = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.DateiAnlegen(dirDst+"/StarPEG_CumBonds_CrosslinkerPEGConnectedGel_"+FileName+".dat", false);
		rg.setzeZeile("# t[MCS] <CumBond>  <CumBond/maxBond> d<CumBond>");
		
		BondSaver = new BFMFileSaver();
		//BondSaver.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_Bonds_HepPEGConnectedGel_"+FileName+".dat", false);
		BondSaver.DateiAnlegen(dirDst+"/StarPEG_Bonds_CrosslinkerPEGConnectedGel_"+FileName+".dat", false);
		BondSaver.setzeZeile("# t[MCS] <Single>  <Double> <Triple> <Quad> <Free> <CumBonds> Sum=1");
		
		FunktionalitaetCrosslinkerSaver = new BFMFileSaver();
		//FunktionalitaetHeparinSaver.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/HeparinFunctionality_HepPEGConnectedGel_"+FileName+".dat", false);
		FunktionalitaetCrosslinkerSaver.DateiAnlegen(dirDst+"/CrosslinkerFunctionality_CrosslinkerPEGConnectedGel_"+FileName+".dat", false);
		
		
		SaverBondsAtGamma95 = new BFMFileSaver();
		//SaverBondsAtGamma95.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung/defects_gamma_0_95conversion.dat", true);
		SaverBondsAtGamma95.DateiAnlegen(dirDst+"/defects_gamma_0_95conversion.dat", true);
		//SaverBondsAtGamma95.setzeZeile("# gamma <Single>  <Double> <Triple> <Quad>");
		
		SaverBondsAtGamma75 = new BFMFileSaver();
		//SaverBondsAtGamma75.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung/defects_gamma_0_75conversion.dat", true);
		SaverBondsAtGamma75.DateiAnlegen(dirDst+"/defects_gamma_0_75conversion.dat", true);
		//SaverBondsAtGamma75.setzeZeile("# gamma <Single>  <Double> <Triple> <Quad>");
		
		BondSaver_ExtentOfReactionHistogramm = new BFMFileSaver();
		BondSaver_ExtentOfReactionHistogramm.DateiAnlegen(dirDst+"/Bonds_PEG_SimpleDefects_CrosslinkerPEGConnectedGel_"+FileName+".dat", false);
		BondSaver_ExtentOfReactionHistogramm.setzeZeile("# p <Single>  <Double> <Triple> <Quad> <Free> <CumBonds=1>");
		
		
		
		FunktionalitaetCrosslinker = new int[5];
		
		
		SingleBondsAtGamma95 = new Statistik();
		DoubleBondsAtGamma95 = new Statistik();
		TripleBondsAtGamma95 = new Statistik();
		QuadBondsAtGamma95 = new Statistik();
		
		SingleBondsAtGamma75 = new Statistik();
		DoubleBondsAtGamma75 = new Statistik();
		TripleBondsAtGamma75 = new Statistik();
		QuadBondsAtGamma75 = new Statistik();
		
		
		bonds = new Statistik[maxframe+1];
		
		for(int i = 0; i <= maxframe; i++)
			bonds[i] = new Statistik();
		
		Singlebonds = new Statistik[maxframe+1];
		Doublebonds= new Statistik[maxframe+1];
		Triplebonds= new Statistik[maxframe+1];
		Quadbonds= new Statistik[maxframe+1];
		DenglingEnds= new Statistik[maxframe+1];
		
		DenglingEnds2= new Statistik[maxframe+1];
		
		for(int i = 0; i <= maxframe; i++)
			{
				Singlebonds[i] = new Statistik();
				Doublebonds[i] = new Statistik();
				Triplebonds[i] = new Statistik();
				Quadbonds[i] = new Statistik();
				DenglingEnds[i] = new Statistik();
				DenglingEnds2[i] = new Statistik();
				
			}
		
		durchschnittbond = new Statistik();
		
		
		
		DecimalFormat dh = new DecimalFormat("000");
		NumberFormat nf = NumberFormat.getInstance(Locale.US);
	    nf.setMaximumFractionDigits(3);
	    DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMANY);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(',');
	    DecimalFormat df =   new DecimalFormat  ( "0.00000", otherSymbols );
		//LoadFile(FileName+"__"+dh.format(11)+".bfm", 1, maxframe);
		//LoadFile(FileName+"__"+dh.format(12)+".bfm", 1, maxframe);
		//for(int i = 1; i <= 26; i++)
		for(int i = 1; i <= nrOfFiles; i+=1)//i++)
			LoadFile(FileName+"__"+dh.format(i)+".bfm", 1, maxframe);
		
		
		//for(int i = 11; i <= 25; i++)
		//	LoadFile(FileName+"__"+dh.format(i)+".bfm", 1, maxframe);
		
		//for(int i = 16; i <= 26; i++)
		//	LoadFile(FileName+"__"+dh.format(i)+".bfm", 1, maxframe);
		
		
		//LoadFile(FileName+"__"+dh.format(10)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(11)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(16)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(21)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(26)+".bfm", 1);
		
		
		//rg.setzeKommentar("c <Rg2> <(Rg2)2> dF N");
		//rg.setzeZeile((importData.NrOfMonomers*8.0/(Gitter_x*Gitter_y*Gitter_z))+" "+rg_xyz_stat.ReturnM1()+" "+rg_xyz_stat.ReturnM2()+" "+( 2.0* rg_xyz_stat.ReturnSigma()/ Math.sqrt(1.0*rg_xyz_stat.ReturnN() ))+ " " + rg_xyz_stat.ReturnN());
		for(int i=0; i <=maxframe; i++)
		rg.setzeZeile((deltaT*i) + " " + bonds[i].ReturnM1()+" "+(bonds[i].ReturnM1()/(4.0*NrOfStars))+" "+( 2.0* bonds[i].ReturnSigma()/Math.sqrt(17))+ " " + 1.0*bonds[i].ReturnN() );
		 
		rg.DateiSchliessen();
		
		for(int i=0; i <=maxframe; i++)
			BondSaver.setzeZeile((deltaT*i) + " " + Singlebonds[i].ReturnM1()+ " " + Doublebonds[i].ReturnM1()+ " " + Triplebonds[i].ReturnM1()+ " " + Quadbonds[i].ReturnM1()+ " " + DenglingEnds[i].ReturnM1() +" " +(bonds[i].ReturnM1()/(4.0*NrOfStars))+" " +(Singlebonds[i].ReturnM1()+ Doublebonds[i].ReturnM1()+ Triplebonds[i].ReturnM1()+  Quadbonds[i].ReturnM1()+ DenglingEnds[i].ReturnM1() ));
		
			//BondSaver.setzeZeile((10000*i) + " " + Singlebonds[i].ReturnM1()+ " " + Doublebonds[i].ReturnM1()+ " " + Triplebonds[i].ReturnM1()+ " " + Quadbonds[i].ReturnM1()+ " " + DenglingEnds[i].ReturnM1() +" "+DenglingEnds2[i].ReturnM1() +" " +(Singlebonds[i].ReturnM1()+ Doublebonds[i].ReturnM1()+ Triplebonds[i].ReturnM1()+  Quadbonds[i].ReturnM1()+ DenglingEnds[i].ReturnM1() ));
			 
		BondSaver.DateiSchliessen();
		
		//# p <Single>  <Double> <Triple> <Quad> <Free> <CumBonds=1>
		for(int i = 0; i < Histogramm_S0D0T0Q0.GetNrBins(); i++)
		{
			//HigherOrderDefectSaver_Histogramm.setzeZeile("# p\t<S0D0T0D0>\t<S1D0T0Q0>\t<S2D0T0Q0>\t<S0D1T0Q0>\t<S3D0T0Q0>\t<S1D1T0Q0>\t<S0D0T1Q0>\t<S4D0T0Q0>\t<S2D1T0Q0>\t<S0D2T0Q0>\t<S1D0T1Q0>\t<S0D0T0Q1>\tSum=1");
			double factor = 1./(1.0*nrOfFiles*NrOfStars);
			
			double quadBond = 1.0*factor*Histogramm_S0D0T0Q1.GetCumulativeNrInBin(i);
			double tripleBond = 0.75*factor*(Histogramm_S0D0T1Q0.GetCumulativeNrInBin(i)+Histogramm_S1D0T1Q0.GetCumulativeNrInBin(i));
			double doubleBond = 0.5*factor*(Histogramm_S0D1T0Q0.GetCumulativeNrInBin(i)+Histogramm_S1D1T0Q0.GetCumulativeNrInBin(i)+Histogramm_S2D1T0Q0.GetCumulativeNrInBin(i)+2.0*Histogramm_S0D2T0Q0.GetCumulativeNrInBin(i));
			double singleBond = 0.25*factor*(Histogramm_S1D0T0Q0.GetCumulativeNrInBin(i)+2.0*Histogramm_S2D0T0Q0.GetCumulativeNrInBin(i)+3.*Histogramm_S3D0T0Q0.GetCumulativeNrInBin(i)+Histogramm_S1D1T0Q0.GetCumulativeNrInBin(i)+4.*Histogramm_S4D0T0Q0.GetCumulativeNrInBin(i)+2.*Histogramm_S2D1T0Q0.GetCumulativeNrInBin(i)+Histogramm_S1D0T1Q0.GetCumulativeNrInBin(i));
			
			double freeBond = 1.-(singleBond+doubleBond+tripleBond+quadBond);
			double cumBond = (singleBond+doubleBond+tripleBond+quadBond);
			
			// sum = 1 -> bonds = extentofReaction -> counting correct
			//double Sum = 1.0 + Histogramm_S0D0T0Q0.GetRangeInBin(i)-0.25*(factor*Histogramm_S1D0T0Q0.GetCumulativeNrInBin(i)+2.*factor*Histogramm_S2D0T0Q0.GetCumulativeNrInBin(i)+2.*factor*Histogramm_S0D1T0Q0.GetCumulativeNrInBin(i)+3.*factor*Histogramm_S3D0T0Q0.GetCumulativeNrInBin(i)+3.*factor*Histogramm_S1D1T0Q0.GetCumulativeNrInBin(i)+3.*factor*Histogramm_S0D0T1Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S4D0T0Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S2D1T0Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S0D2T0Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S1D0T1Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S0D0T0Q1.GetCumulativeNrInBin(i));
			double Sum = freeBond+cumBond;
			
			if(counterFiles[(int) Math.floor(Histogramm_S0D0T0Q0.GetRangeInBin(i)*4*NrOfStars)] == nrOfFiles)
				BondSaver_ExtentOfReactionHistogramm.setzeZeile(df.format(Histogramm_S0D0T0Q0.GetRangeInBin(i))+"\t"+df.format(singleBond)+"\t"+df.format(doubleBond)+"\t"+df.format(tripleBond)+"\t"+df.format(quadBond)+"\t"+df.format(freeBond)+"\t"+df.format(cumBond)+"\t"+df.format(Sum));
			
				//BondSaver_ExtentOfReactionHistogramm.setzeZeile(df.format(Histogramm_S0D0T0Q0.GetRangeInBin(i))+"\t"+df.format(1.0-Histogramm_S0D0T0Q0.GetCumulativeNrInBinNormiert(i))+"\t"+df.format(factor*Histogramm_S1D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S2D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D1T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S3D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S1D1T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D0T1Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S4D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S2D1T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D2T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S1D0T1Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D0T0Q1.GetCumulativeNrInBin(i))+"\t"+df.format(Sum));
			
		}
	
		BondSaver_ExtentOfReactionHistogramm.DateiSchliessen();
		
		
		SaverBondsAtGamma95.setzeZeile((gamma.replace('_', '.')) + " " + SingleBondsAtGamma95.ReturnM1()+ " " + DoubleBondsAtGamma95.ReturnM1()+ " " + TripleBondsAtGamma95.ReturnM1()+ " " + QuadBondsAtGamma95.ReturnM1());
		SaverBondsAtGamma95.DateiSchliessen();
		
		SaverBondsAtGamma75.setzeZeile((gamma.replace('_', '.')) + " " + SingleBondsAtGamma75.ReturnM1()+ " " + DoubleBondsAtGamma75.ReturnM1()+ " " + TripleBondsAtGamma75.ReturnM1()+ " " + QuadBondsAtGamma75.ReturnM1());
		SaverBondsAtGamma75.DateiSchliessen();
		
		
		//BiggestMonomerCluster
		BFMFileSaver ClusterSaver = new BFMFileSaver();
		ClusterSaver.DateiAnlegen(dirDst+"/Percolation_BMC_Distribution_CrosslinkerPEGConnectedGel_"+FileName+".dat", false);
		ClusterSaver.setzeZeile("# crosslinks(all) <BMC Monomers/AllMonomers>");
		
		
		for(int i = 0; i < CrosslinksVsAllMonomersInCluster.GetNrBins(); i++)
		{
			if(counterFiles[(int) Math.floor(CrosslinksVsAllMonomersInCluster.GetRangeInBin(i)*4*NrOfStars)] == nrOfFiles)
			ClusterSaver.setzeZeile(CrosslinksVsAllMonomersInCluster.GetRangeInBin(i)+" "+CrosslinksVsAllMonomersInCluster.GetAverageInBin(i));
		}
		ClusterSaver.DateiSchliessen();
		
		//StarsInBiggestMonomerCluster
		BFMFileSaver NrOfStarsSaver = new BFMFileSaver();
		NrOfStarsSaver.DateiAnlegen(dirDst+"/Percolation_PEG_BMC_Distribution_CrosslinkerPEGConnectedGel_"+FileName+".dat", false);
		NrOfStarsSaver.setzeZeile("# Init Nr Crosslinker: " + NrOfCrosslinker);
		NrOfStarsSaver.setzeZeile("# Init Nr PEG: " + NrOfStars);
		NrOfStarsSaver.setzeZeile("# Init Fct Group Crosslinker: " + InitialFunctionalGroupCrosslinker.ReturnM1());
		NrOfStarsSaver.setzeZeile("# Init Fct Group Stars: " + InitialFunctionalGroupStars.ReturnM1());
		NrOfStarsSaver.setzeZeile("# crosslinks(all) <NrStars BMC> <NrStars BMC/NrStars> <NrStars Sol> <NrStars Sol/NrStars>");
		
		for(int i = 0; i < CrosslinksVsNrStarsBiggestCluster.GetNrBins(); i++)
		{
			if(counterFiles[(int) Math.floor(CrosslinksVsNrStarsBiggestCluster.GetRangeInBin(i)*4*NrOfStars)] == nrOfFiles)
				NrOfStarsSaver.setzeZeile(CrosslinksVsNrStarsBiggestCluster.GetRangeInBin(i)+" "+CrosslinksVsNrStarsBiggestCluster.GetAverageInBin(i)+" "+(CrosslinksVsNrStarsBiggestCluster.GetAverageInBin(i)/NrOfStars)+" "+(NrOfStars-CrosslinksVsNrStarsBiggestCluster.GetAverageInBin(i))+" "+(1.0-(CrosslinksVsNrStarsBiggestCluster.GetAverageInBin(i)/NrOfStars)));
		}
		NrOfStarsSaver.DateiSchliessen();

		
		BFMFileSaver NrOfCrosslinkerSaver = new BFMFileSaver();
		NrOfCrosslinkerSaver.DateiAnlegen(dirDst+"/Percolation_Crosslinker_BMC_Distribution_CrosslinkerPEGConnectedGel_"+FileName+".dat", false);
		NrOfCrosslinkerSaver.setzeZeile("# Init Nr Crosslinker: " + NrOfCrosslinker);
		NrOfCrosslinkerSaver.setzeZeile("# Init Nr PEG: " + NrOfStars);
		NrOfCrosslinkerSaver.setzeZeile("# Init Fct Group HEP: " + InitialFunctionalGroupCrosslinker.ReturnM1());
		NrOfCrosslinkerSaver.setzeZeile("# Init Fct Group Stars: " + InitialFunctionalGroupStars.ReturnM1());
		NrOfCrosslinkerSaver.setzeZeile("# crosslinks(all) <NrCrosslinker BMC> <NrCrosslinker BMC/NrCrosslinker> <NrCrosslinker Sol> <NrCrosslinker Sol/NrCrosslinker>");;
		
		for(int i = 0; i < CrosslinksVsNrCrosslinkerBiggestCluster.GetNrBins(); i++)
		{
			if(counterFiles[(int) Math.floor(CrosslinksVsNrCrosslinkerBiggestCluster.GetRangeInBin(i)*4*NrOfStars)] == nrOfFiles)
				NrOfCrosslinkerSaver.setzeZeile(CrosslinksVsNrCrosslinkerBiggestCluster.GetRangeInBin(i)+" "+CrosslinksVsNrCrosslinkerBiggestCluster.GetAverageInBin(i)+" "+(CrosslinksVsNrCrosslinkerBiggestCluster.GetAverageInBin(i)/NrOfCrosslinker)+" "+(NrOfCrosslinker-CrosslinksVsNrCrosslinkerBiggestCluster.GetAverageInBin(i))+" "+(1.0-(CrosslinksVsNrCrosslinkerBiggestCluster.GetAverageInBin(i)/NrOfCrosslinker)));
		}
		NrOfCrosslinkerSaver.DateiSchliessen();
	
		//output functionality biggest Cluster
		BFMFileSaver FuncHEPBiggestClusterSaver = new BFMFileSaver();
		FuncHEPBiggestClusterSaver.DateiAnlegen(dirDst+"/Percolation_Crosslinker_BMC_Functionality_CrosslinkerPEGConnectedGel_"+FileName+".dat", false);
		FuncHEPBiggestClusterSaver.setzeZeile("# Init Fct Group Crosslinker: " + InitialFunctionalGroupCrosslinker.ReturnM1());
		FuncHEPBiggestClusterSaver.setzeZeile("# crosslinks(all) <FunctionalGroup Crosslinker BMC> <Functional Group Crosslinker BMC/InitFctGroup> <FunctionalGroup Crosslinker Sol> <Functional Group Crosslinker Sol/InitFctGroup>");;
		
		for(int i = 0; i < CrosslinksVsFuncCrosslinkerBiggestCluster.GetNrBins(); i++)
		{
			FuncHEPBiggestClusterSaver.setzeZeile(CrosslinksVsFuncCrosslinkerBiggestCluster.GetRangeInBin(i)+" "+CrosslinksVsFuncCrosslinkerBiggestCluster.GetAverageInBin(i)+" "+(CrosslinksVsFuncCrosslinkerBiggestCluster.GetAverageInBin(i)/InitialFunctionalGroupCrosslinker.ReturnM1())+" "+CrosslinksVsFuncCrosslinkerSol.GetAverageInBin(i)+" "+(CrosslinksVsFuncCrosslinkerSol.GetAverageInBin(i)/InitialFunctionalGroupCrosslinker.ReturnM1()));
		}
		FuncHEPBiggestClusterSaver.DateiSchliessen();
		
		BFMFileSaver FuncStarsBiggestClusterSaver = new BFMFileSaver();
		FuncStarsBiggestClusterSaver.DateiAnlegen(dirDst+"/Percolation_PEG_BMC_Functionality_CrosslinkerPEGConnectedGel_"+FileName+".dat", false);
		FuncStarsBiggestClusterSaver.setzeZeile("# Init Fct Group Stars: " + InitialFunctionalGroupStars.ReturnM1());
		FuncStarsBiggestClusterSaver.setzeZeile("# crosslinks(all) <FunctionalGroup Stars BMC> <Functional Group Stars BMC/InitFctGroup>  <FunctionalGroup Stars Sol> <FunctionalGroup Stars Sol/InitFctGroup>");;
		
		for(int i = 0; i < CrosslinksVsFuncStarsBiggestCluster.GetNrBins(); i++)
		{
			FuncStarsBiggestClusterSaver.setzeZeile(CrosslinksVsFuncStarsBiggestCluster.GetRangeInBin(i)+" "+CrosslinksVsFuncStarsBiggestCluster.GetAverageInBin(i)+" "+(CrosslinksVsFuncStarsBiggestCluster.GetAverageInBin(i)/InitialFunctionalGroupStars.ReturnM1())+" "+CrosslinksVsFuncStarsSol.GetAverageInBin(i)+" "+(CrosslinksVsFuncStarsSol.GetAverageInBin(i)/InitialFunctionalGroupStars.ReturnM1()));
		}
		FuncStarsBiggestClusterSaver.DateiSchliessen();
		
		
		
		int counterFuncCrosslinker=0;
		for(int i=0; i <= 4; i++)
			counterFuncCrosslinker +=FunktionalitaetCrosslinker[i];
		
		double DurchschnittsfunktionalitaetCrosslinker = 0.0;
		for(int i=0; i <= 4; i++)
			DurchschnittsfunktionalitaetCrosslinker += i*(FunktionalitaetCrosslinker[i]/(1.0*counterFuncCrosslinker));
		
		FunktionalitaetCrosslinkerSaver.setzeZeile("# total sample: "+counterFuncCrosslinker +"   ;  average functionality: "+DurchschnittsfunktionalitaetCrosslinker);
		
		FunktionalitaetCrosslinkerSaver.setzeZeile("# f <relative occurence> <absolute occurence> total: "+counterFuncCrosslinker);
		
		
		for(int i=0; i <=4; i++)
			FunktionalitaetCrosslinkerSaver.setzeZeile(i+ " " + (FunktionalitaetCrosslinker[i]/(1.0*counterFuncCrosslinker))+ " " + (FunktionalitaetCrosslinker[i]) );
			 
		FunktionalitaetCrosslinkerSaver.DateiSchliessen();
		
		double dummyFunc = 0.0;
		for(int i=0; i <= 4; i++)
			dummyFunc += (FunktionalitaetCrosslinker[i]/(1.0*counterFuncCrosslinker));
		
		System.out.println("SummeFunktionalitaet: " +dummyFunc +"  == 1.0");
		
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1());
		
		
		BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(dirDst+"/Defects_Gamma_"+gamma+".batch", false);
		xmgrace.setzeZeile("# t[MCS] <CumBond>  <CumBond/maxBond> d<CumBond>");
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ NXY \""+dirDst+"StarPEG_Bonds_CrosslinkerPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace.setzeZeile(" world xmin 0");
	    xmgrace.setzeZeile(" world xmax "+importData.MCSTime);//225000");
	    xmgrace.setzeZeile(" world ymin 0");
	    xmgrace.setzeZeile(" world ymax 1.0");
	    xmgrace.setzeZeile(" xaxis label \"time [MCS]\"");
	    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace.setzeZeile(" xaxis tick major "+(maxframe-1)*deltaT/4);//50000");
	    //xmgrace.setzeZeile(" xaxis tick minor 10000");
	    xmgrace.setzeZeile(" yaxis label \"relative frequency\"");
	    xmgrace.setzeZeile(" yaxis tick major 0.2");
	    xmgrace.setzeZeile(" yaxis tick minor 0.1");

	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 legend \"SingleBonds\"");

	    xmgrace.setzeZeile(" s1 line color 2");
	    xmgrace.setzeZeile(" s1 line linestyle 1");
	    xmgrace.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace.setzeZeile(" s1 legend \"DoubleBonds\"");
		 		
	    xmgrace.setzeZeile(" s2 line color 3");
	    xmgrace.setzeZeile(" s2 line linestyle 1");
	    xmgrace.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace.setzeZeile(" s2 legend \"TripleBonds\"");

	    xmgrace.setzeZeile(" s3 line color 4");
	    xmgrace.setzeZeile(" s3 line linestyle 1");
	    xmgrace.setzeZeile(" s3 line linewidth 1.5");
	    xmgrace.setzeZeile(" s3 legend \"QuadBonds\"");

	    xmgrace.setzeZeile(" s4 line color 11");
	    xmgrace.setzeZeile(" s4 line linestyle 1");
	    xmgrace.setzeZeile(" s4 line linewidth 1.5");
	    xmgrace.setzeZeile(" s4 legend \"FreeEnds\"");

	    xmgrace.setzeZeile(" s5 line color 10");
	    xmgrace.setzeZeile(" s5 line linestyle 1");
	    xmgrace.setzeZeile(" s5 line linewidth 1.5");
	    xmgrace.setzeZeile(" s5 legend \"CumBonds\"");

	    xmgrace.setzeZeile(" subtitle \""+Experiment+"; N\\sCrosslinker\\N="+NrOfCrosslinker+"; f\\sCross\\N="+CrosslinkerFunctionality+"; N\\sStars\\N="+NrOfStars+"; N\\sStars\\N\\SArm\\N="+NrOfMonomersPerStarArm+"; \\f{Symbol}a\\f{}="+nf.format((CrosslinkerFunctionality*NrOfCrosslinker/(4.0*NrOfStars)))+"; \"");

	    xmgrace.setzeZeile(" SAVEALL \""+dirDst+"AddedBonds_CrosslinkerPEG_"+FileName+".agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+dirDst+"AddedBonds_CrosslinkerPEG_"+FileName+".ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	    
	    try {
		      Runtime.getRuntime().exec("xmgrace -batch "+dirDst+"/Defects_Gamma_"+gamma+".batch -nosafe -hardcopy");
		    } catch (Exception e) {
		      System.err.println(e.toString());
		    }
		    
		    BFMFileSaver xmgrace3 = new BFMFileSaver();
			xmgrace3.DateiAnlegen(dirDst+"/Bonds_Distribution_SimpleDefects_CrosslinkerPEGConnectedGel_"+FileName+".batch", false);
			xmgrace3.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
			xmgrace3.setzeZeile("page size 792, 612");
			xmgrace3.setzeZeile("page scroll 5%");
			xmgrace3.setzeZeile("page inout 5%");
			xmgrace3.setzeZeile("link page off");
			xmgrace3.setzeZeile("VIEW 0.150000, 0.150000, 1.150000, 0.850000");
			xmgrace3.setzeZeile("FOCUS G0");
			xmgrace3.setzeZeile(" AUTOSCALE ONREAD None");
			xmgrace3.setzeZeile("READ NXY \""+dirDst+"Bonds_PEG_SimpleDefects_CrosslinkerPEGConnectedGel_"+FileName+".dat\"");
			xmgrace3.setzeZeile("");
			xmgrace3.setzeZeile(" LEGEND 0.525, 0.8");
			xmgrace3.setzeZeile("");
			xmgrace3.setzeZeile(" world xmin 0");
			xmgrace3.setzeZeile(" world xmax 1.0");//225000");
			xmgrace3.setzeZeile(" world ymin 0");
			xmgrace3.setzeZeile(" world ymax 1.0");
			xmgrace3.setzeZeile(" xaxis label \"extent of reaction p\"");
			xmgrace3.setzeZeile(" xaxis TICK MAJOR on");
			xmgrace3.setzeZeile(" xaxis TICK MINOR on");
			xmgrace3.setzeZeile(" xaxis tick major 0.1");//50000");
			xmgrace3.setzeZeile(" xaxis tick minor 0.05");
			
			xmgrace3.setzeZeile(" yaxis label \"relative frequency\"");
			xmgrace3.setzeZeile(" yaxis ticklabel place both");
			xmgrace3.setzeZeile(" yaxis tick major 0.1");
			xmgrace3.setzeZeile(" yaxis tick minor 0.05");

			xmgrace3.setzeZeile(" s0 line color 1");
			xmgrace3.setzeZeile(" s0 line linestyle 1");
			xmgrace3.setzeZeile(" s0 line linewidth 1.5");
			xmgrace3.setzeZeile(" s0 legend \"SingleBonds\"");

			xmgrace3.setzeZeile(" s1 line color 2");
			xmgrace3.setzeZeile(" s1 line linestyle 1");
			xmgrace3.setzeZeile(" s1 line linewidth 1.5");
			xmgrace3.setzeZeile(" s1 legend \"DoubleBonds\"");
				 		
			xmgrace3.setzeZeile(" s2 line color 3");
			xmgrace3.setzeZeile(" s2 line linestyle 1");
			xmgrace3.setzeZeile(" s2 line linewidth 1.5");
			xmgrace3.setzeZeile(" s2 legend \"TripleBonds\"");

			xmgrace3.setzeZeile(" s3 line color 4");
			xmgrace3.setzeZeile(" s3 line linestyle 1");
			xmgrace3.setzeZeile(" s3 line linewidth 1.5");
			xmgrace3.setzeZeile(" s3 legend \"QuadBonds\"");

			xmgrace3.setzeZeile(" s4 line color 11");
			xmgrace3.setzeZeile(" s4 line linestyle 1");
			xmgrace3.setzeZeile(" s4 line linewidth 1.5");
			xmgrace3.setzeZeile(" s4 legend \"FreeEnds\"");

			xmgrace3.setzeZeile(" s5 line color 10");
			xmgrace3.setzeZeile(" s5 line linestyle 1");
			xmgrace3.setzeZeile(" s5 line linewidth 1.5");
			xmgrace3.setzeZeile(" s5 legend \"CumBonds\"");

			xmgrace3.setzeZeile(" subtitle \""+Experiment+"; N\\sCrosslinker\\N="+NrOfCrosslinker+"; f\\sCross\\N="+CrosslinkerFunctionality+"; N\\sStars\\N="+NrOfStars+"; N\\sStars\\N\\SArm\\N="+NrOfMonomersPerStarArm+"; \\f{Symbol}a\\f{}="+nf.format((CrosslinkerFunctionality*NrOfCrosslinker/(4.0*NrOfStars)))+"; \"");

			//xmgrace3.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\sHEP,init\\N=28; c="+dc.format(concentration)+"\"");
			xmgrace3.setzeZeile("");
			xmgrace3.setzeZeile(" PAGE SIZE 842, 595"); //A4
			xmgrace3.setzeZeile("");
			xmgrace3.setzeZeile(" SAVEALL \""+dirDst+"Bonds_Distribution_SimpleDefects_CrosslinkerPEGConnectedGel_"+FileName+".agr\"");

			xmgrace3.setzeZeile(" PRINT TO \""+dirDst+"Bonds_Distribution_SimpleDefects_CrosslinkerPEGConnectedGel_"+FileName+".ps\"");
			xmgrace3.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
			xmgrace3.setzeZeile("PRINT");
				
			xmgrace3.DateiSchliessen();
			    
			    try {
			    	  System.out.println("xmgrace -batch "+dirDst+"/Bonds_Distribution_SimpleDefects_CrosslinkerPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
			          Runtime.getRuntime().exec("xmgrace -batch "+dirDst+"/Bonds_Distribution_SimpleDefects_CrosslinkerPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
				    } catch (Exception e) {
				      System.err.println(e.toString());
				    }
				    
			BFMFileSaver xmgracePercBMC = new BFMFileSaver();
			xmgracePercBMC.DateiAnlegen(dirDst+"/Percolation_Distribution_BMC_CrosslinkerPEGConnectedGel_"+FileName+".batch", false);
			xmgracePercBMC.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
			xmgracePercBMC.setzeZeile("FOCUS G0");
			xmgracePercBMC.setzeZeile(" AUTOSCALE ONREAD None");
			xmgracePercBMC.setzeZeile("READ BLOCK \""+dirDst+"Percolation_Crosslinker_BMC_Distribution_CrosslinkerPEGConnectedGel_"+FileName+".dat\"");
			xmgracePercBMC.setzeZeile("BLOCK xy \"1:3\"");
			xmgracePercBMC.setzeZeile("BLOCK xy \"1:5\"");
			
			xmgracePercBMC.setzeZeile("READ BLOCK \""+dirDst+"Percolation_PEG_BMC_Distribution_CrosslinkerPEGConnectedGel_"+FileName+".dat\"");
			xmgracePercBMC.setzeZeile("BLOCK xy \"1:3\"");
			xmgracePercBMC.setzeZeile("BLOCK xy \"1:5\"");
			
			xmgracePercBMC.setzeZeile("READ BLOCK \""+dirDst+"Percolation_BMC_Distribution_CrosslinkerPEGConnectedGel_"+FileName+".dat\"");
			xmgracePercBMC.setzeZeile("BLOCK xy \"1:2\"");
			xmgracePercBMC.setzeZeile(" world xmin 0");
			xmgracePercBMC.setzeZeile(" world xmax 1.0");
			xmgracePercBMC.setzeZeile(" world ymin 0");
			xmgracePercBMC.setzeZeile(" world ymax 1.0");
			xmgracePercBMC.setzeZeile(" xaxis label \"extent of reaction p\"");
			xmgracePercBMC.setzeZeile(" xaxis TICK MAJOR on");
			xmgracePercBMC.setzeZeile(" xaxis TICK MINOR on");
			xmgracePercBMC.setzeZeile(" xaxis tick major 0.1");//50000");
			xmgracePercBMC.setzeZeile(" xaxis tick minor 0.05");
			xmgracePercBMC.setzeZeile(" yaxis label \"relative frequency\"");
			xmgracePercBMC.setzeZeile(" yaxis tick major 0.1");
			xmgracePercBMC.setzeZeile(" yaxis tick minor 0.05");

			xmgracePercBMC.setzeZeile(" s0 line color 1");
			xmgracePercBMC.setzeZeile(" s0 line linestyle 1");
			xmgracePercBMC.setzeZeile(" s0 line linewidth 1.5");
			xmgracePercBMC.setzeZeile(" s0 legend \"ClusterCrosslinker (BMC)\"");

			xmgracePercBMC.setzeZeile(" s1 line color 2");
			xmgracePercBMC.setzeZeile(" s1 line linestyle 1");
			xmgracePercBMC.setzeZeile(" s1 line linewidth 1.5");
			xmgracePercBMC.setzeZeile(" s1 legend \"SolCrosslinker (BMC)\"");
				    
			xmgracePercBMC.setzeZeile(" s2 line color 3");
			xmgracePercBMC.setzeZeile(" s2 line linestyle 1");
			xmgracePercBMC.setzeZeile(" s2 line linewidth 1.5");
			xmgracePercBMC.setzeZeile(" s2 legend \"ClusterStars (BMC)\"");

			xmgracePercBMC.setzeZeile(" s3 line color 4");
			xmgracePercBMC.setzeZeile(" s3 line linestyle 1");
			xmgracePercBMC.setzeZeile(" s3 line linewidth 1.5");
			xmgracePercBMC.setzeZeile(" s3 legend \"SolStars (BMC)\"");
				    
			xmgracePercBMC.setzeZeile(" s4 line color 8");
			xmgracePercBMC.setzeZeile(" s4 line linestyle 1");
			xmgracePercBMC.setzeZeile(" s4 line linewidth 1.5");
			xmgracePercBMC.setzeZeile(" s4 legend \"Monomers in Cluster (BMC)\"");
				    
				    
				    
			xmgracePercBMC.setzeZeile(" LEGEND 0.15, 0.6");
			//xmgracePercBMC.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\sHEP,init\\N="+InitialFunctionalGroupHeparin.ReturnM1()+"\"");
			xmgracePercBMC.setzeZeile(" subtitle \""+Experiment+"; N\\sCrosslinker\\N="+NrOfCrosslinker+"; f\\sCross\\N="+CrosslinkerFunctionality+"; N\\sStars\\N="+NrOfStars+"; N\\sStars\\N\\SArm\\N="+NrOfMonomersPerStarArm+"; \\f{Symbol}a\\f{}="+nf.format((CrosslinkerFunctionality*NrOfCrosslinker/(4.0*NrOfStars)))+"; \"");

			xmgracePercBMC.setzeZeile("");
			xmgracePercBMC.setzeZeile(" PAGE SIZE 842, 595"); //A4
			xmgracePercBMC.setzeZeile("");
					
			xmgracePercBMC.setzeZeile(" SAVEALL \""+dirDst+"Percolation_Distribution_BMC_CrosslinkerPEGConnectedGel_"+FileName+".agr\"");

			xmgracePercBMC.setzeZeile(" PRINT TO \""+dirDst+"Percolation_Distribution_BMC_CrosslinkerPEGConnectedGel_"+FileName+".ps\"");
			xmgracePercBMC.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
			xmgracePercBMC.setzeZeile("PRINT");
					
				    xmgracePercBMC.DateiSchliessen();
				   
				    try {
				    	
				    	  System.out.println("xmgrace -batch "+dirDst+"/Percolation_Distribution_BMC_CrosslinkerPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
					      Runtime.getRuntime().exec("xmgrace -batch "+dirDst+"/Percolation_Distribution_BMC_CrosslinkerPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
					    } catch (Exception e) {
					      System.err.println(e.toString());
		}
		
			BFMFileSaver xmgraceFunc = new BFMFileSaver();
			xmgraceFunc.DateiAnlegen(dirDst+"/Percolation_Functionality_BMC_CrosslinkerPEGConnectedGel_"+FileName+".batch", false);
			xmgraceFunc.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
			xmgraceFunc.setzeZeile("FOCUS G0");
			xmgraceFunc.setzeZeile(" AUTOSCALE ONREAD None");
			xmgraceFunc.setzeZeile("READ BLOCK \""+dirDst+"Percolation_Crosslinker_BMC_Functionality_CrosslinkerPEGConnectedGel_"+FileName+".dat\"");
			xmgraceFunc.setzeZeile("BLOCK xy \"1:3\"");
					    //xmgraceFunc.setzeZeile("READ BLOCK \""+dstDir+"Percolation_FuncHEP_Sol_AllMonomers_HepPEGConnectedGel_"+FileName+".dat\"");
			xmgraceFunc.setzeZeile("BLOCK xy \"1:5\"");
			xmgraceFunc.setzeZeile("READ BLOCK \""+dirDst+"Percolation_PEG_BMC_Functionality_CrosslinkerPEGConnectedGel_"+FileName+".dat\"");
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
			xmgraceFunc.setzeZeile(" s0 legend \"ClusterCrosslinker\"");

			xmgraceFunc.setzeZeile(" s1 line color 2");
			xmgraceFunc.setzeZeile(" s1 line linestyle 1");
			xmgraceFunc.setzeZeile(" s1 line linewidth 1.5");
			xmgraceFunc.setzeZeile(" s1 legend \"SolCrosslinker\"");
					    
			xmgraceFunc.setzeZeile(" s2 line color 3");
			xmgraceFunc.setzeZeile(" s2 line linestyle 1");
			xmgraceFunc.setzeZeile(" s2 line linewidth 1.5");
			xmgraceFunc.setzeZeile(" s2 legend \"ClusterPEG\"");

			xmgraceFunc.setzeZeile(" s3 line color 4");
			xmgraceFunc.setzeZeile(" s3 line linestyle 1");
			xmgraceFunc.setzeZeile(" s3 line linewidth 1.5");
			xmgraceFunc.setzeZeile(" s3 legend \"SolPEG\"");

			//xmgraceFunc.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\sHEP,init\\N="+InitialFunctionalGroupHeparin.ReturnM1()+"\"");
			
			xmgraceFunc.setzeZeile(" subtitle \""+Experiment+"; N\\sCrosslinker\\N="+NrOfCrosslinker+"; f\\sCross\\N="+CrosslinkerFunctionality+"; N\\sStars\\N="+NrOfStars+"; N\\sStars\\N\\SArm\\N="+NrOfMonomersPerStarArm+"; \\f{Symbol}a\\f{}="+nf.format((CrosslinkerFunctionality*NrOfCrosslinker/(4.0*NrOfStars)))+"; \"");

			xmgraceFunc.setzeZeile("");
			xmgraceFunc.setzeZeile(" PAGE SIZE 842, 595"); //A4
			xmgraceFunc.setzeZeile("");
			
			xmgraceFunc.setzeZeile(" SAVEALL \""+dirDst+"Percolation_Functionality_BMC_CrosslinkerPEGConnectedGel_"+FileName+".agr\"");

			xmgraceFunc.setzeZeile(" PRINT TO \""+dirDst+"Percolation_Functionality_BMC_CrosslinkerPEGConnectedGel_"+FileName+".ps\"");
			xmgraceFunc.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
			xmgraceFunc.setzeZeile("PRINT");
						
			xmgraceFunc.DateiSchliessen();
					    
			try {
					    	
				System.out.println("xmgrace -batch "+dirDst+"/Percolation_Functionality_BMC_CrosslinkerPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
				Runtime.getRuntime().exec("xmgrace -batch "+dirDst+"/Percolation_Functionality_BMC_CrosslinkerPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
				} catch (Exception e) {
				System.err.println(e.toString());
			}		

					   
		/*System.out.println();
		int pegs = 630;
		for(int u = 0; u < NrOfHeparin; u++)
		{
			if(HeparinSterneVerknuepfung[u][pegs-1] != 0)
				System.out.print((u+1) + "["+HeparinSterneVerknuepfung[u][pegs-1]+"] ");
		}*/
	}
	
	protected void LoadFile(String file, int startframe, int maxframe)
	{
		//FileName = file;
		System.out.println("file : " +file );
		System.out.println("dir : " + FileDirectory);
		System.out.println("lade System");
		addedBondsDuringSimulation = 0;
		
		LadeSystem(FileDirectory, file);	
		
		System.out.println("addedBondsDuringSimulation : " +addedBondsDuringSimulation );
		
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(FileDirectory+file);
		  
		 importData.GetFrameOfSimulation(currentFrame);
		  
		  
			
		  importData.CloseSimulationFile();
		  
		  
		  
		  importData.OpenSimulationFile(FileDirectory+file);
			
		  //addedBondsDuringSimulation = 0;
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
			
			durchschnittbond.AddValue(importData.additionalbonds.size());
		   
			
			currentFrame = z;
			
			if( (currentFrame+skipFrames) >= NrofFrames)
				currentFrame =  NrofFrames;
			
			
			
			for(int cross = 0; cross < NrOfCrosslinker; cross++)
			{
				int counterFunktionalitaet = 0;
				for(int u = 0; u < NrOfStars; u++)
				{
					if(CrosslinkerSterneVerknuepfung[cross][u] != 0)
						counterFunktionalitaet+=CrosslinkerSterneVerknuepfung[cross][u];
				}
				
				if(counterFunktionalitaet == 0)
					FunktionalitaetCrosslinker[0]++;
				else
					FunktionalitaetCrosslinker[counterFunktionalitaet]++;
			}
			
			
			
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 6)
		{
			System.out.println("Berechnung Bindungsstatistik von Crosslinker+PEG");
			System.out.println("USAGE: dirSrc/ HydrogelStar_Star_[xxx]_NStar_[yyy]__Cross_[zzz]_F_[a]__NoPerXYZ64_[type](__xxx.bfm) StringAlpha dirDst/ StringExperiment Files");
		}
		else new Auswertung_Stars_CrossStars_VerknuepfungDefekte(args[0], args[1], args[2] ,args[3], args[4], Integer.parseInt(args[5]));//,args[1],args[2]);
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
		
		Attributesystem = null;
		Attributesystem = new int[MONOMERZAHL];
		
		dumpsystem = null;
		dumpsystem = new int[MONOMERZAHL];
	
		boolean periodRB_x = importData.periodic_x;
		boolean periodRB_y = importData.periodic_y;
		boolean periodRB_z = importData.periodic_z;
		
		NrOfStars = importData.NrOfStars;
		NrOfMonomersPerStarArm = importData.NrOfMonomersPerStarArm;
		NrOfCrosslinker = importData.NrOfCrosslinker;
		CrosslinkerFunctionality = importData.CrosslinkerFunctionality;
		
		System.out.println("Stars: " + NrOfStars + "    ArmLength: "+ NrOfMonomersPerStarArm + "    Crosslinker:"+NrOfCrosslinker + "    CrossFunc: "+ CrosslinkerFunctionality);
		
		//NrOfHeparin = importData.NrOfHeparin;
		
		CrosslinkerSterneVerknuepfung = null;
		CrosslinkerSterneVerknuepfung = new int[NrOfCrosslinker][NrOfStars];
		
		PEGBonds = new int[NrOfStars];
		
		PEGBondsDifferentHEP = null;
		PEGBondsDifferentHEP= new int[NrOfStars];
		
		//System.out.println("Stars: " + NrOfStars + "    ArmLength: "+ NrOfMonomersPerStarArm + "    Heparin:"+NrOfHeparin);
		boolean periodRB = false;
		
		if ((periodRB_x == true) || (periodRB_y == true) || (periodRB_z == true))
			periodRB = true;
		
		
		//System.arraycopy(importData.Polymersystem,0,Polymersystem,0, Polymersystem.length);
		
		System.arraycopy(importData.Attributes,0,Attributesystem,0, Attributesystem.length);
		
		//attributes: 1 -> HEP-COOH,  2 -> PEG-NH2
		int counterStarsGroup = 0;
		int counterCrosslinkerGroup = 0;
		
		for (int k= 1; k <= NrOfStars; k++)
			for (int i= ((4*NrOfMonomersPerStarArm + 1)*(k-1) +1); i <= ( 4*NrOfMonomersPerStarArm + 1)*k; i++)
				if(Attributesystem[i]==2)
					counterStarsGroup++;
			
		for (int k= ((4*NrOfMonomersPerStarArm + 1)*NrOfStars+1); k <= ((4*NrOfMonomersPerStarArm + 1)*NrOfStars+NrOfCrosslinker); k++)
			if(Attributesystem[k]==1)
			{
				counterCrosslinkerGroup+=CrosslinkerFunctionality;
				Attributesystem[k]=CrosslinkerFunctionality;
			}
		
		
		InitialFunctionalGroupCrosslinker.AddValue((1.0*counterCrosslinkerGroup)/NrOfCrosslinker);
		InitialFunctionalGroupStars.AddValue((1.0*counterStarsGroup)/NrOfStars);
		
		System.out.println("Stars: " + NrOfStars + "    ArmLength: "+ NrOfMonomersPerStarArm + "    Heparin:"+NrOfCrosslinker + "   InitAverageFuncHEP:"+((1.0*counterCrosslinkerGroup)/NrOfCrosslinker) + "   InitAverageFuncStars:"+ ((1.0*counterStarsGroup)/NrOfStars));
		
		
		InitArrays();
		
		
		NrofFrames = importData.GetNrOfFrames();
	
		String zutrt = 	FileName.substring(0, FileName.length()-4);

		System.out.println("String : " + zutrt);
		
		//checking and adding maybe initial bonds
		for(int it = 0; it < importData.initialbonds.size(); it++)
		{
			resetArrays();
			
			long bondobj = importData.initialbonds.get(it);
			//System.out.println(it + " bond " + bondobj);
			
			int a = getMono1Nr(bondobj);
			int b = getMono2Nr(bondobj);
			
			int PEGnr=0;
			int Crosslinkernr=0;
			
			
			
			
			if((a>NrOfStars*(4*NrOfMonomersPerStarArm+1)) && (b <= NrOfStars*(4*NrOfMonomersPerStarArm+1))) //a=Crosslinker, b=Peg-Stern
			{
				CrosslinkerSterneVerknuepfung[(a-NrOfStars*(4*NrOfMonomersPerStarArm+1)-1)][(b-1)/(4*NrOfMonomersPerStarArm+1)]++;
				PEGBonds[(b-1)/(4*NrOfMonomersPerStarArm+1)]++;
				
				PEGnr=(b-1)/(4*NrOfMonomersPerStarArm+1);
				Crosslinkernr=(a-NrOfStars*(4*NrOfMonomersPerStarArm+1)-1);
				
				if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==1)
					PEGBondsDifferentHEP[PEGnr]++;
			}
			if((b>NrOfStars*(4*NrOfMonomersPerStarArm+1)) && (a <= NrOfStars*(4*NrOfMonomersPerStarArm+1))) //a=Crosslinker, b=Peg-Stern
			//else //b=Crosslinker, a=Peg-Stern
			{
				CrosslinkerSterneVerknuepfung[(b-NrOfStars*(4*NrOfMonomersPerStarArm+1)-1)][(a-1)/(4*NrOfMonomersPerStarArm+1)]++;
				PEGBonds[(a-1)/(4*NrOfMonomersPerStarArm+1)]++;
				
				PEGnr=(a-1)/(4*NrOfMonomersPerStarArm+1);
				Crosslinkernr=(b-NrOfStars*(4*NrOfMonomersPerStarArm+1)-1);
				
				if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==1)
					PEGBondsDifferentHEP[PEGnr]++;
			}
			
			if(PEGnr != Crosslinkernr)
			{
				System.out.println("a0: " +PEGnr + "    b0: " + Crosslinkernr);
				
				/*
				 * Perkolationsalgorithmus
				 */
				
				for(int j = 1; j <= NrOfStars; j++)
				{
					if(ColorStars[j] == j) //Stern und Abzweigungen wurden noch nicht untersucht 
					for(int i = 0; i < returnCrosslinkerConnectedToPEG(j).size(); i++)
					{
						ColoringCrosslinker(returnCrosslinkerConnectedToPEG(j).get(i), j);
					}
						
				}
				
				
			addedBondsDuringSimulation++;
			
			counterFiles[addedBondsDuringSimulation]++;
			
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
			

			//int offset = 90*NrOfHeparin;
			int ColorHighestNumber = 0;
			ColorHighestNumber = findMaximumArrayValueIndex();
			 
			int NrOfStarsInCluster = 0;
			int NrOfCrosslinkerInCluster = 0;
			
			for (int k= 1; k <= NrOfStars; k++)
				if(ColorStars[k]==ColorHighestNumber)
				 {
					NrOfStarsInCluster++;
				 }
			
			for (int k= 1; k <= NrOfCrosslinker; k++)
				if(ColorCrosslinker[k]==ColorHighestNumber)
				{
					
				  NrOfCrosslinkerInCluster++;
			 }
			
			System.out.println("InitConfiguration     maxCluster: "+ (117*StarClusterSize[findMaximumArrayValueIndex()]+1*CrosslinkerClusterSize[findMaximumArrayValueIndex()]) +"       relativeMonomers: "+(117*StarClusterSize[findMaximumArrayValueIndex()]+1*CrosslinkerClusterSize[findMaximumArrayValueIndex()])/(117.0*NrOfStars+1.0*NrOfCrosslinker)+"   addBonds: " + addedBondsDuringSimulation + "   Color: "+ ColorHighestNumber);
			System.out.println("Stars: "+ (StarClusterSize[ColorHighestNumber]) + "  vs. "+NrOfStarsInCluster);
			System.out.println("HEP  : "+ (CrosslinkerClusterSize[ColorHighestNumber])+ "  vs. "+NrOfCrosslinkerInCluster);
			
			
			if(PEGBonds[PEGnr]==1)
			{
				//Statistik_S1D0T0Q0_OneRun[addedBondsDuringSimulation]++;
				//Statistik_S0D0T0Q0_OneRun[addedBondsDuringSimulation]--;
				
				Histogramm_S1D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
				Histogramm_S0D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
				
				//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
				//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
			}
			
			if(PEGBonds[PEGnr]==2)
			{	
				if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==1)
				{
					//Statistik_S2D0T0Q0_OneRun[addedBondsDuringSimulation]++;
					//Statistik_S1D0T0Q0_OneRun[addedBondsDuringSimulation]--;
					
					Histogramm_S2D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					Histogramm_S1D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					
					//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
					//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
				}
				if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==2)
				{
					//Statistik_S0D1T0Q0_OneRun[addedBondsDuringSimulation]++;
					//Statistik_S1D0T0Q0_OneRun[addedBondsDuringSimulation]--;
					
					Histogramm_S0D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					Histogramm_S1D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					
					//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
					//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
					//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
				}
			}
			
			if(PEGBonds[PEGnr]==3)
			{	
				if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==1)
				{
					if(PEGBondsDifferentHEP[PEGnr]==3)
					{
						//Statistik_S3D0T0Q0_OneRun[addedBondsDuringSimulation]++;
						//Statistik_S2D0T0Q0_OneRun[addedBondsDuringSimulation]--;
						
						Histogramm_S3D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						Histogramm_S2D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						
						//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
						//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
					}
					if(PEGBondsDifferentHEP[PEGnr]==2)
					{
						//Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]++;
						//Statistik_S0D1T0Q0_OneRun[addedBondsDuringSimulation]--;
						
						Histogramm_S1D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						Histogramm_S0D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						
						//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
						//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
					}
				}
				
				if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==2)
				{
					//Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]++;
					//Statistik_S2D0T0Q0_OneRun[addedBondsDuringSimulation]--;
					
					Histogramm_S1D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					Histogramm_S2D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					
					//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
					//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
					//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
				}
				
				if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==3)
				{
					//Statistik_S0D0T1Q0_OneRun[addedBondsDuringSimulation]++;
					//Statistik_S0D1T0Q0_OneRun[addedBondsDuringSimulation]--;
					
					Histogramm_S0D0T1Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					Histogramm_S0D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					
					//Statistik_Triplebonds_OneRun[addedBondsDuringSimulation]++;
					//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]--;
					//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
				}
			}
			
			if(PEGBonds[PEGnr]==4)
			{	
				if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==1)
				{
					if(PEGBondsDifferentHEP[PEGnr]==4)
					{
						//Statistik_S4D0T0Q0_OneRun[addedBondsDuringSimulation]++;
						//Statistik_S3D0T0Q0_OneRun[addedBondsDuringSimulation]--;
						
						Histogramm_S4D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						Histogramm_S3D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					
						//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
						//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
					}
					if(PEGBondsDifferentHEP[PEGnr]==3)
					{
						//Statistik_S2D1T0Q0_OneRun[addedBondsDuringSimulation]++;
						//Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]--;
						
						Histogramm_S2D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						Histogramm_S1D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						
						//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
						//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
					}
					if(PEGBondsDifferentHEP[PEGnr]==2)
					{
						//Statistik_S1D0T1Q0_OneRun[addedBondsDuringSimulation]++;
						//Statistik_S0D0T1Q0_OneRun[addedBondsDuringSimulation]--;
						
						Histogramm_S1D0T1Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						Histogramm_S0D0T1Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						
						//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
						//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
					}
				}
				
				if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==2)
				{
					if(PEGBondsDifferentHEP[PEGnr]==3)
					{
						//Statistik_S2D1T0Q0_OneRun[addedBondsDuringSimulation]++;
						//Statistik_S3D0T0Q0_OneRun[addedBondsDuringSimulation]--;
						
						Histogramm_S2D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						Histogramm_S3D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						
						//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
						//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
						//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
					}
					
					if(PEGBondsDifferentHEP[PEGnr]==2)
					{
						//Statistik_S0D2T0Q0_OneRun[addedBondsDuringSimulation]++;
						//Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]--;
						
						Histogramm_S0D2T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						Histogramm_S1D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						
						//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
						//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
						//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
					}
				}
				
				if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==3)
				{
					//Statistik_S1D0T1Q0_OneRun[addedBondsDuringSimulation]++;
					//Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]--;
					
					Histogramm_S1D0T1Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					Histogramm_S1D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					
					//Statistik_Triplebonds_OneRun[addedBondsDuringSimulation]++;
					//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]--;
					//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
				}
				
				if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==4)
				{
					//Statistik_S0D0T0Q1_OneRun[addedBondsDuringSimulation]++;
					//Statistik_S0D0T1Q0_OneRun[addedBondsDuringSimulation]--;
					
					Histogramm_S0D0T0Q1.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					Histogramm_S0D0T1Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					
					//Statistik_Quadbonds_OneRun[addedBondsDuringSimulation]++;
					//Statistik_Triplebonds_OneRun[addedBondsDuringSimulation]--;
					//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
				}
			}
			
			CrosslinksVsAllMonomersInCluster.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars), (117*StarClusterSize[findMaximumArrayValueIndex()]+1*CrosslinkerClusterSize[findMaximumArrayValueIndex()])/(117.0*NrOfStars+1.0*NrOfCrosslinker));
			CrosslinksVsNrStarsBiggestCluster.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars), NrOfStarsInCluster);
			CrosslinksVsNrCrosslinkerBiggestCluster.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars), NrOfCrosslinkerInCluster);
			
			
			
			}
			
		}
		
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
					
					int PEGnr=0;
					int Crosslinkernr=0;
					
					
					System.out.println("a: " +a + "    b: " + b);
					
					if(a > b) //a=Crosslinker, b=Peg-Stern
					{
						CrosslinkerSterneVerknuepfung[(a-NrOfStars*(4*NrOfMonomersPerStarArm+1)-1)][(b-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(b-1)/(4*NrOfMonomersPerStarArm+1)]++;
						
						PEGnr=(b-1)/(4*NrOfMonomersPerStarArm+1);
						Crosslinkernr=(a-NrOfStars*(4*NrOfMonomersPerStarArm+1)-1);
						
						Attributesystem[a]-=1;
						Attributesystem[b]-=2;
						
						if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==1)
							PEGBondsDifferentHEP[PEGnr]++;
					}
					else //b=Crosslinker, a=Peg-Stern
					{
						CrosslinkerSterneVerknuepfung[(b-NrOfStars*(4*NrOfMonomersPerStarArm+1)-1)][(a-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(a-1)/(4*NrOfMonomersPerStarArm+1)]++;
						
						PEGnr=(a-1)/(4*NrOfMonomersPerStarArm+1);
						Crosslinkernr=(b-NrOfStars*(4*NrOfMonomersPerStarArm+1)-1);
						
						Attributesystem[a]-=2;
						Attributesystem[b]-=1;
						
						if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==1)
							PEGBondsDifferentHEP[PEGnr]++;
					}
					
					/*
					 * Perkolationsalgorithmus
					 */
					
					for(int j = 1; j <= NrOfStars; j++)
					{
						if(ColorStars[j] == j) //Stern und Abzweigungen wurden noch nicht untersucht 
						for(int i = 0; i < returnCrosslinkerConnectedToPEG(j).size(); i++)
						{
							ColoringCrosslinker(returnCrosslinkerConnectedToPEG(j).get(i), j);
						}
							
					}
					
					
					
					addedBondsDuringSimulation++;
					
					counterFiles[addedBondsDuringSimulation]++;
					
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
					

					//int offset = 90*NrOfHeparin;
					int ColorHighestNumber = 0;
					ColorHighestNumber = findMaximumArrayValueIndex();
					 
					int NrOfStarsInCluster = 0;
					int NrOfCrosslinkerInCluster = 0;
					
					for (int k= 1; k <= NrOfStars; k++)
						if(ColorStars[k]==ColorHighestNumber)
						 {
							NrOfStarsInCluster++;
						 }
					
					for (int k= 1; k <= NrOfCrosslinker; k++)
						if(ColorCrosslinker[k]==ColorHighestNumber)
						{
							
						  NrOfCrosslinkerInCluster++;
					 }
					
					System.out.println("Frame: " +frame + "     maxCluster: "+ (117*StarClusterSize[findMaximumArrayValueIndex()]+1*CrosslinkerClusterSize[findMaximumArrayValueIndex()]) +"       relativeMonomers: "+(117*StarClusterSize[findMaximumArrayValueIndex()]+1*CrosslinkerClusterSize[findMaximumArrayValueIndex()])/(117.0*NrOfStars+1.0*NrOfCrosslinker)+"   addBonds: " + addedBondsDuringSimulation + "   Color: "+ ColorHighestNumber);
					System.out.println("Stars: "+ (StarClusterSize[ColorHighestNumber]) + "  vs. "+NrOfStarsInCluster);
					System.out.println("Crosslinker  : "+ (CrosslinkerClusterSize[ColorHighestNumber])+ "  vs. "+NrOfCrosslinkerInCluster);
					
					// FunctionalGroup in biggestCluster
					
					int counterFuncGroupBiggestClusterCrosslinker=0;
					int counterFuncGroupBiggestClusterStars=0;
					
					for (int k= 1; k <= NrOfStars; k++)
						if(ColorStars[k]==ColorHighestNumber)
						 {
							for (int i= ((4*NrOfMonomersPerStarArm + 1)*(k-1) +1); i <= ( 4*NrOfMonomersPerStarArm + 1)*k; i++)
								if(Attributesystem[i]==2)
									counterFuncGroupBiggestClusterStars++;
						 }
						
					for (int k= 1; k <= NrOfCrosslinker; k++)
						if(ColorCrosslinker[k]==ColorHighestNumber)
						{
							if(Attributesystem[(4*NrOfMonomersPerStarArm + 1)*NrOfStars+k]>0)
							{
								counterFuncGroupBiggestClusterCrosslinker+=Attributesystem[(4*NrOfMonomersPerStarArm + 1)*NrOfStars+k];
								
							}
						}
					

					CrosslinksVsFuncStarsBiggestCluster.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars), (1.0*counterFuncGroupBiggestClusterStars)/NrOfStarsInCluster);
					
					CrosslinksVsFuncCrosslinkerBiggestCluster.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars), (1.0*counterFuncGroupBiggestClusterCrosslinker)/NrOfCrosslinkerInCluster);
					
					
					// FunctionalGroup in Sol
					
					int counterFuncGroupSolCrosslinker=0;
					int counterFuncGroupSolStars=0;
					
					for (int k= 1; k <= NrOfStars; k++)
						if(ColorStars[k]!=ColorHighestNumber)
						 {
							for (int i= ((4*NrOfMonomersPerStarArm + 1)*(k-1) +1); i <= ( 4*NrOfMonomersPerStarArm + 1)*k; i++)
								if(Attributesystem[i]==2)
									 counterFuncGroupSolStars++;
							
						 }
					
					
					for (int k= 1; k <= NrOfCrosslinker; k++)
						if(ColorCrosslinker[k]!=ColorHighestNumber)
						{
							if(Attributesystem[(4*NrOfMonomersPerStarArm + 1)*NrOfStars+k]>0)
							{
								 counterFuncGroupSolCrosslinker+=Attributesystem[(4*NrOfMonomersPerStarArm + 1)*NrOfStars+k];
							}
					    }
					
					//test if Heparin still there
					if((NrOfCrosslinker-NrOfCrosslinkerInCluster)!=0)
					CrosslinksVsFuncCrosslinkerSol.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars), (1.0*counterFuncGroupSolCrosslinker)/(NrOfCrosslinker-NrOfCrosslinkerInCluster));
					
					//test if Star-sol still there
					if((NrOfStars-NrOfStarsInCluster)!=0)
					CrosslinksVsFuncStarsSol.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars), (1.0*counterFuncGroupSolStars)/(NrOfStars-NrOfStarsInCluster));
					
					// end - FunctionalGroup in Sol
					
					for (int k= 1; k < MONOMERZAHL; k++)
						if(Attributesystem[k] < 0)
						{
							System.out.println("Functional group not correct -  Zeit:"+importData.MCSTime);
							System.exit(1);
						}
					
					// End-FunctionalGroup in biggestCluster
					
					
					
					
					if(PEGBonds[PEGnr]==1)
					{
						//Statistik_S1D0T0Q0_OneRun[addedBondsDuringSimulation]++;
						//Statistik_S0D0T0Q0_OneRun[addedBondsDuringSimulation]--;
						
						Histogramm_S1D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						Histogramm_S0D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						
						//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
						//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
					}
					
					if(PEGBonds[PEGnr]==2)
					{	
						if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==1)
						{
							//Statistik_S2D0T0Q0_OneRun[addedBondsDuringSimulation]++;
							//Statistik_S1D0T0Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S2D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S1D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
							//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
						if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==2)
						{
							//Statistik_S0D1T0Q0_OneRun[addedBondsDuringSimulation]++;
							//Statistik_S1D0T0Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S0D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S1D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
							//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
							//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
					}
					
					if(PEGBonds[PEGnr]==3)
					{	
						if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==1)
						{
							if(PEGBondsDifferentHEP[PEGnr]==3)
							{
								//Statistik_S3D0T0Q0_OneRun[addedBondsDuringSimulation]++;
								//Statistik_S2D0T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S3D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S2D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
								//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
							if(PEGBondsDifferentHEP[PEGnr]==2)
							{
								//Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]++;
								//Statistik_S0D1T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S1D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S0D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
								//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
						}
						
						if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==2)
						{
							//Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]++;
							//Statistik_S2D0T0Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S1D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S2D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
							//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
							//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
						
						if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==3)
						{
							//Statistik_S0D0T1Q0_OneRun[addedBondsDuringSimulation]++;
							//Statistik_S0D1T0Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S0D0T1Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S0D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							//Statistik_Triplebonds_OneRun[addedBondsDuringSimulation]++;
							//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]--;
							//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
					}
					
					if(PEGBonds[PEGnr]==4)
					{	
						if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==1)
						{
							if(PEGBondsDifferentHEP[PEGnr]==4)
							{
								//Statistik_S4D0T0Q0_OneRun[addedBondsDuringSimulation]++;
								//Statistik_S3D0T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S4D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S3D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
								//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
								//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
							if(PEGBondsDifferentHEP[PEGnr]==3)
							{
								//Statistik_S2D1T0Q0_OneRun[addedBondsDuringSimulation]++;
								//Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S2D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S1D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
								//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
							if(PEGBondsDifferentHEP[PEGnr]==2)
							{
								//Statistik_S1D0T1Q0_OneRun[addedBondsDuringSimulation]++;
								//Statistik_S0D0T1Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S1D0T1Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S0D0T1Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
								//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
						}
						
						if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==2)
						{
							if(PEGBondsDifferentHEP[PEGnr]==3)
							{
								//Statistik_S2D1T0Q0_OneRun[addedBondsDuringSimulation]++;
								//Statistik_S3D0T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S2D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S3D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
								//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
								//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
							
							if(PEGBondsDifferentHEP[PEGnr]==2)
							{
								//Statistik_S0D2T0Q0_OneRun[addedBondsDuringSimulation]++;
								//Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S0D2T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S1D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
								//Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
								//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
						}
						
						if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==3)
						{
							//Statistik_S1D0T1Q0_OneRun[addedBondsDuringSimulation]++;
							//Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S1D0T1Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S1D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							//Statistik_Triplebonds_OneRun[addedBondsDuringSimulation]++;
							//Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]--;
							//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
						
						if(CrosslinkerSterneVerknuepfung[Crosslinkernr][PEGnr]==4)
						{
							//Statistik_S0D0T0Q1_OneRun[addedBondsDuringSimulation]++;
							//Statistik_S0D0T1Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S0D0T0Q1.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S0D0T1Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							//Statistik_Quadbonds_OneRun[addedBondsDuringSimulation]++;
							//Statistik_Triplebonds_OneRun[addedBondsDuringSimulation]--;
							//Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
						
					}
					
					if((addedBondsDuringSimulation/(4.0*NrOfStars) >= 0.74) && (addedBondsDuringSimulation/(4.0*NrOfStars) <= 0.76))
					{
						int counterSinglebonds = 0;
						int counterDoublebonds = 0;
						int counterTriplebonds = 0;
						int counterQuadbonds = 0;
						
						for(int i = 0; i < NrOfCrosslinker; i++)
							for(int j = 0; j < NrOfStars; j++)
							{
								if(CrosslinkerSterneVerknuepfung[i][j] == 1)
									counterSinglebonds++;
								else if (CrosslinkerSterneVerknuepfung[i][j] == 2)
									counterDoublebonds++;
								else if (CrosslinkerSterneVerknuepfung[i][j] == 3)
									counterTriplebonds++;
								else if (CrosslinkerSterneVerknuepfung[i][j] == 4)
									counterQuadbonds++;
							}
						
						SingleBondsAtGamma75.AddValue(counterSinglebonds/(4.0*NrOfStars));
						DoubleBondsAtGamma75.AddValue(2.0*counterDoublebonds/(4.0*NrOfStars));
						TripleBondsAtGamma75.AddValue(3.0*counterTriplebonds/(4.0*NrOfStars));
						QuadBondsAtGamma75.AddValue(4.0*counterQuadbonds/(4.0*NrOfStars));
					}
					
					if((addedBondsDuringSimulation/(4.0*NrOfStars) >= 0.94) && (addedBondsDuringSimulation/(4.0*NrOfStars) <= 0.96))
					{
						int counterSinglebonds = 0;
						int counterDoublebonds = 0;
						int counterTriplebonds = 0;
						int counterQuadbonds = 0;
						
						for(int i = 0; i < NrOfCrosslinker; i++)
							for(int j = 0; j < NrOfStars; j++)
							{
								if(CrosslinkerSterneVerknuepfung[i][j] == 1)
									counterSinglebonds++;
								else if (CrosslinkerSterneVerknuepfung[i][j] == 2)
									counterDoublebonds++;
								else if (CrosslinkerSterneVerknuepfung[i][j] == 3)
									counterTriplebonds++;
								else if (CrosslinkerSterneVerknuepfung[i][j] == 4)
									counterQuadbonds++;
							}
						
						SingleBondsAtGamma95.AddValue(counterSinglebonds/(4.0*NrOfStars));
						DoubleBondsAtGamma95.AddValue(2.0*counterDoublebonds/(4.0*NrOfStars));
						TripleBondsAtGamma95.AddValue(3.0*counterTriplebonds/(4.0*NrOfStars));
						QuadBondsAtGamma95.AddValue(4.0*counterQuadbonds/(4.0*NrOfStars));
					}
					
					CrosslinksVsAllMonomersInCluster.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars), (117*StarClusterSize[findMaximumArrayValueIndex()]+1*CrosslinkerClusterSize[findMaximumArrayValueIndex()])/(117.0*NrOfStars+1.0*NrOfCrosslinker));
					CrosslinksVsNrStarsBiggestCluster.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars), NrOfStarsInCluster);
					CrosslinksVsNrCrosslinkerBiggestCluster.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars), NrOfCrosslinkerInCluster);
					
				}
				
				int counterSinglebonds = 0;
				int counterDoublebonds = 0;
				int counterTriplebonds = 0;
				int counterQuadbonds = 0;
				int counterDenglingEnds = 0;
				
				for(int i = 0; i < NrOfCrosslinker; i++)
					for(int j = 0; j < NrOfStars; j++)
					{
						if(CrosslinkerSterneVerknuepfung[i][j] == 1)
							counterSinglebonds++;
						else if (CrosslinkerSterneVerknuepfung[i][j] == 2)
							counterDoublebonds++;
						else if (CrosslinkerSterneVerknuepfung[i][j] == 3)
							counterTriplebonds++;
						else if (CrosslinkerSterneVerknuepfung[i][j] == 4)
							counterQuadbonds++;
						else if (CrosslinkerSterneVerknuepfung[i][j] > 4)
						{
							System.out.println("Verknuepfung 5! Sollte nicht vorkommen! Wert: " +CrosslinkerSterneVerknuepfung[i][j] + " bei Hep:" + (i+1) + "   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
							System.exit(1); 
						}
					}
				
				for(int j = 0; j < NrOfStars; j++)
				{
					//if(PEGBonds[j] == 0)
						counterDenglingEnds += 4-PEGBonds[j];
						
					 if(PEGBonds[j] > 4)
					 {
							System.out.println("Verknuepfung 5! Sollte nicht vorkommen!   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
							System.exit(1); 
					 }
					 
					 if(PEGBondsDifferentHEP[j] > 4)
					 {
							System.out.println("Verknuepfung zu 5 verschiedene HEP! Sollte nicht vorkommen!   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
							System.exit(1); 
					 }
				}
				
				Singlebonds[(int) (importData.MCSTime/(deltaT))].AddValue(counterSinglebonds/(4.0*NrOfStars));
				Doublebonds[(int) (importData.MCSTime/(deltaT))].AddValue(2.0*counterDoublebonds/(4.0*NrOfStars));
				Triplebonds[(int) (importData.MCSTime/(deltaT))].AddValue(3.0*counterTriplebonds/(4.0*NrOfStars));
				Quadbonds[(int) (importData.MCSTime/(deltaT))].AddValue(4.0*counterQuadbonds/(4.0*NrOfStars));
				
				DenglingEnds[(int) (importData.MCSTime/(deltaT))].AddValue((4.0*NrOfStars-counterSinglebonds-2.0*counterDoublebonds-3.0*counterTriplebonds-4.0*counterQuadbonds)/(4.0*NrOfStars));
				
				DenglingEnds2[(int) (importData.MCSTime/(deltaT))].AddValue((counterDenglingEnds)/(4.0*NrOfStars));
				
				
				bonds[(int) (importData.MCSTime/(deltaT))].AddValue(importData.additionalbonds.size());
				//System.out.println("size: " +bonds[(int) (importData.MCSTime/(10000))].ReturnN());
				System.out.println("size: " +importData.addedBondsBetweenFrames.size());
				importData.addedBondsBetweenFrames.clear();
	}

	private void ColoringCrosslinker(int CrosslinkerNr, int farbe)
	{
		//noch keine Verknuepfung
		if(ColorCrosslinker[CrosslinkerNr] < 0)
		{
			ColorCrosslinker[CrosslinkerNr] = farbe;
			CrosslinkerClusterSize[farbe]++;
			
			for(int i = 0; i < returnPEGConnectedToCrosslinker(CrosslinkerNr).size(); i++)
				ColoringPEG(returnPEGConnectedToCrosslinker(CrosslinkerNr).get(i), farbe);
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
			
			for(int i = 0; i < returnCrosslinkerConnectedToPEG(PEGNr).size(); i++)
				ColoringCrosslinker(returnCrosslinkerConnectedToPEG(PEGNr).get(i), farbe);
		}
	}
	
	/**
	 * @param PEGnr starts at 1, end at NrOfStars
	 * @return IntArrayList with Crosslinkernumber connected to PEG with 1 <= Crosslinkernr <= NrOfCrosslinker
	 */
	private IntArrayList returnCrosslinkerConnectedToPEG(int PEGnr)
	{
		IntArrayList dummyList = new IntArrayList();
		
		int j = PEGnr-1;
		
		for(int i = 0; i < NrOfCrosslinker; i++)
			{
				if((CrosslinkerSterneVerknuepfung[i][j] >= 1))
				{
					dummyList.add(i+1);
				}
			}
		
		return dummyList;
	}
	
	/**
	 * @param Crosslinkernr 1 <= Crosslinkernr <= NrOfCrosslinker
	 * @return IntArrayList with PEGnumber connected to Crosslinkernr with 1 <= PEGnr <= NrOfStars
	 */
	private IntArrayList returnPEGConnectedToCrosslinker(int HEPnr)
	{
		IntArrayList dummyList = new IntArrayList();
		
		int i = HEPnr-1;
		
		for(int j = 0; j < NrOfStars; j++)
			{
				if((CrosslinkerSterneVerknuepfung[i][j] >= 1))
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
			
				if((117*StarClusterSize[i]+1*CrosslinkerClusterSize[i]) > maximum) {
		            maximum = (117*StarClusterSize[i]+1*CrosslinkerClusterSize[i]);   // new maximum
		            maximumIndex = i;
		        }
			
			
		}
		return maximumIndex;
	}
	
	private void InitArrays()
	  {
		  CrosslinkerSterneVerknuepfung = null;
		  CrosslinkerSterneVerknuepfung = new int[NrOfCrosslinker][NrOfStars];
		  
		  ColorCrosslinker = new int[NrOfCrosslinker+1];
		  ColorStars = new int[NrOfStars+1];
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  ColorStars[i]=i;
		  
		  for(int i = 0; i <= NrOfCrosslinker; i++)
			  ColorCrosslinker[i]=-i;
		  
		  StarClusterSize = new int[NrOfStars+1]; //Anzahl Sterne im Cluster
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  StarClusterSize[i]=1; 	//Anzahl der Bindungen - Summe ergibt NrOfStars auch nach Perkolation
		  
		  CrosslinkerClusterSize = new int[NrOfStars+1]; //Anzahl Sterne im Cluster
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  CrosslinkerClusterSize[i]=0; 	//Anzahl der Bindungen - Summe ergibt NrOfHeparin auch nach Perkolation
	  }
	  
	  
	  private void resetArrays()
	  {
		  for(int i = 0; i <= NrOfStars; i++)
			  ColorStars[i]=i;
		  
		  for(int i = 0; i <= NrOfCrosslinker; i++)
			  ColorCrosslinker[i]=-i;
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  StarClusterSize[i]=1; 	//Anzahl der Bindungen - Summe ergibt NrOfStars auch nach Perkolation
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  CrosslinkerClusterSize[i]=0; 	//Anzahl der Bindungen - Summe ergibt NrOfHeparin auch nach Perkolation
	  
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
