package example.com.challengePicPay.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private static final String CLIENT_SIGNIN_URL = "/client/signin";
    private static final String SHOPKEEPER_SIGNIN_URL = "/shopkeeper/signin";
    private static final String LOGIN_URL = "/login";

    @Autowired
    private JwtConfig jwtConfig;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.requestMatchers(HttpMethod.POST, CLIENT_SIGNIN_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, SHOPKEEPER_SIGNIN_URL).permitAll()
                        .requestMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                        .anyRequest().authenticated())
                .oauth2ResourceServer(config -> config.jwt(jwt -> jwt.decoder(this.jwtConfig.jwtDecoder())));

        return http.build();
    }

}
