/**
 * 
 * Time-stamp: <2019-01-10 16:31:16 rlc3>
 * 
 * ImageManipulation.java 
 *
 * Class allows the manipulation of an image by 
 * three alternative methods.
 *
 * @author Roy Crole
 * 
 */

import java.awt.image.*;
import java.awt.*;
import java.util.*; 

public class ImageManipulation {

// ----- template code commented out BEGIN     

    // linear transformation to compute prei from i
    // O D and P are points on a line with i between D and P
    static double linTrans (double O, double D, double i, double P) {
    	return D - (((i - D) / (P - D)) * (D - O));
    }

//----- template code commented out END

// ----- template code commented out BEGIN
    
   // take a cartesian coordinate (I,J) assumed to be in the positive octant OGV
   // and compute (preI, PreJ) returned as elements pre[0] pre[1] of pre 
    static int[] octlinTrans (int O, int D, int I, int J, int size) { 
  		   
        // we will compute preI and preJ and return them in pre 
    	int [] pre = new int[2];

    	// compute d from I and J 
    	double d = Math.sqrt((I * I) + (J * J));
    
    	// calculate P (from theta, itself from I and J) 
    	double theta, P;
    	theta = Math.atan(J / I);
    	if(I > J)
    	{
    		P = size / Math.cos(theta);
    	}
    	else
    	{
    		P = size / Math.sin(theta);
    	}
    		
    	// compute pred from O, D, d, p
    	double pred = linTrans(O, D, d, P);

	    pre[0] = pred * Math.cos(theta);
	    pre[1] = pred * Math.sin(theta);

	    return pre;

} // end octlinTrans

//----- template code commented out END

// ---- BEGIN linearBox 

    static public void linearBox(BufferedImage image, int n, int x, int y, int size) {
					 
    	BufferedImage temp = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
    	(temp.getGraphics()).drawImage(image, 0, 0, image.getWidth(), image.getHeight(),null);

// ----- template code commented out BEGIN     

    	// check if A(x,y) lies within image
    	if (x + size < image.getWidth() && x - size >= 0 && y + size < image.getHeight() && y - size >= 0) {
    		// loop visiting each pixel p at coordinate (i,j) in A(x,y) 
    		// set values for O, D, P
    		int O, D, P;
    		O = x - size;
    		D = x + n;
    		P = x + size;
	 
   			// ensure that D lies between O and P (as n varies)
   			// if n too large restrict D to P
   			if (D > P) D = P;
   			// spinner box allows n < 0; if so, reset n to 0
   			if(n < 0) D = x;

   			for (int i = x - size; i <= x + size; i++) {
   				for(int j = y - size; j <= y + size; j++) {// <---- start a for loop for j here 
   					// now apply IMGTRANS as per instructions 
   					// check IF i is between D and P
   					if(i >= D && i <= P)
   					{
   						// if so compute prei
   						int prei = (int) linTrans(O, D, i, P);
   	   					// and hence get the prep pixel RGB and set the p pixel RGB
   						image.setRGB(i, j, temp.getRGB(prei, j));
   					}
   					// IF not make pixel (i,j) grey with 0xaaaaaa
   					else
   					{
   						image.setRGB(i, j, 0xaaaaaa);
   					}

   				}// <--- end forLoop j here 
   			} // end forLoop i
   		} // end check that A(x,y) is in image 

//----- template code commented out END

   } // end method linearBox

// ---- END linearBox

// ---- BEGIN linearOct

    static public void linearOct(BufferedImage image, int n, int x, int y, int size) {
					 
    	BufferedImage temp=new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        (temp.getGraphics()).drawImage(image, 0, 0, image.getWidth(), image.getHeight(),null);

// ----- template code commented out BEGIN     

        // check if A(x,y) lies within image
        if (x + size < image.getWidth() && x - size >= 0 && y + size < image.getHeight() && y - size >= 0) {
        	// loop visiting each pixel in A(x,y) at image coordinate (i,j) 
        	for (int i = x - size; i <= x + size; i++) {
   				for(int j = y - size; j <= y + size; j++) {
        			// Apply IMGTRANS to each line ODP specified by an (i,j): 
		     
        			// a list to store preI and preJ as element 0 and 1 
        			// pre is calculated below using octlinTrans
        			int [] pre = new int[2];
        			int preI; 
        			int preJ;

        			// convert image coordinates (i,j) to cartesian coordinates 
        			//  .... for example mouse position (x,y) is converted to (x,-y)
       	    	    // then move the mouse position to the origin (0,0) and 
        			// ... translate all other positions relatively ...
        			// so that you now work with A(0,0) in cartesian coordinates 
        			// I and J below are relative cartesian coordinates 
        			// note: Cart Coord -j moves up (ie - ) by an amount -y  
                    int I = i - x;
                    int J = -j + y;

                    // set d = distance of origin to (I,J)
                    int d = Math.sqrt((I * I) + (J * J));

		    // if (I,J) is outside the circle of radius size/3
		    // then we compute (preI, preJ) from (I,J) using octlinTrans
		    int radius = size / 3;
		    if (d > radius) { // radius test 
		     // perform linear transformation in octant OGV 
		     // 0 < J < I
		    	if (0 < J < I) {
		    		pre = octlinTrans(n, d, I, J, size); // use octlinTrans
		    		preI = pre[0]; preJ = pre[1];
         	    }
		     // perform linear transformation in octant OVH 
		     // 0 < -J < I 
		    	else if (0 < -J < I)
		    	{
		    		//rotate octant 90 degrees counter clockwise to be in position x > 0, y > 0
		    		tempI = I;
		    		I = -J;
		    		J = tempI;
		    		
		    		pre = octlinTrans(n, d, I, J, size);
		    		preI = pre[0]; preJ = pre[1];
		    		
		    		//rotate octant 90 degrees clockwise to return to actual position
		    		tempI = I;
		    		I = J;
		    		J = -tempI;
		    	}
		     // perform linear transformation in octant OKU 
		    	else if()
		     // perform linear transformation in octant OUF
		     ?? 
		     // identity transformation elsewhere (outside the circle) 
		     else { 
			 preI = I; ?? } // end nested if statements

       	    	    // transform relative cartesian coordinate (preI,preJ) 
		    // back to image coordinate (prei,prej) 
		    ??

		    // set RGB of pixel at (i,j) to RGB from (prei,prej)
		    ??

		    } // matches radius test 
		    else { 
		    	image.setRGB(i, j, 0xaaaaaa);
		    } // end if  

	} // end forLoop j
       	} // end forLoop i
    } // end check that A(x,y) is in image 

//----- template code commented out END

 } // end method linearOct 

// ---- END linearOct

// ---- BEGIN phaseShift 

    static public void phaseShift(BufferedImage image, int n, int x, int y, int size) {

	// creates a copy of the image called temp
        BufferedImage temp=new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        (temp.getGraphics()).drawImage(image,0,0,image.getWidth(), image.getHeight(),null );

/* ----- template code commented out BEGIN 

	// loop through each pixel (i,j) in "A(x,y) intersect (image - boundary)"
	for (int i = ??; i<= Math.min(image.getWidth()-1-2*n, ??); ++i) {

  // *****************************************************************************
  // ?? the rest of your code goes here: a loop for j, plus remainder of your code
  // *****************************************************************************

	} // end loop i 

   ----- template code commented out END */

 } // end method phaseShift 

// ---- END phaseShift 

} // ImageManipulation
