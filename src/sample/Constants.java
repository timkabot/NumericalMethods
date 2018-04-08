package sample;

/**
 * Created by Timkabor on 3/15/2018.
 */
class Constants {
    private static int max_X = 4;
    static double f(double x, double y) {
        return (4/(x*x)) - (y/x) - (y *y);
        //return x*x*x - Math.exp(-2*x) - 2*y;
    }
    static double getC(double x0,double y0) {
        return (2 + (x0*y0) ) / ( (Math.pow(x0,5) * y0) - (2 * Math.pow(x0,4)) );
        //return ( (Math.pow(x0,-5)) / (y0 - (2/x0)) + Math.pow(x0,-4)/4 );
    }
    static void setMax_X(int x) {
        max_X = x;
    }

    static int getMax_X() {
        return max_X;
    }
}
