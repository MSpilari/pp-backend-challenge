package example.com.challengePicPay.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import example.com.challengePicPay.controllers.dto.NewShopkeeperDTO;
import example.com.challengePicPay.entities.ShopkeeperEntity;
import example.com.challengePicPay.repositories.ShopkeeperRepository;

@Service
public class ShopkeeperService {

    @Autowired
    private ShopkeeperRepository shopkeeperRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public ShopkeeperEntity createShopkeeper(NewShopkeeperDTO shopkeeperDTO) {
        var shopkeeperExists = this.shopkeeperRepository.findByEmailOrCnpj(shopkeeperDTO.email(), shopkeeperDTO.cnpj());

        if (shopkeeperExists.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email/Cnpj already exists.");
        }

        var newShopkeeper = new ShopkeeperEntity();

        newShopkeeper.setName(shopkeeperDTO.name());
        newShopkeeper.setEmail(shopkeeperDTO.email());
        newShopkeeper.setCnpj(shopkeeperDTO.cnpj());
        newShopkeeper.setPassword(bCryptPasswordEncoder.encode(shopkeeperDTO.password()));

        return this.shopkeeperRepository.save(newShopkeeper);
    }
}
