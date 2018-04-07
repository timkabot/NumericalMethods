package sample;

import javafx.scene.chart.XYChart;

import static sample.Constants.f;


/**
 * Created by Timkabor on 3/15/2018.
 */
class EulersMethod {
    private double x0 = 1.0, y0 = 3.0 , h = 4;
    private int Max_X = Constants.get_max_h();
    private double x[] = new double[Max_X],
                   y[] = new double[Max_X];
    private XYChart.Series eulers_method,
                   eulers_method_error;
    private ExactSolution exactSolution;

    // y` = 4/(x^2) - y/x - y^2   x0 = 1, y0 = 3, step = 4
    EulersMethod(double x0, double y0, int N, ExactSolution solution) {
            this.x0 = x0;
            this.y0 = y0;
            this.h = Max_X * 1.0 /N;
            initGraphs();
            exactSolution = solution;
            execute();
            executeError();
    }

    EulersMethod(ExactSolution solution) {
        exactSolution = solution;
        initGraphs();
        execute();
        executeError();
    }
    private void initGraphs() {
        eulers_method = new XYChart.Series();
        eulers_method_error = new XYChart.Series();
        eulers_method.setName("Eulers method");
        eulers_method_error.setName("Eulers method");
    }
    void execute() {
        x[0] = x0; y[0] = y0;

        for (int i = 1; i < Max_X; i++)
            x[i] = x[i-1] + h;

        for (int i = 1; i < Max_X; i++) {
            y[i] = y[i - 1] + h * f(x[i - 1], y[i - 1]);
            y[i] = Math.round(y[i] * 1000.0) / 1000.0;
            if (Double.valueOf(y[i]).isInfinite()) { Max_X = i; break; }
        }

        for (int i = 0; i< Max_X; i++)
            eulers_method.getData().add(new XYChart.Data(x[i], y[i]));
    }
    void executeError( ) {
        for (int i = 0; i < Max_X; i++) {
            System.out.println(y[i] + " " + exactSolution.getY(i));
            y[i] = Math.abs(y[i] - exactSolution.getY(i));
            y[i] = Math.round(y[i] * 1000.0) / 1000.0;

            if (Double.valueOf(y[i]).isInfinite()) { Max_X = i; break; }
        }

        for (int i = 1; i< Max_X; i++)
            eulers_method_error.getData().add(new XYChart.Data(x[i], y[i]));
    }
    double find_max_error(ExactSolution exactSolution, int N) {
        Max_X = Constants.get_max_h();
        double x[] = new double[Max_X];
        double y[] = new double[Max_X];
        x[0] = x0; y[0] = y0;
        h = Max_X * 1.0 /N;
        double max_error = 0;
        for (int i = 1; i < Max_X; i++)
            x[i] = x[i-1] + h;

        for (int i = 1; i < Max_X; i++) {
            y[i] = y[i - 1] + h * f(x[i - 1], y[i - 1]);
            y[i] = Math.round(y[i] * 100.0) / 100.0;
            if (Double.valueOf(y[i]).isInfinite()) {Max_X = i;break;}
        }
        for (int i = 0; i< Max_X; i++)
                max_error = Math.max(Math.abs(y[i] - exactSolution.getY(i)),max_error);
        return max_error;
    }

    XYChart.Series getGraph() {
        return eulers_method;
    }

    XYChart.Series getErrorGraph() {
        return eulers_method_error;
    }
}
