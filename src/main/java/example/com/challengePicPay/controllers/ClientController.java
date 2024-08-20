package example.com.challengePicPay.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.com.challengePicPay.controllers.dto.DepositDTO;
import example.com.challengePicPay.controllers.dto.NewClientDTO;
import example.com.challengePicPay.controllers.dto.TransferDTO;
import example.com.challengePicPay.entities.ClientEntity;
import example.com.challengePicPay.services.ClientService;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/signin")
    public ResponseEntity<ClientEntity> createNewClient(@RequestBody NewClientDTO clientDTO) {
        var newClient = this.clientService.createClient(clientDTO.name(), clientDTO.cpf(), clientDTO.email(),
                clientDTO.password());

        return ResponseEntity.status(HttpStatus.CREATED).body(newClient);
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasAuthority('SCOPE_client')")
    public ResponseEntity<Map<String, String>> deposit(@RequestBody DepositDTO depositInfo,
            JwtAuthenticationToken token) {

        var message = this.clientService.deposit(depositInfo.value(), token);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(message);
    }

}
