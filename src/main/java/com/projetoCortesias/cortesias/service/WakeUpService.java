package com.projetoCortesias.cortesias.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@EnableScheduling
public class WakeUpService {

    private static final Logger LOG = LoggerFactory.getLogger(WakeUpService.class);

    @Scheduled(cron = "0 0 * * * *")
    public void manterAtivo() {
        LOG.info("Mantendo ativo");
    }
}
