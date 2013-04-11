package EvalToolProject_bcc.SingleObjects;
import java.text.DecimalFormat;
import java.util.List;


import EvalToolProject_bcc.*;
//import EvalToolProject.Graph.Node;
import EvalToolProject_bcc.tools.BFMFileSaver;
import EvalToolProject_bcc.tools.BFMImportData;
import EvalToolProject_bcc.tools.Dijkstra;
import EvalToolProject_bcc.tools.Edge;
import EvalToolProject_bcc.tools.Statistik;
import EvalToolProject_bcc.tools.Vertex;

public class Auswertung_LineareObjekte_MinimalPath {


	
	
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

	
	//Int_IntArrayList_Hashtable Bindungsnetzwerk; 
	
	
	
	/*Statistik[] MessungG1;
	Statistik[] MessungG3;
	
	double[] RcmX;
	double[] RcmY;
	double[] RcmZ;
	
	double[][] RX;
	double[][] RY;
	double[][] RZ;*/
	
	Statistik[] L_t;
	Statistik[] c_t;
	Statistik[] Rg_t;
	
	int MonoStrang=0;
	int dt=0;
	int maxFrame = 0;
	
	//Graph conGraph;
	Vertex[] Knoten;
	
	Statistik durchschnittbond;
	
	public Auswertung_LineareObjekte_MinimalPath(String prob)
	{
		
		
		Polymersystem = new int[1];
		durchschnittbond = new Statistik();

		
		
		 MonoStrang=1000;
		 dt=10000;
		 maxFrame = 3001;//51;
		 //String prob = "1_0";
		 
		 
		 
		
		 /*Knoten = new Vertex[1001];
		 
		 for(int zu = 0; zu < 1001; zu++)
			 Knoten[zu] = new Vertex(""+zu);
		 */
		/* Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  1.0)};
		 Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  1.0)};
		 
		 for(int zu = 2; zu < 1000; zu++)
			 Knoten[zu].adjacencies = new Edge[]{ new Edge(Knoten[zu-1],  1.0),new Edge(Knoten[zu+1],  1.0) };
		 */
		 
		 //Dijkstra.computePaths(Knoten[1]);
		 //System.out.println("Distance to " + Knoten[10] + ": " + Knoten[10].minDistance);
		 //List<Vertex> path = Dijkstra.getShortestPathTo(Knoten[10]);
		 //System.out.println("Path: " + path);
		
		// for(int zu = 0; zu < 1001; zu++)
		//	 Knoten[zu].minDistance = Double.POSITIVE_INFINITY;
		 
		 
		 L_t = new Statistik[maxFrame+1];
		 
		 for(int i = 0; i < maxFrame; i++)
			L_t[i] = new Statistik(); 
		 
		 c_t = new Statistik[maxFrame+1];
		 
		 for(int i = 0; i < maxFrame; i++)
			c_t[i] = new Statistik(); 
		 
		 Rg_t = new Statistik[maxFrame+1];
		 
		 for(int i = 0; i < maxFrame; i++)
			Rg_t[i] = new Statistik(); 
		 
		/* MessungG1 = new Statistik[maxFrame+2];
		 MessungG3 = new Statistik[maxFrame+2];
		
		 for(int i = 0; i <= maxFrame; i++)
				MessungG1[i] = new Statistik(); 
			
			for(int i = 0; i <= maxFrame; i++)
				MessungG3[i] = new Statistik();
		 
			RcmX = new double[maxFrame+2];
			 RcmY = new double[maxFrame+2];
			 RcmZ = new double[maxFrame+2];
			 
			 RX = new double[maxFrame+2][MonoStrang+1];
			 RY = new double[maxFrame+2][MonoStrang+1];
			 RZ = new double[maxFrame+2][MonoStrang+1];
			 */
	
	
		FileName = "Linear1000__0001test.bfm";
		
		//FileDirectory = "/home/users/dockhorn/Marco_BFM_Sources/Bfm_2010.08.18/";
		FileDirectory = "/home/users/dockhorn/Messung/LoopCreation20100802/p_"+prob+"/";
		
		
		System.out.println("file : " +FileName );
		System.out.println("dir : " + FileDirectory);
		
		DecimalFormat dh = new DecimalFormat("0000");
		
		//for(int i = 0; i <= 9; i++)
		//	LoadFile("buerste_straight_25_0"+i+".bfm", 1);
		//LoadFile(FileName, currentFrame);
		
		for(int i = 1; i <= 2500; i++)
		{	
			skipFrames = 0;
			currentFrame = 1;
			FileName = "Linear1000__"+dh.format(i)+".bfm";
			LoadFile(FileName,currentFrame);//"Linear_N"+MonoStrang+"__"+dh.format(i)+"_MSD.bfm",1);
		}
		
		
		BFMFileSaver save_MinPath = new BFMFileSaver();
		save_MinPath.DateiAnlegen("/home/users/dockhorn/Messung/LoopCreation20100802/MinPath/MinPath_p_"+prob+".dat",false);
		
		save_MinPath.setzeZeile("# Probability:"+prob+"   added bonds: "+durchschnittbond.ReturnM1());
		save_MinPath.setzeZeile("# t <L(t)> d<L(t)>");
		
		for(int l = 0; l < maxFrame; l++)//10001; l++)
		{
			save_MinPath.setzeZeile(l*dt  + " " +L_t[l].ReturnM1() +" "+  ( 2.0* L_t[l].ReturnSigma()/ Math.sqrt(1.0*L_t[l].ReturnN()))  );
		}
		
		save_MinPath.DateiSchliessen();
		
		BFMFileSaver save_MinPathRee = new BFMFileSaver();
		save_MinPathRee.DateiAnlegen("/home/users/dockhorn/Messung/LoopCreation20100802/MinPath/MinPathRee_p_"+prob+".dat",false);
		
		save_MinPathRee.setzeZeile("# Probability:"+prob+"   added bonds: "+durchschnittbond.ReturnM1());
		save_MinPathRee.setzeZeile("# t <c(t)> d<c(t)>");
		
		for(int l = 0; l < maxFrame; l++)//10001; l++)
		{
			save_MinPathRee.setzeZeile(l*dt  + " " +c_t[l].ReturnM1() +" "+  ( 2.0* c_t[l].ReturnSigma()/ Math.sqrt(1.0*c_t[l].ReturnN()))  );
		}
		
		save_MinPathRee.DateiSchliessen();
		
		BFMFileSaver save_Rg = new BFMFileSaver();
		save_Rg.DateiAnlegen("/home/users/dockhorn/Messung/LoopCreation20100802/MinPath/Rg_p_"+prob+".dat",false);
		
		save_Rg.setzeZeile("# Probability:"+prob+"   added bonds: "+durchschnittbond.ReturnM1());
		save_Rg.setzeZeile("# t <Rg2(t)> <(Rg2(t))2> d<Rg(t)>");
		
		for(int l = 0; l < maxFrame; l++)//10001; l++)
		{
			save_Rg.setzeZeile(l*dt  + " " +Rg_t[l].ReturnM1() + " " +Rg_t[l].ReturnM2() +" "+ ( 2.0* Rg_t[l].ReturnSigma()/ Math.sqrt(1.0*Rg_t[l].ReturnN()))  );
		}
		
		save_Rg.DateiSchliessen();
		
		
		System.out.println("durchschnittbindung :" + durchschnittbond.ReturnM1());
	}
	
	protected void LoadFile(String file, int startframe)
	{
		FileName = file;
		
		System.out.println("lade System");
		LadeSystem(FileDirectory, FileName);	
		
		System.out.println();
		System.out.println("bonds :" +importData.bonds.size());
		System.out.println("addbonds :" +importData.additionalbonds.size());
		System.out.println();
		
		durchschnittbond.AddValue(importData.additionalbonds.size());
		
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
		new Auswertung_LineareObjekte_MinimalPath(args[0]);
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

				Knoten = null;
				Knoten = new Vertex[1001];
				 
				 for(int zu = 1; zu < 1001; zu++)
					 Knoten[zu] = new Vertex(""+zu);
				 
				/* Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  1.0)};
				 Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  1.0)};
				 
				 for(int zu = 2; zu < 1000; zu++)
					 Knoten[zu].adjacencies = new Edge[]{ new Edge(Knoten[zu-1],  1.0),new Edge(Knoten[zu+1],  1.0) };
				 */
				 
				 //Dijkstra.computePaths(Knoten[1]);
				 //System.out.println("Distance to " + Knoten[10] + ": " + Knoten[10].minDistance);
				 //List<Vertex> path = Dijkstra.getShortestPathTo(Knoten[10]);
				 //System.out.println("Path: " + path);
				
				/* for(int zu = 0; zu < 1001; zu++)
					 Knoten[zu].minDistance = Double.POSITIVE_INFINITY;
				 
				//Reset der Berechnungen und fuellen mit neuen Abstandsdaten
				for(int zu = 0; zu < 1001; zu++)
				{
					 Knoten[zu].minDistance = Double.POSITIVE_INFINITY;
					// Knoten[zu].previous = null;
				}*/
				
				/*Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  importData.GetDistance(1,2))};
				Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  importData.GetDistance(1000,999))};
				 
				 for(int zu = 2; zu < 1000; zu++)
					 Knoten[zu].adjacencies = new Edge[]{ new Edge(Knoten[zu-1],  importData.GetDistance(zu,zu-1)),new Edge(Knoten[zu+1],  importData.GetDistance(zu,zu+1)) };
				 
				
				 
				
				for(int i = 0; i < importData.additionalbonds.size(); i++)
				{
					//conGraph.addNode(""+getMono1Nr(importData.additionalbonds.get(i))).setCost(Graph.Node.H_COST, 2);
					//conGraph.addNode(""+getMono2Nr(importData.additionalbonds.get(i))).setCost(Graph.Node.H_COST, 2);
					if((getMono1Nr(importData.additionalbonds.get(i)) == 1) || (getMono2Nr(importData.additionalbonds.get(i)) == 1) || (getMono1Nr(importData.additionalbonds.get(i)) == 1000) || (getMono2Nr(importData.additionalbonds.get(i)) == 1000))
					{
						if((getMono1Nr(importData.additionalbonds.get(i)) == 1) && (getMono2Nr(importData.additionalbonds.get(i)) == 1000))
						{
							Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  importData.GetDistance(1,2)),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], importData.GetDistance(1,1000) )};
							Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  importData.GetDistance(1000,999)),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], importData.GetDistance(1000,1))};
						}
						
						if((getMono2Nr(importData.additionalbonds.get(i)) == 1) && (getMono1Nr(importData.additionalbonds.get(i)) == 1000))
						{
							Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  importData.GetDistance(1,2)),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], importData.GetDistance(1,1000) )};
							Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  importData.GetDistance(1000,999)),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], importData.GetDistance(1,1000) )};
						}
						
						if((getMono1Nr(importData.additionalbonds.get(i)) == 1) && (getMono2Nr(importData.additionalbonds.get(i)) != 1000))
						{
							Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  importData.GetDistance(1,2)),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], importData.GetDistance(1,getMono2Nr(importData.additionalbonds.get(i))))};
							Knoten[getMono2Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))-1],  importData.GetDistance(getMono2Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i))-1)),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))+1],  importData.GetDistance(getMono2Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i))+1)), new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], importData.GetDistance(getMono2Nr(importData.additionalbonds.get(i)),getMono1Nr(importData.additionalbonds.get(i)))) };
						}
						
						if((getMono2Nr(importData.additionalbonds.get(i)) == 1) && (getMono1Nr(importData.additionalbonds.get(i)) != 1000))
						{
							Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  importData.GetDistance(1,2)),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], importData.GetDistance(1,getMono1Nr(importData.additionalbonds.get(i))) )};
							Knoten[getMono1Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))-1],  importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono1Nr(importData.additionalbonds.get(i))-1)),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))+1],  importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono1Nr(importData.additionalbonds.get(i))+1)), new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i)))) };
						}
						
						if((getMono1Nr(importData.additionalbonds.get(i)) == 1000) && (getMono2Nr(importData.additionalbonds.get(i)) != 1))
						{
							Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  importData.GetDistance(1000,999)),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i))) )};
							Knoten[getMono2Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))-1],  importData.GetDistance(getMono2Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i))-1)),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))+1],  importData.GetDistance(getMono2Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i))+1)), new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i)))) };
						}
						
						if((getMono2Nr(importData.additionalbonds.get(i)) == 1000) && (getMono1Nr(importData.additionalbonds.get(i)) != 1))
						{
							Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  importData.GetDistance(1000,999)),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i))) )};
							Knoten[getMono1Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))-1],  importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono1Nr(importData.additionalbonds.get(i))-1)),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))+1],  importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono1Nr(importData.additionalbonds.get(i))+1)), new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i)))) };
						}
					
						
					}
					
					
					
					else
						//if((getMono1Nr(importData.additionalbonds.get(i)) != 1) && (getMono2Nr(importData.additionalbonds.get(i)) != 1) && (getMono1Nr(importData.additionalbonds.get(i)) != 1000) && (getMono2Nr(importData.additionalbonds.get(i)) != 1000))
					{
						Knoten[getMono1Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))-1],  importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono1Nr(importData.additionalbonds.get(i))-1)),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))+1],  importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono1Nr(importData.additionalbonds.get(i))+1)), new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i)))) };
						Knoten[getMono2Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))-1],  importData.GetDistance(getMono2Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i))-1)),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))+1],  importData.GetDistance(getMono2Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i))+1)), new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i)))) };
					}
					 
					
				}*/
				
				 Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  1.0)};
					Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  1.0)};
					 
					 for(int zu = 2; zu < 1000; zu++)
						 Knoten[zu].adjacencies = new Edge[]{ new Edge(Knoten[zu-1],  1.0),new Edge(Knoten[zu+1],  1.0) };
					 
					
					 
					
					for(int i = 0; i < importData.additionalbonds.size(); i++)
					{
						if((getMono1Nr(importData.additionalbonds.get(i)) == 1) || (getMono2Nr(importData.additionalbonds.get(i)) == 1) || (getMono1Nr(importData.additionalbonds.get(i)) == 1000) || (getMono2Nr(importData.additionalbonds.get(i)) == 1000))
						{
							if((getMono1Nr(importData.additionalbonds.get(i)) == 1) && (getMono2Nr(importData.additionalbonds.get(i)) == 1000))
							{
								Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  1.0),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], 1.0 )};
								Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  1.0),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], 1.0)};
							}
							
							if((getMono2Nr(importData.additionalbonds.get(i)) == 1) && (getMono1Nr(importData.additionalbonds.get(i)) == 1000))
							{
								Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  1.0),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], 1.0 )};
								Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  1.0),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], 1.0 )};
							}
							
							if((getMono1Nr(importData.additionalbonds.get(i)) == 1) && (getMono2Nr(importData.additionalbonds.get(i)) != 1000))
							{
								Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  1.0),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], 1.0)};
								Knoten[getMono2Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))-1],  1.0),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))+1],  1.0), new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], 1.0) };
							}
							
							if((getMono2Nr(importData.additionalbonds.get(i)) == 1) && (getMono1Nr(importData.additionalbonds.get(i)) != 1000))
							{
								Knoten[1].adjacencies = new Edge[]{ new Edge(Knoten[2],  1.0),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], 1.0 )};
								Knoten[getMono1Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))-1],  1.0),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))+1],  1.0), new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], 1.0) };
							}
							
							if((getMono1Nr(importData.additionalbonds.get(i)) == 1000) && (getMono2Nr(importData.additionalbonds.get(i)) != 1))
							{
								Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  1.0),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], 1.0 )};
								Knoten[getMono2Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))-1],  1.0),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))+1],  1.0), new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], 1.0) };
							}
							
							if((getMono2Nr(importData.additionalbonds.get(i)) == 1000) && (getMono1Nr(importData.additionalbonds.get(i)) != 1))
							{
								Knoten[1000].adjacencies = new Edge[]{ new Edge(Knoten[999],  1.0),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], 1.0 )};
								Knoten[getMono1Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))-1],  1.0),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))+1],  1.0), new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], 1.0) };
							}
						
							
						}
						
						
						
						else
							//if((getMono1Nr(importData.additionalbonds.get(i)) != 1) && (getMono2Nr(importData.additionalbonds.get(i)) != 1) && (getMono1Nr(importData.additionalbonds.get(i)) != 1000) && (getMono2Nr(importData.additionalbonds.get(i)) != 1000))
						{
							Knoten[getMono1Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))-1],  1.0),new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))+1],  1.0), new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))], 1.0) };
							Knoten[getMono2Nr(importData.additionalbonds.get(i))].adjacencies = new Edge[]{ new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))-1],  1.0),new Edge(Knoten[getMono2Nr(importData.additionalbonds.get(i))+1],  1.0), new Edge(Knoten[getMono1Nr(importData.additionalbonds.get(i))], 1.0) };
						}
						 
						
					}
				
				 
				 Dijkstra.computePaths(Knoten[1]);
				 
				/* for (Vertex v : Knoten)
					{
					    System.out.println("Distance to " + v + ": " + v.minDistance);
					    List<Vertex> path = Dijkstra.getShortestPathTo(v);
					    System.out.println("Path: " + path);
					}*/
				
				//System.out.println("Distance to " + Knoten[1000] + ": " + Knoten[1000].minDistance);
				
				if (Knoten[1000].minDistance == Double.POSITIVE_INFINITY)
				{
					
				}
					List<Vertex> path = Dijkstra.getShortestPathTo(Knoten[1000]);
					 //System.out.println("Path: " + path);
					// System.out.println("Distance to " + Knoten[1000] + ": " + Knoten[1000].minDistance + "  "+"   used monomers:"+ path.size() + "   added monomers:"+ importData.additionalbonds.size());
					// System.out.println("used monomers:"+ path.size());
					// System.out.println("added monomers:"+ importData.additionalbonds.size());
					 
					// for(int i = 0; i < path.size()-1; i++ )
						// System.out.print(" " + Integer.parseInt(path.get(i).name) );
					 
					 double hg =0.0;
					 for(int i = 0; i < path.size()-1; i++ )						 
						 hg += importData.GetDistance(Integer.parseInt(path.get(i).name),Integer.parseInt(path.get(i).name)+1);
					
					 //System.out.flush();
					 //System.out.println(frame+ "Pathlegth:" + hg);
				
				/* List<Vertex> path = Dijkstra.getShortestPathTo(Knoten[1000]);
				 System.out.println("Path: " + path);
				 System.out.println("used monomers:"+ path.size());
				 System.out.println("added monomers:"+ importData.additionalbonds.size());
				 
				*/
				 
				 L_t[(int)(importData.MCSTime)/dt].AddValue(hg);//Knoten[1000].minDistance);
				 
				 
				 c_t[(int)(importData.MCSTime)/dt].AddValue(importData.GetDistanceWithoutCheck(1, 1000)/hg);
				 
				 
				 
				 	double Rg2 = 0.0;
					double Rg2_x = 0.0;
					double Rg2_y = 0.0;
					double Rg2_z = 0.0;
						
					for (int i= 1; i <= 1000; i++)
					  for (int j = i; j <= 1000; j++)
					  {
						  Rg2_x += 1.0*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0])*(importData.PolymerKoordinaten[i][0] - importData.PolymerKoordinaten[j][0]);
						  Rg2_y += 1.0*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1])*(importData.PolymerKoordinaten[i][1] - importData.PolymerKoordinaten[j][1]);
						  Rg2_z += 1.0*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2])*(importData.PolymerKoordinaten[i][2] - importData.PolymerKoordinaten[j][2]);

						  
					  }
					 
					Rg2 = Rg2_x + Rg2_y + Rg2_z;
					 
					  //Rg2_x /= 1.0*((4*NrOfMonomersPerStarArm + 1)*(4*NrOfMonomersPerStarArm + 1));
					 // Rg2_y /= 1.0*((4*NrOfMonomersPerStarArm + 1)*(4*NrOfMonomersPerStarArm + 1));
					  //Rg2_z /= 1.0*((4*NrOfMonomersPerStarArm + 1)*(4*NrOfMonomersPerStarArm + 1));
					  Rg2 /= 1.0*1000.0*1000.0;//(4*NrOfMonomersPerStarArm + 1));
					  
					//rg_x_stat.AddValue(Rg2_x);
					//rg_y_stat.AddValue(Rg2_y);
					//rg_z_stat.AddValue(Rg2_z);
					Rg_t[(int)(importData.MCSTime)/dt].AddValue(Rg2);
				/* for(int i = 0; i < importData.bonds.size(); i++)
					 importData.GetDistance(getMono1Nr(importData.bonds.get(i)),getMono2Nr(importData.bonds.get(i)));

				 for(int i = 0; i < importData.additionalbonds.size(); i++)
					 importData.GetDistance(getMono1Nr(importData.additionalbonds.get(i)),getMono2Nr(importData.additionalbonds.get(i)));
				 */
				 
				 /*
				double Rcm_x1 = 0.0;
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
				
				
				
				
				RcmX[(int) (importData.MCSTime/dt)] = Rcm_x1;
				RcmY[(int) (importData.MCSTime/dt)] = Rcm_y1;
				RcmZ[(int) (importData.MCSTime/dt)] = Rcm_z1;
				
				
				for (int zk = 1; zk <= MonoStrang ;zk++)
				{
				RX[(int) (importData.MCSTime/dt)][zk] = importData.PolymerKoordinaten[zk][0] + 0.5;
				RY[(int) (importData.MCSTime/dt)][zk] = importData.PolymerKoordinaten[zk][1] + 0.5;
				RZ[(int) (importData.MCSTime/dt)][zk] = importData.PolymerKoordinaten[zk][2] + 0.5;
				}*/
				
				
					
	}
	
	/*public void BerechneG1()
	{
		
		for(int i = 0; i <= maxFrame;i++)
		{
			double summe = 0.0;
			for(int kl=1; kl <=MonoStrang; kl++)
				summe += (RX[i][kl]-RX[0][kl])*(RX[i][kl]-RX[0][kl]) + (RY[i][kl]-RY[0][kl])*(RY[i][kl]-RY[0][kl]) + (RZ[i][kl]-RZ[0][kl])*(RZ[i][kl]-RZ[0][kl]);
			
			summe /= (1.0*MonoStrang);
			
			MessungG1[i].AddValue( summe);
		}
		
	}
	
	public void BerechneG3()
	{
		
		for(int i = 0; i <= maxFrame;i++)
		MessungG3[i].AddValue( (RcmX[i]-RcmX[0])*(RcmX[i]-RcmX[0]) + (RcmY[i]-RcmY[0])*(RcmY[i]-RcmY[0]) + (RcmZ[i]-RcmZ[0])*(RcmZ[i]-RcmZ[0]));
	
	}*/
	
	

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
