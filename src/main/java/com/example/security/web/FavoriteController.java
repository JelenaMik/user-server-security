package com.example.security.web;

import com.example.security.auth.AuthenticationService;
import com.example.security.service.UserDataService;
import com.example.security.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class FavoriteController {
    private final UserService userService;
    private final AuthenticationService service;
    private final UserDataService userDataService;

    @PostMapping("/add-favorite")
    public ResponseEntity<HttpStatus> addFavoriteProvider(Long clientId, Long providerId){
        log.info("Client id: {}", clientId);
        userDataService.addFavoriteProvider(clientId, providerId);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/favorite-providers/{clientId}")
    public ResponseEntity<List> getFavoriteProvidersIds(@PathVariable Long clientId){
        return new ResponseEntity<>(userDataService.getFavoriteProviders(clientId), HttpStatus.OK);
    }

    @DeleteMapping("/delete-favorite")
    public ResponseEntity<HttpStatus> removeFavoriteProvider(Long clientId, Long providerId){
        userDataService.deleteFavoriteProvider(clientId, providerId);
        return ResponseEntity.noContent().build();
    }
}
