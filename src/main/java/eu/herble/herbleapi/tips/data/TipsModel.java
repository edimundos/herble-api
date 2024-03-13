package eu.herble.herbleapi.tips.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipsModel {

    private int Id;
    private String title;
    private String description;
    private byte[] picture;

}
