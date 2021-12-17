package com.example.differential_equations.entity;

import com.storedobject.chart.*;

import lombok.Getter;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;

@Getter
public class LineChart {

    private final Random randomGen = new Random();
    private final Data dataY = new Data();
    private final Data dataX = new Data();
    private final SOChart soChart;

    public LineChart(Double[] dataY, Double[] dataX) {
        this.dataY.addAll(Arrays.stream(dataY).collect(Collectors.toList()));
        this.dataX.addAll(Arrays.stream(dataX).collect(Collectors.toList()));

        soChart = new SOChart();
        soChart.setSize("800px", "800px");


        XAxis xAxis = new XAxis(this.dataX);
        xAxis.setMin(dataX[0]);
        xAxis.setMax(dataX[dataX.length - 1]);
        xAxis.getLabel(true);
        YAxis yAxis = new YAxis(this.dataY);
        yAxis.setMin(dataY[0]);
        yAxis.setMax(dataY[dataY.length - 1]);
        yAxis.getLabel(true);

        com.storedobject.chart.LineChart lc = new com.storedobject.chart.LineChart(this.dataX, this.dataY);
        lc.plotOn(new RectangularCoordinate(xAxis, yAxis));
        lc.setSmoothness(false);

        soChart.add(lc);
    }


}

