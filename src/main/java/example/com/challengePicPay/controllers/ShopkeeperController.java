package example.com.challengePicPay.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import example.com.challengePicPay.controllers.dto.NewShopkeeperDTO;
import example.com.challengePicPay.entities.ShopkeeperEntity;
import example.com.challengePicPay.services.ShopkeeperService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping("/shopkeeper")
public class ShopkeeperController {

    @Autowired
    private ShopkeeperService shopkeeperService;

    @PostMapping("/signin")
    public ResponseEntity<ShopkeeperEntity> signin(@RequestBody NewShopkeeperDTO shopkeeperDTO) {
        var newShopkeeper = this.shopkeeperService.createShopkeeper(shopkeeperDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(newShopkeeper);
    }

}
