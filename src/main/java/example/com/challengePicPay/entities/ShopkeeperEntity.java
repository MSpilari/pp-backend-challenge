package example.com.challengePicPay.entities;

import example.com.challengePicPay.enums.UserType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_shopkeeper")
@Getter
@Setter
public class ShopkeeperEntity extends UserEntity {

    @Column(unique = true)
    private String cnpj;

    public ShopkeeperEntity() {
        this.setUserType(UserType.shopkeeper);
    }
}
