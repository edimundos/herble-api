package eu.herble.herbleapi.users.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordRequest {

    private String email;

    private String oldPassword;
    private String newPassword;
}
