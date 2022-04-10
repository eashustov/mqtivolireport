package ru.sberbank.mqtivolireport.view;

import com.helger.commons.csv.CSVWriter;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H2;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.apache.commons.io.IOUtils;
import ru.sberbank.mqtivolireport.domain.MqTivoliData;
import ru.sberbank.mqtivolireport.repo.MqTivoliRepo;

import java.io.InputStream;
import java.io.StringWriter;

@Route
@PageTitle("MQ Tivoli Report")
public class MainView extends VerticalLayout {
    private final MqTivoliRepo repo;
    private Grid<MqTivoliData> grid;
    private H2 header = new H2("Покрытие агентами Tivoli MQ");
    private final TextField filter;
    private final Anchor anchor;

       public MainView(MqTivoliRepo repo) {
        this.header = header;
        this.repo = repo;
        this.grid = new Grid<>(MqTivoliData.class);
        this.filter = new TextField();
        this.anchor = new Anchor(new StreamResource("MQTivoli_Report.csv", this::getInputStream), "Экспорт в CSV");
           // build layout
        HorizontalLayout actions = new HorizontalLayout(filter);
        setHorizontalComponentAlignment(Alignment.CENTER, header);
        add(header, anchor, actions, grid);

        grid.setHeight("300px");
        grid.setColumns("id", "mqName", "serverName", "tivoliInstall");
        grid.getColumnByKey("id").setWidth("50px").setFlexGrow(0);

        anchor.getElement().setAttribute("download", true);
        setHorizontalComponentAlignment(Alignment.END, anchor);

        filter.setPlaceholder("Поиск");
        filter.setValueChangeMode(ValueChangeMode.EAGER);
        filter.addValueChangeListener(e -> showData(e.getValue()));

        showData("");

        }

    private InputStream getInputStream() {
        StringWriter stringWriter = new StringWriter();

        CSVWriter csvWriter = new CSVWriter(stringWriter);
        csvWriter.writeNext("id", "ServerName", "MqName", "TivoliInstall");
        repo.findAll().forEach(c -> csvWriter.writeNext("" + c.getId(), c.getServerName(), c.getMqName(), c.getTivoliInstall()));

        return IOUtils.toInputStream(stringWriter.toString(), "UTF-8");

    }

    private void showData(String filterText) {
        if (filterText.isEmpty()){
            grid.setItems(repo.findAll());
        } else {
            grid.setItems(repo.findByServerName(filterText));
        }

    }


}
