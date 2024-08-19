package example.com.challengePicPay.services;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import example.com.challengePicPay.controllers.dto.LoginDTO;
import example.com.challengePicPay.entities.UserEntity;
import example.com.challengePicPay.enums.UserType;
import example.com.challengePicPay.repositories.ClientRepository;
import example.com.challengePicPay.repositories.ShopkeeperRepository;

@Service
public class TokenService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ShopkeeperRepository shopkeeperRepository;

    @Autowired
    private JwtEncoder jwtEncoder;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserEntity findUserByEmailAndType(String email, String userType) {
        if (userType.equals("client")) {
            return this.clientRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found"));
        } else if (userType.equals("shopkeeper")) {
            return this.shopkeeperRepository.findByEmail(email)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email not found"));
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Type not found");
        }
    }

    private JwtClaimsSet claimsSet(String issuer, String subject, Long expTime, UserType userType) {

        var now = Instant.now();

        var claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .subject(subject)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expTime))
                .claim("scope", userType)
                .build();

        return claims;
    }

    public String login(LoginDTO login) {
        var user = this.findUserByEmailAndType(login.email(), login.type());

        var isPasswordValid = user.passwordVerifier(login.password(), bCryptPasswordEncoder);

        if (!isPasswordValid || !user.getUserType().name().equals(login.type())) {
            throw new BadCredentialsException("Email/password/type invalid");
        }

        var claims = claimsSet("pic_pay_backend", user.getEmail(), 300L, user.getUserType());

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();

    }
}
