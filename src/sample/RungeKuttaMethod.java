package sample;

import javafx.scene.chart.XYChart;

import static sample.Constants.f;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

/**
 * Created by Timkabor on 3/15/2018.
 */
class RungeKuttaMethod {
    private int max_x = Constants.getMax_X();
    private double x0 = 1.0, y0 = 3.0 , h = (max_x-x0) * 1.0 / 20;
    private double x[] = new double[100000],
                   y[] = new double[100000];
    private XYChart.Series<Number, Number> graph, error_graph;
    private ExactSolution exact_solution;

    RungeKuttaMethod(double x, double y, int N, ExactSolution solution) {
        x0 = x;
        y0 = y;
        max_x = Constants.getMax_X();
        h = (max_x-x0) /N;

        exact_solution = solution;
        initGraph();
        execute();
        executeError();
    }

    RungeKuttaMethod(ExactSolution solution) {
        exact_solution = solution;
        initGraph();
        execute();
        executeError();
    }
    private void initGraph(){
        graph = new Series<>();
        graph.setName("Runge Kutta");
        error_graph = new Series<>();
        error_graph.setName("Runge Kutta Error");
    }
    void execute() {
        x[0] = x0;y[0] = y0;
        graph.getData().add(new Data<>(x[0], y[0]));
        double half_of_step = h/2;
        double k1, k2, k3, k4;
        int i = 1;
        while(true) {
            x[i] = x[i-1] + h;
            if(x[i]- max_x >0.001) break;
            k1 = f(x[i-1],y[i-1]);
            k2 = f(x[i-1] + half_of_step,y[i-1] + (half_of_step*k1));
            k3 = f(x[i-1] + half_of_step, y[i-1] + (half_of_step * k2));
            k4 = f(x[i-1] + h, y[i-1] + (h*k3));
            y[i] = y[i-1] + (h/6) * (k1 + (2*k2) + (2*k3) + k4);
            graph.getData().add(new Data<>(x[i], y[i]));
            i++;
        }


    }
    private void executeError() {
        int i = 0;
        while(true) {
            if(x[i]- max_x >0.001) break;
            y[i] = Math.abs(y[i] - exact_solution.getY(i));
            error_graph.getData().add(new Data<>(x[i], y[i]));
            i++;
        }
    }
    double find_max_error(ExactSolution exactSolution, int N) {
        max_x = Constants.getMax_X();
        h = (max_x-x0) * 1.0 /N;
        x[0] = x0;  y[0] = y0;
        double max_error = 0,
               half_of_step = h/2,
               k1, k2, k3, k4;
        int i = 1;
        while(true ) {
            x[i] = x[i-1] + h;
            if(x[i]- max_x >0.001) break;
            k1 = f(x[i-1],y[i-1]);
            k2 = f(x[i-1] + half_of_step,y[i-1] + half_of_step*k1);
            k3 = f(x[i-1] + half_of_step, y[i-1] + half_of_step * k2);
            k4 = f(x[i-1] + h, y[i-1] + h*k3);
            y[i] = y[i-1] + (h/6) * (k1 + 2*k2 + 2*k3 + k4);
            max_error = Math.max(Math.abs(y[i] - exactSolution.getY(i)), max_error);
            i++;
        }
            return max_error;
    }

    Series<Number,Number> get_graph() {
        return graph;
    }

    Series<Number,Number> get_error_graph() {
        return error_graph;
    }

}
