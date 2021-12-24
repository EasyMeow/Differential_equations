package com.example.differential_equations.ui;


import com.example.differential_equations.entity.Chart;
import com.example.differential_equations.entity.Equestion;
import com.github.appreciated.card.Card;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;




@PageTitle("Главная")
@Route(value = "main")
public class MainPage extends VerticalLayout {
    private Card cardGraphSolution;
    private Card cardGraphErrors;
    private boolean hasResizeListener = false;

    @Autowired
    public MainPage() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        initContent();
    }

    private void initContent() {
        Editor editor = new Editor();
        Card cardEditor = new Card();
        cardEditor.setSizeFull();
        cardEditor.setHeight("37%");
        cardEditor.addComponentAsFirst(editor);
        editor.setAction(this::countAndDraw);

        cardGraphSolution = new Card();
        cardGraphSolution.setHeightFull();
        cardGraphSolution.setWidth("50%");
        cardGraphSolution.getContent().setPadding(true);
        cardGraphSolution.setVisible(false);

        cardGraphErrors = new Card();
        cardGraphErrors.setHeightFull();
        cardGraphErrors.setWidth("50%");
        cardGraphErrors.getContent().setPadding(true);
        cardGraphErrors.setVisible(false);

        HorizontalLayout horizontalLayout = new HorizontalLayout(cardGraphSolution, cardGraphErrors);
        horizontalLayout.setAlignItems(Alignment.AUTO);
        horizontalLayout.setWidthFull();
        horizontalLayout.setHeight("60%");
        horizontalLayout.setPadding(true);
        horizontalLayout.setSpacing(true);
        add(cardEditor, horizontalLayout);
    }

    private void countAndDraw(Equestion equestion) {
        equestion.findSolutionAndError();

        updateCharts(equestion);
        if(!hasResizeListener) {
            hasResizeListener = true;
            UI.getCurrent().getPage().addBrowserWindowResizeListener(event-> updateCharts(equestion));
        }
    }

    private void updateCharts(Equestion equestion) {
        if(cardGraphSolution.getContent()!=null) {
            cardGraphSolution.getContent().removeAll();
        }

        Chart chartSolution = new Chart(equestion.getXs(), equestion.getTs());
        Label xN = new Label("xN = " + equestion.getXs()[equestion.getXs().length - 1]);
        xN.setWidthFull();
        HorizontalLayout xNlayout = new HorizontalLayout(xN);
        xNlayout.setWidthFull();
        cardGraphSolution.add(xNlayout,chartSolution.getSoChart());
        cardGraphSolution.setVisible(true);

        if(cardGraphErrors.getContent()!=null) {
            cardGraphErrors.getContent().removeAll();
        }

        Chart chartError = new Chart(equestion.getErrors(), equestion.getSteps(), equestion.getErrorMin(), equestion.getErrorMax());
        Label ePn = new Label("eПN = " + equestion.getErrors()[equestion.getErrors().length - 1]);
        ePn.setWidth("33%");
        ePn.getElement().getStyle()
                .set("overflow","hidden")
                .set("white-space","nowrap")
                .set("text-overflow","ellipsis");
        Label eMin = new Label("ErrorMin = " + equestion.getErrorMin());
        eMin.setWidth("33%");
        eMin.getElement().getStyle()
                .set("overflow","hidden")
                .set("white-space","nowrap")
                .set("text-overflow","ellipsis");
        Label eMax = new Label("ErrorMax = " + equestion.getErrorMax());
        eMax.setWidth("33%");
        eMax.getElement().getStyle()
                .set("overflow","hidden")
                .set("white-space","nowrap")
                .set("text-overflow","ellipsis");
        HorizontalLayout errors = new HorizontalLayout(ePn,eMin,eMax);
        errors.setWidthFull();
        cardGraphErrors.add(errors,chartError.getSoChart());
        cardGraphErrors.setVisible(true);
    }
}
