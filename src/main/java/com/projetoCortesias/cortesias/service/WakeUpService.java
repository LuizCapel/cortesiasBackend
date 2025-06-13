package com.projetoCortesias.cortesias.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@EnableScheduling
public class WakeUpService {

    private static final Logger LOG = LoggerFactory.getLogger(WakeUpService.class);

    private static final String PING_URL = "https://exclusive-krista-luizcapel-78430027.koyeb.app/api/ping";
//    private static final String PING_URL = "http://localhost:8080/api/ping";

//    @Scheduled(fixedDelay = 5 * 60 * 1000) // a cada 5 minutos
    public void manterServidorAtivo() {
        try {
            LOG.info("Mantendo ativo");
            HttpURLConnection connection = (HttpURLConnection) new URL(PING_URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            int responseCode = connection.getResponseCode();
            System.out.println("Keep-alive ping: " + responseCode);
        } catch (Exception e) {
            System.err.println("Erro no keep-alive: " + e.getMessage());
        }
    }
}
