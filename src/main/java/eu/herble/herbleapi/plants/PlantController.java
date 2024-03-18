package eu.herble.herbleapi.plants;

import eu.herble.herbleapi.plants.data.PlantModel;
import eu.herble.herbleapi.plants.model.Plant;
import eu.herble.herbleapi.plants.service.PlantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/api/plants")
public class PlantController {

    @Autowired
    private PlantService plantService;

    @PostMapping("/create")
    public ResponseEntity<Plant> createPlant (@RequestBody PlantModel plantsModel) {
        Plant savedPlant = plantService.createPlant(plantsModel);
        return new ResponseEntity<>(savedPlant, HttpStatus.CREATED);
    }

    @PostMapping("/getAllByUserId")
    public ResponseEntity<List<Plant>> getAllPlantsByUserId(@RequestBody PlantModel plantsModel) {
        List<Plant> plants = plantService.getAllPlantsByUserId(plantsModel.getUserId());
        return ResponseEntity.ok().body(plants);
    }

    @PostMapping("/deleteByPlantId")
    public ResponseEntity<String> deleteByPlantId(@RequestBody PlantModel plantModel) {
        String responseMessage = plantService.deletePlantById(plantModel.getPlantId());
        return ResponseEntity.ok().body(responseMessage);
    }

    @PutMapping("/update")
    public ResponseEntity<Plant> updatePlant(@RequestBody PlantModel plantModel) {
        try {
            Plant updatedPlant = plantService.updatePlant(plantModel);
            return new ResponseEntity<>(updatedPlant, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/getPlant")
    public ResponseEntity<Plant> getPlantById(@RequestBody PlantModel plantModel) {
        Optional<Plant> plant = plantService.getPlantById((long) plantModel.getPlantId());
        return plant.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/getPlantId")
    public ResponseEntity<?> getPlantId(@RequestBody PlantModel plantModel) {
        Optional<Long> plantId = plantService.getPlantId(plantModel);
        return ResponseEntity.ok().body(plantId);
//        return plantId.map(ResponseEntity::ok)
//                .orElseGet(() -> ResponseEntity.notFound().build());

    }
}
