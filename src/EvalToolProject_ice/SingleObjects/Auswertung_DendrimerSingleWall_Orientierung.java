package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;


import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Histogramm;
import EvalToolProject_ice.tools.Histogramm2D;
import EvalToolProject_ice.tools.Histogramm3D;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;


public class Auswertung_DendrimerSingleWall_Orientierung {


	
	
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

	/*Statistik[] GesamtStat_x;
	Statistik[] GesamtStat_y;
	Statistik[] GesamtStat_z;*/
	
	int NrOfHeparin = 0;
	int NrOfStars = 0;
	int NrOfMonomersPerStarArm = 0;
	
	int NrOfCounterions = 0;
	
	//BFMFileSaver rg;
	BFMFileSaver orient;
	BFMFileSaver punktwolke;
	BFMFileSaver histo3D;
	BFMFileSaver histo_theta;
	BFMFileSaver histo_phi;
	BFMFileSaver histo_ree;
	
	BFMFileSaver histo_ReeX;
	BFMFileSaver histo_ReeY;
	BFMFileSaver histo_ReeZ;
	
	BFMFileSaver histo3D_Counterions;
	BFMFileSaver histo_Distance_Counterions;
	
	BFMFileSaver histo2D_Counterions;
	BFMFileSaver histo2D_CounterionsNormiert;
	BFMFileSaver histo2D_CounterionsGnuplot;
	
	BFMFileSaver histo2D_Rod;
	BFMFileSaver histo2D_RodNormiert;
	BFMFileSaver histo2D_RodGnuplot;
	
	BFMFileSaver histo2D_RodHalf;
	BFMFileSaver histo2D_RodHalfNormiert;
	BFMFileSaver histo2D_RodHalfGnuplot;
	
	BFMFileSaver histo2D_CounterionsHalf;
	BFMFileSaver histo2D_CounterionsHalfNormiert;
	BFMFileSaver histo2D_CounterionsHalfGnuplot;
	
	BFMFileSaver histo2D_Solvent;
	BFMFileSaver histo2D_SolventNormiert;
	BFMFileSaver histo2D_SolventGnuplot;
	
	BFMFileSaver histo2D_SolventHalf;
	BFMFileSaver histo2D_SolventHalfNormiert;
	BFMFileSaver histo2D_SolventHalfGnuplot;
	BFMFileSaver histo3D_Solvent;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	double rcm_x_t0=0;
	double rcm_y_t0=0;
	double rcm_z_t0=0;
	
	int time_t0=0;
	
	//Statistik[] MessungStat = new Statistik[34001];
	
	//double rcmX[] = new double[34001];
	//double rcmY[] = new double[34001];
	//double rcmZ[] = new double[34001];
	int stupidcounter;
	
	Histogramm3D HG3D_Rod;
	Histogramm2D HG2D_Rod;
	Histogramm2D HG2D_RodHalf;
	
	Histogramm3D HG3D_Counterions;
	Histogramm2D HG2D_Counterions;
	Histogramm HG_Distance_Counterions;
	
	Histogramm2D HG2D_CounterionsHalf;
	
	Histogramm2D HG2D_Solvent;
	Histogramm2D HG2D_SolventHalf;
	Histogramm3D HG3D_Solvent;
	
	Histogramm HG_theta, HG_phi, HG_ree;
	Histogramm HG_ReeX, HG_ReeY, HG_ReeZ;
	
	Statistik ReeRodStatistik;
	
	long lambdacounterPZ;
	long lambdacounterMZ;
	long lambdacounterPMZ;
	
	double minHistoCounterionsXY;
	double maxHistoCounterionsXY;
	double minHistoCounterionsZ;
	double maxHistoCounterionsZ;
	
	public Auswertung_DendrimerSingleWall_Orientierung(String srcdir, String fname, String dstDir)
	{
		//FileName = "1024_1024_0.00391_32";
		//FileName = "HeparinNetz_N_84_MSD";
		//FileName = "HeparinNetzHarder_N_90_MSD";
		//FileName = "HeparinQuad_N_80_MSD";
		FileName =  fname;
		//FileDirectory = "/home/users/dockhorn/workspace/Evaluation_Tools/";
		FileDirectorySrc = srcdir;
		FileDirectoryDst = dstDir;
		
		
		minHistoCounterionsXY=-64.01;
		maxHistoCounterionsXY=64.01;
		int binHistoCounterionsXY = 128;
		
		minHistoCounterionsZ=-128.01;//40.2;
		maxHistoCounterionsZ=128.01;//40.2;
		int binHistoCounterionsZ = 256;//25;
		
		
		//HG3D_Rod = new Histogramm3D(-1.1,1.1,50,-1.1,1.1,50,-1.1,1.1,50);
		HG3D_Rod = new Histogramm3D(minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		HG2D_Rod= new Histogramm2D(0.0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		
		HG3D_Counterions = new Histogramm3D(minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		HG2D_Counterions = new Histogramm2D(0.0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		
		HG2D_RodHalf= new Histogramm2D(minHistoCounterionsXY,maxHistoCounterionsXY,2*binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		
		HG2D_CounterionsHalf= new Histogramm2D(minHistoCounterionsXY,maxHistoCounterionsXY,2*binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		
		HG2D_Solvent= new Histogramm2D(0.0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		HG2D_SolventHalf= new Histogramm2D(minHistoCounterionsXY,maxHistoCounterionsXY,2*binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		HG3D_Solvent = new Histogramm3D(minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		
		
		HG_theta = new Histogramm(-0.001,Math.PI+.001,100);
		HG_phi = new Histogramm(-0.001,2.0*Math.PI+0.001,100);
		HG_ree = new Histogramm(0,90,100);
		
		HG_ReeX = new Histogramm(-1.05,1.05,30);
		HG_ReeY = new Histogramm(-1.05,1.05,30);
		HG_ReeZ = new Histogramm(-1.05,1.05,30);
		
		HG_Distance_Counterions = new Histogramm(0,90,100);
		
		Polymersystem = new int[1];
		skipFrames = 0;
		currentFrame = 1;
		//System.out.println("cf="+currentFrame);
		
		//rg = new BFMFileSaver();
		orient = new BFMFileSaver();
		punktwolke= new BFMFileSaver();
		histo3D = new BFMFileSaver();
		histo_theta= new BFMFileSaver();
		histo_phi= new BFMFileSaver();
		histo_ree= new BFMFileSaver();
		
		histo_ReeX= new BFMFileSaver();
		histo_ReeY= new BFMFileSaver();
		histo_ReeZ= new BFMFileSaver();
		
		histo3D_Counterions = new BFMFileSaver();
		histo_Distance_Counterions= new BFMFileSaver();
		
		histo2D_Counterions = new BFMFileSaver();
		histo2D_CounterionsNormiert = new BFMFileSaver();
		histo2D_CounterionsGnuplot = new BFMFileSaver();
		
		histo2D_Rod = new BFMFileSaver();
		histo2D_RodNormiert = new BFMFileSaver();
		histo2D_RodGnuplot = new BFMFileSaver();
		
		histo2D_RodHalf = new BFMFileSaver();
		histo2D_RodHalfNormiert = new BFMFileSaver();
		histo2D_RodHalfGnuplot = new BFMFileSaver();
		
		histo2D_CounterionsHalf = new BFMFileSaver();
		histo2D_CounterionsHalfNormiert = new BFMFileSaver();
		histo2D_CounterionsHalfGnuplot = new BFMFileSaver();
		
		histo2D_Solvent = new BFMFileSaver();
		histo2D_SolventNormiert = new BFMFileSaver();
		histo2D_SolventGnuplot = new BFMFileSaver();
		
		histo2D_SolventHalf = new BFMFileSaver();
		histo2D_SolventHalfNormiert = new BFMFileSaver();
		histo2D_SolventHalfGnuplot = new BFMFileSaver();
		
		histo3D_Solvent = new BFMFileSaver();
		
		
		lambdacounterPZ = 0;
		lambdacounterMZ = 0;
		lambdacounterPMZ = 0;
		
		ReeRodStatistik = new Statistik() ;
		
		//rg.DateiAnlegen("Linear25FreeMSD_MSD_10E6MCS.dat", false);
		orient.DateiAnlegen(FileDirectoryDst+FileName+"_Orient.dat", false);
		punktwolke.DateiAnlegen(FileDirectoryDst+FileName+"_Punktwolke.dat", false);
		histo3D.DateiAnlegen(FileDirectoryDst+FileName+"_Histo3D.vtk", false);
		histo_theta.DateiAnlegen(FileDirectoryDst+FileName+"_Histo_theta.dat", false);
		histo_phi.DateiAnlegen(FileDirectoryDst+FileName+"_Histo_phi.dat", false);
		histo_ree.DateiAnlegen(FileDirectoryDst+FileName+"_Histo_ree.dat", false);
		
		histo_ReeX.DateiAnlegen(FileDirectoryDst+FileName+"_Histo_ReeX.dat", false);
		histo_ReeY.DateiAnlegen(FileDirectoryDst+FileName+"_Histo_ReeY.dat", false);
		histo_ReeZ.DateiAnlegen(FileDirectoryDst+FileName+"_Histo_ReeZ.dat", false);
		
		histo3D_Counterions.DateiAnlegen(FileDirectoryDst+FileName+"_Histo3D_Counterions.vtk", false);
		histo_Distance_Counterions.DateiAnlegen(FileDirectoryDst+FileName+"_Histo_Distance_Counterions.dat", false);
		
		
		histo2D_Counterions.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_Counterions.dat", false);
		histo2D_CounterionsNormiert.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_CounterionsNormiert.dat", false);
		histo2D_CounterionsGnuplot.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_CounterionsGnuplot.gnu", false);
		
		histo2D_CounterionsGnuplot.setzeZeile("set term postscript enhanced color");
		histo2D_CounterionsGnuplot.setzeZeile("set output \'"+FileDirectoryDst+FileName+"_Histo2D_CounterionsNormiert.ps"+"\'");
		histo2D_CounterionsGnuplot.setzeZeile("set view map");
		histo2D_CounterionsGnuplot.setzeZeile("unset surface");
		histo2D_CounterionsGnuplot.setzeZeile("set cbrange [0:1]");
		histo2D_CounterionsGnuplot.setzeZeile("set style data pm3d");
		histo2D_CounterionsGnuplot.setzeZeile("set style function pm3d");
		histo2D_CounterionsGnuplot.setzeZeile("set xlabel \"{/Symbol r}\" ");
		histo2D_CounterionsGnuplot.setzeZeile("set ylabel \"z\" ");
		histo2D_CounterionsGnuplot.setzeZeile("set pm3d implicit at b");
		histo2D_CounterionsGnuplot.setzeZeile("set title \"density distribution of other NT around NT (normalized)\" ");
		histo2D_CounterionsGnuplot.setzeZeile("unset key");
		histo2D_CounterionsGnuplot.setzeZeile("set size square");
		histo2D_CounterionsGnuplot.setzeZeile("#set palette defined (0 \"white\", 0.001 \"red\", 0.002 \"blue\")");
		histo2D_CounterionsGnuplot.setzeZeile("#set palette model CMY rgbformulae 7,5,15");
		histo2D_CounterionsGnuplot.setzeZeile("#set palette rgb 30,31,32; set palette negative");
		histo2D_CounterionsGnuplot.setzeZeile("#set palette model RGB");
		histo2D_CounterionsGnuplot.setzeZeile("#set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000')");
		histo2D_CounterionsGnuplot.setzeZeile("#set palette defined ( 0 '#000090', 1 '#000fff', 2 '#0090ff', 3 '#0fffee', 4 '#90ff70', 5 '#ffee00', 6 '#ff7000', 7 '#ee0000', 8 '#7f0000')");
		histo2D_CounterionsGnuplot.setzeZeile("set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000'); set palette negative");
		histo2D_CounterionsGnuplot.setzeZeile("#set palette rgbformulae 22,13,-31");
		histo2D_CounterionsGnuplot.setzeZeile("#splot \'"+FileDirectoryDst+FileName+"_Histo2D_Counterions.dat"+"\' matrix using (($1/"+binHistoCounterionsXY+")*"+maxHistoCounterionsXY+"):("+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_CounterionsGnuplot.setzeZeile("splot \'"+FileDirectoryDst+FileName+"_Histo2D_CounterionsNormiert.dat"+"\' matrix using (($1/"+binHistoCounterionsXY+")*"+maxHistoCounterionsXY+"):("+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_CounterionsGnuplot.setzeLeerzeile();
		histo2D_CounterionsGnuplot.DateiSchliessen();

		
		histo2D_Rod.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_Dendrimer.dat", false);
		histo2D_RodNormiert.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_DendrimerNormiert.dat", false);
		histo2D_RodGnuplot.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_DendrimerGnuplot.gnu", false);
		
		histo2D_RodGnuplot.setzeZeile("set term postscript enhanced color");
		histo2D_RodGnuplot.setzeZeile("set output \'"+FileDirectoryDst+FileName+"_Histo2D_Dendrimer.ps"+"\'");
		histo2D_RodGnuplot.setzeZeile("set view map");
		histo2D_RodGnuplot.setzeZeile("unset surface");
		histo2D_RodGnuplot.setzeZeile("#set cbrange [0:1]");
		histo2D_RodGnuplot.setzeZeile("set style data pm3d");
		histo2D_RodGnuplot.setzeZeile("set style function pm3d");
		histo2D_RodGnuplot.setzeZeile("set xlabel \"{/Symbol r}\" ");
		histo2D_RodGnuplot.setzeZeile("set ylabel \"z\" ");
		histo2D_RodGnuplot.setzeZeile("set pm3d implicit at b");
		histo2D_RodGnuplot.setzeZeile("set title \"density distribution of Dendrimer\" ");
		histo2D_RodGnuplot.setzeZeile("unset key");
		histo2D_RodGnuplot.setzeZeile("#set xrange [0:50]");
		histo2D_RodGnuplot.setzeZeile("#set yrange [0:50]");
		histo2D_RodGnuplot.setzeZeile("set size square");
		histo2D_RodGnuplot.setzeZeile("#set palette defined (0 \"white\", 0.001 \"red\", 0.002 \"blue\")");
		histo2D_RodGnuplot.setzeZeile("#set palette model CMY rgbformulae 7,5,15");
		histo2D_RodGnuplot.setzeZeile("#set palette rgb 30,31,32; set palette negative");
		histo2D_RodGnuplot.setzeZeile("#set palette model RGB");
		histo2D_RodGnuplot.setzeZeile("#set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000')");
		histo2D_RodGnuplot.setzeZeile("set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000'); set palette negative");
		histo2D_RodGnuplot.setzeZeile("#set palette defined ( 0 '#000090', 1 '#000fff', 2 '#0090ff', 3 '#0fffee', 4 '#90ff70', 5 '#ffee00', 6 '#ff7000', 7 '#ee0000', 8 '#7f0000')");
		histo2D_RodGnuplot.setzeZeile("#set palette rgbformulae 22,13,-31");
		histo2D_RodGnuplot.setzeZeile("splot \'"+FileDirectoryDst+FileName+"_Histo2D_Dendrimer.dat"+"\' matrix using (($1/"+binHistoCounterionsXY+")*"+maxHistoCounterionsXY+"):(0.5*"+HG2D_Rod.GetIntervallThicknessOfBins2()+"+"+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_RodGnuplot.setzeZeile("#splot \'"+FileDirectoryDst+FileName+"_Histo2D_DendrimerNormiert.dat"+"\' matrix using (($1/"+binHistoCounterionsXY+")*"+maxHistoCounterionsXY+"):(0.5*"+HG2D_Rod.GetIntervallThicknessOfBins2()+"+"+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_RodGnuplot.setzeLeerzeile();
		histo2D_RodGnuplot.DateiSchliessen();

		
		histo2D_RodHalf.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_RodHalf.dat", false);
		histo2D_RodHalfNormiert.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_RodHalfNormiert.dat", false);
		histo2D_RodHalfGnuplot.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_RodHalfGnuplot.gnu", false);
		
		histo2D_RodHalfGnuplot.setzeZeile("set term postscript enhanced color");
		histo2D_RodHalfGnuplot.setzeZeile("set output \'"+FileDirectoryDst+FileName+"_Histo2D_RodHalfNormiert.ps"+"\'");
		histo2D_RodHalfGnuplot.setzeZeile("set view map");
		histo2D_RodHalfGnuplot.setzeZeile("unset surface");
		histo2D_RodHalfGnuplot.setzeZeile("set cbrange [0:1]");
		histo2D_RodHalfGnuplot.setzeZeile("set style data pm3d");
		histo2D_RodHalfGnuplot.setzeZeile("set style function pm3d");
		histo2D_RodHalfGnuplot.setzeZeile("set xlabel \"{/Symbol r}\" ");
		histo2D_RodHalfGnuplot.setzeZeile("set ylabel \"z\" ");
		histo2D_RodHalfGnuplot.setzeZeile("set pm3d implicit at b");
		histo2D_RodHalfGnuplot.setzeZeile("set title \"density distribution of original NT (halfspace\\\\&normalized)\" ");
		histo2D_RodHalfGnuplot.setzeZeile("unset key");
		histo2D_RodHalfGnuplot.setzeZeile("set size square");
		histo2D_RodHalfGnuplot.setzeZeile("#set palette defined (0 \"white\", 0.001 \"red\", 0.002 \"blue\")");
		histo2D_RodHalfGnuplot.setzeZeile("#set palette model CMY rgbformulae 7,5,15");
		histo2D_RodHalfGnuplot.setzeZeile("#set palette rgb 30,31,32; set palette negative");
		histo2D_RodHalfGnuplot.setzeZeile("#set palette model RGB");
		histo2D_RodHalfGnuplot.setzeZeile("#set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000')");
		histo2D_RodHalfGnuplot.setzeZeile("set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000'); set palette negative");
		histo2D_RodHalfGnuplot.setzeZeile("#set palette defined ( 0 '#000090', 1 '#000fff', 2 '#0090ff', 3 '#0fffee', 4 '#90ff70', 5 '#ffee00', 6 '#ff7000', 7 '#ee0000', 8 '#7f0000')");
		histo2D_RodHalfGnuplot.setzeZeile("#set palette rgbformulae 22,13,-31");
		histo2D_RodHalfGnuplot.setzeZeile("#splot \'"+FileDirectoryDst+FileName+"_Histo2D_RodHalf.dat"+"\' matrix using ("+minHistoCounterionsXY+"+($1/"+2*binHistoCounterionsXY+")*"+(maxHistoCounterionsXY-minHistoCounterionsXY)+"):("+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_RodHalfGnuplot.setzeZeile("splot \'"+FileDirectoryDst+FileName+"_Histo2D_RodHalfNormiert.dat"+"\' matrix using ("+minHistoCounterionsXY+"+($1/"+2*binHistoCounterionsXY+")*"+(maxHistoCounterionsXY-minHistoCounterionsXY)+"):("+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_RodHalfGnuplot.setzeLeerzeile();
		histo2D_RodHalfGnuplot.DateiSchliessen();

		histo2D_CounterionsHalf.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_CounterionsHalf.dat", false);
		histo2D_CounterionsHalfNormiert.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_CounterionsHalfNormiert.dat", false);
		histo2D_CounterionsHalfGnuplot.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_CounterionsHalfGnuplot.gnu", false);

		histo2D_CounterionsHalfGnuplot.setzeZeile("set term postscript enhanced color");
		histo2D_CounterionsHalfGnuplot.setzeZeile("set output \'"+FileDirectoryDst+FileName+"_Histo2D_CounterionsHalfNormiert.ps"+"\'");
		histo2D_CounterionsHalfGnuplot.setzeZeile("set view map");
		histo2D_CounterionsHalfGnuplot.setzeZeile("unset surface");
		histo2D_CounterionsHalfGnuplot.setzeZeile("set cbrange [0:1]");
		histo2D_CounterionsHalfGnuplot.setzeZeile("set style data pm3d");
		histo2D_CounterionsHalfGnuplot.setzeZeile("set style function pm3d");
		histo2D_CounterionsHalfGnuplot.setzeZeile("set xlabel \"{/Symbol r}\" ");
		histo2D_CounterionsHalfGnuplot.setzeZeile("set ylabel \"z\" ");
		histo2D_CounterionsHalfGnuplot.setzeZeile("set pm3d implicit at b");
		histo2D_CounterionsHalfGnuplot.setzeZeile("set title \"density distribution of other NT around NT (halfspace\\\\&normalized)\" ");
		histo2D_CounterionsHalfGnuplot.setzeZeile("unset key");
		histo2D_CounterionsHalfGnuplot.setzeZeile("set size square");
		histo2D_CounterionsHalfGnuplot.setzeZeile("#set palette defined (0 \"white\", 0.001 \"red\", 0.002 \"blue\")");
		histo2D_CounterionsHalfGnuplot.setzeZeile("#set palette model CMY rgbformulae 7,5,15");
		histo2D_CounterionsHalfGnuplot.setzeZeile("#set palette rgb 30,31,32; set palette negative");
		histo2D_CounterionsHalfGnuplot.setzeZeile("#set palette model RGB");
		histo2D_CounterionsHalfGnuplot.setzeZeile("#set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000')");
		histo2D_CounterionsHalfGnuplot.setzeZeile("set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000'); set palette negative");
		histo2D_CounterionsHalfGnuplot.setzeZeile("#set palette defined ( 0 '#000090', 1 '#000fff', 2 '#0090ff', 3 '#0fffee', 4 '#90ff70', 5 '#ffee00', 6 '#ff7000', 7 '#ee0000', 8 '#7f0000')");
		histo2D_CounterionsHalfGnuplot.setzeZeile("#set palette rgbformulae 22,13,-31");
		histo2D_CounterionsHalfGnuplot.setzeZeile("#splot \'"+FileDirectoryDst+FileName+"_Histo2D_CounterionsHalf.dat"+"\' matrix using ("+minHistoCounterionsXY+"+($1/"+2*binHistoCounterionsXY+")*"+(maxHistoCounterionsXY-minHistoCounterionsXY)+"):("+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_CounterionsHalfGnuplot.setzeZeile("splot \'"+FileDirectoryDst+FileName+"_Histo2D_CounterionsHalfNormiert.dat"+"\' matrix using ("+minHistoCounterionsXY+"+($1/"+2*binHistoCounterionsXY+")*"+(maxHistoCounterionsXY-minHistoCounterionsXY)+"):("+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_CounterionsHalfGnuplot.setzeLeerzeile();
		histo2D_CounterionsHalfGnuplot.DateiSchliessen();

		histo3D_Solvent.DateiAnlegen(FileDirectoryDst+FileName+"_Histo3D_Solvent.vtk", false);
		
		histo2D_Solvent.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_Solvent.dat", false);
		histo2D_SolventNormiert.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_SolventNormiert.dat", false);
		histo2D_SolventGnuplot.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_SolventGnuplot.gnu", false);
		
		histo2D_SolventGnuplot.setzeZeile("set term postscript enhanced color");
		histo2D_SolventGnuplot.setzeZeile("set output \'"+FileDirectoryDst+FileName+"_Histo2D_SolventNormiert.ps"+"\'");
		histo2D_SolventGnuplot.setzeZeile("set view map");
		histo2D_SolventGnuplot.setzeZeile("unset surface");
		histo2D_SolventGnuplot.setzeZeile("set cbrange [0:1]");
		histo2D_SolventGnuplot.setzeZeile("set style data pm3d");
		histo2D_SolventGnuplot.setzeZeile("set style function pm3d");
		histo2D_SolventGnuplot.setzeZeile("set xlabel \"{/Symbol r}\" ");
		histo2D_SolventGnuplot.setzeZeile("set ylabel \"z\" ");
		histo2D_SolventGnuplot.setzeZeile("set pm3d implicit at b");
		histo2D_SolventGnuplot.setzeZeile("set title \"density distribution of solvent (normalized)\" ");
		histo2D_SolventGnuplot.setzeZeile("unset key");
		histo2D_SolventGnuplot.setzeZeile("set size square");
		histo2D_SolventGnuplot.setzeZeile("#set palette defined (0 \"white\", 0.001 \"red\", 0.002 \"blue\")");
		histo2D_SolventGnuplot.setzeZeile("#set palette model CMY rgbformulae 7,5,15");
		histo2D_SolventGnuplot.setzeZeile("#set palette rgb 30,31,32; set palette negative");
		histo2D_SolventGnuplot.setzeZeile("#set palette model RGB");
		histo2D_SolventGnuplot.setzeZeile("#set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000')");
		histo2D_SolventGnuplot.setzeZeile("set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000'); set palette negative");
		histo2D_SolventGnuplot.setzeZeile("#set palette defined ( 0 '#000090', 1 '#000fff', 2 '#0090ff', 3 '#0fffee', 4 '#90ff70', 5 '#ffee00', 6 '#ff7000', 7 '#ee0000', 8 '#7f0000')");
		histo2D_SolventGnuplot.setzeZeile("#set palette rgbformulae 22,13,-31");
		histo2D_SolventGnuplot.setzeZeile("#splot \'"+FileDirectoryDst+FileName+"_Histo2D_Solvent.dat"+"\' matrix using (($1/"+binHistoCounterionsXY+")*"+maxHistoCounterionsXY+"):("+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_SolventGnuplot.setzeZeile("splot \'"+FileDirectoryDst+FileName+"_Histo2D_SolventNormiert.dat"+"\' matrix using (($1/"+binHistoCounterionsXY+")*"+maxHistoCounterionsXY+"):("+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_SolventGnuplot.setzeLeerzeile();
		histo2D_SolventGnuplot.DateiSchliessen();
	
		histo2D_SolventHalf.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_SolventHalf.dat", false);
		histo2D_SolventHalfNormiert.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_SolventHalfNormiert.dat", false);
		histo2D_SolventHalfGnuplot.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_SolventHalfGnuplot.gnu", false);

		histo2D_SolventHalfGnuplot.setzeZeile("set term postscript enhanced color");
		histo2D_SolventHalfGnuplot.setzeZeile("set output \'"+FileDirectoryDst+FileName+"_Histo2D_SolventHalfNormiert.ps"+"\'");
		histo2D_SolventHalfGnuplot.setzeZeile("set view map");
		histo2D_SolventHalfGnuplot.setzeZeile("unset surface");
		histo2D_SolventHalfGnuplot.setzeZeile("set cbrange [0:1]");
		histo2D_SolventHalfGnuplot.setzeZeile("set style data pm3d");
		histo2D_SolventHalfGnuplot.setzeZeile("set style function pm3d");
		histo2D_SolventHalfGnuplot.setzeZeile("set xlabel \"{/Symbol r}\" ");
		histo2D_SolventHalfGnuplot.setzeZeile("set ylabel \"z\" ");
		histo2D_SolventHalfGnuplot.setzeZeile("set pm3d implicit at b");
		histo2D_SolventHalfGnuplot.setzeZeile("set title \"density distribution of solvent (halfspace\\\\&normalized)\" ");
		histo2D_SolventHalfGnuplot.setzeZeile("unset key");
		histo2D_SolventHalfGnuplot.setzeZeile("set size square");
		histo2D_SolventHalfGnuplot.setzeZeile("#set palette defined (0 \"white\", 0.001 \"red\", 0.002 \"blue\")");
		histo2D_SolventHalfGnuplot.setzeZeile("#set palette model CMY rgbformulae 7,5,15");
		histo2D_SolventHalfGnuplot.setzeZeile("#set palette rgb 30,31,32; set palette negative");
		histo2D_SolventHalfGnuplot.setzeZeile("#set palette model RGB");
		histo2D_SolventHalfGnuplot.setzeZeile("#set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000')");
		histo2D_SolventHalfGnuplot.setzeZeile("set palette defined ( 0 '#ffffff', 1 '#F9FF00', 2 '#38D728', 3 '#0057A8', 4 '#000000'); set palette negative");
		histo2D_SolventHalfGnuplot.setzeZeile("#set palette defined ( 0 '#000090', 1 '#000fff', 2 '#0090ff', 3 '#0fffee', 4 '#90ff70', 5 '#ffee00', 6 '#ff7000', 7 '#ee0000', 8 '#7f0000')");
		histo2D_SolventHalfGnuplot.setzeZeile("#set palette rgbformulae 22,13,-31");
		histo2D_SolventHalfGnuplot.setzeZeile("#splot \'"+FileDirectoryDst+FileName+"_Histo2D_SolventHalf.dat"+"\' matrix using ("+minHistoCounterionsXY+"+($1/"+2*binHistoCounterionsXY+")*"+(maxHistoCounterionsXY-minHistoCounterionsXY)+"):("+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_SolventHalfGnuplot.setzeZeile("splot \'"+FileDirectoryDst+FileName+"_Histo2D_SolventHalfNormiert.dat"+"\' matrix using ("+minHistoCounterionsXY+"+($1/"+2*binHistoCounterionsXY+")*"+(maxHistoCounterionsXY-minHistoCounterionsXY)+"):("+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_SolventHalfGnuplot.setzeLeerzeile();
		histo2D_SolventHalfGnuplot.DateiSchliessen();
				
		System.out.println("file : " +FileName );
		System.out.println("dirDst : " + FileDirectoryDst);
		
		DecimalFormat dh = new DecimalFormat("00");
		
		//for(int i = 0; i <= 9; i++)
		//	LoadFile("buerste_straight_25_0"+i+".bfm", 1);
		orient.setzeZeile("# r	theta	phi");
		punktwolke.setzeZeile("# ree_x	ree_y	ree_z");
		
		//for(int i = 1; i <= 25; i++)
		//LoadFile(FileName+"__"+dh.format(i)+".bfm", 1);
		LoadFile(FileName+".bfm", 1);
		
		
		histo_theta.setzeZeile("# theta_k     f(theta_k)*sin(theta_k)");
		
		double test = 0.0;
		for(int i = 0; i < HG_theta.GetNrBins(); i++)
		{
			histo_theta.setzeZeile(HG_theta.GetRangeInBin(i)+" "+(HG_theta.GetNrInBinNormiert(i)/(HG_theta.GetIntervallThickness())));
			test += HG_theta.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_theta: "+ test);	
		
		test = 0.0;
		for(int i = 0; i < HG_phi.GetNrBins(); i++)
		{
			histo_phi.setzeZeile(HG_phi.GetRangeInBin(i)+" "+ (HG_phi.GetNrInBinNormiert(i)/HG_phi.GetIntervallThickness()));
			test += HG_phi.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_phi: "+ test);	
		
		test = 0.0;
		for(int i = 0; i < HG_ree.GetNrBins(); i++)
		{
			histo_ree.setzeZeile(HG_ree.GetRangeInBin(i)+" "+(HG_ree.GetNrInBinNormiert(i)/(HG_ree.GetRangeInBin(i)*HG_ree.GetRangeInBin(i)*HG_ree.GetIntervallThickness())));
			test += HG_ree.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_ree: "+ test);	
		
		histo_ReeX.setzeZeile("# x-component of Vec Ree");
		test = 0.0;
		for(int i = 0; i < HG_ReeX.GetNrBins(); i++)
		{
			histo_ReeX.setzeZeile(HG_ReeX.GetRangeInBin(i)+" "+HG_ReeX.GetNrInBinNormiert(i));
			test += HG_ReeX.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_ReeX: "+ test);	
		
		histo_ReeY.setzeZeile("# y-component of Vec Ree");
		test = 0.0;
		for(int i = 0; i < HG_ReeY.GetNrBins(); i++)
		{
			histo_ReeY.setzeZeile(HG_ReeY.GetRangeInBin(i)+" "+HG_ReeY.GetNrInBinNormiert(i));
			test += HG_ReeY.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_ReeY: "+ test);
		
		histo_ReeZ.setzeZeile("# z-component of Vec Ree");
		test = 0.0;
		for(int i = 0; i < HG_ReeZ.GetNrBins(); i++)
		{
			histo_ReeZ.setzeZeile(HG_ReeZ.GetRangeInBin(i)+" "+HG_ReeZ.GetNrInBinNormiert(i));
			test += HG_ReeZ.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_ReeZ: "+ test);

		histo3D.setzeZeile("# vtk DataFile Version 1.0");
		histo3D.setzeZeile("Histogramm Orientierung EndzuEndVektor von Heparin");
		histo3D.setzeZeile("ASCII");
		histo3D.setzeZeile("DATASET STRUCTURED_POINTS");
		histo3D.setzeZeile("DIMENSIONS "+HG3D_Rod.GetNrBins1() + " " +HG3D_Rod.GetNrBins2() +" "+HG3D_Rod.GetNrBins3());
		//histo3D.setzeZeile("ORIGIN 0.0 0.0 0.0");
		//histo3D.setzeZeile("ASPECT_RATIO 1.0 1.0 1.0");
		histo3D.setzeZeile("ORIGIN "+(float) minHistoCounterionsXY+" "+(float) minHistoCounterionsXY+" "+(float) minHistoCounterionsZ);
		histo3D.setzeZeile("ASPECT_RATIO "+ (float)(maxHistoCounterionsXY-minHistoCounterionsXY)/binHistoCounterionsXY+" "+(float) (maxHistoCounterionsXY-minHistoCounterionsXY)/binHistoCounterionsXY+" "+(float) (maxHistoCounterionsZ-minHistoCounterionsZ)/binHistoCounterionsZ);
		histo3D.setzeZeile("");
		histo3D.setzeZeile("POINT_DATA " +(HG3D_Rod.GetNrBins1()  * HG3D_Rod.GetNrBins2() * HG3D_Rod.GetNrBins3() ));
		histo3D.setzeZeile("SCALARS scalars double");
		histo3D.setzeZeile("LOOKUP_TABLE default");
		
		
		for(int z = 0; z < (HG3D_Rod.GetNrBins3() );z++)
		{
			
			StringBuffer objectBuffer = new StringBuffer(3000);
			
			for(int y = 0; y < (HG3D_Rod.GetNrBins2() );y++)
				for(int x = 0; x < (HG3D_Rod.GetNrBins1() );x++)
				{	
					
					//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
					objectBuffer.append(((float) HG3D_Rod.GetNrInBinNormiert(x,y,z) ) + " ");
					
				}
			
			histo3D.setzeZeile(objectBuffer.toString());
					
		}
		
		//for(int l = 0; l <  201; l+=1)
		//	rg.setzeZeile((l*50000) + " " +MessungStat[l].ReturnM1() +" "+  ( 2.0* MessungStat[l].ReturnSigma()/ Math.sqrt(1.0*MessungStat[l].ReturnN())) );
		orient.DateiSchliessen();
		punktwolke.DateiSchliessen();
		histo_theta.DateiSchliessen();
		histo_phi.DateiSchliessen();
		histo_ree.DateiSchliessen();
		histo3D.DateiSchliessen();
		
		histo_ReeX.DateiSchliessen();
		histo_ReeY.DateiSchliessen();
		histo_ReeZ.DateiSchliessen();
		
		histo3D_Counterions.setzeZeile("# vtk DataFile Version 1.0");
		histo3D_Counterions.setzeZeile("Histogramm Verteilung Gegenionen um Heparin");
		histo3D_Counterions.setzeZeile("ASCII");
		histo3D_Counterions.setzeZeile("DATASET STRUCTURED_POINTS");
		histo3D_Counterions.setzeZeile("DIMENSIONS "+HG3D_Counterions.GetNrBins1() + " " +HG3D_Counterions.GetNrBins2() +" "+HG3D_Counterions.GetNrBins3());
		histo3D_Counterions.setzeZeile("ORIGIN "+(float) minHistoCounterionsXY+" "+(float) minHistoCounterionsXY+" "+(float) minHistoCounterionsZ);
		histo3D_Counterions.setzeZeile("ASPECT_RATIO "+ (float)(maxHistoCounterionsXY-minHistoCounterionsXY)/binHistoCounterionsXY+" "+(float) (maxHistoCounterionsXY-minHistoCounterionsXY)/binHistoCounterionsXY+" "+(float) (maxHistoCounterionsZ-minHistoCounterionsZ)/binHistoCounterionsZ);
		histo3D_Counterions.setzeZeile("");
		histo3D_Counterions.setzeZeile("POINT_DATA " +(HG3D_Counterions.GetNrBins1()  * HG3D_Counterions.GetNrBins2() * HG3D_Counterions.GetNrBins3() ));
		histo3D_Counterions.setzeZeile("SCALARS scalars double");
		histo3D_Counterions.setzeZeile("LOOKUP_TABLE default");
		
		// coordinate = origin + index*spacing 
		// idx_flat = k*(npts_x*npts_y) + j*nptr_x + i.

		for(int z = 0; z < (HG3D_Counterions.GetNrBins3() );z++)
		{
			StringBuffer objectBuffer = new StringBuffer(3000);
			//String zhl = "";
			
			for(int y = 0; y < (HG3D_Counterions.GetNrBins2() );y++)
				for(int x = 0; x < (HG3D_Counterions.GetNrBins1() );x++)
				{	
					
					//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
					objectBuffer.append(((float) HG3D_Counterions.GetNrInBinNormiert(x,y,z) ) + " ");
					
				}
			
			histo3D_Counterions.setzeZeile(objectBuffer.toString());
					
		}
		
		histo3D_Counterions.DateiSchliessen();
		
		test = 0.0;
		for(int i = 0; i < HG_Distance_Counterions.GetNrBins(); i++)
		{
			histo_Distance_Counterions.setzeZeile(HG_Distance_Counterions.GetRangeInBin(i)+" "+(HG_Distance_Counterions.GetNrInBinNormiert(i)/HG_Distance_Counterions.GetIntervallThickness()));
			test += HG_Distance_Counterions.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_Distance_Counterions: "+ test);	
		
		histo_Distance_Counterions.DateiSchliessen();
		
		double Probabilitydensity_HG2D_CounterionsMax=0.0;
		for(int z = 0; z < (HG2D_Counterions.GetNrBins2() );z++)
			{
				StringBuffer objectBuffer = new StringBuffer(3000); 
				
				for(int rho = 0; rho < (HG2D_Counterions.GetNrBins1() );rho++)
				{	
					
					//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
					//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
					double  Probabilitydensity_HG2D_Counterions = HG2D_Counterions.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_Counterions.GetRangeInBin1(rho)*HG2D_Counterions.GetIntervallThicknessOfBins1()*HG2D_Counterions.GetIntervallThicknessOfBins2()); 
					
					if(Probabilitydensity_HG2D_Counterions > Probabilitydensity_HG2D_CounterionsMax)
						Probabilitydensity_HG2D_CounterionsMax=Probabilitydensity_HG2D_Counterions;
					
					objectBuffer.append(((float) Probabilitydensity_HG2D_Counterions ) + " ");
					
					//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
					
				}
			
				histo2D_Counterions.setzeZeile(objectBuffer.toString());
			}
		
		
		histo2D_Counterions.DateiSchliessen();
		
		for(int z = 0; z < (HG2D_Counterions.GetNrBins2() );z++)
		{
			StringBuffer objectBuffer = new StringBuffer(3000); 
			
			for(int rho = 0; rho < (HG2D_Counterions.GetNrBins1() );rho++)
			{	
				
				//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
				//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
				double  Probabilitydensity_HG2D_Counterions = HG2D_Counterions.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_Counterions.GetRangeInBin1(rho)*HG2D_Counterions.GetIntervallThicknessOfBins1()*HG2D_Counterions.GetIntervallThicknessOfBins2()); 
				
				objectBuffer.append(((float) (Probabilitydensity_HG2D_Counterions/Probabilitydensity_HG2D_CounterionsMax)) + " ");
				
				//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
				
			}
		
			histo2D_CounterionsNormiert.setzeZeile(objectBuffer.toString());
		}
	
	
		histo2D_CounterionsNormiert.DateiSchliessen();
		
		double Probabilitydensity_HG2D_RodMax=0.0;
		for(int z = 0; z < (HG2D_Rod.GetNrBins2() );z++)
		{
			StringBuffer objectBuffer = new StringBuffer(3000); 
			
			for(int rho = 0; rho < (HG2D_Rod.GetNrBins1() );rho++)
			{	
				
				//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
				//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
				double  Probabilitydensity_HG2D_Rod = HG2D_Rod.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_Rod.GetRangeInBin1(rho)*HG2D_Rod.GetIntervallThicknessOfBins1()*HG2D_Rod.GetIntervallThicknessOfBins2()); 
				
				if(Probabilitydensity_HG2D_Rod > Probabilitydensity_HG2D_RodMax)
					Probabilitydensity_HG2D_RodMax=Probabilitydensity_HG2D_Rod;
				
				
				objectBuffer.append(((float) Probabilitydensity_HG2D_Rod ) + " ");
				
				//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
				
			}
		
			histo2D_Rod.setzeZeile(objectBuffer.toString());
		}
	
	
	histo2D_Rod.DateiSchliessen();

	for(int z = 0; z < (HG2D_Rod.GetNrBins2() );z++)
	{
		StringBuffer objectBuffer = new StringBuffer(3000); 
		
		for(int rho = 0; rho < (HG2D_Rod.GetNrBins1() );rho++)
		{	
			
			//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
			//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
			double  Probabilitydensity_HG2D_Rod = HG2D_Rod.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_Rod.GetRangeInBin1(rho)*HG2D_Rod.GetIntervallThicknessOfBins1()*HG2D_Rod.GetIntervallThicknessOfBins2()); 
			
			objectBuffer.append(((float) (Probabilitydensity_HG2D_Rod/Probabilitydensity_HG2D_RodMax)) + " ");
			
			//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
			
		}
	
		histo2D_RodNormiert.setzeZeile(objectBuffer.toString());
	}


	histo2D_RodNormiert.DateiSchliessen();

	double  Probabilitydensity_HG2D_RodHalfMax=0.0;
	for(int z = 0; z < (HG2D_RodHalf.GetNrBins2() );z++)
	{
		StringBuffer objectBuffer = new StringBuffer(3000); 
		
		for(int rho = 0; rho < (HG2D_RodHalf.GetNrBins1() );rho++)
		{	
			
			//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
			//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
			//double  Probabilitydensity_HG2D_RodHalf = HG2D_RodHalf.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_RodHalf.GetRangeInBin1(rho)*HG2D_RodHalf.GetIntervallThicknessOfBins1()*HG2D_RodHalf.GetIntervallThicknessOfBins2()); 
			double  Probabilitydensity_HG2D_RodHalf = HG2D_RodHalf.GetNrInBinNormiert(rho,z)/(Math.PI*Math.abs(HG2D_RodHalf.GetRangeInBin1(rho))*HG2D_RodHalf.GetIntervallThicknessOfBins1()*HG2D_RodHalf.GetIntervallThicknessOfBins2()); 
			
			if(Probabilitydensity_HG2D_RodHalf > Probabilitydensity_HG2D_RodHalfMax)
				Probabilitydensity_HG2D_RodHalfMax=Probabilitydensity_HG2D_RodHalf;
			
			
			objectBuffer.append(((float) Probabilitydensity_HG2D_RodHalf ) + " ");
			
			//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
			
		}
	
		histo2D_RodHalf.setzeZeile(objectBuffer.toString());
	}


histo2D_RodHalf.DateiSchliessen();


for(int z = 0; z < (HG2D_RodHalf.GetNrBins2() );z++)
{
	StringBuffer objectBuffer = new StringBuffer(3000); 
	
	for(int rho = 0; rho < (HG2D_RodHalf.GetNrBins1() );rho++)
	{	
		
		//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
		//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
		//double  Probabilitydensity_HG2D_RodHalf = HG2D_RodHalf.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_RodHalf.GetRangeInBin1(rho)*HG2D_RodHalf.GetIntervallThicknessOfBins1()*HG2D_RodHalf.GetIntervallThicknessOfBins2()); 
		double  Probabilitydensity_HG2D_RodHalf = HG2D_RodHalf.GetNrInBinNormiert(rho,z)/(Math.PI*Math.abs(HG2D_RodHalf.GetRangeInBin1(rho))*HG2D_RodHalf.GetIntervallThicknessOfBins1()*HG2D_RodHalf.GetIntervallThicknessOfBins2()); 
		
		objectBuffer.append(((float) (Probabilitydensity_HG2D_RodHalf/Probabilitydensity_HG2D_RodHalfMax) ) + " ");
		
		//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
		
	}

	histo2D_RodHalfNormiert.setzeZeile(objectBuffer.toString());
}


histo2D_RodHalfNormiert.DateiSchliessen();



//all arcs
double  Probabilitydensity_HG2D_CounterionsHalfMax=0.0;
for(int z = 0; z < (HG2D_CounterionsHalf.GetNrBins2() );z++)
{
	StringBuffer objectBuffer = new StringBuffer(3000); 
	
	for(int rho = 0; rho < (HG2D_CounterionsHalf.GetNrBins1() );rho++)
	{	
		
		//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
		//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
		//double  Probabilitydensity_HG2D_RodHalf = HG2D_RodHalf.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_RodHalf.GetRangeInBin1(rho)*HG2D_RodHalf.GetIntervallThicknessOfBins1()*HG2D_RodHalf.GetIntervallThicknessOfBins2()); 
		double  Probabilitydensity_HG2D_CounterionsHalf = HG2D_CounterionsHalf.GetNrInBinNormiert(rho,z)/(Math.PI*Math.abs(HG2D_CounterionsHalf.GetRangeInBin1(rho))*HG2D_CounterionsHalf.GetIntervallThicknessOfBins1()*HG2D_CounterionsHalf.GetIntervallThicknessOfBins2()); 
		
		if(Probabilitydensity_HG2D_CounterionsHalf > Probabilitydensity_HG2D_CounterionsHalfMax)
			Probabilitydensity_HG2D_CounterionsHalfMax=Probabilitydensity_HG2D_CounterionsHalf;
		
		
		objectBuffer.append(((float) Probabilitydensity_HG2D_CounterionsHalf ) + " ");
		
		//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
		
	}

	histo2D_CounterionsHalf.setzeZeile(objectBuffer.toString());
}


histo2D_CounterionsHalf.DateiSchliessen();


for(int z = 0; z < (HG2D_CounterionsHalf.GetNrBins2() );z++)
{
	StringBuffer objectBuffer = new StringBuffer(3000); 
	
	for(int rho = 0; rho < (HG2D_CounterionsHalf.GetNrBins1() );rho++)
	{	
		
		//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
		//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
		//double  Probabilitydensity_HG2D_RodHalf = HG2D_RodHalf.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_RodHalf.GetRangeInBin1(rho)*HG2D_RodHalf.GetIntervallThicknessOfBins1()*HG2D_RodHalf.GetIntervallThicknessOfBins2()); 
		double  Probabilitydensity_HG2D_CounterionsHalf = HG2D_CounterionsHalf.GetNrInBinNormiert(rho,z)/(Math.PI*Math.abs(HG2D_CounterionsHalf.GetRangeInBin1(rho))*HG2D_CounterionsHalf.GetIntervallThicknessOfBins1()*HG2D_CounterionsHalf.GetIntervallThicknessOfBins2()); 
		
		
		objectBuffer.append(((float) (Probabilitydensity_HG2D_CounterionsHalf/Probabilitydensity_HG2D_CounterionsHalfMax) ) + " ");
		
		//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
		
	}

	histo2D_CounterionsHalfNormiert.setzeZeile(objectBuffer.toString());
}


histo2D_CounterionsHalfNormiert.DateiSchliessen();

	

double Probabilitydensity_HG2D_SolventMax=0.0;
for(int z = 0; z < (HG2D_Solvent.GetNrBins2() );z++)
{
	StringBuffer objectBuffer = new StringBuffer(3000); 
	
	for(int rho = 0; rho < (HG2D_Solvent.GetNrBins1() );rho++)
	{	
		
		//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
		//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
		double  Probabilitydensity_HG2D_Solvent = HG2D_Solvent.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_Solvent.GetRangeInBin1(rho)*HG2D_Solvent.GetIntervallThicknessOfBins1()*HG2D_Solvent.GetIntervallThicknessOfBins2()); 
		
		if(Probabilitydensity_HG2D_Solvent > Probabilitydensity_HG2D_SolventMax)
			Probabilitydensity_HG2D_SolventMax=Probabilitydensity_HG2D_Solvent;
		
		
		objectBuffer.append(((float) Probabilitydensity_HG2D_Solvent ) + " ");
		
		//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
		
	}

	histo2D_Solvent.setzeZeile(objectBuffer.toString());
}


histo2D_Solvent.DateiSchliessen();

for(int z = 0; z < (HG2D_Solvent.GetNrBins2() );z++)
{
StringBuffer objectBuffer = new StringBuffer(3000); 

for(int rho = 0; rho < (HG2D_Solvent.GetNrBins1() );rho++)
{	
	
	//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
	//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
	double  Probabilitydensity_HG2D_Solvent = HG2D_Solvent.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_Solvent.GetRangeInBin1(rho)*HG2D_Solvent.GetIntervallThicknessOfBins1()*HG2D_Solvent.GetIntervallThicknessOfBins2()); 
	
	objectBuffer.append(((float) (Probabilitydensity_HG2D_Solvent/Probabilitydensity_HG2D_SolventMax)) + " ");
	
	//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
	
}

histo2D_SolventNormiert.setzeZeile(objectBuffer.toString());
}


histo2D_SolventNormiert.DateiSchliessen();

double  Probabilitydensity_HG2D_SolventHalfMax=0.0;
for(int z = 0; z < (HG2D_SolventHalf.GetNrBins2() );z++)
{
StringBuffer objectBuffer = new StringBuffer(3000); 

for(int rho = 0; rho < (HG2D_SolventHalf.GetNrBins1() );rho++)
{	
	
	//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
	//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
	//double  Probabilitydensity_HG2D_SolventHalf = HG2D_SolventHalf.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_SolventHalf.GetRangeInBin1(rho)*HG2D_SolventHalf.GetIntervallThicknessOfBins1()*HG2D_SolventHalf.GetIntervallThicknessOfBins2()); 
	double  Probabilitydensity_HG2D_SolventHalf = HG2D_SolventHalf.GetNrInBinNormiert(rho,z)/(Math.PI*Math.abs(HG2D_SolventHalf.GetRangeInBin1(rho))*HG2D_SolventHalf.GetIntervallThicknessOfBins1()*HG2D_SolventHalf.GetIntervallThicknessOfBins2()); 
	
	if(Probabilitydensity_HG2D_SolventHalf > Probabilitydensity_HG2D_SolventHalfMax)
		Probabilitydensity_HG2D_SolventHalfMax=Probabilitydensity_HG2D_SolventHalf;
	
	
	objectBuffer.append(((float) Probabilitydensity_HG2D_SolventHalf ) + " ");
	
	//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");
	
}

histo2D_SolventHalf.setzeZeile(objectBuffer.toString());
}


histo2D_SolventHalf.DateiSchliessen();


for(int z = 0; z < (HG2D_SolventHalf.GetNrBins2() );z++)
{
StringBuffer objectBuffer = new StringBuffer(3000); 

for(int rho = 0; rho < (HG2D_SolventHalf.GetNrBins1() );rho++)
{	

//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
//double  Probabilitydensity_HG2D_SolventHalf = HG2D_SolventHalf.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_SolventHalf.GetRangeInBin1(rho)*HG2D_SolventHalf.GetIntervallThicknessOfBins1()*HG2D_SolventHalf.GetIntervallThicknessOfBins2()); 
double  Probabilitydensity_HG2D_SolventHalf = HG2D_SolventHalf.GetNrInBinNormiert(rho,z)/(Math.PI*Math.abs(HG2D_SolventHalf.GetRangeInBin1(rho))*HG2D_SolventHalf.GetIntervallThicknessOfBins1()*HG2D_SolventHalf.GetIntervallThicknessOfBins2()); 

objectBuffer.append(((float) (Probabilitydensity_HG2D_SolventHalf/Probabilitydensity_HG2D_SolventHalfMax) ) + " ");

//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z)/HG2D_Counterions.GetRangeInBin1(rho) ) + " ");

}

histo2D_SolventHalfNormiert.setzeZeile(objectBuffer.toString());
}


histo2D_SolventHalfNormiert.DateiSchliessen();


histo3D_Solvent.setzeZeile("# vtk DataFile Version 1.0");
histo3D_Solvent.setzeZeile("Histogramm Verteilung Gegenionen um Heparin");
histo3D_Solvent.setzeZeile("ASCII");
histo3D_Solvent.setzeZeile("DATASET STRUCTURED_POINTS");
histo3D_Solvent.setzeZeile("DIMENSIONS "+HG3D_Solvent.GetNrBins1() + " " +HG3D_Solvent.GetNrBins2() +" "+HG3D_Solvent.GetNrBins3());
histo3D_Solvent.setzeZeile("ORIGIN "+(float) minHistoCounterionsXY+" "+(float) minHistoCounterionsXY+" "+(float) minHistoCounterionsZ);
histo3D_Solvent.setzeZeile("ASPECT_RATIO "+ (float)(maxHistoCounterionsXY-minHistoCounterionsXY)/binHistoCounterionsXY+" "+(float) (maxHistoCounterionsXY-minHistoCounterionsXY)/binHistoCounterionsXY+" "+(float) (maxHistoCounterionsZ-minHistoCounterionsZ)/binHistoCounterionsZ);
histo3D_Solvent.setzeZeile("");
histo3D_Solvent.setzeZeile("POINT_DATA " +(HG3D_Solvent.GetNrBins1()  * HG3D_Solvent.GetNrBins2() * HG3D_Solvent.GetNrBins3() ));
histo3D_Solvent.setzeZeile("SCALARS scalars double");
histo3D_Solvent.setzeZeile("LOOKUP_TABLE default");

// coordinate = origin + index*spacing 
// idx_flat = k*(npts_x*npts_y) + j*nptr_x + i.

for(int z = 0; z < (HG3D_Solvent.GetNrBins3() );z++)
{
	StringBuffer objectBuffer = new StringBuffer(3000);
	//String zhl = "";
	
	for(int y = 0; y < (HG3D_Solvent.GetNrBins2() );y++)
		for(int x = 0; x < (HG3D_Solvent.GetNrBins1() );x++)
		{	
			
			//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
			objectBuffer.append(((float) HG3D_Solvent.GetNrInBinNormiert(x,y,z) ) + " ");
			
		}
	
	histo3D_Solvent.setzeZeile(objectBuffer.toString());
			
}

histo3D_Solvent.DateiSchliessen();

		
		//rg.DateiSchliessen();
		//newDataclass.DateiSchliessen();закрыть файл
		System.out.println("pZ : " +lambdacounterPZ + "    MZ : " +lambdacounterMZ+ "    +-Z : " +lambdacounterPMZ);
		System.out.println("reeRod : " +ReeRodStatistik.ReturnM1());
		
		
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
			System.out.println("Calculation of Density of Counter-ions around HEP-Rod");
			System.out.println("HEP-Rod will be Oriented in ez Direction");
			System.out.println("USAGE: SrcDir/ FileName[.bfm] DstDir/");
			System.exit(1);
		}
		System.out.println("Calculation of Density of Counter-ions around HEP-Rod");
		new Auswertung_DendrimerSingleWall_Orientierung(args[0], args[1], args[2]);
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
				
				/*if(importData.MCSTime < time_t0)
					return;
				*/
				
				
				double rcm_x=0;
				double rcm_y=0;
				double rcm_z=0;
				
				long counterCOM1 =0;
				
				for(int j=1; j <= importData.NrOfMonomers; j++)
				{
					if(importData.Attributes[j]==1)
					{
					rcm_x += importData.PolymerKoordinaten[j][0];
					rcm_y += importData.PolymerKoordinaten[j][1];
					rcm_z += importData.PolymerKoordinaten[j][2];
					
					counterCOM1++;
					}
				}
				
				rcm_x /= (1.0*counterCOM1);
				rcm_y /= (1.0*counterCOM1);
				rcm_z /= (1.0*counterCOM1);
				
				System.out.println("used monomers: " +counterCOM1);
				
				for (int l= 1; l <= 1; l++)
				{
				
				double ree_x_original = 0.0;// =importData.PolymerKoordinaten[240][0]-importData.PolymerKoordinaten[1][0];
				double ree_y_original = 0.0;// =importData.PolymerKoordinaten[240][1]-importData.PolymerKoordinaten[1][1];
				double ree_z_original = -rcm_z;// =importData.PolymerKoordinaten[240][2]-importData.PolymerKoordinaten[1][2];
					      
			    double reeOrigin_x_original = rcm_x;//importData.PolymerKoordinaten[1][0];
			    
			    double reeOrigin_y_original = rcm_y;//importData.PolymerKoordinaten[1][1];
			    	   
			    double reeOrigin_z_original = rcm_z;//importData.PolymerKoordinaten[1][2];
	   
			    
			    double ree_original = Math.sqrt(ree_x_original*ree_x_original+ree_y_original*ree_y_original+ree_z_original*ree_z_original);
				double ree_xy_original = Math.sqrt(ree_x_original*ree_x_original+ree_y_original*ree_y_original);
						
			    

				HG_ree.AddValue(ree_original);
				
				ReeRodStatistik.AddValue(ree_original);
				
				HG_ReeX.AddValue(ree_x_original/ree_original);
				HG_ReeY.AddValue(ree_y_original/ree_original);
				HG_ReeZ.AddValue(ree_z_original/ree_original);
				 
				
				double theta = 0.0;//Math.atan(Math.sqrt(ree_x*ree_x+ree_y*ree_y)/ree_z);
				
				if((ree_z_original/ree_original) > 1)
					theta = Math.PI;
				else
					theta = Math.acos(ree_z_original/ree_original);
				
				HG_theta.AddValue(theta);
				   
				
				double phi =  Math.atan2(ree_y_original, ree_x_original)+Math.PI;
				HG_phi.AddValue(phi);
				
				stupidcounter++;
				
				orient.setzeZeile(stupidcounter+" "+ree_original +" "+ theta + " "+ phi);
				punktwolke.setzeZeile((ree_x_original/ree_original)+" "+(ree_y_original/ree_original)+" "+(ree_z_original/ree_original));
				
			
				//distribution of Dendrimer-cubes
				for(int HEPCubes = 1; HEPCubes <= importData.NrOfMonomers; HEPCubes++)
				{
					
					int co_X = importData.PolymerKoordinaten[HEPCubes][0];//((importData.PolymerKoordinaten[HEPCubes][0]%Lattice_x)+Lattice_x)%Lattice_x;
					int co_Y = importData.PolymerKoordinaten[HEPCubes][1];//((importData.PolymerKoordinaten[HEPCubes][1]%Lattice_y)+Lattice_y)%Lattice_y;
					int co_Z = importData.PolymerKoordinaten[HEPCubes][2];//((importData.PolymerKoordinaten[HEPCubes][2]%Lattice_z)+Lattice_z)%Lattice_z;
					
					
					// relative distance between COM and Dendrimer
					double relDistance_X =0.0;
					double relDistance_Y =0.0;
					double relDistance_Z =0.0;
					
					//relDistance_X = calculateMinDistance(co_X,reeOrigin_x_original, Lattice_x);
					//relDistance_Y = calculateMinDistance(co_Y,reeOrigin_y_original, Lattice_y);
					//relDistance_Z = calculateMinDistance(co_Z,reeOrigin_z_original, Lattice_z);
					
					relDistance_X = co_X-reeOrigin_x_original;//((co_X-reeOrigin_x_original) < -0.5*Lattice_x) ? (co_X-reeOrigin_x_original+Lattice_x) : ( ((co_X-reeOrigin_x_original) > 0.5*Lattice_x) ? (co_X-reeOrigin_x_original-Lattice_x) : (co_X-reeOrigin_x_original) ) ;
					relDistance_Y = co_Y-reeOrigin_y_original;//((co_Y-reeOrigin_y_original) < -0.5*Lattice_y) ? (co_Y-reeOrigin_y_original+Lattice_y) : ( ((co_Y-reeOrigin_y_original) > 0.5*Lattice_y) ? (co_Y-reeOrigin_y_original-Lattice_y) : (co_Y-reeOrigin_y_original) ) ;
					relDistance_Z = co_Z-reeOrigin_z_original-ree_z_original;//((co_Z-reeOrigin_z_original) < -0.5*Lattice_z) ? (co_Z-reeOrigin_z_original+Lattice_z) : ( ((co_Z-reeOrigin_z_original) > 0.5*Lattice_z) ? (co_Z-reeOrigin_z_original-Lattice_z) : (co_Z-reeOrigin_z_original) ) ;
					
					
					double distance_X = Math.sqrt(relDistance_X*relDistance_X);
					double distance_Y = Math.sqrt(relDistance_Y*relDistance_Y);
					double distance_Z = Math.sqrt(relDistance_Z*relDistance_Z);
					
					System.out.println("z: " + (distance_Z));
				    
					
					double distance = Math.sqrt(distance_X*distance_X + distance_Y*distance_Y + distance_Z*distance_Z);

				    double distance_XY = Math.sqrt(relDistance_X*relDistance_X + relDistance_Y*relDistance_Y);
				    
				   
				    
					if((relDistance_X) < maxHistoCounterionsXY && (relDistance_X) > minHistoCounterionsXY )
						if((relDistance_Y) < maxHistoCounterionsXY && (relDistance_Y) > minHistoCounterionsXY)
							if((relDistance_Z) < maxHistoCounterionsZ && (relDistance_Z) > minHistoCounterionsZ)
							HG3D_Rod.AddValue(relDistance_X, relDistance_Y, relDistance_Z);
				
					
					
					if((distance_XY) < maxHistoCounterionsXY )
						if((distance_Z) < maxHistoCounterionsZ && (distance_Z) > minHistoCounterionsZ)
							HG2D_Rod.AddValue(distance_XY,distance_Z);
					
					
					
				}
			    
				
				//distribution of counter-ions
				int offset = 0;//240;//90*NrOfHeparin+(4*NrOfMonomersPerStarArm+1)*NrOfStars;
				
				/*
				//subtract 1 at the boundary because of overdominating end-positions
				for(int counterions = 1; counterions <= importData.NrOfMonomers; counterions++)
				{
					//return ( ((x1-x2) < LATTICE_HALF_NEGATIV) ? (x1-x2+LATTICE) : ( ((x1-x2) > LATTICE_HALF) ? (x1-x2-LATTICE) : (x1-x2) ) );
					
					//find the minimum 'image'
					
					//int co_X = xwert(importData.Polymersystem[offset+counterions]);
					//int co_Y = ywert(importData.Polymersystem[offset+counterions]);
					//int co_Z = zwert(importData.Polymersystem[offset+counterions]);
					//int co_X = ((importData.PolymerKoordinaten[offset+counterions][0]%Lattice_x)+Lattice_x)%Lattice_x;
					//int co_Y = ((importData.PolymerKoordinaten[offset+counterions][1]%Lattice_y)+Lattice_y)%Lattice_y;
					//int co_Z = ((importData.PolymerKoordinaten[offset+counterions][2]%Lattice_z)+Lattice_z)%Lattice_z;
					int co_X = importData.PolymerKoordinaten[offset+counterions][0];
					int co_Y = importData.PolymerKoordinaten[offset+counterions][1];
					int co_Z = importData.PolymerKoordinaten[offset+counterions][2];
					
					
					// relative distance between Origin HEP and Counterion
					double relDistance_X =0.0;
					double relDistance_Y =0.0;
					double relDistance_Z =0.0;
					
					//relDistance_X = ((co_X-reeOrigin_x_original) < -0.5*Lattice_x) ? (co_X-reeOrigin_x_original+Lattice_x) : ( ((co_X-reeOrigin_x_original) > 0.5*Lattice_x) ? (co_X-reeOrigin_x_original-Lattice_x) : (co_X-reeOrigin_x_original) ) ;
					//relDistance_Y = ((co_Y-reeOrigin_y_original) < -0.5*Lattice_y) ? (co_Y-reeOrigin_y_original+Lattice_y) : ( ((co_Y-reeOrigin_y_original) > 0.5*Lattice_y) ? (co_Y-reeOrigin_y_original-Lattice_y) : (co_Y-reeOrigin_y_original) ) ;
					//relDistance_Z = ((co_Z-reeOrigin_z_original) < -0.5*Lattice_z) ? (co_Z-reeOrigin_z_original+Lattice_z) : ( ((co_Z-reeOrigin_z_original) > 0.5*Lattice_z) ? (co_Z-reeOrigin_z_original-Lattice_z) : (co_Z-reeOrigin_z_original) ) ;
					
					//relDistance_X = calculateMinDistance(co_X,reeOrigin_x_original, Lattice_x);
					//relDistance_Y = calculateMinDistance(co_Y,reeOrigin_y_original, Lattice_y);
					//relDistance_Z = calculateMinDistance(co_Z,reeOrigin_z_original, Lattice_z);
					
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
					
					if((distance_xNew) < maxHistoCounterionsXY && (distance_xNew) > minHistoCounterionsXY )
						if((distance_yNew) < maxHistoCounterionsXY && (distance_yNew) > minHistoCounterionsXY)
							if((distance_zNew) < maxHistoCounterionsZ && (distance_zNew) > minHistoCounterionsZ)
							HG3D_Counterions.AddValue(distance_xNew, distance_yNew, distance_zNew);
				
					//sqrt(2*L²/4)
					if(distance_New_xy < 90.0)
						HG_Distance_Counterions.AddValue(distance_New_xy);
					
					
					if((distance_New_xy) < maxHistoCounterionsXY )
						if((distance_zNew) < maxHistoCounterionsZ && (distance_zNew) > minHistoCounterionsZ)
							HG2D_Counterions.AddValue(distance_New_xy,distance_zNew);
					
					//for the half axis
					if((distance_New_xy) < maxHistoCounterionsXY )
						if((distance_zNew) < maxHistoCounterionsZ && (distance_zNew) > minHistoCounterionsZ)
							{
								double vecX = 0.0;//importData.PolymerKoordinaten[241][0]-importData.PolymerKoordinaten[1][0];
								double vecY = 0.0;//importData.PolymerKoordinaten[241][1]-importData.PolymerKoordinaten[1][1];
								double vecZ = rcm_z;//importData.PolymerKoordinaten[241][2]-importData.PolymerKoordinaten[1][2];
								
								double vecX2 = importData.PolymerKoordinaten[offset+counterions][0];//-importData.PolymerKoordinaten[1][0];
								double vecY2 = importData.PolymerKoordinaten[offset+counterions][1];//-importData.PolymerKoordinaten[1][1];
								double vecZ2 = importData.PolymerKoordinaten[offset+counterions][2];//-importData.PolymerKoordinaten[1][2];
								
								double scalar = vecX*vecX2 + vecY*vecY2 + vecZ*vecZ2;
								
								if (scalar >0)
									{
										
										HG2D_CounterionsHalf.AddValue(distance_New_xy,distance_zNew);
									}
								else
								{
									
									HG2D_CounterionsHalf.AddValue(-distance_New_xy,distance_zNew);
								}
							}
					
				}
				*/
				
				/*
				//distribution of solvent
				offset = 480;
				
				//subtract 1 at the boundary because of overdominating end-positions
				for(int solvent = 1; solvent < MONOMERZAHL-offset; solvent++)
				{
					//return ( ((x1-x2) < LATTICE_HALF_NEGATIV) ? (x1-x2+LATTICE) : ( ((x1-x2) > LATTICE_HALF) ? (x1-x2-LATTICE) : (x1-x2) ) );
					
					//find the minimum 'image'
					
					//int co_X = xwert(importData.Polymersystem[offset+counterions]);
					//int co_Y = ywert(importData.Polymersystem[offset+counterions]);
					//int co_Z = zwert(importData.Polymersystem[offset+counterions]);
					int co_X = ((importData.PolymerKoordinaten[offset+solvent][0]%Lattice_x)+Lattice_x)%Lattice_x;
					int co_Y = ((importData.PolymerKoordinaten[offset+solvent][1]%Lattice_y)+Lattice_y)%Lattice_y;
					int co_Z = ((importData.PolymerKoordinaten[offset+solvent][2]%Lattice_z)+Lattice_z)%Lattice_z;
					//int co_X = importData.PolymerKoordinaten[offset+solvent][0];
					//int co_Y = importData.PolymerKoordinaten[offset+solvent][1];
					//int co_Z = importData.PolymerKoordinaten[offset+solvent][2];
					
					
					// relative distance between Origin HEP and Counterion
					double relDistance_X =0.0;
					double relDistance_Y =0.0;
					double relDistance_Z =0.0;
					
					relDistance_X = ((co_X-reeOrigin_x_original) < -0.5*Lattice_x) ? (co_X-reeOrigin_x_original+Lattice_x) : ( ((co_X-reeOrigin_x_original) > 0.5*Lattice_x) ? (co_X-reeOrigin_x_original-Lattice_x) : (co_X-reeOrigin_x_original) ) ;
					relDistance_Y = ((co_Y-reeOrigin_y_original) < -0.5*Lattice_y) ? (co_Y-reeOrigin_y_original+Lattice_y) : ( ((co_Y-reeOrigin_y_original) > 0.5*Lattice_y) ? (co_Y-reeOrigin_y_original-Lattice_y) : (co_Y-reeOrigin_y_original) ) ;
					relDistance_Z = ((co_Z-reeOrigin_z_original) < -0.5*Lattice_z) ? (co_Z-reeOrigin_z_original+Lattice_z) : ( ((co_Z-reeOrigin_z_original) > 0.5*Lattice_z) ? (co_Z-reeOrigin_z_original-Lattice_z) : (co_Z-reeOrigin_z_original) ) ;
					
					//relDistance_X = calculateMinDistance(co_X,reeOrigin_x_original, Lattice_x);
					//relDistance_Y = calculateMinDistance(co_Y,reeOrigin_y_original, Lattice_y);
					//relDistance_Z = calculateMinDistance(co_Z,reeOrigin_z_original, Lattice_z);
					
					//relDistance_X = co_X-reeOrigin_x_original;//((co_X-reeOrigin_x_original) < -0.5*Lattice_x) ? (co_X-reeOrigin_x_original+Lattice_x) : ( ((co_X-reeOrigin_x_original) > 0.5*Lattice_x) ? (co_X-reeOrigin_x_original-Lattice_x) : (co_X-reeOrigin_x_original) ) ;
					//relDistance_Y = co_Y-reeOrigin_y_original;//((co_Y-reeOrigin_y_original) < -0.5*Lattice_y) ? (co_Y-reeOrigin_y_original+Lattice_y) : ( ((co_Y-reeOrigin_y_original) > 0.5*Lattice_y) ? (co_Y-reeOrigin_y_original-Lattice_y) : (co_Y-reeOrigin_y_original) ) ;
					//relDistance_Z = co_Z-reeOrigin_z_original;//((co_Z-reeOrigin_z_original) < -0.5*Lattice_z) ? (co_Z-reeOrigin_z_original+Lattice_z) : ( ((co_Z-reeOrigin_z_original) > 0.5*Lattice_z) ? (co_Z-reeOrigin_z_original-Lattice_z) : (co_Z-reeOrigin_z_original) ) ;
					
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
				    
				   // if(LambdaPoint > 0.5)
				   // 	lambdacounterPZ++;
				   // else if (LambdaPoint < 0.5)
				   // 	lambdacounterMZ++;
				   // else lambdacounterPMZ++;
				   //
					if((distance_xNew) < maxHistoCounterionsXY && (distance_xNew) > minHistoCounterionsXY )
						if((distance_yNew) < maxHistoCounterionsXY && (distance_yNew) > minHistoCounterionsXY)
							if((distance_zNew) < maxHistoCounterionsZ && (distance_zNew) > minHistoCounterionsZ)
							HG3D_Solvent.AddValue(distance_xNew, distance_yNew, distance_zNew);
				
					//sqrt(2*L²/4)
					//if(distance_New_xy < 90.0)
					//	HG_Distance_Counterions.AddValue(distance_New_xy);
					
					
					if((distance_New_xy) < maxHistoCounterionsXY )
						if((distance_zNew) < maxHistoCounterionsZ && (distance_zNew) > minHistoCounterionsZ)
							HG2D_Solvent.AddValue(distance_New_xy,distance_zNew);
					
					//for the half axis
					if((distance_New_xy) < maxHistoCounterionsXY )
						if((distance_zNew) < maxHistoCounterionsZ && (distance_zNew) > minHistoCounterionsZ)
							{
								double vecX = importData.PolymerKoordinaten[241][0]-importData.PolymerKoordinaten[1][0];
								double vecY = importData.PolymerKoordinaten[241][1]-importData.PolymerKoordinaten[1][1];
								double vecZ = importData.PolymerKoordinaten[241][2]-importData.PolymerKoordinaten[1][2];
								
								double vecX2 = importData.PolymerKoordinaten[offset+solvent][0]-importData.PolymerKoordinaten[1][0];
								double vecY2 = importData.PolymerKoordinaten[offset+solvent][1]-importData.PolymerKoordinaten[1][1];
								double vecZ2 = importData.PolymerKoordinaten[offset+solvent][2]-importData.PolymerKoordinaten[1][2];
								
								double scalar = vecX*vecX2 + vecY*vecY2 + vecZ*vecZ2;
								
								if (scalar >0)
									{
										
										HG2D_SolventHalf.AddValue(distance_New_xy,distance_zNew);
									}
								else
								{
									
									HG2D_SolventHalf.AddValue(-distance_New_xy,distance_zNew);
								}
							}
					//if((distance_xNew/radiusGroundArea) < 4.0 && (distance_xNew/radiusGroundArea) > -4.0 )
					//	if((distance_yNew/radiusGroundArea) < 4.0 && (distance_yNew/radiusGroundArea) > -4.0)
					//		if((distance_zNew/zNew) < 2.0 && (distance_zNew/zNew) > -2.0)
					//		HG3D_Counterions.AddValue(distance_xNew/radiusGroundArea, distance_yNew/radiusGroundArea, distance_zNew/zNew);
				//
					//if(distance_New_xy < 12.0)
					//	HG_Distance_Counterions.AddValue(distance_New_xy/radiusGroundArea);
					//
					//
					//if((distance_New_xy/radiusGroundArea) < 4.0 && (distance_New_xy/radiusGroundArea) > -4.0 )
					//	if((distance_zNew/zNew) < 2.0 && (distance_zNew/zNew) > -2.0)
					//		HG2D_Counterions.AddValue(distance_New_xy/radiusGroundArea,distance_zNew/zNew);
					//
				}
				*/
			    
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

}
