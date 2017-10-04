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

public class Auswertung_Cubes_RCom2Com_WHAM {


	
	
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
	
	Statistik[] RCom2Com_stat;
	
	Histogramm[] HG_RCom2Com_Plain;
	HistogrammStatistik[] HG_RCom2Com_Statistik;
	
	HistogrammStatistik HG_RCom2Com_StatistikTotal;
	
	BFMFileSaver histo_RCom2Com;
	
	Int_IntArrayList_Table Bindungsnetzwerk; 
	
	long deltaT;
	
	double[] EquLength;
	double[] OffSetF;
	
	
	String dstDir;
	
	
	public Auswertung_Cubes_RCom2Com_WHAM(String fdir, String fname, String dirDst, double springconstant)//, String skip, String current)
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
		
		int NrOfAllHistogramms = 14;//20;
		
		RCom2Com_stat= new Statistik[NrOfAllHistogramms];
		HG_RCom2Com_Plain = new Histogramm[NrOfAllHistogramms];
		HG_RCom2Com_Statistik = new HistogrammStatistik[NrOfAllHistogramms];
		
		EquLength= new double[NrOfAllHistogramms];
		OffSetF= new double[NrOfAllHistogramms];
		
		for(int i = 0; i < NrOfAllHistogramms; i++)
		{
			RCom2Com_stat[i] =new Statistik();
			HG_RCom2Com_Plain[i] = new Histogramm(+0.125,20.0+0.125,80);
			HG_RCom2Com_Statistik[i] = new HistogrammStatistik(+0.125,20.0+0.125,80);
			OffSetF[i]=0.0;
			EquLength[i]=3.0+0.5*i;
			System.out.println("EquLength["+i+"]: " + EquLength[i]);
		}
		
		HG_RCom2Com_StatistikTotal = new HistogrammStatistik(+0.125,20.0+0.125,80);
		
		/*histo_RCom2Com= new BFMFileSaver();
		histo_RCom2Com.DateiAnlegen(dirDst+"/"+FileName+"_Histo_RCom2Com.dat", false);
		histo_RCom2Com.setzeZeile("# radial probability distributions p(r)");
		histo_RCom2Com.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = p0*int_0 to inf p(r)*r^2 dr=1");
		histo_RCom2Com.setzeZeile("# intervall from ["+HG_RCom2Com.GetRangeInBinLowerLimit(0)+";"+HG_RCom2Com.GetRangeInBinLowerLimit(HG_RCom2Com.GetNrBins())+"]");
		histo_RCom2Com.setzeZeile("# intervall thickness: dI="+HG_RCom2Com.GetIntervallThickness());
		*/
		DecimalFormat dh = new DecimalFormat("000");
		
		
		//LoadFile(FileNameWithEnd, 1);
		for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
		{
			//LoadFile(fname+"R_"+EquLength[nrHisto]+".bfm",1,nrHisto);
			LoadFile(fname+"R_"+EquLength[nrHisto]+"_C_0.2_HardColloids.bfm",1,nrHisto);
		}
		
		
		//perform the normalization of histograms to spherical coordinates
		for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
		for(int i = 0; i < HG_RCom2Com_Plain[nrHisto].GetNrBins(); i++)
		{
			double testnorm = (HG_RCom2Com_Plain[nrHisto].GetNrInBinNormiert(i)/(4*Math.PI*HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)*HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)*HG_RCom2Com_Plain[nrHisto].GetIntervallThickness()));
		
			HG_RCom2Com_Statistik[nrHisto].AddValue(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i), testnorm);
		}
	
	//perform all calculation to achieve selfconsitency
	for(int selfconsitency = 0; selfconsitency < 200; selfconsitency++)
	{
		//calculate the normalization of total histograms
		double normTotal = 0.0;
		double weightTotal = 0.0;
		
		for(int i = 0; i < HG_RCom2Com_StatistikTotal.GetNrBins(); i++)
		{
			normTotal = 0.0;
			weightTotal = 0.0;
			
			for(int nrHisto = 0; nrHisto < NrOfAllHistogramms; nrHisto++)
			{
				//double normalisation=4*Math.PI*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)*HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i));
			
				normTotal += HG_RCom2Com_Plain[nrHisto].GetNrOfCounts()*Math.exp(- (0.5*springconstant*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto]) - OffSetF[nrHisto]) );
				weightTotal += HG_RCom2Com_Plain[nrHisto].GetNrOfCounts()*HG_RCom2Com_Statistik[nrHisto].GetAverageInBin(i);
			}
			
			// there are entries for the PMF
			if(normTotal != 0.0)
			{
				//double normalisation=4*Math.PI*(HG_RCom2Com_StatistikTotal.GetRangeInBin(i)*HG_RCom2Com_StatistikTotal.GetRangeInBin(i)*HG_RCom2Com_StatistikTotal.GetIntervallThickness());
				
				HG_RCom2Com_StatistikTotal.CleanBin(i);
				HG_RCom2Com_StatistikTotal.AddValue(HG_RCom2Com_StatistikTotal.GetRangeInBin(i), weightTotal/(normTotal));
			}
			else HG_RCom2Com_StatistikTotal.CleanBin(i);
			
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
			
			for(int i = 0; i < HG_RCom2Com_Plain[nrHisto].GetNrBins(); i++)
			{
				//double normalisation=4*Math.PI*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)*HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i));
				//integral += normalisation*HG_RCom2Com_Plain[nrHisto].GetIntervallThickness()*HG_RCom2Com_StatistikTotal.GetAverageInBin(i)*Math.exp(- (0.5*0.6*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])));
				
				integral += 4*Math.PI*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)*HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i))*HG_RCom2Com_Plain[nrHisto].GetIntervallThickness()*HG_RCom2Com_StatistikTotal.GetAverageInBin(i)*Math.exp(- (0.5*springconstant*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])*(HG_RCom2Com_Plain[nrHisto].GetRangeInBin(i)-EquLength[nrHisto])));
			
			}
			
			OffSetF[nrHisto] = -Math.log(integral);
			System.out.println("OffSetF["+nrHisto+"]: " + OffSetF[nrHisto]);
		}
		
		
	}
	
	
	//file dump
	
	BFMFileSaver histo_RPMF= new BFMFileSaver();
	histo_RPMF.DateiAnlegen(dirDst+"/"+FileName+"Histo_PMF_WHAM.dat", false);
	histo_RPMF.setzeZeile("# normalized to unity: 4*PI*int_0 to inf p(r)*r^2 dr = 1");
	histo_RPMF.setzeZeile("# calculation of the mean free energy F using WHAM");
	histo_RPMF.setzeZeile("# intervall from ["+HG_RCom2Com_StatistikTotal.GetRangeInBinLowerLimit(0)+";"+HG_RCom2Com_StatistikTotal.GetRangeInBinLowerLimit(HG_RCom2Com_StatistikTotal.GetNrBins())+"]");
	histo_RPMF.setzeZeile("# intervall thickness: dI="+HG_RCom2Com_StatistikTotal.GetIntervallThickness());
	histo_RPMF.setzeZeile("# r  p(r) F=-ln(p)");
	
	for(int i = 0; i < HG_RCom2Com_StatistikTotal.GetNrBins(); i++)
	{
		if(HG_RCom2Com_StatistikTotal.GetAverageInBin(i) != 0)
			histo_RPMF.setzeZeile(HG_RCom2Com_StatistikTotal.GetRangeInBin(i)+" "+HG_RCom2Com_StatistikTotal.GetAverageInBin(i) + " "+(-Math.log(HG_RCom2Com_StatistikTotal.GetAverageInBin(i))));
	}
	
	histo_RPMF.DateiSchliessen();
	
		
		
	}
	
	

}
