package eu.herble.herbleapi.users.event;

import eu.herble.herbleapi.users.model.AppUser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.ApplicationEvent;

@Getter
@Setter
public class RegistrationComplete extends ApplicationEvent {

    private AppUser appUser;
    private String applicationUrl;

    public RegistrationComplete(AppUser appUser, String applicationUrl) {
        super(appUser);
        this.appUser = appUser;
        this.applicationUrl = applicationUrl;
    }
}
