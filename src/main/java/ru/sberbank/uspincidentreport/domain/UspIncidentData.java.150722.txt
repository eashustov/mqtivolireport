package ru.sberbank.uspincidentreport.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Entity
//@Table(schema = "SMPRIMARY", name = "probsummarym1")
@Table(name = "probsummarym1")
public class UspIncidentData {
    @Id
    @Column(name = "NUMBER")
    private String NUMBER;
    @Column(name = "BRIEF_DESCRIPTION")
    private String BRIEF_DESCRIPTION;
    @Column(name = "PRIORITY_CODE")
    private String PRIORITY_CODE;
    @Column(name = "OPEN_TIME")
    @DateTimeFormat(pattern="dd.MM.yyyy HH:mm:ss")
    private String OPEN_TIME;
    @Column(name = "HPC_ASSIGNEE_NAME")
    private String HPC_ASSIGNEE_NAME;
    @Column(name = "HPC_ASSIGNMENT")
    private String HPC_ASSIGNMENT;
    @Column(name = "HPC_CREATED_BY_NAME")
    private String HPC_CREATED_BY_NAME;
    @Column(name = "HPC_STATUS")
    private String HPC_STATUS;
    @Column(name = "ACTION")
    private String ACTION;
    @Column(name = "OPENED_BY")
    private String OPENED_BY;
    @Column(name = "AFFECTED_ITEM")
    private String AFFECTED_ITEM;
    private String ZABBIX_HISTORY;
    private String HOST;
    private String RESOLUTION = "https://RLM.ca.sbrf.ru/Auth";
    private String PROBLEM;
    private String RESOLUTION_GUIDE = "https://confluence.ca.sbrf.ru/";



    public UspIncidentData(String NUMBER, String BRIEF_DESCRIPTION, String PRIORITY_CODE, String OPEN_TIME,
                           String HPC_ASSIGNEE_NAME, String HPC_ASSIGNMENT, String HPC_CREATED_BY_NAME,
                           String ACTION, String ZABBIX_HISTORY, String HOST, String HPC_STATUS, String PROBLEM, String RESOLUTION, String OPENED_BY, String RESOLUTION_GUIDE, String AFFECTED_ITEM) {
        this.NUMBER = NUMBER;
        this.BRIEF_DESCRIPTION = BRIEF_DESCRIPTION;
        this.PRIORITY_CODE = PRIORITY_CODE;
        this.OPEN_TIME = OPEN_TIME;
        this.HPC_ASSIGNEE_NAME = HPC_ASSIGNEE_NAME;
        this.HPC_ASSIGNMENT = HPC_ASSIGNMENT;
        this.HPC_CREATED_BY_NAME = HPC_CREATED_BY_NAME;
        this.ACTION = ACTION;
        this.HOST = HOST;
        this.HPC_STATUS = HPC_STATUS;
        this.ZABBIX_HISTORY = ZABBIX_HISTORY;
        this.RESOLUTION = RESOLUTION;
        this.PROBLEM = PROBLEM;
        this.OPENED_BY = OPENED_BY;
        this.RESOLUTION_GUIDE = RESOLUTION_GUIDE;
        this.AFFECTED_ITEM = AFFECTED_ITEM;

    }

    public UspIncidentData() {
    }

    public String getNUMBER() {
        if (NUMBER != null){
            return NUMBER;
        }
        return NUMBER = "";
    }

    public String getBRIEF_DESCRIPTION() {
        if (BRIEF_DESCRIPTION != null){
            return BRIEF_DESCRIPTION;
        }
        return BRIEF_DESCRIPTION = "";
    }

    public String getPRIORITY_CODE() {
        if (PRIORITY_CODE != null){
            return PRIORITY_CODE;
        }
        return PRIORITY_CODE = "";
     }

    public String getOPEN_TIME() {
        if (OPEN_TIME != null){
            return OPEN_TIME;
        }
        return OPEN_TIME = "";
    }

    public String getHPC_ASSIGNEE_NAME() {
        if (HPC_ASSIGNEE_NAME != null){
        return HPC_ASSIGNEE_NAME;
        }
        return HPC_ASSIGNEE_NAME = "";
    }
    public String getHPC_ASSIGNMENT() {
        if (HPC_ASSIGNMENT != null){
            return HPC_ASSIGNMENT;
        }
        return HPC_ASSIGNMENT = "";
    }

    public String getHPC_CREATED_BY_NAME() {
        if (HPC_CREATED_BY_NAME != null){
            return HPC_CREATED_BY_NAME;
        }
        return HPC_CREATED_BY_NAME = "";
    }

    public String getACTION() {

        if (ACTION != null){
            return ACTION;
        }
        return ACTION = "";
    }

    public String getHOST() {
        if (HOST != null){
            return HOST;
        }
        return HOST = "";
    }

    public String getHPC_STATUS() {
        if (HPC_STATUS != null){
            return HPC_STATUS;
        }
        return HPC_STATUS = "";
    }

    public String getZABBIX_HISTORY() {
        if (ZABBIX_HISTORY != null){
            return ZABBIX_HISTORY;
        }
        return ZABBIX_HISTORY = "";
       }

    public String getPROBLEM() {
        if (PROBLEM != null){
            return PROBLEM;
        }
        return PROBLEM = "";
        }

    public String getRESOLUTION() {
        try {
            if (RESOLUTION != null) {
                if (PROBLEM.contains("MQ_Queue_Depth")) {
                    return "https://nlb-jenkins/cis/job/OASP_2/job/tivoli/job/TIVOLI_AGENT_MANAGE/build+" + HOST;
                } else {
                    return "https://RLM.ca.sbrf.ru/Auth";
                }
            } else {return "https://RLM.ca.sbrf.ru/Auth";}
        } catch (NullPointerException e) {
            return "https://RLM.ca.sbrf.ru/Auth";
        }


    }

    public String getOPENED_BY() {
        if (OPENED_BY != null){
            return OPENED_BY;
        }
        return OPENED_BY = "";
     }

    public String getRESOLUTION_GUIDE() {
        // Инциденты для IBM MQ
        if (PROBLEM.contains("MQ Message_ID= AMQ9513 Message_Text= Maximum number of channels reached")
                || PROBLEM.contains ("Message_ID= AMQ9513E Message_Text= Maximum number of channels reached")) {
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518601";

        } else if (PROBLEM.contains("MQ Message_ID= AMQ9620 Message_Text= Internal error on call to SSL function on channel")
                || PROBLEM.contains("MQ Message_ID= AMQ9620E")) {
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518711";

        }else if (PROBLEM.contains("MQHealthcheck MQ_SERVER_DOWN")){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518637";

        }else if (PROBLEM.contains("MQ_Connection_On_Channel_Maxinstances_max_reached")) {
            return "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Connection_On_Channel_Maxinstances_max_reached";

        }else if (PROBLEM.contains("MQ_Listener_Not_Started")) {
            return "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Listener_Not_Started";

        }else if (PROBLEM.contains("MQ_Queue_Depth_200_In_Transmission_Queue")){
            return "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Queue_Depth_200_In_Transmission_Queue";

        }else if (PROBLEM.contains("MQ_Queue_Manager_Problem")){
            return "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Queue_Manager_Problem+Manager_Name%3D+XXXXXXX+MQ_Manager_Status%3D+4";

        }else if (PROBLEM.contains("MQ_Sender_Channel_Problem")){
            return "https://confluence.ca.sbrf.ru/display/SberInfra/MQ.+MQ_Sender_Channel_Problem";

        }else if (PROBLEM.contains("CPU Utilization > 95")&&(AFFECTED_ITEM.equals("CI02021291"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518550";

        }else if (PROBLEM.contains("Disk space is more than 95")&&(AFFECTED_ITEM.equals("CI02021291"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518562";

        }else if (PROBLEM.contains("Memory Utilization > 95")&&(AFFECTED_ITEM.equals("CI02021291"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518575";

        }else if (PROBLEM.contains("MQ_Queue_Depth_50%")){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518662";

            //Инциденты для IBM Apache Kafka
        }else if (PROBLEM.contains("EventQueueSize")&&(AFFECTED_ITEM.equals("CI02192117"))){
            return "https://confluence.ca.sbrf.ru/display/SberInfra/Apache+Kafka.+%5BAI%5DEventQueueSize";

        }else if (PROBLEM.contains("CPU Utilization")&&(AFFECTED_ITEM.equals("CI02192117"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518847";

        }else if (PROBLEM.contains("Disk space is more than 85% full on volume")&&(AFFECTED_ITEM.equals("CI02192117"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518859";

        }else if (PROBLEM.contains("Disk space is more than 95% full on volume")&&(AFFECTED_ITEM.equals("CI02192117"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518871";

        }else if (PROBLEM.contains("Время сборки мусора PS MarkSweep превышает 600 мс")&&(AFFECTED_ITEM.equals("CI02192117"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518883";

        }else if (PROBLEM.contains("Количество UnderMinIsr партиций")&&(AFFECTED_ITEM.equals("CI02192117"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518896";

        }else if (PROBLEM.contains("Количество партиций, не имеющих активного лидера")&&(AFFECTED_ITEM.equals("CI02192117"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518908";

        }else if (PROBLEM.contains("Размер очереди запросов")&&(AFFECTED_ITEM.equals("CI02192117"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518920";

            //Инциденты для IBM DataPower
        } else if (PROBLEM.contains("WEB Status DataPower port 5550")
                || PROBLEM.contains("WEB Status DataPower port 9090")){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638519059";

        } else if (PROBLEM.contains("MQ total connections is exceed")){
            return "https://confluence.ca.sbrf.ru/display/SberInfra/IBM+DataPower.+MQ+total+connections+is+exceed";

        } else if (PROBLEM.contains("down in domain")){
            return "https://confluence.ca.sbrf.ru/display/SberInfra/IBM+DataPower.+down+in+domain%3A+XXXXXXX";

        } else if (PROBLEM.contains("OASP_DP_CPUUsage average 1 minute")){
            return "https://confluence.ca.sbrf.ru/display/SberInfra/IBM+DataPower.+OASP_DP_CPUUsage+average+1+minute+%3E+9X";

        } else if (PROBLEM.contains("Response message is: Connection Refused")){
            return "https://confluence.ca.sbrf.ru/display/SberInfra/IBM+DataPower.+Response+message+is%3A+Connection+Refused";

        } else if (PROBLEM.contains("Response message is: Connect/Read Timed")){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518512";

        }else if (PROBLEM.contains("is unavailable by ICMP")&&(AFFECTED_ITEM.equals("CI02021290"))){
            return "https://confluence.ca.sbrf.ru/display/SberInfra/IBM+DataPower.+unavailable+by+ICMP";

        }else if (PROBLEM.contains("CPUUsage one minute")&&(AFFECTED_ITEM.equals("CI02021290"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518463";

            //Инциденты для SOWA
        }else if (PROBLEM.contains("CPU Utilization")&&(AFFECTED_ITEM.equals("CI02192118"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518792";

        }else if (PROBLEM.contains("Disk space is more than 9")&&(AFFECTED_ITEM.equals("CI02192118"))){
            return "https://confluence.ca.sbrf.ru/pages/viewpage.action?pageId=7638518804";

        }
        return "https://confluence.ca.sbrf.ru/";
    }

    public String getAFFECTED_ITEM() {
        if (AFFECTED_ITEM != null){
            return AFFECTED_ITEM;
        }
        return AFFECTED_ITEM = "";
    }

}
