package org.iolani.util;

/**
 * @author wkd
 */
public class Utility {
    
	public static int window(int input, int min, int max) {
        if(input < min) return min;
        if(input > max) return max;
        return input;
    }
	
    public static double window(double input, double min, double max) {
        if(input < min) return min;
        if(input > max) return max;
        return input;
    }
    
    public static double window(double input, double max) {
        return Utility.window(input, -max, max);
    }
    
    public static double sign(double input) {
        if(input == 0.0) return 0.0;
        return (input > 0.0) ? 1.0 : -1.0; 
    }
    
    public static double limit(double num) {
        if (num > 1.0) {
            return 1.0;
        }
        if (num < -1.0) {
            return -1.0;
        }
        return num;
    }
}