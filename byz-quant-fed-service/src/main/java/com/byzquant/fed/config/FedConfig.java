package com.byzquant.fed.config;

import com.byzquant.fed.adapters.rest.FedRestAdapter;
import com.byzquant.fed.domain.FedCategoryService;
import com.byzquant.fed.ports.FedDataPort;
import com.byzquant.fed.ports.FedPersistencePort;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class FedConfig {

    @Value("${fed.api.base-url}")
    private String baseUrl;

    @Value("${fed.api.key}")
    private String apiKey;

    @Bean
    public WebClient fedWebClient() {
        // FED sunucularının HTTP/2 protokolündeki RST_STREAM kısıtlamalarını aşmak için
        // akışı Postman gibi güvenli HTTP/1.1 seviyesine sabitleyen kararlı motor
        HttpClient javaHttpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(2))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new JdkClientHttpConnector(javaHttpClient))
                .build();
    }

    @Bean
    public FedDataPort fedDataPort(WebClient fedWebClient) {
        return new FedRestAdapter(fedWebClient, apiKey);
    }

    @Bean
    public FedCategoryService fedCategoryService(
            FedDataPort fedDataPort,
            FedPersistencePort fedPersistencePort) { // Veritabanı portu enjekte edildi
        // Servisimize hem internet adaptörünü hem de veritabanı adaptörünü lego gibi taktık
        return new FedCategoryService(fedDataPort, fedPersistencePort);
    }
}
