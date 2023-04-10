package ru.sberbank.uspincidentreport.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.converter.json.GsonBuilderUtils;
import org.springframework.stereotype.Component;
import ru.sberbank.uspincidentreport.UspIncidentReportApplication;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

@Component
public class USPIncResolutionGuideMap {

    // Создание MAP текст в проблеме: http ссылка на инструкцию для устранения
    private static Map<String, String> CI02192117KafkaMap = createMapIncResolution(UspIncidentReportApplication.CI02192117KafkaMapFile);
    private static Map<String, String> CI02192118SOWAMap =createMapIncResolution(UspIncidentReportApplication.CI02192118SOWAMapFile);
    private static Map<String, String> CI02021290DPMap =createMapIncResolution(UspIncidentReportApplication.CI02021290DPMapFile);
    private static Map<String, String> CI02021291MQMap =createMapIncResolution(UspIncidentReportApplication.CI02021291MQMapFile);


    //Инциденты УСП
    static Map<String, Map<String, String>> USPIncResolutionGuideMap  = new HashMap<>() {{
        put("CI02192117", CI02192117KafkaMap);
        put("CI02192118", CI02192118SOWAMap);
        put("CI02021290", CI02021290DPMap);
        put("CI02021291", CI02021291MQMap);
    }};

    // Заполнение map из файла
    public static Map<String, String> createMapIncResolution(String filePath) {
        Map<String, String> MapIncResolution = new HashMap<>();
        try (Stream<String> lines = Files.lines(Paths.get(filePath))) {
            lines.filter(line -> line.contains(";"))
                    .forEach(line -> {
                        String[] keyValuePair = line.split(";", 2);
                        String key = keyValuePair[0];
                        String value = keyValuePair[1];
                        MapIncResolution.put(key, value);

                    });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return MapIncResolution;
    }


    public static String GetResolutionGuide(String Affected_Item, String Problem) {

        try  {
            if (    Affected_Item.equals("CI02192117") ||
                    Affected_Item.equals("CI02192118") ||
                    Affected_Item.equals("CI02021290")||
                    Affected_Item.equals("CI02021291")) {

                for (Map.Entry<String, String> entry : USPIncResolutionGuideMap.get(Affected_Item).entrySet()) {
                    if (Problem.contains(entry.getKey())) {
                        return entry.getValue();
                    }
                }
                return "https://confluence.ca.sbrf.ru/";
            }
        } catch (Exception e) {
            return "https://confluence.ca.sbrf.ru/";
        }
        return "https://confluence.ca.sbrf.ru/";
    }

}
