package sample;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import static sample.Constants.f;

/**
 * Created by Timkabor on 3/15/2018.
 */
class ImprovedEulersMethod {
    private int max_x = Constants.getMax_X();
    private double x0 = 1.0, y0 = 3.0 , h = (max_x-x0) * 1.0 / 20;
    private double x[] = new double[100000],
                   y[] = new double[100000];
    private Series<Number,Number> graph, errorGraph;
    private ExactSolution exactSolution;

    ImprovedEulersMethod(double x, double y, int N, ExactSolution solution) {
        x0 = x;
        y0 = y;
        max_x = Constants.getMax_X();
        h = (max_x-x0) * 1.0 /N;
        exactSolution = solution;
        initGraph();
        execute();
        executeError();
    }

    ImprovedEulersMethod(ExactSolution solution) {
        exactSolution = solution;
        initGraph();
        execute();
        executeError();
    }
    private void initGraph(){
        graph = new Series<>();
        graph.setName("Improved Euler");
        errorGraph = new Series<>();
        errorGraph.setName("Improved Eulers Error");
    }
    private void execute() {
        x[0] = x0; y[0] = y0;
        int i = 1;
        double half_of_step = h/2;
        while(true ) {
            x[i] = x[i-1] + h;
            if(x[i]- max_x >0.001) break;
            y[i] = y[i - 1] + half_of_step * (f(x[i-1],y[i-1]) + f(x[i-1] + h, y[i-1] + h*f(x[i-1],y[i-1]) ));

            graph.getData().add(new Data<>(x[i], y[i]));
            i++;
        }
    }
    private void executeError() {
        int i = 0;
        while(true) {
            if(x[i]- max_x >0.001) break;
            y[i] = Math.abs(y[i] - exactSolution.getY(i));
            errorGraph.getData().add(new Data<>(x[i], y[i]));
            i++;
        }
    }
    double find_max_error(ExactSolution exactSolution, int N) {
        max_x = Constants.getMax_X();
        y[0] = y0;
        x[0] = x0;
        h = (max_x-x0) * 1.0 /N;
        double half_of_step = h / 2,
                max_error = 0;
        int i = 1;
        while(true) {
            x[i] = x[i - 1] + h;
            if(x[i]- max_x >0.001) break;
            y[i] = y[i - 1] + half_of_step * (f(x[i - 1], y[i - 1]) + f(x[i - 1] + h, y[i - 1] + h * f(x[i - 1], y[i - 1])));
            max_error = Math.max(Math.abs(y[i] - exactSolution.getY(i)), max_error);
            i++;
        }
        return max_error;
    }
    Series<Number,Number> getGraph() {
        return graph;
    }

    Series<Number,Number> getErrorGraph() {
        return errorGraph;
    }
}
