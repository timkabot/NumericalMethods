package sample;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private LineChart<Number, Number> MyChart;

    private XYChart.Series Sin_ser;
    private XYChart.Series Cos_ser;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        double x0 = -Math.PI, X = Math.PI;
        int N = 50;
        double x[] = new double[N];
        double y_sin[] = new double[N];
        double y_cos[] = new double[N];

        double h = (X - x0) / N;

        x[0]=x0;
        for (int i = 1; i < N; i++) {
            x[i] = x[i-1] + h;
        }

        for (int i = 0; i < N; i++) {
            y_sin[i] = Math.sin(x[i]);
            y_cos[i] = Math.cos(x[i]);
        }

        Sin_ser = new XYChart.Series();
        Sin_ser.setName("Sin");
        Cos_ser = new XYChart.Series();
        Cos_ser.setName("Cos");

        for (int i=0; i<x.length; i++)
        {
            Sin_ser.getData().add(new XYChart.Data(x[i], y_sin[i]));
            Cos_ser.getData().add(new XYChart.Data(x[i], y_cos[i]));
        }

        MyChart.getData().addAll(Sin_ser, Cos_ser);

    }

}
