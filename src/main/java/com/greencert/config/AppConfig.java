package com.greencert.config;

import com.greencert.ai.AiServiceManager;
import com.greencert.ai.IsoConsultantAgent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public IsoConsultantAgent isoConsultantAgent() {
        // Obtenemos el agente desde el manager, que ahora es más robusto
        return AiServiceManager.getAgent();
    }
}
