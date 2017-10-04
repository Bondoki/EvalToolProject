package EvalToolProject_ice.HEP_PEG;
import java.text.DecimalFormat;

import EvalToolProject_ice.tools.BFMFileSaver;
import EvalToolProject_ice.tools.BFMImportData;
import EvalToolProject_ice.tools.Statistik;

public class Auswertung_Verteilung_HepPEG {


	
	
	String FileName;
	String FileDirectory;
	
	int[] Polymersystem;
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
	
	
	BFMImportData importData;
	
	
	
	int[] dumpsystem;
	
	//BFMFileSaver saveRc2c;
	
	/*BFMFileSaver saveRcmRing1;
	
	BFMFileSaver saveRcmRing2;
	
	BFMFileSaver SaverMetrikG;
	
	BFMFileSaver SaverRcmTotal;*/
	
	/*Statistik Rg2_stat;
	Statistik Rg2_xstat;
	Statistik Rg2_ystat;
	Statistik Rg2_zstat;
	
	BFMFileSaver Rg2Total;
	BFMFileSaver Rg2Total_x;
	BFMFileSaver Rg2Total_y;
	BFMFileSaver Rg2Total_z;*/


	int NrOfHeparin = 0;
	int NrOfStars = 0;
	int NrOfMonomersPerStarArm = 0;
	
	int MONOMERZAHL;
	

	
	DecimalFormat df ;
	DecimalFormat dh;
	
	
	
	
	Statistik durchschnittbond;
	
	int [] haufigkeit = new int[2001];
	
	int [][] haufigkeitUzeit = new int[2001][1001];
	
	int maxBox_x;
	int maxBox_y;
	int maxBox_z;
	
	int Gitter_x;
	int Gitter_y;
	int Gitter_z;
	
	int[][][] Anzahlgitter;
	double[][][] Ordnungsgitter;
	
	int MinMono;
	
	public Auswertung_Verteilung_HepPEG()
	{
		
		
		
		FileName = "poly.bfm";
		FileDirectory = "";
		
		Polymersystem = new int[1];
		skipFrames = 0;
		currentFrame = 1;
		
		df =   new DecimalFormat  ( "00" );
		dh =   new DecimalFormat  ( "0000" );
		 
		durchschnittbond = new Statistik();
		/*Rg2Total = new BFMFileSaver();
		Rg2Total.DateiAnlegen("Ring1000_Rg2", false);
		
		Rg2Total_x = new BFMFileSaver();
		Rg2Total_x.DateiAnlegen("Ring1000_Rg2_x", false);
		
		Rg2Total_y = new BFMFileSaver();
		Rg2Total_y.DateiAnlegen("Ring1000_Rg2_y", false);
		
		Rg2Total_z = new BFMFileSaver();
		Rg2Total_z.DateiAnlegen("Ring1000_Rg2_z", false);*/
		
		
		
		double[] kli = new double[6];
		kli[0] =1.0;
		kli[1] = Double.NaN;
		kli[2] = 2.6;
		
		for(int zu = 0; zu < 6 ; zu++)
		{
			if (!Double.isNaN(kli[zu])) 
				System.out.println(zu + "  "+ kli[zu]);
		}
		
		//System.exit(0);
		
		
		
		//String WSpfad = "";
		String WS = "Hydrogel_HEP_633__PEG_633_NStar_117__NoPerXYZ128";//"p_0_01__q_0_001";
		
		for(int i = 1; i <= 4; i++)
		{
			switch (i){
			
			case 0: maxBox_x = 4;
					maxBox_y = 4;
					maxBox_z = 4;
					MinMono=4;
					break;
					
			case 1: maxBox_x = 8;
					maxBox_y = 8;
					maxBox_z = 8;
					MinMono=4;
					break;
					
			case 2: maxBox_x = 16;
					maxBox_y = 16;
					maxBox_z = 16;
					MinMono=10;
					break;
					
			case 3: maxBox_x = 32;
					maxBox_y = 32;
					maxBox_z = 32;
					MinMono=15;
					break;
					
			case 4: maxBox_x = 64;
					maxBox_y = 64;
					maxBox_z = 64;
					MinMono=40;
					break;
					
			/*case 5: WSpfad="0_005/";
					WS = "0#005";
					break;
					
			case 6: WSpfad="0_001/";
					WS = "0#001";
					break;
			*/
			
			
					
			}
			//WSpfad="d/";
			//WS = "frei";
			//WSpfad="1_0/";
			//WS = "gd";
			FileDirectory = "/home/users/dockhorn/workspace/Evaluation_Tools/";
			
		
		
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		//LoadFile("DNALadderX1023YZ10Parted.bfm", 1);
		//LoadFile("Ring500ReplicationX1023YZ15ExtBond__01.bfm", 1);
		LoadFile("Hydrogel_HEP_633__PEG_633_NStar_117__NoPerXYZ128.bfm",1,i);
		//for(int i = 10; i <= 100; i+=5)
		
		
		/*int anzahl = 0;
		
		for(int zu = 0; zu < MONOMERZAHL; zu++)
			anzahl += haufigkeit[zu];
		*/
		
		int[] anzahlzeit = new int[NrofFrames+1];
		
		for(int zeit = 0; zeit < NrofFrames+1; zeit++)
			for(int zu = 0; zu <= 2000; zu++)
				anzahlzeit[zeit] += haufigkeitUzeit[zu][zeit];
		
		BFMFileSaver saveVer_Stat = new BFMFileSaver();
		saveVer_Stat.DateiAnlegen("Verteilung__"+WS+"_phi_zw_M0.2_P0.2_"+maxBox_x+"x"+maxBox_y+"x"+maxBox_z+".dat",false);
		saveVer_Stat.setzeZeile("#   Trennung mit hoher Verknuepfung");
		saveVer_Stat.setzeZeile("#   durchschnittbindung :" + durchschnittbond.ReturnM1() + " bei N = "+ durchschnittbond.ReturnN());
		//saveVer_Stat.setzeZeile("#   anzahl aller zeiten:" + anzahl[] );
		
		
		BFMFileSaver saveVerM1_Stat = new BFMFileSaver();
		saveVerM1_Stat.DateiAnlegen("Verteilung__"+WS+"_phi_kl_M0.8_"+maxBox_x+"x"+maxBox_y+"x"+maxBox_z+".dat",false);
		saveVerM1_Stat.setzeZeile("#   Trennung mit hoher Verknuepfung");
		saveVerM1_Stat.setzeZeile("#   durchschnittbindung :" + durchschnittbond.ReturnM1() + " bei N = "+ durchschnittbond.ReturnN());
		//saveVer_Stat.setzeZeile("#   anzahl aller zeiten:" + anzahl[] );
		
		BFMFileSaver saveVerM2_Stat = new BFMFileSaver();
		saveVerM2_Stat.DateiAnlegen("Verteilung__"+WS+"_phi_gr_P0.8_"+maxBox_x+"x"+maxBox_y+"x"+maxBox_z+".dat",false);
		saveVerM2_Stat.setzeZeile("#   Trennung mit hoher Verknuepfung");
		saveVerM2_Stat.setzeZeile("#   durchschnittbindung :" + durchschnittbond.ReturnM1() + " bei N = "+ durchschnittbond.ReturnN());
		//saveVer_Stat.setzeZeile("#   anzahl aller zeiten:" + anzahl[] );
		
		
		double summe = 0.0;
		double summek08 = 0.0;
		double summeg08= 0.0;
		
		for(int zeit = 0; zeit < NrofFrames+1; zeit++)
		{
			summe = 0.0;
			summek08 =0.0;
			summeg08 =0.0;
			
			for(int l = 0; l <= 2000; l++)//10001; l++)
			{
				//l=0 -> -1.0
				//l=1000 -> 0
				//l=2000 -> +1.0
				//(l-1000)/1000.0 = x bei phi(x)
				if((l >= 800) && (l <= 1200)  )
				{
					summe += haufigkeitUzeit[l][zeit];
				}
				
				if(l <= 200) 
				{
					summek08 += haufigkeitUzeit[l][zeit];
				}
				
				if(l >= 1800) 
				{
					summeg08 += haufigkeitUzeit[l][zeit];
				}
				
			}

			saveVer_Stat.setzeZeile( (50000*zeit) + " " +(summe/(1.0*anzahlzeit[zeit]))+ " "+anzahlzeit[zeit]);

			saveVerM1_Stat.setzeZeile( (50000*zeit) + " " +(summek08/(1.0*anzahlzeit[zeit]))+ " "+anzahlzeit[zeit]);

			saveVerM2_Stat.setzeZeile( (50000*zeit) + " " +(summeg08/(1.0*anzahlzeit[zeit]))+ " "+anzahlzeit[zeit]);

			
		}
		
		
		
		saveVer_Stat.DateiSchliessen();
		
		saveVerM1_Stat.DateiSchliessen();
		saveVerM2_Stat.DateiSchliessen();
		
		
		
		
		
		
		
		
		
		
		
		
		

		
		}
	}
	
	
	
	
	protected void LoadFile(String file, int startframe, int Durchmesser)
	{
		FileName = file;
		
		
		
		System.out.println("lade System");
		LadeSystem(FileDirectory, FileName);
		
		Anzahlgitter = null;
		Anzahlgitter = new int[Gitter_x/maxBox_x][Gitter_y/maxBox_y][Gitter_z/maxBox_z];
		Ordnungsgitter = null;
		Ordnungsgitter = new double[Gitter_x/maxBox_x][Gitter_y/maxBox_y][Gitter_z/maxBox_z];
		
		System.out.println();
		System.out.println("bonds :" +importData.bonds.size());
		System.out.println("addbonds :" +importData.additionalbonds.size());
		System.out.println();
		
		durchschnittbond.AddValue(importData.additionalbonds.size());
		
		
		
		/*int zu = 0;
		
		for(int i = 0; i < importData.additionalbonds.size(); i++)
		{
			int diff = Math.abs(getMono1Nr(importData.additionalbonds.get(i))-getMono2Nr(importData.additionalbonds.get(i)));
			zu ++;
			//System.out.println("mo1 :" +getMono1Nr(importData.additionalbonds.get(i)) + "     mo2 :"+ getMono2Nr(importData.additionalbonds.get(i)));
			haeufigkeit[diff] +=1;
		}
		System.out.println("zu :" +zu);
		
		zeit_anfang = 0;
		zeit_ende = 0;
		
		ZeitZurRate.clear();*/
		
		skipFrames = 0;
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(FileDirectory+FileName);
		  
		  System.arraycopy(importData.GetFrameOfSimulation(currentFrame),0,Polymersystem,0, Polymersystem.length);
		  
		  importData.CloseSimulationFile();
		  
		  importData.OpenSimulationFile(FileDirectory+FileName);
			
			System.out.println("open File...");
			System.out.println("file : " +FileName );
			System.out.println("dir : " + FileDirectory);
			
			//int z = currentFrame;//1;
				
		    while ( (currentFrame <= NrofFrames))
		      {
		    	
		    	//try
				//{
		    		playSimulation(currentFrame);
		    		//System.out.println("playing frame " + z);
		    		currentFrame++;
		    	
		    		for(int u = 1; u <= skipFrames; u++)
		    		{
		    			//System.out.println("skip");
		    			currentFrame++;
		    		}
		    		
		    	//	Thread.sleep(100);
				
				//}
				//catch(InterruptedException e)
		    	//{
		    	//System.out.println("thread abbgeb");
		    	//}
		    	
		      }
		    
			
		    
		    
		    importData.CloseSimulationFile();
		    
		  
		    
		   
			
		//	currentFrame = z;
			
			if( (currentFrame+skipFrames) >= NrofFrames)
				currentFrame =  NrofFrames;
		      
		
			
			
			
		
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Auswertung_Verteilung_HepPEG();
	}
	
	public void LadeSystem(String FileDir, String FileName)
	{
		importData = null;
		System.gc();
		importData = new BFMImportData(FileDir+FileName);
		
		MONOMERZAHL = importData.NrOfMonomers + 1;
		
		Gitter_x = importData.box_x;
		Gitter_y = importData.box_y;
		Gitter_z = importData.box_z;
		
		NrOfStars = importData.NrOfStars;
		NrOfMonomersPerStarArm = importData.NrOfMonomersPerStarArm;
		NrOfHeparin = importData.NrOfHeparin;
		
		haufigkeit = new int[MONOMERZAHL];
		
		
		haufigkeitUzeit = new int[2001][importData.GetNrOfFrames()+1]; //[Diskretisierung][Zeit]
		
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
		System.out.println("NrofFrames : " + NrofFrames);
		
		
		for(int k = 0; k < MONOMERZAHL; k++)
			haufigkeit[k]=0;
		
		for(int k = 0; k < 2001; k++)
			for(int l = 0; l < NrofFrames+1; l++)
			haufigkeitUzeit [k][l]=0;
		
		
		String zutrt = 	FileName.substring(0, FileName.length()-4);

		System.out.println("String : " + zutrt);
		
		
			
		
		
		
	}
	
	public void playSimulation(int frame)
	{
			
				//bfm3d.setzePosition(importData.GetFrameOfSimulation( frame));
				
				//bfm3d.setzePosition(dumpsystem);
				
				int MonoHalb = 1000;
				
				int Monozaehler_A = 0;
				int Monozaehler_B = 0;
				
				int Monogesamt = 0;
				int alleMono =0;
				
				//if(frame <= 1001)//1)
				{
					//System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);
					importData.GetFrameOfSimulation( frame);
					
					for(int i = 0; i < Gitter_x/maxBox_x; i++)
						for (int k = 0; k < Gitter_y/maxBox_y; k++)
							for (int l = 0; l < Gitter_z/maxBox_z; l++)
								{
									Anzahlgitter[i][k][l] =0;
									Ordnungsgitter[i][k][l] = 0.0;
								}
					
					for(int nr = 1; nr <= (4*NrOfMonomersPerStarArm + 1)*NrOfStars+90*NrOfHeparin; nr++)
					{
						Anzahlgitter[xwert(importData.Polymersystem[nr])/maxBox_x][ywert(importData.Polymersystem[nr])/maxBox_y][zwert(importData.Polymersystem[nr])/maxBox_z] ++;
					}
					
					for(int nr = 1; nr <= 90*NrOfHeparin; nr++)
					{
						Ordnungsgitter[xwert(importData.Polymersystem[nr])/maxBox_x][ywert(importData.Polymersystem[nr])/maxBox_y][zwert(importData.Polymersystem[nr])/maxBox_z] ++;
					}
					
					for(int nr = 90*NrOfHeparin+1; nr <= (4*NrOfMonomersPerStarArm + 1)*NrOfStars+90*NrOfHeparin; nr++)
					{
						Ordnungsgitter[xwert(importData.Polymersystem[nr])/maxBox_x][ywert(importData.Polymersystem[nr])/maxBox_y][zwert(importData.Polymersystem[nr])/maxBox_z]--;
					}
					
					for(int i = 0; i < Gitter_x/maxBox_x; i++)
						for (int k = 0; k < Gitter_y/maxBox_y; k++)
							for (int l = 0; l < Gitter_z/maxBox_z; l++)
								{
									if(Anzahlgitter[i][k][l] > MinMono)
									{
										Ordnungsgitter[i][k][l] /= Anzahlgitter[i][k][l];
										haufigkeitUzeit[(int) (1000*Ordnungsgitter[i][k][l])+1000][frame-1] += 1;
									}
									else Ordnungsgitter[i][k][l] = Double.NaN;
									
									
								}
					
					
				
				
				
				if(frame  == 1001)
				{
					System.out.println(""+frame);
					exportVtkData(frame-1);
				}
				
				
				}
								
				
	}
	
	
	
	public void exportVtkData(int frame)
	{
		BFMFileSaver save = new BFMFileSaver();
		
		save.DateiAnlegen("Verteilung__Hydrogel_HEP_633__PEG_633_N_"+(frame*50000)+"_"+maxBox_x+"x"+maxBox_y+"x"+maxBox_z+".vtk",false);

		save.setzeZeile("# vtk DataFile Version 1.0");
		save.setzeZeile("Trennung im 128*128*128 Gitter");
		save.setzeZeile("ASCII");
		save.setzeZeile("DATASET STRUCTURED_POINTS");
		save.setzeZeile("DIMENSIONS "+(Gitter_x/maxBox_x) + " " +(Gitter_y/maxBox_y) +" "+(Gitter_z/maxBox_z));
		save.setzeZeile("ORIGIN 0.0 0.0 0.0");
		save.setzeZeile("ASPECT_RATIO 1.0 1.0 1.0");
		save.setzeZeile("");
		save.setzeZeile("POINT_DATA " +(Gitter_x/maxBox_x * Gitter_y/maxBox_y*Gitter_z/maxBox_z));
		save.setzeZeile("SCALARS scalars double");
		save.setzeZeile("LOOKUP_TABLE default");
		
		for(int z = 0; z < (Gitter_z/maxBox_z);z++)
		{
			String zhl = "";
			
			for(int y = 0; y < (Gitter_y/maxBox_y);y++)
				for(int x = 0; x < (Gitter_x/maxBox_x);x++)
				{	
					if (Double.isNaN(Ordnungsgitter[x][y][z]))
						zhl +=  "0.0 ";
					else zhl += ((float) Ordnungsgitter[x][y][z]) + " ";
				}
			
			save.setzeZeile(zhl);
					
		}
		save.DateiSchliessen();
		//save.setzeZeile("#   durchschnittbindung :" + durchschnittbond.ReturnM1() + " bei N = "+ durchschnittbond.ReturnN());

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
		
	

	
	
		
}
