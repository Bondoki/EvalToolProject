package EvalToolProject.tools;
import java.io.Reader;
import java.util.StringTokenizer;


public class BFMImportData {

	Reader FileReader;
	//StreamTokenizer st;
	
	BFMFileLoader loadFile;
	BFMFileParser Parser;
	
	
	private IntBitSet boxBitSet;
	
	//int option = 0;
	
	public int NrOfMonomers = 100;
	int Max_Connectivity = 2;
	public int box_x = 128;
	public int box_y = 128;
	public int box_z = 128;
	public boolean periodic_x = false;
	public boolean periodic_y = false;
	public boolean periodic_z = false;
	
	boolean efield_on = false;
	double eforce = 0.0;
	boolean efield_x = false;
	boolean efield_y = false;
	boolean efield_z = false;
	
	boolean absorption_on = false;
	double absorption_force = 0.0;
	boolean absorption_x = false;
	boolean absorption_y = false;
	boolean absorption_z = false;
	
	boolean monomers_fixed = false;
	boolean excluded_volume = true;
	
	//int Kettenlaenge = 0;
	//int Kettenanzahl = 0;
	//double graftingDensity;
	
	public int NrOfHeparin=0;
	public int NrOfStars=0;
	public int NrOfMonomersPerStarArm = 0;
	
	public int NrOfCrosslinker;
	public int CrosslinkerFunctionality;
	
	public int NrOfChargesPerHeparin = 0;
	public double ReducedTemperature = 0.0;
	
	int startsystem = 0;
	
	// private Map typedefinition;// = new HashMap();
	// private Map types;
	// private List additionalbonds;
	
	public LongArrayList additionalbonds;
	public LongArrayList bonds;
	
	public LongArrayList addedBondsBetweenFrames;
	
	
	
	IntArrayList neueKetten;
	 
	 int wechselz = 0;
	 int zaehler = 0;
	 int internz = 0;
	 
	 public long MCSTime = 0;
	 
	 public int [] Polymersystem;
	 //int [] Ladungen;
	 public int [] Attributes;
	 
	 int NrOfMCS = 0;
	 
	 int NrofFrames = 0;
	
	 
	 int[] dummysystem; 
	 
	 boolean gehtweiter = true;
	 
	 public int[][] PolymerKoordinaten; //[Mononummer] [0->x ; 1->y; 2->z;]
	 
	 boolean firstInit;
	 
	 int[] JumpsOver_X;
     int[] JumpsOver_Y;
     int[] JumpsOver_Z;
     
     boolean isDetermined;
     
     long DeltaT=0;
     long DeltaTStart=0;
     
     boolean[] BondArray;
     int[] BondAsciiArray;
 	 int[] Ascii2BondArray;
 	 
 	 String Filename;
	
	public BFMImportData(String FileName)
	{
		Filename=FileName;
		loadFile = new BFMFileLoader();
		Parser = new BFMFileParser(this);
		
		//typedefinition = new HashMap();
		//types = new HashMap();
		//additionalbonds = new ArrayList();
		//bonds = new ArrayList();
		
		boxBitSet=null;
		
		BondArray = new boolean[512];
		java.util.Arrays.fill(BondArray, true);
		
		BondAsciiArray = new int[512];
		
		Ascii2BondArray = new int[512];
		
		
		additionalbonds = new LongArrayList();
		bonds = new LongArrayList();
		
		addedBondsBetweenFrames = new LongArrayList();
		
		neueKetten = new IntArrayList();
		
		
		NrOfMCS = 0;
		
		firstInit = false;
		isDetermined = false;
		
		FileReader = loadFile.DateiOeffnen(FileName);
		NrofFrames = GetNrOfMCS();
		loadFile.DateiSchliessen();
		
		//System.out.println("nrofmcs:" +NrOfMCS);
		//for(int t=0; t<13;t++)
		//	loadFile.readNextLine();
		FileReader = loadFile.DateiOeffnen(FileName);
		
		parsingFile();
		
		//importData.GetMeaning(loadFile.readNextLine());
		loadFile.DateiSchliessen();
	}
	
	public int GetNrOfMCS()
	{
		String line;
		
		while ((line = loadFile.readNextLine()) != "") {
            
	         if ((line.indexOf("!mcs") != -1 )&& (line.indexOf("!mcs") < 15))
	         {
	         NrOfMCS++;
	         }
	   }
		
		return NrOfMCS;
	}
	
	public int GetNrOfFrames()
	{
		return NrofFrames;
	}
	
	public void OpenSimulationFile(String FileName)
	{
		FileReader = loadFile.DateiOeffnen(FileName);
		NrOfMCS = 1;
		
		additionalbonds = null;
		additionalbonds = new LongArrayList();
		
		addedBondsBetweenFrames = null;
		addedBondsBetweenFrames = new LongArrayList();
	}
	
	public void CloseSimulationFile()
	{
		loadFile.DateiSchliessen();
	}
	
	public int[] GetFrameOfSimulation(int frame)
	{
		//FileReader = loadFile.DateiOeffnen(FileName);
		

		//System.out.println(1+ " F1 " +JumpsOver_Z[1]);
		
		
		
		String line;
		boolean weiterladen = true;
		
		do { 
		
			//line = loadFile.readNextLine();
		if ((line = loadFile.readNextLine()) == "") 
			weiterladen = false;
		
		if ((line.indexOf("!mcs") != -1 )&& (line.indexOf("!mcs") < 10))
		{ 
		 //System.out.println("nrofmcs:" +NrOfMCS);
			
			StringTokenizer st = new StringTokenizer(line, ": ");
				
					 if(frame != NrOfMCS )//1)
					 {
						 NrOfMCS++;
						 //frame--;
						do
						{
							line = loadFile.readNextLine();
							
						}while(!line.equals(""));
						
					 }
			
					 else
					 {
						// System.out.println("frame " + frame + "   mcs " +NrOfMCS  + "   von" +NrofFrames+ "  " + line);
						
						 //Marcos Zeitschreibung
						 String str = st.nextToken();
						 if(str.length() == 5)
						 {
							 MCSTime =  Long.parseLong(st.nextToken()) ;
						 }//meine
						 else
						 {
							 MCSTime =  Long.parseLong(str.substring(5)) ;
						 }
						 DeltaT=MCSTime-DeltaTStart;
					     DeltaTStart=MCSTime;
					 //wechselz = (int) Parser.mcs/1000000;
					// internz = (int) (Parser.mcs % 100);
					 //zaehler = (int) (Parser.mcs-wechselz*1000000- internz)/100;
					
					     
					      
					
					 
					 int ChainStartCounter=0;
					 
						while (st.hasMoreTokens()) {
							
							if (periodic_x == true)
								JumpsOver_X[neueKetten.get(ChainStartCounter)]=Integer.parseInt(st.nextToken());
							
							if (periodic_y == true)
								JumpsOver_Y[neueKetten.get(ChainStartCounter)]=Integer.parseInt(st.nextToken());
							
							if (periodic_z == true)
								JumpsOver_Z[neueKetten.get(ChainStartCounter)]=Integer.parseInt(st.nextToken());
							
							ChainStartCounter++;
					     }
						
						System.out.println("mcstime: "+MCSTime + " dt: "+DeltaT +  "  CS: "+ChainStartCounter);
						 
						
						if((ChainStartCounter > 0) && (ChainStartCounter != neueKetten.size()))
						{
							for(int Ket=0; Ket < neueKetten.size(); Ket++)
							{
								System.out.println(Ket + " "+ neueKetten.get(Ket));
							}
							System.out.println(ChainStartCounter +" "+ neueKetten.size());
							System.out.println("invalid chain jumps... exiting");
							BFMFileSaver ErrorSaver = new BFMFileSaver();
							ErrorSaver.DateiAnlegen(System.getProperty("user.home")+"/ErrorBFM.txt", true);
							ErrorSaver.setzeZeile(Filename+" ...invalid chain jumps");
							ErrorSaver.DateiSchliessen();
							System.exit(1);
						}
							
						
						if(ChainStartCounter > 0)
							{
								
								System.out.println("_countingJumps");
								
								
								
								
									for(int Ket=0; Ket < neueKetten.size(); Ket++)
									{
										for(int mono = neueKetten.get(Ket); mono < (Ket != (neueKetten.size()-1) ? neueKetten.get(Ket+1) : NrOfMonomers+1); mono++)
										{
											
										
										if((neueKetten.get(Ket) != mono) && (periodic_x == true))
											JumpsOver_X[mono]=JumpsOver_X[neueKetten.get(Ket)]; 
										
										//if(MCSTime == 102000000)
										//	System.out.print("gf");
										
										if((neueKetten.get(Ket) != mono) && (periodic_y == true))
											JumpsOver_Y[mono]=JumpsOver_Y[neueKetten.get(Ket)]; 
										
										if((neueKetten.get(Ket) != mono) && (periodic_z == true))
											JumpsOver_Z[mono]=JumpsOver_Z[neueKetten.get(Ket)]; 
										
										}
									}
									
								
							}
					 
						 DetSystem(frame);
						
					 weiterladen = false;
					 NrOfMCS++;
					 }
					
		}
		
		if ((line.indexOf("!add_bonds") != -1 )&& (line.indexOf("!add_bonds") < 15))
		{ 
			
			addedBondsBetweenFrames = null;
			addedBondsBetweenFrames = new LongArrayList();
			
			Add_Bonds();
					
					 
					
		}
		
		if ((line.indexOf("!remove_bonds") != -1 )&& (line.indexOf("!remove_bonds") < 15))
		{ 
			
			//addedBondsBetweenFrames = null;
			//addedBondsBetweenFrames = new LongArrayList();
			
			Remove_Bonds();
					
					 
					
		}
		
		}while(weiterladen == true);
	
		
		
		
		//loadFile.DateiSchliessen();
		
		return Polymersystem;
	}
	
	public long GetDeltaT()
	{
		return DeltaT;
	}
	
	public void DetSystem(int frame)
	{
		
		boxBitSet.clear();
		gehtweiter = true;
		
		String line = ""; //= loadFile.readNextLine();
		
		//if (line == null) 
			//return;
		int x,y,z;
		String StringOfBonds = "";
		
		int aktuellesMono = 1;
		
		int bind = 0;
		
		//double Rcm_x1 = 0.0, Rcm_y1 = 0.0, Rcm_z1 = 0.0;
		//double Rcm_x2 = 0.0, Rcm_y2 = 0.0, Rcm_z2 = 0.0;
	
		do
		{
			line = loadFile.readNextLine();
			
			//System.out.println("line :" + line);
			//StringTokenizer st;
			
			
			StringTokenizer st = new StringTokenizer(line, " ");
			
		
		while (st.hasMoreTokens()) {
			
			x = Integer.parseInt(st.nextToken());
			y = Integer.parseInt(st.nextToken());
			z = Integer.parseInt(st.nextToken());
			//ascii = Integer.parseInt(st.nextToken());
			StringOfBonds = st.nextToken("");
		
			Polymersystem[aktuellesMono]=koord(x, y, z);
			
			GitterSetzen(aktuellesMono);
			
			PolymerKoordinaten[aktuellesMono][0] = x;
			PolymerKoordinaten[aktuellesMono][1] = y;
			PolymerKoordinaten[aktuellesMono][2] = z;

			if (periodic_x == true)
				PolymerKoordinaten[aktuellesMono][0]+=JumpsOver_X[aktuellesMono]*box_x;
						
			if (periodic_y == true)
				PolymerKoordinaten[aktuellesMono][1]+=JumpsOver_Y[aktuellesMono]*box_y;
			
			if (periodic_z == true)
				PolymerKoordinaten[aktuellesMono][2]+=JumpsOver_Z[aktuellesMono]*box_z;

		    //System.out.println(aktuellesMono+ "  " +JumpsOver_Z[aktuellesMono]);
			
			
			
			
			//neueKetten.add(aktuellesMono);
			
			
			aktuellesMono++;
			
	       //System.out.println("length1: " +StringOfBonds.length());
			
	        for (int zk = 1; zk < StringOfBonds.length();zk++)
  		  {
  			  
  				 // bind = ASCIItoBond(StringOfBonds.charAt(zk));
  				 bind = Ascii2BondArray[(int) StringOfBonds.charAt(zk)];
  				
  				 int comp_x = xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ;
  				 int comp_y = ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3 ;
  				 int comp_z = zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3 ;
   				
  				 //Test if new monomer position allowed
  				 if((periodic_x == false) && ((comp_x < 0) || (comp_x > (box_x-2))))
  				 {
  					 System.out.println("xwert exceeds the box of monomer "+aktuellesMono+ " with "+ comp_x+". Exiting...");
  					BFMFileSaver ErrorSaver = new BFMFileSaver();
					ErrorSaver.DateiAnlegen(System.getProperty("user.home")+"/ErrorBFM.txt", true);
					ErrorSaver.setzeZeile(Filename+" ...xwert exceeds the box");
					ErrorSaver.DateiSchliessen();
  					 System.exit(1);
  				 }
  				 
  				 if((periodic_y == false) && ((comp_y < 0) || (comp_y > (box_y-2))))
  				 {
  					 System.out.println("ywert exceeds the box of monomer "+aktuellesMono+ " with "+ comp_y+". Exiting...");
  					BFMFileSaver ErrorSaver = new BFMFileSaver();
					ErrorSaver.DateiAnlegen(System.getProperty("user.home")+"/ErrorBFM.txt", true);
					ErrorSaver.setzeZeile(Filename+" ...ywert exceeds the box");
					ErrorSaver.DateiSchliessen();
  					 System.exit(1);
  				 }
  				 
  				 if((periodic_z == false) && ((comp_z < 0) || (comp_z > (box_z-2))))
  				 {
  					 System.out.println("zwert exceeds the box of monomer "+aktuellesMono+ " with "+ comp_z+". Exiting...");
  					BFMFileSaver ErrorSaver = new BFMFileSaver();
					ErrorSaver.DateiAnlegen(System.getProperty("user.home")+"/ErrorBFM.txt", true);
					ErrorSaver.setzeZeile(Filename+" ...zwert exceeds the box");
					ErrorSaver.DateiSchliessen();
  					 System.exit(1);
  				 }
  				  
  				 //Whenever box is periodic fold back
  				if((periodic_x == true))
 				 {
  					comp_x = (box_x+comp_x)%box_x;
 				 }
  				
  				if((periodic_y == true))
				 {
 					comp_y = (box_y+comp_y)%box_y;
				 }
  				
  				if((periodic_z == true))
				 {
					comp_z = (box_z+comp_z)%box_z;
				 }
  				 
  				
  				//Set new postion
  				Polymersystem[aktuellesMono] = koord( comp_x ,  comp_y, comp_z);
  				GitterSetzen(aktuellesMono);
  				
  				//check if bond allowed
  				if(BondArray[KoordBondArray(xwert(bind) - 3, ywert(bind) - 3,zwert(bind) - 3)] == true)
  				{
  					System.out.println("bonds corrupted . Exiting...");
  					BFMFileSaver ErrorSaver = new BFMFileSaver();
					ErrorSaver.DateiAnlegen(System.getProperty("user.home")+"/ErrorBFM.txt", true);
					ErrorSaver.setzeZeile(Filename+" ...bonds corrupted");
					ErrorSaver.DateiSchliessen();
  					System.exit(1);
  				}
  				 // Polymersystem[aktuellesMono] = koord((box_x + xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3) % box_x, (box_y + ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3) % box_y, (box_z + zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3) % box_z);
   				 // Polymersystem[aktuellesMono] = koord( xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ,  ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3, zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3);

   				PolymerKoordinaten[aktuellesMono][0] = PolymerKoordinaten[aktuellesMono-1][0] + xwert(bind) - 3 ;
   				PolymerKoordinaten[aktuellesMono][1] = PolymerKoordinaten[aktuellesMono-1][1] + ywert(bind) - 3 ;
   				PolymerKoordinaten[aktuellesMono][2] = PolymerKoordinaten[aktuellesMono-1][2] + zwert(bind) - 3 ;
   				  
   				 
  				  
  				  aktuellesMono++;
  				 
  		  }
  		  
	   
	     }
		
		
		
		}while(!line.equals(""));
		
		/*line = loadFile.readNextLine();
		
		st = new StringTokenizer(line, " ");
			
		
		while (st.hasMoreTokens()) {
			
			x = Integer.parseInt(st.nextToken());
			y = Integer.parseInt(st.nextToken());
			z = Integer.parseInt(st.nextToken());
			//ascii = Integer.parseInt(st.nextToken());
			StringOfBonds = st.nextToken("");
		
			Polymersystem[aktuellesMono]=koord(x, y, z);
			
			PolymerKoordinaten[aktuellesMono][0] = x;
			PolymerKoordinaten[aktuellesMono][1] = y;
			PolymerKoordinaten[aktuellesMono][2] = z;
			
			
			
			//neueKetten.add(aktuellesMono);
			
			
			aktuellesMono++;
			
	       //System.out.println("length2: " +StringOfBonds.length());
			
	        for (int zk = 1; zk < StringOfBonds.length();zk++)
  		  {
  			  
  				  bind = ASCIItoBond(StringOfBonds.charAt(zk));
  				  
  				if((xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ) < 0)
 					 System.out.println("xwert < 0 bei Ring2");
 				 
 				if((xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ) > box_x)
					 System.out.println("xwert > "+box_x+" bei Ring2");
  				
  				  
   				  Polymersystem[aktuellesMono] = koord( xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ,  ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3,  zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3);

   				PolymerKoordinaten[aktuellesMono][0] = PolymerKoordinaten[aktuellesMono-1][0] + xwert(bind) - 3 ;
   				PolymerKoordinaten[aktuellesMono][1] = PolymerKoordinaten[aktuellesMono-1][1] + ywert(bind) - 3 ;
   				PolymerKoordinaten[aktuellesMono][2] = PolymerKoordinaten[aktuellesMono-1][2] + zwert(bind) - 3 ;
   				 
   				  
   				 
  				  
  				  aktuellesMono++;
  				 
  		  }
  		  
	        
	     }*/
		
		
		
		//System.out.println("Rcm_x1: " + Rcm_x1 + " Rcm_x2: "+ Rcm_x2 + "  diff "+ (Rcm_x1 - Rcm_x2 ));
		//System.out.println("Rcm_y1: " + Rcm_y1 + " Rcm_y2: "+ Rcm_y2);
		//System.out.println("Rcm_z1: " + Rcm_z1 + " Rcm_z2: "+ Rcm_z2);
		//System.out.println();
		
		
		if(aktuellesMono != (NrOfMonomers+1))
		{
			System.out.println("number of monomers inside frame != NrOfMonomers. Exiting...");
			BFMFileSaver ErrorSaver = new BFMFileSaver();
			ErrorSaver.DateiAnlegen(System.getProperty("user.home")+"/ErrorBFM.txt", true);
			ErrorSaver.setzeZeile(Filename+" ...number of monomers inside frame != NrOfMonomers");
			ErrorSaver.DateiSchliessen();
			System.exit(1);
		}
		
		
		
		//line = loadFile.readNextLine();
		
		/*int hj=0;
		for(int i = 0; i < box_x; i++)
			for (int k = 0; k < box_y; k++)
				for (int l = 0; l < box_z; l++)
					if (boxBitSet.get(i + box_x*k + box_x*box_y*l) == true)
						hj++;
		
		System.out.println("besaetzte plaetze\t" +hj  +" von " + (NrOfMonomers*8));
		
		if(hj !=  (NrOfMonomers*8))
		{
			System.out.println("invalid number of occupied lattice points. Exiting...");
			BFMFileSaver ErrorSaver = new BFMFileSaver();
			ErrorSaver.DateiAnlegen(System.getProperty("user.home")+"/ErrorBFM.txt", true);
			ErrorSaver.setzeZeile(Filename+" ...invalid number of occupied lattice points");
			ErrorSaver.DateiSchliessen();
			System.exit(1);
		}*/
		
		
		//checking all additional bonds
		for(int nr = 0; nr < additionalbonds.size(); nr++ )
		{
			long bondobj = additionalbonds.get(nr);
			//System.out.println(it + " bond " + bondobj);
			
			int mono1 = getMono1Nr(bondobj);
			int mono2 = getMono2Nr(bondobj);
			
			int comp_x = xwert(Polymersystem[mono1])- xwert(Polymersystem[mono2]);
			int comp_y = ywert(Polymersystem[mono1])- ywert(Polymersystem[mono2]);
			int comp_z = zwert(Polymersystem[mono1])- zwert(Polymersystem[mono2]);
			
			//Whenever box is periodic fold back
			if((periodic_x == true))
			 {		
				if(comp_x < -3)
					comp_x = (box_x+comp_x);
				if(comp_x > +3)
					comp_x = (-box_x+comp_x);
			 }
				
			if((periodic_y == true))
			 {
				if(comp_y < -3)
					comp_y = (box_y+comp_y);
				if(comp_y > +3)
					comp_y = (-box_y+comp_y);
				
			 }
				
			if((periodic_z == true))
			 {
				if(comp_z < -3)
					comp_z = (box_z+comp_z);
				if(comp_z > +3)
					comp_z = (-box_z+comp_z);
			 }
				 
			//check if bond allowed
			if(BondArray[KoordBondArray(comp_x, comp_y, comp_z)] == true)
			{
				System.out.println("bonds corrupted. ("+comp_x+", "+comp_y+", "+comp_z+") ");
				System.out.println("bonds corrupted. mono1("+mono1+") mono2("+mono2+")");
				BFMFileSaver ErrorSaver = new BFMFileSaver();
				ErrorSaver.DateiAnlegen(System.getProperty("user.home")+"/ErrorBFM.txt", true);
				ErrorSaver.setzeZeile(Filename+" ...bonds corrupted");
				ErrorSaver.DateiSchliessen();
				System.exit(1);
			}
			
		}
		
	}
	
	
	public void parsingFile()
	{
		String line;
		boolean weiterladen = true;
		
		
		
		do { 
		
		if ((line = loadFile.readNextLine()) == "") 
			weiterladen = false;
		
		switch(Parser.parse(line))
		{
			case -1:weiterladen = false;
					break;
		
			case 1: NrOfMonomers = Parser.value;
					Attributes = new int[NrOfMonomers+1];
					break;
					
			case 2: startsystem = Parser.value;
					//Max_Connectivity = Parser.value;
					break;
			
			case 3: box_x = Parser.value;
					break;
					
			case 4: box_y = Parser.value;
					break;
					
			case 5: box_z = Parser.value;
					break;
					
			case 6: periodic_x = (Parser.value != 0);
						if(periodic_x == true)
							JumpsOver_X = new int[NrOfMonomers+1];
					break;
					
			case 7: periodic_y = (Parser.value != 0);
						if(periodic_y == true)
							JumpsOver_Y = new int[NrOfMonomers+1];
					break;
					
			case 8: periodic_z = (Parser.value != 0);
						if(periodic_z == true)
							JumpsOver_Z = new int[NrOfMonomers+1];
					break;
					
			case 9: CompareBondvectorSet();
					break;
			
			case 10: Add_Bonds();
					 break;
			
			case 11: Remove_Bonds();
			 		 break;
			 		 
			case 12: SetAttributes();
			 		 break;
			 
			case 13: SetAdditionalBonds();
			 		 break;
			 
			case 14: //System.out.println("nrofmcs:" +NrOfMCS);
					StringTokenizer st = new StringTokenizer(Parser.TokenString, ": ");
				
					if ((firstInit == false) && (NrOfMCS != 1))
					{
					 DetermineSystem();
					 firstInit = true;
					 NrOfMCS--;
					}
					
					else
					 { 
					
					 if(NrOfMCS > 1)
					 {
						NrOfMCS--;
						
						//System.out.println("Zeitermittlung");
						 
						 String str = st.nextToken();
						 //System.out.println(str);
						//Marcos Zeitschreibung
						 if(str.startsWith("!mcs"))
						 {
							 MCSTime =  Long.parseLong(st.nextToken()) ;
							 wechselz = (int) ( MCSTime/1000000L);
							 internz = (int) ( MCSTime % 100L);
							 zaehler = (int) (( MCSTime-wechselz*1000000L- internz)/100L);
						 }//meine
						 else
						 {
							 MCSTime =  Long.parseLong(str) ;
							 wechselz = (int) ( MCSTime/1000000L);
							 internz = (int) ( MCSTime % 100L);
							 zaehler = (int) (( MCSTime-wechselz*1000000L- internz)/100L);
						 }
						 DeltaT=MCSTime-DeltaTStart;
					     DeltaTStart=MCSTime;
						
					   
						
						do
						{
							line = loadFile.readNextLine();
							
						}while(!line.equals(""));
						
					 }
			
					 else
					 {
						 
						 System.out.println("Zeitermittlung");
						 
						 String str = st.nextToken();
						 System.out.println(str);
						 System.out.println("Zeitermittlung " + str);
						//Marcos Zeitschreibung
						 if(str.length() == 5)
						 {
							 
							 MCSTime =  Long.parseLong(st.nextToken()) ;
							 wechselz = (int) ( MCSTime/1000000L);
							 internz = (int) ( MCSTime % 100L);
							 zaehler = (int) (( MCSTime-wechselz*1000000L- internz)/100L);
						 }//meine
						 else
						 {
							 MCSTime =  Long.parseLong(str) ;
							 wechselz = (int) ( MCSTime/1000000L);
							 internz = (int) ( MCSTime % 100L);
							 zaehler = (int) (( MCSTime-wechselz*1000000L- internz)/100L);
						 }
						 DeltaT=MCSTime-DeltaTStart;
					     DeltaTStart=MCSTime;
						// MCSTime = Long.parseLong(st.nextToken().substring(5));
						// wechselz = (int) ( MCSTime/1000000L);
						// internz = (int) ( MCSTime % 100L);
						 //zaehler = (int) (( MCSTime-wechselz*1000000L- internz)/100L);
						 //MCSTime = Long.parseLong(Parser.TokenString);
					// wechselz = (int) ( Long.parseLong(Parser.TokenString)/1000000L);
					// internz = (int) ( Long.parseLong(Parser.TokenString) % 100L);
					// zaehler = (int) (( Long.parseLong(Parser.TokenString)-wechselz*1000000L- internz)/100L);
					 //System.out.println("w:" + wechselz +" z:"+zaehler+ " i:"+internz );
					 
					 DetermineSystem();
					 

					/* int ChainStartCounter=0;
					 
					while (st.hasMoreTokens()) {
						
						if (periodic_x == true)
							JumpsOver_X[neueKetten.get(ChainStartCounter)]=Integer.parseInt(st.nextToken());
						
						if (periodic_y == true)
							JumpsOver_Y[neueKetten.get(ChainStartCounter)]=Integer.parseInt(st.nextToken());
						
						if (periodic_z == true)
							JumpsOver_Z[neueKetten.get(ChainStartCounter)]=Integer.parseInt(st.nextToken());
						
						ChainStartCounter++;
				     }
					
					if((ChainStartCounter > 0) && (ChainStartCounter != neueKetten.size()))
					{
						System.out.println("invalid chain jumps... exiting");
						System.exit(1);
					}
						
					
					if(ChainStartCounter > 0)
						{
							
							System.out.println("_countingJumps");
							
							
							
							
								for(int Ket=0; Ket < neueKetten.size(); Ket++)
								{
									for(int mono = neueKetten.get(Ket); mono < (Ket != (neueKetten.size()-1) ? neueKetten.get(Ket+1) : NrOfMonomers+1); mono++)
									{
										
									
									if((neueKetten.get(Ket) != mono) && (periodic_x == true))
										JumpsOver_X[mono]+=JumpsOver_X[neueKetten.get(Ket)]; 
									
									if((neueKetten.get(Ket) != mono) && (periodic_y == true))
										JumpsOver_Y[mono]+=JumpsOver_Y[neueKetten.get(Ket)]; 
									
									if((neueKetten.get(Ket) != mono) && (periodic_z == true))
										JumpsOver_Z[mono]+=JumpsOver_Z[neueKetten.get(Ket)]; 
									
									}
								}
								
							
						}*/
					 //weiterladen = false;
					 }
					 }
					 break;
					 
			case 15: efield_on = (Parser.value != 0);
					 break;
			
			case 16: eforce = Parser.doublevalue;
					 break;
			
			case 17: efield_x = (Parser.value != 0);
					 break;
					
			case 18: efield_y = (Parser.value != 0);
		  			 break;
	
			case 19: efield_z = (Parser.value != 0);
					 break;
	
			case 20: absorption_on = (Parser.value != 0);
					 break;
				
			case 21: absorption_force = Parser.doublevalue;
					 break;	 
					 
			case 22: absorption_x = (Parser.value != 0);
			 		 break;
			
			case 23: absorption_y = (Parser.value != 0);
					 break;

			case 24: absorption_z = (Parser.value != 0);
					 break;
			 
			case 25: monomers_fixed = (Parser.value != 0);
					 break;
					 
			case 26: excluded_volume = (Parser.value != 0);
					 break;
					 
			case 27: NrOfStars = Parser.value;
					 break;
					 
			case 28: NrOfMonomersPerStarArm = Parser.value;
					 break;
					 
			case 29: NrOfHeparin= Parser.value;
					 break;
					 
			case 30: NrOfCrosslinker = Parser.value;
					break;
			 
			case 31: CrosslinkerFunctionality= Parser.value;
					break;		 
			
			case 32: NrOfChargesPerHeparin = Parser.value;
					break;
	 
			case 33: ReducedTemperature= Parser.value;
					break;		

	
		}
		
		}while(weiterladen == true);
	}
	
	public double GetDistance(int mono1, int mono2)
	{
		double z = Math.sqrt((PolymerKoordinaten[mono1][0]-PolymerKoordinaten[mono2][0])*(PolymerKoordinaten[mono1][0]-PolymerKoordinaten[mono2][0]) + (PolymerKoordinaten[mono1][1]-PolymerKoordinaten[mono2][1])*(PolymerKoordinaten[mono1][1]-PolymerKoordinaten[mono2][1]) + (PolymerKoordinaten[mono1][2]-PolymerKoordinaten[mono2][2])*(PolymerKoordinaten[mono1][2]-PolymerKoordinaten[mono2][2]) );
		
		if ((z < 2.0) || (z > 3.17))
			{
				System.out.println("Bond too big between "+mono1+" "+mono2+ " : " +z);
				System.exit(1);
			}
		
		return z; 
	}
	
	public double GetDistanceWithoutCheck(int mono1, int mono2)
	{
		double z = Math.sqrt((PolymerKoordinaten[mono1][0]-PolymerKoordinaten[mono2][0])*(PolymerKoordinaten[mono1][0]-PolymerKoordinaten[mono2][0]) + (PolymerKoordinaten[mono1][1]-PolymerKoordinaten[mono2][1])*(PolymerKoordinaten[mono1][1]-PolymerKoordinaten[mono2][1]) + (PolymerKoordinaten[mono1][2]-PolymerKoordinaten[mono2][2])*(PolymerKoordinaten[mono1][2]-PolymerKoordinaten[mono2][2]) );
		
		/*if ((z < 2.0) || (z > 3.17))
			{
				System.out.println("Bond too big between "+mono1+" "+mono2+ " : " +z);
				System.exit(1);
			}*/
		
		return z; 
	}
	
	public void CompareBondvectorSet()
	{
		/*String line = ""; //= loadFile.readNextLine();
			
		//if (line == null) 
			//return;
		
		
		int x,y,z, ascii;
		
		do
		{
			line = loadFile.readNextLine();
			StringTokenizer st = new StringTokenizer(line, " :");
			
		
		while (st.hasMoreTokens()) {
			
			x = Integer.parseInt(st.nextToken());
			y = Integer.parseInt(st.nextToken());
			z = Integer.parseInt(st.nextToken());
			ascii = Integer.parseInt(st.nextToken());
			
			if (GetIntegerOfBondvector(x,y,z) == ascii)
			{
				;//System.out.println("stimmt  x:" +x + " y:" +y+ " z:" + " ascii:"+ascii);
			}
			else {
				System.err.println("passt nicht");
				System.exit(1);
			}
	     }//zeile.trim().length() != 0)
		}while(!line.equals(""));
		
		*/
		String line = ""; //= loadFile.readNextLine();
		
		//if (line == null) 
			//return;
		
		
		int x,y,z, ascii;
		
		int zahler_bonds = 0;
		for (int i = 0; i < BondArray.length; i++)
			if (BondArray[i]== false)
				zahler_bonds++;
		
		System.out.println("Bonds vor File: " + zahler_bonds);
		
		zahler_bonds = 0;
		
		do
		{
			line = loadFile.readNextLine();
			StringTokenizer st = new StringTokenizer(line, " :");
			
		
		while (st.hasMoreTokens()) {
			
			x = Integer.parseInt(st.nextToken());
			y = Integer.parseInt(st.nextToken());
			z = Integer.parseInt(st.nextToken());
			ascii = Integer.parseInt(st.nextToken());
			
			System.out.println("stimmt  x:" +x + " y:" +y+ " z:" + z + " ascii:"+ascii);
			
			if (BondArray[KoordBondArray(x,y,z)] == true)
				zahler_bonds++;
			
			BondArray[KoordBondArray(x,y,z)] = false;
			
			BondAsciiArray[KoordBondArray(x,y,z)] = ascii;
			
			Ascii2BondArray[ascii] = koord(3+x, 3+y, 3+z);
			/*if (GetIntegerOfBondvector(x,y,z) == ascii)
			{
	        System.out.println("stimmt  x:" +x + " y:" +y+ " z:" + " ascii:"+ascii);
			}
			else System.out.println("passt nicht");*/
	     }//zeile.trim().length() != 0)
		}while(!line.equals(""));
		
		System.out.println("Bonds nach File1: " + zahler_bonds);
		
		zahler_bonds = 0;
		
		for (int i = 0; i < BondArray.length; i++)
			if (BondArray[i]== false)
				zahler_bonds++;
		
		System.out.println("Bonds nach File2: " + zahler_bonds);
	}
	
	
	
	public void SetAttributes()
	{
		String line = ""; 
		
		
		int NumberBeginn = 0;
		int NumberEnd = 0;
		int Attribute = 0;
	
		do
		{
			line = loadFile.readNextLine();
			StringTokenizer st = new StringTokenizer(line, " -:");
			
		
		while (st.hasMoreTokens()) {
			
			NumberBeginn = Integer.valueOf(st.nextToken()).intValue();
			NumberEnd = Integer.valueOf(st.nextToken()).intValue();
			Attribute = Integer.valueOf(st.nextToken()).intValue();
			
			
	       // System.out.println("type  :" +Attribute + " a:" + NumberBeginn + " b:" + NumberEnd);
			//types.put(Type, (NumberBeginn+ " " + NumberEnd)); 
	        for(int ret = NumberBeginn; ret <= NumberEnd; ret++)
	        	Attributes[ret]= Attribute;
	        	//Ladungen[ret]= ((Attribute == 2) ? -1 : 1) ;
			
			
	       
			
	     }
		}while(!line.equals(""));
	}
	
	public void SetAdditionalBonds()
	{
		String line = ""; //= loadFile.readNextLine();
		
		//if (line == null) 
			//return;
		int NumberBeginn = 0;
		int NumberEnd = 0;
	
		//System.out.println(" adddbond:");
		do
		{
			line = loadFile.readNextLine();
			StringTokenizer st = new StringTokenizer(line, " ");
			
		
		while (st.hasMoreTokens()) {
			
			NumberBeginn = Integer.parseInt(st.nextToken());
			NumberEnd = Integer.parseInt(st.nextToken());
		
			
			
	       // System.out.println(" a:" + NumberBeginn + " b:" + NumberEnd);
			//additionalbonds.add(new Integer(Intergerwert(NumberBeginn, NumberEnd))); 
	        additionalbonds.add(Longwert(NumberBeginn, NumberEnd)); 
			
	     }//zeile.trim().length() != 0)
		}while(!line.equals(""));
	}
	
	
	public void Add_Bonds()
	{
		String line = ""; 
		
		int NumberBeginn = 0;
		int NumberEnd = 0;
	
		//System.out.println(" add_bonds:");
		do
		{
			line = loadFile.readNextLine();
			StringTokenizer st = new StringTokenizer(line, " ");
			
		
		while (st.hasMoreTokens()) {
			
			NumberBeginn = Integer.parseInt(st.nextToken());
			NumberEnd = Integer.parseInt(st.nextToken());
		
			
			
	       // System.out.println("add_bonds   a:" + NumberBeginn + " b:" + NumberEnd);
			//additionalbonds.add(new Integer(Intergerwert(NumberBeginn, NumberEnd))); 
	        additionalbonds.add(Longwert(NumberBeginn, NumberEnd)); 
	        addedBondsBetweenFrames.add(Longwert(NumberBeginn, NumberEnd)); 
	     }//zeile.trim().length() != 0)
		}while(!line.equals(""));
		
		
	}
	
	
	public void Remove_Bonds()
	{
		String line = ""; 
		
		int NumberBeginn = 0;
		int NumberEnd = 0;
		
		long BondInteger1 = 0;
		long BondInteger2 = 0;
	
		//System.out.println(" remove_bonds:");
		do
		{
			line = loadFile.readNextLine();
			StringTokenizer st = new StringTokenizer(line, " ");
			
		
		while (st.hasMoreTokens()) {
			
			NumberBeginn = Integer.parseInt(st.nextToken());
			NumberEnd = Integer.parseInt(st.nextToken());
		
			BondInteger1 = Longwert(NumberBeginn, NumberEnd);
			BondInteger2 = Longwert(NumberEnd, NumberBeginn);
			
	        //System.out.println("remove_bonds   a:" + NumberBeginn + " b:" + NumberEnd);
			//additionalbonds.add(new Integer(Intergerwert(NumberBeginn, NumberEnd))); 
	        
	        //bond in diesen array- keine neue kette noetig
	        for(int it = 0; it < additionalbonds.size(); it++)
	        {
	        	if(additionalbonds.get(it) == BondInteger1)
	        		{
	        			additionalbonds.remove(it);
	        			break;
	        		}
	        	else if (additionalbonds.get(it) == BondInteger2)
	        		{
	        			additionalbonds.remove(it);
	        			break;
	        		}
	        }
	        
	        //bond in diesen array- immer neue kette
	        for(int it = 0; it < bonds.size(); it++)
	        {
	        	if(bonds.get(it) == BondInteger1)
	        		{
	        			bonds.remove(it);
	        			
	        			//da lineare Anordnung in File, groessere nummer neue kette
	        			if(NumberBeginn < NumberEnd)
	        				neueKetten.add(NumberEnd);
	        			
	        			if(NumberBeginn > NumberEnd)
	        				neueKetten.add(NumberBeginn);
	        			
	        			break;
	        		}
	        	else if (bonds.get(it) == BondInteger2)
	        		{
	        			bonds.remove(it);
	        			
	        			//da lineare Anordnung in File, groessere nummer neue kette
	        			if(NumberBeginn < NumberEnd)
	        				neueKetten.add(NumberEnd);
	        			
	        			if(NumberBeginn > NumberEnd)
	        				neueKetten.add(NumberBeginn);
	        			
	        			break;
	        		}
	        }
	        	
	       
			
	     }//zeile.trim().length() != 0)
		}while(!line.equals(""));
	}
	
	public IntArrayList GetBeginnChain()
	{
		return neueKetten;
	}
	
	public void DetermineSystem()
	{
		if(boxBitSet == null)
			boxBitSet = new IntBitSet(box_x*box_y*box_z);
		
		boxBitSet.clear();
		
		if (isDetermined == false)
		{
		Polymersystem = new int [NrOfMonomers+1];
		
		PolymerKoordinaten = new int [NrOfMonomers+1] [3];
		
		dummysystem  = new int [NrOfMonomers+1];
		
		
		System.out.println("Determin System...");
		String line = ""; //= loadFile.readNextLine();
		
		//if (line == null) 
			//return;
		int x,y,z;
		String StringOfBonds = "";
		
		int aktuellesMono = 1;
		
		int bind = 0;
		
		
	
		do
		{
			line = loadFile.readNextLine();
			
			//System.out.println("line :" + line);
			//StringTokenizer st;
			
			
			StringTokenizer st = new StringTokenizer(line, " ");
			
		
		while (st.hasMoreTokens()) {
			
			x = Integer.parseInt(st.nextToken());
			y = Integer.parseInt(st.nextToken());
			z = Integer.parseInt(st.nextToken());
			//ascii = Integer.parseInt(st.nextToken());
			StringOfBonds = st.nextToken("");
		
			Polymersystem[aktuellesMono]=koord(x,y,z);
			GitterSetzen(aktuellesMono);
			
			if (periodic_x == true)
				JumpsOver_X[aktuellesMono]=0;
			
			if (periodic_y == true)
				JumpsOver_Y[aktuellesMono]=0;
			
			if (periodic_z == true)
				JumpsOver_Z[aktuellesMono]=0;
			
			neueKetten.add(aktuellesMono);
			
			aktuellesMono++;
			
			
	        //System.out.println(" x:" + x + " y:" + y + " z:" + z +"  S: " +StringOfBonds);
			
	        for (int zk = 1; zk < StringOfBonds.length();zk++)
  		  {
	        	//System.out.println("länge" +StringOfBonds.length());
	        	//System.out.println("zk :" +zk +"   "+ ((int) StringOfBonds.charAt(zk)));
  			  
  				 // bind = ASCIItoBond(StringOfBonds.charAt(zk));
  				  bind = Ascii2BondArray[(int) StringOfBonds.charAt(zk)];
  				 // System.out.println("xw: " + xwert(Polymersystem[aktuellesMono-1])+ "     yw: "+ ywert(Polymersystem[aktuellesMono-1]) + "        zw: " + zwert(Polymersystem[aktuellesMono-1]));
  				  //Polymersystem[aktuellesMono] = koord((box_x + xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3) % box_x, (box_y + ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3) % box_y, (box_z + zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3) % box_z);
  				 // GitterSetzen(aktuellesMono);
  				  
  				 int comp_x = xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ;
  				 int comp_y = ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3 ;
  				 int comp_z = zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3 ;
   				
  				 //Test if new monomer position allowed
  				 if((periodic_x == false) && ((comp_x < 0) || (comp_x > (box_x-2))))
  				 {
  					 System.out.println("xwert exceeds the box. Exiting...");
  					BFMFileSaver ErrorSaver = new BFMFileSaver();
					ErrorSaver.DateiAnlegen("/home/users/dockhorn/ErrorBFM.txt", true);
					ErrorSaver.setzeZeile(Filename+" ...xwert exceeds the box");
					ErrorSaver.DateiSchliessen();
  					 System.exit(1);
  				 }
  				 
  				 if((periodic_y == false) && ((comp_y < 0) || (comp_y > (box_y-2))))
  				 {
  					 System.out.println("ywert exceeds the box. Exiting...");
  					BFMFileSaver ErrorSaver = new BFMFileSaver();
					ErrorSaver.DateiAnlegen("/home/users/dockhorn/ErrorBFM.txt", true);
					ErrorSaver.setzeZeile(Filename+" ...ywert exceeds the box");
					ErrorSaver.DateiSchliessen();
  					 System.exit(1);
  				 }
  				 
  				 if((periodic_z == false) && ((comp_z < 0) || (comp_z > (box_z-2))))
  				 {
  					 System.out.println("zwert exceeds the box. Exiting...");
  					BFMFileSaver ErrorSaver = new BFMFileSaver();
					ErrorSaver.DateiAnlegen("/home/users/dockhorn/ErrorBFM.txt", true);
					ErrorSaver.setzeZeile(Filename+" ...zwert exceeds the box");
					ErrorSaver.DateiSchliessen();
  					 System.exit(1);
  				 }
  				  
  				 //Whenever box is periodic fold back
  				if((periodic_x == true))
 				 {
  					comp_x = (box_x+comp_x)%box_x;
 				 }
  				
  				if((periodic_y == true))
				 {
 					comp_y = (box_y+comp_y)%box_y;
				 }
  				
  				if((periodic_z == true))
				 {
					comp_z = (box_z+comp_z)%box_z;
				 }
  				 
  				
  				//Set new postion
  				Polymersystem[aktuellesMono] = koord( comp_x ,  comp_y, comp_z);
  				GitterSetzen(aktuellesMono);
  				  
  				  SetBond(aktuellesMono-1, aktuellesMono);
  				  
  				if (periodic_x == true)
  				{
  					if((xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3) >= box_x)
  						JumpsOver_X[aktuellesMono]=JumpsOver_X[aktuellesMono-1]+1;
  					
  					if((xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3) < 0)
  						JumpsOver_X[aktuellesMono]=JumpsOver_X[aktuellesMono-1]-1;
  					
  				}
					
  				if (periodic_y == true)
  				{
  					if((ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3) >= box_y)
  						JumpsOver_Y[aktuellesMono]=JumpsOver_Y[aktuellesMono-1]+1;
  					
  					if((ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3) < 0)
  						JumpsOver_Y[aktuellesMono]=JumpsOver_Y[aktuellesMono-1]-1;
  					
  				}
  				
  				if (periodic_z == true)
  				{
  					if((zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3) >= box_z)
  						JumpsOver_Z[aktuellesMono]=JumpsOver_Z[aktuellesMono-1]+1;
  					
  					if((zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3) < 0)
  						JumpsOver_Z[aktuellesMono]=JumpsOver_Z[aktuellesMono-1]-1;
  					
  				}
  				  
  				  
  				  aktuellesMono++;
  				  
  				  
  				 
  		  }
  		  
	       
	        //System.out.println(1+ "  DetSys1 " +JumpsOver_Z[1]);
	     }
			//zeile.trim().length() != 0)
		}while(!line.equals(""));
		
		isDetermined = true;
		
		if(aktuellesMono != (NrOfMonomers+1))
		{
			System.out.println("number of monomers inside frame != NrOfMonomers. Exiting...");
			BFMFileSaver ErrorSaver = new BFMFileSaver();
			ErrorSaver.DateiAnlegen("/home/users/dockhorn/ErrorBFM.txt", true);
			ErrorSaver.setzeZeile(Filename+" ...number of monomers inside frame != NrOfMonomers");
			ErrorSaver.DateiSchliessen();
			System.exit(1);
		}
		
		}
		else{
			
			System.out.println("Determin System2...");
			String line = ""; //= loadFile.readNextLine();
			
			//if (line == null) 
				//return;
			int x,y,z;
			String StringOfBonds = "";
			
			int aktuellesMono = 1;
			
			int bind = 0;
			
			
		
			do
			{
				line = loadFile.readNextLine();
				
				//System.out.println("line :" + line);
				//StringTokenizer st;
				
				
				StringTokenizer st = new StringTokenizer(line, " ");
				
			
			while (st.hasMoreTokens()) {
				
				x = Integer.parseInt(st.nextToken());
				y = Integer.parseInt(st.nextToken());
				z = Integer.parseInt(st.nextToken());
				//ascii = Integer.parseInt(st.nextToken());
				StringOfBonds = st.nextToken("");
			
				Polymersystem[aktuellesMono]=koord(x,y,z);
				
				GitterSetzen(aktuellesMono);
				
				//System.out.println(aktuellesMono+ "   Det2A " +JumpsOver_Z[aktuellesMono]);
				
				
				aktuellesMono++;
				
		        //System.out.println(" x:" + x + " y:" + y + " z:" + z +"  S: " +StringOfBonds);
				
		        for (int zk = 1; zk < StringOfBonds.length();zk++)
	  		  {
		        	//System.out.println("länge" +StringOfBonds.length());
		        	//System.out.println("zk :" +zk +"   "+ ((int) StringOfBonds.charAt(zk)));
	  			  
	  				 // bind = ASCIItoBond(StringOfBonds.charAt(zk));
	  				  bind = Ascii2BondArray[(int) StringOfBonds.charAt(zk)];
	  				 // System.out.println("xw: " + xwert(Polymersystem[aktuellesMono-1])+ "     yw: "+ ywert(Polymersystem[aktuellesMono-1]) + "        zw: " + zwert(Polymersystem[aktuellesMono-1]));
	  				  
	  				 // Polymersystem[aktuellesMono] = koord((box_x + xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3) % box_x, (box_y + ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3) % box_y, (box_z + zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3) % box_z);
	   				 int comp_x = xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ;
	  				 int comp_y = ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3 ;
	  				 int comp_z = zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3 ;
	   				
	  				 //Test if new monomer position allowed
	  				 if((periodic_x == false) && ((comp_x < 0) || (comp_x > (box_x-2))))
	  				 {
	  					 System.out.println("xwert exceeds the box. Exiting...");
	  					BFMFileSaver ErrorSaver = new BFMFileSaver();
						ErrorSaver.DateiAnlegen("/home/users/dockhorn/ErrorBFM.txt", true);
						ErrorSaver.setzeZeile(Filename+" ...xwert exceeds the box");
						ErrorSaver.DateiSchliessen();
	  					 System.exit(1);
	  				 }
	  				 
	  				 if((periodic_y == false) && ((comp_y < 0) || (comp_y > (box_y-2))))
	  				 {
	  					 System.out.println("ywert exceeds the box. Exiting...");
	  					BFMFileSaver ErrorSaver = new BFMFileSaver();
						ErrorSaver.DateiAnlegen("/home/users/dockhorn/ErrorBFM.txt", true);
						ErrorSaver.setzeZeile(Filename+" ...ywert exceeds the box");
						ErrorSaver.DateiSchliessen();
	  					 System.exit(1);
	  				 }
	  				 
	  				 if((periodic_z == false) && ((comp_z < 0) || (comp_z > (box_z-2))))
	  				 {
	  					 System.out.println("zwert exceeds the box. Exiting...");
	  					BFMFileSaver ErrorSaver = new BFMFileSaver();
						ErrorSaver.DateiAnlegen("/home/users/dockhorn/ErrorBFM.txt", true);
						ErrorSaver.setzeZeile(Filename+" ...zwert exceeds the box");
						ErrorSaver.DateiSchliessen();
	  					 System.exit(1);
	  				 }
	  				  
	  				 //Whenever box is periodic fold back
	  				if((periodic_x == true))
	 				 {
	  					comp_x = (box_x+comp_x)%box_x;
	 				 }
	  				
	  				if((periodic_y == true))
					 {
	 					comp_y = (box_y+comp_y)%box_y;
					 }
	  				
	  				if((periodic_z == true))
					 {
						comp_z = (box_z+comp_z)%box_z;
					 }
	  				 
	  				
	  				//Set new postion
	  				Polymersystem[aktuellesMono] = koord( comp_x ,  comp_y, comp_z);
	  				GitterSetzen(aktuellesMono);
	  				 // Polymersystem[aktuellesMono] = koord((box_x + xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3) % box_x, (box_y + ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3) % box_y, (box_z + zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3) % box_z);
	   				 // Polymersystem[aktuellesMono] = koord( xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ,  ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3, zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3);
	  				  
	  				  
	  				  
	  				  SetBond(aktuellesMono-1, aktuellesMono);
	  				  
	  			//	System.out.println(1+ "  Det2C " +JumpsOver_Z[1]);
	  				
	  				if (periodic_x == true)
	  				{
	  					if((xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3) >= box_x)
	  						JumpsOver_X[aktuellesMono]=JumpsOver_X[aktuellesMono-1]+1;
	  					
	  					if((xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3) < 0)
	  						JumpsOver_X[aktuellesMono]=JumpsOver_X[aktuellesMono-1]-1;
	  					
	  				}
						
	  				if (periodic_y == true)
	  				{
	  					if((ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3) >= box_y)
	  						JumpsOver_Y[aktuellesMono]=JumpsOver_Y[aktuellesMono-1]+1;
	  					
	  					if((ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3) < 0)
	  						JumpsOver_Y[aktuellesMono]=JumpsOver_Y[aktuellesMono-1]-1;
	  					
	  				}
	  				
	  				if (periodic_z == true)
	  				{
	  					if((zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3) >= box_z)
	  						JumpsOver_Z[aktuellesMono]=JumpsOver_Z[aktuellesMono-1]+1;
	  					
	  					if((zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3) < 0)
	  						JumpsOver_Z[aktuellesMono]=JumpsOver_Z[aktuellesMono-1]-1;
	  					
	  				}
	  				  
	  				//System.out.println(aktuellesMono+ "  Det2C " +JumpsOver_Z[aktuellesMono]);
	  				//System.out.println(1+ "  Det2C " +JumpsOver_Z[1]);
	  				
	  				aktuellesMono++;
	  				  
	  				  
	  				 
	  		  }
	  		  
		        //System.out.println(1+ "  Det2B " +JumpsOver_Z[1]);
				
		        
		     }
				//zeile.trim().length() != 0)
			}while(!line.equals(""));
			
			if(aktuellesMono != (NrOfMonomers+1))
			{
				System.out.println("number of monomers inside frame != NrOfMonomers. Exiting...");
				BFMFileSaver ErrorSaver = new BFMFileSaver();
				ErrorSaver.DateiAnlegen("/home/users/dockhorn/ErrorBFM.txt", true);
				ErrorSaver.setzeZeile(Filename+" ...number of monomers inside frame != NrOfMonomers");
				ErrorSaver.DateiSchliessen();
				System.exit(1);
			}
			
		}//end if
		
		
		//System.out.println("start" + startsystem);
		
		int hj=0;
		for(int i = 0; i < box_x; i++)
			for (int k = 0; k < box_y; k++)
				for (int l = 0; l < box_z; l++)
					if (boxBitSet.get(i + box_x*k + box_x*box_y*l) == true)
						hj++;
		
		System.out.println("besaetzte plaetze\t" +hj  +" von " + (NrOfMonomers*8));
		
		if(hj !=  (NrOfMonomers*8))
		{
			System.out.println("invalid number of occupied lattice points. Exiting...");
			BFMFileSaver ErrorSaver = new BFMFileSaver();
			ErrorSaver.DateiAnlegen("/home/users/dockhorn/ErrorBFM.txt", true);
			ErrorSaver.setzeZeile(Filename+" ...invalid number of occupied lattice points");
			ErrorSaver.DateiSchliessen();
			System.exit(1);
		}
		
		//System.out.println("mono" + (NrOfMonomers+1));
	}
	
	private int KoordBondArray(int x, int y, int z)
	{
		return (x & 7) + ((y&7) << 3) + ((z&7) << 6);
	}
	
	public int xwert (int ds) {
		/** Umwandlung von int-wert zur x-Koordinate (0...boxbreite-2)Kernpunkt*/
		return (ds & 1023);//(ds & 1023);
	}
	
	public int ywert (int ds) {
		/** Umwandlung von int-wert zur y-Koordinate (0...boxbreite-2)Kernpunkt*/
		return (ds & 1047552) >> 10;
	}
	
	public int zwert (int ds) {
		/** Umwandlung von int-wert zur z-Koordinate (0...boxbreite-2)Kernpunkt*/
		return (ds & 1072693248) >> 20;
	}
	
	public int koord (int xwert,int ywert, int zwert) {
		/** Umwandlung der Koordinate in int-Wert */
		return xwert + (ywert << 10) + (zwert << 20) ;
	}
	
	public void SetBond(int mono1, int mono2)
	   {
		   //bonds.add(new Integer(Intergerwert(mono1, mono2)));//.addElement(new Integer(Intergerwert(mono1, mono2)));
		   bonds.add(Longwert(mono1, mono2));//.addElement(new Integer(Intergerwert(mono1, mono2)));
	   }
	   
	public long Longwert(int mono1, int mono2)
	   {
		   return (mono1 + ((0L+mono2) << 31));
	   }
	   
	 private int getMono1Nr(long obj)//int mono)
	   {
		   return ((int) ( obj & 2147483647));   
	   }
	   
	private int getMono2Nr(long obj)//(int mono)
	   {   
		return (int) (( (obj >> 31) & 2147483647));
	   }
	
	public int GetIntegerOfBondvector(int diffx, int diffy, int diffz)
	{
		
		if (diffx == 2)
		{	if (diffy == 0)
			{
				if (diffz == 0)
					return  17;	//check
				if (diffz == 1)
					return  36;	//check
				if (diffz == -1)
					return  39;	//check
			}
		
			if (diffy == 1)
			{
				if (diffz == 0)
					return  23;	//check
				if (diffz == 1)
					return  47;	//check
				if (diffz == -1)
					return  59;	//check
				if (diffz == 2)
					return  78;	//check
				if (diffz == -2)
					return  81;	//check
			}
			
			if (diffy == -1)
			{
				if (diffz == 0)
					return  29;	//check
				if (diffz == 1)
					return  53;	//check
				if (diffz == 2)
					return  90;	//check
				if (diffz == -1)
					return  65;	//check
				if (diffz == -2)
					return  93;	//check
			}
			
			if (diffy == 2)
			{
				if (diffz == 1)
					return  77;	//check
				if (diffz == -1)
					return  89;	//check
			}
			
			if (diffy == -2)
			{
				if (diffz == 1)
					return  83;	//check
				if (diffz == -1)
					return  95;	//check
			}
		}
		
		if (diffx == -2)
		{	if (diffy == 0)
			{
				if (diffz == 0)
					return  20;	//check
				if (diffz == 1)
					return  42;	//check
				if (diffz == -1)
					return  45;	//check
				
			}
		
			if (diffy == 1)
			{
				if (diffz == 0)
					return  26;	//check
				if (diffz == 1)
					return  50;	//check
				if (diffz == 2)
					return  84;	//check
				if (diffz == -1)
					return  62;	//check
				if (diffz == -2)
					return  87;	//check
			}
			
			if (diffy == -1)
			{
				if (diffz == 0)
					return  32;	//check
				if (diffz == 1)
					return  56;	//check
				if (diffz == 2)
					return  96;	//check
				if (diffz == -1)
					return  68;	//check
				if (diffz == -2)
					return  99;	//check
			}
			
			if (diffy == 2)
			{
				if (diffz == 1)
					return  80;	//check
				if (diffz == -1)
					return  92;	//check
			}
			
			if (diffy == -2)
			{
				if (diffz == 1)
					return  86;	//check
				if (diffz == -1)
					return  98;	//check
			}
		}
		
		if (diffx == 3)
		{
			if (diffy == 0)
			{
				if (diffz == 0)
					return  71;	//check
				if (diffz == 1)
					return  114;	//check
				if (diffz == -1)
					return  117;	//check
			}
			if (diffy == 1)
			{
				if (diffz == 0)
					return  101;	//check
			}
			if (diffy == -1)
			{
				if (diffz == 0)
					return  107;	//check
			}
		}
		
		if (diffx == -3)
		{
			if (diffy == 0)
			{
				if (diffz == 0)
					return  74;	//check
				if (diffz == 1)
					return  120;	//check
				if (diffz == -1)
					return  123;	//check
			}
			if (diffy == 1)
			{
				if (diffz == 0)
					return  104;	//check
			}
			
			if (diffy == -1)
			{
				if (diffz == 0)
					return  110;	//check
			}
		}
		
		// y-abfrage
		
		if (diffy == 2)
		{	if (diffx == 0)
			{
				if (diffz == 0)
					return  19;	//check
				if (diffz == 1)
					return  25;	//check
				if (diffz == -1)
					return  31;	//check
			}
		
			if (diffx == 1)
			{
				if (diffz == 0)
					return  35;	//check
				if (diffz == 1)
					return  49;	//check
				if (diffz == 2)
					return  79;	//check
				if (diffz == -1)
					return  55;	//check
				if (diffz == -2)
					return  85;	//check
			}
			
			if (diffx == -1)
			{
				if (diffz == 0)
					return  38;	//check
				if (diffz == 1)
					return  61;	//check
				if (diffz == 2)
					return  91;	//check
				if (diffz == -1)
					return  67;	//check
				if (diffz == -2)
					return  97;	//check
			}
			
		}
		
		if (diffy == -2)
		{	if (diffx == 0)
			{
				if (diffz == 0)
					return  22;	//check
				if (diffz == 1)
					return  28;	//check
				if (diffz == -1)
					return  34;	//check
			}
		
			if (diffx == 1)
			{
				if (diffz == 0)
					return  41;	//check
				if (diffz == 1)
					return  52;	//check
				if (diffz == 2)
					return  82;	//check
				if (diffz == -1)
					return  58;	//check
				if (diffz == -2)
					return  88;	//check
			}
			
			if (diffx == -1)
			{
				if (diffz == 0)
					return  44;	//check
				if (diffz == 1)
					return  64;	//check
				if (diffz == 2)
					return  94;	//check
				if (diffz == -1)
					return  70;	//check
				if (diffz == -2)
					return  100;	//check
			}
			
		}
		
		if (diffy == 3)
		{
			if (diffx == 0)
			{
				if (diffz == 0)
					return  73;	//check
				if (diffz == 1)
					return  103;	//check
				if (diffz == -1)
					return  109;	//check
			}
			if (diffx == 1)
			{
				if (diffz == 0)
					return  113;	//check
			}
			if (diffx == -1)
			{
				if (diffz == 0)
					return  116;	//check
			}
		}
		
		if (diffy == -3)
		{
			if (diffx == 0)
			{
				if (diffz == 0)
					return  76;	//check
				if (diffz == 1)
					return  106;	//check
				if (diffz == -1)
					return  112;	//check
			}
			if (diffx == 1)
			{
				if (diffz == 0)
					return  119;	//check
			}
			if (diffx == -1)
			{
				if (diffz == 0)
					return  122;	//check
			}
		}
		
		// z-abfrage
		
		if (diffz == 2)
		{	if (diffx == 0)
			{
				if (diffy == 0)
					return  18;	//check
				if (diffy == 1)
					return  37;	//check
				if (diffy == -1)
					return  40;	//check
			}
		
			if (diffx == 1)
			{
				if (diffy == 0)
					return  24;	//check
				if (diffy == 1)
					return  48;	//check
				if (diffy == -1)
					return  60;	//check
			}
			
			if (diffx == -1)
			{
				if (diffy == 0)
					return  30;	//check
				if (diffy == 1)
					return  54;	//check
				if (diffy == -1)
					return  66;	//check
			}
			
			
		}
		
		if (diffz == -2)
		{	if (diffx == 0)
			{
				if (diffy == 0)
					return  21;	//check
				if (diffy == 1)
					return  43;	//check
				if (diffy == -1)
					return  46;	//check
			}
		
			if (diffx == 1)
			{
				if (diffy == 0)
					return  27;	//check
				if (diffy == 1)
					return  51;	//check
				if (diffy == -1)
					return  63;	//check
			}
			
			if (diffx == -1)
			{
				if (diffy == 0)
					return  33;	//check
				if (diffy == 1)
					return  57;	//check
				if (diffy == -1)
					return  69;	//check
			}
			
		}
		
		if (diffz == 3)
		{
			if (diffx == 0)
			{
				if (diffy == 0)
					return  72;	//check
				if (diffy == 1)
					return  115;	//check
				if (diffy == -1)
					return  118;	//check
			}
			if (diffx == 1)
			{
				if (diffy == 0)
					return  102;	//check
			}
			
			if (diffx == -1)
			{
				if (diffy == 0)
					return  108;	//check
			}
		}
		
		if (diffz == -3)
		{
			if (diffx == 0)
			{
				if (diffy == 0)
					return  75;	//check
				if (diffy == 1)
					return  121;	//check
				if (diffy == -1)
					return  124;	//check
			}
			if (diffx == 1)
			{
				if (diffy == 0)
					return  105;	//check
			}
			if (diffx == -1)
			{
				if (diffy == 0)
					return  111;	//check
			}
		}
		
		
		
		
		// extended bonds
		
		
		
		// x-abfrage
		if (diffx == 2)
		{
			if (diffy == 2)
			{
				if (diffz == 2)
					return  125;	//check
				if (diffz == -2)
					return  129;	//check
			}
			if (diffy == -2)
			{
				if (diffz == 2)
					return  127;	//check
				if (diffz == -2)
					return  131;	//check
			}
			
		}
		
		if (diffx == -2)
		{
			if (diffy == 2)
			{
				if (diffz == 2)
					return  126;	//check
				if (diffz == -2)
					return  130;	//check
			}
			if (diffy == -2)
			{
				if (diffz == 2)
					return  128;	//check
				if (diffz == -2)
					return  132;	//check
			}
		}
		
		
		if (diffx == 3)
		{
			if (diffy == 1)
			{
				if (diffz == 1)
					return  133;	//check
				if (diffz == -1)
					return  145;	//check
			}
			if (diffy == -1)
			{
				if (diffz == 1)
					return  139;	//check
				if (diffz == -1)
					return  151;	//check
			}
		}
		
		if (diffx == -3)
		{
			if (diffy == 1)
			{
				if (diffz == 1)
					return  136;	//check
				if (diffz == -1)
					return  148;	//check
			}
			if (diffy == -1)
			{
				if (diffz == 1)
					return  142;	//check
				if (diffz == -1)
					return  154;	//check
			}
		}
		
		// z-abfrage
		
		if (diffz == 3)
		{
			if (diffx == 1)
			{
				if (diffy == 1)
					return  134;	//check
				if (diffy == -1)
					return  146;	//check
			}
			
			if (diffx == -1)
			{
				if (diffy == 1)
					return  140;	//check
				if (diffy == -1)
					return  152;	//check
			}
		}
		
		if (diffz == -3)
		{
			if (diffx == 1)
			{
				if (diffy == 1)
					return  137;	//check
				if (diffy == -1)
					return  149;	//check
			}
			
			if (diffx == -1)
			{
				if (diffy == 1)
					return  143;	//check
				if (diffy == -1)
					return  155;	//check
			}
		}
		
		// y-abfrage
		
		if (diffy == 3)
		{
			if (diffx == 1)
			{
				if (diffz == 1)
					return  135;	//check
				if (diffz == -1)
					return  141;	//check
			}
			if (diffx == -1)
			{
				if (diffz == 1)
					return  147;	//check
				if (diffz == -1)
					return  153;	//check
			}
		}
		
		if (diffy == -3)
		{
			if (diffx == 1)
			{
				if (diffz == 1)
					return  138;	//check
				if (diffz == -1)
					return  144;	//check
			}
			if (diffx == -1)
			{
				if (diffz == 1)
					return  150;	//check
				if (diffz == -1)
					return  156;	//check
			}
		}
		
// additional bonds 
		
		// x-abfrage
		if (diffx == 2)
		{
			if (diffy == 2)
			{
				if (diffz == 0)
					return  157;	//ch
			}
			
			if (diffy == -2)
			{
				if (diffz == 0)
					return  160;	//ch
			}
			
			if (diffy == 0)
			{
				if (diffz == 2)
					return  158;	//ch
				
				if (diffz == -2)
					return  164;	//ch
			}
			
		}
		
		if (diffy == 2)
		{
			if (diffx == 0)
			{
				if (diffz == 2)
					return  159;	//ch
				if (diffz == -2)
					return  162;	//ch
			}
		}
		
		
		if (diffx == -2)
		{
			if (diffy == -2)
			{
				if (diffz == 0)
					return  166;	//ch
				
			}
			
			if (diffy == 0)
			{
				if (diffz == -2)
					return  168;	//ch
				if (diffz == 2)
					return  165;	//ch
				
			}
			
			if (diffy == 2)
			{
				if (diffz == 0)
					return  161;	//ch
				
			}
			
		}
		
		if (diffy == -2)
		{
			if (diffx == 0)
			{
				if (diffz == -2)
					return  167;	//ch
				if (diffz == 2)
					return  163;	//ch
				
			}
		}
		//System.out.println("Error-no bond vector");
		//System.out.println("x :" +diffx +"  y :"+ diffy+ "  z :"+ diffz);
		return 0; //FEHLER
		
		
	}
	
	/* Berechnung des Bindungsvektors + (3,3,3)aus ASCII-Codes
	 * @return int vektor 
	 
	public int ASCIItoBond(char ascii) {
		
		if (ascii == 17)
			return koord(5,3,3);
		if (ascii == 18)
			return koord(3,3,5);
		if (ascii == 19)
			return koord(3,5,3);
		if (ascii == 20)
			return koord(1,3,3);
		if (ascii == 21)
			return koord(3,3,1);
		if (ascii == 22)
			return koord(3,1,3);
		if (ascii == 23)
			return koord(5,4,3);
		if (ascii == 24)
			return koord(4,3,5);
		if (ascii == 25)
			return koord(3,5,4);
		if (ascii == 26)
			return koord(1,4,3);
		if (ascii == 27)
			return koord(4,3,1);
		if (ascii == 28)
			return koord(3,1,4);
		if (ascii == 29)
			return koord(5,2,3);
		if (ascii == 30)
			return koord(2,3,5);
		if (ascii == 31)
			return koord(3,5,2);
		if (ascii == 32)
			return koord(1,2,3);
		if (ascii == 33)
			return koord(2,3,1);
		if (ascii == 34)
			return koord(3,1,2);
		if (ascii == 35)
			return koord(4,5,3);
		if (ascii == 36)
			return koord(5,3,4);
		if (ascii == 37)
			return koord(3,4,5);
		if (ascii == 38)
			return koord(2,5,3);
		if (ascii == 39)
			return koord(5,3,2);
		if (ascii == 40)
			return koord(3,2,5);
		if (ascii == 41)
			return koord(4,1,3);
		if (ascii == 42)
			return koord(1,3,4);
		if (ascii == 43)
			return koord(3,4,1);
		if (ascii == 44)
			return koord(2,1,3);
		if (ascii == 45)
			return koord(1,3,2);
		if (ascii == 46)
			return koord(3,2,1);
		if (ascii == 47)
			return koord(5,4,4);
		if (ascii == 48)
			return koord(4,4,5);
		if (ascii == 49)
			return koord(4,5,4);
		if (ascii == 50)
			return koord(1,4,4);
		if (ascii == 51)
			return koord(4,4,1);
		if (ascii == 52)
			return koord(4,1,4);
		if (ascii == 53)
			return koord(5,2,4);
		if (ascii == 54)
			return koord(2,4,5);
		if (ascii == 55)
			return koord(4,5,2);
		if (ascii == 56)
			return koord(1,2,4);
		if (ascii == 57)
			return koord(2,4,1);
		if (ascii == 58)
			return koord(4,1,2);
		if (ascii == 59)
			return koord(5,4,2);
		if (ascii == 60)
			return koord(4,2,5);
		if (ascii == 61)
			return koord(2,5,4);
		if (ascii == 62)
			return koord(1,4,2);
		if (ascii == 63)
			return koord(4,2,1);
		if (ascii == 64)
			return koord(2,1,4);
		if (ascii == 65)
			return koord(5,2,2);
		if (ascii == 66)
			return koord(2,2,5);
		if (ascii == 67)
			return koord(2,5,2);
		if (ascii == 68)
			return koord(1,2,2);
		if (ascii == 69)
			return koord(2,2,1);
		if (ascii == 70)
			return koord(2,1,2);
		if (ascii == 71)
			return koord(6,3,3);
		if (ascii == 72)
			return koord(3,3,6);
		if (ascii == 73)
			return koord(3,6,3);
		if (ascii == 74)
			return koord(0,3,3);
		if (ascii == 75)
			return koord(3,3,0);
		if (ascii == 76)
			return koord(3,0,3);
		if (ascii == 77)
			return koord(5,5,4);
		if (ascii == 78)
			return koord(5,4,5);
		if (ascii == 79)
			return koord(4,5,5);
		if (ascii == 80)
			return koord(1,5,4);
		if (ascii == 81)
			return koord(5,4,1);
		if (ascii == 82)
			return koord(4,1,5);
		if (ascii == 83)
			return koord(5,1,4);
		if (ascii == 84)
			return koord(1,4,5);
		if (ascii == 85)
			return koord(4,5,1);
		if (ascii == 86)
			return koord(1,1,4);
		if (ascii == 87)
			return koord(1,4,1);
		if (ascii == 88)
			return koord(4,1,1);
		if (ascii == 89)
			return koord(5,5,2);
		if (ascii == 90)
			return koord(5,2,5);
		if (ascii == 91)
			return koord(2,5,5);
		if (ascii == 92)
			return koord(1,5,2);
		if (ascii == 93)
			return koord(5,2,1);
		if (ascii == 94)
			return koord(2,1,5);
		if (ascii == 95)
			return koord(5,1,2);
		if (ascii == 96)
			return koord(1,2,5);
		if (ascii == 97)
			return koord(2,5,1);
		if (ascii == 98)
			return koord(1,1,2);
		if (ascii == 99)
			return koord(1,2,1);
		if (ascii == 100)
			return koord(2,1,1);
		if (ascii == 101)
			return koord(6,4,3);
		if (ascii == 102)
			return koord(4,3,6);
		if (ascii == 103)
			return koord(3,6,4);
		if (ascii == 104)
			return koord(0,4,3);
		if (ascii == 105)
			return koord(4,3,0);
		if (ascii == 106)
			return koord(3,0,4);
		if (ascii == 107)
			return koord(6,2,3);
		if (ascii == 108)
			return koord(2,3,6);
		if (ascii == 109)
			return koord(3,6,2);
		if (ascii == 110)
			return koord(0,2,3);
		if (ascii == 111)
			return koord(2,3,0);
		if (ascii == 112)
			return koord(3,0,2);
		if (ascii == 113)
			return koord(4,6,3);
		if (ascii == 114)
			return koord(6,3,4);
		if (ascii == 115)
			return koord(3,4,6);
		if (ascii == 116)
			return koord(2,6,3);
		if (ascii == 117)
			return koord(6,3,2);
		if (ascii == 118)
			return koord(3,2,6);
		if (ascii == 119)
			return koord(4,0,3);
		if (ascii == 120)
			return koord(0,3,4);
		if (ascii == 121)
			return koord(3,4,0);
		if (ascii == 122)
			return koord(2,0,3);
		if (ascii == 123)
			return koord(0,3,2);
		if (ascii == 124)
			return koord(3,2,0);
		// erweiterter Bindungssatz
		//System.out.println("erweiterter Satz");
		
		if (ascii == 125)
			return koord(5,5,5);
		if (ascii == 126)
			return koord(1,5,5);
		if (ascii == 127)
			return koord(5,1,5);
		if (ascii == 128)
			return koord(1,1,5);
		if (ascii == 129)
			return koord(5,5,1);
		if (ascii == 130)
			return koord(1,5,1);
		if (ascii == 131)
			return koord(5,1,1);
		if (ascii == 132)
			return koord(1,1,1);
		if (ascii == 133)
			return koord(6,4,4);
		if (ascii == 134)
			return koord(4,4,6);
		if (ascii == 135)
			return koord(4,6,4);
		if (ascii == 136)
			return koord(0,4,4);
		if (ascii == 137)
			return koord(4,4,0);
		if (ascii == 138)
			return koord(4,0,4);
		if (ascii == 139)
			return koord(6,2,4);
		if (ascii == 140)
			return koord(2,4,6);
		if (ascii == 141)
			return koord(4,6,2);
		if (ascii == 142)
			return koord(0,2,4);
		if (ascii == 143)
			return koord(2,4,0);
		if (ascii == 144)
			return koord(4,0,2);
		if (ascii == 145)
			return koord(6,4,2);
		if (ascii == 146)
			return koord(4,2,6);
		if (ascii == 147)
			return koord(2,6,4);
		if (ascii == 148)
			return koord(0,4,2);
		if (ascii == 149)
			return koord(4,2,0);
		if (ascii == 150)
			return koord(2,0,4);
		if (ascii == 151)
			return koord(6,2,2);
		if (ascii == 152)
			return koord(2,2,6);
		if (ascii == 153)
			return koord(2,6,2);
		if (ascii == 154)
			return koord(0,2,2);
		if (ascii == 155)
			return koord(2,2,0);
		if (ascii == 156)
			return koord(2,0,2);
		
		// zusaetzlicher Bindungssatz
		if (ascii == 157)
			return koord(5,5,3);
		if (ascii == 158)
			return koord(5,3,5);
		if (ascii == 159)
			return koord(3,5,5);
		if (ascii == 160)
			return koord(5,1,3);
		if (ascii == 161)
			return koord(1,5,3);
		if (ascii == 162)
			return koord(3,5,1);
		if (ascii == 163)
			return koord(3,1,5);
		if (ascii == 164)
			return koord(5,3,1);
		if (ascii == 165)
			return koord(1,3,5);
		if (ascii == 166)
			return koord(1,1,3);
		if (ascii == 167)
			return koord(3,1,1);
		if (ascii == 168)
			return koord(1,3,1);
		
		System.out.println("FEHLER ssss");
		return koord(3,3,3);
		
	}
	
	
	
	*/
	
	private void GitterSetzen(int monomer) {
		/** Setzt boxpunkte des Polymers mit wert*/
		
		if ((periodic_x == false) && (periodic_y == false) && (periodic_z == false))
		{
			boxBitSet.set(xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer])+ box_x*box_y*zwert(Polymersystem[monomer]));
			boxBitSet.set(1+xwert(Polymersystem[monomer]) +box_x*ywert(Polymersystem[monomer]) + box_x*box_y*zwert(Polymersystem[monomer]));
			boxBitSet.set(1+xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_x*box_y*zwert(Polymersystem[monomer]));
			boxBitSet.set(xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_x*box_y*zwert(Polymersystem[monomer]));
			boxBitSet.set(xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_x*box_y*(1+zwert(Polymersystem[monomer])));
			boxBitSet.set(1+xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_x*box_y*(1+zwert(Polymersystem[monomer])));
			boxBitSet.set(1+xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_x*box_y*(1+zwert(Polymersystem[monomer])));
			boxBitSet.set(xwert(Polymersystem[monomer])+ box_x*(1+ywert(Polymersystem[monomer])) + box_x*box_y*(1+zwert(Polymersystem[monomer])));
		}
		else
		{
			int box_proxy = box_x*box_y;
			
			if ((periodic_x == true) && (periodic_y == false) && (periodic_z == false))
			{
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*ywert(Polymersystem[monomer])+ box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*zwert(Polymersystem[monomer]));

				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*(1+zwert(Polymersystem[monomer])));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*ywert(Polymersystem[monomer]) + box_proxy*(1+zwert(Polymersystem[monomer])));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*(1+zwert(Polymersystem[monomer])));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*(1+zwert(Polymersystem[monomer])));

			}
		
			if ((periodic_x == true) && (periodic_y == true) && (periodic_z == false))
			{
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*zwert(Polymersystem[monomer]));

				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*(1+zwert(Polymersystem[monomer])));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*ywert(Polymersystem[monomer]) + box_proxy*(1+zwert(Polymersystem[monomer])));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*(1+zwert(Polymersystem[monomer])));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*(1+zwert(Polymersystem[monomer])));

			}
				
			if ((periodic_x == true) && (periodic_y == false) && (periodic_z == true))
			{
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*zwert(Polymersystem[monomer]));

				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*ywert(Polymersystem[monomer]) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));

			}
		
			if ((periodic_x == false) && (periodic_y == true) && (periodic_z == false))
			{
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*zwert(Polymersystem[monomer]));

				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*(1+zwert(Polymersystem[monomer])));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*ywert(Polymersystem[monomer]) + box_proxy*(1+zwert(Polymersystem[monomer])));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*(1+zwert(Polymersystem[monomer])));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*(1+zwert(Polymersystem[monomer])));

			}
		
			if ((periodic_x == false) && (periodic_y == true) && (periodic_z == true))
			{
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*zwert(Polymersystem[monomer]));

				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*ywert(Polymersystem[monomer]) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));

			}
				
		
			if ((periodic_x == false) && (periodic_y == false) && (periodic_z == true))
			{
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*zwert(Polymersystem[monomer]));

				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*ywert(Polymersystem[monomer]) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( (1+xwert(Polymersystem[monomer])) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));

			}
		
			if ((periodic_x == true) && (periodic_y == true) && (periodic_z == true))
				{
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*ywert(Polymersystem[monomer]) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*zwert(Polymersystem[monomer]));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*zwert(Polymersystem[monomer]));

				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*ywert(Polymersystem[monomer]) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( ((1+xwert(Polymersystem[monomer]))% (box_x)) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				boxBitSet.set( xwert(Polymersystem[monomer]) + box_x*((1+ywert(Polymersystem[monomer]))% (box_y)) + box_proxy*((1+zwert(Polymersystem[monomer]))% (box_z)));
				}
		
			
			
			
		}
		
		
			
		
		
		
	}
	
}
