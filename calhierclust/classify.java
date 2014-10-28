import calhierclust.*;

import com.mathworks.toolbox.javabuilder.*;

class classify
{
	public static void main(String[] args) throws MWException
	{
		calclust thecalclust=new calclust();
		Object[] y = null;
		
		//Provider pool
		//First provider is the current provider
		//First provider values are promised_avail,promised_bw,promised_cost,current_trust_avail,current_trust_bw
		//Rest providers are prospective new providers
		//Rest providers values are offered_avail,offered_bw,offered_cost,current_trust_avail,current_trust_bw
		double[][] AData = {{81.94,88,100,1.2,1.1},
							{95.02,56,100,1.6,1.6},
							{91.22,46,100,1.6,1.6},
							{91.79,49,100,1.6,1.6},
							{88.04,47,100,1.6,1.6},
							{95.78,39,101,1.6,1.6},
							{96.59,39,101,1.6,1.6},
							{98.52,26,101,1.6,1.6},
							{82.4,90,102,1.6,1.6},
							{84.57,38,102,1.6,1.6},
							{96.87,10,102,1.6,1.6},
							{97.61,74,102,1.6,1.6},
							{82.94,61,102,1.6,1.6},
							{80.88,21,103,1.6,1.6},
							{81.88,19,103,1.6,1.6},
							{88.9,48,103,1.6,1.6},
							{90.77,12,103,1.6,1.6},
							{80.56,83,103,1.1,0.9},
							{94.18,44,104,1.6,1.6},
							{90.74,15,105,1.6,1.6},
							{98.37,100,105,1.6,1.6},
							{98.12,31,105,1.6,1.6},
							{98.94,100,105,1.6,1.6},
							{90.72,81,105,1.6,1.6},
							{84.04,96,105,1.6,1.6},
							{98.94,100,105,1.6,1.6},
							{88.47,50,106,1.6,1.6}};
		//double index=2.0;
		MWNumericArray x = new MWNumericArray(AData, MWClassID.DOUBLE);
		//MWNumericArray idx = new MWNumericArray(index, MWClassID.DOUBLE);
		//y=thecalclust.calhierclust(1,x,idx);
		y=thecalclust.calhierclust(1,x);
		System.out.println("The output is  = \n" + y[0].toString());
		int number = Integer.parseInt(y[0].toString());
		System.out.format("The new provider selected is = %d ",number);
		// If the output is 1, that means there is no provider found suitable.
		// otherwise the output is index of the provider from above provider pool
		
	}
}