package ru.sberbank.uspincidentreport.domain;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import ru.sberbank.uspincidentreport.service.USPIncResolutionGuideMap;
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

        try {
            return RESOLUTION_GUIDE = USPIncResolutionGuideMap.GetResolutionGuide(getAFFECTED_ITEM(), getPROBLEM());
        } catch (Exception e) {
            return RESOLUTION_GUIDE = "https://confluence.ca.sbrf.ru/";
        }
    }

    public String getAFFECTED_ITEM() {
        if (AFFECTED_ITEM != null){
            return AFFECTED_ITEM;
        }
        return AFFECTED_ITEM = "";
    }

}
