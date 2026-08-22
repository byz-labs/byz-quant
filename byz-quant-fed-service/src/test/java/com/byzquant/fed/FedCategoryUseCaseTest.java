package com.byzquant.fed;

import com.byzquant.fed.domain.FedCategory;
import com.byzquant.fed.domain.FedCategoryService;
import com.byzquant.fed.ports.FedDataPort;
import com.byzquant.fed.ports.FedPersistencePort;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

class FedCategoryUseCaseTest {

    private final FedDataPort fedDataPort = Mockito.mock(FedDataPort.class);
    private final FedPersistencePort fedPersistencePort = Mockito.mock(FedPersistencePort.class); // Yeni eklenen mock
    // Güncellenen constructor enjeksiyonu
    private final FedCategoryService fedCategoryService = new FedCategoryService(fedDataPort, fedPersistencePort);

    @Test
    void shouldReturnCategory_WhenPortReturnsData() {
        // Given
        Long categoryId = 125L;
        FedCategory mockCategory = new FedCategory(categoryId, "Macroeconomics", Optional.empty());

        // KESİN ÇÖZÜM: 'when' metodu jenerik dönüş tipini (Optional<FedCategory>) tam
        // olarak korur.
        // Derleyici nesnenin tipinden %100 emin olduğu için 'unchecked conversion'
        // endişesi tamamen biter.
        Mockito.when(fedDataPort.fetchCategoryById(categoryId))
                .thenReturn(Optional.of(mockCategory));

        // When
        Optional<FedCategory> result = fedCategoryService.getCategory(categoryId);

        // Then
        assertThat(result).isPresent();

        // Tam istediğiniz gibi: Sıfır lambda, sıfır baskılama, saf metot referansı!
        String actualName = result
                .map(FedCategory::name)
                .orElse("");

        assertThat(actualName).isEqualTo("Macroeconomics");
    }
}
