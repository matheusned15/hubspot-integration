package com.example.hubspot.service;

import com.example.hubspot.config.HubSpotConfig;
import com.example.hubspot.entity.OAuthToken;
import com.example.hubspot.repository.OAuthTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuthService {

    private final HubSpotConfig config;
    private final OAuthTokenRepository tokenRepository;
    private final WebClient webClient = WebClient.create("https://api.hubapi.com");

    public String buildAuthorizationUrl() {
        return "https://app.hubspot.com/oauth/authorize"
                + "?client_id=" + config.getClientId()
                + "&redirect_uri=" + config.getRedirectUri()
                + "&scope=contacts"
                + "&response_type=code";
    }

    public void exchangeCodeForToken(String code) {
        Mono<Map> response = webClient.post()
                .uri("/oauth/v1/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue("grant_type=authorization_code"
                        + "&client_id=" + config.getClientId()
                        + "&client_secret=" + config.getClientSecret()
                        + "&redirect_uri=" + config.getRedirectUri()
                        + "&code=" + code)
                .retrieve()
                .bodyToMono(Map.class);

        Map<String, Object> resp = response.block();
        String accessToken = (String) resp.get("access_token");
        String refreshToken = (String) resp.get("refresh_token");
        Integer expiresIn = (Integer) resp.get("expires_in");

        OAuthToken token = new OAuthToken();
        token.setAccessToken(accessToken);
        token.setRefreshToken(refreshToken);
        token.setExpiresAt(Instant.now().plusSeconds(expiresIn));
        tokenRepository.save(token);
        log.info("Novo token salvo, expira em: {}", token.getExpiresAt());
    }

    public String getValidAccessToken() {
        return tokenRepository.findAll().stream()
                .max((a, b) -> a.getExpiresAt().compareTo(b.getExpiresAt()))
                .map(OAuthToken::getAccessToken)
                .orElseThrow(() -> new RuntimeException("Token não encontrado"));
    }
}
