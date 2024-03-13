package eu.herble.herbleapi.users;

import eu.herble.herbleapi.users.data.ChangePasswordRequest;
import eu.herble.herbleapi.users.data.EmailDetails;
import eu.herble.herbleapi.users.data.LoginRequest;
import eu.herble.herbleapi.users.data.UserRegistrationRequest;
import eu.herble.herbleapi.users.event.RegistrationComplete;
import eu.herble.herbleapi.users.model.AppUser;
import eu.herble.herbleapi.users.model.Token;
import eu.herble.herbleapi.users.service.EmailService;
import eu.herble.herbleapi.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class RegistrationController {

    private final UserService userService;

    private final ApplicationEventPublisher publisher;

    private final EmailService emailService;

    public RegistrationController(UserService userService, ApplicationEventPublisher publisher, EmailService emailService) {
        this.userService = userService;
        this.publisher = publisher;
        this.emailService = emailService;
    }


    @PostMapping("/register")
    public String registerUser(@RequestBody UserRegistrationRequest userRegistrationRequest, final HttpServletRequest request) {
        AppUser appUser = userService.registerUser(userRegistrationRequest);
        String url = applicationUrl(request);
        publisher.publishEvent(new RegistrationComplete(appUser, url));

        return "Success";
    }

    @PostMapping("/login")
    public String registerUser(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
        String account = userService.loginUser(loginRequest);
        if (account.equalsIgnoreCase("valid")) {
            return "Success";
        }
        return account;
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") UUID token) {
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")) {
            return "User verified successfully";
        }
        return "Bad user";
    }

    @GetMapping("/hello")
    public String halo() {
        return "hello";
    }

    @GetMapping("/resendVerifyToken")
    public String resendVerificationToken(@RequestParam("token") UUID oldToken, HttpServletRequest request) {
        Token verificationToken = userService.generateNewVerificationToken(oldToken);
        AppUser appUser = verificationToken.getAppUser();
        verifyTokenMail(appUser, applicationUrl((request)), verificationToken);
        return "Verification Link Sent";
    }

    private void verifyTokenMail(AppUser appUser, String applicationUrl, Token verificationToken) {
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();

        String body = "Click the link to verify your account " + url + "\n" +
                "If You received this email without previous notice don't click the link reach out our customer support on www.herble.eu";

        EmailDetails email = new EmailDetails(appUser.getEmail(), body, "Verification link for Herble app", "");
        String status = emailService.sendSimpleMail(email);

    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody ChangePasswordRequest changePasswordRequest, HttpServletRequest request) {

        AppUser appUser = userService.findUserByEmail(changePasswordRequest.getEmail());

        String url = "";

        if (appUser != null) {
            UUID token = UUID.randomUUID();
            userService.createPasswordResetTokenForUser(appUser, token);
            url = passwordResetTokenMail(appUser, applicationUrl(request), token);
        }

        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") UUID token, @RequestBody ChangePasswordRequest
            changePasswordRequest) {

        String result = userService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "Invalid Token";
        }

        Optional<AppUser> user = userService.getUserByPasswordResetToken(token);

        if (user.isPresent()) {
            userService.changePassword(user.get(), changePasswordRequest.getNewPassword());
            return "Password reset successful";
        } else {
            return "invalid Token";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody ChangePasswordRequest changePasswordRequest) {
        AppUser appUser = userService.findUserByEmail(changePasswordRequest.getEmail());
        if (!userService.checkIfValidOldPassword(appUser, changePasswordRequest.getOldPassword())) {
            return "Invalid Old Password";
        }

        userService.changePassword(appUser, changePasswordRequest.getNewPassword());
        return "Password Changed Successfully";
    }

    private String passwordResetTokenMail(AppUser appUser, String applicationUrl, UUID token) {
        String url = applicationUrl + "/savePassword?token=" + token;

        // sendVerificationEmail()
        log.info("Click the link to reset your password " + url);
        return url;
    }

    private String applicationUrl(HttpServletRequest request) {
        String url = "http://" + request.getRequestURI();
        log.info("link should be called --- {}", url); // this is for localhost, must change later
        return url;
    }

    @PostMapping("/sendMail")
    public String sendMail(@RequestBody EmailDetails details) {

        String status = emailService.sendSimpleMail(details);

        return status;
    }

    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(@RequestBody EmailDetails details) {
        String status = emailService.sendMailWithAttachment(details);
        return status;
    }
}
