package com.byzquant.fed;

import com.byzquant.fed.domain.FedCategory;
import org.junit.jupiter.api.Test;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FedCategoryTest {

    @Test
    void shouldCreateValidFedCategory_WithRecord() {
        // Given & When
        Long categoryId = 125L;
        String categoryName = "Macroeconomics";
        FedCategory category = new FedCategory(categoryId, categoryName, Optional.empty());

        // Then
        assertThat(category.id()).isEqualTo(categoryId);
        assertThat(category.name()).isEqualTo(categoryName);
        assertThat(category.parentId()).isEmpty();
    }

    @Test
    void shouldThrowException_WhenNameIsBlank_UsingValidation() {
        assertThatThrownBy(() -> new FedCategory(125L, "", Optional.empty()))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Category ID and Name cannot be null or empty");
    }
}
