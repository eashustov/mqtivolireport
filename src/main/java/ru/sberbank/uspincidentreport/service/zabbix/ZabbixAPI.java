package ru.sberbank.uspincidentreport.service.zabbix;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.hengyunabc.zabbix.api.DefaultZabbixApi;
import io.github.hengyunabc.zabbix.api.Request;
import io.github.hengyunabc.zabbix.api.RequestBuilder;
import ru.sberbank.uspincidentreport.view.Analitics;

import java.awt.image.ImageProducer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ZabbixAPI {

    public static DefaultZabbixApi zabbixApi;
    //Переменные статистики по дефолтных с уровнем критичности 0 триггерам продуктов ОИП
    public volatile static List<Trigger> listTriggersForSOWA = new ArrayList<>();
    public volatile static List<Trigger> listTriggersForKafka = new ArrayList<>();
    public volatile static List<Trigger> listTriggersForMQ = new ArrayList<>();
    public volatile static List<Trigger> listTriggersForDP = new ArrayList<>();
    //Создание списка дефолтных с уровнем критичности 0 триггеров с инцидентами по продуктам ОИП
    public volatile static List<Trigger> listTriggersWithIncForSOWA = new ArrayList<>();
    public volatile static List<Trigger> listTriggersWithIncForKafka = new ArrayList<>();
    public volatile static List<Trigger> listTriggersWithIncForMQ = new ArrayList<>();
    public volatile static List<Trigger> listTriggersWithIncForDP = new ArrayList<>();
    //Создание списка триггеров по продуктам ОИП с заданным уровнем критичности
    public volatile static List<Trigger> listTriggersWithCustomSeverityForSOWA;
    public volatile static List<Trigger> listTriggersWithCustomSeverityForKafka;
    public volatile static List<Trigger> listTriggersWithCustomSeverityForMQ;
    public volatile static List<Trigger> listTriggersWithCustomSeverityForDP;
    //Создание списка триггеров с инцидентами по продуктам ОИП с заданным уровнем критичности
    public volatile static List<Trigger> listTriggersWithIncWithCustomSeverityForSOWA;
    public volatile static List<Trigger> listTriggersWithIncWithCustomSeverityForKafka;
    public volatile static List<Trigger> listTriggersWithIncWithCustomSeverityForMQ;
    public volatile static List<Trigger> listTriggersWithIncWithCustomSeverityForDP;
    //Расчет процента покрытия по продуктам ОИП
    public volatile static int percentOfCoverByIncidentForSOWA;
    public volatile static int percentOfCoverByIncidentForKafka;
    public volatile static int percentOfCoverByIncidentForMQ;
    public volatile static int percentOfCoverByIncidentForDP;

    //------------------------------------------------------------------------------------------------------------------

    //Переменные статистики по дефолтных с уровнем критичности 0 продуктов Стандартных платформ
    public volatile static List<Trigger> listTriggersForNginx = new ArrayList<>();
    public volatile static List<Trigger> listTriggersForWildFly = new ArrayList<>();
    public volatile static List<Trigger> listTriggersForWAS = new ArrayList<>();
    public volatile static List<Trigger> listTriggersForWebLogic = new ArrayList<>();
    //Создание списка дефолтных с уровнем критичности 0 триггеров с инцидентами по продуктам Стандартных платформ
    public volatile static List<Trigger> listTriggersWithIncForNginx = new ArrayList<>();
    public volatile static List<Trigger> listTriggersWithIncForWildFly = new ArrayList<>();
    public volatile static List<Trigger> listTriggersWithIncForWAS = new ArrayList<>();
    public volatile static List<Trigger> listTriggersWithIncForWebLogic = new ArrayList<>();
    //Создание списка триггеров по продуктам Стандартных платформ c заданным уровнем критичности
    public volatile static List<Trigger> listTriggersWithCustomSeverityForNginx;
    public volatile static List<Trigger> listTriggersWithCustomSeverityForWAS;
    public volatile static List<Trigger> listTriggersWithCustomSeverityForWildFly;
    public volatile static List<Trigger> listTriggersWithCustomSeverityForWebLogic;

    //Создание списка триггеров с инцидентами по продуктам Стандартных платформ c заданным уровнем критичности
    public volatile static List<Trigger> listTriggersWithIncWithCustomSeverityForNginx;
    public volatile static List<Trigger> listTriggersWithIncWithCustomSeverityForWAS;
    public volatile static List<Trigger> listTriggersWithIncWithCustomSeverityForWildFly;
    public volatile static List<Trigger> listTriggersWithIncWithCustomSeverityForWebLogic;
    //Расчет процента покрытия по продуктам Стандартных платформ
    public volatile static int percentOfCoverByIncidentForNginx;
    public volatile static int percentOfCoverByIncidentForWildFly;
    public volatile static int percentOfCoverByIncidentForWAS;
    public volatile static int percentOfCoverByIncidentForWebLogic;

    //------------------------------------------------------------------------------------------------------------------

    //Создание списка дефолтных с уровнем критичности 0 триггеров продуктов Платформа управления контейнерами (Terra)
    public volatile static List<Trigger> listTriggersForOpenShift = new ArrayList<>();
    //Создание списка дефолтных с уровнем критичности 0 триггеров с инцидентами по продуктам Платформа управления контейнерами (Terra)
    public volatile static List<Trigger> listTriggersWithIncForOpenShift = new ArrayList<>();
    //Создание списка триггеров по продуктам Платформа управления контейнерами (Terra)
    public volatile static List<Trigger> listTriggersWithCustomSeverityForOpenShift;
    //Создание списка триггеров с инцидентами по продуктам Платформа управления контейнерами (Terra) c заданным уровнем критичности
    public volatile static List<Trigger> listTriggersWithIncWithCustomSeverityForOpenShift;
    //Расчет процента покрытия по продуктам Платформа управления контейнерами (Terra)
    public volatile static int percentOfCoverByIncidentForOpenShift;

    //------------------------------------------------------------------------------------------------------------------


//    public static void main(String[] args) throws JsonProcessingException {
//
//        ZabbixAPIRegistration();
//
////        long countTriggersWithIncident = getTriggersForGroupNameWithIncidentTag("Linux servers", "scope", "availability").stream().count();
////        long countAllTrigger = getTriggersForGroupName("Linux servers").stream().count();
////        int percentOfCoverByIncident = (int)(((float)countTriggersWithIncident/(float)countAllTrigger)*100);
////        System.out.println("Триггеры с инцидентами: " + countTriggersWithIncident);
////        System.out.println("Все триггеры: " + countAllTrigger);
////        System.out.println("Процент покрытия: " + percentOfCoverByIncident);
//
////        List<Trigger> triggers_1 = getTriggersAllForGroupIDs(getHostGropuIDbyName(getHostGroups("Zabbix servers", "Linux servers")));
////        List<Trigger> triggers_2 = getTriggesWithIncidentTagForGroupIDs(getHostGropuIDbyName(getHostGroups("Zabbix servers", "Linux servers")),
////                "scope", "availability");
//
////        System.out.println(getTriggersWithSeverity(getTriggersAllForGroupIDs(getHostGropuIDbyName(getHostGroups("Zabbix servers",
////                "Linux servers"))),
////                "3"));
//
////        System.out.println("Триггеры:" + getTriggersWithSeverity(
////                getTriggersAllForGroupIDs(getHostGropuIDbyName(getHostGroups("Zabbix servers","Linux servers"))),
////                "3"));
//
////        System.out.println("Имя шаблона" + getTemplatebyID("17491").getName());
//
//        //Триггеры для хостгрупп без учета наличия тега
//        triggerListWithTemplateName ("3", "Zabbix servers",
//                "Linux servers");
//
//        //Триггеры для хостгрупп с учетом тега
//        triggerListWithIncidentTagWithTemplateName("scope", "availability", "3", "Zabbix servers","Linux servers");
//
//    }


    public static void ZabbixAPIRegistration(String url, String user, String password) {
        zabbixApi = new DefaultZabbixApi(url);
        zabbixApi.init();
        boolean login = zabbixApi.login(user, password);
        System.out.println("login:" + login);
    }

    public static int percentOfCoverByIncident(List<Trigger> listTriggersAllOfProduct, List<Trigger> listTriggersWithIncOfProduct) {
        long countTriggersWithIncident = listTriggersWithIncOfProduct.stream().count();
        long countAllTrigger = listTriggersAllOfProduct.stream().count();

        int percentOfCoverByIncident = (int) (((float) countTriggersWithIncident / (float) countAllTrigger) * 100);

        return percentOfCoverByIncident;
    }


    static List<Trigger> getTriggersAllForGroupIDs(List<String> groupids) throws JsonProcessingException {

        Request getRequest = RequestBuilder.newBuilder()
                .method("trigger.get")
                .paramEntry("output", new String[]{"triggerid", "description", "priority", "templateid"})
                .paramEntry("groupids", groupids)
//                .paramEntry("min_severity", 2)
//                .paramEntry("tags", tagJSONArray)
                .build();
        JSONObject getResponse = zabbixApi.call(getRequest);

        String triggers = getResponse.getJSONArray("result").toJSONString();
        ObjectMapper objectMapper = new ObjectMapper();

        List<Trigger> listTrigger = objectMapper.readValue(triggers, new TypeReference<List<Trigger>>() {
        });
        System.out.println("Все триггеры по IDs групп: " + listTrigger);

        return listTrigger;


    }

    static List<Trigger> getTriggersAllWithIncidentTagForGroupIDs(List<String> groupids, String tag, String value) throws JsonProcessingException {

        JSONArray tagJSONArray = new JSONArray();
        tagJSONArray.add(new JSONObject() {{
            put("tag", tag);
            put("value", value);
            put("operator", "0");
        }});

        Request getRequest = RequestBuilder.newBuilder()
                .method("trigger.get")
                .paramEntry("output", new String[]{"triggerid", "description", "priority", "templateid"})
                .paramEntry("groupids", groupids)
//                .paramEntry("min_severity", severity)
                .paramEntry("tags", tagJSONArray)
                .build();
        JSONObject getResponse = zabbixApi.call(getRequest);

        String triggers = getResponse.getJSONArray("result").toJSONString();
        ObjectMapper objectMapper = new ObjectMapper();

        List<Trigger> listTrigger = objectMapper.readValue(triggers, new TypeReference<List<Trigger>>() {
        });
        System.out.println("Все триггеры c инцидентами по IDs групп: " + listTrigger);

        return listTrigger;

    }

    //Получение прототипа триггеров-------------------------------------------------------------------------------------
    static List<Trigger> getTriggerprototypeAllForGroupIDs(List<String> groupids) throws JsonProcessingException {

        Request getRequest = RequestBuilder.newBuilder()
                .method("triggerprototype.get")
                .paramEntry("output", new String[]{"triggerid", "description", "priority", "templateid"})
                .paramEntry("groupids", groupids)
//                .paramEntry("min_severity", 2)
//                .paramEntry("tags", tagJSONArray)
                .build();
        JSONObject getResponse = zabbixApi.call(getRequest);

        String triggers = getResponse.getJSONArray("result").toJSONString();
        ObjectMapper objectMapper = new ObjectMapper();

        List<Trigger> listTrigger = objectMapper.readValue(triggers, new TypeReference<List<Trigger>>() {
        });
        System.out.println("Все прототипы триггеров по IDs групп: " + listTrigger);

        return listTrigger;


    }

    static List<Trigger> getTriggerprototypeWithIncidentTagForGroupIDs(List<String> groupids, String tag, String value) throws JsonProcessingException {

        JSONArray tagJSONArray = new JSONArray();
        tagJSONArray.add(new JSONObject() {{
            put("tag", tag);
            put("value", value);
            put("operator", "0");
        }});

        Request getRequest = RequestBuilder.newBuilder()
                .method("triggerprototype.get")
                .paramEntry("output", new String[]{"triggerid", "description", "priority", "templateid"})
                .paramEntry("groupids", groupids)
//                .paramEntry("min_severity", severity)
                .paramEntry("tags", tagJSONArray)
                .build();
        JSONObject getResponse = zabbixApi.call(getRequest);

        String triggers = getResponse.getJSONArray("result").toJSONString();
        ObjectMapper objectMapper = new ObjectMapper();

        List<Trigger> listTrigger = objectMapper.readValue(triggers, new TypeReference<List<Trigger>>() {
        });
        System.out.println("Все прототипы триггеров c инцидентами по IDs групп: " + listTrigger);

        return listTrigger;

    }

    //Получение хост групп

    static List<HostGroup> getHostGroups(String... groups) throws JsonProcessingException {

        JSONObject filter = new JSONObject();
        List<String> listGroups = new ArrayList<>();
        for (String group : groups) {
            listGroups.add(group);
        }

        filter.put("name", listGroups);

        Request getRequest = RequestBuilder.newBuilder()
                .method("hostgroup.get")
                .paramEntry("output", "extend")
                .paramEntry("filter", filter)
                .build();
        JSONObject getResponse = zabbixApi.call(getRequest);

        String triggers = getResponse.getJSONArray("result").toJSONString();
        ObjectMapper objectMapper = new ObjectMapper();

        List<HostGroup> listHostGroup = objectMapper.readValue(triggers, new TypeReference<List<HostGroup>>() {
        });
        System.out.println("Хостгруппы: " + listHostGroup);

        return listHostGroup;


    }

    static List<String> getHostGropuIDbyName(List<HostGroup> hostGroups) {

        List<String> hostGroupIDs = hostGroups.stream()
                .map(hostgroup -> hostgroup.groupid)
                .collect(Collectors.toList());
        return hostGroupIDs;

    }

    //------------------------------------------------------------------------------------------------------------------

    static List<Trigger> getTriggersForGroupNameWithIncidentTag(String group, String tag, String value) throws JsonProcessingException {

        JSONArray tagJSONArray = new JSONArray();
        tagJSONArray.add(new JSONObject() {{
            put("tag", tag);
            put("value", value);
            put("operator", "0");
        }});

        Request getRequest = RequestBuilder.newBuilder()
//                .method("host.get").paramEntry("filter", filter)
                .method("trigger.get")
                .paramEntry("output", new String[]{"triggerid", "description", "priority", "templateid"})
                .paramEntry("group", group)
//                .paramEntry("min_severity", 2)
                .paramEntry("tags", tagJSONArray)
                .build();
        JSONObject getResponse = zabbixApi.call(getRequest);
//            System.err.println(getResponse);
//              System.out.println(getResponse);
//        String hostid = getResponse.getJSONArray("result")
//                .getJSONObject(0).getString("hostid");
        String triggers = getResponse.getJSONArray("result").toJSONString();
//        System.out.println(triggers);
        ObjectMapper objectMapper = new ObjectMapper();

        List<Trigger> listTrigger = objectMapper.readValue(triggers, new TypeReference<List<Trigger>>() {
        });
        System.out.println("Триггеры с инцидентами: " + listTrigger);

        return listTrigger;
    }

    static List<Trigger> getTriggersForGroupName(String group) throws JsonProcessingException {

        Request getRequest = RequestBuilder.newBuilder()
//                .method("host.get").paramEntry("filter", filter)
                .method("trigger.get")
                .paramEntry("output", new String[]{"triggerid", "description", "priority", "templateid"})
                .paramEntry("group", group)
//                .paramEntry("min_severity", 2)
//                .paramEntry("tags", tagJSONArray)
                .build();
        JSONObject getResponse = zabbixApi.call(getRequest);

        String triggers = getResponse.getJSONArray("result").toJSONString();
        ObjectMapper objectMapper = new ObjectMapper();

        List<Trigger> listTrigger = objectMapper.readValue(triggers, new TypeReference<List<Trigger>>() {
        });
        System.out.println("Все триггеры: " + listTrigger);

        return listTrigger;
    }

    static List<Trigger> getTriggersWithSeverity(List<Trigger> listTriggers, String severity) {
        List<Trigger> listTriggersWithSeverity = listTriggers.stream()
                .filter(trigger -> Integer.parseInt(trigger.priority) >= (Integer.parseInt(severity)))
                .collect(Collectors.toList());
        System.out.println("Триггеры с минимальным уровнем критичности: " + severity + listTriggersWithSeverity);
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

    public static List<Trigger> triggerListWithTemplateName(String severity, String... groups) throws JsonProcessingException {
        List<Trigger> triggerListWithTemplateName = new ArrayList<>();
        List<Trigger> triggerprototypeListWithTemplateName = new ArrayList<>();
        getTriggersWithSeverity(getTriggersAllForGroupIDs(getHostGropuIDbyName(getHostGroups(groups))),
                severity)
                .stream()
                .forEach(trigger -> {
                    try {
                        trigger.setTemplateName(getTemplatebyID(trigger.getTemplateid()).getName());
                        triggerListWithTemplateName.add(trigger);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });

        getTriggersWithSeverity(getTriggerprototypeAllForGroupIDs(getHostGropuIDbyName(getHostGroups(groups))),
                severity)
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

        System.out.println(triggerListWithTemplateName);
        return triggerListWithTemplateName;

    }

    public static List<Trigger> triggerListWithIncidentTagWithTemplateName(String tag, String value, String severity, String... groups) throws JsonProcessingException {
        List<Trigger> triggerListWithIncidentTagWithTemplateName = new ArrayList<>();
        List<Trigger> triggerprototypeListWithTemplateName = new ArrayList<>();
        getTriggersWithSeverity(getTriggersAllWithIncidentTagForGroupIDs(getHostGropuIDbyName(getHostGroups(groups)), tag, value), severity)
                .stream()
                .forEach(trigger -> {
                    try {
                        trigger.setTemplateName(getTemplatebyID(trigger.getTemplateid()).getName());
                        triggerListWithIncidentTagWithTemplateName.add(trigger);
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                });
        getTriggersWithSeverity(getTriggerprototypeWithIncidentTagForGroupIDs(getHostGropuIDbyName(getHostGroups(groups)), tag, value), severity)
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

        System.out.println(triggerListWithIncidentTagWithTemplateName);
        return triggerListWithIncidentTagWithTemplateName;

    }

    public static void getTriggerListForGroupUSP(String severity) throws JsonProcessingException {
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

        //Создание списка триггеров с инцидентами по продуктам Стандартных платформ c заданным уровнем критичности
        listTriggersWithIncWithCustomSeverityForNginx = getTriggersWithSeverity(listTriggersWithIncForNginx, severity);
        listTriggersWithIncWithCustomSeverityForWAS = getTriggersWithSeverity(listTriggersWithIncForWAS, severity);
        listTriggersWithIncWithCustomSeverityForWildFly = getTriggersWithSeverity(listTriggersWithIncForWildFly, severity);
        listTriggersWithIncWithCustomSeverityForWebLogic = getTriggersWithSeverity(listTriggersWithIncForWebLogic, severity);

        //Расчет процента покрытия по продуктам Стандартных платформ c заданным уровнем критичности
        percentOfCoverByIncidentForNginx = percentOfCoverByIncident(listTriggersWithCustomSeverityForNginx,
                listTriggersWithIncWithCustomSeverityForNginx);
        percentOfCoverByIncidentForWAS = percentOfCoverByIncident(listTriggersWithCustomSeverityForWAS,
                listTriggersWithIncWithCustomSeverityForWAS);
        percentOfCoverByIncidentForWildFly = percentOfCoverByIncident(listTriggersWithCustomSeverityForWildFly,
                listTriggersWithIncWithCustomSeverityForWildFly);
        percentOfCoverByIncidentForWebLogic = percentOfCoverByIncident(listTriggersWithCustomSeverityForWebLogic,
                listTriggersWithIncWithCustomSeverityForWebLogic);

        //Создание списка триггеров по продуктам Платформа управления контейнерами (Terra)
        listTriggersWithCustomSeverityForOpenShift = getTriggersWithSeverity(listTriggersForOpenShift, severity);

        //Создание списка триггеров с инцидентами по продуктам Платформа управления контейнерами (Terra) c заданным уровнем критичности
        listTriggersWithIncWithCustomSeverityForOpenShift = getTriggersWithSeverity(listTriggersWithIncForOpenShift, severity);

        //Расчет процента покрытия по продуктам Платформа управления контейнерами (Terra) c заданным уровнем критичности
        percentOfCoverByIncidentForOpenShift = percentOfCoverByIncident(listTriggersWithCustomSeverityForOpenShift,
                listTriggersWithIncWithCustomSeverityForOpenShift);

    }

    public static List<Trigger> listTriggersWithoutIncWithCustomSeverity(List<Trigger> listTriggersWithCustomSeverity, List<Trigger> listTriggersWithIncWithCustomSeverity)
    {
    //Создание списка триггеров без инцидента по продуктам заданным уровнем критичности
    List<Trigger> listTriggersWithoutIncWithCustomSeverity = listTriggersWithCustomSeverity.stream()
            .filter(element -> !listTriggersWithIncWithCustomSeverity.toString().contains(element.toString()))
            .collect(Collectors.toList());
        System.out.println("Все триггеры" + listTriggersWithCustomSeverity + " ;" + "Триггеры с инцидентами: " +
                listTriggersWithIncWithCustomSeverity);
//        List<Trigger> listTriggersWithoutIncWithCustomSeverity = new ArrayList<>(listTriggersWithCustomSeverity);
//        listTriggersWithoutIncWithCustomSeverity.removeAll(listTriggersWithIncWithCustomSeverity);
    return listTriggersWithoutIncWithCustomSeverity;
    }

    //Этот метод запускается при старте приложения и собирает сатистику по триггерам с уровнем критичности 0
    public static void getTriggerStatisticDefault(String severity, String tagName, String tagValue, String[] zabbixGroupsSOWA,
    String[] zabbixGroupsKafka, String[] zabbixGroupsMQ, String[] zabbixGroupsDP, String[] zabbixGroupsNginx, String[] zabbixGroupsWAS,
    String[] zabbixGroupsWildFly, String[] zabbixGroupsWeblogic, String[] zabbixGroupsOpenShift) throws JsonProcessingException {

        //Создание списка всех триггеров по продуктам ОИП
        listTriggersForSOWA.addAll(triggerListWithTemplateName (severity, zabbixGroupsSOWA));
        listTriggersForKafka.addAll(triggerListWithTemplateName (severity, zabbixGroupsKafka));
        listTriggersForMQ.addAll(triggerListWithTemplateName (severity, zabbixGroupsMQ));
        listTriggersForDP.addAll(triggerListWithTemplateName (severity, zabbixGroupsDP));

        //Создание списка триггеров с инцидентами по продуктам ОИП
        listTriggersWithIncForSOWA.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, zabbixGroupsSOWA));
        listTriggersWithIncForKafka.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, zabbixGroupsKafka));
        listTriggersWithIncForMQ.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, zabbixGroupsMQ));
        listTriggersWithIncForDP.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, zabbixGroupsDP));

        //--------------------------------------------------------------------------------------------------------------

        //Создание списка всех триггеров по продуктам Стандартных платформ
        listTriggersForNginx.addAll(triggerListWithTemplateName (severity, zabbixGroupsNginx));
        listTriggersForWAS.addAll(triggerListWithTemplateName (severity, zabbixGroupsWAS));
        listTriggersForWildFly.addAll(triggerListWithTemplateName (severity, zabbixGroupsWildFly));
        listTriggersForWebLogic.addAll(triggerListWithTemplateName (severity, zabbixGroupsWeblogic));

        //Создание списка триггеров с инцидентами по продуктам Стандартных платформ
        listTriggersWithIncForNginx.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, zabbixGroupsNginx));
        listTriggersWithIncForWAS.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, zabbixGroupsWAS));
        listTriggersWithIncForWildFly.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, zabbixGroupsWildFly));
        listTriggersWithIncForWebLogic.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, zabbixGroupsWeblogic));

        //--------------------------------------------------------------------------------------------------------------

        //Создание списка всех триггеров по продуктам Платформа управления контейнерами (Terra)
        listTriggersForOpenShift.addAll(triggerListWithTemplateName (severity, zabbixGroupsOpenShift));
        //Создание списка триггеров с инцидентами по продуктам Платформа управления контейнерами (Terra)
        listTriggersWithIncForOpenShift.addAll(triggerListWithIncidentTagWithTemplateName(tagName, tagValue,
                severity, zabbixGroupsOpenShift));

        zabbixApi.destroy();


//        //Создание списка всех триггеров по продуктам ОИП
//        listTriggersForSOWA =  triggerListWithTemplateName (severity, "IS SOWA/OS");
//        listTriggersForKafka =  triggerListWithTemplateName (severity, "IS Kafka/OS",
//                "IS Kafka/App", "Kafka Clusters");
//        listTriggersForMQ =  triggerListWithTemplateName (severity, "IS IBM MQ/OS", "Tivoli MQ");
//        listTriggersForDP =  triggerListWithTemplateName (severity, "IS DataPower/DP", "Tivoli DP");
//
//        //Создание списка триггеров с инцидентами по продуктам ОИП
//        listTriggersWithIncForSOWA =  triggerListWithIncidentTagWithTemplateName("scope", "availability",
//                severity, "IS SOWA/OS");
//        listTriggersWithIncForKafka =  triggerListWithIncidentTagWithTemplateName("scope", "availability",
//                severity, "IS Kafka/OS",
//                "IS Kafka/App", "Kafka Clusters");
//        listTriggersWithIncForMQ =  triggerListWithIncidentTagWithTemplateName("scope", "availability",
//                severity, "IS IBM MQ/OS", "Tivoli MQ");
//        listTriggersWithIncForDP =  triggerListWithIncidentTagWithTemplateName("scope", "availability",
//                severity, "IS DataPower/DP", "Tivoli DP");
//
////        //Расчет процента покрытия по продуктам ОИП
////        percentOfCoverByIncidentForSOWA = percentOfCoverByIncident(listTriggersForSOWA, listTriggersWithIncForSOWA);
////        percentOfCoverByIncidentForKafka =percentOfCoverByIncident(listTriggersForKafka, listTriggersWithIncForKafka);
////        percentOfCoverByIncidentForMQ = percentOfCoverByIncident(listTriggersForMQ, listTriggersWithIncForMQ);
////        percentOfCoverByIncidentForDP = percentOfCoverByIncident(listTriggersForDP, listTriggersWithIncForDP);
//
//        //--------------------------------------------------------------------------------------------------------------
//
//        //Создание списка всех триггеров по продуктам Стандартных платформ
//        listTriggersForNginx =  triggerListWithTemplateName (severity, "IS SOWA/OS");
//        listTriggersForWAS =  triggerListWithTemplateName (severity, "IS Kafka/OS",
//                "IS Kafka/App", "Kafka Clusters");
//        listTriggersForWildFly =  triggerListWithTemplateName (severity, "IS IBM MQ/OS", "Tivoli MQ");
//        listTriggersForWebLogic =  triggerListWithTemplateName (severity, "IS DataPower/DP", "Tivoli DP");
//
//        //Создание списка триггеров по продуктам Стандартных платформ
//        listTriggersWithIncForNginx =  triggerListWithIncidentTagWithTemplateName("scope", "availability",
//                severity, "IS NGINX/OS");
//        listTriggersWithIncForWAS =  triggerListWithIncidentTagWithTemplateName("scope", "availability",
//                severity, "IS WebSphere Universal/OS");
//        listTriggersWithIncForWildFly =  triggerListWithIncidentTagWithTemplateName("scope", "availability",
//                severity, "IS WildFly/OS", "IS WildFly/App");
//        listTriggersWithIncForWebLogic =  triggerListWithIncidentTagWithTemplateName("scope", "availability",
//                severity, "IS Weblogic/OS", "IS Weblogic/App");
//
////        //Расчет процента покрытия по продуктам Стандартных платформ
////        percentOfCoverByIncidentForNginx = percentOfCoverByIncident(listTriggersForNginx, listTriggersWithIncForNginx);
////        percentOfCoverByIncidentForWAS =percentOfCoverByIncident(listTriggersForWAS, listTriggersWithIncForWAS);
////        percentOfCoverByIncidentForWildFly = percentOfCoverByIncident(listTriggersForWildFly, listTriggersWithIncForWildFly);
////        percentOfCoverByIncidentForSiebel = percentOfCoverByIncident(listTriggersForSiebel, listTriggersWithIncForSiebel);
//
//        //--------------------------------------------------------------------------------------------------------------
//
//        //Создание списка всех триггеров по продуктам Платформа управления контейнерами (Terra)
//        listTriggersForOpenShift =  triggerListWithTemplateName (severity, "OpenShift", "Sigma servers");
//        //Создание списка триггеров по продуктам Платформа управления контейнерами (Terra)
//        listTriggersWithIncForOpenShift =  triggerListWithIncidentTagWithTemplateName("scope", "availability",
//                severity, "OpenShift", "Sigma servers");
//
//
////        //Расчет процента покрытия по продуктам Платформа управления контейнерами (Terra)
////        percentOfCoverByIncidentForOpenShift = percentOfCoverByIncident(listTriggersForOpenShift, listTriggersWithIncForOpenShift);

    };


}



//        String host = "192.168.66.29";
//        JSONObject filter = new JSONObject();
//
//        filter.put("host", new String[] { host });

//        tag.keySet().forEach(keyStr->
//        {
//            Object keyvalue = tag.get(keyStr);
//            System.out.println("key: "+ keyStr + " value: " + keyvalue);
//
//        });


//        Map<String, String> tags = new HashMap<String, String>() {{
//            put("tag", "scope");
//            put("value", "availability");
//            put("operator", "0");
//        }};


//        JSONObject tag = new JSONObject();
//        tag.put("tag", "scope");
//        tag.put("value", "availability");
//        tag.put("operator", "0");
