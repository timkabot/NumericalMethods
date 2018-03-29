package sample;

import javafx.scene.chart.XYChart;

import static sample.Constants.f;

/**
 * Created by Timkabor on 3/15/2018.
 */
public class RungeKuttaMethod {
    private double x0 = 1.0, y0 = 3.0 , h = 4;
    private int Max_X = Constants.get_max_h();
    private double x[] = new double[Max_X];
    private double y[] = new double[Max_X];
    private int N;
    public RungeKuttaMethod(double x0, double y0, int N) {
        this.x0 = x0;
        this.y0 = y0;
        this.h = Max_X * 1.0 /N;
        this.N = N;
    }

    public RungeKuttaMethod() {
    }

    public XYChart.Series execute() {
        x[0] = x0;
        for (int i = 1; i < Max_X; i++) {
            x[i] = x[i-1] + h;
        }
        y[0] = y0;
        double half_of_step = h/2;
        double k1, k2, k3, k4;
        for (int i = 1; i < Max_X; i++) {
            k1 = f(x[i-1],y[i-1]);
            k2 = f(x[i-1] + half_of_step,y[i-1] + half_of_step*k1);
            k3 = f(x[i-1] + half_of_step, y[i-1] + half_of_step * k2);
            k4 = f(x[i-1] + h, y[i-1] + h*k3);
            y[i] = y[i-1] + (h/6) * (k1 + 2*k2 + 2*k3 + k4);
            y[i] = Math.round(y[i] * 10000.0) / 10000.0;

            if (Double.valueOf(y[i]).isInfinite()) {
                Max_X = i;
                break;
            }
        }
        XYChart.Series runge_kutta_method = new XYChart.Series();
        runge_kutta_method.setName("Runge Kutta method");

        for (int i = 0; i< Max_X; i++)
            runge_kutta_method.getData().add(new XYChart.Data(x[i], y[i]));

        return runge_kutta_method;
    }
    public XYChart.Series executeError(ExactSolution exactSolution) {
        for (int i = 0; i < Max_X; i++) {
            y[i] = Math.abs(y[i] - exactSolution.getY(i));
            y[i] = Math.round(y[i] * 10000.0) / 10000.0;
            if (Double.valueOf(y[i]).isInfinite()) {
                Max_X = i;
                break;
            }
        }
        XYChart.Series runge_kutta_method_error = new XYChart.Series();
        runge_kutta_method_error.setName("Runge Kutta method Error");

        for (int i = 0; i< Max_X; i++)
            runge_kutta_method_error.getData().add(new XYChart.Data(x[i], y[i]));

        return runge_kutta_method_error;
    }
    public double find_max_error(ExactSolution exactSolution, int N) {
        h = Max_X * 1.0 /N;
        x[0] = x0;  y[0] = y0;
        for (int i = 1; i < Max_X; i++)
            x[i] = x[i-1] + h;

        double max_error = Math.abs(y[0] - exactSolution.getY(0)),
               half_of_step = h/2,
               k1, k2, k3, k4;

        for (int i = 1; i < Max_X; i++) {
            k1 = f(x[i-1],y[i-1]);
            k2 = f(x[i-1] + half_of_step,y[i-1] + half_of_step*k1);
            k3 = f(x[i-1] + half_of_step, y[i-1] + half_of_step * k2);
            k4 = f(x[i-1] + h, y[i-1] + h*k3);
            y[i] = y[i-1] + (h/6) * (k1 + 2*k2 + 2*k3 + k4);
            y[i] = Math.abs(y[i] - exactSolution.getY(i));
            y[i] = Math.round(y[i] * 10000.0) / 10000.0;

            max_error = Math.max(max_error,y[i]);

            if (Double.valueOf(y[i]).isInfinite()) break;

        }
        return max_error;
    }

}
