package ru.sberbank.uspincidentreport.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataTotalCount;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;

import java.util.List;

@Repository
public interface UspIncidentDataTotalCountRepo extends CrudRepository<UspIncidentData, String> {


    //   @Override
//    @Query(value = "SELECT\n" +
//            "\t\"HPC_ASSIGNMENT\", COUNT (\"NUMBER\")AS \"COUNT_INC\"\n" +
//            "FROM\n" +
//            "\t(\tSELECT\n" +
//            "\t\t\tprob1.\"NUMBER\",\n" +
//            "\t\t\tBRIEF_DESCRIPTION,\n" +
//            "\t\t\tPRIORITY_CODE,\n" +
//            "\t\t\tOPEN_TIME,\n" +
//            "\t\t\tto_char(ACTION)                                                           \n" +
//            "\t\t\tAS ACTION,\n" +
//            "\t\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){6}.*'), \n" +
//            "\t\t\t'htt.*$') AS ZABBIX_HISTORY,\n" +
//            "\t\tREPLACE\n" +
//            "\t\t\t(\n" +
//            "\t\t\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){4}.*'), \n" +
//            "\t\t\t\t'Хост:\\s.*$'),\n" +
//            "\t\t\t\t'Хост: '\n" +
//            "\t\t\t)\n" +
//            "\t\t\tAS HOST,\n" +
//            "\t\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION),\n" +
//            "\t'^.*(\\s.*){1}.*'),\n" +
//            "\t'Проблема.*$')                                                              \n" +
//            "\tAS PROBLEM,\n" +
//            "\tHPC_STATUS,\n" +
//            "\tsubstr(prob1.hpc_assignee_name, 1, instr(prob1.hpc_assignee_name, '(') - 2) \n" +
//            "\tAS HPC_ASSIGNEE_NAME,\n" +
//            "\tsubstr(prob1.hpc_assignment, 1, instr(prob1.hpc_assignment, '(') - 2 )      \n" +
//            "\tAS HPC_ASSIGNMENT,\n" +
//            "\tHPC_CREATED_BY_NAME,\n" +
//            "\t'RESOLUTION'                                                                \n" +
//            "\tAS RESOLUTION,\n" +
//            "\tOPENED_BY \n" +
//            "FROM\n" +
//            "\tsmprimary.probsummarym1 prob1 \n" +
//            "WHERE\n" +
//            "\tprob1.hpc_assignment IN ( :groupAssignment) \n" +
//            "UNION\n" +
//            "SELECT\n" +
//            "\tprob1.\"NUMBER\",\n" +
//            "\tBRIEF_DESCRIPTION,\n" +
//            "\tPRIORITY_CODE,\n" +
//            "\tOPEN_TIME,\n" +
//            "\tto_char(ACTION)                                                           AS \n" +
//            "\tACTION,\n" +
//            "\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){6}.*'), 'htt.*$') AS \n" +
//            "\tZABBIX_HISTORY,\n" +
//            "REPLACE\n" +
//            "\t(\n" +
//            "\t\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION), '^.*(\\s.*){4}.*'), \n" +
//            "\t\t'Хост:\\s.*$'),\n" +
//            "\t\t'Хост: '\n" +
//            "\t)\n" +
//            "\tAS HOST,\n" +
//            "\tREGEXP_SUBSTR(REGEXP_SUBSTR(to_char(ACTION),\n" +
//            "\t'^.*(\\s.*){1}.*'),\n" +
//            "\t'Проблема.*$') AS PROBLEM,\n" +
//            "\tHPC_STATUS,\n" +
//            "\tsubstr(prob1.hpc_assignee_name,\n" +
//            "\t1,\n" +
//            "\tinstr(prob1.hpc_assignee_name,\n" +
//            "\t'(') - 2)      AS HPC_ASSIGNEE_NAME,\n" +
//            "\tsubstr(prob1.hpc_assignment,\n" +
//            "\t1,\n" +
//            "\tinstr(prob1.hpc_assignment,\n" +
//            "\t'(') - 2)      AS HPC_ASSIGNMENT,\n" +
//            "\tHPC_CREATED_BY_NAME,\n" +
//            "\t'RESOLUTION'   AS RESOLUTION,\n" +
//            "\t'OPENED_BY'    AS OPENED_BY \n" +
//            "FROM\n" +
//            "\tsmprimary.SBPROBSUMMARYTSM1 prob1 \n" +
//            "WHERE\n" +
//            "\tprob1.hpc_assignment IN ( :groupAssignment)) \n" +
//            "WHERE\n" +
//            "\tOPENED_BY = 'int_zabbix_si' AND OPEN_TIME BETWEEN TO_TIMESTAMP(:startDate '00:00:00', 'DD.MM.RRRR HH24:MI:SS') AND TO_TIMESTAMP(:endDate '23:59:59', 'DD.MM.RRRR HH24:MI:SS')\n" +
//            "\tGROUP BY \"HPC_ASSIGNMENT\" ORDER BY \"COUNT_INC\" DESC",
//            nativeQuery = true)
        @Query(value = "select p.HPC_ASSIGNMENT as Assignment, COUNT (p.NUMBER) AS countInc from probsummarym1 p where p.OPEN_TIME BETWEEN TO_CHAR(:startDate, 'dd.MM.yyyy hh:mm:ss') AND TO_CHAR(:endDate, 'dd.MM.yyyy hh:mm:ss') GROUP BY Assignment ORDER BY countInc DESC", nativeQuery = true)

//    @Query(value = "select p.HPC_ASSIGNMENT as Assignment, COUNT (p.NUMBER) AS countInc from probsummarym1 p GROUP BY Assignment ORDER BY countInc DESC", nativeQuery = true)
    List<IUspIncidentDataTotalCount> findIncCount(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
