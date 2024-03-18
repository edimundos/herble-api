package eu.herble.herbleapi.plants.repo;

import eu.herble.herbleapi.plants.data.PlantModel;
import eu.herble.herbleapi.plants.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantRepository extends JpaRepository<Plant, Long> {
    List<Plant> findAlByUserId (int userId);
    void deletePlantByPlantId (int plantId);
    @Query("SELECT p.plantId FROM Plant p WHERE p.userId = :userId AND p.plantName = :plantName AND p.plantDescription = :plantDescription AND p.dayCount = :dayCount AND p.waterVolume = :waterVolume")
    Long getPlantIdByDetails(@Param("userId") int userId,
                             @Param("plantName") String plantName,
                             @Param("plantDescription") String plantDescription,
                             @Param("dayCount") int dayCount,
                             @Param("waterVolume") int waterVolume);
}
