package com.example.differential_equations.ui;


import com.example.differential_equations.entity.LineChart;
import com.example.differential_equations.entity.Equestion;
import com.github.appreciated.card.Card;
import com.storedobject.chart.*;
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
        setAlignItems(Alignment.BASELINE);
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
        cardGraphSolution.setVisible(false);
        cardGraphErrors = new Card();
        cardGraphErrors.setSizeFull();
        cardGraphErrors.setVisible(false);
        HorizontalLayout horizontalLayout = new HorizontalLayout(cardGraphSolution,cardGraphErrors);
        horizontalLayout.setAlignItems(Alignment.AUTO);
        horizontalLayout.setPadding(true);
        horizontalLayout.setSpacing(true);
        add(cardEditor, horizontalLayout);
    }

    private void countAndDraw(Equestion equestion) {
        equestion.findSolutionAndError();

        LineChart chartSolution = new LineChart(equestion.getXs(), equestion.getTs());
        SOChart soChart = chartSolution.getSoChart();
        cardGraphSolution.add(soChart);
        cardGraphSolution.setVisible(true);

        Double[] datax= new Double[equestion.getSteps()];
        for(int i = 0;i<equestion.getSteps();i++) {
            datax[i] =(double) i;
        }
        LineChart chartError = new LineChart(equestion.getErrors(), datax);
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
        private final Equestion equestion = new Equestion();
        private final Binder<Equestion> binder = new Binder<>(Equestion.class);
        private Consumer<Equestion> action;

        public Editor() {
            equestion.findSolutionAndError();

            equestionTextField.setValue("(1+sin(x)+x^2)^(-3)*cos(x)");
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
            binder.forField(steps)
                    .asRequired()
                    .bind(Equestion::getSteps, Equestion::setSteps);

            resultButton.addClickListener(event -> {
                if (binder.writeBeanIfValid(equestion)) {
                    action.accept(equestion);
                }
            });

            HorizontalLayout firstLisne = new HorizontalLayout(equestionTextField, tFirst, tLast);
            firstLisne.setWidthFull();
            firstLisne.setPadding(true);
            HorizontalLayout secondLine = new HorizontalLayout(xFirst, steps, resultButton);
            secondLine.setWidthFull();
            secondLine.setAlignItems(Alignment.BASELINE);
            secondLine.setPadding(true);

            setSizeFull();
            add(firstLisne, secondLine);
        }

        public void setAction(Consumer<Equestion> action) {
            this.action = action;
        }
    }

//    public class Graf {
//
//        private ApexCharts apexChart;
//        private XAxis xAxis;
//
//        public Graf(Series<Double> pwmSignal1, Series<Double> pwmSignal2, Series<Double> pwmSignal3) {
//            apexChart = ApexChartsBuilder.get()
//                    .withChart(ChartBuilder.get()
//                            .withType(Type.line)
//                            .withZoom(ZoomBuilder.get()
//                                    .withEnabled(true)
//                                    .build())
//                            .withToolbar(ToolbarBuilder.get()
//                                    .withShow(true)
//                                    .build())
//                            .build())
//                    .withLegend(LegendBuilder.get()
//                            .withShow(true)
//                            .build())
//                    .withDataLabels(DataLabelsBuilder.get()
//                            .withEnabled(false)
//                            .build())
//                    .withColors("#77B6EA", "#545454")
//                    .withTooltip(TooltipBuilder.get()
//                            .withEnabled(false)
//                            .build())
//                    .withStroke(StrokeBuilder.get()
//                            .withCurve(Curve.straight)
//                            .build())
//                    .withTitle(TitleSubtitleBuilder.get()
//                            .withText("График")
//                            .withAlign(Align.left)
//                            .build())
//                    .withGrid(GridBuilder.get()
//                            .withRow(RowBuilder.get()
//                                    .withColors("#f3f3f3", "transparent")
//                                    .withOpacity(0.5)
//                                    .build())
//                            .build())
//                    .withXaxis(XAxisBuilder.get()
//                            .withFloating(true)
//                            .build())
//                    .withYaxis(YAxisBuilder.get()
//                            .withTitle(TitleBuilder.get()
//                                    .withText("Measurements")
//                                    .build())
//                            .build())
//                    .withSeries(new Series[] { pwmSignal1, pwmSignal2, pwmSignal3})
//                    .build();
//
//        }
//
//        public void updateTheGraf(String[] datesX, float[][] series) {
//
//        }
//
//    }


}
