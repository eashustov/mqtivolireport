package ru.sberbank.uspincidentreport.repo;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataTotalCount;

import java.util.List;

@Repository
@Profile("dev")
public interface UspIncidentDataTotalCountRepoDev extends UspIncidentDataTotalCountRepo{
    @Query(value = "select p.HPC_ASSIGNMENT AS HPC_ASSIGNMENT, COUNT (p.NUMBER) AS COUNT_INC from probsummarym1 p GROUP BY HPC_ASSIGNMENT ORDER BY COUNT_INC DESC", nativeQuery = true)
    List<IUspIncidentDataTotalCount> findIncByAssignmentCount(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "select p.AFFECTED_ITEM AS AFFECTED_ITEM, COUNT (p.NUMBER) AS COUNT_INC from probsummarym1 p GROUP BY AFFECTED_ITEM ORDER BY COUNT_INC DESC", nativeQuery = true)
    List<IUspIncidentDataTotalCount> findIncByAffectedItemCount(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
