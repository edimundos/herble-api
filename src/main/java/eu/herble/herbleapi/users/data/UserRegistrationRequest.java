package eu.herble.herbleapi.users.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationRequest {

    private String firstname;
    private String lastName;
    private String email;
    private String password;
    private String matchingPassword;
}
