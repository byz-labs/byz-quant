package com.byzquant.fed.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.JdkClientHttpConnector;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;
import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class FedWebClientConfig {

    @Bean
    public WebClient fedWebClient(@Value("${fed.api.base-url}") String baseUrl) {
        // 1. HTTP/1.1 Kararlı Bağlantı Motoru
        HttpClient javaHttpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .connectTimeout(Duration.ofSeconds(2))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();

        // 2. KESİN ÇÖZÜM: 256 KB sınırını yıkıp hafıza havuzunu 10 MB seviyesine fırlatıyoruz!
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
                .build();

        return WebClient.builder()
                .baseUrl(baseUrl)
                .clientConnector(new JdkClientHttpConnector(javaHttpClient))
                .exchangeStrategies(exchangeStrategies) // Yeni stratejiyi enjekte ettik
                .build();
    }
}
