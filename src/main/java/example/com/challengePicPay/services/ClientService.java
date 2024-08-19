package example.com.challengePicPay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import example.com.challengePicPay.entities.ClientEntity;
import example.com.challengePicPay.repositories.ClientRepository;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private BCryptPasswordEncoder bPasswordEncoder;

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
}
