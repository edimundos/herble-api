package eu.herble.herbleapi.users.service;

import eu.herble.herbleapi.users.repo.TokenRepository;
import org.springframework.stereotype.Service;

@Service
public class TokenService {


    private final TokenRepository tokenRepository;

    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }
}
