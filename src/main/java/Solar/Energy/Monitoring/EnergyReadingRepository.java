
package Solar.Energy.Monitoring;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnergyReadingRepository
        extends JpaRepository<EnergyReading, Long> {

    List<EnergyReading> findAllByOrderByIdAsc();
}
