package eu.herble.herbleapi.users.repo;

import eu.herble.herbleapi.users.model.Token;
import eu.herble.herbleapi.users.model.TokenType;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    Token findByTypeAndToken(TokenType type, UUID token);
}
