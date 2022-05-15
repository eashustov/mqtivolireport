package ru.sberbank.uspincidentreport.view;

import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sberbank.uspincidentreport.repo.UspIncidentRepo;

@Route(value = "analitics")
@PageTitle("Аналитика автоинцидентов УСП")
public class Analitics extends Div {
    private H4 header;
    @Autowired
    private UspIncidentRepo repo;

    public Analitics() {
        setText("Hello @Route!");
    }

}
