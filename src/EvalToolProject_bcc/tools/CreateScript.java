package EvalToolProject_bcc.tools;

import java.text.DecimalFormat;


public class CreateScript {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		BFMFileSaver rg = new BFMFileSaver();
		rg.DateiAnlegen("/home/users/dockhorn/Marco_BFM_Sources/Bfm_2010.08.18/scripts/dummyscript.txt", false);
		
		DecimalFormat dh = new DecimalFormat("0000");
		
		for(int i = 1251; i <= 2500; i++)
			rg.setzeZeile("./bfm_20100818_p_0_001 /home/users/dockhorn/Messung/LoopCreation20100802/p_0_001/Linear1000__"+dh.format(i)+".bfm");
		
		rg.DateiSchliessen();
	}

}
