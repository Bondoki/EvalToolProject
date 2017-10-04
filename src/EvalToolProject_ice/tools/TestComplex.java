package EvalToolProject_ice.tools;


public class TestComplex
{
	public static void main(String[] args)
	{
		Complex complex1, complex2, complex3, complex4;
		long start, stop;
		
		complex1 = new Complex(3, 4);
		complex1.printComplexNumber(" Anlegen der 1. komplexen Zahl\n");

		complex2 = new Complex(5, 6);
		complex2.printComplexNumber("\n Anlegen der 2. komplexen Zahl\n");
		
		complex3 = complex1.sqrt(2);
		complex3.printComplexNumber("\n");
		
		for(int i=0; i<=complex3.getSqrtArgArray().length-1; i++)
			System.out.println(complex3.getSqrtArgArray()[i]);
		
		complex4 = new Complex(-complex3.getReal(), -complex3.getImag());
		complex4.printComplexNumber("\n");
		
//		complex3 = complex1.add(complex2);
//		complex3.printComplexNumber("\n Addition der 1. mit der 2. komplexen Zahl\n");
//
//		complex3 = complex1.sub(complex2);
//		complex3.printComplexNumber("\n Subtraktion der 1. mit der 2. komplexen Zahl\n");
//
//		complex3 = complex1.mul(complex2);
//		complex3.printComplexNumber("\n Multiplikation der 1. mit der 2. komplexen Zahl\n");
//
//		complex3 = complex1.div(complex2);
//		complex3.printComplexNumber("\n Division der 1. mit der 2. komplexen Zahl\n");
//
//		complex3 = complex1.pow(2);
//		complex3.printComplexNumber("\n Quadratur der 1. komplexen Zahl\n");
//
//		complex3 = complex1.sqrt(3);
//		complex3.printComplexNumber("\n 3. Wurzel aus der 2. komplexen Zahl\n" +
//									" Zus�tzlich wird ein double-Array mit allen drei m�glichen Winkeln erzeugt\n");
//		
//		complex3 = complex1.sin();
//		complex3.printComplexNumber("\n Sinus der 1. komplexen Zahl\n");
//
//		complex3 = complex1.cos();
//		complex3.printComplexNumber("\n Kosinus der 1. komplexen Zahl\n");
//
//		complex3 = complex1.tan();
//		complex3.printComplexNumber("\n Tangens der 1. komplexen Zahl\n");
//
//		complex3 = complex1.cot();
//		complex3.printComplexNumber("\n Kotangens der 1. komplexen Zahl\n");
//
//		complex3 = complex1.sinh();
//		complex3.printComplexNumber("\n Sinus-Hyperbolikus der 1. komplexen Zahl\n");
//
//		complex3 = complex1.cosh();
//		complex3.printComplexNumber("\n Kosinus-Hyperbolikus der 1. komplexen Zahl\n");
//
//		complex3 = complex1.tanh();
//		complex3.printComplexNumber("\n Tangens-Hyperbolikus der 1. komplexen Zahl\n");
//
//		complex3 = complex1.coth();
//		complex3.printComplexNumber("\n Kotangens-Hyperbolikus der 1. komplexen Zahl\n");
//
//		complex3 = complex1.exp();
//		complex3.printComplexNumber("\n Exponent e^z der 1. komplexen Zahl\n");
//
//		complex3 = complex1.ln();
//		complex3.printComplexNumber("\n Logarythmus-Naturalis der 1. komplexen Zahl\n");
//
//		complex3 = complex1.log();
//		complex3.printComplexNumber("\n 10er Logarithmus der 1. komplexen Zahl\n");
//
//		complex3 = complex1.conj();
//		complex3.printComplexNumber("\n konjugierte komplexe Zahl\n");
//
//		complex3 = new Complex();
//		System.out.println("\n 1.000.000 Additionen.\n Start!");
//		start = System.currentTimeMillis();
//		for(int i = 0; i<=1000000; i++) {
//			complex3 = complex1.add(complex3);
//		}
//		stop = System.currentTimeMillis();
//		System.out.println(" Ende!");
//		System.out.println(" Zeit: "+ (stop - start)+" ms");
//		
//		complex3.printComplexNumber();

	}
}

