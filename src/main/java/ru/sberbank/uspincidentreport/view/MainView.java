package ru.sberbank.uspincidentreport.view;
import com.helger.commons.csv.CSVWriter;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.grid.HeaderRow;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;
import ru.sberbank.uspincidentreport.repo.UspIncidentRepo;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.function.Consumer;
@Route
@PageTitle("Автоинциденты системы мониторинга УСП")
public class MainView extends VerticalLayout {
    private H4 header;
    @Autowired
    private UspIncidentRepo repo;
    private Grid<UspIncidentData> grid;
    private GridListDataView<UspIncidentData> dataView;
    private Anchor anchor;

    public MainView(UspIncidentRepo repo) {
        this.repo = repo;
        this.header = new H4("Автоинциденты системы мониторинга УСП");
        this.grid = new Grid<>(UspIncidentData.class, false);
        this.dataView = grid.setItems(repo.findAll());
        this.anchor = new Anchor(new StreamResource("uspincidentreport.csv", this::getInputStream), "Экспорт в CSV");
        setHorizontalComponentAlignment(Alignment.CENTER, header);

//Grid View
        Grid<UspIncidentData> grid = new Grid<>(UspIncidentData.class, false);
        grid.setHeight("500px");
        grid.addThemeVariants(GridVariant.LUMO_WRAP_CELL_CONTENT, GridVariant.LUMO_ROW_STRIPES);
        grid.setItemDetailsRenderer(new ComponentRenderer<>(incident -> {
            VerticalLayout layout = new VerticalLayout();
            layout.add(new Label(incident.getACTION()));
            return layout;
        }));
        IncidentContextMenu incContextMenu = new IncidentContextMenu(grid);
        Grid.Column<UspIncidentData> NUMBER = grid
                .addColumn(UspIncidentData::getNUMBER).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> BRIEF_DESCRIPTION = grid
                .addColumn(UspIncidentData::getBRIEF_DESCRIPTION).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> PRIORITY_CODE = grid
                .addColumn(UspIncidentData::getPRIORITY_CODE).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> OPEN_TIME = grid
                .addColumn(UspIncidentData::getOPEN_TIME).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> ASSIGNEE_NAME = grid
                .addColumn(UspIncidentData::getHPC_ASSIGNEE_NAME).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> ASSIGNMENT = grid
                .addColumn(UspIncidentData::getHPC_ASSIGNMENT).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> CREATED_BY_NAME = grid
                .addColumn(UspIncidentData::getHPC_CREATED_BY_NAME).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> ZABBIX_HISTORY = grid
                .addColumn(new ComponentRenderer<>(z -> (new Anchor(z.getZABBIX_HISTORY(), "История проблем по хосту")))).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> STATUS = grid
                .addColumn(UspIncidentData::getHPC_STATUS).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> RESOLUTION = grid
                .addColumn(new ComponentRenderer<>(z -> (new Anchor(z.getRESOLUTION(), "Сценарий устранения")))).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> ACTION = grid
                .addColumn(UspIncidentData::getACTION).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> HOST = grid
                .addColumn(UspIncidentData::getHOST).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<UspIncidentData> PROBLEM = grid
                .addColumn(UspIncidentData::getPROBLEM).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        GridListDataView<UspIncidentData> dataView = grid.setItems(repo.findAll());
        PersonFilter personFilter = new PersonFilter(dataView);

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
                .setComponent(createFilterHeader("Назначен на группу", personFilter::setAssignment));
        headerRow.getCell(CREATED_BY_NAME)
                .setComponent(createFilterHeader("Инициатор", personFilter::setCreatedByName));
        headerRow.getCell(ZABBIX_HISTORY)
                .setComponent(createFilterHeader("История в Zabbix", personFilter::setZabbixHistory));
        headerRow.getCell(STATUS)
                .setComponent(createFilterHeader("Статус", personFilter::setStatus));
        headerRow.getCell(RESOLUTION)
                .setComponent(createFilterHeader("Сценарий устранения", personFilter::setResolution));
        headerRow.getCell(ACTION)
                .setComponent(createFilterHeader("Подробное описание", personFilter::setAction));
        headerRow.getCell(HOST)
                .setComponent(createFilterHeader("Сервер", personFilter::setHost));
        headerRow.getCell(PROBLEM)
                .setComponent(createFilterHeader("Проблема", personFilter::setProblem));

        //Anchor block
        anchor.getElement().setAttribute("download", true);

        //Column Visibility
        Button menuButton = new Button(new Icon(VaadinIcon.LIST_SELECT));
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(menuButton);
        columnToggleContextMenu.addColumnToggleItem("Номер инцидента", NUMBER);
        columnToggleContextMenu.addColumnToggleItem("Краткое описание", BRIEF_DESCRIPTION);
        columnToggleContextMenu.addColumnToggleItem("Важность", PRIORITY_CODE);
        columnToggleContextMenu.addColumnToggleItem("Время регистрации", OPEN_TIME);
        columnToggleContextMenu.addColumnToggleItem("Исполнитель", ASSIGNEE_NAME);
        columnToggleContextMenu.addColumnToggleItem("Назначен на группу", ASSIGNMENT);
        columnToggleContextMenu.addColumnToggleItem("Инициатор", CREATED_BY_NAME);
        columnToggleContextMenu.addColumnToggleItem("История в Zabbix", ZABBIX_HISTORY);
        columnToggleContextMenu.addColumnToggleItem("Статус", STATUS);
        columnToggleContextMenu.addColumnToggleItem("Сценарий для устранения", RESOLUTION);
        columnToggleContextMenu.addColumnToggleItem("Подробное описание", ACTION);
        columnToggleContextMenu.addColumnToggleItem("Сервер", HOST);
        columnToggleContextMenu.addColumnToggleItem("Проблема", PROBLEM);

        // build HorizontalLayout
        HorizontalLayout actions = new HorizontalLayout(anchor, menuButton);
        actions.setVerticalComponentAlignment(Alignment.CENTER, anchor, menuButton);
        setHorizontalComponentAlignment(Alignment.END, actions);

        add(header, actions, grid, incContextMenu);

        //Simple Filter View
//
//        filter.setPlaceholder("Поиск");
//        filter.setValueChangeMode(ValueChangeMode.EAGER);
//        filter.addValueChangeListener(e -> showData(e.getValue()));

//        showData("");
    }

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
//        filterField.setPrefixComponent(new Icon(VaadinIcon.SEARCH));
        filterField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(label, filterField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");
        return layout;
    }

    private InputStream getInputStream() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);
        csvWriter.writeNext("Номер инцидента", "Краткое описание", "Важность", "Время регистрации", "Исполнитель",
                "Назначен на группу", "Инициатор", "История в Zabbix", "Статус", "Сценарий устранения", "Подробное описание",
                "Сервер", "Проблема");
        dataView.getItems().forEach(c -> csvWriter.writeNext("" + c.getNUMBER(), c.getBRIEF_DESCRIPTION(), c.getPRIORITY_CODE(),
                c.getOPEN_TIME(), c.getHPC_ASSIGNEE_NAME(), c.getHPC_ASSIGNMENT(), c.getHPC_CREATED_BY_NAME(), c.getZABBIX_HISTORY(),
                c.getHPC_STATUS(), c.getRESOLUTION(), c.getACTION(), c.getHOST(), c.getPROBLEM()));
        return IOUtils.toInputStream(stringWriter.toString(), "UTF-8");

    }

    // Simple showData with one filter text
//    private void showData(String filterText) {
//        if (filterText.isEmpty()) {
//            grid.setItems(repo.findAll());
//        } else {
//            grid.setItems(repo.findByServerName(filterText));
//        }
//
//    }
    private static class PersonFilter {


        private final GridListDataView<UspIncidentData> dataViewFiltered;
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
        private String action;
        private String host;
        private String problem;

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
            return matchesNumber && matchesBriefDescription && matchesPriorityCode && matchesOpenTime &&
                    matchesAssigneeName && matchesAssignment && matchesCreatedByName && matchesZabbixHistory &&
                    matchesStatus && matchesResolution && matchesAction && matchesHost && matchesProblem;

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

    private static class IncidentContextMenu extends GridContextMenu<UspIncidentData> {
        public IncidentContextMenu(Grid<UspIncidentData> target) {
            super(target);

                    addItem("Сценарий устранения", e -> e.getItem().ifPresent(incident -> {
                        getUI().get().getPage().open(incident.getRESOLUTION(), "Сценарий устранения");
                        // System.out.printf("Edit: %s%n", person.getFullName());
                    }));
                    addItem("История в Zabbix", e -> e.getItem().ifPresent(incident -> {
                        getUI().get().getPage().open(incident.getZABBIX_HISTORY(), "История проблем по хосту");
                        // System.out.printf("Delete: %s%n", person.getFullName());
                    }));
        }
    }
}