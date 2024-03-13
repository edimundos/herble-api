package eu.herble.herbleapi.users.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

@Data
@Table(name = "tokens")
@Entity
public class Token {
    private static final int TTL_IN_MINUTES = 10;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    private Long id;

    @Enumerated(EnumType.STRING)
    private TokenType type;

    private UUID token;
    private LocalDateTime expiresAt;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(
            name = "user_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "FK_USER_PASSWORD_TOKEN"))
    private AppUser appUser;

    public Token(TokenType type, AppUser appUser, UUID token) {
        super();
        this.type = type;
        this.appUser = appUser;
        this.token = token;
        this.expiresAt = LocalDateTime.now().plusMinutes(TTL_IN_MINUTES);
    }

    public Token() {}
}
