package EvalToolProject_bcc.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_bcc.tools.BFMFileSaver;
import EvalToolProject_bcc.tools.BFMImportData;
import EvalToolProject_bcc.tools.Histogramm;
import EvalToolProject_bcc.tools.Histogramm2D;
import EvalToolProject_bcc.tools.Histogramm3D;
import EvalToolProject_bcc.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_bcc.tools.Statistik;

public class Auswertung_C60_Orientierung_Elektrostatik {


	
	
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
	
	BFMFileSaver histo2D_C60Shell;
	
	BFMFileSaver histo2D_Counterions;
	BFMFileSaver histo2D_CounterionsGnuplot;
	
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
	
	Histogramm3D HG3D;
	Histogramm2D HG2D_C60Shell;
	
	Histogramm3D HG3D_Counterions;
	Histogramm2D HG2D_Counterions;
	Histogramm HG_Distance_Counterions;
	
	Histogramm HG_theta, HG_phi, HG_ree;
	Histogramm HG_ReeX, HG_ReeY, HG_ReeZ;
	
	long lambdacounterPZ;
	long lambdacounterMZ;
	
	Statistik RadiusSphere;
	Statistik TestRadius;
	
	public Auswertung_C60_Orientierung_Elektrostatik(String srcdir, String fname, String dstDir)
	{
		//FileName = "1024_1024_0.00391_32";
		//FileName = "HeparinNetz_N_84_MSD";
		//FileName = "HeparinNetzHarder_N_90_MSD";
		//FileName = "HeparinQuad_N_80_MSD";
		FileName =  fname;
		//FileDirectory = "/home/users/dockhorn/workspace/Evaluation_Tools/";
		FileDirectorySrc = srcdir;
		FileDirectoryDst = dstDir;
		
		
		
		double minHistoCounterionsXY=-50.1;//-32.1;
		double maxHistoCounterionsXY=50.1;//32.1;
		int binHistoCounterionsXY = 100;
		
		double minHistoCounterionsZ=-40.1;//-32.1;
		double maxHistoCounterionsZ=40.1;//32.1;
		int binHistoCounterionsZ = 100;
		
		//C60-Shell
		HG3D = new Histogramm3D(minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		HG2D_C60Shell  = new Histogramm2D(0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY);
		
		//C60-Counterions
		HG3D_Counterions = new Histogramm3D(minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		HG2D_Counterions = new Histogramm2D(0,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY);
		
		HG_theta = new Histogramm(-0.01,Math.PI+.01,100);
		HG_phi = new Histogramm(-0.01,2.0*Math.PI+0.01,100);
		HG_ree = new Histogramm(0,15,100);
		
		HG_ReeX = new Histogramm(-1.05,1.05,30);
		HG_ReeY = new Histogramm(-1.05,1.05,30);
		HG_ReeZ = new Histogramm(-1.05,1.05,30);
		
		HG_Distance_Counterions = new Histogramm(0,15,100);
		
		Polymersystem = new int[1];
		skipFrames = 0;
		currentFrame = 90001;
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
		
		histo2D_C60Shell = new BFMFileSaver();
		
		histo2D_Counterions = new BFMFileSaver();
		histo2D_CounterionsGnuplot = new BFMFileSaver();
		
		
		RadiusSphere = new Statistik() ;
		TestRadius = new Statistik() ;
		//BFMFileSaver newDataclass = new BFMFileSaver(); создание нового объекта класса - это первое что надо сделать
		// newDataclass.DataiAnlegen ("написать имя файла", не правда  - создание нового файла, правда - добавление к существующему файлу)
		// newDataclass.setzeZeile("someting to print out into the file");
		
		lambdacounterPZ = 0;
		lambdacounterMZ = 0;
		
		
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
		
		histo2D_C60Shell.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_C60Shell.dat", false);
		
		histo2D_Counterions.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_Counterions.dat", false);
		histo2D_CounterionsGnuplot.DateiAnlegen(FileDirectoryDst+FileName+"_Histo2D_CounterionsGnuplot.gnu", false);
		
		histo2D_CounterionsGnuplot.setzeZeile("set view map");
		histo2D_CounterionsGnuplot.setzeZeile("unset surface");
		histo2D_CounterionsGnuplot.setzeZeile("set style data pm3d");
		histo2D_CounterionsGnuplot.setzeZeile("set style function pm3d");
		histo2D_CounterionsGnuplot.setzeZeile("set xlabel \"rho\" ");
		histo2D_CounterionsGnuplot.setzeZeile("set ylabel \"z\" ");
		histo2D_CounterionsGnuplot.setzeZeile("set pm3d implicit at b");
		histo2D_CounterionsGnuplot.setzeZeile("set title \"density distribution of counter-ions around C60\" ");
		histo2D_CounterionsGnuplot.setzeZeile("unset key");
		histo2D_CounterionsGnuplot.setzeZeile("set yrange[-15:15]");
		histo2D_CounterionsGnuplot.setzeZeile("set xrange[0:30]");
		histo2D_CounterionsGnuplot.setzeZeile("set size square");
		histo2D_CounterionsGnuplot.setzeZeile("set palette rgbformulae 22,13,-31");
		histo2D_CounterionsGnuplot.setzeZeile("splot \'"+FileDirectoryDst+FileName+"_Histo2D_Counterions.dat"+"\' matrix using (($1/"+binHistoCounterionsXY+")*"+maxHistoCounterionsXY+"):("+minHistoCounterionsZ+"+($2/"+binHistoCounterionsZ+")*"+(maxHistoCounterionsZ-minHistoCounterionsZ)+"):3 ");
		histo2D_CounterionsGnuplot.setzeLeerzeile();
		histo2D_CounterionsGnuplot.DateiSchliessen();
		
		System.out.println("file : " +FileName );
		System.out.println("dirDst : " + FileDirectoryDst);
		
		DecimalFormat dh = new DecimalFormat("0.0000");
		
		//for(int i = 0; i <= 9; i++)
		//	LoadFile("buerste_straight_25_0"+i+".bfm", 1);
		orient.setzeZeile("# r	theta	phi");
		punktwolke.setzeZeile("# ree_x	ree_y	ree_z");
		
		LoadFile(FileName+".bfm", currentFrame);
		
		double test = 0.0;
		for(int i = 0; i < HG_theta.GetNrBins(); i++)
		{
			histo_theta.setzeZeile(HG_theta.GetRangeInBin(i)+" "+(HG_theta.GetNrInBinNormiert(i)/(Math.sin(HG_theta.GetRangeInBin(i))*HG_theta.GetIntervallThickness())));
			test += HG_theta.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_theta: "+ test);	
		
		test = 0.0;
		for(int i = 0; i < HG_phi.GetNrBins(); i++)
		{
			histo_phi.setzeZeile(HG_phi.GetRangeInBin(i)+" "+(HG_phi.GetNrInBinNormiert(i)/HG_phi.GetIntervallThickness()));
			test += HG_phi.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_phi: "+ test);	
		
		test = 0.0;
		for(int i = 0; i < HG_ree.GetNrBins(); i++)
		{
			histo_ree.setzeZeile(HG_ree.GetRangeInBin(i)+" "+HG_ree.GetNrInBinNormiert(i)/(HG_ree.GetRangeInBin(i)*HG_ree.GetRangeInBin(i)*HG_ree.GetIntervallThickness()));
			test += HG_ree.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_ree: "+ test);	
		
		test = 0.0;
		for(int i = 0; i < HG_ReeX.GetNrBins(); i++)
		{
			histo_ReeX.setzeZeile(HG_ReeX.GetRangeInBin(i)+" "+HG_ReeX.GetNrInBinNormiert(i));
			test += HG_ReeX.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_ReeX: "+ test);	
		
		test = 0.0;
		for(int i = 0; i < HG_ReeY.GetNrBins(); i++)
		{
			histo_ReeY.setzeZeile(HG_ReeY.GetRangeInBin(i)+" "+HG_ReeY.GetNrInBinNormiert(i));
			test += HG_ReeY.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_ReeY: "+ test);
		
		test = 0.0;
		for(int i = 0; i < HG_ReeZ.GetNrBins(); i++)
		{
			histo_ReeZ.setzeZeile(HG_ReeZ.GetRangeInBin(i)+" "+HG_ReeZ.GetNrInBinNormiert(i));
			test += HG_ReeZ.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_ReeZ: "+ test);

		histo3D.setzeZeile("# vtk DataFile Version 1.0");
		histo3D.setzeZeile("Orientierung C60");
		histo3D.setzeZeile("ASCII");
		histo3D.setzeZeile("DATASET STRUCTURED_POINTS");
		histo3D.setzeZeile("DIMENSIONS "+HG3D.GetNrBins1() + " " +HG3D.GetNrBins2() +" "+HG3D.GetNrBins3());
		histo3D.setzeZeile("ORIGIN 0.0 0.0 0.0");
		histo3D.setzeZeile("ASPECT_RATIO 1.0 1.0 1.0");
		histo3D.setzeZeile("");
		histo3D.setzeZeile("POINT_DATA " +(HG3D.GetNrBins1()  * HG3D.GetNrBins2() * HG3D.GetNrBins3() ));
		histo3D.setzeZeile("SCALARS scalars double");
		histo3D.setzeZeile("LOOKUP_TABLE default");
		
		
		for(int z = 0; z < (HG3D.GetNrBins3() );z++)
		{
			String zhl = "";
			
			for(int y = 0; y < (HG3D.GetNrBins2() );y++)
				for(int x = 0; x < (HG3D.GetNrBins1() );x++)
				{	
					
					//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
					zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
					
				}
			
			histo3D.setzeZeile(zhl);
					
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
		histo3D_Counterions.setzeZeile("Verteilung Gegenionen um C60");
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
		test = 0.0;
		
		for(int z = 0; z < (HG3D_Counterions.GetNrBins3() );z++)
		{
			StringBuffer objectBuffer = new StringBuffer(3000);
			//String zhl = "";
			
			for(int y = 0; y < (HG3D_Counterions.GetNrBins2() );y++)
				for(int x = 0; x < (HG3D_Counterions.GetNrBins1() );x++)
				{	
					
					//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
					//test += HG3D_Counterions.GetNrInBinNormiert(x,y,z)*HG3D_Counterions.GetIntervallThicknessOfBins1()*HG3D_Counterions.GetIntervallThicknessOfBins2()*HG3D_Counterions.GetIntervallThicknessOfBins3();
					test += HG3D_Counterions.GetNrInBinNormiert(x,y,z);
					
					objectBuffer.append(((float) HG3D_Counterions.GetNrInBinNormiert(x,y,z) ) + " ");
					
				}
			
			histo3D_Counterions.setzeZeile(objectBuffer.toString());
					
		}
		
		histo3D_Counterions.DateiSchliessen();
		
		System.out.println("test_HG3D_Counterions: "+ test);	
		
		
		test = 0.0;
		for(int i = 0; i < HG_Distance_Counterions.GetNrBins(); i++)
		{
			histo_Distance_Counterions.setzeZeile(HG_Distance_Counterions.GetRangeInBin(i)+" "+HG_Distance_Counterions.GetNrInBinNormiert(i));
			test += HG_Distance_Counterions.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_Distance_Counterions: "+ test);	
		
		histo_Distance_Counterions.DateiSchliessen();
		
	
		
		for(int z = 0; z < (HG2D_Counterions.GetNrBins2() );z++)
			{
				StringBuffer objectBuffer = new StringBuffer(3000); 
				
				for(int rho = 0; rho < (HG2D_Counterions.GetNrBins1() );rho++)
				{	
					
					//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
					//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
					double  Probabilitydensity_HG2D_Counterions = HG2D_Counterions.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_Counterions.GetRangeInBin1(rho)*HG2D_Counterions.GetIntervallThicknessOfBins1()*HG2D_Counterions.GetIntervallThicknessOfBins2()); 
					
					objectBuffer.append(((float) Probabilitydensity_HG2D_Counterions ) + " ");
					//objectBuffer.append(((float) (8.0*HG2D_Counterions.GetNrInBin(rho,z)/HG2D_CounterionsNormalization.GetNrInBin(rho,0) )) + " ");
					
				}
			
				histo2D_Counterions.setzeZeile(objectBuffer.toString());
			}
		
		
		histo2D_Counterions.DateiSchliessen();
		
		double testDichte = 0.0;
		
		for(int z = 0; z < (HG2D_C60Shell.GetNrBins2() );z++)
		{
			StringBuffer objectBuffer = new StringBuffer(3000); 
			
			for(int rho = 0; rho < (HG2D_C60Shell.GetNrBins1() );rho++)
			{	
				
				//zhl += ((float) HG3D.GetNrInBinNormiert(x,y,z) ) + " ";
				//objectBuffer.append(((float) HG2D_Counterions.GetNrInBinNormiert(rho,z) ) + " ");
				double  Probabilitydensity_HG2D_C60Shell = HG2D_C60Shell.GetNrInBinNormiert(rho,z)/(2.0*Math.PI*HG2D_C60Shell.GetRangeInBin1(rho)*HG2D_C60Shell.GetIntervallThicknessOfBins1()*HG2D_C60Shell.GetIntervallThicknessOfBins2()); 
				objectBuffer.append(((float) Probabilitydensity_HG2D_C60Shell ) + " ");
				//objectBuffer.append(((float) (8.0*HG2D_Counterions.GetNrInBin(rho,z)/HG2D_CounterionsNormalization.GetNrInBin(rho,0) )) + " ");
				testDichte += Probabilitydensity_HG2D_C60Shell * 2.0*Math.PI*HG2D_C60Shell.GetRangeInBin1(rho)*HG2D_C60Shell.GetIntervallThicknessOfBins1()*HG2D_C60Shell.GetIntervallThicknessOfBins2();
			}
		
			histo2D_C60Shell.setzeZeile(objectBuffer.toString());
		}
	
	
	histo2D_C60Shell.DateiSchliessen();
	System.out.println("Integral DichteC60Shell (rho,z) : " +testDichte );

	
		//rg.DateiSchliessen();
		//newDataclass.DateiSchliessen();закрыть файл
		System.out.println("RadiusSphere : " +RadiusSphere.ReturnM1() + "   testRadiuis : " +TestRadius.ReturnM1());
	}
	
	protected void LoadFile(String file, int startframe)
	{
		FileName = file;
		
		System.out.println("lade System");
		LadeSystem(FileDirectorySrc, FileName);	
		
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(FileDirectorySrc+FileName);
		  
		  System.arraycopy(importData.GetFrameOfSimulation(currentFrame),0,Polymersystem,0, Polymersystem.length);
		  
		  importData.CloseSimulationFile();
		  
		  importData.OpenSimulationFile(FileDirectorySrc+FileName);
			
		  
			System.out.println("file : " +FileName );
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
			System.out.println("Calculation of Density of Counter-ions around C60");
			
			System.out.println("USAGE: SrcDir/ FileName[.bfm] DstDir/");
			System.exit(1);
		}
		System.out.println("Calculation of Density of Counter-ions around C60");
		new Auswertung_C60_Orientierung_Elektrostatik(args[0], args[1], args[2]);
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
		
		
		System.arraycopy(importData.Polymersystem,0,Polymersystem,0, Polymersystem.length);
		
		
		
		
		
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
				
				if(importData.MCSTime == time_t0)
				{
					System.out.println ("phys. NrofMonomers: " + importData.NrOfMonomers);
					
					for(int j=1; j <= importData.NrOfMonomers; j++)
					{
						rcm_x_t0 += importData.PolymerKoordinaten[j][0];
						rcm_y_t0 += importData.PolymerKoordinaten[j][1];
						rcm_z_t0 += importData.PolymerKoordinaten[j][2];
					}
					
					rcm_x_t0 /= (1.0*importData.NrOfMonomers);
					rcm_y_t0 /= (1.0*importData.NrOfMonomers);
					rcm_z_t0 /= (1.0*importData.NrOfMonomers);
					
				}*/
				
				/*double rcm_x=0;
				double rcm_y=0;
				double rcm_z=0;
				
				for(int j=1; j <= importData.NrOfMonomers; j++)
				{
					rcm_x += importData.PolymerKoordinaten[j][0];
					rcm_y += importData.PolymerKoordinaten[j][1];
					rcm_z += importData.PolymerKoordinaten[j][2];
				}
				
				rcm_x /= (1.0*importData.NrOfMonomers);
				rcm_y /= (1.0*importData.NrOfMonomers);
				rcm_z /= (1.0*importData.NrOfMonomers);*/
				
				//int start=1;
				
				//int ende=73; //HeparinNetzHarder
				
				//for (int l= 1; l <= NrOfHeparin; l++)
				{
				/*
				double ree_x  =importData.PolymerKoordinaten[90*(l-1)+ende+3][0]-importData.PolymerKoordinaten[90*(l-1)+start][0];
					   ree_x +=importData.PolymerKoordinaten[90*(l-1)+ende+5][0]-importData.PolymerKoordinaten[90*(l-1)+start+2][0];
					   ree_x +=importData.PolymerKoordinaten[90*(l-1)+ende  ][0]-importData.PolymerKoordinaten[90*(l-1)+start+3][0];
					   ree_x +=importData.PolymerKoordinaten[90*(l-1)+ende+2][0]-importData.PolymerKoordinaten[90*(l-1)+start+5][0];
					
					   ree_x /= 4.0;
					   
				double ree_y  =importData.PolymerKoordinaten[90*(l-1)+ende+3][1]-importData.PolymerKoordinaten[90*(l-1)+start][1];
				   	   ree_y +=importData.PolymerKoordinaten[90*(l-1)+ende+5][1]-importData.PolymerKoordinaten[90*(l-1)+start+2][1];
				       ree_y +=importData.PolymerKoordinaten[90*(l-1)+ende  ][1]-importData.PolymerKoordinaten[90*(l-1)+start+3][1];
				       ree_y +=importData.PolymerKoordinaten[90*(l-1)+ende+2][1]-importData.PolymerKoordinaten[90*(l-1)+start+5][1];
					
				       ree_y /= 4.0;
				
				double ree_z  =importData.PolymerKoordinaten[90*(l-1)+ende+3][2]-importData.PolymerKoordinaten[90*(l-1)+start][2];
					   ree_z +=importData.PolymerKoordinaten[90*(l-1)+ende+5][2]-importData.PolymerKoordinaten[90*(l-1)+start+2][2];
			           ree_z +=importData.PolymerKoordinaten[90*(l-1)+ende  ][2]-importData.PolymerKoordinaten[90*(l-1)+start+3][2];
			           ree_z +=importData.PolymerKoordinaten[90*(l-1)+ende+2][2]-importData.PolymerKoordinaten[90*(l-1)+start+5][2];
				
			           ree_z /= 4.0;
			           
			    double reeOrigin_x = importData.PolymerKoordinaten[90*(l-1)+start  ][0]+importData.PolymerKoordinaten[90*(l-1)+start+2][0]+
				   					 importData.PolymerKoordinaten[90*(l-1)+start+3][0]+importData.PolymerKoordinaten[90*(l-1)+start+5][0];
				
			    	   reeOrigin_x /= 4.0;
			    
			    double reeOrigin_y = importData.PolymerKoordinaten[90*(l-1)+start  ][1]+importData.PolymerKoordinaten[90*(l-1)+start+2][1]+
	   					             importData.PolymerKoordinaten[90*(l-1)+start+3][1]+importData.PolymerKoordinaten[90*(l-1)+start+5][1];
	
			    	   reeOrigin_y /= 4.0;
			    	   
			    double reeOrigin_z = importData.PolymerKoordinaten[90*(l-1)+start  ][2]+importData.PolymerKoordinaten[90*(l-1)+start+2][2]+
				                     importData.PolymerKoordinaten[90*(l-1)+start+3][2]+importData.PolymerKoordinaten[90*(l-1)+start+5][2];

			    	   reeOrigin_z /= 4.0;
	   
			    //map origin into [0;+box]
			    	   reeOrigin_x %= Lattice_x;
			    	   reeOrigin_y %= Lattice_y;
			    	   reeOrigin_z %= Lattice_z;
			    	   
			    	   if(reeOrigin_x < 0)
			    		   reeOrigin_x = reeOrigin_x+Lattice_x;
			    	   
			    	   if(reeOrigin_y < 0)
			    		   reeOrigin_y = reeOrigin_y+Lattice_y;
			    	   
			    	   if(reeOrigin_z < 0)
			    		   reeOrigin_z = reeOrigin_z+Lattice_z;
			    	*/   
			    
			    // find center of mass C60
			    double reeOrigin_x_cm = 0;
			    double reeOrigin_y_cm = 0;
			    double reeOrigin_z_cm = 0;
			    
			    for(int uio=1;uio <=60; uio++)
			    {
			    	reeOrigin_x_cm += importData.PolymerKoordinaten[uio][0];
			    	reeOrigin_y_cm += importData.PolymerKoordinaten[uio][1];
			    	reeOrigin_z_cm += importData.PolymerKoordinaten[uio][2];
			    }
			    
			    reeOrigin_x_cm /= 60.0;
			    reeOrigin_y_cm /= 60.0;
			    reeOrigin_z_cm /= 60.0;
			    
			    // find the radius of the ground area
			    double radiusGroundArea = 0.0;
			    
			    for(int uio=1;uio <=60; uio++)
			    {
			    	radiusGroundArea += (importData.PolymerKoordinaten[uio][0]-reeOrigin_x_cm)*(importData.PolymerKoordinaten[uio][0]-reeOrigin_x_cm);
			    	radiusGroundArea += (importData.PolymerKoordinaten[uio][1]-reeOrigin_y_cm)*(importData.PolymerKoordinaten[uio][1]-reeOrigin_y_cm);
			    	radiusGroundArea += (importData.PolymerKoordinaten[uio][2]-reeOrigin_z_cm)*(importData.PolymerKoordinaten[uio][2]-reeOrigin_z_cm);  
			    }
			    
			    radiusGroundArea /= 60.0;
			    radiusGroundArea = Math.sqrt(radiusGroundArea);
			    
			    RadiusSphere.AddValue(radiusGroundArea);
			    
			    for(int uio=1;uio <=60; uio++)
			    {
			    	double relDistance_X = (importData.PolymerKoordinaten[uio][0]-reeOrigin_x_cm);
			    	double relDistance_Y = (importData.PolymerKoordinaten[uio][1]-reeOrigin_y_cm);
			    	double relDistance_Z = (importData.PolymerKoordinaten[uio][2]-reeOrigin_z_cm);  
			    
			    	if((relDistance_X) < 32.0 && (relDistance_X) > -32.0 )
			    		if((relDistance_Y) < 32.0 && (relDistance_Y) > -32.0)
			    			if((relDistance_Z) < 32.0 && (relDistance_Z) > -32.0)
			    				HG3D.AddValue(relDistance_X,relDistance_Y,relDistance_Z);
			    	
			    	double distance = Math.sqrt(relDistance_X*relDistance_X + relDistance_Y*relDistance_Y + relDistance_Z*relDistance_Z);
			    	double distanceXY = Math.sqrt(relDistance_X*relDistance_X + relDistance_Y*relDistance_Y);
					
			    	//System.out.println("distance: " + (distance));
			    	
			    	HG_ree.AddValue(distance);
			    	
			    	HG_ReeX.AddValue(relDistance_X/distance);
			    	HG_ReeY.AddValue(relDistance_Y/distance);
			    	HG_ReeZ.AddValue(relDistance_Z/distance);
			    	
			    	double theta = 0.0;//Math.atan(Math.sqrt(ree_x*ree_x+ree_y*ree_y)/ree_z);
					
					/*if(relDistance_Z == 0.0)
					{
						theta = Math.PI/2.0;
						
					}
					else
						{
						if(relDistance_Z > 0)
							theta = Math.atan(distanceXY/relDistance_Z);
						else theta = Math.atan(distanceXY/relDistance_Z)+Math.PI;
						}
					*/
					
			    	//theta = Math.PI/2.0 - Math.acos(relDistance_Z/distanceXY);
					{
						if((relDistance_Z/distance) > 1)
							theta = Math.PI;
						//else if((relDistance_Z/distance) < -1)
						//	theta = 0;
						else
							theta = Math.acos(relDistance_Z/distance);
					}
					//Math.acos(a)
					
					//theta=(360.0/(2.0*Math.PI))*theta;
					
					HG_theta.AddValue(theta);
					   
					double phi = Math.atan(relDistance_Y/relDistance_X);
					
					/*if(relDistance_X == 0.0)
					{
						if(relDistance_Y > 0.0)
						{
							phi= Math.PI/2.0;
						}
						else phi = -Math.PI/2.0;
					}
					
					else{
						if (relDistance_X > 0.0)
							phi = Math.atan(relDistance_Y/relDistance_X);
						else
							{
								if(relDistance_Y > 0) 
									phi = Math.atan(relDistance_Y/relDistance_X)+Math.PI;
								else
									phi = Math.atan(relDistance_Y/relDistance_X)-Math.PI;
								
							}
					}
					*/
					phi = Math.atan2(relDistance_Y, relDistance_X)+Math.PI;
					//phi=(360.0/(2.0*Math.PI))*phi;
					
					
					//System.out.println("phi: " + (phi));
					
					HG_phi.AddValue(phi);
					
			    	
			    	
					if((distanceXY) < 32.0 )
						if((relDistance_Z) < 32.0 && (relDistance_Z) > -32.0)
							HG2D_C60Shell.AddValue(distanceXY,relDistance_Z);
					
			    }
			    // find the radius of the ground area
			    
			    double test = 0.0;
		    	
			    for(int uio=1;uio <=60; uio++)
			    {
			    	
			    	test += (importData.PolymerKoordinaten[uio][0]-reeOrigin_x_cm)*(importData.PolymerKoordinaten[uio][0]-reeOrigin_x_cm);
			    	test += (importData.PolymerKoordinaten[uio][1]-reeOrigin_y_cm)*(importData.PolymerKoordinaten[uio][1]-reeOrigin_y_cm);
			    	test += (importData.PolymerKoordinaten[uio][2]-reeOrigin_z_cm)*(importData.PolymerKoordinaten[uio][2]-reeOrigin_z_cm);  
			    
			    	
			    }
			    
			    test /= 60;
			    test = Math.sqrt(test);
			    
			    TestRadius.AddValue(test);
			    
			  //map cm into [0;+box]
		    	   reeOrigin_x_cm %= Lattice_x;
		    	   reeOrigin_y_cm %= Lattice_y;
		    	   reeOrigin_z_cm %= Lattice_z;
		    	   
		    	   if(reeOrigin_x_cm < 0)
		    		   reeOrigin_x_cm = reeOrigin_x_cm+Lattice_x;
		    	   
		    	   if(reeOrigin_y_cm < 0)
		    		   reeOrigin_y_cm = reeOrigin_y_cm+Lattice_y;
		    	   
		    	   if(reeOrigin_z_cm < 0)
		    		   reeOrigin_z_cm = reeOrigin_z_cm+Lattice_z;
			    
			    
			    
                /*
			    double radiusGroundArea = 0.0;
			    
			    for(int uio=0;uio <=5; uio++)
			    {
			    	radiusGroundArea += (importData.PolymerKoordinaten[90*(l-1)+start+uio][0]-reeOrigin_x_cm)*(importData.PolymerKoordinaten[90*(l-1)+start+uio][0]-reeOrigin_x_cm);
			    	radiusGroundArea += (importData.PolymerKoordinaten[90*(l-1)+start+uio][1]-reeOrigin_y_cm)*(importData.PolymerKoordinaten[90*(l-1)+start+uio][1]-reeOrigin_y_cm);
			    	radiusGroundArea += (importData.PolymerKoordinaten[90*(l-1)+start+uio][2]-reeOrigin_z_cm)*(importData.PolymerKoordinaten[90*(l-1)+start+uio][2]-reeOrigin_z_cm);  
			    }
			    
			    radiusGroundArea /= 6.0;
			    radiusGroundArea = Math.sqrt(radiusGroundArea);
			    
			    //System.out.println("128.1%128: " + (128.1%128));
				
			    double ree = Math.sqrt(ree_x*ree_x+ree_y*ree_y+ree_z*ree_z);
				double ree_xy = Math.sqrt(ree_x*ree_x+ree_y*ree_y);
						
			           
			    double cylinderUnitAxis_x = ree_x/ree;
			    double cylinderUnitAxis_y = ree_y/ree;
			    double cylinderUnitAxis_z = ree_z/ree;
			     
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
			    double xOld=ree_x;//cylinderUnitAxis_x;
			    double yOld=ree_y;//cylinderUnitAxis_y;
			    double zOld=ree_z;//cylinderUnitAxis_z;
			    
			    double xNew = (CosRot+OneMinusCosRot*eRot_x_norm*eRot_x_norm)*xOld + (eRot_x_norm*eRot_y_norm)*OneMinusCosRot*yOld        + eRot_y_norm*SinRot*zOld;
			    double yNew = (OneMinusCosRot*eRot_x_norm*eRot_y_norm)*xOld        + (CosRot+OneMinusCosRot*eRot_y_norm*eRot_y_norm)*yOld - eRot_x_norm*SinRot*zOld;
			    double zNew = (-eRot_y_norm*SinRot)*xOld						   + (eRot_x_norm*SinRot)*yOld							  + CosRot*zOld;
			    	
			    double Ree = Math.sqrt(xNew*xNew+yNew*yNew+zNew*zNew);
				double Ree_xy = Math.sqrt(xNew*xNew+yNew*yNew);
				*/
				//by definition NewX and NewY == 0!!! 
				//System.out.println(" cmX: " + reeOrigin_x_cm + "   cmY: " + reeOrigin_y_cm + "   cmZ: " + reeOrigin_z_cm );
			    //System.out.println("NewX: " + xNew + "   NewY: " + yNew + "   NewZ: " + zNew + "   ReeNew: "+Ree);
			   // System.out.println("eRot_x_norm: " + eRot_x_norm + "   eRot_y_norm: " + eRot_y_norm);
				   
			    // Maybe the Rotation doesn´t work
			    /*if(xNew > 1.0 || xNew < -1.0 || yNew > 1.0 || yNew < -1.0 )
			    	System.exit(2);
			    */
			    	 //break;
			 /*   {
			    	xNew = (CosRot+OneMinusCosRot*eRot_x_norm*eRot_x_norm)*xOld + (eRot_x_norm*eRot_y_norm)*OneMinusCosRot*yOld        + eRot_y_norm*SinRot*zOld;
				    yNew = (OneMinusCosRot*eRot_x_norm*eRot_y_norm)*xOld        + (CosRot+OneMinusCosRot*eRot_y_norm*eRot_y_norm)*yOld - eRot_x_norm*SinRot*zOld;
				    zNew = (-eRot_y_norm*SinRot)*xOld						   + (eRot_x_norm*SinRot)*yOld							  + CosRot*zOld;
				    	
				    Ree = Math.sqrt(xNew*xNew+yNew*yNew+zNew*zNew);
					Ree_xy = Math.sqrt(xNew*xNew+yNew*yNew);
					
			    }*/
			    //	break;
			    
				int offset = 60;
				
				for(int counterions = 1; counterions <= 60; counterions++)
				{
					//return ( ((x1-x2) < LATTICE_HALF_NEGATIV) ? (x1-x2+LATTICE) : ( ((x1-x2) > LATTICE_HALF) ? (x1-x2-LATTICE) : (x1-x2) ) );
					
					//find the minimum 'image'
					
					int co_X = xwert(importData.Polymersystem[offset+counterions]);
					int co_Y = ywert(importData.Polymersystem[offset+counterions]);
					int co_Z = zwert(importData.Polymersystem[offset+counterions]);
					
					// relative distance between Origin HEP and Counterion
					double relDistance_X =0.0;
					double relDistance_Y =0.0;
					double relDistance_Z =0.0;
					
					relDistance_X = ((co_X-reeOrigin_x_cm) < -0.5*Lattice_x) ? (co_X-reeOrigin_x_cm+Lattice_x) : ( ((co_X-reeOrigin_x_cm) > 0.5*Lattice_x) ? (co_X-reeOrigin_x_cm-Lattice_x) : (co_X-reeOrigin_x_cm) ) ;
					relDistance_Y = ((co_Y-reeOrigin_y_cm) < -0.5*Lattice_y) ? (co_Y-reeOrigin_y_cm+Lattice_y) : ( ((co_Y-reeOrigin_y_cm) > 0.5*Lattice_y) ? (co_Y-reeOrigin_y_cm-Lattice_y) : (co_Y-reeOrigin_y_cm) ) ;
					relDistance_Z = ((co_Z-reeOrigin_z_cm) < -0.5*Lattice_z) ? (co_Z-reeOrigin_z_cm+Lattice_z) : ( ((co_Z-reeOrigin_z_cm) > 0.5*Lattice_z) ? (co_Z-reeOrigin_z_cm-Lattice_z) : (co_Z-reeOrigin_z_cm) ) ;
					
					//System.out.println(" relX: " + relDistance_X + "   relY: " + relDistance_Y + "   relZ: " + relDistance_Z );
				    
					/*
					//Calculate the crossing-point
					double LambdaPoint = (relDistance_X*ree_x+relDistance_Y*ree_y+relDistance_Z*ree_z)/(ree*ree);
					
					double distance_X = relDistance_X-LambdaPoint*ree_x;
					double distance_Y = relDistance_Y-LambdaPoint*ree_y;
					double distance_Z = relDistance_Z-LambdaPoint*ree_z;
					
					
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
				    System.out.println("distance: " + (distance) + "   newdistance: " + (distance_New) + "   LambdaPoint: " +  LambdaPoint);
				    System.out.println("Ree: " + (zNew) + "   RadiusGroundArea: " + (radiusGroundArea) + "   Ree/RadiusRGAt: " +  (zNew/radiusGroundArea));
				    
				    
				   // System.out.println("xOldTrafo: " + distance_X + "   YOldTrafo: " + distance_Y + "   ZOldTrafo: " + distance_Z);
				   // System.out.println("xNewTrafo: " + distance_xNew + "   YNewTrafo: " + distance_yNew + "   ZNewTrafo: " + distance_zNew);
				    
				    if((distance_zNew) > 2.0 || (distance_zNew) < -2.0)
				    	System.exit(2);
				    
				    //calculate crossing point and shift center of calculation 0.5 znew down
				    distance_zNew = LambdaPoint*zNew-0.5*zNew;
				    
				    if(LambdaPoint > 0.5)
				    	lambdacounterPZ++;
				    else lambdacounterMZ++;
					*/
					/*if((relDistance_X/radiusGroundArea) < 2.0 && (relDistance_X/radiusGroundArea) > -2.0 )
						if((relDistance_Y/radiusGroundArea) < 2.0 && (relDistance_Y/radiusGroundArea) > -2.0)
							if((relDistance_Z/radiusGroundArea) < 2.0 && (relDistance_Z/radiusGroundArea) > -2.0)
							HG3D_Counterions.AddValue(relDistance_X/radiusGroundArea,relDistance_Y/radiusGroundArea,relDistance_Z/radiusGroundArea);
				
					double distance = Math.sqrt(relDistance_X*relDistance_X + relDistance_Y*relDistance_Y);
					
					if((distance/radiusGroundArea) < 2.0 )
						if((relDistance_Z/radiusGroundArea) < 2.0 && (relDistance_Z/radiusGroundArea) > -2.0)
							HG2D_Counterions.AddValue(distance/radiusGroundArea,relDistance_Z/radiusGroundArea);
					*/
					
					if((relDistance_X) < 32.0 && (relDistance_X) > -32.0 )
						if((relDistance_Y) < 32.0 && (relDistance_Y) > -32.0)
							if((relDistance_Z) < 32.0 && (relDistance_Z) > -32.0)
							HG3D_Counterions.AddValue(relDistance_X,relDistance_Y,relDistance_Z);
				
					double distance = Math.sqrt(relDistance_X*relDistance_X + relDistance_Y*relDistance_Y);
					
					if((distance) < 32.0 )
						if((relDistance_Z) < 32.0 && (relDistance_Z) > -32.0)
							HG2D_Counterions.AddValue(distance,relDistance_Z);
					
					if(distance/radiusGroundArea < 12.0)
						HG_Distance_Counterions.AddValue(distance/radiusGroundArea);
					
					
					
				/*	if((distance_xNew/radiusGroundArea) < 4.0 && (distance_xNew/radiusGroundArea) > -4.0 )
						if((distance_yNew/radiusGroundArea) < 4.0 && (distance_yNew/radiusGroundArea) > -4.0)
							if((distance_zNew/zNew) < 2.0 && (distance_zNew/zNew) > -2.0)
							HG3D_Counterions.AddValue(distance_xNew/radiusGroundArea, distance_yNew/radiusGroundArea, distance_zNew/zNew);
				
					
					if(distance_New_xy < 12.0)
						HG_Distance_Counterions.AddValue(distance_New_xy/radiusGroundArea);
				*/
				}
			    
				/*HG_ree.AddValue(ree);
				
				HG_ReeX.AddValue(ree_x/ree);
				HG_ReeY.AddValue(ree_y/ree);
				HG_ReeZ.AddValue(ree_z/ree);
				 
				HG3D.AddValue(ree_x/ree, ree_y/ree, ree_z/ree);
				
				double theta = 0.0;//Math.atan(Math.sqrt(ree_x*ree_x+ree_y*ree_y)/ree_z);
				
				if(ree_z == 0.0)
				{
					theta = Math.PI/2.0;
				}
				else
					{
					if(ree_z > 0)
						theta = Math.atan(ree_xy/ree_z);
					else theta = Math.atan(ree_xy/ree_z)+Math.PI;
					}
				
				theta=(360.0/(2.0*Math.PI))*theta;
				
				
				
				HG_theta.AddValue(theta);
				   
				double phi = Math.atan(ree_y/ree_x);
				
				if(ree_x == 0.0)
				{
					if(ree_y > 0.0)
					{
						phi= Math.PI/2.0;
					}
					else phi = -Math.PI/2.0;
				}
				
				else{
					if (ree_x > 0.0)
						phi = Math.atan(ree_y/ree_x);
					else
						{
							if(ree_y > 0) 
								phi = Math.atan(ree_y/ree_x)+Math.PI;
							else
								phi = Math.atan(ree_y/ree_x)-Math.PI;
							
						}
				}
				
				phi=(360.0/(2.0*Math.PI))*phi;
				
				//if(phi > Math.PI)
				//	phi-= Math.PI;
				//else if (phi < -Math.PI)
				//	phi += Math.PI;
				//
				System.out.println("phi: " + (phi));
				
				HG_phi.AddValue(phi);
				
				stupidcounter++;
				
				orient.setzeZeile(stupidcounter+" "+Ree +" "+ theta + " "+ phi);
				punktwolke.setzeZeile((ree_x/ree)+" "+(ree_y/ree)+" "+(ree_z/ree));*/
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

}
