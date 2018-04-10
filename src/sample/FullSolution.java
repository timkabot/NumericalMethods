package sample;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import javafx.scene.chart.XYChart.Series;
/**
 * Created by Timkabor on 4/10/2018.
 */
public class FullSolution {
    ExactSolution exactSolution;
    EulersMethod eulersMethod;
    ImprovedEulersMethod improvedEulersMethod;
    RungeKuttaMethod rungeKuttaMethod;
    public FullSolution()
    {
        exactSolution = new ExactSolution();
        eulersMethod = new EulersMethod(exactSolution);
        rungeKuttaMethod = new RungeKuttaMethod(exactSolution);
        improvedEulersMethod = new ImprovedEulersMethod(exactSolution);
    }
    public FullSolution(double x0, double y0,int N)
    {
        exactSolution = new ExactSolution(x0,y0,N);
        eulersMethod = new EulersMethod(x0, y0,N,exactSolution);
        improvedEulersMethod = new ImprovedEulersMethod(x0, y0, N,exactSolution);
        rungeKuttaMethod = new RungeKuttaMethod(x0, y0, N,exactSolution);
    }
    public ArrayList<Series<Number, Number>> getGraphs()
    {
        ArrayList<Series<Number, Number>> result = new ArrayList<>();
        result.add(exactSolution.getGraph());
        result.add(eulersMethod.getGraph());
        result.add(improvedEulersMethod.getGraph());
        result.add(rungeKuttaMethod.get_graph());
        return result;
    }
    public ArrayList<Series<Number, Number>> getErrorGraphs()
    {
        ArrayList<Series<Number, Number>> result = new ArrayList<>();
        result.add(eulersMethod.getErrorGraph());
        result.add(improvedEulersMethod.getErrorGraph());
        result.add(rungeKuttaMethod.get_error_graph());
        return result;
    }

    public ExactSolution getExactSolution() {
        return exactSolution;
    }

    public EulersMethod getEulersMethod() {
        return eulersMethod;
    }

    public ImprovedEulersMethod getImprovedEulersMethod() {
        return improvedEulersMethod;
    }

    public RungeKuttaMethod getRungeKuttaMethod() {
        return rungeKuttaMethod;
    }
}
