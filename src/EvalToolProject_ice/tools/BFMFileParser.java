package EvalToolProject_ice.tools;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;



public class BFMFileParser {

    /** Eine Liste aller Fuktionen die benutzt werden. */
    private Map functions = new HashMap();
 
    BFMImportData importD;
    
    int value = 0;
    double doublevalue = 0.0;
   // long mcs = 0;
    String TokenString = "";
	
	BFMFileParser(BFMImportData im)
	{
		this.importD =im;
		
		
		
		addFunction( "#", new Function(){
			public int MacheWas(String kommentar)
			{
				//(Integer.parseInt((String) tokens.get( 1 ))
				System.out.println(kommentar);
				return 0;
			}
        });
		
		addFunction( "!number_of_monomers=", new Function(){
			public int MacheWas(String zah)
			{   int zahl = Integer.parseInt(zah);
				System.out.println("nr="+ zahl);
				value = zahl;
				return 1;
			}
        });
		
		/*addFunction( "!max_connectivity=", new Function(){
			public int MacheWas(String zah)
			{   int zahl = Integer.parseInt(zah);
				System.out.println("connectivity="+ zahl);
				value = zahl;
				return 2;
			}
        });*/
		
		addFunction( "#!system_jbfm=", new Function(){
			public int MacheWas(String zah)
			{   int zahl = Integer.parseInt(zah);
				System.out.println("system_jbfm"+ zahl);
				value = zahl;
				return 2;
			}
        });
		
		addFunction( "!box_x=", new Function(){
			public int MacheWas(String zah)
			{   int zahl = Integer.parseInt(zah);
				System.out.println("box_x="+ zahl);
				value = zahl;
				return 3;
			}
        });
		
		addFunction( "!box_y=", new Function(){
			public int MacheWas(String zah)
			{   int zahl = Integer.parseInt(zah);
				System.out.println("box_y="+ zahl);
				value = zahl;
				return 4;
			}
        });
		
		addFunction( "!box_z=", new Function(){
			public int MacheWas(String zah)
			{   int zahl = Integer.parseInt(zah);
				System.out.println("box_z="+ zahl);
				value = zahl;
				return 5;
			}
        });
		
		addFunction( "!periodic_x=", new Function(){
			public int MacheWas(String zah)
			{   int zahl = Integer.parseInt(zah);
				System.out.println("perio_x="+ zahl);
				value = zahl;
				return 6;
			}
        }); 
		
		addFunction( "!periodic_y=", new Function(){
			public int MacheWas(String zah)
			{   int zahl = Integer.parseInt(zah);
				System.out.println("perio_y="+ zahl);
				value = zahl;
				return 7;
			}
        }); 
		
		addFunction( "!periodic_z=", new Function(){
			public int MacheWas(String zah)
			{   int zahl = Integer.parseInt(zah);
				System.out.println("perio_z="+ zahl);
				value = zahl;
				return 8;
			}
        }); 
		
		addFunction( "!set_of_bondvectors", new Function(){
			public int MacheWas(String zah)
			{   
				System.out.println("bondvectors");
				
				return 9;
			}
        });
		
		addFunction( "!add_bonds", new Function(){
			public int MacheWas(String zah)
			{   
				//System.out.println("add bonds:");
				
				return 10;
			}
        });
		
		addFunction( "!remove_bonds", new Function(){
			public int MacheWas(String zah)
			{   
				//System.out.println("remove_bonds");
				
				return 11;
			}
        });
		
		addFunction( "!attributes", new Function(){
			public int MacheWas(String zah)
			{   
				System.out.println("attributes");
				
				return 12;
			}
        });
		
		addFunction( "!bonds", new Function(){
			public int MacheWas(String zah)
			{   
				System.out.println("more bonds");
				
				return 13;
			}
        });
		
		addFunction( "!mcs=", new Function(){
			public int MacheWas(String zah)
			{  // mcs = Long.parseLong(zah);
				//System.out.println("mcs=" +mcs);
			
				//System.out.println("mcs=" +zah);
				TokenString = zah;
			
				return 14;
			}
        });
		
		addFunction( "#!electric_field_on=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("efeld an="+ zahl);
				value = zahl;
				
				return 15;
			}
        });
		
		
		addFunction( "#!electric_force=", new Function(){
			public int MacheWas(String zah)
			{   
				double zahl = Double.parseDouble(zah);
				System.out.println("ekraft="+ zahl);
				doublevalue = zahl;
				
				return 16;
			}
        });
		
		addFunction( "#!efield_x=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("efeld_x="+ zahl);
				value = zahl;
				
				return 17;
			}
        });
		
		addFunction( "#!efield_y=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("efeld_y="+ zahl);
				value = zahl;
				
				return 18;
			}
        });
		
		addFunction( "#!efield_z=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("efeld_z="+ zahl);
				value = zahl;
				return 19;
			}
        });
		
		addFunction( "#!absorption_on=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("ab an="+ zahl);
				value = zahl;
				
				return 20;
			}
        });
		
		addFunction( "#!absorption_energy=", new Function(){
			public int MacheWas(String zah)
			{   
				double zahl = Double.parseDouble(zah);
				doublevalue = zahl;
				System.out.println("ab energie="+ zahl);
				
				return 21;
			}
        });
		
		addFunction( "#!absorption_x=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("ab_x="+ zahl);
				value = zahl;
				
				return 22;
			}
        });
		
		addFunction( "#!absorption_y=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("ab_y="+ zahl);
				value = zahl;
				
				return 23;
			}
        });
		
		addFunction( "#!absorption_z=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("ab_z="+ zahl);
				value = zahl;
				return 24;
			}
        });
		
		
		
		addFunction( "#!monomers_fixed=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("fixed="+ zahl);
				value = zahl;
				return 25;
			}
	   });
		
		addFunction( "#!excluded_volume=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("excluded="+ zahl);
				value = zahl;
				return 26;
			}
	   });
				     
		addFunction( "#!NrOfStars=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("NrOfStars="+ zahl);
				value = zahl;
				return 27;
			}
	   });
					 
		addFunction( "#!NrOfMonomersPerStarArm=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("NrOfMonomersPerStarArm="+ zahl);
				value = zahl;
				return 28;
			}
	   });
		
		addFunction( "#!NrOfHeparin=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("NrOfHeparin="+ zahl);
				value = zahl;
				return 29;
			}
	   });
		
		addFunction( "#!NrOfCrosslinker=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("NrOfCrosslinker="+ zahl);
				value = zahl;
				return 30;
			}
	   });
		
		addFunction( "#!CrosslinkerFunctionality=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("CrosslinkerFunctionality="+ zahl);
				value = zahl;
				return 31;
			}
	   });
		
		addFunction( "#!NrOfChargesPerHeparin=", new Function(){
			public int MacheWas(String zah)
			{   
				int zahl = Integer.parseInt(zah);
				System.out.println("NrOfChargesPerHeparin="+ zahl);
				value = zahl;
				return 32;
			}
	   });
		
		addFunction( "#!ReducedTemperature=", new Function(){
			public int MacheWas(String zah)
			{   
				//System.out.println("ReducedTemperature="+ zah);
				//double zahl = 0.0;
				if(zah.compareToIgnoreCase("Inf")==0)
					doublevalue=Double.POSITIVE_INFINITY;
				
				else doublevalue = Double.parseDouble(zah);
				
				
				System.out.println("ReducedTemperature="+ doublevalue);
				
				return 33;
			}
	   });
		
		addFunction( "#!virtual_spring_constant=", new Function(){
			public int MacheWas(String zah)
			{   
				//System.out.println("ReducedTemperature="+ zah);
				//double zahl = 0.0;
				if(zah.compareToIgnoreCase("Inf")==0)
					doublevalue=Double.POSITIVE_INFINITY;
				
				else doublevalue = Double.parseDouble(zah);
				
				
				System.out.println("virtual_spring_constant="+ doublevalue);
				
				return 34;
			}
	   });
		
		addFunction( "#!virtual_spring_length=", new Function(){
			public int MacheWas(String zah)
			{   
				//System.out.println("ReducedTemperature="+ zah);
				//double zahl = 0.0;
				if(zah.compareToIgnoreCase("Inf")==0)
					doublevalue=Double.POSITIVE_INFINITY;
				
				else doublevalue = Double.parseDouble(zah);
				
				
				System.out.println("virtual_spring_length="+ doublevalue);
				
				return 35;
			}
	   });

		
			addFunction( "#!NrOfMonoPerChain=", new Function(){
				public int MacheWas(String zah)
				{   
					int zahl = Integer.parseInt(zah);
					System.out.println("NrOfMonoPerChain="+ zahl);
					value = zahl;
					return 101;
					
				}
		   });
		
		
	}
	
	
	
    public void addFunction( String name, Function function ){
        functions.put( name, function );
    }
    
    
    /**
     * Parst eine mathematische Formel.
     * @param formula Die Formel
     * @return Der Wert dieser Formel
     * @throws IllegalArgumentException Sollte der übergebene String gar keine Formel sein.
     */
    public int parse( String formula ){
        /*
         * Es ist unpraktisch die Formel als String zu behandeln, deshalb
         * wird sie zuerst in sog. Tokens aufgeteilt. Jedes Token ist ein Object
         * welches irgendetwas darstellt, z.B. ein Double, OPEN, CLOSE, ...
         */
    	if (formula == null)
    		return -1;
    	
        List tokens = tokenize( formula );

        int size = tokens.size();
        
        if( size > 1 )
        {
        	Function function = (Function)functions.get((String) tokens.get( 0 ) );
            if( function != null ){
            	
            	String TokenString="";
            	for(int i = 1; i < tokens.size();i++)
            		TokenString+=(String) tokens.get( i );
            	
            	//System.out.println("tok>:"+TokenString);
            	return function.MacheWas(TokenString);
            }
               
        }
        if( size == 1 )
        {
        	Function function = (Function)functions.get((String) tokens.get( 0 ) );
            if( function != null ){
            	//System.out.println("hier");
            	return function.MacheWas("");
            }
               
        }
        
        
      
        return 0;
    } 
    
    
    
    
    /**
     * Wandelt die Formel welche als Text vorliegt in eine Liste von
     * Strings, Doubles, {@link #OPEN}, {@link #CLOSE} und {@link #SEPARATOR}
     * um.
     * @param formula Die Formel
     * @return Die "Tokens" der Formel.
     */
    private List tokenize( String formula ){
        // Index des Chars, der zurzeit betrachtet wird.
        int offset = 0;
        int length = formula.length();
        List parts = new ArrayList();

        // Strings die eine Bedeutung haben
        Set texts = new HashSet();
        texts.addAll( functions.keySet() );
       // texts.addAll( operations.keySet() ); 

        while( offset < length ){
            char current = formula.charAt( offset );
          // System.out.println("of "+(int) current+ "  : " +  current) ;

            // Tabulator, Leerzeichen etc. interessieren nicht
            if( !Character.isWhitespace( current )){
            	
            	if(( current == '#' ) && (formula.charAt( (offset +1) ) != '!'))  {
            		
            		
            			parts.add( "#" );
                        parts.add( formula);
                        offset = length;
            		
                }
            	
            	else if(  current == '-'  ){
                	//System.out.println("ca "+current);
                    // Es folgt nun eine Zahl, welche ausgelesen werden muss
                    int end = offset+1;
                    boolean pointSeen = current == '.';

                    while( end < length ){
                        char next = formula.charAt( end );
                        if( Character.isDigit( next ))
                            end++;
                        else if( next == '.' && !pointSeen ){
                            pointSeen = true;
                            end++;
                        }
                        else
                            break;
                    }

                    parts.add(  formula.substring( offset, end )  );
                    offset = end;
                }
            	else if( current == ':'  ){
                	//System.out.println("ca "+current);
                    // Es folgt nun eine Zahl, welche ausgelesen werden muss
            		parts.add( formula.substring( offset, length)  );
                    //parts.add( formula);
                    offset = length;
            		
                   
                    

                   
                }
                
            	else if( Character.isDigit( current )  ){
                	//System.out.println("ca "+current);
                    // Es folgt nun eine Zahl, welche ausgelesen werden muss
                    int end = offset+1;
                    boolean pointSeen = current == '.';

                    while( end < length ){
                        char next = formula.charAt( end );
                        if( Character.isDigit( next ))
                            end++;
                        else if( next == '.' && !pointSeen ){
                            pointSeen = true;
                            end++;
                        }
                        else
                            break;
                    }

                    parts.add(  formula.substring( offset, end )  );
                    offset = end;
                }
                else
                {
                    /*
                     * Ein Platzhalter für eine Operation, Funktion oder Konstante
                     * wird gelesen.
                     */
                    int bestLength = 0;
                    String best = null;
                    String check;

                    for( Iterator it = texts.iterator(); it.hasNext(); ){
                    	check = (String) it.next();
                        if( formula.startsWith( check, offset )){
                            if( check.length() > bestLength ){
                                bestLength = check.length();
                                best = check;
                            }
                        }
                    }

                    if( best == null )
                    {
                    	System.err.println("Attention: Unknown command!");
                    	best=formula.substring(offset);
                    	bestLength = best.length();
                    }
                        //throw new IllegalArgumentException( "An dieser Formel stimmt was nicht" );

                    offset += bestLength;
                    parts.add( best );
                }
            }
            else
                offset++;
        }
        
        String TokenString="";
    	for(int i = 1; i < parts.size();i++)
    		TokenString+=(String) parts.get( i );
    	
    	//System.out.println("tok>:"+TokenString);
        
        return parts;
    } 
}
