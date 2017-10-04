package EvalToolProject_ice.tools;
import java.io.Reader;
import java.util.NoSuchElementException;
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
	
	public double virtual_spring_constant=0.0;
	public double virtual_spring_length=0.0;
	
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
	
	public int NrOfMonoPerChain = 0;
	
	int startsystem = 0;
	
	// private Map typedefinition;// = new HashMap();
	// private Map types;
	// private List additionalbonds;
	
	public LongArrayList additionalbonds;
	public LongArrayList bonds;
	
	public LongArrayList addedBondsBetweenFrames;
	public LongArrayList addedBondsInBeginning;
	
	
	IntArrayList neueKetten;
	 
	 int wechselz = 0;
	 int zaehler = 0;
	 int internz = 0;
	 
	 public long MCSTime = 0;
	 
	// public int [] Polymersystem;
	 //int [] Ladungen;
	 public int [] Attributes;
	 
	 int NrOfMCS = 0;
	 
	 int NrofFrames = 0;
	
	 
	 int[] dummysystem; 
	 
	 boolean gehtweiter = true;
	 
	 public int[][] PolymerKoordinaten; //[Mononummer] [0->x ; 1->y; 2->z;]
	 
	 boolean firstInit;
	 
	// int[] JumpsOver_X;
    // int[] JumpsOver_Y;
    // int[] JumpsOver_Z;
     
     boolean isDetermined;
     
     long DeltaT=0;
     long DeltaTStart=0;
     
     public  boolean[] BondArray;
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
		addedBondsInBeginning  = new LongArrayList();
		
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
		
		//addedBondsInBeginning = null;
		//addedBondsInBeginning = new LongArrayList();
	}
	
	public void CloseSimulationFile()
	{
		loadFile.DateiSchliessen();
	}
	
	public void GetFrameOfSimulation(int frame)
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
					
					     
					     System.out.println("frame " + frame   + " / " +NrofFrames+ "      MCSTime:" +  MCSTime);
					
					 
					 
					 
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
		
		//return Polymersystem;
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
			try{
				StringOfBonds = st.nextToken("");
            }catch(NoSuchElementException e)
            {
               // System.out.println("No more String of Bonds");
                StringOfBonds = "";
            }
			//Polymersystem[aktuellesMono]=koord(x, y, z);
			PolymerKoordinaten[aktuellesMono][0] = x;
			PolymerKoordinaten[aktuellesMono][1] = y;
			PolymerKoordinaten[aktuellesMono][2] = z;

			GitterSetzen(aktuellesMono);
			
			
			
		    //System.out.println(aktuellesMono+ "  " +JumpsOver_Z[aktuellesMono]);
			
			
			
			
			//neueKetten.add(aktuellesMono);
			
			
			aktuellesMono++;
			
	       //System.out.println("length1: " +StringOfBonds.length());
			
	        for (int zk = 1; zk < StringOfBonds.length();zk++)
  		  {
  			  
  				 // bind = ASCIItoBond(StringOfBonds.charAt(zk));
  				 bind = Ascii2BondArray[(int) StringOfBonds.charAt(zk)];
  				
  				// int comp_x = xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ;
  				// int comp_y = ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3 ;
  				// int comp_z = zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3 ;
   				
  				 int comp_x = PolymerKoordinaten[aktuellesMono-1][0] + xwert(bind) - 3 ;
 				 int comp_y = PolymerKoordinaten[aktuellesMono-1][1] + ywert(bind) - 3 ;
 				 int comp_z = PolymerKoordinaten[aktuellesMono-1][2] + zwert(bind) - 3 ;
  				
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
  				//Polymersystem[aktuellesMono] = koord( comp_x ,  comp_y, comp_z);

   				PolymerKoordinaten[aktuellesMono][0] = PolymerKoordinaten[aktuellesMono-1][0] + xwert(bind) - 3 ;
   				PolymerKoordinaten[aktuellesMono][1] = PolymerKoordinaten[aktuellesMono-1][1] + ywert(bind) - 3 ;
   				PolymerKoordinaten[aktuellesMono][2] = PolymerKoordinaten[aktuellesMono-1][2] + zwert(bind) - 3 ;
   				
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
  				/*else 
  				{
  					System.out.println("bonds ok");
  				}*/
  				 // Polymersystem[aktuellesMono] = koord((box_x + xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3) % box_x, (box_y + ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3) % box_y, (box_z + zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3) % box_z);
   				 // Polymersystem[aktuellesMono] = koord( xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ,  ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3, zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3);
  
   				 
  				  
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
		
		System.out.println("besaetzte plaetze\t" +hj*8  +" von " + (NrOfMonomers*8));
		
		if(hj*8 !=  (NrOfMonomers*8))
		{
			System.out.println("invalid number of occupied lattice points. Exiting...");
			BFMFileSaver ErrorSaver = new BFMFileSaver();
			ErrorSaver.DateiAnlegen(System.getProperty("user.home")+"/ErrorBFM.txt", true);
			ErrorSaver.setzeZeile(Filename+" ...invalid number of occupied lattice points");
			ErrorSaver.DateiSchliessen();
			System.exit(1);
		}*/
		
		/*
		//checking all additional bonds
		for(int nr = 0; nr < additionalbonds.size(); nr++ )
		{
			long bondobj = additionalbonds.get(nr);
			//System.out.println(it + " bond " + bondobj);
			
			int mono1 = getMono1Nr(bondobj);
			int mono2 = getMono2Nr(bondobj);
			
			//int comp_x = xwert(Polymersystem[mono1])- xwert(Polymersystem[mono2]);
			//int comp_y = ywert(Polymersystem[mono1])- ywert(Polymersystem[mono2]);
			//int comp_z = zwert(Polymersystem[mono1])- zwert(Polymersystem[mono2]);

			int comp_x = PolymerKoordinaten[mono1][0]- PolymerKoordinaten[mono2][0];
			int comp_y = PolymerKoordinaten[mono1][1]- PolymerKoordinaten[mono2][1];
			int comp_z = PolymerKoordinaten[mono1][2]- PolymerKoordinaten[mono2][2];

				
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
			
		}*/
		
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
						
					break;
					
			case 7: periodic_y = (Parser.value != 0);
						
					break;
					
			case 8: periodic_z = (Parser.value != 0);
						
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
							 
							// MCSTime =  Long.parseLong(st.nextToken()) ;
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
					
			case 34: virtual_spring_constant= Parser.doublevalue;
					break;
					
			case 35: virtual_spring_length= Parser.doublevalue;
					break;

			case 101: NrOfMonoPerChain= Parser.value;
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
	        addedBondsInBeginning.add(Longwert(NumberBeginn, NumberEnd)); 
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
		//Polymersystem = new int [NrOfMonomers+1];
		
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
			try{
				StringOfBonds = st.nextToken("");
            }catch(NoSuchElementException e)
            {
               // System.out.println("No more String of Bonds");
                StringOfBonds = "";
            }
			//Polymersystem[aktuellesMono]=koord(x,y,z);
			PolymerKoordinaten[aktuellesMono][0] = x;
			PolymerKoordinaten[aktuellesMono][1] = y;
			PolymerKoordinaten[aktuellesMono][2] = z;

			GitterSetzen(aktuellesMono);
			
			//automatic determine NrOfMonomersPerChain
			NrOfMonoPerChain= -aktuellesMono;
			
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
  				  
  				// int comp_x = xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ;
  				// int comp_y = ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3 ;
  				// int comp_z = zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3 ;
   				
  				 int comp_x = PolymerKoordinaten[aktuellesMono-1][0] + xwert(bind) - 3 ;
 				 int comp_y = PolymerKoordinaten[aktuellesMono-1][1] + ywert(bind) - 3 ;
 				 int comp_z = PolymerKoordinaten[aktuellesMono-1][2] + zwert(bind) - 3 ;
  				
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
  				//Polymersystem[aktuellesMono] = koord( comp_x ,  comp_y, comp_z);
  				PolymerKoordinaten[aktuellesMono][0] = PolymerKoordinaten[aktuellesMono-1][0] + xwert(bind) - 3 ;
   				PolymerKoordinaten[aktuellesMono][1] = PolymerKoordinaten[aktuellesMono-1][1] + ywert(bind) - 3 ;
   				PolymerKoordinaten[aktuellesMono][2] = PolymerKoordinaten[aktuellesMono-1][2] + zwert(bind) - 3 ;
   				
  				GitterSetzen(aktuellesMono);
  				  
  				  SetBond(aktuellesMono-1, aktuellesMono);
  				  
  				
  				  
  				  aktuellesMono++;
  				  
  				  
  				 
  		  }
	        NrOfMonoPerChain += aktuellesMono;
	        
	       
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
				try{
					StringOfBonds = st.nextToken("");
	            }catch(NoSuchElementException e)
	            {
	               // System.out.println("No more String of Bonds");
	                StringOfBonds = "";
	            }
			//	Polymersystem[aktuellesMono]=koord(x,y,z);
				PolymerKoordinaten[aktuellesMono][0] = x;
				PolymerKoordinaten[aktuellesMono][1] = y;
				PolymerKoordinaten[aktuellesMono][2] = z;

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
	   				// int comp_x = xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ;
	  				// int comp_y = ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3 ;
	  				// int comp_z = zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3 ;
	   				

	  				 int comp_x = PolymerKoordinaten[aktuellesMono-1][0] + xwert(bind) - 3 ;
	 				 int comp_y = PolymerKoordinaten[aktuellesMono-1][1] + ywert(bind) - 3 ;
	 				 int comp_z = PolymerKoordinaten[aktuellesMono-1][2] + zwert(bind) - 3 ;
	  				
	 				 
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
	  				//Polymersystem[aktuellesMono] = koord( comp_x ,  comp_y, comp_z);
	  				PolymerKoordinaten[aktuellesMono][0] = PolymerKoordinaten[aktuellesMono-1][0] + xwert(bind) - 3 ;
	   				PolymerKoordinaten[aktuellesMono][1] = PolymerKoordinaten[aktuellesMono-1][1] + ywert(bind) - 3 ;
	   				PolymerKoordinaten[aktuellesMono][2] = PolymerKoordinaten[aktuellesMono-1][2] + zwert(bind) - 3 ;
	   				
	  				GitterSetzen(aktuellesMono);
	  				 // Polymersystem[aktuellesMono] = koord((box_x + xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3) % box_x, (box_y + ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3) % box_y, (box_z + zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3) % box_z);
	   				 // Polymersystem[aktuellesMono] = koord( xwert(Polymersystem[aktuellesMono-1]) + xwert(bind) - 3 ,  ywert(Polymersystem[aktuellesMono-1]) + ywert(bind) - 3, zwert(Polymersystem[aktuellesMono-1]) + zwert(bind) - 3);
	  				  
	  				  
	  				  
	  				  SetBond(aktuellesMono-1, aktuellesMono);
	  				  
	  			//	System.out.println(1+ "  Det2C " +JumpsOver_Z[1]);
	  				
	  				
	  				  
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
		
		System.out.println("besaetzte plaetze\t" +(8*hj)  +" von " + (NrOfMonomers*8));
		
		/*if((8*hj) !=  (NrOfMonomers*8))
		{
			System.out.println("invalid number of occupied lattice points. Exiting...");
			BFMFileSaver ErrorSaver = new BFMFileSaver();
			ErrorSaver.DateiAnlegen("/home/users/dockhorn/ErrorBFM.txt", true);
			ErrorSaver.setzeZeile(Filename+" ...invalid number of occupied lattice points");
			ErrorSaver.DateiSchliessen();
			System.exit(1);
		}
		*/
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
	

	
	private void GitterSetzen(int monomer) {
		/** Setzt boxpunkte des Polymers mit wert*/
		
		if ((periodic_x == false) && (periodic_y == false) && (periodic_z == false))
		{
		/*	boxBitSet.set(xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer])+ box_x*box_y*zwert(Polymersystem[monomer]));
			boxBitSet.set(1+xwert(Polymersystem[monomer]) +box_x*ywert(Polymersystem[monomer]) + box_x*box_y*zwert(Polymersystem[monomer]));
			boxBitSet.set(1+xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_x*box_y*zwert(Polymersystem[monomer]));
			boxBitSet.set(xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_x*box_y*zwert(Polymersystem[monomer]));
			boxBitSet.set(xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_x*box_y*(1+zwert(Polymersystem[monomer])));
			boxBitSet.set(1+xwert(Polymersystem[monomer]) + box_x*ywert(Polymersystem[monomer]) + box_x*box_y*(1+zwert(Polymersystem[monomer])));
			boxBitSet.set(1+xwert(Polymersystem[monomer]) + box_x*(1+ywert(Polymersystem[monomer])) + box_x*box_y*(1+zwert(Polymersystem[monomer])));
			boxBitSet.set(xwert(Polymersystem[monomer])+ box_x*(1+ywert(Polymersystem[monomer])) + box_x*box_y*(1+zwert(Polymersystem[monomer])));
		*/
			
			/*boxBitSet.set(-1+PolymerKoordinaten[monomer][0] + box_x*(-1+PolymerKoordinaten[monomer][1])+ box_x*box_y*(-1+PolymerKoordinaten[monomer][2]));
			boxBitSet.set( 1+PolymerKoordinaten[monomer][0] + box_x*(-1+PolymerKoordinaten[monomer][1])+ box_x*box_y*(-1+PolymerKoordinaten[monomer][2]));
			boxBitSet.set(-1+PolymerKoordinaten[monomer][0] + box_x*( 1+PolymerKoordinaten[monomer][1])+ box_x*box_y*(-1+PolymerKoordinaten[monomer][2]));
			boxBitSet.set( 1+PolymerKoordinaten[monomer][0] + box_x*( 1+PolymerKoordinaten[monomer][1])+ box_x*box_y*(-1+PolymerKoordinaten[monomer][2]));
			
			boxBitSet.set(-1+PolymerKoordinaten[monomer][0] + box_x*(-1+PolymerKoordinaten[monomer][1])+ box_x*box_y*( 1+PolymerKoordinaten[monomer][2]));
			boxBitSet.set( 1+PolymerKoordinaten[monomer][0] + box_x*(-1+PolymerKoordinaten[monomer][1])+ box_x*box_y*( 1+PolymerKoordinaten[monomer][2]));
			boxBitSet.set(-1+PolymerKoordinaten[monomer][0] + box_x*( 1+PolymerKoordinaten[monomer][1])+ box_x*box_y*( 1+PolymerKoordinaten[monomer][2]));
			boxBitSet.set( 1+PolymerKoordinaten[monomer][0] + box_x*( 1+PolymerKoordinaten[monomer][1])+ box_x*box_y*( 1+PolymerKoordinaten[monomer][2]));
			*/
			//set only the core
			boxBitSet.set( PolymerKoordinaten[monomer][0] + box_x*(PolymerKoordinaten[monomer][1])+ box_x*box_y*(PolymerKoordinaten[monomer][2]));
			
		}
		else
		{
			int box_proxy = box_x*box_y;
			
			//c++ (ISO 1998)
			//there is a problem with the modulo-operator with negative numbers
			//in ISO 1998 the sign is implementation defined :(
			//in ISO (2011) C++11 the sign has the dividend sign
			//the sake of simplicity here is an ugly test function
			/*if ((-1 % 34) != 33) {
				System.out.println(" Java Implementation of %-operator. ");
				System.out.println(" % yields incorrect results e.g. -1%34 = 33 . Implementation yields: -1%34 = "+ (-1 % 34) );
				System.out.println(" % yields incorrect results e.g. -1%34 = 33 . Implementation yields: ((-1%34)+34)%34 = "+ (((-1 % 34) + 34) % 34));
			} else {
				System.out.println(" % yields correct results e.g. -1%34 = 33 . Implementation yields: -1%34 = " +(-1 % 34) );
			}*/
			
		
			/*if ((periodic_x == true) && (periodic_y == true) && (periodic_z == true))
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
				*/
			
			/*boxBitSet.set( ((((-1+PolymerKoordinaten[monomer][0]) % box_x) + box_x) % box_x) + box_x*((((-1+PolymerKoordinaten[monomer][1]) % box_y) + box_y) % box_y)+ box_x*box_y*((((-1+PolymerKoordinaten[monomer][2]) % box_z) + box_z) % box_z));
			boxBitSet.set( (((( 1+PolymerKoordinaten[monomer][0]) % box_x) + box_x) % box_x) + box_x*((((-1+PolymerKoordinaten[monomer][1]) % box_y) + box_y) % box_y)+ box_x*box_y*((((-1+PolymerKoordinaten[monomer][2]) % box_z) + box_z) % box_z));
			boxBitSet.set( ((((-1+PolymerKoordinaten[monomer][0]) % box_x) + box_x) % box_x) + box_x*(((( 1+PolymerKoordinaten[monomer][1]) % box_y) + box_y) % box_y)+ box_x*box_y*((((-1+PolymerKoordinaten[monomer][2]) % box_z) + box_z) % box_z));
			boxBitSet.set( (((( 1+PolymerKoordinaten[monomer][0]) % box_x) + box_x) % box_x) + box_x*(((( 1+PolymerKoordinaten[monomer][1]) % box_y) + box_y) % box_y)+ box_x*box_y*((((-1+PolymerKoordinaten[monomer][2]) % box_z) + box_z) % box_z));
			
			boxBitSet.set( ((((-1+PolymerKoordinaten[monomer][0]) % box_x) + box_x) % box_x) + box_x*((((-1+PolymerKoordinaten[monomer][1]) % box_y) + box_y) % box_y)+ box_x*box_y*(((( 1+PolymerKoordinaten[monomer][2]) % box_z) + box_z) % box_z));
			boxBitSet.set( (((( 1+PolymerKoordinaten[monomer][0]) % box_x) + box_x) % box_x) + box_x*((((-1+PolymerKoordinaten[monomer][1]) % box_y) + box_y) % box_y)+ box_x*box_y*(((( 1+PolymerKoordinaten[monomer][2]) % box_z) + box_z) % box_z));
			boxBitSet.set( ((((-1+PolymerKoordinaten[monomer][0]) % box_x) + box_x) % box_x) + box_x*(((( 1+PolymerKoordinaten[monomer][1]) % box_y) + box_y) % box_y)+ box_x*box_y*(((( 1+PolymerKoordinaten[monomer][2]) % box_z) + box_z) % box_z));
			boxBitSet.set( (((( 1+PolymerKoordinaten[monomer][0]) % box_x) + box_x) % box_x) + box_x*(((( 1+PolymerKoordinaten[monomer][1]) % box_y) + box_y) % box_y)+ box_x*box_y*(((( 1+PolymerKoordinaten[monomer][2]) % box_z) + box_z) % box_z));
			*/
			//set only the core
			boxBitSet.set( (((( PolymerKoordinaten[monomer][0]) % box_x) + box_x) % box_x) + box_x*(((( PolymerKoordinaten[monomer][1]) % box_y) + box_y) % box_y)+ box_x*box_y*((((PolymerKoordinaten[monomer][2]) % box_z) + box_z) % box_z));
			
		}
		
		
			
		
		
		
	}
	
}
