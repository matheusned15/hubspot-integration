package com.example.hubspot.controller;

import com.example.hubspot.model.ContatoDTO;
import com.example.hubspot.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hubspot/contacts")
@Tag(name = "HubSpot - Contatos", description = "Endpoints para criar e gerenciar contatos no HubSpot")
@RequiredArgsConstructor
@Slf4j
public class HubSpotController {

    private final ContactService contactService;

    @Operation(summary = "Criar contato no HubSpot")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Contato criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    @PostMapping
    public ResponseEntity<String> criarContato(@Valid @RequestBody ContatoDTO dto) {
        log.info("Criando contato: {}", dto.getEmail());
        contactService.createContact(dto);
        return ResponseEntity.ok("Contato criado com sucesso");
    }
}
