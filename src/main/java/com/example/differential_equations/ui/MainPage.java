package com.example.differential_equations.ui;


import com.example.differential_equations.entity.Chart;
import com.example.differential_equations.entity.Equestion;
import com.github.appreciated.card.Card;
import com.storedobject.chart.Text;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;




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
        Editor editor = new Editor();
        Card cardEditor = new Card();
        cardEditor.setSizeFull();
        cardEditor.add(editor);
        cardEditor.setWidth("100%");
        editor.setAction(this::countAndDraw);

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
        Label xN = new Label("xN = " + equestion.getXs()[equestion.getXs().length - 1]);
        xN.setWidthFull();
        cardGraphSolution.add(xN,chartSolution.getSoChart());
        cardGraphSolution.setVisible(true);

        if(cardGraphErrors.getContent()!=null) {
            cardGraphErrors.getContent().removeAll();
        }

        Chart chartError = new Chart(equestion.getErrors(), equestion.getSteps(), equestion.getErrorMin(), equestion.getErrorMax());
        Label ePn = new Label("eПN = " + equestion.getErrors()[equestion.getErrors().length - 1]);
        ePn.setWidthFull();
        Label eMin = new Label("ErrorMin = " + equestion.getErrorMin());
        eMin.setWidthFull();
        Label eMax = new Label("ErrorMax = " + equestion.getErrorMax());
        eMax.setWidthFull();
        HorizontalLayout errors = new HorizontalLayout(ePn,eMin,eMax);
        errors.setWidthFull();
        cardGraphErrors.add(errors,chartError.getSoChart());
        cardGraphErrors.setVisible(true);
    }
}
