package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;

public class ScreenController implements Initializable {
    @FXML
    private LineChart<Number, Number> chart,
            errorChart,
                                      MaxErrorChart;
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
                      N_MIN;
    @FXML
    private CheckBox EulerBox,
            ImprovedEulerBox,
            RungeKuttaBox;
    @FXML
    private HBox hBox;
    // y` = 4/(x^2) - y/x - y^2   x0 = 1, y0 = 3, step = 4
    private EulersMethod eulers_method;
    private ImprovedEulersMethod improvedEulersMethod;
    private RungeKuttaMethod rungeKuttaMethod;
    private ExactSolution exactSolution;
    private XYChart.Series eulers_method_max_error,
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
                rungeKuttaMethod.getGraph(),
                exactSolution.getGraph()
        );
        errorChart.getData().addAll(
                eulers_method.getErrorGraph(),
                improvedEulersMethod.getErrorGraph(),
                rungeKuttaMethod.getErrorGraph()
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
                errorChart.getData().remove(eulers_method.getErrorGraph());
            }
            else {
                chart.getData().add(eulers_method.getGraph());
                errorChart.getData().add(eulers_method.getErrorGraph());
            }
        });
        ImprovedEulerBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue){
                chart.getData().remove(improvedEulersMethod.getGraph());
                errorChart.getData().remove(improvedEulersMethod.getErrorGraph());
            }
            else {
                chart.getData().add(improvedEulersMethod.getGraph());
                errorChart.getData().add(improvedEulersMethod.getErrorGraph());
            }
        });
        RungeKuttaBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue){
                chart.getData().remove(rungeKuttaMethod.getGraph());
                errorChart.getData().remove(rungeKuttaMethod.getErrorGraph());
            }
            else {
                chart.getData().add(rungeKuttaMethod.getGraph());
                errorChart.getData().add(rungeKuttaMethod.getErrorGraph());
            }
        });
    }

    private void firstGraphButtonListener(){
        Show_First_Screen_Button.setOnAction(event -> {
            double x0 = toDouble(X_Field),
                   y0 = toDouble(Y_Field);
            int N = toInt(N_Field);

            exactSolution = new ExactSolution(x0,y0,N);
            eulers_method = new EulersMethod(x0, y0,N,exactSolution);
            improvedEulersMethod = new ImprovedEulersMethod(x0, y0, N,exactSolution);
            rungeKuttaMethod = new RungeKuttaMethod(x0, y0, N,exactSolution);

            Info_Field.clear();
            Info_Field.setText("x0=" + x0 + ", y0=" + y0 + ", step=" + Constants.get_max_h() * 1.0 /N);

            chart.getData().clear();
            errorChart.getData().clear();
            chart.getData().add(exactSolution.getGraph());
        });
    }
    private void secondGraphButtonListener(){
        Show_Second_Screen_Button.setOnAction(event -> {
            MaxErrorChart.getData().clear();
            int n_min = toInt(N_MIN);
            int n_max = toInt(N_MAX);
            eulers_method_max_error = new XYChart.Series(); eulers_method_max_error.setName("Euler");
            improved_eulers_method_max_error = new XYChart.Series(); improved_eulers_method_max_error.setName("Improved euler");
            runge_kutta_max_error = new XYChart.Series(); runge_kutta_max_error.setName("Runge Kutta");
            double x0, y0;
            for(int i = n_min ;i <= n_max;i++) {
                x0 = exactSolution.getX0();
                y0 = exactSolution.getY0();
                exactSolution = new ExactSolution(x0,y0,i);
                double max_error_euler = eulers_method.find_max_error(exactSolution,i),
                        max_error_improved_euler = improvedEulersMethod.find_max_error(exactSolution,i),
                        max_error_runge_kutta = rungeKuttaMethod.find_max_error(exactSolution,i);
                eulers_method_max_error.getData().add(new XYChart.Data(i, max_error_euler));
                runge_kutta_max_error.getData().add(new XYChart.Data(i, max_error_runge_kutta));
                improved_eulers_method_max_error.getData().add(new XYChart.Data(i, max_error_improved_euler));
            }
            MaxErrorChart.getData().addAll(
                    eulers_method_max_error,
                    improved_eulers_method_max_error,
                    runge_kutta_max_error);
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
