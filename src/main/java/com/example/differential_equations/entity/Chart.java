package com.example.differential_equations.entity;

import com.storedobject.chart.*;

import lombok.Getter;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public class Chart {

    private final Data dataY = new Data();
    private final Data dataX = new Data();
    private final SOChart soChart;

    // simple line chart constructor
    public Chart(Double[] dataY, Double[] dataX) {
        this.dataY.addAll(Arrays.stream(dataY).collect(Collectors.toList()));
        this.dataX.addAll(Arrays.stream(dataX).collect(Collectors.toList()));

        soChart = new SOChart();
        soChart.setSize("900px", "650px");


        XAxis xAxis = new XAxis(this.dataX);
        xAxis.setMin(dataX[0]);
        xAxis.setMax(dataX[dataX.length - 1]);
        xAxis.getLabel(true);
        YAxis yAxis = new YAxis(this.dataY);
        yAxis.setMin(dataY[0]);
        yAxis.setMax(dataY[dataY.length - 1]);
        yAxis.getLabel(true);

        LineChart lc = new LineChart(this.dataX, this.dataY);
        lc.setName("Решение");
        lc.plotOn(new RectangularCoordinate(xAxis, yAxis));
        lc.setSmoothness(true);

        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());

        soChart.add(lc, toolbox);
    }

    // errors chart constructor
    public Chart(Double[] dataY, Integer dataX, Double errorMin, Double errorMax) {
        Double[] dataxArray = new Double[dataX];
        for (int i = 0; i < dataX; i++) {
            dataxArray[i] = (double) i;
        }

        this.dataY.addAll(Arrays.stream(dataY).collect(Collectors.toList()));
        this.dataX.addAll(Arrays.stream(dataxArray).collect(Collectors.toList()));

        soChart = new SOChart();
        soChart.setSize("900px", "650px");


        XAxis xAxis = new XAxis(this.dataX);
        xAxis.setMin(dataxArray[0]);
        xAxis.setMax(dataxArray[dataxArray.length - 1]);
        xAxis.getLabel(true);
        YAxis yAxis = new YAxis(this.dataY);
        yAxis.setMin(errorMin);
        yAxis.setMax(errorMax);
        yAxis.getLabel(true);

        LineChart lc = new LineChart(this.dataX, this.dataY);
        lc.setName("Погрешность");
        lc.plotOn(new RectangularCoordinate(xAxis, yAxis));
        lc.setSmoothness(true);
        lc.setConnectNullPoints(true);

        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download(), new Toolbox.Zoom());

        soChart.add(lc, toolbox);
    }
}

