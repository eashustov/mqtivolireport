package ru.sberbank.uspincidentreport.service.zabbix;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Discoveryrule {
    String itemid;

    public String getItemid() {
        return itemid;
    }

    public void setItemid(String itemid) {
        this.itemid = itemid;
    }

    @Override
    public String toString() {
        return "Discoveryrule{" +
                "itemid='" + itemid + '\'' +
                '}';
    }
}
