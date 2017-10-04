package EvalToolProject_ice.tools;

public class Int_IntArrayList_Table {

		
		public Int_IntArrayList_Table()
		{
	          initialize(1);
	    }

	    public Int_IntArrayList_Table(int initialSize)
	    {
	          initialize(initialSize);
	    }

	    public int size() {
	        return values.length;//count;
	    }

	    public boolean isEmpty() {
	    	return (values.length == 0);//count == 0;
	    }

	            //ueberschriebene Version von mir
	    public void put(int key, int value) {
//	                if (count > highWaterMark) {
//	                    rehash();
//	                }
//	                // erste Zuordnung key->value
//	                int index = find(key);
//	                if (keyList[index] <= MAX_UNUSED) { // deleted or empty
//	                   keyList[index] = key;
//	                    ++count;
//	                }
	    			ensureCapacity(key);
	    	
	                values[key].add(value); // reset value
	                
//	                System.out.println();
//	                System.out.println("minC: " + key + "   count: " + count);
//        		    
//        		    for (int j = 0; j <  values.length; ++j) 
//        		    for (int i = 0; i <  values[j].size(); ++i) {
//		                   // keyList[i] = EMPTY;
//        		    	 	System.out.println("C: " + j +" val "+ values[j].get(i));
//		               }
	                
	   }

	          public IntArrayList get(int key) {
	               //return values[find(key)];
	        	  return values[key];
	           }

	          
	          private void ensureCapacity(int minCapacity) {
	        		
	        		int oldCapacity = values.length-1;
	        		if (minCapacity > oldCapacity) {
	        		    IntArrayList oldData[] = values;
	        		    int newCapacity = minCapacity + 1;//(oldCapacity * 3)/2 + 1;
	        	    	
	        		    values = new IntArrayList[newCapacity];
	        		    for (int i = 0; i < newCapacity; ++i) {
			                   // keyList[i] = EMPTY;
			                    values[i] = new IntArrayList();
			               }
	        		    
	        		    System.arraycopy(oldData, 0, values, 0, oldData.length);
	        		    
//	        		    System.out.println("minC: " + minCapacity + "   count: " + count);
//	        		    System.out.println();
//	        		    for (int j = 0; j <  values.length; ++j) 
//	        		    for (int i = 0; i <  values[j].size(); ++i) {
//			                   // keyList[i] = EMPTY;
//	        		    	 	System.out.println("C: " + j +" val "+ values[j].get(i));
//			               }
	        		    
	        		    ++count;
	        		}
	        	    }
	          /*  public void remove(int key) {
	               int index = find(key);
	               if (keyList[index] > MAX_UNUSED) { // neither deleted nor empty
	                   keyList[index] = DELETED; // set to deleted
	                   values[index] = null;//defaultValue; // set to default
	                   --count;
	                   if (count < lowWaterMark) {
	                       rehash();
	                    }
	                }
	           }*/

//	           public int getDefaultValue() {
//	               return defaultValue;
//	           }
//
//	           public void setDefaultValue(int newValue) {
//	               defaultValue = newValue;
//	               rehash();
//	           }

	           

	           
	           // =======================PRIVATES============================
	           private int defaultValue = 0;

	           // the tables have to have prime-number lengths. Rather than compute
	           // primes, we just keep a table, with the current index we are using.
//	           private int primeIndex;

	            // highWaterFactor determines the maximum number of elements before
	           // a rehash. Can be tuned for different performance/storage characteristics.
//	           private static final float HIGH_WATER_FACTOR = 0.4F;
//	           private int highWaterMark;

	           // lowWaterFactor determines the minimum number of elements before
	           // a rehash. Can be tuned for different performance/storage characteristics.
//	           private static final float LOW_WATER_FACTOR = 0.0F;
//	           private int lowWaterMark;

	           private int count;
	           
	          

	           // we use two arrays to minimize allocations
	           private IntArrayList[] values;
	           //private int[] keyList;

	            private static final int EMPTY = Integer.MIN_VALUE;
	           private static final int DELETED = EMPTY + 1;
	            private static final int MAX_UNUSED = DELETED;

	            private void initialize(int initialSize)
		           {
	            	   
		               values = new IntArrayList[initialSize];
		               //keyList = new int[initialSize];
		               for (int i = 0; i < initialSize; ++i) {
		                   // keyList[i] = EMPTY;
		                    values[i] = new IntArrayList();
		               }
		               count = 0;
//		                count = 0;
//		                lowWaterMark = (int) (initialSize * LOW_WATER_FACTOR);
//		                highWaterMark = (int) (initialSize * HIGH_WATER_FACTOR);
		           }
	            
	          /* private void initialize(int primeIndex)
	           {
	                if (primeIndex < 0)
	                {
	                   primeIndex = 0;
	                } 
	                else if (primeIndex >= PRIMES.length)
	                {
	                   System.out.println("TOO BIG");
	                   primeIndex = PRIMES.length - 1;
	                   // throw new java.util.IllegalArgumentError();
	                }
	                
	               this.primeIndex = primeIndex;
	               int initialSize = PRIMES[primeIndex];
	               values = new IntArrayList[initialSize];
	               keyList = new int[initialSize];
	               for (int i = 0; i < initialSize; ++i) {
	                    keyList[i] = EMPTY;
	                    values[i] = new IntArrayList();
	               }
	                count = 0;
	                lowWaterMark = (int) (initialSize * LOW_WATER_FACTOR);
	                highWaterMark = (int) (initialSize * HIGH_WATER_FACTOR);
	           }*/

	           /*private void rehash() {
	        	   IntArrayList[] oldValues = values;
	               int[] oldkeyList = keyList;
	               int newPrimeIndex = primeIndex;
	                if (count > highWaterMark) {
	                   ++newPrimeIndex;
	               } else if (count < lowWaterMark) {
	                   newPrimeIndex -= 2;
	              }
	               initialize(newPrimeIndex);
	               for (int i = oldValues.length - 1; i >= 0; --i) {
	                    int key = oldkeyList[i];
	                   if (key > MAX_UNUSED) {
	                       putInternal(key, oldValues[i]);
	                   }
	                }
	           }*/

	          /* public void putInternal(int key, IntArrayList value) {
	                int index = find(key);
	                if (keyList[index] < MAX_UNUSED) { // deleted or empty
	                   keyList[index] = key;
	                   ++count;
	               }
	                values[index] = value; // reset value
	           }

	           private int find(int key) {
	                if (key <= MAX_UNUSED)
	                    throw new IllegalArgumentException(
	                           "key can't be less than 0xFFFFFFFE");
	                int firstDeleted = -1; // assume invalid index
	               int index = (key ^ 0x4000000) % keyList.length;
	                if (index < 0)
	                    index = -index; // positive only
	               int jump = 0; // lazy evaluate
	                while (true) {
	                   int tableHash = keyList[index];
	                   if (tableHash == key) { // quick check
	                        return index;
	                    } else if (tableHash > MAX_UNUSED) { // neither correct nor unused
	                        // ignore
	                    } else if (tableHash == EMPTY) { // empty, end o' the line
	                        if (firstDeleted >= 0) {
	                            index = firstDeleted; // reset if had deleted slot
	                        }
	                       return index;
	                   } else if (firstDeleted < 0) { // remember first deleted
	                       firstDeleted = index;
	                   }
	                    if (jump == 0) { // lazy compute jump
	                        jump = (key % (keyList.length - 1));
	                        if (jump < 0)
	                           jump = -jump;
	                        ++jump;
	                    }

	                    index = (index + jump) % keyList.length;
	                    if (index == firstDeleted) {
	                        // We've searched all entries for the given key.
	                        return index;
	                    }
	                }
	           }*/

	         /*  private static int leastGreaterPrimeIndex(int source) {
	                int i;
	                for (i = 0; i < PRIMES.length; ++i) {
	                    if (source < PRIMES[i]) {
	                        break;
	                    }
	                }
	                return (i == 0) ? 0 : (i - 1);
	            }

	            // This list is the result of buildList below. Can be tuned for different
	            // performance/storage characteristics.
	            private static final int[] PRIMES = { 17, 37, 67, 131, 257, 521,
	                    1031, 2053, 4099, 8209, 16411, 32771, 65537, 131101,
	                    262147, 524309, 1048583, 2097169, 4194319, 8388617,
	                   16777259, 33554467, 67108879, 134217757, 268435459,
	                    536870923, 1073741827, 2147483647 };*/
}






