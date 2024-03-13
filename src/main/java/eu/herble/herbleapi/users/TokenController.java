package eu.herble.herbleapi.users;

import static eu.herble.herbleapi.errors.ApiErrorType.USER_NOT_FOUND;

import eu.herble.herbleapi.errors.ApiException;
import eu.herble.herbleapi.users.data.TokenResponse;
import eu.herble.herbleapi.users.service.TokenService;
import eu.herble.herbleapi.users.service.UserService;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/tokens")
public class TokenController {
    private final UserService userService;

    private final TokenService tokenService;

    public TokenController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    @GetMapping("/{userId}/{token}")
    public ResponseEntity<TokenResponse> getToken(@PathVariable Long userId, @PathVariable UUID token) {
        userService.findById(userId).orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        // find token
        return ResponseEntity.ok(new TokenResponse(UUID.randomUUID()));
    }
}
