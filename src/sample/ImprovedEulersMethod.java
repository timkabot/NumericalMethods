package sample;

import javafx.scene.chart.XYChart;

import static sample.Constants.f;

/**
 * Created by Timkabor on 3/15/2018.
 */
public class ImprovedEulersMethod {
    double x0 = 1.0, y0 = 3.0 , h = 4;
    int N = 50;
    double x[] = new double[N];
    double y[] = new double[N];
    public ImprovedEulersMethod(double x0, double y0, double h) {
        this.x0 = x0;
        this.y0 = y0;
        this.h = h;
    }
    public ImprovedEulersMethod() {
    }
    private XYChart.Series improved_eulers_method;
    public XYChart.Series execute() {
        x[0] = x0;
        for (int i = 1; i < N; i++) {
            x[i] = x[i-1] + h;
        }
        y[0] = y0;
        double half_of_step = h/2;
        for (int i = 1; i < N; i++) {
            //System.out.println( y[i-1] + "  = " + 4/(x[i-1]*x[i-1]) + " - " + y[i-1]/x[i-1] + " - " + (y[i-1]*y[i-1]));
            y[i] = y[i - 1] + half_of_step * (f(x[i-1],y[i-1]) + f(x[i-1] + h, y[i-1] + h* f(x[i-1],y[i-1]) ));
            y[i] = Math.round(y[i] * 1000.0) / 1000.0;

            if (Double.valueOf(y[i]).isInfinite()) {
                N = i;
                break;
            }
        }
        improved_eulers_method = new XYChart.Series();
        improved_eulers_method.setName("Improved Eulers method");


        for (int i=0; i<N; i++)
        {
            System.out.println(x[i] + " " + y[i]);
            improved_eulers_method.getData().add(new XYChart.Data(x[i], y[i]));
        }
        return improved_eulers_method;
    }
}
