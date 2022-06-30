package ru.sberbank.uspincidentreport.service;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.server.StreamResource;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataTop10;
import ru.sberbank.uspincidentreport.repo.UspIncidentDataTop10Repo;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class ExporToCSV {

    public static StreamResource exportToCSV(GridListDataView<UspIncidentData> dataView) {

        //        Export to CSV
        var streamResource = new StreamResource("uspIncidents.csv",
                () -> {
                    Stream<UspIncidentData> uspIncidentList = dataView.getItems();
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

    ;

    public static List<String[]> createCsvDataTop10(UspIncidentDataTop10Repo dataTop10IncRepo, String startDate, String endDate) {
        DateTimeFormatter europeanDateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        List<String[]> top10IncList = new ArrayList<>();
        top10IncList.add(new ArrayList<>() {{
            add("ИТ-услуга");
            add("Сервер");
            add("Количество");
        }}.toArray(new String[3]));
        dataTop10IncRepo.findTop10IncCount(startDate, endDate).stream()
                .forEach(e -> {
                    String AffectedItem = e.getAffected_Item();
                    String Host = e.getHost();
                    String CountInc = e.getCount_Inc().toString();
                    List<String> incItem = new ArrayList<>();
                    incItem.add(AffectedItem);
                    incItem.add(Host);
                    incItem.add(CountInc);
                    top10IncList.add(incItem.toArray(new String[incItem.size()]));

                });

        return top10IncList;

    }

    public static StreamResource exportTop10ToCSV(UspIncidentDataTop10Repo dataTop10IncRepo, String startDate, String endDate) {

        List<String[]> csvData = createCsvDataTop10(dataTop10IncRepo, startDate, endDate);

        var streamResource = new StreamResource("uspIncidents.csv",
                () -> {
                    StringWriter output = new StringWriter();
                    CSVWriter writer = new CSVWriter(output);
                    writer.writeAll(csvData);
                    var contents = output.toString();
                    return new ByteArrayInputStream(contents.getBytes());
                }
        );
        return streamResource;

    }
}
