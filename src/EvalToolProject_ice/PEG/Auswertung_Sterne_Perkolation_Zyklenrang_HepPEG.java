package EvalToolProject_ice.PEG;
import java.text.DecimalFormat;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.HistogrammStatistik;
import EvalToolProject_ice.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Sterne_Perkolation_Zyklenrang_HepPEG {


	
	
	String FileName;
	String FileDirectory;
	
	int[] Polymersystem;
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
	
	
	
	BFMImportData importData;
	
	int NrOfHeparin = 0;
	int NrOfStars = 0;
	int NrOfMonomersPerStarArm = 0;
	
	int[] dumpsystem;

	int ColorHeparin[];
	int ColorStars[];
	int StarClusterSize[];

	
	
	Statistik durchschnittbond;
	
	
	
	int[] PEGBonds;
	
	
	BFMFileSaver StarSaver;
	BFMFileSaver StarSaverNormalized;
	
	BFMFileSaver StarSaverLinear;
	
	BFMFileSaver SaverZyklenrangReduziert;
	BFMFileSaver SaverZyklenrangGraph;
	
	BFMFileSaver SaverZyklenrangReduziertNormiert;
	BFMFileSaver SaverZyklenrangGraphNormiert;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	HistogrammStatistik CrosslinksVsCluster;
	HistogrammStatistik CrosslinksVsCluster_LinearBonds; //Clusterverteilung aus singelBonds
	
	HistogrammStatistik HSZyklenrangReduziert;	//elastische Zyklenrang
	HistogrammStatistik HSZyklenrangGraph;	//mathematischer Zyklenrang
	
	HistogrammStatistik HSZyklenrangReduziertNormiert;	//elastische Zyklenrang normiert zur Laufzeit durch groesstes Cluster
	HistogrammStatistik HSZyklenrangGraphNormiert;	//mathematischer Zyklenrang normiert zur Laufzeit durch groesstes Cluster
	
	int[][] HeparinSterneVerknuepfung;
	int counterSinglebonds;
	
	int counterAdditionalBonds;
	
	
	
	public Auswertung_Sterne_Perkolation_Zyklenrang_HepPEG(String dstDir, String fdir, String fname, String gamma)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		CrosslinksVsCluster = new HistogrammStatistik(-0.01,1.01,102);//51);
		CrosslinksVsCluster_LinearBonds = new HistogrammStatistik(-0.01,1.01,102);//51);;
		
		HSZyklenrangReduziert = new HistogrammStatistik(-0.01,1.01,102);
		HSZyklenrangGraph = new HistogrammStatistik(-0.01,1.01,102);
		
		HSZyklenrangReduziertNormiert= new HistogrammStatistik(-0.01,1.01,102);
		HSZyklenrangGraphNormiert= new HistogrammStatistik(-0.01,1.01,102);
		
		StarSaverNormalized = new BFMFileSaver();
		StarSaverNormalized.DateiAnlegen(dstDir+"Auswertung_Gamma_"+gamma+"/Percolation_StarPEG_HepPEGConnectedGel_"+FileName+".dat", false);
		StarSaverNormalized.setzeZeile("# functionality <NrOfStars/AllStars>");
		
		StarSaver = new BFMFileSaver();
		StarSaver.DateiAnlegen(dstDir+"Auswertung_Gamma_"+gamma+"/PercolationGammaEff_StarPEG_HepPEGConnectedGel_"+FileName+".dat", false);
		StarSaver.setzeZeile("# GammaEff <NrOfStars/AllStars>");
		
		StarSaverLinear = new BFMFileSaver();
		StarSaverLinear.DateiAnlegen(dstDir+"/Auswertung_Gamma_"+gamma+"/PercolationSingleBonds_StarPEG_HepPEGConnectedGel_"+FileName+".dat", false);
		StarSaverLinear.setzeZeile("# functionality(singlebonds) <NrOfStars/AllStars>");
		
		
		SaverZyklenrangReduziert = new BFMFileSaver();
		SaverZyklenrangReduziert.DateiAnlegen(dstDir+"Auswertung_Gamma_"+gamma+"/ZyklenrangReduziert_StarPEG_HepPEGConnectedGel_"+FileName+".dat", false);
		SaverZyklenrangReduziert.setzeZeile("# functionality <zeta_el = Kanten_el-(HEP+PEG)+1>");
		
		SaverZyklenrangGraph = new BFMFileSaver();
		SaverZyklenrangGraph.DateiAnlegen(dstDir+"Auswertung_Gamma_"+gamma+"/ZyklenrangGraph_StarPEG_HepPEGConnectedGel_"+FileName+".dat", false);
		SaverZyklenrangGraph.setzeZeile("# functionality <zeta_graph = Kanten_graph-(HEP+PEG)+1>");
		
		SaverZyklenrangReduziertNormiert = new BFMFileSaver();
		SaverZyklenrangReduziertNormiert.DateiAnlegen(dstDir+"Auswertung_Gamma_"+gamma+"/ZyklenrangReduziertNormiert_StarPEG_HepPEGConnectedGel_"+FileName+".dat", false);
		SaverZyklenrangReduziertNormiert.setzeZeile("# normierter Zyklenrang zur Laufzeit durch groessten Cluster");
		SaverZyklenrangReduziertNormiert.setzeZeile("# functionality <zeta_el = Kanten_el-(HEP+PEG)+1>/zeta_ideal");
		
		SaverZyklenrangGraphNormiert = new BFMFileSaver();
		SaverZyklenrangGraphNormiert.DateiAnlegen(dstDir+"Auswertung_Gamma_"+gamma+"/ZyklenrangGraphNormiert_StarPEG_HepPEGConnectedGel_"+FileName+".dat", false);
		SaverZyklenrangGraphNormiert.setzeZeile("# normierter Zyklenrang zur Laufzeit durch groessten Cluster");
		SaverZyklenrangGraphNormiert.setzeZeile("# functionality <zeta_graph = Kanten_graph-(HEP+PEG)+1>/zeta_ideal");
		
		
		
		durchschnittbond = new Statistik();
		
		
		
		DecimalFormat dh = new DecimalFormat("000");
		
		for(int i = 1; i <= 26; i++)
		{
			counterAdditionalBonds = 0;
			LoadFile(FileName+"__"+dh.format(i)+".bfm", 1);
			
		}
		
		/*counterAdditionalBonds = 0;
		LoadFile(FileName+"__"+dh.format(11)+".bfm", 1);counterAdditionalBonds = 0;
		LoadFile(FileName+"__"+dh.format(16)+".bfm", 1);counterAdditionalBonds = 0;
		LoadFile(FileName+"__"+dh.format(21)+".bfm", 1);counterAdditionalBonds = 0;
		LoadFile(FileName+"__"+dh.format(26)+".bfm", 1);
		*/
		
		
		for(int i = 0; i < CrosslinksVsCluster.GetNrBins(); i++)
		{
			StarSaverNormalized.setzeZeile(CrosslinksVsCluster.GetRangeInBin(i)+" "+CrosslinksVsCluster.GetAverageInBin(i));
		}
		StarSaverNormalized.DateiSchliessen();
		
		for(int i = 0; i < CrosslinksVsCluster.GetNrBins(); i++)
		{
			StarSaver.setzeZeile(CrosslinksVsCluster.GetRangeInBin(i)*(NrOfStars/(1.0*NrOfHeparin))+" "+CrosslinksVsCluster.GetAverageInBin(i));
		}
		StarSaver.DateiSchliessen();
		
		for(int i = 0; i < CrosslinksVsCluster_LinearBonds.GetNrBins(); i++)
		{
			StarSaverLinear.setzeZeile(CrosslinksVsCluster_LinearBonds.GetRangeInBin(i)+" "+CrosslinksVsCluster_LinearBonds.GetAverageInBin(i));
		}
		StarSaverLinear.DateiSchliessen();
		
		//Ausgabe Zyklenrang
		
		for(int i = 0; i < HSZyklenrangReduziert.GetNrBins(); i++)
		{
			SaverZyklenrangReduziert.setzeZeile(HSZyklenrangReduziert.GetRangeInBin(i)+" "+HSZyklenrangReduziert.GetAverageInBin(i));
		}
		SaverZyklenrangReduziert.DateiSchliessen();
		
		for(int i = 0; i < HSZyklenrangGraph.GetNrBins(); i++)
		{
			SaverZyklenrangGraph.setzeZeile(HSZyklenrangGraph.GetRangeInBin(i)+" "+HSZyklenrangGraph.GetAverageInBin(i));
		}
		SaverZyklenrangGraph.DateiSchliessen();
		
		//Ausgabe Zyklenrang normiert
		
		for(int i = 0; i < HSZyklenrangReduziertNormiert.GetNrBins(); i++)
		{
			SaverZyklenrangReduziertNormiert.setzeZeile(HSZyklenrangReduziertNormiert.GetRangeInBin(i)+" "+HSZyklenrangReduziertNormiert.GetAverageInBin(i));
		}
		SaverZyklenrangReduziertNormiert.DateiSchliessen();
		
		for(int i = 0; i < HSZyklenrangGraphNormiert.GetNrBins(); i++)
		{
			SaverZyklenrangGraphNormiert.setzeZeile(HSZyklenrangGraphNormiert.GetRangeInBin(i)+" "+HSZyklenrangGraphNormiert.GetAverageInBin(i));
		}
		SaverZyklenrangGraphNormiert.DateiSchliessen();
		
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1());
		
		
		
		
		
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
		  
		  InitArrays();
		  
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
	
	 
	  private void InitArrays()
	  {
		  HeparinSterneVerknuepfung = null;
		  HeparinSterneVerknuepfung = new int[NrOfHeparin][NrOfStars];
		  
		  ColorHeparin = new int[NrOfHeparin+1];
		  ColorStars = new int[NrOfStars+1];
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  ColorStars[i]=i;
		  
		  for(int i = 0; i <= NrOfHeparin; i++)
			  ColorHeparin[i]=-i;
		  
		  StarClusterSize = new int[NrOfStars+1]; //Anzahl Sterne im Cluster
		  
		  for(int i = 0; i <= NrOfStars; i++)
			  StarClusterSize[i]=1; 	//Anzahl der Bindungen - Summe ergibt 4*NrOfStars auch nach Perkolation
	  }
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 4)
		{
			System.out.println("Berechnung Perkolation und Clustergroesse von Sternen im PEG-HEP-Netzwerk (nur von PEG)");
			System.out.println("USAGE: dirdst/ dirsrc/ Hydrogel_HEP_hep__PEG_peg_NStar_length__NoPerXYZ128[__xxx.bfm] StringGamma");
			System.exit(1);
		}
		System.out.println("Berechnung Perkolation und Clustergroesse von Sternen im PEG-HEP-Netzwerk (nur von PEG)");
		new Auswertung_Sterne_Perkolation_Zyklenrang_HepPEG(args[0], args[1], args[2], args[3]);//,args[1],args[2]);
	}
	
	public void LadeSystem(String FileDir, String FileName)
	{
		importData=null;
		importData = new BFMImportData(FileDir+FileName);
		
		MONOMERZAHL = importData.NrOfMonomers + 1;
		
		
		
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
					
					int HEPNumber = 0;
					int StarNumber = 0;
					
					if(a < b) //a=Heparin, b=Peg-Stern
					{
						HEPNumber = ((a-1)/90)+1;
						StarNumber = ((b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1))+1;
							
						HeparinSterneVerknuepfung[(a-1)/90][(b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(b-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
					}
					else //b=Heparin, a=Peg-Stern
					{
						HEPNumber = ((b-1)/90)+1;
						StarNumber = ((a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1))+1;
						
						HeparinSterneVerknuepfung[(b-1)/90][(a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
						PEGBonds[(a-90*NrOfHeparin-1)/(4*NrOfMonomersPerStarArm+1)]++;
					}
					
					if((HEPNumber <= 0) || (HEPNumber > NrOfHeparin))
					{
						System.out.println("Fehler bei Heparin-Indizierung: " + HEPNumber +" von "+ NrOfHeparin );
						System.exit(1);
					}
					
					if((StarNumber <= 0) || (StarNumber > NrOfStars))
					{
						System.out.println("Fehler bei StarPEG-Indizierung: " + StarNumber +" von "+ NrOfStars );
						System.exit(1);
					}
					
					
					
					
					/*
					 * Bestimmung der SingleBonds
					 */
					
					int counterSinglebondsTmp = 0;
					int counterDoublebondsTmp = 0;
					int counterTriplebondsTmp = 0;
					int counterQuadbondsTmp = 0;
					for(int i = 0; i < NrOfHeparin; i++)
						for(int j = 0; j < NrOfStars; j++)
						{
							if(HeparinSterneVerknuepfung[i][j] == 1)
								counterSinglebondsTmp++;
							else if (HeparinSterneVerknuepfung[i][j] == 2)
								counterDoublebondsTmp++;
							else if (HeparinSterneVerknuepfung[i][j] == 3)
								counterTriplebondsTmp++;
							else if (HeparinSterneVerknuepfung[i][j] == 4)
								counterQuadbondsTmp++;
							
							else if (HeparinSterneVerknuepfung[i][j] > 4)
							{
								System.out.println("Verknuepfung 5! Sollte nicht vorkommen! Wert: " +HeparinSterneVerknuepfung[i][j] + " bei Hep:" + (i+1) + "   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
								System.exit(1); 
							}
						}
					
					
					/*
					 * Perkolationsalgorithmus
					 */
					
					//ColorHeparin -> init -HEPnr, später: + PEGnr
					
					if(ColorHeparin[HEPNumber] < 0)	//Heparin hat noch keine Verbindung
					{
						ColorHeparin[HEPNumber] = ColorStars[StarNumber];
					}
					else if (ColorHeparin[HEPNumber] != ColorStars[StarNumber]) //Verbindung zu Stern(cluster), wo noch keine war
					{
						//Cluster von Heparin kleiner/gleich dem vom Stern 
						//Umfaerben des HEP-Clustes zum Sternclusters
						if((StarClusterSize[ColorHeparin[HEPNumber]] <= StarClusterSize[ColorStars[StarNumber]]  ))  
						{
							int oldColor = ColorHeparin[HEPNumber];
							int newColor = ColorStars[StarNumber];
							
							for(int i = 1; i <= NrOfHeparin; i++)
							{
								if(ColorHeparin[i] == oldColor)
								{
									ColorHeparin[i] = newColor;
								}
							}
							
							int counter = 0;
							for(int i = 1; i <= NrOfStars; i++)
							{
								if(ColorStars[i] == oldColor)
								{
									counter+=StarClusterSize[i];
									ColorStars[i]=newColor;
								}
							}
							
							StarClusterSize[oldColor]=0;
							StarClusterSize[newColor]+=counter;
							
						}
						else //neue Verbindung zum groesseren Cluster
						{   //Cluster von Heparin groesser als der vom Stern 
							//Umfaerben des Stern-Clustes zum Heparinclusters
							
							int oldColor = ColorStars[StarNumber];
							int newColor = ColorHeparin[HEPNumber];
							
							for(int i = 1; i <= NrOfHeparin; i++)
							{
								if(ColorHeparin[i] == oldColor)
								{
									ColorHeparin[i] = newColor;
								}
							}
							
							int counter = 0;
							for(int i = 1; i <= NrOfStars; i++)
							{
								if(ColorStars[i] == oldColor)
								{
									counter+=StarClusterSize[i];
									ColorStars[i]=newColor;
								}
							}
							
							StarClusterSize[oldColor]=0;
							StarClusterSize[newColor]+=counter;
						}
					}
					//else if: Mehrfachbindung zum gleichen Cluster
					
					/*
					 * Ende Perkolationsalgorithmus
					 */
					
					
					/* 
					 * Zyklenrangbestimmung: Zeta = Kanten - Ecken + 1
					 */
					
					int maxColor = findColorOfMaximumCluster();
					int HeparinEcken = 0;
					int PEGEcken = 0;
					int KantenReduziert = 0;	//elastische Verbindungen
					int KantenGraph = 0;	//mathematischen Verbindungen
					
					for(int i = 1; i <= NrOfHeparin; i++)
					{
						if(ColorHeparin[i] == maxColor)
						{
							HeparinEcken++; //Heparin gehoehrt zum Cluster
						}
					}
					
					for(int i = 1; i <= NrOfStars; i++)
					{
						if(ColorStars[i] == maxColor)
						{
							PEGEcken++; //PEG gehoehrt zum Cluster
						}
					}
					
					for(int i = 0; i < NrOfHeparin; i++)
						for(int j = 0; j < NrOfStars; j++)
						{
							if(ColorHeparin[i+1] == maxColor) //Heparin gehoehrt zum Cluster
								if(ColorStars[j+1] == maxColor) //PEG gehoehrt zum Cluster
								{
									if(HeparinSterneVerknuepfung[i][j] > 0)
										KantenReduziert++; //nur eine Kante tragt bei
									
							if(HeparinSterneVerknuepfung[i][j] == 1)
								KantenGraph++;
							else if (HeparinSterneVerknuepfung[i][j] == 2)
								KantenGraph+=2;
							else if (HeparinSterneVerknuepfung[i][j] == 3)
								KantenGraph+=3;
							else if (HeparinSterneVerknuepfung[i][j] == 4)
								KantenGraph+=4;
							else if (HeparinSterneVerknuepfung[i][j] > 4)
							{
								System.out.println("Verknuepfung 5! Sollte nicht vorkommen! Wert: " +HeparinSterneVerknuepfung[i][j] + " bei Hep:" + (i+1) + "   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
								System.exit(1); 
							}
							
							}
						}
					
					int ZyklenrangReduziert = 0;	//elastische Verbindungen
					int ZyklenrangGraph = 0;	//mathematischen Verbindungen
					
					ZyklenrangReduziert = KantenReduziert-(HeparinEcken+PEGEcken)+1;
					ZyklenrangGraph = KantenGraph-(HeparinEcken+PEGEcken)+1;
						
					/* 
					 * Ende Zyklenrangbestimmung
					 */
					
					counterAdditionalBonds++;
					
					int allStars=0;
					for(int i = 1; i<= NrOfStars; i++)
						allStars += StarClusterSize[i];
					
					System.out.println("Frame: " +frame + " bond: "+counterAdditionalBonds+ "  HEPEcken: "+ HeparinEcken+  "    PEGEcken: "+PEGEcken+ "    Kanten: "+ KantenReduziert +"  ZyklenrangReduziert: "+ZyklenrangReduziert);
					System.out.println("Frame: " +frame + " bond: "+counterAdditionalBonds+ "  HEPEcken: "+ HeparinEcken+  "    PEGEcken: "+PEGEcken+ "    KantenGraph: "+ KantenGraph +"  ZyklenrangGraph: "+ZyklenrangGraph);
					
					System.out.println("Frame: " +frame + "  ZyklenrangReduziert: "+ZyklenrangReduziert+  "    ZyklenrangGraph: "+ZyklenrangGraph+ "     maxCluster: "+ findMaximumArrayValue() +"       SumCluster: " + allStars + "      addBonds: " + counterAdditionalBonds);
					
					for(int j = 0; j < NrOfStars; j++)
					{
						 if(PEGBonds[j] > 4)
						 {
								System.out.println("Verknuepfung 5! Sollte nicht vorkommen!   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
								System.exit(1); 
						 }
					}
					
					CrosslinksVsCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), findMaximumArrayValue()/(1.0*NrOfStars));
					
					if(counterSinglebondsTmp != counterSinglebonds) //Bindung hat sich erhoeht
					{
						counterSinglebonds=counterSinglebondsTmp;
						CrosslinksVsCluster_LinearBonds.AddValue(counterSinglebonds/(4.0*NrOfStars), findMaximumArrayValue()/(1.0*NrOfStars));
						
					}
					
					
					HSZyklenrangReduziert.AddValue(counterAdditionalBonds/(4.0*NrOfStars), ZyklenrangReduziert);
					HSZyklenrangGraph.AddValue(counterAdditionalBonds/(4.0*NrOfStars), ZyklenrangGraph);
				
				
					//normiert zur Laufzeit
					HSZyklenrangReduziertNormiert.AddValue(counterAdditionalBonds/(4.0*NrOfStars), ZyklenrangReduziert/(4.0*PEGEcken-(HeparinEcken+PEGEcken)+1));
					HSZyklenrangGraphNormiert.AddValue(counterAdditionalBonds/(4.0*NrOfStars), ZyklenrangGraph/(4.0*PEGEcken-(HeparinEcken+PEGEcken)+1));
				
				}
				
				/*int allStars=0;
				for(int i = 1; i<= NrOfStars; i++)
					allStars += StarClusterSize[i];
				
				System.out.println("Frame: " +frame + "     maxCluster: "+ findMaximumArrayValue() +"       SumCluster: " + allStars + "      addBonds: " + counterAdditionalBonds);
				
				for(int j = 0; j < NrOfStars; j++)
				{
					 if(PEGBonds[j] > 4)
					 {
							System.out.println("Verknuepfung 5! Sollte nicht vorkommen!   PEG:"+ (j+1) + "  Zeit:"+importData.MCSTime);
							System.exit(1); 
					 }
				}
				
				CrosslinksVsCluster.AddValue(counterAdditionalBonds/(4.0*NrOfStars), findMaximumArrayValue()/(1.0*NrOfStars));
				*/
				//System.out.println("size: " +bonds[(int) (importData.MCSTime/(10000))].ReturnN());
				System.out.println("size: " +importData.addedBondsBetweenFrames.size());
				importData.addedBondsBetweenFrames.clear();
	}
	
	
	private int findMaximumArrayValue()
	{
		int maximum = StarClusterSize[1];   // 1.Wert
		for (int i=2; i <= NrOfStars; i++)
		{
			if(StarClusterSize[i] > maximum) {
		            maximum = StarClusterSize[i];   // new maximum
		        }
		}
		return maximum;
	}
	
	private int findColorOfMaximumCluster()
	{
		int color = 1;
		int maximum = StarClusterSize[1];   // 1.Wert
		for (int i=2; i <= NrOfStars; i++)
		{
			if(StarClusterSize[i] > maximum) {
		            maximum = StarClusterSize[i];   // new maximum
		            color = i; //Farbe des groessten Clusters
		        }
		}
		return color;
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
