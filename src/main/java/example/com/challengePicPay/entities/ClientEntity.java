package example.com.challengePicPay.entities;

import example.com.challengePicPay.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_clients")
@Getter
@Setter
public class ClientEntity extends UserEntity {

    @Column(unique = true)
    private String cpf;

    public ClientEntity() {
        this.setUserType(UserType.client);
    }

}
