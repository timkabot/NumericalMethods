package sample;

import javafx.scene.chart.XYChart;

/**
 * Created by Timkabor on 3/26/2018.
 */
class ExactSolution {
    private double x0 = 1.0, y0 = 3.0, h = 4, C;
    private int Max_X = Constants.get_max_h();
    private double x[] = new double[Max_X],
                   y[] = new double[Max_X];
    private XYChart.Series exact_solution;

    ExactSolution(double x, double y, int N) {
        x0 = x;
        y0 = y;
        h = Max_X * 1.0 /N;
        C = Constants.getC(x0, y0);
        initGraph();
        execute();
    }
    private void initGraph(){
        exact_solution = new XYChart.Series();
        exact_solution.setName("Exact Solution");
    }
    ExactSolution() {
        C = Constants.getC(x0, y0);
        initGraph();
        execute();
    }

    void execute() {

        x[0] = x0;
        y[0] = y0;
        for (int i = 1; i < Max_X; i++) {
            x[i] = x[i - 1] + h;
            y[i] = 1 / (C * Math.pow(x[i], 5) - x[i] / 4) + 2 / x[i];
            y[i] = Math.round(y[i] * 10000.0) / 10000.0;
        }

        for (int i = 0; i < Max_X; i++)
            exact_solution.getData().add(new XYChart.Data(x[i], y[i]));

    }
    double getY(int index) {
        return y[index];
    }

    public double getX0() {
        return x0;
    }

    public double getY0() {
        return y0;
    }

    XYChart.Series getGraph() {
        return exact_solution;
    }
}
