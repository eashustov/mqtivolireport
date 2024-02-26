package ru.sberbank.uspincidentreport.repo;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;

import java.util.List;

@Repository
@Profile("dev")
public interface UspIncidentAnaliticsRepoDev extends UspIncidentAnaliticsRepo{
    @Query(value = "select * from probsummarym1 p where p.OPEN_TIME BETWEEN TO_CHAR(:startDate, 'dd.MM.yyyy HH:mm:ss') AND TO_CHAR(:endDate, 'dd.MM.yyyy HH:mm:ss')", nativeQuery = true)
    List<UspIncidentData> findIncByDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

    @Query(value = "select * from probsummarym1 p where concat (upper(p.NUMBER), ' ', upper(p.HOST), ' ', upper(p.HPC_ASSIGNEE_NAME), ' ', upper(p.HPC_ASSIGNMENT), ' ', upper(p.AFFECTED_ITEM)) like concat ('%', upper(:searchFilter), '%') and p.OPEN_TIME BETWEEN TO_CHAR(:startDate, 'dd.MM.yyyy HH:mm:ss') AND TO_CHAR(:endDate, 'dd.MM.yyyy HH:mm:ss')", nativeQuery = true)
    List<UspIncidentData> findIncBySearchFilter(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("searchFilter") String searchFilter);
}
