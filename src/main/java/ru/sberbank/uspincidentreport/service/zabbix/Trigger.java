package ru.sberbank.uspincidentreport.service.zabbix;

public class Trigger {
    String description;
    String priority;
    String templateid;
    String triggerid;
//
    String templateName;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

   public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setTemplateid(String templateid) {
        this.templateid = templateid;
    }

    public void setTriggerid(String triggerid) {
        this.triggerid = triggerid;
    }

    public String getDescription() {
        return description;
    }

    public String getPriority() {
        return priority;
    }

    public String getTemplateid() {
        return templateid;
    }

    public String getTriggerid() {
        return triggerid;
    }

    @Override
    public String toString() {
        return "Trigger{" +
                "description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", templateid='" + templateid + '\'' +
                ", triggerid='" + triggerid + '\'' +
                ", templateName='" + templateName + '\'' +
                '}';
    }

}
