package com.example.differential_equations.ui;


import com.example.differential_equations.entity.Chart;
import com.example.differential_equations.entity.Equestion;
import com.github.appreciated.card.Card;
import com.storedobject.chart.*;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;


import java.util.function.Consumer;


@PageTitle("Главная")
@Route(value = "main")
public class MainPage extends VerticalLayout {
    Card cardGraphSolution;
    Card cardGraphErrors;

    @Autowired
    public MainPage() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        initContent();
    }

    private void initContent() {
        Card cardEditor = new Card();
        cardEditor.setSizeFull();
        Editor editor = new Editor();
        editor.setAction(this::countAndDraw);
        cardEditor.add(editor);
        cardEditor.setWidth("100%");
        cardGraphSolution = new Card();
        cardGraphSolution.setSizeFull();
        cardGraphSolution.getContent().setPadding(true);
        cardGraphSolution.setVisible(false);
        cardGraphErrors = new Card();
        cardGraphErrors.setSizeFull();
        cardGraphErrors.getContent().setPadding(true);
        cardGraphErrors.setVisible(false);
        HorizontalLayout horizontalLayout = new HorizontalLayout(cardGraphSolution, cardGraphErrors);
        horizontalLayout.setAlignItems(Alignment.AUTO);
        horizontalLayout.setPadding(true);
        horizontalLayout.setSpacing(true);
        add(cardEditor, horizontalLayout);
    }

    private void countAndDraw(Equestion equestion) {
        equestion.findSolutionAndError();

        if(cardGraphSolution.getContent()!=null) {
            cardGraphSolution.getContent().removeAll();
        }
        Chart chartSolution = new Chart(equestion.getXs(), equestion.getTs());
        SOChart soChart = chartSolution.getSoChart();
        cardGraphSolution.add(soChart);
        cardGraphSolution.setVisible(true);

        if(cardGraphErrors.getContent()!=null) {
            cardGraphErrors.getContent().removeAll();
        }

        Chart chartError = new Chart(equestion.getErrors(), equestion.getSteps(), equestion.getErrorMin(), equestion.getErrorMax());
        SOChart soChartError = chartError.getSoChart();
        cardGraphErrors.add(soChartError);
        cardGraphErrors.setVisible(true);
    }

    public static class Editor extends VerticalLayout {
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
                            return ValidationResult.error("");
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
                            return ValidationResult.error("");
                        }
                    })
                    .bind(Equestion::getXt0, Equestion::setXt0);

            steps.setMin(1);
            steps.setValue(100);
            steps.setMax(1000);
            steps.setMinWidth("10%");
            binder.forField(steps)
                    .asRequired()
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
            add(firstLine, secondLine);
        }

        public void setAction(Consumer<Equestion> action) {
            this.action = action;
        }
    }
}
