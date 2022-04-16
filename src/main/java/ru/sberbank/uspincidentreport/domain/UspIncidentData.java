package ru.sberbank.uspincidentreport.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private String ZABBIX_HISTORY;
    private String HOST;
    private String RESOLUTION;
    private String PROBLEM;


    public UspIncidentData(String NUMBER, String BRIEF_DESCRIPTION, String PRIORITY_CODE, String OPEN_TIME,
                           String HPC_ASSIGNEE_NAME, String HPC_ASSIGNMENT, String HPC_CREATED_BY_NAME,
                           String ACTION, String ZABBIX_HISTORY, String HOST, String HPC_STATUS, String PROBLEM, String RESOLUTION) {
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

    }

    public UspIncidentData() {
    }

    public String getNUMBER() {
        return NUMBER;
    }

    public String getBRIEF_DESCRIPTION() {
        return BRIEF_DESCRIPTION;
    }

    public String getPRIORITY_CODE() {
        return PRIORITY_CODE;
    }

    public String getOPEN_TIME() {
        return OPEN_TIME;
    }

    public String getHPC_ASSIGNEE_NAME() {
        if (HPC_ASSIGNEE_NAME != null){
        return HPC_ASSIGNEE_NAME;
        }
        return HPC_ASSIGNEE_NAME = "";
    }
    public String getHPC_ASSIGNMENT() {
        return HPC_ASSIGNMENT;
    }

    public String getHPC_CREATED_BY_NAME() {
        return HPC_CREATED_BY_NAME;
    }

    public String getACTION() {
        return ACTION;
    }

    public String getHOST() {
        return HOST;
    }

    public String getHPC_STATUS() {
        return HPC_STATUS;
    }

    public String getZABBIX_HISTORY() {
        return ZABBIX_HISTORY;
    }

    public String getPROBLEM() {
        return PROBLEM;
    }
        public String getRESOLUTION() {
        if (PROBLEM.contains("MQ_Queue_Depth")){
            return "https://nlb-jenkins/cis/job/OASP_2/job/tivoli/job/TIVOLI_AGENT_MANAGE/build+" + HOST;
        } else {
            return "Нет сценариев для устранения инцидента";
        }

    }


}
