package EvalToolProject.PEG;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

import EvalToolProject.tools.Histogramm;
import EvalToolProject.tools.BFMFileSaver;
import EvalToolProject.tools.BFMImportData;
import EvalToolProject.tools.Int_IntArrayList_Hashtable;
import EvalToolProject.tools.Statistik;

public class Auswertung_Sterne_VerknuepfungHoehereDefekte_HepPEG {


	
	
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
	
	int NrOfHeparin = 0;
	int NrOfStars = 0;
	int NrOfMonomersPerStarArm = 0;
	
	int[] dumpsystem;

	/*Statistik[] GesamtStat_x;
	Statistik[] GesamtStat_y;
	Statistik[] GesamtStat_z;*/
	
	//double [] Rcm_x;
	//double [] Rcm_y;
	//double [] Rcm_z;
	
	Statistik[] bonds;
	Statistik durchschnittbond;
	
	int[][] HeparinSterneVerknuepfung;
	
	int[] PEGBonds;
	int[] PEGBondsDifferentHEP; //Nr of PEG-bonds attached to distinguishable heparin - index = PEGnr - value in [0, 4]
	
	Statistik[] DenglingEnds2;
	
	Statistik[] Singlebonds;
	Statistik[] Doublebonds;
	Statistik[] Triplebonds;
	Statistik[] Quadbonds;
	Statistik[] DenglingEnds;
	
	int[] Statistik_DenglingEnds2;
	
	int[] Statistik_Singlebonds;
	int[] Statistik_Doublebonds;
	int[] Statistik_Triplebonds;
	int[] Statistik_Quadbonds;
	int[] Statistik_DenglingEnds;
	
	int[] Statistik_Singlebonds_OneRun;
	int[] Statistik_Doublebonds_OneRun;
	int[] Statistik_Triplebonds_OneRun;
	int[] Statistik_Quadbonds_OneRun;
	int[] Statistik_DenglingEnds_OneRun;
	
	//higher order defects
	int[] Statistik_S0D0T0Q0;
	int[] Statistik_S1D0T0Q0;
	int[] Statistik_S2D0T0Q0;
	int[] Statistik_S0D1T0Q0;
	int[] Statistik_S3D0T0Q0;
	int[] Statistik_S1D1T0Q0;
	int[] Statistik_S0D0T1Q0;
	int[] Statistik_S4D0T0Q0;
	int[] Statistik_S2D1T0Q0;
	int[] Statistik_S0D2T0Q0;
	int[] Statistik_S1D0T1Q0;
	int[] Statistik_S0D0T0Q1;
	
	int[] Statistik_S0D0T0Q0_OneRun;
	int[] Statistik_S1D0T0Q0_OneRun;
	int[] Statistik_S2D0T0Q0_OneRun;
	int[] Statistik_S0D1T0Q0_OneRun;
	int[] Statistik_S3D0T0Q0_OneRun;
	int[] Statistik_S1D1T0Q0_OneRun;
	int[] Statistik_S0D0T1Q0_OneRun;
	int[] Statistik_S4D0T0Q0_OneRun;
	int[] Statistik_S2D1T0Q0_OneRun;
	int[] Statistik_S0D2T0Q0_OneRun;
	int[] Statistik_S1D0T1Q0_OneRun;
	int[] Statistik_S0D0T0Q1_OneRun;
	
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
	
	//FunctionalityOfHeparin
	int[] Statistik_FunctionalityOfHeparin;
	int[] Statistik_FunctionalityOfHeparin_OneRun;
	Histogramm Histogramm_FunctionalityOfHeparin;
	
	
	int[] counterFiles;
	
	BFMFileSaver rg;
	BFMFileSaver BondSaver;
	BFMFileSaver HigherOrderDefectSaver;
	BFMFileSaver HigherOrderDefectSaver_Histogramm;
	BFMFileSaver BondSaver_ExtentOfReaction;
	BFMFileSaver BondSaver_ExtentOfReactionHistogramm;
	
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	int FunktionalitaetHeparin[];
	BFMFileSaver FunktionalitaetHeparinSaver;
	
	//relative frequency of bond types at special cross-links
	Statistik SingleBondsAtGamma95;
	Statistik DoubleBondsAtGamma95;
	Statistik TripleBondsAtGamma95;
	Statistik QuadBondsAtGamma95;
	
	BFMFileSaver SaverBondsAtGamma95;
	
	Statistik SingleBondsAtGamma90;
	Statistik DoubleBondsAtGamma90;
	Statistik TripleBondsAtGamma90;
	Statistik QuadBondsAtGamma90;
	
	BFMFileSaver SaverBondsAtGamma90;
	
	Statistik SingleBondsAtGamma75;
	Statistik DoubleBondsAtGamma75;
	Statistik TripleBondsAtGamma75;
	Statistik QuadBondsAtGamma75;
	
	BFMFileSaver SaverBondsAtGamma75;
	
	int addedBondsDuringSimulation;
	
	long deltaT;
	
	public Auswertung_Sterne_VerknuepfungHoehereDefekte_HepPEG(String fdir, String fname, String gamma, String dirDst,  String Experiment, int nrOfFiles)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ fname+"__001.bfm");
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		NrOfStars= FirstData.NrOfStars;
		NrOfHeparin= FirstData.NrOfHeparin;
		Gitter_x= FirstData.box_x;
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT+ "  Stars: "+ NrOfStars);
		//End - Determine MaxFrame out of the first file
		
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.GERMANY);
		otherSymbols.setDecimalSeparator('.');
		otherSymbols.setGroupingSeparator(','); 
		DecimalFormat df =   new DecimalFormat  ( "0.00000", otherSymbols );
		DecimalFormat dc =   new DecimalFormat  ( "0.00", otherSymbols );
		
		double concentration=8.0*(NrOfStars*117+90*NrOfHeparin)/(Gitter_x*Gitter_x*Gitter_x);
		   
		
		//higher order defects - index = extent of reaction (PEG-Bonds)
		Statistik_S0D0T0Q0 = new int[4*NrOfStars+1];
		Statistik_S1D0T0Q0 = new int[4*NrOfStars+1];
		Statistik_S2D0T0Q0 = new int[4*NrOfStars+1];
		Statistik_S0D1T0Q0 = new int[4*NrOfStars+1];
		Statistik_S3D0T0Q0 = new int[4*NrOfStars+1];
		Statistik_S1D1T0Q0 = new int[4*NrOfStars+1];
		Statistik_S0D0T1Q0 = new int[4*NrOfStars+1];
		Statistik_S4D0T0Q0 = new int[4*NrOfStars+1];
		Statistik_S2D1T0Q0 = new int[4*NrOfStars+1];
		Statistik_S0D2T0Q0 = new int[4*NrOfStars+1];
		Statistik_S1D0T1Q0 = new int[4*NrOfStars+1];
		Statistik_S0D0T0Q1 = new int[4*NrOfStars+1];
		
		Statistik_S0D0T0Q0_OneRun = new int[4*NrOfStars+1];
		Statistik_S1D0T0Q0_OneRun = new int[4*NrOfStars+1];
		Statistik_S2D0T0Q0_OneRun = new int[4*NrOfStars+1];
		Statistik_S0D1T0Q0_OneRun = new int[4*NrOfStars+1];
		Statistik_S3D0T0Q0_OneRun = new int[4*NrOfStars+1];
		Statistik_S1D1T0Q0_OneRun = new int[4*NrOfStars+1];
		Statistik_S0D0T1Q0_OneRun = new int[4*NrOfStars+1];
		Statistik_S4D0T0Q0_OneRun = new int[4*NrOfStars+1];
		Statistik_S2D1T0Q0_OneRun = new int[4*NrOfStars+1];
		Statistik_S0D2T0Q0_OneRun = new int[4*NrOfStars+1];
		Statistik_S1D0T1Q0_OneRun = new int[4*NrOfStars+1];
		Statistik_S0D0T0Q1_OneRun = new int[4*NrOfStars+1];
		
		counterFiles = new int[4*NrOfStars+1];
		
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
		
		
		Statistik_FunctionalityOfHeparin = new int[4*NrOfStars+1];;
		Statistik_FunctionalityOfHeparin_OneRun = new int[4*NrOfStars+1];;
		Histogramm_FunctionalityOfHeparin = new Histogramm(lowerBoundary,higherBoundary,NrBins);
		
		//Statistik_DenglingEnds2;
		
		Statistik_Singlebonds = new int[4*NrOfStars+1];
		Statistik_Doublebonds = new int[4*NrOfStars+1];
		Statistik_Triplebonds = new int[4*NrOfStars+1];
		Statistik_Quadbonds = new int[4*NrOfStars+1];
		Statistik_DenglingEnds = new int[4*NrOfStars+1];
		
		Statistik_Singlebonds_OneRun = new int[4*NrOfStars+1];
		Statistik_Doublebonds_OneRun = new int[4*NrOfStars+1];
		Statistik_Triplebonds_OneRun = new int[4*NrOfStars+1];
		Statistik_Quadbonds_OneRun = new int[4*NrOfStars+1];
		Statistik_DenglingEnds_OneRun = new int[4*NrOfStars+1];
		
		
		/*Statistik_S0D0T0Q0 = new Statistik[4*NrOfStars+1];
		Statistik_S1D0T0Q0 = new Statistik[4*NrOfStars+1];
		Statistik_S2D0T0Q0 = new Statistik[4*NrOfStars+1];
		Statistik_S0D1T0Q0 = new Statistik[4*NrOfStars+1];
		Statistik_S3D0T0Q0 = new Statistik[4*NrOfStars+1];
		Statistik_S1D1T0Q0 = new Statistik[4*NrOfStars+1];
		Statistik_S0D0T1Q0 = new Statistik[4*NrOfStars+1];
		Statistik_S4D0T0Q0 = new Statistik[4*NrOfStars+1];
		Statistik_S2D1T0Q0 = new Statistik[4*NrOfStars+1];
		Statistik_S0D2T0Q0 = new Statistik[4*NrOfStars+1];
		Statistik_S1D0T1Q0 = new Statistik[4*NrOfStars+1];
		Statistik_S0D0T0Q1 = new Statistik[4*NrOfStars+1];
		
		for(int i = 0; i < 4*NrOfStars+1; i++)
			{
				Statistik_S0D0T0Q0[i] = new Statistik();
				Statistik_S1D0T0Q0[i] = new Statistik();
				Statistik_S2D0T0Q0[i] = new Statistik();
				Statistik_S0D1T0Q0[i] = new Statistik();
				Statistik_S3D0T0Q0[i] = new Statistik();
				Statistik_S1D1T0Q0[i] = new Statistik();
				Statistik_S0D0T1Q0[i] = new Statistik();
				Statistik_S4D0T0Q0[i] = new Statistik();
				Statistik_S2D1T0Q0[i] = new Statistik();
				Statistik_S0D2T0Q0[i] = new Statistik();
				Statistik_S1D0T1Q0[i] = new Statistik();
				Statistik_S0D0T0Q1[i] = new Statistik();
			}
		*/
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		rg = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/Bonds_PEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.DateiAnlegen(dirDst+"/Bonds_PEG_CumBonds_Time_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.setzeZeile("# t[MCS] <CumBond>  <CumBond/maxBond> d<CumBond>");
		
		BondSaver = new BFMFileSaver();
		//BondSaver.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/Bonds_PEG_Bonds_HepPEGConnectedGel_"+FileName+".dat", false);
		BondSaver.DateiAnlegen(dirDst+"/Bonds_PEG_SimpleDefects_Time_HepPEGConnectedGel_"+FileName+".dat", false);
		BondSaver.setzeZeile("# t[MCS] <Single>  <Double> <Triple> <Quad> <Free> <CumBonds> Sum=1");
		
		BondSaver_ExtentOfReaction = new BFMFileSaver();
		BondSaver_ExtentOfReaction.DateiAnlegen(dirDst+"/Bonds_PEG_SimpleDefects_EveryBond_HepPEGConnectedGel_"+FileName+".dat", false);
		BondSaver_ExtentOfReaction.setzeZeile("# p <Single>  <Double> <Triple> <Quad> <Free> <CumBonds=1>");
		
		BondSaver_ExtentOfReactionHistogramm = new BFMFileSaver();
		BondSaver_ExtentOfReactionHistogramm.DateiAnlegen(dirDst+"/Bonds_PEG_SimpleDefects_HepPEGConnectedGel_"+FileName+".dat", false);
		BondSaver_ExtentOfReactionHistogramm.setzeZeile("# p <Single>  <Double> <Triple> <Quad> <Free> <CumBonds> <Sum=1>");
		
		
		
		HigherOrderDefectSaver = new BFMFileSaver();
		HigherOrderDefectSaver.DateiAnlegen(dirDst+"/Bonds_PEG_HigherOrderDefects_EveryBond_HepPEGConnectedGel_"+FileName+".dat", false);
		HigherOrderDefectSaver.setzeZeile("# p <S0D0T0D0> <S1D0T0Q0> <S2D0T0Q0> <S0D1T0Q0> <S3D0T0Q0> <S1D1T0Q0> <S0D0T1Q0> <S4D0T0Q0> <S2D1T0Q0> <S0D2T0Q0> <S1D0T1Q0> <S0D0T0Q1> Sum=1 <f_HEP>");
		
		HigherOrderDefectSaver_Histogramm = new BFMFileSaver();
		HigherOrderDefectSaver_Histogramm.DateiAnlegen(dirDst+"/Bonds_PEG_HigherOrderDefects_HepPEGConnectedGel_"+FileName+".dat", false);
		HigherOrderDefectSaver_Histogramm.setzeZeile("# Histogramm ["+lowerBoundary+";"+higherBoundary+"]  dI="+ Histogramm_S0D0T0Q0.GetIntervallThickness() +"  NrBins:"+Histogramm_S0D0T0Q0.GetNrBins());
		HigherOrderDefectSaver_Histogramm.setzeZeile("# p <S0D0T0D0> <S1D0T0Q0> <S2D0T0Q0> <S0D1T0Q0> <S3D0T0Q0> <S1D1T0Q0> <S0D0T1Q0> <S4D0T0Q0> <S2D1T0Q0> <S0D2T0Q0> <S1D0T1Q0> <S0D0T0Q1> Sum=1 <f_HEP>");
		HigherOrderDefectSaver_Histogramm.setzeZeile("0.00000	1.00000	0.00000	0.00000	0.00000	0.00000	0.00000	0.00000	0.00000	0.00000	0.00000	0.00000	0.00000	1.00000 1.00000");
		
		FunktionalitaetHeparinSaver = new BFMFileSaver();
		//FunktionalitaetHeparinSaver.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/HeparinFunctionality_HepPEGConnectedGel_"+FileName+".dat", false);
		FunktionalitaetHeparinSaver.DateiAnlegen(dirDst+"/Bonds_HEP_Functionality_p0_90_HepPEGConnectedGel_"+FileName+".dat", false);
		
		
		SaverBondsAtGamma95 = new BFMFileSaver();
		//SaverBondsAtGamma95.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung/defects_gamma_0_95conversion.dat", true);
		SaverBondsAtGamma95.DateiAnlegen(dirDst+"/Bonds_Defects_p0_95_"+FileName+".dat", true);
		//SaverBondsAtGamma95.setzeZeile("# gamma <Single>  <Double> <Triple> <Quad>");
		
		SaverBondsAtGamma90 = new BFMFileSaver();
		//SaverBondsAtGamma75.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung/defects_gamma_0_75conversion.dat", true);
		SaverBondsAtGamma90.DateiAnlegen(dirDst+"/Bonds_Defects_p0_90_"+FileName+".dat", true);
		//SaverBondsAtGamma75.setzeZeile("# gamma <Single>  <Double> <Triple> <Quad>");
		
		SaverBondsAtGamma75 = new BFMFileSaver();
		//SaverBondsAtGamma75.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung/defects_gamma_0_75conversion.dat", true);
		SaverBondsAtGamma75.DateiAnlegen(dirDst+"/Bonds_Defects_p0_75_"+FileName+".dat", true);
		//SaverBondsAtGamma75.setzeZeile("# gamma <Single>  <Double> <Triple> <Quad>");
		
		
		FunktionalitaetHeparin = new int[29];
		
		
		SingleBondsAtGamma95 = new Statistik();
		DoubleBondsAtGamma95 = new Statistik();
		TripleBondsAtGamma95 = new Statistik();
		QuadBondsAtGamma95 = new Statistik();
		
		SingleBondsAtGamma90 = new Statistik();
		DoubleBondsAtGamma90 = new Statistik();
		TripleBondsAtGamma90 = new Statistik();
		QuadBondsAtGamma90 = new Statistik();
		
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
		
		//LoadFile(FileName+"__"+dh.format(11)+".bfm", 1, maxframe);
		//LoadFile(FileName+"__"+dh.format(12)+".bfm", 1, maxframe);
		//for(int i = 1; i <= 26; i++)
		
		int usedFilesForAveraging = 0;
		
		for(int i = 1; i <= nrOfFiles; i+=1)//i++)
		{
			usedFilesForAveraging++;
			LoadFile(FileName+"__"+dh.format(i)+".bfm", 1, maxframe);
			
			for(int j=0; j <=4*NrOfStars; j++)
			{
				Statistik_S0D0T0Q0[j] += Statistik_S0D0T0Q0_OneRun[j];
				Statistik_S0D0T0Q0_OneRun[j]=0;
				
				Statistik_S1D0T0Q0[j] += Statistik_S1D0T0Q0_OneRun[j];
				Statistik_S1D0T0Q0_OneRun[j]=0;
				
				Statistik_S2D0T0Q0[j] += Statistik_S2D0T0Q0_OneRun[j];
				Statistik_S2D0T0Q0_OneRun[j]=0;
				
				Statistik_S0D1T0Q0[j] += Statistik_S0D1T0Q0_OneRun[j];
				Statistik_S0D1T0Q0_OneRun[j]=0;
				
				Statistik_S3D0T0Q0[j] += Statistik_S3D0T0Q0_OneRun[j];
				Statistik_S3D0T0Q0_OneRun[j]=0;
				
				Statistik_S1D1T0Q0[j] += Statistik_S1D1T0Q0_OneRun[j];
				Statistik_S1D1T0Q0_OneRun[j]=0;
				
				Statistik_S0D0T1Q0[j] += Statistik_S0D0T1Q0_OneRun[j];
				Statistik_S0D0T1Q0_OneRun[j]=0;
				
				Statistik_S4D0T0Q0[j] += Statistik_S4D0T0Q0_OneRun[j];
				Statistik_S4D0T0Q0_OneRun[j]=0;
				
				Statistik_S2D1T0Q0[j] += Statistik_S2D1T0Q0_OneRun[j];
				Statistik_S2D1T0Q0_OneRun[j]=0;
				
				Statistik_S0D2T0Q0[j] += Statistik_S0D2T0Q0_OneRun[j];
				Statistik_S0D2T0Q0_OneRun[j]=0;
				
				Statistik_S1D0T1Q0[j] += Statistik_S1D0T1Q0_OneRun[j];
				Statistik_S1D0T1Q0_OneRun[j]=0;
				
				Statistik_S0D0T0Q1[j] += Statistik_S0D0T0Q1_OneRun[j];
				Statistik_S0D0T0Q1_OneRun[j]=0;
				
				Statistik_Singlebonds[j] += Statistik_Singlebonds_OneRun[j];
				Statistik_Singlebonds_OneRun[j]=0;
				
				Statistik_Doublebonds[j] += Statistik_Doublebonds_OneRun[j];
				Statistik_Doublebonds_OneRun[j]=0;
				
				Statistik_Triplebonds[j] += Statistik_Triplebonds_OneRun[j];
				Statistik_Triplebonds_OneRun[j]=0;
				
				Statistik_Quadbonds[j] += Statistik_Quadbonds_OneRun[j];
				Statistik_Quadbonds_OneRun[j]=0;
				
				Statistik_DenglingEnds[j] += Statistik_DenglingEnds_OneRun[j];
				Statistik_DenglingEnds_OneRun[j]=0;
				
				Statistik_FunctionalityOfHeparin[j] += Statistik_FunctionalityOfHeparin_OneRun[j];
				Statistik_FunctionalityOfHeparin_OneRun[j]=0;
			}
		}
		
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
		
		for(int i=0; i <=4*NrOfStars; i++)
		{
			
			//Sum = 1 -> all stars 
			//double Sum = (1.0+Statistik_S0D0T0Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+ (Statistik_S1D0T0Q0[i]+Statistik_S2D0T0Q0[i]+Statistik_S0D1T0Q0[i]+Statistik_S3D0T0Q0[i]+Statistik_S1D1T0Q0[i]+Statistik_S0D0T1Q0[i]+Statistik_S4D0T0Q0[i]+Statistik_S2D1T0Q0[i]+Statistik_S0D2T0Q0[i]+Statistik_S1D0T1Q0[i]+Statistik_S0D0T0Q1[i])/(usedFilesForAveraging*1.0*NrOfStars);
			
			// sum = 1 -> bonds = extentofReaction -> counting correct
			double Sum = 1.0+(Statistik_DenglingEnds[i]+Statistik_Singlebonds[i]+2.0*Statistik_Doublebonds[i]+3.0*Statistik_Triplebonds[i]+4.0*Statistik_Quadbonds[i])/(4.0*usedFilesForAveraging*NrOfStars);
		
			//check if all files contribute to sampling
			if(counterFiles[i] == usedFilesForAveraging)
				BondSaver_ExtentOfReaction.setzeZeile((i/(4.0*NrOfStars))+"\t"+df.format(Statistik_Singlebonds[i]/(4.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(2.0*Statistik_Doublebonds[i]/(4.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(3.0*Statistik_Triplebonds[i]/(4.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(4.0*Statistik_Quadbonds[i]/(4.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(1.0+Statistik_DenglingEnds[i]/(4.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Sum));
			
		}
		
		BondSaver_ExtentOfReaction.DateiSchliessen();
		
		//# p <Single>  <Double> <Triple> <Quad> <Free> <CumBonds=1>
		for(int i = 0; i < Histogramm_S0D0T0Q0.GetNrBins(); i++)
		{
			//HigherOrderDefectSaver_Histogramm.setzeZeile("# p\t<S0D0T0D0>\t<S1D0T0Q0>\t<S2D0T0Q0>\t<S0D1T0Q0>\t<S3D0T0Q0>\t<S1D1T0Q0>\t<S0D0T1Q0>\t<S4D0T0Q0>\t<S2D1T0Q0>\t<S0D2T0Q0>\t<S1D0T1Q0>\t<S0D0T0Q1>\tSum=1");
			double factor = 1./(1.0*usedFilesForAveraging*NrOfStars);
			
			double quadBond = 1.0*factor*Histogramm_S0D0T0Q1.GetCumulativeNrInBin(i);
			double tripleBond = 0.75*factor*(Histogramm_S0D0T1Q0.GetCumulativeNrInBin(i)+Histogramm_S1D0T1Q0.GetCumulativeNrInBin(i));
			double doubleBond = 0.5*factor*(Histogramm_S0D1T0Q0.GetCumulativeNrInBin(i)+Histogramm_S1D1T0Q0.GetCumulativeNrInBin(i)+Histogramm_S2D1T0Q0.GetCumulativeNrInBin(i)+2.0*Histogramm_S0D2T0Q0.GetCumulativeNrInBin(i));
			double singleBond = 0.25*factor*(Histogramm_S1D0T0Q0.GetCumulativeNrInBin(i)+2.0*Histogramm_S2D0T0Q0.GetCumulativeNrInBin(i)+3.*Histogramm_S3D0T0Q0.GetCumulativeNrInBin(i)+Histogramm_S1D1T0Q0.GetCumulativeNrInBin(i)+4.*Histogramm_S4D0T0Q0.GetCumulativeNrInBin(i)+2.*Histogramm_S2D1T0Q0.GetCumulativeNrInBin(i)+Histogramm_S1D0T1Q0.GetCumulativeNrInBin(i));
			
			double freeBond = 1.-(singleBond+doubleBond+tripleBond+quadBond);
			double cumBond = (singleBond+doubleBond+tripleBond+quadBond);
			
			// sum = 1 -> bonds = extentofReaction -> counting correct
			//double Sum = 1.0 + Histogramm_S0D0T0Q0.GetRangeInBin(i)-0.25*(factor*Histogramm_S1D0T0Q0.GetCumulativeNrInBin(i)+2.*factor*Histogramm_S2D0T0Q0.GetCumulativeNrInBin(i)+2.*factor*Histogramm_S0D1T0Q0.GetCumulativeNrInBin(i)+3.*factor*Histogramm_S3D0T0Q0.GetCumulativeNrInBin(i)+3.*factor*Histogramm_S1D1T0Q0.GetCumulativeNrInBin(i)+3.*factor*Histogramm_S0D0T1Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S4D0T0Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S2D1T0Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S0D2T0Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S1D0T1Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S0D0T0Q1.GetCumulativeNrInBin(i));
			double Sum = freeBond+cumBond;
			
			if(counterFiles[(int) Math.floor(Histogramm_S0D0T0Q0.GetRangeInBin(i)*4*NrOfStars)] == usedFilesForAveraging)
				BondSaver_ExtentOfReactionHistogramm.setzeZeile(df.format(Histogramm_S0D0T0Q0.GetRangeInBin(i))+"\t"+df.format(singleBond)+"\t"+df.format(doubleBond)+"\t"+df.format(tripleBond)+"\t"+df.format(quadBond)+"\t"+df.format(freeBond)+"\t"+df.format(cumBond)+"\t"+df.format(Sum));
			
				//BondSaver_ExtentOfReactionHistogramm.setzeZeile(df.format(Histogramm_S0D0T0Q0.GetRangeInBin(i))+"\t"+df.format(1.0-Histogramm_S0D0T0Q0.GetCumulativeNrInBinNormiert(i))+"\t"+df.format(factor*Histogramm_S1D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S2D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D1T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S3D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S1D1T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D0T1Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S4D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S2D1T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D2T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S1D0T1Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D0T0Q1.GetCumulativeNrInBin(i))+"\t"+df.format(Sum));
			
		}
	
		BondSaver_ExtentOfReactionHistogramm.DateiSchliessen();
		
		
		//HigherOrderDefectSaver.setzeZeile("# p\t<S0D0T0D0>\t<S1D0T0Q0>\t<S2D0T0Q0>\t<S0D1T0Q0>\t<S3D0T0Q0>\t<S1D1T0Q0>\t<S0D0T1Q0>\t<S4D0T0Q0>\t<S2D1T0Q0>\t<S0D2T0Q0>\t<S1D0T1Q0>\t<S0D0T0Q1>\tSum=1");
		for(int i=0; i <=4*NrOfStars; i++)
		{
			
			//Sum = 1 -> all stars 
			//double Sum = (1.0+Statistik_S0D0T0Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+ (Statistik_S1D0T0Q0[i]+Statistik_S2D0T0Q0[i]+Statistik_S0D1T0Q0[i]+Statistik_S3D0T0Q0[i]+Statistik_S1D1T0Q0[i]+Statistik_S0D0T1Q0[i]+Statistik_S4D0T0Q0[i]+Statistik_S2D1T0Q0[i]+Statistik_S0D2T0Q0[i]+Statistik_S1D0T1Q0[i]+Statistik_S0D0T0Q1[i])/(usedFilesForAveraging*1.0*NrOfStars);
			
			// sum = 1 -> bonds = extentofReaction -> counting correct
			double Sum = 1.0 + i/(4.0*NrOfStars)-(Statistik_S1D0T0Q0[i]+2.*Statistik_S2D0T0Q0[i]+2.*Statistik_S0D1T0Q0[i]+3.*Statistik_S3D0T0Q0[i]+3.*Statistik_S1D1T0Q0[i]+3.*Statistik_S0D0T1Q0[i]+4.*Statistik_S4D0T0Q0[i]+4.*Statistik_S2D1T0Q0[i]+4.*Statistik_S0D2T0Q0[i]+4.*Statistik_S1D0T1Q0[i]+4.*Statistik_S0D0T0Q1[i])/(usedFilesForAveraging*4.0*NrOfStars);
		
			//check if all files contribute to sampling
			if(counterFiles[i] == usedFilesForAveraging)
				HigherOrderDefectSaver.setzeZeile((i/(4.0*NrOfStars))+"\t"+df.format(1.0+Statistik_S0D0T0Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Statistik_S1D0T0Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Statistik_S2D0T0Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Statistik_S0D1T0Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Statistik_S3D0T0Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Statistik_S1D1T0Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Statistik_S0D0T1Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Statistik_S4D0T0Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Statistik_S2D1T0Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Statistik_S0D2T0Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Statistik_S1D0T1Q0[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Statistik_S0D0T0Q1[i]/(1.0*usedFilesForAveraging*NrOfStars))+"\t"+df.format(Sum)+"\t"+df.format(1.0+Statistik_FunctionalityOfHeparin[i]/(28.0*usedFilesForAveraging*NrOfHeparin)));
			
		}
		
		HigherOrderDefectSaver.DateiSchliessen();
		
		
		
		
		for(int i = 0; i < Histogramm_S0D0T0Q0.GetNrBins(); i++)
		{
			//HigherOrderDefectSaver_Histogramm.setzeZeile("# p\t<S0D0T0D0>\t<S1D0T0Q0>\t<S2D0T0Q0>\t<S0D1T0Q0>\t<S3D0T0Q0>\t<S1D1T0Q0>\t<S0D0T1Q0>\t<S4D0T0Q0>\t<S2D1T0Q0>\t<S0D2T0Q0>\t<S1D0T1Q0>\t<S0D0T0Q1>\tSum=1\t<f_HEP>");
			double factor = 1./(1.0*usedFilesForAveraging*NrOfStars);
			
			// sum = 1 -> bonds = extentofReaction -> counting correct
			double Sum = 1.0 + Histogramm_S0D0T0Q0.GetRangeInBin(i)-0.25*(factor*Histogramm_S1D0T0Q0.GetCumulativeNrInBin(i)+2.*factor*Histogramm_S2D0T0Q0.GetCumulativeNrInBin(i)+2.*factor*Histogramm_S0D1T0Q0.GetCumulativeNrInBin(i)+3.*factor*Histogramm_S3D0T0Q0.GetCumulativeNrInBin(i)+3.*factor*Histogramm_S1D1T0Q0.GetCumulativeNrInBin(i)+3.*factor*Histogramm_S0D0T1Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S4D0T0Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S2D1T0Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S0D2T0Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S1D0T1Q0.GetCumulativeNrInBin(i)+4.*factor*Histogramm_S0D0T0Q1.GetCumulativeNrInBin(i));
		
			if(counterFiles[(int) Math.floor(Histogramm_S0D0T0Q0.GetRangeInBin(i)*4*NrOfStars)] == usedFilesForAveraging)
			HigherOrderDefectSaver_Histogramm.setzeZeile(df.format(Histogramm_S0D0T0Q0.GetRangeInBin(i))+"\t"+df.format(1.0-Histogramm_S0D0T0Q0.GetCumulativeNrInBinNormiert(i))+"\t"+df.format(factor*Histogramm_S1D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S2D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D1T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S3D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S1D1T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D0T1Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S4D0T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S2D1T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D2T0Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S1D0T1Q0.GetCumulativeNrInBin(i))+"\t"+df.format(factor*Histogramm_S0D0T0Q1.GetCumulativeNrInBin(i))+"\t"+df.format(Sum)+"\t"+df.format(1.0-Histogramm_FunctionalityOfHeparin.GetCumulativeNrInBin(i)/(28.0*usedFilesForAveraging*NrOfHeparin)));
			
		}
		
		HigherOrderDefectSaver_Histogramm.DateiSchliessen();
		
		SaverBondsAtGamma95.setzeZeile((gamma.replace('_', '.')) + " " + SingleBondsAtGamma95.ReturnM1()+ " " + DoubleBondsAtGamma95.ReturnM1()+ " " + TripleBondsAtGamma95.ReturnM1()+ " " + QuadBondsAtGamma95.ReturnM1());
		SaverBondsAtGamma95.DateiSchliessen();
		
		SaverBondsAtGamma90.setzeZeile((gamma.replace('_', '.')) + " " + SingleBondsAtGamma90.ReturnM1()+ " " + DoubleBondsAtGamma90.ReturnM1()+ " " + TripleBondsAtGamma90.ReturnM1()+ " " + QuadBondsAtGamma90.ReturnM1());
		SaverBondsAtGamma90.DateiSchliessen();
		
		
		SaverBondsAtGamma75.setzeZeile((gamma.replace('_', '.')) + " " + SingleBondsAtGamma75.ReturnM1()+ " " + DoubleBondsAtGamma75.ReturnM1()+ " " + TripleBondsAtGamma75.ReturnM1()+ " " + QuadBondsAtGamma75.ReturnM1());
		SaverBondsAtGamma75.DateiSchliessen();
		
		
		int counterFuncHEP=0;
		for(int i=0; i <= 28; i++)
			counterFuncHEP +=FunktionalitaetHeparin[i];
		
		double DurchschnittsfunktionalitaetHEP = 0.0;
		for(int i=0; i <= 28; i++)
			DurchschnittsfunktionalitaetHEP += i*(FunktionalitaetHeparin[i]/(1.0*counterFuncHEP));
		
		FunktionalitaetHeparinSaver.setzeZeile("# total sample: "+counterFuncHEP +"   ;  average functionality: "+DurchschnittsfunktionalitaetHEP);
		
		FunktionalitaetHeparinSaver.setzeZeile("# f <relative occurence> <absolute occurence> total: "+counterFuncHEP);
		
		
		for(int i=0; i <=28; i++)
			FunktionalitaetHeparinSaver.setzeZeile(i+ " " + (FunktionalitaetHeparin[i]/(1.0*counterFuncHEP))+ " " + (FunktionalitaetHeparin[i]) );
			 
		FunktionalitaetHeparinSaver.DateiSchliessen();
		
		double dummyFunc = 0.0;
		for(int i=0; i <= 28; i++)
			dummyFunc += (FunktionalitaetHeparin[i]/(1.0*counterFuncHEP));
		
		System.out.println("SummeFunktionalitaet: " +dummyFunc +"  == 1.0");
		
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1());
		
		
		BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(dirDst+"/Bonds_Distribution_SimpleDefects_Time_HepPEGConnectedGel_"+FileName+".batch", false);
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
		xmgrace.setzeZeile("page size 792, 612");
		xmgrace.setzeZeile("page scroll 5%");
		xmgrace.setzeZeile("page inout 5%");
		xmgrace.setzeZeile("link page off");
		xmgrace.setzeZeile("VIEW 0.150000, 0.150000, 1.150000, 0.850000");
	    xmgrace.setzeZeile("FOCUS G0");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ NXY \""+dirDst+"Bonds_PEG_SimpleDefects_Time_HepPEGConnectedGel_"+FileName+".dat\"");
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
	    xmgrace.setzeZeile(" yaxis ticklabel place both");
	    xmgrace.setzeZeile(" yaxis tick major 0.2");
	    xmgrace.setzeZeile(" yaxis tick minor 0.1");

	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 symbol 1");
	    xmgrace.setzeZeile(" s0 legend \"SingleBonds\"");

	    xmgrace.setzeZeile(" s1 line color 2");
	    xmgrace.setzeZeile(" s1 line linestyle 1");
	    xmgrace.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace.setzeZeile(" s1 symbol 2");
	    xmgrace.setzeZeile(" s1 legend \"DoubleBonds\"");
		 		
	    xmgrace.setzeZeile(" s2 line color 3");
	    xmgrace.setzeZeile(" s2 line linestyle 1");
	    xmgrace.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace.setzeZeile(" s2 symbol 3");
	    xmgrace.setzeZeile(" s2 legend \"TripleBonds\"");

	    xmgrace.setzeZeile(" s3 line color 4");
	    xmgrace.setzeZeile(" s3 line linestyle 1");
	    xmgrace.setzeZeile(" s3 line linewidth 1.5");
	    xmgrace.setzeZeile(" s3 symbol 4");
	    xmgrace.setzeZeile(" s3 legend \"QuadBonds\"");

	    xmgrace.setzeZeile(" s4 line color 11");
	    xmgrace.setzeZeile(" s4 line linestyle 1");
	    xmgrace.setzeZeile(" s4 line linewidth 1.5");
	    xmgrace.setzeZeile(" s4 symbol 5");
	    xmgrace.setzeZeile(" s4 symbol color 11");
	    xmgrace.setzeZeile(" s4 legend \"FreeEnds\"");

	    xmgrace.setzeZeile(" s5 line color 10");
	    xmgrace.setzeZeile(" s5 line linestyle 1");
	    xmgrace.setzeZeile(" s5 line linewidth 1.5");
	    xmgrace.setzeZeile(" s5 symbol 6");
	    xmgrace.setzeZeile(" s5 symbol color 10");
	    xmgrace.setzeZeile(" s5 legend \"CumBonds\"");

	    xmgrace.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\sHEP,init\\N=28; c="+dc.format(concentration)+"\"");

	    xmgrace.setzeZeile(" SAVEALL \""+dirDst+"Bonds_Distribution_SimpleDefects_Time_HepPEGConnectedGel_"+FileName+".agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+dirDst+"Bonds_Distribution_SimpleDefects_Time_HepPEGConnectedGel_"+FileName+".ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	    
	    try {
	    	  System.out.println("xmgrace -batch "+dirDst+"/Bonds_Distribution_SimpleDefects_Time_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
		      Runtime.getRuntime().exec("xmgrace -batch "+dirDst+"/Bonds_Distribution_SimpleDefects_Time_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
		    } catch (Exception e) {
		      System.err.println(e.toString());
		    }
		    
		BFMFileSaver xmgrace2 = new BFMFileSaver();
		xmgrace2.DateiAnlegen(dirDst+"/Bonds_Distribution_HigherOrderDefects_HepPEGConnectedGel_"+FileName+".batch", false);
		xmgrace2.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
		xmgrace2.setzeZeile("page size 792, 612");
		xmgrace2.setzeZeile("page scroll 5%");
		xmgrace2.setzeZeile("page inout 5%");
		xmgrace2.setzeZeile("link page off");
		xmgrace2.setzeZeile("VIEW 0.150000, 0.150000, 1.150000, 0.850000");
		xmgrace2.setzeZeile("FOCUS G0");
		xmgrace2.setzeZeile(" AUTOSCALE ONREAD None");
		xmgrace2.setzeZeile("READ NXY \""+dirDst+"Bonds_PEG_HigherOrderDefects_HepPEGConnectedGel_"+FileName+".dat\"");
		xmgrace2.setzeZeile(" world xmin 0");
		xmgrace2.setzeZeile(" world xmax 1");//225000");
		xmgrace2.setzeZeile(" world ymin 0");
		xmgrace2.setzeZeile(" world ymax 1.0");
		xmgrace2.setzeZeile(" xaxis label \"extent of reaction p\"");
		xmgrace2.setzeZeile(" xaxis TICK MAJOR on");
		xmgrace2.setzeZeile(" xaxis TICK MINOR on");
		xmgrace2.setzeZeile(" xaxis tick major 0.1");
		xmgrace2.setzeZeile(" xaxis tick minor 0.05");
		xmgrace2.setzeZeile(" yaxis label \"relative frequency\"");
		xmgrace2.setzeZeile(" yaxis ticklabel place both");
		xmgrace2.setzeZeile(" yaxis tick major 0.1");
		xmgrace2.setzeZeile(" yaxis tick minor 0.05");

		xmgrace2.setzeZeile(" s0 line color 1");
		xmgrace2.setzeZeile(" s0 line linestyle 1");
		xmgrace2.setzeZeile(" s0 line linewidth 1.5");
		xmgrace2.setzeZeile(" s0 symbol 1");
	    xmgrace2.setzeZeile(" s0 symbol color 1");
	    xmgrace2.setzeZeile(" s0 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s0 SYMBOL Skip 4");
	    //xmgrace2.setzeZeile(" s0 SYMBOL FILL pattern 1");
	    //xmgrace2.setzeZeile(" s0 SYMBOL FILL COLOR 1");
		xmgrace2.setzeZeile(" s0 legend \"S0D0T0D0\"");

		xmgrace2.setzeZeile(" s1 line color 2");
		xmgrace2.setzeZeile(" s1 line linestyle 1");
		xmgrace2.setzeZeile(" s1 line linewidth 1.5");
		xmgrace2.setzeZeile(" s1 symbol 2");
	    xmgrace2.setzeZeile(" s1 symbol color 2");
	    xmgrace2.setzeZeile(" s1 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s1 SYMBOL Skip 4");
	    //xmgrace2.setzeZeile(" s1 SYMBOL FILL COLOR 2");
		xmgrace2.setzeZeile(" s1 legend \"S1D0T0Q0\"");
			 		
		xmgrace2.setzeZeile(" s2 line color 3");
		xmgrace2.setzeZeile(" s2 line linestyle 1");
		xmgrace2.setzeZeile(" s2 line linewidth 1.5");
		xmgrace2.setzeZeile(" s2 symbol 3");
	    xmgrace2.setzeZeile(" s2 symbol color 3");
	    xmgrace2.setzeZeile(" s2 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s2 SYMBOL Skip 4");
		xmgrace2.setzeZeile(" s2 legend \"S2D0T0Q0\"");

		xmgrace2.setzeZeile(" s3 line color 6");
		xmgrace2.setzeZeile(" s3 line linestyle 1");
		xmgrace2.setzeZeile(" s3 line linewidth 1.5");
		xmgrace2.setzeZeile(" s3 symbol 4");
	    xmgrace2.setzeZeile(" s3 symbol color 6");
	    xmgrace2.setzeZeile(" s3 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s3 SYMBOL Skip 4");
		xmgrace2.setzeZeile(" s3 legend \"S0D1T0Q0\"");

		xmgrace2.setzeZeile(" s4 line color 4");
		xmgrace2.setzeZeile(" s4 line linestyle 1");
		xmgrace2.setzeZeile(" s4 line linewidth 1.5");
		xmgrace2.setzeZeile(" s4 symbol 11");
	    xmgrace2.setzeZeile(" s4 symbol color 4");
	    xmgrace2.setzeZeile(" s4 symbol char font 12");
	    xmgrace2.setzeZeile(" s4 symbol char 196");
	    xmgrace2.setzeZeile(" s4 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s4 SYMBOL Skip 4");
		xmgrace2.setzeZeile(" s4 legend \"S3D0T0Q0\"");

		xmgrace2.setzeZeile(" s5 line color 8");
		xmgrace2.setzeZeile(" s5 line linestyle 1");
		xmgrace2.setzeZeile(" s5 line linewidth 1.5");
		xmgrace2.setzeZeile(" s5 symbol 11");
	    xmgrace2.setzeZeile(" s5 symbol color 8");
	    xmgrace2.setzeZeile(" s5 symbol char font 12");
	    xmgrace2.setzeZeile(" s5 symbol char 170");
	    xmgrace2.setzeZeile(" s5 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s5 SYMBOL Skip 4");
		xmgrace2.setzeZeile(" s5 legend \"S1D1T0Q0\"");
		
		xmgrace2.setzeZeile(" s6 line color 9");
		xmgrace2.setzeZeile(" s6 line linestyle 1");
		xmgrace2.setzeZeile(" s6 line linewidth 1.5");
		xmgrace2.setzeZeile(" s6 symbol 6");
	    xmgrace2.setzeZeile(" s6 symbol color 9");
	    xmgrace2.setzeZeile(" s6 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s6 SYMBOL Skip 4");
		xmgrace2.setzeZeile(" s6 legend \"S0D0T1Q0\"");
		
		xmgrace2.setzeZeile(" s7 line color 10");
		xmgrace2.setzeZeile(" s7 line linestyle 1");
		xmgrace2.setzeZeile(" s7 line linewidth 1.5");
		xmgrace2.setzeZeile(" s7 symbol 10");
	    xmgrace2.setzeZeile(" s7 symbol color 10");
	    xmgrace2.setzeZeile(" s7 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s7 SYMBOL Skip 4");
		xmgrace2.setzeZeile(" s7 legend \"S4D0T0Q0\"");

		xmgrace2.setzeZeile(" s8 line color 11");
		xmgrace2.setzeZeile(" s8 line linestyle 1");
		xmgrace2.setzeZeile(" s8 line linewidth 1.5");
		xmgrace2.setzeZeile(" s8 symbol 11");
	    xmgrace2.setzeZeile(" s8 symbol color 11");
	    xmgrace2.setzeZeile(" s8 symbol char font 12");
	    xmgrace2.setzeZeile(" s8 symbol char 167");
	    xmgrace2.setzeZeile(" s8 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s8 SYMBOL Skip 4");
		xmgrace2.setzeZeile(" s8 legend \"S2D1T0Q0\"");

		xmgrace2.setzeZeile(" s9 line color 12");
		xmgrace2.setzeZeile(" s9 line linestyle 1");
		xmgrace2.setzeZeile(" s9 line linewidth 1.5");
		xmgrace2.setzeZeile(" s9 symbol 11");
	    xmgrace2.setzeZeile(" s9 symbol color 12");
	    xmgrace2.setzeZeile(" s9 symbol char font 12");
	    xmgrace2.setzeZeile(" s9 symbol char 169");
	    xmgrace2.setzeZeile(" s9 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s9 SYMBOL Skip 4");
		xmgrace2.setzeZeile(" s9 legend \"S0D2T0Q0\"");
		
		xmgrace2.setzeZeile(" s10 line color 13");
		xmgrace2.setzeZeile(" s10 line linestyle 1");
		xmgrace2.setzeZeile(" s10 line linewidth 1.5");
		xmgrace2.setzeZeile(" s10 symbol 5");
	    xmgrace2.setzeZeile(" s10 symbol color 13");
	    xmgrace2.setzeZeile(" s10 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s10 SYMBOL Skip 4");
		xmgrace2.setzeZeile(" s10 legend \"S1D0T1Q0\"");

		xmgrace2.setzeZeile(" s11 line color 14");
		xmgrace2.setzeZeile(" s11 line linestyle 1");
		xmgrace2.setzeZeile(" s11 line linewidth 1.5");
		xmgrace2.setzeZeile(" s11 symbol 7");
	    xmgrace2.setzeZeile(" s11 symbol color 14");
	    xmgrace2.setzeZeile(" s11 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s11 SYMBOL Skip 4");
		xmgrace2.setzeZeile(" s11 legend \"S0D0T0Q1\"");
		
		xmgrace2.setzeZeile(" s12 OFF");
		xmgrace2.setzeZeile(" s12 legend \"Sum=1\"");
		
		
		xmgrace2.setzeZeile(" s13 line color 15");
		xmgrace2.setzeZeile(" s13 line linestyle 1");
		xmgrace2.setzeZeile(" s13 line linewidth 1.5");
		xmgrace2.setzeZeile(" s13 symbol 11");
	    xmgrace2.setzeZeile(" s13 symbol color 15");
	    xmgrace2.setzeZeile(" s13 symbol char font 12");
	    xmgrace2.setzeZeile(" s13 symbol char 211");
	    xmgrace2.setzeZeile(" s13 SYMBOL SIZE 1");
	    xmgrace2.setzeZeile(" s13 SYMBOL Skip 8");
		xmgrace2.setzeZeile(" s13 legend \"f\\sHEP\\N/f\\sHEP,init\\N\"");
		
		xmgrace2.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\sHEP,init\\N=28; c="+dc.format(concentration)+"\"");

		xmgrace2.setzeZeile(" SAVEALL \""+dirDst+"Bonds_Distribution_HigherOrderDefects_HepPEGConnectedGel_"+FileName+".agr\"");

		xmgrace2.setzeZeile(" PRINT TO \""+dirDst+"Bonds_Distribution_HigherOrderDefects_HepPEGConnectedGel_"+FileName+".ps\"");
		xmgrace2.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
		xmgrace2.setzeZeile("PRINT");
			
		xmgrace2.DateiSchliessen();
		    
		try {
			 System.out.println("xmgrace -batch "+dirDst+"/Bonds_Distribution_HigherOrderDefects_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
			 Runtime.getRuntime().exec("xmgrace -batch "+dirDst+"/Bonds_Distribution_HigherOrderDefects_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
			} catch (Exception e)
			{
			  System.err.println(e.toString());
			}	
			
		BFMFileSaver xmgrace3 = new BFMFileSaver();
		xmgrace3.DateiAnlegen(dirDst+"/Bonds_Distribution_SimpleDefects_HepPEGConnectedGel_"+FileName+".batch", false);
		xmgrace3.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
		xmgrace3.setzeZeile("page size 792, 612");
		xmgrace3.setzeZeile("page scroll 5%");
		xmgrace3.setzeZeile("page inout 5%");
		xmgrace3.setzeZeile("link page off");
		xmgrace3.setzeZeile("VIEW 0.150000, 0.150000, 1.150000, 0.850000");
		xmgrace3.setzeZeile("FOCUS G0");
		xmgrace3.setzeZeile(" AUTOSCALE ONREAD None");
		xmgrace3.setzeZeile("READ NXY \""+dirDst+"Bonds_PEG_SimpleDefects_HepPEGConnectedGel_"+FileName+".dat\"");
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

		xmgrace3.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\sHEP,init\\N=28; c="+dc.format(concentration)+"\"");

		xmgrace3.setzeZeile(" SAVEALL \""+dirDst+"Bonds_Distribution_SimpleDefects_HepPEGConnectedGel_"+FileName+".agr\"");

		xmgrace3.setzeZeile(" PRINT TO \""+dirDst+"Bonds_Distribution_SimpleDefects_HepPEGConnectedGel_"+FileName+".ps\"");
		xmgrace3.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
		xmgrace3.setzeZeile("PRINT");
			
		xmgrace3.DateiSchliessen();
		    
		    try {
		    	  System.out.println("xmgrace -batch "+dirDst+"/Bonds_Distribution_SimpleDefects_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
		          Runtime.getRuntime().exec("xmgrace -batch "+dirDst+"/Bonds_Distribution_SimpleDefects_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
			    } catch (Exception e) {
			      System.err.println(e.toString());
			    }	
	
	NumberFormat nf = NumberFormat.getInstance(Locale.US);

	nf.setMaximumFractionDigits(2);
				    
			    
	BFMFileSaver xmgrace4 = new BFMFileSaver();
	xmgrace4.DateiAnlegen(dirDst+"/Bonds_HEP_Functionality_p0_90_HepPEGConnectedGel_"+FileName+".batch", false);
	xmgrace4.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	xmgrace4.setzeZeile("FOCUS G0");
	xmgrace4.setzeZeile(" AUTOSCALE ONREAD None");
	xmgrace4.setzeZeile("READ BLOCK \""+dirDst+"/Bonds_HEP_Functionality_p0_90_HepPEGConnectedGel_"+FileName+".dat\"");
	xmgrace4.setzeZeile("BLOCK xy \"1:2\"");
	    
	xmgrace4.setzeZeile(" world xmin 0");
	xmgrace4.setzeZeile(" world xmax 30");
	xmgrace4.setzeZeile(" world ymin 0");
	xmgrace4.setzeZeile(" world ymax 1.0");
	xmgrace4.setzeZeile(" xaxis label \"chem. functionality\"");
	xmgrace4.setzeZeile(" xaxis TICK MAJOR on");
	xmgrace4.setzeZeile(" xaxis TICK MINOR on");
	xmgrace4.setzeZeile(" xaxis tick major 5");
	xmgrace4.setzeZeile(" xaxis tick minor 1");
	//xmgrace.setzeZeile(" xaxis tick minor 10000");
	xmgrace4.setzeZeile(" yaxis label \"relative frequency\"");
	xmgrace4.setzeZeile(" yaxis tick major 0.2");
	xmgrace4.setzeZeile(" yaxis tick minor 0.1");

	xmgrace4.setzeZeile(" s0 line color 1");
	xmgrace4.setzeZeile(" s0 line linestyle 1");
	xmgrace4.setzeZeile(" s0 line linewidth 1.5");
	xmgrace4.setzeZeile(" s0 symbol 1");
	xmgrace4.setzeZeile(" s0 symbol color 1");
	xmgrace4.setzeZeile(" s0 legend \"HEP p=0.5 <f>="+nf.format(DurchschnittsfunktionalitaetHEP)+"\"");
	
	xmgrace4.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; q=1.0; f\\s0\\N="+nf.format((4.0*NrOfStars/NrOfHeparin))+"; f\\s0,Cross\\N="+nf.format((4.0*NrOfStars*0.9/NrOfHeparin))+"\"");

	xmgrace4.setzeZeile(" SAVEALL \""+dirDst+"/Bonds_HEP_Functionality_p0_90_HepPEGConnectedGel_"+FileName+".agr\"");

	xmgrace4.setzeZeile(" PRINT TO \""+dirDst+"/Bonds_HEP_Functionality_p0_90_HepPEGConnectedGel_"+FileName+".ps\"");
	xmgrace4.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	xmgrace4.setzeZeile("PRINT");
				
	xmgrace4.DateiSchliessen();
			    
	try {
			System.out.println("xmgrace -batch "+dirDst+"/Bonds_HEP_Functionality_p0_90_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
			Runtime.getRuntime().exec("xmgrace -batch "+dirDst+"/Bonds_HEP_Functionality_p0_90_HepPEGConnectedGel_"+FileName+".batch -nosafe -hardcopy");
			} catch (Exception e) {
			System.err.println(e.toString());
		}
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
			
		  addedBondsDuringSimulation = 0;
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
			
			
			
			
			
			
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 6)
		{
			System.out.println("Berechnung Bindungsstatistik von PEG+HEP");
			System.out.println("USAGE: dirSrc/ Hydrogel_HEP_hep__PEG_peg_NStar_length__NoPerXYZ128[__xxx.bfm] StringGamma dirDst/ StringExperiment Files");
		}
		else new Auswertung_Sterne_VerknuepfungHoehereDefekte_HepPEG(args[0], args[1], args[2] ,args[3], args[4], Integer.parseInt(args[5]));//,args[1],args[2]);
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
		NrOfHeparin = importData.NrOfHeparin;
		
		HeparinSterneVerknuepfung = null;
		HeparinSterneVerknuepfung = new int[NrOfHeparin][NrOfStars];
		
		PEGBonds = null;
		PEGBonds = new int[NrOfStars];
		
		
		PEGBondsDifferentHEP = null;
		PEGBondsDifferentHEP= new int[NrOfStars];
		

		


		
		System.out.println("Stars: " + NrOfStars + "    ArmLength: "+ NrOfMonomersPerStarArm + "    Heparin:"+NrOfHeparin);
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
					
					int PEGnr=0;
					int HEPnr=0;
					
					
					
					if(a < b) //a=Heparin, b=Peg-Stern
					{
						HeparinSterneVerknuepfung[(a-1)/90][(b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						
						PEGnr=(b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1);
						HEPnr=(a-1)/90;
						
						System.out.println("PEGnr: " +PEGnr + "    HEPnr: " + HEPnr);
						
						if(HeparinSterneVerknuepfung[HEPnr][PEGnr]==1)
							PEGBondsDifferentHEP[PEGnr]++;
					}
					else //b=Heparin, a=Peg-Stern
					{
						HeparinSterneVerknuepfung[(b-1)/90][(a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						
						PEGnr=(a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1);
						HEPnr=(b-1)/90;
						
						System.out.println("PEGnr: " +PEGnr + "    HEPnr: " + HEPnr);
						
						if(HeparinSterneVerknuepfung[HEPnr][PEGnr]==1)
							PEGBondsDifferentHEP[PEGnr]++;
					}
					
					addedBondsDuringSimulation++;
					
					counterFiles[addedBondsDuringSimulation]++;
					
					Statistik_S0D0T0Q0_OneRun[addedBondsDuringSimulation] = Statistik_S0D0T0Q0_OneRun[addedBondsDuringSimulation-1];
					Statistik_S1D0T0Q0_OneRun[addedBondsDuringSimulation] = Statistik_S1D0T0Q0_OneRun[addedBondsDuringSimulation-1];
					Statistik_S2D0T0Q0_OneRun[addedBondsDuringSimulation] = Statistik_S2D0T0Q0_OneRun[addedBondsDuringSimulation-1];
					Statistik_S0D1T0Q0_OneRun[addedBondsDuringSimulation] = Statistik_S0D1T0Q0_OneRun[addedBondsDuringSimulation-1];
					Statistik_S3D0T0Q0_OneRun[addedBondsDuringSimulation] = Statistik_S3D0T0Q0_OneRun[addedBondsDuringSimulation-1];
					Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation] = Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation-1];
					Statistik_S0D0T1Q0_OneRun[addedBondsDuringSimulation] = Statistik_S0D0T1Q0_OneRun[addedBondsDuringSimulation-1]; 
					Statistik_S4D0T0Q0_OneRun[addedBondsDuringSimulation] = Statistik_S4D0T0Q0_OneRun[addedBondsDuringSimulation-1];
					Statistik_S2D1T0Q0_OneRun[addedBondsDuringSimulation] = Statistik_S2D1T0Q0_OneRun[addedBondsDuringSimulation-1]; 
					Statistik_S0D2T0Q0_OneRun[addedBondsDuringSimulation] = Statistik_S0D2T0Q0_OneRun[addedBondsDuringSimulation-1]; 
					Statistik_S1D0T1Q0_OneRun[addedBondsDuringSimulation] = Statistik_S1D0T1Q0_OneRun[addedBondsDuringSimulation-1]; 
					Statistik_S0D0T0Q1_OneRun[addedBondsDuringSimulation] = Statistik_S0D0T0Q1_OneRun[addedBondsDuringSimulation-1];
					
					Statistik_Singlebonds_OneRun[addedBondsDuringSimulation] = Statistik_Singlebonds_OneRun[addedBondsDuringSimulation-1];
					Statistik_Doublebonds_OneRun[addedBondsDuringSimulation] = Statistik_Doublebonds_OneRun[addedBondsDuringSimulation-1];
					Statistik_Triplebonds_OneRun[addedBondsDuringSimulation] = Statistik_Triplebonds_OneRun[addedBondsDuringSimulation-1];
					Statistik_Quadbonds_OneRun[addedBondsDuringSimulation] = Statistik_Quadbonds_OneRun[addedBondsDuringSimulation-1];
					Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation] = Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation-1];
					
					Statistik_FunctionalityOfHeparin_OneRun[addedBondsDuringSimulation] = Statistik_FunctionalityOfHeparin_OneRun[addedBondsDuringSimulation-1];
					
					Statistik_FunctionalityOfHeparin_OneRun[addedBondsDuringSimulation]--;
					Histogramm_FunctionalityOfHeparin.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
					
					
					if(PEGBonds[PEGnr]==1)
					{
						Statistik_S1D0T0Q0_OneRun[addedBondsDuringSimulation]++;
						Statistik_S0D0T0Q0_OneRun[addedBondsDuringSimulation]--;
						
						Histogramm_S1D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						Histogramm_S0D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
						
						Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
						Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
					}
					
					if(PEGBonds[PEGnr]==2)
					{	
						if(HeparinSterneVerknuepfung[HEPnr][PEGnr]==1)
						{
							Statistik_S2D0T0Q0_OneRun[addedBondsDuringSimulation]++;
							Statistik_S1D0T0Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S2D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S1D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
							Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
						if(HeparinSterneVerknuepfung[HEPnr][PEGnr]==2)
						{
							Statistik_S0D1T0Q0_OneRun[addedBondsDuringSimulation]++;
							Statistik_S1D0T0Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S0D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S1D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
							Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
							Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
					}
					
					if(PEGBonds[PEGnr]==3)
					{	
						if(HeparinSterneVerknuepfung[HEPnr][PEGnr]==1)
						{
							if(PEGBondsDifferentHEP[PEGnr]==3)
							{
								Statistik_S3D0T0Q0_OneRun[addedBondsDuringSimulation]++;
								Statistik_S2D0T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S3D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S2D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
								Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
							if(PEGBondsDifferentHEP[PEGnr]==2)
							{
								Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]++;
								Statistik_S0D1T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S1D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S0D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
								Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
						}
						
						if(HeparinSterneVerknuepfung[HEPnr][PEGnr]==2)
						{
							Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]++;
							Statistik_S2D0T0Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S1D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S2D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
							Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
							Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
						
						if(HeparinSterneVerknuepfung[HEPnr][PEGnr]==3)
						{
							Statistik_S0D0T1Q0_OneRun[addedBondsDuringSimulation]++;
							Statistik_S0D1T0Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S0D0T1Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S0D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							Statistik_Triplebonds_OneRun[addedBondsDuringSimulation]++;
							Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]--;
							Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
					}
					
					if(PEGBonds[PEGnr]==4)
					{	
						if(HeparinSterneVerknuepfung[HEPnr][PEGnr]==1)
						{
							if(PEGBondsDifferentHEP[PEGnr]==4)
							{
								Statistik_S4D0T0Q0_OneRun[addedBondsDuringSimulation]++;
								Statistik_S3D0T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S4D0T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S3D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
								Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
								Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
							if(PEGBondsDifferentHEP[PEGnr]==3)
							{
								Statistik_S2D1T0Q0_OneRun[addedBondsDuringSimulation]++;
								Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S2D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S1D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
								Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
							if(PEGBondsDifferentHEP[PEGnr]==2)
							{
								Statistik_S1D0T1Q0_OneRun[addedBondsDuringSimulation]++;
								Statistik_S0D0T1Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S1D0T1Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S0D0T1Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]++;
								Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
						}
						
						if(HeparinSterneVerknuepfung[HEPnr][PEGnr]==2)
						{
							if(PEGBondsDifferentHEP[PEGnr]==3)
							{
								Statistik_S2D1T0Q0_OneRun[addedBondsDuringSimulation]++;
								Statistik_S3D0T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S2D1T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S3D0T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
								Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
								Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
							
							if(PEGBondsDifferentHEP[PEGnr]==2)
							{
								Statistik_S0D2T0Q0_OneRun[addedBondsDuringSimulation]++;
								Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]--;
								
								Histogramm_S0D2T0Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								Histogramm_S1D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
								
								Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]++;
								Statistik_Singlebonds_OneRun[addedBondsDuringSimulation]--;
								Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
							}
						}
						
						if(HeparinSterneVerknuepfung[HEPnr][PEGnr]==3)
						{
							Statistik_S1D0T1Q0_OneRun[addedBondsDuringSimulation]++;
							Statistik_S1D1T0Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S1D0T1Q0.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S1D1T0Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							Statistik_Triplebonds_OneRun[addedBondsDuringSimulation]++;
							Statistik_Doublebonds_OneRun[addedBondsDuringSimulation]--;
							Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
						
						if(HeparinSterneVerknuepfung[HEPnr][PEGnr]==4)
						{
							Statistik_S0D0T0Q1_OneRun[addedBondsDuringSimulation]++;
							Statistik_S0D0T1Q0_OneRun[addedBondsDuringSimulation]--;
							
							Histogramm_S0D0T0Q1.AddValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							Histogramm_S0D0T1Q0.ReduceValue(addedBondsDuringSimulation/(4.0*NrOfStars));
							
							Statistik_Quadbonds_OneRun[addedBondsDuringSimulation]++;
							Statistik_Triplebonds_OneRun[addedBondsDuringSimulation]--;
							Statistik_DenglingEnds_OneRun[addedBondsDuringSimulation]--;
						}
						
					}
					
				//	Statistik_S0D0T0Q0[addedBondsDuringSimulation]
				//	Singlebonds[(int) (importData.MCSTime/(deltaT))].AddValue(cou
					
					if((addedBondsDuringSimulation/(4.0*NrOfStars) >= 0.74) && (addedBondsDuringSimulation/(4.0*NrOfStars) <= 0.76))
					{
						int counterSinglebonds = 0;
						int counterDoublebonds = 0;
						int counterTriplebonds = 0;
						int counterQuadbonds = 0;
						
						for(int i = 0; i < NrOfHeparin; i++)
							for(int j = 0; j < NrOfStars; j++)
							{
								if(HeparinSterneVerknuepfung[i][j] == 1)
									counterSinglebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 2)
									counterDoublebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 3)
									counterTriplebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 4)
									counterQuadbonds++;
							}
						
						SingleBondsAtGamma75.AddValue(counterSinglebonds/(4.0*NrOfStars));
						DoubleBondsAtGamma75.AddValue(2.0*counterDoublebonds/(4.0*NrOfStars));
						TripleBondsAtGamma75.AddValue(3.0*counterTriplebonds/(4.0*NrOfStars));
						QuadBondsAtGamma75.AddValue(4.0*counterQuadbonds/(4.0*NrOfStars));
					}
					
					if((addedBondsDuringSimulation/(4.0*NrOfStars) >= 0.89) && (addedBondsDuringSimulation/(4.0*NrOfStars) <= 0.91))
					{
						int counterSinglebonds = 0;
						int counterDoublebonds = 0;
						int counterTriplebonds = 0;
						int counterQuadbonds = 0;
						
						for(int i = 0; i < NrOfHeparin; i++)
							for(int j = 0; j < NrOfStars; j++)
							{
								if(HeparinSterneVerknuepfung[i][j] == 1)
									counterSinglebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 2)
									counterDoublebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 3)
									counterTriplebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 4)
									counterQuadbonds++;
							}
						
						SingleBondsAtGamma90.AddValue(counterSinglebonds/(4.0*NrOfStars));
						DoubleBondsAtGamma90.AddValue(2.0*counterDoublebonds/(4.0*NrOfStars));
						TripleBondsAtGamma90.AddValue(3.0*counterTriplebonds/(4.0*NrOfStars));
						QuadBondsAtGamma90.AddValue(4.0*counterQuadbonds/(4.0*NrOfStars));
					}
					
					if((addedBondsDuringSimulation/(4.0*NrOfStars) >= 0.94) && (addedBondsDuringSimulation/(4.0*NrOfStars) <= 0.96))
					{
						int counterSinglebonds = 0;
						int counterDoublebonds = 0;
						int counterTriplebonds = 0;
						int counterQuadbonds = 0;
						
						for(int i = 0; i < NrOfHeparin; i++)
							for(int j = 0; j < NrOfStars; j++)
							{
								if(HeparinSterneVerknuepfung[i][j] == 1)
									counterSinglebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 2)
									counterDoublebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 3)
									counterTriplebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 4)
									counterQuadbonds++;
							}
						
						SingleBondsAtGamma95.AddValue(counterSinglebonds/(4.0*NrOfStars));
						DoubleBondsAtGamma95.AddValue(2.0*counterDoublebonds/(4.0*NrOfStars));
						TripleBondsAtGamma95.AddValue(3.0*counterTriplebonds/(4.0*NrOfStars));
						QuadBondsAtGamma95.AddValue(4.0*counterQuadbonds/(4.0*NrOfStars));
					}
					
					if((addedBondsDuringSimulation/(4.0*NrOfStars) >= 0.895) && (addedBondsDuringSimulation/(4.0*NrOfStars) <= 0.905))
					{
					for(int hep = 0; hep < NrOfHeparin; hep++)
					{
						int counterFunktionalitaet = 0;
						for(int u = 0; u < NrOfStars; u++)
						{
							if(HeparinSterneVerknuepfung[hep][u] != 0)
								counterFunktionalitaet+=HeparinSterneVerknuepfung[hep][u];
						}
						
						if(counterFunktionalitaet == 0)
							FunktionalitaetHeparin[0]++;
						else
							FunktionalitaetHeparin[counterFunktionalitaet]++;
					}
					}
					
				}
				
				int counterSinglebonds = 0;
				int counterDoublebonds = 0;
				int counterTriplebonds = 0;
				int counterQuadbonds = 0;
				int counterDenglingEnds = 0;
				
				for(int i = 0; i < NrOfHeparin; i++)
					for(int j = 0; j < NrOfStars; j++)
					{
						if(HeparinSterneVerknuepfung[i][j] == 1)
							counterSinglebonds++;
						else if (HeparinSterneVerknuepfung[i][j] == 2)
							counterDoublebonds++;
						else if (HeparinSterneVerknuepfung[i][j] == 3)
							counterTriplebonds++;
						else if (HeparinSterneVerknuepfung[i][j] == 4)
							counterQuadbonds++;
						else if (HeparinSterneVerknuepfung[i][j] > 4)
						{
							System.out.println("Verknuepfung 5! Sollte nicht vorkommen! Wert: " +HeparinSterneVerknuepfung[i][j] + " bei Hep:" + (i+1) + "   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
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
				
				/*int counter_S0D0T0Q0 = 0;
				int counter_S1D0T0Q0 = 0;
				int counter_S2D0T0Q0 = 0;
				int counter_S0D1T0Q0 = 0;
				int counter_S3D0T0Q0 = 0;
				int counter_S1D1T0Q0 = 0;
				int counter_S0D0T1Q0 = 0;
				int counter_S4D0T0Q0 = 0;
				int counter_S2D1T0Q0 = 0;
				int counter_S0D2T0Q0 = 0;
				int counter_S1D0T1Q0 = 0;
				int counter_S0D0T0Q1 = 0;
				
				for(int i = 0; i < NrOfHeparin; i++)
					for(int j = 0; j < NrOfStars; j++)
					{
						if(HeparinSterneVerknuepfung[i][j] == 1)
							counterSinglebonds++;
						else if (HeparinSterneVerknuepfung[i][j] == 2)
							counterDoublebonds++;
						else if (HeparinSterneVerknuepfung[i][j] == 3)
							counterTriplebonds++;
						else if (HeparinSterneVerknuepfung[i][j] == 4)
							counterQuadbonds++;
						else if (HeparinSterneVerknuepfung[i][j] > 4)
						{
							System.out.println("Verknuepfung 5! Sollte nicht vorkommen! Wert: " +HeparinSterneVerknuepfung[i][j] + " bei Hep:" + (i+1) + "   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
							System.exit(1); 
						}
					}*/
				
				Singlebonds[(int) (importData.MCSTime/(deltaT))].AddValue(counterSinglebonds/(4.0*NrOfStars));
				Doublebonds[(int) (importData.MCSTime/(deltaT))].AddValue(2.0*counterDoublebonds/(4.0*NrOfStars));
				Triplebonds[(int) (importData.MCSTime/(deltaT))].AddValue(3.0*counterTriplebonds/(4.0*NrOfStars));
				Quadbonds[(int) (importData.MCSTime/(deltaT))].AddValue(4.0*counterQuadbonds/(4.0*NrOfStars));
				
				DenglingEnds[(int) (importData.MCSTime/(deltaT))].AddValue((4.0*NrOfStars-counterSinglebonds-2.0*counterDoublebonds-3.0*counterTriplebonds-4.0*counterQuadbonds)/(4.0*NrOfStars));
				
				DenglingEnds2[(int) (importData.MCSTime/(deltaT))].AddValue((counterDenglingEnds)/(4.0*NrOfStars));
				
				
				bonds[(int) (importData.MCSTime/(deltaT))].AddValue(importData.additionalbonds.size());
				//System.out.println("size: " +bonds[(int) (importData.MCSTime/(10000))].ReturnN());
				System.out.println("size: " +importData.addedBondsBetweenFrames.size() + "  "+ addedBondsDuringSimulation);
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

	private int getMono1Nr(long obj)//int mono)
	   {
		   return ((int) ( obj & 2147483647));   
	   }
	   
	private int getMono2Nr(long obj)//(int mono)
	   {   
		return (int) (( (obj >> 31) & 2147483647));
	   }
	   
}
