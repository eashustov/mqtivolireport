package ru.sberbank.uspincidentreport.service.zabbix;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Trigger {
    String description;
    String priority;
    String templateid;
    String triggerid;
    String templateName;
    List<HashMap> tags;

    public void setTags(List<HashMap> tags) {
        this.tags = tags;
    }

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

    public List<HashMap> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        return "Trigger{" +
                "description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", templateid='" + templateid + '\'' +
                ", triggerid='" + triggerid + '\'' +
                ", templateName='" + templateName + '\'' +
                ", tags=" + tags +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Trigger)) return false;
        Trigger trigger = (Trigger) o;
        return Objects.equals(description, trigger.description) &&
                Objects.equals(priority, trigger.priority);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, priority);
    }
}
