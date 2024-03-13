package eu.herble.herbleapi.instructions.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InstructionsModel {

    private int Id;
    private String question;
    private String answer;

}
