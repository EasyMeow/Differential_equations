package com.example.differential_equations.entity;


import com.fathzer.soft.javaluator.DoubleEvaluator;
import com.fathzer.soft.javaluator.StaticVariableSet;
import com.vaadin.flow.component.notification.Notification;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;
import java.util.Locale;

@Getter
@Setter
public class Equestion {
    private String f;
    private Double t0;
    private Double T;
    private Double xt0;
    private Integer steps;
    private Double[] Xs;
    private Double[] Ts;
    private Double[] Errors;
    private Double errorMax;
    private Double errorMin;

    private final DoubleEvaluator eval = new DoubleEvaluator();

    public void findSolutionAndError() {
        f = f.toLowerCase(Locale.ROOT);
        findSolution(2);
        Double[] xs = Arrays.copyOf(Xs, Xs.length);
        Double[] ts = Arrays.copyOf(Ts, Ts.length);
        findSolution(1);
        errorMax = (double) 0;
        errorMin = (double) 0;
        Errors = new Double[steps];
        for (int m = 0; m < steps; m++) {
            Errors[m] = Math.pow(2, 3) * (xs[2 * m] - Xs[m]) / (Math.pow(2, 3) - 1);
            if (Errors[m] > errorMax) errorMax = Errors[m];
            else if (Errors[m] < errorMin) errorMin = Errors[m];
        }
    }

    private void findSolution(int koef) {
        double h = (T - t0) / (this.steps * koef);

        int steps = this.steps * koef + 1;

        Xs = new Double[steps];
        Ts = new Double[steps];

        for (int m = 0; m < steps; m++) {
            Ts[m] = t0 + m * h;
        }

        double K1;
        double K2;
        double K3;

        Xs[0] = xt0;

        for (int m = 0; m < steps - 1; m++) {
            K1 = h * f(Xs[m]);
            K2 = h * f(Xs[m] + K1 / 3.0);
            K3 = h * f(Xs[m] + (2.0 / 3.0 * K2));
            Xs[m + 1] = Xs[m] + (1 / 4.0 * K1) + (3 / 4.0 * K3);
        }

    }

    private double f(double xCurr) {
        try {
            StaticVariableSet<Double> variables = new StaticVariableSet<>();
            variables.set("x", xCurr);
            return eval.evaluate(f, variables);
        } catch (Exception e) {
            Notification notification = new Notification("Неправильно введена функция",3000);
            notification.setThemeName("error", true);
            notification.setPosition(Notification.Position.TOP_END);
            notification.open();
            throw e;
        }
    }
}
