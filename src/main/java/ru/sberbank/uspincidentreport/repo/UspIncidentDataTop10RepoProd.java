package ru.sberbank.uspincidentreport.repo;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataTop10;

import java.util.List;

@Repository
@Profile("prod")
public interface UspIncidentDataTop10RepoProd extends UspIncidentDataTop10Repo{
    @Query(value = "WITH RWS AS (\n" +
            "    SELECT top_10.*, row_number() over (PARTITION BY \"AFFECTED_ITEM\" ORDER BY \"COUNT_INC\" DESC) RN\n" +
            "    FROM (\n" +
            "             SELECT\n" +
            "                 \"AFFECTED_ITEM\", HOST, COUNT (\"NUMBER\")AS \"COUNT_INC\"\n" +
            "             FROM\n" +
            "                 (\tSELECT\n" +
            "                          prob1.\"NUMBER\",\n" +
            "                          BRIEF_DESCRIPTION,\n" +
            "                          PRIORITY_CODE,\n" +
            "                          OPEN_TIME,\n" +
            "                          to_char(dbms_lob.substr(ACTION,2000,1))\n" +
            "                                                  AS ACTION,\n" +
            "                          REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){6}.*'),\n" +
            "                                        'htt.*$') AS ZABBIX_HISTORY,\n" +
            "                          REPLACE\n" +
            "                              (\n" +
            "                                  REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){4}.*'),\n" +
            "                                                'Хост:\\s.*$'),\n" +
            "                                  'Хост: '\n" +
            "                              )\n" +
            "                                                  AS HOST,\n" +
            "                          REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)),\n" +
            "                                                      '^.*(\\s.*){1}.*'),\n" +
            "                                        'Проблема.*$')\n" +
            "                                                  AS PROBLEM,\n" +
            "                          HPC_STATUS,\n" +
            "                          substr(prob1.hpc_assignee_name, 1, instr(prob1.hpc_assignee_name, '(') - 2)\n" +
            "                                                  AS HPC_ASSIGNEE_NAME,\n" +
            "                          substr(prob1.hpc_assignment, 1, instr(prob1.hpc_assignment, '(') - 2 )\n" +
            "                                                  AS HPC_ASSIGNMENT,\n" +
            "                          HPC_CREATED_BY_NAME,\n" +
            "                          'RESOLUTION'\n" +
            "                                                  AS RESOLUTION,\n" +
            "                          OPENED_BY,\n" +
            "                          AFFECTED_ITEM\n" +
            "                      FROM\n" +
            "                          smprimary.probsummarym1 prob1\n" +
            "                      WHERE\n" +
            "                              prob1.AFFECTED_ITEM IN ( 'CI02021303',\n" +
            "                                                       'CI02021304',\n" +
            "                                                       'CI02584076',\n" +
            "                                                       'CI02584077',\n" +
            "                                                       'CI02584078',\n" +
            "                                                       'CI02021298',\n" +
            "                                                       'CI02021301',\n" +
            "                                                       'CI02021292',\n" +
            "                                                       'CI02021302',\n" +
            "                                                       'CI02021294',\n" +
            "                                                       'CI02021296',\n" +
            "                                                       'CI02021299',\n" +
            "                                                       'CI02021293',\n" +
            "                                                       'CI02021295',\n" +
            "                                                       'CI02192117',\n" +
            "                                                       'CI02021290',\n" +
            "                                                       'CI02021291',\n" +
            "                                                       'CI02021300',\n" +
            "                                                       'CI02192118',\n" +
            "                                                       'CI02021306',\n" +
            "                                                       'CI00737141',\n" +
            "                                                       'CI00737140',\n" +
            "                                                       'CI00737137',\n" +
            "                                                       'CI02008623',\n" +
            "                                                       'CI01563053', \n" +
            "                                                       'CI04178739', \n" +
            "                                                       'CI04085569')\n" +
            "                      UNION\n" +
            "                      SELECT\n" +
            "                          prob1.\"NUMBER\",\n" +
            "                          BRIEF_DESCRIPTION,\n" +
            "                          PRIORITY_CODE,\n" +
            "                          OPEN_TIME,\n" +
            "                          to_char(dbms_lob.substr(ACTION,2000,1))                                                           AS\n" +
            "                                                                                                       ACTION,\n" +
            "                          REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){6}.*'), 'htt.*$') AS\n" +
            "                                                                                                       ZABBIX_HISTORY,\n" +
            "                          REPLACE\n" +
            "                              (\n" +
            "                                  REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)), '^.*(\\s.*){4}.*'),\n" +
            "                                                'Хост:\\s.*$'),\n" +
            "                                  'Хост: '\n" +
            "                              )\n" +
            "                                                                                                    AS HOST,\n" +
            "                          REGEXP_SUBSTR(REGEXP_SUBSTR(to_char(dbms_lob.substr(ACTION,2000,1)),\n" +
            "                                                      '^.*(\\s.*){1}.*'),\n" +
            "                                        'Проблема.*$') AS PROBLEM,\n" +
            "                          HPC_STATUS,\n" +
            "                          substr(prob1.hpc_assignee_name,\n" +
            "                                 1,\n" +
            "                                 instr(prob1.hpc_assignee_name,\n" +
            "                                       '(') - 2)      AS HPC_ASSIGNEE_NAME,\n" +
            "                          substr(prob1.hpc_assignment,\n" +
            "                                 1,\n" +
            "                                 instr(prob1.hpc_assignment,\n" +
            "                                       '(') - 2)      AS HPC_ASSIGNMENT,\n" +
            "                          HPC_CREATED_BY_NAME,\n" +
            "                          'RESOLUTION'   AS RESOLUTION,\n" +
            "                          'OPENED_BY'    AS OPENED_BY,\n" +
            "                          AFFECTED_ITEM\n" +
            "                      FROM\n" +
            "                          smprimary.SBPROBSUMMARYTSM1 prob1\n" +
            "                      WHERE\n" +
            "                              prob1.AFFECTED_ITEM IN ( 'CI02021303',\n" +
            "                                                       'CI02021304',\n" +
            "                                                       'CI02584076',\n" +
            "                                                       'CI02584077',\n" +
            "                                                       'CI02584078',\n" +
            "                                                       'CI02021298',\n" +
            "                                                       'CI02021301',\n" +
            "                                                       'CI02021292',\n" +
            "                                                       'CI02021302',\n" +
            "                                                       'CI02021294',\n" +
            "                                                       'CI02021296',\n" +
            "                                                       'CI02021299',\n" +
            "                                                       'CI02021293',\n" +
            "                                                       'CI02021295',\n" +
            "                                                       'CI02192117',\n" +
            "                                                       'CI02021290',\n" +
            "                                                       'CI02021291',\n" +
            "                                                       'CI02021300',\n" +
            "                                                       'CI02192118',\n" +
            "                                                       'CI02021306',\n" +
            "                                                       'CI00737141',\n" +
            "                                                       'CI00737140',\n" +
            "                                                       'CI00737137',\n" +
            "                                                       'CI02008623',\n" +
            "                                                       'CI01563053', \n" +
            "                                                       'CI04178739', \n" +
            "                                                       'CI04085569'))\n" +
            "             WHERE\n" +
            "        HPC_CREATED_BY_NAME in ('Технологический пользователь АС ZABBIX_SI (958891)', 'INT_SC_SERVICE_PROXY (756759)')\n" +
            "AND OPEN_TIME BETWEEN TO_TIMESTAMP(:startDate, 'DD.MM.RRRR HH24:MI:SS') AND TO_TIMESTAMP(:endDate, 'DD.MM.RRRR HH24:MI:SS')\n" +
            "             GROUP BY \"AFFECTED_ITEM\", HOST\n" +
            "             ORDER BY \"AFFECTED_ITEM\", \"COUNT_INC\" DESC) top_10)\n" +
            "SELECT \"AFFECTED_ITEM\", \"HOST\", \"COUNT_INC\" FROM RWS WHERE RN <=10",
            nativeQuery = true)
    List<IUspIncidentDataTop10> findTop10IncCount(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
