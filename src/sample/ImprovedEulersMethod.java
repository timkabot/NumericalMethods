package sample;

import javafx.scene.chart.XYChart;

import static sample.Constants.f;

/**
 * Created by Timkabor on 3/15/2018.
 */
public class ImprovedEulersMethod {
    private double x0 = 1.0, y0 = 3.0 , h = 4;
    private int Max_X = Constants.get_max_h();
    private double x[] = new double[Max_X],
                   y[] = new double[Max_X];
    private int N;
    public ImprovedEulersMethod(double x0, double y0, int N) {
        this.x0 = x0;
        this.y0 = y0;
        this.h = Max_X * 1.0 /N;
        this.N = N;
    }
    public ImprovedEulersMethod() {
    }

    public XYChart.Series execute() {
        x[0] = x0;
        for (int i = 1; i < Max_X; i++) {
            x[i] = x[i-1] + h;
        }
        y[0] = y0;
        double half_of_step = h/2;
        for (int i = 1; i < Max_X; i++) {
            //System.out.println( y[i-1] + "  = " + 4/(x[i-1]*x[i-1]) + " - " + y[i-1]/x[i-1] + " - " + (y[i-1]*y[i-1]));
            y[i] = y[i - 1] + half_of_step * (f(x[i-1],y[i-1]) + f(x[i-1] + h, y[i-1] + h* f(x[i-1],y[i-1]) ));
            y[i] = Math.round(y[i] * 1000.0) / 1000.0;

            if (Double.valueOf(y[i]).isInfinite()) {
                Max_X = i;
                break;
            }
        }
        XYChart.Series improved_eulers_method = new XYChart.Series();
        improved_eulers_method.setName("Improved Eulers method");

        for (int i = 0; i< Max_X; i++)
            improved_eulers_method.getData().add(new XYChart.Data(x[i], y[i]));

        return improved_eulers_method;
    }
    public XYChart.Series executeError(ExactSolution exactSolution) {
        for (int i = 0; i < Max_X; i++) {
            y[i] = Math.abs(y[i] - exactSolution.getY(i));
            y[i] = Math.round(y[i] * 1000.0) / 1000.0;
            if (Double.valueOf(y[i]).isInfinite()) {
                Max_X = i;
                break;
            }
        }
        XYChart.Series improved_eulers_method_error = new XYChart.Series();
        improved_eulers_method_error.setName("Improved Eulers method Error");

        for (int i = 0; i< Max_X; i++)
            improved_eulers_method_error.getData().add(new XYChart.Data(x[i], y[i]));

        return improved_eulers_method_error;
    }
    public double find_max_error(ExactSolution exactSolution, int N) {
        y[0] = y0; x[0] = x0;
        h = Max_X * 1.0 /N;
        double half_of_step = h/2,
               max_error = Math.abs(y[0] - exactSolution.getY(0));
        for (int i = 1; i < Max_X; i++)
            x[i] = x[i-1] + h;

        for (int i = 1; i < Max_X; i++) {
            y[i] = y[i - 1] + half_of_step * (f(x[i-1],y[i-1]) + f(x[i-1] + h, y[i-1] + h* f(x[i-1],y[i-1]) ));
            y[i] = Math.abs(y[i] - exactSolution.getY(i));
            y[i] = Math.round(y[i] * 10000.0) / 10000.0;
            max_error = Math.max(max_error,y[i]);
            if (Double.valueOf(y[i]).isInfinite())
                break;

        }
        return max_error;
    }
}
