package ru.sberbank.uspincidentreport.repo;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sberbank.uspincidentreport.domain.UspIncidentData;

import java.util.List;


@Repository
@Profile("dev")
public interface UspIncidentRepoDev extends UspIncidentRepo{
    @Query(value = "select * from probsummarym1 p LIMIT 500", nativeQuery = true)
    List<UspIncidentData> findAll();
    @Query(value = "select * from probsummarym1 p where PROBLEM LIKE concat ('%',:triggerDescription, '%') " +
            "AND p.OPEN_TIME BETWEEN TO_CHAR(:startDate, 'dd.MM.yyyy HH:mm:ss') AND TO_CHAR(:endDate, 'dd.MM.yyyy HH:mm:ss')" , nativeQuery = true)
    List<UspIncidentData> findIncByTrigger(@Param("startDate") String startDate, @Param("endDate") String endDate, @Param("triggerDescription") String triggerDescription);
}
