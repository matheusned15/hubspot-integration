package com.example.hubspot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/hubspot/webhooks")
@Tag(name = "HubSpot - Webhooks", description = "Recebimento de eventos do HubSpot")
@Slf4j
public class WebhookController {

    @PostMapping("/contact-creation")
    @Operation(summary = "Recebe evento de criação de contato")
    public ResponseEntity<Void> handleContactCreation(@RequestBody Map<String, Object> payload) {
        log.info("Webhook recebido: {}", payload);
        return ResponseEntity.ok().build();
    }
}
