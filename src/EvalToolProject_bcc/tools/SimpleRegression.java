package EvalToolProject_bcc.tools;
import java.util.*;


public class SimpleRegression {

	double dataArray_X[];
	double dataArray_Y[];
	
	int counterDataPoints;
	
	double fitParameter;
	
	double A,C,kappa,Radius,DNull;
	
	public SimpleRegression()
	{
		counterDataPoints = LoadExpData("/home/users/dockhorn/Zuarbeit/SteffiHempel/20101112/Fit/VerschiedeneSpitzen20101112/SiO2nativ_10-2M_KCl__FIBTip2.dat");
		
		A = 9.0398562e12; // nN*(nm)^2/(pC)^2
		//C = 1.0/(72.0*pi); // nN*(nm)^7

		//kappa = 1.0; //nm^-1 bei 10-1M
		kappa = 1.0/3.0; //nm^-1 bei 10-2M
		//kappa = 1.0/10.0; //nm^-1 bei 10-3M
		//kappa = 1.0/30.0; // nm^-1 bei 10-4M
		
		//Radius = 2330.0;//nm CP
		Radius = 50;	//nm FIBTip
		DNull = 0.01; //nm
		
		fitParameter = FitData(1E-18,1E-15, 1E-22);
		
		BFMFileSaver saver = new BFMFileSaver();
		saver.DateiAnlegen("/home/users/dockhorn/Zuarbeit/SteffiHempel/20101112/Fit/VerschiedeneSpitzen20101112/SiO2nativ_10-2M_KCl__FIBTip__FitApproach.dat", false);
		
		saver.setzeKommentar(" Fit-Sigma [pC^2/nm^4]: " +fitParameter);
		for(int i = 0; i < 400; i++)
		{
			saver.setzeZeile((i*0.1)+ " " + Fitfunktion((i*0.1), fitParameter));
		}
		
		saver.DateiSchliessen();
	}
	
	private int LoadExpData(String File){
		
		BFMFileLoader loadFile = new BFMFileLoader();
		loadFile.DateiOeffnen(File);
		
		int couterDataLines = 0;
		
		String line;
		
		while ((line = loadFile.readNextLine()) != "") {
            
	         if ((line.length() > 1) && (line.startsWith("#") != true ))
	         {
	        	 couterDataLines++;
	         }
	   }
		
		loadFile.DateiSchliessen();
		
		System.out.println("DataLines: " + couterDataLines);
		
		dataArray_X = new double[couterDataLines];
		dataArray_Y = new double[couterDataLines];
		
		
		loadFile.DateiOeffnen(File);
		couterDataLines =0;
		
		while ((line = loadFile.readNextLine()) != "") {
            
	         if ((line.length() > 1) && (line.startsWith("#") != true ))
	         {
	        	 
	        	 StringTokenizer st = new StringTokenizer(line);//(line, " ");
	        	 
	        	 dataArray_X[couterDataLines]=Double.parseDouble(st.nextToken());
	        	 dataArray_Y[couterDataLines]=Double.parseDouble(st.nextToken());
	        	 
	        	 couterDataLines++;
	         }
	   }
		
		loadFile.DateiSchliessen();
		
		for(int i = 0; i < couterDataLines; i++)
			System.out.println("i: "+i+ "     X:" +dataArray_X[i] +"    Y:" +dataArray_Y[i]);
		
		
		
		return couterDataLines;
	}
	
	private double FitData(double min, double max, double tolerance)
	{
		
		double parameter1 = min;
		double parameter2 = max;
		boolean foundValue = false;
		
		do
		{	
			foundValue = true;
			double LS_tmp2 = 0.0;
			double LS_tmp1 = 0.0;
			
			for(int i = 0; i < counterDataPoints; i++)
			{
				LS_tmp1 += (dataArray_Y[i]-Fitfunktion(dataArray_X[i], parameter1))*(dataArray_Y[i]-Fitfunktion(dataArray_X[i], parameter1));
			}
			
			for(int i = 0; i < counterDataPoints; i++)
			{
				LS_tmp2 += (dataArray_Y[i]-Fitfunktion(dataArray_X[i], parameter2))*(dataArray_Y[i]-Fitfunktion(dataArray_X[i], parameter2));
			}
			
			if(LS_tmp1 < LS_tmp2)
			{
				parameter2 = parameter1+ 0.5*(parameter2-parameter1);
				foundValue = false;
			}
			if(LS_tmp1 > LS_tmp2)
			{
				parameter1 = parameter2 - 0.5*(parameter2-parameter1);
				foundValue = false;
			}
			
			if(Math.abs(( parameter2 - 0.5*(parameter2-parameter1))) < tolerance)
				foundValue = true;
			
		}while(!foundValue);
		
		System.out.println("parameter: " + ( parameter2 - 0.5*(parameter2-parameter1)));
		
		return ( parameter2 - 0.5*(parameter2-parameter1));
	}
	
	private double Fitfunktion(double x, double par)
	{
		//2.0*pi*Radius*(A*par(1)/kappa.*(exp(-kappa.*(x-DNull))+exp(-kappa.*(x.+2.0*Radius-DNull))) + C.*((x.-DNull).^(-8.0) + (x.+2.0*Radius.-DNull).^(-8.0))) + 2.0*pi*(A.*par(1)*(kappa^(-2.0))*(exp(-kappa*(x.+2.0*Radius.-DNull))-exp(-kappa*(x.-DNull))) + (C/7.0)*((x.+2.0*Radius.-DNull).^(-7.0) - (x.-DNull).^(-7.0)))   
		return (2.0*Math.PI*Radius*(A*par/kappa*(Math.exp(-kappa*(x-DNull))+Math.exp(-kappa*(x+2.0*Radius-DNull)))) + 2.0*Math.PI*(A*par/(kappa*kappa)*(Math.exp(-kappa*(x+2.0*Radius-DNull))-Math.exp(-kappa*(x-DNull))) ) )  ;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new SimpleRegression();
	}

}
