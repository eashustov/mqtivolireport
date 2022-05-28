package ru.sberbank.uspincidentreport.view;

import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ToolbarBuilder;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.grid.builder.RowBuilder;
import com.github.appreciated.apexcharts.config.legend.HorizontalAlign;
import com.github.appreciated.apexcharts.config.legend.Position;
import com.github.appreciated.apexcharts.config.markers.builder.HoverBuilder;
import com.github.appreciated.apexcharts.config.plotoptions.builder.PieBuilder;
import com.github.appreciated.apexcharts.config.plotoptions.pie.builder.*;
import com.github.appreciated.apexcharts.config.responsive.builder.OptionsBuilder;
import com.github.appreciated.apexcharts.config.stroke.Curve;
import com.github.appreciated.apexcharts.config.subtitle.Align;
import com.github.appreciated.apexcharts.helper.Series;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataCountPerMonth;
import ru.sberbank.uspincidentreport.repo.UspIncidentDataCountPerMonthRepo;
import ru.sberbank.uspincidentreport.repo.UspIncidentDataTotalCountRepo;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.valueOf;

@Route(value = "analitics")
@PageTitle("Аналитика автоинцидентов УСП за период")
public class Analitics extends VerticalLayout {
    private H4 header;
    ApexCharts donutChart;
    ApexCharts lineChart;
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
    private UspIncidentDataCountPerMonthRepo dataCountPerMonthRepo;


    public Analitics(UspIncidentDataTotalCountRepo dataTotalCountRepo, UspIncidentDataCountPerMonthRepo dataCountPerMonthRepo) {
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
        end_Date.setMin(start_Date.getValue());
        start_Date.setMax(end_Date.getValue());
        startDate = start_Date.getValue().format(europeanDateFormatter) + "00.00.00";
        endDate = end_Date.getValue().format(europeanDateFormatter) + "23.59.59";
        this.dataTotalCountRepo = dataTotalCountRepo;
        this.dataCountPerMonthRepo = dataCountPerMonthRepo;
        getTotalCountAnaliticsData(start_Date,end_Date);
        this.donutChart = donutChartInit(seriesData,labelsData);
        this.lineChart = LineChartInit ();
        getTotalCounPerMonthAnaliticsData(start_Date,end_Date);


        //Кнопка запроса аналитики
        Button buttonQuery = new Button();
        buttonQuery.setText("Запрос данных");


        //Отображение. Добавление компонентов
//        VerticalLayout dateLayout = new VerticalLayout(start_Date, end_Date, buttonQuery);
        HorizontalLayout dateLayout = new HorizontalLayout(start_Date, end_Date, buttonQuery);
        dateLayout.setVerticalComponentAlignment(Alignment.END, start_Date, end_Date, buttonQuery);
        setHorizontalComponentAlignment(Alignment.CENTER, dateLayout);
        HorizontalLayout horizontalLayout = new HorizontalLayout(donutChart, lineChart);
        add(header, dateLayout, horizontalLayout);

        //Обработчик кнопки
        buttonQuery.addClickListener(clickEvent -> {
            getTotalCountAnaliticsData(start_Date,end_Date);
            horizontalLayout.removeAll();
            donutChart = donutChartInit(seriesData,labelsData);
            donutChart.setLegend(LegendBuilder.get()
                    .withPosition(Position.right)
                    .withFontSize("15")
                    .withOffsetX(200.0)
                    .build());
            horizontalLayout.add(donutChart, lineChart);
            horizontalLayout.setVerticalComponentAlignment(Alignment.START, donutChart, lineChart);
            setHorizontalComponentAlignment(Alignment.END, horizontalLayout);

        });


    }

    private ApexCharts donutChartInit(List<Double>seriesData, List<String>labelsData ){
        ApexCharts donutChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.donut)
                        .withZoom(ZoomBuilder.get()
                                .withEnabled(true)
                                .build())
                        .withToolbar(ToolbarBuilder.get()
                                .withShow(true)
                                .build())
                        .withOffsetX(-300.0)
                        .build())
                .withTitle(TitleSubtitleBuilder.get()
                        .withText("Количество автоинцидентов за период")
                        .withAlign(Align.center)
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
                        .withHorizontalAlign(HorizontalAlign.right)
                        .withFloating(true)
                        .withFontSize("15")
                        .withOffsetX(-30.0)
                        .withOffsetY(-30.0)
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

        donutChart.setWidth("1000");
        donutChart.setHeight("600");

        return donutChart;
    }

    @SneakyThrows
    private void getTotalCountAnaliticsData(DatePicker start_Date, DatePicker end_Date){
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

    private void getTotalCounPerMonthAnaliticsData(DatePicker start_Date, DatePicker end_Date){
//        String assignmentGroup = Files.readString(Paths.get("usp_incident_assignmentGroup.txt"));
        Map<String,Map<String, Integer>> assignmentMapToMonthData = new HashMap<>();
//        Map<Integer,Map<String, Integer>> assignmentMapToMonthData = new HashMap<>();
        Map<String, Integer> monthYearCountInc = new HashMap<>();
        startDate = start_Date.getValue().format(europeanDateFormatter);
        endDate = end_Date.getValue().format(europeanDateFormatter);
//        String assignmentGroupPrev = "";
        List<String> assignmentGroupExecute = new ArrayList<>();

        List<IUspIncidentDataCountPerMonth> TotalCounPerMonthAnaliticsData = dataCountPerMonthRepo.findIncCountPerMonth(startDate, endDate);

                ListIterator<IUspIncidentDataCountPerMonth> totalCounPerMonthAnaliticsDataIter = TotalCounPerMonthAnaliticsData.listIterator();
        while(totalCounPerMonthAnaliticsDataIter.hasNext()){
            monthYearCountInc.clear();
            String assignmentGroup = totalCounPerMonthAnaliticsDataIter.next().getAssignment();


            if (!assignmentGroupExecute.contains(assignmentGroup)) {

                for (IUspIncidentDataCountPerMonth e:TotalCounPerMonthAnaliticsData) {
                    if(e.getAssignment().equals(assignmentGroup)) {
                        String year = e.getYear();
                        String month = e.getMonth();
                        Integer countInc = e.getCountInc();
                        monthYearCountInc.put(year + " " + month, countInc);
                    }
                }

                assignmentGroupExecute.add(assignmentGroup);

                System.out.println(assignmentGroup);
                System.out.println(monthYearCountInc);
                System.out.println(assignmentGroupExecute.toString()+  " Список добавленных");

            } else {
                continue;
            }
            assignmentMapToMonthData.put(assignmentGroup, new HashMap<String, Integer>(monthYearCountInc));
            System.out.println(assignmentMapToMonthData);

        }

    }

    private ApexCharts LineChartInit (){
        ApexCharts lineChart = ApexChartsBuilder.get()
            .withChart(ChartBuilder.get()
                    .withType(Type.line)
                    .withZoom(ZoomBuilder.get()
                            .withEnabled(true)
                            .build())
                    .build())
            .withStroke(StrokeBuilder.get()
                    .withCurve(Curve.straight)
                    .build())
            .withMarkers(MarkersBuilder.get()
                    .withSize(1.0, 1.0)
                    .withHover(HoverBuilder.get().build())
                    .build())
            .withTitle(TitleSubtitleBuilder.get()
                    .withText("Динамика количества автоинцидентов по месяцам")
                    .withAlign(Align.center)
                    .build())
            .withGrid(GridBuilder.get()
                    .withRow(RowBuilder.get()
                            .withColors("#f3f3f3", "transparent")
                            .withOpacity(0.5).build()
                    ).build())
            .withXaxis(XAxisBuilder.get()
                    .withCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep")
                    .build())
            .withSeries(new Series<>("Компьютеры",20.0, 31.0, 45.0, 61.0, 29.0, 92.0, 39.0, 51.0, 248.0),
                        new Series<>("Desktops", 10.0, 41.0, 35.0, 51.0, 49.0, 62.0, 69.0, 91.0, 148.0))
            .build();
        lineChart.setWidth("1000");
        lineChart.setHeight("600");

        return lineChart;
    }

}



