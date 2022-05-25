package ru.sberbank.uspincidentreport.domain;

public interface IUspIncidentDataCountPerMonth {

    String getAssignment();
    String getMonth();
    String getMonthNumber();
    String getYear();
    Integer getCountInc();

}
