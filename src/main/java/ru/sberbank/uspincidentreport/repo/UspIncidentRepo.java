package ru.sberbank.uspincidentreport.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;

import java.util.List;

@Repository
public interface UspIncidentRepo extends CrudRepository<UspIncidentData, String> {

   @Override
   @Query(value = "SELECT\n" +
           "\t* \n" +
           "FROM\n" +
           "\t(\tSELECT\n" +
           "\t\t\tprob1.\"NUMBER\",\n" +
           "\t\t\tBRIEF_DESCRIPTION,\n" +
           "\t\t\tPRIORITY_CODE,\n" +
           "\t\t\tOPEN_TIME,\n" +
           "\t\t\tto_char(ACTION) AS ACTION,\n" +
           "\t\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){6}.*'), 'htt.*$') AS ZABBIX_HISTORY,\n" +
           "\t\t\tREPLACE(REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){4}.*'), 'Хост:\\s.*$'), 'Хост: ') AS HOST,\n" +
           "\t\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){1}.*'), 'Проблема.*$') AS PROBLEM,\n" +
           "\t\t\tHPC_STATUS,\n" +
           "\t\t\tsubstr(prob1.hpc_assignee_name, 1, instr(prob1.hpc_assignee_name, \n" +
           "\t\t\t'(') - 2) AS HPC_ASSIGNEE_NAME,\n" +
           "\t\t\tsubstr(prob1.hpc_assignment, 1, instr(prob1.hpc_assignment, '(') - 2 \n" +
           "\t\t\t)      AS HPC_ASSIGNMENT,\n" +
           "\t\t\tHPC_CREATED_BY_NAME, 'RESOLUTION' AS RESOLUTION\n" +
           "\t\t\t\n" +
           "\t\tFROM\n" +
           "\t\t\tsmprimary.probsummarym1 prob1 \n" +
           "\t\tWHERE\n" +
           "\t\t\tprob1.hpc_assignment IN ( 'ЦСИТ Серверы приложений (00001092)',\n" +
           "\t\t\t'ЦСИТ ОАСП Стандартные платформы (00003984)',\n" +
           "\t\t\t'Сопровождение WAS ОСЦИТУ ДВБ (00001341)',\n" +
           "\t\t\t'ЦИ Запад Стандартные платформы (00011217)',\n" +
           "\t\t\t'ЦИ Центр Стандартные платформы (00011213)',\n" +
           "\t\t\t'ЦСИТ ОАСП Специализированные платформы (00003978)',\n" +
           "\t\t\t'ЦСИТ ОАСП Интеграционные платформы (00003982)',\n" +
           "\t\t\t'ЦИ ОАСП Системы очередей сообщений (00014339)',\n" +
           "\t\t\t'ЦИ ОАСП Шлюзовые решения (00014345)',\n" +
           "\t\t\t'ЦИ ОАСП Технологический стэк ППРБ (00014341)',\n" +
           "\t\t\t'ЦИ Восток Интеграционные платформы (00011221)',\n" +
           "\t\t\t'ЦИ Запад Интеграционные платформы (00011219)',\n" +
           "\t\t\t'ЦИ Центр Интеграционные платформы (00011215)',\n" +
           "\t\t\t'СБТ ДК ОСА Серверы приложений (Щелчков Р.А.) (00010280)',\n" +
           "\t\t\t'Сопровождение Платформы управления контейнерами (00018435)' ) AND\n" +
           "\t\t\tprob1.hpc_status NOT IN ( '6 Выполнен',\n" +
           "\t\t\t'7 Закрыт',\n" +
           "\t\t\t'5 Выполнен',\n" +
           "\t\t\t'6 Закрыт') \n" +
           "\t\tUNION\n" +
           "SELECT\n" +
           "\tprob1.\"NUMBER\",\n" +
           "\tBRIEF_DESCRIPTION,\n" +
           "\tPRIORITY_CODE,\n" +
           "\tOPEN_TIME,\n" +
           "\tto_char(ACTION) AS ACTION,\n" +
           "\t\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){6}.*'), 'htt.*$') AS ZABBIX_HISTORY,\n" +
           "\t\t\tREPLACE(REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){4}.*'), 'Хост:\\s.*$'), 'Хост: ') AS HOST,\n" +
           "\t\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){1}.*'), 'Проблема.*$') AS PROBLEM,\n" +
           "\tHPC_STATUS,\n" +
           "\tsubstr(prob1.hpc_assignee_name, 1, instr(prob1.hpc_assignee_name, '(') - 2) \n" +
           "\tAS HPC_ASSIGNEE_NAME,\n" +
           "\tsubstr(prob1.hpc_assignment, 1, instr(prob1.hpc_assignment, '(') - 2)       \n" +
           "\tAS HPC_ASSIGNMENT,\n" +
           "\tHPC_CREATED_BY_NAME, 'RESOLUTION' AS RESOLUTION\n" +
           "FROM\n" +
           "\tsmprimary.SBPROBSUMMARYTSM1 prob1 \n" +
           "WHERE\n" +
           "\tprob1.hpc_assignment IN ( 'ЦСИТ Серверы приложений (00001092)',\n" +
           "\t'ЦСИТ ОАСП Стандартные платформы (00003984)',\n" +
           "\t'Сопровождение WAS ОСЦИТУ ДВБ (00001341)',\n" +
           "\t'ЦИ Запад Стандартные платформы (00011217)',\n" +
           "\t'ЦИ Центр Стандартные платформы (00011213)',\n" +
           "\t'ЦСИТ ОАСП Специализированные платформы (00003978)',\n" +
           "\t'ЦСИТ ОАСП Интеграционные платформы (00003982)',\n" +
           "\t'ЦИ ОАСП Системы очередей сообщений (00014339)',\n" +
           "\t'ЦИ ОАСП Шлюзовые решения (00014345)',\n" +
           "\t'ЦИ ОАСП Технологический стэк ППРБ (00014341)',\n" +
           "\t'ЦИ Восток Интеграционные платформы (00011221)',\n" +
           "\t'ЦИ Запад Интеграционные платформы (00011219)',\n" +
           "\t'ЦИ Центр Интеграционные платформы (00011215)',\n" +
           "\t'СБТ ДК ОСА Серверы приложений (Щелчков Р.А.) (00010280)',\n" +
           "\t'Сопровождение Платформы управления контейнерами (00018435)' ) AND\n" +
           "\tprob1.hpc_status NOT IN ( '6 Выполнен',\n" +
           "\t'7 Закрыт',\n" +
           "\t'5 Выполнен',\n" +
           "\t'6 Закрыт') ) \n" +
           "WHERE\n" +
           "\tHPC_CREATED_BY_NAME = 'Технологический пользователь АС ZABBIX_SI (958891)'",
           nativeQuery = true)
   List<UspIncidentData> findAll();

//    List<UspIncidentData> findByServerName(String ServerName);
//
//    UspIncidentData findById(long id);
}
