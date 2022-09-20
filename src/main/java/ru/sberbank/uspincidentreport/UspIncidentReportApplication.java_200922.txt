package ru.sberbank.uspincidentreport;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import ru.sberbank.uspincidentreport.service.zabbix.ZabbixAPI;

@SpringBootApplication
public class UspIncidentReportApplication {

    private static ConfigurableApplicationContext context;

//    //Из application.properties нельзя вставить значение в статическую переменную напряму.
//    // Поэтому требуется метод setURLs, setUser, setPassword
//    private static String[] urls;
//    @Value("${zabbix.api.url}")
//    private void setURLs(String[] zabbix_urls){
//        urls = zabbix_urls;
//    }
//
//    private static String user;
//    @Value("${zabbix.api.user}")
//    private void setUser(String zabbix_user){
//        user = zabbix_user;
//    }
//
//    private static String password;
//    @Value("${zabbix.api.password}")
//    private void setPassword(String zabbix_password){
//        password = zabbix_password;
//    }

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

    static String zabbixAPITriggersSeverityDefaultValue;
    @Value("${zabbix.api.trigger.severity.default}")
    private void setZabbixAPITriggersSeverityDefaultValue(String TriggersSeverityDefaultValue){
        zabbixAPITriggersSeverityDefaultValue = TriggersSeverityDefaultValue;
    }

    //Выставление значений zabbix групп тега из appliation.properties
    //Из application.properties нельзя вставить значение в статическую переменную напряму.
    // Поэтому требуется метод setGroups*
    //ОИП
    static String[] zabbixGroupsSOWA;
    @Value("${zabbix.api.groupsname.sowa}")
    private void setGroupsSOWA(String[] groupsName){
        zabbixGroupsSOWA = groupsName;
    }

    static String[] zabbixGroupsKafka;
    @Value("${zabbix.api.groupsname.kafka}")
    private void setGroupsKafka(String[] groupsName){
        zabbixGroupsKafka = groupsName;
    }

    static String[] zabbixGroupsMQ;
    @Value("${zabbix.api.groupsname.mq}")
    private void setGroupsMQ(String[] groupsName){
        zabbixGroupsMQ = groupsName;
    }

    static String[] zabbixGroupsDP;
    @Value("${zabbix.api.groupsname.dp}")
    private void setGroupsDP(String[] groupsName){
        zabbixGroupsDP = groupsName;
    }

    //Стандартные платформы
    static String[] zabbixGroupsNginx;
    @Value("${zabbix.api.groupsname.nginx}")
    private void setZabbixGroupsNginx(String[] groupsName){
        zabbixGroupsNginx = groupsName;
    }

    static String[] zabbixGroupsWAS;
    @Value("${zabbix.api.groupsname.nginx}")
    private void setZabbixGroupsWAS(String[] groupsName){
        zabbixGroupsWAS = groupsName;
    }

    static String[] zabbixGroupsWildFly;
    @Value("${zabbix.api.groupsname.wildfly}")
    private void setZabbixGroupsWildFly(String[] groupsName){
        zabbixGroupsWildFly = groupsName;
    }

    static String[] zabbixGroupsWeblogic;
    @Value("${zabbix.api.groupsname.weblogic}")
    private void setZabbixGroupsWeblogic(String[] groupsName){
        zabbixGroupsWeblogic = groupsName;
    }
    //Платформа управления контейнерами (Terra)
    static String[] zabbixGroupsOpenShift;
    @Value("${zabbix.api.groupsname.openshift}")
    private void setZabbixGroupsOpenShift(String[] groupsName){
        zabbixGroupsOpenShift = groupsName;
    }

    //Период опроса Zabbix серверов
    static int zabbixRequestInterval;
    @Value("${zabbix.api.request.interval}")
    private void setZabbixGroupsOpenShift(int requestInterval){
        zabbixRequestInterval = requestInterval;
    }

    public static void main(String[] args) {
        context = SpringApplication.run(UspIncidentReportApplication.class, args);

        //Получение статистики Zabbix
        Runnable ZabbixAPIGetStatistic = ()->{
                while (true) {
//                    for(String url:urls) {
//                        try {
//                            ZabbixAPI.ZabbixAPIRegistration(url, user, password);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                            continue;
//                        }
                        try {
                            ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_0 =
                                        ZabbixAPI.getPercentOfCoverTriggersByInc("0", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsSOWA);
                            ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_1 =
                                        ZabbixAPI.getPercentOfCoverTriggersByInc("1", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsSOWA);
                            ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_2 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("2", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsSOWA);
                            ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_3 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("3", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsSOWA);
                            ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_4 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("4", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsSOWA);
                            ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_5 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("5", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsSOWA);
                            ZabbixAPI.percentOfCoverByIncidentForKafka_sev_0 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("0", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsKafka);
                            ZabbixAPI.percentOfCoverByIncidentForKafka_sev_1 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("1", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsKafka);
                            ZabbixAPI.percentOfCoverByIncidentForKafka_sev_2 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("2", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsKafka);
                            ZabbixAPI.percentOfCoverByIncidentForKafka_sev_3 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("3", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsKafka);
                            ZabbixAPI.percentOfCoverByIncidentForKafka_sev_4 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("4", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsKafka);
                            ZabbixAPI.percentOfCoverByIncidentForKafka_sev_5 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("5", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsKafka);
                            ZabbixAPI.percentOfCoverByIncidentForMQ_sev_0 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("0", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsMQ);
                            ZabbixAPI.percentOfCoverByIncidentForMQ_sev_1 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("1", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsMQ);
                            ZabbixAPI.percentOfCoverByIncidentForMQ_sev_2 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("2", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsMQ);
                            ZabbixAPI.percentOfCoverByIncidentForMQ_sev_3 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("3", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsMQ);
                            ZabbixAPI.percentOfCoverByIncidentForMQ_sev_4 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("4", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsMQ);
                            ZabbixAPI.percentOfCoverByIncidentForMQ_sev_5 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("5", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsMQ);
                            ZabbixAPI.percentOfCoverByIncidentForDP_sev_0 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("0", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsDP);
                            ZabbixAPI.percentOfCoverByIncidentForDP_sev_1 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("1", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsDP);
                            ZabbixAPI.percentOfCoverByIncidentForDP_sev_2 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("2", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsDP);
                            ZabbixAPI.percentOfCoverByIncidentForDP_sev_3 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("3", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsDP);
                            ZabbixAPI.percentOfCoverByIncidentForDP_sev_4 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("4", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsDP);
                            ZabbixAPI.percentOfCoverByIncidentForDP_sev_5 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("5", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsDP);
                            //Для стандартных платформ
                            ZabbixAPI.percentOfCoverByIncidentForNginx_sev_0 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("0", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsNginx);
                            ZabbixAPI.percentOfCoverByIncidentForNginx_sev_1 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("1", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsNginx);
                            ZabbixAPI.percentOfCoverByIncidentForNginx_sev_2 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("2", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsNginx);
                            ZabbixAPI.percentOfCoverByIncidentForNginx_sev_3 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("3", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsNginx);
                            ZabbixAPI.percentOfCoverByIncidentForNginx_sev_4 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("4", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsNginx);
                            ZabbixAPI.percentOfCoverByIncidentForNginx_sev_5 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("5", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsNginx);
                            ZabbixAPI.percentOfCoverByIncidentForWAS_sev_0 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("0", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWAS);
                            ZabbixAPI.percentOfCoverByIncidentForWAS_sev_1 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("1", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWAS);
                            ZabbixAPI.percentOfCoverByIncidentForWAS_sev_2 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("2", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWAS);
                            ZabbixAPI.percentOfCoverByIncidentForWAS_sev_3 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("3", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWAS);
                            ZabbixAPI.percentOfCoverByIncidentForWAS_sev_4 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("4", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWAS);
                            ZabbixAPI.percentOfCoverByIncidentForWAS_sev_5 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("5", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWAS);
                            ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_0 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("0", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWildFly);
                            ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_1 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("1", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWildFly);
                            ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_2 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("2", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWildFly);
                            ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_3 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("3", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWildFly);
                            ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_4 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("4", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWildFly);
                            ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_5 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("5", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWildFly);
                            ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_0 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("0", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWeblogic);
                            ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_1 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("1", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWeblogic);
                            ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_2 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("2", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWeblogic);
                            ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_3 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("3", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWeblogic);
                            ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_4 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("4", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWeblogic);
                            ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_5 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("5", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsWeblogic);
                            //Для OpenShift
                            ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_0 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("0", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsOpenShift);
                            ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_1 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("1", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsOpenShift);
                            ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_2 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("2", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsOpenShift);
                            ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_3 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("3", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsOpenShift);
                            ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_4 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("4", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsOpenShift);
                            ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_5 =
                                    ZabbixAPI.getPercentOfCoverTriggersByInc("5", zabbixAPITagName, zabbixAPITagValue, zabbixGroupsOpenShift);
//Ниже код используется при получении по API списка триггеров. В ПРОМ очень долго. ПОэтому данный функуионал убран
//                            ZabbixAPI.getTriggerStatisticDefault(zabbixAPITriggersSeverityDefaultValue, zabbixAPITagName,
//                                    zabbixAPITagValue, zabbixGroupsSOWA, zabbixGroupsKafka, zabbixGroupsMQ, zabbixGroupsDP,
//                                    zabbixGroupsNginx, zabbixGroupsWAS, zabbixGroupsWildFly, zabbixGroupsWeblogic,
//                                    zabbixGroupsOpenShift);
//                            ZabbixAPI.zabbixApi.destroy();

                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                            continue;
                        }
//                    }
                    try {
    //                    Thread.sleep(7200000);
                        System.out.println("Пауза между опросами " + zabbixRequestInterval + " минут");
                        Thread.sleep(zabbixRequestInterval*60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_0 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForKafka_sev_0 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForMQ_sev_0 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForDP_sev_0 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_1 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForKafka_sev_1 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForMQ_sev_1 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForDP_sev_1 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_2 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForKafka_sev_2 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForMQ_sev_2 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForDP_sev_2 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_3 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForKafka_sev_3 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForMQ_sev_3 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForDP_sev_3 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_4 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForKafka_sev_4 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForMQ_sev_4 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForDP_sev_4 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForSOWA_sev_5 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForKafka_sev_5 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForMQ_sev_5 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForDP_sev_5 = 0;

                        ZabbixAPI.percentOfCoverByIncidentForNginx_sev_0 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_0 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWAS_sev_0 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_0 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForNginx_sev_1 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_1 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWAS_sev_1 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_1 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForNginx_sev_2 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_2 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWAS_sev_2 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_2 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForNginx_sev_3 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_3 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWAS_sev_3 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_3 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForNginx_sev_4 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_4 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWAS_sev_4 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_4 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForNginx_sev_5 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWildFly_sev_5 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWAS_sev_5 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForWebLogic_sev_5 = 0;

                        ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_0 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_1 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_2 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_3 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_4 = 0;
                        ZabbixAPI.percentOfCoverByIncidentForOpenShift_sev_5 = 0;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        //Запуск получения статистики Zabbix
        Thread zabbixAPIGetStatisticThread = new Thread(ZabbixAPIGetStatistic,"ZabbixAPIGetStatistic");
        zabbixAPIGetStatisticThread.start();
        System.out.println("ZabbixAPIGetStatistic started...");

        HeapControl ();

    }

    public static void HeapControl () {
        System.out.println("Heap контроль запущен");
        while (true) {

            // Sleep to emulate background work
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            System.gc();
            System.out.println("Запущен GC");

            // Get current size of heap in bytes
            long heapSize = Runtime.getRuntime().totalMemory();

            // Get maximum size of heap in bytes. The heap cannot grow beyond this size.// Any attempt will result in an OutOfMemoryException.
            long heapMaxSize = Runtime.getRuntime().maxMemory();

            float usedHeapPrc = (float) heapSize / heapMaxSize;

            System.out.println("totalMemory() :" + heapSize + " maxMemory(): " + heapMaxSize + " usedHeapPrc: " + usedHeapPrc);

            if (usedHeapPrc > 0.80) {
                //Если использование heap больше 80 процентов, выполнить рестарт приложения

                restart();
            }

        }
    }

    public static void restart() {
        ApplicationArguments args = context.getBean(ApplicationArguments.class);

        Thread thread = new Thread(() -> {
            context.close();
            context = SpringApplication.run(UspIncidentReportApplication.class, args.getSourceArgs());
        });

        thread.setDaemon(false);
        thread.start();
    }


}
