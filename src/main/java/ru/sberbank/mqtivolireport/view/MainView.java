package ru.sberbank.mqtivolireport.view;
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
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import ru.sberbank.mqtivolireport.domain.MqTivoliData;
import ru.sberbank.mqtivolireport.repo.MqTivoliRepo;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.function.Consumer;
@Route
@PageTitle("MQ Tivoli Report")
public class MainView extends VerticalLayout {
    private H4 header;
    @Autowired
    private MqTivoliRepo repo;
    private Grid<MqTivoliData> grid;
    private GridListDataView<MqTivoliData> dataView;
    private Anchor anchor = new Anchor(new StreamResource("MQTivoli_Report.csv", this::getInputStream), "Экспорт в CSV");;

    public MainView(MqTivoliRepo repo) {
        this.repo = repo;
        this.header = new H4("Покрытие агентами Tivoli MQ");
        this.grid = new Grid<>(MqTivoliData.class, false);
        this.dataView = grid.setItems(repo.findAll());
        this.anchor = new Anchor(new StreamResource("MQTivoli_Report.csv", this::getInputStream), "Экспорт в CSV");
        setHorizontalComponentAlignment(Alignment.CENTER, header);

//Grid View
        Grid<MqTivoliData> grid = new Grid<>(MqTivoliData.class, false);
        grid.setHeight("500px");
        grid.addThemeVariants(GridVariant.LUMO_COMPACT, GridVariant.LUMO_ROW_STRIPES);
//      Grid.Column<MqTivoliData> id = grid.addColumn(MqTivoliData::getId);
        Grid.Column<MqTivoliData> mqName = grid.addColumn(MqTivoliData::getMqName).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<MqTivoliData> serverName = grid.addColumn(MqTivoliData::getServerName).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
        Grid.Column<MqTivoliData> tivoliInstall = grid
                .addColumn(MqTivoliData::getTivoliInstall).setSortable(true).setResizable(true).setTextAlign(ColumnTextAlign.START);
//        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);
        GridListDataView<MqTivoliData> dataView = grid.setItems(repo.findAll());
        PersonFilter personFilter = new PersonFilter(dataView);
        //Create header for Grid
        grid.getHeaderRows().clear();
        HeaderRow headerRow = grid.appendHeaderRow();
        headerRow.getCell(mqName).setComponent(
                createFilterHeader("MQName", personFilter::setMqName));
        headerRow.getCell(serverName).setComponent(
                createFilterHeader("ServerName", personFilter::setServerName));
        headerRow.getCell(tivoliInstall).setComponent(
                createFilterHeader("Tivoli", personFilter::setTivoliInstall));

        //Anchor block
        anchor.getElement().setAttribute("download", true);

        //Column Visibility
        Button menuButton = new Button("Видимость колонок");
        menuButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        ColumnToggleContextMenu columnToggleContextMenu = new ColumnToggleContextMenu(menuButton);
        columnToggleContextMenu.addColumnToggleItem("MQName", mqName);
        columnToggleContextMenu.addColumnToggleItem("ServerName", serverName);
        columnToggleContextMenu.addColumnToggleItem("Tivoli", tivoliInstall);

        // build HorizontalLayout
        HorizontalLayout actions = new HorizontalLayout(anchor, menuButton);
        actions.setVerticalComponentAlignment(Alignment.CENTER, anchor, menuButton);
        setHorizontalComponentAlignment(Alignment.END, actions);



        add(header, actions, grid);

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
        TextField textField = new TextField();
        textField.setValueChangeMode(ValueChangeMode.EAGER);
        textField.setClearButtonVisible(true);
        textField.addThemeVariants(TextFieldVariant.LUMO_SMALL);
        textField.setWidthFull();
        textField.getStyle().set("max-width", "100%");
        textField.setPlaceholder("Поиск " + labelText);
        textField.addValueChangeListener(
                e -> filterChangeConsumer.accept(e.getValue()));
        VerticalLayout layout = new VerticalLayout(label, textField);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");
        return layout;
    }
    private InputStream getInputStream() {
        StringWriter stringWriter = new StringWriter();
        CSVWriter csvWriter = new CSVWriter(stringWriter);
        csvWriter.writeNext("id", "ServerName", "MqName", "TivoliInstall");
        dataView.getItems().forEach(c -> csvWriter.writeNext("" + c.getId(), c.getServerName(), c.getMqName(), c.getTivoliInstall()));
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


        private final GridListDataView<MqTivoliData> dataViewFiltered;
        private String mqName;
        private String serverName;
        private String tivoliInstall;
        public PersonFilter(GridListDataView<MqTivoliData> dataView) {
            this.dataViewFiltered = dataView;
            this.dataViewFiltered.addFilter(this::test);

        }
        public void setMqName(String mqName) {
            this.mqName = mqName;
            this.dataViewFiltered.refreshAll();
        }
        public void setServerName(String serverName) {

            this.serverName = serverName;
            this.dataViewFiltered.refreshAll();
        }

        public void setTivoliInstall(String tivoliInstall) {
            this.tivoliInstall = tivoliInstall;
            this.dataViewFiltered.refreshAll();
        }
        public boolean test(MqTivoliData mqTivoliData) {
            boolean matchesFullName = matches(mqTivoliData.getMqName(), mqName);
            boolean matchesEmail = matches(mqTivoliData.getServerName(), serverName);
            boolean matchesProfession = matches(mqTivoliData.getTivoliInstall(), tivoliInstall);
            return matchesFullName && matchesEmail && matchesProfession;
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

        void addColumnToggleItem(String label, Grid.Column<MqTivoliData> column) {
            MenuItem menuItem = this.addItem(label, e -> {
                column.setVisible(e.getSource().isChecked());
            });
            menuItem.setCheckable(true);
            menuItem.setChecked(column.isVisible());
        }
    }
}