package ru.sberbank.uspincidentreport.repo;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;

import java.util.List;

@Repository
@Profile("!dev & !prod")
public interface UspIncidentRepo extends CrudRepository<UspIncidentData, String> {

   @Override
   @Query(value = "SELECT\n" +
           "    *\n" +
           "FROM\n" +
           "    (\tSELECT\n" +
           "             prob1.\"NUMBER\",\n" +
           "             BRIEF_DESCRIPTION,\n" +
           "             PRIORITY_CODE,\n" +
           "             OPEN_TIME,\n" +
           "             to_char(dbms_lob.substr(ACTION,2000,1))\n" +
           "                                     AS ACTION,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){6}.*'),\n" +
           "                           'htt.*$') AS ZABBIX_HISTORY,\n" +
           "             REPLACE\n" +
           "                 (\n" +
           "                     REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){4}.*'),\n" +
           "                                   'Хост:\\s.*$'),\n" +
           "                     'Хост: '\n" +
           "                 )\n" +
           "                                     AS HOST,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)),\n" +
           "                                         '^.*(\\s.*){1}.*'),\n" +
           "                           'Проблема.*$')\n" +
           "                                     AS PROBLEM,\n" +
           "             HPC_STATUS,\n" +
           "             substr(prob1.hpc_assignee_name, 1, instr(prob1.hpc_assignee_name, '(') - 2)\n" +
           "                                     AS HPC_ASSIGNEE_NAME,\n" +
           "             substr(prob1.hpc_assignment, 1, instr(prob1.hpc_assignment, '(') - 2 )\n" +
           "                                     AS HPC_ASSIGNMENT,\n" +
           "             HPC_CREATED_BY_NAME,\n" +
           "             'RESOLUTION'\n" +
           "                                     AS RESOLUTION,\n" +
           "             OPENED_BY,\n" +
           "             AFFECTED_ITEM, 'RESOLUTION_GUIDE' AS RESOLUTION_GUIDE\n" +
           "         FROM\n" +
           "             SMPRIMARYSAFE.probsummarym1 prob1\n" +
           "         WHERE\n" +
           "                 prob1.hpc_assignment IN ( 'ЦСИТ Серверы приложений (00001092)',\n" +
           "                                           'ЦСИТ ОАСП Стандартные платформы (00003984)',\n" +
           "                                           'Сопровождение WAS ОСЦИТУ ДВБ (00001341)',\n" +
           "                                           'ЦИ Запад Стандартные платформы (00011217)',\n" +
           "                                           'ЦИ Центр Стандартные платформы (00011213)',\n" +
           "                                           'ЦСИТ ОАСП Специализированные платформы (00003978)',\n" +
           "                                           'ЦСИТ ОАСП Интеграционные платформы (00003982)',\n" +
           "                                           'ЦИ ОАСП Системы очередей сообщений (00014339)',\n" +
           "                                           'ЦИ ОАСП Шлюзовые решения (00014345)',\n" +
           "                                           'ЦИ ОАСП Технологический стэк ППРБ (00014341)',\n" +
           "                                           'ЦИ Восток Интеграционные платформы (00011221)',\n" +
           "                                           'ЦИ Запад Интеграционные платформы (00011219)',\n" +
           "                                           'ЦИ Центр Интеграционные платформы (00011215)',\n" +
           "                                           'СБТ ДК ОСА Серверы приложений (Щелчков Р.А.) (00010280)',\n" +
           "                                           'Сопровождение Платформы управления контейнерами (00018435)',\n" +
           "                                           'SberInfra УСП Интеграционные платформы (Гоголев К.Ю.) (00019273)',\n" +
           "                                           'SberInfra Сопровождение Платформы управления контейнерами (Косов М.В.)')\n" +
           "                                            AND\n" +
           "                 prob1.hpc_status NOT IN ( '6 Выполнен',\n" +
           "                                           '7 Закрыт',\n" +
           "                                           '5 Выполнен',\n" +
           "                                           '6 Закрыт')\n" +
           "         UNION\n" +
           "         SELECT\n" +
           "             prob1.\"NUMBER\",\n" +
           "             BRIEF_DESCRIPTION,\n" +
           "             PRIORITY_CODE,\n" +
           "             OPEN_TIME,\n" +
           "             to_char(dbms_lob.substr(ACTION,2000,1))                                                           AS\n" +
           "                                                                                          ACTION,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){6}.*'), 'htt.*$') AS\n" +
           "                                                                                          ZABBIX_HISTORY,\n" +
           "             REPLACE\n" +
           "                 (\n" +
           "                     REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){4}.*'),\n" +
           "                                   'Хост:\\s.*$'),\n" +
           "                     'Хост: '\n" +
           "                 )\n" +
           "                                                                                       AS HOST,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)),\n" +
           "                                         '^.*(\\s.*){1}.*'),\n" +
           "                           'Проблема.*$') AS PROBLEM,\n" +
           "             HPC_STATUS,\n" +
           "             substr(prob1.hpc_assignee_name,\n" +
           "                    1,\n" +
           "                    instr(prob1.hpc_assignee_name,\n" +
           "                          '(') - 2)      AS HPC_ASSIGNEE_NAME,\n" +
           "             substr(prob1.hpc_assignment,\n" +
           "                    1,\n" +
           "                    instr(prob1.hpc_assignment,\n" +
           "                          '(') - 2)      AS HPC_ASSIGNMENT,\n" +
           "             HPC_CREATED_BY_NAME,\n" +
           "             'RESOLUTION' AS RESOLUTION,\n" +
           "             'OPENED_BY',\n" +
           "             AFFECTED_ITEM, 'RESOLUTION_GUIDE' AS RESOLUTION_GUIDE\n" +
           "         FROM\n" +
           "             SMPRIMARYSAFE.SBPROBSUMMARYTSM1 prob1\n" +
           "         WHERE\n" +
           "                 prob1.hpc_assignment IN ( 'ЦСИТ Серверы приложений (00001092)',\n" +
           "                                           'ЦСИТ ОАСП Стандартные платформы (00003984)',\n" +
           "                                           'Сопровождение WAS ОСЦИТУ ДВБ (00001341)',\n" +
           "                                           'ЦИ Запад Стандартные платформы (00011217)',\n" +
           "                                           'ЦИ Центр Стандартные платформы (00011213)',\n" +
           "                                           'ЦСИТ ОАСП Специализированные платформы (00003978)',\n" +
           "                                           'ЦСИТ ОАСП Интеграционные платформы (00003982)',\n" +
           "                                           'ЦИ ОАСП Системы очередей сообщений (00014339)',\n" +
           "                                           'ЦИ ОАСП Шлюзовые решения (00014345)',\n" +
           "                                           'ЦИ ОАСП Технологический стэк ППРБ (00014341)',\n" +
           "                                           'ЦИ Восток Интеграционные платформы (00011221)',\n" +
           "                                           'ЦИ Запад Интеграционные платформы (00011219)',\n" +
           "                                           'ЦИ Центр Интеграционные платформы (00011215)',\n" +
           "                                           'СБТ ДК ОСА Серверы приложений (Щелчков Р.А.) (00010280)',\n" +
           "                                           'Сопровождение Платформы управления контейнерами (00018435)',\n" +
           "                                           'SberInfra УСП Интеграционные платформы (Гоголев К.Ю.) (00019273)',\n" +
           "                                           'SberInfra Сопровождение Платформы управления контейнерами (Косов М.В.)')\n" +
           "                                            AND\n" +
           "                 prob1.hpc_status NOT IN ( '6 Выполнен',\n" +
           "                                           '7 Закрыт',\n" +
           "                                           '5 Выполнен',\n" +
           "                                           '6 Закрыт') )\n" +
           "WHERE\n" +
           "        HPC_CREATED_BY_NAME in ('Технологический пользователь АС ZABBIX_SI (958891)', 'INT_SC_SERVICE_PROXY (756759)')", nativeQuery = true)
   List<UspIncidentData> findAll();

   @Query(value = "SELECT\n" +
           "\t* \n" +
           "FROM\n" +
           "\t(\tSELECT\n" +
           "\t\t\tprob1.\"NUMBER\",\n" +
           "\t\t\tBRIEF_DESCRIPTION,\n" +
           "\t\t\tPRIORITY_CODE,\n" +
           "\t\t\tOPEN_TIME,\n" +
           "\t\t\tto_char(dbms_lob.substr(ACTION,2000,1))                                                           \n" +
           "\t\t\tAS ACTION,\n" +
           "\t\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){6}.*'), \n" +
           "\t\t\t'htt.*$') AS ZABBIX_HISTORY,\n" +
           "\t\tREPLACE\n" +
           "\t\t\t(\n" +
           "\t\t\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){4}.*'), \n" +
           "\t\t\t\t'Хост:\\s.*$'),\n" +
           "\t\t\t\t'Хост: '\n" +
           "\t\t\t)\n" +
           "\t\t\tAS HOST,\n" +
           "\t\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)),\n" +
           "\t'^.*(\\s.*){1}.*'),\n" +
           "\t'Проблема.*$')                                                              \n" +
           "\tAS PROBLEM,\n" +
           "\tHPC_STATUS,\n" +
           "\tsubstr(prob1.hpc_assignee_name, 1, instr(prob1.hpc_assignee_name, '(') - 2) \n" +
           "\tAS HPC_ASSIGNEE_NAME,\n" +
           "\tsubstr(prob1.hpc_assignment, 1, instr(prob1.hpc_assignment, '(') - 2 )      \n" +
           "\tAS HPC_ASSIGNMENT,\n" +
           "\tHPC_CREATED_BY_NAME,\n" +
           "\t'RESOLUTION'                                                                \n" +
           "\tAS RESOLUTION,\n" +
           "\tOPENED_BY, \n" +
           "\tAFFECTED_ITEM, 'RESOLUTION_GUIDE' AS RESOLUTION_GUIDE\n" +
           "FROM\n" +
           "\tSMPRIMARYSAFE.probsummarym1 prob1 \n" +
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
           "\t'Сопровождение Платформы управления контейнерами (00018435)',\n" +
           "\t'SberInfra УСП Интеграционные платформы (Гоголев К.Ю.) (00019273)',\n" +
           "\t'SberInfra Сопровождение Платформы управления контейнерами (Косов М.В.)')\n" +
           "UNION\n" +
           "SELECT\n" +
           "\tprob1.\"NUMBER\",\n" +
           "\tBRIEF_DESCRIPTION,\n" +
           "\tPRIORITY_CODE,\n" +
           "\tOPEN_TIME,\n" +
           "\tto_char(dbms_lob.substr(ACTION,2000,1))                                                           AS \n" +
           "\tACTION,\n" +
           "\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){6}.*'), 'htt.*$') AS \n" +
           "\tZABBIX_HISTORY,\n" +
           "REPLACE\n" +
           "\t(\n" +
           "\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){4}.*'), \n" +
           "\t\t'Хост:\\s.*$'),\n" +
           "\t\t'Хост: '\n" +
           "\t)\n" +
           "\tAS HOST,\n" +
           "\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)),\n" +
           "\t'^.*(\\s.*){1}.*'),\n" +
           "\t'Проблема.*$') AS PROBLEM,\n" +
           "\tHPC_STATUS,\n" +
           "\tsubstr(prob1.hpc_assignee_name,\n" +
           "\t1,\n" +
           "\tinstr(prob1.hpc_assignee_name,\n" +
           "\t'(') - 2)      AS HPC_ASSIGNEE_NAME,\n" +
           "\tsubstr(prob1.hpc_assignment,\n" +
           "\t1,\n" +
           "\tinstr(prob1.hpc_assignment,\n" +
           "\t'(') - 2)      AS HPC_ASSIGNMENT,\n" +
           "\tHPC_CREATED_BY_NAME,\n" +
           "\t'RESOLUTION'   AS RESOLUTION,\n" +
           "\t'OPENED_BY'    AS OPENED_BY, \n" +
           "\tAFFECTED_ITEM, 'RESOLUTION_GUIDE' AS RESOLUTION_GUIDE\n" +
           "FROM\n" +
           "\tSMPRIMARYSAFE.SBPROBSUMMARYTSM1 prob1 \n" +
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
           "\t'Сопровождение Платформы управления контейнерами (00018435)',\n" +
           "\t'SberInfra УСП Интеграционные платформы (Гоголев К.Ю.) (00019273)',\n" +
           "\t'SberInfra Сопровождение Платформы управления контейнерами (Косов М.В.)'))\n" +
           "WHERE\n" +
           "\tPROBLEM LIKE REPLACE (:triggerDescription, '\"', '*')\n" +
           "\tAND\n" +
           "\tHPC_CREATED_BY_NAME in ('Технологический пользователь АС ZABBIX_SI (958891)', 'INT_SC_SERVICE_PROXY (756759)')\n" +
           "\tAND OPEN_TIME BETWEEN TO_TIMESTAMP(:startDate, 'DD.MM.RRRR HH24:MI:SS')" +
           " AND TO_TIMESTAMP(:endDate, 'DD.MM.RRRR HH24:MI:SS')" , nativeQuery = true)
   List<UspIncidentData> findIncByTrigger(@Param("startDate") String startDate, @Param("endDate") String endDate,
                                          @Param("triggerDescription") String triggerDescription);

}
