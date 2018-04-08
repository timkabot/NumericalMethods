package sample;

import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
/**
 * Created by Timkabor on 3/26/2018.
 */
class ExactSolution {
    private double x0 = 1, y0 = 3, h = 4, C;
    private int max_x = Constants.getMax_X();
    private double x[] = new double[100000], y[] = new double[100000];
    private Series<Number, Number> exact_solution;

    ExactSolution(double x, double y, int N) {
        x0 = x;
        y0 = y;
        max_x = Constants.getMax_X();
        h = (max_x - x0) * 1.0 /N;
        C = Constants.getC(x0, y0);
        initGraph();
        execute();
    }
    private void initGraph(){
        exact_solution = new Series<>();
        exact_solution.setName("Exact Solution");
    }
    ExactSolution() {
        C = Constants.getC(x0, y0);
        initGraph();
        execute();
    }

    private void execute() {
        x[0] = x0;
        y[0] = y0;
        exact_solution.getData().add(new Data<>(x[0], y[0]));
        int i = 1;
        while(true) {
            x[i] = x[i - 1] + h;
            if(x[i] - max_x > 0.0001) break;
            //y[i] = Math.exp(-2*x[i]) * (1/4 * x[i]*x[i]*x[i]*x[i] + 1);
            y[i] = (2*(C * Math.pow(x[i],4) +  1) ) / (x[i] * (C * Math.pow(x[i],4) - 1) );
            //y[i] = 1 / (C * Math.pow(x[i], 5) - x[i] / 4) + 2 / x[i];
            exact_solution.getData().add(new Data<>(x[i], y[i]));
            i++;
        }

    }
    double getY(int index) {
        return y[index];
    }

    double getX0() {
        return x0;
    }

    double getY0() {
        return y0;
    }

    Series<Number,Number> getGraph() {
        return exact_solution;
    }
}
