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

public class Auswertung_Dendrimer_ZCOM_WHAM {


	
	
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
	
	HistogrammStatistik HG_ZCOM_StatistikTotal;
	
	BFMFileSaver histo_ZCOM;
	
	Int_IntArrayList_Table Bindungsnetzwerk; 
	
	long deltaT;
	
	double[] EquLength;
	double[] OffSetF;
	
	
	String dstDir;
	
	
	public Auswertung_Dendrimer_ZCOM_WHAM(String fdir, String fname, String dirDst, double springconstant)//, String skip, String current)
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
		
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		int NrOfAllHistogramms = 32;
		
		double lowerBoundary = 0.0-0.5;
		double higherBoundary = 128-0.5;
		int NrBins = 256;
		
		ZCOM_stat= new Statistik[NrOfAllHistogramms];
		HG_ZCOM_Plain = new Histogramm[NrOfAllHistogramms];
		HG_ZCOM_Statistik = new HistogrammStatistik[NrOfAllHistogramms];
		
		EquLength= new double[NrOfAllHistogramms];
		OffSetF= new double[NrOfAllHistogramms];
		
		for(int i = 0; i < NrOfAllHistogramms; i++)
		{
			ZCOM_stat[i] =new Statistik();
			HG_ZCOM_Plain[i] = new Histogramm(lowerBoundary,higherBoundary,NrBins);
			HG_ZCOM_Statistik[i] = new HistogrammStatistik(lowerBoundary,higherBoundary,NrBins);
			OffSetF[i]=0.0;
			//EquLength[i]=1.0+2.0*i;
			EquLength[i]=2.0+2.0*i;
			System.out.println("EquLength["+i+"]: " + EquLength[i]);
		}
		
		HG_ZCOM_StatistikTotal = new HistogrammStatistik(lowerBoundary,higherBoundary,NrBins);
		
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
			//LoadFile(fname+"L"+(int)(EquLength[nrHisto])+".bfm",1,nrHisto);
			//LoadFile(fname+"L"+(int)(EquLength[nrHisto])+"_reduced.bfm",2000,nrHisto);
			//LoadFile(fname+"L"+(int)(EquLength[nrHisto])+"_reduced.bfm",20,nrHisto);
			LoadFile(fname+"_r"+(int)(EquLength[nrHisto])+"_d0.5_reduced.bfm",200,nrHisto);
			
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
		System.out.println(dirDst+"/"+FileName+"Histo_"+EquLength[nrHisto]+".dat");
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
		
		//perform all calculation to achieve selfconsitency
				double sumSquare = 0.0;
				double errorSumSquareFreeEnergy=0.000001;
				int selfconsitency = 0;
				
			//for(int selfconsitency = 0; selfconsitency < 50000; selfconsitency++)
			
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
		/*System.out.println("PMF-prob");
		for(int i = 0; i < HG_ZCOM_StatistikTotal.GetNrBins(); i++)
		{
			System.out.println(HG_ZCOM_StatistikTotal.GetRangeInBin(i)+" "+HG_ZCOM_StatistikTotal.GetAverageInBin(i));
		}
		*/
		
		
		System.out.println("Iteration: " + selfconsitency);
		//perform the integration to get the OffsetF
		sumSquare = 0.0;
		
		//perform the integration to get the OffsetF
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
			
		}
		
		// reduce all OffSet to zero by forcing last window to be OffSetF[NrOfAllHistogramms]=0.0
		
		for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
		{
			OffSetF[nrHisto] -= OffSetF[NrOfAllHistogramms-1];
			//System.out.println("OffSetF["+nrHisto+"]: " + OffSetF[nrHisto]);
			
			if(selfconsitency%20 ==0)
			System.out.println("OffSetF["+nrHisto+"]: " + OffSetF[nrHisto]);
		}
		
		
		System.out.println("SumOffSetF^2: " + sumSquare);
		selfconsitency++;

	}while(sumSquare > errorSumSquareFreeEnergy);
	
	/*
	// constraint of sum of all weights should be unity
	double weightSum =0.0;
	
	for(int nrHistoAll = 0; nrHistoAll < NrOfAllHistogramms; nrHistoAll++)
	{
		double weightSumAtHisto =0.0;
		
		for(int i = 0; i < HG_ZCOM_Plain[nrHistoAll].GetNrBins(); i++)
	{
		double normTotal = 0.0;
		double weightTotal = 0.0;
		
		for(int j = 0; j < HG_ZCOM_Plain[nrHistoAll].GetNrBins(); j++)
		for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
		{
			//double normalisation=4*Math.PI*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)*HG_ZCOM_Plain[nrHisto].GetRangeInBin(i));
		
			//normTotal += HG_ZCOM_Plain[nrHisto].GetNrOfCounts()*Math.exp(- (0.5*springconstant*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto]) - OffSetF[nrHisto]) );
			normTotal += HG_ZCOM_Plain[nrHisto].GetNrOfCounts()*Math.exp(- (0.5*springconstant*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(j)-EquLength[nrHisto])*(HG_ZCOM_Plain[nrHisto].GetRangeInBin(j)-EquLength[nrHisto]) - OffSetF[nrHisto]) );
			
			
		}
		//weightTotal = HG_ZCOM_Plain[nrHistoAll].GetNrOfCounts()*Math.exp(- (0.5*springconstant*(HG_ZCOM_Plain[nrHistoAll].GetRangeInBin(i)-EquLength[nrHistoAll])*(HG_ZCOM_Plain[nrHistoAll].GetRangeInBin(i)-EquLength[nrHistoAll]) - OffSetF[nrHistoAll]) );
		weightTotal = HG_ZCOM_Plain[nrHistoAll].GetNrOfCounts()*Math.exp(- (0.5*springconstant*(HG_ZCOM_Plain[nrHistoAll].GetRangeInBin(i)-EquLength[nrHistoAll])*(HG_ZCOM_Plain[nrHistoAll].GetRangeInBin(i)-EquLength[nrHistoAll]) - OffSetF[nrHistoAll]) );
		
		// there are entries for the PMF
		if(normTotal != 0.0)
			weightSumAtHisto +=weightTotal/normTotal;
		
		if(normTotal != 0.0)
		System.out.println("Weight at Histo "+ nrHistoAll + "  in bin " + HG_ZCOM_Plain[nrHistoAll].GetRangeInBin(i) + " == " + weightTotal/normTotal);
	}
		System.out.println("Weight at Histo "+ nrHistoAll + " == " + weightSumAtHisto);
		
		weightSum += weightSumAtHisto;
	}
	
	System.out.println("Weight Sum 1.0 == " + weightSum);
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
	
	BFMFileSaver histo_Offset= new BFMFileSaver();
	histo_Offset.DateiAnlegen(dirDst+"/"+FileName+"Histo_PMF_WHAM_Offset.dat", false);
	histo_Offset.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
	histo_Offset.setzeZeile("# calculation of the mean free energy F using WHAM");
	histo_Offset.setzeZeile("# intervall from ["+HG_ZCOM_StatistikTotal.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM_StatistikTotal.GetRangeInBinLowerLimit(HG_ZCOM_StatistikTotal.GetNrBins())+"]");
	histo_Offset.setzeZeile("# intervall thickness: dI="+HG_ZCOM_StatistikTotal.GetIntervallThickness());
	histo_Offset.setzeZeile("# window_i F_i k_i l_0,i");
	
	for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
	{
		histo_Offset.setzeZeile(nrHisto + " " + OffSetF[nrHisto]+ " " + EquLength[nrHisto] + " " + springconstant);
	}
	
	histo_Offset.DateiSchliessen();
		
		/*double testnorm =0.0;
		for(int i = 0; i < HG_ZCOM.GetNrBins(); i++)
		{
			testnorm += (HG_ZCOM.GetNrInBinNormiert(i)*(4*Math.PI*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetIntervallThickness()));
		}
		histo_ZCOM.setzeZeile("# normalization p0,Sim: "+(1.0/(testnorm)));
		histo_ZCOM.setzeZeile("# number of entries: "+ HG_ZCOM.GetNrOfCounts());
		
		double test = 0.0;
		
		for(int i = 0; i < HG_ZCOM.GetNrBins(); i++)
		{
			histo_ZCOM.setzeZeile(HG_ZCOM.GetRangeInBin(i)+" "+(HG_ZCOM.GetNrInBinNormiert(i)/(4*Math.PI*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetIntervallThickness()))+" "+(HG_ZCOM.GetNrInBin(i)));
			//testnorm += (HG_ZCOM.GetNrInBinNormiert(i)*(4*Math.PI*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetIntervallThickness()));
			test += HG_ZCOM.GetNrInBinNormiert(i);
		}
		
		
		histo_ZCOM.DateiSchliessen();
		
		System.out.println("test_ree: "+ test);	
		System.out.println("test_norm: "+ testnorm);	
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1() );
		
		BFMFileSaver rg = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.DateiAnlegen(dirDst+"/"+FileName+"_Rg2.dat", false);
		rg.setzeZeile("# RG2 of Cubes");
		rg.setzeZeile("# <(Rg2)>  <(Rg2)^2> d<Rg2> SampleSize");
		
		rg.setzeZeile(Rg2_Stat.ReturnM1()+" "+(Rg2_Stat.ReturnM2())+" "+( 2.0* Rg2_Stat.ReturnSigma()/Math.sqrt(1.0*Rg2_Stat.ReturnN())) + " " +Rg2_Stat.ReturnN());
		rg.setzeZeile("#");
		rg.setzeZeile("#");
		rg.setzeZeile("# <(Rg2_x)>  <(Rg2_x)^2> d<Rg2_x> SampleSize");
		rg.setzeZeile(Rg2_x_Stat.ReturnM1()+" "+(Rg2_x_Stat.ReturnM2())+" "+( 2.0* Rg2_x_Stat.ReturnSigma()/Math.sqrt(1.0*Rg2_x_Stat.ReturnN())) + " " +Rg2_x_Stat.ReturnN());
		rg.setzeZeile("#");
		rg.setzeZeile("#");
		rg.setzeZeile("# <(Rg2_y)>  <(Rg2_y)^2> d<Rg2_y> SampleSize");
		rg.setzeZeile(Rg2_y_Stat.ReturnM1()+" "+(Rg2_y_Stat.ReturnM2())+" "+( 2.0* Rg2_y_Stat.ReturnSigma()/Math.sqrt(1.0*Rg2_y_Stat.ReturnN())) + " " +Rg2_y_Stat.ReturnN());
		rg.setzeZeile("#");
		rg.setzeZeile("#");
		rg.setzeZeile("# <(Rg2_z)>  <(Rg2_z)^2> d<Rg2_z> SampleSize");
		rg.setzeZeile(Rg2_z_Stat.ReturnM1()+" "+(Rg2_z_Stat.ReturnM2())+" "+( 2.0* Rg2_z_Stat.ReturnSigma()/Math.sqrt(1.0*Rg2_z_Stat.ReturnN())) + " " +Rg2_z_Stat.ReturnN());
		rg.setzeZeile("#");
		rg.setzeZeile("#");
		rg.setzeZeile("# <R_C2C> <(R_C2C)^2> d<R_C2C> SampleSize");
		rg.setzeZeile(ZCOM_stat.ReturnM1()+" "+(ZCOM_stat.ReturnM2())+" "+( 2.0* ZCOM_stat.ReturnSigma()/Math.sqrt(1.0*ZCOM_stat.ReturnN())) + " " +ZCOM_stat.ReturnN());
		rg.setzeZeile("#");
		rg.setzeZeile("#");
		rg.setzeZeile("# <b^2)>  <(b^2)^2> d<b^2> SampleSize");
		rg.setzeZeile(Bondlength2_Stat.ReturnM1()+" "+(Bondlength2_Stat.ReturnM2())+" "+( 2.0* Bondlength2_Stat.ReturnSigma()/Math.sqrt(1.0*Bondlength2_Stat.ReturnN())) + " " +Bondlength2_Stat.ReturnN());
		rg.setzeZeile("#");
		rg.setzeZeile("#");
		rg.setzeZeile("# <Rg^2/b^2)> d<Rg^2/b^2>");
		rg.setzeZeile((Rg2_Stat.ReturnM1()/Bondlength2_Stat.ReturnM1())+" "+( (1.0/Bondlength2_Stat.ReturnM1())*(2.0* Rg2_Stat.ReturnSigma()/Math.sqrt(1.0*Rg2_Stat.ReturnN())) +  (Rg2_Stat.ReturnM1()/(Bondlength2_Stat.ReturnM1()*Bondlength2_Stat.ReturnM1()))*( 2.0* Bondlength2_Stat.ReturnSigma()/Math.sqrt(1.0*Bondlength2_Stat.ReturnN())) ) );
		
		rg.DateiSchliessen();
		
		System.out.println("<R_C2C>  : " + ZCOM_stat.ReturnM1());
		//calculates the normalized radial probability for the 3D isotropic HO
		
		double normalisation =0.0;
		double eql =equilibriumlength; //eqlength
		double spc =springconstant; //spring const - std: 0.6
		
		//double incomleteGamma(from x=6.0 to inf with shape)=GammaFunction.incompleteGammaQ(0.5, 6.0)*Gamma.gamma(0.5);		
		
		double gamma=GammaFunction.incompleteGammaQ(0.5, 0.5*spc*eql*eql)*Gamma.gamma(0.5);
		normalisation = -1.0*gamma*eql*eql/Math.sqrt(2*spc);
		
		normalisation += Math.sqrt(2.0*Math.PI)*eql*eql/Math.sqrt(spc);
		
		gamma=GammaFunction.incompleteGammaQ(1.0, 0.5*spc*eql*eql)*Gamma.gamma(1.0);
		normalisation += 2.0*gamma*eql/spc;
		
		gamma=GammaFunction.incompleteGammaQ(1.5, 0.5*spc*eql*eql)*Gamma.gamma(1.5);
		normalisation += Math.sqrt(2.0)*gamma/(spc*Math.sqrt(spc));
		
		normalisation += Math.sqrt(2.0*Math.PI)/(spc*Math.sqrt(spc));
		
		
		System.out.println("normalisation:    "+ normalisation);
		
		BFMFileSaver histo_RSpringPotProb= new BFMFileSaver();
		histo_RSpringPotProb.DateiAnlegen(dirDst+"/"+FileName+"_Histo_RSpringPotProb.dat", false);
		histo_RSpringPotProb.setzeZeile("# radial probability distributions p(r) of exp(-k/2*(r-l0)^2)");
		histo_RSpringPotProb.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
		histo_RSpringPotProb.setzeZeile("# intervall from ["+HG_ZCOM.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM.GetRangeInBinLowerLimit(HG_ZCOM.GetNrBins())+"]");
		histo_RSpringPotProb.setzeZeile("# intervall thickness: dI="+HG_ZCOM.GetIntervallThickness());
		histo_RSpringPotProb.setzeZeile("# normalization: "+(1.0/(4*Math.PI*normalisation)));
		histo_RSpringPotProb.setzeZeile("# spring constant k = " + spc);
		histo_RSpringPotProb.setzeZeile("# equilibrium length = " + eql);
		histo_RSpringPotProb.setzeZeile("# r  p(r) exp(-k/2*(r-l0)^2)");
		
		for(int i = 0; i < HG_ZCOM.GetNrBins(); i++)
		{
			double valueAtR = Math.exp(-0.5*spc*(HG_ZCOM.GetRangeInBin(i)-eql)*(HG_ZCOM.GetRangeInBin(i)-eql));
			histo_RSpringPotProb.setzeZeile(HG_ZCOM.GetRangeInBin(i)+" "+(valueAtR/((4*Math.PI*normalisation))) + " " + valueAtR);
			
		}
		
		histo_RSpringPotProb.DateiSchliessen();
		
		BFMFileSaver histo_P_over_PSpring= new BFMFileSaver();
		histo_P_over_PSpring.DateiAnlegen(dirDst+"/"+FileName+"_Histo_Fraction_P_PRSpringPotProb.dat", false);
		histo_P_over_PSpring.setzeZeile("# fraction of p(r) over pS(r)=pS0*exp(-k/2*(r-l0)^2)");
		histo_P_over_PSpring.setzeZeile("# intervall from ["+HG_ZCOM.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM.GetRangeInBinLowerLimit(HG_ZCOM.GetNrBins())+"]");
		histo_P_over_PSpring.setzeZeile("# intervall thickness: dI="+HG_ZCOM.GetIntervallThickness());
		histo_P_over_PSpring.setzeZeile("# normalization pS0: "+(1.0/(4*Math.PI*normalisation)));
		histo_P_over_PSpring.setzeZeile("# spring constant k = " + spc);
		histo_P_over_PSpring.setzeZeile("# equilibrium length = " + eql);
		histo_P_over_PSpring.setzeZeile("# r  p(r)/pS(r) -ln(p(r)/pS(r)))");
		histo_P_over_PSpring.setzeZeile("# set infinity to 1000.0 for visualization");
		
		for(int i = 0; i < HG_ZCOM.GetNrBins(); i++)
		{
			double valueAtR = Math.exp(-0.5*spc*(HG_ZCOM.GetRangeInBin(i)-eql)*(HG_ZCOM.GetRangeInBin(i)-eql));
			double value_P_over_PS = ( (HG_ZCOM.GetNrInBinNormiert(i)/(4*Math.PI*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetIntervallThickness()))/(valueAtR/((4*Math.PI*normalisation))) );
			
			double value_Ln_P_over_PS =0.0;
			
			if(value_P_over_PS != 0.0)
				value_Ln_P_over_PS = -Math.log(value_P_over_PS);
			else value_Ln_P_over_PS =1000.0;
			
			
			histo_P_over_PSpring.setzeZeile(HG_ZCOM.GetRangeInBin(i)+" "+ value_P_over_PS  + " " + value_Ln_P_over_PS);
			
		}
		
		histo_P_over_PSpring.DateiSchliessen();
		
		BFMFileSaver histo_P_over_PSpringCrop= new BFMFileSaver();
		histo_P_over_PSpringCrop.DateiAnlegen(dirDst+"/"+FileName+"_Histo_Fraction_P_PRSpringPotProbCrop.dat", false);
		histo_P_over_PSpringCrop.setzeZeile("# fraction of p(r) over pS(r)=pS0*exp(-k/2*(r-l0)^2)");
		histo_P_over_PSpringCrop.setzeZeile("# intervall from ["+HG_ZCOM.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM.GetRangeInBinLowerLimit(HG_ZCOM.GetNrBins())+"]");
		histo_P_over_PSpringCrop.setzeZeile("# intervall thickness: dI="+HG_ZCOM.GetIntervallThickness());
		histo_P_over_PSpringCrop.setzeZeile("# normalization pS0: "+(1.0/(4*Math.PI*normalisation)));
		histo_P_over_PSpringCrop.setzeZeile("# spring constant k = " + spc);
		histo_P_over_PSpringCrop.setzeZeile("# equilibrium length = " + eql);
		histo_P_over_PSpringCrop.setzeZeile("# r  p(r)/pS(r) -ln(p(r)/pS(r)))");
		histo_P_over_PSpringCrop.setzeZeile("# set infinity to 1000.0 for visualization");
		histo_P_over_PSpringCrop.setzeZeile("# number of entries: "+ HG_ZCOM.GetNrOfCounts());
		for(int i = 0; i < HG_ZCOM.GetNrBins(); i++)
			if(Math.abs(ZCOM_stat.ReturnM1()-HG_ZCOM.GetRangeInBin(i)) <= 1.55)
		{
			double valueAtR = Math.exp(-0.5*spc*(HG_ZCOM.GetRangeInBin(i)-eql)*(HG_ZCOM.GetRangeInBin(i)-eql));
			double value_P_over_PS = ( (HG_ZCOM.GetNrInBinNormiert(i)/(4*Math.PI*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetIntervallThickness()))/(valueAtR/((4*Math.PI*normalisation))) );
			double value_only_P = (HG_ZCOM.GetNrInBinNormiert(i)/(4*Math.PI*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetIntervallThickness()));
					
			double value_Ln_P_over_PS =0.0;
			HG_ZCOM.GetBinOfValue(6.0);
			if(value_P_over_PS != 0.0)
				value_Ln_P_over_PS = -Math.log(value_P_over_PS);
			else value_Ln_P_over_PS =1000.0;
			
			
			histo_P_over_PSpringCrop.setzeZeile(HG_ZCOM.GetRangeInBin(i)+" "+ value_P_over_PS  + " " + value_Ln_P_over_PS + " "+value_only_P+ " "+ (HG_ZCOM.GetNrInBin(i)) );
			
		}
		
		histo_P_over_PSpringCrop.DateiSchliessen();
		
		BFMFileSaver histo_MeanDep= new BFMFileSaver();
		histo_MeanDep.DateiAnlegen(dirDst+"/"+FileName+"_Histo_MeanDepletion.dat", false);
		histo_MeanDep.setzeZeile("# fraction of p(r) over pS(r)=pS0*exp(-k/2*(r-l0)^2)");
		histo_MeanDep.setzeZeile("# intervall from ["+HG_ZCOM.GetRangeInBinLowerLimit(0)+";"+HG_ZCOM.GetRangeInBinLowerLimit(HG_ZCOM.GetNrBins())+"]");
		histo_MeanDep.setzeZeile("# intervall thickness: dI="+HG_ZCOM.GetIntervallThickness());
		histo_MeanDep.setzeZeile("# normalization pS0: "+(1.0/(4*Math.PI*normalisation)));
		histo_MeanDep.setzeZeile("# spring constant k = " + spc);
		histo_MeanDep.setzeZeile("# equilibrium length = " + eql);
		histo_MeanDep.setzeZeile("# normalization p0,Sim: "+(1.0/(testnorm)));
		histo_MeanDep.setzeZeile("# set infinity to 1000.0 for visualization");
		histo_MeanDep.setzeZeile("# r  -ln(pSim(r)/p0) - exp(-k*(r-l0)^2/2)");
		
		
		for(int i = 0; i < HG_ZCOM.GetNrBins(); i++)
		{
			double valueAtR = (0.5*spc*(HG_ZCOM.GetRangeInBin(i)-eql)*(HG_ZCOM.GetRangeInBin(i)-eql));
			double value_PSim = (HG_ZCOM.GetNrInBinNormiert(i)/(4*Math.PI*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetRangeInBin(i)*HG_ZCOM.GetIntervallThickness()));
			
			double value_Ln_PSim =0.0;
			
			if(value_PSim != 0.0)
				value_Ln_PSim = -Math.log(value_PSim)+Math.log(1.0/testnorm);
			//value_Ln_PSim = -Math.log(value_PSim/testnorm);
			else value_Ln_PSim =1000.0;
			
			
			histo_MeanDep.setzeZeile(HG_ZCOM.GetRangeInBin(i)+" "+ (value_Ln_PSim-valueAtR)  );
			
		}
		
		histo_MeanDep.DateiSchliessen();
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
		if(args.length != 4)
		{
			System.out.println("Berechnung Rc2c Cubes");
			System.out.println("USAGE: dirSrc/ FileLinearChains[.xo]  dirDst/ springconstant");
		}
		//else new Auswertung_Cubes_ZCOM_WHAM(args[0], args[1], args[2], Double.parseDouble(args[3]), Double.parseDouble(args[4]));//,args[1],args[2]);
		else new Auswertung_Dendrimer_ZCOM_WHAM(args[0], args[1], args[2], Double.parseDouble(args[3]));//,args[1],args[2]);
		
		
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
				
				 
				
				ZCOM_stat[histonr].AddValue(ZCom1_z);
				 
				HG_ZCOM_Plain[histonr].AddValue(ZCom1_z);
			
				
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
