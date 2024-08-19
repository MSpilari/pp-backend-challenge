package example.com.challengePicPay.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import example.com.challengePicPay.entities.ClientEntity;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByEmail(String email);

    Optional<ClientEntity> findByCpf(String cpf);

    Optional<ClientEntity> findByEmailOrCpf(String email, String cpf);
}
