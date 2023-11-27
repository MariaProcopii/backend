package com.training.license.sharing.services;

import com.training.license.sharing.dto.CostViewDTO;
import com.training.license.sharing.entities.CostView;
import com.training.license.sharing.repositories.CostViewRepository;
import com.training.license.sharing.repositories.MonthlyCostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CostServiceTest {

    @Mock
    private CostViewRepository costViewRepository;

    @Mock
    private MonthlyCostRepository monthlyCostRepository;

    @InjectMocks
    private CostService service;

    @Test
    void getCosts_WhenSuccessful_ShouldReturnTotalCosts2022AndDelta() {
        CostView entity = createMockEntity();
        when(costViewRepository.findAll()).thenReturn(Collections.singletonList(entity));
        when(monthlyCostRepository.findAll()).thenReturn(Collections.emptyList());

        CostViewDTO result = service.getCosts();

        assertThat(result).isNotNull();
        assertThat(result.getTotalCosts2022()).isEqualTo(entity.getTotalCosts2022());
        assertThat(result.getDeltaTotalCosts2022()).isEqualTo(entity.getDeltaTotalCosts2022());
    }


    private CostView createMockEntity() {
        return CostView.builder()
                .id(UUID.randomUUID())
                .totalCosts2022(4200)
                .deltaTotalCosts2022(1900)
                .build();
    }

    @Test
    void getCosts_WhenNoData_ShouldThrowException() {
        when(costViewRepository.findAll()).thenReturn(Collections.emptyList());

        assertThatThrownBy(() -> service.getCosts()).isInstanceOf(RuntimeException.class);
    }


    @Test
    void getCosts_WhenMultipleRecords_ShouldReturnFirstRecord() {
        CostView entity1 = createMockEntity();
        CostView entity2 = createMockEntity();
        when(costViewRepository.findAll()).thenReturn(List.of(entity1, entity2));
        when(monthlyCostRepository.findAll()).thenReturn(Collections.emptyList());

        CostViewDTO result = service.getCosts();

        assertThat(result).isNotNull();
        assertThat(result.getTotalCosts2022()).isEqualTo(entity1.getTotalCosts2022());
        assertThat(result.getDeltaTotalCosts2022()).isEqualTo(entity1.getDeltaTotalCosts2022());
    }


    @Test
    void getCosts_WhenFieldsAreNull_ShouldReturnObjectWithNullTotalCosts2022() {
        CostView entity = createMockEntityWithNullFields();
        when(costViewRepository.findAll()).thenReturn(Collections.singletonList(entity));
        when(monthlyCostRepository.findAll()).thenReturn(Collections.emptyList());

        CostViewDTO result = service.getCosts();

        assertThat(result).isNotNull();
        assertThat(result.getTotalCosts2022()).isNull();
    }


    private CostView createMockEntityWithNullFields() {
        CostView entity = new CostView();
        entity.setId(UUID.randomUUID());
        return entity;
    }
}
