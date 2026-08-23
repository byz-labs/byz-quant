package com.byzquant.fed;

import com.byzquant.fed.adapters.web.FedWebAdapter;
import com.byzquant.fed.domain.FedCategory;
import com.byzquant.fed.domain.FedCategoryService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FedWebAdapter.class) // Sadece HTTP/Web katmanını ayağa kaldıran hafif test
class FedWebAdapterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean // Spring Boot 3.4+ ve 4.x için güncel Mockito entegrasyon anotasyonu
    private FedCategoryService fedCategoryService;

    @Test
    void shouldReturnChildrenCategoriesJson_WhenParentExists() throws Exception {
        // Given
        Long parentId = 0L;
        FedCategory child1 = new FedCategory(10L, "National Accounts", Optional.of(parentId));
        FedCategory child2 = new FedCategory(20L, "Interest Rates", Optional.of(parentId));

        Mockito.when(fedCategoryService.getChildrenCategories(parentId))
                .thenReturn(java.util.List.of(child1, child2));

        // When & Then (HTTP GET /categories/0/children çağrısı yapıp dizi kontrolü
        // sağlıyoruz)
        mockMvc.perform(get("/api/v1/fed/categories/" + parentId + "/children"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].name").value("National Accounts"))
                .andExpect(jsonPath("$[1].id").value(20L))
                .andExpect(jsonPath("$[1].name").value("Interest Rates"));
    }

    @Test
    void shouldReturnRelatedCategoriesJson_WhenCategoryExists() throws Exception {
        // Given
        Long categoryId = 125L;
        FedCategory related1 = new FedCategory(10L, "National Accounts", java.util.Optional.empty());

        org.mockito.Mockito.when(fedCategoryService.getRelatedCategories(categoryId))
                .thenReturn(java.util.List.of(related1));

        // When & Then (HTTP GET /categories/{id}/related çağrısını denetliyoruz)
        mockMvc.perform(get("/api/v1/fed/categories/" + categoryId + "/related"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(10L))
                .andExpect(jsonPath("$[0].name").value("National Accounts"));
    }

}
