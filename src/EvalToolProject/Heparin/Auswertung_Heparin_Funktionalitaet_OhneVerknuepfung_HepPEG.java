package EvalToolProject.Heparin;
import java.text.DecimalFormat;

import EvalToolProject.tools.BFMFileSaver;
import EvalToolProject.tools.BFMImportData;
import EvalToolProject.tools.Int_IntArrayList_Hashtable;
import EvalToolProject.tools.Statistik;



public class Auswertung_Heparin_Funktionalitaet_OhneVerknuepfung_HepPEG {


	
	
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

	
	
	
	Statistik durchschnittbond;
	
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	int FunktionalitaetHeparin[];
	BFMFileSaver FunktionalitaetHeparinSaver;
	

	
	
	public Auswertung_Heparin_Funktionalitaet_OhneVerknuepfung_HepPEG(String fdir, String fname, String gamma, String chemFktRatio)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		
		FunktionalitaetHeparin = new int[29];
		
		
		
		
		
		
		DecimalFormat dh = new DecimalFormat("000");
		
		for(int i = 1; i <= 26; i++)
			LoadFile(FileName+"__"+dh.format(i)+".bfm", 1);
		
		//for(int i = 16; i <= 26; i++)
		//	LoadFile(FileName+"__"+dh.format(i)+".bfm", 1, maxframe);
		
		
		//LoadFile(FileName+"__"+dh.format(10)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(11)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(16)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(21)+".bfm", 1);
		//LoadFile(FileName+"__"+dh.format(26)+".bfm", 1);
		
		
		FunktionalitaetHeparinSaver = new BFMFileSaver();
		//FunktionalitaetHeparinSaver.DateiAnlegen("/home/users/dockhorn/Simulationen/HEPPEGConnectedGel/Auswertung_Gamma_"+gamma+"/HeparinFunctionality_HepPEGConnectedGel_"+FileName+".dat", false);
		FunktionalitaetHeparinSaver.DateiAnlegen(fdir+"/HeparinFunctionality_Distribution_chemFct_"+chemFktRatio+"__"+FileName+".dat", false);
		
		
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
		
		
		//System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1());
		
		
		
		
		/*System.out.println();
		int pegs = 630;
		for(int u = 0; u < NrOfHeparin; u++)
		{
			if(HeparinSterneVerknuepfung[u][pegs-1] != 0)
				System.out.print((u+1) + "["+HeparinSterneVerknuepfung[u][pegs-1]+"] ");
		}*/
		
		BFMFileSaver xmgrace = new BFMFileSaver();
		xmgrace.DateiAnlegen(fdir+"/HeparinFunctionality_Distribution_chemFct_"+chemFktRatio+"__"+FileName+".batch", false);
		//xmgrace.setzeZeile("# t[MCS] <CumBond>  <CumBond/maxBond> d<CumBond>");
		xmgrace.setzeZeile("arrange (1,1,.1,.6,.6,ON,ON,ON)");
	    xmgrace.setzeZeile("FOCUS G0");
	    xmgrace.setzeZeile(" AUTOSCALE ONREAD None");
	    xmgrace.setzeZeile("READ NXY \""+fdir+"/HeparinFunctionality_Distribution_chemFct_"+chemFktRatio+"__"+FileName+".dat\"");
	    xmgrace.setzeZeile(" world xmin 0");
	    xmgrace.setzeZeile(" world xmax 30");
	    xmgrace.setzeZeile(" world ymin 0");
	    xmgrace.setzeZeile(" world ymax 1.0");
	    xmgrace.setzeZeile(" xaxis label \"chem. functionality\"");
	    xmgrace.setzeZeile(" xaxis TICK MAJOR on");
	    xmgrace.setzeZeile(" xaxis TICK MINOR on");
	    xmgrace.setzeZeile(" xaxis tick major 5");
	    xmgrace.setzeZeile(" xaxis tick minor 1");
	    //xmgrace.setzeZeile(" xaxis tick minor 10000");
	    xmgrace.setzeZeile(" yaxis label \"relative frequency\"");
	    xmgrace.setzeZeile(" yaxis tick major 0.2");
	    xmgrace.setzeZeile(" yaxis tick minor 0.1");

	    xmgrace.setzeZeile(" s0 line color 1");
	    xmgrace.setzeZeile(" s0 line linestyle 1");
	    xmgrace.setzeZeile(" s0 line linewidth 1.5");
	    xmgrace.setzeZeile(" s0 legend \"F="+chemFktRatio.replace('_', '.')+"\"");

	    xmgrace.setzeZeile(" G0.S1 HIDDEN TRUE");

	   /* xmgrace.setzeZeile(" s1 line color 2");
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

	    */
	    xmgrace.setzeZeile(" subtitle \"N\\sHEP\\N="+NrOfHeparin+"; N\\sStars\\N="+NrOfStars+"; \\f{Symbol}g\\f{}="+gamma.replace('_', '.')+"; p=1.0; F="+chemFktRatio.replace('_', '.')+"; <f>="+(Double.parseDouble(chemFktRatio.replace('_', '.'))*NrOfStars*4.0/NrOfHeparin)+"\"");

	    xmgrace.setzeZeile(" SAVEALL \""+fdir+"/AddedBonds_Heparin_"+FileName+".agr\"");

	    xmgrace.setzeZeile(" PRINT TO \""+fdir+"/AddedBonds_Heparin_"+FileName+".ps\"");
	    xmgrace.setzeZeile("DEVICE \"EPS\" OP \"level2\"");
	    xmgrace.setzeZeile("PRINT");
		
	    xmgrace.DateiSchliessen();
	    
	    try {
		      Runtime.getRuntime().exec("xmgrace -batch "+fdir+"/HeparinFunctionality_Distribution_chemFct_"+chemFktRatio+"__"+FileName+".batch -nosafe -hardcopy");
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
		  
		  System.arraycopy(importData.GetFrameOfSimulation(currentFrame),0,Polymersystem,0, Polymersystem.length);
		  
		  
			
		  importData.CloseSimulationFile();
		  
		 /* importData.OpenSimulationFile(FileDirectory+file);
			
		  
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
			*/
			
			
			
			
			//Ausgabe Datei mit Verteilungsstatistik der chem. aktiven Gruppen
			int HEP[] = new int[NrOfHeparin];
			
			for(int mono = 1; mono <= 90*NrOfHeparin; mono++)
			{
				if(importData.Attributes[mono] == 1) //COOH-Gruppe
					HEP[(mono-1)/90]++;
			}
			
			
			
			for(int hep = 0; hep < NrOfHeparin; hep++)
			{
				FunktionalitaetHeparin[HEP[hep]]++;
			}
			
			
			
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Berechnung Funktionalität von Heparin aus Verteilung der chem. aktiven Gruppen");
		System.out.println("USAGE: dir/ Hydrogel_HEP_hep__PEG_peg_NStar_length__NoPerXYZ128__F_a_bb[__xxx.bfm] StringGamma StringChemFunctionalityRatio");
		if((args.length == 4 ))
		new Auswertung_Heparin_Funktionalitaet_OhneVerknuepfung_HepPEG(args[0], args[1], args[2], args[3]);//,args[1],args[2]);
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
