package sample;

/**
 * Created by Timkabor on 3/15/2018.
 */
public  class Constants {
    static double f(double x, double y) {
        return 4/(x*x) - (y/x) - (y *y);
    }
    static double getC(double x0,double y0) {
        return ( (Math.pow(x0,-5)) / (y0 - (2/x0)) + Math.pow(x0,-4)/4 );
    }
    static int get_max_h() {
        return 50;
    }
}
