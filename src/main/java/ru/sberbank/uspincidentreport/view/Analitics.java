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
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.H5;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.radiobutton.RadioGroupVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataCountPerMonth;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataTop10;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;
import ru.sberbank.uspincidentreport.repo.UspIncidentDataCountPerMonthRepo;
import ru.sberbank.uspincidentreport.repo.UspIncidentDataTop10Repo;
import ru.sberbank.uspincidentreport.repo.UspIncidentDataTotalCountRepo;
import ru.sberbank.uspincidentreport.repo.UspIncidentAnaliticsRepo;
import ru.sberbank.uspincidentreport.service.ExporToCSV;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.function.Consumer;
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
    @Autowired
    private UspIncidentDataTop10Repo dataTop10IncRepo;

    private Map<String,Map<String, Integer>> assignmentGroupMapToMonthData;

    private RadioButtonGroup<String> typeAnaliticsSelect = new RadioButtonGroup<>();
    IncTop10Filter incTop10Filter;

//    String assignmentGroup = readString(Paths.get("/home/eshustov/IdeaProjects/usp_incident_assignmentGroup.txt"));

    public Analitics(UspIncidentDataTotalCountRepo dataTotalCountRepo, UspIncidentDataCountPerMonthRepo dataCountPerMonthRepo, UspIncidentAnaliticsRepo repoAnalitics,
                     UspIncidentDataTop10Repo dataTop10IncRepo) throws IOException {
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
        this.dataTop10IncRepo = dataTop10IncRepo;
        getTotalCountAnaliticsData(start_Date,end_Date);
        this.assignmentGroupMapToMonthData = getTotalCounPerMonthAnaliticsData(start_Date,end_Date);
        this.donutChart = donutChartInit(seriesData,labelsData);
        this.lineChart = LineChartInit();

        //Кнопка поиска
        TextField searchField = new TextField();
        searchField.getElement().setAttribute("aria-label", "search");
        searchField.setPlaceholder("Найти инцидент");
        searchField.setClearButtonVisible(true);
        searchField.setPrefixComponent(VaadinIcon.SEARCH.create());
//        searchField.setHelperText("Любое значение: Имя хоста, исполнитель, группа сопрровждения, ИТ-услуга");


        //Кнопка запроса аналитики
        Button buttonQuery = new Button();
        buttonQuery.setText("Запрос данных");

        //Anchor block
        Anchor downloadToCSV = new Anchor(ExporToCSV.exportToCSV(initGridIncData (start_Date,end_Date)), "Сохранить в CSV" );
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

        HorizontalLayout dateLayout = new HorizontalLayout(typeAnaliticsSelect, start_Date, end_Date, buttonQuery, downloadToCSV, searchField);
        dateLayout.setVerticalComponentAlignment(Alignment.END, typeAnaliticsSelect, start_Date, end_Date, buttonQuery, downloadToCSV, searchField);
        setHorizontalComponentAlignment(Alignment.CENTER, dateLayout);

        FormLayout formLayout = new FormLayout();
        formLayout.add(donutChart, lineChart);
        formLayout.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 2));
        formLayout.setSizeUndefined();

        add(header, dateLayout);

        //Обработчик поиска
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.setValueChangeTimeout(3000);
        searchField.addValueChangeListener(changeListener->{
            if (!searchField.getValue().equals(""))
            {
                search(start_Date,end_Date, searchField.getValue());
            }
        });
        searchField.addKeyPressListener(Key.ENTER, keyPressEvent -> search(start_Date,end_Date, searchField.getValue()));

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
            formLayout.add(donutChart, lineChart,top10IncGridInit());
            formLayout.setSizeUndefined();
            add(formLayout);
            buttonDownloadCSV.setEnabled(true);

        });

        //Обработчик выбора типа аналитики
        typeAnaliticsSelect.addValueChangeListener(changeEvent -> {
            buttonQuery.focus();
        });
    }

    //Метод диалога поиска инцидента
    private void search(DatePicker start_date, DatePicker end_date, String searchValue) {
        VerticalLayout searchLayout = new VerticalLayout();
        startDate = start_date.getValue().format(europeanDateFormatter) + " 00:00:00";
        endDate = end_date.getValue().format(europeanDateFormatter) + " 23:59:59";
        String periodDate = start_date.getValue().format(europeanDateFormatter) + " - " + end_date.getValue().format(europeanDateFormatter);

        //Создание диалога поиска
        Dialog dialog = new Dialog();
        dialog.setWidth("80%");
        dialog.setHeight("80%");
        //        dialog.setHeightFull();
        //        dialog.setWidthFull();
        dialog.setDraggable(true);
        dialog.setResizable(true);
        //Кнопка закрытия диалога поиска
        Button closeButton = new Button(new Icon("lumo", "cross"), (e) -> dialog.close());
        closeButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        setHorizontalComponentAlignment(Alignment.END, closeButton);

        Grid<UspIncidentData> searchGrid = new Grid<>(UspIncidentData.class, false);
        GridListDataView<UspIncidentData> searchDataView = searchGrid.setItems(repoAnalitics.findIncBySearchFilter(startDate,endDate,searchValue));

//        searchGrid.setAllRowsVisible(true); //Автоматическая высота таблицы в зависимости от количества строк
        searchGrid.setHeight("70%");
        searchGrid.setWidth("100%");
        searchGrid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);
        searchGrid.setColumnReorderingAllowed(true);
        //Create column for Grid

        Grid.Column<UspIncidentData> NUMBER = searchGrid
                .addColumn(UspIncidentData::getNUMBER).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Номер инцидента");
        Grid.Column<UspIncidentData> BRIEF_DESCRIPTION = searchGrid
                .addColumn(UspIncidentData::getBRIEF_DESCRIPTION).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Краткое описание");
        Grid.Column<UspIncidentData> PRIORITY_CODE = searchGrid
                .addColumn(UspIncidentData::getPRIORITY_CODE).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Важность");
        Grid.Column<UspIncidentData> OPEN_TIME = searchGrid
                .addColumn(UspIncidentData::getOPEN_TIME).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Время регистрации");
        Grid.Column<UspIncidentData> ASSIGNEE_NAME = searchGrid
                .addColumn(UspIncidentData::getHPC_ASSIGNEE_NAME).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Исполнитель");
        Grid.Column<UspIncidentData> ASSIGNMENT = searchGrid
                .addColumn(UspIncidentData::getHPC_ASSIGNMENT).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Назначен в группу");
        Grid.Column<UspIncidentData> STATUS = searchGrid
                .addColumn(UspIncidentData::getHPC_STATUS).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Статус");
        Grid.Column<UspIncidentData> HOST = searchGrid
                .addColumn(UspIncidentData::getHOST).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Сервер");
        Grid.Column<UspIncidentData> AFFECTED_ITEM = searchGrid
                .addColumn(UspIncidentData::getAFFECTED_ITEM).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("ИТ-услуга");


        // Вывод подробной информации по инциденту по выделению строки таблицы
        searchGrid.setItemDetailsRenderer(new ComponentRenderer<>(incident -> {
            VerticalLayout layout = new VerticalLayout();
            layout.add(new Label(incident.getACTION()));
            return layout;
        }));
        MainView.IncidentContextMenu searchGridContextMenu = new MainView.IncidentContextMenu(searchGrid);

        //Anchor block
        Anchor searchDownloadToCSV = new Anchor(ExporToCSV.exportToCSV(searchDataView), "Сохранить в CSV" );
        Button searchButtonDownloadCSV = new Button(new Icon(VaadinIcon.DOWNLOAD));
        searchButtonDownloadCSV.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON);
        searchDownloadToCSV.removeAll();
        searchDownloadToCSV.add(searchButtonDownloadCSV);
        setHorizontalComponentAlignment(Alignment.END, searchDownloadToCSV);

        Label searchHeader = new Label("Автоинциденты за период " + periodDate + " (" + searchDataView.getItemCount() + " шт.)");
        setHorizontalComponentAlignment(Alignment.CENTER, searchHeader);

        searchLayout.add(closeButton, searchHeader, searchDownloadToCSV);
        dialog.add(searchLayout, searchGrid, searchGridContextMenu, new Label("Найдено автоинцидентов: " + searchDataView.getItemCount()));
        dialog.open();

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
                                        .withTotal(TotalBuilder.get().withShow(true).withLabel("Всего").build())
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

    //Построение таблицы top 10 автоинцидентов
    private VerticalLayout top10IncGridInit() {
        String startDate = start_Date.getValue().format(europeanDateFormatter) + " 00:00:00";
        String endDate = end_Date.getValue().format(europeanDateFormatter) + " 23:59:59";
        //top 10 автоинцидентов вертикальная сетка
        VerticalLayout top10Inclayout = new VerticalLayout();
        H5 top10Header = new H5("Топ 10 серверов по количеству автоинцидентов за период " + start_Date.getValue().format(europeanDateFormatter) + " - " + end_Date.getValue().format(europeanDateFormatter));
        Grid<IUspIncidentDataTop10> top10IncGrid = new Grid<>(IUspIncidentDataTop10.class, false);

        GridListDataView<IUspIncidentDataTop10> top10IncDataView = top10IncGrid.setItems(dataTop10IncRepo.findTop10IncCount(startDate,endDate));

        top10IncGrid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);
        top10IncGrid.setColumnReorderingAllowed(true);

        //Create column for Grid

        Grid.Column<IUspIncidentDataTop10> AFFECTED_ITEM = top10IncGrid
                .addColumn(IUspIncidentDataTop10::getAffected_Item).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("ИТ-услуга");
        Grid.Column<IUspIncidentDataTop10> HOST = top10IncGrid
                .addColumn(IUspIncidentDataTop10::getHost).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Сервер");
        Grid.Column<IUspIncidentDataTop10> COUNT_INC = top10IncGrid
                .addColumn(IUspIncidentDataTop10::getCount_Inc).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Количество");

        top10IncGrid.setMaxWidth("100%");
        top10IncGrid.setWidth("900px");
        top10IncGrid.setMaxHeight("100%");
        top10IncGrid.setHeight("600px");

        //Создание фильтра для ИТ услуги
        incTop10Filter = new Analitics.IncTop10Filter(top10IncDataView);
        top10IncGrid.getHeaderRows().clear();
        HeaderRow headerRow = top10IncGrid.appendHeaderRow();
        headerRow.getCell(AFFECTED_ITEM)
                .setComponent(createFilterHeader("ИТ-услуга", incTop10Filter::setAffectedItem, top10IncDataView));


        //Anchor block

        Anchor top10IncDownloadToCSV = new Anchor(ExporToCSV.exportTop10ToCSV(dataTop10IncRepo, startDate,endDate), "Сохранить в CSV");
        Button top10IncButtonDownloadCSV = new Button(new Icon(VaadinIcon.DOWNLOAD));
        top10IncButtonDownloadCSV.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON);
        top10IncDownloadToCSV.removeAll();
        top10IncDownloadToCSV.add(top10IncButtonDownloadCSV);
        HorizontalLayout top10HeaderLayout = new HorizontalLayout(top10Header,top10IncDownloadToCSV);

        top10HeaderLayout.setVerticalComponentAlignment(Alignment.END, top10Header,top10IncDownloadToCSV);
        setHorizontalComponentAlignment(Alignment.CENTER, top10HeaderLayout);
        top10Inclayout.add(top10HeaderLayout, top10IncGrid);

        return top10Inclayout;
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
                            .replace("CI02021303", "IBM Connections")
                            .replace("CI02021304", "IBM WebSphere Portal")
                            .replace("CI02584076", "IBM HTTP Server")
                            .replace("CI02584077", "LDAP ADAM")
                            .replace("CI02584078", "Oracle Web Tier")
                            .replace("CI02021298", "Oracle Application Server BI")
                            .replace("CI02021301", "Платформа GridGain (native)")
                            .replace("CI02021292", "WildFly")
                            .replace("CI02021302", "Nginx")
                            .replace("CI02021294", "Oracle WebLogic Server")
                            .replace("CI02021296", "Oracle Siebel CRM")
                            .replace("CI02021299", "IBM WebSphere Application Server")
                            .replace("CI02021293", "IBM BPM – Pega")
                            .replace("CI02021295", "IBM FileNet Content Manager")
                            .replace("CI02192117", "Apache Kafka")
                            .replace("CI02021290", "IBM DataPower")
                            .replace("CI02021291", "IBM WebSphere MQ")
                            .replace("CI02021300", "Apache Zookeeper")
                            .replace("CI02192118", "SOWA")
                            .replace("CI02021306", "Сервисы интеграции приложений WebSphere (IBM App services)")
                            .replace("CI00737141", "Специализированные платформы серверов приложений (IBM Portal, Oracle Siebel CRM, Teradat, IBM FileNet)")
                            .replace("CI00737140", "Интеграционные платформы серверов приложений (WMQ, WMB, DataPower, Pega PRPC)")
                            .replace("CI00737137", "Стандартные платформы серверов приложений (WAS, WLS)")
                            .replace("CI02008623", "Мониторинг использования лицензий (МИЛИ)")
                            .replace("CI01563053", "Платформа управления контейнерами (Terra)"))
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
                            .replace("CI02021303", "IBM Connections")
                            .replace("CI02021304", "IBM WebSphere Portal")
                            .replace("CI02584076", "IBM HTTP Server")
                            .replace("CI02584077", "LDAP ADAM")
                            .replace("CI02584078", "Oracle Web Tier")
                            .replace("CI02021298", "Oracle Application Server BI")
                            .replace("CI02021301", "Платформа GridGain (native)")
                            .replace("CI02021292", "WildFly")
                            .replace("CI02021302", "Nginx")
                            .replace("CI02021294", "Oracle WebLogic Server")
                            .replace("CI02021296", "Oracle Siebel CRM")
                            .replace("CI02021299", "IBM WebSphere Application Server")
                            .replace("CI02021293", "IBM BPM – Pega")
                            .replace("CI02021295", "IBM FileNet Content Manager")
                            .replace("CI02192117", "Apache Kafka")
                            .replace("CI02021290", "IBM DataPower")
                            .replace("CI02021291", "IBM WebSphere MQ")
                            .replace("CI02021300", "Apache Zookeeper")
                            .replace("CI02192118", "SOWA")
                            .replace("CI02021306", "Сервисы интеграции приложений WebSphere (IBM App services)")
                            .replace("CI00737141", "Специализированные платформы серверов приложений (IBM Portal, Oracle Siebel CRM, Teradat, IBM FileNet)")
                            .replace("CI00737140", "Интеграционные платформы серверов приложений (WMQ, WMB, DataPower, Pega PRPC)")
                            .replace("CI00737137", "Стандартные платформы серверов приложений (WAS, WLS)")
                            .replace("CI02008623", "Мониторинг использования лицензий (МИЛИ)")
                            .replace("CI01563053", "Платформа управления контейнерами (Terra)");

                    itemExecute.add(affectedItem);

                } else {
                    continue;
                }
                valueMapToMonthData.put(affectedItem, new TreeMap<String, Integer>(monthYearCountInc));
            }
        }

        //Определение временной шкаолы - Labels

        labels.clear();

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

    private Component createFilterHeader(String labelText, Consumer<String> filterChangeConsumer, GridListDataView<IUspIncidentDataTop10> incTop10DataViewFiltered) {
        Label label = new Label(labelText);
        label.getStyle().set("padding-top", "var(--lumo-space-m)")
                .set("font-size", "var(--lumo-font-size-xs)");
        Map<String,String> affectedItemMap = new HashMap<>(){{
            put("CI02021304", "IBM WebSphere Portal");
            put("CI02584076", "IBM HTTP Server");
            put("CI02584077", "LDAP ADAM");
            put("CI02584078", "Oracle Web Tier");
            put("CI02021298", "Oracle Application Server BI");
            put("CI02021301", "Платформа GridGain (native)");
            put("CI02021292", "WildFly");
            put("CI02021302", "Nginx");
            put("CI02021294", "Oracle WebLogic Server");
            put("CI02021296", "Oracle Siebel CRM");
            put("CI02021299", "IBM WebSphere Application Server");
            put("CI02021293", "IBM BPM – Pega");
            put("CI02021295", "IBM FileNet Content Manager");
            put("CI02192117", "Apache Kafka");
            put("CI02021290", "IBM DataPower");
            put("CI02021291", "IBM WebSphere MQ");
            put("CI02021300", "Apache Zookeeper");
            put("CI02192118", "SOWA");
            put("CI02021306", "Сервисы интеграции приложений WebSphere (IBM App services)");
            put("CI00737141", "Специализированные платформы серверов приложений (IBM Portal, Oracle Siebel CRM, Teradat, IBM FileNet)");
            put("CI00737140", "Интеграционные платформы серверов приложений (WMQ, WMB, DataPower, Pega PRPC)");
            put("CI00737137", "Стандартные платформы серверов приложений (WAS, WLS)");
            put("CI02008623", "Мониторинг использования лицензий (МИЛИ)");
            put("CI01563053", "Платформа управления контейнерами (Terra)");
        }};

        Set<String> affectedItem = new HashSet<>(incTop10DataViewFiltered.getItems()
                .map(item -> item.getAffected_Item())
                .collect(Collectors.toSet()));
        Set<String> affectedItemHuman = new HashSet<String>(affectedItem.stream()
                .map(item -> affectedItemMap.get(item))
                .collect(Collectors.toSet()));


        ComboBox<String> incTop10DilterComboBox = new ComboBox<>();
        incTop10DilterComboBox.setPlaceholder("Выберите ИТ-услугу");
        incTop10DilterComboBox.setItems(affectedItemHuman);
        incTop10DilterComboBox.setClearButtonVisible(true);
        incTop10DilterComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        incTop10DilterComboBox.setWidthFull();
        incTop10DilterComboBox.getStyle().set("max-width", "100%");

        incTop10DilterComboBox.addValueChangeListener(e -> filterChangeConsumer.accept(getAffectedItem(affectedItemMap, e.getValue())));

        VerticalLayout layout = new VerticalLayout(incTop10DilterComboBox);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");
        layout.setJustifyContentMode(JustifyContentMode.START);
        return layout;


    }

    public String getAffectedItem(Map<String, String> affectedItemMap, String mapValue) {
        String affectedItem;
        for (Map.Entry<String, String> entry : affectedItemMap.entrySet()) {
            if (entry.getValue().equals(mapValue)) {
                affectedItem = entry.getKey();
                return affectedItem;
            }
        }
        return "";
    }

    private static class IncTop10Filter {


        private GridListDataView<IUspIncidentDataTop10> incTop10DataViewFiltered;

        private String affectedItem;

        public IncTop10Filter(GridListDataView<IUspIncidentDataTop10> dataView) {
            this.incTop10DataViewFiltered = dataView;
            this.incTop10DataViewFiltered.addFilter(this::test);

        }


        public void setAffectedItem(String affectedItem) {
            this.affectedItem = affectedItem;
            this.incTop10DataViewFiltered.refreshAll();
        }


        public boolean test(IUspIncidentDataTop10 uspIncidentData) {
            boolean matchesAffectedItem = matches(uspIncidentData.getAffected_Item(), affectedItem);
            return matchesAffectedItem;

        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty() || value
                    .toLowerCase().contains(searchTerm.toLowerCase());
        }
    }


}



