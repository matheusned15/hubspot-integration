package com.example.hubspot.service;

import com.example.hubspot.model.ContatoDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactService {

    private final OAuthService oauthService;
    private final WebClient webClient = WebClient.create("https://api.hubapi.com");


    public void createContact(ContatoDTO dto) {
        webClient.post()
                .uri("/crm/v3/objects/contacts")
                .headers(headers -> headers.setBearerAuth(oauthService.getValidAccessToken()))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(Map.of("properties", Map.of(
                        "email", dto.getEmail(),
                        "firstname", dto.getFirstname(),
                        "lastname", dto.getLastname()
                )))
                .retrieve()
                .bodyToMono(String.class)
                .block();
        log.info("Contato {} criado no HubSpot", dto.getEmail());
    }

    private void fallbackCreateContact(ContatoDTO dto, Throwable ex) {
        log.error("Falha ao criar contato após tentativas: {}", ex.getMessage());
        throw new RuntimeException("Não foi possível criar contato no momento");
    }
}
