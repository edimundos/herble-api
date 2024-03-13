package eu.herble.herbleapi.users.event.listener;

import eu.herble.herbleapi.users.data.EmailDetails;
import eu.herble.herbleapi.users.event.RegistrationComplete;
import eu.herble.herbleapi.users.model.AppUser;
import eu.herble.herbleapi.users.service.EmailService;
import eu.herble.herbleapi.users.service.UserService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationComplete> {
    private final UserService userService;

    private final EmailService emailService;

    public RegistrationCompleteEventListener(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @Override
    public void onApplicationEvent(RegistrationComplete event) {
        // verify token for user with link
        AppUser appUser = event.getAppUser();
        UUID token = UUID.randomUUID();
        userService.saveVerificationTokenForUser(token, appUser);
        // send mail to user

        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;

        // sendVerificationEmail()

        String body = "Click the link to verify your account " + url + "\n" +
                "If You received this email without previous notice don't click the link reach out our customer support on www.herble.eu";

        EmailDetails email = new EmailDetails(appUser.getEmail(), body, "Verification link for Herble app", "");
        String status = emailService.sendSimpleMail(email);

        log.info(status);
    }
}
