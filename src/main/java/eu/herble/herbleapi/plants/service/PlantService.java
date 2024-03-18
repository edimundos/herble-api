package eu.herble.herbleapi.plants.service;

import eu.herble.herbleapi.plants.data.PlantModel;
import eu.herble.herbleapi.plants.model.Plant;
import eu.herble.herbleapi.plants.repo.PlantRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PlantService {

    @Autowired
    private PlantRepository plantRepository;

    public Plant createPlant (PlantModel plantModel) {
        Plant plant = new Plant();
        plant.setPlantName(plantModel.getPlantName());
        plant.setPlantDescription(plantModel.getPlantDescription());
        plant.setDayCount(plantModel.getDayCount());
        plant.setWaterVolume(plantModel.getWaterVolume());
        plant.setPicture(plantModel.getPicture());
        plant.setUserId(plantModel.getUserId());
        plantRepository.save(plant);
        return plant;
    }

    public List<Plant> getAllPlantsByUserId(int userId) {
        return plantRepository.findAlByUserId(userId);
    }

    @Transactional
    public String deletePlantById(int plantId) {
        log.info("Attempting to delete plant with ID: {}", plantId);
        try {
            plantRepository.deletePlantByPlantId(plantId);
            return "Success";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public Optional<Plant> getPlantById(Long plantId) {
        return plantRepository.findById(plantId);
    }

    public Optional<Long> getPlantId(PlantModel plantModel) {
        Long plantId = plantRepository.getPlantIdByDetails(plantModel.getUserId(), plantModel.getPlantName(), plantModel.getPlantDescription(), plantModel.getDayCount(), plantModel.getWaterVolume());
        return Optional.ofNullable(plantId);
    }

    public Plant updatePlant(PlantModel plantModel) {
        Optional<Plant> existingPlantOptional = plantRepository.findById((long) plantModel.getPlantId());
        if (existingPlantOptional.isPresent()) {
            Plant existingPlant = existingPlantOptional.get();
            existingPlant.setPlantName(plantModel.getPlantName());
            existingPlant.setPlantDescription(plantModel.getPlantDescription());
            existingPlant.setDayCount(plantModel.getDayCount());
            existingPlant.setWaterVolume(plantModel.getWaterVolume());
            existingPlant.setPicture(plantModel.getPicture());
            return plantRepository.save(existingPlant);
        } else {
            throw new RuntimeException("Plant not found with id " + plantModel.getPlantId());
        }
    }

}
