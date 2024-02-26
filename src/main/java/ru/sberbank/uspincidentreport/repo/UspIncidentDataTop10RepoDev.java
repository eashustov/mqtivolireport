package ru.sberbank.uspincidentreport.repo;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.sberbank.uspincidentreport.domain.IUspIncidentDataTop10;

import java.util.List;

@Repository
@Profile("dev")
public interface UspIncidentDataTop10RepoDev extends UspIncidentDataTop10Repo{
    @Query(value = "select p.AFFECTED_ITEM as AFFECTED_ITEM, p.HOST as HOST, COUNT (p.NUMBER) AS COUNT_INC from probsummarym1 p GROUP BY AFFECTED_ITEM, HOST ORDER BY AFFECTED_ITEM, COUNT_INC DESC", nativeQuery = true)
    List<IUspIncidentDataTop10> findTop10IncCount(@Param("startDate") String startDate, @Param("endDate") String endDate);
}
