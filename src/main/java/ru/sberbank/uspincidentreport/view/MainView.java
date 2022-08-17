package ru.sberbank.uspincidentreport.view;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;
import ru.sberbank.uspincidentreport.repo.UspIncidentRepo;
import java.io.*;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.Files.readString;


@Route
@PageTitle("Автоинциденты системы мониторинга УСП")
//Сохранение состояния таблицы при обновлении
//@PreserveOnRefresh
public class MainView extends VerticalLayout {
    private H4 header;
    @Autowired
    private UspIncidentRepo repo;
    private Grid<UspIncidentData> grid;
    private GridListDataView<UspIncidentData> dataView;
    private RefreshThread thread;
    PersonFilter personFilter;

//    String assignmentGroup = readString(Paths.get("/home/eshustov/IdeaProjects/usp_incident_assignmentGroup.txt"));

    @Override
    protected void onAttach(AttachEvent attachEvent) {
        // Start the data feed thread
        thread = new RefreshThread(attachEvent.getUI(), this);
        thread.start();
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        // Cleanup
        thread.interrupt();
        thread = null;
    }


    public MainView(UspIncidentRepo repo) throws IOException {
        this.repo = repo;
        this.header = new H4("Активные автоинциденты системы мониторинга УСП");
        this.grid = new Grid<>(UspIncidentData.class, false);
//        this.dataView = grid.setItems(repo.findAll());
        setHorizontalComponentAlignment(Alignment.CENTER, header);
        setJustifyContentMode(JustifyContentMode.START);

//Grid View
        Grid<UspIncidentData> grid = new Grid<>(UspIncidentData.class, false);
        grid.setHeight("500px");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
        grid.setColumnReorderingAllowed(true);

        IncidentContextMenu incContextMenu = new IncidentContextMenu(grid);
        Grid.Column<UspIncidentData> NUMBER = grid
                .addColumn(UspIncidentData::getNUMBER).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Номер инцидента");
        Grid.Column<UspIncidentData> BRIEF_DESCRIPTION = grid
                .addColumn(UspIncidentData::getBRIEF_DESCRIPTION).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Краткое описание");
        Grid.Column<UspIncidentData> PRIORITY_CODE = grid
                .addColumn(UspIncidentData::getPRIORITY_CODE).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Важность");
        Grid.Column<UspIncidentData> OPEN_TIME = grid
                .addColumn(UspIncidentData::getOPEN_TIME).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Время регистрации");
        Grid.Column<UspIncidentData> ASSIGNEE_NAME = grid
                .addColumn(UspIncidentData::getHPC_ASSIGNEE_NAME).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Исполнитель");
        Grid.Column<UspIncidentData> ASSIGNMENT = grid
                .addColumn(UspIncidentData::getHPC_ASSIGNMENT).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Назначен в группу");
        Grid.Column<UspIncidentData> CREATED_BY_NAME = grid
                .addColumn(UspIncidentData::getHPC_CREATED_BY_NAME).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Инициатор");
        CREATED_BY_NAME.setVisible(false);
        Grid.Column<UspIncidentData> ZABBIX_HISTORY = grid
                .addColumn(new ComponentRenderer<>(z -> (new Anchor(z.getZABBIX_HISTORY(), "История в Zabbix"))))
                .setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("История в Zabbix");
        ZABBIX_HISTORY.setVisible(false);
        Grid.Column<UspIncidentData> STATUS = grid
                .addColumn(UspIncidentData::getHPC_STATUS).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Статус");
        Grid.Column<UspIncidentData> RESOLUTION = grid
                .addColumn(new ComponentRenderer<>(z -> (new Anchor(z.getRESOLUTION(), "Сценарий устранения"))))
                .setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Сценарий устранения");
        RESOLUTION.setVisible(false);
        Grid.Column<UspIncidentData> ACTION = grid
                .addColumn(UspIncidentData::getACTION).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Подробное описание");
        ACTION.setVisible(false);
        Grid.Column<UspIncidentData> HOST = grid
                .addColumn(UspIncidentData::getHOST).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Сервер");
        Grid.Column<UspIncidentData> PROBLEM = grid
                .addColumn(UspIncidentData::getPROBLEM).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Проблема");
        PROBLEM.setVisible(false);
        Grid.Column<UspIncidentData> AFFECTED_ITEM = grid
                .addColumn(UspIncidentData::getAFFECTED_ITEM).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("ИТ-услуга");
        Grid.Column<UspIncidentData> RESOLUTION_GUIDE = grid
                .addColumn(new ComponentRenderer<>(z -> (new Anchor(z.getRESOLUTION_GUIDE(), "Инструкция для устранения"))))
                .setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START).setHeader("Инструкция для устранения");
        RESOLUTION_GUIDE.setVisible(false);

//        GridListDataView<UspIncidentData> dataView = grid.setItems(repo.findAll(assignmentGroup));
        GridListDataView<UspIncidentData> dataView = grid.setItems(repo.findAll());
        personFilter = new PersonFilter(dataView);

        //Create headers for Grid

        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(NUMBER)
                .setComponent(createFilterHeader("Номер инцидента", personFilter::setNumber));
        headerRow.getCell(BRIEF_DESCRIPTION)
                .setComponent(createFilterHeader("Краткое описание", personFilter::setBriefDescription));
        headerRow.getCell(PRIORITY_CODE)
                .setComponent(createFilterHeader("Важность", personFilter::setPriorityCode));
        headerRow.getCell(OPEN_TIME)
                .setComponent(createFilterHeader("Время регистрации", personFilter::setOpenTime));
        headerRow.getCell(ASSIGNEE_NAME)
                .setComponent(createFilterHeader("Исполнитель", personFilter::setAssigneeName));
        headerRow.getCell(ASSIGNMENT)
                .setComponent(createFilterHeader("Назначен в группу", personFilter::setAssignment));
        headerRow.getCell(CREATED_BY_NAME)
                .setComponent(createFilterHeader("Инициатор", personFilter::setCreatedByName));
        headerRow.getCell(ZABBIX_HISTORY)
                .setComponent(createFilterHeader("История в Zabbix", personFilter::setZabbixHistory));
        headerRow.getCell(STATUS)
                .setComponent(createFilterHeader("Статус", personFilter::setStatus));
        headerRow.getCell(RESOLUTION)
                .setComponent(createFilterHeader("Сценарий устранения", personFilter::setResolution));
        headerRow.getCell(RESOLUTION_GUIDE)
                .setComponent(createFilterHeader("Инструкция для устранения", personFilter::setResolutionGuide));
        headerRow.getCell(ACTION)
                .setComponent(createFilterHeader("Подробное описание", personFilter::setAction));
        headerRow.getCell(HOST)
                .setComponent(createFilterHeader("Сервер", personFilter::setHost));
        headerRow.getCell(PROBLEM)
                .setComponent(createFilterHeader("Проблема", personFilter::setProblem));
        headerRow.getCell(AFFECTED_ITEM)
                .setComponent(FilterActiveIncident.createFilterHeader("ИТ-услуга", personFilter::setAffectedItem, dataView));
        //        AFFECTED_ITEM.setHeader(FilterActiveIncident.createFilterHeader("ИТ-услуга", personFilter::setAffectedItem, dataView));
//        headerRow.getCell(AFFECTED_ITEM)
//                .setComponent(createFilterHeader("ИТ-услуга", personFilter::setAffectedItem));


        // Вывод подробной информации по инциденту по выделению строки таблицы
        grid.setItemDetailsRenderer(new ComponentRenderer<>(incident -> {
            VerticalLayout layout = new VerticalLayout();
            layout.add(new Label(incident.getACTION()));
            return layout;
        }));

//        Export to CSV
        var streamResource = new StreamResource("uspOpenIncidents.csv",
                () -> {
                    Stream<UspIncidentData> uspIncidentList = personFilter.dataViewFiltered.getItems();
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

        //Anchor block
        Anchor downloadToCSV = new Anchor(streamResource, "Сохранить в CSV" );
        Button buttonDownloadCSV = new Button(new Icon(VaadinIcon.DOWNLOAD));
        buttonDownloadCSV.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL, ButtonVariant.LUMO_ICON);
        downloadToCSV.removeAll();
        downloadToCSV.add(buttonDownloadCSV);

        //Link to Analitics
        Anchor analiticsChart = new Anchor("analitics", "Аналитика");
        analiticsChart.setTarget("_blank");



        //Создание панели инструментов
        MenuBar menuBar = new MenuBar();
        ComponentEventListener<ClickEvent<MenuItem>> listenerForRefresh = e -> thread.needRefresh = true;


////Так можно делать обработку меню видимости столбцов без изсползования ColumnToggleContextMenu. Силль отображения одинаковый. ПРоще делать с ColumnToggleContextMenu.
//        ComponentEventListener<ClickEvent<MenuItem>> incNumberlistener = e -> {
//            if (e.getSource().isChecked()) {
//                NUMBER.setVisible(true);
//            } else {
//                NUMBER.setVisible(false);
//            }
//        };


        menuBar.addThemeVariants(MenuBarVariant.LUMO_TERTIARY, MenuBarVariant.LUMO_SMALL, MenuBarVariant.LUMO_ICON);

        MenuItem style = menuBar.addItem("Вид");
        SubMenu styleSubMenu = style.getSubMenu();
        MenuItem normal = styleSubMenu.addItem("Номальный");
        normal.setCheckable(true);
        normal.setChecked(true);
        MenuItem compact = styleSubMenu.addItem("Компактный");
        compact.setCheckable(true);
        compact.setChecked(false);

        ComponentEventListener<ClickEvent<MenuItem>> NormalStylelistener = e -> {
            if (e.getSource().isChecked()) {
                grid.removeThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);
                grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                compact.setChecked(false);
            }
        };

        ComponentEventListener<ClickEvent<MenuItem>> CompactStylelistener = e -> {
            if (e.getSource().isChecked()) {
                grid.removeThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
                grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);
                normal.setChecked(false);
            }
        };

        normal.addClickListener(NormalStylelistener);
        compact.addClickListener(CompactStylelistener);

        menuBar.addItem(new Icon(VaadinIcon.REFRESH), listenerForRefresh);
        menuBar.addItem(downloadToCSV);
        menuBar.addItem("Столбцы");
        ////Так можно делать меню видимости столбцов без изсползования ColumnToggleContextMenu. Силль отображения одинаковый. ПРоще делать с ColumnToggleContextMenu.
//        SubMenu styleSubMenu = column.getSubMenu();
//        MenuItem incNumber = styleSubMenu.addItem("Номер инцидента");
//        incNumber.setCheckable(true);
//        incNumber.setChecked(true);
//        incNumber.addClickListener(incNumberlistener);


        //Column Visibility
//        Так можно прикрутить кнопку к меню выбора видимости столбцов. В данном приложении используется MenuBar
//        Button menuButton = new Button(new Icon(VaadinIcon.LIST_SELECT));
//        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(menuBar.getItems().get(3));
        columnToggleContextMenu.addColumnToggleItem("Номер инцидента", NUMBER);
        columnToggleContextMenu.addColumnToggleItem("Краткое описание", BRIEF_DESCRIPTION);
        columnToggleContextMenu.addColumnToggleItem("Важность", PRIORITY_CODE);
        columnToggleContextMenu.addColumnToggleItem("Время регистрации", OPEN_TIME);
        columnToggleContextMenu.addColumnToggleItem("Исполнитель", ASSIGNEE_NAME);
        columnToggleContextMenu.addColumnToggleItem("Назначен в группу", ASSIGNMENT);
        columnToggleContextMenu.addColumnToggleItem("Инициатор", CREATED_BY_NAME);
        columnToggleContextMenu.addColumnToggleItem("История в Zabbix", ZABBIX_HISTORY);
        columnToggleContextMenu.addColumnToggleItem("Статус", STATUS);
        columnToggleContextMenu.addColumnToggleItem("Сценарий для устранения", RESOLUTION);
        columnToggleContextMenu.addColumnToggleItem("Инструкция для устранения", RESOLUTION_GUIDE);
        columnToggleContextMenu.addColumnToggleItem("Подробное описание", ACTION);
        columnToggleContextMenu.addColumnToggleItem("Сервер", HOST);
        columnToggleContextMenu.addColumnToggleItem("Проблема", PROBLEM);
        columnToggleContextMenu.addColumnToggleItem("ИТ-услуга", AFFECTED_ITEM);


        //Кнопка обновления
//        Button refreshButton = new Button(new Icon(VaadinIcon.REFRESH));
//        refreshButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
//        refreshButton.addClickListener(e -> {
//            // Обновление таблицы
//            counter.set(0);
//            }
//        );

        // build top HorizontalLayout
        HorizontalLayout actions = new HorizontalLayout(analiticsChart,menuBar);
        actions.setVerticalComponentAlignment(Alignment.END, menuBar);
        actions.setVerticalComponentAlignment(Alignment.CENTER, analiticsChart);
        setHorizontalComponentAlignment(Alignment.END, actions);


//        Добавление компонентов в основной layout
        add(header, actions, grid, incContextMenu);

//        countRefresh(counter);


//        new Thread(() -> { while (true) {
//            // Update the data for a while
//            while (counter.get() > 0) {
//                // Sleep to emulate background work
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                counter.getAndDecrement();
//
//            }
//        }}).start();


    }

//    public static void countRefresh(AtomicInteger counter){
//        while (true) {
//            // Update the data for a while
//            while (counter.get() > 0) {
//                // Sleep to emulate background work
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                counter.getAndDecrement();
//
//            }
//        }
//    }

    private Component createFilterHeader(String labelText, Consumer<String> filterChangeConsumer) {
        Label label = new Label(labelText);
        label.getStyle().set("padding-top", "var(--lumo-space-m)")
                .set("font-size", "var(--lumo-font-size-xs)");
        TextField filterField = new TextField();
        filterField.setValueChangeMode(ValueChangeMode.EAGER);
        filterField.setClearButtonVisible(true);
        filterField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        filterField.setWidthFull();
        filterField.getStyle().set("max-width", "100%");
//        filterField.setPlaceholder("Поиск");
        filterField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filterField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(filterField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");
        layout.setJustifyContentMode(JustifyContentMode.START);
        return layout;
    }

    private void affectedItemFilterRefresh(){
        String selectedItem;
        try {
            FilterActiveIncident.affectedItem.clear();
            FilterActiveIncident.affectedItemHuman.clear();
            if (FilterActiveIncident.filterAffectedItemComboBox.getElement().getProperty("selectedItem")!=null) {
                HashMap selectedItemMap = new ObjectMapper().readValue(FilterActiveIncident.filterAffectedItemComboBox.getElement().getProperty("selectedItem"), HashMap.class);
                selectedItem = selectedItemMap.get("label").toString();
            }else {selectedItem = "";}
            if (dataView.getItems().count() != 0) {
                FilterActiveIncident.affectedItem = dataView.getItems()
                        .map(item -> item.getAFFECTED_ITEM())
                        .collect(Collectors.toSet());
                FilterActiveIncident.affectedItem.stream()
                        .forEach(item -> {
                            //Проверка на услугу не принадлежащую УСП
                            if (FilterActiveIncident.affectedItemMap.get(item)==null){
                                FilterActiveIncident.affectedItemHuman.add("*");
                            }else {
                                FilterActiveIncident.affectedItemHuman.add(FilterActiveIncident.affectedItemMap.get(item));
                            }
                        });
//                FilterActiveIncident.affectedItemHuman = FilterActiveIncident.affectedItem.stream()
//                        .map(item -> FilterActiveIncident.affectedItemMap.get(item))
//                        .collect(Collectors.toSet());
                if (FilterActiveIncident.affectedItemHuman.contains("*")) FilterActiveIncident.affectedItemHuman.remove("*");
                FilterActiveIncident.filterAffectedItemComboBox.setItems(FilterActiveIncident.affectedItemHuman);
                FilterActiveIncident.filterAffectedItemComboBox.setValue(selectedItem);
            } else {FilterActiveIncident.filterAffectedItemComboBox.setItems("");}
        } catch (NullPointerException e) {FilterActiveIncident.filterAffectedItemComboBox.setItems("");} catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    private static class PersonFilter {


        private GridListDataView<UspIncidentData> dataViewFiltered;
        private String number;
        private String briefDescription;
        private String priorityCode;
        private String openTime;
        private String assigneeName;
        private String assignment;
        private String createdByName;
        private String zabbixHistory;
        private String status;
        private String resolution;
        private String resolutionGuide;
        private String action;
        private String host;
        private String problem;
        private String affectedItem;

        public PersonFilter(GridListDataView<UspIncidentData> dataView) {
            this.dataViewFiltered = dataView;
            this.dataViewFiltered.addFilter(this::test);

        }

        public void setNumber(String number) {
            this.number = number;
            this.dataViewFiltered.refreshAll();
        }

        public void setBriefDescription(String briefDescription) {
            this.briefDescription = briefDescription;
            this.dataViewFiltered.refreshAll();
        }

        public void setPriorityCode(String priorityCode) {
            this.priorityCode = priorityCode;
            this.dataViewFiltered.refreshAll();
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
            this.dataViewFiltered.refreshAll();
        }

        public void setAssigneeName(String assigneeName) {
            this.assigneeName = assigneeName;
            this.dataViewFiltered.refreshAll();
        }

        public void setAssignment(String assignment) {
            this.assignment = assignment;
            this.dataViewFiltered.refreshAll();
        }

        public void setCreatedByName(String createdByName) {
            this.createdByName = createdByName;
            this.dataViewFiltered.refreshAll();
        }

        public void setZabbixHistory(String zabbixHistory) {
            this.zabbixHistory = zabbixHistory;
            this.dataViewFiltered.refreshAll();
        }

        public void setStatus(String status) {
            this.status = status;
            this.dataViewFiltered.refreshAll();
        }

        public void setResolution(String resolution) {
            this.resolution = resolution;
            this.dataViewFiltered.refreshAll();
        }

        public void setAction(String action) {
            this.action = action;
            this.dataViewFiltered.refreshAll();
        }

        public void setHost(String host) {
            this.host = host;
            this.dataViewFiltered.refreshAll();
        }

        public void setProblem(String problem) {
            this.problem = problem;
            this.dataViewFiltered.refreshAll();
        }

        public void setAffectedItem(String affectedItem) {
            this.affectedItem = affectedItem;
            this.dataViewFiltered.refreshAll();
        }

        public void setResolutionGuide(String resolutionGuide){
            this.dataViewFiltered.refreshAll();
        }

        public boolean test(UspIncidentData uspIncidentData) {
            boolean matchesNumber = matches(uspIncidentData.getNUMBER(), number);
            boolean matchesBriefDescription = matches(uspIncidentData.getBRIEF_DESCRIPTION(), briefDescription);
            boolean matchesPriorityCode = matches(uspIncidentData.getPRIORITY_CODE(), priorityCode);
            boolean matchesOpenTime = matches(uspIncidentData.getOPEN_TIME(), openTime);
            boolean matchesAssigneeName = matches(uspIncidentData.getHPC_ASSIGNEE_NAME(), assigneeName);
            boolean matchesAssignment = matches(uspIncidentData.getHPC_ASSIGNMENT(), assignment);
            boolean matchesCreatedByName = matches(uspIncidentData.getHPC_CREATED_BY_NAME(), createdByName);
            boolean matchesZabbixHistory = matches(uspIncidentData.getZABBIX_HISTORY(), zabbixHistory);
            boolean matchesStatus = matches(uspIncidentData.getHPC_STATUS(), status);
            boolean matchesResolution = matches(uspIncidentData.getRESOLUTION(), resolution);
            boolean matchesAction = matches(uspIncidentData.getACTION(), action);
            boolean matchesHost = matches(uspIncidentData.getHOST(), host);
            boolean matchesProblem = matches(uspIncidentData.getPROBLEM(), problem);
            boolean matchesAffectedItem = matches(uspIncidentData.getAFFECTED_ITEM(), affectedItem);
            boolean matchesResolutionGuide = matches(uspIncidentData.getAFFECTED_ITEM(), resolutionGuide);
            return matchesNumber && matchesBriefDescription && matchesPriorityCode && matchesOpenTime &&
                    matchesAssigneeName && matchesAssignment && matchesCreatedByName && matchesZabbixHistory &&
                    matchesStatus && matchesResolution && matchesAction && matchesHost && matchesProblem &&
                    matchesAffectedItem && matchesResolutionGuide;

        }

        private boolean matches(String value, String searchTerm) {
            return searchTerm == null || searchTerm.isEmpty() || value
                    .toLowerCase().contains(searchTerm.toLowerCase());
        }
    }

    private static class ColumnToggleContextMenu extends ContextMenu {
        public ColumnToggleContextMenu(Component target) {
            super(target);
            setOpenOnClick(true);
        }

        void addColumnToggleItem(String label, Grid.Column<UspIncidentData> column) {
            MenuItem menuItem = this.addItem(label, e -> {
                column.setVisible(e.getSource().isChecked());
            });
            menuItem.setCheckable(true);
            menuItem.setChecked(column.isVisible());
        }
    }

    public static class IncidentContextMenu extends GridContextMenu<UspIncidentData> {
        public IncidentContextMenu(Grid<UspIncidentData> target) {
            super(target);

            addItem("Сценарий устранения в RLM", e -> e.getItem().ifPresent(incident -> {
                if (!incident.getRESOLUTION().equals("")) {
                    getUI().get().getPage().open(incident.getRESOLUTION(), "Сценарий устранения в RLM");
                } else {Notification.show("Нет сценариев RLM для устранения автоинцидента", 1000, Notification.Position.MIDDLE);}
                // System.out.printf("Edit: %s%n", person.getFullName());
            }));
            addItem("Инструкция для устранения автоинцидента", e -> e.getItem().ifPresent(incident -> {
                if (!incident.getRESOLUTION_GUIDE().equals("")) {
                    getUI().get().getPage().open(incident.getRESOLUTION_GUIDE(), "Инструкция для устранения автоинцидента");
                } else {Notification.show("Нет инструкции для устранения автоинцидента", 1000, Notification.Position.MIDDLE);}
                // System.out.printf("Edit: %s%n", person.getFullName());
            }));
            addItem("История в Zabbix", e -> e.getItem().ifPresent(incident -> {
                getUI().get().getPage().open(incident.getZABBIX_HISTORY(), "История в Zabbix");
            }));
            addItem("Открыть в Service Manager", e -> e.getItem().ifPresent(incident -> {
                getUI().get().getPage().open(
                        "https://servicemanager.ca.sbrf.ru/hpsm/index.do?lang=ru&ctx=docEngine&file=probsummary&query=number%3D%22" + incident.getNUMBER() + "%22&action=&title=",
                        "Открыть в Service Manager");
            }));
        }
    }

    private class RefreshThread extends Thread {
        private final UI ui;
        private final MainView view;
        private Span span = new Span();
        private Span incCount = new Span();
        private Span incFilteredCount = new Span();
        public boolean needRefresh = false;


        public RefreshThread(UI ui, MainView view) {
            this.ui = ui;
            this.view = view;
            view.dataView = grid.setItems(repo.findAll());

        }

        @Override
        public void run() {
             try {
                while (true) {
                    int counter = 300;
                    // Update the data for a while
                    while (counter > 0 && !needRefresh) {
                    // Sleep to emulate background work
                        Thread.sleep(10000);
                        String message = "Обновление данных через " + (counter = counter -10) + " сек";

                        ui.access(() -> {
                            view.remove(incFilteredCount, incCount, span);
                            span.setText(message);
                            incCount.setText("Всего инцидентов: " + String.valueOf(view.dataView.getItemCount()));
                            personFilter.dataViewFiltered.removeItems(personFilter.dataViewFiltered.getItems().collect(Collectors.toList()));
                            personFilter.dataViewFiltered.addItems(view.dataView.getItems().collect(Collectors.toList()));
                            incFilteredCount.setText("Отфильтровано: " + String.valueOf(view.personFilter.dataViewFiltered.getItemCount()));
                            view.add(incFilteredCount, incCount, span);

                        });
                    }

                    // Inform that we are done
                    ui.access(() -> {
                        view.remove(incFilteredCount, incCount, span);
                        personFilter.dataViewFiltered.removeItems(personFilter.dataViewFiltered.getItems().collect(Collectors.toList()));
//                        view.dataView = grid.setItems(repo.findAll(assignmentGroup));
//                        personFilter.dataViewFiltered.addItems(repo.findAll(assignmentGroup));
                        view.dataView = grid.setItems(repo.findAll());
                        personFilter.dataViewFiltered.addItems(repo.findAll());
                        affectedItemFilterRefresh();
                        Notification.show("Данные обновлены", 1000, Notification.Position.TOP_CENTER);
                        span.setText("Данные обновлены");
                        incFilteredCount.setText("Отфильтровано: " + String.valueOf(view.personFilter.dataViewFiltered.getItemCount()));
                        incCount.setText("Всего инцидентов: " + String.valueOf(view.dataView.getItemCount()));
                        view.add(incFilteredCount, incCount, span);
                        needRefresh=false;
//                        System.gc();
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}