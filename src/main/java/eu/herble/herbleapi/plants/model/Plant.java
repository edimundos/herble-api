package eu.herble.herbleapi.plants.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Data;

@Entity
@Data
public class Plant {

    @jakarta.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int plantId;
    private int userId;
    private String plantName;
    private String plantDescription;
    private int dayCount;
    private int waterVolume;
    private byte[] picture;

}
