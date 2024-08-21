package example.com.challengePicPay.services;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import example.com.challengePicPay.controllers.dto.TransferDTO;
import example.com.challengePicPay.entities.ClientEntity;
import example.com.challengePicPay.repositories.ClientRepository;
import example.com.challengePicPay.repositories.ShopkeeperRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BCryptPasswordEncoder bPasswordEncoder;

    @Autowired
    private ShopkeeperRepository shopkeeperRepository;

    public ClientEntity createClient(String name, String cpf, String email, String password) {

        var clientExists = this.clientRepository.findByEmailOrCpf(email, cpf);

        if (clientExists.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email/Cpf already exists.");
        }

        var newClient = new ClientEntity();

        newClient.setName(name);
        newClient.setCpf(cpf);
        newClient.setEmail(email);
        newClient.setPassword(bPasswordEncoder.encode(password));

        return clientRepository.save(newClient);
    }

    public Map<String, String> deposit(String value, JwtAuthenticationToken token) {
        var receiver = this.clientRepository.findByEmail(token.getToken().getSubject())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver Email not found"));

        receiver.setWallet(receiver.getWallet().add(new BigDecimal(value)));

        this.clientRepository.save(receiver);

        return Map.of("message", "Deposit made successfully");
    }

    private ClientEntity findClientByEmail(String email) {
        return this.clientRepository.findByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Sender Email not found"));
    }

    public Map<String, Object> transfer(String email, TransferDTO info) {

        var sender = this.findClientByEmail(email);

        if (sender.getWallet().compareTo(new BigDecimal(info.value())) == -1)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insufficient Balance");

        if (sender.getCpf().equals(info.cpf()))
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Failed ! To deposit value in your account, use the deposit method.");

        if (info.cpf() != null && info.cnpj() == null) {
            var receiver = this.clientRepository.findByCpf(info.cpf())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver CPF not found"));

            receiver.setWallet(receiver.getWallet().add(new BigDecimal(info.value())));
            sender.setWallet(sender.getWallet().subtract(new BigDecimal(info.value())));

            this.clientRepository.save(receiver);
            this.clientRepository.save(sender);

            return Map.of("sender_id", sender.getId(), "sender_email", sender.getEmail(), "sender_value", info.value(),
                    "receiver_id", receiver.getId(), "receiver_email", receiver.getEmail());
        }

        if (info.cnpj() != null && info.cpf() == null) {
            var receiver = this.shopkeeperRepository.findByCnpj(info.cnpj())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Receiver CNPJ not found"));

            receiver.setWallet(receiver.getWallet().add(new BigDecimal(info.value())));
            sender.setWallet(sender.getWallet().subtract(new BigDecimal(info.value())));

            this.shopkeeperRepository.save(receiver);
            this.clientRepository.save(sender);

            return Map.of("sender_id", sender.getId(), "sender_email", sender.getEmail(), "sender_value", info.value(),
                    "receiver_id", receiver.getId(), "receiver_email", receiver.getEmail());
        }

        else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Insert one CNPJ or CPF valid.");
        }

    }
}
