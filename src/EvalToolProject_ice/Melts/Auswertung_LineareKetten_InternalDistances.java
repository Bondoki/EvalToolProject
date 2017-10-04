package EvalToolProject_ice.Melts;
import java.text.DecimalFormat;

import EvalToolProject_ice.SingleObjects.Auswertung_Brush_Time_Rg_COM;
import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Histogramm;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_LineareKetten_InternalDistances {


	
	
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
	
	Statistik Ree2_stat, durchschnittbond;
	
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	long deltaT;
	
	int NrOfMonoPerChain;
	int NrOfChains;
	
	Statistik Bondlength2_Stat;
	
	Statistik[] InternalDistance_Stat;
	
	String dstDir;
	
	
	public Auswertung_LineareKetten_InternalDistances(String fdir, String fname, String dirDst, int nrOfMonoPerChain, int nrOfChains)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileNameWithEnd  = fname;
		FileName = fname.replaceAll(".bfm", "").replaceFirst(".xo", "");
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		dstDir = dirDst;
		
		NrOfMonoPerChain=nrOfMonoPerChain;
		NrOfChains=nrOfChains;
		
		
		System.out.println("-7%6="+ (-7%6));
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ FileNameWithEnd);
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		//End - Determine MaxFrame out of the first file
		
		
		
		
		skipFrames =  0;//Integer.parseInt(skip);
		//currentFrame = (int) (5000000/deltaT)+1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		currentFrame = 1;
		
		Bondlength2_Stat= new Statistik();
		
		
		InternalDistance_Stat= new Statistik[NrOfMonoPerChain];
		
		for(int i = 0; i < NrOfMonoPerChain; i++)
		{
			InternalDistance_Stat[i] = new Statistik();
		}
		
		
		
		durchschnittbond = new Statistik();
		Ree2_stat= new Statistik();
		
		
		
		DecimalFormat dh = new DecimalFormat("000");
		
		
		LoadFile(FileNameWithEnd, currentFrame);
		
		
		
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1() );
		
		BFMFileSaver rg = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.DateiAnlegen(dirDst+"/"+FileName+"_InternalDistance.dat", false);
		rg.setzeZeile("# NrOfMonomersPerChain="+ NrOfMonoPerChain);
		rg.setzeZeile("# NrOfChains="+ NrOfChains);
		rg.setzeZeile("# NrDensity c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z)));
		rg.setzeZeile("# d is element [0;" + (NrOfMonoPerChain-1)+"]");
		rg.setzeZeile("# IntDis = (r_(i+d) - r_i)**2");
		rg.setzeZeile("# IntDisDelta = [(r_(i+d) - r_i)**2]/d");
		rg.setzeZeile("# IntDisDeltaBondLength = [(r_(i+d) - r_i)**2]/(d*b**2)");
		rg.setzeZeile("# ");
		rg.setzeZeile("# <b^2)>  <(b^2)^2> d<b^2> SampleSize");
		rg.setzeZeile("# "+Bondlength2_Stat.ReturnM1()+" "+(Bondlength2_Stat.ReturnM2())+" "+( 2.0* Bondlength2_Stat.ReturnSigma()/Math.sqrt(1.0*Bondlength2_Stat.ReturnN())) + " " +Bondlength2_Stat.ReturnN());
		rg.setzeZeile("# ");
		rg.setzeZeile("# Delta <IntDis> d<IntDis> <IntDisDelta> d<IntDisDelta> <IntDisDeltaBondLength> d<IntDisDeltaBondLength> SampleSize");
		
		for(int delta=1; delta < NrOfMonoPerChain; delta++)
			if(InternalDistance_Stat[delta].ReturnN() > 2)
				rg.setzeZeile((delta) + " " + InternalDistance_Stat[delta].ReturnM1()+" "+( 2.0* InternalDistance_Stat[delta].ReturnSigma()/Math.sqrt(1.0*InternalDistance_Stat[delta].ReturnN())) + " " + (InternalDistance_Stat[delta].ReturnM1()/(1.0*delta))+" "+( 2.0* InternalDistance_Stat[delta].ReturnSigma()/Math.sqrt(1.0*InternalDistance_Stat[delta].ReturnN()))/(1.0*delta) + " " + (InternalDistance_Stat[delta].ReturnM1()/(1.0*delta*Bondlength2_Stat.ReturnM1()))+" "+( 2.0* InternalDistance_Stat[delta].ReturnSigma()/Math.sqrt(1.0*InternalDistance_Stat[delta].ReturnN())/(1.0*delta*Bondlength2_Stat.ReturnM1()) + ( 2.0* Bondlength2_Stat.ReturnSigma()/Math.sqrt(1.0*Bondlength2_Stat.ReturnN()))*(InternalDistance_Stat[delta].ReturnM1()/(1.0*delta*Bondlength2_Stat.ReturnM1()*Bondlength2_Stat.ReturnM1())) ) + " " + InternalDistance_Stat[delta].ReturnN());
		
		
		rg.DateiSchliessen();
		
		
		createXmGraceFile();
	}
	
	protected void createXmGraceFile()
	{
		BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(dstDir+"/"+FileName+"_InternalDistance.batch", false);
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_InternalDistance.dat\"");
	    xmgrace.setzeZeile("BLOCK xydy \"1:2:3\"");
	    xmgrace.setzeZeile(" world xmin 0");
	    xmgrace.setzeZeile(" world xmax " + NrOfMonoPerChain);
	    xmgrace.setzeZeile(" world ymin 0");
	    xmgrace.setzeZeile(" world ymax 5000");
	    xmgrace.setzeZeile(" xaxis label \"\\xD\\f{}\"");
	    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace.setzeZeile(" xaxis tick major 10");
	    xmgrace.setzeZeile(" xaxis tick minor 5");
	    xmgrace.setzeZeile(" yaxis label \"<(\\6r\\4\\si+\\xD\\f{}\\N - \\6r\\4\\si\\N)\\S2\\N>\\si,t\\N\"");
	    xmgrace.setzeZeile(" yaxis ticklabel place opposite");
	    xmgrace.setzeZeile(" yaxis tick major 1000");
	    xmgrace.setzeZeile(" yaxis tick minor 500");

	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 symbol 1");
	    xmgrace.setzeZeile(" s0 symbol Skip 5");
	    xmgrace.setzeZeile(" s0 errorbar off");
	    xmgrace.setzeZeile(" s0 legend \"Internal distance N="+NrOfMonoPerChain+"\"");


	    xmgrace.setzeZeile(" LEGEND 0.15, 0.6");
	    xmgrace.setzeZeile(" subtitle \"Melt; N="+NrOfMonoPerChain+"; n\\sChain\\N="+NrOfChains+"; c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z))+";\"");

	    xmgrace.setzeZeile(" SAVEALL \""+dstDir+"/"+FileName+"_InternalDistance.agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+dstDir+"/"+FileName+"_InternalDistance.ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	   
	    try {
	    	
	    	  System.out.println("xmgrace -batch "+dstDir+"/"+FileName+"_InternalDistance.batch -nosafe -hardcopy");
		      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/"+FileName+"_InternalDistance.batch -nosafe -hardcopy");
		    } catch (Exception e) {
		      System.err.println(e.toString());
		    }
		    
		    xmgrace = new BFMFileSaver();
			xmgrace.DateiAnlegen(dstDir+"/"+FileName+"_InternalDistance_Delta.batch", false);
			xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
		    xmgrace.setzeZeile("FOCUS G0");
		    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
		    xmgrace.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_InternalDistance.dat\"");
		    xmgrace.setzeZeile("BLOCK xydy \"1:4:5\"");
		    xmgrace.setzeZeile(" world xmin 0");
		    xmgrace.setzeZeile(" world xmax " + NrOfMonoPerChain);
		    xmgrace.setzeZeile(" world ymin 0");
		    xmgrace.setzeZeile(" world ymax 50");
		    xmgrace.setzeZeile(" xaxis label \"\\xD\\f{}\"");
		    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
		    xmgrace.setzeZeile(" xaxis TICK MINOR on");
		    xmgrace.setzeZeile(" xaxis tick major 10");
		    xmgrace.setzeZeile(" xaxis tick minor 5");
		    xmgrace.setzeZeile(" yaxis label \"<(\\6r\\4\\si+\\xD\\f{}\\N - \\6r\\4\\si\\N)\\S2\\N>\\si,t\\N/\\xD\\f{}\"");
		    xmgrace.setzeZeile(" yaxis tick major 10");
		    xmgrace.setzeZeile(" yaxis tick minor 5");

		    xmgrace.setzeZeile(" s0 line color 1");
		    xmgrace.setzeZeile(" s0 line linestyle 1");
		    xmgrace.setzeZeile(" s0 line linewidth 1.5");
		    xmgrace.setzeZeile(" s0 symbol 1");
		    xmgrace.setzeZeile(" s0 symbol Skip 5");
		    xmgrace.setzeZeile(" s0 errorbar off");
		    xmgrace.setzeZeile(" s0 legend \"Internal distance N="+NrOfMonoPerChain+"\"");


		    xmgrace.setzeZeile(" LEGEND 0.15, 0.6");
		    xmgrace.setzeZeile(" subtitle \"Melt; N="+NrOfMonoPerChain+"; n\\sChain\\N="+NrOfChains+"; c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z))+";\"");

		    xmgrace.setzeZeile(" SAVEALL \""+dstDir+"/"+FileName+"_InternalDistance_Delta.agr\"");

		    xmgrace.setzeZeile(" PRINT TO \""+dstDir+"/"+FileName+"_InternalDistance_Delta.ps\"");
		    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
		    xmgrace.setzeZeile("PRINT");
			
		    xmgrace.DateiSchliessen();
		   
		    try {
		    	
		    	  System.out.println("xmgrace -batch "+dstDir+"/"+FileName+"_InternalDistance_Delta.batch -nosafe -hardcopy");
			      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/"+FileName+"_InternalDistance_Delta.batch -nosafe -hardcopy");
			    } catch (Exception e) {
			      System.err.println(e.toString());
			    }
			    
			    
			    xmgrace = new BFMFileSaver();
				xmgrace.DateiAnlegen(dstDir+"/"+FileName+"_InternalDistance_DeltaNormiert.batch", false);
				xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
			    xmgrace.setzeZeile("FOCUS G0");
			    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
			    xmgrace.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_InternalDistance.dat\"");
			    xmgrace.setzeZeile("BLOCK xydy \"1:6:7\"");
			    xmgrace.setzeZeile(" world xmin 0");
			    xmgrace.setzeZeile(" world xmax " + NrOfMonoPerChain);
			    xmgrace.setzeZeile(" world ymin 1");
			    xmgrace.setzeZeile(" world ymax 5");
			    xmgrace.setzeZeile(" xaxis label \"\\xD\\f{}\"");
			    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
			    xmgrace.setzeZeile(" xaxis TICK MINOR on");
			    xmgrace.setzeZeile(" xaxis tick major 10");
			    xmgrace.setzeZeile(" xaxis tick minor 5");
			    xmgrace.setzeZeile(" yaxis label \"<(\\6r\\4\\si+\\xD\\f{}\\N - \\6r\\4\\si\\N)\\S2\\N>\\si,t\\N/\\xD\\f{}\\#{b7}<b\\S2\\N>\"");
			    xmgrace.setzeZeile(" yaxis tick major 1");
			    xmgrace.setzeZeile(" yaxis tick minor 0.25");

			    xmgrace.setzeZeile(" s0 line color 1");
			    xmgrace.setzeZeile(" s0 line linestyle 1");
			    xmgrace.setzeZeile(" s0 line linewidth 1.5");
			    xmgrace.setzeZeile(" s0 symbol 1");
			    xmgrace.setzeZeile(" s0 symbol Skip 5");
			    xmgrace.setzeZeile(" s0 errorbar off");
			    xmgrace.setzeZeile(" s0 legend \"Internal distance N="+NrOfMonoPerChain+"\"");


			    xmgrace.setzeZeile(" LEGEND 0.15, 0.6");
			    xmgrace.setzeZeile(" subtitle \"Melt; N="+NrOfMonoPerChain+"; n\\sChain\\N="+NrOfChains+"; c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z))+";\"");

			    xmgrace.setzeZeile(" SAVEALL \""+dstDir+"/"+FileName+"_InternalDistance_DeltaNormiert.agr\"");

			    xmgrace.setzeZeile(" PRINT TO \""+dstDir+"/"+FileName+"_InternalDistance_DeltaNormiert.ps\"");
			    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
			    xmgrace.setzeZeile("PRINT");
				
			    xmgrace.DateiSchliessen();
			   
			    try {
			    	
			    	  System.out.println("xmgrace -batch "+dstDir+"/"+FileName+"_InternalDistance_DeltaNormiert.batch -nosafe -hardcopy");
				      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/"+FileName+"_InternalDistance_DeltaNormiert.batch -nosafe -hardcopy");
				    } catch (Exception e) {
				      System.err.println(e.toString());
				    }
	}
	
	
	protected void LoadFile(String file, int startframe)
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
		if(args.length != 5)
		{
			System.out.println("Berechnung Rg2");
			System.out.println("USAGE: dirSrc/ FileLinearChains[.xo]  dirDst/ NrOfMonoPerChains NrOfChains");
		}
		else new Auswertung_LineareKetten_InternalDistances(args[0], args[1], args[2] , Integer.parseInt(args[3]) , Integer.parseInt(args[4]));//,args[1],args[2]);
	
		
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
		
		
		System.out.println("Nr of Monomers per Chains in File: "+ importData.NrOfMonoPerChain);
		if(importData.NrOfMonoPerChain != NrOfMonoPerChain)
		{
			System.out.println("Nr of Monomers per Chains does not met. Exiting...");
			System.exit(1);
		}
		System.out.println("Nr of Chains in File: "+ (importData.NrOfMonomers/importData.NrOfMonoPerChain));
		if( (importData.NrOfMonomers/importData.NrOfMonoPerChain) != NrOfChains)
		{
			System.out.println("Nr of Chains does not met. Exiting...");
			System.exit(1);
		}
		
		NrofFrames = importData.GetNrOfFrames();
	
		String zutrt = 	FileName.substring(0, FileName.length()-4);

		System.out.println("String : " + zutrt);
		
		
		
	}
	
	public void playSimulation(int frame)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				importData.GetFrameOfSimulation( frame);;
				
				int offset = 0;
				
				for (int nrChains= 0; nrChains < NrOfChains; nrChains++)
				{
					double diff_x2 = 0.0;
					double diff_y2 = 0.0;
					double diff_z2 = 0.0;
						
					int deltaMonomers = 0;
					
					 for (int i= nrChains*NrOfMonoPerChain+1; i <= (nrChains+1)*NrOfMonoPerChain; i++)
					  for (int j = i; j <= (nrChains+1)*NrOfMonoPerChain; j++)
					  {
						  deltaMonomers = j-i;
						  
						  diff_x2 = 1.0*(importData.PolymerKoordinaten[offset+i][0] - importData.PolymerKoordinaten[offset+j][0])*(importData.PolymerKoordinaten[offset+i][0] - importData.PolymerKoordinaten[offset+j][0]);
						  diff_y2 = 1.0*(importData.PolymerKoordinaten[offset+i][1] - importData.PolymerKoordinaten[offset+j][1])*(importData.PolymerKoordinaten[offset+i][1] - importData.PolymerKoordinaten[offset+j][1]);
						  diff_z2 = 1.0*(importData.PolymerKoordinaten[offset+i][2] - importData.PolymerKoordinaten[offset+j][2])*(importData.PolymerKoordinaten[offset+i][2] - importData.PolymerKoordinaten[offset+j][2]);
					 
						  InternalDistance_Stat[deltaMonomers].AddValue(diff_x2+diff_y2+diff_z2);
					  }
					 
					
					
					for (int i= nrChains*NrOfMonoPerChain+1; i < (nrChains+1)*NrOfMonoPerChain; i++)
					{
						double bondlength_X = 1.0*(importData.PolymerKoordinaten[offset+i+1][0]-importData.PolymerKoordinaten[offset+i][0]);
						double bondlength_Y = 1.0*(importData.PolymerKoordinaten[offset+i+1][1]-importData.PolymerKoordinaten[offset+i][1]);
						double bondlength_Z = 1.0*(importData.PolymerKoordinaten[offset+i+1][2]-importData.PolymerKoordinaten[offset+i][2]);
						
						Bondlength2_Stat.AddValue(bondlength_X*bondlength_X+bondlength_Y*bondlength_Y+bondlength_Z*bondlength_Z);
					}
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
