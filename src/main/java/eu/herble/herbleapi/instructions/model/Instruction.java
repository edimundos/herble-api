package eu.herble.herbleapi.instructions.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int Id;
    private String question;
    private String answer;

}
