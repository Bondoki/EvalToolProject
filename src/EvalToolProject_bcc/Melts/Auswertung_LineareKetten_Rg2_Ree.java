package EvalToolProject_bcc.Melts;
import java.text.DecimalFormat;

import EvalToolProject_bcc.SingleObjects.Auswertung_Brush_Time_Rg_COM;
import EvalToolProject_bcc.tools.BFMFileSaver;
import EvalToolProject_bcc.tools.BFMImportData;
import EvalToolProject_bcc.tools.Histogramm;
import EvalToolProject_bcc.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_bcc.tools.Statistik;

public class Auswertung_LineareKetten_Rg2_Ree {


	
	
	String FileName;
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
	
	Histogramm HG_ree;
	BFMFileSaver histo_ree;
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	long deltaT;
	
	int NrOfMonoPerChain;
	int NrOfChains;
	
	Statistik Rg2_Stat;
	Statistik Rg2_x_Stat;
	Statistik Rg2_y_Stat;
	Statistik Rg2_z_Stat;
	
	Statistik[] Rg2_Time_Stat;
	Statistik[] Rg2_x_Time_Stat;
	Statistik[] Rg2_y_Time_Stat;
	Statistik[] Rg2_z_Time_Stat;
	
	
	String dstDir;
	
	
	public Auswertung_LineareKetten_Rg2_Ree(String fdir, String fname, String dirDst, int nrOfMonoPerChain, int nrOfChains)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		dstDir = dirDst;
		
		NrOfMonoPerChain=nrOfMonoPerChain;
		NrOfChains=nrOfChains;
		
		
		System.out.println("-7%6="+ (-7%6));
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ fname+".xo");
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		//End - Determine MaxFrame out of the first file
		
		
		
		
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		Rg2_Stat= new Statistik();
		Rg2_x_Stat= new Statistik();
		Rg2_y_Stat= new Statistik();
		Rg2_z_Stat= new Statistik();
		
		Rg2_Time_Stat= new Statistik[maxframe+1];
		Rg2_x_Time_Stat= new Statistik[maxframe+1];
		Rg2_y_Time_Stat= new Statistik[maxframe+1];
		Rg2_z_Time_Stat= new Statistik[maxframe+1];
		
		
		for(int i = 0; i <= maxframe; i++)
		{
			Rg2_Time_Stat[i] = new Statistik();
			Rg2_x_Time_Stat[i] = new Statistik();
			Rg2_y_Time_Stat[i] = new Statistik();
			Rg2_z_Time_Stat[i] = new Statistik();
		}
		
		
		
		durchschnittbond = new Statistik();
		Ree2_stat= new Statistik();
		HG_ree = new Histogramm(0,25,100);
		histo_ree= new BFMFileSaver();
		histo_ree.DateiAnlegen(dirDst+"/"+fname+"_Histo_Ree.dat", false);
		
		
		
		DecimalFormat dh = new DecimalFormat("000");
		
		
		LoadFile(FileName+".xo", 1);
		
		
		
		double test = 0.0;
		for(int i = 0; i < HG_ree.GetNrBins(); i++)
		{
			histo_ree.setzeZeile(HG_ree.GetRangeInBin(i)+" "+HG_ree.GetNrInBinNormiert(i));
			test += HG_ree.GetNrInBinNormiert(i);
		}
		
		histo_ree.DateiSchliessen();
		
		System.out.println("test_ree: "+ test);	
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1() );
		
		BFMFileSaver rg = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.DateiAnlegen(dirDst+"/"+FileName+"_Rg2.dat", false);
		rg.setzeZeile("# NrOfMonomersPerChain="+ NrOfMonoPerChain);
		rg.setzeZeile("# NrOfChains="+ NrOfChains);
		rg.setzeZeile("# NrDensity c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z)));
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
			
		rg.DateiSchliessen();
		
		
		BFMFileSaver rgtime = new BFMFileSaver();
		rgtime.DateiAnlegen(dirDst+"/"+FileName+"_Rg2_Time.dat", false);
		rgtime.setzeZeile("# Time <(Rg2)>  <(Rg2)^2> d<Rg2> <(Rg2_x)>  <(Rg2_x)^2> d<Rg2_x> <(Rg2_y)>  <(Rg2_y)^2> d<Rg2_y> <(Rg2_z)>  <(Rg2_z)^2> d<Rg2_z> SampleSize");
		
		for(int i=0; i < maxframe; i++)
			rgtime.setzeZeile((deltaT*i) + " " + Rg2_Time_Stat[i].ReturnM1()+" "+(Rg2_Time_Stat[i].ReturnM2())+" "+( 2.0* Rg2_Time_Stat[i].ReturnSigma()/Math.sqrt(1.0*Rg2_Time_Stat[i].ReturnN())) + " " + Rg2_x_Time_Stat[i].ReturnM1()+" "+(Rg2_x_Time_Stat[i].ReturnM2())+" "+( 2.0* Rg2_x_Time_Stat[i].ReturnSigma()/Math.sqrt(1.0*Rg2_x_Time_Stat[i].ReturnN())) + " " + Rg2_y_Time_Stat[i].ReturnM1()+" "+(Rg2_y_Time_Stat[i].ReturnM2())+" "+( 2.0* Rg2_y_Time_Stat[i].ReturnSigma()/Math.sqrt(1.0*Rg2_y_Time_Stat[i].ReturnN())) + " " + Rg2_z_Time_Stat[i].ReturnM1()+" "+(Rg2_z_Time_Stat[i].ReturnM2())+" "+( 2.0* Rg2_z_Time_Stat[i].ReturnSigma()/Math.sqrt(1.0*Rg2_z_Time_Stat[i].ReturnN())) + " " + Rg2_z_Time_Stat[i].ReturnN());
			
		rgtime.DateiSchliessen();
		
		createXmGraceFile();
	}
	
	protected void createXmGraceFile()
	{
		BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(dstDir+"/"+FileName+"_Rg2_Time.batch", false);
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ BLOCK \""+dstDir+""+FileName+"_Rg2_Time.dat\"");
	    xmgrace.setzeZeile("BLOCK xydy \"1:2:4\"");
	    xmgrace.setzeZeile("BLOCK xydy \"1:5:7\"");
	    xmgrace.setzeZeile("BLOCK xydy \"1:8:10\"");
	    xmgrace.setzeZeile("BLOCK xydy \"1:11:13\"");
	    //xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_PEG_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    //xmgrace.setzeZeile("BLOCK xy \"1:3\"");
	    //xmgrace.setzeZeile("BLOCK xy \"1:5\"");
	    //xmgrace.setzeZeile("READ BLOCK \""+dstDir+"Percolation_BMC_Distribution_HepPEGConnectedGel_"+FileName+".dat\"");
	    //xmgrace.setzeZeile("BLOCK xy \"1:2\"");
	    xmgrace.setzeZeile(" world xmin 0");
	    xmgrace.setzeZeile(" world xmax 100000000.0");
	    xmgrace.setzeZeile(" world ymin 0");
	    xmgrace.setzeZeile(" world ymax 1000.0");
	    xmgrace.setzeZeile(" xaxis label \"t [MCS]\"");
	    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace.setzeZeile(" xaxis tick major 10000000");
	    xmgrace.setzeZeile(" xaxis tick minor 5000000");
	    xmgrace.setzeZeile(" yaxis label \"Rg\\S2\\N\"");
	    xmgrace.setzeZeile(" yaxis tick major 100");
	    xmgrace.setzeZeile(" yaxis tick minor 50");

	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 symbol 1");
	    xmgrace.setzeZeile(" s0 symbol Skip 100");
	    xmgrace.setzeZeile(" s0 errorbar off");
	    xmgrace.setzeZeile(" s0 legend \"Rg\\S2\\N\"");

	    xmgrace.setzeZeile(" s1 line color 2");
	    xmgrace.setzeZeile(" s1 line linestyle 1");
	    xmgrace.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace.setzeZeile(" s1 symbol 2");
	    xmgrace.setzeZeile(" s1 symbol Skip 100");
	    xmgrace.setzeZeile(" s1 errorbar off");
	    xmgrace.setzeZeile(" s1 legend \"Rg\\S2\\N\\sx\\N\"");
	    
	    xmgrace.setzeZeile(" s2 line color 3");
	    xmgrace.setzeZeile(" s2 line linestyle 1");
	    xmgrace.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace.setzeZeile(" s2 symbol 3");
	    xmgrace.setzeZeile(" s2 symbol Skip 100");
	    xmgrace.setzeZeile(" s2 errorbar off");
	    xmgrace.setzeZeile(" s2 legend \"Rg\\S2\\N\\sy\\N\"");

	    xmgrace.setzeZeile(" s3 line color 4");
	    xmgrace.setzeZeile(" s3 line linestyle 1");
	    xmgrace.setzeZeile(" s3 line linewidth 1.5");
	    xmgrace.setzeZeile(" s3 symbol 4");
	    xmgrace.setzeZeile(" s3 symbol Skip 100");
	    xmgrace.setzeZeile(" s3 errorbar off");
	    xmgrace.setzeZeile(" s3 legend \"Rg\\S2\\N\\sz\\N\"");

	    xmgrace.setzeZeile(" LEGEND 0.15, 0.6");
	    xmgrace.setzeZeile(" subtitle \"Melt; N="+NrOfMonoPerChain+"; n\\sChain\\N="+NrOfChains+"; c="+((8.0*(MONOMERZAHL-1))/(Gitter_x*Gitter_y*Gitter_z))+";\"");

	    xmgrace.setzeZeile(" SAVEALL \""+dstDir+"/"+FileName+"_Rg2_Time.agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+dstDir+"/"+FileName+"_Rg2_Time.ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	   
	    try {
	    	
	    	  System.out.println("xmgrace -batch "+dstDir+"/"+FileName+"_Rg2_Time.batch -nosafe -hardcopy");
		      Runtime.getRuntime().exec("xmgrace -batch "+dstDir+"/"+FileName+"_Rg2_Time.batch -nosafe -hardcopy");
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
		else new Auswertung_LineareKetten_Rg2_Ree(args[0], args[1], args[2] , Integer.parseInt(args[3]) , Integer.parseInt(args[4]));//,args[1],args[2]);
	
		
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
				
				
				
				for (int nrChains= 0; nrChains < NrOfChains; nrChains++)
				{
					double Rg2 = 0.0;
					double Rg2_x = 0.0;
					double Rg2_y = 0.0;
					double Rg2_z = 0.0;
						
					 for (int i= nrChains*NrOfMonoPerChain+1; i <= (nrChains+1)*NrOfMonoPerChain; i++)
					  for (int j = i; j <= (nrChains+1)*NrOfMonoPerChain; j++)
					  {
						  Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
						  Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
						  Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);
					  }
					 
					Rg2 = Rg2_x + Rg2_y + Rg2_z;
					 
					Rg2_x /= 1.0*(NrOfMonoPerChain*NrOfMonoPerChain);
					Rg2_y /= 1.0*(NrOfMonoPerChain*NrOfMonoPerChain);
					Rg2_z /= 1.0*(NrOfMonoPerChain*NrOfMonoPerChain);
					Rg2 /= 1.0*(NrOfMonoPerChain*NrOfMonoPerChain);
					  
					Rg2_Stat.AddValue(Rg2);
					Rg2_x_Stat.AddValue(Rg2_x);
					Rg2_y_Stat.AddValue(Rg2_y);
					Rg2_z_Stat.AddValue(Rg2_z);
					
					Rg2_Time_Stat[(int) (importData.MCSTime/(deltaT))].AddValue(Rg2);
					Rg2_x_Time_Stat[(int) (importData.MCSTime/(deltaT))].AddValue(Rg2_x);
					Rg2_y_Time_Stat[(int) (importData.MCSTime/(deltaT))].AddValue(Rg2_y);
					Rg2_z_Time_Stat[(int) (importData.MCSTime/(deltaT))].AddValue(Rg2_z);
					
					Ree2_stat.AddValue(importData.GetDistanceWithoutCheck(nrChains*NrOfMonoPerChain+1, (nrChains+1)*NrOfMonoPerChain));
				 
					HG_ree.AddValue(Math.sqrt(importData.GetDistanceWithoutCheck(nrChains*NrOfMonoPerChain+1, (nrChains+1)*NrOfMonoPerChain)));
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
