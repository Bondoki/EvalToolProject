package EvalToolProject_ice.General;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.IntBitSet;
import EvalToolProject_ice.tools.Int_IntArrayList_Table;
import EvalToolProject_ice.tools.LongArrayList;
import EvalToolProject_ice.tools.Statistik;

public class CheckBondsAndLattice {


	
	
	String PathToFile;
	
	
	int[] Polymersystem;
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
	int Gitter_x;
	int Gitter_y;
	int Gitter_z;
	
	//IntBitSet boxBitSetCore;
	IntBitSet boxBitSetBox;
	
	BFMImportData importData;
	
	int[] StartFrame_X;
	int[] StartFrame_Y;
	int[] StartFrame_Z;
	
	int[] dumpsystem;

	BFMFileSaver NotMovedMonomersSaver;
	
	int MONOMERZAHL;
	
	LongArrayList NotMovedMonomers;
	
	Int_IntArrayList_Table Bindungsnetzwerk; 
	
	long deltaT;
	
	boolean[] BondArray;
	
	public CheckBondsAndLattice(String SrcDirFileName)//, String skip, String current)
	{
		PathToFile =SrcDirFileName;
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		
		
		//Determine MaxFrame out of the first file
		BFMImportData FirstData = new BFMImportData(PathToFile);
		int maxframe = FirstData.GetNrOfFrames();
		deltaT = FirstData.GetDeltaT();
		
		System.out.println("First init: maxFrame: "+ maxframe + "  dT "+deltaT);
		
		
		System.out.println("file : " +PathToFile );
		
		DecimalFormat dh = new DecimalFormat("0.0000");
		
		
		LoadFile(PathToFile, currentFrame);
		
		
		System.out.println("Successful Run - nothing special found");
	    
	}
	
	protected void LoadFile(String file, int startframe)
	{
		
		
		System.out.println("lade System");
		LadeSystem(PathToFile);	
		
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(PathToFile);
		  
		  importData.GetFrameOfSimulation(currentFrame);
		  
		  importData.CloseSimulationFile();
		  
		  importData.OpenSimulationFile(PathToFile);
			
		  
			System.out.println("file : " +PathToFile);
			
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
			
			
			//second run
		 importData.OpenSimulationFile(PathToFile);
			
		 System.out.println("2.nd file : " +PathToFile);
		 System.out.println("possible not moved monomers : " +NotMovedMonomers.size());
				
		 for(int nr = 0; nr < NotMovedMonomers.size(); nr++ )
			{
			 System.out.println(nr +" : "+NotMovedMonomers.get(nr));
			}
		 
		 NotMovedMonomersSaver = new BFMFileSaver();
		 NotMovedMonomersSaver.DateiAnlegen(PathToFile+"NotMovedMonomers.dat", false);
		 
				z = 1;
					
			    while ( (z <= NrofFrames))
			      {

			    		playSimulation2ndrun(z);
			    		z++;
			    	
			    		for(int u = 1; u <= skipFrames; u++)
			    		{
			    			z++;
			    		}

			      }
			   importData.CloseSimulationFile();
			    
		NotMovedMonomersSaver.DateiSchliessen();
		
				currentFrame = z;
				
				if( (currentFrame+skipFrames) >= NrofFrames)
					currentFrame =  NrofFrames;
		
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		if(args.length != 1)
		{
			System.out.println("Check the bonds and lattice occupation of specified file");
			System.out.println("USAGE: SrcDir/SrcFile");
		}
		else new CheckBondsAndLattice(args[0]);
	}
	
	public void LadeSystem(String FileDirFileName)
	{
		importData=null;
		importData = new BFMImportData(FileDirFileName);
		
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
		
		boolean periodRB = false;
		
		if ((periodRB_x == true) || (periodRB_y == true) || (periodRB_z == true))
			periodRB = true;
		
		//boxBitSetCore = new IntBitSet(Gitter_x*Gitter_y*Gitter_z);
		boxBitSetBox = new IntBitSet(Gitter_x*Gitter_y*Gitter_z);
		//System.arraycopy(importData.Polymersystem,0,Polymersystem,0, Polymersystem.length);
		
		StartFrame_X = new int[MONOMERZAHL];
		StartFrame_Y = new int[MONOMERZAHL];
		StartFrame_Z = new int[MONOMERZAHL];
		
		NotMovedMonomers = new LongArrayList();
		
		NrofFrames = importData.GetNrOfFrames();
	
		BondArray = new boolean[512];
		java.util.Arrays.fill(BondArray, true);
		
		for (int i = 0; i < importData.BondArray.length; i++)
			BondArray[i]=importData.BondArray[i];
		
		Bindungsnetzwerk = null;
		Bindungsnetzwerk = new Int_IntArrayList_Table(MONOMERZAHL);
		
		
		for(int it = 0; it < importData.bonds.size(); it++)
		{
			long bondobj = importData.bonds.get(it);
			//System.out.println(it + " bond " + bondobj);
			int mono1 = getMono1Nr(bondobj);
			int mono2 = getMono2Nr(bondobj);
			
			Bindungsnetzwerk.put(mono1, mono2);
			Bindungsnetzwerk.put(mono2, mono1);
			System.out.println("bonds_sim: "+it+"   a:" + getMono1Nr(bondobj) + " b:" + getMono2Nr(bondobj));
		}
		
		for(int it = 0; it < importData.additionalbonds.size(); it++)
		{
			long bondobj = importData.additionalbonds.get(it);
			//System.out.println(it + " bond " + bondobj);
			int mono1 = getMono1Nr(bondobj);
			int mono2 = getMono2Nr(bondobj);
			
			Bindungsnetzwerk.put(mono1, mono2);
			Bindungsnetzwerk.put(mono2, mono1);
			System.out.println("additionalbonds_sim: "+it+"   a:" + getMono1Nr(bondobj) + " b:" + getMono2Nr(bondobj));
		}
		
		
		
	}
	
	public void playSimulation(int frame)
	{
				
				//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
				importData.GetFrameOfSimulation( frame);
				
				//boxBitSetCore.clear();
				boxBitSetBox.clear();
				
				for(int mono= 1; mono <= importData.NrOfMonomers; mono++)
				{
					//boxBitSetCore.set( (((( importData.PolymerKoordinaten[mono][0]) % Gitter_x) + Gitter_x) % Gitter_x) + Gitter_x*(((( importData.PolymerKoordinaten[mono][1]) % Gitter_y) + Gitter_y) % Gitter_y)+ Gitter_x*Gitter_y*((((importData.PolymerKoordinaten[mono][2]) % Gitter_z) + Gitter_z) % Gitter_z));
					
					boxBitSetBox.set( (((( importData.PolymerKoordinaten[mono][0]  ) % Gitter_x) + Gitter_x) % Gitter_x) + Gitter_x*(((( importData.PolymerKoordinaten[mono][1]  ) % Gitter_y) + Gitter_y) % Gitter_y)+ Gitter_x*Gitter_y*((((importData.PolymerKoordinaten[mono][2]  ) % Gitter_z) + Gitter_z) % Gitter_z));
					boxBitSetBox.set( (((( importData.PolymerKoordinaten[mono][0]+1) % Gitter_x) + Gitter_x) % Gitter_x) + Gitter_x*(((( importData.PolymerKoordinaten[mono][1]  ) % Gitter_y) + Gitter_y) % Gitter_y)+ Gitter_x*Gitter_y*((((importData.PolymerKoordinaten[mono][2]  ) % Gitter_z) + Gitter_z) % Gitter_z));
					boxBitSetBox.set( (((( importData.PolymerKoordinaten[mono][0]  ) % Gitter_x) + Gitter_x) % Gitter_x) + Gitter_x*(((( importData.PolymerKoordinaten[mono][1]+1) % Gitter_y) + Gitter_y) % Gitter_y)+ Gitter_x*Gitter_y*((((importData.PolymerKoordinaten[mono][2]  ) % Gitter_z) + Gitter_z) % Gitter_z));
					boxBitSetBox.set( (((( importData.PolymerKoordinaten[mono][0]+1) % Gitter_x) + Gitter_x) % Gitter_x) + Gitter_x*(((( importData.PolymerKoordinaten[mono][1]+1) % Gitter_y) + Gitter_y) % Gitter_y)+ Gitter_x*Gitter_y*((((importData.PolymerKoordinaten[mono][2]  ) % Gitter_z) + Gitter_z) % Gitter_z));
					
					boxBitSetBox.set( (((( importData.PolymerKoordinaten[mono][0]  ) % Gitter_x) + Gitter_x) % Gitter_x) + Gitter_x*(((( importData.PolymerKoordinaten[mono][1]  ) % Gitter_y) + Gitter_y) % Gitter_y)+ Gitter_x*Gitter_y*((((importData.PolymerKoordinaten[mono][2]+1) % Gitter_z) + Gitter_z) % Gitter_z));
					boxBitSetBox.set( (((( importData.PolymerKoordinaten[mono][0]+1) % Gitter_x) + Gitter_x) % Gitter_x) + Gitter_x*(((( importData.PolymerKoordinaten[mono][1]  ) % Gitter_y) + Gitter_y) % Gitter_y)+ Gitter_x*Gitter_y*((((importData.PolymerKoordinaten[mono][2]+1) % Gitter_z) + Gitter_z) % Gitter_z));
					boxBitSetBox.set( (((( importData.PolymerKoordinaten[mono][0]  ) % Gitter_x) + Gitter_x) % Gitter_x) + Gitter_x*(((( importData.PolymerKoordinaten[mono][1]+1) % Gitter_y) + Gitter_y) % Gitter_y)+ Gitter_x*Gitter_y*((((importData.PolymerKoordinaten[mono][2]+1) % Gitter_z) + Gitter_z) % Gitter_z));
					boxBitSetBox.set( (((( importData.PolymerKoordinaten[mono][0]+1) % Gitter_x) + Gitter_x) % Gitter_x) + Gitter_x*(((( importData.PolymerKoordinaten[mono][1]+1) % Gitter_y) + Gitter_y) % Gitter_y)+ Gitter_x*Gitter_y*((((importData.PolymerKoordinaten[mono][2]+1) % Gitter_z) + Gitter_z) % Gitter_z));
					
				}
				
				
				int hj=0;
				for(int i = 0; i < Gitter_x; i++)
					for (int k = 0; k < Gitter_y; k++)
						for (int l = 0; l < Gitter_z; l++)
							if (boxBitSetBox.get(i + Gitter_x*k + Gitter_x*Gitter_y*l) == true)
								hj++;
				
				System.out.println("besaetzte plaetze\t" +hj  +" von " + (8*(MONOMERZAHL-1)));
				
				
				if(hj !=  (8*(MONOMERZAHL-1)))
				{
					System.out.println("invalid number of occupied lattice points. Exiting...");
					//BFMFileSaver ErrorSaver = new BFMFileSaver();
					//ErrorSaver.DateiAnlegen(System.getProperty("user.home")+"/ErrorBFM.txt", true);
					//ErrorSaver.setzeZeile(Filename+" ...invalid number of occupied lattice points");
					//ErrorSaver.DateiSchliessen();
					System.exit(1);
				}
				else System.out.println("occupied lattice points is valid.");
				
				/*for(int i = 0; i < Gitter_x; i++)
					for (int k = 0; k < Gitter_y; k++)
						for (int l = 0; l < Gitter_z; l++)
							if (boxBitSetCore.get(i + Gitter_x*k + Gitter_x*Gitter_y*l) == true)
								if (boxBitSetBox.get(i + Gitter_x*k + Gitter_x*Gitter_y*l) == true)
								{
									System.out.println("Violating excluded volume: core of monomer is multiply occupied . Exiting...");
									System.exit(1);
								}
				*/
				if(frame==1)
				{
					for(int mono= 1; mono <= importData.NrOfMonomers; mono++)
					{
						StartFrame_X[mono] = importData.PolymerKoordinaten[mono][0];
						StartFrame_Y[mono] = importData.PolymerKoordinaten[mono][1];
						StartFrame_Z[mono] = importData.PolymerKoordinaten[mono][2];
					}
				}
				
				if(frame == NrofFrames)
				{
					System.out.println("testing movement");
					
					for(int mono= 1; mono <= importData.NrOfMonomers; mono++)
					{
						/*if(StartFrame_X[mono] == importData.PolymerKoordinaten[mono][0])
						{
							System.out.println("monomer " + mono + " not moved in X");
							System.out.println("start: " + StartFrame_X[mono] + " " + StartFrame_Y[mono]+ " " + StartFrame_Z[mono]);
							System.out.println("end  : " + importData.PolymerKoordinaten[mono][0] + " " + importData.PolymerKoordinaten[mono][1]+ " " + importData.PolymerKoordinaten[mono][2]);
							
						}
						
						if(StartFrame_Y[mono] == importData.PolymerKoordinaten[mono][1])
						{
							System.out.println("monomer " + mono + " not moved in Y");
							System.out.println("start: " + StartFrame_X[mono] + " " + StartFrame_Y[mono]+ " " + StartFrame_Z[mono]);
							System.out.println("end  : " + importData.PolymerKoordinaten[mono][0] + " " + importData.PolymerKoordinaten[mono][1]+ " " + importData.PolymerKoordinaten[mono][2]);
							
						}
						
						if(StartFrame_Z[mono] == importData.PolymerKoordinaten[mono][2])
						{
							System.out.println("monomer " + mono + " not moved in Z");
							System.out.println("start: " + StartFrame_X[mono] + " " + StartFrame_Y[mono]+ " " + StartFrame_Z[mono]);
							System.out.println("end  : " + importData.PolymerKoordinaten[mono][0] + " " + importData.PolymerKoordinaten[mono][1]+ " " + importData.PolymerKoordinaten[mono][2]);
							
						}
						*/
						if((Math.abs(StartFrame_X[mono] - importData.PolymerKoordinaten[mono][0]) <= 4) && (Math.abs(StartFrame_Y[mono] - importData.PolymerKoordinaten[mono][1]) <=4) && (Math.abs(StartFrame_Z[mono] - importData.PolymerKoordinaten[mono][2]) <=4))
						{
							System.out.println("monomer " + mono + " not moved in XYZ");
							System.out.println("start: " + StartFrame_X[mono] + " " + StartFrame_Y[mono]+ " " + StartFrame_Z[mono]);
							System.out.println("end  : " + importData.PolymerKoordinaten[mono][0] + " " + importData.PolymerKoordinaten[mono][1]+ " " + importData.PolymerKoordinaten[mono][2]);
							
							NotMovedMonomers.add(mono);
						}
					}
				}
				
				//System.out.println("Mono 37417...");
				//System.out.println(37417+ " (pos "+ importData.PolymerKoordinaten[37417][0] +","+importData.PolymerKoordinaten[37417][1]+","+importData.PolymerKoordinaten[37417][2] +") ");
				//System.out.println("Mono 37433...");
				//System.out.println(37433+ " (pos "+ importData.PolymerKoordinaten[37433][0] +","+importData.PolymerKoordinaten[37433][1]+","+importData.PolymerKoordinaten[37433][2] +") ");
				
				
				System.out.println("Checking Bonds...");
				
				for (int i= 1; i < MONOMERZAHL; i++)
					for (int j = 0; j < Bindungsnetzwerk.get(i).size(); j++)
						if(i < Bindungsnetzwerk.get(i).get(j))
				{
					int bondlength_X =(importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][0]-importData.PolymerKoordinaten[i][0]);
					int bondlength_Y =(importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][1]-importData.PolymerKoordinaten[i][1]);
					int bondlength_Z =(importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][2]-importData.PolymerKoordinaten[i][2]);
					
					if((bondlength_X > 3) || (bondlength_X < -3))
					{
						System.out.println("Invalid XBond...");
						System.out.println(bondlength_X + " " + bondlength_Y+ " " + bondlength_Z + "  between mono: " +i+ " (pos "+ importData.PolymerKoordinaten[i][0] +","+importData.PolymerKoordinaten[i][1]+","+importData.PolymerKoordinaten[i][2] +") and " + Bindungsnetzwerk.get(i).get(j) + " (pos "+ importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][0] +","+importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][1]+","+importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][2] +")");
						System.exit(1);
					}
					
					if((bondlength_Y > 3) || (bondlength_Y < -3))
					{
						System.out.println("Invalid YBond...");
						System.out.println(bondlength_X + " " + bondlength_Y+ " " + bondlength_Z + "  between mono: " +i+ " (pos "+ importData.PolymerKoordinaten[i][0] +","+importData.PolymerKoordinaten[i][1]+","+importData.PolymerKoordinaten[i][2] +") and " + Bindungsnetzwerk.get(i).get(j) + " (pos "+ importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][0] +","+importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][1]+","+importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][2] +")");
						System.exit(1);
					}
					
					if((bondlength_Z > 3) || (bondlength_Z < -3))
						{
							System.out.println("Invalid ZBond...");
							System.out.println(bondlength_X + " " + bondlength_Y+ " " + bondlength_Z + "  between mono: " +i+ " (pos "+ importData.PolymerKoordinaten[i][0] +","+importData.PolymerKoordinaten[i][1]+","+importData.PolymerKoordinaten[i][2] +") and " + Bindungsnetzwerk.get(i).get(j) + " (pos "+ importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][0] +","+importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][1]+","+importData.PolymerKoordinaten[Bindungsnetzwerk.get(i).get(j)][2] +")");
							System.exit(1);
						}
					
					
					if(BondArray[KoordBondArray(bondlength_X, bondlength_Y, bondlength_Z)] == true)
	  				{
	  					System.out.println("bonds corrupted . Exiting...");
	  					
	  					System.exit(1);
	  				}
						
				}
	}

	
	public void playSimulation2ndrun(int frame)
	{
		importData.GetFrameOfSimulation( frame);
		
		StringBuffer objectBuffer = new StringBuffer(3000);
		
		objectBuffer.append(frame + " ");
		
		for(int nr = 0; nr < NotMovedMonomers.size(); nr++ )
		{
			objectBuffer.append(NotMovedMonomers.get(nr) + " "+ importData.PolymerKoordinaten[(int)NotMovedMonomers.get(nr)][0] + " " + importData.PolymerKoordinaten[(int)NotMovedMonomers.get(nr)][1]+ " " + importData.PolymerKoordinaten[(int)NotMovedMonomers.get(nr)][2] + " ");
		}
		
		NotMovedMonomersSaver.setzeZeile(objectBuffer.toString());
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
	
	private int KoordBondArray(int x, int y, int z)
	{
		return (x & 7) + ((y&7) << 3) + ((z&7) << 6);
	}
}
