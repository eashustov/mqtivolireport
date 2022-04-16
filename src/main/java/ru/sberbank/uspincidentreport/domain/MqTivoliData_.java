package ru.sberbank.uspincidentreport.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class MqTivoliData {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;
    private String mqName;
    private String serverName;
    private String tivoliInstall;
}
