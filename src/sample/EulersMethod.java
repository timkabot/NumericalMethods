package sample;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import static sample.Constants.f;


/**
 * Created by Timkabor on 3/15/2018.
 */
class EulersMethod {
    private int max_x = Constants.getMax_X();
    private double x0 = 1.0, y0 = 3.0 , h = (max_x-x0) * 1.0 / 20;
    private double x[] = new double[100000], y[] = new double[100000];
    private Series<Number, Number> eulers_method,
                   eulers_method_error;
    private ExactSolution exactSolution;

    // y` = 4/(x^2) - y/x - y^2   x0 = 1, y0 = 3, step = 4
    EulersMethod(double x0, double y0, int N, ExactSolution solution) {
            this.x0 = x0;
            this.y0 = y0;
            max_x = Constants.getMax_X();
            this.h = (max_x-x0) * 1.0 /N;
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
        x[0] = x0;
        y[0] = y0;
        eulers_method.getData().add(new Data<>(x[0], y[0]));
        int i = 1;
        while(true) {
            x[i] = x[i - 1] + h;
            if(x[i]- max_x >0.001) break;
            y[i] = y[i - 1] + (h * f(x[i - 1], y[i - 1]));
            eulers_method.getData().add(new Data<>(x[i], y[i]));
            i++;
        }
    }
    void executeError( ) {
        int i=0;
        while(true) {
            if(x[i]- max_x >0.001) break;
            y[i] = Math.abs(y[i] - exactSolution.getY(i));
            eulers_method_error.getData().add(new Data<>(x[i], y[i]));
            i++;
        }
    }
    double find_max_error(ExactSolution exactSolution, int N) {
        x[0] = x0; y[0] = y0;
        h = (max_x-x0) * 1.0 /N;

        double max_error = 0;
        int i = 1;
        while(true) {
            x[i] = x[i - 1] + h;
            if(x[i] - max_x > 0.001) break;
            y[i] = y[i - 1] + h * f(x[i - 1], y[i - 1]);
            max_error = Math.max(Math.abs(y[i] - exactSolution.getY(i)), max_error);
            i++;
        }
        return max_error;
    }

    Series<Number,Number> getGraph() {
        return eulers_method;
    }

    Series<Number,Number> getErrorGraph() {
        return eulers_method_error;
    }
}
