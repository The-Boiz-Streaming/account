package com.boiz.streaming.account.controller;

import com.boiz.streaming.account.dto.*;
import com.boiz.streaming.account.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Login body,
                                   HttpServletResponse response) {
        TokenPair tokenPair = authService.login(body, response);
        return ResponseEntity.status(HttpStatus.OK)
                .body(tokenPair);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Signup body) {
        AccountInfo accountInfo = authService.signup(body);
        return ResponseEntity.status(HttpStatus.OK)
                .body(accountInfo);
    }

    @PostMapping("/token")
    public ResponseEntity<?> token(@RequestBody RefreshToken body) {
        AccessToken accessToken = authService.getAccessToken(body);
        return ResponseEntity.status(HttpStatus.OK)
                .body(accessToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        authService.logout(Logout.of(response));
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

}
