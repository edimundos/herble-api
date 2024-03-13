package eu.herble.herbleapi.users;

import eu.herble.herbleapi.users.data.EmailDetails;
import eu.herble.herbleapi.users.data.LoginModel;
import eu.herble.herbleapi.users.data.PasswordModel;
import eu.herble.herbleapi.users.data.UserModel;
import eu.herble.herbleapi.users.event.RegistrationComplete;
import eu.herble.herbleapi.users.model.AppUser;
import eu.herble.herbleapi.users.model.Token;
import eu.herble.herbleapi.users.service.EmailService;
import eu.herble.herbleapi.users.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private EmailService emailService;

    @PostMapping("/register")
    public String registerUser(@RequestBody UserModel userModel, final HttpServletRequest request) {
        AppUser appUser = userService.registerUser(userModel);
        String url=applicationUrl(request);
        publisher.publishEvent(new RegistrationComplete(appUser, url));
//        verifyTokenMail(appUser,url,appUser.);
        return "Success";
    }

    @PostMapping("/login")
    public String registerUser(@RequestBody LoginModel loginModel, HttpServletRequest request) {
        String account = userService.loginUser(loginModel);
        if (account.equalsIgnoreCase("valid")) {
            return "Success";
        }
        return account;
    }

    @GetMapping("/verifyRegistration")
    public String verifyRegistration(@RequestParam("token") String token) {
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
    public String resendVerificationToken(@RequestParam("token") String oldToken, HttpServletRequest request) {
        Token verificationToken = userService.generateNewVerificationToken(oldToken);
        AppUser appUser = verificationToken.getAppUser();
        verifyTokenMail(appUser, applicationUrl((request)), verificationToken);
        return "Verification Link Sent";
    }

    private void verifyTokenMail(AppUser appUser, String applicationUrl, Token verificationToken) {
        String url = applicationUrl + "/verifyRegistration?token=" + verificationToken.getToken();

        String body="Click the link to verify your account " + url+"\n"+
                "If You received this email without previous notice don't click the link reach out our customer support on www.herble.eu";

        EmailDetails email= new EmailDetails(appUser.getEmail(),body, "Verification link for Herble app", "");
        String status = emailService.sendSimpleMail(email);

    }

    @PostMapping("/resetPassword")
    public String resetPassword(@RequestBody PasswordModel passwordModel, HttpServletRequest request) {

        AppUser appUser = userService.findUserByEmail(passwordModel.getEmail());

        String url = "";

        if (appUser != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(appUser, token);
            url = passwordResetTokenMail(appUser, applicationUrl(request), token);
        }

        return url;
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("token") String token, @RequestBody PasswordModel passwordModel) {

        String result = userService.validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            return "Invalid Token";
        }

        Optional<AppUser> user = userService.getUserByPasswordResetToken(token);

        if (user.isPresent()) {
            userService.changePassword(user.get(), passwordModel.getNewPassword());
            return "Password reset successful";
        } else {
            return "invalid Token";
        }
    }

    @PostMapping("/changePassword")
    public String changePassword(@RequestBody PasswordModel passwordModel) {
        AppUser appUser = userService.findUserByEmail(passwordModel.getEmail());
        if (!userService.checkIfValidOldPassword(appUser, passwordModel.getOldPassword())) {
            return "Invalid Old Password";
        }

        userService.changePassword(appUser, passwordModel.getNewPassword());
        return "Password Changed Successfully";
    }

    private String passwordResetTokenMail(AppUser appUser, String applicationUrl, String token) {
        String url = applicationUrl + "/savePassword?token=" + token;

        // sendVerificationEmail()
        log.info("Click the link to reset your password " + url);
        return url;
    }

    private String applicationUrl(HttpServletRequest request) {
        String url = "http://" + request.getServerName() + ":" + request.getServerPort();
        log.info("link should be called --- {}", url); // this is for localhost, must change later
        return url;
    }



    // Sending a simple Email
    @PostMapping("/sendMail")
    public String
    sendMail(@RequestBody EmailDetails details)
    {

        String status = emailService.sendSimpleMail(details);

        return status;
    }

    // Sending email with attachment
    @PostMapping("/sendMailWithAttachment")
    public String sendMailWithAttachment(
            @RequestBody EmailDetails details)
    {
        String status
                = emailService.sendMailWithAttachment(details);
        return status;
    }
}
