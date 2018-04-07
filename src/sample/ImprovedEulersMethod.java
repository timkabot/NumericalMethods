package sample;

import javafx.scene.chart.XYChart;

import static sample.Constants.f;

/**
 * Created by Timkabor on 3/15/2018.
 */
class ImprovedEulersMethod {
    private double x0 = 1.0, y0 = 3.0 , h = 4;
    private int Max_X = Constants.get_max_h();
    private double x[] = new double[Max_X],
                   y[] = new double[Max_X];
    private XYChart.Series graph, errorGraph;
    private ExactSolution exactSolution;

    ImprovedEulersMethod(double x, double y, int N, ExactSolution solution) {
        x0 = x;
        y0 = y;
        h = Max_X * 1.0 /N;
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
        graph = new XYChart.Series();
        graph.setName("Improved Euler");
        errorGraph = new XYChart.Series();
        errorGraph.setName("Improved Eulers Error");
    }
    private void execute() {
        x[0] = x0;
        for (int i = 1; i < Max_X; i++) {
            x[i] = x[i-1] + h;
        }
        y[0] = y0;
        double half_of_step = h/2;
        for (int i = 1; i < Max_X; i++) {
            y[i] = y[i - 1] + half_of_step * (f(x[i-1],y[i-1]) + f(x[i-1] + h, y[i-1] + h* f(x[i-1],y[i-1]) ));
            y[i] = Math.round(y[i] * 1000.0) / 1000.0;

            if (Double.valueOf(y[i]).isInfinite()) {
                Max_X = i;
                break;
            }
        }
        for (int i = 0; i< Max_X; i++)
            graph.getData().add(new XYChart.Data(x[i], y[i]));
    }
    private void executeError() {
        for (int i = 0; i < Max_X; i++) {
            y[i] = Math.abs(y[i] - exactSolution.getY(i));
            y[i] = Math.round(y[i] * 1000.0) / 1000.0;
            if (Double.valueOf(y[i]).isInfinite()) {
                Max_X = i;
                break;
            }
        }

        for (int i = 1; i< Max_X; i++)
            errorGraph.getData().add(new XYChart.Data(x[i], y[i]));

    }
    double find_max_error(ExactSolution exactSolution, int N) {
        Max_X = Constants.get_max_h();
        double x[] = new double[Max_X];
        double y[] = new double[Max_X];
        y[0] = y0;
        x[0] = x0;
        h = Max_X * 1.0 / N;
        double half_of_step = h / 2,
                max_error = 0;
        for (int i = 1; i < Max_X; i++)
            x[i] = x[i - 1] + h;

        for (int i = 1; i < Max_X; i++) {
            y[i] = y[i - 1] + half_of_step * (f(x[i - 1], y[i - 1]) + f(x[i - 1] + h, y[i - 1] + h * f(x[i - 1], y[i - 1])));
            y[i] = Math.round(y[i] * 10000.0) / 10000.0;
            if (Double.valueOf(y[i]).isInfinite()) {Max_X = i;break;}

        }
        for (int i = 1; i < Max_X; i++)
            max_error = Math.max(Math.abs(y[i] - exactSolution.getY(i)), max_error);
        return max_error;
    }
    XYChart.Series getGraph() {
        return graph;
    }

    XYChart.Series getErrorGraph() {
        return errorGraph;
    }
}
