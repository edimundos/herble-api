package eu.herble.herbleapi.users.service;

import static eu.herble.herbleapi.users.model.TokenType.PASSWORD_RESET;
import static eu.herble.herbleapi.users.model.TokenType.VERIFICATION;

import eu.herble.herbleapi.users.data.LoginRequest;
import eu.herble.herbleapi.users.data.UserRegistrationRequest;
import eu.herble.herbleapi.users.model.AppUser;
import eu.herble.herbleapi.users.model.Token;
import eu.herble.herbleapi.users.repo.TokenRepository;
import eu.herble.herbleapi.users.repo.UserRepository;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final TokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, TokenRepository tokenRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AppUser registerUser(UserRegistrationRequest userRegistrationRequest) {
        AppUser appUser = new AppUser();
        appUser.setEmail(userRegistrationRequest.getEmail());
        appUser.setFirstname(userRegistrationRequest.getFirstname());
        appUser.setLastName(userRegistrationRequest.getLastName());
        appUser.setRole("USER");
        appUser.setPassword(passwordEncoder.encode(userRegistrationRequest.getPassword()));
        userRepository.save(appUser);
        return appUser;
    }

    public void saveVerificationTokenForUser(UUID token, AppUser appUser) {
        Token verificationToken = new Token(VERIFICATION, appUser, token);

        tokenRepository.save(verificationToken);
        log.info(verificationToken.toString());
    }

    public String validateVerificationToken(UUID token) {
        Token verificationToken = tokenRepository.findByTypeAndToken(VERIFICATION, token);

        if (verificationToken == null) {
            return "invalid";
        }

        AppUser appUser = verificationToken.getAppUser();

        if (verificationToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(verificationToken);
            return "expired";
        }

        appUser.setEnabled(true);
        userRepository.save(appUser);
        return "valid";
    }

    public Token generateNewVerificationToken(UUID oldToken) {
        Token verificationToken = tokenRepository.findByTypeAndToken(VERIFICATION, oldToken);
        verificationToken.setToken(UUID.randomUUID());
        tokenRepository.save(verificationToken);
        return verificationToken;
    }

    public Optional<AppUser> findById(Long id) {
        return userRepository.findById(id);
    }

    public AppUser findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void createPasswordResetTokenForUser(AppUser appUser, UUID token) {
        Token passwordResetToken = new Token(PASSWORD_RESET, appUser, token);

        tokenRepository.save(passwordResetToken);
    }

    public String validatePasswordResetToken(UUID token) {
        Token passwordResetToken = tokenRepository.findByTypeAndToken(PASSWORD_RESET, token);

        if (passwordResetToken == null) {
            return "invalid";
        }

        if (passwordResetToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            tokenRepository.delete(passwordResetToken);
            return "expired";
        }

        return "valid";
    }

    public void changePassword(AppUser appUser, String newPassword) {
        appUser.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(appUser);
    }

    public Optional<AppUser> getUserByPasswordResetToken(UUID token) {
        return Optional.ofNullable(tokenRepository.findByTypeAndToken(PASSWORD_RESET, token).getAppUser());
    }

    public boolean checkIfValidOldPassword(AppUser appUser, String oldPassword) {
        return passwordEncoder.matches(oldPassword, appUser.getPassword());
    }

    public String loginUser(LoginRequest loginRequest) {
        AppUser appUser = findUserByEmail(loginRequest.getEmail());
        String pwd = loginRequest.getPassword();
        if (appUser == null) {
            return "Invalid email";
        }
        if (!appUser.isEnabled()) {
            return "Invalid email";
        }

        if (!passwordEncoder.matches(pwd, appUser.getPassword())) {
            return "Invalid password";
        }

        return appUser.getId().toString();
    }
}
