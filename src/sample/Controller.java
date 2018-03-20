package sample;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {


    @FXML
    private LineChart<Number, Number> MyChart;
    @FXML
    private javafx.scene.control.TextField StepField;
    @FXML
    private javafx.scene.control.TextField xField;
    @FXML
    private javafx.scene.control.TextField yField;
    @FXML
    private Button show;
    @FXML
    private TextField infoField;
    // y` = 4/(x^2) - y/x - y^2   x0 = 1, y0 = 3, step = 4
    private EulersMethod eulers_method;
    private ImprovedEulersMethod improvedEulersMethod;
    private RungeKuttaMethod rungeKuttaMethod;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        eulers_method = new EulersMethod();
        improvedEulersMethod = new ImprovedEulersMethod();
        rungeKuttaMethod = new RungeKuttaMethod();
        MyChart.getData().addAll(eulers_method.execute(), improvedEulersMethod.execute(),rungeKuttaMethod.execute());
        show.setOnAction(event -> {
            double x0, y0;
            x0 = toDouble(xField.getText());
            y0 = toDouble(yField.getText());
            double step = Double.valueOf(StepField.getText());
            eulers_method = new EulersMethod(x0, y0,step);
            improvedEulersMethod = new ImprovedEulersMethod(x0, y0, step);
            rungeKuttaMethod = new RungeKuttaMethod(x0, y0, step);
            infoField.clear();
            infoField.setText("x0=" + x0 + ", y0=" + y0 + ", step=" + step);

            MyChart.getData().clear();
            MyChart.getData().addAll(eulers_method.execute(), improvedEulersMethod.execute(),rungeKuttaMethod.execute());
        });
    }
    private double toDouble(String num){
        return Double.valueOf(num);
    }

}
