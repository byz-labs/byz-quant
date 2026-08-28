package com.byzquant.fed.client.impl;

import com.byzquant.fed.client.EconomicDataClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@Slf4j
public class FredRestClient implements EconomicDataClient {

    private final RestClient restClient;
    private final String apiKey;
    private final String baseUrl;

    public FredRestClient(@Value("${fed.api.base-url}") String baseUrl,
            @Value("${fed.api.key}") String apiKey) {
        this.baseUrl = baseUrl;
        this.apiKey = apiKey;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(10000);
        requestFactory.setReadTimeout(10000);

        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(requestFactory)
                .defaultHeader("User-Agent",
                        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .defaultHeader("Accept", "application/json")
                .defaultHeader("Connection", "keep-alive")
                .build();
    }

    @Override
    public <T> T fetch(String endpoint, Class<T> responseType, Object... uriVariables) {
        String cleanPath = endpoint.contains("?") ? endpoint.split("\\?")[0] : endpoint;
        UriComponentsBuilder builder = UriComponentsBuilder.fromUriString(this.baseUrl + cleanPath);

        if (uriVariables != null && uriVariables.length > 0) {
            // 🚀 KESİN TOKAT: Havada array referansı aramıyoruz! 0. indeksteki gerçek
            // gösterge stringini söküp alıyoruz!
            String indicatorId = uriVariables[0].toString().trim().toUpperCase();
            builder.queryParam("series_id", indicatorId);

            if (uriVariables.length > 1) {
                builder.queryParam("observation_start", uriVariables[1].toString().trim());
            }

            // Enflasyonların yanına o kutsal yıllık yüzde değişim parametresini (pc1) nokta
            // atışı ekle şef!
            if ("CPILFESL".equals(indicatorId) || "PCEPILFE".equals(indicatorId)) {
                builder.queryParam("units", "pc1");
            }
        }

        String finalUrl = builder.queryParam("api_key", this.apiKey)
                .queryParam("file_type", "json")
                .build()
                .toUriString();

        log.info("📡 [FRED LIVE REQUEST] FRED Sunucularına Çakılan Nihai Temiz URL: {}", finalUrl);

        return this.restClient.get()
                .uri(finalUrl)
                .retrieve()
                .body(responseType);
    }
}
