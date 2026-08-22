package com.byzquant.fed;

import com.byzquant.fed.adapters.persistence.FedPersistenceAdapter;
import com.byzquant.fed.adapters.persistence.FedCategoryEntity; // Entity import edildi
import com.byzquant.fed.adapters.persistence.FedCategoryRepository; // Repository import edildi
import com.byzquant.fed.domain.FedCategory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import(FedPersistenceAdapter.class)
class FedPersistenceAdapterTest {

    @Autowired
    private FedPersistenceAdapter fedPersistenceAdapter;

    @Autowired
    private FedCategoryRepository fedCategoryRepository; // Ham repository'yi doğrudan enjekte ettik

    @Test
    void shouldSaveAndFetchCategory_WithFunctionalFlow() {
        // Given
        FedCategory domainCategory = new FedCategory(125L, "Macroeconomics", Optional.empty());

        // When
        fedPersistenceAdapter.save(domainCategory);
        
        // H2 İçindeki Veriyi Gözümüzle Görmek İçin Araya Giriyoruz
        System.out.println("==================================================");
        System.out.println("🔍 BYZ QUANT H2 VERİTABANI KONTROLÜ BAŞLADI");
        
        fedCategoryRepository.findById(125L).ifPresentOrElse(
            entity -> {
                System.out.println("✅ VERİ TABANINDA DATA BULUNDU!");
                System.out.println("➔ ID: " + entity.getId());
                System.out.println("➔ NAME: " + entity.getName());
                System.out.println("➔ PARENT ID: " + entity.getParentId());
            },
            () -> System.out.println("❌ HATA: Veri tabanında kayıt bulunamadı!")
        );
        
        System.out.println("==================================================");

        Optional<FedCategory> result = fedPersistenceAdapter.fetchCategoryById(125L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.map(FedCategory::name).orElse("")).isEqualTo("Macroeconomics");
    }
}
