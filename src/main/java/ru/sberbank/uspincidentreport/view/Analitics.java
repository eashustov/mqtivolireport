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
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.SneakyThrows;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataTotalCount;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;
import ru.sberbank.uspincidentreport.repo.UspIncidentDataCountPerMonthRepo;
import ru.sberbank.uspincidentreport.repo.UspIncidentDataTotalCountRepo;
import ru.sberbank.uspincidentreport.repo.UspIncidentRepo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Route(value = "analitics")
@PageTitle("Аналитика автоинцидентов УСП за период")
public class Analitics extends VerticalLayout {
    private H4 header;
    ApexCharts donutChart;
    String startDate;
    String endDate;
    DatePicker start_Date;
    DatePicker end_Date;
    DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    List<String> labelsData;
    List<Double> seriesData;
    @Autowired
    private UspIncidentDataTotalCountRepo dataTotalCountRepo;
    @Autowired
    private UspIncidentDataCountPerMonthRepo countPerMonthRepo;


    public Analitics(UspIncidentDataTotalCountRepo dataTotalCountRepo) {
        this.header = new H4("Аналитика автоинцидентов УСП за период");
        setHorizontalComponentAlignment(Alignment.CENTER, header);

        LocalDate now = LocalDate.now(ZoneId.systemDefault());
//        DatePicker.DatePickerI18n singleFormatI18n = new DatePicker.DatePickerI18n();
//        singleFormatI18n.setDateFormat("dd.MM.yyyy");
        start_Date = new DatePicker("Начало");
//        start_Date.setI18n(singleFormatI18n);
        end_Date = new DatePicker("Конец");
//        end_Date.setI18n(singleFormatI18n);
        end_Date.setMax(now);
        end_Date.setValue(now);
        start_Date.setMax(now);
        start_Date.setValue(now.minusMonths(1));
        start_Date.addValueChangeListener(e -> end_Date.setMin(e.getValue()));
        end_Date.addValueChangeListener(e -> start_Date.setMax(e.getValue()));
        startDate = start_Date.getValue().format(europeanDateFormatter);
        endDate = end_Date.getValue().format(europeanDateFormatter);
        this.dataTotalCountRepo = dataTotalCountRepo;
        getAnaliticsData();


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
//                .withSeries(44.0, 55.0, 41.0, 17.0, 15.0, 14.0, 65.0)
                .withSeries(seriesData.stream().toArray(Double[]::new))
                .withLabels(labelsData.stream().toArray(String[]::new))
//                .withLabels("Первый", "ЦИ ОАСП Системы очередей сообщений", "Третий", "Четвертый", "Пятый", "ЦИ ОАСП Системы очередей сообщений", "ЦИ ОАСП Системы очередей сообщений")
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

    @SneakyThrows
    private void getAnaliticsData(){
//        String assignmentGroup = Files.readString(Paths.get("usp_incident_assignmentGroup.txt"));
        startDate = start_Date.getValue().format(europeanDateFormatter);
        endDate = end_Date.getValue().format(europeanDateFormatter);

        seriesData = dataTotalCountRepo.findIncCount(startDate, endDate)
                .stream()
                .map(t -> t.getCountInc().doubleValue())
                .collect(Collectors.toList());

        labelsData = dataTotalCountRepo.findIncCount(startDate, endDate)
                .stream()
                .map(t -> t.getAssignment())
                .collect(Collectors.toList());

        }


}



