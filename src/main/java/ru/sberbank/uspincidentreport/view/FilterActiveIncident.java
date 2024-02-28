package ru.sberbank.uspincidentreport.view;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.ComboBoxVariant;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FilterActiveIncident {

    static Map<String, String> affectedItemMap = new HashMap<>() {{
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
        put("CI02021300", "SKeeper");
        put("CI02192118", "SOWA");
        put("CI02021306", "Сервисы интеграции приложений WebSphere (IBM App services)");
        put("CI00737141", "Специализированные платформы серверов приложений (IBM Portal, Oracle Siebel CRM, Teradat, IBM FileNet)");
        put("CI00737140", "Интеграционные платформы серверов приложений (WMQ, WMB, DataPower, Pega PRPC)");
        put("CI00737137", "Стандартные платформы серверов приложений (WAS, WLS)");
        put("CI02008623", "Мониторинг использования лицензий (МИЛИ)");
        put("CI01563053", "Платформа управления контейнерами (Terra)");
        put("CI04178739", "SynGX");
        put("CI04085569", "Platform V Corax (Kafka SE)");
        put("CI04452790", "SIDEC");
        put("CI05435889", "IAGW");
        put("CI05879203", "EAGLE");
    }};

    static Set<String> affectedItem;
    static Set<String> affectedItemHuman;
    static ComboBox<String> filterAffectedItemComboBox;


    //Меттод создания динамического фильтра

    static Component createFilterHeader(String labelText, Consumer<String> filterChangeConsumer, GridListDataView<UspIncidentData> AffectedItemDataViewFiltered) {
        Label label = new Label(labelText);
        label.getStyle().set("padding-top", "var(--lumo-space-m)")
                .set("font-size", "var(--lumo-font-size-xs)");

//        Set<String> affectedItem = new HashSet<>(AffectedItemDataViewFiltered.getItems()
//                .map(item -> item.getAFFECTED_ITEM())
//                .collect(Collectors.toSet()));
//        Set<String> affectedItemHuman = new HashSet<String>(affectedItem.stream()
//                .map(item -> affectedItemMap.get(item))
//                .collect(Collectors.toSet()));

        affectedItem = new HashSet<>(AffectedItemDataViewFiltered.getItems()
                .map(item -> item.getAFFECTED_ITEM())
                .collect(Collectors.toSet()));

        affectedItemHuman = new HashSet<>();
        affectedItem.stream()
                .forEach(item -> {
                    //Проверка на услугу не принадлежащую УСП
                    if (affectedItemMap.get(item)==null){
                        affectedItemHuman.add("*");
                    }else {
                        affectedItemHuman.add(affectedItemMap.get(item));
                    }
                });
//        affectedItemHuman = new HashSet<String>(affectedItem.stream()
//                .map(item -> affectedItemMap.get(item))
//                .collect(Collectors.toSet()));

        Label acceptedItemLabel = new Label("ИТ-услуга");
        acceptedItemLabel.getStyle().set("padding-top", "var(--lumo-space-m)")
                .set("font-size", "var(--lumo-font-size-xs)");
        filterAffectedItemComboBox = new ComboBox<>();
        filterAffectedItemComboBox.setPlaceholder("Выберите ИТ-услугу");
        if (affectedItemHuman.contains("*")) affectedItemHuman.remove("*");
        filterAffectedItemComboBox.setItems(affectedItemHuman);
        filterAffectedItemComboBox.setClearButtonVisible(true);
        filterAffectedItemComboBox.addThemeVariants(ComboBoxVariant.LUMO_SMALL);
        filterAffectedItemComboBox.setWidthFull();
        filterAffectedItemComboBox.getStyle().set("max-width", "100%");

        filterAffectedItemComboBox.addValueChangeListener(e -> filterChangeConsumer.accept(getAffectedItem(affectedItemMap, e.getValue())));

        VerticalLayout layout = new VerticalLayout(filterAffectedItemComboBox);
        layout.getThemeList().clear();
        layout.getThemeList().add("spacing-xs");
        layout.setJustifyContentMode(FlexComponent.JustifyContentMode.START);

        return layout;
    }

    static String getAffectedItem(Map<String, String> affectedItemMap, String mapValue) {
        String affectedItem;
        for (Map.Entry<String, String> entry : affectedItemMap.entrySet()) {
            if (entry.getValue().equals(mapValue)) {
                affectedItem = entry.getKey();
                return affectedItem;
            }
        }
        return "";
    }

}
