package com.training.license.sharing.services;

import com.training.license.sharing.dto.AverageUserCostViewDTO;
import com.training.license.sharing.entities.AverageUserCostView;
import com.training.license.sharing.repositories.AverageUserCostViewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AverageUserCostService {
    private final AverageUserCostViewRepository repository;

    public List<AverageUserCostViewDTO> getAverageUserCosts() {
        return repository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AverageUserCostViewDTO convertToDTO(AverageUserCostView entity) {
        return AverageUserCostViewDTO.builder()
                .calculation(entity.getCalculation())
                .disciplineName(entity.getDisciplineName())
                .averageCostsUserDiscipline(entity.getAverageCostsUserDiscipline())
                .build();
    }
}