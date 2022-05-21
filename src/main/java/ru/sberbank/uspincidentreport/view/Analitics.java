package ru.sberbank.uspincidentreport.view;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.ChartBuilder;
import com.github.appreciated.apexcharts.config.builder.LegendBuilder;
import com.github.appreciated.apexcharts.config.builder.PlotOptionsBuilder;
import com.github.appreciated.apexcharts.config.builder.ResponsiveBuilder;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.plotoptions.builder.PieBuilder;
import com.github.appreciated.apexcharts.config.plotoptions.pie.builder.*;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;
import ru.sberbank.uspincidentreport.repo.UspIncidentCountRepo;
import ru.sberbank.uspincidentreport.repo.UspIncidentRepo;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route(value = "analitics")
@PageTitle("Аналитика автоинцидентов УСП за период")
public class Analitics extends VerticalLayout {
    private H4 header;

    public Analitics() {
        this.header = new H4("Аналитика автоинцидентов УСП за период");
        setHorizontalComponentAlignment(Alignment.CENTER, header);
        ApexCharts donutChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.donut)
                        .withOffsetX(-300.0)
                        .build())
                .withPlotOptions(PlotOptionsBuilder.get().withPie(PieBuilder.get()
                        .withDonut(DonutBuilder.get()
                                .withLabels(LabelsBuilder.get()
                                        .withShow(true)
                                        .withName(NameBuilder.get().withShow(true).build())
                                        .withTotal(TotalBuilder.get().withShow(true).withLabel("Всего автоинцидентов за период").build())
                                        .build())
                                .build())
                        .build())
                        .build())
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.right)
                        .withFontSize("15")
                        .withOffsetX(-10.0)
                        .build())
                .withSeries(44.0, 55.0, 41.0, 17.0, 15.0, 14.0, 65.0)
                .withLabels("Первый", "ЦИ ОАСП Системы очередей сообщений", "Третий", "Четвертый", "Пятый", "ЦИ ОАСП Системы очередей сообщений", "ЦИ ОАСП Системы очередей сообщений")
                .withResponsive(ResponsiveBuilder.get()
                        .withBreakpoint(480.0)
                        .withOptions(OptionsBuilder.get()
                                .withLegend(LegendBuilder.get()
                                        .withPosition(Position.bottom)
                                        .build())
                                .build())
                        .build())
                .build();
        donutChart.setWidth("1300");
        donutChart.setHeight("500");
        donutChart.setVisible(false);
        donutChart.setVisible(true);

        //Выбор даты

        DatePicker startDate = new DatePicker("Начало");
        DatePicker endDate = new DatePicker("Конец");
        startDate.addValueChangeListener(e -> endDate.setMin(e.getValue()));
        endDate.addValueChangeListener(e -> startDate.setMax(e.getValue()));
        VerticalLayout dateLayout = new VerticalLayout(startDate, endDate);

        //Отображение. Добавление компонентов
        HorizontalLayout horizontalLayout = new HorizontalLayout(donutChart, dateLayout);
        add(header, horizontalLayout);


    }

}



