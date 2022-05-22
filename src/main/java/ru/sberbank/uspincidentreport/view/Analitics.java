package ru.sberbank.uspincidentreport.view;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;
import ru.sberbank.uspincidentreport.repo.UspIncidentCountRepo;
import ru.sberbank.uspincidentreport.repo.UspIncidentRepo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Route(value = "analitics")
@PageTitle("Аналитика автоинцидентов УСП за период")
public class Analitics extends VerticalLayout {
    private H4 header;
    ApexCharts donutChart;
    String startDate;
    String endDate;
    @Autowired
    private static UspIncidentCountRepo repo;
    List<String> labelsData;
    List<Double> seriesData;



    public Analitics() {
        this.header = new H4("Аналитика автоинцидентов УСП за период");
        setHorizontalComponentAlignment(Alignment.CENTER, header);


        //Выбор даты
        LocalDate now = LocalDate.now(ZoneId.systemDefault());
        DatePicker start_Date = new DatePicker("Начало");
        DatePicker end_Date = new DatePicker("Конец");
        end_Date.setMax(now);
        end_Date.setValue(now);
        start_Date.setMax(now);
        start_Date.setValue(now.minusMonths(1));
        start_Date.addValueChangeListener(e -> end_Date.setMin(e.getValue()));
        end_Date.addValueChangeListener(e -> start_Date.setMax(e.getValue()));
        startDate = start_Date.getValue().toString();
        endDate = end_Date.getValue().toString();

        //Кнопка запроса аналитики
        Button buttonQuery = new Button();
        buttonQuery.setText("Запрос данных");
        buttonQuery.addClickListener(clickEvent -> {
            getAnaliticsData();
            donutChart.resetSeries(true,false);
            donutChart.setLabels(String.valueOf(labelsData));
            donutChart.setSeries(Double.valueOf(String.valueOf(seriesData)));
            donutChart.render();
        });

        //Отображение. Добавление компонентов
        VerticalLayout dateLayout = new VerticalLayout(start_Date, end_Date, buttonQuery);
        HorizontalLayout horizontalLayout = new HorizontalLayout(donutChartInit(), dateLayout);
        add(header, horizontalLayout);


    }

    private ApexCharts donutChartInit(){
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
        return donutChart;
    }

    private void getAnaliticsData(){
        ObjectMapper oMapper = new ObjectMapper();
        Map<String, Integer> AnaliticsData = oMapper.convertValue(repo.findIncCount(),Map.class);
        List<String> labelsData = AnaliticsData.entrySet().stream()
                .map(a -> a.getKey()).collect(Collectors.toList());

        List<Double> seriesData = AnaliticsData.entrySet().stream()
                .map(a -> a.getValue().doubleValue())
                .collect(Collectors.toList());

        }


}



