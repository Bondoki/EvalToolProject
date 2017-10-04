package EvalToolProject_ice.Heparin;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Heparin_Funktionality_HepPEG {


	
	
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

	/*Statistik[] GesamtStat_x;
	Statistik[] GesamtStat_y;
	Statistik[] GesamtStat_z;*/
	
	//double [] Rcm_x;
	//double [] Rcm_y;
	//double [] Rcm_z;
	
	Statistik[] bonds;
	Statistik durchschnittbond;
	
	int[][] HeparinSterneVerknuepfung;
	
	int[] PEGBonds;
	Statistik[] DenglingEnds2;
	
	Statistik[] Singlebonds;
	Statistik[] Doublebonds;
	Statistik[] Triplebonds;
	Statistik[] Quadbonds;
	Statistik[] DenglingEnds;
	
	//BFMFileSaver rg;
	BFMFileSaver BondSaver;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	int FunktionalitaetHeparin[];
	BFMFileSaver FunktionalitaetHeparinSaver;
	
	int addedBondsDuringSimulation;
	
	long deltaT;
	
	boolean FuncHeparinCalculated;
	float minCrosslinks;
	
	
	public Auswertung_Heparin_Funktionality_HepPEG(String fdir, String fname, String gamma, String dirDst, float minimumCrosslinks)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		minCrosslinks = minimumCrosslinks;
		/*rg = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.DateiAnlegen(dirDst+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.setzeZeile("# t[MCS] <CumBond>  <CumBond/maxBond> d<CumBond>");
		*/
		BondSaver = new BFMFileSaver();
		//BondSaver.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_Bonds_HepPEGConnectedGel_"+FileName+".dat", false);
		BondSaver.DateiAnlegen(dirDst+"/StarPEG_Bonds_HepPEGConnectedGel_"+FileName+".dat", false);
		BondSaver.setzeZeile("# t[MCS] <Single>  <Double> <Triple> <Quad> <Free> <CumBonds> Sum=1");
		
		FunktionalitaetHeparinSaver = new BFMFileSaver();
		//FunktionalitaetHeparinSaver.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/HeparinFunctionality_HepPEGConnectedGel_"+FileName+".dat", false);
		FunktionalitaetHeparinSaver.DateiAnlegen(dirDst+"/HeparinFunctionality_HepPEGConnectedGel_"+FileName+".dat", false);
		
		
		DecimalFormat dh = new DecimalFormat("000");
		
		FunktionalitaetHeparin = new int[29];
		
		
		
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ FileName+"__"+dh.format(1)+".bfm");
		 
		
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		
		bonds = new Statistik[maxframe+1];
		
		for(int i = 0; i <= maxframe; i++)
			bonds[i] = new Statistik();
		
		Singlebonds = new Statistik[maxframe+1];
		Doublebonds= new Statistik[maxframe+1];
		Triplebonds= new Statistik[maxframe+1];
		Quadbonds= new Statistik[maxframe+1];
		DenglingEnds= new Statistik[maxframe+1];
		
		DenglingEnds2= new Statistik[maxframe+1];
		
		for(int i = 0; i <= maxframe; i++)
			{
				Singlebonds[i] = new Statistik();
				Doublebonds[i] = new Statistik();
				Triplebonds[i] = new Statistik();
				Quadbonds[i] = new Statistik();
				DenglingEnds[i] = new Statistik();
				DenglingEnds2[i] = new Statistik();
				
			}
		
		durchschnittbond = new Statistik();
		
		
		
		
		
		//LoadFile(FileName+"__"+dh.format(11)+".bfm", 1, maxframe);
		//LoadFile(FileName+"__"+dh.format(12)+".bfm", 1, maxframe);
		for(int i = 1; i <= 26; i++)
		{
		    FuncHeparinCalculated = false;
			LoadFile(FileName+"__"+dh.format(i)+".bfm", 1, maxframe);
		}
		
		//for(int i = 11; i <= 25; i++)
		//	LoadFile(FileName+"__"+dh.format(i)+".bfm", 1, maxframe);
		
		//for(int i = 16; i <= 26; i++)
		//	LoadFile(FileName+"__"+dh.format(i)+".bfm", 1, maxframe);
		
		
		//LoadFile(FileName+"__"+dh.format(10)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(11)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(16)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(21)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(26)+".bfm", 1);
		
		
		//rg.setzeKommentar("c <Rg2> <(Rg2)2> dF N");
		//rg.setzeZeile((importData.NrOfMonomers*8.0/(Gitter_x*Gitter_y*Gitter_z))+" "+rg_xyz_stat.ReturnM1()+" "+rg_xyz_stat.ReturnM2()+" "+( 2.0* rg_xyz_stat.ReturnSigma()/ Math.sqrt(1.0*rg_xyz_stat.ReturnN() ))+ " " + rg_xyz_stat.ReturnN());
		/*for(int i=0; i <=maxframe; i++)
		rg.setzeZeile((deltaT*i) + " " + bonds[i].ReturnM1()+" "+(bonds[i].ReturnM1()/(4.0*NrOfStars))+" "+( 2.0* bonds[i].ReturnSigma()/Math.sqrt(17))+ " " + 1.0*bonds[i].ReturnN() );
		 
		rg.DateiSchliessen();
		*/
		for(int i=0; i <=maxframe; i++)
			BondSaver.setzeZeile((deltaT*i) + " " + Singlebonds[i].ReturnM1()+ " " + Doublebonds[i].ReturnM1()+ " " + Triplebonds[i].ReturnM1()+ " " + Quadbonds[i].ReturnM1()+ " " + DenglingEnds[i].ReturnM1() +" " +(bonds[i].ReturnM1()/(4.0*NrOfStars))+" " +(Singlebonds[i].ReturnM1()+ Doublebonds[i].ReturnM1()+ Triplebonds[i].ReturnM1()+  Quadbonds[i].ReturnM1()+ DenglingEnds[i].ReturnM1() ));
		
			//BondSaver.setzeZeile((10000*i) + " " + Singlebonds[i].ReturnM1()+ " " + Doublebonds[i].ReturnM1()+ " " + Triplebonds[i].ReturnM1()+ " " + Quadbonds[i].ReturnM1()+ " " + DenglingEnds[i].ReturnM1() +" "+DenglingEnds2[i].ReturnM1() +" " +(Singlebonds[i].ReturnM1()+ Doublebonds[i].ReturnM1()+ Triplebonds[i].ReturnM1()+  Quadbonds[i].ReturnM1()+ DenglingEnds[i].ReturnM1() ));
			 
		BondSaver.DateiSchliessen();
		
		/*SaverBondsAtGamma95.setzeZeile((gamma.replace('_', '.')) + " " + SingleBondsAtGamma95.ReturnM1()+ " " + DoubleBondsAtGamma95.ReturnM1()+ " " + TripleBondsAtGamma95.ReturnM1()+ " " + QuadBondsAtGamma95.ReturnM1());
		SaverBondsAtGamma95.DateiSchliessen();
		
		SaverBondsAtGamma75.setzeZeile((gamma.replace('_', '.')) + " " + SingleBondsAtGamma75.ReturnM1()+ " " + DoubleBondsAtGamma75.ReturnM1()+ " " + TripleBondsAtGamma75.ReturnM1()+ " " + QuadBondsAtGamma75.ReturnM1());
		SaverBondsAtGamma75.DateiSchliessen();
		*/
		
		int counterFuncHEP=0;
		for(int i=0; i <= 28; i++)
			counterFuncHEP +=FunktionalitaetHeparin[i];
		
		double DurchschnittsfunktionalitaetHEP = 0.0;
		for(int i=0; i <= 28; i++)
			DurchschnittsfunktionalitaetHEP += i*(FunktionalitaetHeparin[i]/(1.0*counterFuncHEP));
		
		FunktionalitaetHeparinSaver.setzeZeile("# total sample: "+counterFuncHEP +"   ;  average functionality: "+DurchschnittsfunktionalitaetHEP);
		
		FunktionalitaetHeparinSaver.setzeZeile("# f <relative occurence> <absolute occurence> total: "+counterFuncHEP);
		
		
		for(int i=0; i <=28; i++)
			FunktionalitaetHeparinSaver.setzeZeile(i+ " " + (FunktionalitaetHeparin[i]/(1.0*counterFuncHEP))+ " " + (FunktionalitaetHeparin[i]) );
			 
		FunktionalitaetHeparinSaver.DateiSchliessen();
		
		double dummyFunc = 0.0;
		for(int i=0; i <= 28; i++)
			dummyFunc += (FunktionalitaetHeparin[i]/(1.0*counterFuncHEP));
		
		System.out.println("SummeFunktionalitaet: " +dummyFunc +"  == 1.0");
		
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1());
		
		
		BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(dirDst+"/Defects_Gamma_"+gamma+".batch", false);
		xmgrace.setzeZeile("# t[MCS] <CumBond>  <CumBond/maxBond> d<CumBond>");
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ NXY \""+dirDst+"StarPEG_Bonds_HepPEGConnectedGel_"+FileName+".dat\"");
	    xmgrace.setzeZeile(" world xmin 0");
	    xmgrace.setzeZeile(" world xmax "+importData.MCSTime);//225000");
	    xmgrace.setzeZeile(" world ymin 0");
	    xmgrace.setzeZeile(" world ymax 1.0");
	    xmgrace.setzeZeile(" xaxis label \"time [MCS]\"");
	    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace.setzeZeile(" xaxis tick major "+(maxframe-1)*deltaT/4);//50000");
	    //xmgrace.setzeZeile(" xaxis tick minor 10000");
	    xmgrace.setzeZeile(" yaxis label \"relative frequency\"");
	    xmgrace.setzeZeile(" yaxis tick major 0.2");
	    xmgrace.setzeZeile(" yaxis tick minor 0.1");

	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 legend \"SingleBonds\"");

	    xmgrace.setzeZeile(" s1 line color 2");
	    xmgrace.setzeZeile(" s1 line linestyle 1");
	    xmgrace.setzeZeile(" s1 line linewidth 1.5");
	    xmgrace.setzeZeile(" s1 legend \"DoubleBonds\"");
		 		
	    xmgrace.setzeZeile(" s2 line color 3");
	    xmgrace.setzeZeile(" s2 line linestyle 1");
	    xmgrace.setzeZeile(" s2 line linewidth 1.5");
	    xmgrace.setzeZeile(" s2 legend \"TripleBonds\"");

	    xmgrace.setzeZeile(" s3 line color 4");
	    xmgrace.setzeZeile(" s3 line linestyle 1");
	    xmgrace.setzeZeile(" s3 line linewidth 1.5");
	    xmgrace.setzeZeile(" s3 legend \"QuadBonds\"");

	    xmgrace.setzeZeile(" s4 line color 11");
	    xmgrace.setzeZeile(" s4 line linestyle 1");
	    xmgrace.setzeZeile(" s4 line linewidth 1.5");
	    xmgrace.setzeZeile(" s4 legend \"FreeEnds\"");

	    xmgrace.setzeZeile(" s5 line color 10");
	    xmgrace.setzeZeile(" s5 line linestyle 1");
	    xmgrace.setzeZeile(" s5 line linewidth 1.5");
	    xmgrace.setzeZeile(" s5 legend \"CumBonds\"");

	    xmgrace.setzeZeile(" subtitle \"N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; p=1.0; f\\sHEP,init\\N=28\"");

	    xmgrace.setzeZeile(" SAVEALL \""+dirDst+"AddedBonds_Heparin_"+FileName+".agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+dirDst+"AddedBonds_Heparin_"+FileName+".ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	    
	    try {
		      Runtime.getRuntime().exec("xmgrace -batch "+dirDst+"/Defects_Gamma_"+gamma+".batch -nosafe -hardcopy");
		    } catch (Exception e) {
		      System.err.println(e.toString());
		    }
		    
		    NumberFormat nf = NumberFormat.getInstance(Locale.US);

		    nf.setMaximumFractionDigits(2);
		    
		    BFMFileSaver xmgrace2 = new BFMFileSaver();
			xmgrace2.DateiAnlegen(dirDst+"/HeparinFunctionality_Gamma_"+gamma+".batch", false);
			xmgrace2.setzeZeile("# functionality relativeFrequency");
			xmgrace2.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
		    xmgrace2.setzeZeile("FOCUS G0");
		    xmgrace2.setzeZeile(" AUTOSCALE ONREAD None");
		    xmgrace2.setzeZeile("READ NXY \""+dirDst+"HeparinFunctionality_HepPEGConnectedGel_"+FileName+".dat\"");
		    xmgrace2.setzeZeile(" world xmin 0");
		    xmgrace2.setzeZeile(" world xmax "+28);//225000");
		    xmgrace2.setzeZeile(" world ymin 0");
		    xmgrace2.setzeZeile(" world ymax 1.0");
		    xmgrace2.setzeZeile(" xaxis label \"chemical functionality f\"");
		    xmgrace2.setzeZeile(" xaxis TICK MAJOR on");
		    xmgrace2.setzeZeile(" xaxis TICK MINOR on");
		    xmgrace2.setzeZeile(" xaxis tick major 5");//50000");
		    xmgrace2.setzeZeile(" xaxis tick minor 1");
		    xmgrace2.setzeZeile(" yaxis label \"relative frequency\"");
		    xmgrace2.setzeZeile(" yaxis tick major 0.1");
		    xmgrace2.setzeZeile(" yaxis tick minor 0.02");

		    xmgrace2.setzeZeile(" s0 line color 1");
		    xmgrace2.setzeZeile(" s0 line linestyle 1");
		    xmgrace2.setzeZeile(" s0 line linewidth 1.5");
		    xmgrace2.setzeZeile(" s0 symbol 1");
		    xmgrace2.setzeZeile(" s0 legend \"<f>="+nf.format(DurchschnittsfunktionalitaetHEP)+"\"");

		    xmgrace2.setzeZeile(" s1 off");
		    
		   
		    
		    
		    xmgrace2.setzeZeile(" subtitle \"N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; p=1.0; f\\sHEP,init\\N=28; f\\s0\\N="+nf.format((4.0*NrOfStars/NrOfHeparin))+"; f\\s0,Cross\\N="+nf.format((4.0*NrOfStars*minCrosslinks/NrOfHeparin))+"; Crosslinks: "+nf.format(minCrosslinks)+"\\%\"");

		    xmgrace2.setzeZeile(" SAVEALL \""+dirDst+"HeparinFunctionality_"+FileName+".agr\"");

		    xmgrace2.setzeZeile(" PRINT TO \""+dirDst+"HeparinFunctionality_"+FileName+".ps\"");
		    xmgrace2.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
		    xmgrace2.setzeZeile("PRINT");
			
		    xmgrace2.DateiSchliessen();
		    
		    try {
			      Runtime.getRuntime().exec("xmgrace -batch "+dirDst+"/HeparinFunctionality_Gamma_"+gamma+".batch -nosafe -hardcopy");
			    } catch (Exception e) {
			      System.err.println(e.toString());
			    }
		    
		/*System.out.println();
		int pegs = 630;
		for(int u = 0; u < NrOfHeparin; u++)
		{
			if(HeparinSterneVerknuepfung[u][pegs-1] != 0)
				System.out.print((u+1) + "["+HeparinSterneVerknuepfung[u][pegs-1]+"] ");
		}*/
	}
	
	protected void LoadFile(String file, int startframe, int maxframe)
	{
		//FileName = file;
		System.out.println("file : " +file );
		System.out.println("dir : " + FileDirectory);
		System.out.println("lade System");
		LadeSystem(FileDirectory, file);	
		
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(FileDirectory+file);
		  
		  System.arraycopy(importData.GetFrameOfSimulation(currentFrame),0,Polymersystem,0, Polymersystem.length);
		  
		  
			
		  importData.CloseSimulationFile();
		  
		  importData.OpenSimulationFile(FileDirectory+file);
			
		  addedBondsDuringSimulation = 0;
			System.out.println("file : " +file );
			System.out.println("dir : " + FileDirectory);
			
			int z = currentFrame;//1;
			
			/*if(FuncHeparinCalculated != true)	
			if(maxframe == -1)
		    while ( (z <= NrofFrames))
		      {

		    		playSimulation(z);
		    		z++;
		    	
		    		for(int u = 1; u <= skipFrames; u++)
		    		{
		    			z++;
		    		}

		      }
			else*/
			while ( (z <= maxframe))
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
			System.out.println("Berechnung Bindungsstatistik von PEG+HEP");
			System.out.println("USAGE: dirSrc/ Hydrogel_HEP_hep__PEG_peg_NStar_length__NoPerXYZ128[__xxx.bfm] StringGamma  dirDst/");
		}
		else new Auswertung_Heparin_Funktionality_HepPEG(args[0], args[1], args[2],args[3], Float.parseFloat(args[4]));//,args[1],args[2]);
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
		
		HeparinSterneVerknuepfung = null;
		HeparinSterneVerknuepfung = new int[NrOfHeparin][NrOfStars];
		
		PEGBonds = new int[NrOfStars];
		
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
				importData.GetFrameOfSimulation(frame);
				
				
				for(int it = 0; it < importData.addedBondsBetweenFrames.size(); it++)
					//if(FuncHeparinCalculated != true)
				{
					long bondobj = importData.addedBondsBetweenFrames.get(it);
					//System.out.println(it + " bond " + bondobj);
					
					int a = getMono1Nr(bondobj);
					int b = getMono2Nr(bondobj);
					
					System.out.println("a: " +a + "    b: " + b);
					
					if(a < b) //a=Heparin, b=Peg-Stern
					{
						HeparinSterneVerknuepfung[(a-1)/90][(b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
					}
					else //b=Heparin, a=Peg-Stern
					{
						HeparinSterneVerknuepfung[(b-1)/90][(a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
					}
					
					addedBondsDuringSimulation++;
					
					if((addedBondsDuringSimulation/(4.0*NrOfStars) >= minCrosslinks) && (FuncHeparinCalculated == false))
					{
						int counterSinglebonds = 0;
						int counterDoublebonds = 0;
						int counterTriplebonds = 0;
						int counterQuadbonds = 0;
						
						for(int i = 0; i < NrOfHeparin; i++)
							for(int j = 0; j < NrOfStars; j++)
							{
								if(HeparinSterneVerknuepfung[i][j] == 1)
									counterSinglebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 2)
									counterDoublebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 3)
									counterTriplebonds++;
								else if (HeparinSterneVerknuepfung[i][j] == 4)
									counterQuadbonds++;
							}
						
						for(int hep = 0; hep < NrOfHeparin; hep++)
						{
							int counterFunktionalitaet = 0;
							for(int u = 0; u < NrOfStars; u++)
							{
								if(HeparinSterneVerknuepfung[hep][u] != 0)
									counterFunktionalitaet+=HeparinSterneVerknuepfung[hep][u];
							}
							
							if(counterFunktionalitaet == 0)
								FunktionalitaetHeparin[0]++;
							else
								FunktionalitaetHeparin[counterFunktionalitaet]++;
						}
						
						FuncHeparinCalculated = true;
						
						
					}
					
					
				}
				
				int counterSinglebonds = 0;
				int counterDoublebonds = 0;
				int counterTriplebonds = 0;
				int counterQuadbonds = 0;
				int counterDenglingEnds = 0;
				
				for(int i = 0; i < NrOfHeparin; i++)
					for(int j = 0; j < NrOfStars; j++)
					{
						if(HeparinSterneVerknuepfung[i][j] == 1)
							counterSinglebonds++;
						else if (HeparinSterneVerknuepfung[i][j] == 2)
							counterDoublebonds++;
						else if (HeparinSterneVerknuepfung[i][j] == 3)
							counterTriplebonds++;
						else if (HeparinSterneVerknuepfung[i][j] == 4)
							counterQuadbonds++;
						else if (HeparinSterneVerknuepfung[i][j] > 4)
						{
							System.out.println("Verknuepfung 5! Sollte nicht vorkommen! Wert: " +HeparinSterneVerknuepfung[i][j] + " bei Hep:" + (i+1) + "   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
							System.exit(1); 
						}
					}
				
				for(int j = 0; j < NrOfStars; j++)
				{
					//if(PEGBonds[j] == 0)
						counterDenglingEnds += 4-PEGBonds[j];
						
					 if(PEGBonds[j] > 4)
					 {
							System.out.println("Verknuepfung 5! Sollte nicht vorkommen!   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
							System.exit(1); 
					 }
				}
				
				Singlebonds[(int) (importData.MCSTime/(deltaT))].AddValue(counterSinglebonds/(4.0*NrOfStars));
				Doublebonds[(int) (importData.MCSTime/(deltaT))].AddValue(2.0*counterDoublebonds/(4.0*NrOfStars));
				Triplebonds[(int) (importData.MCSTime/(deltaT))].AddValue(3.0*counterTriplebonds/(4.0*NrOfStars));
				Quadbonds[(int) (importData.MCSTime/(deltaT))].AddValue(4.0*counterQuadbonds/(4.0*NrOfStars));
				
				DenglingEnds[(int) (importData.MCSTime/(deltaT))].AddValue((4.0*NrOfStars-counterSinglebonds-2.0*counterDoublebonds-3.0*counterTriplebonds-4.0*counterQuadbonds)/(4.0*NrOfStars));
				
				DenglingEnds2[(int) (importData.MCSTime/(deltaT))].AddValue((counterDenglingEnds)/(4.0*NrOfStars));
				
				
				bonds[(int) (importData.MCSTime/(deltaT))].AddValue(importData.additionalbonds.size());
				//System.out.println("size: " +bonds[(int) (importData.MCSTime/(10000))].ReturnN());
				System.out.println("size: " +importData.addedBondsBetweenFrames.size());
				importData.addedBondsBetweenFrames.clear();
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
	   
}
