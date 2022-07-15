package ru.sberbank.uspincidentreport.service;

import java.util.HashMap;
import java.util.Map;

public class USPIncResolutionGuideMap {

    //Инциденты для CI02192117 Kafka
   static Map<String, String> CI02192117KafkaMap  = new HashMap<>() {{
        put("Disk inode is more than", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521473");
        put("Memory Utilization >", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521996");
        put("EventQueueSize", "https://confluence.ca.sbrf.ru/display/SberInfra/Apache+Kafka.+EventQueueSize");
        put("CPU Utilization", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518847");
        put("Disk space is more than 85% full on volume", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518859");
        put("Disk space is more than 95% full on volume", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518871");
        put("Время сборки мусора PS MarkSweep превышает 600 мс", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518883");
        put("Количество UnderMinIsr партиций", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518896");
        put("Количество партиций, не имеющих активного лидера", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518908");
        put("Размер очереди запросов", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518920");
        put("Пользователем kafka запущено более", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522009");
        put("Пользователем kafka использовано более", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522021");
        put("Процент использования массивов семафоров", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522035");
        put("Процент использования процессов", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522047");
        put("Процент использования разделяемой памяти", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522059");
        put("Процент использования сегментов разделяемой памяти", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522073");
        put("Процент использования семафоров", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522085");
        put("Процент использования файловых-дескрипторов", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522098");
        put("Количество сборок мусора в минуту", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522126");
        put("Время сборки мусора", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522140");
        put("Количество открытых файлов превышает порог", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522156");
        put("LogDirectory", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522173");
        put("Более 90% Heap памяти использовано", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522112");
    }};

    //Инциденты для CI02192118 SOWA
   static Map<String, String> CI02192118SOWAMap  = new HashMap<>() {{
        put("CPU Utilization", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518792");
        put("Disk space is more than 9", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518804");
        put("Disk inode is more than", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521437");
        put("Пользователем sowactl запущено более", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521653");
        put("Пользователем sowactl использовано более", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521665  ");
        put("Процент использования массивов семафоров", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521679");
        put("Процент использования процессов", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521694");
        put("Процент использования разделяемой памяти", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521707");
        put("Процент использования сегментов разделяемой памяти", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521720");
        put("Процент использования семафоров", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521735");
        put("Процент использования файловых-дескрипторов", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521748");
        put("в состоянии down", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521760");
        put("Certificate", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521774");
        put("Unavailable by ICMP ping", "https://confluence.ca.sbrf.ru/display/SberInfra/SOWA.+Unavailable+by+ICMP+ping");
        put("Memory Utilization >", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521641");
    }};

    //Инциденты для CI02021290 IBM DataPower
   static Map<String, String> CI02021290DPMap  = new HashMap<>() {{
        put("WEB Status DataPower port", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638519059");
        put("MQ total connections is exceed", "https://confluence.ca.sbrf.ru/display/SberInfra/IBM+DataPower.+MQ+total+connections+is+exceed");
        put("down in domain", "https://confluence.ca.sbrf.ru/display/SberInfra/IBM+DataPower.+down+in+domain%3A+XXXXXXX");
        put("OASP_DP_CPUUsage average 1 minute", "https://confluence.ca.sbrf.ru/display/SberInfra/IBM+DataPower.+OASP_DP_CPUUsage+average+1+minute+%3E+9X");
        put("Response message is: Connection Refused", "https://confluence.ca.sbrf.ru/display/SberInfra/IBM+DataPower.+Response+message+is%3A+Connection+Refused");
        put("Response message is: Connect/Read Timed", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518512");
        put("is unavailable by ICMP", "https://confluence.ca.sbrf.ru/display/SberInfra/IBM+DataPower.+unavailable+by+ICMP");
        put("CPUUsage one minute", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518463");
        put("OASP_DP_Memory Status Usage", "https://confluence.ca.sbrf.ru/display/SberInfra/IBM+DataPower.+OASP_DP_Memory+Status+Usage+%3E+90");
        put("Eth no link on", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522212");
        put("Battery fail", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522224");
        put("Battery life end", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522236");
        put("Battery missing", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522254");
        put("Certificate", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522268");
        put("Disk Space Insufficient", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522281");
        put("Fan stopped", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522293");
        put("Memory full", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522306");
        put("Parser out of memory", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522373");
        put("Power fail", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522387");
        put("System out of memory", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522407");
        put("File system util is exceed", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522428");
        put("Memory usage", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522446");
        put("Current system load is", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638522467");
   }};

    //Инциденты для CI02021291 IBM MQ
    static Map<String, String> CI02021291MQMap  = new HashMap<>() {{
        put("Maximum number of channels reached", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518601");
        put("Internal error on call to SSL function on channel", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518711");
        put("MQHealthcheck MQ_SERVER_DOWN", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518637");
        put("MQ_Connection_On_Channel_Maxinstances_max_reached", "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Connection_On_Channel_Maxinstances_max_reached");
        put("MQ_Listener_Not_Started", "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Listener_Not_Started");
        put("MQ_Queue_Depth_200_In_Transmission_Queue", "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Queue_Depth_200_In_Transmission_Queue");
        put("MQ_Queue_Manager_Problem", "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Queue_Manager_Problem+Manager_Name%3D+XXXXXXX+MQ_Manager_Status%3D+4");
        put("MQ_Sender_Channel_Problem", "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Sender_Channel_Problem");
        put("CPU Utilization", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518550");
        put("Disk space is more than", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518562");
        put("Memory Utilization", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518575");
        put("MQ_Queue_Depth_50%", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518662");
        put("Processor load is over", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521842");
        put("Disk space is less than", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521854");
        put("Lack of available virtual memory on server", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521866");
        put("Пользователем mqm использовано более", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521891");
        put("Процент использования массивов семафоров", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521903");
        put("Процент использования процессов", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521915");
        put("Процент использования разделяемой памяти", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521927");
        put("Процент использования сегментов разделяемой памяти", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521939");
        put("Процент использования семафоров", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521951");
        put("Процент использования файловых-дескрипторов", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521963");
        put("Disk space is less than", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521975");
        put("MQ_Channel_Out_Of_Sync", "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Channel_Out_Of_Sync");
        put("MQ_Error_in_Log_SSL_Certificate_Expired", "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Error_in_Log_SSL_Certificate_Expired");
        put("Disk inode is more than", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521454");
        put("Free memory on server", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521830");
        put("Пользователем mqm запущено более", "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638521879");
    }};


    //Инциденты УСП
   static Map<String, Map<String, String>> USPIncResolutionGuideMap  = new HashMap<>() {{
        put("CI02192117", CI02192117KafkaMap);
        put("CI02192118", CI02192118SOWAMap);
        put("CI02021290", CI02021290DPMap);
        put("CI02021291", CI02021291MQMap);
    }};

    public static String GetResolutionGuide(String Affected_Item, String Problem) {


        try  {
            if (Affected_Item.equals("CI02192117") ||
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
