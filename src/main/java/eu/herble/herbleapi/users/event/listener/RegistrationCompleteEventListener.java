package eu.herble.herbleapi.users.event.listener;

import eu.herble.herbleapi.users.event.RegistrationComplete;
import eu.herble.herbleapi.users.model.AppUser;
import eu.herble.herbleapi.users.service.UserService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RegistrationCompleteEventListener
        implements ApplicationListener<RegistrationComplete> {

    @Autowired private UserService userService;

    @Override
    public void onApplicationEvent(RegistrationComplete event) {
        // verif token for user with link
        AppUser appUser = event.getAppUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(token, appUser);
        // send mail to user

        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;

        // sendVerificationEmail()
        log.info("Click the link to verify your account " + url);
    }
}
