package ru.sberbank.uspincidentreport.repo;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataTotalCount;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;
import java.util.List;

@Repository
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
           "             to_char(ACTION)\n" +
           "                                     AS ACTION,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){6}.*'),\n" +
           "                           'htt.*$') AS ZABBIX_HISTORY,\n" +
           "             REPLACE\n" +
           "                 (\n" +
           "                     REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){4}.*'),\n" +
           "                                   'Хост:\\s.*$'),\n" +
           "                     'Хост: '\n" +
           "                 )\n" +
           "                                     AS HOST,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION),\n" +
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
           "             smprimary.probsummarym1 prob1\n" +
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
           "                                           'SberInfra УСП Интеграционные платформы (Гоголев К.Ю.) (00019273)') AND\n" +
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
           "             to_char(ACTION)                                                           AS\n" +
           "                                                                                          ACTION,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){6}.*'), 'htt.*$') AS\n" +
           "                                                                                          ZABBIX_HISTORY,\n" +
           "             REPLACE\n" +
           "                 (\n" +
           "                     REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){4}.*'),\n" +
           "                                   'Хост:\\s.*$'),\n" +
           "                     'Хост: '\n" +
           "                 )\n" +
           "                                                                                       AS HOST,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION),\n" +
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
           "             'RESOLUTION'   AS RESOLUTION,\n" +
           "             'OPENED_BY',\n" +
           "             AFFECTED_ITEM, 'RESOLUTION_GUIDE' AS RESOLUTION_GUIDE\n" +
           "         FROM\n" +
           "             smprimary.SBPROBSUMMARYTSM1 prob1\n" +
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
           "                                           'SberInfra УСП Интеграционные платформы (Гоголев К.Ю.) (00019273)') AND\n" +
           "                 prob1.hpc_status NOT IN ( '6 Выполнен',\n" +
           "                                           '7 Закрыт',\n" +
           "                                           '5 Выполнен',\n" +
           "                                           '6 Закрыт') )\n" +
           "WHERE\n" +
           "        OPENED_BY = 'int_zabbix_si'",
           nativeQuery = true)

   List<UspIncidentData> findAll();


//   @Query(value = "select * from probsummarym1 p WHERE p.HPC_ASSIGNMENT IN (:assignmentGroup) LIMIT 500", nativeQuery = true)
//   List<UspIncidentData> findAll(@Param("assignmentGroup") String assignmentGroup);

//   @Query(value = "select * from probsummarym1 p where p.OPEN_TIME BETWEEN TO_CHAR(:startDate, 'dd.MM.yyyy HH:mm:ss') AND TO_CHAR(:endDate, 'dd.MM.yyyy HH:mm:ss')", nativeQuery = true)
//   List<UspIncidentData> findIncByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

//   @Query(value = "select * from probsummarym1 LIMIT 500", nativeQuery = true)
   @Query(value = "SELECT\n" +
           "    *\n" +
           "FROM\n" +
           "    (\tSELECT\n" +
           "             prob1.\"NUMBER\",\n" +
           "             BRIEF_DESCRIPTION,\n" +
           "             PRIORITY_CODE,\n" +
           "             OPEN_TIME,\n" +
           "             to_char(ACTION)\n" +
           "                                     AS ACTION,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){6}.*'),\n" +
           "                           'htt.*$') AS ZABBIX_HISTORY,\n" +
           "             REPLACE\n" +
           "                 (\n" +
           "                     REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){4}.*'),\n" +
           "                                   'Хост:\\s.*$'),\n" +
           "                     'Хост: '\n" +
           "                 )\n" +
           "                                     AS HOST,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION),\n" +
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
           "             AFFECTED_ITEM , 'RESOLUTION_GUIDE' AS RESOLUTION_GUIDE\n" +
           "         FROM\n" +
           "             smprimary.probsummarym1 prob1\n" +
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
           "                                           'SberInfra УСП Интеграционные платформы (Гоголев К.Ю.) (00019273)')\n" +
           "         UNION\n" +
           "         SELECT\n" +
           "             prob1.\"NUMBER\",\n" +
           "             BRIEF_DESCRIPTION,\n" +
           "             PRIORITY_CODE,\n" +
           "             OPEN_TIME,\n" +
           "             to_char(ACTION)                                                           AS\n" +
           "                                                                                          ACTION,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){6}.*'), 'htt.*$') AS\n" +
           "                                                                                          ZABBIX_HISTORY,\n" +
           "             REPLACE\n" +
           "                 (\n" +
           "                     REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){4}.*'),\n" +
           "                                   'Хост:\\s.*$'),\n" +
           "                     'Хост: '\n" +
           "                 )\n" +
           "                                                                                       AS HOST,\n" +
           "             REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION),\n" +
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
           "             'RESOLUTION',\n" +
           "             'OPENED_BY',\n" +
           "             AFFECTED_ITEM, 'RESOLUTION_GUIDE' AS RESOLUTION_GUIDE\n" +
           "         FROM\n" +
           "             smprimary.SBPROBSUMMARYTSM1 prob1\n" +
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
           "                                           'SberInfra УСП Интеграционные платформы (Гоголев К.Ю.) (00019273)'))\n" +
           "WHERE\n" +
           "        OPENED_BY = 'int_zabbix_si' AND OPEN_TIME BETWEEN TO_TIMESTAMP(:startDate ' 00:00:00', 'DD.MM.RRRR HH24:MI:SS') AND TO_TIMESTAMP(:endDate ' 23:59:59', 'DD.MM.RRRR HH24:MI:SS')", nativeQuery = true)
   List<UspIncidentData> findIncByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);
//   List<UspIncidentData> findIncByDate(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("assignmentGroup") String assignmentGroup);



}
