package ru.sberbank.uspincidentreport.service.zabbix;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hengyunabc.zabbix.api.DefaultZabbixApi;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.RequestBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.sberbank.uspincidentreport.view.Analitics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class ZabbixAPI {

    public static DefaultZabbixApi zabbixApi;
    //Переменные статистики по дефолтных с уровнем критичности 0 триггерам продуктов ОИП
    public volatile static Set<Trigger> listTriggersForSOWA = new HashSet<>();
    public volatile static Set<Trigger> listTriggersForKafka = new HashSet<>();
    public volatile static Set<Trigger> listTriggersForMQ = new HashSet<>();
    public volatile static Set<Trigger> listTriggersForDP = new HashSet<>();
    //Создание списка дефолтных с уровнем критичности 0 триггеров с инцидентами по продуктам ОИП
    public volatile static Set<Trigger> listTriggersWithIncForSOWA = new HashSet<>();
    public volatile static Set<Trigger> listTriggersWithIncForKafka = new HashSet<>();
    public volatile static Set<Trigger> listTriggersWithIncForMQ = new HashSet<>();
    public volatile static Set<Trigger> listTriggersWithIncForDP = new HashSet<>();
    //Создание списка триггеров по продуктам ОИП с заданным уровнем критичности
    public volatile static Set<Trigger> listTriggersWithCustomSeverityForSOWA;
    public volatile static Set<Trigger> listTriggersWithCustomSeverityForKafka;
    public volatile static Set<Trigger> listTriggersWithCustomSeverityForMQ;
    public volatile static Set<Trigger> listTriggersWithCustomSeverityForDP;
    //Создание списка триггеров с инцидентами по продуктам ОИП с заданным уровнем критичности
    public volatile static Set<Trigger> listTriggersWithIncWithCustomSeverityForSOWA;
    public volatile static Set<Trigger> listTriggersWithIncWithCustomSeverityForKafka;
    public volatile static Set<Trigger> listTriggersWithIncWithCustomSeverityForMQ;
    public volatile static Set<Trigger> listTriggersWithIncWithCustomSeverityForDP;
    //Расчет процента покрытия по продуктам ОИП
    public volatile static int percentOfCoverByIncidentForSOWA;
    public volatile static int percentOfCoverByIncidentForKafka;
    public volatile static int percentOfCoverByIncidentForMQ;
    public volatile static int percentOfCoverByIncidentForDP;

    //------------------------------------------------------------------------------------------------------------------

    //Переменные статистики по дефолтных с уровнем критичности 0 продуктов Стандартных платформ
    public volatile static Set<Trigger> listTriggersForNginx = new HashSet<>();
    public volatile static Set<Trigger> listTriggersForWildFly = new HashSet<>();
    public volatile static Set<Trigger> listTriggersForWAS = new HashSet<>();
    public volatile static Set<Trigger> listTriggersForWebLogic = new HashSet<>();
    public volatile static Set<Trigger> listTriggersForSiebel = new HashSet<>();
    //Создание списка дефолтных с уровнем критичности 0 триггеров с инцидентами по продуктам Стандартных платформ
    public volatile static Set<Trigger> listTriggersWithIncForNginx = new HashSet<>();
    public volatile static Set<Trigger> listTriggersWithIncForWildFly = new HashSet<>();
    public volatile static Set<Trigger> listTriggersWithIncForWAS = new HashSet<>();
    public volatile static Set<Trigger> listTriggersWithIncForWebLogic = new HashSet<>();
    public volatile static Set<Trigger> listTriggersWithIncForSiebel = new HashSet<>();
    //Создание списка триггеров по продуктам Стандартных платформ c заданным уровнем критичности
    public volatile static Set<Trigger> listTriggersWithCustomSeverityForNginx;
    public volatile static Set<Trigger> listTriggersWithCustomSeverityForWAS;
    public volatile static Set<Trigger> listTriggersWithCustomSeverityForWildFly;
    public volatile static Set<Trigger> listTriggersWithCustomSeverityForWebLogic;
    public volatile static Set<Trigger> listTriggersWithCustomSeverityForSiebel;
    //Создание списка триггеров с инцидентами по продуктам Стандартных платформ c заданным уровнем критичности
    public volatile static Set<Trigger> listTriggersWithIncWithCustomSeverityForNginx;
    public volatile static Set<Trigger> listTriggersWithIncWithCustomSeverityForWAS;
    public volatile static Set<Trigger> listTriggersWithIncWithCustomSeverityForWildFly;
    public volatile static Set<Trigger> listTriggersWithIncWithCustomSeverityForWebLogic;
    public volatile static Set<Trigger> listTriggersWithIncWithCustomSeverityForSiebel;
    //Расчет процента покрытия по продуктам Стандартных платформ
    public volatile static int percentOfCoverByIncidentForNginx;
    public volatile static int percentOfCoverByIncidentForWildFly;
    public volatile static int percentOfCoverByIncidentForWAS;
    public volatile static int percentOfCoverByIncidentForWebLogic;
    public volatile static int percentOfCoverByIncidentForSiebel;

    //------------------------------------------------------------------------------------------------------------------

    //Создание списка дефолтных с уровнем критичности 0 триггеров продуктов Платформа управления контейнерами (Terra)
    public volatile static Set<Trigger> listTriggersForOpenShift = new HashSet<>();
    //Создание списка дефолтных с уровнем критичности 0 триггеров с инцидентами по продуктам Платформа управления контейнерами (Terra)
    public volatile static Set<Trigger> listTriggersWithIncForOpenShift = new HashSet<>();
    //Создание списка триггеров по продуктам Платформа управления контейнерами (Terra)
    public volatile static Set<Trigger> listTriggersWithCustomSeverityForOpenShift;
    //Создание списка триггеров с инцидентами по продуктам Платформа управления контейнерами (Terra) c заданным уровнем критичности
    public volatile static Set<Trigger> listTriggersWithIncWithCustomSeverityForOpenShift;
    //Расчет процента покрытия по продуктам Платформа управления контейнерами (Terra)
    public volatile static int percentOfCoverByIncidentForOpenShift;

    static List<Discoveryrule> listLLD = new ArrayList<>();

    //Выставление значений тега из appliation.properties
    //Из application.properties нельзя вставить значение в статическую переменную напряму.
    // Поэтому требуется метод setURLs, setUser, setPassword
    static String zabbixAPITagName;
    @Value("${zabbix.api.tag.name}")
    private void setTagName(String zabbixTagName){
        zabbixAPITagName = zabbixTagName;
    }

    static String zabbixAPITagValue;
    @Value("${zabbix.api.tag.value}")
    private void setTagValue(String zabbixTagValue){
        zabbixAPITagValue = zabbixTagValue;
    }


    public static void ZabbixAPIRegistration(String url, String user, String password) {
        zabbixApi = new DefaultZabbixApi(url);
        zabbixApi.init();
        boolean login = zabbixApi.login(user, password);
        System.out.println("login:" + login);
    }

    static Set<Trigger> getTriggersAllForHostName(String hostName) throws JsonProcessingException {
        Set<Trigger> listTrigger = new HashSet<>();
        Request getRequest = RequestBuilder.newBuilder()
                .method("trigger.get")
                .paramEntry("output", new String[]{"triggerid", "description", "priority", "templateid"})
                .paramEntry("host", hostName)
                .paramEntry("inherited", true)
                .paramEntry("monitored", true)
                .build();
        try{

            JSONObject getResponse = zabbixApi.call(getRequest);

            String triggers = getResponse.getJSONArray("result").toJSONString();
            ObjectMapper objectMapper = new ObjectMapper();

            listTrigger = objectMapper.readValue(triggers, new TypeReference<Set<Trigger>>() {
            });
//            System.out.println("Все триггеры по по имени хоста: " + listTrigger);
            return listTrigger;
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return listTrigger;
        }

    }

    static Set<Trigger> getTriggersAllWithIncidentTagForHostName(String hostName, String tag, String value) throws JsonProcessingException {
        Set<Trigger> listTrigger = new HashSet<>();
        JSONArray tagJSONArray = new JSONArray();
        tagJSONArray.add(new JSONObject() {{
            put("tag", tag);
            put("value", value);
            put("operator", "0");
        }});

        Request getRequest = RequestBuilder.newBuilder()
                .method("trigger.get")
                .paramEntry("output", new String[]{"triggerid", "description", "priority", "templateid"})
                .paramEntry("host", hostName)
                .paramEntry("inherited", true)
                .paramEntry("tags", tagJSONArray)
                .paramEntry("monitored", true)
                .build();
        try{
            JSONObject getResponse = zabbixApi.call(getRequest);

            String triggers = getResponse.getJSONArray("result").toJSONString();
            ObjectMapper objectMapper = new ObjectMapper();

            listTrigger = objectMapper.readValue(triggers, new TypeReference<Set<Trigger>>() {
            });
//            System.out.println("Все триггеры c инцидентами по по имени хоста: " + listTrigger);
            return listTrigger;
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return listTrigger;
        }
    }

    //Получение списка LLDId по хосту

    static List<Discoveryrule> getLLDId(String hostName){
        listLLD.clear();
        String hostID;
        JSONObject filterHost = new JSONObject();
        filterHost.put("host", new String[] {hostName});

        //Получение hostID
        Request getRequestHostID = RequestBuilder.newBuilder()
                .method("host.get")
                .paramEntry("output", "hostid")
                .paramEntry("filter", filterHost)
                .build();
        try {
            JSONObject getResponseHostID = zabbixApi.call(getRequestHostID);
            hostID = getResponseHostID.getJSONArray("result").getJSONObject(0).getString("hostid");
//            System.out.println("Хост ID: " + hostID);
        } catch (Exception e) {
            hostID="";
            e.printStackTrace();
        }

        //Получение списка правил обнаоужения LLD по hostID
        Request getRequestLLD = RequestBuilder.newBuilder()
                .method("discoveryrule.get")
                .paramEntry("output", "itemid")
                .paramEntry("hostids", hostID)
                .paramEntry("monitored", true)
                .build();
        try {
            JSONObject getResponseLLD = zabbixApi.call(getRequestLLD);

            String triggers = getResponseLLD.getJSONArray("result").toJSONString();
            ObjectMapper objectMapper = new ObjectMapper();

            listLLD = objectMapper.readValue(triggers, new TypeReference<List<Discoveryrule>>() {
            });
//            System.out.println("Все LLD по hostID: " + listLLD);
        } catch (NullPointerException | JsonProcessingException npe) {
            npe.printStackTrace();
            return listLLD;
        }
        return listLLD;
    }

    //Получение прототипа триггеров-------------------------------------------------------------------------------------
    static Set<Trigger> getTriggerprototypeAllForHostName() throws JsonProcessingException {
        Set<Trigger> listTrigger = new HashSet<>();

        Request getRequest = RequestBuilder.newBuilder()
                .method("triggerprototype.get")
                .paramEntry("output", new String[]{"triggerid", "description", "priority", "templateid"})
                .paramEntry("discoveryids", listLLD.stream().map(item->item.getItemid()).toArray(String[]::new))
                .build();
        try {
            JSONObject getResponse = zabbixApi.call(getRequest);

            String triggers = getResponse.getJSONArray("result").toJSONString();
//            System.out.println("Прототипы триггеров с тегом: " + triggers);
            ObjectMapper objectMapper = new ObjectMapper();

            listTrigger = objectMapper.readValue(triggers, new TypeReference<Set<Trigger>>() {
            });
//            System.out.println("Все прототипы триггеров по по имени хоста: " + listTrigger);

            return listTrigger;
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return listTrigger;
        }

    }

    static Set<Trigger> getTriggerprototypeWithIncidentTagForHostName(String tag, String value) throws JsonProcessingException {
        Set<Trigger> listTrigger;
        Set<Trigger> listTriggerWithInc = new HashSet<>();
        JSONArray tagJSONArray = new JSONArray();
        tagJSONArray.add(new JSONObject() {{
            put("tag", tag);
            put("value", value);
            put("operator", "0");
        }});

        Request getRequest = RequestBuilder.newBuilder()
                .method("triggerprototype.get")
                .paramEntry("output", new String[]{"triggerid", "description", "priority", "templateid"})
                .paramEntry("discoveryids", listLLD.stream().map(item->item.getItemid()).toArray(String[]::new))
                .paramEntry("selectTags", "extend")
//                .paramEntry("tags", tagJSONArray)
                .build();
        try {
            JSONObject getResponse = zabbixApi.call(getRequest);

            String triggers = getResponse.getJSONArray("result").toJSONString();
            ObjectMapper objectMapper = new ObjectMapper();

            listTrigger = objectMapper.readValue(triggers, new TypeReference<Set<Trigger>>() {
            });

            listTrigger.
                    forEach(trigger -> {
                        AtomicBoolean inc = new AtomicBoolean(false);
                        trigger.getTags()
                                .stream()
                                .forEach(map->{
                                    if(map.containsValue(zabbixAPITagName)&&map.containsValue(zabbixAPITagValue)){
                                        inc.set(true);
                                    }
                                });
                        if (inc.get()) {
                            listTriggerWithInc.add(trigger);
                        }
                    });

//            System.out.println("Все прототипы триггеров c инцидентами по имени хоста: " + listTriggerWithInc);
            return listTriggerWithInc;
        } catch (NullPointerException npe) {
            npe.printStackTrace();
            return listTriggerWithInc;
        }

    }


    public static int percentOfCoverByIncident(Set<Trigger> listTriggersAllOfProduct, Set<Trigger> listTriggersWithIncOfProduct) {
        long countTriggersWithIncident = listTriggersWithIncOfProduct.stream().count();
        long countAllTrigger = listTriggersAllOfProduct.stream().count();

        int percentOfCoverByIncident = (int) (((float) countTriggersWithIncident / (float) countAllTrigger) * 100);

        return percentOfCoverByIncident;
    }


    static Set<Trigger> getTriggersWithSeverity(Set<Trigger> listTriggers, String severity) {
        Set<Trigger> listTriggersWithSeverity;
        try{
            if (Analitics.typeSeverity.equals(">=")) {
                listTriggersWithSeverity = listTriggers.stream()
                        .filter(trigger -> Integer.parseInt(trigger.priority) >= (Integer.parseInt(severity)))
                        .collect(Collectors.toSet());
            } else if (Analitics.typeSeverity.equals("=")) {
                listTriggersWithSeverity = listTriggers.stream()
                        .filter(trigger -> Integer.parseInt(trigger.priority) == (Integer.parseInt(severity)))
                        .collect(Collectors.toSet());
            } else {
                listTriggersWithSeverity = listTriggers.stream()
                        .filter(trigger -> Integer.parseInt(trigger.priority) >= (Integer.parseInt(severity)))
                        .collect(Collectors.toSet());
            }
        } catch (NullPointerException e) {
            listTriggersWithSeverity = listTriggers.stream()
                    .filter(trigger -> Integer.parseInt(trigger.priority) >= (Integer.parseInt(severity)))
                    .collect(Collectors.toSet());
        }
//        System.out.println("Триггеры с минимальным уровнем критичности: " + severity + listTriggersWithSeverity);
        return listTriggersWithSeverity;
    }
    static Template getTemplatebyID(String triggerids) throws JsonProcessingException, IndexOutOfBoundsException {
        Template template = new Template();
        Request getRequest = RequestBuilder.newBuilder()
//                .method("host.get").paramEntry("filter", filter)
                .method("template.get")
                .paramEntry("output", new String[]{"host", "name", "templateid", "description", "status"})
                .paramEntry("triggerids", triggerids)
                .build();
        JSONObject getResponse = zabbixApi.call(getRequest);

        if (!getResponse.getJSONArray("result").isEmpty()) {
            String triggers = getResponse.getJSONArray("result").getJSONObject(0).toJSONString();
            ObjectMapper objectMapper = new ObjectMapper();

            template = objectMapper.readValue(triggers, Template.class);
//            System.out.println("Шаблон: " + template.toString());
        } else {

            template.setHost("");
            template.setDescription("");
            template.setName("");
            template.setStatus("");
            template.setTemplateid("");
        }
        return template;
    }

    public static Set<Trigger> triggerListWithTemplateName(String severity, String hostName) throws JsonProcessingException {
        Set<Trigger> triggerListWithTemplateName = new HashSet<>();
        Set<Trigger> triggerprototypeListWithTemplateName = new HashSet<>();
        getTriggersWithSeverity(getTriggersAllForHostName(hostName), severity)
                .stream()
                .forEach(trigger -> {
                    try {
                        trigger.setTemplateName(getTemplatebyID(trigger.getTemplateid()).getName());
                        triggerListWithTemplateName.add(trigger);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
        getLLDId(hostName);
        getTriggersWithSeverity(getTriggerprototypeAllForHostName(), severity)
                .stream()
                .forEach(trigger -> {
                    try {
                        trigger.setTemplateName(getTemplatebyID(trigger.getTemplateid()).getName());
                        triggerprototypeListWithTemplateName.add(trigger);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
        triggerListWithTemplateName.addAll(triggerprototypeListWithTemplateName);

//        System.out.println(triggerListWithTemplateName);
        return triggerListWithTemplateName;

    }

    public static Set<Trigger> triggerListWithIncidentTagWithTemplateName(String tag, String value, String severity, String hostName) throws JsonProcessingException {
        Set<Trigger> triggerListWithIncidentTagWithTemplateName = new HashSet<>();
        Set<Trigger> triggerprototypeListWithTemplateName = new HashSet<>();
        getTriggersWithSeverity(getTriggersAllWithIncidentTagForHostName(hostName, tag, value), severity)
                .stream()
                .forEach(trigger -> {
                    try {
                        trigger.setTemplateName(getTemplatebyID(trigger.getTemplateid()).getName());
                        triggerListWithIncidentTagWithTemplateName.add(trigger);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
        getLLDId(hostName);
        getTriggersWithSeverity(getTriggerprototypeWithIncidentTagForHostName(tag, value), severity)
                .stream()
                .forEach(trigger -> {
                    try {
                        trigger.setTemplateName(getTemplatebyID(trigger.getTemplateid()).getName());
                        triggerprototypeListWithTemplateName.add(trigger);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });

        triggerListWithIncidentTagWithTemplateName.addAll(triggerprototypeListWithTemplateName);

        return triggerListWithIncidentTagWithTemplateName;

    }

    public static void getTriggerListUSP(String severity) throws JsonProcessingException {
        //Создание списка всех триггеров по продуктам ОИП c заданным уровнем критичности
        listTriggersWithCustomSeverityForSOWA = getTriggersWithSeverity(listTriggersForSOWA, severity);
        listTriggersWithCustomSeverityForKafka = getTriggersWithSeverity(listTriggersForKafka, severity);
        listTriggersWithCustomSeverityForMQ = getTriggersWithSeverity(listTriggersForMQ, severity);
        listTriggersWithCustomSeverityForDP = getTriggersWithSeverity(listTriggersForDP, severity);

        //Создание списка триггеров с инцидентами по продуктам ОИП с заданным уровнем критичности
        listTriggersWithIncWithCustomSeverityForSOWA = getTriggersWithSeverity(listTriggersWithIncForSOWA, severity);
        listTriggersWithIncWithCustomSeverityForKafka = getTriggersWithSeverity(listTriggersWithIncForKafka, severity);
        listTriggersWithIncWithCustomSeverityForMQ = getTriggersWithSeverity(listTriggersWithIncForMQ, severity);
        listTriggersWithIncWithCustomSeverityForDP = getTriggersWithSeverity(listTriggersWithIncForDP, severity);

        //Расчет процента покрытия по продуктам ОИП c заданным уровнем критичности
        percentOfCoverByIncidentForSOWA = percentOfCoverByIncident(listTriggersWithCustomSeverityForSOWA, listTriggersWithIncWithCustomSeverityForSOWA);
        percentOfCoverByIncidentForKafka = percentOfCoverByIncident(listTriggersWithCustomSeverityForKafka, listTriggersWithIncWithCustomSeverityForKafka);
        percentOfCoverByIncidentForMQ = percentOfCoverByIncident(listTriggersWithCustomSeverityForMQ, listTriggersWithIncWithCustomSeverityForMQ);
        percentOfCoverByIncidentForDP = percentOfCoverByIncident(listTriggersWithCustomSeverityForDP, listTriggersWithIncWithCustomSeverityForDP);

        //--------------------------------------------------------------------------------------------------------------

        //Создание списка триггеров по продуктам Стандартных платформ c заданным уровнем критичности
        listTriggersWithCustomSeverityForNginx = getTriggersWithSeverity(listTriggersForNginx, severity);
        listTriggersWithCustomSeverityForWAS = getTriggersWithSeverity(listTriggersForWAS, severity);
        listTriggersWithCustomSeverityForWildFly = getTriggersWithSeverity(listTriggersForWildFly, severity);
        listTriggersWithCustomSeverityForWebLogic = getTriggersWithSeverity(listTriggersForWebLogic, severity);
        listTriggersWithCustomSeverityForSiebel = getTriggersWithSeverity(listTriggersForSiebel, severity);

        //Создание списка триггеров с инцидентами по продуктам Стандартных платформ c заданным уровнем критичности
        listTriggersWithIncWithCustomSeverityForNginx = getTriggersWithSeverity(listTriggersWithIncForNginx, severity);
        listTriggersWithIncWithCustomSeverityForWAS = getTriggersWithSeverity(listTriggersWithIncForWAS, severity);
        listTriggersWithIncWithCustomSeverityForWildFly = getTriggersWithSeverity(listTriggersWithIncForWildFly, severity);
        listTriggersWithIncWithCustomSeverityForWebLogic = getTriggersWithSeverity(listTriggersWithIncForWebLogic, severity);
        listTriggersWithIncWithCustomSeverityForSiebel = getTriggersWithSeverity(listTriggersWithIncForSiebel, severity);

        //Расчет процента покрытия по продуктам Стандартных платформ c заданным уровнем критичности
        percentOfCoverByIncidentForNginx = percentOfCoverByIncident(listTriggersWithCustomSeverityForNginx,
                listTriggersWithIncWithCustomSeverityForNginx);
        percentOfCoverByIncidentForWAS = percentOfCoverByIncident(listTriggersWithCustomSeverityForWAS,
                listTriggersWithIncWithCustomSeverityForWAS);
        percentOfCoverByIncidentForWildFly = percentOfCoverByIncident(listTriggersWithCustomSeverityForWildFly,
                listTriggersWithIncWithCustomSeverityForWildFly);
        percentOfCoverByIncidentForWebLogic = percentOfCoverByIncident(listTriggersWithCustomSeverityForWebLogic,
                listTriggersWithIncWithCustomSeverityForWebLogic);
        percentOfCoverByIncidentForSiebel = percentOfCoverByIncident(listTriggersWithCustomSeverityForSiebel,
                listTriggersWithIncWithCustomSeverityForSiebel);

        //Создание списка триггеров по продуктам Платформа управления контейнерами (Terra)
        listTriggersWithCustomSeverityForOpenShift = getTriggersWithSeverity(listTriggersForOpenShift, severity);

        //Создание списка триггеров с инцидентами по продуктам Платформа управления контейнерами (Terra) c заданным уровнем критичности
        listTriggersWithIncWithCustomSeverityForOpenShift = getTriggersWithSeverity(listTriggersWithIncForOpenShift, severity);

        //Расчет процента покрытия по продуктам Платформа управления контейнерами (Terra) c заданным уровнем критичности
        percentOfCoverByIncidentForOpenShift = percentOfCoverByIncident(listTriggersWithCustomSeverityForOpenShift,
                listTriggersWithIncWithCustomSeverityForOpenShift);

    }

    public static Set<Trigger> listTriggersWithoutIncWithCustomSeverity(Set<Trigger> listTriggersWithCustomSeverity, Set<Trigger> listTriggersWithIncWithCustomSeverity) {
        //Создание списка триггеров без инцидента по продуктам заданным уровнем критичности
        Set<Trigger> listTriggersWithoutIncWithCustomSeverity = listTriggersWithCustomSeverity.stream()
                .filter(element -> !listTriggersWithIncWithCustomSeverity.contains(element))
                .collect(Collectors.toSet());
//        System.out.println("Все триггеры" + listTriggersWithCustomSeverity + " ;" + "Триггеры с инцидентами: " +
//                listTriggersWithIncWithCustomSeverity + "Тригегры без инцидентов: " + listTriggersWithoutIncWithCustomSeverity);
        return listTriggersWithoutIncWithCustomSeverity;
    }

    //Этот метод запускается при старте приложения и собирает сатистику по триггерам с уровнем критичности 0
    public static void getTriggerStatisticDefault(String severity, String tagName, String tagValue, String hostSOWA,
                                                  String hostKafka, String hostMQ, String hostDP, String hostNginx, String hostWAS,
                                                  String hostWildFly, String hostWeblogic, String hostSiebel, String hostOpenShift) throws JsonProcessingException {

        //Создание списка всех триггеров по продуктам ОИП
        listTriggersForSOWA.addAll(triggerListWithTemplateName(severity, hostSOWA));
        listTriggersForKafka.addAll(triggerListWithTemplateName(severity, hostKafka));
        listTriggersForMQ.addAll(triggerListWithTemplateName(severity, hostMQ));
        listTriggersForDP.addAll(triggerListWithTemplateName(severity, hostDP));

        //Создание списка триггеров с инцидентами по продуктам ОИП
        listTriggersWithIncForSOWA.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, hostSOWA));
        listTriggersWithIncForKafka.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, hostKafka));
        listTriggersWithIncForMQ.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, hostMQ));
        listTriggersWithIncForDP.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, hostDP));

        //--------------------------------------------------------------------------------------------------------------

        //Создание списка всех триггеров по продуктам Стандартных платформ
        listTriggersForNginx.addAll(triggerListWithTemplateName(severity, hostNginx));
        listTriggersForWAS.addAll(triggerListWithTemplateName(severity, hostWAS));
        listTriggersForWildFly.addAll(triggerListWithTemplateName(severity, hostWildFly));
        listTriggersForWebLogic.addAll(triggerListWithTemplateName(severity, hostWeblogic));
        listTriggersForSiebel.addAll(triggerListWithTemplateName(severity, hostSiebel));

        //Создание списка триггеров с инцидентами по продуктам Стандартных платформ
        listTriggersWithIncForNginx.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, hostNginx));
        listTriggersWithIncForWAS.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, hostWAS));
        listTriggersWithIncForWildFly.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, hostWildFly));
        listTriggersWithIncForWebLogic.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, hostWeblogic));
        listTriggersWithIncForSiebel.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, hostSiebel));

        //--------------------------------------------------------------------------------------------------------------

        //Создание списка всех триггеров по продуктам Платформа управления контейнерами (Terra)
        listTriggersForOpenShift.addAll(triggerListWithTemplateName(severity, hostOpenShift));
        //Создание списка триггеров с инцидентами по продуктам Платформа управления контейнерами (Terra)
        listTriggersWithIncForOpenShift.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, hostOpenShift));

        zabbixApi.destroy();
    }
}

