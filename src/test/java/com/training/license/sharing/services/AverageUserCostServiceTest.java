package com.training.license.sharing.services;

import com.training.license.sharing.dto.AverageUserCostViewDTO;
import com.training.license.sharing.entities.AverageUserCostView;
import com.training.license.sharing.repositories.AverageUserCostViewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AverageUserCostServiceTest {

    @Mock
    private AverageUserCostViewRepository repository;

    @InjectMocks
    private AverageUserCostService service;

    @Test
    void getAverageUserCosts_WhenSuccessful_ShouldReturnCalculationData() {
        AverageUserCostView entity = createMockEntity();
        when(repository.findAll()).thenReturn(Collections.singletonList(entity));
        List<AverageUserCostViewDTO> result = service.getAverageUserCosts();
        assertThat(result).isNotEmpty();
        assertThat(entity.getCalculation()).isEqualTo(result.get(0).getCalculation());
    }

    @Test
    void getAverageUserCosts_WhenNoData_ShouldReturnEmptyList() {
        when(repository.findAll()).thenReturn(Collections.emptyList());
        List<AverageUserCostViewDTO> result = service.getAverageUserCosts();
        assertThat(result).isEmpty();
    }

    @Test
    void getAverageUserCosts_WithMultipleRecords () {
        AverageUserCostView entity1 = createMockEntity();
        AverageUserCostView entity2 = createMockEntity();
        when(repository.findAll()).thenReturn(List.of(entity1, entity2));
        List<AverageUserCostViewDTO> result = service.getAverageUserCosts();
        assertThat(result).hasSize(2);
    }

    private AverageUserCostView createMockEntity() {
        return AverageUserCostView.builder()
                .id(UUID.randomUUID())
                .calculation(100)
                .disciplineName("Development")
                .averageCostsUserDiscipline(2000)
                .build();
    }
}
