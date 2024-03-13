package eu.herble.herbleapi.users.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor


public class EmailDetails {


    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}
