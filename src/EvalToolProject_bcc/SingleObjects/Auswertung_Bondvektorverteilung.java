package EvalToolProject_bcc.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_bcc.tools.BFMFileSaver;
import EvalToolProject_bcc.tools.BFMImportData;
import EvalToolProject_bcc.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_bcc.tools.Statistik;

public class Auswertung_Bondvektorverteilung {


	
	
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
	
	
	
	int[] dumpsystem;

	
	
	Statistik durchschnittbond;
	Statistik mittelbond;
	
	int BondNrVektor[];
	int BondNr[];
	int BondAscIIArray[];
	int AllNr = 0;
	
	BFMFileSaver BondSaver;
	BFMFileSaver BondSaver2;
	
	int MONOMERZAHL;
	
	int haeufigkeit[];
	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	public Auswertung_Bondvektorverteilung(String fdir, String fname)//, String skip, String current)
	{
		//FileName = "1024_1024_0.00391_32";
		FileName = fname;
		FileDirectory = fdir;//"/home/users/dockhorn/Simulationen/HEPPEGSolution/";
		
		
		Polymersystem = new int[1];
		skipFrames =  0;//Integer.parseInt(skip);
		currentFrame = 1;//Integer.parseInt(current);
		//System.out.println("cf="+currentFrame);
		
		
		BondSaver = new BFMFileSaver();
		BondSaver.DateiAnlegen(fdir+fname+"_bondstatistik.dat", false);
		
		BondSaver2 = new BFMFileSaver();
		BondSaver2.DateiAnlegen(fdir+fname+"_bondstatistikVektor.dat", false);
		
		BondNrVektor = new int [7];
		BondNr = new int [512];
		BondAscIIArray = new int[512];
		InitBondAsciiArray();
		
		
		durchschnittbond = new Statistik();
		mittelbond=new Statistik();
		
		
		DecimalFormat dh = new DecimalFormat("000");
		
		
		LoadFile(FileName+".bfm", 1);
		
		BondSaver.setzeZeile("# ASCII <relat. freq.> char x y z b=" +mittelbond.ReturnM1());
		
		for(int i= 17; i <= 168; i++)
			for(int xc = -3; xc <= 3; xc++)
			 for(int yc = -3; yc <= 3; yc++)
			  for(int zc = -3; zc <= 3; zc++)
			  {
				  if(GetIntegerOfBondvector(xc,yc,zc) == i)
					  BondSaver.setzeZeile(i+" "+  (BondNr[KoordBind(xc,yc,zc)]/(1.0*AllNr)) + " "+ BondAscIIArray[KoordBind(xc,yc,zc)] + " " + xc + " " + yc + " " + zc);
			  }
		
		BondSaver.DateiSchliessen();
		
		BondSaver2.setzeZeile("# 0=P(2,0,0); 1=P(2,1,0); 2=P(2,1,1); 3=P(2,2,1); 4=P(3,0,0); 5=P(3,1,0); 6=P(2,2,0)");
		BondSaver2.setzeZeile("# bond <relat. freq.>  b=" +mittelbond.ReturnM1());
		
		for(int i= 0; i <= 6; i++)
		{
			BondSaver2.setzeZeile(i+" "+  (BondNrVektor[i]/(1.0*AllNr)));
		}
		
		BondSaver2.DateiSchliessen();
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1()+ "  AllNr:" +AllNr );
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
		new Auswertung_Bondvektorverteilung(args[0], args[1]);//,args[1],args[2]);
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
				importData.GetFrameOfSimulation( frame);;
				
				for(int it = 0; it < importData.bonds.size(); it++)
				{
					long bondobj = importData.bonds.get(it);
					//System.out.println(it + " bond " + bondobj);
					
					int a = getMono1Nr(bondobj);
					int b = getMono2Nr(bondobj);
					
					BondNr[KoordBind(importData.PolymerKoordinaten[a][0]-importData.PolymerKoordinaten[b][0], importData.PolymerKoordinaten[a][1]-importData.PolymerKoordinaten[b][1], importData.PolymerKoordinaten[a][2]-importData.PolymerKoordinaten[b][2])]++;
					AllNr++;
					
					mittelbond.AddValue(Math.sqrt(1.0*(importData.PolymerKoordinaten[a][0]-importData.PolymerKoordinaten[b][0])*(importData.PolymerKoordinaten[a][0]-importData.PolymerKoordinaten[b][0]) + (importData.PolymerKoordinaten[a][1]-importData.PolymerKoordinaten[b][1])*(importData.PolymerKoordinaten[a][1]-importData.PolymerKoordinaten[b][1]) +(importData.PolymerKoordinaten[a][2]-importData.PolymerKoordinaten[b][2])*(importData.PolymerKoordinaten[a][2]-importData.PolymerKoordinaten[b][2])));
				
					int x=Math.abs(importData.PolymerKoordinaten[a][0]-importData.PolymerKoordinaten[b][0]);
					int y=Math.abs(importData.PolymerKoordinaten[a][1]-importData.PolymerKoordinaten[b][1]);
					int z=Math.abs(importData.PolymerKoordinaten[a][2]-importData.PolymerKoordinaten[b][2]);
					
					if((x*x+y*y+z*z)==4)
						BondNrVektor[0]++;
					
					if((x*x+y*y+z*z)==5)
						BondNrVektor[1]++;
					
					if((x*x+y*y+z*z)==6)
						BondNrVektor[2]++;
					
					if(((x*x+y*y+z*z)==9) && ((x==2) || (y==2) || (z==2)))
						BondNrVektor[3]++;
					
					if(((x*x+y*y+z*z)==9) && ((x==3) || (y==3) || (z==3)))
						BondNrVektor[4]++;
					
					if(((x*x+y*y+z*z)==10) )
						BondNrVektor[5]++;
					
					if(((x*x+y*y+z*z)== 8) )
						BondNrVektor[6]++;
					
				}
				
				for(int it = 0; it < importData.additionalbonds.size(); it++)
				{
					long bondobj = importData.additionalbonds.get(it);
					//System.out.println(it + " bond " + bondobj);
					
					int a = getMono1Nr(bondobj);
					int b = getMono2Nr(bondobj);
					
					BondNr[KoordBind(importData.PolymerKoordinaten[a][0]-importData.PolymerKoordinaten[b][0], importData.PolymerKoordinaten[a][1]-importData.PolymerKoordinaten[b][1], importData.PolymerKoordinaten[a][2]-importData.PolymerKoordinaten[b][2])]++;
					AllNr++;
					
					mittelbond.AddValue(Math.sqrt(1.0*(importData.PolymerKoordinaten[a][0]-importData.PolymerKoordinaten[b][0])*(importData.PolymerKoordinaten[a][0]-importData.PolymerKoordinaten[b][0]) + (importData.PolymerKoordinaten[a][1]-importData.PolymerKoordinaten[b][1])*(importData.PolymerKoordinaten[a][1]-importData.PolymerKoordinaten[b][1]) +(importData.PolymerKoordinaten[a][2]-importData.PolymerKoordinaten[b][2])*(importData.PolymerKoordinaten[a][2]-importData.PolymerKoordinaten[b][2])));
				
					int x=Math.abs(importData.PolymerKoordinaten[a][0]-importData.PolymerKoordinaten[b][0]);
					int y=Math.abs(importData.PolymerKoordinaten[a][1]-importData.PolymerKoordinaten[b][1]);
					int z=Math.abs(importData.PolymerKoordinaten[a][2]-importData.PolymerKoordinaten[b][2]);
					
					if((x*x+y*y+z*z)==4)
						BondNrVektor[0]++;
					
					if((x*x+y*y+z*z)==5)
						BondNrVektor[1]++;
					
					if((x*x+y*y+z*z)==6)
						BondNrVektor[2]++;
					
					if(((x*x+y*y+z*z)==9) && ((x==2) || (y==2) || (z==2)))
						BondNrVektor[3]++;
					
					if(((x*x+y*y+z*z)==9) && ((x==3) || (y==3) || (z==3)))
						BondNrVektor[4]++;
					
					if(((x*x+y*y+z*z)==10) )
						BondNrVektor[5]++;
					
					if(((x*x+y*y+z*z)== 8) )
						BondNrVektor[6]++;
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
	
	private void InitBondAsciiArray()
	{
		java.util.Arrays.fill(BondAscIIArray, 0);
		
		for(int xc = -3; xc <= 3; xc++)
			for(int yc = -3; yc <= 3; yc++)
				for(int zc = -3; zc <= 3; zc++)
					BondAscIIArray[KoordBind(xc,yc,zc)] = GetIntegerOfBondvector(xc,yc,zc);
		
	}
	
	private int GetIntegerOfBondvector(int diffx, int diffy, int diffz)
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
}
