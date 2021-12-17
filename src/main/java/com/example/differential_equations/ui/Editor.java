package com.example.differential_equations.ui;

import com.example.differential_equations.entity.Equestion;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;

import java.util.function.Consumer;

public class Editor extends VerticalLayout {
    private final Label label= new Label("Интегрирование с постоянным шагом");
    private final TextField equestionTextField = new TextField("Функция f(x)");
    private final NumberField tFirst = new NumberField("t0");
    private final NumberField tLast = new NumberField("T");
    private final NumberField xFirst = new NumberField("x(t0)");
    private final IntegerField steps = new IntegerField("Количество шагов");
    private final Button resultButton = new Button("Получить результат");
    private Equestion equestion = new Equestion();
    private final Binder<Equestion> binder = new Binder<>(Equestion.class);
    private Consumer<Equestion> action;

    public Editor() {
        equestionTextField.setValue("(1+sin(x)+x^2)^(-3)*cos(x)");
        equestionTextField.setWidthFull();
        binder.forField(equestionTextField)
                .asRequired()
                .bind(Equestion::getF, Equestion::setF);

        tFirst.setMin(0);
        tFirst.setValue((double) 0);
        tFirst.setMax(10);
        binder.forField(tFirst)
                .asRequired()
                .withValidator((value, context) -> {
                    if (tLast.getValue() > value) {
                        return ValidationResult.ok();
                    } else {
                        return ValidationResult.error("t0 не может быть больше T!");
                    }
                })
                .bind(Equestion::getT0, Equestion::setT0);

        tLast.setMin(1);
        tLast.setValue((double) 4);
        tLast.setMax(10);
        binder.forField(tLast)
                .asRequired()
                .withValidator((value, context) -> {
                    if (tFirst.getValue() < value) {
                        return ValidationResult.ok();
                    } else {
                        return ValidationResult.error("T не может быть меньше t0!");
                    }
                })
                .bind(Equestion::getT, Equestion::setT);

        xFirst.setValue((double) 0);
        binder.forField(xFirst)
                .asRequired()
                .withValidator((value, context) -> {
                    if ((tFirst.getValue() <= value) && tLast.getValue() >= value) {
                        return ValidationResult.ok();
                    } else {
                        return ValidationResult.error("x0 должен быть в пределах [t0;T]!");
                    }
                })
                .bind(Equestion::getXt0, Equestion::setXt0);

        steps.setMin(1);
        steps.setValue(100);
        steps.setMax(1000);
        steps.setMinWidth("10%");
        binder.forField(steps)
                .asRequired()
                .withValidator((value, context) -> {
                    if (value > 0) {
                        return ValidationResult.ok();
                    } else {
                        return ValidationResult.error("Количество шагов не может быть отрицательным!");
                    }
                })
                .bind(Equestion::getSteps, Equestion::setSteps);

        resultButton.setMinWidth("10%");
        resultButton.addClickListener(event -> {
            equestion = new Equestion();
            if (binder.writeBeanIfValid(equestion)) {
                action.accept(equestion);
            }
        });

        setAlignItems(Alignment.AUTO);
        HorizontalLayout firstLine = new HorizontalLayout(equestionTextField, tFirst, tLast);
        firstLine.setWidthFull();
        firstLine.setPadding(true);
        Div div =new Div();
        div.setWidthFull();
        HorizontalLayout secondLine = new HorizontalLayout(div,xFirst, steps, resultButton);
        secondLine.setWidthFull();
        secondLine.setAlignItems(Alignment.BASELINE);
        secondLine.setPadding(true);

        setSizeFull();
        add(label, firstLine, secondLine);
    }

    public void setAction(Consumer<Equestion> action) {
        this.action = action;
    }
}
