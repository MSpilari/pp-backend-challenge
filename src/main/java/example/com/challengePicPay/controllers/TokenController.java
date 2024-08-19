package example.com.challengePicPay.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import example.com.challengePicPay.controllers.dto.LoginDTO;
import example.com.challengePicPay.services.TokenService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginDTO loginDTO) {
        var token = this.tokenService.login(loginDTO);

        return ResponseEntity.ok(Map.of("token", token));
    }

}
