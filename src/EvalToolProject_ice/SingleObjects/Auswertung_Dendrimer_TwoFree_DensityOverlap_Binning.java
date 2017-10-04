package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Histogram2DStatistic;
import EvalToolProject_ice.tools.Histogramm;
import EvalToolProject_ice.tools.Histogramm2D;
import EvalToolProject_ice.tools.Histogramm3D;
import EvalToolProject_ice.tools.HistogrammStatistik;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;


public class Auswertung_Dendrimer_TwoFree_DensityOverlap_Binning {


	
	
	String FileName;
	String FileDirectorySrc;
	String FileDirectoryDst;
	
	int[] Polymersystem;
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
	int Lattice_x;
	int Lattice_y;
	int Lattice_z;
		
	BFMImportData importData;
	
	int Kettenanzahl;
	int Kettenlaenge;
	
	int[] dumpsystem;

	
	int NrOfHeparin = 0;
	int NrOfStars = 0;
	int NrOfMonomersPerStarArm = 0;
	
	int NrOfCounterions = 0;
	
	
	BFMFileSaver Histo2DSaver_DensityDendrimerOne;
	BFMFileSaver Histo2DSaver_DensityDendrimerOneGnuplot;
	
	BFMFileSaver Histo2DSaver_DensityDendrimerTwo;
	BFMFileSaver Histo2DSaver_DensityDendrimerTwoGnuplot;
	
	BFMFileSaver Histo2DSaver_DensityDendrimersTotal;
	BFMFileSaver Histo2DSaver_DensityDendrimersTotalGnuplot;
	
	int MONOMERZAHL;
	
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	
	
	int stupidcounter;
	
	//Histogramm2D HG2D_DensityDendrimerOne;
	//Histogramm2D HG2D_DensityDendrimerTwo;
	
	//Histogramm2D HG2D_DensityDendrimersTotal;
	
	Statistik[] Rc2cSquared_Stat; //center to center-separation
	
	Histogramm2D[] HG2D_DensityDendrimerOne;
	Histogramm2D[] HG2D_DensityDendrimerTwo;
	
	Histogramm2D[] HG2D_DensityDendrimersTotal;
	
	
	Statistik Rcom2com_Statistik;
	
	long lambdacounterPZ;
	long lambdacounterMZ;
	long lambdacounterPMZ;
	
	double minHistoCounterionsXY;
	double maxHistoCounterionsXY;
	double minHistoCounterionsZ;
	double maxHistoCounterionsZ;
	
	double numMonosDendrimerOne;
	double numMonosDendrimerTwo;
	
	int NrOfAllBins;
	double MinimumOfBinningRange;
	double IntervallOfBinningRange;
	
	public Auswertung_Dendrimer_TwoFree_DensityOverlap_Binning(String srcdir, String fname, String dstDir)
	{
		FileName =  fname;
		//FileDirectory = "/home/users/dockhorn/workspace/Evaluation_Tools/";
		FileDirectorySrc = srcdir;
		FileDirectoryDst = dstDir;
		
		
		minHistoCounterionsXY=-32.01;
		maxHistoCounterionsXY=32.01;
		int binHistoCounterionsXY = 64;
		
		minHistoCounterionsZ=-64.01;
		maxHistoCounterionsZ=64.01;
		int binHistoCounterionsZ = 128;
		
		//this means implicit [0.0+NrOfAllBins*IntervallOfBinningRange]
		int NrOfAllSimulations = 32; //64
		NrOfAllBins = 64; //128
		MinimumOfBinningRange = 0.0;
		IntervallOfBinningRange = 0.5;
		
		int minimumCounts = 50;
		
		Rc2cSquared_Stat = new Statistik[NrOfAllBins];
		HG2D_DensityDendrimerOne= new Histogramm2D[NrOfAllBins];
		HG2D_DensityDendrimerTwo= new Histogramm2D[NrOfAllBins];
		HG2D_DensityDendrimersTotal =  new Histogramm2D[NrOfAllBins];
		
		for(int i = 0; i < NrOfAllBins; i++)
		{
			Rc2cSquared_Stat[i] =new Statistik();
			
			HG2D_DensityDendrimerOne[i]= new Histogramm2D(0.0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
			HG2D_DensityDendrimerTwo[i]= new Histogramm2D(0.0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
			
			HG2D_DensityDendrimersTotal[i] = new Histogramm2D(0.0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
			
		}
		
		//HG2D_DensityDendrimerOne= new Histogramm2D(0.0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		//HG2D_DensityDendrimerTwo= new Histogramm2D(0.0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		//HG2D_DensityDendrimersTotal = new Histogramm2D(0.0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		
		
		Polymersystem = new int[1];
		skipFrames = 0;
		currentFrame = 1;
		//System.out.println("cf="+currentFrame);
		
		numMonosDendrimerOne = 0.0;
		numMonosDendrimerTwo = 0.0;
		
		
		lambdacounterPZ = 0;
		lambdacounterMZ = 0;
		lambdacounterPMZ = 0;
		
		Rcom2com_Statistik = new Statistik() ;
		
		
	
		
				
		System.out.println("file : " +FileName );
		System.out.println("dirDst : " + FileDirectoryDst);
		
		DecimalFormat dh = new DecimalFormat("000");
		
		//-------------------------------
		//load file
		for(int nrSim = 0; nrSim < NrOfAllSimulations; nrSim++)
		{
			//LoadFile(fname+"L"+(int)(EquLength[nrHisto])+".bfm",1,nrHisto);
			LoadFile(fname+"_L"+(int)(1+1*nrSim)+"_reduced.bfm",100);
			//LoadFile(fname+"R_"+EquLength[nrHisto]+"_C_0.2_HardColloids.bfm",1,nrHisto);
		}
		//LoadFile(FileName+".bfm", 10);
		//-------------------------------
		
		
		
		// printout of the densities
	for(int i = 0; i < NrOfAllBins; i++)
	{
		double rc2c = Math.sqrt(Rc2cSquared_Stat[i].ReturnM1());
		
		//check for accurate statistics
		if(Rc2cSquared_Stat[i].ReturnN() > minimumCounts)
		{
		Histo2DSaver_DensityDendrimerOneGnuplot = new BFMFileSaver();
		
		Histo2DSaver_DensityDendrimerOneGnuplot.DateiAnlegen(FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityOne_Gnuplot.gnu", false);
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set term postscript portrait enhanced color");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set output \'"+FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityOne.ps"+"\'");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set view map");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("unset surface");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("#set cbrange [0:1]");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set style data pm3d");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set style function pm3d");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set xlabel \"{/Symbol r}\" ");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set ylabel \"z\" ");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set pm3d implicit at b");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set title \"density distribution of 1.st Dendrimer (centered)\\n N = {/Symbol \\362} {/Symbol r}_1 {/Symbol r} d {/Symbol r} dz\" ");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("unset key");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set xrange [0:"+maxHistoCounterionsXY+"]");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set yrange ["+minHistoCounterionsZ+":"+maxHistoCounterionsZ+"]");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set size ratio -1");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("#set size square");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("#set palette defined (0 \"white\", 0.001 \"red\", 0.002 \"blue\")");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("#set palette model CMY rgbformulae 7,5,15");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("#set palette rgb 30,31,32; set palette negative");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("#set palette model RGB");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("#set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000')");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000'); set palette negative");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("#set palette defined ( 0 '#000090', 1 '#000fff', 2 '#0090ff', 3 '#0fffee', 4 '#90ff70', 5 '#ffee00', 6 '#ff7000', 7 '#ee0000', 8 '#7f0000')");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("#set palette rgbformulae 22,13,-31");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeZeile("splot \'"+FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityOne.dat"+"\' matrix using (($1/"+binHistoCounterionsXY+")*"+maxHistoCounterionsXY+"):(0.5*"+HG2D_DensityDendrimerOne[i].GetIntervallThicknessOfBins2()+"+"+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		Histo2DSaver_DensityDendrimerOneGnuplot.setzeLeerzeile();
		Histo2DSaver_DensityDendrimerOneGnuplot.DateiSchliessen();
		}
	}
		
	for(int i = 0; i < NrOfAllBins; i++)
	{
		double rc2c = Math.sqrt(Rc2cSquared_Stat[i].ReturnM1());
		
		//check for accurate statistics
		if(Rc2cSquared_Stat[i].ReturnN() > minimumCounts)
		{
		Histo2DSaver_DensityDendrimerTwoGnuplot= new BFMFileSaver();
		
		Histo2DSaver_DensityDendrimerTwoGnuplot.DateiAnlegen(FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityTwo_Gnuplot.gnu", false);
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set term postscript portrait enhanced color");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set output \'"+FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityTwo.ps"+"\'");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set view map");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("unset surface");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("#set cbrange [0:1]");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set style data pm3d");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set style function pm3d");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set xlabel \"{/Symbol r}\" ");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set ylabel \"z\" ");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set pm3d implicit at b");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set title \"density distribution of 2.nd Dendrimer (centered)\\n N = {/Symbol \\362} {/Symbol r}_2 {/Symbol r} d {/Symbol r} dz\" ");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("unset key");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set xrange [0:"+maxHistoCounterionsXY+"]");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set yrange ["+minHistoCounterionsZ+":"+maxHistoCounterionsZ+"]");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set size ratio -1");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("#set size square");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("#set palette defined (0 \"white\", 0.001 \"red\", 0.002 \"blue\")");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("#set palette model CMY rgbformulae 7,5,15");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("#set palette rgb 30,31,32; set palette negative");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("#set palette model RGB");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("#set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000')");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000'); set palette negative");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("#set palette defined ( 0 '#000090', 1 '#000fff', 2 '#0090ff', 3 '#0fffee', 4 '#90ff70', 5 '#ffee00', 6 '#ff7000', 7 '#ee0000', 8 '#7f0000')");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("#set palette rgbformulae 22,13,-31");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeZeile("splot \'"+FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityTwo.dat"+"\' matrix using (($1/"+binHistoCounterionsXY+")*"+maxHistoCounterionsXY+"):(0.5*"+HG2D_DensityDendrimerTwo[i].GetIntervallThicknessOfBins2()+"+"+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		Histo2DSaver_DensityDendrimerTwoGnuplot.setzeLeerzeile();
		Histo2DSaver_DensityDendrimerTwoGnuplot.DateiSchliessen();
		}
	}
		
	for(int i = 0; i < NrOfAllBins; i++)
	{
		double rc2c = Math.sqrt(Rc2cSquared_Stat[i].ReturnM1());
		
		//check for accurate statistics
		if(Rc2cSquared_Stat[i].ReturnN() > minimumCounts)
		{
		Histo2DSaver_DensityDendrimersTotalGnuplot= new BFMFileSaver();
		Histo2DSaver_DensityDendrimersTotalGnuplot.DateiAnlegen(FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityTotal_Gnuplot.gnu", false);
		
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set term postscript portrait enhanced color");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set output \'"+FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityTotal.ps"+"\'");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set view map");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("unset surface");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("#set cbrange [0:1]");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set style data pm3d");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set style function pm3d");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set xlabel \"{/Symbol r}\" ");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set ylabel \"z\" ");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set pm3d implicit at b");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set title \"total density distribution of both dendrimers (centered)\\n 2N = {/Symbol \\362} ({/Symbol r}_1 + {/Symbol r}_2) {/Symbol r} d {/Symbol r} dz\" ");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("unset key");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set xrange [0:"+maxHistoCounterionsXY+"]");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set yrange ["+minHistoCounterionsZ+":"+maxHistoCounterionsZ+"]");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set size ratio -1");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("#set size square");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("#set palette defined (0 \"white\", 0.001 \"red\", 0.002 \"blue\")");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("#set palette model CMY rgbformulae 7,5,15");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("#set palette rgb 30,31,32; set palette negative");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("#set palette model RGB");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("#set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000')");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000'); set palette negative");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("#set palette defined ( 0 '#000090', 1 '#000fff', 2 '#0090ff', 3 '#0fffee', 4 '#90ff70', 5 '#ffee00', 6 '#ff7000', 7 '#ee0000', 8 '#7f0000')");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("#set palette rgbformulae 22,13,-31");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeZeile("splot \'"+FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityTotal.dat"+"\' matrix using (($1/"+binHistoCounterionsXY+")*"+maxHistoCounterionsXY+"):(0.5*"+HG2D_DensityDendrimersTotal[i].GetIntervallThicknessOfBins2()+"+"+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		Histo2DSaver_DensityDendrimersTotalGnuplot.setzeLeerzeile();
		Histo2DSaver_DensityDendrimersTotalGnuplot.DateiSchliessen();
		}
	}
		
		
		
			

		
	
	//calculate and printout of the monomer density of dendrimer one
	for(int i = 0; i < NrOfAllBins; i++)
	{
		double rc2c = Math.sqrt(Rc2cSquared_Stat[i].ReturnM1());
		
		//check for accurate statistics
		if(Rc2cSquared_Stat[i].ReturnN() > minimumCounts)
		{
	Histo2DSaver_DensityDendrimerOne = new BFMFileSaver();
	Histo2DSaver_DensityDendrimerOne.DateiAnlegen(FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityOne.dat", false);
	
	for(int z = 0; z < (HG2D_DensityDendrimerOne[i].GetNrBins2() );z++)
	{
		StringBuffer objectBuffer = new StringBuffer(3000); 
		
		for(int rho = 0; rho < (HG2D_DensityDendrimerOne[i].GetNrBins1() );rho++)
		{	
			double  Probabilitydensity_HG2D_Rod = numMonosDendrimerOne*HG2D_DensityDendrimerOne[i].GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_DensityDendrimerOne[i].GetRangeInBin1(rho)*HG2D_DensityDendrimerOne[i].GetIntervallThicknessOfBins1()*HG2D_DensityDendrimerOne[i].GetIntervallThicknessOfBins2()); 
			
			objectBuffer.append(((float) Probabilitydensity_HG2D_Rod ) + " ");
		}
		Histo2DSaver_DensityDendrimerOne.setzeZeile(objectBuffer.toString());
	}
	Histo2DSaver_DensityDendrimerOne.DateiSchliessen();
		}
	}

	for(int i = 0; i < NrOfAllBins; i++)
	{
		double rc2c = Math.sqrt(Rc2cSquared_Stat[i].ReturnM1());
	
		//check for accurate statistics
		if(Rc2cSquared_Stat[i].ReturnN() > minimumCounts)
		{
	Histo2DSaver_DensityDendrimerTwo= new BFMFileSaver();
	Histo2DSaver_DensityDendrimerTwo.DateiAnlegen(FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityTwo.dat", false);
	
	for(int z = 0; z < (HG2D_DensityDendrimerTwo[i].GetNrBins2() );z++)
	{
		StringBuffer objectBuffer = new StringBuffer(3000); 
		
		for(int rho = 0; rho < (HG2D_DensityDendrimerTwo[i].GetNrBins1() );rho++)
		{	
			double  Probabilitydensity_HG2D_Rod = numMonosDendrimerTwo*HG2D_DensityDendrimerTwo[i].GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_DensityDendrimerTwo[i].GetRangeInBin1(rho)*HG2D_DensityDendrimerTwo[i].GetIntervallThicknessOfBins1()*HG2D_DensityDendrimerTwo[i].GetIntervallThicknessOfBins2()); 
			
			objectBuffer.append(((float) Probabilitydensity_HG2D_Rod ) + " ");
		}
		Histo2DSaver_DensityDendrimerTwo.setzeZeile(objectBuffer.toString());
	}
	Histo2DSaver_DensityDendrimerTwo.DateiSchliessen();
		}
	}
	
	for(int i = 0; i < NrOfAllBins; i++)
	{
		double rc2c = Math.sqrt(Rc2cSquared_Stat[i].ReturnM1());
		
		//check for accurate statistics
		if(Rc2cSquared_Stat[i].ReturnN() > minimumCounts)
		{
	Histo2DSaver_DensityDendrimersTotal= new BFMFileSaver();
	Histo2DSaver_DensityDendrimersTotal.DateiAnlegen(FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityTotal.dat", false);
	
	for(int z = 0; z < (HG2D_DensityDendrimersTotal[i].GetNrBins2() );z++)
		{
			StringBuffer objectBuffer = new StringBuffer(3000); 
			
			for(int rho = 0; rho < (HG2D_DensityDendrimersTotal[i].GetNrBins1() );rho++)
			{	
				
				//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
				//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
				double  Probabilitydensity_HG2D_Counterions = (numMonosDendrimerOne+numMonosDendrimerTwo)*HG2D_DensityDendrimersTotal[i].GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_DensityDendrimersTotal[i].GetRangeInBin1(rho)*HG2D_DensityDendrimersTotal[i].GetIntervallThicknessOfBins1()*HG2D_DensityDendrimersTotal[i].GetIntervallThicknessOfBins2()); 
				
				objectBuffer.append(((float) Probabilitydensity_HG2D_Counterions ) + " ");
				
				//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
				
			}
		
			Histo2DSaver_DensityDendrimersTotal.setzeZeile(objectBuffer.toString());
		}
	Histo2DSaver_DensityDendrimersTotal.DateiSchliessen();
		}
	}
	
	//----------------------------------------------------------
	//
	//overlap integral distribution
	//
	//----------------------------------------------------------
	double[] OverlapIntegral = new double[NrOfAllBins];
	
	for(int i = 0; i < NrOfAllBins; i++)
	{
		double rc2c = Math.sqrt(Rc2cSquared_Stat[i].ReturnM1());
	
		//check for accurate statistics
		if(Rc2cSquared_Stat[i].ReturnN() > minimumCounts)
		{
	Histogram2DStatistic HG2D_OverlapDensityDendrimers= new Histogram2DStatistic(0.0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
	
	double overlapIntegral = 0.0;
	
	for(int z = 0; z < (HG2D_OverlapDensityDendrimers.GetNrBins2() );z++)
	{
		for(int rho = 0; rho < (HG2D_OverlapDensityDendrimers.GetNrBins1() );rho++)
		{	
			double densityProduct = 0.0;
			double density_HG2D_DOne = numMonosDendrimerOne*HG2D_DensityDendrimerOne[i].GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_DensityDendrimerOne[i].GetRangeInBin1(rho)*HG2D_DensityDendrimerOne[i].GetIntervallThicknessOfBins1()*HG2D_DensityDendrimerOne[i].GetIntervallThicknessOfBins2()); 
			double density_HG2D_DTwo = numMonosDendrimerTwo*HG2D_DensityDendrimerTwo[i].GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_DensityDendrimerTwo[i].GetRangeInBin1(rho)*HG2D_DensityDendrimerTwo[i].GetIntervallThicknessOfBins1()*HG2D_DensityDendrimerTwo[i].GetIntervallThicknessOfBins2()); 
			
			overlapIntegral += density_HG2D_DTwo*density_HG2D_DOne*2.0*Math.PI*HG2D_OverlapDensityDendrimers.GetRangeInBin1(rho)*HG2D_OverlapDensityDendrimers.GetIntervallThicknessOfBins1()*HG2D_OverlapDensityDendrimers.GetIntervallThicknessOfBins2();
					
			densityProduct = density_HG2D_DOne*density_HG2D_DTwo;
			
			HG2D_OverlapDensityDendrimers.AddValueInBin(rho, z, densityProduct);
			//HG2D_OverlapDensityDendrimers.AddValue(HG2D_OverlapDensityDendrimers.GetRangeInBin1(rho), HG2D_OverlapDensityDendrimers.GetRangeInBin2(z), densityProduct);
		}
	}
	
	OverlapIntegral[i] = overlapIntegral;
	
	
	BFMFileSaver Histo2DSaver_DensityOverlapDendrimersGnuplot= new BFMFileSaver();
	Histo2DSaver_DensityOverlapDendrimersGnuplot.DateiAnlegen(FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityOverlapDendrimers_Gnuplot.gnu", false);
	
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set term postscript portrait enhanced color");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set output \'"+FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityOverlapDendrimers.ps"+"\'");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set view map");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("unset surface");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("#set cbrange [0:1]");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set style data pm3d");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set style function pm3d");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set xlabel \"{/Symbol r}\" ");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set ylabel \"z\" ");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set pm3d implicit at b");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set title \"density overlap distribution of both dendrimers (centered)\\n N^{2}/V = {/Symbol \\362} ({/Symbol r}_1 {/Symbol \\267} {/Symbol r}_2) {/Symbol r} d {/Symbol r} dz\" ");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("unset key");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set xrange [0:"+maxHistoCounterionsXY+"]");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set yrange ["+minHistoCounterionsZ+":"+maxHistoCounterionsZ+"]");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set size ratio -1");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("#set size square");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("#set palette defined (0 \"white\", 0.001 \"red\", 0.002 \"blue\")");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("#set palette model CMY rgbformulae 7,5,15");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("#set palette rgb 30,31,32; set palette negative");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("#set palette model RGB");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("#set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000')");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000'); set palette negative");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("#set palette defined ( 0 '#000090', 1 '#000fff', 2 '#0090ff', 3 '#0fffee', 4 '#90ff70', 5 '#ffee00', 6 '#ff7000', 7 '#ee0000', 8 '#7f0000')");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("#set palette rgbformulae 22,13,-31");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeZeile("splot \'"+FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityOverlapDendrimers.dat"+"\' matrix using (($1/"+binHistoCounterionsXY+")*"+maxHistoCounterionsXY+"):(0.5*"+HG2D_OverlapDensityDendrimers.GetIntervallThicknessOfBins2()+"+"+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
	Histo2DSaver_DensityOverlapDendrimersGnuplot.setzeLeerzeile();
	Histo2DSaver_DensityOverlapDendrimersGnuplot.DateiSchliessen();

	BFMFileSaver Histo2DSaver_DensityOverlapDendrimers= new BFMFileSaver();
	Histo2DSaver_DensityOverlapDendrimers.DateiAnlegen(FileDirectoryDst+FileName+"_bin_"+dh.format(i)+"_rc2c_"+rc2c+"_Histo2D_TwoDendrimers_DensityOverlapDendrimers.dat", false);
	
	for(int z = 0; z < (HG2D_OverlapDensityDendrimers.GetNrBins2() );z++)
		{
			StringBuffer objectBuffer = new StringBuffer(3000); 
			
			for(int rho = 0; rho < (HG2D_OverlapDensityDendrimers.GetNrBins1() );rho++)
			{	
				double  overlapDensityDendrimers = HG2D_OverlapDensityDendrimers.GetAverageInBin(rho,z); 
				
				objectBuffer.append(((float) overlapDensityDendrimers) + " ");	
			}
		
			Histo2DSaver_DensityOverlapDendrimers.setzeZeile(objectBuffer.toString());
		}
	Histo2DSaver_DensityOverlapDendrimers.DateiSchliessen();
	
	}
	}
	
	// overlap integral - output
	BFMFileSaver Saver_OverlapIntegral= new BFMFileSaver();
		
	Saver_OverlapIntegral.DateiAnlegen(FileDirectoryDst+FileName+"_TwoDendrimers_OverlapIntegral_Binning.dat", false);
		//Saver_OverlapIntegral.DateiAnlegen(FileDirectoryDst+FileName+"_OverlapIntegral.dat", false);
	Saver_OverlapIntegral.setzeZeile("# Average overlap integral I = int <rho1>*<rho2> dV");
	Saver_OverlapIntegral.setzeZeile("# Averaging only in bins and direct output of mean value in bin");
	Saver_OverlapIntegral.setzeZeile("# intervall from ["+MinimumOfBinningRange+";"+GetRangeInBin(NrOfAllBins)+"]");
	Saver_OverlapIntegral.setzeZeile("# intervall thickness: dI="+IntervallOfBinningRange);
	Saver_OverlapIntegral.setzeZeile("#");
	Saver_OverlapIntegral.setzeZeile("# <(rc2c)^2>^0.5  OverlapIntegral  CountsInBinRange  MeanValueOfBin");

	for(int i = 0; i < NrOfAllBins; i++)
	{
		double rc2c = Math.sqrt(Rc2cSquared_Stat[i].ReturnM1());
		
		//check for accurate statistics
		if(Rc2cSquared_Stat[i].ReturnN() > minimumCounts)
		Saver_OverlapIntegral.setzeZeile(rc2c+" "+OverlapIntegral[i] +" " +Rc2cSquared_Stat[i].ReturnN() + " " + GetRangeInBin(i));
	}
	
	Saver_OverlapIntegral.DateiSchliessen();

	//System.out.println("int density1*density2 drho dz =  " +overlapIntegral);
	
		
		
	
		//rg.DateiSchliessen();
		//newDataclass.DateiSchliessen();
		System.out.println("pZ : " +lambdacounterPZ + "    MZ : " +lambdacounterMZ+ "    +-Z : " +lambdacounterPMZ);
		System.out.println("reeRod : " +Rcom2com_Statistik.ReturnM1());
		
	}
	
	protected void LoadFile(String file, int startframe)
	{
		//FileName = file;
		
		System.out.println("lade System");
		LadeSystem(FileDirectorySrc, file);	
		
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(FileDirectorySrc+file);
		  
		 importData.GetFrameOfSimulation(currentFrame);
		  
		  importData.CloseSimulationFile();
		  
		  importData.OpenSimulationFile(FileDirectorySrc+file);
			
		  
			System.out.println("file : " +file );
			System.out.println("dirSrc : " + FileDirectorySrc);
			skipFrames=0;
			int z = currentFrame;//1;
			stupidcounter=0;
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
		
		if(args.length != 3)
		{
			System.out.println("Calculation of Density of Two Dendrimers");
			System.out.println("Dendrimers will be Oriented in ez Direction");
			System.out.println("USAGE: SrcDir/ FileName[.bfm] DstDir/");
			System.exit(1);
		}
		System.out.println("Calculation of Density of Counter-ions around HEP-Rod");
		new Auswertung_Dendrimer_TwoFree_DensityOverlap_Binning(args[0], args[1], args[2]);
	}
	
	public void LadeSystem(String FileDir, String FileName)
	{
		importData=null;
		importData = new BFMImportData(FileDir+FileName);
		
		MONOMERZAHL = importData.NrOfMonomers + 1;
		
		Lattice_x = importData.box_x;
		Lattice_y = importData.box_y;
		Lattice_z = importData.box_z;
		
		
		
		Polymersystem = null;
		Polymersystem = new int[MONOMERZAHL];
		
		dumpsystem = null;
		dumpsystem = new int[MONOMERZAHL];
	
		boolean periodRB_x = importData.periodic_x;
		boolean periodRB_y = importData.periodic_y;
		boolean periodRB_z = importData.periodic_z;
		
		Kettenlaenge = importData.NrOfMonomers;
		Kettenanzahl = 1;
		
		NrOfStars = importData.NrOfStars;
		NrOfMonomersPerStarArm = importData.NrOfMonomersPerStarArm;
		NrOfHeparin = importData.NrOfHeparin;
		
		if(importData.NrOfChargesPerHeparin != 0)
			NrOfCounterions = importData.NrOfMonomers - 90*NrOfHeparin - (4*NrOfMonomersPerStarArm+1)*NrOfStars;
		
		System.out.println("Stars: " + NrOfStars + "    ArmLength: "+ NrOfMonomersPerStarArm + "    Heparin:"+NrOfHeparin + "  Counterions: "+ NrOfCounterions + "  NrChargesHeparins:" + importData.NrOfChargesPerHeparin*NrOfHeparin);
		
		boolean periodRB = false;
		
		if ((periodRB_x == true) || (periodRB_y == true) || (periodRB_z == true))
			periodRB = true;
		
		
		//System.arraycopy(importData.Polymersystem,0,Polymersystem,0, Polymersystem.length);
		
		
		
		
		
		NrofFrames = importData.GetNrOfFrames();
	
		String zutrt = 	FileName.substring(0, FileName.length()-4);

		System.out.println("String : " + zutrt);
		
		
		
	}
	
	public void playSimulation(int frame)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				importData.GetFrameOfSimulation( frame);
				//int AnzahlMono = dumpsystem.length-1;
				
				

				double rcm_xA=0;
				double rcm_yA=0;
				double rcm_zA=0;
				
				int counterCOM_A =0;
				
				for(int j=1; j <= importData.NrOfMonomers; j++)
				{
					if(importData.Attributes[j]==1)
					{
					rcm_xA += importData.PolymerKoordinaten[j][0];
					rcm_yA += importData.PolymerKoordinaten[j][1];
					rcm_zA += importData.PolymerKoordinaten[j][2];
					
					counterCOM_A++;
					}
				}
				
				rcm_xA /= (1.0*counterCOM_A);
				rcm_yA /= (1.0*counterCOM_A);
				rcm_zA /= (1.0*counterCOM_A);
				
				double rcm_xB=0;
				double rcm_yB=0;
				double rcm_zB=0;
				
				int counterCOM_B =0;
				
				for(int j=1; j <= importData.NrOfMonomers; j++)
				{
					if(importData.Attributes[j]==2)
					{
					rcm_xB += importData.PolymerKoordinaten[j][0];
					rcm_yB += importData.PolymerKoordinaten[j][1];
					rcm_zB += importData.PolymerKoordinaten[j][2];
					
					counterCOM_B++;
					}
				}
				
				rcm_xB /= (1.0*counterCOM_B);
				rcm_yB /= (1.0*counterCOM_B);
				rcm_zB /= (1.0*counterCOM_B);
				
				numMonosDendrimerOne = counterCOM_A;
				numMonosDendrimerTwo = counterCOM_B;
				
				System.out.println("used monomers: " +counterCOM_A + " and " +counterCOM_B);
				
				
				//for (int l= 1; l <= NrOfHeparin; l++)
				{
				double ree_x_original  =rcm_xB-rcm_xA; 
				double ree_y_original  =rcm_yB-rcm_yA;
				double ree_z_original  =rcm_zB-rcm_zA;
					  
			    double reeOrigin_x_original = rcm_xA;
			    double reeOrigin_y_original = rcm_yA;
			    double reeOrigin_z_original = rcm_zA;
			    
			    // find the radius of the ground area
			    double ree_original = Math.sqrt(ree_x_original*ree_x_original+ree_y_original*ree_y_original+ree_z_original*ree_z_original);
				double ree_xy_original = Math.sqrt(ree_x_original*ree_x_original+ree_y_original*ree_y_original);
						
				// calculate the Rc2cSquared as rounded index
				// round to the lowest next integer e.g.
				// 0.5 -> 0 with dI=1.0
				// 0.5 -> 1 with dI=0.5
				int Rc2cBinIndex = GetBinOfValue(ree_original);
				
				if(Rc2cBinIndex < NrOfAllBins)
				Rc2cSquared_Stat[Rc2cBinIndex].AddValue(ree_original*ree_original);
			           
			    double cylinderUnitAxis_x = ree_x_original/ree_original;
			    double cylinderUnitAxis_y = ree_y_original/ree_original;
			    double cylinderUnitAxis_z = ree_z_original/ree_original;
			     
			    // Align clinderaxis on ez
			    // Rotation along axis eRot=eCylinder cross ez
				
			    double eRot_x = cylinderUnitAxis_y;
			    double eRot_y = -cylinderUnitAxis_x;
			    // double eRot_z = 0 due to cross ez
			    
			    //double AngleRot = Math.asin(Math.sqrt(eRot_x*eRot_x+eRot_y*eRot_y));
			    double AngleRot = Math.acos(cylinderUnitAxis_z);
			    double CosRot = Math.cos(AngleRot);
			    double OneMinusCosRot = 1.0-Math.cos(AngleRot);
			    double SinRot = Math.sin(AngleRot);
			    
			    double eRot_x_norm = eRot_x / Math.sqrt(eRot_x*eRot_x+eRot_y*eRot_y);
			    double eRot_y_norm = eRot_y / Math.sqrt(eRot_x*eRot_x+eRot_y*eRot_y);
			    
			    //Trafo-Matrix
			    double xOld=ree_x_original;//cylinderUnitAxis_x;
			    double yOld=ree_y_original;//cylinderUnitAxis_y;
			    double zOld=ree_z_original;//cylinderUnitAxis_z;
			    
			    double xNew = (CosRot+OneMinusCosRot*eRot_x_norm*eRot_x_norm)*xOld + (eRot_x_norm*eRot_y_norm)*OneMinusCosRot*yOld        + eRot_y_norm*SinRot*zOld;
			    double yNew = (OneMinusCosRot*eRot_x_norm*eRot_y_norm)*xOld        + (CosRot+OneMinusCosRot*eRot_y_norm*eRot_y_norm)*yOld - eRot_x_norm*SinRot*zOld;
			    double zNew = (-eRot_y_norm*SinRot)*xOld						   + (eRot_x_norm*SinRot)*yOld							  + CosRot*zOld;
			    	
			    double Ree_Trafo = Math.sqrt(xNew*xNew+yNew*yNew+zNew*zNew);
				double Ree_xy_Trafo = Math.sqrt(xNew*xNew+yNew*yNew);
				
				//by definition NewX and NewY == 0!!! 
				//by definition NewX and NewY == 0!!! 
				//System.out.println("OldX: " + xOld + "   OldY: " + yOld + "   OldZ: " + zOld + "   ReeOld: "+ree_original + "   Ree_xyOld: "+ree_xy_original);
			    //System.out.println("NewX: " + xNew + "   NewY: " + yNew + "   NewZ: " + zNew + "   ReeNew: "+Ree_Trafo + "   Ree_xyNew: "+Ree_xy_Trafo);
				
			    //if transformation goes wrong
				/*if((AngleRot*360.0/(2.0*Math.PI) - ((Math.PI-Math.asin(Math.sqrt(eRot_x*eRot_x+eRot_y*eRot_y)))*360.0/(2.0*Math.PI))) > 1e-04 || (AngleRot*360.0/(2.0*Math.PI) - ((Math.PI-Math.asin(Math.sqrt(eRot_x*eRot_x+eRot_y*eRot_y)))*360.0/(2.0*Math.PI))) < -1e-04 )
				{
					System.out.println("phicos: " + (AngleRot*360.0/(2.0*Math.PI)) + "  phisin:" +((Math.PI-Math.asin(Math.sqrt(eRot_x*eRot_x+eRot_y*eRot_y)))*360.0/(2.0*Math.PI)));
				    System.out.println(" OldX: " + xOld + "   OldY: " + yOld + "   OldZ: " + zOld + "   ReeOld: "+ree_original + "   Ree_xyOld: "+ree_xy_original);
			        System.out.println("NewCosX: " + xNew + "   NewCosY: " + yNew + "   NewCosZ: " + zNew + "   ReeCosNew: "+Ree_Trafo + "   Ree_xyNew: "+Ree_xy_Trafo);
			  
			        //Trafo-Matrix
				    double xOldTrafo=ree_x_original;//cylinderUnitAxis_x;
				    double yOldTrafo=ree_y_original;//cylinderUnitAxis_y;
				    double zOldTrafo=ree_z_original;//cylinderUnitAxis_z;
				    
				    double AngleRotTrafo = Math.PI-Math.asin(Math.sqrt(eRot_x*eRot_x+eRot_y*eRot_y));
				    double CosRotTrafo = Math.cos(AngleRotTrafo);
				    double OneMinusCosRotTrafo = 1.0-Math.cos(AngleRotTrafo);
				    double SinRotTrafo = Math.sin(AngleRotTrafo);
				    
				    
				    double xNewTrafo = (CosRotTrafo+OneMinusCosRotTrafo*eRot_x_norm*eRot_x_norm)*xOldTrafo + (eRot_x_norm*eRot_y_norm)*OneMinusCosRotTrafo*yOldTrafo        + eRot_y_norm*SinRotTrafo*zOldTrafo;
				    double yNewTrafo = (OneMinusCosRotTrafo*eRot_x_norm*eRot_y_norm)*xOldTrafo        + (CosRotTrafo+OneMinusCosRotTrafo*eRot_y_norm*eRot_y_norm)*yOldTrafo - eRot_x_norm*SinRotTrafo*zOldTrafo;
				    double zNewTrafo = (-eRot_y_norm*SinRotTrafo)*xOldTrafo					   		+ (eRot_x_norm*SinRotTrafo)*yOldTrafo   						    + CosRotTrafo*zOldTrafo;
				    
				    
				    double ReeTrafo = Math.sqrt(xNewTrafo*xNewTrafo+yNewTrafo*yNewTrafo+zNewTrafo*zNewTrafo);
				    System.out.println("NewSinX: " + xNewTrafo + "   NewSinY: " + yNewTrafo + "   NewSinZ: " + zNewTrafo + "   ReeSinNew: "+ReeTrafo);
					  
			        // System.out.println("eRot_x_norm: " + eRot_x_norm + "   eRot_y_norm: " + eRot_y_norm);
				}
				*/
			    // Maybe the Rotation doesn´t work
			    if( (AngleRot*360.0/(2.0*Math.PI)) > 180.0 || (AngleRot*360.0/(2.0*Math.PI)) < 0.0 )
			    	System.exit(2);
			    // Maybe the Rotation doesn´t work
			    if(xNew > 1.0 || xNew < -1.0 || yNew > 1.0 || yNew < -1.0 || zNew < -1.0)
			    	System.exit(2);
			    	 //break;
			    

				Rcom2com_Statistik.AddValue(ree_original);
				
				
				stupidcounter++;
				
				if(Rc2cBinIndex < NrOfAllBins)
				{// calculation only for bins
					
					
				//distribution of DendrimerOne
				for(int monoDendrimerOne = 1; monoDendrimerOne <= counterCOM_A; monoDendrimerOne++)
				{
					//return ( ((x1-x2) < LATTICE_HALF_NEGATIV) ? (x1-x2+LATTICE) : ( ((x1-x2) > LATTICE_HALF) ? (x1-x2-LATTICE) : (x1-x2) ) );
					
					//find the minimum 'image'
					
					int co_X = importData.PolymerKoordinaten[monoDendrimerOne][0];
					int co_Y = importData.PolymerKoordinaten[monoDendrimerOne][1];
					int co_Z = importData.PolymerKoordinaten[monoDendrimerOne][2];
					
					// relative distance between Origin HEP and Counterion
					double relDistance_X =0.0;
					double relDistance_Y =0.0;
					double relDistance_Z =0.0;
					
					relDistance_X = co_X-reeOrigin_x_original;//((co_X-reeOrigin_x_original) < -0.5*Lattice_x) ? (co_X-reeOrigin_x_original+Lattice_x) : ( ((co_X-reeOrigin_x_original) > 0.5*Lattice_x) ? (co_X-reeOrigin_x_original-Lattice_x) : (co_X-reeOrigin_x_original) ) ;
					relDistance_Y = co_Y-reeOrigin_y_original;//((co_Y-reeOrigin_y_original) < -0.5*Lattice_y) ? (co_Y-reeOrigin_y_original+Lattice_y) : ( ((co_Y-reeOrigin_y_original) > 0.5*Lattice_y) ? (co_Y-reeOrigin_y_original-Lattice_y) : (co_Y-reeOrigin_y_original) ) ;
					relDistance_Z = co_Z-reeOrigin_z_original;//((co_Z-reeOrigin_z_original) < -0.5*Lattice_z) ? (co_Z-reeOrigin_z_original+Lattice_z) : ( ((co_Z-reeOrigin_z_original) > 0.5*Lattice_z) ? (co_Z-reeOrigin_z_original-Lattice_z) : (co_Z-reeOrigin_z_original) ) ;
					
					//System.out.println("reeOrigin_x: " + reeOrigin_x_original + "   reeOrigin_y: " + reeOrigin_y_original + "   reeOrigin_z: " + reeOrigin_z_original);
					//System.out.println("co_X: " + co_X + "   co_Y: " + co_Y + "   co_Z: " + co_Z);
					//System.out.println("relDistance_X: " + relDistance_X + "   relDistance_Y: " + relDistance_Y + "   relDistance_Z: " + relDistance_Z);
					
					//Calculate the crossing-point
					//LambdaPoint=0 origin of HEP-rod
					//LambdaPoint=1 end of HEP-rod
					
					double LambdaPoint = (relDistance_X*ree_x_original+relDistance_Y*ree_y_original+relDistance_Z*ree_z_original)/(ree_original*ree_original);
					
					double distance_X = relDistance_X-LambdaPoint*ree_x_original;
					double distance_Y = relDistance_Y-LambdaPoint*ree_y_original;
					double distance_Z = relDistance_Z-LambdaPoint*ree_z_original;
					
					
					double distance = Math.sqrt(distance_X*distance_X + distance_Y*distance_Y + distance_Z*distance_Z);
					
					//Trafo-Matrix
					double xOldTrafo=distance_X;//ree_x;//cylinderUnitAxis_x;
				    double yOldTrafo=distance_Y;//ree_y;//cylinderUnitAxis_y;
				    double zOldTrafo=distance_Z;//ree_z;//cylinderUnitAxis_z;
				    
				    double distance_xNew = (CosRot+OneMinusCosRot*eRot_x_norm*eRot_x_norm)*xOldTrafo + (eRot_x_norm*eRot_y_norm)*OneMinusCosRot*yOldTrafo        + eRot_y_norm*SinRot*zOldTrafo;
				    double distance_yNew = (OneMinusCosRot*eRot_x_norm*eRot_y_norm)*xOldTrafo        + (CosRot+OneMinusCosRot*eRot_y_norm*eRot_y_norm)*yOldTrafo - eRot_x_norm*SinRot*zOldTrafo;
				    double distance_zNew = (-eRot_y_norm*SinRot)*xOldTrafo  						    + (eRot_x_norm*SinRot)*yOldTrafo						    + CosRot*zOldTrafo;
				    
				    //after Trafo distance_zNew = 0 by definition
				    
				    
				    double distance_New = Math.sqrt(distance_xNew*distance_xNew + distance_yNew*distance_yNew + distance_zNew*distance_zNew);
				    double distance_New_xy = Math.sqrt(distance_xNew*distance_xNew + distance_yNew*distance_yNew);
				    //System.out.println("distance: " + (distance) + "   newdistance: " + (distance_New_xy) + "   LambdaPoint: " +  LambdaPoint);
				   // System.out.println("Ree: " + (zNew) + "   RadiusGroundArea: " + (radiusGroundArea) + "   Ree/RadiusRGAt: " +  (zNew/radiusGroundArea));
				    
				    
				   // System.out.println("xOldTrafo: " + distance_X + "   YOldTrafo: " + distance_Y + "   ZOldTrafo: " + distance_Z);
				   // System.out.println("xNewTrafo: " + distance_xNew + "   YNewTrafo: " + distance_yNew + "   ZNewTrafo: " + distance_zNew);
				    
				    if((distance_zNew) > 2.0 || (distance_zNew) < -2.0)
				    	System.exit(2);
				    
				    //calculate crossing point and shift center of calculation 0.5 znew down
				   // distance_zNew = (LambdaPoint-0.5)*zNew;
				    
				    //distance_zNew = (LambdaPoint)*zNew;
				    
				    //calculate crossing point and shift center of calculation 0.5 znew down
				    distance_zNew = (LambdaPoint-0.5)*zNew;
				    
				    
				   /* if(LambdaPoint > 0.5)
				    	lambdacounterPZ++;
				    else if (LambdaPoint < 0.5)
				    	lambdacounterMZ++;
				    else lambdacounterPMZ++;
				    */
					
					
					//if(distance_New_xy < 90.0)
					//	HG_Distance_Counterions.AddValue(distance_New_xy);
					
					
					if((distance_New_xy) < maxHistoCounterionsXY )
						if((distance_zNew) < maxHistoCounterionsZ && (distance_zNew) > minHistoCounterionsZ)
							HG2D_DensityDendrimersTotal[Rc2cBinIndex].AddValue(distance_New_xy,distance_zNew);
					
					if((distance_New_xy) < maxHistoCounterionsXY )
						if((distance_zNew) < maxHistoCounterionsZ && (distance_zNew) > minHistoCounterionsZ)
							HG2D_DensityDendrimerOne[Rc2cBinIndex].AddValue(distance_New_xy,distance_zNew);
					
					/*if((distance_xNew/radiusGroundArea) < 4.0 && (distance_xNew/radiusGroundArea) > -4.0 )
						if((distance_yNew/radiusGroundArea) < 4.0 && (distance_yNew/radiusGroundArea) > -4.0)
							if((distance_zNew/zNew) < 2.0 && (distance_zNew/zNew) > -2.0)
							HG3D_Counterions.AddValue(distance_xNew/radiusGroundArea, distance_yNew/radiusGroundArea, distance_zNew/zNew);
				
					if(distance_New_xy < 12.0)
						HG_Distance_Counterions.AddValue(distance_New_xy/radiusGroundArea);
					
					
					if((distance_New_xy/radiusGroundArea) < 4.0 && (distance_New_xy/radiusGroundArea) > -4.0 )
						if((distance_zNew/zNew) < 2.0 && (distance_zNew/zNew) > -2.0)
							HG2D_Counterions.AddValue(distance_New_xy/radiusGroundArea,distance_zNew/zNew);
					*/
				}
			    
				
				//distribution of counter-ions
				
				int offset = counterCOM_A;
				
				for(int monoDendrimerTwo = 1; monoDendrimerTwo <= counterCOM_B; monoDendrimerTwo++)
				{
					//return ( ((x1-x2) < LATTICE_HALF_NEGATIV) ? (x1-x2+LATTICE) : ( ((x1-x2) > LATTICE_HALF) ? (x1-x2-LATTICE) : (x1-x2) ) );
					
					//find the minimum 'image'
					
					int co_X = importData.PolymerKoordinaten[monoDendrimerTwo+offset][0];
					int co_Y = importData.PolymerKoordinaten[monoDendrimerTwo+offset][1];
					int co_Z = importData.PolymerKoordinaten[monoDendrimerTwo+offset][2];
					
					// relative distance between Origin HEP and Counterion
					double relDistance_X =0.0;
					double relDistance_Y =0.0;
					double relDistance_Z =0.0;
					
					relDistance_X = co_X-reeOrigin_x_original;//((co_X-reeOrigin_x_original) < -0.5*Lattice_x) ? (co_X-reeOrigin_x_original+Lattice_x) : ( ((co_X-reeOrigin_x_original) > 0.5*Lattice_x) ? (co_X-reeOrigin_x_original-Lattice_x) : (co_X-reeOrigin_x_original) ) ;
					relDistance_Y = co_Y-reeOrigin_y_original;//((co_Y-reeOrigin_y_original) < -0.5*Lattice_y) ? (co_Y-reeOrigin_y_original+Lattice_y) : ( ((co_Y-reeOrigin_y_original) > 0.5*Lattice_y) ? (co_Y-reeOrigin_y_original-Lattice_y) : (co_Y-reeOrigin_y_original) ) ;
					relDistance_Z = co_Z-reeOrigin_z_original;//((co_Z-reeOrigin_z_original) < -0.5*Lattice_z) ? (co_Z-reeOrigin_z_original+Lattice_z) : ( ((co_Z-reeOrigin_z_original) > 0.5*Lattice_z) ? (co_Z-reeOrigin_z_original-Lattice_z) : (co_Z-reeOrigin_z_original) ) ;
					
					
					//System.out.println("reeOrigin_x: " + reeOrigin_x_original + "   reeOrigin_y: " + reeOrigin_y_original + "   reeOrigin_z: " + reeOrigin_z_original);
					//System.out.println("co_X: " + co_X + "   co_Y: " + co_Y + "   co_Z: " + co_Z);
					//System.out.println("relDistance_X: " + relDistance_X + "   relDistance_Y: " + relDistance_Y + "   relDistance_Z: " + relDistance_Z);
					
					//Calculate the crossing-point
					//LambdaPoint=0 origin of HEP-rod
					//LambdaPoint=1 end of HEP-rod
					double LambdaPoint = (relDistance_X*ree_x_original+relDistance_Y*ree_y_original+relDistance_Z*ree_z_original)/(ree_original*ree_original);
					
					double distance_X = relDistance_X-LambdaPoint*ree_x_original;
					double distance_Y = relDistance_Y-LambdaPoint*ree_y_original;
					double distance_Z = relDistance_Z-LambdaPoint*ree_z_original;
					
					
					double distance = Math.sqrt(distance_X*distance_X + distance_Y*distance_Y + distance_Z*distance_Z);
					
					//Trafo-Matrix
					double xOldTrafo=distance_X;//ree_x;//cylinderUnitAxis_x;
				    double yOldTrafo=distance_Y;//ree_y;//cylinderUnitAxis_y;
				    double zOldTrafo=distance_Z;//ree_z;//cylinderUnitAxis_z;
				    
				    double distance_xNew = (CosRot+OneMinusCosRot*eRot_x_norm*eRot_x_norm)*xOldTrafo + (eRot_x_norm*eRot_y_norm)*OneMinusCosRot*yOldTrafo        + eRot_y_norm*SinRot*zOldTrafo;
				    double distance_yNew = (OneMinusCosRot*eRot_x_norm*eRot_y_norm)*xOldTrafo        + (CosRot+OneMinusCosRot*eRot_y_norm*eRot_y_norm)*yOldTrafo - eRot_x_norm*SinRot*zOldTrafo;
				    double distance_zNew = (-eRot_y_norm*SinRot)*xOldTrafo  						    + (eRot_x_norm*SinRot)*yOldTrafo						    + CosRot*zOldTrafo;
				    
				    //after Trafo distance_zNew = 0 by definition
				    
				    
				    double distance_New = Math.sqrt(distance_xNew*distance_xNew + distance_yNew*distance_yNew + distance_zNew*distance_zNew);
				    double distance_New_xy = Math.sqrt(distance_xNew*distance_xNew + distance_yNew*distance_yNew);
				    //System.out.println("distance: " + (distance) + "   newdistance: " + (distance_New_xy) + "   LambdaPoint: " +  LambdaPoint);
				   // System.out.println("Ree: " + (zNew) + "   RadiusGroundArea: " + (radiusGroundArea) + "   Ree/RadiusRGAt: " +  (zNew/radiusGroundArea));
				    
				    
				   // System.out.println("xOldTrafo: " + distance_X + "   YOldTrafo: " + distance_Y + "   ZOldTrafo: " + distance_Z);
				   // System.out.println("xNewTrafo: " + distance_xNew + "   YNewTrafo: " + distance_yNew + "   ZNewTrafo: " + distance_zNew);
				    
				    if((distance_zNew) > 2.0 || (distance_zNew) < -2.0)
				    	System.exit(2);
				    
				    //calculate crossing point and shift center of calculation 0.5 znew down
				    distance_zNew = (LambdaPoint-0.5)*zNew;
				    
				    if(LambdaPoint > 0.5)
				    	lambdacounterPZ++;
				    else if (LambdaPoint < 0.5)
				    	lambdacounterMZ++;
				    else lambdacounterPMZ++;
					
					if((distance_New_xy) < maxHistoCounterionsXY )
						if((distance_zNew) < maxHistoCounterionsZ && (distance_zNew) > minHistoCounterionsZ)
							HG2D_DensityDendrimersTotal[Rc2cBinIndex].AddValue(distance_New_xy,distance_zNew);
					
					if((distance_New_xy) < maxHistoCounterionsXY )
						if((distance_zNew) < maxHistoCounterionsZ && (distance_zNew) > minHistoCounterionsZ)
							HG2D_DensityDendrimerTwo[Rc2cBinIndex].AddValue(distance_New_xy,distance_zNew);
					
					
				}
				
				} //end calculation only for bins
			    
				//punktwolke.setzeZeile((xNew/Ree)+" "+(yNew/Ree)+" "+(zNew/Ree));
				}
				//punktwolke.setzeZeile(stupidcounter+" "+(ree_x/ree)+" "+(ree_y/ree)+" "+(ree_z/ree));
				//rcmX[(int)(importData.MCSTime/50000)]= rcm_x;
				//rcmY[(int)(importData.MCSTime/50000)]= rcm_y;
				//rcmZ[(int)(importData.MCSTime/50000)]= rcm_z;
				
				//MessungStat[(int)((importData.MCSTime-time_t0)/50000)].AddValue(((rcm_x-rcm_x_t0)*(rcm_x-rcm_x_t0) + (rcm_y-rcm_y_t0)*(rcm_y-rcm_y_t0) + (rcm_z-rcm_z_t0)*(rcm_z-rcm_z_t0)));
				//GesamtStat_x[frame-1].AddValue(Rg2_x);
				//GesamtStat_y[frame-1].AddValue(Rg2_y);
				//GesamtStat_z[frame-1].AddValue(Rg2_z);
				
					
	}
	
	/*public double GetRotationAroundAxis_XComponent(double CosRot, double OneMinusCosRot, double SinRot, double eRot_x_norm, double eRot_y_norm, double XBeforeTrafo, double YBeforeTrafo, double ZBeforeTrafo)
	{
		return (CosRot+OneMinusCosRot*eRot_x_norm*eRot_x_norm)*XBeforeTrafo + (eRot_x_norm*eRot_y_norm)*OneMinusCosRot*YBeforeTrafo        + eRot_y_norm*SinRot*ZBeforeTrafo;
	    
	}
	
	public double GetRotationAroundAxis_YComponent(double CosRot, double OneMinusCosRot, double SinRot, double eRot_x_norm, double eRot_y_norm, double XBeforeTrafo, double YBeforeTrafo, double ZBeforeTrafo)
	{
		return (OneMinusCosRot*eRot_x_norm*eRot_y_norm)*XBeforeTrafo        + (CosRot+OneMinusCosRot*eRot_y_norm*eRot_y_norm)*YBeforeTrafo - eRot_x_norm*SinRot*ZBeforeTrafo;
	}
	
	public double GetRotationAroundAxis_ZComponent(double CosRot, double OneMinusCosRot, double SinRot, double eRot_x_norm, double eRot_y_norm, double XBeforeTrafo, double YBeforeTrafo, double ZBeforeTrafo)
	{
		return (-eRot_y_norm*SinRot)*XBeforeTrafo  						    + (eRot_x_norm*SinRot)*YBeforeTrafo						    + CosRot*ZBeforeTrafo;
	}
	*/
	

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

	public double calculateMinDistance(double x1, double x2, double Lattice)
	{
		return ( ((x1-x2) < (-0.5*Lattice)) ? (x1-x2 + Lattice) : ( ((x1-x2) > 0.5*Lattice) ? (x1-x2-Lattice) : (x1-x2) ) );
		//return ( ((x1-x2) < LATTICE_HALF_NEGATIV) ? ((x1-x2) & LATTICE_XM1) : ( ((x1-x2) > LATTICE_HALF) ? ((x2-x1)& LATTICE_XM1) : abs(x1-x2) ) );
	}

	public int GetBinOfValue(double value)
	{
		return (int) Math.floor((value-MinimumOfBinningRange)/IntervallOfBinningRange);
	}
	
	public double GetRangeInBin(int bin)
	{
		return (MinimumOfBinningRange+(bin)*IntervallOfBinningRange + IntervallOfBinningRange/2.0);
		
	}
	
}
