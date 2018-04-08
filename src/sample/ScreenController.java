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
    private EulersMethod eulers_method;
    private ImprovedEulersMethod improvedEulersMethod;
    private RungeKuttaMethod rungeKuttaMethod;
    private ExactSolution exactSolution;

    private Series eulers_method_max_error,
            improved_eulers_method_max_error,
            runge_kutta_max_error;

    private void initializeMethods(){
        exactSolution = new ExactSolution();
        eulers_method = new EulersMethod(exactSolution);
        rungeKuttaMethod = new RungeKuttaMethod(exactSolution);
        improvedEulersMethod = new ImprovedEulersMethod(exactSolution);
    }
    private void addToMyChart() {
        chart.getData().addAll(
                eulers_method.getGraph(),
                improvedEulersMethod.getGraph(),
                rungeKuttaMethod.get_graph(),
                exactSolution.getGraph()
        );
        error_chart.getData().addAll(
                eulers_method.getErrorGraph(),
                improvedEulersMethod.getErrorGraph(),
                rungeKuttaMethod.get_error_graph()
        );
    }

    private void initialize(){
        initializeMethods();
        addToMyChart();
        initializeBoxListeners();
        hBox.setSpacing(10);
    }
    private void initializeBoxListeners() {
        EulerBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue){
                chart.getData().remove(eulers_method.getGraph());
                error_chart.getData().remove(eulers_method.getErrorGraph());
            }
            else {
                chart.getData().add(eulers_method.getGraph());
                error_chart.getData().add(eulers_method.getErrorGraph());
            }
        });
        ImprovedEulerBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue){
                chart.getData().remove(improvedEulersMethod.getGraph());
                error_chart.getData().remove(improvedEulersMethod.getErrorGraph());
            }
            else {
                chart.getData().add(improvedEulersMethod.getGraph());
                error_chart.getData().add(improvedEulersMethod.getErrorGraph());
            }
        });
        RungeKuttaBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue){
                chart.getData().remove(rungeKuttaMethod.get_graph());
                error_chart.getData().remove(rungeKuttaMethod.get_error_graph());
            }
            else {
                chart.getData().add(rungeKuttaMethod.get_graph());
                error_chart.getData().add(rungeKuttaMethod.get_error_graph());
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
            exactSolution = new ExactSolution(x0,y0,N);
            eulers_method = new EulersMethod(x0, y0,N,exactSolution);
            improvedEulersMethod = new ImprovedEulersMethod(x0, y0, N,exactSolution);
            rungeKuttaMethod = new RungeKuttaMethod(x0, y0, N,exactSolution);

            Info_Field.clear();
            Info_Field.setText("x0=" + x0 + ", y0=" + y0 + ", step=" + (Constants.getMax_X()-x0)*1.0 /N);

            chart.getData().clear();
            error_chart.getData().clear();
            chart.getData().add(exactSolution.getGraph());
            EulerBox.setSelected(false);
            ImprovedEulerBox.setSelected(false);
            RungeKuttaBox.setSelected(false);
        });
    }
    private void updateAxisRange(NumberAxis axis,double lowerBound, double upperBound)
    {
        axis.setAutoRanging(false);
        axis.setLowerBound(lowerBound);
        axis.setUpperBound(upperBound);
    }
    private void secondGraphButtonListener(){
        Show_Second_Screen_Button.setOnAction(event -> {
            max_error_chart.getData().clear();

            int n_min = toInt(N_MIN);
            int n_max = toInt(N_MAX);
            updateAxisRange(second_screen_x_axis,n_min,n_max);
            eulers_method_max_error = new Series<>(); eulers_method_max_error.setName("Euler");
            improved_eulers_method_max_error = new Series<>(); improved_eulers_method_max_error.setName("Improved euler");
            runge_kutta_max_error = new Series<>(); runge_kutta_max_error.setName("Runge Kutta");

            double x0, y0;
            for(int i = n_min ;i <= n_max;i++) {
                x0 = exactSolution.getX0();
                y0 = exactSolution.getY0();
                exactSolution = new ExactSolution(x0,y0,i);
                double max_error_euler = eulers_method.find_max_error(exactSolution,i),
                        max_error_improved_euler = improvedEulersMethod.find_max_error(exactSolution,i),
                        max_error_runge_kutta = rungeKuttaMethod.find_max_error(exactSolution,i);

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

}
