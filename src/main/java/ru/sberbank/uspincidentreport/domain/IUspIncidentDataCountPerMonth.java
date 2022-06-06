package ru.sberbank.uspincidentreport.domain;

public interface IUspIncidentDataCountPerMonth {

    String getHPC_Assignment();
    String getMonth();
    String getMonth_Number();
    String getYear();
    Integer getCount_Inc();

}
