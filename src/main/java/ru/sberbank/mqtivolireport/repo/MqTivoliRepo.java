package ru.sberbank.mqtivolireport.repo;

import com.vaadin.flow.function.SerializableTriConsumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.sberbank.mqtivolireport.domain.MqTivoliData;

import java.util.List;

public interface MqTivoliRepo extends JpaRepository<MqTivoliData, Long> {
    List<MqTivoliData> findAll();
    @Query("from MqTivoliData m where  concat (m.mqName, ' ', m.serverName, ' ', m.tivoliInstall) like concat ('%', :filter, '%')")
    List<MqTivoliData> findByServerName(@Param("filter") String filter);

}
