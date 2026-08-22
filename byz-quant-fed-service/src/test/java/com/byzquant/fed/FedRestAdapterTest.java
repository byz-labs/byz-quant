package com.byzquant.fed;

import com.byzquant.fed.adapters.rest.FedRestAdapter;
import com.byzquant.fed.adapters.rest.FedCategoryResponse;
import com.byzquant.fed.domain.FedCategory;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import static org.assertj.core.api.Assertions.assertThat;

class FedRestAdapterTest {

        @Test
        void shouldFetchCategoryFromFedApi_WithMockedWebClient() {
                // Given
                WebClient webClientMock = Mockito.mock(WebClient.class);
                WebClient.RequestHeadersUriSpec<?> requestHeadersUriSpecMock = Mockito
                                .mock(WebClient.RequestHeadersUriSpec.class);
                WebClient.RequestHeadersSpec<?> requestHeadersSpecMock = Mockito
                                .mock(WebClient.RequestHeadersSpec.class);
                WebClient.ResponseSpec responseSpecMock = Mockito.mock(WebClient.ResponseSpec.class);

                // TDD Kuralı: Gerçek FED API'den gelen dizi sarmalını test ortamında simüle
                // ediyoruz
                FedCategoryResponse.CategoryDto mockDto = new FedCategoryResponse.CategoryDto(125L, "Trade Balance",
                                13L);
                FedCategoryResponse mockResponse = new FedCategoryResponse(List.of(mockDto));

                Mockito.doReturn(requestHeadersUriSpecMock).when(webClientMock).get();

                // Derleyicinin jenerik Class eşleşme hatasını kökten çözen temiz Mockito tanımı
                Mockito.doReturn(requestHeadersSpecMock)
                                .when(requestHeadersUriSpecMock)
                                .uri(Mockito.any(Function.class));

                Mockito.doReturn(responseSpecMock).when(requestHeadersSpecMock).retrieve();

                Mockito.when(responseSpecMock.bodyToMono(FedCategoryResponse.class))
                                .thenReturn(Mono.just(mockResponse));

                FedRestAdapter adapter = new FedRestAdapter(webClientMock, "mock_api_key");

                // When
                Optional<FedCategory> result = adapter.fetchCategoryById(125L);

                // Then
                assertThat(result).isPresent();
                assertThat(result.get().name()).isEqualTo("Trade Balance");
                assertThat(result.get().parentId()).contains(13L);
        }
}
