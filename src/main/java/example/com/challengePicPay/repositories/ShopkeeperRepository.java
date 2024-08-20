package example.com.challengePicPay.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import example.com.challengePicPay.entities.ShopkeeperEntity;

@Repository
public interface ShopkeeperRepository extends JpaRepository<ShopkeeperEntity, Long> {
    Optional<ShopkeeperEntity> findByEmail(String email);

    Optional<ShopkeeperEntity> findByEmailOrCnpj(String email, String cnpj);

    Optional<ShopkeeperEntity> findByCnpj(String cnpj);
}
