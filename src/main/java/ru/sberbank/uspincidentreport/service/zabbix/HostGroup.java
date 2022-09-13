package ru.sberbank.uspincidentreport.service.zabbix;

public class HostGroup {

    String groupid;
    String name;
    String flags;
    String uuid;

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFlags() {
        return flags;
    }

    public void setFlags(String flags) {
        this.flags = flags;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "HostGroup{" +
                "groupid='" + groupid + '\'' +
                ", name='" + name + '\'' +
                ", flags='" + flags + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
