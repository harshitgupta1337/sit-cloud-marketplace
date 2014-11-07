package test;
//import migrdecider.*;
import migrdecidernew.migrdecidernew;
import com.mathworks.toolbox.javabuilder.*;

class migrationcheck
{
	public static void main(String[] args) throws MWException
	{
		//migrdecider theMigration = null;
		migrdecidernew theMigration = null;
		//theMigration = new migrdecider();
		theMigration = new migrdecidernew();
		
		Object[] y = null;
		
		double[][] AData = {{0.9,0.9},{0.8,0.6},{0.3,0.5}};
		//The above values are G_avail and G_bw for each VM in the range between 0-1
		MWNumericArray x = new MWNumericArray(AData, MWClassID.DOUBLE);
		//Object[][] y = new Object(x);
		//y=theMigration.migration(1,x);
		y=theMigration.migration_shrink(1,x);
		System.out.println("The output is  = \n" + y[0]);
		
		String[] parts = y[0].toString().split("\n");
	    float[] numbers = new float[parts.length];
	    for (int i = 0; i < parts.length; ++i) {
	        float number = Float.parseFloat(parts[i]);
	        //float rounded = (int) Math.round(number * 1000) / 1000f;
	        numbers[i] = number;
	        System.out.format("The float value is = %f ",numbers[i]);
	    }
		
	}
}