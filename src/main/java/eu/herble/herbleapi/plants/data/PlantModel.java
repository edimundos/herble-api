package eu.herble.herbleapi.plants.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlantModel {

    private int plantId;
    private int userId;
    private String plantName;
    private String plantDescription;
    private int dayCount;
    private int waterVolume;
    private byte[] picture;

}
