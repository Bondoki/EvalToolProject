package EvalToolProject_bcc.SingleObjects;
import java.text.DecimalFormat;

import EvalToolProject_bcc.tools.BFMFileLoader;
import EvalToolProject_bcc.tools.BFMFileSaver;
import EvalToolProject_bcc.tools.BFMImportData;
import EvalToolProject_bcc.tools.Int_IntArrayList_Hashtable;
import EvalToolProject_bcc.tools.Statistik;

public class Auswertung_Doppelstrang_Winkel {


	
	
	String FileName;
	String FileDirectory;
	
	int[] Polymersystem;
	int NrofFrames;
	
	int skipFrames;
	int currentFrame;
	
	int Gitter_z;
		
	BFMImportData importData;
	
	int Kettenanzahl;
	int Kettenlaenge;
	
	int[] dumpsystem;

	

	int MONOMERZAHL;

	
	Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	
	
	
	BFMFileSaver saveWinkel = new BFMFileSaver();
	BFMFileSaver saveWinkelQuad = new BFMFileSaver();
	BFMFileSaver saveWinkelEbene = new BFMFileSaver();
	
	BFMFileSaver savePunkte = new BFMFileSaver();
	
	BFMFileSaver saveTwistElementKette1 = new BFMFileSaver();
	BFMFileSaver saveTwistElementKette2 = new BFMFileSaver();
	
	int MonoProStrang=0;
	Statistik WinkelStat[];
	Statistik WinkelStatQuad[];
	Statistik WinkelStatEbene[];
	
	Statistik Twist_StatElementKette1[];
	Statistik Twist_StatElementKette2[];
	
	String Name;
	
	public Auswertung_Doppelstrang_Winkel()
	{
		
		
		Polymersystem = new int[1];
		
		//System.out.println("cf="+currentFrame);
		
		 MonoProStrang=25;//250;
		 //String Name = "DoppelstrangA250_fest";
		 Name = "DoppelstrangA25MSD";
		//Name = "Strang0_75Drehung";
		
		WinkelStat = new Statistik[MonoProStrang+1];
		 for (int i = 0; i <= MonoProStrang; i++)
			 WinkelStat[i] = new Statistik();
		 
		 WinkelStatQuad = new Statistik[MonoProStrang+1];
		 for (int i = 0; i <= MonoProStrang; i++)
			 WinkelStatQuad[i] = new Statistik();
		 
		 WinkelStatEbene = new Statistik[MonoProStrang+1];
		 for (int i = 0; i <= MonoProStrang; i++)
			 WinkelStatEbene[i] = new Statistik();
	
		 Twist_StatElementKette1= new Statistik[2400];
		 	for (int i = 0; i < 2400; i++)
		 		Twist_StatElementKette1[i] = new Statistik();

		 Twist_StatElementKette2= new Statistik[2400];
		 	for (int i = 0; i < 2400; i++)
		 		Twist_StatElementKette2[i] = new Statistik();
		 	//FileName = "1024_1024_0.00391_32";
		//FileDirectory  = "/home/users/dockhorn/workspace/Evaluation_Tools/";
		FileDirectory = "/home/users/dockhorn/MessungDiplom/MessunGGW/";
		
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		DecimalFormat dh = new DecimalFormat("0000");
		
		
		saveWinkel.DateiAnlegen(Name+"_WinkelSkalarprodukt.dat",false);
		saveWinkelQuad.DateiAnlegen(Name+"_WinkelQuadSkalarprodukt.dat",false);
		saveWinkelEbene.DateiAnlegen(Name+"_WinkelEbene.dat",false);
			
		saveTwistElementKette1.DateiAnlegen(Name+"_WindungElementKette1.dat",false);
		saveTwistElementKette2.DateiAnlegen(Name+"_WindungElementKette2.dat",false);
		
		skipFrames = 0;
		currentFrame = 1;
		
		LoadFile(Name+".bfm",1);
		
		saveWinkel.setzeZeile("# Abstand[Mono] <phi>[°] dF");
		saveWinkelQuad.setzeZeile("# Abstand[Mono] <phi**2>[°] dF");
		saveWinkelEbene.setzeZeile("# Abstand[Mono] <phi>[°] dF");
		saveTwistElementKette1.setzeZeile("# Abstand[Mono] <torsion>[2pi] <torsion^2>[2pi] dF");
		saveTwistElementKette2.setzeZeile("# Abstand[Mono] <torsion>[2pi] <torsion^2>[2pi] dF");
		
		for (int i = 1; i < MonoProStrang; i++)
		{
			saveWinkel.setzeZeile(i +" "+ WinkelStat[i].ReturnM1() +" "+ ( 2.0* WinkelStat[i].ReturnSigma()/ Math.sqrt(1.0*WinkelStat[i].ReturnN())));
			saveWinkelQuad.setzeZeile(i +" "+ WinkelStatQuad[i].ReturnM1() +" "+ ( 2.0* WinkelStatQuad[i].ReturnSigma()/ Math.sqrt(1.0*WinkelStatQuad[i].ReturnN())));
			saveWinkelEbene.setzeZeile(i +" "+ WinkelStatEbene[i].ReturnM1() +" "+ ( 2.0* WinkelStatEbene[i].ReturnSigma()/ Math.sqrt(1.0*WinkelStatEbene[i].ReturnN())));

		}
		
		for (int i = 0; i < 2399; i++)
		{
			saveTwistElementKette1.setzeZeile((1.01+i*0.01) +" "+ Twist_StatElementKette1[i].ReturnM1() +" "+ Twist_StatElementKette1[i].ReturnM2() +" "+ ( 2.0* Twist_StatElementKette1[i].ReturnSigma()/ Math.sqrt(1.0*Twist_StatElementKette1[i].ReturnN())) + " " + Twist_StatElementKette1[i].ReturnN());
			saveTwistElementKette2.setzeZeile((1.01+i*0.01) +" "+ Twist_StatElementKette2[i].ReturnM1() +" "+ Twist_StatElementKette2[i].ReturnM2() +" "+ ( 2.0* Twist_StatElementKette2[i].ReturnSigma()/ Math.sqrt(1.0*Twist_StatElementKette2[i].ReturnN())) + " " + Twist_StatElementKette2[i].ReturnN());

		}
		
		saveWinkel.DateiSchliessen();
		saveWinkelQuad.DateiSchliessen();
	    saveWinkelEbene.DateiSchliessen();
	    saveTwistElementKette1.DateiSchliessen();
	    saveTwistElementKette2.DateiSchliessen();
	}
	
	protected void LoadFile(String file, int startframe)
	{
		FileName = file;
		
		System.out.println("lade System");
		LadeSystem(FileDirectory, FileName);	
		
		currentFrame = startframe;
		  
		importData.OpenSimulationFile(FileDirectory+FileName);
		  
		  System.arraycopy(importData.GetFrameOfSimulation(currentFrame),0,Polymersystem,0, Polymersystem.length);
		  
		  importData.CloseSimulationFile();
		  
		  importData.OpenSimulationFile(FileDirectory+FileName);
			
		  
			System.out.println("file : " +FileName );
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
		    
		  
		    
		   
			
			currentFrame = z;
			
			if( (currentFrame+skipFrames) >= NrofFrames)
				currentFrame =  NrofFrames;
			
		
	}
	
	 
	  
	  
	  
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new Auswertung_Doppelstrang_Winkel();
	}
	
	public void LadeSystem(String FileDir, String FileName)
	{
		importData=null;
		importData = new BFMImportData(FileDir+FileName);
		
		MONOMERZAHL = importData.NrOfMonomers + 1;
		
		Gitter_z = importData.box_z;
		
		
		
		Polymersystem = null;
		Polymersystem = new int[MONOMERZAHL];
		
		dumpsystem = null;
		dumpsystem = new int[MONOMERZAHL];
	
		boolean periodRB_x = importData.periodic_x;
		boolean periodRB_y = importData.periodic_y;
		boolean periodRB_z = importData.periodic_z;
		
		Kettenlaenge = importData.NrOfMonomers;
		Kettenanzahl = 1;
		
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
				
				System.arraycopy(importData.GetFrameOfSimulation( frame),0,dumpsystem,0, dumpsystem.length);

				//int AnzahlMono = dumpsystem.length-1;
				
				/*if(importData.MCSTime < time_t0)
					return;
				*/
				
				
				/*double vec1_x, vec1_y, vec1_z = 0.0;
				double vec2_x, vec2_y, vec2_z = 0.0;
				
				vec1_x = importData.PolymerKoordinaten[MonoProStrang+1][0] - importData.PolymerKoordinaten[1][0];
				vec1_y = importData.PolymerKoordinaten[MonoProStrang+1][1] - importData.PolymerKoordinaten[1][1];
				vec1_z = importData.PolymerKoordinaten[MonoProStrang+1][2] - importData.PolymerKoordinaten[1][2];
				
				double winkel = 0;
				//nur ueber Skalarprodukt
				 for (int zk = 1; zk <= MonoProStrang ;zk++)
				 {
					 vec2_x = importData.PolymerKoordinaten[MonoProStrang+zk][0] - importData.PolymerKoordinaten[zk][0];
					 vec2_y = importData.PolymerKoordinaten[MonoProStrang+zk][1] - importData.PolymerKoordinaten[zk][1];
					 vec2_z = importData.PolymerKoordinaten[MonoProStrang+zk][2] - importData.PolymerKoordinaten[zk][2];
					 
					 double coswinkel = (vec1_x*vec2_x +vec1_y*vec2_y+ vec1_z*vec2_z)/(Math.sqrt(vec1_x*vec1_x +vec1_y*vec1_y+ vec1_z*vec1_z)*Math.sqrt(vec2_x*vec2_x +vec2_y*vec2_y+ vec2_z*vec2_z));
				
					 if(coswinkel > 1.0)
					 {
						 //System.out.println("Manuelle Aenderung Winkel " + coswinkel);
						 coswinkel = 1.0;
					 }
					 
					 if(coswinkel < -1.0)
					 {
						 //System.out.println("Manuelle Aenderung Winkel " + coswinkel);
						 coswinkel = -1.0;
					 }
					

					 
					 winkel += Math.acos(coswinkel)*180.0/Math.PI;
					//WinkelStat[zk].AddValue(Math.acos(coswinkel));
					 WinkelStat[zk].AddValue(Math.acos(coswinkel)*180.0/Math.PI);
					 WinkelStatQuad[zk].AddValue(winkel);
					 //WinkelStat[zk].AddValue(coswinkel);
				 }
				 
				 double v1_x,v1_y,v1_z, v2_x,v2_y,v2_z, v3_x,v3_y,v3_z, v4_x,v4_y,v4_z =0.0;
				 
				 for (int zk = 1; zk < MonoProStrang-1 ;zk++)
				 {
					 v1_x = 0.5*(importData.PolymerKoordinaten[zk+1][0] - importData.PolymerKoordinaten[zk][0] + importData.PolymerKoordinaten[MonoProStrang+zk+1][0] - importData.PolymerKoordinaten[MonoProStrang+zk][0]);
					 v1_y = 0.5*(importData.PolymerKoordinaten[zk+1][1] - importData.PolymerKoordinaten[zk][1] + importData.PolymerKoordinaten[MonoProStrang+zk+1][1] - importData.PolymerKoordinaten[MonoProStrang+zk][1]);
					 v1_z = 0.5*(importData.PolymerKoordinaten[zk+1][2] - importData.PolymerKoordinaten[zk][2] + importData.PolymerKoordinaten[MonoProStrang+zk+1][2] - importData.PolymerKoordinaten[MonoProStrang+zk][2]);
					
					 v2_x = 0.5*(importData.PolymerKoordinaten[MonoProStrang+zk][0] - importData.PolymerKoordinaten[zk][0] + importData.PolymerKoordinaten[MonoProStrang+zk+1][0] - importData.PolymerKoordinaten[zk+1][0]);
					 v2_y = 0.5*(importData.PolymerKoordinaten[MonoProStrang+zk][1] - importData.PolymerKoordinaten[zk][1] + importData.PolymerKoordinaten[MonoProStrang+zk+1][1] - importData.PolymerKoordinaten[zk+1][1]);
					 v2_z = 0.5*(importData.PolymerKoordinaten[MonoProStrang+zk][2] - importData.PolymerKoordinaten[zk][2] + importData.PolymerKoordinaten[MonoProStrang+zk+1][2] - importData.PolymerKoordinaten[zk+1][2]);
					
					 double normal1_x, normal1_y, normal1_z = 0.0;
					 normal1_x = v1_y*v2_z - v1_z*v2_y;
					 normal1_y = v1_z*v2_x - v1_x*v2_z;
					 normal1_z = v1_x*v2_y - v1_y*v2_x;
					 
					 double normal1= Math.sqrt(normal1_x*normal1_x+normal1_y*normal1_y+normal1_z*normal1_z);
					 if(normal1 == 0.0 )
						 System.out.println("normal1 null");
					 
					 
					 v3_x = 0.5*(importData.PolymerKoordinaten[zk+2][0] - importData.PolymerKoordinaten[zk+1][0] + importData.PolymerKoordinaten[MonoProStrang+zk+2][0] - importData.PolymerKoordinaten[MonoProStrang+zk+1][0]);
					 v3_y = 0.5*(importData.PolymerKoordinaten[zk+2][1] - importData.PolymerKoordinaten[zk+1][1] + importData.PolymerKoordinaten[MonoProStrang+zk+2][1] - importData.PolymerKoordinaten[MonoProStrang+zk+1][1]);
					 v3_z = 0.5*(importData.PolymerKoordinaten[zk+2][2] - importData.PolymerKoordinaten[zk+1][2] + importData.PolymerKoordinaten[MonoProStrang+zk+2][2] - importData.PolymerKoordinaten[MonoProStrang+zk+1][2]);
					
					 v4_x = 0.5*(importData.PolymerKoordinaten[MonoProStrang+zk+2][0] - importData.PolymerKoordinaten[zk+2][0] + importData.PolymerKoordinaten[MonoProStrang+zk+1][0] - importData.PolymerKoordinaten[zk+1][0]);
					 v4_y = 0.5*(importData.PolymerKoordinaten[MonoProStrang+zk+2][1] - importData.PolymerKoordinaten[zk+2][1] + importData.PolymerKoordinaten[MonoProStrang+zk+1][1] - importData.PolymerKoordinaten[zk+1][1]);
					 v4_z = 0.5*(importData.PolymerKoordinaten[MonoProStrang+zk+2][2] - importData.PolymerKoordinaten[zk+2][2] + importData.PolymerKoordinaten[MonoProStrang+zk+1][2] - importData.PolymerKoordinaten[zk+1][2]);
					
					 double normal2_x, normal2_y, normal2_z = 0.0;
					 normal2_x = v3_y*v4_z - v3_z*v4_y;
					 normal2_y = v3_z*v4_x - v3_x*v4_z;
					 normal2_z = v3_x*v4_y - v3_y*v4_x;
					 
					 double normal2= Math.sqrt(normal2_x*normal2_x+normal2_y*normal2_y+normal2_z*normal2_z);
					 if(normal2 == 0.0 )
						 System.out.println("normal2 null");
					 
					 
					 double coswinkel = (normal1_x*normal2_x + normal1_y*normal2_y+ normal1_z*normal2_z)/(normal1*normal2);
						
					 if(coswinkel > 1.0)
					 {
						 //System.out.println("Manuelle Aenderung Winkel " + coswinkel);
						 coswinkel = 1.0;
					 }
					 
					 if(coswinkel < -1.0)
					 {
						 //System.out.println("Manuelle Aenderung Winkel " + coswinkel);
						 coswinkel = -1.0;
					 }
					
					//WinkelStat[zk].AddValue(Math.acos(coswinkel));
					 //WinkelStat[zk].AddValue(Math.acos(coswinkel)*180.0/Math.PI);
					 //WinkelStatQuad[zk].AddValue((Math.acos(coswinkel)*180.0/Math.PI)*(Math.acos(coswinkel)*180.0/Math.PI));
					 WinkelStatEbene[zk].AddValue(Math.acos(coswinkel)*180.0/Math.PI);
				 }
	
				 
				 double t1_x,t1_y,t1_z, t2_x,t2_y,t2_z =0.0;
				 
				 for (int zk = 1; zk < MonoProStrang-1 ;zk++)
				 {
					 t1_x = importData.PolymerKoordinaten[zk+1][0] - importData.PolymerKoordinaten[zk][0];
					 t1_y = importData.PolymerKoordinaten[zk+1][1] - importData.PolymerKoordinaten[zk][1];
					 t1_z = importData.PolymerKoordinaten[zk+1][2] - importData.PolymerKoordinaten[zk][2];
					
					 t2_x = importData.PolymerKoordinaten[MonoProStrang+zk][0] - importData.PolymerKoordinaten[zk][0];
					 t2_y = importData.PolymerKoordinaten[MonoProStrang+zk][1] - importData.PolymerKoordinaten[zk][1];
					 t2_z = importData.PolymerKoordinaten[MonoProStrang+zk][2] - importData.PolymerKoordinaten[zk][2];
					
					 double normal1_x, normal1_y, normal1_z = 0.0;
					 normal1_x = t1_y*t2_z - t1_z*t2_y;
					 normal1_y = t1_z*t2_x - t1_x*t2_z;
					 normal1_z = t1_x*t2_y - t1_y*t2_x;
					 
					 double normal1= Math.sqrt(normal1_x*normal1_x+normal1_y*normal1_y+normal1_z*normal1_z);
					 if(normal1 == 0.0 )
						 System.out.println("normal1 null tw");
					 
					 
					 v3_x = 0.5*(importData.PolymerKoordinaten[zk+2][0] - importData.PolymerKoordinaten[zk+1][0] + importData.PolymerKoordinaten[MonoProStrang+zk+2][0] - importData.PolymerKoordinaten[MonoProStrang+zk+1][0]);
					 v3_y = 0.5*(importData.PolymerKoordinaten[zk+2][1] - importData.PolymerKoordinaten[zk+1][1] + importData.PolymerKoordinaten[MonoProStrang+zk+2][1] - importData.PolymerKoordinaten[MonoProStrang+zk+1][1]);
					 v3_z = 0.5*(importData.PolymerKoordinaten[zk+2][2] - importData.PolymerKoordinaten[zk+1][2] + importData.PolymerKoordinaten[MonoProStrang+zk+2][2] - importData.PolymerKoordinaten[MonoProStrang+zk+1][2]);
					
					 v4_x = 0.5*(importData.PolymerKoordinaten[MonoProStrang+zk+2][0] - importData.PolymerKoordinaten[zk+2][0] + importData.PolymerKoordinaten[MonoProStrang+zk+1][0] - importData.PolymerKoordinaten[zk+1][0]);
					 v4_y = 0.5*(importData.PolymerKoordinaten[MonoProStrang+zk+2][1] - importData.PolymerKoordinaten[zk+2][1] + importData.PolymerKoordinaten[MonoProStrang+zk+1][1] - importData.PolymerKoordinaten[zk+1][1]);
					 v4_z = 0.5*(importData.PolymerKoordinaten[MonoProStrang+zk+2][2] - importData.PolymerKoordinaten[zk+2][2] + importData.PolymerKoordinaten[MonoProStrang+zk+1][2] - importData.PolymerKoordinaten[zk+1][2]);
					
					 double normal2_x, normal2_y, normal2_z = 0.0;
					 normal2_x = v3_y*v4_z - v3_z*v4_y;
					 normal2_y = v3_z*v4_x - v3_x*v4_z;
					 normal2_z = v3_x*v4_y - v3_y*v4_x;
					 
					 double normal2= Math.sqrt(normal2_x*normal2_x+normal2_y*normal2_y+normal2_z*normal2_z);
					 if(normal2 == 0.0 )
						 System.out.println("normal2 null");
					 
					 
					 double coswinkel = (normal1_x*normal2_x + normal1_y*normal2_y+ normal1_z*normal2_z)/(normal1*normal2);
						
					 if(coswinkel > 1.0)
					 {
						 //System.out.println("Manuelle Aenderung Winkel " + coswinkel);
						 coswinkel = 1.0;
					 }
					 
					 if(coswinkel < -1.0)
					 {
						 //System.out.println("Manuelle Aenderung Winkel " + coswinkel);
						 coswinkel = -1.0;
					 }
					
					//WinkelStat[zk].AddValue(Math.acos(coswinkel));
					 //WinkelStat[zk].AddValue(Math.acos(coswinkel)*180.0/Math.PI);
					 //WinkelStatQuad[zk].AddValue((Math.acos(coswinkel)*180.0/Math.PI)*(Math.acos(coswinkel)*180.0/Math.PI));
					 WinkelStatEbene[zk].AddValue(Math.acos(coswinkel)*180.0/Math.PI);
				 }
				 
				 */
				System.out.println("frame" + frame);
				
				//if(frame <= 20)
				{
				 savePunkte.DateiAnlegen("/home/users/dockhorn/MessungDiplom/octave/"+Name+"_Punkte.dat",false);
					savePunkte.setzeZeile("# Created by Octave 3.2.4, Fri May 07 16:09:50 2010 CEST <dockhorn@sn14>");
					savePunkte.setzeZeile("# name: B");
					savePunkte.setzeZeile("# type: matrix");
					savePunkte.setzeZeile("# rows: 25");
					savePunkte.setzeZeile("# columns: 6");
					
					for(int i = 1; i <= MonoProStrang;i++)
		    				savePunkte.setzeZeile(importData.PolymerKoordinaten[MonoProStrang+i][0]+ " "+ importData.PolymerKoordinaten[MonoProStrang+i][1] + " " +importData.PolymerKoordinaten[MonoProStrang+i][2] + " " +importData.PolymerKoordinaten[i][0]+ " "+ importData.PolymerKoordinaten[i][1] + " " +importData.PolymerKoordinaten[i][2]);
		    		
					 savePunkte.DateiSchliessen();
					 
					 try {
					      Runtime.getRuntime().exec("octave /home/users/dockhorn/MessungDiplom/octave/AuswertungTwistN25_Kette1.m").waitFor();
					     Thread.sleep(25);
					      Runtime.getRuntime().exec("octave /home/users/dockhorn/MessungDiplom/octave/AuswertungTwistN25_Kette2.m").waitFor();
					      Thread.sleep(25);
					      //Runtime.getRuntime().
					 } catch (Exception e) {
					      System.err.println(e.toString());
					    }
					 
					 
					 BFMFileLoader loader01 = new BFMFileLoader();
					 loader01.DateiOeffnen("/home/users/dockhorn/MessungDiplom/octave/AuswertungTwistN25_StatKette1ElementTorsionKette1.dat");
						
					 String line = "";
					 int i = 0;
					 while ((line = loader01.readNextLine()) != "") {
							Twist_StatElementKette1[i].AddValue(Double.parseDouble(line));
							i++;
						}
					 
					 BFMFileLoader loader02 = new BFMFileLoader();
					 loader02.DateiOeffnen("/home/users/dockhorn/MessungDiplom/octave/AuswertungTwistN25_StatKette1ElementTorsionKette2.dat");
						
					 line = "";
					 i = 0;
					 while ((line = loader02.readNextLine()) != "") {
							Twist_StatElementKette2[i].AddValue(Double.parseDouble(line));
							i++;
						}
				}
				/*double Rcm_x1 = 0.0;
				double Rcm_y1 = 0.0;
				double Rcm_z1 = 0.0;
				
			    for (int zk = 1; zk <= MonoStrang ;zk++)
		  		  {  
		   			Rcm_x1 += importData.PolymerKoordinaten[zk][0] + 0.5;
		   			Rcm_y1 += importData.PolymerKoordinaten[zk][1] + 0.5;
		   			Rcm_z1 += importData.PolymerKoordinaten[zk][2] + 0.5;	 
		  		  }
		  		  
				
				Rcm_x1 /= 1.0*MonoStrang;
				Rcm_y1 /= 1.0*MonoStrang;
				Rcm_z1 /= 1.0*MonoStrang;
				
				
				double Rcm_x2 = 0.0;
				double Rcm_y2 = 0.0;
				double Rcm_z2 = 0.0;

				
				
				
				//Schwerpunkt Ring1
			    for (int zk = (MonoHalb+1); zk <= (2*MonoHalb) ;zk++)
		  		  {  
		   			Rcm_x2 += importData.PolymerKoordinaten[zk][0] + 0.5;
		   			Rcm_y2 += importData.PolymerKoordinaten[zk][1] + 0.5;
		   			Rcm_z2 += importData.PolymerKoordinaten[zk][2] + 0.5;	 
		  		  }
		  		  
				
				Rcm_x2 /= 1.0*MonoHalb;
				Rcm_y2 /= 1.0*MonoHalb;
				Rcm_z2 /= 1.0*MonoHalb;*/
				
				
				
				
				
					
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
