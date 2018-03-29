package sample;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class ScreenController implements Initializable {
    @FXML
    private LineChart<Number, Number> MyChart;
    @FXML
    private LineChart<Number, Number> ErrorChart;
    @FXML
    private LineChart<Number, Number> MaxErrorChart;
    @FXML
    private javafx.scene.control.TextField N_Field;
    @FXML
    private javafx.scene.control.TextField X_Field;
    @FXML
    private javafx.scene.control.TextField Y_Field;
    @FXML
    private Button Show_First_Screen_Button;
    @FXML
    private Button Show_Second_Screen_Button;
    @FXML
    private TextField Info_Field;
    @FXML
    private TextField N_MAX;
    @FXML
    private TextField N_MIN;

    // y` = 4/(x^2) - y/x - y^2   x0 = 1, y0 = 3, step = 4
    private EulersMethod eulers_method;
    private ImprovedEulersMethod improvedEulersMethod;
    private RungeKuttaMethod rungeKuttaMethod;
    private ExactSolution exactSolution;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eulers_method = new EulersMethod();
        improvedEulersMethod = new ImprovedEulersMethod();
        rungeKuttaMethod = new RungeKuttaMethod();
        exactSolution = new ExactSolution();
        MyChart.getData().addAll(eulers_method.execute(),
                improvedEulersMethod.execute(),
                rungeKuttaMethod.execute(),
                exactSolution.execute());

        ErrorChart.getData().addAll(
                eulers_method.executeError(exactSolution),
                improvedEulersMethod.executeError(exactSolution),
                rungeKuttaMethod.executeError(exactSolution));

        Show_First_Screen_Button.setOnAction(event -> {
            double x0, y0;
            x0 = toDouble(X_Field.getText());
            y0 = toDouble(Y_Field.getText());
            int N = Integer.valueOf(N_Field.getText());
            eulers_method = new EulersMethod(x0, y0,N);
            improvedEulersMethod = new ImprovedEulersMethod(x0, y0, N);
            rungeKuttaMethod = new RungeKuttaMethod(x0, y0, N);
            exactSolution = new ExactSolution(x0,y0,N);
            Info_Field.clear();
            Info_Field.setText("x0=" + x0 + ", y0=" + y0 + ", step=" + Constants.get_max_h() * 1.0 /N);

            MyChart.getData().clear();
            MyChart.getData().addAll(
                    eulers_method.execute(),
                    improvedEulersMethod.execute(),
                    rungeKuttaMethod.execute(),
                    exactSolution.execute());
            ErrorChart.getData().clear();
            ErrorChart.getData().addAll(
                    eulers_method.executeError(exactSolution),
                    improvedEulersMethod.executeError(exactSolution),
                    rungeKuttaMethod.executeError(exactSolution));
        });
        Show_Second_Screen_Button.setOnAction(event -> {
            MaxErrorChart.getData().clear();
            int n_min = Integer.valueOf(N_MIN.getText());
            int n_max = Integer.valueOf(N_MAX.getText());
            XYChart.Series eulers_method_max_error = new XYChart.Series(),
                           improved_eulers_method_max_error = new XYChart.Series(),
                           runge_kutta_max_error = new XYChart.Series();

            for(int i =n_min;i<=n_max;i++) {
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
    private double toDouble(String num){
        return Double.valueOf(num);
    }

}
