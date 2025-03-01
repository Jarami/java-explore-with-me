package ru.practicum.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.StatClient;

import java.time.Duration;

@Configuration
public class StatClientConfig {

    @Bean
    public StatClient getStatClient(@Value("${stat.url}") String statServerUrl,
                                    @Value("${stat.readTimeout}") Long readTimeout,
                                    @Value("${stat.connectTimeout}") Long connectTimeout) {

        return new StatClient(statServerUrl, Duration.ofMillis(connectTimeout), Duration.ofMillis(readTimeout));
    }
}
