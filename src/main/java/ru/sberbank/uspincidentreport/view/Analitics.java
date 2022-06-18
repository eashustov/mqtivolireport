package ru.sberbank.uspincidentreport.view;
import com.github.appreciated.apexcharts.ApexCharts;
import com.github.appreciated.apexcharts.ApexChartsBuilder;
import com.github.appreciated.apexcharts.config.builder.*;
import com.github.appreciated.apexcharts.config.chart.Type;
import com.github.appreciated.apexcharts.config.chart.builder.ToolbarBuilder;
import com.github.appreciated.apexcharts.config.chart.builder.ZoomBuilder;
import com.github.appreciated.apexcharts.config.chart.toolbar.Tools;
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
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataCountPerMonth;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;
import ru.sberbank.uspincidentreport.repo.UspIncidentDataCountPerMonthRepo;
import ru.sberbank.uspincidentreport.repo.UspIncidentDataTotalCountRepo;
import ru.sberbank.uspincidentreport.repo.UspIncidentAnaliticsRepo;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    TreeSet<String>labels = new TreeSet<>();
    private Grid<UspIncidentData> grid_analitics;
    private GridListDataView<UspIncidentData> dataView_analitics;

    DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    List<String> labelsData;
    List<Double> seriesData;
    @Autowired
    private UspIncidentDataTotalCountRepo dataTotalCountRepo;
    @Autowired
    private UspIncidentDataCountPerMonthRepo dataCountPerMonthRepo;
    @Autowired
    private UspIncidentAnaliticsRepo repoAnalitics;

    private Map<String,Map<String, Integer>> assignmentGroupMapToMonthData;

    private RadioButtonGroup<String> typeAnaliticsSelect = new RadioButtonGroup<>();

//    String assignmentGroup = readString(Paths.get("/home/eshustov/IdeaProjects/usp_incident_assignmentGroup.txt"));

    public Analitics(UspIncidentDataTotalCountRepo dataTotalCountRepo, UspIncidentDataCountPerMonthRepo dataCountPerMonthRepo, UspIncidentAnaliticsRepo repoAnalitics) throws IOException {
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
        startDate = start_Date.getValue().format(europeanDateFormatter) + " 00:00:00";
        endDate = end_Date.getValue().format(europeanDateFormatter) + " 23:59:59";
        this.dataTotalCountRepo = dataTotalCountRepo;
        this.dataCountPerMonthRepo = dataCountPerMonthRepo;
        this.repoAnalitics = repoAnalitics;
        getTotalCountAnaliticsData(start_Date,end_Date);
        this.assignmentGroupMapToMonthData = getTotalCounPerMonthAnaliticsData(start_Date,end_Date);
        this.donutChart = donutChartInit(seriesData,labelsData);
        this.lineChart = LineChartInit();



        //Кнопка запроса аналитики
        Button buttonQuery = new Button();
        buttonQuery.setText("Запрос данных");

        //Anchor block
        Anchor downloadToCSV = new Anchor ((exporttoCSV(start_Date,end_Date)), "Сохранить в CSV" );
        Button buttonDownloadCSV = new Button(new Icon(VaadinIcon.DOWNLOAD));
        buttonDownloadCSV.setText("Сохранить в CSV");
        buttonDownloadCSV.setEnabled(false);
//        buttonDownloadCSV.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON);
        downloadToCSV.removeAll();
        downloadToCSV.add(buttonDownloadCSV);

        //Отображение. Добавление компонентов
        //Выбор типа аналитики
        typeAnaliticsSelect.addThemeVariants(RadioGroupVariant.LUMO_VERTICAL);
        typeAnaliticsSelect.setLabel("");
        typeAnaliticsSelect.setItems("по группам сопровождения", "по ИТ-услуге");
        typeAnaliticsSelect.setValue("по группам сопровождения");

        HorizontalLayout dateLayout = new HorizontalLayout(typeAnaliticsSelect, start_Date, end_Date, buttonQuery, downloadToCSV );
        dateLayout.setVerticalComponentAlignment(Alignment.END, typeAnaliticsSelect, start_Date, end_Date, buttonQuery, downloadToCSV);
        setHorizontalComponentAlignment(Alignment.CENTER, dateLayout);

        FormLayout formLayout = new FormLayout();
        formLayout.add(donutChart, lineChart);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        formLayout.setSizeUndefined();

        add(header, dateLayout);

        //Обработчик кнопки
        buttonQuery.addClickListener(clickEvent -> {
            formLayout.removeAll();
            remove(formLayout);
            getTotalCountAnaliticsData(start_Date,end_Date);
            assignmentGroupMapToMonthData = getTotalCounPerMonthAnaliticsData(start_Date,end_Date);
            lineChart = LineChartInit();
            donutChart = donutChartInit(seriesData,labelsData);
            donutChart.setMaxWidth("100%");
            donutChart.setWidth("900px");
            donutChart.setMaxHeight("100%");
            donutChart.setHeight("600px");
            formLayout.add(donutChart, lineChart);
            formLayout.setSizeUndefined();
            add(formLayout);
            buttonDownloadCSV.setEnabled(true);

        });

        //Обработчик выбора типа аналитики
        typeAnaliticsSelect.addValueChangeListener(changeEvent -> {
            buttonQuery.focus();
        });
    }

    private ApexCharts donutChartInit(List<Double>seriesData, List<String>labelsData ){
        String periodDate = start_Date.getValue().format(europeanDateFormatter) + " - " + end_Date.getValue().format(europeanDateFormatter);
        ApexCharts donutChart = ApexChartsBuilder.get()
                .withChart(ChartBuilder.get().withType(Type.donut)
                        .withZoom(ZoomBuilder.get()
                                .withEnabled(true)
                                .withAutoScaleYaxis(true)
                                .build())
                        .withToolbar(ToolbarBuilder.get()
                                .withShow(true)
                                .withTools(new Tools())
                                .build())
//                        .withOffsetX(-100.0)
                        .withOffsetY(-30.0) //-30 Это смешение вверх
//                        .withWidth("700px")
//                        .withHeight("400px")
                        .build())
                .withTitle(TitleSubtitleBuilder.get()
                        .withText("Количество автоинцидентов за период " + periodDate)
                        .withAlign(Align.center)
                        .build())
                .withPlotOptions(PlotOptionsBuilder.get().withPie(PieBuilder.get()
                        .withDonut(DonutBuilder.get()
                                .withLabels(LabelsBuilder.get()
                                        .withShow(true)
                                        .withName(NameBuilder.get().withShow(true).build())
                                        .withTotal(TotalBuilder.get().withShow(true).withLabel("Всего за период").build())
                                        .build())
                                .build())
                        .build())
                        .build())
                .withLegend(LegendBuilder.get()
                        .withPosition(Position.bottom)
                        .withHorizontalAlign(HorizontalAlign.center)
//                        .withHeight(10.0)
//                        .withFloating(true)
//                        .withFontSize("15")
//                        .withOffsetX(0.0)
                        .withOffsetY(5.0)
                        .build())
//                .withSeries(44.0, 55.0, 41.0, 17.0, 15.0, 14.0, 65.0)
                .withSeries(seriesData.stream().toArray(Double[]::new))
                .withLabels(labelsData.stream().toArray(String[]::new))
                .withResponsive(ResponsiveBuilder.get()
                        .withBreakpoint(480.0)
                        .withOptions(OptionsBuilder.get()
                                .withLegend(LegendBuilder.get()
                                        .withPosition(Position.bottom)
                                        .build())
                                .build())
                        .build())
                .build();

        donutChart.setColors("#FF0000", "#800000", "#FF8C00", "#808000", "#00FF00", "#008000",
                "#00FFFF", "#008080", "#0000FF", "#000080", "#800080", "#FF00FF", "#808080", "#000000");
        donutChart.setMaxWidth("100%");
        donutChart.setWidth("900px");
        donutChart.setMaxHeight("100%");
        donutChart.setHeight("600px");

        return donutChart;
    }

    private ApexCharts LineChartInit (){
        String periodDate = start_Date.getValue().format(europeanDateFormatter) + " - " + end_Date.getValue().format(europeanDateFormatter);
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
                        .withText("Динамика автоинцидентов по месяцам за период " + periodDate)
                        .withAlign(Align.center)
                        .build())
                .withGrid(GridBuilder.get()
                        .withRow(RowBuilder.get()
                                .withColors("#f3f3f3", "transparent")
                                .withOpacity(0.5).build()
                        ).build())
                .withXaxis(XAxisBuilder.get()
                        .withCategories(new ArrayList<String>(labels))
//                    .withCategories("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep")
                        .build())

                .withSeries(SetSeries(assignmentGroupMapToMonthData).stream().toArray(Series[]::new))

//                    new Series<>("Компьютеры",20.0, 31.0, 45.0, 61.0, 29.0, 92.0, 39.0, 51.0, 248.0),
//                    new Series<>("Desktops", 10.0, 41.0, 35.0, 51.0, 49.0, 62.0, 69.0, 91.0, 148.0))
                .build();
        lineChart.setColors("#FF0000", "#800000", "#FF8C00", "#808000", "#00FF00", "#008000",
                "#00FFFF", "#008080", "#0000FF", "#000080", "#800080", "#FF00FF", "#808080", "#000000");
        lineChart.setMaxWidth("100%");
        lineChart.setWidth("900px");
        lineChart.setMaxHeight("100%");
        lineChart.setHeight("600px");
//        lineChart.render();

        return lineChart;
    }

    @SneakyThrows
    private void getTotalCountAnaliticsData(DatePicker start_Date, DatePicker end_Date){
//        String assignmentGroup = Files.readString(Paths.get("usp_incident_assignmentGroup.txt"));
        startDate = start_Date.getValue().format(europeanDateFormatter) + " 00:00:00";
        endDate = end_Date.getValue().format(europeanDateFormatter) + " 23:59:59";

        if (typeAnaliticsSelect.getValue() == "по группам сопровождения") {
//        seriesData = dataTotalCountRepo.findIncByAssignmentCount(startDate, endDate, assignmentGroup)
            seriesData = dataTotalCountRepo.findIncByAssignmentCount(startDate, endDate)
                    .stream()
                    .map(t -> t.getCount_Inc().doubleValue())
                    .collect(Collectors.toList());

//        labelsData = dataTotalCountRepo.findIncByAssignmentCount(startDate, endDate, assignmentGroup)
            labelsData = dataTotalCountRepo.findIncByAssignmentCount(startDate, endDate)
                    .stream()
                    .map(t -> t.getHPC_Assignment())
                    .collect(Collectors.toList());
        } else {
            seriesData = dataTotalCountRepo.findIncByAffectedItemCount(startDate, endDate)
                    .stream()
                    .map(t -> t.getCount_Inc().doubleValue())
                    .collect(Collectors.toList());

            labelsData = dataTotalCountRepo.findIncByAffectedItemCount(startDate, endDate)
                    .stream()
                    .map(t -> t.getAffected_Item())
                    .map(t->t
                            .replace("CI02021291", "Услуга 1")
                            .replace("CI02021299", "Услуга 2")
                            .replace("CI01563053", "Услуга 3")
                            .replace("April", "Услуга 4")
                            .replace("May", "Услуга 5")
                            .replace("June", "Услуга 6")
                            .replace("July", "Услуга 7")
                            .replace("August", "Услуга 8")
                            .replace("September","Услуга 9")
                            .replace("October","Услуга 10")
                            .replace("November", "Услуга 11")
                            .replace("December", "Услуга 12"))
                    .collect(Collectors.toList());
        }

    }

    private Map<String,Map<String, Integer>> getTotalCounPerMonthAnaliticsData(DatePicker start_Date, DatePicker end_Date){
//        String assignmentGroup = Files.readString(Paths.get("usp_incident_assignmentGroup.txt"));
        Map<String,Map<String, Integer>> valueMapToMonthData = new HashMap<>();
        Map<String, Integer> monthYearCountInc = new HashMap<>();
        startDate = start_Date.getValue().format(europeanDateFormatter) + " 00:00:00";
        endDate = end_Date.getValue().format(europeanDateFormatter) + " 23:59:59";
        List<String> itemExecute = new ArrayList<>();

        //По группе сопровождения
        if (typeAnaliticsSelect.getValue() == "по группам сопровождения") {
            List<IUspIncidentDataCountPerMonth> TotalCounPerMonthAnaliticsData = dataCountPerMonthRepo.findIncAssignmentCountPerMonth(startDate, endDate);

            ListIterator<IUspIncidentDataCountPerMonth> totalCounPerMonthAnaliticsDataIter = TotalCounPerMonthAnaliticsData.listIterator();
            while(totalCounPerMonthAnaliticsDataIter.hasNext()){
                monthYearCountInc.clear();
                String assignmentGroup = totalCounPerMonthAnaliticsDataIter.next().getHPC_Assignment();


                if (!itemExecute.contains(assignmentGroup)) {

                    for (IUspIncidentDataCountPerMonth e:TotalCounPerMonthAnaliticsData) {
                        if(e.getHPC_Assignment().equals(assignmentGroup)) {
                            String year = e.getYear();
                            String month = e.getMonth_Number();
//                        String month = e.getMonth()
//                                .replace("January", "1")
//                                .replace("February", "2")
//                                .replace("March", "3")
//                                .replace("April", "4")
//                                .replace("May", "5")
//                                .replace("June", "6")
//                                .replace("July", "7")
//                                .replace("August", "8")
//                                .replace("September","9")
//                                .replace("October","10")
//                                .replace("November", "11")
//                                .replace("December", "12");
                            Integer countInc = e.getCount_Inc();
                            monthYearCountInc.put(year + " " + month, countInc);
                        }
                    }

                    itemExecute.add(assignmentGroup);

//                System.out.println(assignmentGroup);
//
//                System.out.println(monthYearCountInc);
//                System.out.println(itemExecute.toString()+  " Список добавленных");

                } else {
                    continue;
                }
                valueMapToMonthData.put(assignmentGroup, new TreeMap<String, Integer>(monthYearCountInc));
//            System.out.println(valueMapToMonthData);

            }
//        System.out.println(valueMapToMonthData);

            //По ИТ услуге
        } else {
            List<IUspIncidentDataCountPerMonth> TotalCounPerMonthAnaliticsData = dataCountPerMonthRepo.findIncAffectedItemCountPerMonth(startDate, endDate);
            ListIterator<IUspIncidentDataCountPerMonth> totalCounPerMonthAnaliticsDataIter = TotalCounPerMonthAnaliticsData.listIterator();
            while (totalCounPerMonthAnaliticsDataIter.hasNext()) {
                monthYearCountInc.clear();
                String affectedItem = totalCounPerMonthAnaliticsDataIter.next().getAffected_Item();

                if (!itemExecute.contains(affectedItem)) {

                    for (IUspIncidentDataCountPerMonth e : TotalCounPerMonthAnaliticsData) {
                        if (e.getAffected_Item().equals(affectedItem)) {
                            String year = e.getYear();
                            String month = e.getMonth_Number();
                            Integer countInc = e.getCount_Inc();
                            monthYearCountInc.put(year + " " + month, countInc);
                        }
                    }

                    affectedItem = affectedItem
                            .replace("CI02021291", "Услуга 1")
                            .replace("CI02021299", "Услуга 2")
                            .replace("CI01563053", "Услуга 3")
                            .replace("April", "Услуга 4")
                            .replace("May", "Услуга 5")
                            .replace("June", "Услуга 6")
                            .replace("July", "Услуга 7")
                            .replace("August", "Услуга 8")
                            .replace("September","Услуга 9")
                            .replace("October","Услуга 10")
                            .replace("November", "Услуга 11")
                            .replace("December", "Услуга 12");

                    itemExecute.add(affectedItem);

                } else {
                    continue;
                }
                valueMapToMonthData.put(affectedItem, new TreeMap<String, Integer>(monthYearCountInc));
            }
        }

        //Определение временной шкаолы - Labels

        List<Set<String>> alllabels;

        alllabels = valueMapToMonthData.entrySet()
                .stream()
                .map(e-> e.getValue())
                .map(e->e.keySet())
                .collect(Collectors.toList());

        alllabels.stream()
                .forEach(l-> {
                    for (String dataLabel:l) {

                        labels.add(dataLabel);

                    }
                });


//        System.out.println("Временная шкала: " + labels);

        //Определение максимального колиичества значений из всех групп.

//        int maxData=0;
//        int dataCount;
//
//        for (String key : valueMapToMonthData.keySet()) {
//            dataCount = valueMapToMonthData.get(key).size();
//            if (dataCount > maxData) maxData = dataCount;

//            System.out.println(key + ":" + valueMapToMonthData.get(key).size());
//        }
//        System.out.println("Максимальное количество " + maxData);


        //Определение и форматирование данных для назначенных групп


        valueMapToMonthData.entrySet()
                .stream()
                .map(e-> e.getValue())
                .forEach(e->{
                    for (String key:labels) {
                        if (!e.containsKey(key))
                            e.put(key, 0);
                    }
                });
//        System.out.println(valueMapToMonthData);
        return valueMapToMonthData;

    }

    private List<Series> SetSeries (Map<String,Map<String, Integer>> valueMapToMonthData) {
        // Получение Series для данных групп
        List<Series> setSeries = new ArrayList<>();
        valueMapToMonthData.entrySet()
                .stream()
                .forEach(e->{
                    String seriesName = e.getKey();
                    List<Double> seriesData = e.getValue().entrySet()
                            .stream()
                            .map(z->z.getValue().doubleValue())
                            .collect(Collectors.toList());
                    setSeries.add(new Series<>(seriesName, seriesData.stream().toArray(Double[]::new)));
                });
        return setSeries;
    }

    private GridListDataView<UspIncidentData> initGridIncData (DatePicker start_Date, DatePicker end_Date){
        startDate = start_Date.getValue().format(europeanDateFormatter) + " 00:00:00";
        endDate = end_Date.getValue().format(europeanDateFormatter) + " 23:59:59";
        grid_analitics = new Grid<>(UspIncidentData.class, false);
//        dataView = grid.setItems(repo.findIncByDate(startDate, endDate, assignmentGroup ));
        dataView_analitics = grid_analitics.setItems(repoAnalitics.findIncByDate(startDate, endDate));
        return dataView_analitics;
    };

    private StreamResource exporttoCSV(DatePicker start_Date, DatePicker end_Date){
        //        Export to CSV
        var streamResource = new StreamResource("uspIncidents.csv",
                () -> {
                    Stream<UspIncidentData> uspIncidentList = initGridIncData (start_Date, end_Date).getItems();
                    StringWriter output = new StringWriter();
                    StatefulBeanToCsv<UspIncidentData> beanToCSV = null;
                    try {
                        beanToCSV = new StatefulBeanToCsvBuilder<UspIncidentData>(output)
                                .withIgnoreField(UspIncidentData.class, UspIncidentData.class.getDeclaredField("ACTION"))
                                .build();
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    }
                    try {
                        beanToCSV.write(uspIncidentList);
                        var contents = output.toString();
                        return new ByteArrayInputStream(contents.getBytes());
                    } catch (CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
                        e.printStackTrace();
                        return null;
                    }
                }
        );
        return streamResource;
    }

}



