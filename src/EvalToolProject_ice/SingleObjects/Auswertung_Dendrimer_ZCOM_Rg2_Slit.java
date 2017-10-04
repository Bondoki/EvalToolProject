package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Gamma;
import EvalToolProject_ice.tools.GammaFunction;
import EvalToolProject_ice.tools.Histogramm;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Int_IntArrayList_Table;
import EvalToolProject_ice.tools.Statistik;

import EvalToolProject_ice.tools.HistogrammStatistik;

public class Auswertung_Dendrimer_ZCOM_Rg2_Slit {


	
	
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
	
	Statistik[] ZCOM_stat;
	
	Histogramm[] HG_ZCOM_Plain;
	HistogrammStatistik[] HG_ZCOM_Statistik;
	HistogrammStatistik HG_ZCOM_Statistik_NormAll;
	
	HistogrammStatistik HG_ZCOM_StatistikTotal;
	HistogrammStatistik HG_ZCOM_Rg2Para_StatistikTotal;
	HistogrammStatistik HG_ZCOM_Rg2Perp_StatistikTotal;
	
	HistogrammStatistik HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal;
	HistogrammStatistik HG_ZCOM_Rg2ParaCorrectedWHAMNormalization_StatistikTotal;
	HistogrammStatistik HG_ZCOM_Rg2ParaCorrectedWHAMGeneralNorm_StatistikTotal;
	
	HistogrammStatistik HG_ZCOM_Rg2PerpCorrectedWHAM_StatistikTotal;
	HistogrammStatistik HG_ZCOM_Rg2PerpCorrectedWHAMNormalization_StatistikTotal;
	
	HistogrammStatistik HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal;
	HistogrammStatistik HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal;
	
	HistogrammStatistik[] HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_Statistik;
	HistogrammStatistik[] HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_Statistik;
	
	BFMFileSaver histo_ZCOM;
	
	Int_IntArrayList_Table Bindungsnetzwerk; 
	
	long deltaT;
	
	double[] EquLength;
	double[] OffSetF;
	
	
	String dstDir;
	
	
	double MaximumPotentialBias;
	
	public Auswertung_Dendrimer_ZCOM_Rg2_Slit(String fdir, String fname, String dirDst)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileNameWithEnd  = fname;
		FileName = fname.replaceAll(".bfm", "").replaceFirst(".xo", "");
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		dstDir = dirDst;
		
		System.out.println("-7%6="+ (-7%6));
		
		/*
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ FileNameWithEnd);
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		
		//End - Determine MaxFrame out of the first file
		*/
		
		skipFrames =  3;//10;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		int NrOfAllHistogramms = 247;
		
		int numBin = 256*4;//128*2;
		double minHG =-0.5;
		double maxHG =256*2-0.5;//128.0-0.5;
		
		
		ZCOM_stat= new Statistik[NrOfAllHistogramms];
		HG_ZCOM_Plain = new Histogramm[NrOfAllHistogramms];
		HG_ZCOM_Statistik = new HistogrammStatistik[NrOfAllHistogramms];
		
		EquLength= new double[NrOfAllHistogramms];
		OffSetF= new double[NrOfAllHistogramms];
		
		HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_Statistik = new HistogrammStatistik[NrOfAllHistogramms];
		HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_Statistik = new HistogrammStatistik[NrOfAllHistogramms];
		
		for(int i = 0; i < NrOfAllHistogramms; i++)
		//for(int i = 0; i < 21; i++)
		{
			ZCOM_stat[i] =new Statistik();
			HG_ZCOM_Plain[i] = new Histogramm(minHG, maxHG, numBin);
			HG_ZCOM_Statistik[i] = new HistogrammStatistik(minHG, maxHG, numBin);
			HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_Statistik[i] = new HistogrammStatistik(minHG, maxHG, numBin);
			HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_Statistik[i] = new HistogrammStatistik(minHG, maxHG, numBin);
			
			OffSetF[i]=0.0;
			EquLength[i]=10.0+1.0*i;//1.0+2.0*i;//
			System.out.println("EquLength["+i+"]: " + EquLength[i]);
		}
		
		/*for(int i = 21; i < NrOfAllHistogramms; i++)
		{
			ZCOM_stat[i] =new Statistik();
			HG_ZCOM_Plain[i] = new Histogramm(minHG, maxHG, numBin);
			HG_ZCOM_Statistik[i] = new HistogrammStatistik(minHG, maxHG, numBin);
			HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_Statistik[i] = new HistogrammStatistik(minHG, maxHG, numBin);
			HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_Statistik[i] = new HistogrammStatistik(minHG, maxHG, numBin);
			
			OffSetF[i]=0.0;
			EquLength[i]=20.0+2.0*(i-20);//1.0+2.0*i;//
			System.out.println("EquLength["+i+"]: " + EquLength[i]);
		}*/
		
		HG_ZCOM_StatistikTotal = new HistogrammStatistik(minHG, maxHG, numBin);
		
		HG_ZCOM_Rg2Para_StatistikTotal = new HistogrammStatistik(minHG, maxHG, numBin);
		HG_ZCOM_Rg2Perp_StatistikTotal = new HistogrammStatistik(minHG, maxHG, numBin);
		
		HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal = new HistogrammStatistik(minHG, maxHG, numBin);
		HG_ZCOM_Rg2ParaCorrectedWHAMNormalization_StatistikTotal = new HistogrammStatistik(minHG, maxHG, numBin);
		
		HG_ZCOM_Rg2PerpCorrectedWHAM_StatistikTotal = new HistogrammStatistik(minHG, maxHG, numBin);
		HG_ZCOM_Rg2PerpCorrectedWHAMNormalization_StatistikTotal = new HistogrammStatistik(minHG, maxHG, numBin);
		
		HG_ZCOM_Rg2ParaCorrectedWHAMGeneralNorm_StatistikTotal= new HistogrammStatistik(minHG, maxHG, numBin);
		
		HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal = new HistogrammStatistik(minHG, maxHG, numBin);
		HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal = new HistogrammStatistik(minHG, maxHG, numBin);
		
		HG_ZCOM_Statistik_NormAll = new HistogrammStatistik(minHG, maxHG, numBin); 
		/*histo_ZCOM= new BFMFileSaver();
		histo_ZCOM.DateiAnlegen(dirDst+"/"+FileName+"_Histo_ZCOM.dat", false);
		histo_ZCOM.setzeZeile("# radial probability distributions p(r)");
		histo_ZCOM.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = p0*int_0 to inf p(r)*r^2 dr=1");
		histo_ZCOM.setzeZeile("# intervall from ["+HG_ZCOM.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM.GetRangeInBinLowerLimit(HG_ZCOM.GetNrBins())+"]");
		histo_ZCOM.setzeZeile("# intervall thickness: dI="+HG_ZCOM.GetIntervallThickness());
		*/
		DecimalFormat dh = new DecimalFormat("000");
		
		
		//LoadFile(FileNameWithEnd, 1);
		for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
		{
			
			LoadFile(fname+"Z"+(int)(EquLength[nrHisto])+"_Slit.bfm",100,nrHisto);
			//LoadFile(fname+"L"+(int)(EquLength[nrHisto])+".bfm",100,nrHisto);
			//LoadFile(fname+"L"+(int)(EquLength[nrHisto])+"_reduced.bfm",100,nrHisto);
			//LoadFile(fname+"R_"+EquLength[nrHisto]+"_C_0.2_HardColloids.bfm",1,nrHisto);
		}
		/*
		LoadFile(fname+"R_3.0.bfm",1,0);
		LoadFile(fname+"R_3.5.bfm",1,1);
		LoadFile(fname+"R_4.0.bfm",1,2);
		LoadFile(fname+"R_4.5.bfm",1,3);
		LoadFile(fname+"R_5.0.bfm",1,4);
		LoadFile(fname+"R_5.5.bfm",1,5);
		LoadFile(fname+"R_6.0.bfm",1,6);
		LoadFile(fname+"R_6.5.bfm",1,7);
		LoadFile(fname+"R_7.0.bfm",1,8);
		LoadFile(fname+"R_7.5.bfm",1,9);
		LoadFile(fname+"R_8.0.bfm",1,10);
		LoadFile(fname+"R_8.5.bfm",1,11);
		LoadFile(fname+"R_9.0.bfm",1,12);
		LoadFile(fname+"R_9.5.bfm",1,13);
		LoadFile(fname+"R_10.0.bfm",1,14);
		LoadFile(fname+"R_10.5.bfm",1,15);
		LoadFile(fname+"R_11.0.bfm",1,16);
		LoadFile(fname+"R_11.5.bfm",1,17);
		LoadFile(fname+"R_12.0.bfm",1,18);
		LoadFile(fname+"R_12.5.bfm",1,19);
		*/
		
		//perform the normalization of histograms to spherical coordinates
		for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
		for(int i = 0; i < HG_ZCOM_Plain[nrHisto].GetNrBins(); i++)
		{
			//sphere
			//double testnorm = (HG_ZCOM_Plain[nrHisto].GetNrInBinNormiert(i)/(4*Math.PI*HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)*HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)*HG_ZCOM_Plain[nrHisto].GetIntervallThickness()));
		
			//1D - linear
			double testnorm = (HG_ZCOM_Plain[nrHisto].GetNrInBinNormiert(i)/(1.0*HG_ZCOM_Plain[nrHisto].GetIntervallThickness()));
			
			
			HG_ZCOM_Statistik[nrHisto].AddValue(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i), testnorm);
		}
		
		for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
		{
		BFMFileSaver histo= new BFMFileSaver();
		histo.DateiAnlegen(dirDst+"/"+FileName+"Histo_"+EquLength[nrHisto]+".dat", false);
		//histo.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
		histo.setzeZeile("# normalized to unity: int_0 to inf p(r) dz = 1");
		
		histo.setzeZeile("# intervall from ["+HG_ZCOM_Statistik[nrHisto].GetRangeInBinLowerLimit(0)+";"+HG_ZCOM_Statistik[nrHisto].GetRangeInBinLowerLimit(HG_ZCOM_Statistik[nrHisto].GetNrBins())+"]");
		histo.setzeZeile("# intervall thickness: dI="+HG_ZCOM_Statistik[nrHisto].GetIntervallThickness());
		histo.setzeZeile("# nr of counts: " + HG_ZCOM_Statistik[nrHisto].GetNrOfCounts());
		histo.setzeZeile("# r  p(r) F=-ln(p)");
		
		for(int i = 0; i < HG_ZCOM_Statistik[nrHisto].GetNrBins(); i++)
		{
			if(HG_ZCOM_Statistik[nrHisto].GetAverageInBin(i) != 0)
				histo.setzeZeile(HG_ZCOM_Statistik[nrHisto].GetRangeInBin(i)+" "+HG_ZCOM_Statistik[nrHisto].GetAverageInBin(i) + " "+(-Math.log(HG_ZCOM_Statistik[nrHisto].GetAverageInBin(i))));
		}
		
		histo.DateiSchliessen();
		}
		
		//normalizing the average per bin for probability of all histograms
		for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
		{
			for(int i = 0; i < HG_ZCOM_Statistik[nrHisto].GetNrBins(); i++)
			{
				HG_ZCOM_Statistik_NormAll.AddValue(HG_ZCOM_Statistik[nrHisto].GetRangeInBin(i), HG_ZCOM_Statistik[nrHisto].GetAverageInBin(i));
			}
		}
		
	//perform all calculation to achieve selfconsitency
	double sumSquare = 0.0;
	double errorSumSquareFreeEnergy=0.000001;
	int selfconsitency = 0;
				
	//perform all calculation to achieve selfconsitency
	//for(int selfconsitency = 0; selfconsitency < 20000; selfconsitency++)
	/*
	do{
		//calculate the normalization of total histograms
		double normTotal = 0.0;
		double weightTotal = 0.0;
		
		for(int i = 0; i < HG_ZCOM_StatistikTotal.GetNrBins(); i++)
		{
			normTotal = 0.0;
			weightTotal = 0.0;
			
			for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
			{
				//double normalisation=4*Math.PI*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)*HG_ZCOM_Plain[nrHisto].GetRangeInBin(i));
			
				normTotal += HG_ZCOM_Plain[nrHisto].GetNrOfCounts()*Math.exp(- (0.5*springconstant*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto]) - OffSetF[nrHisto]) );
				weightTotal += HG_ZCOM_Plain[nrHisto].GetNrOfCounts()*HG_ZCOM_Statistik[nrHisto].GetAverageInBin(i);
			}
			
			// there are entries for the PMF
			if(normTotal != 0.0)
			{
				//double normalisation=4*Math.PI*(HG_ZCOM_StatistikTotal.GetRangeInBin(i)*HG_ZCOM_StatistikTotal.GetRangeInBin(i)*HG_ZCOM_StatistikTotal.GetIntervallThickness());
				
				HG_ZCOM_StatistikTotal.CleanBin(i);
				HG_ZCOM_StatistikTotal.AddValue(HG_ZCOM_StatistikTotal.GetRangeInBin(i), weightTotal/(normTotal));
			}
			else HG_ZCOM_StatistikTotal.CleanBin(i);
			
		}
		
		//dump the mean probability
		System.out.println("PMF-prob");
		//for(int i = 0; i < HG_ZCOM_StatistikTotal.GetNrBins(); i++)
		//{
		//	System.out.println(HG_ZCOM_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_StatistikTotal.GetAverageInBin(i));
		//}
		
		
		System.out.println("Iteration: " + selfconsitency);
		//perform the integration to get the OffsetF
		sumSquare = 0.0;
		
		for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
		{
			double integral = 0.0;
			
			for(int i = 0; i < HG_ZCOM_Plain[nrHisto].GetNrBins(); i++)
			{
				//double normalisation=4*Math.PI*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)*HG_ZCOM_Plain[nrHisto].GetRangeInBin(i));
				//integral += normalisation*HG_ZCOM_Plain[nrHisto].GetIntervallThickness()*HG_ZCOM_StatistikTotal.GetAverageInBin(i)*Math.exp(- (0.5*0.6*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])));
				
				// sphere
				//integral += 4*Math.PI*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)*HG_ZCOM_Plain[nrHisto].GetRangeInBin(i))*HG_ZCOM_Plain[nrHisto].GetIntervallThickness()*HG_ZCOM_StatistikTotal.GetAverageInBin(i)*Math.exp(- (0.5*springconstant*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])));
			
				// 1D linear
				integral += HG_ZCOM_Plain[nrHisto].GetIntervallThickness()*HG_ZCOM_StatistikTotal.GetAverageInBin(i)*Math.exp(- (0.5*springconstant*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])));
			
			}
			sumSquare += (-Math.log(integral)-OffSetF[nrHisto])*(-Math.log(integral)-OffSetF[nrHisto]);
			
			OffSetF[nrHisto] = -Math.log(integral);
			System.out.println("OffSetF["+nrHisto+"]: " + OffSetF[nrHisto]);
		}
		
		// reduce all OffSet to zero by forcing last window to be OffSetF[NrOfAllHistogramms]=0.0
		
		for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
		{
			OffSetF[nrHisto] -= OffSetF[NrOfAllHistogramms-1];
			//System.out.println("OffSetF["+nrHisto+"]: " + OffSetF[nrHisto]);
		}
		
		
	System.out.println("SumOffSetF^2: " + sumSquare);
	selfconsitency++;

	}while(sumSquare > errorSumSquareFreeEnergy);
	
	*/
	
	//file dump
	
	BFMFileSaver histo_RPMF= new BFMFileSaver();
	histo_RPMF.DateiAnlegen(dirDst+"/"+FileName+"Histo_PMF_WHAM.dat", false);
	//histo_RPMF.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
	histo_RPMF.setzeZeile("# normalized to unity: int_0 to inf p(r) dz = 1");
	histo_RPMF.setzeZeile("# calculation of the mean free energy F using WHAM");
	histo_RPMF.setzeZeile("# intervall from ["+HG_ZCOM_StatistikTotal.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM_StatistikTotal.GetRangeInBinLowerLimit(HG_ZCOM_StatistikTotal.GetNrBins())+"]");
	histo_RPMF.setzeZeile("# intervall thickness: dI="+HG_ZCOM_StatistikTotal.GetIntervallThickness());
	histo_RPMF.setzeZeile("# r  p(r) F=-ln(p)");
	
	for(int i = 0; i < HG_ZCOM_StatistikTotal.GetNrBins(); i++)
	{
		if(HG_ZCOM_StatistikTotal.GetAverageInBin(i) != 0)
			histo_RPMF.setzeZeile(HG_ZCOM_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_StatistikTotal.GetAverageInBin(i) + " "+(-Math.log(HG_ZCOM_StatistikTotal.GetAverageInBin(i))));
	}
	
	histo_RPMF.DateiSchliessen();
	
	BFMFileSaver histo_PMF_Offset= new BFMFileSaver();
	histo_PMF_Offset.DateiAnlegen(dirDst+"/"+FileName+"Histo_PMF_Offset_WHAM.dat", false);
	//histo_RPMF.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
	histo_PMF_Offset.setzeZeile("# normalized to unity: int_0 to inf p(r) dz = 1");
	histo_PMF_Offset.setzeZeile("# calculation of the mean free energy F using WHAM");
	histo_PMF_Offset.setzeZeile("# histogram(i)  p(i) Offset_F_i=-ln(p)");
	
	for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
	{
		histo_PMF_Offset.setzeZeile(nrHisto+" "+Math.exp(-OffSetF[nrHisto])+" "+OffSetF[nrHisto]);
	}
	histo_PMF_Offset.DateiSchliessen();
	
	
	
	BFMFileSaver histo_Rg2Para= new BFMFileSaver();
	histo_Rg2Para.DateiAnlegen(dirDst+"/"+FileName+"Histo_Rg2_XY_WHAM.dat", false);
	//histo_RPMF.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
	//histo_Rg2Para.setzeZeile("# normalized to unity: int_0 to inf p(r) dz = 1");
	//histo_Rg2Para.setzeZeile("# calculation of the mean free energy F using WHAM");
	histo_Rg2Para.setzeZeile("# intervall from ["+HG_ZCOM_Rg2Para_StatistikTotal.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM_Rg2Para_StatistikTotal.GetRangeInBinLowerLimit(HG_ZCOM_Rg2Para_StatistikTotal.GetNrBins())+"]");
	histo_Rg2Para.setzeZeile("# intervall thickness: dI="+HG_ZCOM_Rg2Para_StatistikTotal.GetIntervallThickness());
	histo_Rg2Para.setzeZeile("# zeta  Rg2XY");
	
	for(int i = 0; i < HG_ZCOM_Rg2Para_StatistikTotal.GetNrBins(); i++)
	{
		if(HG_ZCOM_Rg2Para_StatistikTotal.GetAverageInBin(i) != 0)
			histo_Rg2Para.setzeZeile(HG_ZCOM_Rg2Para_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_Rg2Para_StatistikTotal.GetAverageInBin(i));
	}
	
	histo_Rg2Para.DateiSchliessen();
	
	BFMFileSaver histo_Rg2Perp= new BFMFileSaver();
	histo_Rg2Perp.DateiAnlegen(dirDst+"/"+FileName+"Histo_Rg2_Z_WHAM.dat", false);
	//histo_RPMF.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
	//histo_Rg2Perp.setzeZeile("# normalized to unity: int_0 to inf p(r) dz = 1");
	//histo_Rg2Perp.setzeZeile("# calculation of the mean free energy F using WHAM");
	histo_Rg2Perp.setzeZeile("# intervall from ["+HG_ZCOM_Rg2Perp_StatistikTotal.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM_Rg2Perp_StatistikTotal.GetRangeInBinLowerLimit(HG_ZCOM_Rg2Perp_StatistikTotal.GetNrBins())+"]");
	histo_Rg2Perp.setzeZeile("# intervall thickness: dI="+HG_ZCOM_Rg2Perp_StatistikTotal.GetIntervallThickness());
	histo_Rg2Perp.setzeZeile("# zeta  Rg2Z");
	
	for(int i = 0; i < HG_ZCOM_Rg2Perp_StatistikTotal.GetNrBins(); i++)
	{
		if(HG_ZCOM_Rg2Perp_StatistikTotal.GetAverageInBin(i) != 0)
			histo_Rg2Perp.setzeZeile(HG_ZCOM_Rg2Perp_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_Rg2Perp_StatistikTotal.GetAverageInBin(i));
	}
	
	histo_Rg2Perp.DateiSchliessen();
	
	/*	
	
	//find the maximum potential difference in the histograms
	MaximumPotentialBias = 0.0;
	
	for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
	{
		
		for(int i = 0; i < HG_ZCOM_Plain[nrHisto].GetNrBins(); i++)
		{
			double max = 0.0;
			
			
			if(HG_ZCOM_Plain[nrHisto].GetNrInBin(i) != 0)
				max = ((0.5*springconstant*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])) );
				//max = Math.exp((0.5*springconstant*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])) );
			
			
			if(max > MaximumPotentialBias)
				{
					MaximumPotentialBias = max;
					System.out.println("MaximumPotentialBias = " + MaximumPotentialBias);
				}
			
		}
	}
	
	System.out.println("Used MaximumPotentialBias = " + MaximumPotentialBias);
	
	
	for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
	{
		
		for(int i = 0; i < HG_ZCOM_Plain[nrHisto].GetNrBins(); i++)
		{
			double max = HG_ZCOM_Plain[nrHisto].GetNrOfCounts()*Math.exp(OffSetF[nrHisto] - ((0.5*springconstant*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])) ) );
			
			HG_ZCOM_Rg2ParaCorrectedWHAMGeneralNorm_StatistikTotal.AddValue(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i), max);
			
		}
	}
	
	//now we load everything again to obtain the correct value for an observable
	for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
	{
		//LoadFileCorrected(fname+"L"+(int)(EquLength[nrHisto])+".bfm",1000,nrHisto);
		LoadFile(fname+"L"+(int)(EquLength[nrHisto])+"_reduced.bfm",100,nrHisto);
		//LoadFile(fname+"R_"+EquLength[nrHisto]+"_C_0.2_HardColloids.bfm",1,nrHisto);
	}
	
	
	BFMFileSaver histo_Rg2Para_corr= new BFMFileSaver();
	histo_Rg2Para_corr.DateiAnlegen(dirDst+"/"+FileName+"Histo_Rg2_Para_corr_WHAM.dat", false);
	//histo_RPMF.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
	//histo_Rg2Para.setzeZeile("# normalized to unity: int_0 to inf p(r) dz = 1");
	//histo_Rg2Para.setzeZeile("# calculation of the mean free energy F using WHAM");
	histo_Rg2Para_corr.setzeZeile("# intervall from ["+HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetRangeInBinLowerLimit(HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetNrBins())+"]");
	histo_Rg2Para_corr.setzeZeile("# intervall thickness: dI="+HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetIntervallThickness());
	histo_Rg2Para_corr.setzeZeile("# used MaximumPotentialBias = " + MaximumPotentialBias);
	histo_Rg2Para_corr.setzeZeile("# zeta  Rg2Para(corr)");
	
	
	
	for(int i = 0; i < HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetNrBins(); i++)
	{
		if(HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetAverageInBin(i) != 0)
			histo_Rg2Para_corr.setzeZeile(HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetAverageInBin(i)+" "+(HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetAverageInBin(i)/HG_ZCOM_Rg2ParaCorrectedWHAMNormalization_StatistikTotal.GetAverageInBin(i)));
	
		System.out.println(HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetAverageInBin(i)+" "+(HG_ZCOM_Rg2ParaCorrectedWHAM_StatistikTotal.GetAverageInBin(i)/HG_ZCOM_Rg2ParaCorrectedWHAMNormalization_StatistikTotal.GetAverageInBin(i)));
	}
	
	histo_Rg2Para_corr.DateiSchliessen();
		
	
	BFMFileSaver histo_Rg2Perp_corr= new BFMFileSaver();
	histo_Rg2Perp_corr.DateiAnlegen(dirDst+"/"+FileName+"Histo_Rg2_Perp_corr_WHAM.dat", false);
	//histo_RPMF.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
	//histo_Rg2Para.setzeZeile("# normalized to unity: int_0 to inf p(r) dz = 1");
	//histo_Rg2Para.setzeZeile("# calculation of the mean free energy F using WHAM");
	histo_Rg2Perp_corr.setzeZeile("# intervall from ["+HG_ZCOM_Rg2PerpCorrectedWHAM_StatistikTotal.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM_Rg2PerpCorrectedWHAM_StatistikTotal.GetRangeInBinLowerLimit(HG_ZCOM_Rg2PerpCorrectedWHAM_StatistikTotal.GetNrBins())+"]");
	histo_Rg2Perp_corr.setzeZeile("# intervall thickness: dI="+HG_ZCOM_Rg2PerpCorrectedWHAM_StatistikTotal.GetIntervallThickness());
	histo_Rg2Perp_corr.setzeZeile("# used MaximumPotentialBias = " + MaximumPotentialBias);
	histo_Rg2Perp_corr.setzeZeile("# zeta  Rg2Para(corr)");
	
	for(int i = 0; i < HG_ZCOM_Rg2PerpCorrectedWHAM_StatistikTotal.GetNrBins(); i++)
	{
		if(HG_ZCOM_Rg2PerpCorrectedWHAM_StatistikTotal.GetAverageInBin(i) != 0)
			histo_Rg2Perp_corr.setzeZeile(HG_ZCOM_Rg2PerpCorrectedWHAM_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_Rg2PerpCorrectedWHAM_StatistikTotal.GetAverageInBin(i)+" "+(HG_ZCOM_Rg2PerpCorrectedWHAM_StatistikTotal.GetAverageInBin(i)/HG_ZCOM_Rg2PerpCorrectedWHAMNormalization_StatistikTotal.GetAverageInBin(i)));
	}
	
	histo_Rg2Perp_corr.DateiSchliessen();
		
	
	BFMFileSaver histo_Rg2Para_corr_Prob= new BFMFileSaver();
	histo_Rg2Para_corr_Prob.DateiAnlegen(dirDst+"/"+FileName+"Histo_Rg2_Para_corr_Prob_WHAM.dat", false);
	//histo_RPMF.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
	//histo_Rg2Para.setzeZeile("# normalized to unity: int_0 to inf p(r) dz = 1");
	//histo_Rg2Para.setzeZeile("# calculation of the mean free energy F using WHAM");
	histo_Rg2Para_corr_Prob.setzeZeile("# intervall from ["+HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetRangeInBinLowerLimit(HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetNrBins())+"]");
	histo_Rg2Para_corr_Prob.setzeZeile("# intervall thickness: dI="+HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetIntervallThickness());
	histo_Rg2Para_corr_Prob.setzeZeile("# used MaximumPotentialBias = " + MaximumPotentialBias);
	histo_Rg2Para_corr_Prob.setzeZeile("# zeta  Rg2Para(corrProb)");
	
	for(int i = 0; i < HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetNrBins(); i++)
	{
		//if(HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetAverageInBin(i) != 0)
		//if(HG_ZCOM_Rg2Para_StatistikTotal.GetAverageInBin(i) != 0)
		{
			double norm = 0.0;
			double average = 0.0;
			//normalizing the average per bin for probability of all histograms
			for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
			{
				average += HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_Statistik[nrHisto].GetAverageInBin(i);
				
				norm += HG_ZCOM_Statistik[nrHisto].GetAverageInBin(i);
			}
			
			//histo_Rg2Para_corr_Prob.setzeZeile(HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetAverageInBin(i)+" "+(HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetAverageInBin(i)/HG_ZCOM_Statistik_NormAll.GetAverageInBin(i))+" "+HG_ZCOM_Statistik_NormAll.GetAverageInBin(i) );
			//histo_Rg2Para_corr_Prob.setzeZeile(HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetAverageInBin(i)+" "+(HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_StatistikTotal.GetAverageInBin(i)/norm)+" "+norm );
			//histo_Rg2Para_corr_Prob.setzeZeile(HG_ZCOM_Rg2Para_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_Rg2Para_StatistikTotal.GetAverageInBin(i)+" "+(HG_ZCOM_Rg2Para_StatistikTotal.GetAverageInBin(i)/norm)+" "+norm );
			histo_Rg2Para_corr_Prob.setzeZeile(HG_ZCOM_Rg2Para_StatistikTotal.GetRangeInBin(i)+" "+average+" "+HG_ZCOM_Rg2Para_StatistikTotal.GetAverageInBin(i)+" "+average/norm+" "+(HG_ZCOM_Rg2Para_StatistikTotal.GetAverageInBin(i)/norm)+" "+norm );
			
			
		}
	}
	
	histo_Rg2Para_corr_Prob.DateiSchliessen();
	
	
	BFMFileSaver histo_Rg2Perp_corr_Prob= new BFMFileSaver();
	histo_Rg2Perp_corr_Prob.DateiAnlegen(dirDst+"/"+FileName+"Histo_Rg2_Perp_corr_Prob_WHAM.dat", false);
	//histo_RPMF.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
	//histo_Rg2Para.setzeZeile("# normalized to unity: int_0 to inf p(r) dz = 1");
	//histo_Rg2Para.setzeZeile("# calculation of the mean free energy F using WHAM");
	histo_Rg2Perp_corr_Prob.setzeZeile("# intervall from ["+HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetRangeInBinLowerLimit(HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetNrBins())+"]");
	histo_Rg2Perp_corr_Prob.setzeZeile("# intervall thickness: dI="+HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetIntervallThickness());
	histo_Rg2Perp_corr_Prob.setzeZeile("# used MaximumPotentialBias = " + MaximumPotentialBias);
	histo_Rg2Perp_corr_Prob.setzeZeile("# zeta  Rg2Para(corrProb)");
	
	for(int i = 0; i < HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetNrBins(); i++)
	{
		//if(HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetAverageInBin(i) != 0)
		if(HG_ZCOM_Rg2Perp_StatistikTotal.GetAverageInBin(i) != 0)
		{
			double norm = 0.0;
			double average = 0.0;
			//normalizing the average per bin for probability of all histograms
			for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
			{
				average += HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_Statistik[nrHisto].GetAverageInBin(i);
				norm += HG_ZCOM_Statistik[nrHisto].GetAverageInBin(i);
			}
			
			//histo_Rg2Perp_corr_Prob.setzeZeile(HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetAverageInBin(i)+" "+(HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetAverageInBin(i)/HG_ZCOM_Statistik_NormAll.GetAverageInBin(i))+" "+HG_ZCOM_Statistik_NormAll.GetAverageInBin(i) );
			//histo_Rg2Perp_corr_Prob.setzeZeile(HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetAverageInBin(i)+" "+(HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_StatistikTotal.GetAverageInBin(i)/norm)+" "+norm );
			//histo_Rg2Perp_corr_Prob.setzeZeile(HG_ZCOM_Rg2Perp_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_Rg2Perp_StatistikTotal.GetAverageInBin(i)+" "+(HG_ZCOM_Rg2Perp_StatistikTotal.GetAverageInBin(i)/norm)+" "+norm );
			histo_Rg2Perp_corr_Prob.setzeZeile(HG_ZCOM_Rg2Perp_StatistikTotal.GetRangeInBin(i)+" "+average+" "+HG_ZCOM_Rg2Perp_StatistikTotal.GetAverageInBin(i)+" "+average/norm+" "+(HG_ZCOM_Rg2Perp_StatistikTotal.GetAverageInBin(i)/norm)+" "+norm );
			
		}
	}
	
	histo_Rg2Perp_corr_Prob.DateiSchliessen();
	
	*/
		//createXmGraceFile(equilibriumlength);
	}
	
	protected void createXmGraceFile(double equilibriumlength)
	{
		/*BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(dstDir+"/"+FileName+"_Histo_Rc2c_Probability.batch", false);
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    xmgrace.setzeZeile("# AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("# AUTOSCALE ONREAD YAxes");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_Histo_ZCOM.dat\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_Histo_RSpringPotProb.dat\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_Histo_Fraction_P_PRSpringPotProb.dat\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:3\"");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_Histo_MeanDepletion.dat\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:2\"");
	   
	    //xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_PEG_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    //xmgrace.setzeZeile("BLOCK xy \"1:3\"");
	    //xmgrace.setzeZeile("BLOCK xy \"1:5\"");
	    //xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    //xmgrace.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace.setzeZeile(" world xmin 0");
	    xmgrace.setzeZeile(" world xmax 20.0");
	    xmgrace.setzeZeile(" world ymin -2");
	    xmgrace.setzeZeile(" world ymax 2.0");
	    xmgrace.setzeZeile(" xaxis label \"r [a]\"");
	    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace.setzeZeile(" xaxis tick major 5");
	    xmgrace.setzeZeile(" xaxis tick minor 1");
	    xmgrace.setzeZeile(" yaxis label \"-ln(p\\sSim\\N/p\\sSpring\\N)\"");
	    xmgrace.setzeZeile(" yaxis tick major 1");
	    xmgrace.setzeZeile(" yaxis tick minor 0.25");

	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 symbol 1");
	    xmgrace.setzeZeile(" s0 symbol Skip 100");
	    xmgrace.setzeZeile(" s0 errorbar off");
	    xmgrace.setzeZeile(" s0 legend \"p\\sSim\\N(r)\"");
	    xmgrace.setzeZeile(" AUTOSCALE S0");
	    
	    xmgrace.setzeZeile(" s1 line color 2");
	    xmgrace.setzeZeile(" s1 line linestyle 1");
	    xmgrace.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace.setzeZeile(" s1 symbol 2");
	    xmgrace.setzeZeile(" s1 symbol Skip 100");
	    xmgrace.setzeZeile(" s1 errorbar off");
	    xmgrace.setzeZeile(" s1 legend \"p\\sSpring\\N(r)\"");
	    
	    xmgrace.setzeZeile(" s2 hidden true");
	    xmgrace.setzeZeile(" s2 line color 3");
	    xmgrace.setzeZeile(" s2 line linestyle 1");
	    xmgrace.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace.setzeZeile(" s2 symbol 3");
	    xmgrace.setzeZeile(" s2 symbol Skip 100");
	    xmgrace.setzeZeile(" s2 errorbar off");
	    xmgrace.setzeZeile(" s2 legend \"p\\sSim\\N/p\\sSpring\\N\"");

	    xmgrace.setzeZeile(" s3 hidden true");
	    xmgrace.setzeZeile(" s3 line color 4");
	    xmgrace.setzeZeile(" s3 line linestyle 1");
	    xmgrace.setzeZeile(" s3 line linewidth 1.5");
	    xmgrace.setzeZeile(" s3 symbol 4");
	    xmgrace.setzeZeile(" s3 symbol Skip 100");
	    xmgrace.setzeZeile(" s3 errorbar off");
	    xmgrace.setzeZeile(" s3 legend \"-ln(p\\sSim\\N/p\\sSpring\\N)\"");

	    xmgrace.setzeZeile(" s4 hidden true");
	    xmgrace.setzeZeile(" s4 line color 10");
	    xmgrace.setzeZeile(" s4 line linestyle 1");
	    xmgrace.setzeZeile(" s4 line linewidth 1.5");
	    xmgrace.setzeZeile(" s4 symbol 1");
	    xmgrace.setzeZeile(" s4 symbol color 10");
	    xmgrace.setzeZeile(" s4 symbol Skip 5");
	    xmgrace.setzeZeile(" s4 errorbar off");
	    xmgrace.setzeZeile(" s4 legend \"-ln(p\\sSim\\N/p\\s0,Sim\\N)-U\\sSpring\\N\"");

	    xmgrace.setzeZeile(" with line");
	    xmgrace.setzeZeile(" line on");
	    xmgrace.setzeZeile(" line loctype world");
	    xmgrace.setzeZeile(" line g0");
	    xmgrace.setzeZeile(" line "+ZCOM_stat.ReturnM1()+", -100, "+ZCOM_stat.ReturnM1()+", 100");
	    xmgrace.setzeZeile(" line linewidth 1.5");
	    xmgrace.setzeZeile(" line linestyle 1");
	    xmgrace.setzeZeile(" line color 1");
	    xmgrace.setzeZeile(" line def");
	    
	    xmgrace.setzeZeile(" with line");
	    xmgrace.setzeZeile(" line on");
	    xmgrace.setzeZeile(" line loctype world");
	    xmgrace.setzeZeile(" line g0");
	    xmgrace.setzeZeile(" line "+equilibriumlength+", -100, "+equilibriumlength+", 100");
	    xmgrace.setzeZeile(" line linewidth 1.5");
	    xmgrace.setzeZeile(" line linestyle 2");
	    xmgrace.setzeZeile(" line color 2");
	    xmgrace.setzeZeile(" line def");
	    
	    xmgrace.setzeZeile(" LEGEND 0.15, 0.85");
	   // xmgrace.setzeZeile(" subtitle \"Melt; N="+NrOfMonoPerChain+"; n\\sChain\\N="+NrOfChains+"; c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z))+";\"");

	    xmgrace.setzeZeile(" SAVEALL \""+dstDir+"/"+FileName+"_Histo_Rc2c_Probability.agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+dstDir+"/"+FileName+"_Histo_Rc2c_Probability.ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	   
	    BFMFileSaver xmgrace2 = new BFMFileSaver();
		xmgrace2.DateiAnlegen(dstDir+"/"+FileName+"_Histo_MeanPotential.batch", false);
		xmgrace2.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace2.setzeZeile("FOCUS G0");
	    xmgrace2.setzeZeile("# AUTOSCALE ONREAD None");
	    xmgrace2.setzeZeile("# AUTOSCALE ONREAD YAxes");
	    xmgrace2.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_Histo_ZCOM.dat\"");
	    xmgrace2.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace2.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_Histo_RSpringPotProb.dat\"");
	    xmgrace2.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace2.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_Histo_Fraction_P_PRSpringPotProb.dat\"");
	    xmgrace2.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace2.setzeZeile("BLOCK xy \"1:3\"");
	    xmgrace2.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_Histo_MeanDepletion.dat\"");
	    xmgrace2.setzeZeile("BLOCK xy \"1:2\"");
	   
	    xmgrace2.setzeZeile(" world xmin 0");
	    xmgrace2.setzeZeile(" world xmax 20.0");
	    xmgrace2.setzeZeile(" world ymin -10");
	    xmgrace2.setzeZeile(" world ymax 10.0");
	    xmgrace2.setzeZeile(" xaxis label \"r [a]\"");
	    xmgrace2.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace2.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace2.setzeZeile(" xaxis tick major 5");
	    xmgrace2.setzeZeile(" xaxis tick minor 1");
	    xmgrace2.setzeZeile(" yaxis label \"-ln(p\\sSim\\N/p\\sSpring\\N)\"");
	    xmgrace2.setzeZeile(" yaxis tick major 1");
	    xmgrace2.setzeZeile(" yaxis tick minor 0.5");

	    xmgrace2.setzeZeile(" s0 hidden true");
	    xmgrace2.setzeZeile(" s0 line color 1");
	    xmgrace2.setzeZeile(" s0 line linestyle 1");
	    xmgrace2.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace2.setzeZeile(" s0 symbol 1");
	    xmgrace2.setzeZeile(" s0 symbol Skip 100");
	    xmgrace2.setzeZeile(" s0 errorbar off");
	    xmgrace2.setzeZeile(" s0 legend \"p\\sSim\\N(r)\"");
	    xmgrace2.setzeZeile("# AUTOSCALE S0");
	    
	    xmgrace2.setzeZeile(" s1 hidden true");
	    xmgrace2.setzeZeile(" s1 line color 2");
	    xmgrace2.setzeZeile(" s1 line linestyle 1");
	    xmgrace2.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace2.setzeZeile(" s1 symbol 2");
	    xmgrace2.setzeZeile(" s1 symbol Skip 100");
	    xmgrace2.setzeZeile(" s1 errorbar off");
	    xmgrace2.setzeZeile(" s1 legend \"p\\sSpring\\N(r)\"");
	    
	    xmgrace2.setzeZeile("# s2 hidden true");
	    xmgrace2.setzeZeile(" s2 line color 3");
	    xmgrace2.setzeZeile(" s2 line linestyle 1");
	    xmgrace2.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace2.setzeZeile(" s2 symbol 3");
	    xmgrace2.setzeZeile(" s2 symbol Skip 100");
	    xmgrace2.setzeZeile(" s2 errorbar off");
	    xmgrace2.setzeZeile(" s2 legend \"p\\sSim\\N/p\\sSpring\\N\"");

	    xmgrace2.setzeZeile("# s3 hidden true");
	    xmgrace2.setzeZeile(" s3 line color 4");
	    xmgrace2.setzeZeile(" s3 line linestyle 1");
	    xmgrace2.setzeZeile(" s3 line linewidth 1.5");
	    xmgrace2.setzeZeile(" s3 symbol 4");
	    xmgrace2.setzeZeile(" s3 symbol Skip 100");
	    xmgrace2.setzeZeile(" s3 errorbar off");
	    xmgrace2.setzeZeile(" s3 legend \"-ln(p\\sSim\\N/p\\sSpring\\N)\"");

	    xmgrace2.setzeZeile("# s4 hidden true");
	    xmgrace2.setzeZeile(" s4 line color 10");
	    xmgrace2.setzeZeile(" s4 line linestyle 1");
	    xmgrace2.setzeZeile(" s4 line linewidth 1.5");
	    xmgrace2.setzeZeile(" s4 symbol 1");
	    xmgrace2.setzeZeile(" s4 symbol color 10");
	    xmgrace2.setzeZeile(" s4 symbol Skip 5");
	    xmgrace2.setzeZeile(" s4 errorbar off");
	    xmgrace2.setzeZeile(" s4 legend \"-ln(p\\sSim\\N/p\\s0,Sim\\N)-U\\sSpring\\N\"");

	    xmgrace2.setzeZeile(" with line");
	    xmgrace2.setzeZeile(" line on");
	    xmgrace2.setzeZeile(" line loctype world");
	    xmgrace2.setzeZeile(" line g0");
	    xmgrace2.setzeZeile(" line "+ZCOM_stat.ReturnM1()+", -100, "+ZCOM_stat.ReturnM1()+", 100");
	    xmgrace2.setzeZeile(" line linewidth 1.5");
	    xmgrace2.setzeZeile(" line linestyle 1");
	    xmgrace2.setzeZeile(" line color 1");
	    xmgrace2.setzeZeile(" line def");
	    
	    xmgrace2.setzeZeile(" with line");
	    xmgrace2.setzeZeile(" line on");
	    xmgrace2.setzeZeile(" line loctype world");
	    xmgrace2.setzeZeile(" line g0");
	    xmgrace2.setzeZeile(" line "+equilibriumlength+", -100, "+equilibriumlength+", 100");
	    xmgrace2.setzeZeile(" line linewidth 1.5");
	    xmgrace2.setzeZeile(" line linestyle 2");
	    xmgrace2.setzeZeile(" line color 2");
	    xmgrace2.setzeZeile(" line def");
	    
	    xmgrace2.setzeZeile(" LEGEND 0.15, 0.85");
	   // xmgrace.setzeZeile(" subtitle \"Melt; N="+NrOfMonoPerChain+"; n\\sChain\\N="+NrOfChains+"; c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z))+";\"");

	    xmgrace2.setzeZeile(" SAVEALL \""+dstDir+"/"+FileName+"_Histo_MeanPotential.agr\"");

	    xmgrace2.setzeZeile(" PRINT TO \""+dstDir+"/"+FileName+"_Histo_MeanPotential.ps\"");
	    xmgrace2.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace2.setzeZeile("PRINT");
		
	    xmgrace2.DateiSchliessen();
	   	    
	    
	    try {
	    	
	    	  System.out.println("xmgrace -batch "+dstDir+"/"+FileName+"_Histo_Rc2c_Probability.batch -nosafe -hardcopy");
		      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/"+FileName+"_Histo_Rc2c_Probability.batch -nosafe -hardcopy");
		      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/"+FileName+"_Histo_MeanPotential.batch -nosafe -hardcopy");
			    
	    } catch (Exception e) {
		      System.err.println(e.toString());
		    }
	    */
	}
	
	protected void LoadFile(String file, int startframe, int histonr)
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

		    		playSimulation(z,histonr);
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
		if(args.length != 3)
		{
			System.out.println("Berechnung Rc2c Cubes");
			System.out.println("USAGE: dirSrc/ FileLinearChains[.xo]  dirDst/ ");
		}
		//else new Auswertung_Cubes_ZCOM_WHAM(args[0], args[1], args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]));//,args[1],args[2]);
		else new Auswertung_Dendrimer_ZCOM_Rg2_Slit(args[0], args[1], args[2]);//,args[1],args[2]);
		
		
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
		
		
		
		
		NrofFrames = importData.GetNrOfFrames();
	
		String zutrt = 	FileName.substring(0, FileName.length()-4);

		System.out.println("String : " + zutrt);
		
		
		Bindungsnetzwerk = null;
		Bindungsnetzwerk = new Int_IntArrayList_Table(MONOMERZAHL);
		
		
		for(int it = 0; it < importData.bonds.size(); it++)
		{
			long bondobj = importData.bonds.get(it);
			//System.out.println(it + " bond " + bondobj);
			int mono1 = getMono1Nr(bondobj);
			int mono2 = getMono2Nr(bondobj);
			
			Bindungsnetzwerk.put(mono1, mono2);
			Bindungsnetzwerk.put(mono2, mono1);
			System.out.println("bonds_sim: "+it+"   a:" + getMono1Nr(bondobj) + " b:" + getMono2Nr(bondobj));
		}
		
		
	}
	
	public void playSimulation(int frame, int histonr)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				importData.GetFrameOfSimulation( frame);;
				
				
				
				double ZCom1_x = 0.0;
				double ZCom1_y = 0.0;
				double ZCom1_z = 0.0;
					
				 for (int i= 1; i <= importData.NrOfMonomers; i++)
				  {
					 ZCom1_x += 1.0*(importData.PolymerKoordinaten[i][0]);
					 ZCom1_y += 1.0*(importData.PolymerKoordinaten[i][1]);
					 ZCom1_z += 1.0*(importData.PolymerKoordinaten[i][2]);
				  }
				 
				 ZCom1_x /= importData.NrOfMonomers;
				 ZCom1_y /= importData.NrOfMonomers;
				 ZCom1_z /= importData.NrOfMonomers;
				
				 
				 double Rg2_para = 0.0;
				 double Rg2_perp = 0.0;
				 
				 double Rg2_x = 0.0;
				 double Rg2_y = 0.0;
				 double Rg2_z = 0.0;
				 
				 for (int i= 1; i <= importData.NrOfMonomers; i++)
					 for (int j= i; j <= importData.NrOfMonomers; j++)
				  {
						 Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0]-importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0]-importData.PolymerKoordinaten[j][0]);
						 Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1]-importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1]-importData.PolymerKoordinaten[j][1]);
						 Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2]-importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2]-importData.PolymerKoordinaten[j][2]);
						  
				  }
				 
				 Rg2_x /= 1.0*(importData.NrOfMonomers*importData.NrOfMonomers);
				 Rg2_y /= 1.0*(importData.NrOfMonomers*importData.NrOfMonomers);
				 Rg2_z /= 1.0*(importData.NrOfMonomers*importData.NrOfMonomers);
				 
				 Rg2_para = Rg2_x+Rg2_y;
				 Rg2_perp = Rg2_z;
				
				ZCOM_stat[histonr].AddValue(ZCom1_z);
				 
				HG_ZCOM_Plain[histonr].AddValue(ZCom1_z);
			
				HG_ZCOM_Rg2Para_StatistikTotal.AddValue(ZCom1_z, Rg2_para);
				
				HG_ZCOM_Rg2Perp_StatistikTotal.AddValue(ZCom1_z, Rg2_perp);
				
				HG_ZCOM_Rg2ParaCorrectedProbabilityWHAM_Statistik[histonr].AddValue(ZCom1_z, Rg2_para);
				HG_ZCOM_Rg2PerpCorrectedProbabilityWHAM_Statistik[histonr].AddValue(ZCom1_z, Rg2_perp);
	
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
