package com.example.hubspot.controller;

import com.example.hubspot.service.OAuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hubspot/oauth")
@Tag(name = "HubSpot - OAuth", description = "Endpoints de autorização OAuth")
@RequiredArgsConstructor
public class OAuthController {

    private final OAuthService oauthService;

    @GetMapping("/authorize-url")
    @Operation(summary = "Gera URL para autorização OAuth")
    public ResponseEntity<String> getAuthorizationUrl() {
        return ResponseEntity.ok(oauthService.buildAuthorizationUrl());
    }

    @GetMapping("/callback")
    @Operation(summary = "Processa callback OAuth e armazena token")
    public ResponseEntity<String> handleCallback(@RequestParam("code") String code) {
        oauthService.exchangeCodeForToken(code);
        return ResponseEntity.ok("Token salvo com sucesso");
    }
}
