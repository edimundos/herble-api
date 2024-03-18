package eu.herble.herbleapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class WebSecurityConfig {

    private static final String[] WHITELIST_URLS = new String[]{
            "/actuator/**",
            "/hello",
            "/api/instructions/create",
            "/api/instructions/getAll",
            "/api/notifications",
            "/api/notifications/cancel",
            "/api/plants/create",
            "/api/plants/getAllByUserId",
            "/api/plants/deleteByPlantId",
            "/api/plants/getPlantId",
            "/api/plants/update",
            "/api/plants/getPlant",
            "/api/tips/create",
            "/api/tips/getAll",
            "/register",
            "/resendVerifyToken*",
            "/verifyRegistration*",
            "/login",
            "/sendMail",
            "/api/v1/tokens/**",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        (authorize) ->
                                authorize
                                        .requestMatchers(WHITELIST_URLS)
                                        .permitAll()
                                        .anyRequest()
                                        .authenticated());
        return http.build();
    }
}
