package example.com.challengePicPay.entities;

import java.math.BigDecimal;

import org.springframework.security.crypto.password.PasswordEncoder;

import example.com.challengePicPay.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_type")
    private UserType userType;

    // configures the field to store up to 19 digits, with 2 decimal places, which
    // is typical for monetary values.
    @Column(precision = 19, scale = 2)
    private BigDecimal wallet = BigDecimal.ZERO;

    public Boolean passwordVerifier(String password, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(password, this.password);
    }
}
