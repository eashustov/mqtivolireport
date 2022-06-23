package ru.sberbank.uspincidentreport.service;

import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.server.StreamResource;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;

import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.stream.Stream;

public class ExporToCSV {

    public static StreamResource exportToCSV(GridListDataView<UspIncidentData> dataView){
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
    };

}
