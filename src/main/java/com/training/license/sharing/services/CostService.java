package com.training.license.sharing.services;

import com.training.license.sharing.dto.CostViewDTO;
import com.training.license.sharing.entities.CostView;
import com.training.license.sharing.repositories.CostViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CostService {

    private final CostViewRepository repository;

    public List<CostViewDTO> getCosts() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private CostViewDTO convertToDTO(CostView entity) {
        return CostViewDTO.builder()
                .totalCosts2022(entity.getTotalCosts2022())
                .deltaTotalCosts2022(entity.getDeltaTotalCosts2022())
                .software(entity.getSoftware())
                .deltaSoftware(entity.getDeltaSoftware())
                .trainings(entity.getTrainings())
                .deltaTrainings(entity.getDeltaTrainings())
                .build();
    }
}
