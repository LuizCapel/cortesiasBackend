package com.projetoCortesias.cortesias.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class BitlyService {

    @Value("${bitly.token}")
    private String bitlyToken;

    private final RestTemplate restTemplate = new RestTemplate();

    public Map<String, String> encurtar(String longUrl) {
        String apiUrl = "https://api-ssl.bitly.com/v4/shorten";

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(bitlyToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"long_url\": \"%s\"}", longUrl);

        HttpEntity<String> request = new HttpEntity<>(body, headers);
        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, request, Map.class);

        String link = response.getBody().get("link").toString();
        String qrCodeUrl = link + ".qr"; // padrão Bitly para QR Code

        return Map.of(
                "shortUrl", link,
                "qrCodeUrl", qrCodeUrl
        );
    }
}
