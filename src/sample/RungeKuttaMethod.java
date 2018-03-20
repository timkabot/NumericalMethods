package sample;

import javafx.scene.chart.XYChart;

import static sample.Constants.f;

/**
 * Created by Timkabor on 3/15/2018.
 */
public class RungeKuttaMethod {
    private double x0 = 1.0, y0 = 3.0 , h = 4;
    private int N = 50;
    private double x[] = new double[N];
    private double y[] = new double[N];
    public RungeKuttaMethod(double x0, double y0, double h) {
        this.x0 = x0;
        this.y0 = y0;
        this.h = h;
    }
    public RungeKuttaMethod(double x0, double y0, double h, int N) {
        this.x0 = x0;
        this.y0 = y0;
        this.h = h;
        this.N = N;
    }
    public RungeKuttaMethod() {
    }

    public XYChart.Series execute() {
        x[0] = x0;
        for (int i = 1; i < N; i++) {
            x[i] = x[i-1] + h;
        }
        y[0] = y0;
        double half_of_step = h/2;
        for (int i = 1; i < N; i++) {
            //System.out.println( y[i-1] + "  = " + 4/(x[i-1]*x[i-1]) + " - " + y[i-1]/x[i-1] + " - " + (y[i-1]*y[i-1]));
            double k1, k2, k3, k4;
            k1 = f(x[i-1],y[i-1]);
            k2 = f(x[i-1] + half_of_step,y[i-1] + half_of_step*k1);
            k3 = f(x[i-1] + half_of_step, y[i-1] + half_of_step * k2);
            k4 = f(x[i-1] + h, y[i-1] + h*k3);
            y[i] = y[i-1] + (h/6) * (k1 + 2*k2 + 2*k3 + k4);
            y[i] = Math.round(y[i] * 1000.0) / 1000.0;

            if (Double.valueOf(y[i]).isInfinite()) {
                N = i;
                break;
            }
        }
        XYChart.Series runge_kutta_method = new XYChart.Series();
        runge_kutta_method.setName("Runge Kutta method");


        for (int i=0; i<N; i++)
        {
            System.out.println(x[i] + " " + y[i]);
            runge_kutta_method.getData().add(new XYChart.Data(x[i], y[i]));
        }
        return runge_kutta_method;
    }
}
