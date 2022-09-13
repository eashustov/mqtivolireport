package ru.sberbank.uspincidentreport.service.zabbix;

import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.server.StreamResource;


import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.util.stream.Stream;

public class ExportToCSV {

    public static StreamResource exportToCSV(GridListDataView<Trigger> dataView){
        //        Export to CSV
        var streamResource = new StreamResource("USPTriggers.csv",
                () -> {
                    Stream<Trigger> uspTriggersIncidentList = dataView.getItems();
                    StringWriter output = new StringWriter();
                    StatefulBeanToCsv<Trigger> beanToCSV = null;
                    beanToCSV = new StatefulBeanToCsvBuilder<Trigger>(output)
//                                .withIgnoreField(Trigger.class, Trigger.class.getDeclaredField("ACTION"))
                            .build();
                    try {
                        beanToCSV.write(uspTriggersIncidentList);
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
