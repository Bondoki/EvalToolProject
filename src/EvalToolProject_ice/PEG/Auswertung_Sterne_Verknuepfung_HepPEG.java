package EvalToolProject_ice.PEG;
import java.text.DecimalFormat;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Sterne_Verknuepfung_HepPEG {


	
	
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
	
	BFMFileSaver rg;
	BFMFileSaver BondSaver;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	int FunktionalitaetHeparin[];
	BFMFileSaver FunktionalitaetHeparinSaver;
	
	//relative frequency of bond types at special cross-links
	Statistik SingleBondsAtGamma95;
	Statistik DoubleBondsAtGamma95;
	Statistik TripleBondsAtGamma95;
	Statistik QuadBondsAtGamma95;
	
	BFMFileSaver SaverBondsAtGamma95;
	
	Statistik SingleBondsAtGamma75;
	Statistik DoubleBondsAtGamma75;
	Statistik TripleBondsAtGamma75;
	Statistik QuadBondsAtGamma75;
	
	BFMFileSaver SaverBondsAtGamma75;
	
	int addedBondsDuringSimulation;
	
	long deltaT;
	
	public Auswertung_Sterne_Verknuepfung_HepPEG(String fdir, String fname, String gamma, String dirDst,  String Experiment, int nrOfFiles)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(FileDirectory+ fname+"__001.bfm");
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		//End - Determine MaxFrame out of the first file
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		rg = new BFMFileSaver();
		//rg.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.DateiAnlegen(dirDst+"/StarPEG_CumBonds_HepPEGConnectedGel_"+FileName+".dat", false);
		rg.setzeZeile("# t[MCS] <CumBond>  <CumBond/maxBond> d<CumBond>");
		
		BondSaver = new BFMFileSaver();
		//BondSaver.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/StarPEG_Bonds_HepPEGConnectedGel_"+FileName+".dat", false);
		BondSaver.DateiAnlegen(dirDst+"/StarPEG_Bonds_HepPEGConnectedGel_"+FileName+".dat", false);
		BondSaver.setzeZeile("# t[MCS] <Single>  <Double> <Triple> <Quad> <Free> <CumBonds> Sum=1");
		
		FunktionalitaetHeparinSaver = new BFMFileSaver();
		//FunktionalitaetHeparinSaver.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/HeparinFunctionality_HepPEGConnectedGel_"+FileName+".dat", false);
		FunktionalitaetHeparinSaver.DateiAnlegen(dirDst+"/HeparinFunctionality_HepPEGConnectedGel_"+FileName+".dat", false);
		
		
		SaverBondsAtGamma95 = new BFMFileSaver();
		//SaverBondsAtGamma95.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung/defects_gamma_0_95conversion.dat", true);
		SaverBondsAtGamma95.DateiAnlegen(dirDst+"/defects_gamma_0_95conversion.dat", true);
		//SaverBondsAtGamma95.setzeZeile("# gamma <Single>  <Double> <Triple> <Quad>");
		
		SaverBondsAtGamma75 = new BFMFileSaver();
		//SaverBondsAtGamma75.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung/defects_gamma_0_75conversion.dat", true);
		SaverBondsAtGamma75.DateiAnlegen(dirDst+"/defects_gamma_0_75conversion.dat", true);
		//SaverBondsAtGamma75.setzeZeile("# gamma <Single>  <Double> <Triple> <Quad>");
		
		
		FunktionalitaetHeparin = new int[29];
		
		
		SingleBondsAtGamma95 = new Statistik();
		DoubleBondsAtGamma95 = new Statistik();
		TripleBondsAtGamma95 = new Statistik();
		QuadBondsAtGamma95 = new Statistik();
		
		SingleBondsAtGamma75 = new Statistik();
		DoubleBondsAtGamma75 = new Statistik();
		TripleBondsAtGamma75 = new Statistik();
		QuadBondsAtGamma75 = new Statistik();
		
		
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
		
		
		
		DecimalFormat dh = new DecimalFormat("000");
		
		//LoadFile(FileName+"__"+dh.format(11)+".bfm", 1, maxframe);
		//LoadFile(FileName+"__"+dh.format(12)+".bfm", 1, maxframe);
		//for(int i = 1; i <= 26; i++)
		for(int i = 1; i <= nrOfFiles; i+=1)//i++)
			LoadFile(FileName+"__"+dh.format(i)+".bfm", 1, maxframe);
		
		
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
		for(int i=0; i <=maxframe; i++)
		rg.setzeZeile((deltaT*i) + " " + bonds[i].ReturnM1()+" "+(bonds[i].ReturnM1()/(4.0*NrOfStars))+" "+( 2.0* bonds[i].ReturnSigma()/Math.sqrt(17))+ " " + 1.0*bonds[i].ReturnN() );
		 
		rg.DateiSchliessen();
		
		for(int i=0; i <=maxframe; i++)
			BondSaver.setzeZeile((deltaT*i) + " " + Singlebonds[i].ReturnM1()+ " " + Doublebonds[i].ReturnM1()+ " " + Triplebonds[i].ReturnM1()+ " " + Quadbonds[i].ReturnM1()+ " " + DenglingEnds[i].ReturnM1() +" " +(bonds[i].ReturnM1()/(4.0*NrOfStars))+" " +(Singlebonds[i].ReturnM1()+ Doublebonds[i].ReturnM1()+ Triplebonds[i].ReturnM1()+  Quadbonds[i].ReturnM1()+ DenglingEnds[i].ReturnM1() ));
		
			//BondSaver.setzeZeile((10000*i) + " " + Singlebonds[i].ReturnM1()+ " " + Doublebonds[i].ReturnM1()+ " " + Triplebonds[i].ReturnM1()+ " " + Quadbonds[i].ReturnM1()+ " " + DenglingEnds[i].ReturnM1() +" "+DenglingEnds2[i].ReturnM1() +" " +(Singlebonds[i].ReturnM1()+ Doublebonds[i].ReturnM1()+ Triplebonds[i].ReturnM1()+  Quadbonds[i].ReturnM1()+ DenglingEnds[i].ReturnM1() ));
			 
		BondSaver.DateiSchliessen();
		
		SaverBondsAtGamma95.setzeZeile((gamma.replace('_', '.')) + " " + SingleBondsAtGamma95.ReturnM1()+ " " + DoubleBondsAtGamma95.ReturnM1()+ " " + TripleBondsAtGamma95.ReturnM1()+ " " + QuadBondsAtGamma95.ReturnM1());
		SaverBondsAtGamma95.DateiSchliessen();
		
		SaverBondsAtGamma75.setzeZeile((gamma.replace('_', '.')) + " " + SingleBondsAtGamma75.ReturnM1()+ " " + DoubleBondsAtGamma75.ReturnM1()+ " " + TripleBondsAtGamma75.ReturnM1()+ " " + QuadBondsAtGamma75.ReturnM1());
		SaverBondsAtGamma75.DateiSchliessen();
		
		
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

	    xmgrace.setzeZeile(" subtitle \""+Experiment+"; N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; p=1.0; f\\sHEP,init\\N=28\"");

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
			else
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
			
			
			
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 6)
		{
			System.out.println("Berechnung Bindungsstatistik von PEG+HEP");
			System.out.println("USAGE: dirSrc/ Hydrogel_HEP_hep__PEG_peg_NStar_length__NoPerXYZ128[__xxx.bfm] StringGamma dirDst/ StringExperiment Files");
		}
		else new Auswertung_Sterne_Verknuepfung_HepPEG(args[0], args[1], args[2] ,args[3], args[4], Integer.parseInt(args[5]));//,args[1],args[2]);
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
					
					if((addedBondsDuringSimulation/(4.0*NrOfStars) >= 0.74) && (addedBondsDuringSimulation/(4.0*NrOfStars) <= 0.76))
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
						
						SingleBondsAtGamma75.AddValue(counterSinglebonds/(4.0*NrOfStars));
						DoubleBondsAtGamma75.AddValue(2.0*counterDoublebonds/(4.0*NrOfStars));
						TripleBondsAtGamma75.AddValue(3.0*counterTriplebonds/(4.0*NrOfStars));
						QuadBondsAtGamma75.AddValue(4.0*counterQuadbonds/(4.0*NrOfStars));
					}
					
					if((addedBondsDuringSimulation/(4.0*NrOfStars) >= 0.94) && (addedBondsDuringSimulation/(4.0*NrOfStars) <= 0.96))
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
						
						SingleBondsAtGamma95.AddValue(counterSinglebonds/(4.0*NrOfStars));
						DoubleBondsAtGamma95.AddValue(2.0*counterDoublebonds/(4.0*NrOfStars));
						TripleBondsAtGamma95.AddValue(3.0*counterTriplebonds/(4.0*NrOfStars));
						QuadBondsAtGamma95.AddValue(4.0*counterQuadbonds/(4.0*NrOfStars));
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
