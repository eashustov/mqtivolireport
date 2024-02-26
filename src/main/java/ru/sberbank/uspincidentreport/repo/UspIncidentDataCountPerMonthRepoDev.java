package ru.sberbank.uspincidentreport.repo;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataCountPerMonth;

import java.util.List;

@Repository
@Profile("dev")
public interface UspIncidentDataCountPerMonthRepoDev extends UspIncidentDataCountPerMonthRepo {
    @Query(value = "select p.HPC_ASSIGNMENT as HPC_ASSIGNMENT,\n" +
            "       MONTHNAME(PARSEDATETIME(p.OPEN_TIME, 'yyyy-MM-dd HH:mm:ss')) AS \"MONTH\",\n" +
            "       MONTH(PARSEDATETIME(p.OPEN_TIME, 'yyyy-MM-dd HH:mm:ss')) AS MONTH_NUMBER,\n" +
            "       YEAR(PARSEDATETIME(p.OPEN_TIME, 'yyyy-MM-dd HH:mm:ss')) AS \"YEAR\",\n" +
            "       COUNT (p.NUMBER) AS COUNT_INC\n" +
            "from probsummarym1 p\n" +
            "GROUP BY p.HPC_ASSIGNMENT, \"MONTH\", MONTH_NUMBER, \"YEAR\"\n" +
            "ORDER BY HPC_ASSIGNMENT, \"YEAR\", month_number ASC", nativeQuery = true)
    List<IUspIncidentDataCountPerMonth> findIncAssignmentCountPerMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "select p.AFFECTED_ITEM as AFFECTED_ITEM,\n" +
            "MONTHNAME(PARSEDATETIME(p.OPEN_TIME, 'yyyy-MM-dd HH:mm:ss')) AS \"MONTH\",\n" +
            "MONTH(PARSEDATETIME(p.OPEN_TIME, 'yyyy-MM-dd HH:mm:ss')) AS MONTH_NUMBER,\n" +
            "YEAR(PARSEDATETIME(p.OPEN_TIME, 'yyyy-MM-dd HH:mm:ss')) AS \"YEAR\",\n" +
            "COUNT (p.NUMBER) AS COUNT_INC\n" +
            "from probsummarym1 p\n" +
            "GROUP BY AFFECTED_ITEM, \"MONTH\", MONTH_NUMBER, \"YEAR\"\n" +
            "ORDER BY AFFECTED_ITEM, \"YEAR\", MONTH_NUMBER ASC", nativeQuery = true)
    List<IUspIncidentDataCountPerMonth> findIncAffectedItemCountPerMonth(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
