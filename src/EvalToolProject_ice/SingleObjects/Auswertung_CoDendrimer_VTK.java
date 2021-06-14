package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;


import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Histogramm;
import EvalToolProject_ice.tools.Histogramm2D;
import EvalToolProject_ice.tools.Histogramm3D;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;


public class Auswertung_CoDendrimer_VTK {


	
	
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
	
	BFMFileSaver histo3D;
	
	
	BFMFileSaver histo3D_Counterions;
	
	
	
	BFMFileSaver histo3D_Solvent;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	double rcm_x_t0=0;
	double rcm_y_t0=0;
	double rcm_z_t0=0;
	
	int time_t0=135000000;//95000000;
	
	//Statistik[] MessungStat = new Statistik[34001];
	
	//double rcmX[] = new double[34001];
	//double rcmY[] = new double[34001];
	//double rcmZ[] = new double[34001];
	int stupidcounter;
	
	Histogramm3D HG3D_Rod;
	
	Histogramm3D HG3D_Counterions;
	
	
	Histogramm3D HG3D_Solvent;
	
	
	Statistik ReeRodStatistik;
	
	long lambdacounterPZ;
	long lambdacounterMZ;
	long lambdacounterPMZ;
	
	double minHistoCounterionsXY;
	double maxHistoCounterionsXY;
	double minHistoCounterionsZ;
	double maxHistoCounterionsZ;
	
	public Auswertung_CoDendrimer_VTK(String srcdir, String fname, String dstDir)
	{
		//FileName = "1024_1024_0.00391_32";
		//FileName = "HeparinNetz_N_84_MSD";
		//FileName = "HeparinNetzHarder_N_90_MSD";
		//FileName = "HeparinQuad_N_80_MSD";
		FileName =  fname;
		//FileDirectory = "/home/users/dockhorn/workspace/Evaluation_Tools/";
		FileDirectorySrc = srcdir;
		FileDirectoryDst = dstDir;
		
		
		minHistoCounterionsXY=-0.01;//0.01;//-0.01;//25.01;
		maxHistoCounterionsXY=256.01;//128.01;//15.01;//25.01;
		int binHistoCounterionsXY = 128;// 64;//40
		
		minHistoCounterionsZ=-0.01;//-0.01;//-2.01;//40.01;
		maxHistoCounterionsZ=256.01;//128.01;//70.01;//40.01;
		int binHistoCounterionsZ = 128;// 64;//25;
		
		
		//HG3D_Rod = new Histogramm3D(-1.1,1.1,50,-1.1,1.1,50,-1.1,1.1,50);
		HG3D_Rod = new Histogramm3D(minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		
		HG3D_Counterions = new Histogramm3D(minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		
		HG3D_Solvent = new Histogramm3D(minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsXY,maxHistoCounterionsXY,binHistoCounterionsXY,minHistoCounterionsZ,maxHistoCounterionsZ,binHistoCounterionsZ);
		
		
		
		Polymersystem = new int[1];
		skipFrames = 0;
		currentFrame = 1;
		//System.out.println("cf="+currentFrame);
		
		//rg = new BFMFileSaver();
		histo3D = new BFMFileSaver();
		
		histo3D_Counterions = new BFMFileSaver();
		
		histo3D_Solvent = new BFMFileSaver();
		
		
		lambdacounterPZ = 0;
		lambdacounterMZ = 0;
		lambdacounterPMZ = 0;
		
		ReeRodStatistik = new Statistik() ;
		
		//rg.DateiAnlegen("Linear25FreeMSD_MSD_10E6MCS.dat", false);
		histo3D.DateiAnlegen(FileDirectoryDst+FileName+"_Histo3D_DendriticPart_CoDendrimer.vtk", false);
		
		histo3D_Counterions.DateiAnlegen(FileDirectoryDst+FileName+"_Histo3D_LinearChainPart_CoDendrimer.vtk", false);
		
		histo3D_Solvent.DateiAnlegen(FileDirectoryDst+FileName+"_Histo3D_Solvent.vtk", false);
		
				
		System.out.println("file : " +FileName );
		System.out.println("dirDst : " + FileDirectoryDst);
		
		DecimalFormat dh = new DecimalFormat("00");
		
		
		//for(int i = 1; i <= 25; i++)
		//LoadFile(FileName+"__"+dh.format(i)+".bfm", 1);
		LoadFile(FileName+".bfm", 1);
		
		
	
		histo3D.setzeZeile("# vtk DataFile Version 1.0");
		histo3D.setzeZeile("Histogram Dendrtic Part of CoDendrimer");
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
		
		
		histo3D.DateiSchliessen();
		
		histo3D_Counterions.setzeZeile("# vtk DataFile Version 1.0");
		histo3D_Counterions.setzeZeile("Histogram LinearChain Part of CoDendrimer");
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
		new Auswertung_CoDendrimer_VTK(args[0], args[1], args[2]);
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
				
				if(importData.MCSTime >= time_t0)
				for(int j=1; j <= importData.NrOfMonomers; j++)
				{
					if((importData.Attributes[j]==1) || (importData.Attributes[j]==2))
					{
						double distance_X = importData.PolymerKoordinaten[j][0];//relDistance_X-LambdaPoint*ree_x_original;
						double distance_Y = importData.PolymerKoordinaten[j][1];//relDistance_Y-LambdaPoint*ree_y_original;
						double distance_Z = importData.PolymerKoordinaten[j][2];//relDistance_Z-LambdaPoint*ree_z_original;
				
						// fold back all coordinates into the box eg. for negative numbers ((x % k) + k) % k
						double distance_xNew = ( (distance_X % importData.box_x) + importData.box_x ) % importData.box_x;
						double distance_yNew = ( (distance_Y % importData.box_y) + importData.box_y ) % importData.box_y;
						double distance_zNew = ( (distance_Z % importData.box_z) + importData.box_z ) % importData.box_z;
						
						// for Martin and MIC
						// double distance_xNew = distance_X - importData.box_x * Math.round((distance_X)/importData.box_x);
						// double distance_yNew = distance_Y - importData.box_y * Math.round((distance_Y)/importData.box_y);
						// double distance_zNew = distance_Z - importData.box_z * Math.round((distance_Z)/importData.box_z);
						
						// for Martin and MIC
						/*
						 double COMDistVec_X_relativ = (RCom2_x-RCom1_x) - importData.box_x * Math.round((RCom2_x-RCom1_x)/importData.box_x);
						 double COMDistVec_Y_relativ = (RCom2_y-RCom1_y) - importData.box_y * Math.round((RCom2_y-RCom1_y)/importData.box_y);
						 double COMDistVec_Z_relativ = (RCom2_z-RCom1_z) - importData.box_z * Math.round((RCom2_z-RCom1_z)/importData.box_z);
						
						 double distanceCom2Com=Math.sqrt((COMDistVec_X_relativ)*(COMDistVec_X_relativ) + (COMDistVec_Y_relativ)*(COMDistVec_Y_relativ) + (COMDistVec_Z_relativ)*(COMDistVec_Z_relativ));
						*/
						
						HG3D_Rod.AddValue(distance_xNew, distance_yNew, distance_zNew);
					}
					
					if((importData.Attributes[j]==4) || (importData.Attributes[j]==5))
					{
						double distance_X = importData.PolymerKoordinaten[j][0];//relDistance_X-LambdaPoint*ree_x_original;
						double distance_Y = importData.PolymerKoordinaten[j][1];//relDistance_Y-LambdaPoint*ree_y_original;
						double distance_Z = importData.PolymerKoordinaten[j][2];//relDistance_Z-LambdaPoint*ree_z_original;
				
						// fold back all coordinates into the box eg. for negative numbers ((x % k) + k) % k
						double distance_xNew = ( (distance_X % importData.box_x) + importData.box_x ) % importData.box_x;
						double distance_yNew = ( (distance_Y % importData.box_y) + importData.box_y ) % importData.box_y;
						double distance_zNew = ( (distance_Z % importData.box_z) + importData.box_z ) % importData.box_z;
						
						// for Martin and MIC
						// double distance_xNew = distance_X - importData.box_x * Math.round((distance_X)/importData.box_x);
						// double distance_yNew = distance_Y - importData.box_y * Math.round((distance_Y)/importData.box_y);
						// double distance_zNew = distance_Z - importData.box_z * Math.round((distance_Z)/importData.box_z);
						
						// for Martin and MIC
						/*
						 double COMDistVec_X_relativ = (RCom2_x-RCom1_x) - importData.box_x * Math.round((RCom2_x-RCom1_x)/importData.box_x);
						 double COMDistVec_Y_relativ = (RCom2_y-RCom1_y) - importData.box_y * Math.round((RCom2_y-RCom1_y)/importData.box_y);
						 double COMDistVec_Z_relativ = (RCom2_z-RCom1_z) - importData.box_z * Math.round((RCom2_z-RCom1_z)/importData.box_z);
						
						 double distanceCom2Com=Math.sqrt((COMDistVec_X_relativ)*(COMDistVec_X_relativ) + (COMDistVec_Y_relativ)*(COMDistVec_Y_relativ) + (COMDistVec_Z_relativ)*(COMDistVec_Z_relativ));
						*/
						
						 HG3D_Counterions.AddValue(distance_xNew, distance_yNew, distance_zNew);
					}
					
				}
				
					
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
