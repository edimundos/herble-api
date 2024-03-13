package eu.herble.herbleapi.tips.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Tip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String title;
    private String description;
    private byte[] picture;

}