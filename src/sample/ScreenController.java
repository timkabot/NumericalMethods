package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.chart.XYChart.Data;

import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ScreenController implements Initializable {
    @FXML
    private LineChart<Number, Number> chart,
                                      error_chart,
                                      max_error_chart;
    @FXML
    private javafx.scene.control.TextField N_Field,
                                           X_Field,
                                           Y_Field;
    @FXML
    private Button Show_First_Screen_Button,
                   Show_Second_Screen_Button;
    @FXML
    private TextField Info_Field,
                      N_MAX,
                      N_MIN,
                      X_Max;
    @FXML
    private CheckBox EulerBox,
                     ImprovedEulerBox,
                     RungeKuttaBox;
    @FXML
    private HBox hBox;
    @FXML
    private NumberAxis second_screen_x_axis;
    @FXML
    private NumberAxis first_screen_x_axis;
    @FXML
    private NumberAxis first_screen_x_error_axis ;
    // y` = 4/(x^2) - y/x - y^2   x0 = 1, y0 = 3, X = 4
    private ExactSolution exactSolution;

    private Series eulers_method_max_error,
            improved_eulers_method_max_error,
            runge_kutta_max_error;
    private  FullSolution fullSolution;
    private void initializeMethods(){
        fullSolution = new FullSolution();
    }
    private void addToMyChart() {
        chart.getData().addAll(fullSolution.getGraphs());

        error_chart.getData().addAll(fullSolution.getErrorGraphs());
    }

    private void initialize(){
        updateAxisRange(first_screen_x_axis,1,Constants.getMax_X());
        updateAxisRange(first_screen_x_error_axis,1,Constants.getMax_X());
        initializeMethods();
        addToMyChart();
        initializeBoxListeners();
        hBox.setSpacing(10);
    }
    private void initializeBoxListeners() {
        EulerBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue){
                chart.getData().remove(getEulersMethod().getGraph());
                error_chart.getData().remove(getEulersMethod().getErrorGraph());
            }
            else {
                chart.getData().add(getEulersMethod().getGraph());
                error_chart.getData().add(getEulersMethod().getErrorGraph());
            }
        });
        ImprovedEulerBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue){
                chart.getData().remove(getImprovedEulersMethod().getGraph());
                error_chart.getData().remove(getImprovedEulersMethod().getErrorGraph());
            }
            else {
                chart.getData().add(getImprovedEulersMethod().getGraph());
                error_chart.getData().add(getImprovedEulersMethod().getErrorGraph());
            }
        });
        RungeKuttaBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue){
                chart.getData().remove(getRungeKuttaMethod().get_graph());
                error_chart.getData().remove(getRungeKuttaMethod().get_error_graph());
            }
            else {
                chart.getData().add(getRungeKuttaMethod().get_graph());
                error_chart.getData().add(getRungeKuttaMethod().get_error_graph());
            }
        });
    }

    private void firstGraphButtonListener(){
        Show_First_Screen_Button.setOnAction(event -> {
            double x0 = toDouble(X_Field),
                   y0 = toDouble(Y_Field);
            int N = toInt(N_Field);
            Constants.setMax_X(toInt(X_Max));
            updateAxisRange(first_screen_x_axis,x0,Constants.getMax_X());
            updateAxisRange(first_screen_x_error_axis,x0,Constants.getMax_X());
            fullSolution = new FullSolution(x0,y0,N);
            Info_Field.clear();
            Info_Field.setText("x0=" + x0 + ", y0=" + y0 + ", step=" + (Constants.getMax_X()-x0)*1.0 /N);

            chart.getData().clear();
            error_chart.getData().clear();
            chart.getData().add(getExactSolution().getGraph());
            resetBoxes();
        });
    }
    private void resetBoxes()
    {
        EulerBox.setSelected(false);
        ImprovedEulerBox.setSelected(false);
        RungeKuttaBox.setSelected(false);
    }
    private void updateAxisRange(NumberAxis axis,double lowerBound, double upperBound)
    {
        axis.setAutoRanging(false);
        axis.setLowerBound(lowerBound);
        axis.setUpperBound(upperBound);
    }
    private void initializeErrorSeries()
    {
        eulers_method_max_error = new Series<>(); eulers_method_max_error.setName("Euler");
        improved_eulers_method_max_error = new Series<>(); improved_eulers_method_max_error.setName("Improved euler");
        runge_kutta_max_error = new Series<>(); runge_kutta_max_error.setName("Runge Kutta");
    }
    private void secondGraphButtonListener(){
        Show_Second_Screen_Button.setOnAction(event -> {
            max_error_chart.getData().clear();

            int n_min = toInt(N_MIN);
            int n_max = toInt(N_MAX);
            updateAxisRange(second_screen_x_axis,n_min,n_max);
            initializeErrorSeries();

            double x0, y0;

            for(int i = n_min ;i <= n_max;i++) {
                x0 = getExactSolution().getX0();
                y0 = getExactSolution().getY0();
                exactSolution = new ExactSolution(x0,y0,i);
                double max_error_euler = getEulersMethod().find_max_error(exactSolution,i),
                        max_error_improved_euler = getImprovedEulersMethod().find_max_error(exactSolution,i),
                        max_error_runge_kutta = getRungeKuttaMethod().find_max_error(exactSolution,i);

                if(!Double.isNaN(max_error_euler) && !Double.isInfinite(max_error_euler))
                    eulers_method_max_error.getData().add(new Data<>(i, max_error_euler));
                if(!Double.isNaN(max_error_runge_kutta) && !Double.isInfinite(max_error_runge_kutta))
                    runge_kutta_max_error.getData().add(new Data<>(i, max_error_runge_kutta));
                if(!Double.isNaN(max_error_improved_euler) && !Double.isInfinite(max_error_improved_euler))
                    improved_eulers_method_max_error.getData().add(new Data<>(i, max_error_improved_euler));
            }
            max_error_chart.getData().addAll(
                    eulers_method_max_error,
                    improved_eulers_method_max_error,
                    runge_kutta_max_error
            );
        });
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initialize();
        firstGraphButtonListener();
        secondGraphButtonListener();

    }
    private double toDouble(TextField num){
        return Double.valueOf(num.getText());
    }
    private int toInt(TextField num){
        return Integer.valueOf(num.getText());
    }
    private EulersMethod getEulersMethod()
    {
        return fullSolution.getEulersMethod();
    }
    private ImprovedEulersMethod getImprovedEulersMethod()
    {
        return fullSolution.getImprovedEulersMethod();
    }
    private RungeKuttaMethod getRungeKuttaMethod()
    {
        return fullSolution.getRungeKuttaMethod();
    }
    private ExactSolution getExactSolution()
    {
        return fullSolution.getExactSolution();
    }
}
