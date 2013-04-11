package EvalToolProject.PEG;
import java.text.DecimalFormat;

import EvalToolProject.tools.BFMFileSaver;
import EvalToolProject.tools.BFMImportData;
import EvalToolProject.tools.Histogramm;
import EvalToolProject.tools.Int_IntArrayList_Hashtable;
import EvalToolProject.tools.R250_521;
import EvalToolProject.tools.Statistik;

public class Auswertung_Sterne_HepPEG_PaarkorrfktPEG {


	
	
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

	
	
	double [] Rcm_x;
	double [] Rcm_y;
	double [] Rcm_z;
	
	
	Statistik density;
	
	BFMFileSaver rg;
	
	int MONOMERZAHL;
	
	Histogramm HG_Paarkorrfct;
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	private R250_521 rand;// = new R250_521();
	
	private int counterTime;
	private long counterMonomer;
	double Bereich;
	
	public Auswertung_Sterne_HepPEG_PaarkorrfktPEG(String dstdir, String fdir, String fname)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		counterTime=0;
		counterMonomer=0;
		Bereich= 25;
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		HG_Paarkorrfct = new Histogramm(0.50,250.5,250);
		
		calculateRadialDistribution(0.50,250.5,250);
		
		System.exit(0);
		
		rand = new R250_521();
		
		
		
		
		
		
		
		density = new Statistik();
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		DecimalFormat dh = new DecimalFormat("0.0000");
		
		
		
		
		rg = new BFMFileSaver();
		rg.DateiAnlegen(dstdir+"/StarPEG_Paarkorrfkt_HepPEGSolution_"+FileName+".dat", false);
		rg.setzeZeile("# Paarcorrfct for StarPEG");
		rg.setzeZeile("# r[a] <g(r)> ");
		
		LoadFile(FileName+".bfm", currentFrame);
		
		//double constant =4.0*Math.PI*NrOfHeparin/(3.0*Gitter_x*Gitter_y*Gitter_z);
		
		double test = 0.0;
		for(int i = 0; i < HG_Paarkorrfct.GetNrBins() && HG_Paarkorrfct.GetRangeInBin(i) < (0.5*Gitter_x-Bereich); i++)
		{
			
			//double particleInDV = ((4.0*NrOfMonomersPerStarArm + 1.0)*NrOfStars) /(1.0*Gitter_x*Gitter_y*Gitter_z)* getDeltaVolume(i);
			double particleInDV = density.ReturnM1()*getDeltaVolume(i);
			
			//double particleInDV = (1.0*(MONOMERZAHL-1))/(1.0*Gitter_x*Gitter_y*Gitter_z)* getDeltaVolume(i);
			
			//double normalizedParticleNumber = counterTime*(4.0*NrOfMonomersPerStarArm + 1.0)*NrOfStars;
			double normalizedParticleNumber = counterMonomer;
			//double normalizedParticleNumber = counterMonomer/(1.0*(4.0*NrOfMonomersPerStarArm + 1.0)*NrOfStars);
			
			
			rg.setzeZeile(HG_Paarkorrfct.GetRangeInBin(i)+" "+ HG_Paarkorrfct.GetNrInBin(i)/(particleInDV*normalizedParticleNumber) );

			//rg.setzeZeile(HG_Paarkorrfct.GetRangeInBin(i)+" "+ (((1.0*Gitter_x*Gitter_y*Gitter_z)/(1.0*(1.0*counterMonomer)*(1.0)))*HG_Paarkorrfct.GetNrInBin(i)/getDeltaVolume(i)) );
			
			//test += HG_Paarkorrfct.GetNrInBinNormiert(i);
		}
		
		for(int i = 0; i < HG_Paarkorrfct.GetNrBins(); i++)
		{
			test += HG_Paarkorrfct.GetNrInBinNormiert(i);
		}
		
		System.out.println("test_HG_Paarkorrfct: "+ test + "    timeCounter: "+ counterTime+ "    counterMonomer: "+ counterMonomer + "    histCounter: "+ HG_Paarkorrfct.GetNrOfCounts());	
		
		rg.DateiSchliessen();
		
		
	}
	
	protected double getDeltaVolume(int bin)
	{
		return (4.0/3.0)*Math.PI*(Math.pow(HG_Paarkorrfct.GetRangeInBinUpperLimit(bin), 3.0) - Math.pow(HG_Paarkorrfct.GetRangeInBinLowerLimit(bin), 3.0));
		//return (4.0/3.0)*Math.PI*(Math.pow(bin+1.0, 3.0) - Math.pow(bin, 3.0))*Math.pow(HG_Paarkorrfct.GetIntervallThickness(),3.0);
		
	}
	
	protected void calculateRadialDistribution(double start, double end, int bins)
	{
		
		double dI = (end-start)/bins;
		double[] Werte = new double[bins+1];
		System.out.println("dI  "+dI+"  bins:"+ bins);
		for (int dx=-128; dx < 128; dx++)
			for (int dy=-128; dy < 128; dy++)
				for (int dz=-128; dz < 128; dz++)
				{
					double abstand = Math.sqrt(1.0*dx*dx+dy*dy+dz*dz);
					Werte[(int) ((abstand-start)/dI)]++;
				}
		
		
		for (int i=0; i< 30; i++)
			System.out.println(i + "  "+(start+(i)*dI)+"-"+(start+(i+1.0)*dI)+"  :"+ Werte[i] + " "+getDeltaVolume(i));
	}
	
	protected void LoadFile(String file, int startframe)
	{
		FileName = file;
		
		System.out.println("lade System");
		LadeSystem(FileDirectory, FileName);	
		
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(FileDirectory+FileName);
		  
		  System.arraycopy(importData.GetFrameOfSimulation(currentFrame),0,Polymersystem,0, Polymersystem.length);
		  
		  importData.CloseSimulationFile();
		  
		  importData.OpenSimulationFile(FileDirectory+FileName);
			
		  
			System.out.println("file : " +FileName );
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
			System.out.println("USAGE: dirDestiny/ dirSrc fileSrc[.bfm] ");
			System.exit(1);
		}
		else new Auswertung_Sterne_HepPEG_PaarkorrfktPEG(args[0], args[1], args[2]);//,args[1],args[2]);
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
		
		Rcm_x = new double[NrOfStars+1];
		Rcm_y = new double[NrOfStars+1];
		Rcm_z = new double[NrOfStars+1];
		
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
				importData.GetFrameOfSimulation( frame);;
				
				
				
				
				
				//System.out.println("Laenge :" + AnzahlMono);
				 //int offset = 90*NrOfHeparin;
				 
				 //if((frame >= 500)&& ((frame%5)==0))
				 if((frame) >= 5)
				
				 {
						System.out.println("Frame :" +  frame + "    counterMonomer: "+counterMonomer + "   density: "+density.ReturnM1());
					 counterTime++;
					 int offset = 90*NrOfHeparin;
					 
					
					//Zentrum Sterne
					/* for (int k=  (offset +1); k <= offset +( 4*NrOfMonomersPerStarArm + 1)*NrOfStars; k+=( 4*NrOfMonomersPerStarArm + 1))
						 for (int i=  k; i <= offset +( 4*NrOfMonomersPerStarArm + 1)*NrOfStars; i+=( 4*NrOfMonomersPerStarArm + 1))
					 {
							 double distance = 0.0;
							 
							 if(i != k)
								// if((importData.PolymerKoordinaten[i][0] > (0.5*Gitter_x-Bereich)) && (importData.PolymerKoordinaten[i][0] < (0.5*Gitter_x+Bereich)) && (importData.PolymerKoordinaten[i][1] > (0.5*Gitter_y-Bereich)) && (importData.PolymerKoordinaten[i][1] < (0.5*Gitter_y+Bereich)) && (importData.PolymerKoordinaten[i][2] > (0.5*Gitter_z-Bereich)) && (importData.PolymerKoordinaten[i][2] < (0.5*Gitter_z+Bereich)))
									 if((importData.PolymerKoordinaten[k][0] > (0.5*Gitter_x-Bereich)) && (importData.PolymerKoordinaten[k][0] < (0.5*Gitter_x+Bereich)) && (importData.PolymerKoordinaten[k][1] > (0.5*Gitter_y-Bereich)) && (importData.PolymerKoordinaten[k][1] < (0.5*Gitter_y+Bereich)) && (importData.PolymerKoordinaten[k][2] > (0.5*Gitter_z-Bereich)) && (importData.PolymerKoordinaten[k][2] < (0.5*Gitter_z+Bereich)))
									 {
										 distance = Math.sqrt(1.0*(importData.PolymerKoordinaten[i][0]-importData.PolymerKoordinaten[k][0])*(importData.PolymerKoordinaten[i][0]-importData.PolymerKoordinaten[k][0])+(importData.PolymerKoordinaten[i][1]-importData.PolymerKoordinaten[k][1])*(importData.PolymerKoordinaten[i][1]-importData.PolymerKoordinaten[k][1])+(importData.PolymerKoordinaten[i][2]-importData.PolymerKoordinaten[k][2])*(importData.PolymerKoordinaten[i][2]-importData.PolymerKoordinaten[k][2]));
										 HG_Paarkorrfct.AddValue(distance);
										 counterMonomer++;
									 }
										
					 }
					 */
					 
					 long counterMonomerForDensity = 0;
					 
					 //alle Monomere
					 for (int k=  (offset +1); k <= offset +( 4*NrOfMonomersPerStarArm + 1)*NrOfStars; k++)
						 if((importData.PolymerKoordinaten[k][0] > (0.5*Gitter_x-Bereich)) && (importData.PolymerKoordinaten[k][0] < (0.5*Gitter_x+Bereich)) && (importData.PolymerKoordinaten[k][1] > (0.5*Gitter_y-Bereich)) && (importData.PolymerKoordinaten[k][1] < (0.5*Gitter_y+Bereich)) && (importData.PolymerKoordinaten[k][2] > (0.5*Gitter_z-Bereich)) && (importData.PolymerKoordinaten[k][2] < (0.5*Gitter_z+Bereich)))
						 {
							 counterMonomer++;
							 counterMonomerForDensity++;
							 
							for (int i=  k; i <= offset +( 4*NrOfMonomersPerStarArm + 1)*NrOfStars; i++)
							{
							 double distance = 0.0;
							 
							 if(i != k)
							 {
								
								 		distance = Math.sqrt(1.0*(importData.PolymerKoordinaten[i][0]-importData.PolymerKoordinaten[k][0])*(importData.PolymerKoordinaten[i][0]-importData.PolymerKoordinaten[k][0])+(importData.PolymerKoordinaten[i][1]-importData.PolymerKoordinaten[k][1])*(importData.PolymerKoordinaten[i][1]-importData.PolymerKoordinaten[k][1])+(importData.PolymerKoordinaten[i][2]-importData.PolymerKoordinaten[k][2])*(importData.PolymerKoordinaten[i][2]-importData.PolymerKoordinaten[k][2]));
										//if(distance <= (0.5*Gitter_x))
										 { 
										   HG_Paarkorrfct.AddValue(distance);
										   HG_Paarkorrfct.AddValue(distance);
										}
									 
							 }
						}		
					 }
					 
					 density.AddValue((1.0*counterMonomerForDensity)/(8.0*Bereich*Bereich*Bereich));
					 
					//alle Monomere
					 /*for (int k=  1; k < MONOMERZAHL; k++)
						 for (int i=  k; i < MONOMERZAHL; i++)
					 {
							 double distance = 0.0;
							 
							 if(i != k)
							 {
								//double distanceCenter = Math.sqrt(1.0*(0.5*Gitter_x-importData.PolymerKoordinaten[k][0])*(0.5*Gitter_x-importData.PolymerKoordinaten[k][0])+(0.5*Gitter_y-importData.PolymerKoordinaten[k][1])*(0.5*Gitter_y-importData.PolymerKoordinaten[k][1])+(0.5*Gitter_z-importData.PolymerKoordinaten[k][2])*(0.5*Gitter_z-importData.PolymerKoordinaten[k][2]));
								
								     //if((importData.PolymerKoordinaten[i][0] > (0.5*Gitter_x-Bereich)) && (importData.PolymerKoordinaten[i][0] < (0.5*Gitter_x+Bereich)) && (importData.PolymerKoordinaten[i][1] > (0.5*Gitter_y-Bereich)) && (importData.PolymerKoordinaten[i][1] < (0.5*Gitter_y+Bereich)) && (importData.PolymerKoordinaten[i][2] > (0.5*Gitter_z-Bereich)) && (importData.PolymerKoordinaten[i][2] < (0.5*Gitter_z+Bereich)))
									 if((importData.PolymerKoordinaten[k][0] > (0.5*Gitter_x-Bereich)) && (importData.PolymerKoordinaten[k][0] < (0.5*Gitter_x+Bereich)) && (importData.PolymerKoordinaten[k][1] > (0.5*Gitter_y-Bereich)) && (importData.PolymerKoordinaten[k][1] < (0.5*Gitter_y+Bereich)) && (importData.PolymerKoordinaten[k][2] > (0.5*Gitter_z-Bereich)) && (importData.PolymerKoordinaten[k][2] < (0.5*Gitter_z+Bereich)))
								//if(distanceCenter <= Bereich)	
								{
										 distance = Math.sqrt(1.0*(importData.PolymerKoordinaten[i][0]-importData.PolymerKoordinaten[k][0])*(importData.PolymerKoordinaten[i][0]-importData.PolymerKoordinaten[k][0])+(importData.PolymerKoordinaten[i][1]-importData.PolymerKoordinaten[k][1])*(importData.PolymerKoordinaten[i][1]-importData.PolymerKoordinaten[k][1])+(importData.PolymerKoordinaten[i][2]-importData.PolymerKoordinaten[k][2])*(importData.PolymerKoordinaten[i][2]-importData.PolymerKoordinaten[k][2]));
										 
										 //if(distance <= (0.5*Gitter_x-Bereich))
										 { HG_Paarkorrfct.AddValue(distance);
											//HG_Paarkorrfct.AddValue(distance);
										 counterMonomer++;
										}
									 }
							 }
										
					 }
					 */
					 
					 
					 /*for (int k= 1; k <= NrOfStars; k++)
					 {
						 double Rcm1 = 0.0;
						double Rcm_x1 = 0.0;
						double Rcm_y1 = 0.0;
						double Rcm_z1 = 0.0;
							
						 for (int i=; i <= offset +( 4*NrOfMonomersPerStarArm + 1)*k; i++)
						  {
							  Rcm_x1 += 1.0*(importData.PolymerKoordinaten[i][0]);// - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
							  Rcm_y1 += 1.0*(importData.PolymerKoordinaten[i][1]);// - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
							  Rcm_z1 += 1.0*(importData.PolymerKoordinaten[i][2]);// - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);

							  
						  }
						 Rcm_x1 /= 1.0*(4*NrOfMonomersPerStarArm + 1);
						 Rcm_y1 /= 1.0*(4*NrOfMonomersPerStarArm + 1);
						 Rcm_z1 /= 1.0*(4*NrOfMonomersPerStarArm + 1);
						 
						 Rcm_x[k] = Rcm_x1;
						 Rcm_y[k] = Rcm_y1;
						 Rcm_z[k] = Rcm_z1;
					 }*/
					 
					 
					 
					
					
					 
					/* for(int star= 1; star <= NrOfStars; star++)
					 for (int l= star; l <= NrOfStars; l++)
					 {
						 double distance = 0.0;
						if(l != star)
							if((Rcm_x[star] > (0.5*Gitter_x-Bereich)) && (Rcm_x[star] < (0.5*Gitter_x+Bereich)) && (Rcm_y[star] > (0.5*Gitter_y-Bereich)) && (Rcm_y[star] < (0.5*Gitter_y+Bereich)) && (Rcm_z[star] > (0.5*Gitter_z-Bereich)) && (Rcm_z[star] < (0.5*Gitter_z+Bereich)))
							{
								distance = Math.sqrt((Rcm_x[l]-Rcm_x[star])*(Rcm_x[l]-Rcm_x[star])+(Rcm_y[l]-Rcm_y[star])*(Rcm_y[l]-Rcm_y[star])+(Rcm_z[l]-Rcm_z[star])*(Rcm_z[l]-Rcm_z[star]));
								HG_Paarkorrfct.AddValue(distance);
								counterMonomer++;
							}
						 
					 }*/
					 
					
				 }
				 
				 
				
					
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

}
