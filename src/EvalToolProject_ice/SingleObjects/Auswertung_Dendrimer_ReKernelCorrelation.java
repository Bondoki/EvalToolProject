package EvalToolProject_ice.SingleObjects;
import java.text.DecimalFormat;


import EvalToolProject_ice.tools.*;


public class Auswertung_Dendrimer_ReKernelCorrelation {


	
	
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
	
	Statistik durchschnittbond;
	
	
	Int_IntArrayList_Table Bindungsnetzwerk; 
	
	long deltaT;
	
	int NrOfMonoPerChain;
	int NrOfChains;
	
	
	
	double[][] rKernel_X; //[time][idx=chainnumber] First Chain Monomer
	double[][] rKernel_Y; //[time][idx=chainnumber] First Chain Monomer
	double[][] rKernel_Z; //[time][idx=chainnumber] First Chain Monomer
	
	double[][] rAllEnds_X; //[time][idx=end group number] Center Monomer
	double[][] rAllEnds_Y; //[time][idx=end group number] Center Monomer
	double[][] rAllEnds_Z; //[time][idx=end group number] Center Monomer
	
	
	Statistik[] ReeCorr_Time_Stat; //Ree-correlation
	
	int startAtFrame;
	int diffFrame;
	
	
	String dstDir;
	
	IntArrayList EndArray;
	
	public Auswertung_Dendrimer_ReKernelCorrelation(String fdir, String fname, String dirDst, int nrOfMonoPerChain, int nrOfChains, int _startAtFrame, int _diffFrame)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileNameWithEnd = fname;
		FileName = fname.replaceAll(".bfm", "").replaceFirst(".xo", "");
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		dstDir = dirDst;
		
		NrOfMonoPerChain=nrOfMonoPerChain;
		NrOfChains=nrOfChains;
		
		startAtFrame = _startAtFrame-1;
		diffFrame = _diffFrame;
		
		System.out.println("-7%6="+ (-7%6));
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ FileNameWithEnd);
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT + "  startAtFrame:" +startAtFrame);
		//End - Determine MaxFrame out of the first file
		
		System.out.println("Difference in frame: "+ diffFrame+ "  = dt "+ (diffFrame*deltaT));
		
		
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		
		rKernel_X = new double[maxframe+1][nrOfChains]; //[time][idx=chainnumber]
		rKernel_Y = new double[maxframe+1][nrOfChains]; //[time][idx=chainnumber]
		rKernel_Z = new double[maxframe+1][nrOfChains]; //[time][idx=chainnumber]
		
		
		rAllEnds_X = new double[maxframe+1][193]; //[time][idx=chainnumber]
		rAllEnds_Y = new double[maxframe+1][193]; //[time][idx=chainnumber]
		rAllEnds_Z = new double[maxframe+1][193]; //[time][idx=chainnumber]
		
		
		ReeCorr_Time_Stat = new Statistik[maxframe+1];
		
		for(int i = 0; i <= maxframe; i++)
		{
			
			ReeCorr_Time_Stat[i] = new Statistik();
		}
		
		
		
		durchschnittbond = new Statistik();
		
		
		
		DecimalFormat dh = new DecimalFormat("000");
		
		//load file
		LoadFile(FileNameWithEnd, 1);
		
		System.out.println("calculation <r_k,z(t+dt)*r_k,z(t)>");
		
		//calculate g1, g2, g3
		for(int dt = 0; dt <= maxframe; dt+=diffFrame)
			{
			System.out.print("dt="+dt+ " ");
			
			for(int time = 0; time <= maxframe; time++)
				if( (startAtFrame+time+dt) < maxframe)
				{
					for (int nrChains= 0; nrChains < NrOfChains; nrChains++)
					{
						
						for(int ends=0; ends <= 192; ends++)
						{
							double diffReeZ =(rKernel_Z[startAtFrame+time+dt][nrChains]-rAllEnds_Z[startAtFrame+time+dt][ends])*(rKernel_Z[startAtFrame+dt][nrChains]-rAllEnds_Z[startAtFrame+dt][ends]);
							double diffReeY =(rKernel_Y[startAtFrame+time+dt][nrChains]-rAllEnds_Y[startAtFrame+time+dt][ends])*(rKernel_Y[startAtFrame+dt][nrChains]-rAllEnds_Y[startAtFrame+dt][ends]);
							double diffReeX =(rKernel_X[startAtFrame+time+dt][nrChains]-rAllEnds_X[startAtFrame+time+dt][ends])*(rKernel_X[startAtFrame+dt][nrChains]-rAllEnds_X[startAtFrame+dt][ends]);
							
							ReeCorr_Time_Stat[time].AddValue(diffReeX+diffReeY+diffReeZ);
							
						}
						
						
						
					}
				}
			
				
			}
		
		
			
		
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1() );
		
		

		BFMFileSaver CorrSaver = new BFMFileSaver();
		CorrSaver.DateiAnlegen(dirDst+"/"+FileName+"_ReeCorr_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".dat", false);
		CorrSaver.setzeZeile("# NrOfMonomersPerChain="+ NrOfMonoPerChain);
		CorrSaver.setzeZeile("# NrOfChains="+ NrOfChains);
		CorrSaver.setzeZeile("# NrDensity c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z)));
		CorrSaver.setzeZeile("# tBeginn = "+ (startAtFrame*deltaT) + "    startAtFrame(+1) = " + (startAtFrame+1));
		CorrSaver.setzeZeile("# dt = "+ (diffFrame*deltaT) + "    dframe = " + diffFrame);
		CorrSaver.setzeZeile("# t  <r_z,1(t+dt)*r_z,1(t)> <r_z,127(t+dt)*r_z,127(t)> SampleSize");
		
		for(int time=0; time < (maxframe-startAtFrame); time++)
		{
			if(ReeCorr_Time_Stat[time].ReturnN() > 2)
				CorrSaver.setzeZeile((deltaT*time) + " " + ReeCorr_Time_Stat[time].ReturnM1()+ " " +ReeCorr_Time_Stat[time].ReturnN());
		}
		
		CorrSaver.DateiSchliessen();
		
		
		
		//createXmGraceFile();
	}
	
	protected void createXmGraceFile()
	{
		BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(dstDir+"/"+FileName+"_Ree_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".batch", false);
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    //xmgrace.setzeZeile("G0 TYPE XY");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_Ree_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".dat\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:5\"");
	    xmgrace.setzeZeile("BLOCK xy \"1:8\"");
	    xmgrace.setzeZeile(" xaxes scale Logarithmic");
	    xmgrace.setzeZeile(" yaxes scale Logarithmic");
	    xmgrace.setzeZeile(" world xmin 10000");
	    xmgrace.setzeZeile(" world xmax 1000000000.0");
	    xmgrace.setzeZeile(" world ymin 0.5");
	    xmgrace.setzeZeile(" world ymax 500000.0");
	    
	    xmgrace.setzeZeile(" xaxis label \"t [MCS]\"");
	    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace.setzeZeile(" xaxis tick major 10");
	    xmgrace.setzeZeile(" xaxis tick minor 1");
	    xmgrace.setzeZeile(" yaxis label \"g1, g2, g3\"");
	    xmgrace.setzeZeile(" yaxis  label place opposite");
	    xmgrace.setzeZeile(" yaxis tick major 10");
	    xmgrace.setzeZeile(" yaxis tick minor 1");

	    
	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 symbol 0");
	    //xmgrace.setzeZeile(" s0 symbol 1");
	    xmgrace.setzeZeile(" s0 errorbar off");
	    xmgrace.setzeZeile(" s0 SYMBOL Skip 100");
	    xmgrace.setzeZeile(" s0 legend \"g1\"");

	    
	    xmgrace.setzeZeile(" s1 line color 2");
	    xmgrace.setzeZeile(" s1 line linestyle 1");
	    xmgrace.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace.setzeZeile(" s1 symbol 0");
	    //xmgrace.setzeZeile(" s1 symbol 2");
	    xmgrace.setzeZeile(" s1 errorbar off");
	    xmgrace.setzeZeile(" s1 SYMBOL Skip 100");
	    xmgrace.setzeZeile(" s1 legend \"g2\"");
	    
	    
	    xmgrace.setzeZeile(" s2 line color 3");
	    xmgrace.setzeZeile(" s2 line linestyle 1");
	    xmgrace.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace.setzeZeile(" s2 symbol 0");
	    //xmgrace.setzeZeile(" s2 symbol 3");
	    xmgrace.setzeZeile(" s2 errorbar off");
	    xmgrace.setzeZeile(" s2 SYMBOL Skip 100");
	    xmgrace.setzeZeile(" s2 SYMBOL linewidth 1.5");
	    xmgrace.setzeZeile(" s2 legend \"g3\"");

//	    xmgrace.setzeZeile(" s3 line color 4");
//	    xmgrace.setzeZeile(" s3 line linestyle 1");
//	    xmgrace.setzeZeile(" s3 line linewidth 1.5");
//	    xmgrace.setzeZeile(" s3 symbol 0");
	    //xmgrace.setzeZeile(" s3 symbol 3");
//	    xmgrace.setzeZeile(" s3 errorbar off");
//	    xmgrace.setzeZeile(" s3 SYMBOL Skip 100");
//	    xmgrace.setzeZeile(" s3 SYMBOL linewidth 1.5");
//	    xmgrace.setzeZeile(" s3 legend \"g4\"");

	    
	    xmgrace.setzeZeile(" LEGEND 0.15, 0.6");
	    xmgrace.setzeZeile(" subtitle \"Melt; N="+NrOfMonoPerChain+"; n\\sChain\\N="+NrOfChains+"; c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z))+";dt="+(diffFrame*deltaT)+";t\\sBegin\\N="+(startAtFrame*deltaT)+"\"");

	    xmgrace.setzeZeile(" SAVEALL \""+dstDir+"/"+FileName+"_Ree_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+dstDir+"/"+FileName+"_Ree_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	   
	    try {
	    	
	    	  System.out.println("xmgrace -batch "+dstDir+"/"+FileName+"_g123_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".batch -nosafe -hardcopy");
		      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/"+FileName+"_g123_Time_df"+diffFrame+"_f0_"+(startAtFrame+1)+".batch -nosafe -hardcopy");
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
		if(args.length != 7)
		{
			System.out.println("Berechnung g1, g2, g3");
			System.out.println("Starting at [startAtFrame] with [deltaFrames]");
			System.out.println("[startAtFrame] is element [1:maxframe]");
			System.out.println("USAGE: dirSrc/ FileLinearChains[.xo]  dirDst/ NrOfMonoPerChains NrOfChains startAtFrame DiffFrames");
		}
		else new Auswertung_Dendrimer_ReKernelCorrelation(args[0], args[1], args[2] , Integer.parseInt(args[3]) , Integer.parseInt(args[4]), Integer.parseInt(args[5]), Integer.parseInt(args[6]));//,args[1],args[2]);
	
		
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
		/*if(importData.NrOfMonoPerChain != NrOfMonoPerChain)
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
		*/
		
		Bindungsnetzwerk = null;
		Bindungsnetzwerk = new Int_IntArrayList_Table(MONOMERZAHL);
		
		
		for(int it = 0; it < importData.bonds.size(); it++)
		{
			long bondobj = importData.bonds.get(it);
			//System.out.println(it + " bond " + bondobj);
			int mono1 = getMono1Nr(bondobj);
			int mono2 = getMono2Nr(bondobj);
			
			boolean foundBond = false;  
			
			for (int j = 0; j < Bindungsnetzwerk.get(mono1).size(); j++)
				if(Bindungsnetzwerk.get(mono1).get(j) == mono2)
					foundBond = true;
			
			for (int j = 0; j < Bindungsnetzwerk.get(mono2).size(); j++)
				if(Bindungsnetzwerk.get(mono2).get(j) == mono1)
					foundBond = true;
			
			if(foundBond == false)
			{
				Bindungsnetzwerk.put(mono1, mono2);
				Bindungsnetzwerk.put(mono2, mono1);
				System.out.println("bonds_sim: "+it+"   a:" + getMono1Nr(bondobj) + " b:" + getMono2Nr(bondobj));
			}
			
		}
		
		for(int it = 0; it < importData.additionalbonds.size(); it++)
		{
			long bondobj = importData.additionalbonds.get(it);
			//System.out.println(it + " bond " + bondobj);
			int mono1 = getMono1Nr(bondobj);
			int mono2 = getMono2Nr(bondobj);
			
			boolean foundBond = false;  
			
			for (int j = 0; j < Bindungsnetzwerk.get(mono1).size(); j++)
				if(Bindungsnetzwerk.get(mono1).get(j) == mono2)
					foundBond = true;
			
			for (int j = 0; j < Bindungsnetzwerk.get(mono2).size(); j++)
				if(Bindungsnetzwerk.get(mono2).get(j) == mono1)
					foundBond = true;
			
			if(foundBond == false)
			{
				Bindungsnetzwerk.put(mono1, mono2);
				Bindungsnetzwerk.put(mono2, mono1);
				System.out.println("additionalbonds_sim: "+it+"   a:" + getMono1Nr(bondobj) + " b:" + getMono2Nr(bondobj));
			}
			
			
		}
		
		EndArray = null;
		EndArray = new IntArrayList();
		
		int counter = 0;
		for (int i= 1; i < MONOMERZAHL; i++)
		{
			
			if(Bindungsnetzwerk.get(i).size() == 1)
			{
				counter ++;
				System.out.println(counter + " Monomer with 1 connection: "+ i);
				EndArray.add(i);
			}
			
			if(Bindungsnetzwerk.get(i).size() > 3)
				System.out.println("Monomers with >3 connections: "+ i);
		}
		
		NrofFrames = importData.GetNrOfFrames();
	
		String zutrt = 	FileName.substring(0, FileName.length()-4);

		System.out.println("String : " + zutrt);
		
		
		
	}
	
	public void playSimulation(int frame)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				importData.GetFrameOfSimulation( frame);
				
				
				
				for (int nrChains= 0; nrChains < NrOfChains; nrChains++)
				{
					
					
					rKernel_X[frame-1][nrChains]=importData.PolymerKoordinaten[1][0]; //[time][idx=chainnumber]
					rKernel_Y[frame-1][nrChains]=importData.PolymerKoordinaten[1][1]; //[time][idx=chainnumber]
					rKernel_Z[frame-1][nrChains]=importData.PolymerKoordinaten[1][2]; //[time][idx=chainnumber]
					
					for(int i = 0; i < EndArray.size(); i++)
					{
						rAllEnds_X[frame-1][i]=importData.PolymerKoordinaten[nrChains*0+ EndArray.get(i)][0];
						rAllEnds_Y[frame-1][i]=importData.PolymerKoordinaten[nrChains*0+ EndArray.get(i)][1];
						rAllEnds_Z[frame-1][i]=importData.PolymerKoordinaten[nrChains*0+ EndArray.get(i)][2];
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
